package Roto.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import Roto.Basic.SwingConstans;
import Roto.Gui.Frames;
import Roto.Gui.JRFrame;
import Roto.Gui.JRInternalFrame;
import Roto.Gui.Tree.CheckNode;

public class ProfileUtil {
	private static ProfileUtil instance = null;
	private Frames frames;
	private PropertyUtil propertyUtil;
	private SwingConstans swc;
	private MessageUtil message;
	private boolean isLoggedin;
	private UserUtil user;
	private String profileName;
	private IOUtil ioUtil;
	
	public static ProfileUtil getInstance() {
		if (instance == null) {
			instance = new ProfileUtil();
		}
		return instance;
	}
	
	public ProfileUtil() {
		frames = Frames.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		swc = SwingConstans.getInstance();
		message = MessageUtil.getInstance();
		user = UserUtil.getInstance();
		profileName = "";
		ioUtil = IOUtil.getInstance();
	}
		
	private void loadProfileSettings() {
		//Set framePositions
		HashMap<String, JRFrame> frameMap = propertyUtil.getFrameMap();
		for(String key : frameMap.keySet()) {
			frameMap.get(key).loadWindowPosition();
			//frameMap.get(key).setVisible(true);
		}
		//Set internal frame positions
		HashMap<String, JRInternalFrame> internalFrameMap = propertyUtil.getInternalFrameMap();
		for(String key : internalFrameMap.keySet()) {
			internalFrameMap.get(key).loadWindowPosition();
			//internalFrameMap.get(key).setVisible(true);
		} 
	}
	
	public void loginProfile(String ProfileName, String UserName){
		if (ProfileName.length()>0) {
			if (isUserInProfile(ProfileName, UserName)){
				//Set last logged in profile
				propertyUtil.setProfileProperty("Profiles.LastLogin", ProfileName.trim());
				
				ProfileName = ProfileName.toLowerCase().trim();
				String sUserDirectory = "";
	
				//Load user
				if ((sUserDirectory = getProfileDirectory(ProfileName)).equals("") && message.showConfirmDialog(propertyUtil.getLangText("Confirm.CreateNewProfile"))){
					//Create new profile if not exists
					sUserDirectory = createProfile(ProfileName, UserName);
				}
				
				if (sUserDirectory.length()>0) {
					//Create directory if missing
					File fDirectoy = new File(sUserDirectory);
					if (!fDirectoy.exists()) {
						fDirectoy.mkdir();
						message.showErrorMessage(propertyUtil.getLangText("Alert.LoadUserSettings.MissingDirectory"), propertyUtil.getLangText("Alert.System.Error.Title"));	
					}
					propertyUtil.setLoggedUserDirectory(sUserDirectory+"\\");
					setLoggedin(true);
					setGUILogin();
					loadProfileSettings();
				}
				
				//Reload profile combo, if user created
				user.fillProfileCombo(UserName);
				
				//Set profileName
				this.profileName = ProfileName;
			}
			//Show message if profile name belongs to another user
			else {
				message.showErrorMessage(propertyUtil.getLangText("Alert.ProfileName.DifferentUser.Message"), propertyUtil.getLangText("Alert.Input.Error.Title"));	
			}
			
		} else {
			message.showErrorMessage(propertyUtil.getLangText("Alert.ProfileName.Empty.Message"), propertyUtil.getLangText("Alert.Input.Error.Title"));
		}
				
	}
	
	public void logoutProfile() {
		setLoggedin(false);
		this.profileName = "";
		setGUILogout();
		//Reload basic options
		propertyUtil.setLoggedUserDirectory(swc.basicPath);
		loadProfileSettings();
		
	}
	
	public List<String> getProfileRebateFileNames(String ProfileName){
		List<String> rebateList = new ArrayList<String>();
		File profileDirectory = new File (getProfileDirectory(ProfileName)+"\\Rebate");
		if (profileDirectory.exists()){
			String[] fileList = profileDirectory.list();
			for (int i=0; i<fileList.length; i++){
				if (fileList[i].toString().endsWith(".xml")){
					rebateList.add(fileList[i].toString());
				}
			}
		}
		return rebateList;
	}
	
	
	public String getProfileDefaultRebateFilName(String ProfileName) {
		//Load profile default rebate file name
		String path = propertyUtil.getStringProperty(propertyUtil.getProfileProperty(), "Profiles." + ProfileName + ".RebateFile");
		String fileName = "";
		if (path.length() > 0) {
			fileName = path.substring(path.lastIndexOf("\\")+1);
		}
		return fileName;
	}
	
	public String getProfileDefaultRebatePath(String ProfileName) {
		//Load profile default rebate file path
		return propertyUtil.getStringProperty(propertyUtil.getProfileProperty(), "Profiles." + ProfileName + ".RebateFile");
	}
	
	public String getProfileDirectory(String ProfileName) {
		//Load existing profile
		return propertyUtil.getStringProperty(propertyUtil.getProfileProperty(), "Profiles." + ProfileName + ".Directory");
	}
	
	private String getProfileUser(String ProfileName) {
		//Load profile user
		return propertyUtil.getStringProperty(propertyUtil.getProfileProperty(), "Profiles." + ProfileName + ".User");
	}
	
	private boolean isUserInProfile(String ProfileName, String UserName){
		// Return user not exists in profile value if default name used 
		if (ProfileName.toLowerCase().trim().equalsIgnoreCase("default")){
			return false;
		}
		// Check existing profile name
		String profileUser = getProfileUser(ProfileName).toLowerCase().trim();
		if (profileUser.length() > 0 && UserName.length() > 0 && ProfileName.length() > 0 && !profileUser.equals(UserName.toLowerCase().trim())){
			return false;
		} else {
			return true;
		}
	}
	
	public String createProfile(String ProfilName, String UserName) {
		//Create new user
		String sUserDirectory = "";
			try {
				int iDirectory = 1;
				File fDirectory, fRebateDirectory, fDefaultRebate, fTargetRebate;
				//Search for new director
				while(new File(swc.profilePath + ProfilName.replaceAll(" ", "") + "_" + String.valueOf(iDirectory)).isDirectory() && iDirectory < 3000) {
					iDirectory++;
				}
				fDirectory = new File(swc.profilePath + ProfilName.replaceAll(" ", "") + "_" + String.valueOf(iDirectory));
				//Create new directory
				fDirectory.mkdir();
				
				//Create new rebate firectory
				fRebateDirectory = new File(fDirectory + "/" + "Rebate");
				if (!fRebateDirectory.exists()) fRebateDirectory.mkdir();
				
				//Copy default rebate file
				fDefaultRebate = new File (swc.RebatePath + "/Default.xml");
				fTargetRebate = new File (fRebateDirectory + "/Default.xml");
				if (fDefaultRebate.exists()){
					ioUtil.copyFile(fDefaultRebate, fTargetRebate);
								
					//Set property to default rebate for profile 
					propertyUtil.setProfileProperty( "Profiles." + ProfilName.toLowerCase().trim() + ".RebateFile", fTargetRebate.getPath());
				}
				
				//Set property for profile
				propertyUtil.setProfileProperty("Profiles." + ProfilName + ".Directory", fDirectory.getPath()+"\\");
				sUserDirectory = fDirectory.getPath();

				// Set user for profile
				propertyUtil.setProfileProperty("Profiles." + ProfilName + ".User" , UserName);
				
			} catch (Exception e) {
				message.showErrorMessage(propertyUtil.getLangText("Alert.CreateUser.Error.Message")+"\n"+e.getMessage(), propertyUtil.getLangText("Alert.Input.Error.Title"));
			}
			
			return sUserDirectory;
	}
	
	private void setGUILogout(){
		frames.mainTabPane.setTitleAt(0, propertyUtil.getLangText("Frame.Main.Tab.Login"));
		frames.mainTabPane.setIconAt(0, new ImageIcon(swc.iconPath + "Login.jpg"));
		frames.jlLogin.setIcon(new ImageIcon(swc.iconPath  + "Login.jpg"));
		frames.jlPc.setIcon(new ImageIcon(swc.iconPath  + "UserLogged.gif"));
		frames.tfProfile.setEnabled(true);
		frames.tfProfile.setBorder(BorderFactory.createLoweredBevelBorder());
		frames.jcbProfiles.setEnabled(true);
		frames.jbLogin.setEnabled(true);
		frames.jbLogout.setActionCommand("logoutuser");
	}
	
	private void setGUILogin(){
		frames.mainTabPane.setTitleAt(0, propertyUtil.getLangText("Frame.Main.Tab.Logout"));
		frames.mainTabPane.setIconAt(0, new ImageIcon(swc.iconPath + "Protection.jpg"));
		frames.jlLogin.setIcon(new ImageIcon(swc.iconPath  + "Protection.jpg"));
		frames.jlPc.setIcon(new ImageIcon(swc.iconPath  + "ProfileLogged.gif"));
		frames.jcbProfiles.setEnabled(false);
		frames.tfProfile.setEnabled(false);
		frames.tfProfile.setBorder(BorderFactory.createEtchedBorder());
		frames.jbLogin.setEnabled(false);
		frames.jbLogout.setActionCommand("logoutprofile");
		frames.mainTabPane.setSelectedIndex(swc.tabResize);
	}
	
	public boolean isLoggedin() {
		return isLoggedin;
	}

	public void setLoggedin(boolean isLoggedin) {
		this.isLoggedin = isLoggedin;
	}
	
	public String getProfileName() {
		return profileName;
	}
	
}
