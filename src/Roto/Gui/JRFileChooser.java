package Roto.Gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;


import Roto.Utils.FileChooserFilter;

public class JRFileChooser extends JFileChooser{
	private String Description;
	private String Extension;
	private String Title;
	private String Directory;
	private String[] Extensions;
	private boolean directoryChangeEnabled = true;

	public JRFileChooser(String Extension, String Description, String Title, String Directory) {
		this.Extension = Extension;
		this.Description = Description;
		this.Title = Title;
		this.Directory = Directory;
		init();
	}
	
	public JRFileChooser(String[] Extensions, String Description, String Title, String Directory) {
		this.Extensions = Extensions;
		this.Description = Description;
		this.Title = Title;
		this.Directory = Directory;
		init();
	}
	
	public void setDirectoryChangeEnabled(boolean isEnabled){
		this.directoryChangeEnabled = isEnabled;
		init();
	}
	
	public void init () {
		if (Extensions != null){
			setFileFilter(new FileChooserFilter(Extensions,Description));
		} else {
			setFileFilter(new FileChooserFilter(Extension,Description));
		}
		setAcceptAllFileFilterUsed(false);
		setDialogTitle(Title);
		setCurrentDirectory(new File(Directory));
		
		// Add listener on chooser to detect changes to current directory
		addPropertyChangeListener(new PropertyChangeListener() {
		    public void propertyChange(PropertyChangeEvent evt) {
		        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(evt.getPropertyName())) {
		           // JFileChooser chooser = (JFileChooser)evt.getSource();
		            if (!directoryChangeEnabled) {
		            	//disable all directory changes
		            	setCurrentDirectory(new File(Directory));
		            }
		        }
		    }
		}) ;
	}
}
