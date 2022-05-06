package Roto.Utils;

import java.io.File;

public class FileChooserFilter extends javax.swing.filechooser.FileFilter {
	String  Description;
	String 	Extension;
	String[] Extensions;

	public FileChooserFilter(String Extension, String Description) {
		this.Extension = Extension;
		this.Description = Description;
	}
	
	public FileChooserFilter(String[] Extensions, String Description) {
		this.Extensions = Extensions;
		this.Description = Description;
	}
	
	public boolean accept(File f) {
	  String name = f.getName().toUpperCase();
	  boolean accept = false;
	  if (Extensions != null) {
		  for (int i=0; i<Extensions.length; i++){
			  if (f.isDirectory() || name.endsWith(Extensions[i].toString().toUpperCase())){
				  accept = true;
			  }
		  }
		  return accept;
	  } else {
		  return f.isDirectory() ||  name.endsWith(Extension);
	  }
	}
	
	public String getDescription() {
	  return Description;
	}
	  
}
