package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "cogniCrypt", storages = {@Storage("cogniCrypt.xml")})

class CogniCryptSettingsPersistentComponent implements ICongniCryptSettings
{
    private String _rulesDirectory = "./icognicrypt/resources/CrySLRules/JavaCryptographicArchitecture";
    private boolean _findAutomatically = true;
    private boolean _includeSigned = false;

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
        return _rulesDirectory;
    }

    public void setRulesDirectory(String rulesDirectory)
    {
        _rulesDirectory = rulesDirectory;
    }

    @Override
    public boolean getFindAutomatically()
    {
        return _findAutomatically;
    }

    @Override
    public void setFindAutomatically(boolean findAutomatically)
    {
        _findAutomatically = findAutomatically;
    }

    @Override
    public boolean getIncludeSigned()
    {
        return _includeSigned;
    }

    @Override
    public void setIncludeSigned(boolean includeSigned)
    {
        _includeSigned = includeSigned;
    }

    @Override
    public int getFindApkMode()
    {
        return 0;
    }

    @Override
    public void setFindApkMode(int findApkMode)
    {

    }
}

