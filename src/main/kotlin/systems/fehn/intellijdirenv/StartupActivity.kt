package systems.fehn.intellijdirenv

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import systems.fehn.intellijdirenv.services.DirenvProjectService
import systems.fehn.intellijdirenv.settings.DirenvSettingsState

class MyStartupActivity : StartupActivity, DumbAware {
    private val logger by lazy { logger<MyStartupActivity>() }

    override fun runActivity(project: Project) {
        logger.trace("Opened project ${project.name}")

        val projectService = project.getService(DirenvProjectService::class.java)
        val appSettings = DirenvSettingsState.getInstance()

        projectService.projectEnvrcFile?.let {
            if (appSettings.direnvSettingsImportOnStartup) {
                projectService.importDirenv(it)
            } else {
                notify(project, projectService, it)
            }
        }
    }

    private fun notify(project: Project, projectService: DirenvProjectService, it: VirtualFile) {
        val notification = notificationGroup
            .createNotification(
                MyBundle.message("envrcFileFound"),
                it.path,
                NotificationType.INFORMATION,
            )
            .addAction(
                NotificationAction.create(MyBundle.message("importDirenvStartupMessage")) { _, notification ->
                    notification.hideBalloon()

                    projectService.importDirenv(it)
                },
            )

        notification.notify(project)
    }
}
