package Roto.Gui;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;
import Roto.Utils.PropertyUtil;

public class ReplaceTableModel extends DefaultTableModel implements ActionListener,
				MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private SwingConstans swc;
	private PropertyUtil propertyUtil;
	
	public ReplaceTableModel() {
		swc = SwingConstans.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		createColumns();
		createRows();
	}
	
	//Override
	public boolean isCellEditable(int row, int col) {
		if (col == 0){	
			return false;
		}
		return true;
	}
	
	public void createColumns(){
		addColumn(propertyUtil.getLangText("InternalFrame.ReplaceData.State"));
		addColumn(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceFrom"));
		addColumn(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceTo"));
	}
	
	public void createRows(){
		addRow(new Object[]{
				false,
				"",
				""
		});
	}
	
	public void showCell(int row, int column) {
	    Rectangle rect =  table.getCellRect(row, column, true);
	    table.scrollRectToVisible(rect);
	    table.clearSelection();
	    if (table.getRowCount() > 0) {
	    	table.setRowSelectionInterval(row, row);
	        fireTableDataChanged(); // notify the model
	        table.getSelectionModel().setSelectionInterval(row,row);
	    }
	}
	
	public void setProperties(JTable table){
		this.table = table;
		table.getTableHeader().setFont(swc.fontExcelHeader);
		table.getTableHeader().setBackground(swc.colorExcelHeader);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.addMouseListener(this);
		table.setFocusable(false);
		setNumberCell(1,6,"select_SAP");
		setNumberCell(2,6,"select_SAP");
	}
	
	//Create numbertextfield for number cells
	private void setNumberCell(int column, int maxLength, String actionCommand){
		JRNumberTextField itemCount = new JRNumberTextField();
		itemCount.addBadChars("-");
		itemCount.setBorder(BorderFactory.createLoweredBevelBorder());
		itemCount.setMaxLength(maxLength);
		itemCount.addActionListener(this);
		itemCount.setCursor(new Cursor(Cursor.HAND_CURSOR));
		if (actionCommand != null) {
			itemCount.addMouseListener(this);
			itemCount.setActionCommand(actionCommand);
		} else {
			itemCount.setActionCommand("numbercellmodified");
		}
		
		DefaultCellEditor cellEditor = new DefaultCellEditor(itemCount);
		this.table.getColumnModel().getColumn(column).setCellEditor(cellEditor);
	}
	
	//Set checkBox values 
	public Class getColumnClass (int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}

	public void actionPerformed(ActionEvent e) {
		
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource().getClass() == JRNumberTextField.class){
			if (e.getClickCount() > 1){
				Frames.getInstance().jriPriceListSelect.setVisibleToFocus();
				Frames.getInstance().jbPriceListFilterAdd.setActionCommand("replacedatasetsap");
			}
		} else if (e.getSource().getClass() == JTable.class){
			if (e.getClickCount() == 1){
				table.editCellAt(0,0);
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
}
