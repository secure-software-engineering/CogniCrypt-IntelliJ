package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptWindowBase;
import groovy.lang.Tuple;

import javax.swing.*;
import java.util.Dictionary;

public class CogniCryptErrorWindow extends CogniCryptWindowBase
{
    private JPanel _content;
    private JTextField _textField1;

    public CogniCryptErrorWindow(ToolWindow toolWindow)
    {
        super(toolWindow);
        System.out.println("Error Window Thread: " + Thread.currentThread().getId());
    }

    @Override
    public JPanel GetContent(){
        return _content;
    }

    public void SetText(String text){
        _textField1.setText(text);
    }
}
