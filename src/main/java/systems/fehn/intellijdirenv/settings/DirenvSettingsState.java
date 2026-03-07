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
    public Boolean direnvSettingsImportOnStartup = false;
    public Boolean direnvSettingsImportEveryExecution = false;
    public Boolean direnvSettingsImportRecursive = false;

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
}
