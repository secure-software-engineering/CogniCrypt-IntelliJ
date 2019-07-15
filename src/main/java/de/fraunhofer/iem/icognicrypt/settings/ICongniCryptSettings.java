package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.PersistentStateComponent;

public interface ICongniCryptSettings extends PersistentStateComponent<CogniCryptSettingsPersistentComponent>
{
    String getRulesDirectory();

    void setRulesDirectory(String rulesDirectory);
}
