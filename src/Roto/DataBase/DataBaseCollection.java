package Roto.DataBase;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import Roto.Basic.SwingConstans;
import Roto.Gui.Frames;
import Roto.Utils.PriceUtil;
import Roto.Utils.PropertyUtil;

public class DataBaseCollection {

	private HashMap<String, Integer> selection;
	private PriceUtil priceUtil;
	private PropertyUtil propertyUtil;
	private DefaultTableModel tableModel;
	private NumberFormat formatter;
	private SwingConstans swc;
	private int garniture,width,height,width2,height2;
	private String typeName,colorName,direction,databaseName;
	private List<String> descriptions;
	private Frames frames;
	
	public DataBaseCollection(HashMap<String, Integer> selection, JTable jtCollection) {
		this.selection = selection;
		this.tableModel = (DefaultTableModel) jtCollection.getModel();
		this.priceUtil = PriceUtil.getInstance();
		this.propertyUtil = PropertyUtil.getInstance();
		this.swc = SwingConstans.getInstance();
		this.formatter = swc.formatter;
		this.frames = Frames.getInstance();
		
		removeRows();
		addRows();
	}
	
	//Remove an item from selection
	public void removeCollectionItem(String SAP){
		selection.remove(SAP);
	}
	
	//Add an item to selection
	public void addCollectionItem(String SAP){
		//SAP not exist yet
		addSelectionRow(SAP,1);
	}
	
	//Increase or decrease an item in selection
	public void modifyCollectionItemCount(String SAP, int modifyValue){
		int count = selection.get(SAP);
		if (count + modifyValue != 0){
			//Remove existing element
			selection.remove(SAP);
			//Add modified item
			selection.put(SAP, (count + modifyValue));
		}
	}
	
	//Set item count in selection
	public void setCollectionItemCount(String SAP, int itemCount){
		int count = selection.get(SAP);
		if (itemCount != 0){
			//Remove existing element
			selection.remove(SAP);
			//Add modified item
			selection.put(SAP, (itemCount));
		}
	}

	
	//Add a row to the selection, handle duplicate items
	private void addSelectionRow(String SAP, int itemCount ){
		//Add item if not exists
		if (!selection.containsKey(SAP)){
			selection.put(SAP, itemCount);
		} else {
			//Get count of the existing element
			int count = selection.get(SAP);
			//Remove existing element
			selection.remove(SAP);
			//Add aggregated item
			selection.put(SAP, (count+itemCount));
		}
	}
	
	private void removeRows(){
		while (this.tableModel.getRowCount() >0){
			this.tableModel.removeRow(0);
		}
	}
	
	public void addRows() {
		Object[]  key  = selection.keySet().toArray();
		Arrays.sort(key);           
		for (int  i=0; i<key.length; i++)   {
			String SAP = key[i].toString();
			int pieces = selection.get(SAP); 

			tableModel.addRow(new Object[]{
					selection.get(SAP).toString(), 
					SAP,
					priceUtil.getListText(SAP),
					formatter.format(priceUtil.getListPriceMultiplied(SAP)),
					formatter.format(priceUtil.getListPriceCalculated(SAP)),
					formatter.format(formatDouble(priceUtil.getListPriceCalculated(SAP) * pieces))
					});
		}

		//Add summary
		if (key.length >0){		
			frames.tfCollectionSum.setText(formatter.format(formatDouble(getNetSum())) + " " +priceUtil.getCurrency() + " " + propertyUtil.getLangText("InternalFrame.CollectionList.Vat"));
		}
	}
	
	public double getNetSum(){
		double priceNetSum = 0;
		Object[]  key  = selection.keySet().toArray();
		Arrays.sort(key);
		
		for (int  i=0; i<key.length; i++)   {
			String SAP = key[i].toString();
			int pieces = selection.get(SAP);
			priceNetSum += formatDouble(priceUtil.getListPriceCalculated(SAP) * pieces);
		}
		
		return priceNetSum;
	}
	
	private double formatDouble(Double value){
		try {
			String Price = formatter.format(value);
			Price = Price.replace(",",".");
			return Double.parseDouble(Price);
		} catch (Exception e) {
			System.out.println("Can't format value: " + value);
			e.printStackTrace();
			return value;
		}
	}
	
	public List<String> getDescriptions() {
		return descriptions;
	}
	
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	
	public HashMap<String, Integer> getSelection(){
		return selection;
	}
	
	public int getGarniture() {
		return garniture;
	}

	public void setGarniture(int garniture) {
		this.garniture = garniture;
	}
		
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth2() {
		return width2;
	}

	public void setWidth2(int width2) {
		this.width2 = width2;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight2() {
		return height2;
	}

	public void setHeight2(int height2) {
		this.height2 = height2;
	}

	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	
}
