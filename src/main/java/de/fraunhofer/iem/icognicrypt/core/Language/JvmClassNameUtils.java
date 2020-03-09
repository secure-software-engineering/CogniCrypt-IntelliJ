package de.fraunhofer.iem.icognicrypt.core.Language;

import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import de.fraunhofer.iem.icognicrypt.core.Helpers.StringTrimming;
import de.fraunhofer.iem.icognicrypt.core.java.Lazy;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


// TODO: Optimize, optimize, optimize!!!
public class JvmClassNameUtils
{
    //private static SupportedLanguagesUtils _languagesUtils;

    private static Lazy<SupportedLanguagesUtils> _languagesUtils = new Lazy<>(() -> ServiceManager.getService(SupportedLanguagesUtils.class));

    private static SupportedLanguagesUtils LanguagesUtils = _languagesUtils.GetValue();



    //TODO: Currently we cannot find private classes or package-private classes which are in the same file and the public class.
    /*TODO: We cannot find distinguish between modules. Result: if the same Fully Qualified Name exists in two different modules we always pick the first.
        So apparently one can build a android app having two modules with the exact same id (e.g.: com.vendor.module). The app compiles and runs. I'm not sure whoever which of the
        module get used by the app when accessing to a a class with the same method. The order of the gradle.build dependencies does not seem to be relevant.
        I know that this is a totally uncommon edge case. Be we should be aware of it.
     */
    public static String FindFileFromFullyQualifiedName(String fullyQualifiedName, Project project)
    {
        String relativeContainerPath = fullyQualifiedName.split("\\$")[0];
        char separator = File.separatorChar;
        String relativePath = relativeContainerPath.replace('.',separator);
        List<VirtualFile> sourceRoots = getSourceRoots(project);

        Iterable<SupportedLanguage> supportedLanguages = LanguagesUtils.SupportedLanguages;

        for(VirtualFile m : sourceRoots)
        {
            String absolutePathWithoutFileName = Paths.get(m.getPath(), relativePath).toString();

            for (SupportedLanguage language : supportedLanguages)
            {
                String possibleAbsolutePath = PathUtil.makeFileName(absolutePathWithoutFileName, LanguagesUtils.GetFileExtension(language));
                String sourceFilePath = FindSourceFileForLanguage(possibleAbsolutePath, language);
                if (sourceFilePath != null)
                    return sourceFilePath;
            }
        }
        return null;
    }

    private static String FindSourceFileForLanguage(String absolutePath, SupportedLanguage language)
    {
        File sourceFile = new File(absolutePath);
        if (sourceFile.exists())
            return sourceFile.getAbsolutePath();
        if (SupportedLanguagesUtils.RequiresSourceFileNameModification(language))
        {
            File modifiedFile = ModifySourceFileName(sourceFile, language);
            if (modifiedFile.exists())
                return modifiedFile.getAbsolutePath();
        }
        return null;
    }

    private static File ModifySourceFileName(File file, SupportedLanguage language) {
        // TODO
        String parentPath = file.getParent();
        String fileName = FilenameUtils.removeExtension(file.toPath().getFileName().toString());
        String newFileNameWithoutExtension = ModifySourceFileName(fileName, language);
        String newFileName = PathUtil.makeFileName(newFileNameWithoutExtension, LanguagesUtils.GetFileExtension(language));
        return new File(parentPath, newFileName);
    }

    private static String ModifySourceFileName(String fileName, SupportedLanguage language) {
        switch (language)
        {
            case Kotlin:
                return StringTrimming.TrimEnd(fileName, "Kt");
            default:
                return fileName;
        }
    }


    //TODO: Currently we cannot find private classes not package-private classes which are in the same file and the public class.
    public static List<String> findFullyQualifiedClassNames(Project project)
    {
        List<String> results = Lists.newArrayList();
        List<VirtualFile> sourceRoots = getSourceRoots(project);
        SupportedLanguagesUtils languagesUtils = ServiceManager.getService(SupportedLanguagesUtils.class);

        for (VirtualFile root : sourceRoots)
        {
            String path = root.getPath();
            Collection<File> supportedFiles = languagesUtils.GetSupportedSourceFiles(root);
            results.addAll(supportedFiles.stream().map(f -> convertToFullyQualifiedClassName(f, path)).distinct().collect(Collectors.toList()));
        }
        return results;
    }

    public static List<String> findFullyQualifiedClassNames(Module module)
    {
        List<String> results = Lists.newArrayList();
        List<VirtualFile> sourceRoots = getSourceRoots(module);
        SupportedLanguagesUtils languagesUtils = ServiceManager.getService(SupportedLanguagesUtils.class);

        for (VirtualFile root : sourceRoots)
        {
            String path = root.getPath();
            Collection<File> supportedFiles = languagesUtils.GetSupportedSourceFiles(root);
            results.addAll(supportedFiles.stream().map(f -> convertToFullyQualifiedClassName(f, path)).distinct().collect(Collectors.toList()));
        }
        return results;
    }

    private static String convertToFullyQualifiedClassName(File javaFile, String sourceCodeBasePath) {
        String withoutFileEnding = FilenameUtils.removeExtension(javaFile.getAbsolutePath());

        if (SystemUtils.IS_OS_WINDOWS){
            withoutFileEnding = withoutFileEnding.replace(File.separator,"/");
        }

        String stripBasePath = withoutFileEnding.replace(sourceCodeBasePath,"");
        String slashesToDots = stripBasePath.replace("/",".");
        return slashesToDots.replaceFirst(".","");
    }

    private static List<VirtualFile> getSourceRoots(Project project) {
        return getSourceRoots(project, false, false);
    }

    private static List<VirtualFile> getSourceRoots(Module module) {
        return getSourceRoots(module, false, false);
    }

    private static List<VirtualFile> getSourceRoots(Project project, boolean includeTests, boolean includeGeneratedCode)
    {
        List<VirtualFile> res = Lists.newArrayList();
        for (Module m : ModuleManager.getInstance(project).getModules())
        {
            res.addAll(getSourceRoots(m, includeTests, includeGeneratedCode));
        }
        return res;
    }

    private static List<VirtualFile> getSourceRoots(Module module, boolean includeTests, boolean includeGeneratedCode)
    {
        List<VirtualFile> res = Lists.newArrayList();
        ModuleRootManager mgr = ModuleRootManager.getInstance(module);
        for (VirtualFile file : mgr.getSourceRoots(includeTests))
        {
            boolean pathFromGenerated = file.getPath().contains("build/generated");

            if (!pathFromGenerated || (pathFromGenerated && includeGeneratedCode))
                res.add(file);
        }
        return res;
    }
}
