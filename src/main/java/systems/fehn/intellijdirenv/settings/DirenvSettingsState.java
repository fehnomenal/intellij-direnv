package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@State(
        name = "systems.fehn.intellijdirenv.settings.DirenvSettingsState",
        storages = @Storage("DirenvSettings.xml")
)
public class DirenvSettingsState implements PersistentStateComponent<DirenvSettingsState> {
    public String direnvSettingsPath = "";
    private Map<String, String> direnvVars = new HashMap<>();

    public static DirenvSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(DirenvSettingsState.class);
    }

    @Override
    public @Nullable DirenvSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DirenvSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Map<String, String> getDirenvVars() {
        return this.direnvVars;
    }

    public void setDirenvVars(Map<String, String> direnvVars) {
        this.direnvVars = direnvVars;
    }
}
