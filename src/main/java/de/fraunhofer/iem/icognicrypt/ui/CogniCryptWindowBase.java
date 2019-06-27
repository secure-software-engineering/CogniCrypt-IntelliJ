package de.fraunhofer.iem.icognicrypt.ui;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public abstract class CogniCryptWindowBase
{
    protected CogniCryptWindowBase(ToolWindow toolWindow){

    }

    public abstract JPanel GetContent();
}
