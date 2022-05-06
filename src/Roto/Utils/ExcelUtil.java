package Roto.Utils;

import java.io.BufferedReader;
import java.io.File; 
import java.io.IOException;
import java.io.StringReader;
import java.util.Date; 
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;
import Roto.DataBase.DataBaseManager;
import Roto.DataBase.TextDBRow;
import Roto.Gui.DataBaseTableModel;

import jxl.*; 
import jxl.format.Alignment;
import jxl.write.*; 
import jxl.write.Number;

public class ExcelUtil {
	private static ExcelUtil instance = null;
	private MessageUtil message;
	private PropertyUtil propertyUtil;
	private DataBaseManager dataBase;
	private TextDBRow tableRow;
	private SwingConstans swc;

	public static ExcelUtil getInstance() {
		if (instance == null) {
			instance = new ExcelUtil();
		}
		return instance;
	}
	
	public ExcelUtil (){
		message = MessageUtil.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		dataBase = DataBaseManager.getInstance();
		swc = SwingConstans.getInstance();
	}
	
	// Save excel database
	public void saveExcelDataBase(File outPutFile, JTable table){
		try {
			DataBaseTableModel dbModel = (DataBaseTableModel) table.getModel();
			tableRow = new TextDBRow();
			String header = swc.excelDataBaseHeaer;
			
			WritableWorkbook workbook = Workbook.createWorkbook(outPutFile);
			WritableSheet sheet = workbook.createSheet((outPutFile.getName()+"        ").substring(0,8)+"...", 0);
			
			//Create header
			StringTokenizer tokenizer = new StringTokenizer(header,"|");
			int colIndex = 0;
			while (tokenizer.hasMoreTokens()){
				String headerValue = tokenizer.nextToken();
				if (colIndex ==0) {
					headerValue = dataBase.getEditingType() + headerValue;
				}
				sheet.addCell(new Label(colIndex, 0, headerValue));
				colIndex ++;
			}
			
			//Set column width
			CellView cellView = new CellView();
			
			//Set font
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD); 
			WritableCellFormat cellFormat = new WritableCellFormat();
			cellFormat.setFont(font);

			int [] DataBaseCellWidths = {700,2220,3500,700,11700,2220,2220,2220,2220,700,1000,2220,
					2220,2220,2220,2220,2220,3260,3260,2220,2220,2220,2220,1680,2220};
			//Create content 
			for (int row=0; row<dbModel.getRowCount(); row++){
				int columnIndex=0;
				
				for (int col=0; col<dbModel.getColumnCount(); col++){

					String rowData = dbModel.getValueAt(row, col).toString().trim();
					 if (rowData.equals("true")) rowData ="IGAZ";
					 if (rowData.equals("false")) rowData ="HAMIS";
					 
					 if (columnIndex == 23){
						cellFormat = new WritableCellFormat();
						cellFormat.setFont(font);
						cellFormat.setAlignment(Alignment.RIGHT);
						if (rowData.equals("IGAZ")) {rowData =" IGAZ";}
					 } else {
						cellFormat = new WritableCellFormat();
						cellFormat.setFont(font);
						cellFormat.setAlignment(Alignment.LEFT); 
					 }
					 
					 sheet.addCell(new Label(columnIndex, row+1, rowData, cellFormat));
					 
					 //Set width
					 int textLength = tableRow.createRowItem(swc.propertyColumnNames[col], rowData).length();
					 cellView.setSize(DataBaseCellWidths[columnIndex]);
					 sheet.setColumnView(columnIndex,cellView);
									 
					 if (col==1) {
						columnIndex++;
						textLength = tableRow.createRowItem("MARTIKEL", "").length();
						cellView.setSize(DataBaseCellWidths[columnIndex]);
						sheet.setColumnView(columnIndex,cellView);
					 }
					 if (col==17) {
						columnIndex++;
						textLength = tableRow.createRowItem("ANZEIGEN", "").length();
						cellView.setSize(DataBaseCellWidths[columnIndex]);
						sheet.setColumnView(columnIndex,cellView);
					 }
					 
					
					 //System.out.println("LEN ("+ col  +"): " + textLength);					 
					 columnIndex++;
				}
				
			}
			
			workbook.write(); 
			workbook.close();
			message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.Save.Ready"));
			
		} catch (Exception e) {
			message.showErrorMessage(propertyUtil.getLangText("Error.Excel.Save"), "");
			e.printStackTrace();
		}
	}
	
	public void saveExcelList(File excelFile, JTable contentTable, JTable headerTable, String comment){
		try {
			DefaultTableModel header = (DefaultTableModel) headerTable.getModel();
			DefaultTableModel content = (DefaultTableModel) contentTable.getModel();
			
			WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
			WritableSheet sheet = workbook.createSheet("List", 0);
			
			WritableFont fontBold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD); 
			//fontBold.setColour(Color.RED);
			
			//Set column width
			CellView cellView = new CellView();
			cellView.setSize(1000);
			sheet.setColumnView(0,cellView);
			cellView.setSize(80000); 
			sheet.setColumnView(3,cellView);
			cellView.setSize(4000); 
			sheet.setColumnView(4,cellView);
			sheet.setColumnView(5,cellView);
			sheet.setColumnView(6,cellView);
						
			int headerRowStart =1;
			int colStart =1;
			int contentRowStart = headerRowStart + header.getRowCount() +1;
			int commentRowStart = contentRowStart + content.getRowCount() +2;
			
			//Process header
			for (int r=0; r<header.getRowCount(); r++){
				
				for (int c=0; c < header.getColumnCount(); c++){
					WritableCellFormat cellFormat = new WritableCellFormat();
					int colIndex = c+colStart;
					if (c==1) colIndex++;
					if (c==0) cellFormat.setFont(fontBold);
					sheet.addCell(new Label(colIndex, r+headerRowStart, header.getValueAt(r,c).toString(), cellFormat));
				}
			}
			
		    //Process content column header
			/*Deprecated
		    for (int c=0; c<content.getColumnCount(); c++){
		    	WritableCellFormat cellFormat = new WritableCellFormat(fontBold);
		    	String columnTitle = contentTable.getColumnModel().getColumn(c).getHeaderValue().toString();
		    	sheet.addCell(new Label(c + colStart, contentRowStart, columnTitle , cellFormat));
		    }
		    */
		    contentRowStart++;
		    
		    //Process content			
			for (int r=0; r<content.getRowCount(); r++){
				for (int c=0; c < content.getColumnCount(); c++){
					WritableCellFormat cellFormat = new WritableCellFormat();
					if (c>2) {cellFormat.setAlignment(Alignment.RIGHT);}
					else {cellFormat.setAlignment(Alignment.LEFT);}
					if (c==5){cellFormat.setFont(fontBold);}
					
					if (content.getValueAt(r,c) != null){
						if (c==2){
							sheet.addCell(new Label(c+colStart, r+contentRowStart, content.getValueAt(r,c).toString(), cellFormat));
						} else {
							//Try to add as number
							try{
								String value = contentTable.getValueAt(r,c).toString().trim().replace(",", ".");
								Double doubleValue = Double.parseDouble(value);
								
								sheet.addCell(new Number(c+colStart, r+contentRowStart, doubleValue , cellFormat));
								
							} catch (Exception e) {
								sheet.addCell(new Label(c+colStart, r+contentRowStart, contentTable.getValueAt(r,c).toString(), cellFormat));
							}
						}
					}

				}
				
			}
			
			//Process comment						
		    StringReader reader = new StringReader(comment);
		    BufferedReader in = new BufferedReader(reader);
		    String line;
		    
		    if (comment.trim().length() > 0){
		    	WritableCellFormat cellFormat = new WritableCellFormat(fontBold);
		    	sheet.addCell(new Label(colStart, commentRowStart, propertyUtil.getLangText("InternalFrame.ExcelList.Comment") + ":", cellFormat));
		    
		    }
		    
		    commentRowStart ++;
		    int row = 0;
		    
		    while ((line = in.readLine()) != null) {
		    	sheet.addCell(new Label(colStart, commentRowStart + row, line));
		    	row++;
		    }

			workbook.write(); 
			workbook.close();
			
			message.showInformationMessage(propertyUtil.getLangText("Information.ExcelList.Saved"));
			
		} catch (Exception e) {
			message.showErrorMessage(propertyUtil.getLangText("Error.Excel.Save"), "");
			e.printStackTrace();
		}
		
	}
	

	public void saveExcelStockList(File excelFile, JTable contentTable, DefaultListModel fileList){
		try {
			
			WritableWorkbook workbook = Workbook.createWorkbook(excelFile);
			WritableSheet sheet = workbook.createSheet("Stock list", 0);
			
			WritableFont fontBold = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			WritableCellFormat cellFormatBold = new WritableCellFormat(fontBold);
			//fontBold.setColour(Colour.RED);
			
			//Set column width
			CellView cellView = new CellView();
			cellView.setSize(2000);
			sheet.setColumnView(0,cellView);
			cellView.setSize(80000); 
			sheet.setColumnView(2,cellView);
			cellView.setSize(2500); 
			sheet.setColumnView(3,cellView);
			
			int fileListStartRow = 3;
			int headerStartRow = fileListStartRow + fileList.size() + 3;
			int contentStartRow = headerStartRow + 1;
			
		    sheet.addCell(new Label(0, 1, propertyUtil.getLangText("InternalFrame.StockList.Excel.DataBaseList") + ":", cellFormatBold));
		    sheet.addCell(new Label(3, 1, propertyUtil.getLangText("InternalFrame.StockList.Excel.Date") + ":", cellFormatBold));
		    sheet.addCell(new Label(3, 2, swc.dateFormatter.format(new Date())));
		    
		    //Process file list
		    for (int i=0; i < fileList.size(); i++){
		    	sheet.addCell(new Label(0, i+fileListStartRow, ((File)fileList.get(i)).getName()));
		    }
		    
		    sheet.addCell(new Label(0, fileListStartRow + fileList.size() + 1, propertyUtil.getLangText("InternalFrame.StockList.Excel.StockList") + ":",cellFormatBold));
		    
		    //Process content column header
		    for (int c=0; c<contentTable.getColumnCount(); c++){
		    	
		    	String columnTitle = contentTable.getColumnModel().getColumn(c).getHeaderValue().toString();
		    	sheet.addCell(new Label(c, headerStartRow, columnTitle , cellFormatBold));
		    }
		    
		    //Process content			
			for (int r=0; r<contentTable.getRowCount(); r++){
				for (int c=0; c < contentTable.getColumnCount(); c++){
					WritableCellFormat cellFormat = new WritableCellFormat();
					if (c>2) {
						cellFormat.setAlignment(Alignment.RIGHT);
						cellFormat.setFont(fontBold);
					}
					else {cellFormat.setAlignment(Alignment.LEFT);}
					if (c==5){}
					
					if (contentTable.getValueAt(r,c) != null){
						if (c==2){
							sheet.addCell(new Label(c, r + contentStartRow, contentTable.getValueAt(r,c).toString(), cellFormat));
						} else {
							//Try to add as number
							try{
								String value = contentTable.getValueAt(r,c).toString().trim().replace(",", ".");
								Double doubleValue = Double.parseDouble(value);
								
								sheet.addCell(new Number(c, r + contentStartRow, doubleValue , cellFormat));
								
							} catch (Exception e) {
								sheet.addCell(new Label(c, r + contentStartRow, contentTable.getValueAt(r,c).toString(), cellFormat));
							}
						}
					}

				}
				
			}
			
			workbook.write(); 
			workbook.close();
			
			message.showInformationMessage(propertyUtil.getLangText("Information.ExcelList.Saved"));
			
		} catch (Exception e) {
			message.showErrorMessage(propertyUtil.getLangText("Error.Excel.Save"), "");
			e.printStackTrace();
		}
		
	}

}
