package de.fraunhofer.iem.icognicrypt.results;

import javax.swing.table.DefaultTableColumnModel;

public class ErrorTableColumnModel extends DefaultTableColumnModel
{
    @Override
    public void moveColumn(int columnIndex, int newIndex)
    {
        if (columnIndex != 0 && newIndex != 00)
            super.moveColumn(columnIndex, newIndex);
    }
}
