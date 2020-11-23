package systems.fehn.intellijdirenv

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
