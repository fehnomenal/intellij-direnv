package systems.fehn.intellijdirenv.listeners

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import systems.fehn.intellijdirenv.MyBundle
import systems.fehn.intellijdirenv.notificationGroup
import systems.fehn.intellijdirenv.services.DirenvProjectService

internal class MyProjectManagerListener : ProjectManagerListener {
    private val logger by lazy { logger<MyProjectManagerListener>() }

    override fun projectOpened(project: Project) {
        logger.trace("Opened project ${project.name}")

        val projectService = project.getService(DirenvProjectService::class.java)

        projectService.projectEnvrcFile?.let {
            val notification = notificationGroup
                .createNotification(
                    MyBundle.message("envrcFileFound"),
                    NotificationType.INFORMATION,
                )
                .addAction(
                    NotificationAction.create(MyBundle.message("importDirenvAction")) { _, notification ->
                        notification.hideBalloon()

                        projectService.importDirenv(it)
                    },
                )

            Notifications.Bus.notify(notification, project)
        }
    }
}
