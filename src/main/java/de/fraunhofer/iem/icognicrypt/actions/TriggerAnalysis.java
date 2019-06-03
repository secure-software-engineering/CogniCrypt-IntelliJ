package de.fraunhofer.iem.icognicrypt.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataConstants;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import de.fraunhofer.iem.icognicrypt.Constants;
import de.fraunhofer.iem.icognicrypt.analysis.CompilationListener;
import org.jetbrains.annotations.NotNull;

public class TriggerAnalysis extends AnAction {

    public TriggerAnalysis() {
        super("Text _Boxes","Item description", IconLoader.getIcon("/icons/cognicrypt.png"));
    }
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project p = e.getDataContext().getData(PlatformDataKeys.PROJECT);
        CompilationListener.startAnalyser(Constants.IDE_ANDROID_STUDIO,p);
    }
}
