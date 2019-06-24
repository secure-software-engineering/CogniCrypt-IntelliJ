package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.diagnostic.Logger;
import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.analysis.CompilationListener;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
        logger.info("Try finding all built .apk files.");

        if (!Files.exists(projectRootPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        HashSet<File> result = new HashSet<>();

        result.addAll(GetModuleOutputs(projectRootPath, options));

        return result;
    }

    private Collection<File> GetModuleOutputs(Path projectRootPath, OutputFinderOptions options) throws IOException, OperationNotSupportedException
    {
        GradleSettings settings = new GradleSettings(projectRootPath);
        ProjectModuleManager moduleManager = new ProjectModuleManager(settings);

        List<File> result = new ArrayList<>();
        for (JavaModule module : moduleManager.GetModules())
        {
            for (String output : module.GetOutputs(options))
            {
                File file = new File(output);
                if (file.exists())
                {
                    result.add(file);
                    logger.info("Found .apk File: " + file.getCanonicalPath());
                }
            }
        }
        return result;
    }
}

