package systems.fehn.intellijdirenv

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import systems.fehn.intellijdirenv.services.DirenvProjectService
import systems.fehn.intellijdirenv.services.DirenvService
import java.nio.file.Path

class DirenvAction : AnAction(MyBundle.message("importDirenvAction")) {
    private val service by lazy { service<DirenvService>() }
    private lateinit var direnvExecutable: Path

    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isEnabledAndVisible = false
            return
        }

        val executable = service.direnvExecutable
        if (executable != null) {
            direnvExecutable = executable

            val service = project.service<DirenvProjectService>()
            e.presentation.isEnabledAndVisible = service.hasEnvrcFile()
        } else {
            e.presentation.isEnabledAndVisible = false
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val service = project.service<DirenvProjectService>()
        service.importDirenv(direnvExecutable)
    }
}
