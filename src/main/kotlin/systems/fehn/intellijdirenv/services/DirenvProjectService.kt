package systems.fehn.intellijdirenv.services

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
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

    private val envService by lazy { ApplicationManager.getApplication().getService(EnvironmentService::class.java) }

    private val jsonFactory by lazy { JsonFactory() }

    fun hasEnvrcFile() = envrcFile != null

    fun importDirenv() {
        val process = executeDirenv("export", "json")

        if (process.waitFor() != 0) {
            handleDirenvError(process)
            return
        }

        val notification = jsonFactory.createParser(process.inputStream).use { parser ->

            try {
                val didWork = handleDirenvOutput(parser)

                if (didWork) {
                    notificationGroup
                        .createNotification(
                            MyBundle.message("executedSuccessfully"),
                            NotificationType.INFORMATION,
                        )
                } else {
                    notificationGroup
                        .createNotification(
                            MyBundle.message("alreadyUpToDate"),
                            NotificationType.INFORMATION,
                        )
                }
            } catch (e: EnvironmentService.ManipulateEnvironmentException) {
                notificationGroup
                    .createNotification(
                        MyBundle.message("exceptionNotification"),
                        e.localizedMessage,
                        NotificationType.ERROR,
                        null,
                    )
            }
        }

        Notifications.Bus.notify(notification, project)
    }

    private fun handleDirenvOutput(parser: JsonParser): Boolean {
        var didWork = false

        while (parser.nextToken() != null) {
            if (parser.currentToken == JsonToken.FIELD_NAME) {
                when (parser.nextToken()) {
                    JsonToken.VALUE_NULL -> envService.unsetVariable(parser.currentName)
                    JsonToken.VALUE_STRING -> envService.setVariable(parser.currentName, parser.valueAsString)

                    else -> continue
                }

                didWork = true
                logger.trace { "Set variable ${parser.currentName} to ${parser.valueAsString}" }
            }
        }

        return didWork
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
