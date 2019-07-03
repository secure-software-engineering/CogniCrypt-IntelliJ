package de.fraunhofer.iem.icognicrypt.results;

import javax.swing.table.AbstractTableModel;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ResultTableModel extends AbstractTableModel implements Serializable
{
    public enum ResultTableColumn
    {
        EmptyColumn(0),
        Severity(1),
        Id(2),
        Description(3),
        File(4),
        Line(5);

        private int value;
        private static Map map = new HashMap<>();

        ResultTableColumn(int value) {
            this.value = value;
        }

        static {
            for (ResultTableColumn pageType : ResultTableColumn.values()) {
                map.put(pageType.value, pageType);
            }
        }

        public static ResultTableColumn valueOf(int pageType) {
            return (ResultTableColumn) map.get(pageType);
        }

        public int getValue() {
            return value;
        }
    }

    private final Vector<ResultTableColumn> _columns = new Vector<>();
    private final Vector<CogniCryptError> _errors = new Vector<>();

    public ResultTableModel()
    {
        _columns.addAll(Arrays.asList(ResultTableColumn.values()));
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount()
    {
        return _errors.size();
    }

    @Override
    public int getColumnCount()
    {
        return _columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (columnIndex == 0)
            return null;

        CogniCryptError error = _errors.elementAt(rowIndex);

        switch (columnIndex){

            case 2:
                return "ID";
            case 3:
                return error.getErrorMessage();
            case 4:
                return error.getClassName();
            case 5:
                return "Line";
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column)
    {
        ResultTableColumn data = _columns.get(column);

        switch (data)
        {
            case EmptyColumn:
            case Severity:
                return "";
            default:
                return (data == null) ? super.getColumnName(column)
                                      : data.toString();
        }
    }

    public void AddError(CogniCryptError error)
    {
        insertRow(getRowCount(), error);
    }

    private void insertRow(int row, CogniCryptError rowData) {
        _errors.insertElementAt(rowData, row);
        fireTableRowsInserted(row, row);
    }
}
