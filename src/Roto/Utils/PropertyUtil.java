package Roto.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import Roto.Basic.SwingConstans;
import Roto.DataBase.TextDBRow;
import Roto.Gui.Frames;
import Roto.Gui.JRFrame;
import Roto.Gui.JRInternalFrame;

public class PropertyUtil {
	
	private static PropertyUtil instance = null;
	private String ProfilePropertyPath = SwingConstans.getInstance().basicPath;
	private final Charset ch_utf8 = Charset.forName("UTF-8");
	private final Charset ch_8859_1 = Charset.forName("8859_1");
	
	//Relative properties
	private final String BASE_PROPERTY_PATH ="Roto.properties";
	private final String FRAME_PROPERTY_PATH ="Frames.properties";
	private final String DATABASELIST_PROPERTY_PATH ="DatabaseList.properties";
	private final String BASE_PROPERTY_NAME ="Base";
	private final String FRAME_PROPERTY_NAME ="Frame";
	private final String DATABASELIST_PROPERTY_NAME ="DatabaseList";
	
	//Fix properties
	private final String LANG_PROPERTY_PATH ="Bin/Properties/Basic/Language.properties";
	private final String PROFILES_PROPERTY_PATH ="Bin/Properties/Basic/Profiles.properties";
	private final String USERS_PROPERTY_PATH ="Bin/Properties/Basic/Users.properties";
	private final String DATABASE_PROPERTY_PATH ="Bin/Properties/Basic/Database.properties";
	private final String PRICELIST_PROPERTY_PATH ="Bin/Properties/Basic/Pricelist.properties";
	
	private final String LANG_PROPERTY_NAME ="Lang";
	private final String PROFILES_PROPERTY_NAME ="Profiles";
	private final String USERS_PROPERTY_NAME ="Users";
	private final String DATABASE_PROPERTY_NAME ="Database";
	private final String PRICELIST_PROPERTY_NAME ="Pricelist";
	
	private HashMap<String, String> pathMap;
	private HashMap<String, Properties> propertyMap;
	private HashMap<String, JRFrame> frameMap;
	private HashMap<String, JRInternalFrame> internalFrameMap;
	private HashMap<String, String> colorMap = new HashMap<String, String>();
	private FileWriter fileWriter;
	private String langCode;
	private SwingConstans swc;
	
	public static PropertyUtil getInstance() {
		if (instance == null) {
			instance = new PropertyUtil();
		}
		return instance;
	}

	protected PropertyUtil() {
		// Do not place in init method, it will be reset values when user logs in.
		frameMap  = new HashMap<String, JRFrame>();
		internalFrameMap = new HashMap<String, JRInternalFrame>();
		swc = SwingConstans.getInstance();
		init();
		//Load language code (only after init)
		loadLanguage();
	}
	
	public void loadLanguage(){
		Properties langProperty = getLanguageProperty();
		String selectedLanguage = langProperty.getProperty("Language.Selected");
		String selectedLangCode = "hu";
		
		for (int i=0; i < swc.flagTitles.length ; i++){
			if (swc.flagTitles[i].equals(selectedLanguage)){
				selectedLangCode = swc.langCodes[i];
			}
		}
		setLangCode(selectedLangCode);
		//Create empty property files
		//createLanguageProperties("c:/");
	}
	
	public void setLoggedUserDirectory(String userDirectory){
		setProfilePropertyPath(userDirectory);
		init();
	}
	
	private void init() {
		pathMap = new HashMap<String, String>();
		propertyMap = new HashMap<String, Properties>();
		//Relative profile
		pathMap.put(BASE_PROPERTY_NAME, ProfilePropertyPath + BASE_PROPERTY_PATH );
		pathMap.put(FRAME_PROPERTY_NAME, ProfilePropertyPath + FRAME_PROPERTY_PATH);
		pathMap.put(DATABASELIST_PROPERTY_NAME, ProfilePropertyPath + DATABASELIST_PROPERTY_PATH);
		//Basic fix
		pathMap.put(PROFILES_PROPERTY_NAME, PROFILES_PROPERTY_PATH);
		pathMap.put(USERS_PROPERTY_NAME, USERS_PROPERTY_PATH);
		pathMap.put(DATABASE_PROPERTY_NAME, DATABASE_PROPERTY_PATH);
		pathMap.put(PRICELIST_PROPERTY_NAME, PRICELIST_PROPERTY_PATH);
		pathMap.put(LANG_PROPERTY_NAME, LANG_PROPERTY_PATH );
		
		//Add languages 
		for (int i =0; i < SwingConstans.getInstance().langCodes.length; i++){
			String langCode = SwingConstans.getInstance().langCodes[i];
			pathMap.put(langCode,"Bin/Properties/Basic/Language_" + langCode + ".properties");
		}
		
		crateAndLoadProperties(pathMap);
		setColors();
	}
	
	//Create property files by pathMap, and put them to propertyMap
	private void crateAndLoadProperties(HashMap<String,String> map) {
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String sKey = (String)iterator.next();
			String sPath = (String)map.get(sKey);
			File fPath = new File (sPath);
			try {
				//Create property if not exists
				if (!fPath.exists()) {
					fileWriter = new FileWriter(sPath);
					fileWriter.write("");
					fileWriter.close();
				}
				
				//Load properties to property map 
				Properties property = null;
				
				if (sKey.length() == 2) {
					//If key is a language, example hu, try to load utf-8 property
					property = loadProperty(sPath, ch_utf8);
				} else {
					property = loadProperty(sPath);
				}
				
				if (property!=null) {
					propertyMap.put(sKey, property);
				}
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			
		}
	}
	
	//Get integer value from a property
    public int getIntProperty(Properties props, String name) { 
		String value = props.getProperty(name); 
		if(value == null) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}
    
    //Get double value from a property
    public Double getDoubleProperty(Properties props, String name) { 
		String value = props.getProperty(name); 
		if(value == null) {
			return 0.0;
		} else {
			return Double.parseDouble(value);
		}
	}
    
	//Get boolean value from a property
    public boolean getBooleanProperty(Properties props, String name) { 
		String value = props.getProperty(name);
		if(value == null || value.trim().toLowerCase().equalsIgnoreCase("false")) {
			return false;
		} else if(value.trim().toLowerCase().equalsIgnoreCase("true")) {
			return true;
		}
		else return false;
	}
    
	//Get string value from a property
    public String getStringProperty(Properties props, String name) { 
		String value = props.getProperty(name);
		if(value == null) {
			return "";
		}
		return value;
	}

	//Get language value 
    public String getLangText(String name) {
    	if (this.langCode == null) {
    		this.langCode = "hu";
    	}
    	
    	Properties langProperty = propertyMap.get(langCode);
    	
		String value = langProperty.getProperty(name);
		//System.out.println("Value: " +value);
		if(value == null) {
			return name;
		}
		return value;
	} 
    
	public void createLanguageProperties(String folderToCreate){
		Properties langPropertyHu = propertyMap.get("hu");
		StringBuffer content = new StringBuffer("");
		
		StringReader reader = new StringReader(IOUtil.getInstance().loadUTF8(SwingConstans.getInstance().basicPath +"/Language_hu.properties"));
	    BufferedReader in = new BufferedReader(reader);
	    String line;	    
		try {
			while ((line = in.readLine()) != null) {
				if (line.length()> 0){
					if (line.indexOf("=") > -1) {
						line = line.substring(0,line.indexOf("=")+1);
					}
					content.append(line+"\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		for (int i =0; i < SwingConstans.getInstance().langCodes.length; i++ ){
			File langFile = new File(folderToCreate + "/Language_" + SwingConstans.getInstance().langCodes[i]+".properties");
			if (!langFile.exists()){
				IOUtil.getInstance().saveUTF8(langFile, content.toString());
				System.out.println("Langfile created: " + langFile.getName());
			}else {
				System.out.println("Langfile exists: " + langFile.getName());		
			}
		}
	}
    
	//Load utf8 property file with normal encoding
    public EncodedProperties loadUTF8Property(String propertyPath) {
    	EncodedProperties encodedProperty = new EncodedProperties();
		try {
			encodedProperty.load(new FileInputStream(propertyPath), ch_utf8);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return encodedProperty;
    }
    
	//Save utf8 property file with normal encoding
    public void saveUTF8Property(EncodedProperties encodedProperty, String propertyPath) {

    	try {
    		encodedProperty.store(new FileOutputStream(propertyPath), ch_utf8, null);
		}catch (Exception e) {
			e.printStackTrace();
		}

    }
	
	//Load a property file with normal encoding
    public Properties loadProperty(String PropertyPath) {
    	return loadProperty(PropertyPath, ch_8859_1);
    }

    //Load a property file with passed encoding    
	private Properties loadProperty(String PropertyPath, Charset charset) {
		EncodedProperties encodedProperty = new EncodedProperties();
		try {

			encodedProperty.load(new FileInputStream(PropertyPath), charset);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return encodedProperty;
	}
	
	//Get frame property
	public Properties getFrameProperty() {
		return propertyMap.get(FRAME_PROPERTY_NAME);
	}
	
	//Set frame property values
	public void setFrameProperty(String key, String value) {
			getFrameProperty().setProperty(key,value);
	}
	
	//Save frame property values
	public synchronized void storeFrameProperty() {
		try {
			if (new File(pathMap.get(FRAME_PROPERTY_NAME)).canWrite()){
				getFrameProperty().store(new FileOutputStream(pathMap.get(FRAME_PROPERTY_NAME)),null);
			}
		}
		catch (Exception e) {
			//System.out.println("Failed to save frame.properties: " + e.getMessage());
		}
	}

	
	//Get profile property
	public Properties getProfileProperty() {
		return propertyMap.get(PROFILES_PROPERTY_NAME);
	}
	
	//Remove profile property values
	public void removeProfileProperty(String key) {
		try {
			getProfileProperty().remove(key);
			getProfileProperty().store(new FileOutputStream(pathMap.get(PROFILES_PROPERTY_NAME)),null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Remove user property values
	public void removeUserProperty(String key) {
		try {
			getUserProperty().remove(key);
			getUserProperty().store(new FileOutputStream(pathMap.get(USERS_PROPERTY_NAME)),null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Save profile property values
	public void setProfileProperty(String key, String value) {
		try {
			getProfileProperty().setProperty(key,value);
			getProfileProperty().store(new FileOutputStream(pathMap.get(PROFILES_PROPERTY_NAME)),null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Get user property
	public Properties getUserProperty() {
		return propertyMap.get(USERS_PROPERTY_NAME);
	}
	
	//Save user property values
	public void setUserProperty(String key, String value) {
		try {
			getUserProperty().setProperty(key,value);
			getUserProperty().store(new FileOutputStream(pathMap.get(USERS_PROPERTY_NAME)),null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Fill color map (used in menu settings)
	private void setColors() {
		colorMap.put("Color.None", SwingConstans.getInstance().colorArray[0]);
		colorMap.put("Color.White", SwingConstans.getInstance().colorArray[1]);
		colorMap.put("Color.Brown", SwingConstans.getInstance().colorArray[2]);
		colorMap.put("Color.NewSilver", SwingConstans.getInstance().colorArray[3]);
		colorMap.put("Color.NatureSilver", SwingConstans.getInstance().colorArray[4]);
		colorMap.put("Color.Gold", SwingConstans.getInstance().colorArray[5]);
		colorMap.put("Color.MatGold",SwingConstans.getInstance().colorArray[6]);
		colorMap.put("Color.Brass", SwingConstans.getInstance().colorArray[7]);
		colorMap.put("Color.Titan", SwingConstans.getInstance().colorArray[8]);
		colorMap.put("Color.OliveBrown", SwingConstans.getInstance().colorArray[9]);
		colorMap.put("Color.GreyBrown", SwingConstans.getInstance().colorArray[10]);
		colorMap.put("Color.MiddleBrass", SwingConstans.getInstance().colorArray[11]);
		colorMap.put("Color.DarkBrass", SwingConstans.getInstance().colorArray[12]);
		colorMap.put("Color.CreamWhite", SwingConstans.getInstance().colorArray[13]);
		colorMap.put("Color.JetBlack", SwingConstans.getInstance().colorArray[14]);
	}
	
	// Get a key by value from a map
	public String getKeysFromValue(HashMap<String, String> hm, String value){
			Set ref = hm.keySet();
		    Iterator it = ref.iterator();
		    String key = "";
		    while (it.hasNext()) {
		      Object o = it.next(); 
		      if(hm.get(o).equals(value)) { 
		       key = String.valueOf(o); 
		      } 
		    } 
		    return key;
	  }

	
	//Get language properties
	public Properties getLanguageProperty() {
		return propertyMap.get(LANG_PROPERTY_NAME);
	}
	
	//Save language properties
	public void saveLanguageProperty() {
		try {
			getLanguageProperty().store(new FileOutputStream(pathMap.get(LANG_PROPERTY_NAME)),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	//Get base properties
	public Properties getRotoProperty() {
		return propertyMap.get(BASE_PROPERTY_NAME);
	}
	
	//Save base properties
	public void saveRotoProperty() {
		try {
			getRotoProperty().store(new FileOutputStream(pathMap.get(BASE_PROPERTY_NAME)),null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Get database property
	public Properties getDatabaseProperty() {
		return propertyMap.get(DATABASE_PROPERTY_NAME);
	}
	
	//Get database list property
	public Properties getDatabaseListProperty() {
		return propertyMap.get(DATABASELIST_PROPERTY_NAME);
	}
	
	//Set database list property 
	public void setDataBaseListProperty(String key, String value) {
		getDatabaseListProperty().setProperty(key,value);
	}
	
	//Save database list property values
	public void saveDataBaseListProperty() {
		try {
			getDatabaseListProperty().store(new FileOutputStream(pathMap.get(DATABASELIST_PROPERTY_NAME)),null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Remove database list property values
	public void removeDataBaseListProperty(String key) {
		try {
			getDatabaseListProperty().remove(key);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Get price list property
	public Properties getPricelistProperty() {
		return propertyMap.get(PRICELIST_PROPERTY_NAME);
	}
	
	public HashMap<String, String> getColorMap() {
		return this.colorMap;
	}
	
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	
	public String getLangCode() {
		return langCode;
	}
	
	public String getProfilePropertyPath() {
		return ProfilePropertyPath;
	}

	public void setProfilePropertyPath(String propertyPath) {
		ProfilePropertyPath = propertyPath;
	}
	
	public HashMap<String, JRFrame> getFrameMap() {
		return frameMap;
	}

	public HashMap<String, JRInternalFrame> getInternalFrameMap() {
		return internalFrameMap;
	}
	
}
