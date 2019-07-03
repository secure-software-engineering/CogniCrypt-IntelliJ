package de.fraunhofer.iem.icognicrypt.results;

import com.intellij.ui.table.JBTable;
import de.fraunhofer.iem.icognicrypt.core.ui.HorizontalAlignmentCellRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.Collections;

public class ErrorTable extends JBTable
{
    private static final long serialVersionUID = 1L;

    public ErrorTable()
    {
        ErrorTableModel model = new ErrorTableModel();

        TableColumnModel columnModel = new ErrorTableColumnModel();
        columnModel.setColumnSelectionAllowed(false);

        setColumnModel(columnModel);
        setModel(model);
        setRowSelectionAllowed(true);

        for (TableColumn column : Collections.list(getColumnModel().getColumns()))
        {
            Object t = ErrorTableModel.ErrorTableColumn.valueOf(column.getModelIndex());
            column.setIdentifier(t);

            if (t != ErrorTableModel.ErrorTableColumn.Description)
                column.sizeWidthToFit();

            column.setWidth(column.getWidth() + 10);
            column.setHeaderRenderer(new HorizontalAlignmentCellRenderer(SwingConstants.LEFT, new EmptyBorder(0,6,0,0)));
        }

        TableColumn emptyColumn = getColumn(ErrorTableModel.ErrorTableColumn.EmptyColumn);
        emptyColumn.setMaxWidth(20);
        emptyColumn.setResizable(false);

        TableColumn severityColumn = getColumn(ErrorTableModel.ErrorTableColumn.Severity);
        severityColumn.setMaxWidth(20);
        severityColumn.setResizable(false);
    }

    public ErrorTableModel GetErrorTableModel()
    {
        return (ErrorTableModel) getModel();
    }
}
