package systems.fehn.intellijdirenv.services

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import systems.fehn.intellijdirenv.MyBundle
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

    private val direnvService by lazy { service<DirenvService>() }
    private val envService by lazy { service<EnvironmentService>() }

    fun hasEnvrcFile() = envrcFile != null

    fun importDirenv() {
        val process = executeDirenv("export", "json") ?: return

        val obj = JsonParser().parse(process.inputStream.bufferedReader()) as JsonObject
        try {
            for (name in obj.keySet()) {
                when (val value = obj[name]) {
                    JsonNull.INSTANCE -> envService.unsetVariable(name)
                    else -> envService.setVariable(name, value.asString)
                }

                logger.trace { "Set variable $name to ${obj[name]}" }
            }
        } catch (e: EnvironmentService.ManipulateEnvironmentException) {
            val notification = direnvService.notificationGroup
                .createNotification(
                    MyBundle.message("exceptionNotification"),
                    e.localizedMessage,
                    NotificationType.ERROR,
                    null,
                )

            Notifications.Bus.notify(notification, project)
        }

        if (process.waitFor() != 0) {
            handleDirenvError(process)
        } else {
            val notification = direnvService.notificationGroup
                .createNotification(
                    MyBundle.message("executedSuccessfully"),
                    NotificationType.INFORMATION,
                )

            Notifications.Bus.notify(notification, project)
        }
    }

    private fun handleDirenvError(process: Process) {
        val error = process.errorStream.bufferedReader().readText()

        val notification = if (error.contains(" is blocked")) {
            direnvService.notificationGroup
                .createNotification(
                    MyBundle.message("envrcNotYetAllowed"),
                    NotificationType.WARNING,
                )
                .addAction(
                    NotificationAction.create(MyBundle.message("allow")) { _, notification ->
                        notification.hideBalloon()
                        (executeDirenv("allow") ?: return@create).waitFor()

                        importDirenv()
                    },
                )
        } else {
            logger.error(error)

            direnvService.notificationGroup
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

    private fun executeDirenv(vararg args: String): Process? {
        if (!hasEnvrcFile() || workingDir == null) {
            return null
        }

        return direnvService.direnvExecutable?.let { executable ->
            ProcessBuilder(executable.toString(), *args)
                .directory(workingDir)
                .start()
        }
    }
}
