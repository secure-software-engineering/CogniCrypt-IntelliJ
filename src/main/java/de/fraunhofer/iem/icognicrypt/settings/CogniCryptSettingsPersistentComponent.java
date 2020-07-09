package de.fraunhofer.iem.icognicrypt.settings;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import de.fraunhofer.iem.icognicrypt.core.Language.SupportedLanguage;
import de.fraunhofer.iem.icognicrypt.core.Language.SupportedLanguagesUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@State(name = "cogniCrypt", storages = {@Storage("cogniCrypt.xml")})
class CogniCryptSettingsPersistentComponent extends CogniCryptSettings implements IPersistableCogniCryptSettings
{
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

    @Override
    public void setOptimizedLanguage(@NotNull SupportedLanguage optimizedLanguage)
    {
        super.setOptimizedLanguage(optimizedLanguage);
        ServiceManager.getService(SupportedLanguagesUtils.class).SupportedLanguages.Promote(optimizedLanguage);
    }
}

