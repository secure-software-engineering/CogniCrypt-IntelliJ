package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.CogniCryptProjectManager;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResultsMouseHandler extends MouseAdapter
{
    @Override
    public void mouseClicked(MouseEvent e)
    {
        JTable table =(JTable) e.getSource();
        int selectedRow = table.getSelectedRow();
        if (e.getClickCount() == 2 && selectedRow != -1) {
            HandleDoubleClick(e, table, selectedRow);
        }
    }

    private void HandleDoubleClick(MouseEvent e, JTable table, int row){
        ICogniCryptResultTableModel model = (ICogniCryptResultTableModel) table.getModel();
        if (model == null)
            return;
        CogniCryptError error = model.GetResultAt(row);
        Project project = CogniCryptProjectManager.GetProjectFromComponent(table);
        System.out.println(project);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}
