package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import java.io.File;
import java.nio.file.Path;

public interface IOutputFinder
{
    public Iterable<File> GetOutputFiles();

    public Iterable<File> GetOutputFiles(Path projectRootPath) throws CogniCryptException;
}
