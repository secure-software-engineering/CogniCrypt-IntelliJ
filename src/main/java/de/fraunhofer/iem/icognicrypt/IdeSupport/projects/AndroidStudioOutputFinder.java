package de.fraunhofer.iem.icognicrypt.IdeSupport.projects;

import com.intellij.openapi.diagnostic.Logger;
import de.fraunhofer.iem.icognicrypt.IdeSupport.gradle.GradleSettings;
import de.fraunhofer.iem.icognicrypt.analysis.CompilationListener;
import de.fraunhofer.iem.icognicrypt.core.Xml.XmlUtilities;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;
import org.apache.xmlbeans.impl.xb.xsdschema.SelectorDocument;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.naming.OperationNotSupportedException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

        logger.info("Try finding all built .apk files.");

        if (!Files.exists(projectRootPath))
            throw new CogniCryptException("Root path of the project does not exist.");

        HashSet<File> result = new HashSet<>();

        result.addAll(GetModuleOutputs(projectRootPath, options));
        result.addAll(GetExportedOutputs(projectRootPath, options));

        return result;
    }

    private  Collection<File> GetExportedOutputs(Path projectRootPath, OutputFinderOptions options)
    {
        logger.info("Get exported .apks from workspace cache");

        File workspaceFile = Paths.get(projectRootPath.toString(), ".idea\\workspace.xml").toFile();
        try
        {
            IHasOutputs workspace = new IdeaWorkspace(workspaceFile);


            if (!workspaceFile.exists()) return Collections.EMPTY_LIST;


            Node buildInfo = XmlUtilities.FindFirstNode(workspaceFile, "//component[@name='PropertiesComponent']/property[@name='ExportApk.BuildVariants']");
            String buildType = XmlUtilities.GetAttributeValue(buildInfo, "value");
            System.out.println(buildType);
        }
        catch (FileNotFoundException e)
        {
            return Collections.EMPTY_LIST;
        }
        catch (XPathExpressionException e)
        {
            return Collections.EMPTY_LIST;
        }

        HashSet<File> result = new HashSet<>();

        return result;
    }

    private Collection<File> GetModuleOutputs(Path projectRootPath, OutputFinderOptions options) throws IOException, OperationNotSupportedException
    {
        logger.info("Get .apks from project modules");
        GradleSettings settings = new GradleSettings(projectRootPath);
        ProjectModuleManager moduleManager = new ProjectModuleManager(settings);

        HashSet<File> result = new HashSet<>();
        for (IHasOutputs module : moduleManager.GetModules())
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

