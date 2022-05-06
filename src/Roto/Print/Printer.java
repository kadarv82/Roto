package Roto.Print;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintGraphics;
import java.awt.PrintJob;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Date;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;
import Roto.DataBase.DataBaseManager;
import Roto.Utils.PropertyUtil;
import Roto.Windows.WindowComponents;
import Roto.Windows.WindowTypes;

public class Printer extends Frame {
	
	private static Printer instance = null;
	private SwingConstans swc;
	private String content;
	private DataBaseManager database;
	private PropertyUtil propertyUtil;
	private int margin = 40;
	private Properties p = new Properties();
	    
	
	public static Printer getInstance() {
		if (instance == null) {
			instance = new Printer();
		}
		return instance;
	}
	
	public Printer() {
		swc = SwingConstans.getInstance();
		database = DataBaseManager.getInstance();
		propertyUtil = PropertyUtil.getInstance();
	}
	
	private String formatRight(String text, int length){
		StringBuffer sbText = new StringBuffer("");
		int addSpaces = length - text.length();
		if (addSpaces < 0) addSpaces = 0;
		
		for (int i =0; i < addSpaces; i++){
			sbText.append(" ");
		}
		
		sbText = sbText.append(text);
		
		return sbText.toString().substring(0,length);
	}
	
	private String formatLeft(String text, int length){
		StringBuffer sbText = new StringBuffer("");
		int addSpaces = length - text.length();
		if (addSpaces < 0) addSpaces = 0;
		
		for (int i =0; i < addSpaces; i++){
			sbText.append(" ");
		}
		
		return (text + sbText.toString()).substring(0,length);
	}

	
	private String getStringValue(Object object){
		String value = "";
		if (object != null){
			value = String.valueOf(object);
		}
		return value;
	}
	
	public void createPrinterContentForStockList(JTable contentTable, DefaultListModel fileList){
		StringBuffer content = new StringBuffer("");
		StringBuffer header = new StringBuffer("");
		String currentDate = swc.dateFormatter.format(new Date());
		
		//Process list date
		int pageWidth = 0;
		for (int i=0; i < swc.printContentColumnLengthsForStockList.length; i++){
			pageWidth += swc.printContentColumnLengthsForStockList[i] + 1;
		}

		content.append(formatRight(propertyUtil.getLangText("InternalFrame.StockList.Excel.Date") + ": " + currentDate + "\n", pageWidth));
		
		//Process database list
		content.append(propertyUtil.getLangText("InternalFrame.StockList.Excel.DataBaseList") + ":"+ "\n");
		content.append("\n");
		for (int i=0; i < fileList.getSize(); i++){
			content.append(((File)fileList.get(i)).getName()+"\n");
		}
		content.append("\n");
		content.append(propertyUtil.getLangText("InternalFrame.StockList.Excel.StockList") + ":"+ "\n");
		content.append("\n");
		
		
	    //Process content column header
	    for (int c=0; c<contentTable.getColumnCount(); c++){
	    	String columnTitle = contentTable.getColumnModel().getColumn(c).getHeaderValue().toString();
	    	
	    	int columnLength = swc.printContentColumnLengthsForStockList[c];
			String value = formatLeft(columnTitle,columnLength);
			if (c > 2) {
				value = formatRight(columnTitle,columnLength);
			}
			header.append(value + " ");
	    }
	    
		content.append(header.toString() + "\n\n");
		
		//Process content (works on jtable to handle sorting)
		for (int row =0; row<contentTable.getRowCount(); row++){
			
			for (int column =0; column<contentTable.getColumnCount(); column++){
				int columnLength = swc.printContentColumnLengthsForStockList[column];
				String tableVale =getStringValue(contentTable.getValueAt(row,column));
				String value = formatLeft(tableVale,columnLength);
				if (column > 2) {
					value = formatRight(tableVale,columnLength);
				}
				
				content.append(value+ " ");
			}
			
			content.append("\n");
		}
		
		this.content = content.toString();
	}
	
	public void createPrinterContentForList(JTable contentTable, JTable headerTable, String comment){
		
		StringBuffer content = new StringBuffer("");
		DefaultTableModel contentModel = (DefaultTableModel) contentTable.getModel();
		DefaultTableModel headerModel = (DefaultTableModel) headerTable.getModel();
		
		//Process header
		for (int r=0; r<headerModel.getRowCount(); r++){
			for (int c=0; c < headerModel.getColumnCount(); c++){
				int columnLength = swc.printHeaderColumnLengthsForList[c];
				String tableVale =getStringValue(headerModel.getValueAt(r,c));
				String value = formatLeft(tableVale,columnLength);
				content.append(value+ " ");
			}
			content.append("\n");
		}
		
		content.append("\n");
		
		//Process content
		for (int row =0; row<contentTable.getRowCount(); row++){
			
			for (int column =0; column<contentTable.getColumnCount(); column++){
				int columnLength = swc.printContentColumnLengthsForList[column];
				String tableVale =getStringValue(contentModel.getValueAt(row,column));
				String value = formatLeft(tableVale,columnLength);
				if (column > 2) {
					value = formatRight(tableVale,columnLength);
				}
				
				content.append(value+ " ");
			}
			
			content.append("\n");
		}
		
		//Process comment						
	    StringReader reader = new StringReader(comment);
	    BufferedReader in = new BufferedReader(reader);
	    String line;
	    
	    if (comment.trim().length() > 0){
	    	content.append("\n");
	    	content.append(propertyUtil.getLangText("InternalFrame.ExcelList.Comment") + ":");
	    
	    }
	    content.append("\n");
		    
	    try {
			while ((line = in.readLine()) != null) {
				content.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.content = content.toString();
	}	
	
	public void printContent(){
			
		 PrintJob pjob =  getToolkit().getPrintJob(Printer.this, "Roto list", p);
		 
	      if (pjob != null) {
	        Graphics pg = pjob.getGraphics();
	        if (pg != null) {
	          printLongString(pjob, pg, content);
	          pg.dispose();
	        }
	        pjob.end();
	      }
	}
	
	private void printLongString (PrintJob pjob, Graphics pg, String s) {
	      
		    int pageNum = 1;
		    int linesForThisPage = 0;
		    int linesForThisJob = 0;
		    // Note: String is immutable so won't change while printing.
		    if (!(pg instanceof PrintGraphics)) {
		      throw new IllegalArgumentException ("Graphics context not PrintGraphics");
		    }
		    StringReader sr = new StringReader(s);
		    LineNumberReader lnr = new LineNumberReader (sr);
		    String nextLine;
		    int pageHeight = pjob.getPageDimension().height - margin;
		    Font font = swc.fontPrint;
		    
		    //have to set the font to get any output
		    pg.setFont (font);
		    FontMetrics fm = pg.getFontMetrics(font);
		    int fontHeight = fm.getHeight();
		    int fontDescent = fm.getDescent();
		    int curHeight = margin;
		    boolean needPageNumber = true;
		    
		    try {
		      do {
		        nextLine = lnr.readLine(); 
		        		        
		        if (nextLine != null) {         
		          if ((curHeight + fontHeight) > pageHeight) {
		            // New Page
		        	  needPageNumber = true;
		           // System.out.println(linesForThisPage + " lines printed for page " + pageNum);
		            if(linesForThisPage == 0) {
		               System.out.println("Font is too big for pages of this size; aborting...");
		               break;
		            }
		            pageNum++;
		            linesForThisPage = 0;
		            pg.dispose();
		            pg = pjob.getGraphics();
		            if (pg != null) {
		              pg.setFont(font);
		            }
		            curHeight = margin;
		          }
		          curHeight += fontHeight;
		          if (pg != null) {
		            //Add lines to page
		        	pg.drawString(nextLine, margin, curHeight - fontDescent);
		        /*	
		        	WindowComponents wc = WindowComponents.getInstance();
		        	WindowTypes wt = WindowTypes.getInstance();
		        	BufferedImage bi = new BufferedImage(600, 600, BufferedImage.TYPE_INT_ARGB);
		            Graphics2D g2d = bi.createGraphics();
		            wc.setGraphic(g2d);
		            wc.setLocation(20, 20);
		            wc.setSize(200, 350);
		            wc.setPrinting(true);
		            wt.drawWindow();
		            pg.drawImage(bi, 20, 20, null);
		        	*/
		            //Add page number if not exist yet
			        if (needPageNumber) pg.drawString("- " + pageNum + " -" , pjob.getPageDimension().width/2 - margin/2 , pageHeight + 20);
			        needPageNumber = false;
			        
		            linesForThisPage++;
		            linesForThisJob++;
		          } 
		          else {
		            System.out.println("pg null");
		          }
		        }
		      } while (nextLine != null);
		    } 
		    catch (EOFException eof) {
		      // Fine, ignore
		    } 
		    catch (Throwable t) { // Anything else
		      t.printStackTrace();
		    }
		    /*
		    System.out.println("" + linesForThisPage + " lines printed for page " + pageNum);
		    System.out.println("pages printed: " + pageNum);
		    System.out.println("total lines printed: " + linesForThisJob);
		    */
		  }
	
}
