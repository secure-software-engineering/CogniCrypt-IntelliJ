package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface IOutputFinder
{
    public Iterable<File> GetOutputFiles();

    public Iterable<File> GetOutputFiles(OutputFinderOptions options);

    public Iterable<File> GetOutputFiles(Path projectRootPath) throws CogniCryptException, IOException, OperationNotSupportedException;

    public Iterable<File> GetOutputFiles(Path projectRootPath, OutputFinderOptions options) throws CogniCryptException, IOException, OperationNotSupportedException;
}
