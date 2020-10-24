package com.github.fehnomenal.intellijdirenv.services

import com.intellij.openapi.project.Project
import com.github.fehnomenal.intellijdirenv.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
