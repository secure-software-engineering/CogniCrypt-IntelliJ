package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.analysis.CompilationListener;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        logger.info("Try finding all built .apk files with options: " + options);

        if (!Files.exists(projectRootPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        GradleSettings settings = new GradleSettings(projectRootPath);

        List<File> result = new ArrayList<>();
        for (String modulePath: settings.GetModulePathsAbsolute())
        {
            try
            {
                logger.info("Processing module: "  + modulePath);

                JavaModule module = new JavaModule(modulePath);
                if (options == OutputFinderOptions.DebugOnly || options == OutputFinderOptions.AnyBuildType)
                {
                    String filePath = module.GetDebugOutputPathAbsolute();
                    if (filePath != null)
                    {
                        File file = new File(filePath);
                        if (file.exists())
                        {
                            result.add(file);
                            logger.info("Found .apk File: " + file.getCanonicalPath());
                        }
                    }
                }
                if (options == OutputFinderOptions.ReleaseOnly || options == OutputFinderOptions.AnyBuildType)
                {
                    String filePath = module.GetReleaseOutputPathAbsolute();
                    if (filePath != null)
                    {
                        File file = new File(filePath);
                        if (file.exists())
                        {
                            result.add(file);
                            logger.info("Found .apk File: " + file.getCanonicalPath());
                        }
                    }
                }
            }
            catch (JavaModuleNotFoundException e)
            {
                logger.info("Unable to find JavaModule: " + e.getMessage());
                continue;
            }
        }

        // Quick and Dirty Fallback
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
}

