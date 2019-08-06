package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.PersistentStateComponent;

// This should not be legal code (ICogniCryptSettings and CogniCryptSettingsPersistentComponent are package-private)...
public interface IPersistableCogniCryptSettings extends ICogniCryptSettings, PersistentStateComponent<CogniCryptSettingsPersistentComponent>
{
}


