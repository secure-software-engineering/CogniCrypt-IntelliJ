package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultWindow;

import javax.swing.*;

public class CogniCryptResultWindow implements ICogniCryptResultWindow
{
    private JPanel _content;
    private CogniCryptResultTable _resultTable;
    private JComboBox _scopeComboBox;
    private JLabel _errorNumberLabel;

    private ICogniCryptResultTableModel _tableModel;

    public CogniCryptResultWindow(ToolWindow toolWindow)
    {
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

