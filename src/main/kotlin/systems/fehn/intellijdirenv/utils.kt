package systems.fehn.intellijdirenv

import com.intellij.notification.NotificationGroup

fun <T : Any> T?.switchNull(
    onNull: (() -> Unit)? = null,
    onNonNull: ((T) -> Unit)? = null,
) = also { thing ->
    if (thing == null) {
        onNull?.invoke()
    } else {
        onNonNull?.invoke(thing)
    }
}

val notificationGroup = NotificationGroup.balloonGroup("Direnv")
