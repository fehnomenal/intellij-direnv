package systems.fehn.intellijdirenv

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import systems.fehn.intellijdirenv.services.DirenvProjectService

class DirenvAction : AnAction(MyBundle.message("importDirenvAction")) {
    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        val service = project.service<DirenvProjectService>()
        e.presentation.isEnabledAndVisible = service.hasEnvrcFile()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = project.service<DirenvProjectService>()
        service.importDirenv()
    }
}
