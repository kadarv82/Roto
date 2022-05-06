package Roto.Windows;

import javax.swing.table.AbstractTableModel;

import Roto.Basic.SwingConstans;

import Roto.Utils.PropertyUtil;

public class WindowTableModel extends AbstractTableModel {
    private PropertyUtil propertyUtil = PropertyUtil.getInstance();
    private SwingConstans swc = SwingConstans.getInstance();
    private String[] columnNames = {
        propertyUtil.getLangText("InternalFrame.Windows.Enabled"),
        propertyUtil.getLangText("InternalFrame.Windows.Type"), propertyUtil.getLangText("InternalFrame.Windows.Left"),
        propertyUtil.getLangText("InternalFrame.Windows.Right"), propertyUtil.getLangText("InternalFrame.Windows.Over"),
        propertyUtil.getLangText("InternalFrame.Windows.Key"), propertyUtil.getLangText("InternalFrame.Windows.Value")
    };
    private Object[][] data = new Object[swc.windowTypeArray.length][7];

    public WindowTableModel() {
        for (int i = 0; i < swc.windowTypeArray.length; i++) {
            data[i] = new Object[] {
                new Boolean(i < 7), swc.windowTypeArray[i], new Boolean(!(i == 3 || i == 7)),
                new Boolean(!(i == 3 || i == 7)), new Boolean(i == 3 || i == 7), "", ""
            };
        }
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

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col == 1 || col == 6) {
            return false;
        } else {
            return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        data[row][col] = validate(value, col);
        fireTableCellUpdated(row, col);
    }

    private Object validate(Object value, int col) {
        return value;
    }

    public Object[][] getModelArray() {
        return data;
    }

    public void setModelArray(Object[][] data) {
        for (int i = 0; i < swc.windowTypeArray.length; i++) {
            for (int j = 0; j < 7; j++) {
                Object value = data[i][j];
                setValueAt(value, i, j);
            }
        }
    }

    public Boolean isMenuEnabled(int rowIndex) {
        return getBooleanValue(getValueAt(rowIndex, 0));
    }

    public Boolean isLeftEnabled(int rowIndex) {
        return getBooleanValue(getValueAt(rowIndex, 2));
    }

    public Boolean isRightEnabled(int rowIndex) {
        return getBooleanValue(getValueAt(rowIndex, 3));
    }

    public Boolean isOverEnabled(int rowIndex) {
        return getBooleanValue(getValueAt(rowIndex, 4));
    }

    public String getMenuText(int rowIndex) {
        return propertyUtil.getLangText(String.valueOf(getValueAt(rowIndex, 5)));
    }

    private Boolean getBooleanValue(Object value) {
        if (value.getClass() == Boolean.class) {
            return Boolean.valueOf(String.valueOf(value));
        } else {
            return false;
        }
    }
}
