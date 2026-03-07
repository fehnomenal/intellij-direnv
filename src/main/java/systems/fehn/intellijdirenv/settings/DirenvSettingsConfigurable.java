package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.options.Configurable;
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
        return !direnvSettingsComponent.getDirenvPath().equals(settings.direnvSettingsPath) ||
                direnvSettingsComponent.getDirenvImportOnStartup() != settings.direnvSettingsImportOnStartup ||
                direnvSettingsComponent.getDirenvImportEveryExecution() != settings.direnvSettingsImportEveryExecution ||
                direnvSettingsComponent.getDirenvImportRecursive() != settings.direnvSettingsImportRecursive;
    }

    @Override
    public void apply() {
        DirenvSettingsState settings = DirenvSettingsState.getInstance();
        settings.direnvSettingsPath = direnvSettingsComponent.getDirenvPath();
        settings.direnvSettingsImportOnStartup = direnvSettingsComponent.getDirenvImportOnStartup();
        settings.direnvSettingsImportEveryExecution = direnvSettingsComponent.getDirenvImportEveryExecution();
        settings.direnvSettingsImportRecursive = direnvSettingsComponent.getDirenvImportRecursive();
    }

    @Override
    public void reset() {
        DirenvSettingsState settings = DirenvSettingsState.getInstance();
        direnvSettingsComponent.setDirenvPath(settings.direnvSettingsPath);
        direnvSettingsComponent.setDirenvImportOnStartup(settings.direnvSettingsImportOnStartup);
        direnvSettingsComponent.setDirenvImportEveryExecution(settings.direnvSettingsImportEveryExecution);
        direnvSettingsComponent.setDirenvImportRecursive(settings.direnvSettingsImportRecursive);
    }

    @Override
    public void disposeUIResources() {
        direnvSettingsComponent = null;
    }
}
