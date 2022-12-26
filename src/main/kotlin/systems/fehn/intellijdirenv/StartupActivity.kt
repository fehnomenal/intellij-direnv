package systems.fehn.intellijdirenv

import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import systems.fehn.intellijdirenv.services.DirenvProjectService

class MyStartupActivity : StartupActivity, DumbAware {
  private val logger by lazy { logger<MyStartupActivity>() }

  override fun runActivity(project: Project) {
      logger.trace("Opened project ${project.name}")

      val projectService = project.getService(DirenvProjectService::class.java)

      projectService.projectEnvrcFile?.let {
          val notification = notificationGroup
              .createNotification(
                  MyBundle.message("envrcFileFound"),
                  "",
                  NotificationType.INFORMATION,
              )
              .addAction(
                  NotificationAction.create(MyBundle.message("importDirenvAction")) { _, notification ->
                      notification.hideBalloon()

                      projectService.importDirenv(it)
                  },
              )

          notification.notify(project)
      }
  }
}
