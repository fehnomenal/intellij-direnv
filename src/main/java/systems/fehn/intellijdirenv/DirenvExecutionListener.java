package systems.fehn.intellijdirenv;

import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import systems.fehn.intellijdirenv.services.DirenvProjectService;
import systems.fehn.intellijdirenv.settings.DirenvSettingsState;


class DirenvExecutionListener implements com.intellij.execution.ExecutionListener {
    public DirenvExecutionListener() {
    }

    @Override
    public void processStarting(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
        if ( !DirenvSettingsState.getInstance().direnvSettingsImportEveryExecution ) {
            com.intellij.execution.ExecutionListener.super.processStarting(executorId, env);
            return;
        }

        Project project = env.getProject();
        DirenvProjectService service = project.getService(DirenvProjectService.class);
        VirtualFile envrcFile = service.getProjectEnvrcFile();
        if (envrcFile != null) {
            service.importDirenv(envrcFile, false);
        }
        com.intellij.execution.ExecutionListener.super.processStarting(executorId, env);
    }
}
