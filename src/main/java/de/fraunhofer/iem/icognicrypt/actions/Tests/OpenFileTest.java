package de.fraunhofer.iem.icognicrypt.actions.Tests;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import de.fraunhofer.iem.icognicrypt.actions.CogniCryptAction;
import de.fraunhofer.iem.icognicrypt.core.Java.JavaFileToClassNameResolver;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;

public class OpenFileTest extends CogniCryptAction
{
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        String fileName = "com.example.lrs.helloworld.MainActivity";
        //String fileName = "C:\\Users\\lrs\\Desktop\\json 1.txt";

        Project p = e.getDataContext().getData(PlatformDataKeys.PROJECT);

        String path = JavaFileToClassNameResolver.FindFileFromFullyQualifiedName(fileName, p);

        if (path == null)
            return;

        VirtualFile f = VfsUtil.findFile(Paths.get(path), false);

        FileEditorManager.getInstance(p).openFile(f, true);
    }
}
