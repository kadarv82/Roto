package Roto.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;

import Roto.Basic.SwingConstans;
import Roto.Gui.Frames;
import Roto.Gui.Tree.CheckNode;

public class UserUtil {
	private static UserUtil instance = null;
	private Frames frames;
	private PropertyUtil propertyUtil;
	private SwingConstans swc;
	private MessageUtil message;
	private boolean isLoggedin;
	private IOUtil ioUtil;
	private String UserName;
	
	public static UserUtil getInstance() {
		if (instance == null) {
			instance = new UserUtil();
		}
		return instance;
	}
	
	public UserUtil() {
		frames = Frames.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		swc = SwingConstans.getInstance();
		message = MessageUtil.getInstance();
		ioUtil = IOUtil.getInstance();
		this.UserName = "";
	}
	
	public void loginUser(String UserName){
		if (UserName.length()>0 && !UserName.toLowerCase().trim().equalsIgnoreCase("default")) {
			//Set last logged in user
			propertyUtil.setUserProperty("Users.LastLogin", UserName.trim());
			
			UserName = UserName.toLowerCase().trim();

			//Create new user if not exists
			if (loadUser(UserName).equals("") && message.showConfirmDialog(propertyUtil.getLangText("Confirm.CreateNewUser"))) {
				//Create new user
				propertyUtil.setUserProperty("Users." + UserName + ".Name", UserName);
			}

			//Login user, if username exists
			if (!loadUser(UserName).equals("")){
				//Fill profile combo
				fillProfileCombo(UserName);
				
				this.UserName = UserName;
				
				setGUILogin();
			}

		} else if (UserName.toLowerCase().trim().equalsIgnoreCase("default")){
			message.showErrorMessage(propertyUtil.getLangText("Alert.UserName.Default.Message"), propertyUtil.getLangText("Alert.Input.Error.Title"));
		} else {
			message.showErrorMessage(propertyUtil.getLangText("Alert.UserName.Empty.Message"), propertyUtil.getLangText("Alert.Input.Error.Title"));
		}
		
	}
	
	public List <String> getUserList(){
		List <String> userList = new ArrayList<String>();
		
		Properties profileProperty = propertyUtil.getUserProperty();
		
		Enumeration enumeration = profileProperty.keys();
		while (enumeration.hasMoreElements()){
			String key = enumeration.nextElement().toString();
			String value = profileProperty.getProperty(key);
			if (!key.toLowerCase().contains("lastlogin")){
				userList.add(value);
			}
		}
		
		return userList;
	}
	
	public List <String> getProfileList(String UserName){
		List <String> profileList = new ArrayList<String>();
		
		Properties profileProperty = propertyUtil.getProfileProperty();
		Enumeration enumeration = profileProperty.keys();
		while (enumeration.hasMoreElements()){
			String key = enumeration.nextElement().toString();
			String value = profileProperty.getProperty(key);
			if (value.toLowerCase().trim().equalsIgnoreCase(UserName.toLowerCase().trim())){
				StringTokenizer tokenizer = new StringTokenizer(key);
				tokenizer.nextToken(".");
				profileList.add(tokenizer.nextToken("."));
			}
		}

		return profileList;
	}
	
	public void deleteUser(String userName){
		//delete profiles
		List <String> profiles = getProfileList(userName);
		for (String profile : profiles ){
			deleteProfile(profile);
		}
		//delete user
		propertyUtil.removeUserProperty("Users." + userName + ".Name");
	}
	
	public boolean isProfileExists(String profileName){
		String value = propertyUtil.getProfileProperty().getProperty("Profiles."+ profileName.toLowerCase() +".Directory"); 
		return !(value == null || value.equals(""));
	}
	
	public boolean isUserExists(String userName){
		String value = propertyUtil.getUserProperty().getProperty("Users." + userName + ".Name"); 
		return !(value == null || value.equals(""));
	}
	
	public void modifyProfileName(String oldProfileName, String newProfileName){
		if (!isProfileExists(newProfileName)){
			Properties profileProperty = propertyUtil.getProfileProperty();
			
			//Add new values
			propertyUtil.setProfileProperty("Profiles."+ newProfileName +".User", profileProperty.getProperty("Profiles."+ oldProfileName +".User").toString());
			propertyUtil.setProfileProperty("Profiles."+ newProfileName +".Directory", profileProperty.getProperty("Profiles."+ oldProfileName +".Directory").toString());
			
			//Remove old values
			propertyUtil.removeProfileProperty("Profiles."+ oldProfileName +".Directory");
			propertyUtil.removeProfileProperty("Profiles."+ oldProfileName +".User");

		}
		else {
			System.out.println("Profile '" + newProfileName + "' allready exists !");
		}
		
	}
	
	public void addNewUser(String userName){
		//Create new user
		propertyUtil.setUserProperty("Users." + userName + ".Name", userName);
	}
	
	public void modifyUserName(String oldUserName, String newUserName){
		
		if (!isUserExists(newUserName)){
			Properties userProperty = propertyUtil.getUserProperty();
			
			//Add new values
			propertyUtil.setUserProperty("Users." + newUserName + ".Name", newUserName);
			
			//Remove old values
			propertyUtil.removeUserProperty("Users." + oldUserName + ".Name");
			
			//Set values for profiles
			Properties profileProperty = propertyUtil.getProfileProperty();
			Enumeration enumeration = profileProperty.keys();
			
			while (enumeration.hasMoreElements()){
				String key = enumeration.nextElement().toString();
				String value = profileProperty.getProperty(key);
				
				if (key.contains(".User") && value.toLowerCase().equalsIgnoreCase(oldUserName.toLowerCase()) ){
					propertyUtil.setProfileProperty(key, newUserName);
				}
			}
			
		}
		else {
			System.out.println("User '" + newUserName + "' allready exists !");
		}
	}
	
	//Build root for profile modul
	public CheckNode buildProfileRoot(CheckNode root){
		root.removeAllChildren();
		root.setImageIconDefault(new ImageIcon(swc.iconPath+("Profiles.jpg")));
		root.setImageIconClosed(new ImageIcon(swc.iconPath+("Profiles.jpg")));
		root.setImageIconOpened(new ImageIcon(swc.iconPath+("Profiles.jpg")));
		
		//Get users
		List<String> userList = getUserList();
		
		for (String user : userList){
			
			CheckNode userNode = new CheckNode(user);
			userNode.setSelectionMode(CheckNode.SINGLE_SELECTION);
			userNode.setCheckBoxEnabled(false);
			userNode.setImageIconDefault(new ImageIcon(swc.iconPath+("TreeProfiles.jpg")));
			userNode.setImageIconClosed(new ImageIcon(swc.iconPath+("TreeProfiles.jpg")));
			userNode.setImageIconOpened(new ImageIcon(swc.iconPath+("TreeProfiles.jpg")));
			
			root.add(userNode);
			
			//Get profiles by users
			List<String> profileList = getProfileList(user);	
			for (String profile : profileList){
				
				CheckNode profileNode = new CheckNode(profile);
				profileNode.setImageIconDefault(new ImageIcon(swc.iconPath+("TreeProfile.jpg")));
				userNode.add(profileNode);
			}
		}
		return root;
	}
	
	public void deleteProfile(String profileName){
		String profileDirectoryKey = "Profiles." + profileName + ".Directory";
		String profileRebateDirectory = "Profiles." + profileName.toLowerCase() + ".RebateFile";
		String profileUserKey = "Profiles." + profileName + ".User";
		
		Properties profileProperty = propertyUtil.getProfileProperty();
		
		//Delete profile folder
		File profileDirectory = new File(profileProperty.getProperty(profileDirectoryKey));
		ioUtil.deleteDirectory(profileDirectory);
		
		//Delete profile from property
		propertyUtil.removeProfileProperty(profileDirectoryKey);
		propertyUtil.removeProfileProperty(profileRebateDirectory);
		propertyUtil.removeProfileProperty(profileUserKey);
		
		//System.out.println(profileDirectory.getAbsolutePath());
	}
	
	public void fillProfileCombo(String UserName){
		String ProfileName = frames.tfProfile.getText().trim();
		frames.jcbProfiles.removeAllItems();
		Properties profileProperty = propertyUtil.getProfileProperty();
		Enumeration enumeration = profileProperty.keys();
		while (enumeration.hasMoreElements()){
			String key = enumeration.nextElement().toString();
			String value = profileProperty.getProperty(key);
			if (value.toLowerCase().trim().equalsIgnoreCase(UserName.toLowerCase().trim())){
				StringTokenizer tokenizer = new StringTokenizer(key);
				tokenizer.nextToken(".");
				frames.jcbProfiles.addItem(tokenizer.nextToken("."));
			}
		}
		frames.tfProfile.setText(ProfileName);
	}
	
	private String loadUser(String UserName) {
		//Load existing user
		return propertyUtil.getStringProperty(propertyUtil.getUserProperty(), "Users." + UserName + ".Name");
	}
	
	public void logoutUser() {
		setGUILogout();
		this.UserName = "";
	}
	
	private void setGUILogin(){
		if (!(AdminUtil.getInstance().isAdminLoggedIn() || AdminUtil.getInstance().isOperatorLoggedIn())){
			frames.jbLoginStatus.setIcon(new ImageIcon(swc.iconPath + "Profiles.jpg"));
			frames.jbLoginStatus.setToolTipText(propertyUtil.getLangText("Frame.Main.LoginStatus") + " : " + this.UserName);
		}
		frames.jcbProfiles.setEnabled(true);
		frames.tfProfile.setEnabled(true);
		frames.tfProfile.setBorder(BorderFactory.createLoweredBevelBorder());
		frames.tfProfile.requestFocus();
		frames.tfUser.setEnabled(false);
		frames.tfUser.setBorder(BorderFactory.createEtchedBorder());
		frames.jbLogin.setActionCommand("loginprofile");
		frames.jbLogout.setEnabled(true);
		frames.jlPc.setIcon(new ImageIcon(swc.iconPath  + "UserLogged.gif"));
	}
	
	private void setGUILogout(){
		if (!(AdminUtil.getInstance().isAdminLoggedIn() || AdminUtil.getInstance().isOperatorLoggedIn())){
			frames.jbLoginStatus.setIcon(new ImageIcon(swc.iconPath + "LoginDefault.jpg"));
			frames.jbLoginStatus.setToolTipText(propertyUtil.getLangText("Frame.Main.LoginStatus"));
		}
		frames.jcbProfiles.removeAllItems();
		frames.jcbProfiles.setEnabled(false);
		frames.tfProfile.setEnabled(false);
		frames.tfProfile.setBorder(BorderFactory.createEtchedBorder());
		frames.tfUser.setEnabled(true);
		frames.tfUser.setBorder(BorderFactory.createLoweredBevelBorder());
		frames.tfUser.requestFocus();
		frames.jbLogin.setActionCommand("loginuser");
		frames.jbLogout.setEnabled(false);
		frames.tfProfile.setText("");
		frames.jlPc.setIcon(new ImageIcon(swc.iconPath  + "LoginPC.jpg"));
	}
	
	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}
}
