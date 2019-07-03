package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.ui.CogniCryptWindowBase;

import javax.swing.*;

public class CogniCryptResultWindow extends CogniCryptWindowBase
{
    private JPanel _content;
    private CogniCryptResultTable _resultTable;
    private JComboBox _scopeComboBox;
    private JLabel _errorNumberLabel;

    private ResultTableModel _tableModel;

    public CogniCryptResultWindow(ToolWindow toolWindow)
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
        _resultTable = new CogniCryptResultTable();
        _tableModel = _resultTable.GetErrorTableModel();

        _tableModel.AddError(new CogniCryptError("123", "123"));
    }
}

