package de.fraunhofer.iem.icognicrypt.IdeSupport.projects.Outputs;

import com.intellij.openapi.diagnostic.Logger;
import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.IdeaWorkspace;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.JavaModule;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.ProjectModuleManager;
import de.fraunhofer.iem.icognicrypt.core.Dialogs.DialogHelper;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import org.apache.commons.lang.NotImplementedException;

import javax.naming.OperationNotSupportedException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;

public class AndroidStudioOutputFinder implements IOutputFinder
{
    private static final Logger logger = Logger.getInstance(AndroidStudioOutputFinder.class);

    private static IOutputFinder _instance;

    public static IOutputFinder GetInstance()
    {
        if (_instance == null) _instance = new AndroidStudioOutputFinder();
        return _instance;
    }

    private AndroidStudioOutputFinder()
    {
    }

    public Iterable<File> GetOutputFiles()
    {
        // TODO: Add from Settings
        EnumSet<OutputFinderOptions.Flags> statusFlags = EnumSet.of(OutputFinderOptions.Flags.AnyBuild, OutputFinderOptions.Flags.IncludeSigned);
        return GetOutputFiles(statusFlags);
    }

    @Override
    public Iterable<File> GetOutputFiles(EnumSet<OutputFinderOptions.Flags> options)
    {
        throw new NotImplementedException();
    }

    // TODO: If AS supports detecting when a project is load we can omit this method as we should always read paths directly from the IDE
    public Iterable<File> GetOutputFiles(Path projectRootPath) throws CogniCryptException, IOException, OperationNotSupportedException
    {
        // TODO: Add from Settings
        EnumSet<OutputFinderOptions.Flags> statusFlags = EnumSet.of(OutputFinderOptions.Flags.AnyBuild, OutputFinderOptions.Flags.IncludeSigned);
        return GetOutputFiles(projectRootPath, statusFlags);
    }

    @Override
    public Iterable<File> GetOutputFiles(Path projectRootPath, EnumSet<OutputFinderOptions.Flags> options) throws CogniCryptException, IOException, OperationNotSupportedException
    {
        //TODO: Once we have this class created by the IDE instance (not in the CompilationListenerClass) we want to have the settings.gradle and workspace.xml models
        // as a weak class field. It should be weak so the developer can delete the files safely without causing the reference kept alive by the GC. When the weak reference is gone we
        // should check for a new file and invalidate this class aganin.

        logger.info("Try finding all built .apk files with options: " + options);

        if (!Files.exists(projectRootPath)) throw new CogniCryptException("Root path of the project does not exist.");

        HashSet<File> result = new HashSet<>();

        if (!options.isEmpty()){
            result.addAll(GetModuleOutputs(projectRootPath, options));
            result.addAll(GetExportedOutputs(projectRootPath, options));
        }

        if (result.isEmpty())
        {
            logger.info("Could not find any file. User is requested to choose one manually");
            FileFilter filter = new FileNameExtensionFilter("Android Apps", "apk");
            File userSelectedFile = DialogHelper.ChooseSingleFileFromDialog("Choose an .apk File to analyze...", filter, projectRootPath);
            if (userSelectedFile == null) logger.info("User did not select any file.");
            else
            {
                logger.info("Added manual file: " + userSelectedFile.getAbsolutePath());
                result.add(userSelectedFile);
            }
        }

        return result;
    }

    private Collection<File> GetExportedOutputs(Path projectRootPath, EnumSet<OutputFinderOptions.Flags> options) throws IOException
    {
        logger.info("Get exported .apks from workspace cache");

        File workspaceFile = Paths.get(projectRootPath.toString(), ".idea\\workspace.xml").toFile();

        try
        {
            IdeaWorkspace workspace = new IdeaWorkspace(workspaceFile);
            return GetOutputs(workspace.GetOutputManager(), options);
        }
        catch (FileNotFoundException e)
        {
            return Collections.EMPTY_LIST;
        }
    }

    private Collection<File> GetModuleOutputs(Path projectRootPath, EnumSet<OutputFinderOptions.Flags> options) throws IOException, OperationNotSupportedException
    {
        logger.info("Get .apks from project modules");
        GradleSettings settings = new GradleSettings(projectRootPath);
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

    private Collection<File> GetOutputs(IHasOutputs outputManager, EnumSet<OutputFinderOptions.Flags> options) throws IOException
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

