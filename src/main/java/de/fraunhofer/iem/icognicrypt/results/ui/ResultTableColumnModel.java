package de.fraunhofer.iem.icognicrypt.results.ui;

import javax.swing.table.DefaultTableColumnModel;

class ResultTableColumnModel extends DefaultTableColumnModel
{
    @Override
    public void moveColumn(int columnIndex, int newIndex)
    {
        if (columnIndex != 0 && newIndex != 00)
            super.moveColumn(columnIndex, newIndex);
    }
}
