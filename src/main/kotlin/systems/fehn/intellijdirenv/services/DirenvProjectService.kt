package systems.fehn.intellijdirenv.services

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.vfs.VirtualFile
import systems.fehn.intellijdirenv.MyBundle
import systems.fehn.intellijdirenv.notificationGroup
import systems.fehn.intellijdirenv.settings.DirenvSettingsState
import systems.fehn.intellijdirenv.switchNull

class DirenvProjectService(private val project: Project) {
    private val logger by lazy { logger<DirenvProjectService>() }

    private val projectDir = project.guessProjectDir()
        .switchNull(
            onNull = { logger.warn("Could not determine project dir of project ${project.name}") },
        )

    val projectEnvrcFile: VirtualFile?
        get() = projectDir?.findChild(".envrc")?.takeUnless { it.isDirectory }
            .switchNull(
                onNull = { logger.trace { "Project ${project.name} contains no .envrc file" } },
                onNonNull = { logger.trace { "Project ${project.name} has .envrc file ${it.path}" } },
            )

    private val envService by lazy { ApplicationManager.getApplication().getService(EnvironmentService::class.java) }

    private val jsonFactory by lazy { JsonFactory() }

    fun importDirenv(envrcFile: VirtualFile) {
        val process = executeDirenv(envrcFile, "export", "json")

        if (process.waitFor() != 0) {
            handleDirenvError(process, envrcFile)
            return
        }

        val notification = jsonFactory.createParser(process.inputStream).use { parser ->

            try {
                val didWork = handleDirenvOutput(parser)

                if (didWork) {
                    notificationGroup
                        .createNotification(
                            MyBundle.message("executedSuccessfully"),
                            "",
                            NotificationType.INFORMATION,
                        )
                } else {
                    notificationGroup
                        .createNotification(
                            MyBundle.message("alreadyUpToDate"),
                            "",
                            NotificationType.INFORMATION,
                        )
                }
            } catch (e: EnvironmentService.ManipulateEnvironmentException) {
                notificationGroup
                    .createNotification(
                        MyBundle.message("exceptionNotification"),
                        e.localizedMessage,
                        NotificationType.ERROR,
                    )
            }
        }

        notification.notify(project)
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

    private fun handleDirenvError(process: Process, envrcFile: VirtualFile) {
        val error = process.errorStream.bufferedReader().readText()

        val notification = if (error.contains(" is blocked")) {
            notificationGroup
                .createNotification(
                    MyBundle.message("envrcNotYetAllowed"),
                    "",
                    NotificationType.WARNING,
                )
                .addAction(
                    NotificationAction.create(MyBundle.message("allow")) { _, notification ->
                        notification.hideBalloon()
                        executeDirenv(envrcFile, "allow").waitFor()

                        importDirenv(envrcFile)
                    },
                )
        } else {
            logger.error(error)

            notificationGroup
                .createNotification(
                    MyBundle.message("errorDuringDirenv"),
                    "",
                    NotificationType.ERROR,
                )
        }

        notification
            .addAction(
                NotificationAction.create(MyBundle.message("openEnvrc")) { _, it ->
                    it.hideBalloon()

                    FileEditorManager.getInstance(project).openFile(envrcFile, true, true)
                },
            )
            .notify(project)
    }

    private fun executeDirenv(envrcFile: VirtualFile, vararg args: String): Process {
        val workingDir = envrcFile.parent.path

        val cli = GeneralCommandLine("direnv", *args)
            .withWorkDirectory(workingDir)

        val appSettings = DirenvSettingsState.getInstance()
        if (appSettings.direnvSettingsPath.isNotEmpty()) {
            cli.withExePath(appSettings.direnvSettingsPath)
        }

        return cli.createProcess()
    }
}
