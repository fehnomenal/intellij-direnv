package systems.fehn.intellijdirenv.services

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.debug
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import systems.fehn.intellijdirenv.MyBundle
import systems.fehn.intellijdirenv.notificationGroup
import systems.fehn.intellijdirenv.switchNull
import java.io.File

class DirenvProjectService(private val project: Project) {
    private val logger by lazy { logger<DirenvProjectService>() }

    private val projectDir = project.guessProjectDir()
        .switchNull(
            onNull = { logger.warn("Could not determine project dir of project ${project.name}") },
        )
    private val workingDir by lazy { projectDir?.let { File(it.path) } }
    private val envrcFile: VirtualFile?
        get() = projectDir?.findChild(".envrc")?.takeUnless { it.isDirectory }
            .switchNull(
                onNull = { logger.trace { "Project ${project.name} contains no .envrc file" } },
                onNonNull = { logger.trace { "Project ${project.name} has .envrc file ${it.path}" } },
            )

    private val envService by lazy { service<EnvironmentService>() }

    fun hasEnvrcFile() = envrcFile != null

    fun importDirenv() {
        val process = executeDirenv("export", "json")

        if (process.waitFor() != 0) {
            handleDirenvError(process)
            return
        }

        val jsonElement = JsonParser().parse(process.inputStream.bufferedReader())
        if (jsonElement == JsonNull.INSTANCE) {
            val notification = notificationGroup
                .createNotification(
                    MyBundle.message("alreadyUpToDate"),
                    NotificationType.INFORMATION,
                )

            Notifications.Bus.notify(notification, project)
            return
        }

        logger.debug { process.errorStream.bufferedReader().readText() }

        val obj = if (jsonElement !is JsonObject) {
            logger.debug { "Parsed JSON was neither null nor an object: $jsonElement" }
            JsonObject()
        } else {
            jsonElement
        }

        val notification = try {
            for (name in obj.keySet()) {
                when (val value = obj[name]) {
                    JsonNull.INSTANCE -> envService.unsetVariable(name)
                    else -> envService.setVariable(name, value.asString)
                }

                logger.trace { "Set variable $name to ${obj[name]}" }
            }

            notificationGroup
                .createNotification(
                    MyBundle.message("executedSuccessfully"),
                    NotificationType.INFORMATION,
                )
        } catch (e: EnvironmentService.ManipulateEnvironmentException) {
            notificationGroup
                .createNotification(
                    MyBundle.message("exceptionNotification"),
                    e.localizedMessage,
                    NotificationType.ERROR,
                    null,
                )
        }

        Notifications.Bus.notify(notification, project)
    }

    private fun handleDirenvError(process: Process) {
        val error = process.errorStream.bufferedReader().readText()

        val notification = if (error.contains(" is blocked")) {
            notificationGroup
                .createNotification(
                    MyBundle.message("envrcNotYetAllowed"),
                    NotificationType.WARNING,
                )
                .addAction(
                    NotificationAction.create(MyBundle.message("allow")) { _, notification ->
                        notification.hideBalloon()
                        executeDirenv("allow").waitFor()

                        importDirenv()
                    },
                )
        } else {
            logger.error(error)

            notificationGroup
                .createNotification(
                    MyBundle.message("errorDuringDirenv"),
                    NotificationType.ERROR,
                )
        }

        Notifications.Bus.notify(
            notification
                .addAction(
                    NotificationAction.create(MyBundle.message("openEnvrc")) { _, it ->
                        it.hideBalloon()

                        envrcFile?.let {
                            FileEditorManager.getInstance(project).openFile(it, true, true)
                        }
                    },
                ),
            project,
        )
    }

    private fun executeDirenv(vararg args: String): Process {
        return GeneralCommandLine("direnv", *args)
            .withWorkDirectory(workingDir)
            .createProcess()
    }
}
