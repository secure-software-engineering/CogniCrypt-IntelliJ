package de.fraunhofer.iem.icognicrypt;

import com.intellij.openapi.progress.ProgressIndicator;
import de.fraunhofer.iem.icognicrypt.core.BackgroundComponent;
import de.fraunhofer.iem.icognicrypt.core.crySL.CrySLExtractor;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;

public class CogniCryptPlugin extends BackgroundComponent
{
    private final CrySLExtractor _extractor;
    private final IPersistableCogniCryptSettings _settings;

    private CogniCryptPlugin(CrySLExtractor extractor, IPersistableCogniCryptSettings settings)
    {
        _extractor = extractor;
        _settings = settings;
    }

    @Override
    protected void InitializeInBackground(ProgressIndicator indicator)
    {
        _extractor.ExtractIfRequired();
        String path = _extractor.GetDefaultCrySLPath(CrySLExtractor.RulesTarget.JCA);
        if (_settings.getRulesDirectory().equals(Constants.DummyCrySLPath) &&
                !path.equals(Constants.DummyCrySLPath))
            _settings.setRulesDirectory(path);
    }
}
