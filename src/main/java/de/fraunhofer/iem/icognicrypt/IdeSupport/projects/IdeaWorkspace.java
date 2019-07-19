package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Json.OutputJson;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IHasOutputManager;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.IHasOutputs;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputFinderOptions;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs.OutputManager;
import de.fraunhofer.iem.icognicrypt.core.Xml.XmlUtilities;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumSet;

// TODO: This class is only tested for Android Studio. Validate for IntelliJ also
public class IdeaWorkspace implements IHasOutputManager
{
    private final File _xmlFile;
    private final IHasOutputs _outputManager;

    private String _apkPath;
    private boolean _debugAvailable;
    private boolean _releaseAvailable;

    @Override
    public IHasOutputs GetOutputManager()
    {
        return _outputManager;
    }

    // TODO: Ideally we want to create this once per IDE instance. Thus we need to add a FileSystemWatcher whether the file was modified or not for better caching.
    public IdeaWorkspace(File xmlFile) throws FileNotFoundException
    {
        if (xmlFile == null || !xmlFile.exists())
            throw new FileNotFoundException();
        _xmlFile = xmlFile;

        _outputManager = new IdeaWorkspaceOutputManager(this);
        Invalidate();
    }

    private void Invalidate()
    {
        _apkPath = null;
        _debugAvailable = false;
        _releaseAvailable = false;
        try
        {
            Node buildTypeNode = XmlUtilities.FindFirstNode(_xmlFile, "//component[@name='PropertiesComponent']/property[@name='ExportApk.BuildVariants']");
            if (buildTypeNode == null)
                return;
            String buildType = XmlUtilities.GetAttributeValue(buildTypeNode, "value");

            _debugAvailable = BuildTypeContains(buildType, "debug");
            _releaseAvailable = BuildTypeContains(buildType, "release");

            Node apkPathNode = XmlUtilities.FindFirstNode(_xmlFile, "//component[@name='PropertiesComponent']/property[@name='ExportApk.ApkPath']");
            if (apkPathNode == null) return;
            _apkPath = XmlUtilities.GetAttributeValue(apkPathNode, "value");

            _outputManager.InvalidateOutput();
        }
        catch (XPathExpressionException e)
        {
            return;
        }
        catch (FileNotFoundException e)
        {
            return;
        }
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

    private class IdeaWorkspaceOutputManager extends OutputManager
    {
        private final IdeaWorkspace _owner;

        public IdeaWorkspaceOutputManager(IdeaWorkspace owner)
        {
            _owner = owner;
        }

        @Override
        public void InvalidateOutput()
        {
            DebugJson = null;
            ReleaseJson = null;
            if (_owner._debugAvailable) DebugJson = FindAndParseJson("debug");
            if (_owner._releaseAvailable) ReleaseJson = FindAndParseJson("release");
        }

        @Override
        public Iterable<String> GetOutputs(EnumSet<OutputFinderOptions.Flags> options)
        {
            if (_apkPath == null || _apkPath.isEmpty())
                return Collections.EMPTY_LIST;
            if (!OutputFinderOptions.containsAny(options, OutputFinderOptions.Flags.IncludeSigned, OutputFinderOptions.Flags.SignedOnly))
                return Collections.EMPTY_LIST;
            return super.GetOutputs(options);
        }

        private OutputJson FindAndParseJson(String buildType)
        {
            if (_owner._apkPath == null || _owner._apkPath.isEmpty() || !Paths.get(_owner._apkPath).toFile().exists())
                return null;

            File jsonFile = Paths.get(_owner._apkPath, buildType, "output.json").toFile();

            if (!jsonFile.exists())
                return null;

            return OutputJson.Deserialize(jsonFile.getAbsolutePath());
        }
    }
}

