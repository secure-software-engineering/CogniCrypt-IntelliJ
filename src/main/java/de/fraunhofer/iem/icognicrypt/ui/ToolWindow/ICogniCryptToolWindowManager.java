package de.fraunhofer.iem.icognicrypt.ui.ToolWindow;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

public interface ICogniCryptToolWindowManager
{
    void RegisterToolWindow(ToolWindow window);

    void RegisterModel(ToolWindowModelType type, ICogniCryptWindowBase model) throws CogniCryptException;

    <T extends ICogniCryptWindowBase> T GetModel(ToolWindowModelType model, Class<T> type) throws CogniCryptException;

    ICogniCryptWindowBase GetModel(ToolWindowModelType type) throws CogniCryptException;
}
