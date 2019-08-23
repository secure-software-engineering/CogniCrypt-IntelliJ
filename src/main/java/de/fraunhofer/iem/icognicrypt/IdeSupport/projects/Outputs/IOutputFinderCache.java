package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import java.util.EnumSet;

public interface IOutputFinderCache extends IOutputFinder
{
    void Invalidate();

    void Invalidate(EnumSet<OutputFinderOptions.Flags> options);
}
