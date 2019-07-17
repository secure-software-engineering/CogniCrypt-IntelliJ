package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.io.FileUtil;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.Collections.Linq;
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
import java.util.Set;


// TODO: Implement a modified detection with a new value only class that overrides equals.
//  Name the class CachedSettingsState.
// TODO: Create a instance of CachedSettingsState that holds the current unapplied changes.
class CogniCryptSettingsView implements Configurable
{
    private JPanel _rootPanel;
    private JButton _browseRules;
    private JTextField _cryslRulesDirectory;
    private JPanel _apkFindGroup;
    private JCheckBox _findAutomaticallyBox;
    private JComboBox _findBuildOptionBox;
    private JCheckBox _includeSignedBuildsBox;
    private JCheckBox _onlySignedBox;
    private JPanel _apkFindOptionsGroup;

    private ICongniCryptSettings _settings;
    private boolean _isModified;

    private boolean _lastFindAutomatically = false;
    private boolean _lastIncludeSigned = false;
    private boolean _lastSignedOnly = false;
    private OutputFinderOptions.Flags _lastBuildType = OutputFinderOptions.Flags.Debug;

    private Runnable _onFindAutomaticallyChanged = () -> OnCheckBoxChanged(_findAutomaticallyBox, _apkFindOptionsGroup, _lastFindAutomatically);
    private Runnable _onIncludeSignedChanged = () -> OnCheckBoxChanged(_includeSignedBuildsBox, _onlySignedBox, _lastIncludeSigned);
    private Runnable _onSignedOnlyChanged = () -> OnCheckBoxChanged(_onlySignedBox, null, _lastSignedOnly);
    private Runnable _onBuildTypeChanged = () -> OnComboboxItemChanged(_lastBuildType);


    CogniCryptSettingsView()
    {
        _settings = ServiceManager.getService(ICongniCryptSettings.class);

        _browseRules.addActionListener(e -> OnBrowseCrySlDirectoryPressed(e));
        _findAutomaticallyBox.addActionListener(e -> _onFindAutomaticallyChanged.run());
        _includeSignedBuildsBox.addActionListener(e -> _onIncludeSignedChanged.run());
        _onlySignedBox.addActionListener(e -> _onSignedOnlyChanged.run());
        _findBuildOptionBox.addActionListener(e -> _onBuildTypeChanged.run());


        SetupUi();
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
        _settings.setRulesDirectory(_cryslRulesDirectory.getText());

        boolean findAutomatically = _findAutomaticallyBox.isSelected();
        boolean includeSigned = _includeSignedBuildsBox.isSelected();
        boolean signedOnly =  _onlySignedBox.isSelected();

        OutputFinderOptions.Flags buildType = (OutputFinderOptions.Flags)_findBuildOptionBox.getSelectedItem();
        _settings.setFindAutomatically(findAutomatically);
        _settings.setFinderBuildType(buildType.getStatusFlagValue());
        _settings.setIncludeSigned(includeSigned);
        _settings.setSignedOnly(signedOnly);

        _lastFindAutomatically = findAutomatically;
        _lastBuildType = buildType;
        _lastIncludeSigned = includeSigned;
        _lastSignedOnly = signedOnly;
        _isModified = false;
    }

    protected void OnCheckBoxChanged(JCheckBox sender, Container dependedContainer, boolean lastValue)
    {
        boolean currentValue = sender.isSelected();
        enableComponents(dependedContainer, currentValue);
        if (lastValue == currentValue)
            return;
        _isModified = true;
    }

    private <T> void OnComboboxItemChanged(T lastValue)
    {
        T currentValue = (T) _findBuildOptionBox.getSelectedItem();
        if (lastValue == currentValue)
            return;
        _isModified = true;
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

    private void SetupUi()
    {
        _apkFindGroup.setBorder(BorderFactory.createTitledBorder("Analyse Options"));
        Set<OutputFinderOptions.Flags> set = EnumSet.of(OutputFinderOptions.Flags.Debug, OutputFinderOptions.Flags.Release, OutputFinderOptions.Flags.AnyBuild);
        _findBuildOptionBox.setModel(new DefaultComboBoxModel(set.toArray()));

        _cryslRulesDirectory.setText(_settings.getRulesDirectory());

        try
        {
            boolean findAutomatically = _settings.getFindAutomatically();
            boolean includeSigned = _settings.getIncludeSigned();
            boolean signedOnly = _settings.getSignedOnly();
            int buildTypeValue = _settings.getFinderBuildType();

            _findAutomaticallyBox.setSelected(findAutomatically);
            _includeSignedBuildsBox.setSelected(includeSigned);
            _onlySignedBox.setSelected(signedOnly);

            OutputFinderOptions.Flags buildType = Linq.last(OutputFinderOptions.getStatusFlags(buildTypeValue));
            _findBuildOptionBox.setSelectedItem(buildType);

            _lastFindAutomatically = findAutomatically;
            _lastIncludeSigned = includeSigned;
            _lastSignedOnly = signedOnly;
        }
        finally
        {
            _onBuildTypeChanged.run();
            _onFindAutomaticallyChanged.run();
            _onIncludeSignedChanged.run();
            _onSignedOnlyChanged.run();
        }
    }

    private void showDownloadCrySLRulesDialog(String newPath) {
        MessageBox.Show("No .cryptslbin files found in "+ newPath+" \nYou can download them here:\n" + Constants.CRYSL_RULES_DOWNLOADLINK,
                JOptionPane.ERROR_MESSAGE, _rootPanel);
    }

    private static void enableComponents(Container container, boolean enable) {
        if (container == null)
            return;
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
