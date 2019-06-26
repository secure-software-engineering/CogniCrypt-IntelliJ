package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.diagnostic.Logger;
import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AndroidStudioOutputFinder implements IOutputFinder
{
    private static final Logger logger = Logger.getInstance(AndroidStudioOutputFinder.class);

    private static IOutputFinder _instance;

    public static IOutputFinder GetInstance(){
        if (_instance == null)
            _instance = new AndroidStudioOutputFinder();
        return _instance;
    }


    private AndroidStudioOutputFinder()
    {
    }


    public Iterable<File> GetOutputFiles(){
       return GetOutputFiles(OutputFinderOptions.AnyBuildType);
    }

    @Override
    public Iterable<File> GetOutputFiles(OutputFinderOptions options)
    {
        return null;
    }

    // TODO: If AS supports detecting when a project is load we can omit this method as we should always read paths directly from the IDE
    public Iterable<File> GetOutputFiles(Path projectRootPath) throws CogniCryptException, IOException, OperationNotSupportedException
    {
        return GetOutputFiles(projectRootPath, OutputFinderOptions.AnyBuildType);
    }

    @Override
    public Iterable<File> GetOutputFiles(Path projectRootPath, OutputFinderOptions options) throws CogniCryptException, IOException, OperationNotSupportedException
    {
        //TODO: Once we have this class created by the IDE instance (not in the CompilationListenerClass) we want to have the settings.gradle and workspace.xml models
        // as a weak class field. It should be weak so the developer can delete the files safely without causing the reference kept alive by the GC. When the weak reference is gone we
        // should check for a new file and invalidate this class aganin.

        logger.info("Try finding all built .apk files with options: " + options);

        if (!Files.exists(projectRootPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        HashSet<File> result = new HashSet<>();

        result.addAll(GetModuleOutputs(projectRootPath, options));
        result.addAll(GetExportedOutputs(projectRootPath, options));

        logger.info("Could not find any file. User is requested to choose one manually");
        if (result.isEmpty()){

            FileFilter filter = new FileNameExtensionFilter("Android Apps", "apk");

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setCurrentDirectory(projectRootPath.toFile());
            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                logger.info("Added manual file: " + selectedFile.getAbsolutePath());
                result.add(selectedFile);
            }
        }

        return result;
    }

    private  Collection<File> GetExportedOutputs(Path projectRootPath, OutputFinderOptions options) throws IOException
    {
        logger.info("Get exported .apks from workspace cache");

        File workspaceFile = Paths.get(projectRootPath.toString(), ".idea\\workspace.xml").toFile();

        IdeaWorkspace workspace;
        try
        {
            workspace = new IdeaWorkspace(workspaceFile);
        }
        catch (FileNotFoundException e)
        {
            return Collections.EMPTY_LIST;
        }

        return GetOutputs(workspace.GetOutputManager(), options);
    }

    private Collection<File> GetModuleOutputs(Path projectRootPath, OutputFinderOptions options) throws IOException, OperationNotSupportedException
    {
        logger.info("Get .apks from project modules");
        GradleSettings settings = new GradleSettings(projectRootPath);
        ProjectModuleManager moduleManager = new ProjectModuleManager(settings);

        HashSet<File> result = new HashSet<>();
        for (JavaModule module : moduleManager.GetModules())
        {
           result.addAll(GetOutputs(module.GetOutputManager(), options));
        }
        return result;
    }

    private Collection<File> GetOutputs(IHasOutputs outputManager, OutputFinderOptions options) throws IOException
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

