package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultWindow;
import de.fraunhofer.iem.icognicrypt.results.IResultProvider;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.Set;

class CogniCryptResultWindow implements ICogniCryptResultWindow
{
    private JPanel _content;
    private CogniCryptResultTable _resultTable;
    private JComboBox _scopeComboBox;
    private JLabel _errorNumberLabel;

    private WeakReference<ToolWindow> _toolWindow;
    private WeakReference<Project> _project;

    private ICogniCryptResultTableModel _tableModel;

    public ICogniCryptResultTableModel GetTableModel(){
        return _tableModel;
    }

    public CogniCryptResultWindow(ToolWindow toolWindow, Project project)
    {
        _toolWindow = new WeakReference<>(toolWindow);
        _project = new WeakReference<>(project);

        IResultProvider service = ServiceManager.getService(project, IResultProvider.class);
        service.Subscribe(_tableModel);


        for (Set<CogniCryptError> errorSet : service.GetErrors().values())
        {
            for (CogniCryptError error : errorSet)
            {
                _tableModel.AddError(error);
            }
        }
    }

    @Override
    public String GetDisplayName()
    {
        return "Analysis Results";
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

