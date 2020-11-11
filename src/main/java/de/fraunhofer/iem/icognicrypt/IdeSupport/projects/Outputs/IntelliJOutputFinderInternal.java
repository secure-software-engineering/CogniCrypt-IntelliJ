package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.project.Project;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

class IntelliJOutputFinderInternal extends InternalOutputFinderBase
{
    @Override
    protected Collection<File> GetModuleOutputs(Project project, Set<OutputFinderOptions.Flags> options)
            throws IOException, OperationNotSupportedException
    {
        return null;
    }

    @Override
    protected Collection<File> GetExportedOutputs(Project project, Set<OutputFinderOptions.Flags> options)
            throws IOException
    {
        return null;
    }

    @Override
    protected Collection<File> GetRunConfigurationOutput(Project project, Set<OutputFinderOptions.Flags> options)
            throws IOException, OperationNotSupportedException
    {
        return null;
    }

    @Override
    protected Iterable<File> GetOutputFilesFromDialogInternal(Project project)
    {
        return null;
    }
}
