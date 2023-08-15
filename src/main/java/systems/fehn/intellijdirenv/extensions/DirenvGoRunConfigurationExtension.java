package systems.fehn.intellijdirenv.extensions;

import com.goide.execution.GoRunConfigurationBase;
import com.goide.execution.GoRunningState;
import com.goide.execution.extension.GoRunConfigurationExtension;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.target.TargetedCommandLineBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import systems.fehn.intellijdirenv.settings.DirenvSettingsState;

import java.util.Map;

public class DirenvGoRunConfigurationExtension extends GoRunConfigurationExtension {
    @Override
    public boolean isApplicableFor(@NotNull GoRunConfigurationBase<?> configuration) {
        return true;
    }

    @Override
    public boolean isEnabledFor(@NotNull GoRunConfigurationBase<?> applicableConfiguration, @Nullable RunnerSettings runnerSettings) {
        return true;
    }

    @Override
    protected void patchCommandLine(@NotNull GoRunConfigurationBase<?> configuration, @Nullable RunnerSettings runnerSettings, @NotNull TargetedCommandLineBuilder cmdLine, @NotNull String runnerId, @NotNull GoRunningState<? extends GoRunConfigurationBase<?>> state, GoRunningState.CommandLineType commandLineType) throws ExecutionException {
        DirenvSettingsState settings = DirenvSettingsState.getInstance();

        Map<String, String> direnvVars = settings.getDirenvVars();
        for (Map.Entry<String, String> entry : direnvVars.entrySet()) {
            cmdLine.addEnvironmentVariable(entry.getKey(), entry.getValue());
        }
    }
}
