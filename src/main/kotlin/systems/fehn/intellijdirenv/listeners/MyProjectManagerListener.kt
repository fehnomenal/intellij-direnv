package systems.fehn.intellijdirenv.listeners

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.service
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

        val projectService = project.service<DirenvProjectService>()

        if (projectService.hasEnvrcFile()) {
            val notification = notificationGroup
                .createNotification(
                    MyBundle.message("envrcFileFound"),
                    NotificationType.INFORMATION,
                )
                .addAction(
                    NotificationAction.create(MyBundle.message("importDirenvAction")) { _, notification ->
                        notification.hideBalloon()

                        projectService.importDirenv()
                    },
                )

            Notifications.Bus.notify(notification, project)
        }
    }
}
