package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "IcognicryptSettings",
        storages = {@Storage("IcognicryptSettings.xml")})


public class IcognicryptSettings implements PersistentStateComponent<IcognicryptSettings> {

    public String RULES_DIRECTORY = "./icognicrypt/resources/CrySLRules";

    IcognicryptSettings() {
    }

    @Nullable
    @Override
    public IcognicryptSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull IcognicryptSettings icognicryptSettings) {
        XmlSerializerUtil.copyBean(icognicryptSettings, this);
    }

    public static IcognicryptSettings getInstance() {
        return ServiceManager.getService(IcognicryptSettings.class);
    }

    public String getRulesDirectory() {
        return RULES_DIRECTORY;
    }

    public void setRulesDirectory(String rulesDirectory) {
        this.RULES_DIRECTORY = rulesDirectory;
    }
}
