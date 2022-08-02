package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DirenvSettingsComponent {
    private final JPanel mainPanel;
    private final TextFieldWithBrowseButton direnvPath = new TextFieldWithBrowseButton();

    public DirenvSettingsComponent() {
        direnvPath.addBrowseFolderListener( "Direnv Path", "Path to the direnv file", null,
                FileChooserDescriptorFactory.createSingleFileDescriptor());
        mainPanel = FormBuilder
                .createFormBuilder()
                .addLabeledComponent(new JLabel("DirenvPath: "), direnvPath, 1, false)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return direnvPath;
    }

    @NotNull
    public String getDirenvPath() {
        return direnvPath.getText();
    }

    public void setDirenvPath(@NotNull String path) {
        direnvPath.setText(path);
    }
}
