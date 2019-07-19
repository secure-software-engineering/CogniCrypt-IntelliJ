package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;

public class IntelliJPlatformOutputFinderWrapper implements IOutputFinder
{
    private final IOutputFinder _serviceProvider;

    private IntelliJPlatformOutputFinderWrapper()
    {
        _serviceProvider = null;
    }

    @Override
    public Iterable<File> GetOutputFiles()
    {
        return _serviceProvider.GetOutputFiles();
    }

    @Override
    public Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options)
    {
        return _serviceProvider.GetOutputFiles(options);
    }

    @Override
    public Iterable<File> GetOutputFiles(Path projectRootPath, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException
    {
        return _serviceProvider.GetOutputFiles(projectRootPath, options);
    }
}
