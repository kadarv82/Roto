package Roto.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JWindow;
import javax.swing.Timer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import Roto.Basic.SwingConstans;
import Roto.Utils.IOUtil;

public class Viewer extends JWindow implements ActionListener {
	private JLabel viewerLabel;
	private JPanel  pnShadow;
	private ImagePanel pnWindow = new ImagePanel();
	private Timer timer, timerOff;
	private int maxWidth;
	private int maxHeight;
	private int currentWidth;
	private int currentHeight;
	private int step = 7;
	private Point currentLocation;
	private JWindow shadow;
	private int startUpTime = 500;
	private String text;
	private boolean showAlways;
	private boolean hasEffect = true;
	private boolean exitOnShutDown;
	private ImageIcon viewerImage;
	JTextPane textPane;
		
	public Viewer(String text){
		init();
		this.startUpTime = 100;
		pnWindow.setBackground(SwingConstans.getInstance().colorViewerMessage);
		pnWindow.add(viewerLabel = new JLabel("",JLabel.CENTER),"Center");
		this.text = text;
		this.maxWidth=text.length()* 7;
		this.maxHeight=30;
	}
	
	public Viewer(ImageIcon background, ImageIcon loader){
		pnWindow = new ImagePanel(background.getImage());
		JPanel pnLoader = new JPanel();
		JLabel jlLoader = new JLabel(loader);
		pnLoader.setOpaque(false);
		jlLoader.setOpaque(false);
		jlLoader.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.maxWidth= background.getIconWidth();
		this.maxHeight=background.getIconHeight();
		
		init();
		
		pnWindow.setLayout(new BorderLayout());
		pnLoader.setLayout(new FlowLayout(FlowLayout.RIGHT));

		pnLoader.add(jlLoader);
		pnWindow.add(pnLoader, "South");
	}
	
	public Viewer(String text, ImageIcon background){
		textPane = new JTextPane();
		int fontSize = 15;
		text = text.replaceAll("<br>", "\n");
		
		pnWindow = new ImagePanel(background.getImage());

		this.maxWidth= background.getIconWidth();
		this.maxHeight=background.getIconHeight();
		init();
		
		StyledDocument doc = textPane.getStyledDocument();
		
		//  Set alignment to be centered for all paragraphs
		MutableAttributeSet standard = new SimpleAttributeSet();
		StyleConstants.setAlignment(standard, StyleConstants.ALIGN_CENTER);
		
		StyleConstants.setBold(standard, true);
		StyleConstants.setItalic(standard, true);
		StyleConstants.setFontSize(standard, fontSize);
		StyleConstants.setFontFamily(standard, "Arial");
		StyleConstants.setForeground(standard, Color.BLACK);
		
		doc.setParagraphAttributes(0, 0, standard, true);
		
		//Set to center
		int lines = IOUtil.getInstance().getLineCount(text);
		int lineCapacity = background.getIconHeight() / fontSize;
		int startFrom = ((lineCapacity - lines) / 2)-1;
		
		StringBuffer textBuffer = new StringBuffer("");
		
		for (int i=0; i < startFrom; i++){
			textBuffer.append("\n");
		}
		
		text = textBuffer.append(text).toString();
		
		//Set text
		textPane.setText(text);
		textPane.setOpaque(false);
		
		pnWindow.setLayout(new BorderLayout());
		pnWindow.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	public Viewer(ImageIcon viewerImage){
		init();
		this.maxWidth=viewerImage.getIconWidth();
		this.maxHeight=viewerImage.getIconHeight();
		this.viewerImage = viewerImage;
//		pnWindow.add(viewerLabel = new JLabel(viewerImage,JLabel.CENTER),"Center");
		pnWindow.add(viewerLabel = new JLabel(),"Center");
	}
	
	public Viewer(){
		init();
	}
	
	private void init(){
		pnShadow = new JPanel();
		viewerLabel = new JLabel();
		pnWindow.setLayout(new BorderLayout());
		pnShadow.setLayout(new BorderLayout());
		shadow = new JWindow();
		
		shadow.setLayout(new BorderLayout());
		shadow.add(pnShadow,"Center");
		
		setLayout(new BorderLayout());
		add(pnWindow,"Center");
		
		pnWindow.setBackground(Color.BLACK);
		pnShadow.setBackground(SwingConstans.getInstance().colorShadow);
	}
	
	private void drawViewer(){
		shadow.setVisible(true);
		setVisible(true);
		timer = new Timer(1, new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent ae) {
				currentWidth = getSize().width;
				currentHeight = getSize().height;
				currentLocation = getLocation();
				
				if (currentHeight/2 > currentWidth){
					currentWidth = maxWidth;
					currentHeight = maxHeight;
				}

				if (getSize().width < maxWidth){
					currentWidth += step;
					currentLocation.x = getLocation().x-step/2;
				} else {
					currentWidth = maxWidth;
				}
				if (getSize().height < maxHeight){
					currentHeight += step;
					currentLocation.y = getLocation().y-step/2;
				} else {
					currentHeight = maxHeight;
				}
							
				setLocation(currentLocation);	
				setSize(currentWidth, currentHeight);
				
				if (currentHeight == maxHeight && currentWidth == maxWidth){
					shadow.setLocation(currentLocation.x+3, currentLocation.y+3);	
					shadow.setSize(currentWidth+1, currentHeight+1);
					
					setSize(maxWidth+2, maxHeight+2);
					viewerLabel.setIcon(viewerImage);
					viewerLabel.setText(text);
					timer.stop();
				}
				
			}
        });
		timer.start();
	}
	
	public void startUp(){
		if (Frames.getInstance().tbViewer.isSelected() || showAlways){
			if (hasEffect) {
				setSize(0,0);
			} else {
				setSize(maxWidth,maxHeight);
			}
			timer = new Timer(startUpTime, new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent ae) {
					timer.stop();	
					drawViewer();
				}
	        });
			timer.start();
		}
	}
	
	public void showStartUpImage(){
		showStartUpImage(null);
	}
	
	public void showStartUpImage(Point p){
			setSize(maxWidth,maxHeight);	
			
			// Get the size of the screen
		    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		    
		    // Determine the new location of the window
		    int w = getSize().width;
		    int h = getSize().height;
		    int x = (dim.width-w)/2;
		    int y = (dim.height-h)/2;
		    
		    // Move the window
		    if (p == null) setLocation(x, y);
		    else setLocation(p.x, p.y);
			
			timer = new Timer(startUpTime, new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent ae) {
					timer.stop();	
					drawViewer();
				}
	        });
			timer.start();
		
	}
	
	public void showErrorMessage(int width, int height){
		
		//Add text pane with scroll (background not working)
		pnWindow.add(textPane,"Center");
		
		this.maxWidth = width;
		this.maxHeight = height;
		setSize(maxWidth,maxHeight);	
		
		// Get the size of the screen
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    // Determine the new location of the window
	    int w = getSize().width;
	    int h = getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    // Move the window
	    setLocation(x, y);
	    
	    JButton jbOk = new JButton("Ok"); 
	    JPanel pnButtons = new JPanel();
	    pnButtons.add(jbOk);
	    pnWindow.add(pnButtons,"South");
	    
	    jbOk.addActionListener(this);
	    jbOk.setActionCommand("errorok");
	    		
	    drawViewer();
		timer.start();
	}
	
	public void showExitImage(){
		
		//Add text pane without scroll to show background
		pnWindow.add(textPane,"Center");
		
		setSize(maxWidth,maxHeight);	
		
		// Get the size of the screen
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    
	    // Determine the new location of the window
	    int w = getSize().width;
	    int h = getSize().height;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2;
	    
	    // Move the window
	    setLocation(x, y);
	    		
	    drawViewer();
		timer.start();
	
	}
	
	public void shutDown(){
		if (timer != null) timer.stop();
		shadow.setVisible(false);
		setVisible(false);
	}
	
	public void shutDown(int shutDownTime){
		if ((Frames.getInstance().tbViewer.isSelected() || showAlways) && timer != null){
			timerOff = new Timer(shutDownTime, new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent ae) {
					timerOff.stop();
					shadow.setVisible(false);
					setVisible(false);
					if (exitOnShutDown){
						System.exit(0);
					}
				}
	        });
			timerOff.start();
		}
	}
	
	public boolean isShowAlways() {
		return showAlways;
	}

	public void setShowAlways(boolean showAlways) {
		this.showAlways = showAlways;
	}
	
	public void setAnimation(boolean hasEffect){
		this.hasEffect = hasEffect;
	}
	
	public boolean isExitOnShutDown() {
		return exitOnShutDown;
	}

	public void setExitOnShutDown(boolean exitOnShutDown) {
		this.exitOnShutDown = exitOnShutDown;
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand().toLowerCase().trim();
		
		if (cmd.equals("errorok")){
			shutDown();
		}
		
	}

}
