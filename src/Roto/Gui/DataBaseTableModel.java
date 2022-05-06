package Roto.Gui;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;

import Roto.Utils.PropertyUtil;

public class DataBaseTableModel extends DefaultTableModel implements ActionListener, MouseListener {

    private static final long serialVersionUID = -8654490788994116690L;

    private JTable table;
    private SwingConstans swc;
    private PropertyUtil propertyUtil;
    private JComboBox directionCombo, barCombo, colorCombo;
    private int columnSelected, rowSelected;
    private String editCellValue;
    private List<Integer> comboColumns;
    private List<Integer> numberColumns;

    public DataBaseTableModel() {
        swc = SwingConstans.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        directionCombo = new JComboBox();
        barCombo = new JComboBox();
        colorCombo = new JComboBox();
        comboColumns = new ArrayList<Integer>();
        numberColumns = new ArrayList<Integer>();
        createColumns();
    }

    //Override
    public boolean isCellEditable(int row, int col) {
        if (col == 3) {
            return false;
        }
        return true;
    }

    public void createColumns() {
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.ItemCount"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.SAP"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.Direction"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.Text"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MDK"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MDS"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MDF"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MKIPP"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.Cilinder"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.Color"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MSSTK"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MSISSTK"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MFFBV"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MFFBB"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MFFHV"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MFFHB"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MSTART"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MSISTART"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MDK2"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MDS2"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MDF2"));
        addColumn(propertyUtil.getLangText("InternalFrame.EditDataBase.MFREI"));
    }


    public void showCell(int row, int column) {
        Rectangle rect = table.getCellRect(row, column, true);
        table.scrollRectToVisible(rect);
        table.clearSelection();
        if (table.getRowCount() > 0) {
            table.setRowSelectionInterval(row, row);
            fireTableDataChanged(); // notify the model
            table.getSelectionModel().setSelectionInterval(row, row);
        }
    }

    public void setProperties(JTable table) {
        this.table = table;
        this.table.addMouseListener(this);
        table.getTableHeader().setFont(swc.fontExcelHeader);
        table.getTableHeader().setBackground(swc.colorExcelHeader);
        //table.setAutoCreateRowSorter(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        table.getColumnModel()
             .getColumn(0)
             .setPreferredWidth(40);
        table.getColumnModel()
             .getColumn(2)
             .setPreferredWidth(140);
        table.getColumnModel()
             .getColumn(3)
             .setPreferredWidth(330);
        table.getColumnModel()
             .getColumn(8)
             .setPreferredWidth(140);
        table.getColumnModel()
             .getColumn(9)
             .setPreferredWidth(155);

        setNumberCell(0, 1);
        setNumberCell(1, 6, "select_SAP");
        setDirectionComboCell(2);
        setBarComboCell(8);
        setColorComboCell(9);
        setNumberCell(10, 1);
        setNumberCell(11, 1);
        setNumberCell(12, 4);
        setNumberCell(13, 4);
        setNumberCell(14, 4);
        setNumberCell(15, 4);
        setNumberCell(16, 6, "select_SAP");
        setNumberCell(17, 6, "select_SAP");
    }

    //Set checkBox values
    public Class getColumnClass(int columnIndex) {
        //System.out.println(columnIndex + ". Column class: " + getValueAt(0, columnIndex).getClass().getSimpleName() );
        return getValueAt(0, columnIndex).getClass();
        /*
		if ((columnIndex > 3 && columnIndex < 8) || columnIndex > 17 ){
			return  Boolean.class;
		}else {
			return String.class;
		}*/
    }

    //Create numbertextfield for number cells
    private void setNumberCell(int column) {
        setNumberCell(column, 0, null);
    }

    //Create numbertextfield for number cells
    private void setNumberCell(int column, int maxLength) {
        setNumberCell(column, maxLength, null);
    }

    //Create numbertextfield for number cells
    private void setNumberCell(int column, int maxLength, String actionCommand) {
        numberColumns.add(column);
        JRNumberTextField itemCount = new JRNumberTextField();
        itemCount.addBadChars("-");
        itemCount.setBorder(BorderFactory.createLoweredBevelBorder());
        itemCount.setMaxLength(maxLength);
        itemCount.addActionListener(this);
        if (actionCommand != null) {
            itemCount.addMouseListener(this);
            itemCount.setActionCommand(actionCommand);
        } else {
            itemCount.setActionCommand("numbercellmodified");
        }

        DefaultCellEditor cellEditor = new DefaultCellEditor(itemCount);
        this.table
            .getColumnModel()
            .getColumn(column)
            .setCellEditor(cellEditor);
    }

    //Create combo for color
    private void setColorComboCell(int column) {
        comboColumns.add(column);
        colorCombo.addItem("");
        colorCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.Empty"));
        colorCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.Catch"));
        colorCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.CatchMagnet"));
        colorCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.LiftingMD"));
        colorCombo.addItem(propertyUtil.getLangText("InternalFrame.Menus.Fan"));
        colorCombo.addItem(propertyUtil.getLangText("InternalFrame.StockList.Node.Cilinder"));
        colorCombo.addItem(propertyUtil.getLangText("MenuItem.Bar.Ko"));
        colorCombo.addItem(propertyUtil.getLangText("MenuItem.Bar.Ksr"));
        colorCombo.addItem(propertyUtil.getLangText("MenuItem.Bar.M"));

        for (int i = 0; i < swc.colorTitles.length; i++) {
            colorCombo.addItem(propertyUtil.getLangText("Color." + swc.colorTitles[i]));
        }

        colorCombo.addActionListener(this);
        colorCombo.addMouseListener(this);
        colorCombo.setActionCommand("color_combo_selected");
        DefaultCellEditor cellEditor = new DefaultCellEditor(colorCombo);
        this.table
            .getColumnModel()
            .getColumn(column)
            .setCellEditor(cellEditor);
    }

    //Create combo for bar
    private void setBarComboCell(int column) {
        comboColumns.add(column);
        barCombo.addItem("");
        barCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.Empty"));
        barCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.Bar"));
        barCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.PullHandle"));
        for (int i = 0; i < swc.closableBarArray.length; i++) {
            barCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.ClosableBar") + " - " +
                             swc.closableBarArray[i]);
        }

        barCombo.addActionListener(this);
        barCombo.addMouseListener(this);
        barCombo.setActionCommand("bar_combo_selected");
        DefaultCellEditor cellEditor = new DefaultCellEditor(barCombo);
        this.table
            .getColumnModel()
            .getColumn(column)
            .setCellEditor(cellEditor);
    }

    //Create combo for direction
    private void setDirectionComboCell(int column) {
        comboColumns.add(column);
        directionCombo.addItem("");
        directionCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase.Empty"));

        for (int i = 0; i < swc.directionTitles.length; i++) {
            directionCombo.addItem(propertyUtil.getLangText("InternalFrame.EditDataBase." + swc.directionTitles[i]));
        }

        directionCombo.addActionListener(this);
        directionCombo.addMouseListener(this);
        directionCombo.setActionCommand("direction_combo_selected");
        DefaultCellEditor cellEditor = new DefaultCellEditor(directionCombo);
        this.table
            .getColumnModel()
            .getColumn(column)
            .setCellEditor(cellEditor);
    }

    //Set value by direction combo
    private void setDirectionComboValue() {

        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();

        //An empty row is added to combo, so index will be decreased
        int selectedIndex = directionCombo.getSelectedIndex() - 1;
        if (selectedColumn > -1 && selectedRow > -1 && selectedIndex > -1) {
            if (selectedIndex == 0) {
                table.setValueAt("", selectedRow, selectedColumn);
            }
            if (selectedIndex > 0) {
                table.setValueAt(swc.directionArray[selectedIndex - 1], selectedRow, selectedColumn);
            }
            editCellValue = table.getValueAt(selectedRow, selectedColumn).toString();
            directionCombo.setSelectedIndex(0);
        }

    }

    //Set value by bar combo
    private void setBarComboValue() {
        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();
        //An empty row is added to combo, so index will be decreased
        int selectedIndex = barCombo.getSelectedIndex() - 1;
        if (selectedColumn > -1 && selectedRow > -1 && selectedIndex > -1) {
            if (selectedIndex == 0) {
                table.setValueAt("", selectedRow, selectedColumn);
            } else if (selectedIndex == 1) {
                table.setValueAt("0", selectedRow, selectedColumn);
            } else if (selectedIndex == 2) {
                table.setValueAt("kh", selectedRow, selectedColumn);
            } else {
                table.setValueAt(swc.closableBarArray[selectedIndex - 3], selectedRow, selectedColumn);
            }
            editCellValue = table.getValueAt(selectedRow, selectedColumn).toString();
            barCombo.setSelectedIndex(0);
        }
    }

    //Set value by color combo
    private void setColorComboValue() {
        int selectedRow = table.getSelectedRow();
        int selectedColumn = table.getSelectedColumn();
        //An empty row is added to combo, so index will be decreased
        int selectedIndex = colorCombo.getSelectedIndex() - 1;
        if (selectedColumn > -1 && selectedRow > -1 && selectedIndex > -1) {
            if (selectedIndex == 0) {
                table.setValueAt("", selectedRow, selectedColumn);
            } else if (selectedIndex == 1) {
                table.setValueAt("cs", selectedRow, selectedColumn);
            } else if (selectedIndex == 2) {
                table.setValueAt("mcs", selectedRow, selectedColumn);
            } else if (selectedIndex == 3) {
                table.setValueAt("h", selectedRow, selectedColumn);
            } else if (selectedIndex == 4) {
                table.setValueAt("r", selectedRow, selectedColumn);
            } else if (selectedIndex == 5) {
                table.setValueAt("c", selectedRow, selectedColumn);
            } else if (selectedIndex == 6) {
                table.setValueAt(swc.selectionBarArray[0], selectedRow, selectedColumn);
            } else if (selectedIndex == 7) {
                table.setValueAt(swc.selectionBarArray[1], selectedRow, selectedColumn);
            } else if (selectedIndex == 8) {
                table.setValueAt(swc.selectionBarArray[2], selectedRow, selectedColumn);
            } else {
                table.setValueAt(swc.colorArray[selectedIndex - 9], selectedRow, selectedColumn);
            }
            colorCombo.setSelectedIndex(0);
            editCellValue = table.getValueAt(selectedRow, selectedColumn).toString();
        }
    }

    private Boolean isComboColumn(int column) {
        if (comboColumns.contains(column))
            return true;
        else
            return false;
    }

    private Boolean isNumberColumn(int column) {
        if (numberColumns.contains(column))
            return true;
        else
            return false;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand()
                      .trim()
                      .toLowerCase();

        if (cmd.equals("direction_combo_selected")) {
            setDirectionComboValue();
        } else if (cmd.equals("bar_combo_selected")) {
            setBarComboValue();
        } else if (cmd.equals("color_combo_selected")) {
            setColorComboValue();
        } else if (cmd.equals("select_sap")) {
        } else if (cmd.equals("numbercellmodified")) {
            editCellValue = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();
        }


    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (e.getSource() == table) {
            //System.out.println("Table pressed. ");

            if (isComboColumn(columnSelected) && editCellValue != null) {
                //System.out.println("Update value. " + editCellValue +" "+ rowSelected+ "," + columnSelected);
                table.setValueAt(editCellValue, rowSelected, columnSelected);
            }

            //Set actual cell values
            columnSelected = table.getSelectedColumn();
            rowSelected = table.getSelectedRow();
            editCellValue = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString();

            //Kenyszeritjuk dupla klikkre a felhasznalot, hogy bejojjon a cella mogotti number field
            if (isNumberColumn(columnSelected) && e.getClickCount() == 1) {
                table.editCellAt(table.getSelectedRow(), 4);
            }

        } else if (e.getSource().getClass() == JRNumberTextField.class) {
            Frames.getInstance()
                  .jriPriceListSelect
                  .setVisibleToFocus();
            Frames.getInstance()
                  .jbPriceListFilterAdd
                  .setActionCommand("editdatabasesetsap");
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public String getEditCellValue() {
        return editCellValue;
    }

    public void setEditCellValue(String editCellValue) {
        this.editCellValue = editCellValue;
    }

}
