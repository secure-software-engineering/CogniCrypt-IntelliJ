package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

public interface IHasOutputs
{
    void InvalidateOutput();

    Iterable<String> GetOutputs(OutputFinderOptions options);

    String GetDebugOutputPath();

    String GetReleaseOutputPath();
}