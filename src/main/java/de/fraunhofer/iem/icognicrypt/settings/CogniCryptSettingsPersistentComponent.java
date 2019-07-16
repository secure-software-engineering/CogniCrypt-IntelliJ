package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;


@State(name = "cogniCrypt", storages = {@Storage("cogniCrypt.xml")})

class CogniCryptSettingsPersistentComponent implements ICongniCryptSettings
{
    private String _rulesDirectory = "./icognicrypt/resources/CrySLRules/JavaCryptographicArchitecture";
    private int _findOutputOptions = 1;

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
    public EnumSet<OutputFinderOptions.Flags> getFindOutputOptions()
    {
        return OutputFinderOptions.getStatusFlags(_findOutputOptions);
    }

    @Override
    public void setFindOutputOptions(EnumSet<OutputFinderOptions.Flags> options)
    {
        _findOutputOptions = OutputFinderOptions.getStatusValue(options);
    }
}

