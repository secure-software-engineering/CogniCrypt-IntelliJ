package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptWindowBase;

import javax.swing.*;

public class CogniCryptErrorWindow extends CogniCryptWindowBase
{
    private JPanel _content;
    private ErrorTable _errorTable;
    private JComboBox _scopeComboBox;
    private JLabel _errorNumberLabel;

    private ErrorTableModel _tableModel;

    public CogniCryptErrorWindow(ToolWindow toolWindow)
    {
        super(toolWindow);
    }

    @Override
    public JPanel GetContent()
    {
        return _content;
    }

    public void SetSearchText(String text)
    {

    }

    private void createUIComponents(){
        _errorTable = new ErrorTable();
        _tableModel = _errorTable.GetErrorTableModel();

        _tableModel.AddError(new CogniCryptError("123", "123"));
    }
}

