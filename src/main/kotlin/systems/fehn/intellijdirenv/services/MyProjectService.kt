package systems.fehn.intellijdirenv.services

import com.intellij.openapi.project.Project
import systems.fehn.intellijdirenv.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
