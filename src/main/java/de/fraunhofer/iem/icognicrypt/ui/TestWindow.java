package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class TestWindow extends CogniCryptWindowBase
{
    private JPanel _content;

    protected TestWindow(ToolWindow toolWindow)
    {
        super(toolWindow);
    }

    @Override
    public JPanel GetContent()
    {
        return _content;
    }
}
