package de.fraunhofer.iem.icognicrypt.core.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HorizontalAlignmentCellRenderer implements TableCellRenderer
{
    private int horizontalAlignment;
    private Border _margin = new EmptyBorder(0, 0, 0, 0);


    public HorizontalAlignmentCellRenderer(int horizontalAlignment)
    {
        this.horizontalAlignment = horizontalAlignment;
    }

    public HorizontalAlignmentCellRenderer(int horizontalAlignment, Border margin)
    {
        this(horizontalAlignment);
        _margin = margin;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        TableCellRenderer r = table.getTableHeader().getDefaultRenderer();
        JLabel label = (JLabel) r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        Border border = label.getBorder();
        label.setBorder(new CompoundBorder(border, _margin));
        label.setHorizontalAlignment(horizontalAlignment);
        return label;
    }
}
