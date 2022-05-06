package Roto.Utils;

import javax.swing.JOptionPane;

import Roto.Gui.Frames;

public class MessageUtil {
	private static MessageUtil instance = null;
	private String sTitle;
	private String sMessage;
	
	
	public static MessageUtil getInstance() {
		if (instance == null) {
			instance = new MessageUtil();
		}
		return instance;
	}
	
	public MessageUtil(){
		this.sMessage = "";
		this.sTitle = "";
	}
	
	public boolean showConfirmDialog(String sQuestion){
		 int option =  JOptionPane.showConfirmDialog(null, sQuestion, "",JOptionPane.YES_NO_OPTION);
		 return option == JOptionPane.YES_OPTION;
	}
	
	public void showInformationMessage(String sMessage){
		 showInformationMessage(sMessage,null);
	}
	
	public void showInformationMessage(String sMessage, String sTitle){
		 JOptionPane.showMessageDialog(null,sMessage, sTitle, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showPlainMessage(String sMessage, String sTitle){
		 JOptionPane.showMessageDialog(null,sMessage, sTitle, JOptionPane.PLAIN_MESSAGE);
	}
	
	public void showErrorMessage(String sMessage, String sTitle){
		 JOptionPane.showMessageDialog(null,sMessage, sTitle, JOptionPane.ERROR_MESSAGE);
	}
	
		
}
