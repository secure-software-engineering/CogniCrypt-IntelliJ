package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumSet;

public interface IOutputFinder
{
    Iterable<File> GetOutputFiles();

    Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options);

    Iterable<File> GetOutputFiles(Path projectRootPath) throws CogniCryptException, IOException, OperationNotSupportedException;

    Iterable<File> GetOutputFiles(Path projectRootPath, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException;
}
