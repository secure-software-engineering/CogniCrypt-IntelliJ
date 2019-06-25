package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;
import de.fraunhofer.iem.icognicrypt.core.Xml.XmlUtilities;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;

// TODO: This class is only tested for Android Studio. Validate for IntelliJ also
public class IdeaWorkspace implements IHasOutputs
{
    private final File _xmlFile;

    private String _apkPath;
    private OutputJson _debugJson;
    private OutputJson _releaseJson;

    // TODO: Ideally we want to create this once per IDE instance. Thus we need to add a FileSystemWatcher whether the file was modified or not for better caching.
    public IdeaWorkspace(File xmlFile) throws FileNotFoundException
    {
        if (xmlFile == null || !xmlFile.exists())
            throw new FileNotFoundException();
        _xmlFile = xmlFile;
        InvalidateOutput();
    }


    @Override
    public void InvalidateOutput()
    {
        _debugJson = null;
        _releaseJson = null;
        _apkPath = null;

        try
        {
            Node buildTypeNode = XmlUtilities.FindFirstNode(_xmlFile, "//component[@name='PropertiesComponent']/property[@name='ExportApk.BuildVariants']");
            if (buildTypeNode == null) return;
            String buildType = XmlUtilities.GetAttributeValue(buildTypeNode, "value");

            boolean checkDebug = BuildTypeContains(buildType, "debug");
            boolean checkRelease = BuildTypeContains(buildType, "release");

            Node apkPathNode = XmlUtilities.FindFirstNode(_xmlFile, "//component[@name='PropertiesComponent']/property[@name='ExportApk.ApkPath']");
            if (apkPathNode == null) return;
            _apkPath = XmlUtilities.GetAttributeValue(apkPathNode, "value");

            if (checkDebug)
                _debugJson = FindAndParseJson("debug");

            if (checkRelease)
                _releaseJson = FindAndParseJson("release");
        }
        catch (XPathExpressionException e)
        {
        }
        catch (FileNotFoundException e)
        {
        }
    }

    @Override
    public Iterable<String> GetOutputs(OutputFinderOptions options)
    {
        if (_apkPath == null || _apkPath.isEmpty())
            return Collections.EMPTY_LIST;

        HashSet<String> result = new HashSet<>();

        if ((options == OutputFinderOptions.DebugOnly || options == OutputFinderOptions.AnyBuildType) && _debugJson != null)
        {
            result.add(GetDebugOutputPath());
        }

        if ((options == OutputFinderOptions.ReleaseOnly || options == OutputFinderOptions.AnyBuildType) && _releaseJson != null)
        {
            result.add(GetReleaseOutputPath());
        }
        return result;
    }

    @Override
    public String GetDebugOutputPath()
    {
        return GetOutputFilePath(_debugJson, true);
    }

    @Override
    public String GetReleaseOutputPath()
    {
        return GetOutputFilePath(_releaseJson, true);
    }

    private boolean BuildTypeContains(String buildType, String searchPattern)
    {
        if (buildType == null || buildType.isEmpty())
            return false;
        String[] types = buildType.split("\n");
        for (String type: types)
        {
            if (type.equals(searchPattern))
                return true;
        }
        return false;
    }

    private OutputJson FindAndParseJson(String buildType)
    {
        if (_apkPath == null || _apkPath.isEmpty() || !Paths.get(_apkPath).toFile().exists())
            return null;

        File jsonFile = Paths.get(_apkPath, buildType, "output.json").toFile();

        if (!jsonFile.exists())
            return null;

        return OutputJson.Deserialize(jsonFile.getAbsolutePath());
    }

    private String GetOutputFilePath(OutputJson outputJson, boolean absolute)
    {
        if (outputJson == null)
            return null;
        return outputJson.GetOutputFilePath(absolute);
    }
}
