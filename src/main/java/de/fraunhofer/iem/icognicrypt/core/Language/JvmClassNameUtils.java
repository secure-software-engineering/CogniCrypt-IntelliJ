package de.fraunhofer.iem.icognicrypt.core.Language;

import com.google.common.collect.Lists;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.ex.VirtualFileManagerEx;
import com.intellij.util.PathUtil;
import com.intellij.util.io.fs.FilePath;
import org.apache.commons.io.FileUtils;
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

        SupportedLanguagesUtils languagesUtils = ServiceManager.getService(SupportedLanguagesUtils.class);
        Iterable<SupportedLanguage> supportedLanguages = languagesUtils.SupportedLanguages;

        for(VirtualFile m : sourceRoots)
        {
            String absolutePathWithoutFileName = Paths.get(m.getPath(), relativePath).toString();

            for (SupportedLanguage language : supportedLanguages)
            {
                String absolutePath = PathUtil.makeFileName(absolutePathWithoutFileName, languagesUtils.GetFileExtension(language));
                File sourceFile = new File(absolutePath);
                if (sourceFile.exists())
                    return sourceFile.getAbsolutePath();
            }
        }
        return null;
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

    private static List<VirtualFile> getSourceRoots(Project project, boolean includeTests, boolean includeGeneratedCode)
    {
        List<VirtualFile> res = Lists.newArrayList();
        for (Module m : ModuleManager.getInstance(project).getModules())
        {
            ModuleRootManager mgr = ModuleRootManager.getInstance(m);
            for (VirtualFile file : mgr.getSourceRoots(includeTests))
            {
                boolean pathFromGenerated = file.getPath().contains("build/generated");

                if (!pathFromGenerated || (pathFromGenerated && includeGeneratedCode))
                    res.add(file);
            }
        }
        return res;
    }
}
