package Roto.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import Roto.Basic.Actions;
import Roto.Basic.SwingConstans;
import Roto.Utils.PropertyUtil;
import Roto.Windows.WindowComponents;
import Roto.Windows.WindowTypes;

public class DisplayCanvas extends JPanel implements MouseListener {
	private static final long serialVersionUID = 1L;
	private static DisplayCanvas instance = null;
	private SwingConstans swc;
	private PropertyUtil propertyUtil;
	private int x, xPosition, y, yPosition;
	private BufferedImage bi;
	private Image image;
	private JLabel jlColorNone,jlWhite,jlBrown,jlTitan,jlNewSilver,jlNatureSilver,jlGold,jlMatGold,
			jlBrass,jlLeft,jlRight,jlOver,jlHandle,jlLiftingMd,jlCatchNone,jlCatch,jlCatchMagnet,jlFan,
			jlCilinder,jlDragImage,jlHandleNone,jlLiftingMdNone,jlFanNone,
			jlCilinderNone,jlOliveBrown,jlGreyBrown,jlMiddleBrass,jlDarkBrass, jlCreamWhite, jlJetBlack;
	public JLabel jlBar,jlClosableBar,jlSelectionBar,jlWarning,jlNT,jlNX;
	private JPanel pnWarning;
	private MediaTracker mt;
	private int canvasToolBarX, canvasToolBarY;
	private WindowComponents wComponents;
	private WindowTypes wTypes;
	public List<JLabel> labelList;
	private Viewer viewer, warningViewer;
	private HashMap<String,String> labelViewImages;
	private Thread hideThread;
	private String warningMessage = "";
	
	public static DisplayCanvas getInstance() {
		if (instance == null) {
			instance = new DisplayCanvas();
		}
		return instance;
	}

	public DisplayCanvas() {
		swc = SwingConstans.getInstance();
		wComponents = WindowComponents.getInstance();
		wTypes = WindowTypes.getInstance();
		propertyUtil = PropertyUtil.getInstance();
		labelList = new ArrayList<JLabel>();
		mt = new MediaTracker(this);
		mt.addImage(image, 1);
		jlWarning = new JLabel(new ImageIcon(swc.iconPath + "Warning.jpg"));
		pnWarning = new JPanel();
		labelViewImages = new HashMap<String, String>();
		try {
			mt.waitForAll();
		} catch (Exception e) {
			System.out.println("Exception while loading image.");
		}
		createGUI();
	}
	
	public void setLabels(JRNumberTextField labelWidth, JRNumberTextField labelWidth2, JRNumberTextField labelHeight, 
			JRNumberTextField labelHeight2,JRNumberTextField labelCorrectionWidth, JRNumberTextField labelCorrectionHeight,
			JRNumberTextField labelGarniture){
		wComponents.setLabels(labelWidth, labelWidth2, labelHeight, labelHeight2);
	}
	
	public void showWarning(boolean showWarning, final String warningMessage) {
	    pnWarning.setVisible(showWarning);
	    
	    if (showWarning) {
	        this.warningMessage = warningMessage;
	        warningViewer = new Viewer(this.warningMessage);
	        try {
    	        warningViewer.setLocation(pnWarning.getLocationOnScreen().x + pnWarning.getBounds().width + 5,
                        pnWarning.getLocationOnScreen().y - pnWarning.getBounds().height / 2);
    	        warningViewer.setAnimation(false);
                warningViewer.startUp();
                warningViewer.shutDown(3000);
	        } catch (Exception e) {}

			if (hideThread == null) {
    			hideThread = new Thread(new Runnable() {
                    
                    public void run() {
                        while (hideThread != null && hideThread.isAlive()) { 
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                            jlWarning.setVisible(false);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                            }
                            jlWarning.setVisible(true);
                        }
                    }
    
                });
    			
    			hideThread.start();
			}
			
		} else {
			if (hideThread != null) {
			    hideThread.interrupt();
			    hideThread = null;
			}
			if (warningViewer != null) {
			    warningViewer.shutDown();
			}
		}
	}
	
	private void createGUI(){
		setBackground(Color.white);
		setPreferredSize(new Dimension(600,800));
		setLayout(null);
		canvasToolBarX = 10;
		canvasToolBarY = 10;
		
		//Add dragging image
		add(jlDragImage = new JLabel("label"));
		jlDragImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
			
		//Create label list for toolbar (label name have to contain the image name) 
		labelList.add(createCanvasLabel(jlLeft,"jlLeft", "Left.jpg", propertyUtil.getLangText("ToolBarButton.Left.ToolTipText")));
		labelList.add(createCanvasLabel(jlRight,"jlRight", "Right.jpg", propertyUtil.getLangText("ToolBarButton.Right.ToolTipText")));
		labelList.add(createCanvasLabel(jlOver,"jlOver", "Over.jpg", propertyUtil.getLangText("ToolBarButton.Over.ToolTipText")));
		
		labelList.add(createCanvasLabel(jlCatchNone,"jl_CatchNone", "CatchNone.jpg", propertyUtil.getLangText("ToolBarButton.CatchNone.ToolTipText")));
		labelList.add(createCanvasLabel(jlCatch,"jl_Catch", "Catch.jpg", propertyUtil.getLangText("ToolBarButton.Catch.ToolTipText")));
		labelList.add(createCanvasLabel(jlCatchMagnet,"jl_CatchMagnet", "CatchMagnet.jpg", propertyUtil.getLangText("ToolBarButton.CatchMagnet.ToolTipText")));
		
		labelList.add(createCanvasLabel(jlHandleNone,"jl_HandleNone", "HandleNone.jpg", propertyUtil.getLangText("ToolBarButton.HandleNone.ToolTipText")));
		labelList.add(createCanvasLabel(jlHandle,"jl_Handle", "Handle.jpg", propertyUtil.getLangText("ToolBarButton.Handle.ToolTipText")));
		labelList.add(new JLabel());
		
		labelList.add(createCanvasLabel(jlLiftingMdNone,"jl_LiftingMdNone", "LiftingMDNone.jpg", propertyUtil.getLangText("ToolBarButton.LiftingMDNone.ToolTipText")));
		labelList.add(createCanvasLabel(jlLiftingMd,"jl_LiftingMd", "LiftingMD.jpg", propertyUtil.getLangText("ToolBarButton.LiftingMD.ToolTipText")));
		labelList.add(new JLabel());
		
		labelList.add(createCanvasLabel(jlFanNone,"jl_FanNone", "FanNone.jpg", propertyUtil.getLangText("ToolBarButton.FanNone.ToolTipText")));
		labelList.add(createCanvasLabel(jlFan,"jl_Fan", "Fan.jpg", propertyUtil.getLangText("ToolBarButton.Fan.ToolTipText")));
		labelList.add(new JLabel());
		
		jlBar = createCanvasLabel(jlBar,"jl_Bar", "Bar.jpg", propertyUtil.getLangText("ToolBarButton.Bar.ToolTipText"));
		labelList.add(jlBar);
		jlClosableBar = createCanvasLabel(jlClosableBar,"jl_ClosableBar", "ClosableBar.jpg", propertyUtil.getLangText("ToolBarButton.ClosableBar.ToolTipText"), "25");
		labelList.add(jlClosableBar);
		jlSelectionBar = createCanvasLabel(jlSelectionBar,"jl_SelectionBar", "Bar.jpg", propertyUtil.getLangText("ToolBarButton.SelectionBar.ToolTipText"), "K");
		labelList.add(jlSelectionBar);
		jlNT = createCanvasLabel(jlNT,"jl_NT", "NT.jpg", propertyUtil.getLangText("ToolBarButton.NT.ToolTipText"), "3/100");
		labelList.add(jlNT);
		jlNX = createCanvasLabel(jlNX,"jl_NX", "NX.jpg", propertyUtil.getLangText("ToolBarButton.NX.ToolTipText"), "3/130");
		labelList.add(jlNX);
		labelList.add(new JLabel());
				
		labelList.add(createCanvasLabel(jlCilinderNone,"jl_CilinderNone", "LockNone.jpg", propertyUtil.getLangText("ToolBarButton.CilinderNone.ToolTipText")));
		labelList.add(createCanvasLabel(jlCilinder,"jl_Cilinder", "Lock.jpg", propertyUtil.getLangText("ToolBarButton.Cilinder.ToolTipText")));
		labelList.add(new JLabel());
		
		labelList.add(createCanvasLabel(jlColorNone,"jl_colornone", "ColorNone.jpg", propertyUtil.getLangText("ToolBarButton.ColorNone.ToolTipText")));
		labelList.add(createCanvasLabel(jlWhite,"jl_colorwhite", "ColorWhite.jpg", propertyUtil.getLangText("ToolBarButton.White.ToolTipText")));
		labelList.add(createCanvasLabel(jlBrown,"jl_colorbrown", "ColorBrown.jpg", propertyUtil.getLangText("ToolBarButton.Brown.ToolTipText")));
		labelList.add(createCanvasLabel(jlOliveBrown,"jl_colorolivebrown", "ColorOliveBrown.jpg", propertyUtil.getLangText("ToolBarButton.OliveBrown.ToolTipText")));
		labelList.add(createCanvasLabel(jlGreyBrown,"jl_colorgreybrown", "ColorGreyBrown.jpg", propertyUtil.getLangText("ToolBarButton.GreyBrown.ToolTipText")));
		labelList.add(createCanvasLabel(jlMatGold,"jl_colormatgold", "ColorMatGold.jpg", propertyUtil.getLangText("ToolBarButton.MatGold.ToolTipText")));
		labelList.add(createCanvasLabel(jlGold,"jl_colorgold", "ColorGold.jpg", propertyUtil.getLangText("ToolBarButton.Gold.ToolTipText")));
		labelList.add(createCanvasLabel(jlTitan,"jl_colortitan", "ColorTitan.jpg", propertyUtil.getLangText("ToolBarButton.Titan.ToolTipText")));
		labelList.add(createCanvasLabel(jlBrass,"jl_colorbrass", "ColorBrass.jpg", propertyUtil.getLangText("ToolBarButton.Brass.ToolTipText")));
		labelList.add(createCanvasLabel(jlMiddleBrass,"jl_colormiddlebrass", "ColorMiddleBrass.jpg", propertyUtil.getLangText("ToolBarButton.MiddleBrass.ToolTipText")));
		labelList.add(createCanvasLabel(jlNatureSilver,"jl_colornaturesilver", "ColorNatureSilver.jpg", propertyUtil.getLangText("ToolBarButton.NatureSilver.ToolTipText")));
		labelList.add(createCanvasLabel(jlNewSilver,"jl_colornewsilver", "ColorNewSilver.jpg", propertyUtil.getLangText("ToolBarButton.NewSilver.ToolTipText")));
		labelList.add(createCanvasLabel(jlDarkBrass,"jl_colordarkbrass", "ColorDarkBrass.jpg", propertyUtil.getLangText("ToolBarButton.DarkBrass.ToolTipText")));
		labelList.add(createCanvasLabel(jlCreamWhite,"jl_colorcreamwhite", "ColorCreamWhite.jpg", propertyUtil.getLangText("ToolBarButton.CreamWhite.ToolTipText")));
		labelList.add(createCanvasLabel(jlJetBlack,"jl_colorjetblack", "ColorJetBlack.jpg", propertyUtil.getLangText("ToolBarButton.JetBlack.ToolTipText")));
		
		createCanvasToolBarLeft(labelList, canvasToolBarX, canvasToolBarY, new Dimension(30,30));
		
		pnWarning.setBounds(125,
                35, 
                30,30);
        pnWarning.setLayout(new BorderLayout());
        pnWarning.setBackground(getBackground());
        pnWarning.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
              warningViewer.setAnimation(false);
              warningViewer.startUp();
              warningViewer.setLocation(pnWarning.getLocationOnScreen().x + pnWarning.getBounds().width + 5,
                                        pnWarning.getLocationOnScreen().y - pnWarning.getBounds().height / 2);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                warningViewer.shutDown(500);
                warningViewer = new Viewer(warningMessage);
            }
        });

        add(pnWarning);
        pnWarning.add(jlWarning, BorderLayout.CENTER);
        pnWarning.setVisible(false);
	}
	
	private JLabel getLabelByName(String labelName){
		JLabel selectedLabel = null;
		for (JLabel label : labelList){
			if (label.getName()!=null && label.getName().equals(labelName)){
				selectedLabel = label;
			}
		}
		return selectedLabel;
	}
	
	public void setLabelImage(String labelName, ImageIcon image){
		JLabel canvasLabel = getLabelByName(labelName);
		if (canvasLabel != null) {
			canvasLabel.setIcon(image);
		}
	}
	
	public void setLabelProperties(String labelName, ToolBarButton toolbarButton){
		JLabel canvasLabel = getLabelByName(labelName);
		if (canvasLabel != null) {
			canvasLabel.setEnabled(toolbarButton.isEnabled());
		}
	}
	
	public void setLabelProperties(String labelName, JMenuItem menuItem){
		JLabel canvasLabel = getLabelByName(labelName);
		if (canvasLabel != null) {
			canvasLabel.setEnabled(menuItem.isEnabled());
		}
	}
	
	private JLabel createCanvasLabel(JLabel label,String labelName,String imageName, String toolTipText) {
		return createCanvasLabel(label, labelName, imageName, toolTipText, null);
	}
	
	private JLabel createCanvasLabel(JLabel label,String labelName,final String imageName, String toolTipText, final String imageText) {
		label = new JLabel(new ImageIcon(swc.iconPath + imageName), JLabel.CENTER) {

			private static final long serialVersionUID = -7632103153512224602L;
			
			String canvasImageText = imageText;
			
			@Override
			public void setText(final String imText) {
				this.canvasImageText = imText;
				this.repaint();
			}

			@Override
			public void paint(Graphics g) {
				
				super.paint(g);
				if (canvasImageText != null) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setFont(swc.fontCanvas);
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
					g2.drawString(canvasImageText, 1, getFont().getSize() -2);
				}
			}
			
		};

		label.setName(labelName);
		label.addMouseListener(this);
		label.addMouseMotionListener(new MouseMotionHandler());
		label.setToolTipText(toolTipText);
		label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		String viewImageName = labelName.replaceAll("jl_", "") + "_view.jpg";
		labelViewImages.put(labelName, viewImageName);
		return label;
	}
	
	private void createCanvasToolBarLeft(List<JLabel> labelList, int x, int y, Dimension iconDimension){
		int cols = 0;
		int componentX = x;
		int componentY = y;
		for (JLabel label : labelList){
			if (cols ==3) {
				componentX = x;
				componentY += (int)iconDimension.getHeight() + 3;
				cols = 0;
			}
			add(label);
			label.setBounds(componentX, componentY, (int)iconDimension.getWidth(), (int)iconDimension.getHeight());
			componentX += (int)iconDimension.getWidth() + 3;
			
			cols ++;
			
		}
		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
		
		wComponents.setGraphic(g2);
		wComponents.setSize(200, 350);
		wComponents.setLocation(getWidth()/2 +80 - wComponents.getSize().width/2, 60);
		wComponents.drawToolBar(5, 5);
		wTypes.drawWindow();
		
		/*System.out.println(wComponents.getWindowHit().x + "," + wComponents.getWindowHit().y
				+"," + wComponents.getWindowHit().width + "," + wComponents.getWindowHit().height);
*/
	//	g2.drawImage(bi, x, y, this);
	}
	
	class MouseMotionHandler extends MouseMotionAdapter {

		/* public void mouseMoved(MouseEvent e){
			 if (e.getSource().getClass().equals(new JLabel().getClass())){
					JLabel selectedLabel = (JLabel) e.getSource();
					selectedLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				}
		 }  */ 
		
		public void mouseDragged(MouseEvent e) {
		
			x = e.getX() - bi.getWidth()/2 + xPosition;

			y = e.getY() - bi.getHeight()/2 + yPosition;
			
			jlDragImage.setLocation(x,y);
			
			jlDragImage.setVisible(jlDragImage.isEnabled());
			//repaint();
		}

	}
	
	private void setLabelDragImage(JLabel label){
		ImageIcon ic =  (ImageIcon) label.getIcon();
		image = ic.getImage();
		xPosition = label.getX();
		yPosition = label.getY();
		bi = new BufferedImage(image.getWidth(this), image.getHeight(this),	BufferedImage.TYPE_INT_ARGB);
	
	/*	Graphics2D big = bi.createGraphics();
		big.drawImage(image, 0, 0, this);*/
		
		jlDragImage.setIcon(ic);
		jlDragImage.setBounds(x, y, bi.getHeight(), bi.getWidth());
		jlDragImage.setEnabled(label.isEnabled());
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == jlClosableBar && e.getButton() != MouseEvent.BUTTON1){
			Actions.getInstance().doAction("popupclosablebarshow");	 
		} else if (e.getSource() == jlSelectionBar && e.getButton() != MouseEvent.BUTTON1){
			Actions.getInstance().doAction("selectionbarpressed");	 
		} else if (e.getSource() == jlNT && e.getButton() != MouseEvent.BUTTON1){
			Actions.getInstance().doAction("ntpressed");	 
		} else if (e.getSource() == jlNX && e.getButton() != MouseEvent.BUTTON1){
			Actions.getInstance().doAction("nxpressed");	 
		}
	}
	
	public void mouseEntered(MouseEvent e) {
		 if (e.getSource() instanceof JLabel){
				JLabel selectedLabel = (JLabel) e.getSource();
				if (selectedLabel.isEnabled()){
					selectedLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
				}
				//Create image view by the name of the label if exists.
				String labelName = selectedLabel.getName();
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				
				if (labelName.contains("jl_")){
					String imageName = labelViewImages.get(labelName);
					
					File imageFile = new File(swc.viewerPath + imageName);
					if (imageFile.exists()){
						ImageIcon image = new ImageIcon(swc.viewerPath + imageName);
						viewer = new Viewer(image);
						viewer.setLocation(x-image.getIconWidth(),y );
						viewer.startUp();
					}
				}
		}
	}

	public void mouseExited(MouseEvent e) {
		if (e.getSource() instanceof JLabel){
			JLabel selectedLabel = (JLabel) e.getSource();
			//selectedLabel.setBorder(BorderFactory.createLoweredBevelBorder());
			selectedLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			if (viewer !=null){
				viewer.shutDown();
			}
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.getSource() instanceof JLabel) {
			JLabel selectedLabel = (JLabel) e.getSource();
			setLabelDragImage(selectedLabel);
			
			//Enable clicking
			x = wComponents.getWindowHit().x + 50;
			y = wComponents.getWindowHit().y + 50;
		}
	}

	public void mouseReleased(MouseEvent e) {
		bi = null;
		if (e.getSource() instanceof JLabel){
			jlDragImage.setVisible(false);
			if (wComponents.isWindowHit(x, y) && jlDragImage.isEnabled()){
				JLabel selectedLabel = (JLabel) e.getSource();
				Actions.getInstance().canvasSelected(selectedLabel.getName());
		}
	}
		//repaint();
	}
	
	public HashMap<String, String> getLabelViewImages() {
		return labelViewImages;
	}

	public void setLabelViewImages(HashMap<String, String> labelViewImages) {
		this.labelViewImages = labelViewImages;
	}
}