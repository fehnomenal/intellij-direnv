package systems.fehn.intellijdirenv.services

import systems.fehn.intellijdirenv.MyBundle
import systems.fehn.intellijdirenv.settings.DirenvSettingsState

class EnvironmentService {
    fun unsetVariable(name: String) {
        val appSettings = DirenvSettingsState.getInstance()

        appSettings.direnvVars.remove(name)
        modifiableEnvironment.remove(name)
    }

    fun setVariable(name: String, value: String) {
        val appSettings = DirenvSettingsState.getInstance()

        appSettings.direnvVars[name] = value
        modifiableEnvironment[name] = value
    }

    class ManipulateEnvironmentException(message: String) : Throwable(message)

    private val modifiableEnvironment by lazy {
        val env = System.getenv()
        val envClass = env.javaClass

        when (envClass.canonicalName) {
            "java.util.Collections.UnmodifiableMap" -> hackUnmodifiableMap(env, envClass)

            else -> throw ManipulateEnvironmentException(MyBundle.message("exception.unknownClass", envClass.name))
        }
    }

    private fun hackUnmodifiableMap(obj: Any, cls: Class<*>): MutableMap<String, String> {
        val mapFields = cls.declaredFields.filter { it.type == Map::class.java }

        val mapField = when (mapFields.size) {
            0 -> throw ManipulateEnvironmentException(
                MyBundle.message(
                    "exception.noMapField",
                    cls.canonicalName,
                    Map::class.java.canonicalName
                )
            )
            1 -> mapFields[0]
            else -> throw ManipulateEnvironmentException(
                MyBundle.message(
                    "exception.multipleMapFields",
                    cls.canonicalName,
                    Map::class.java.canonicalName,
                    mapFields.map { it.name }
                )
            )
        }

        mapField.isAccessible = true
        val map = mapField.get(obj)
        @Suppress("UNCHECKED_CAST")
        return map as MutableMap<String, String>
    }
}
