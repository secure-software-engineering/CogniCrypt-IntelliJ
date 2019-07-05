package de.fraunhofer.iem.icognicrypt.core.Java;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;


// TODO: Optimize, optimize, optimize!!!
public class JavaFileToClassNameResolver
{

    //TODO: Currently we cannot find private classes or package-private classes which are in the same file and the public class.
    /*TODO: We cannot find distinguish between modules. Result: if the same Fully Qualified Name exists in two different modules we always pick the first.
        So apparently one can build a android app having two modules with the exact same id (e.g.: com.vendor.module). The app compiles and runs. I'm not sure whoever which of the
        module get used by the app when accessing to a a class with the same method. The order of the gradle.build dependencies does not seem to be relevant.
        I know that this is a totally uncommon edge case. Be we should be aware of it.
     */
    public static String FindFileFromFullyQualifiedName(String fullyQualifiedName, Project project)
    {
        String relativePath = fullyQualifiedName.replace(".","\\") + ".java";

        List<VirtualFile> sourceRoots = getSourceRoots(project);

        for(VirtualFile m : sourceRoots)
        {
            File javaFile = Paths.get(m.getPath(), relativePath).toFile();
            if (javaFile.exists())
                return javaFile.getAbsolutePath();
        }
        return null;
    }

    //TODO: Currently we cannot find private classes not package-private classes which are in the same file and the public class.
    public static List<String> findFullyQualifiedJavaClassNames(Project project)
    {
        List<String> results = Lists.newArrayList();
        List<VirtualFile> sourceRoots = getSourceRoots(project);
        for(VirtualFile m : sourceRoots){
            results.addAll(FileUtils.listFiles(new File(m.getPath()), new String[]{"java"}, true).stream().map(f -> convertToFullyQualifiedClassName(f, m.getPath())
            ).distinct().collect(Collectors.toList()));
        }
        return results;
    }

    private static String convertToFullyQualifiedClassName(File javaFile, String sourceCodeBasePath) {
        String withoutFileending = javaFile.getAbsolutePath().replace(".java","");
        String replaceWindowsStrings = withoutFileending.replace("\\","/");
        String stripBasePath = replaceWindowsStrings.replace(sourceCodeBasePath,"");
        String slashesToDots = stripBasePath.replace("/",".");
        return slashesToDots.replaceFirst(".","");
    }
    private static List<VirtualFile> getSourceRoots(Project project) {
        List<VirtualFile> res = Lists.newArrayList();
        for(Module m : ModuleManager.getInstance(project).getModules()){
            ModuleRootManager mgr = ModuleRootManager.getInstance(m);
            for(VirtualFile f : mgr.getSourceRoots(false)) {
                res.add(f);
            }
        }
        return res;
    }
}
