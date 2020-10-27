package systems.fehn.intellijdirenv

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.guessProjectDir
import systems.fehn.intellijdirenv.services.DirenvService

class DirenvAction : AnAction("Import direnv") {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val projectDir = project.guessProjectDir() ?: return

        val service = service<DirenvService>()

        TODO("Has this project a `.envrc` file?")
        TODO("Is `direnv` in the path?")
    }
}
