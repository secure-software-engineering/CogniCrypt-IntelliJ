package de.fraunhofer.iem.icognicrypt.results.ui;

import de.fraunhofer.iem.icognicrypt.results.CogniCryptError;
import de.fraunhofer.iem.icognicrypt.results.ICogniCryptResultTableModel;
import de.fraunhofer.iem.icognicrypt.results.IResultsProviderListener;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

class ResultTableModel extends AbstractTableModel implements ICogniCryptResultTableModel, IResultsProviderListener
{
    private static final int EmptyColumnIndex = 0;
    private static final int SeverityColumnIndex = 1;
    private static final int IdColumnIndex  = 0; //2;
    private static final int DescriptionColumnIndex = 1; //3;
    private static final int FileColumnIndex = 2; //4;
    private static final int LineColumnIndex = 3; // 5;

    private final Vector<ResultTableColumn> _columns = new Vector<>();
    private final Vector<CogniCryptError> _results = new Vector<>();

    public ResultTableModel()
    {
        _columns.addAll(Arrays.asList(ResultTableColumn.values()));
        fireTableStructureChanged();
    }

    @Override
    public int getRowCount()
    {
        return _results.size();
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

        CogniCryptError error = _results.elementAt(rowIndex);

        switch (columnIndex){

            case IdColumnIndex:
                return "ID";
            case DescriptionColumnIndex:
                return error.getErrorMessage();
            case FileColumnIndex:
                return error.getClassName();
            case LineColumnIndex:
                return error.getLine();
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
//            case EmptyColumn:
//            case Severity:
//                return "";
            default:
                return (data == null) ? super.getColumnName(column)
                                      : data.toString();
        }
    }

    public void AddError(CogniCryptError error)
    {
        insertRow(getRowCount(), error);
    }

    @Override
    public CogniCryptError GetResultAt(int row)
    {
        if (_results.size() < 0 && (row < 0 || row > _results.size()))
            return null;
        return _results.elementAt(row);
    }

    @Override
    public void ClearErrors()
    {
        for (int i = getRowCount() - 1; i >= 0; i--) {
            RemoveRow(i);
        }
    }

    private void insertRow(int row, CogniCryptError rowData) {
        _results.insertElementAt(rowData, row);
        fireTableRowsInserted(row, row);
    }

    private void RemoveRow(int row){
        try
        {
            _results.removeElementAt(row);
            fireTableRowsDeleted(row, row);
        }
        catch (IndexOutOfBoundsException e){

        }
    }

    @Override
    public void OnResultAdded()
    {

    }

    @Override
    public void OnResultRemoved()
    {

    }

    @Override
    public void OnResultsCleared()
    {

    }

    public enum ResultTableColumn
    {
//        EmptyColumn(EmptyColumnIndex),
//        Severity(SeverityColumnIndex),
        Id(IdColumnIndex),
        Description(DescriptionColumnIndex),
        File(FileColumnIndex),
        Line(LineColumnIndex);

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
}

