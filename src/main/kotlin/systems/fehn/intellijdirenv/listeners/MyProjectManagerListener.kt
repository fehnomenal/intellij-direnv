package systems.fehn.intellijdirenv.listeners

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import systems.fehn.intellijdirenv.MyBundle
import systems.fehn.intellijdirenv.services.DirenvProjectService
import systems.fehn.intellijdirenv.services.DirenvService

internal class MyProjectManagerListener : ProjectManagerListener {
    private val logger by lazy { logger<MyProjectManagerListener>() }
    private val direnvService by lazy { service<DirenvService>() }

    override fun projectOpened(project: Project) {
        logger.trace("Opened project ${project.name}")

        direnvService.direnvExecutable?.let { executable ->
            val projectService = project.service<DirenvProjectService>()

            if (projectService.hasEnvrcFile()) {
                val notification = direnvService.notificationGroup
                    .createNotification(
                        MyBundle.message("envrcFileFound"),
                        NotificationType.INFORMATION,
                    )
                    .addAction(
                        NotificationAction.create(MyBundle.message("importDirenvAction")) { _, notification ->
                            notification.hideBalloon()

                            projectService.importDirenv(executable)
                        },
                    )

                Notifications.Bus.notify(notification, project)
            }
        }
    }
}
