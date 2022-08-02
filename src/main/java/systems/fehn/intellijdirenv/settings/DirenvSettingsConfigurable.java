package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class DirenvSettingsConfigurable implements Configurable {
    private DirenvSettingsComponent direnvSettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "DirEnv Settings";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return direnvSettingsComponent.getPreferredFocusedComponent();
    }

    @Override
    public @Nullable JComponent createComponent() {
        direnvSettingsComponent = new DirenvSettingsComponent();
        return direnvSettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        DirenvSettingsState settings = DirenvSettingsState.getInstance();
        return !direnvSettingsComponent.getDirenvPath().equals(settings.direnvSettingsPath);
    }

    @Override
    public void apply() throws ConfigurationException {
        DirenvSettingsState settings = DirenvSettingsState.getInstance();
        settings.direnvSettingsPath = direnvSettingsComponent.getDirenvPath();
    }

    @Override
    public void reset() {
        DirenvSettingsState settings = DirenvSettingsState.getInstance();
        direnvSettingsComponent.setDirenvPath(settings.direnvSettingsPath);
    }

    @Override
    public void disposeUIResources() {
        direnvSettingsComponent = null;
    }
}
