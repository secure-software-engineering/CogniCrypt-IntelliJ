package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.IdeSupport.IdeType;
import de.fraunhofer.iem.icognicrypt.analysis.CompilationListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class RunCogniCryptAction extends CogniCryptAction {

    public RunCogniCryptAction() {
        super("CogniCrypt Analysis","Run CogniCrypt Analysis", IconLoader.getIcon("/icons/cognicrypt.png"));
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project p = e.getDataContext().getData(PlatformDataKeys.PROJECT);
        CompilationListener.startAnalyser(IdeType.AndroidStudio,p);
    }
}


