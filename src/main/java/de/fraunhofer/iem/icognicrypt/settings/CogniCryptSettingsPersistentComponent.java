package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;


@State(name = "cogniCrypt", storages = {@Storage("cogniCrypt.xml")})

class CogniCryptSettingsPersistentComponent implements ICongniCryptSettings
{
    private String _rulesDirectory = "./icognicrypt/resources/CrySLRules/JavaCryptographicArchitecture";
    private boolean _findAutomatically = true;
    private boolean _includeSigned = false;
    private boolean _signedOnly = false;

    private int _finderBuildType = OutputFinderOptions.Flags.Debug.getStatusFlagValue();


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
    public void setFindAutomatically(boolean value)
    {
        _findAutomatically = value;
    }

    @Override
    public int getFinderBuildType()
    {
        return _finderBuildType;
    }

    @Override
    public void setFinderBuildType(int value)
    {
        //Correct and set to default
        if (value <= 0 || value > 3)
            value = 1;
        _finderBuildType = value;
    }

    @Override
    public boolean getIncludeSigned()
    {
        return _includeSigned;
    }

    @Override
    public void setIncludeSigned(boolean value)
    {
        _includeSigned = value;
    }

    @Override
    public boolean getSignedOnly()
    {
        return _signedOnly;
    }

    @Override
    public void setSignedOnly(boolean value)
    {
        _signedOnly = value;
    }


    public EnumSet<OutputFinderOptions.Flags> GetFindOutputOptions(){
        EnumSet<OutputFinderOptions.Flags> result = EnumSet.noneOf(OutputFinderOptions.Flags.class);
        if (!_findAutomatically)
            return result;
        result.addAll(OutputFinderOptions.getStatusFlags(_finderBuildType));
        if (!_includeSigned)
            return result;
        result.add(OutputFinderOptions.Flags.IncludeSigned);
        if (_signedOnly)
            result.add(OutputFinderOptions.Flags.SignedOnly);
        return result;
    }
}

