package Roto.Utils;

import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import Roto.Basic.SwingConstans;

public class SystemUtil {
	
	private static SystemUtil instance = null;
	private PropertyUtil propertyUtil;
	private SwingConstans swc;
	
	public static SystemUtil getInstance() {
		if (instance == null) {
			instance = new SystemUtil();
		}
		return instance;
	}
	
	public SystemUtil(){
		propertyUtil = PropertyUtil.getInstance();
		swc = SwingConstans.getInstance();
	}
	
	private void debugProperties(){
		Iterator it = System.getProperties().keySet().iterator(); 
    	while(it.hasNext()) { 
    		String key = String.valueOf(it.next()); 
    		String value = System.getProperties().getProperty(key);
    		System.out.println(key + "=" + value);
    	}
	}
	
	public String getJavaInfo(){
		StringBuffer info = new StringBuffer();
		String [] keys = {"java.runtime.name","sun.boot.library.path","java.vm.version","java.vm.vendor",
						  "java.vendor.url","user.country","java.runtime.version","os.name","os.version",
						  "sun.jnu.encoding","file.encoding","java.home","user.language","java.version"};
		Properties properties = System.getProperties();
		
		for (String key : keys){
			info.append(key + "   : " + properties.getProperty(key)+"\n");
		}
		
		return info.toString();
	}
	
	//Check if java version at least 1.6
	public boolean checkVersion(){
		try {
			StringTokenizer token = new StringTokenizer(System.getProperty("java.version"),".");
			int major = Integer.parseInt(token.nextToken()); 
			int minor = Integer.parseInt(token.nextToken());
			if (major < 2 && minor < 6) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e){
			 System.out.println("Error getting java version: " + e.getMessage());
			 return true;
		}
	}
	
	public boolean checkVersionWithSetupProperty(){
		try {
			Properties setupProperty;
			setupProperty = propertyUtil.loadProperty(swc.setupPath + "Setup.properties");
			
			StringTokenizer token = new StringTokenizer(System.getProperty("java.version"),".");
			int majorSystem = Integer.parseInt(token.nextToken()); 
			int minorSystem = Integer.parseInt(token.nextToken());
			
			int majorSetup = Integer.parseInt(setupProperty.getProperty("Setup.Recommended.Version.Major"));
			int minorSetup = Integer.parseInt(setupProperty.getProperty("Setup.Recommended.Version.Minor"));
			
			//System.out.println(Integer.parseInt(String.valueOf(majorSystem) + String.valueOf(minorSystem)));
			//System.out.println(Integer.parseInt(String.valueOf(majorSetup) + String.valueOf(minorSetup)));
			
			if (Integer.parseInt(String.valueOf(majorSystem) + String.valueOf(minorSystem)) <
				Integer.parseInt(String.valueOf(majorSetup) + String.valueOf(minorSetup))) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e){
			 System.out.println("Error getting java version: " + e.getMessage());
			 return true;
		}
		
	}
	
}
