package Roto.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import Roto.Basic.SwingConstans;
import Roto.Utils.PropertyUtil;

public class JRInternalFrame extends JInternalFrame implements MouseListener, ComponentListener, InternalFrameListener{
	
	private static final long serialVersionUID = 1L;
	private PropertyUtil propertyUtil;
	private JDesktopPane deskTop;
	private String name;
	private String title;
	private SwingConstans swc;

	public JRInternalFrame(){
    	init();
    }
    
	public JRInternalFrame(String Name, String Title, JDesktopPane deskTop){
		this.name = Name;
		this.title = Title;
		this.deskTop = deskTop;
		init();
	}
	
	public void init() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		swc = SwingConstans.getInstance();
		setName(name);
		deskTop.add(this);
		propertyUtil = PropertyUtil.getInstance();
		setTitle(propertyUtil.getLangText(title));
		setResizable(true);
		setMaximizable(true);
		setIconifiable(true);
		setClosable(true);
		setLocation(deskTop.getX() + 5, deskTop.getY() - 30);
		setSize(deskTop.getWidth() - swc.menuToolbarWidth -10, deskTop.getHeight() -10);
		setBackground(Color.WHITE);
		loadWindowPosition();
		addComponentListener(this);
		addInternalFrameListener(this);
		addMouseListener(this);
		propertyUtil.getInternalFrameMap().put(this.name, this);
	}
	
	public void loadWindowPosition(){
		try {
			Properties frameProperties = propertyUtil.getFrameProperty();
			int Width = propertyUtil.getIntProperty(frameProperties, "Roto.InternalFrame." + getName() + ".Width");
			int Height = propertyUtil.getIntProperty(frameProperties, "Roto.InternalFrame." + getName() + ".Height");
			int X = propertyUtil.getIntProperty(frameProperties, "Roto.InternalFrame." + getName() + ".X");
			int Y = propertyUtil.getIntProperty(frameProperties, "Roto.InternalFrame." + getName() + ".Y");
			String isMaximum = propertyUtil.getStringProperty(frameProperties, "Roto.InternalFrame." + getName() + ".isMaximum");
						
			if (Width > 0) {
				setSize(Width, Height);
				setLocation(X, Y);
			} 
						
			if (isMaximum.equals("true")){
				setMaximum(true);
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void saveWindowPosition(){
		if (!isMaximum) {
			propertyUtil.setFrameProperty("Roto.InternalFrame." + getName() + ".Width", getWidth()+"");
			propertyUtil.setFrameProperty("Roto.InternalFrame." + getName() + ".Height", getHeight()+"");
			propertyUtil.setFrameProperty("Roto.InternalFrame." + getName() + ".X", getX()+"");
			propertyUtil.setFrameProperty("Roto.InternalFrame." + getName() + ".Y", getY()+"");
		}
			propertyUtil.setFrameProperty("Roto.InternalFrame." + getName() + ".isMaximum", isMaximum+"");
			
			propertyUtil.storeFrameProperty();
	}
	
	public void setMaximumSize() {
		try {
			setMaximum(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setFrameSize(int width, int height){
		if (getWidth() == 0 || getHeight() == 0) {
			setSize(width, height);	
		}
	}
	
	public void setFrameLocation(int X, int Y){
		if (!isMaximum() && getLocation().x == 0 && getLocation().y == 0) {
			setLocation(X, Y);	
		}
	}
	
	public void setVisibleToFocus(){
		try {
			setVisible(true);
			setSelected(true);
			toFront();
		} catch (Exception e) {
			System.out.println("Couldn't select internalframe.");
		}
	}

	public void componentHidden(ComponentEvent arg0) {
		saveWindowPosition();
		
	}

	public void componentMoved(ComponentEvent arg0) {
		//Mouse exited used, duo processors cause write exception
		//saveWindowPosition();
	}

	public void componentResized(ComponentEvent arg0) {
		saveWindowPosition();
	}

	public void componentShown(ComponentEvent arg0) {
	}

	public void internalFrameActivated(InternalFrameEvent e) {
	}

	public void internalFrameClosed(InternalFrameEvent e) {
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		setVisible(false);
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
		saveWindowPosition();
	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {
		
	}

}
