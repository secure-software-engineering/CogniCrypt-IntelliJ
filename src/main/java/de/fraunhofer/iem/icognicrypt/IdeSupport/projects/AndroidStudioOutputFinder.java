package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AndroidStudioOutputFinder implements IOutputFinder
{
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
        if (!Files.exists(projectRootPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        GradleSettings settings = new GradleSettings(projectRootPath);

        List<File> result = new ArrayList<>();
        for (String modulePath: settings.GetModulePathsAbsolute())
        {
            try
            {
                JavaModule module = new JavaModule(modulePath);
                if (options == OutputFinderOptions.DebugOnly || options == OutputFinderOptions.AnyBuildType)
                {
                    String filePath = module.GetDebugOutputPathAbsolute();
                    if (filePath != null)
                    {
                        File file = new File(filePath);
                        if (file.exists())
                            result.add(file);
                    }
                }
                if (options == OutputFinderOptions.ReleaseOnly || options == OutputFinderOptions.AnyBuildType)
                {
                    String filePath = module.GetReleaseOutputPathAbsolute();
                    if (filePath != null)
                    {
                        File file = new File(filePath);
                        if (file.exists())
                            result.add(file);
                    }
                }
            }
            catch (JavaModuleNotFoundException e)
            {
                continue;
            }
        }
        return result;
    }
}

