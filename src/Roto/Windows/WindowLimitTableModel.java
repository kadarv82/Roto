package Roto.Windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.sun.org.apache.bcel.internal.generic.CPInstruction;
import com.sun.security.auth.module.JndiLoginModule;

import Roto.Basic.SwingConstans;
import Roto.Gui.Frames;
import Roto.Gui.JRNumberTextField;
import Roto.Utils.PropertyUtil;

public class WindowLimitTableModel extends DefaultTableModel implements ActionListener,
	PropertyChangeListener {
	private PropertyUtil propertyUtil = PropertyUtil.getInstance();
	private SwingConstans swc = SwingConstans.getInstance();
	private JTable table;
	private JComboBox jcbSize1, jcbSize2, jcbConditions1, jcbConditions2;
	private JRNumberTextField result;
	private final String EMPTY_STRING = "";
	private String WIDTH_STRING = propertyUtil.getLangText("InternalFrame.Windows.Size.Width");
	private String HEIGHT_STRING = propertyUtil.getLangText("InternalFrame.Windows.Size.Height");
	private Frames frames;
	private HashMap<String,Integer> typeMap;
	private boolean checkSelectedValues = true;
	public final String NO_WARNING_MESSAGE = "No warning message";

	public WindowLimitTableModel() {
		frames = Frames.getInstance();
		WIDTH_STRING = propertyUtil.getLangText("InternalFrame.Windows.Size.Width");
		HEIGHT_STRING = propertyUtil.getLangText("InternalFrame.Windows.Size.Height");
		jcbSize1 = new JComboBox();
		jcbSize2 = new JComboBox();
		jcbConditions1 = new JComboBox();
		jcbConditions2 = new JComboBox();
		typeMap = new HashMap<String,Integer>();
		for (int i =0; i < swc.windowTypeArray.length; i++) {
			typeMap.put(swc.windowTypeArray[i],i);	
		}
		
		
		
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Enabled"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Type"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Size"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Condition"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Size"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Condition"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Result"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Key"));
		addColumn(propertyUtil.getLangText("InternalFrame.Windows.Message"));
		
		
		for (int i = 0; i < 8; i++) {
			addRow(
					new Object[] { 
					new Boolean(false),
					swc.windowTypeArray[i], 
					"",
					"",
					"",
					"",
					"",
					"",
					""}
					);
		}
		
		jcbConditions1.addItem("/");
		jcbConditions1.addItem("+");
		jcbConditions1.addItem("*");
		jcbConditions1.addItem("-");
		jcbConditions1.setFont(swc.fontCanvasResize);
		jcbConditions1.addActionListener(this);
		
		jcbConditions2.addItem("=");
		jcbConditions2.addItem("<");
		jcbConditions2.addItem(">");
		jcbConditions2.setFont(swc.fontCanvasResize);
		jcbConditions2.addActionListener(this);
		
		jcbSize1.addItem(EMPTY_STRING);
		jcbSize1.addItem(WIDTH_STRING);
		jcbSize1.addItem(HEIGHT_STRING);
		jcbSize1.addActionListener(this);
		
		jcbSize2.addItem(WIDTH_STRING);
		jcbSize2.addItem(HEIGHT_STRING);
		jcbSize2.addActionListener(this);
		
	}
	
	public void setProperties(JTable table){
		this.table = table;
		result = new JRNumberTextField(0,5,true);
		//	itemCount.addBadChars("-");
		result.setBorder(BorderFactory.createLoweredBevelBorder());
		result.setMaxLength(5);
		
		
		
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(240);
		table.getColumnModel().getColumn(8).setPreferredWidth(400);
		
		DefaultTableCellRenderer renderer;
		renderer =	(DefaultTableCellRenderer) table.getCellRenderer(0, 2);
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		DefaultCellEditor cellEditor = new DefaultCellEditor(jcbSize1);
		table.getColumnModel().getColumn(2).setCellEditor(cellEditor);
		cellEditor = new DefaultCellEditor(jcbConditions1);
		table.getColumnModel().getColumn(3).setCellEditor(cellEditor);
		cellEditor = new DefaultCellEditor(jcbSize2);
		table.getColumnModel().getColumn(4).setCellEditor(cellEditor);
		cellEditor = new DefaultCellEditor(jcbConditions2);
		table.getColumnModel().getColumn(5).setCellEditor(cellEditor);
		cellEditor = new DefaultCellEditor(result);
		table.getColumnModel().getColumn(6).setCellEditor(cellEditor);
		
		table.addPropertyChangeListener(this);
	}
	
	
	@Override
	//Set checkBox values 
	public Class getColumnClass (int columnIndex) {
		return getValueAt(0, columnIndex).getClass();
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
		boolean editable = true;
		
		if (col == 0 && table.getValueAt(row, 1).toString().length() == 0) {
			editable = false;
		}
		
		if (col == 1 || col == 8) {
			editable = false;
		} 
		if (table.getValueAt(row, 2).toString().equals("") && col == 3) {
			editable = false;
		}
		if (col > 1 && !getBooleanValue(table.getValueAt(row, 0))) {
			editable = false;
		} 

		return editable;
	}
	
	public void setModelArray(Object[][] data, WindowTableModel windowTableModel){
    	for (int row = 0; row < swc.windowTypeArray.length-2; row++ ){
			for (int col = 0; col < 9; col++ ){
				Object value = data[row][col];
				if (col==1) {
					setValueAt(windowTableModel.getMenuText(row), row, col);
				} else if (col == 2) {
					try {
						int selectedIndex = Integer.valueOf(String.valueOf(value));
						setValueAt(jcbSize1.getItemAt(selectedIndex), row, col);
					} catch (Exception e) {
						setValueAt(value, row, col);
					}
				}  else if (col == 4) {
					try {
						int selectedIndex = Integer.valueOf(String.valueOf(value));
						setValueAt(jcbSize2.getItemAt(selectedIndex), row, col);
					} catch (Exception e) {
						setValueAt(value, row, col);
					}
				} else {
					setValueAt(value, row, col);
				}
			}
		}
    	checkAllTableRows();
    }

    public String getWarningMessage(boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2, boolean isMDF2, boolean isMDS2, boolean isMFREI){
    	if (getConditionResult(isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI)) {
            int rowIndex = getRowByeType(getTypename(isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI));
            return  propertyUtil.getLangText(String.valueOf(getValueAt(rowIndex, 7)));
    	} else {
    	    return NO_WARNING_MESSAGE;
    	}
    }
	
    public Object[][] getModelArray(){
    	return getModelArray(false);
    }
    
    public Object[][] getModelArray(boolean isSave){
    	Object[][] data = new Object[getRowCount()][getColumnCount()];
    	for (int row = 0; row < getRowCount(); row++ ){
			for (int col = 0; col < getColumnCount(); col++ ){
				if (col  == 2 && isSave){
					String comboText = getValueAt(row, col).toString();
					data [row][col] = getComboIndexByValue(jcbSize1, comboText);
				} else if (col  == 4  && isSave){
					String comboText = getValueAt(row, col).toString();
					data [row][col] = getComboIndexByValue(jcbSize2, comboText);
				} else {
					data [row][col] = getValueAt(row, col);
				}
			}
		}
    	return data;
    }
    
    public int getComboIndexByValue(JComboBox combo, String value) {
    	int index = 0;
    	for (int i=0; i < combo.getItemCount(); i++){ 
    		String comboValue = combo.getItemAt(i).toString();
    		if (comboValue.equals(value)) {
    			index = i;
    		}
    	}
    	return  index;
    }
    
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(jcbSize1)) {
			comboSize1Selected();
		}
		
		updateValues();
	}
	
	private void updateValues() {
		fireTableDataChanged();
		fireTableRowsUpdated(0, table.getRowCount());
	}
	
	private void comboSize1Selected(){
		int row = table.getSelectedRow();
		if (row > -1) {
			if (jcbSize1.getSelectedItem().toString().length() ==0 ) {
				table.setValueAt("",row, 3);
			} else if (table.getSelectedRow() > -1) {
				String value = table.getValueAt(row, 3).toString();
				if (value != null && value.length()==0) {
					table.setValueAt(jcbConditions1.getItemAt(0),row, 3);
				}
			}
		}
	}
	
	 private Boolean getBooleanValue(Object value) {
	    	if (value.getClass() == Boolean.class){
	    		return Boolean.valueOf(String.valueOf(value));
	    	} else {
	    		return false;
	    	}
	    }

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == this.table) {
			int selectedColumn = table.getSelectedColumn();
			if (selectedColumn == 0 || selectedColumn == 6 
					|| table.getSelectedColumn() == 7){
				checkTableRow(table.getSelectedRow());
			}
		}
		this.table.repaint();
		this.table.validate();
	}
	
	private void checkAllTableRows() {
		for (int row = 0; row < table.getRowCount(); row ++) {
			checkTableRow(row);
			//Refresh text by key values. 
			table.setColumnSelectionInterval(7, 7);
			checkTableRow(row);
		}
		updateValues();
	}

	private void checkTableRow(int row) {
		if (row >-1 && isCheckSelectedValues()) {
			if (!getBooleanValue(table.getValueAt(row,0))) {
				for (int col = 2; col < table.getColumnCount(); col++) {
					table.setValueAt("", row, col);
				}
			} else if (table.getEditingColumn() > -1 && table.getEditingColumn() != 6 && table.getEditingColumn() != 7) {
				table.setValueAt(propertyUtil.getLangText("InternalFrame.Windows.Size.Width"), row, 2);
				table.setValueAt("/", row, 3);
				table.setValueAt(propertyUtil.getLangText("InternalFrame.Windows.Size.Height"), row, 4);
				table.setValueAt(">", row, 5);
			}
			
			if (table.getValueAt(row, 1).toString().length() == 0) {
				table.setValueAt(new Boolean(false), row, 0);
				for (int col = 2; col < table.getColumnCount(); col++) {
					table.setValueAt("", row, col);
				}
			}
			if (table.getValueAt(row, 6).toString().length() == 0 &&
					getBooleanValue(table.getValueAt(row,0))) {
				table.setValueAt("0", row, 6);
			}
			
			if (table.getSelectedColumn() == 7) {
				String key = table.getValueAt(row, table.getSelectedColumn()).toString();
				if (key.trim().length() > 0){
					table.setValueAt(propertyUtil.getLangText(key), row, 8);				
				}
			}
		}
	}
	
	private int getRowByeType(String type) {
		if (typeMap.get(type) != null) {
			return typeMap.get(type);
		} else {
			return 0;
		}
	}
	
	private double getSize1Value(String type) {
		int row = getRowByeType(type);
		int value = 0;
		String conditionText = getValueAt(row, 2).toString();
		//When the model is loaded, there are numbers. If modified, text can be found.
		try {
			int index = Integer.parseInt(conditionText);
			conditionText = jcbSize1.getItemAt(index).toString();
		} catch (Exception e) {
			//do nothing.
		}
		if (conditionText.equals(WIDTH_STRING)) {
			value = frames.jntWidth.getNumber()	- frames.jntCorrectionWidth.getNumber();
		} else if (conditionText.equals(HEIGHT_STRING)) {
			value = frames.jntHeight.getNumber() - frames.jntCorrectionHeight.getNumber();
		}
		//System.out.println("getSize1Value(" + type +") = " + value +" conditionText = " + conditionText);
		return value;
	}

	private double getSize2Value(String type) {
		int row = getRowByeType(type);
		int value = 0;
		String conditionText = getValueAt(row, 4).toString();
		//When the model is loaded, there are numbers. If modified, text can be found.
		try {
			int index = Integer.parseInt(conditionText);
			conditionText = jcbSize2.getItemAt(index).toString();
		} catch (Exception e) {
			//do nothing.
		}

		if (conditionText.equals(WIDTH_STRING)) {
			value = frames.jntWidth.getNumber()	- frames.jntCorrectionWidth.getNumber();
		} else if (conditionText.equals(HEIGHT_STRING)) {
			value = frames.jntHeight.getNumber() - frames.jntCorrectionHeight.getNumber();
		}
		//System.out.println("getSize2Value(" + type +") = " + value +" conditionText = " + conditionText);
		return value;
	}
	
	private double getFirsConditionResult(String type) {
		double result = 0.0;
		int row = getRowByeType(type);
		//System.out.println(type + "->" + row);
		String conditionText = getValueAt(row, 3).toString();
		
		if (conditionText.equals(EMPTY_STRING)) {
			result = getSize2Value(type);
		} else if (conditionText.equals("/")) {
			result = getSize1Value(type) / getSize2Value(type);
		} else if (conditionText.equals("*")) {
			result = getSize1Value(type) * getSize2Value(type);
		} else if (conditionText.equals("+")) {
			result = getSize1Value(type) + getSize2Value(type);
		} else if (conditionText.equals("-")) {
			result = getSize1Value(type) - getSize2Value(type);
		}
		//System.out.println("getFirsConditionResult(" + type +") = " + result +" conditionText = " + conditionText);
		return result;
	}
	
	public boolean getConditionResult(boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2, boolean isMDF2, boolean isMDS2, boolean isMFREI) {
		boolean result = false;
		String type = getTypename(isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI);
		
		int row = getRowByeType(type);
		String conditionText = getValueAt(row, 5).toString();
		
		if (conditionText.equals("=")) {
			result = getFirsConditionResult(type) == Double.parseDouble(getValueAt(row, 6).toString().replaceAll(",", "."));
		} else if (conditionText.equals(">")) {
			result = getFirsConditionResult(type) > Double.parseDouble(getValueAt(row, 6).toString().replaceAll(",", "."));
		} else if (conditionText.equals("<")) {
			result = getFirsConditionResult(type) < Double.parseDouble(getValueAt(row, 6).toString().replaceAll(",", "."));
		}
		
		
		//System.out.println("getConditionResult(" + type +") = " + result +" conditionText = " + conditionText);
		return result;
	}

	private String getTypename(boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2, boolean isMDF2, boolean isMDS2, boolean isMFREI) {
		String type = "";
		if (isMDK) {
		 type = swc.windowTypeArray[0];	
		} else if (isMDF) {
		 type = swc.windowTypeArray[1];	
		} else if (isMDS) {
		 type = swc.windowTypeArray[2];	
		} else if (isMKIPP) {
		 type = swc.windowTypeArray[3];	
		} else if (isMDK2) {
		 type = swc.windowTypeArray[4];	
		} else if (isMDF2) {
		 type = swc.windowTypeArray[5];	
		} else if (isMDS2) {
		 type = swc.windowTypeArray[6];	
		} else if (isMFREI) {
		 type = swc.windowTypeArray[7];	
		}
		return type;
	}
	

	public boolean isCheckSelectedValues() {
		return checkSelectedValues;
	}

	public void setCheckSelectedValues(boolean checkSelectedValues) {
		this.checkSelectedValues = checkSelectedValues;
	}
	
}
