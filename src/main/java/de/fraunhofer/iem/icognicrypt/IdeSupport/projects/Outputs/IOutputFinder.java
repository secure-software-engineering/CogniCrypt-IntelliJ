package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public interface IOutputFinder
{
    @NotNull
    Iterable<File> GetOutputFiles(Set<OutputFinderOptions.Flags> options);
}
