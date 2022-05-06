package Roto.DataBase;

import java.util.Vector;

public class TextDBTable {
	Vector<TextDBRow> vTable;
	
	public TextDBTable(){
		vTable = new Vector<TextDBRow>();
	}
	
	public void addRow(TextDBRow row){
		vTable.add(row);
	}
	
	public void removeRows(){
		vTable.removeAllElements();
		vTable = new Vector<TextDBRow>();
	}
	
	public int getRowCount(){
		return vTable.size();
	}
	
	public TextDBRow getRowAt(int index){
		return vTable.get(index);
	}
	
}
