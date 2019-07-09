package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.exceptions.CogniCryptException;

public interface ICogniCryptToolWindowManager
{
    void RegisterToolWindow(ToolWindow window);

    void RegisterModel(CogniCryptToolWindowManagerEx.ToolWindowModelType type, ICogniCryptWindowBase model) throws CogniCryptException;

    <T extends ICogniCryptWindowBase> T GetModel(CogniCryptToolWindowManagerEx.ToolWindowModelType model, Class<T> type) throws CogniCryptException;

    ICogniCryptWindowBase GetModel(CogniCryptToolWindowManagerEx.ToolWindowModelType type) throws CogniCryptException;
}
