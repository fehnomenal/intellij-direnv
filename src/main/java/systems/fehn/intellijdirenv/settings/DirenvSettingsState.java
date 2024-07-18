package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@State(
        name = "systems.fehn.intellijdirenv.settings.DirenvSettingsState",
        storages = @Storage("DirenvSettings.xml")
)
public class DirenvSettingsState implements PersistentStateComponent<DirenvSettingsState> {
    public String direnvSettingsPath = getDirenvPathFromSystem();
    public Boolean direnvSettingsImportOnStartup = false;
    public Boolean direnvSettingsImportEveryExecution = false;

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

    /**
     * Try detect installed direnv executable from PATH environment
     *
     * @return Absolute path to direnv executable, or empty string if program wasn't found
     */
    @NotNull
    private static String getDirenvPathFromSystem() {
        String pathEnv = System.getenv("PATH");
        if (StringUtils.isBlank(pathEnv)) {
            return "";
        }

        for (String dirPath : pathEnv.split(":")) {
            if (!Files.exists(Path.of(dirPath))) {
                continue;
            }

            File[] files = new File(dirPath).listFiles();
            if (files == null) continue;

            Optional<String> direnvPath = Arrays.stream(files)
                    .parallel()
                    .filter(it -> it.getName().startsWith("direnv") && it.canExecute())
                    .findFirst()
                    .map(File::getAbsolutePath);

            if (direnvPath.isPresent()) {
                return direnvPath.get();
            }
        }

        return "";
    }
}
