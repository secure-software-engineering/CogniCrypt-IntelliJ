package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.ServiceManager;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.Language.SupportedLanguage;
import de.fraunhofer.iem.icognicrypt.core.Language.SupportedLanguagesUtils;

import java.util.EnumSet;


abstract class CogniCryptSettings implements ICogniCryptSettings
{
    protected String RulesDirectory = Constants.DummyCrySLPath;
    protected boolean FindAutomatically = true;
    protected boolean IncludeSigned = false;
    protected boolean SignedOnly = false;
    protected SupportedLanguage OptimizedLanguage = SupportedLanguage.Java;


    protected int FinderBuildType = OutputFinderOptions.Flags.Debug.getStatusFlagValue();

    public CogniCryptSettings(){

    }

    public CogniCryptSettings(ICogniCryptSettings other)
    {
        setRulesDirectory(other.getRulesDirectory());
        setFindAutomatically(other.getFindAutomatically());
        setFinderBuildType(other.getFinderBuildType());
        setIncludeSigned(other.getIncludeSigned());
        setSignedOnly(other.getSignedOnly());
        setOptimizedLanguage(other.getOptimizedLanguage());
    }

    public String getRulesDirectory()
    {
        return RulesDirectory;
    }

    public void setRulesDirectory(String rulesDirectory)
    {
        RulesDirectory = rulesDirectory;
    }

    @Override
    public boolean getFindAutomatically()
    {
        return FindAutomatically;
    }

    @Override
    public void setFindAutomatically(boolean value)
    {
        FindAutomatically = value;
    }

    @Override
    public int getFinderBuildType()
    {
        return FinderBuildType;
    }

    @Override
    public void setFinderBuildType(int value)
    {
        //Correct and set to default
        if (value <= 0 || value > 3)
            value = 1;
        FinderBuildType = value;
    }

    @Override
    public boolean getIncludeSigned()
    {
        return IncludeSigned;
    }

    @Override
    public void setIncludeSigned(boolean value)
    {
        IncludeSigned = value;
    }

    @Override
    public boolean getSignedOnly()
    {
        return SignedOnly;
    }

    @Override
    public void setSignedOnly(boolean value)
    {
        SignedOnly = value;
    }

    @Override
    public SupportedLanguage getOptimizedLanguage()
    {
        return OptimizedLanguage;
    }

    @Override
    public void setOptimizedLanguage(SupportedLanguage optimizedLanguage)
    {
        OptimizedLanguage = optimizedLanguage;
    }

    public EnumSet<OutputFinderOptions.Flags> GetFindOutputOptions(){
        EnumSet<OutputFinderOptions.Flags> result = EnumSet.noneOf(OutputFinderOptions.Flags.class);
        if (!FindAutomatically)
            return result;
        result.addAll(OutputFinderOptions.getStatusFlags(FinderBuildType));
        if (!IncludeSigned)
            return result;
        result.add(OutputFinderOptions.Flags.IncludeSigned);
        if (SignedOnly)
            result.add(OutputFinderOptions.Flags.SignedOnly);
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (!(obj instanceof ICogniCryptSettings))
            return false;
        ICogniCryptSettings other = (ICogniCryptSettings) obj;
        return equals(other);
    }

    public boolean equals(ICogniCryptSettings other){
        if (other == null)
            return false;
        if (!RulesDirectory.equals(other.getRulesDirectory()))
            return false;
        if (FindAutomatically != other.getFindAutomatically())
            return false;
        if (FinderBuildType != other.getFinderBuildType())
            return false;
        if (IncludeSigned != other.getIncludeSigned())
            return false;
        if (SignedOnly != other.getSignedOnly())
            return false;
        if (OptimizedLanguage != other.getOptimizedLanguage())
            return false;
        return true;
    }
}
