/*
 * Object-Oriented Analysis and Design
 *
 * Copyright (c) 2023 AIG team, Institute for Software Technology,
 * Graz University of Technology, Austria
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.dell.view.model;

import at.tugraz.ist.ase.dell.view.ButtonColumn;

import javax.swing.table.AbstractTableModel;

public class TableViewModel extends AbstractTableModel {
    private final String[] columnNames;
    private final Object[][] data;

    public TableViewModel(String[] columnNames, Object[][] data) {
        super();

        this.columnNames = columnNames;
        this.data = data;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class<?> getColumnClass(int c) {
        if (c == columnNames.length - 1) {
            return ButtonColumn.class;
        }
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == columnNames.length - 1;
    }
}
