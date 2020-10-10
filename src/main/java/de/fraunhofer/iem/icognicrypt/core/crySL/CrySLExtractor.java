package de.fraunhofer.iem.icognicrypt.core.crySL;

import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.core.Helpers.ResourceExtractor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class CrySLExtractor extends ResourceExtractor
{
    @Override
    protected boolean CheckExtractRequired()
    {
        String extractPath = GetExtractPath();
        if (Paths.get(extractPath).toFile().exists())
            return false;
        return true;

    }

    @Override
    public void Extract()
    {
        String extractPath = GetExtractPath();
        try
        {
            unzip(Constants.ResourceZipPath, new File(extractPath));
        }
        catch (IOException e)
        {
        }
    }

    public String GetDefaultCrySLPath(RulesTarget ruleSet)
    {
        String rootPath = GetExtractPath();
        if (rootPath.equals(Constants.DummyCrySLPath))
            return Constants.DummyCrySLPath;

        // TODO: Eventually get from Json data
        String rulesPath;
        switch (ruleSet)
        {

            case JavaCryptographicArchitecture:
                rulesPath = "JavaCryptographicArchitecture";
                break;
            case BoucyCastle:
                rulesPath = "BouncyCastle";
                break;
            case Tink:
                rulesPath = "Tink";
                break;
            default:
                return Constants.DummyCrySLPath;
        }

        try
        {
            return Paths.get(rootPath, rulesPath).toFile().getCanonicalPath();
        }
        catch (IOException e)
        {
            return Constants.DummyCrySLPath;
        }
    }

    private String GetExtractPath()
    {
        Path jarPath = Paths.get(ExecutingJarPath);
        Path pluginsDirectory = jarPath.getParent().toAbsolutePath();
        try
        {
            return Paths.get(pluginsDirectory.toString(), Constants.DefaultExtractLocation).toFile().getCanonicalPath();
        }
        catch (IOException e)
        {
            return Constants.DummyCrySLPath;
        }
    }

    public enum RulesTarget{
        JavaCryptographicArchitecture,
        BoucyCastle,
        Tink
    }
}

