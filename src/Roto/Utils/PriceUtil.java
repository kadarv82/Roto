package Roto.Utils;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import jxl.Sheet;

import jxl.Workbook;

import jxl.read.biff.BiffException;

import jxl.write.WritableSheet;

import jxl.write.WritableWorkbook;


import Roto.Basic.SwingConstans;
import Roto.DataBase.TextPriceList;
import Roto.DataBase.TextPriceListRow;
import Roto.Gui.Frames;
import Roto.Gui.Viewer;

public class PriceUtil {

	private static final long serialVersionUID = 1L;
	private static PriceUtil instance = null;
	private TextPriceList priceList;
	private TextPriceListRow priceListRow;
	private MessageUtil message;	
	private PropertyUtil propertyUtil;
	private DefaultTableModel tableModel;
	private File PriceListFile; 
	private GroupUtil groupUtil;
	private RebateUtil rebateUtil;
	private NumberFormat formatter;
	private SwingConstans swc;
	private String currency;
	private String priceListDate;

	public static PriceUtil getInstance() {
		if (instance == null) {
			instance = new PriceUtil();
		}
		return instance;
	}
	
	public PriceUtil () {
		priceList = new TextPriceList();
		message = MessageUtil.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		groupUtil = GroupUtil.getInstance();
		rebateUtil = RebateUtil.getInstance();
		swc = SwingConstans.getInstance();
		formatter = swc.formatter;
		initTableModel("");
	}

	private void initTableModel(String[] headerTitles){
		initTableModel("String array used !", headerTitles);
	}
	
	private void initTableModel(String header){
		initTableModel(header, null);
	}
	
	//Create a new table model
	private void initTableModel(String header, String[] headerTitles){
		String headerSAP = "";
		String headerMartikel = "";
		String headerText = "";
		String headerPrice = "";
		String headerGroup = "";
		
		priceListRow = new TextPriceListRow();
		if (header.length()>0){
			//Header starts with "?"
			header = header.substring(1);
			if (headerTitles == null) headerTitles = priceListRow.getHeaderTitles(header);
		
			headerSAP = headerTitles[0];
			headerText = headerTitles[1];
			headerMartikel = headerTitles[2];
			headerPrice = headerTitles[3];
			headerGroup = headerTitles[4];
			
			/*
			System.out.println(titles[0]);
			System.out.println(titles[1]);
			System.out.println(titles[2]);
			System.out.println(titles[3]);
			System.out.println(titles[4]);
			*/
		}
		
		
		tableModel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int col) {
				return false; 
			}
		};
		
		if (headerSAP.length()==0) tableModel.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.SAP"));
		else tableModel.addColumn(headerSAP);
		
		if (headerMartikel.length()==0) tableModel.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Martikel"));
		else tableModel.addColumn(headerMartikel);
		
		if (headerText.length()==0) tableModel.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Text"));
		else tableModel.addColumn(headerText); 
		
		if (headerPrice.length()==0) tableModel.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Price"));
		else tableModel.addColumn(headerPrice);
		
		if (headerGroup.length()==0) tableModel.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Group"));
		else tableModel.addColumn(headerGroup);
		
		//Set print selection titles for radio buttons
		Frames.getInstance().jrPrintSap.setText(headerSAP);
		Frames.getInstance().jrPrintMartikel.setText(headerMartikel);
		
	}
	
	//Set table model
	private void setModel(JTable PriceListTable){
		if (PriceListTable != null && tableModel != null){
			PriceListTable.setModel(tableModel);
			PriceListTable.getColumnModel().getColumn(0).setPreferredWidth(60);
			PriceListTable.getColumnModel().getColumn(1).setPreferredWidth(80);
			PriceListTable.getColumnModel().getColumn(2).setPreferredWidth(360);
			PriceListTable.getColumnModel().getColumn(3).setPreferredWidth(25);
			PriceListTable.getColumnModel().getColumn(4).setPreferredWidth(45);
		}
	}

	//Load Excel file, and create a table with row objects
	public void loadExcelPriceList(File PriceListFile, JTable PricelistTable, Double Multiplier){
		try {
						
			this.PriceListFile = PriceListFile;
			
			//Remove elements from table
			priceList.removeRows();
			//Set multiplier
			if (Multiplier==null || Multiplier == 0){
				Multiplier = 1.0;
			}
			priceList.setMultiplier(Multiplier);
			
			Workbook workbook = Workbook.getWorkbook(PriceListFile);
			Sheet sheet = workbook.getSheet(0);
			int rowCount = sheet.getRows();
			
			//Create header titles
			String[] headerTitles = new String[5];
			for (int i=0; i< headerTitles.length; i++) {
				headerTitles[i] =  sheet.getCell(i,0).getContents().trim();	
			}
			
			//Init table model
			initTableModel(headerTitles);
			
			for (int i=1; i< rowCount; i++) {
				String SAP = sheet.getCell(0,i).getContents().trim();
				String Text = sheet.getCell(1,i).getContents().trim();
				String Martikel = sheet.getCell(2,i).getContents().trim();
				String Price = sheet.getCell(3,i).getContents().trim().replace(",", ".");
				String Group = sheet.getCell(4,i).getContents().trim();
				if (SAP.length()>0 && Text.length()>0 && Price.length()>0 && Group.length()>0){
					priceListRow = new TextPriceListRow();
					priceListRow.setSAP(SAP);
					priceListRow.setMartikel(Martikel);
					priceListRow.setText(Text);
					priceListRow.setPrice(Double.parseDouble(Price));
					priceListRow.setGroup(Group);
					/*
					System.out.println(priceListRow.getSAP());
					System.out.println(priceListRow.getText());
					System.out.println(priceListRow.getPrice());
					System.out.println(priceListRow.getGroup());
					*/
					priceList.addRow(priceListRow);
					tableModel.addRow(new Object[]{
							priceListRow.getSAP(),
							priceListRow.getMartikel(),
							priceListRow.getText(),
							priceListRow.getPrice(),
							priceListRow.getGroup()
							});
				} else if(SAP.startsWith("Date:")){
					SAP = SAP.replace(" ", "").substring(SAP.lastIndexOf(":")).trim();
					this.priceListDate = (SAP+"           ").substring(1,11).trim();
				}
			}
			
			//Set model for JTable
			setModel(PricelistTable);
			
		} catch (Exception e){
			e.printStackTrace();
		
			message.showErrorMessage(e.getMessage(), propertyUtil.getLangText("Error.Pricelist.Load.Error"));
		}
	}
	//Load text file, and create a table with row objects
	public void loadTextPriceList(File PriceListFile, JTable PricelistTable, Double Multiplier){
			try {
				BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(PriceListFile), "UTF8")
				);
				
				this.PriceListFile = PriceListFile;

				String line;
				//Remove elements from table
				priceList.removeRows();
				//Set multiplier
				if (Multiplier==null || Multiplier == 0){
					Multiplier = 1.0;
				}
				priceList.setMultiplier(Multiplier);
				
				//Get header from first row
				String header = br.readLine();
				
				//Init table model
				initTableModel(header);
		
				//Load file

				while((line = br.readLine()) != null) {
					if (line.trim().length()>50){
						priceListRow = new TextPriceListRow(line);
						priceList.addRow(priceListRow);
						tableModel.addRow(new Object[]{
								priceListRow.getSAP(),
								priceListRow.getMartikel(),
								priceListRow.getText(),
								priceListRow.getPrice(),
								priceListRow.getGroup()
										});
					} else if (line.startsWith("Date:")){
						line = line.replace(" ", "").substring(line.lastIndexOf(":")).trim();
						this.priceListDate = (line+"           ").substring(1,11).trim();
					}
				} 
				br.close();
				//Set model for JTable
				setModel(PricelistTable);
				
			} catch (Exception e){
				e.printStackTrace();
				message.showErrorMessage(e.getMessage(), propertyUtil.getLangText("Error.Pricelist.Load.Error"));
			}
	}
	
	//Remove all rows from a table model
	private void clearTableModel(DefaultTableModel tableModel){
		while (tableModel.getRowCount() >0){
			tableModel.removeRow(0);
		}
	}
	
	private void selectTableRow(JTable Table, int selectedRowIndex){
		DefaultTableModel defaultTableModel = (DefaultTableModel) Table.getModel();
		if (defaultTableModel.getColumnCount()>0){
			ListSelectionModel selectionModel =	Table.getSelectionModel();
				selectionModel.setSelectionInterval(0, 0);
		}
	}
	//Copy price list 	
	public void copyPriceList(JTable PriceListTable){
		copyPriceList(PriceListTable, null, null);
	}
	
	//Copy price list with filter
	public void copyPriceList(JTable PriceListTable, String filterSAP, String filterText){
		DefaultTableModel defaultTableModel = (DefaultTableModel) PriceListTable.getModel();
		
		clearTableModel(defaultTableModel);
		
		Vector<TextPriceListRow> pricelistRows = priceList.getRows();
		boolean selected = true;
		
		for (int i=0; i<pricelistRows.size(); i++){
			TextPriceListRow row = pricelistRows.get(i);
			//System.out.println("Filter:'" + filterSAP + "' Chk: " + row.getSAP()+ " res: " + row.getSAP().contains(filterSAP.trim()));
			if (filterSAP != null &&  filterSAP.length() > 0 && !row.getSAP().contains(filterSAP.trim())){
				selected = false;
			}
			if (filterText != null && filterText.length() > 0 &&  !row.getText().toLowerCase().contains(filterText.trim().toLowerCase())){
				selected = false;
			}
			if (selected){
				defaultTableModel.addRow(new Object[]{
						row.getSAP(),
						row.getText()
				});
			}
			selected = true;
		}
		
		selectTableRow(PriceListTable, 0);
	}
	
	private double formatDouble(Double value){
		return formatDouble(value,null);
	}
	
	private double formatDouble(String value){
		return formatDouble(null,value);
	}
	
	private double formatDouble(Double value, String strValue){
		try {
			if (strValue != null) {
				value = Double.parseDouble(strValue.trim().replace(",", "."));
			}
			String Price = formatter.format(value);
			Price = Price.replace(",",".");
			return Double.parseDouble(Price);
		} catch (Exception e) {
			System.out.println("Can't format value: " + value);
			e.printStackTrace();
			return value;
		}
	}
	
	public void checkPriceListGroups(){
		Vector<TextPriceListRow>  rows = priceList.getRows();
		List<String> notCategorizedGroups = new ArrayList<String>();
		
		for (int i =0; i <rows.size(); i++){
			TextPriceListRow row = rows.get(i);
			String group = row.getGroup();
			String groupName = groupUtil.getGroupNameByGroup(group);
			
			if (groupName == null && !notCategorizedGroups.contains(group)) {
				notCategorizedGroups.add(group);
				//System.out.println("Missing: " + group);
			}
		}
		
		Collections.sort(notCategorizedGroups);

		if (notCategorizedGroups.size() > 0){
			StringBuffer content = new StringBuffer("");
			content.append(propertyUtil.getLangText("Error.PriceList.NotCategorised") + ":\n");
			for (String group : notCategorizedGroups){
				content.append(group + "\n");
			}
			
			Viewer viewer = new Viewer(content.toString(),new ImageIcon(""));
			viewer.showErrorMessage(300,300);
		}
	}

	private Double calculateCustomPrice(String SAP) {
		return (formatDouble(rebateUtil.getCustomPriceBySAP(SAP)) * priceList.getMultiplier());
	}

	private Double calculatePrice(String SAP){
		String group = priceList.getGroup(SAP);
		// Handle if SAP is missing from pricelist (no group)
		if (group != null && group.length()>0){
			String groupName = groupUtil.getGroupNameByGroup(group);
			Double customRebate = rebateUtil.getCustomRebateByGroup(group);
			Double rebate = rebateUtil.getRebateByGroupName(groupName);
			Double listPriceMultiplied = priceList.getPriceMultiplied(SAP);

			//If custom rebate found, set the rebate to custom rebate
			if (customRebate > -1) rebate = customRebate;
			
			//Calculate price
			Double price = ((100-rebate)/100.0) * listPriceMultiplied;
			
			/*
			System.out.println("--------------------------------------------");
			System.out.println("SAP: " + SAP);
			System.out.println("Group: " + group);
			System.out.println("GroupName: " + groupName);
			System.out.println("PriceListMultiplied: " + listPriceMultiplied);
			System.out.println("Rebate: " + rebate);
			System.out.println("Price: " + price);
			*/
			return formatDouble(price);
		} else {
			return 0.0;
		}
	}
	
	public boolean isSAPExists(String SAP){
		return priceList.isSAPExists(SAP);
	}
	
	public Double getListPrice(String SAP){
		//Try to get first from custom price list
		if (rebateUtil.getCustomPriceBySAP(SAP) != null){
			return calculateCustomPrice(SAP);
		} else {
			return priceList.getPrice(SAP);
		}
	}
	
	public Double getListPriceMultiplied(String SAP){
		//Try to get first from custom price list
		if (rebateUtil.getCustomPriceBySAP(SAP) != null){
			return calculateCustomPrice(SAP);
		} else {
			//Get the pricelist, but multiplied with price list multiplier (not item count)
			return formatDouble(priceList.getPriceMultiplied(SAP));
		}
	}
	
	public Double getListPriceCalculated(String SAP){
		//Try to get first from custom price list
		if (rebateUtil.getCustomPriceBySAP(SAP) != null){
			return calculateCustomPrice(SAP);
		} else {
			//Get calculated price with rebate
			return calculatePrice(SAP);
		}
	}
	
	public String getMartikel(String SAP){
		return priceList.getMartikel(SAP);
	}
	
	public String getListText(String SAP){
		return priceList.getText(SAP);
	}
	
	public File getPriceListFile() {
		return PriceListFile;
	}

	public void setPriceListFile(File priceListFile) {
		PriceListFile = priceListFile;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getPriceListDate() {
		return priceListDate;
	}

	public void setPriceListDate(String priceListDate) {
		this.priceListDate = priceListDate;
	}
}
