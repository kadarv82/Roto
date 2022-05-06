package Roto.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import Roto.DataBase.DataBaseCollectionList;
import Roto.Gui.Frames;

public class IOUtil {
	private static IOUtil instance = null;
	MessageUtil message = new MessageUtil();
	PropertyUtil propertyUtil = PropertyUtil.getInstance();
	
	public static IOUtil getInstance() {
		if (instance == null) {
			instance = new IOUtil();
		}
		return instance;
	}
	
	public void saveUTF8(String fileName, String text){
		try {
	        Writer out = new BufferedWriter(new OutputStreamWriter(
	            new FileOutputStream(fileName), "UTF8"));
	        out.write(text);
	        out.close();
	    } catch (Exception e) {
	    	message.showErrorMessage("Error saving file: " + fileName, "IO Error");
	    }
	}
	
	public void saveSimpleText(File file, String text){
		try {
	        Writer out = new BufferedWriter(new OutputStreamWriter(
	            new FileOutputStream(file)));
	        out.write(text);
	        out.close();
	    } catch (Exception e) {
	    	message.showErrorMessage("Error saving file: " + file.getName(), "IO Error");
	    }
	}
	
	public void saveUTF8(File file, String text){
		try {
	        Writer out = new BufferedWriter(new OutputStreamWriter(
	            new FileOutputStream(file), "UTF8"));
	        out.write(text);
	        out.close();
	    } catch (Exception e) {
	    	message.showErrorMessage("Error saving file: " + file.getName(), "IO Error");
	    }
	}
	
	public String loadUTF8(String fileName) {
		StringBuffer text = new StringBuffer();
		String line = null;
		try {
	        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
	        while ((line = in.readLine()) != null) {
	        	text.append(line + "\n");
	        }
	        in.close();
	    } catch (Exception e) {
	    	message.showErrorMessage("Error loading file: " + fileName, "IO Error");
	    }
	    return text.toString();
	}
	
	public void saveObject (Object obj, File fOutPut) {
	    try {
	      ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fOutPut));
	      out.writeObject(obj);
	      out.close();
	    }
	    catch (Exception ex) {
	    	message.showErrorMessage("Error saving file: " + fOutPut.getName(), "IO Error");
	    	ex.printStackTrace();
	    }
	}

	public Object openObject(File fInPut){
	    Object obj = new Object();
	    try {
	      ObjectInputStream in = new ObjectInputStream(new FileInputStream(fInPut));
	      obj=in.readObject();
	      in.close();
	    }
	    catch (Exception ex) {
	    	message.showErrorMessage("Error loading file: " + fInPut.getName(), "IO Error");
	    }
	    return obj;
	}
	
	public boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	}

	public int getLineCount(String text){
		int lineCount =0;
		try {
			StringReader reader = new StringReader(text);
		    BufferedReader in = new BufferedReader(reader);

			while ((in.readLine()) != null) {
				lineCount ++;
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lineCount;
	}
	
	public void copyFile(File in, File out) {
		try {
			FileInputStream fis = new FileInputStream(in);
			FileOutputStream fos = new FileOutputStream(out);
			try {
				byte[] buf = new byte[1024];
				int i = 0;
				while ((i = fis.read(buf)) != -1) {
					fos.write(buf, 0, i);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				if (fis != null)
					fis.close();
				if (fos != null)
					fos.close();
			}
		} catch (Exception e) {
			message.showErrorMessage(propertyUtil.getLangText("Error.Copy"), "");
		}
	}
	 
	public String getNextDirectory(String parentDirectory, String directoryName){
		File parent = new File(parentDirectory);
		File directory = new File (parentDirectory + "\\" + directoryName);
		int counter = 1;

		if (parent.exists() && directory.exists()){
			while (directory.exists() && counter < 1000){
				directory = new File(parentDirectory + "\\" + directoryName + "_" + counter);
				counter ++;
			}
			return directory.getName();
		}
		else {
			return directoryName;
		}
	}
	
	 public void copyDirectory(File sourceLocation , File targetLocation) {
		 try {  
			 	
			 	Frames.getInstance().jtExportInfo.append(targetLocation.getAbsolutePath() + "\n");
			 	Frames.getInstance().jtExportInfo.setCaretPosition(Frames.getInstance().jtExportInfo.getText().length());
			 	
		        if (sourceLocation.isDirectory()) {
		            if (!targetLocation.exists()) {
		                targetLocation.mkdir();
		            }
		            
		            String[] children = sourceLocation.list();
		            for (int i=0; i<children.length; i++) {
		                copyDirectory(new File(sourceLocation, children[i]),
		                        new File(targetLocation, children[i]));
		            }
		        } else {
		            
		            InputStream in = new FileInputStream(sourceLocation);
		            OutputStream out = new FileOutputStream(targetLocation);
		            
		            // Copy the bits from instream to outstream
		            byte[] buf = new byte[1024];
		            int len;
		            while ((len = in.read(buf)) > 0) {
		                out.write(buf, 0, len);
		            }
		            in.close();
		            out.close();
		        }
		 	} catch (Exception e) {
				message.showErrorMessage(propertyUtil.getLangText("Error.Copy"), "");
			}
	    }  

	
}
