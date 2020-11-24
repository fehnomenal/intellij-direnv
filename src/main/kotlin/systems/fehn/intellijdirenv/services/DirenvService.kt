package systems.fehn.intellijdirenv.services

import com.intellij.notification.NotificationGroup
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.diagnostic.trace
import systems.fehn.intellijdirenv.switchNull
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class DirenvService {
    private val logger by lazy { logger<DirenvService>() }

    val direnvExecutable: Path?
        get() = System.getenv("PATH")
            .split(':')
            .asSequence()
            .map { Paths.get(it, "direnv") }
            .firstOrNull { Files.exists(it) && Files.isExecutable(it) }
            .switchNull(
                onNull = { logger.trace { "Did not find direnv executable in path" } },
                onNonNull = { logger.trace { "Found direnv executable in $it" } },
            )

    val notificationGroup = NotificationGroup.balloonGroup("Direnv")
}
