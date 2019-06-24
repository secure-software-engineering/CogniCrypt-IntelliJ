package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;


import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import java.io.File;
import java.io.FileNotFoundException;

// TODO: This class is only tested for Android Studio. Validate for IntelliJ also
public class IdeaWorkspace implements IHasOutputs
{
    private final File _xmlFile;

    // TODO: Ideally we want to create this once per IDE instance. Thus we need to add a FileSystemWatcher whether the file was modified or not for better caching.
    public IdeaWorkspace(File xmlFile) throws FileNotFoundException
    {
        if (xmlFile == null || !xmlFile.exists())
            throw new FileNotFoundException();
        _xmlFile = xmlFile;
    }


    @Override
    public void InvalidateOutput()
    {

    }

    @Override
    public Iterable<String> GetOutputs(OutputFinderOptions options)
    {
        return null;
    }

    @Override
    public String GetDebugOutputPath()
    {
        return null;
    }

    @Override
    public String GetDebugOutputPathAbsolute()
    {
        return null;
    }

    @Override
    public String GetReleaseOutputPath()
    {
        return null;
    }

    @Override
    public String GetReleaseOutputPathAbsolute()
    {
        return null;
    }
}
