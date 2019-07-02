package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptWindowBase;

import javax.swing.*;

public class CogniCryptErrorWindow extends CogniCryptWindowBase
{
    private JPanel _content;
    private JTextField _textField1;
    private JTable _errorTable;

    public CogniCryptErrorWindow(ToolWindow toolWindow)
    {
        super(toolWindow);


        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };

    }

    @Override
    public JPanel GetContent(){
        return _content;
    }

    public void SetText(String text){
        _textField1.setText(text);
    }
}
