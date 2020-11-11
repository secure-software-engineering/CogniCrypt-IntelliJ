package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectType;
import com.intellij.openapi.project.ProjectTypeService;
import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.IdeaWorkspace;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.JavaModule;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.ProjectHelper;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.ProjectModuleManager;
import de.fraunhofer.iem.icognicrypt.core.Dialogs.DialogHelper;

import javax.naming.OperationNotSupportedException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

class AndroidStudioOutputFinderInternal extends InternalOutputFinderBase
{
    private static final Logger logger = Logger.getInstance(AndroidStudioOutputFinderInternal.class);

    protected Collection<File> GetExportedOutputs(Project project, Set<OutputFinderOptions.Flags> options) throws IOException
    {
        logger.info("Get exported .apks from workspace cache");
        File workspaceFile = Paths.get(ProjectHelper.GetProjectBasePath(project).toString(), ".idea\\workspace.xml").toFile();
        try
        {
            // TODO: Try WorkspaceSettingsImpl
            IdeaWorkspace workspace = new IdeaWorkspace(workspaceFile);
            return GetOutputs(workspace.GetOutputManager(), options);
        }
        catch (FileNotFoundException e)
        {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    protected Collection<File> GetRunConfigurationOutput(Project project, Set<OutputFinderOptions.Flags> options)
            throws IOException, OperationNotSupportedException
    {
        // TODO:
        return new ArrayList<>();
    }

    protected Collection<File> GetModuleOutputs(Project project, Set<OutputFinderOptions.Flags> options) throws IOException, OperationNotSupportedException
    {
        logger.info("Get .apks from project modules");

        GradleSettings settings = new GradleSettings(ProjectHelper.GetProjectBasePath(project));
        ProjectModuleManager moduleManager = new ProjectModuleManager(settings);

        HashSet<File> result = new HashSet<>();

        if (OutputFinderOptions.contains(options, OutputFinderOptions.Flags.SignedOnly))
            return result;

        for (JavaModule module : moduleManager.GetModules())
        {
            result.addAll(GetOutputs(module.GetOutputManager(), options));
        }
        return result;
    }

    // TODO: Currently no multi selection supported
    @Override
    protected Iterable<File> GetOutputFilesFromDialogInternal(Project project){
        Collection<File> result = new ArrayList<>();
        FileFilter filter = new FileNameExtensionFilter("Android Apps", "apk");
        File userSelectedFile = DialogHelper.ChooseSingleFileFromDialog("Choose an .apk File to analyze...", filter, ProjectHelper.GetProjectBasePath(project));
        if (userSelectedFile == null)
            logger.info("User did not select any file.");
        else
        {
            logger.info("Added manual file: " + userSelectedFile.getAbsolutePath());
            result.add(userSelectedFile);
        }
        return result;
    }

    @Override
    protected boolean ValidateProject(Project project)
    {
        ProjectType projectType = ProjectTypeService.getProjectType(project);
        if (!projectType.getId().equals("Android"))
            return false;
        return true;
    }

    private Collection<File> GetOutputs(IHasOutputs outputManager, Set<OutputFinderOptions.Flags> options) throws IOException
    {
        HashSet<File> result = new HashSet<>();
        for (String output : outputManager.GetOutputs(options))
        {
            File file = new File(output);
            if (file.exists())
            {
                result.add(file);
                logger.info("Found .apk File: " + file.getCanonicalPath());
            }
        }
        return result;
    }
}

