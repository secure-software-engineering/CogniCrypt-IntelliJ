package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

public interface IHasOutputs
{
    void InvalidateOutput();

    Iterable<String> GetOutputs(OutputFinderOptions options);

    String GetDebugOutputPath();

    String GetReleaseOutputPath();
}
