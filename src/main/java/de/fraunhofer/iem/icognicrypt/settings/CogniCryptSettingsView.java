package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLHelper;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

class CogniCryptSettingsView implements Configurable
{
    private JPanel _rootPanel;
    private JButton _browseRules;
    private JTextField _cryslRulesDirectory;
    private JPanel _apkFindGroup;
    private JCheckBox _findAutomatically;
    private JComboBox _findBuildOption;
    private JCheckBox _includeSignedBuilds;

    private ICongniCryptSettings _settings;
    private boolean _isModified;

    CogniCryptSettingsView()
    {
        _settings = ServiceManager.getService(ICongniCryptSettings.class);
        _apkFindGroup.setBorder(BorderFactory.createTitledBorder("Analyse Options"));
        _cryslRulesDirectory.setText(_settings.getRulesDirectory());
        _browseRules.addActionListener(e -> _cryslRulesDirectory.setText(openFileChooserDialog(_settings.getRulesDirectory(), _rootPanel)));
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
    }

    //Accept users file or folder selection and send return value
    private String openFileChooserDialog(String path, JPanel contentPane) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new java.io.File(path));
        int returnValue = fileChooser.showOpenDialog(contentPane);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            _isModified = true;
            String newPath = fileChooser.getSelectedFile().getPath();
            if(CrySLHelper.isValidCrySLRuleDirectory(newPath)){
                return newPath;
            } else {
                showDownloadCrySLRulesDialog(contentPane, newPath);
                return path;
            }
        }
        else {
            return path;
        }
    }

    private void showDownloadCrySLRulesDialog(JPanel contentPane, String newPath) {
        JOptionPane.showMessageDialog(contentPane,
                "No .cryptslbin files found in "+ newPath+" \nYou can download them here:\n" + Constants.CRYSL_RULES_DOWNLOADLINK,
                "CrySL Rule Selection Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
