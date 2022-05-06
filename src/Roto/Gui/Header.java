package Roto.Gui;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;
import Roto.DataBase.DataBaseManager;
import Roto.Utils.PriceUtil;
import Roto.Utils.PropertyUtil;

public class Header {
	private SwingConstans swc;
	private PropertyUtil propertyUtil;
	private PriceUtil priceUtil;
	private String dateToday;
	private DataBaseManager database;
	
	public Header(){
		propertyUtil = PropertyUtil.getInstance();
		priceUtil = PriceUtil.getInstance();
		swc = new SwingConstans();
		dateToday = swc.dateFormatter.format(new Date());
		database = DataBaseManager.getInstance();
	}
	
	public void createExcelHeader(JTable table, String headerType){
		
		DefaultTableModel tablemodel = (DefaultTableModel)table.getModel();
		
		
		tablemodel = new DefaultTableModel() {
			public boolean isCellEditable(int row, int col) {
				if (col == 0 || row > 4) return false;
				return true; 
			}
		};
		
		if (headerType.equals(swc.HEADER_OFFER)){
			tablemodel = new DefaultTableModel() {
				public boolean isCellEditable(int row, int col) {
					if (col == 1 && row == 6) return true;
					if (col == 0 || row > 4) return false;
					return true; 
				}
			};	
		}
		
		tablemodel.addColumn("Header data");
		tablemodel.addColumn("Header value");
		
		String [] headerData = swc.headerDataOrder;
		if (headerType.equals(swc.HEADER_OFFER)) headerData = swc.headerDataOffer;
		
		for (int i=0; i< headerData.length; i++){
			//Get data name
			String Data = propertyUtil.getLangText("InternalFrame.ExcelList.Header." + headerData[i])+":";
			
			//Get default value if exist
			String Value = propertyUtil.getLangText("InternalFrame.ExcelList.Header." + headerData[i] + ".Default");
		
			if (headerData[i].equals("Date")){Value = dateToday;}
			if (headerData[i].equals("PricelistDate")){Value = priceUtil.getPriceListDate();}
			if (headerData[i].equals("Total")){Value = swc.formatter.format(database.getTotalPrice()) + " " + priceUtil.getCurrency() + " " + propertyUtil.getLangText("InternalFrame.CollectionList.Vat");}
			if (headerData[i].equals("Addressee") && headerType.equals(swc.HEADER_OFFER)){Value = "";}
			if (headerData[i].equals("Email") && headerType.equals(swc.HEADER_OFFER)){Value = "";}
			
			if (Value.startsWith("InternalFrame.E")){
				Value = "";
			}
			
			tablemodel.addRow(new Object[]{Data, Value});
		}
		
		table.setModel(tablemodel);
		table.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		table.getColumnModel().getColumn(0).setPreferredWidth(110);
		table.getColumnModel().getColumn(1).setPreferredWidth(350);

		table.setShowHorizontalLines(false);
		table.setRowSelectionAllowed(false);
		table.setRowMargin(0);
		
		/*
		boolean isSelected = table.isCellSelected(1, 1); 
		DefaultTableCellRenderer defRender = (DefaultTableCellRenderer)  table.getCellRenderer(1, 1); 
		Component cellRenderer = defRender.getTableCellRendererComponent(table, 
		                 "Dátum", isSelected, false, 1, 1); 
		         cellRenderer.setBackground(Color.blue); 
		*/
		
	}
	
}
