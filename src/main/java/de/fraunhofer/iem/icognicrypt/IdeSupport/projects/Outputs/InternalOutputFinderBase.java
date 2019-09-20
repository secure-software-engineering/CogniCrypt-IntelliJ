package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import de.fraunhofer.iem.icognicrypt.settings.IPersistableCogniCryptSettings;
import org.jetbrains.annotations.NotNull;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

abstract class InternalOutputFinderBase implements IOutputFinderInternal
{
    private final IPersistableCogniCryptSettings _settings;
    private static final Logger logger = Logger.getInstance(InternalOutputFinderBase.class);

    protected InternalOutputFinderBase()
    {
        _settings = ServiceManager.getService(IPersistableCogniCryptSettings.class);
    }

    @NotNull
    @Override
    public final Iterable<File> GetOutputFiles(Project project)
            throws OperationNotSupportedException, IOException, CogniCryptException
    {
        return GetOutputFiles(project, _settings.GetFindOutputOptions());
    }

    @NotNull
    @Override
    public final Iterable<File> GetOutputFiles(Project project, Set<OutputFinderOptions.Flags> options)
            throws CogniCryptException, IOException, OperationNotSupportedException
    {
        return GetOutputFilesInternal(project, options);
    }

    @NotNull
    @Override
    public final Iterable<File> GetOutputFilesFromDialog(Project project)
    {
        return GetOutputFilesFromDialogInternal(project);
    }

    protected abstract Collection<File> GetModuleOutputs(Project project, Set<OutputFinderOptions.Flags> options) throws IOException, OperationNotSupportedException;

    protected abstract Collection<File> GetExportedOutputs(Project project, Set<OutputFinderOptions.Flags> options) throws IOException;

    protected abstract Collection<File> GetRunConfigurationOutput(Project project, Set<OutputFinderOptions.Flags> options) throws IOException, OperationNotSupportedException;;

    protected abstract Iterable<File> GetOutputFilesFromDialogInternal(Project project);

    @NotNull
    protected final Iterable<File> GetOutputFilesInternal(Project project, Set<OutputFinderOptions.Flags> options)
            throws CogniCryptException, IOException, OperationNotSupportedException
    {
        logger.info("Try finding all built .apk files with options: " + options);

        Path projectPath = Paths.get(project.getBasePath());

        if (!Files.exists(projectPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        if (!ValidateProject(project))
            throw new CogniCryptException("The project is not valid or supported by this IDE");

        HashSet<File> result = new HashSet<>();

        if (!options.isEmpty())
        {
            result.addAll(GetRunConfigurationOutput(project, options));
            result.addAll(GetModuleOutputs(project, options));
            result.addAll(GetExportedOutputs(project, options));
        }

        if (result.isEmpty())
            logger.info("Could not find any file");
        return result;
    }

    protected boolean ValidateProject(Project project)
    {
        return true;
    }
}
