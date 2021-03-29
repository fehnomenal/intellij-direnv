package systems.fehn.intellijdirenv

import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import systems.fehn.intellijdirenv.services.DirenvProjectService

class DirenvToolBarAction : AnAction(MyBundle.message("importDirenvAction")) {
    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = true
        e.presentation.isEnabled = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = project.getService(DirenvProjectService::class.java)
        if (service.hasTopLevelEnvrcFile()) {
            service.importDirenv(null)
        } else {
            val notification = notificationGroup
                .createNotification(
                    MyBundle.message("noTopLevelEnvrcFileFound"),
                    NotificationType.ERROR,
                )

            Notifications.Bus.notify(notification, project)
        }
    }
}
