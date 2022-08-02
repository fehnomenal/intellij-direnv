package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "systems.fehn.intellijdirenv.settings.DirenvSettingsState",
        storages = @Storage("DirenvSettings.xml")
)
public class DirenvSettingsState implements PersistentStateComponent<DirenvSettingsState> {
    public String direnvSettingsPath = "";

    public static DirenvSettingsState getInstance() {
        DirenvSettingsState instance = ApplicationManager.getApplication().getService(DirenvSettingsState.class);
        return instance;
    }

    @Override
    public @Nullable DirenvSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DirenvSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
