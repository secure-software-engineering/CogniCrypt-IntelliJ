package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.io.FileUtil;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.Dialogs.DialogHelper;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLHelper;
import de.fraunhofer.iem.icognicrypt.ui.MessageBox;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;

class CogniCryptSettingsView implements Configurable
{
    private JPanel _rootPanel;
    private JButton _browseRules;
    private JTextField _cryslRulesDirectory;
    private JPanel _apkFindGroup;
    private JCheckBox _findAutomatically;
    private JComboBox _findBuildOption;
    private JCheckBox _includeSignedBuilds;
    private JCheckBox _onlySigned;
    private JPanel _apkFindOptionsGroup;

    private ICongniCryptSettings _settings;
    private boolean _isModified;

    CogniCryptSettingsView()
    {
        _settings = ServiceManager.getService(ICongniCryptSettings.class);
        _apkFindGroup.setBorder(BorderFactory.createTitledBorder("Analyse Options"));
        _cryslRulesDirectory.setText(_settings.getRulesDirectory());
        _browseRules.addActionListener(e -> OnBrowseCrySlDirectoryPressed(e));
        _findAutomatically.addActionListener(e -> OnFindAutomaticallyChanged(e));
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName()
    {
        return "CongiCrypt";
    }

    @Nullable
    @Override
    public JComponent createComponent()
    {
        return _rootPanel;
    }

    @Override
    public boolean isModified()
    {
        return _isModified;
    }

    @Override
    public void apply() throws ConfigurationException
    {
        _settings.setRulesDirectory(_browseRules.getText());
        _isModified = false;
    }

    private void OnFindAutomaticallyChanged(ActionEvent e)
    {
        boolean enabled = _findAutomatically.isSelected();
        enableComponents(_apkFindOptionsGroup, enabled);
    }

    private void OnBrowseCrySlDirectoryPressed(ActionEvent e)
    {
        File defaultPath = Paths.get(_cryslRulesDirectory.getText()).toFile();
        File selectedDirectory = DialogHelper.ChooseSingleDirectoryFromDialog("Select CrySL Folder", defaultPath.toPath(), _rootPanel, selectedPath -> {
            String path = selectedPath.getPath();
            if(CrySLHelper.isValidCrySLRuleDirectory(path)){
                return selectedPath;
            } else {
                showDownloadCrySLRulesDialog(path);
                return null;
            }
        });
        if (_cryslRulesDirectory == null)
            return;
        if (FileUtil.filesEqual(selectedDirectory, defaultPath))
            return;
        _cryslRulesDirectory.setText(selectedDirectory.getPath());
        _isModified = true;
    }

    private EnumSet<OutputFinderOptions.Flags> CreateFlags(){
        EnumSet<OutputFinderOptions.Flags> result = EnumSet.noneOf(OutputFinderOptions.Flags.class);

        if (!_findAutomatically.isSelected())
            return result;

        return result;
    }

    private void showDownloadCrySLRulesDialog(String newPath) {
        MessageBox.Show("No .cryptslbin files found in "+ newPath+" \nYou can download them here:\n" + Constants.CRYSL_RULES_DOWNLOADLINK,
                JOptionPane.ERROR_MESSAGE, _rootPanel);
    }

    private static void enableComponents(Container container, boolean enable) {
        container.setEnabled(enable);
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(enable);
            if (component instanceof Container) {
                enableComponents((Container)component, enable);
            }
        }
    }
}
