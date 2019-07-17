package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;

import java.util.EnumSet;

public interface ICongniCryptSettings extends PersistentStateComponent<CogniCryptSettingsPersistentComponent>
{
    String getRulesDirectory();

    void setRulesDirectory(String rulesDirectory);


    boolean getFindAutomatically();

    void setFindAutomatically(boolean value);


    int getFinderBuildType();

    void setFinderBuildType(int value);


    boolean getIncludeSigned();

    void setIncludeSigned(boolean value);


    boolean getSignedOnly();

    void setSignedOnly(boolean value);


    EnumSet<OutputFinderOptions.Flags> GetFindOutputOptions();
}
