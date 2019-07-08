package de.fraunhofer.iem.icognicrypt.results.ui;

import com.android.tools.idea.profiling.view.AnalysisResultsManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectManager;
import de.fraunhofer.iem.icognicrypt.analysis.CogniCryptAnalysisManager;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultWindow;
import de.fraunhofer.iem.icognicrypt.results.IResultProvider;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;

class CogniCryptResultWindow implements ICogniCryptResultWindow
{
    private JPanel _content;
    private CogniCryptResultTable _resultTable;
    private JComboBox _scopeComboBox;
    private JLabel _errorNumberLabel;

    private WeakReference<ToolWindow> _toolWindow;

    private ICogniCryptResultTableModel _tableModel;

    public ICogniCryptResultTableModel GetTableModel(){
        return _tableModel;
    }

    public CogniCryptResultWindow(ToolWindow toolWindow)
    {
        _toolWindow = new WeakReference<>(toolWindow);
    }

    @Override
    public JPanel GetContent()
    {
        return _content;
    }

    public void SetSearchText(String text)
    {

    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }

    private void createUIComponents(){
        _resultTable = new CogniCryptResultTable();
        _tableModel = _resultTable.GetErrorTableModel();
        _resultTable.addMouseListener(new ResultsMouseHandler());
    }
}

