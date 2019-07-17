package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.Collections.Linq;
import de.fraunhofer.iem.icognicrypt.core.Dialogs.DialogHelper;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLHelper;
import de.fraunhofer.iem.icognicrypt.ui.MessageBox;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

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
    private JLabel _findBuildOptionLabel;

    private ICogniCryptSettings _oldState;
    private ICogniCryptSettings _currentState;

    private final Consumer<Boolean> _setSignedOnlyEnabled = enabled ->
    {
        _onlySignedBox.setEnabled(enabled);
    };

    private final Consumer<Boolean> _setFindOptionsGroupEnabled = enabled ->
    {
        _findBuildOptionBox.setEnabled(enabled);
        _includeSignedBuildsBox.setEnabled(enabled);
        _findBuildOptionLabel.setEnabled(enabled);
        if (!enabled) _onlySignedBox.setEnabled(enabled);
        else _setSignedOnlyEnabled.accept(_includeSignedBuildsBox.isSelected());
    };

    private final Runnable _onFindAutomaticallyChanged = () ->
    {
        boolean newValue = OnCheckBoxChanged(_findAutomaticallyBox, _oldState.getFindAutomatically(), _setFindOptionsGroupEnabled);
        _currentState.setFindAutomatically(newValue);
    };

    private final Runnable _onIncludeSignedChanged = () ->
    {
        boolean newValue = OnCheckBoxChanged(_includeSignedBuildsBox, _oldState.getIncludeSigned(), _setSignedOnlyEnabled);
        _currentState.setIncludeSigned(newValue);
    };

    private final Runnable _onSignedOnlyChanged = () ->
    {
        boolean newValue = OnCheckBoxChanged(_onlySignedBox, _oldState.getSignedOnly(), null);
        _currentState.setSignedOnly(newValue);
    };

    private final Runnable _onBuildTypeChanged = () ->
    {
        OutputFinderOptions.Flags newValue = OnComboboxItemChanged(Linq.last(OutputFinderOptions.getStatusFlags(_currentState.getFinderBuildType())));
        _currentState.setFinderBuildType(newValue.getStatusFlagValue());
    };


    CogniCryptSettingsView()
    {
        _oldState = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        _currentState = new CachedSettingsState(_oldState);
        RegisterEvents();
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
        return !_currentState.equals(_oldState);
    }

    @Override
    public void apply() throws ConfigurationException
    {
        IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        settings.setRulesDirectory(_currentState.getRulesDirectory());
        settings.setFindAutomatically(_currentState.getFindAutomatically());
        settings.setFinderBuildType(_currentState.getFinderBuildType());
        settings.setIncludeSigned(_currentState.getIncludeSigned());
        settings.setSignedOnly(_currentState.getSignedOnly());
        _oldState = new CachedSettingsState(settings);
    }

    @Override
    public void reset()
    {
        SetUiFromSettings(_oldState);
    }

    protected boolean OnCheckBoxChanged(JCheckBox sender, boolean lastValue, Consumer<Boolean> setDependentEnabled)
    {
        boolean currentValue = sender.isSelected();
        if (setDependentEnabled != null) setDependentEnabled.accept(currentValue);
        return currentValue;
    }

    protected  <T> T OnComboboxItemChanged(T lastValue)
    {
        T currentValue = (T) _findBuildOptionBox.getSelectedItem();
        return currentValue;
    }

    protected void OnBrowseCrySlDirectoryPressed(ActionEvent e)
    {
        File defaultPath = Paths.get(_cryslRulesDirectory.getText()).toFile();
        File selectedDirectory = DialogHelper.ChooseSingleDirectoryFromDialog("Select CrySL Folder", defaultPath.toPath(), _rootPanel, selectedPath ->
        {
            String path = selectedPath.getPath();
            if (CrySLHelper.isValidCrySLRuleDirectory(path))
            {
                return selectedPath;
            }
            else
            {
                showDownloadCrySLRulesDialog(path);
                return null;
            }
        });
        if (_cryslRulesDirectory == null)
            return;
        _cryslRulesDirectory.setText(selectedDirectory.getPath());
        _currentState.setRulesDirectory(selectedDirectory.getPath());
    }

    private void SetupUi()
    {
        _apkFindGroup.setBorder(BorderFactory.createTitledBorder("Analyse Options"));
        Set<OutputFinderOptions.Flags> set = EnumSet.of(OutputFinderOptions.Flags.Debug, OutputFinderOptions.Flags.Release, OutputFinderOptions.Flags.AnyBuild);
        _findBuildOptionBox.setModel(new DefaultComboBoxModel(set.toArray()));
        SetUiFromSettings(_oldState);
    }

    private void RegisterEvents()
    {
        _browseRules.addActionListener(e -> OnBrowseCrySlDirectoryPressed(e));
        _findAutomaticallyBox.addActionListener(e -> _onFindAutomaticallyChanged.run());
        _includeSignedBuildsBox.addActionListener(e -> _onIncludeSignedChanged.run());
        _onlySignedBox.addActionListener(e -> _onSignedOnlyChanged.run());
        _findBuildOptionBox.addActionListener(e -> _onBuildTypeChanged.run());
    }


    private void SetUiFromSettings(ICogniCryptSettings settings){
        try
        {
            _cryslRulesDirectory.setText(settings.getRulesDirectory());
            _findAutomaticallyBox.setSelected(settings.getFindAutomatically());
            _includeSignedBuildsBox.setSelected(settings.getIncludeSigned());
            _onlySignedBox.setSelected(settings.getSignedOnly());
            OutputFinderOptions.Flags buildType = Linq.last(OutputFinderOptions.getStatusFlags(settings.getFinderBuildType()));
            _findBuildOptionBox.setSelectedItem(buildType);
        }
        finally
        {
            _onBuildTypeChanged.run();
            _onFindAutomaticallyChanged.run();
            _onIncludeSignedChanged.run();
            _onSignedOnlyChanged.run();
        }
    }

    private void showDownloadCrySLRulesDialog(String newPath)
    {
        MessageBox.Show("No .cryptslbin files found in " + newPath + " \nYou can download them here:\n" + Constants.CRYSL_RULES_DOWNLOADLINK, JOptionPane.ERROR_MESSAGE, _rootPanel);
    }

    private class CachedSettingsState extends CogniCryptSettings
    {
        CachedSettingsState(ICogniCryptSettings other)
        {
            super(other);
        }
    }
}
