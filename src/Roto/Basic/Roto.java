package Roto.Basic;

import java.util.Locale;

import Roto.Gui.GuiBuilder;
import Roto.Registration.Registration;
import Roto.Setup.Setup;
import Roto.Utils.PropertyUtil;
import Roto.Utils.RegisterUtil;

public class Roto  {
	
	public Roto(){
		setLocale();
		startUp();
	}
	
	public void setLocale() {
		Locale.setDefault(new Locale("hu", "HU"));
	}
	
	
	public void startUp(){
		GuiBuilder gui = GuiBuilder.getInstance();
		gui.buildGui();
	}
	
	public static void main(String[] args) {
		if (args.length == 0){
			new Roto();
		}
		else if (args[0].toString().equals("reg")){
			new Registration();
		}
		else if (args[0].toString().equals("removedemo")){
			RegisterUtil.getInstance().removeDemoUserEnanled();
		}
		else if (args[0].toString().equals("setup")){
			new Setup();
		}
		else {
			System.out.println("Invalid parameter: " + args[0].toString());
			new Roto();
		}
	}

}
