package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultWindow;

public class CogniCryptResultWindowFactory
{
    public static ICogniCryptResultWindow CreateWindow(ToolWindow toolWindow){
        return new CogniCryptResultWindow(toolWindow);
    }
}
