package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;

import java.util.EnumSet;

public interface ICongniCryptSettings extends PersistentStateComponent<CogniCryptSettingsPersistentComponent>
{
    String getRulesDirectory();

    void setRulesDirectory(String rulesDirectory);

    EnumSet<OutputFinderOptions.Flags> getFindOutputOptions();

    void setFindOutputOptions(EnumSet<OutputFinderOptions.Flags> options);
}
