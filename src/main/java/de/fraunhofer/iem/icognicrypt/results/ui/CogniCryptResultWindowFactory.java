package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultWindow;

public class CogniCryptResultWindowFactory
{
    public static ICogniCryptResultWindow CreateWindow(ToolWindow toolWindow, Project project){
        return new CogniCryptResultWindow(toolWindow, project);
    }
}
