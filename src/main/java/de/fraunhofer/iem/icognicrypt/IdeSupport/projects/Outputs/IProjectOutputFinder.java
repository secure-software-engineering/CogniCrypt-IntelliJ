package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.io.File;

public interface IProjectOutputFinder extends IOutputFinder
{
    IOutputFinderCache GetCache();

    Iterable<File> GetOutputFiles();

    Iterable<File> GetOutputFilesFromDialog();
}


