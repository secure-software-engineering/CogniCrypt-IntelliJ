package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptWindowBase;

import javax.swing.*;

public class CogniCryptErrorWindow extends CogniCryptWindowBase
{
    private JPanel _content;
    private JTextField _textField1;
    private JTable table1;

    public CogniCryptErrorWindow(ToolWindow toolWindow)
    {
        super(toolWindow);
    }

    @Override
    public JPanel GetContent(){
        return _content;
    }

    public void SetText(String text){
        _textField1.setText(text);
    }
}
