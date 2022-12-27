package systems.fehn.intellijdirenv

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationDisplayType

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

val notificationGroup: NotificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("Direnv")
