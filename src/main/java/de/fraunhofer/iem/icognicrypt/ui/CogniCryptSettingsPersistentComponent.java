package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "IcognicryptSettings",
        storages = {@Storage("IcognicryptSettings.xml")})


public class CogniCryptSettingsPersistentComponent implements PersistentStateComponent<CogniCryptSettingsPersistentComponent> {

    public String RULES_DIRECTORY = "./icognicrypt/resources/CrySLRules/JavaCryptographicArchitecture";

    CogniCryptSettingsPersistentComponent() {
    }

    @Nullable
    @Override
    public CogniCryptSettingsPersistentComponent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CogniCryptSettingsPersistentComponent icognicryptSettings) {
        XmlSerializerUtil.copyBean(icognicryptSettings, this);
    }

    public static CogniCryptSettingsPersistentComponent getInstance() {
        return ServiceManager.getService(CogniCryptSettingsPersistentComponent.class);
    }

    public String getRulesDirectory() {
        return RULES_DIRECTORY;
    }

    public void setRulesDirectory(String rulesDirectory) {
        this.RULES_DIRECTORY = rulesDirectory;
    }
}
