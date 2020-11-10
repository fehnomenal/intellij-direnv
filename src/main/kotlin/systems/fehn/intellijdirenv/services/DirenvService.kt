package systems.fehn.intellijdirenv.services

import com.intellij.notification.NotificationGroup
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DirenvService {
    private var _direnvExecutable: Path? = null
    var resolveDirenvExecutable = true

    val direnvExecutable: Path?
        get() {
            if (resolveDirenvExecutable) {
                _direnvExecutable = resolveDirenvExecutable()
                resolveDirenvExecutable = false
            }
            return _direnvExecutable
        }

    private fun resolveDirenvExecutable(): Path? =
        System.getenv("PATH")
            .split(':')
            .asSequence()
            .map { Paths.get(it, "direnv") }
            .firstOrNull { Files.exists(it) && Files.isExecutable(it) }

    val notificationGroup = NotificationGroup.balloonGroup("Direnv")
}
