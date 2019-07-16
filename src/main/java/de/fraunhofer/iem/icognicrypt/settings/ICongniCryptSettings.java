package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.PersistentStateComponent;

public interface ICongniCryptSettings extends PersistentStateComponent<CogniCryptSettingsPersistentComponent>
{
    String getRulesDirectory();

    void setRulesDirectory(String rulesDirectory);

    boolean getFindAutomatically();

    void setFindAutomatically(boolean findAutomatically);

    boolean getIncludeSigned();

    void setIncludeSigned(boolean includeSigned);

    int getFindApkMode();

    void setFindApkMode(int findApkMode);
}
