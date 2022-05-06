package Roto.Utils;

import java.io.Console;
import java.io.File;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileSystemView;

import sun.reflect.generics.visitor.Reifier;

import Roto.Basic.SwingConstans;
import Roto.DataBase.SerializedRegistration;
import Roto.Gui.Frames;

public class RegisterUtil {
	private static RegisterUtil instance = null;
	private Frames frames;
	private PropertyUtil propertyUtil;
	private IOUtil ioUtil;
	private String registerID;
	private String currentLicencePath;
	private int nextLicenceFileIndex;
	private MessageUtil message; 
	private SwingConstans swc;
	
	public static RegisterUtil getInstance() {
		if (instance == null) {
			instance = new RegisterUtil();
		}
		return instance;
	}
	
	public RegisterUtil(){
		frames = Frames.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		ioUtil = IOUtil.getInstance();
		message = MessageUtil.getInstance();
		swc = SwingConstans.getInstance();
		setNextLicenceFileIndex();
		setCurrentLicencePath();
		
		//System.out.println("Current licence: " + getCurrentLicencePath());
	}

	public void registerProgram(boolean isDemo){

		Calendar calendar = Calendar.getInstance();
		Date dateFrom = calendar.getTime();
		//Add three year to expire (if not demo)
		if (!isDemo) calendar.add(Calendar.YEAR, 3);
		//Add one week to expire (if demo)
		else calendar.add(Calendar.DATE,7);
		
		//Set expire date
		Date dateTo = calendar.getTime();

		//Generate new key
		generateKey(frames.tfKey1, frames.tfKey2, frames.tfKey3);
		
		frames.tfCode1.setText("");
		frames.tfCode2.setText("");
		frames.tfCode3.setText("");
		
		//Save registration
		saveRegistration(new File(getCurrentLicencePath()),dateFrom, dateTo);

		//Set demo disabled 
		if (isDemo) setDemoUserDisabled();
		
		setGUIRegistered(true,isDemo);
		
	}
	
	//Get if program registered as demo (7 days interval)
	public boolean isDemo(){
		SerializedRegistration licence = getLicenceFile();
		if (licence!= null){
			Long difference = licence.getRegistrationTo().getTime() - licence.getRegistrationFrom().getTime();
			
			return (difference / (1000 * 60 * 60 * 24) == 7); 
			
		} else return false;
	}
	
	public String getCodeByKey(String key1, String key2, String key3){
		int offset1= getCharIndex(key1.charAt(0));
		int offset2= getCharIndex(key2.charAt(1));
		int offset3= getCharIndex(key3.charAt(2));
		String offSetKey1 = getOffSetKey(key1, offset1);
		String offSetKey2 = getOffSetKey(key2, offset2);
		String offSetKey3 = getOffSetKey(key3, offset3);
		
		//System.out.println(offSetKey1 + "-" + offSetKey2 + "-" +offSetKey3);
		
		return offSetKey1 + offSetKey2 + offSetKey3;
	}
	
	private String getOffSetKey(String key, int offset){
		String offSetKey = "";
		for (int i=0; i<key.length(); i++){
			char offsetChar = getOffsetChar(key.charAt(i), offset);
			offSetKey += offsetChar;
		}
		return offSetKey;
	}
	
	private char getOffsetChar(char c, int offset){
		for (int i=0; i<offset; i++){
			c++;
			if (c-1 == 'z'){
				c = 'a';
			}
			if (c-1 == 'Z'){
				c = 'A';
			}
			if (c-1 == '9'){
				c = '0';
			}
		}
		return c;
	}
	
	private int getCharIndex(char c){
		int index = 0;
		while (!String.valueOf(c).toLowerCase().equalsIgnoreCase("z")){
			c++;
			index++;
		}
		return index;
	}
	
	public void loadOrCreateCurrentRegistration(){
		File licenceFile = new File(getCurrentLicencePath());
		if (licenceFile.exists()){
			openRegistration(licenceFile);
		} else {
			generateKey(frames.tfKey1, frames.tfKey2, frames.tfKey3);
			saveRegistration(licenceFile);
		}
	}
	
	private SerializedRegistration getLicenceFile(){
		File licenceFile = new File(getCurrentLicencePath());
		if (licenceFile.exists()) return (SerializedRegistration) ioUtil.openObject(licenceFile);
		else return null;
	}
	
	//Set the current license file path for user
	private void setCurrentLicencePath(){
		File [] files = new File (swc.basicPath).listFiles();
		boolean licenceFound = false;
		for (File file : files){
			String fileName = file.getName().toLowerCase();
			if (file.isFile() && file.exists() && fileName.contains("program") && fileName.endsWith(".properties")){
				SerializedRegistration licenceFile = (SerializedRegistration) ioUtil.openObject(file);
				
				//License file found by user id
				if (licenceFile.getRegisterID().equals(getRegisterID())){
					licenceFound = true;
					this.currentLicencePath = swc.basicPath + file.getName();
				}
				
			}
		}
		
		//Generate new filename if license file not found for user
		if (!licenceFound){
			//If default license file name exists, create a new one
			if (new File(swc.defaultLicencePath).exists()) {
				this.currentLicencePath = swc.basicPath + "Program_" + getNextLicenceFileIndex() + ".properties";
			} 
			//If default license file name does not exists, return it
			else {
				this.currentLicencePath = swc.defaultLicencePath;
			}
		}
	}

	//Get the current license file path for user
	public String getCurrentLicencePath(){
		return this.currentLicencePath;
	}
	
	//Get next index for licence file
	public int getNextLicenceFileIndex() {
		return nextLicenceFileIndex;
	}

	//Set next index for licence file
	private void setNextLicenceFileIndex() {
		for (int i = 2; i < 999; i++) {
			if (!new File (swc.basicPath + "//Program_" + i +".properties").exists()){
				this.nextLicenceFileIndex = i;
				i = 999;
			}
		}
	}
	
	//Check program registration
	public void checkRegistration(){
		SerializedRegistration licence = getLicenceFile();
		//If no license file found, create new license (when the program starts, this have to be created)
		if (licence == null) {
			loadOrCreateCurrentRegistration();
			licence = getLicenceFile();
		}
		
		//License file validation
		if (isLicenceValid(licence)){
		
				//Check expire date
				Date today = new Date();
				//Not registered yet
				if (licence.getRegistrationFrom()==null || licence.getRegistrationTo() == null ){
					setGUIRegistered(false);
				}
				//Check expire date		
				if (licence.getRegistrationFrom()!=null && licence.getRegistrationTo() != null){
					
					//Expired
					if (licence.getRegistrationTo().before(today)){
						message.showInformationMessage(propertyUtil.getLangText("Information.Registration.Expired"));
						setGUIRegistered(false);
					}
					
					//Not expired, but only demo
					if (!licence.getRegistrationTo().before(today) && isDemo()){
						setGUIRegistered(true,true);
					}
					
				}
		} 
		//If not valid licence file, set demo gui
		else {
			//message.showInformationMessage(propertyUtil.getLangText("Error.Registration.InvalidLicence"));
			setGUIRegistered(false);
/*
			//Try to delete invalid licence file
			try {
				new File(swc.defaultLicencePath).delete();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		
		//Enable demo or not
		frames.jbRegisterDemo.setEnabled(isDemoUserEnabled());
		
		//setDemoUserEnanled(false);
		//removeDemoUserEnanled();
	}
	
	//Validate licence file
	private boolean isLicenceValid(SerializedRegistration licence){
		return getRegisterID().equals(licence.getRegisterID());
	}
	
	public void removeDemoUserEnanled(){
		try{
			Preferences systemPreferences = Preferences.systemRoot();
			systemPreferences.remove(swc.REGISTRY_DEMO_USER);
		} catch (Exception e) {	}
		try{
			getDemoUserFile().delete();
		} catch (Exception e) {	}
		try{
			SerializedRegistration licence = getLicenceFile();
			licence.setDemoDisabled(false);
			File licenceFile = new File(getCurrentLicencePath());
			ioUtil.saveObject(licence, licenceFile);
		} catch (Exception e) {	}
	}
	
	public String getUserDirectory(){
		return (System.getProperty("user.home")+"/").toString();
	}
	
	public File getDemoUserFile(){
		return new File(getUserDirectory()+"User.dat");
	}
	
	public boolean isRegistryAccess(){
		try {
			Preferences systemPreferences = Preferences.systemRoot();
			systemPreferences.put("Java_Write_Test", "TestValue");
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isUserPathAccess(){
		return new File(getUserDirectory()).canWrite();
	}
	
	private void setDemoUserDisabled(){
		//Try to write to registry
		if (isRegistryAccess()){
			Preferences systemPreferences = Preferences.systemRoot();
			systemPreferences.put(swc.REGISTRY_DEMO_USER, String.valueOf("false"));
			//System.out.println("Write to registry");
		} 
		//Try to write to user folder if registry is not success
		else if (isUserPathAccess()) {
			ioUtil.saveUTF8(getDemoUserFile(), getDemoUserFile().getAbsolutePath());
			//System.out.println("Write to user path");
		}
		//Set demo disabled to licence file
		else {
			SerializedRegistration licence = getLicenceFile();
			
			licence.setDemoDisabled(true);
			
			File licenceFile = new File(getCurrentLicencePath());
			ioUtil.saveObject(licence, licenceFile);
			//System.out.println("Write to licence ");
		}
		
		frames.jbRegisterDemo.setEnabled(false);
	}
	
	public boolean isDemoUserEnabled(){
		//Read from registry if access
		if (isRegistryAccess()) {
			//System.out.println("Load from registry");
			Preferences systemPreferences = Preferences.systemRoot();
			String demoUserEnabled = systemPreferences.get("DemoUserEnabled",null);
	
			if (demoUserEnabled != null){
				if (demoUserEnabled.toLowerCase().equalsIgnoreCase("true")) return true;
				else return false;
			} else return true;
		}
		//Read from user path if access
		else if (isUserPathAccess()){
			//System.out.println("Load from user path");
			return !getDemoUserFile().exists();
		} 
		//Read from licence file
		else {
			//System.out.println("Load from licence file ");
			return !getLicenceFile().isDemoDisabled();
		}
	}
	
	
	public void setGUIRegistered(boolean isRegistered){
		setGUIRegistered(isRegistered, false);
	}
	
	public void setGUIRegistered(boolean isRegistered, boolean onlyDemo){
		
		frames.jmList.setEnabled(isRegistered);
		frames.jmDb.setEnabled(isRegistered);
		frames.jbOpenDb.setEnabled(isRegistered);
		frames.jbOpenListDb.setEnabled(isRegistered);
		
		if (!onlyDemo){
			frames.jbSaveExcelList.setEnabled(isRegistered);
			frames.jbPrintExcelList.setEnabled(isRegistered);	
		} else {
			frames.jbSaveExcelList.setEnabled(false);
			frames.jbPrintExcelList.setEnabled(false);
		}
		
		if (!isRegistered){
			frames.mainTabPane.setSelectedIndex(swc.tabMenus);
			frames.jriRegister.setVisibleToFocus();
		} else {
			frames.mainTabPane.setSelectedIndex(swc.tabLogin);
			frames.jriRegister.setVisible(false);
		}
	}
	
	public void setRegistrationInfo(JTextPane textArea){
		SerializedRegistration registrationInfo = getLicenceFile();
		textArea.setText("");
		StringBuffer content = new StringBuffer("");
		Date today = new Date();
		
		if (registrationInfo != null){
			//Check expire date		
			if (registrationInfo.getRegistrationFrom()!=null && registrationInfo.getRegistrationTo() != null){
				content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.Status") + " : " +  propertyUtil.getLangText("InternalFrame.RegisterInfo.Status.Registered")  + "\n");
				//Expired
				if (registrationInfo.getRegistrationTo().before(today)){
					content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.Validation") + " : " +  propertyUtil.getLangText("InternalFrame.RegisterInfo.Validation.Expired")  + "\n");
				}
				//Active
				else {
					content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.Validation") + " : " +  propertyUtil.getLangText("InternalFrame.RegisterInfo.Validation.Live")  + "\n");					
				}
				
				//Dates
				content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.Start") + " : " +  swc.dateFormatter.format(registrationInfo.getRegistrationFrom()) + "\n");
				content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.End") + " : " +  swc.dateFormatter.format(registrationInfo.getRegistrationTo()) + "\n");
				content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.ToDay") + " : " +  swc.dateFormatter.format(today) + "\n");
				
				long difference = registrationInfo.getRegistrationTo().getTime() - today.getTime();
				if (difference < 0) difference = 0;
				
				content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.LeftTime") + " : " + (difference / (1000 * 60 * 60 * 24)) + " "  + propertyUtil.getLangText("InternalFrame.RegisterInfo.Day")  +"\n");
					
			} else {
				content.append("   " + propertyUtil.getLangText("InternalFrame.RegisterInfo.Status") + " : " +  propertyUtil.getLangText("InternalFrame.RegisterInfo.Status.NotRegistered")  + "\n");		
			}
			
			//Comany data
			content.append("\n");
			content.append("   " + propertyUtil.getLangText("InternalFrame.Register.Form.CompanyName") + " : " + registrationInfo.getCompanyName()+"\n");
			content.append("   " + propertyUtil.getLangText("InternalFrame.Register.Form.Address") + " : " + registrationInfo.getCompanyAddress()+"\n");
			content.append("   " + propertyUtil.getLangText("InternalFrame.Register.Form.Operator") + " : " + registrationInfo.getOperator()+"\n");
			content.append("   " + propertyUtil.getLangText("InternalFrame.Register.Form.Email") + " : " + registrationInfo.getEmail()+"\n");
			content.append("   " + propertyUtil.getLangText("InternalFrame.Register.Form.SalesRep") + " : " + registrationInfo.getSalesMan()+"\n");
			content.append("   " + propertyUtil.getLangText("InternalFrame.Register.Form.Comment") + " : " + registrationInfo.getComment()+"\n");
		
			
		} else {
			content.append("\n\n                       "+ propertyUtil.getLangText("InternalFrame.RegisterInfo.Missing"));
		}
		
		
		textArea.setText(content.toString());
		textArea.setCaretPosition(0);
	}
	
	public void openRegistration(File registrationFile){
		SerializedRegistration registration;
		registration = (SerializedRegistration) ioUtil.openObject(registrationFile);
		if (getRegisterID().equals(registration.getRegisterID())){
			if (registration.isLicenceFile()){
				frames.tfKey1.setText(registration.getKey1());
				frames.tfKey2.setText(registration.getKey2());
				frames.tfKey3.setText(registration.getKey3());
				//System.out.println(registration.getRegistrationFrom());
				//System.out.println(registration.getRegistrationTo());

			}
			frames.tfCode1.setText(registration.getCode1());
			frames.tfCode2.setText(registration.getCode2());
			frames.tfCode3.setText(registration.getCode3());
			frames.tfRegComment.setText(registration.getComment());
			frames.tfRegAddress.setText(registration.getCompanyAddress());
			frames.tfRegCompany.setText(registration.getCompanyName());
			frames.tfRegEmail.setText(registration.getEmail());
			frames.tfRegOperator.setText(registration.getOperator());
			frames.tfRegSalesRep.setText(registration.getSalesMan());
		} else {
			if (!registration.isLicenceFile()) message.showErrorMessage(propertyUtil.getLangText("Error.Registration.InvalidForm"),"");
		}
	}
	
	public void saveRegistration(File registrationFile){
		saveRegistration(registrationFile, null, null);
	}
	
	public void saveRegistration(File registrationFile, Date dateFrom, Date dateTo){
		SerializedRegistration registration = new SerializedRegistration();
		File licenceFile = new File(getCurrentLicencePath());
		
		registration.setKey1(frames.tfKey1.getText());
		registration.setKey2(frames.tfKey2.getText());
		registration.setKey3(frames.tfKey3.getText());
		registration.setCode1(frames.tfCode1.getText());
		registration.setCode2(frames.tfCode2.getText());
		registration.setCode3(frames.tfCode3.getText());
		registration.setComment(frames.tfRegComment.getText());
		registration.setCompanyAddress(frames.tfRegAddress.getText());
		registration.setCompanyName(frames.tfRegCompany.getText());
		registration.setRegisterID(getRegisterID());
		registration.setEmail(frames.tfRegEmail.getText());
		registration.setOperator(frames.tfRegOperator.getText());
		registration.setSalesMan(frames.tfRegSalesRep.getText());
		registration.setLicenceFile(false);
		registration.setDemoDisabled(!frames.jbRegisterDemo.isEnabled());
		
		//Save date when program is registered (set date from input params)
		if (dateFrom != null){ registration.setRegistrationFrom(dateFrom); }
		if (dateTo != null){ registration.setRegistrationTo(dateTo); }
		
		//Load date from licence file if exists (to not delete date if program is registered)
		if (dateFrom == null && getLicenceFile() != null){
			registration.setRegistrationFrom(getLicenceFile().getRegistrationFrom());
		}
		if (dateTo == null && getLicenceFile() != null){
			registration.setRegistrationTo(getLicenceFile().getRegistrationTo());
		}
		
		//Save registration form
		ioUtil.saveObject(registration, registrationFile);
		
		//Save licence file to store form modifications
		registration.setLicenceFile(true);
		
		//System.out.println("saveReg from: " +registration.getRegistrationFrom());
		//System.out.println("saveReg to: " + registration.getRegistrationTo());
		
		//Save licence file
		ioUtil.saveObject(registration, licenceFile);
	}

	public void generateKey(JTextField key1, JTextField key2, JTextField key3){
		StringBuffer key = new StringBuffer("");
		for (int i=0; i <3; i++){
			for (int j=0; j <5; j++){
				key.append(getRandomChar());
			}
			if (i==0) key1.setText(key.toString());
			else if (i==1) key2.setText(key.toString());
			else if (i==2) key3.setText(key.toString());
			key = new StringBuffer("");
		}
	}
	
	private String getRandomChar(){
		char randomChar = (char)((int)'A'+Math.random()*((int)'Z'-(int)'A'+1));
		int randomNumber = (int)(Math.random()*3)+1;
		if (randomNumber == 1) return String.valueOf(randomChar).toLowerCase();
		else if (randomNumber == 2) return String.valueOf(randomChar);
		else return String.valueOf((int)(Math.random()*9)+1);
	}
	
	private String getComputerName(){
		String computerName = null;
		try{
	      computerName=InetAddress.getLocalHost().getHostName();
	    }catch (Exception e){
	      System.out.println(e.getMessage());
	    }
	    return computerName;
	}
	
	private String getTotalDiskSize(){
		File checker = new File(swc.MaterialGroupPath);
		return String.valueOf(checker.getTotalSpace());
	}
	
	public String getUserName(){
		String userHome = System.getProperty("user.home");
		String userName = "user";
		int index = userHome.lastIndexOf("\\");
		if (index > -1) {
			try{
				userName = userHome.substring(index+1);
			} catch (Exception e) {}
		}
		return userName; 
	}
	
	public String getRegisterID() {
		return getComputerName() + "-" + getUserName() + "-" + getTotalDiskSize();
	}

	public void setRegisterID(String registerID) {
		this.registerID = registerID;
	}
}
