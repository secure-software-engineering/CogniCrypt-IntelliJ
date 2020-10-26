package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.Dialogs.DialogHelper;
import de.fraunhofer.iem.icognicrypt.core.Language.SupportedLanguage;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLHelper;
import de.fraunhofer.iem.icognicrypt.ui.MessageBox;
import javaLinq.Linq;
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
    private JPanel _generalGroup;
    private JComboBox _optimizedLanguageComboBox;

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
        boolean newValue = OnCheckBoxChanged(_findAutomaticallyBox, _setFindOptionsGroupEnabled);
        _currentState.setFindAutomatically(newValue);
    };

    private final Runnable _onIncludeSignedChanged = () ->
    {
        boolean newValue = OnCheckBoxChanged(_includeSignedBuildsBox, _setSignedOnlyEnabled);
        _currentState.setIncludeSigned(newValue);
    };

    private final Runnable _onSignedOnlyChanged = () ->
    {
        boolean newValue = OnCheckBoxChanged(_onlySignedBox, null);
        _currentState.setSignedOnly(newValue);
    };

    private final Runnable _onBuildTypeChanged = () ->
    {
        OutputFinderOptions.Flags newValue = OnComboboxItemChanged(_findBuildOptionBox);
        _currentState.setFinderBuildType(newValue.getStatusFlagValue());
    };

    private final Runnable _onOptimizationChanged = () ->
    {
        SupportedLanguage newValue = OnComboboxItemChanged(_optimizedLanguageComboBox);
        _currentState.setOptimizedLanguage(newValue);
    };

    private final Runnable _onOnRulesPathChanges = () ->
    {
        String newValue = OnTextChanged(_cryslRulesDirectory);
        _currentState.setRulesDirectory(newValue);
    };

    CogniCryptSettingsView()
    {
        _oldState = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        _currentState = new CachedSettingsState(_oldState);
        RegisterEvents();
        SetupUi();
    }

    @Override
    public String getDisplayName()
    {
        return "CogniCrypt";
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
    public void apply()
    {
        IPersistableCogniCryptSettings settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
        settings.setRulesDirectory(_currentState.getRulesDirectory());
        settings.setCurrentVersion(_currentState.getCurrentVersion());
        settings.setFindAutomatically(_currentState.getFindAutomatically());
        settings.setFinderBuildType(_currentState.getFinderBuildType());
        settings.setIncludeSigned(_currentState.getIncludeSigned());
        settings.setSignedOnly(_currentState.getSignedOnly());
        settings.setOptimizedLanguage(_currentState.getOptimizedLanguage());
        _oldState = new CachedSettingsState(settings);
    }

    @Override
    public void reset()
    {
        SetUiFromSettings(_oldState);
    }

    protected boolean OnCheckBoxChanged(JCheckBox sender, Consumer<Boolean> setDependentEnabled)
    {
        boolean currentValue = sender.isSelected();
        if (setDependentEnabled != null) setDependentEnabled.accept(currentValue);
        return currentValue;
    }

    protected  <T> T OnComboboxItemChanged(JComboBox comboBox)
    {
        T currentValue = (T) comboBox.getSelectedItem();
        return currentValue;
    }

    private String OnTextChanged(JTextField cryslRulesDirectory) {
        String newText = cryslRulesDirectory.getText();
        if (CrySLHelper.isValidCrySLRuleDirectory(newText)) {
            return newText;
        } else {
            showDownloadCrySLRulesDialog(newText);
        }
        return _oldState.getRulesDirectory();
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
        if (_cryslRulesDirectory == null || selectedDirectory == null)
            return;
        _cryslRulesDirectory.setText(selectedDirectory.getPath());
        _currentState.setRulesDirectory(selectedDirectory.getPath());
    }

    private void SetupUi()
    {
        _apkFindGroup.setBorder(BorderFactory.createTitledBorder("Analyze Options"));
        _generalGroup.setBorder(BorderFactory.createTitledBorder("General"));
        Set<OutputFinderOptions.Flags> outputOptions = EnumSet.of(OutputFinderOptions.Flags.Debug, OutputFinderOptions.Flags.Release, OutputFinderOptions.Flags.AnyBuild);
        _findBuildOptionBox.setModel(new DefaultComboBoxModel(outputOptions.toArray()));
        _optimizedLanguageComboBox.setModel(new DefaultComboBoxModel(SupportedLanguage.values()));
        SetUiFromSettings(_oldState);
    }

    private void RegisterEvents()
    {
        _cryslRulesDirectory.addActionListener(e -> _onOnRulesPathChanges.run());
        _browseRules.addActionListener(e -> OnBrowseCrySlDirectoryPressed(e));
        _findAutomaticallyBox.addActionListener(e -> _onFindAutomaticallyChanged.run());
        _includeSignedBuildsBox.addActionListener(e -> _onIncludeSignedChanged.run());
        _onlySignedBox.addActionListener(e -> _onSignedOnlyChanged.run());
        _findBuildOptionBox.addActionListener(e -> _onBuildTypeChanged.run());
        _optimizedLanguageComboBox.addActionListener(e -> _onOptimizationChanged.run());
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
            _optimizedLanguageComboBox.setSelectedItem(settings.getOptimizedLanguage());
        }
        finally
        {
            _onBuildTypeChanged.run();
            _onFindAutomaticallyChanged.run();
            _onIncludeSignedChanged.run();
            _onSignedOnlyChanged.run();
            _onOptimizationChanged.run();
        }
    }

    private void showDownloadCrySLRulesDialog(String newPath)
    {
        MessageBox.Show(_rootPanel, "No .crysl files found in " + newPath +
                        "\nYou can download them here:\n" + Constants.CRYSL_RULES_DOWNLOADLINK,
                        MessageBox.MessageBoxButton.OK, MessageBox.MessageBoxType.Error);
    }

    private class CachedSettingsState extends CogniCryptSettings
    {
        CachedSettingsState(ICogniCryptSettings other)
        {
            super(other);
        }
    }
}
