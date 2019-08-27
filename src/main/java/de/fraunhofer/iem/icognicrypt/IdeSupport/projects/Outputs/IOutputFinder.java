package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.io.File;
import java.util.EnumSet;

public interface IOutputFinder
{
    Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options);
}
