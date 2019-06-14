package de.fraunhofer.iem.icognicrypt.projects;

import java.io.File;
import java.nio.file.Path;

public interface IOutputFinder
{
    public Iterable<File> GetOutputFiles();

    public Iterable<File> GetOutputFiles(Path projectRootPath);
}
