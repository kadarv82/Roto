package Roto.Windows;

import Roto.Basic.SwingConstans;

public class WindowSystem {
	private String systemName;
	private String systemType;
	private SwingConstans swc = SwingConstans.getInstance();
		
	private WindowTableModel windowTableModel;
	private WindowLimitTableModel windowLimitTableModel;
	
	public WindowSystem(){
		windowTableModel = new WindowTableModel();
		windowLimitTableModel = new WindowLimitTableModel();
	}
	
	public Object[][] getWindowTableModelArray(){
		return windowTableModel.getModelArray();
	}
	
	public Object[][] getWindowLimitTableModelArray(){
		return windowLimitTableModel.getModelArray();
	}
	
	public Object[][] getWindowLimitTableModelArray(boolean isSave){
		return windowLimitTableModel.getModelArray(isSave);
	}
	
	public void setWindowTableModel(WindowTableModel windowTableModel){
		for (int i = 0; i < swc.windowTypeArray.length; i++ ){
			for (int j = 0; j < 7; j++ ){
				Object value = windowTableModel.getValueAt(i, j);
				this.windowTableModel.setValueAt(value, i, j);
			}
		}
	}
	
	public void setWindowLimitTableModel(WindowLimitTableModel windowLimitTableModel) {
		for (int i = 0; i < windowLimitTableModel.getRowCount(); i++ ){
			for (int j = 0; j < windowLimitTableModel.getColumnCount(); j++ ){
				Object value = windowLimitTableModel.getValueAt(i, j);
				this.windowLimitTableModel.setValueAt(value, i, j);
			}
		}
	}	
	
	public WindowTableModel getWindowTableModel(){
		return this.windowTableModel;
	}
	
	public WindowLimitTableModel getWindowLimitTableModel() {
		return windowLimitTableModel;
	}

    public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
}
