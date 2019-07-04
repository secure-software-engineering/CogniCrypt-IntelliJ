package de.fraunhofer.iem.icognicrypt.results.ui;

import com.intellij.ui.table.JBTable;
import de.fraunhofer.iem.icognicrypt.core.ui.HorizontalAlignmentCellRenderer;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.MouseEvent;
import java.util.Collections;

class CogniCryptResultTable extends JBTable
{
    private static final long serialVersionUID = 1L;

    public CogniCryptResultTable()
    {
        ResultTableModel model = new ResultTableModel();

        TableColumnModel columnModel = new ResultTableColumnModel();
        columnModel.setColumnSelectionAllowed(false);

        setColumnModel(columnModel);
        setModel(model);
        setRowSelectionAllowed(true);

        for (TableColumn column : Collections.list(getColumnModel().getColumns()))
        {
            Object t = ResultTableModel.ResultTableColumn.valueOf(column.getModelIndex());
            column.setIdentifier(t);

            if (t != ResultTableModel.ResultTableColumn.Description)
                column.sizeWidthToFit();

            column.setWidth(column.getWidth() + 10);
            column.setHeaderRenderer(new HorizontalAlignmentCellRenderer(SwingConstants.LEFT, new EmptyBorder(0,6,0,0)));
        }

        TableColumn emptyColumn = getColumn(ResultTableModel.ResultTableColumn.EmptyColumn);
        emptyColumn.setMaxWidth(20);
        emptyColumn.setResizable(false);

        TableColumn severityColumn = getColumn(ResultTableModel.ResultTableColumn.Severity);
        severityColumn.setMaxWidth(20);
        severityColumn.setResizable(false);
    }

    public ICogniCryptResultTableModel GetErrorTableModel()
    {
        return (ICogniCryptResultTableModel) getModel();
    }
}
