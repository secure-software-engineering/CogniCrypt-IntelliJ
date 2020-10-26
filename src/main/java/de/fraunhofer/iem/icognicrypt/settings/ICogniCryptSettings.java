package de.fraunhofer.iem.icognicrypt.settings;

import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.core.Language.SupportedLanguage;

import java.util.EnumSet;

interface ICogniCryptSettings
{
    String getRulesDirectory();

    void setRulesDirectory(String rulesDirectory);

    String getCurrentVersion();

    void setCurrentVersion(String current_version);


    boolean getFindAutomatically();

    void setFindAutomatically(boolean value);


    int getFinderBuildType();

    void setFinderBuildType(int value);


    boolean getIncludeSigned();

    void setIncludeSigned(boolean value);


    boolean getSignedOnly();

    void setSignedOnly(boolean value);

    SupportedLanguage getOptimizedLanguage();
    void setOptimizedLanguage(SupportedLanguage language);


    EnumSet<OutputFinderOptions.Flags> GetFindOutputOptions();
}
