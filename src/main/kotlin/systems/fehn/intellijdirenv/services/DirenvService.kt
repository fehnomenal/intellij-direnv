package systems.fehn.intellijdirenv.services

import com.intellij.notification.NotificationGroup
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DirenvService {
    val direnvExecutable: Path?
        get() = System.getenv("PATH")
            .split(':')
            .asSequence()
            .map { Paths.get(it, "direnv") }
            .firstOrNull { Files.exists(it) && Files.isExecutable(it) }

    val notificationGroup = NotificationGroup.balloonGroup("Direnv")
}
