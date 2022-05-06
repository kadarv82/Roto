package Roto.Windows;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

import com.sun.xml.internal.bind.v2.runtime.Location;

import Roto.Gui.DisplayCanvas;
import Roto.Gui.Frames;

public class WindowTypes {
	
	private static WindowTypes instance = null;
	private WindowComponents wComponents;
	private String classification = "";
	private boolean left, right, over, handle, liftingMD, isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI,
					isMDSMDF2, isMDSMDK2, catcNone, catcNormal, catcMagnet, fan, closableBar, normalBar, cilinder, isDoor;
	
	
	public static WindowTypes getInstance() {
		if (instance == null) {
			instance = new WindowTypes();
		}
		return instance;
	}
	
	public WindowTypes(){
		this.wComponents = WindowComponents.getInstance();
	}

	public void setSelection(boolean left, boolean right, boolean over, boolean handle, boolean liftingMD, String colorSign, int width, int height,
			boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2, boolean isMDF2, boolean isMDS2, boolean isMFREI,
			boolean isMDSMDF2, boolean isMDSMDK2, boolean catcNone, boolean catcNormal, boolean catcMagnet, boolean fan, boolean closableBar,
			String closableBarSize, boolean normalBar, boolean cilinder){
		
		this.left = left;
		this.right = right;
		this.over = over;
		this.handle = handle;
		this.liftingMD = liftingMD;
		this.catcNone = catcNone;
		this.catcNormal = catcNormal;
		this.catcMagnet = catcMagnet;
		this.cilinder = cilinder;
		this.fan = fan;
		this.closableBar = closableBar;
		this.isMDK = isMDK;
		this.isMDF = isMDF;
		this.isMDS = isMDS;
		this.isMDK2 = isMDK2;
		this.isMDF2 = isMDF2;
		this.isMDS2 = isMDS2;
		this.isMFREI = isMFREI;
		this.isMKIPP = isMKIPP;
		this.isMDSMDK2 = isMDSMDK2;
		this.isMDSMDF2 = isMDSMDF2;
		
		setToolBarDirection();
		wComponents.setHegihtSizeDistance(30);
		wComponents.setWindowHit(new Rectangle(999999,999999,0,0));
	}
	
	//Set back the original direction by toolbar settings
	private void setToolBarDirection(){
		if (left) wComponents.setLeft();
		else if (right) wComponents.setRight();
		else if (over)	{
				wComponents.setOver("top");
				if (isMFREI) wComponents.setOver();
			}
	}
	
	//Create double frame
	private void createDoubleFrame(boolean CreateFrameToLeft, boolean componentsEnabled, boolean leftComponents, int frameDistanceWidth, int frameDistanceHeight){
		Point origLocation = wComponents.getLocation();
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y - frameDistanceHeight);	
		} else {
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + frameDistanceWidth, origLocation.y -frameDistanceHeight);	
		}
		
		if (!isDoor) wComponents.drawGlass();
		//Draw an empty frame if not enabled
		if (!componentsEnabled) {wComponents.drawFrame(0.3f);}
		//Draw components if enabled.
		else {
			
				if (leftComponents) {
					wComponents.setLeft();
				} else {
					wComponents.setRight();
				}
				wComponents.drawFrame();
				
				if (!isDoor){ 
					wComponents.drawStrap();
				} else {
					wComponents.drawStrapDoor();
				}
				
				wComponents.drawDirection();
				
				//Set original direction
				setToolBarDirection();
		}
		
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + frameDistanceWidth, origLocation.y);
		} else {
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		}
		wComponents.setNormalPen();
	}
	
	//Create double frame Arc
	private void createDoubleFrameArc(boolean CreateFrameToLeft, boolean componentsEnabled, boolean leftComponents){
		Point origLocation = wComponents.getLocation();
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		} else {
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);	
		}
		
		wComponents.drawGlass();
		//Draw an empty frame if not enabled
		if (!componentsEnabled) {
			if (left) wComponents.drawFrameArc(0.3f,"right");
			if (right) wComponents.drawFrameArc(0.3f,"left");
		}
		//Draw components if enabled.
		else {
			
				if (leftComponents) {
					wComponents.setLeft();
					wComponents.drawFrameArc(0,"left");
				} else {
					wComponents.setRight();
					wComponents.drawFrameArc(0,"right");
				}
				
				wComponents.drawDirectionArc();
				wComponents.drawStrapArc();
				//Set original direction
				setToolBarDirection();
		}
		
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);
		} else {
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		}
		wComponents.setNormalPen();
	}
	
	//Create double frame Segment
	private void createDoubleFrameSegment(boolean CreateFrameToLeft, boolean componentsEnabled, boolean leftComponents){
		Point origLocation = wComponents.getLocation();
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		} else {
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);	
		}
		
		wComponents.drawGlass();
		//Draw an empty frame if not enabled
		if (!componentsEnabled) {
			if (left) wComponents.drawFrameSegment(0.3f,"right");
			if (right) wComponents.drawFrameSegment(0.3f,"left");
		}
		//Draw components if enabled.
		else {
			
				if (leftComponents) {
					wComponents.setLeft();
					wComponents.drawFrameSegment(0,"left");
				} else {
					wComponents.setRight();
					wComponents.drawFrameSegment(0,"right");
				}
				
				wComponents.drawDirectionArc();
				wComponents.drawStrapArc();
				//Set original direction
				setToolBarDirection();
		}
		
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);
		} else {
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		}
		wComponents.setNormalPen();
	}
	
	//Create HKS frame
	private void createHKSFrame(boolean CreateFrameToLeft){
		Point origLocation = wComponents.getLocation();
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		} else {
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);	
		}
		
		wComponents.drawGlass();
		if (CreateFrameToLeft){
			wComponents.setRight();
		} else {
			wComponents.setLeft();
		}
		wComponents.drawFrame(0.3f);
		wComponents.drawDirectionArray();
		
		
		if (CreateFrameToLeft){
			//Draw the first frame from 4 to the left size
			wComponents.setLocation(wComponents.getLocation().x - wComponents.getSize().width -5, origLocation.y);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
			//Draw the fourth frame from 4 to the right size			
			wComponents.setLocation(wComponents.getLocation().x + (wComponents.getSize().width +5)*3 +5, origLocation.y);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
		} 
		
		else {
			//Draw the fourth frame from 4 to the right size
			wComponents.setLocation(wComponents.getLocation().x + wComponents.getSize().width +5, origLocation.y);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
			//Draw the first frame from 4 to the left size
			wComponents.setLocation(wComponents.getLocation().x - ((wComponents.getSize().width +5)*3)-5, origLocation.y);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();

		}
		
		//Set original direction
		setToolBarDirection();
		
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);
		} else {
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		}
		wComponents.setNormalPen();
	}
	
	//Create Pusher frame
	private void createPusherFrame(boolean CreateFrameToLeft, boolean drawArrays){
		Point origLocation = wComponents.getLocation();
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2+8, origLocation.y);	
		} else {
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2, origLocation.y);	
		}
		
		wComponents.drawGlass();
		
		if (CreateFrameToLeft){
			wComponents.setRight();
		} else {
			wComponents.setLeft();
		}
		wComponents.drawFrame(0.3f);
		wComponents.drawDirectionArraySimple();
		
		
		if (CreateFrameToLeft){
			//Draw the first frame from 4 to the left size
			wComponents.setLocation(wComponents.getLocation().x +5 - wComponents.getSize().width , origLocation.y-8);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
			wComponents.setLeft();
			if (drawArrays) wComponents.drawDirectionArraySimple();
			wComponents.setRight();
			//Draw the fourth frame from 4 to the right size			
			wComponents.setLocation(wComponents.getLocation().x -10+ (wComponents.getSize().width +5)*3 -15, origLocation.y-8);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
			if (drawArrays) wComponents.drawDirectionArraySimple();
		} 
		
		else {
			//Draw the fourth frame from 4 to the right size
			wComponents.setLocation(wComponents.getLocation().x + wComponents.getSize().width -5, origLocation.y-8);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
			wComponents.setRight();
			if (drawArrays) wComponents.drawDirectionArraySimple();
			wComponents.setLeft();
			//Draw the first frame from 4 to the left size
			wComponents.setLocation(wComponents.getLocation().x - ((wComponents.getSize().width +5)*3)+25, origLocation.y-8);
			wComponents.drawFrame(0.3f);
			wComponents.drawGlass();
			if (drawArrays) wComponents.drawDirectionArraySimple();

		}
		
		//Set original direction
		setToolBarDirection();
		
		if (CreateFrameToLeft){
			wComponents.setLocation(origLocation.x + wComponents.getSize().width/2 + 8, origLocation.y);
		} else {
			wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
		}
		wComponents.setNormalPen();
	}
	
	public void drawWindow(){
		
		wComponents.hideSizeComponents(2);
		this.isDoor = false;
		
		if (classification.equals("Class 1")){
			drawClass_1();
		} else if (classification.equals("Class 2")){
			drawClass_2();
		} else if (classification.equals("Class 3")){
			drawClass_3();
		} else if (classification.equals("Class 4")){
			drawClass_4();
		} else if (classification.equals("Class 5")){
			drawClass_5();
		} else if (classification.equals("Class 6")){
			drawClass_6();
		} else if (classification.equals("Class 7")){
			drawClass_7();
		} else if (classification.equals("Class 8")){
			drawClass_8();
		}

	}
	
	public void drawClass_1(){
		wComponents.setSize(220, 350);
		
		//Draw double frame
		if (isMDS || isMDK2 || isMDF2 || isMDS2 || isMDSMDK2 || isMDSMDF2) {
			//Create another window, modify location !
			createDoubleFrame(right, isMDS2 || isMDSMDK2 || isMDSMDF2 , !left, 8,0);
			
			Dimension origSize = wComponents.getSize(); 
			Point origLocation = wComponents.getLocation();
			
			if (isMDS2) {
				wComponents.setSize(origSize.width * 2 + 10, origSize.height);
				if (right){
					wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
				}
			}
			if (right) wComponents.drawSize("right");
			if (left) wComponents.drawSize("left");

			//Draw second size input
			if (isMDSMDK2 || isMDSMDF2){
					
				if (right) {
					wComponents.setLocation(origLocation.x -wComponents.getSize().width-10, origLocation.y);
					wComponents.drawSize("right",true, false);
				}
				if (left) {
					wComponents.setLocation(origLocation.x +wComponents.getSize().width+10, origLocation.y);
					wComponents.drawSize("left",true, false);
					}
			}
			
			//Set back original location and size
			wComponents.setSize(origSize.width, origSize.height);
			wComponents.setLocation(origLocation.x, origLocation.y);
		} else {
			wComponents.drawSize("left");
		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		wComponents.drawGlass();
		if (handle) wComponents.drawHandle();
		wComponents.drawBar();
		if (cilinder) wComponents.drawCilinder();
		wComponents.drawDirection();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		wComponents.drawFrame();
		wComponents.drawStrap();
		
		//Draw an over direction too
		if ((isMDK || isMDK2 || isMDS2 || isMDSMDK2) && ! over) { 
			wComponents.setOver();
			wComponents.drawDirection();
			setToolBarDirection();
		}
		wComponents.setPen(1);
		
	}
	
	public void drawClass_2(){
		wComponents.setSize(160, 350);
		this.isDoor = true;
		
		//Draw double frame
		if (isMDS || isMDS2) {
			//Create another window, modify location !
			createDoubleFrame(right, isMDS || isMDS2, !left, 8, 0);
			
			Dimension origSize = wComponents.getSize(); 
			Point origLocation = wComponents.getLocation();
			
			if (1==0) {
				wComponents.setSize(origSize.width * 2 + 10, origSize.height);
				if (right){
					wComponents.setLocation(origLocation.x -wComponents.getSize().width/2, origLocation.y);	
				}
			}
			if (right) wComponents.drawSize("right");
			if (left) wComponents.drawSize("left");

			//Set back original location and size
			wComponents.setSize(origSize.width, origSize.height);
			wComponents.setLocation(origLocation.x, origLocation.y);
		} else {
			wComponents.drawSize("left");
		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		if (handle) wComponents.drawHandleDoor();
		if (isMDF || isMDS) {
			wComponents.drawBar("_3");	
		} else {
			wComponents.drawBar("_4");
		}
		if (cilinder) wComponents.drawCilinder();
		wComponents.drawDirection();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		wComponents.drawFrame();
		wComponents.drawStrapDoor();
		wComponents.setPen(1);
	}
	
	public void drawClass_3(){
		wComponents.setSize(130, 350);
		if (isMDS || isMDF2){
			createHKSFrame(!right);
		
		} else {
			createDoubleFrame(right, false, false, 8, 0);
		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		wComponents.drawGlass();
		wComponents.drawDirectionArray();
		
		//Set opposite direction to draw components
		if (left) {wComponents.setRight();}
		else {wComponents.setLeft();}
		
		if (handle) wComponents.drawHandle();
		wComponents.drawBar();
		if (cilinder) wComponents.drawCilinder();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		
		//Set back original direction
		setToolBarDirection();
		
		wComponents.drawFrame();
		
	
		
		if (left) {
			wComponents.setHegihtSizeDistance(wComponents.getSize().width+ 5 + 20);
			wComponents.drawSize("right");
		} 
		if (right){
			if (isMDS || isMDF2){
				wComponents.setHegihtSizeDistance((wComponents.getSize().width + 5) *2 + 20);	
			} else {
				wComponents.setHegihtSizeDistance(5 + 20);
			}
			
			wComponents.drawSize("right");
		}
				
		wComponents.setPen(1);
	}
	
	public void drawClass_4(){
		wComponents.hideSizeComponents(2);
		wComponents.setSize(220, 350);
		wComponents.setArcStartHeight(wComponents.getSize().height/10*4);
		String arcPosition = "center";
		
		//Draw double frame
		if (isMDS || isMDK2 || isMDF2 || isMDSMDK2 || isMDSMDF2) {
			wComponents.setSize(wComponents.getSize().width/10 * 7, 350);	
			//Create another window, modify location !
			createDoubleFrameArc(right, isMDS2 || isMDSMDK2 || isMDSMDF2 , !left);
			if (left) arcPosition = "left";
			if (right) arcPosition = "right";
		} 
		
		wComponents.drawFrameArc(0,arcPosition);
		
		//Modify size array height and location to start array form arc start
		Dimension origSize = wComponents.getSize();
		Point origLocation = wComponents.getLocation();
		if (isMDK || isMDF || isMDK2 || isMDF2 || isMDSMDK2 || isMDSMDF2) {
			wComponents.setLocation(origLocation.x, origLocation.y+wComponents.getArcStartHeight());
			wComponents.setSize(origSize.width,origSize.height - wComponents.getArcStartHeight());
		}				
		//Draw size arrays for arc
		wComponents.drawSize(arcPosition, false , false, true);

		//Set back original size and location after array start modified
		wComponents.setSize(origSize.width, origSize.height);
		wComponents.setLocation(origLocation.x, origLocation.y);
		
		//Draw second width and height
		if (isMDSMDK2 || isMDSMDF2) {
			
			if (right) {
				wComponents.setLocation(origLocation.x -wComponents.getSize().width-10, origLocation.y);
				
				wComponents.drawSize("right",true,true,true);
				wComponents.drawSizeHeight(2, true);
			}
			if (left) {
				wComponents.setLocation(origLocation.x +wComponents.getSize().width+10, origLocation.y);
				
				wComponents.drawSize("left",true,true,true);
				wComponents.drawSizeHeight(2, false);
				}
			
			wComponents.setLocation(origLocation.x, origLocation.y);
		
		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		if (handle) wComponents.drawHandle();
		wComponents.drawBar();
		if (cilinder) wComponents.drawCilinder();
		wComponents.drawDirectionArc();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		wComponents.drawStrapArc();
		//Draw an over direction too
		if ((isMDK || isMDK2 || isMDS2 || isMDSMDK2) && ! over) { 
			wComponents.setOver();
			wComponents.drawDirectionArc();
			setToolBarDirection();
		}
		wComponents.setPen(1);
	}
	
	public void drawClass_5(){
		wComponents.setSize(130, 350);
		wComponents.drawToolBar(5, 5);
		
		Point origLocation = wComponents.getLocation();
		wComponents.setLocation(origLocation.x - wComponents.getSize().width -5, origLocation.y);
		
		//Frame 1 (left)
		wComponents.drawGlass();
		wComponents.drawFrame();
		if (right) {
			wComponents.setLeft();
			wComponents.drawFrameDashed(true);
		}
		wComponents.drawDirection();
		
		if ((isMDK || isMKIPP || isMDF2) && left) {
			wComponents.setOver();
			wComponents.drawDirection();
		}
		
		//Frame 3 (right)	
		setToolBarDirection();
		wComponents.setLocation(origLocation.x + wComponents.getSize().width +5, origLocation.y);
		if (left) {
			wComponents.setRight();
			wComponents.drawFrameDashed(false);
		}
		wComponents.drawGlass();
		wComponents.drawFrame();
		wComponents.drawDirection();
		
		if ((isMDK || isMKIPP || isMDF2) && right) {
			wComponents.setOver();
			wComponents.drawDirection();
		}
		
		//Frame 2 (center)
		setToolBarDirection();
		wComponents.setLocation(origLocation.x, origLocation.y);
		
		wComponents.drawGlass();
		if (right) wComponents.setLeft();
		if (left) wComponents.setRight();
		wComponents.drawDirection();
		wComponents.drawFrame();
		

		setToolBarDirection();
		if (left) wComponents.setLocation(origLocation.x - wComponents.getSize().width -5, origLocation.y);
		if (right) wComponents.setLocation(origLocation.x + wComponents.getSize().width +5, origLocation.y);

		wComponents.setClosable(closableBar);
		if (handle) wComponents.drawHandle();
		wComponents.drawBar();
		if (cilinder) wComponents.drawCilinder();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		
		//Set back original direction
		setToolBarDirection();
				
		if (left) {
			//wComponents.setHegihtSizeDistance(wComponents.getSize().width+ 5 + 20);
			wComponents.drawSize("left");
		} 
		if (right){
			wComponents.drawSize("right");
		}
		
		//Draw door-step
		if(isMDS || isMKIPP || isMDK2 || isMDF2){
			wComponents.drawLineDashed(3,origLocation.x - wComponents.getSize().width -5, origLocation.y+wComponents.getSize().height, origLocation.x + wComponents.getSize().width * 2 + 5, origLocation.y+wComponents.getSize().height);
		}
				
		wComponents.setPen(1);
	}
	
	public void drawClass_6(){
		wComponents.hideSizeComponents(2);
		wComponents.setSize(220, 350);
		wComponents.setArcStartHeight(wComponents.getSize().height/100*15);
		String segmentPosition = "center";
		
		//Draw double frame
		if (isMDS || isMDK2 || isMDF2 || isMDSMDK2 || isMDSMDF2) {
			wComponents.setSize(wComponents.getSize().width/10 * 7, 350);	
			//Create another window, modify location !
			createDoubleFrameSegment(right, isMDS2 || isMDSMDK2 || isMDSMDF2 , !left);
			if (left) segmentPosition = "left";
			if (right) segmentPosition = "right";
		} 
		
		wComponents.drawFrameSegment(0,segmentPosition);
		
		//Modify size array height and location to start array form arc start
		Dimension origSize = wComponents.getSize();
		Point origLocation = wComponents.getLocation();
		if (isMDK || isMDF || isMDK2 || isMDF2 || isMDSMDK2 || isMDSMDF2) {
			wComponents.setLocation(origLocation.x, origLocation.y+wComponents.getArcStartHeight());
			wComponents.setSize(origSize.width,origSize.height - wComponents.getArcStartHeight());
		}				
		//Draw size arrays for arc
		wComponents.drawSize(segmentPosition, false , false, true);

		//Set back original size and location after array start modified
		wComponents.setSize(origSize.width, origSize.height);
		wComponents.setLocation(origLocation.x, origLocation.y);
		
		//Draw second width and height
		if (isMDSMDK2 || isMDSMDF2) {
			
			if (right) {
				wComponents.setLocation(origLocation.x -wComponents.getSize().width-10, origLocation.y);
				
				wComponents.drawSize("right",true,true,true);
				wComponents.drawSizeHeight(2, true);
			}
			if (left) {
				wComponents.setLocation(origLocation.x +wComponents.getSize().width+10, origLocation.y);
				
				wComponents.drawSize("left",true,true,true);
				wComponents.drawSizeHeight(2, false);
				}
			
			wComponents.setLocation(origLocation.x, origLocation.y);
		
		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		if (handle) wComponents.drawHandle();
		wComponents.drawBar();
		if (cilinder) wComponents.drawCilinder();
		wComponents.drawDirectionArc();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		wComponents.drawStrapArc();
		//Draw an over direction too
		if ((isMDK || isMDK2 || isMDS2 || isMDSMDK2) && ! over) { 
			wComponents.setOver();
			wComponents.drawDirectionArc();
			setToolBarDirection();
		}
		wComponents.setPen(1);
	}
	
	public void drawClass_7(){
		wComponents.setSize(110, 350);
		if (isMDF || isMKIPP){
			createPusherFrame(!right,isMKIPP);
		}
		if (isMDK || isMDS){
			createDoubleFrame(right, false, false, -8, 8);
		}
		
		if (isMDK2||isMDF2||isMDS2||isMFREI){
			Point origLocation = wComponents.getLocation();
			
			//Draw frame to center if MFREI (later the location will be set to left or right)
			if (isMFREI) {
				origLocation.x = origLocation.x -wComponents.getSize().width/2;
				
				wComponents.setLocation(origLocation.x, origLocation.y-8);
				wComponents.setSize(wComponents.getSize().width*2, wComponents.getSize().height);
				wComponents.drawGlass();
				wComponents.drawFrame(0.3f);
				origLocation.y = origLocation.y+8;
				wComponents.setSize(wComponents.getSize().width/2, wComponents.getSize().height);
			
			}
			
			//Draw frame 1
			wComponents.setLocation(origLocation.x - wComponents.getSize().width +8, origLocation.y-8);
			wComponents.drawGlass();
			if (isMDS2 || isMFREI) {
				if (right) wComponents.setLeft();
				wComponents.drawDirectionArraySimple();
				setToolBarDirection();
			}
			wComponents.drawFrame(0.3f);
			
			//Draw frame 3
			if (isMFREI) {
				wComponents.setLocation(origLocation.x + wComponents.getSize().width*2 -8, origLocation.y-8);
			} else {
				wComponents.setLocation(origLocation.x + wComponents.getSize().width -8, origLocation.y-8);
			}
			wComponents.drawGlass();
			if (isMDS2 || isMFREI) {
				if (left) wComponents.setRight();
				wComponents.drawDirectionArraySimple();
				setToolBarDirection();
			}

			wComponents.drawFrame(0.3f);
			wComponents.setLocation(origLocation.x, origLocation.y);
			
			//If mfrei, modify location to left or right
			if (isMFREI){
				if (left){
					wComponents.setLocation(origLocation.x - wComponents.getSize().width +8, origLocation.y-8);
				}
				if (right){
					wComponents.setLocation(origLocation.x + wComponents.getSize().width*2 -8, origLocation.y-8);
				}
			}
		}
		
		//Draw plus array on not active frame
		if(isMDS){
			Dimension origSize = wComponents.getSize();
			Point origLocation = wComponents.getLocation();
			if (left) {
				wComponents.setRight();
				wComponents.setLocation(wComponents.getLocation().x + wComponents.getSize().width-5,wComponents.getLocation().y);
				wComponents.drawDirectionArraySimple();
			}
			if (right) {
				wComponents.setLeft();
				wComponents.setLocation(wComponents.getLocation().x - wComponents.getSize().width+5,wComponents.getLocation().y);
				wComponents.drawDirectionArraySimple();
			}
			setToolBarDirection();
			wComponents.setSize(origSize.width, origSize.height);
			wComponents.setLocation(origLocation.x, origLocation.y);
		}
		//Draw window holder
		if (isMDF2){
			int locX = wComponents.getLocation().x;
			int locY = wComponents.getLocation().y;
			int sizeWidth = wComponents.getSize().width;
			int sizeHeight = wComponents.getSize().height;
			if (right) {
				wComponents.drawLineDashed(3, locX+sizeWidth+3 , locY, locX + sizeWidth+3, locY+sizeHeight);
			}
			if (left) {
				wComponents.drawLineDashed(3, locX-3 , locY, locX-3, locY+sizeHeight);
			}

		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		wComponents.drawGlass();
		wComponents.drawDirectionArraySimple();
		
		//Set opposite direction to draw components
		if (left) {wComponents.setRight();}
		else {wComponents.setLeft();}
		
		if (handle) wComponents.drawHandle(-50);
		//Emelotolo
		wComponents.drawBar("_2");
		if (cilinder) wComponents.drawCilinder();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		
		//Set back original direction
		setToolBarDirection();
		
		wComponents.drawFrame();
		
	
		
		if (left) {
			wComponents.setHegihtSizeDistance(wComponents.getSize().width+ 5 + 20);
			if (isMFREI){
				wComponents.setHegihtSizeDistance((wComponents.getSize().width + 5) *3 + 20);
			}
			wComponents.drawSize("right");
		} 
		if (right){
			wComponents.setHegihtSizeDistance(5 + 20);
			if ( isMKIPP || isMDF){
				wComponents.setHegihtSizeDistance((wComponents.getSize().width + 5) *2 + 20);	
			} 
			if (isMDK2||isMDF2||isMDS2){
				wComponents.setHegihtSizeDistance(wComponents.getSize().width+ 5 + 20);	
			}
			wComponents.drawSize("right");
		}
				
		wComponents.setPen(1);
	}
	
	public void drawClass_8(){
		wComponents.setSize(130, 350);
		if (isMDF || isMKIPP || isMDF2 || isMFREI){
			createHKSFrame(!right);
		
		} else {
			createDoubleFrame(right, false, false, 8, 0);
		}
		
		wComponents.setClosable(closableBar);
		wComponents.drawToolBar(5, 5);
		wComponents.drawGlass();
		wComponents.drawDirectionArray();
		
		//Set opposite direction to draw components
		if (left) {wComponents.setRight();}
		else {wComponents.setLeft();}
		
		if (handle) wComponents.drawHandle();
		wComponents.drawBar();
		if (cilinder) wComponents.drawCilinder();
		if (liftingMD) wComponents.drawLiftingMD();
		if (!catcNone)	wComponents.drawCatch(catcMagnet);
		if (fan) wComponents.drawFan();
		
		//Set back original direction
		setToolBarDirection();
		
		wComponents.drawFrame();
		
	
		
		if (left) {
			wComponents.setHegihtSizeDistance(wComponents.getSize().width+ 5 + 20);
			wComponents.drawSize("right");
		} 
		if (right){
			if (isMDF || isMKIPP || isMDF2 || isMFREI){
				wComponents.setHegihtSizeDistance((wComponents.getSize().width + 5) *2 + 20);	
			} else {
				wComponents.setHegihtSizeDistance(5 + 20);
			}
			
			wComponents.drawSize("right");
		}
				
		wComponents.setPen(1);
	}
	
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}
}
