package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import de.fraunhofer.iem.icognicrypt.IdeSupport.projects.ProjectHelper;
import de.fraunhofer.iem.icognicrypt.core.Language.JvmClassNameUtils;
import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Paths;

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
        Project project = ProjectHelper.GetProjectFromComponent(table);

        File sourceFile = CogniCryptError.getSourceFileOfError(error, project);
        VirtualFile f = VfsUtil.findFileByIoFile(sourceFile, false);
        FileEditor fileEditor = FileEditorManager.getInstance(project).openFile(f, true)[0];
        if (fileEditor == null)
            return;

        Editor editorModel = ((TextEditor) fileEditor).getEditor();
        int line = error.getLine() -1;
        editorModel.getCaretModel().moveToLogicalPosition(new LogicalPosition(line, 0));
        editorModel.getScrollingModel().scrollToCaret(ScrollType.CENTER);
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
    }
}
