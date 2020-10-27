package systems.fehn.intellijdirenv.services

import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import systems.fehn.intellijdirenv.MyBundle
import java.io.File

class DirenvProjectService(private val project: Project) {
    private val projectDir = project.guessProjectDir()
    private val workingDir = projectDir?.let { File(it.path) }
    private val envrcFile = projectDir?.findChild(".envrc")?.takeUnless { it.isDirectory }

    private val direnvService = service<DirenvService>()


    fun importDirenv() {
        val process = executeDirenv("export", "json") ?: return

        val obj = JsonParser().parse(process.inputStream.bufferedReader()) as JsonObject
        for (name in obj.keySet()) {
            val value = obj[name]
            when {
                value == JsonNull.INSTANCE -> println("- $name")
                System.getenv(name) == null -> println("+ $name (${value.asString})")
                else -> println("~ $name (${value.asString})")
            }
        }

        if (process.waitFor() != 0) {
            return handleDirenvError(process)
        }
    }

    private fun handleDirenvError(process: Process) {
        val not =
            if (process.errorStream.bufferedReader().readText().contains(" is blocked")) {
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
                        }
                    )
            } else {
                direnvService.notificationGroup
                    .createNotification(
                        MyBundle.message("errorDuringDirenv"),
                        NotificationType.ERROR,
                    )
            }

        Notifications.Bus.notify(
            not
                .addAction(
                    NotificationAction.create(MyBundle.message("openEnvrc")) { _, notification ->
                        notification.hideBalloon()

                        FileEditorManager.getInstance(project).openFile(envrcFile!!, true, true)
                    }
                ),
            project,
        )
    }


    private fun executeDirenv(vararg args: String): Process? {
        envrcFile ?: return null

        val command = listOf(
            direnvService.direnvExecutable?.toString() ?: return null,
            *args,
        )
        return ProcessBuilder(command)
            .directory(workingDir ?: return null)
            .start()
    }
}
