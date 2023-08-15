package systems.fehn.intellijdirenv.settings;

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Map;

public class DirenvSettingsComponent {
    private final JPanel mainPanel;
    private final TextFieldWithBrowseButton direnvPath = new TextFieldWithBrowseButton();
    private final JBTable envStateTable;

    public DirenvSettingsComponent() {
        direnvPath.addBrowseFolderListener( "Direnv Path", "Path to the direnv file", null,
                FileChooserDescriptorFactory.createSingleFileDescriptor());

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Name", "Value"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        envStateTable = new JBTable(tableModel);
        JScrollPane scrollPane = new JBScrollPane(envStateTable);
        envStateTable.setFillsViewportHeight(true);

        mainPanel = FormBuilder
                .createFormBuilder()
                .addLabeledComponent(new JLabel("DirenvPath: "), direnvPath, 1, false)
                .addComponent(scrollPane)
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

    public void updateDirenvVars(Map<String, String> direnvVars) {
        envStateTable.removeAll();
        DefaultTableModel tableModel = (DefaultTableModel)envStateTable.getModel();

        for (Map.Entry<String, String> vars : direnvVars.entrySet()) {
            tableModel.addRow(new String[]{vars.getKey(), vars.getValue()});
        }
    }
}
