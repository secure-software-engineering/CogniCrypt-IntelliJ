package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
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

    CogniCryptSettingsView()
    {
        _apkFindGroup.setBorder(BorderFactory.createTitledBorder("Analyse Options"));
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
        return false;
    }

    @Override
    public void apply() throws ConfigurationException
    {
    }
}
