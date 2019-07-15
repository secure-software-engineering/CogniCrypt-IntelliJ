package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "cogniCrypt", storages = {@Storage("cogniCrypt.xml")})

class CogniCryptSettingsPersistentComponent implements ICongniCryptSettings
{
    private String RULES_DIRECTORY = "./icognicrypt/resources/CrySLRules/JavaCryptographicArchitecture";

    @Nullable
    @Override
    public CogniCryptSettingsPersistentComponent getState()
    {
        return this;
    }

    @Override
    public void loadState(@NotNull CogniCryptSettingsPersistentComponent icognicryptSettings)
    {
        XmlSerializerUtil.copyBean(icognicryptSettings, this);
    }

    public String getRulesDirectory()
    {
        return RULES_DIRECTORY;
    }

    public void setRulesDirectory(String rulesDirectory)
    {
        this.RULES_DIRECTORY = rulesDirectory;
    }
}

