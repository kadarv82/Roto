package Roto.Windows;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;




import Roto.Basic.SwingConstans;
import Roto.Gui.DisplayCanvas;
import Roto.Gui.Frames;
import Roto.Gui.JRNumberTextField;

public class WindowComponents {
	private static WindowComponents instance = null;
	private Graphics2D g2;
	private int x,y,width,height, hegihtSizeDistance, arcStartHeight;
	private boolean isLeft,isRight,isOver, isClosable, isTopPosition;
    private JRNumberTextField jntWidth, jntWidth2, jntHeight, jntHeight2;
	private SwingConstans swc;
	private Rectangle windowHit;

	final static float dash1[] = {3.0f};
    final static BasicStroke dashed = new BasicStroke(0.3f, 
             BasicStroke.CAP_BUTT, 
             BasicStroke.JOIN_MITER, 
             3.0f, dash1, 0.0f);
    final static BasicStroke dashedBold = new BasicStroke(0.6f, 
            BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_MITER, 
            3.0f, dash1, 0.0f);

	public static WindowComponents getInstance() {
		if (instance == null) {
			instance = new WindowComponents();
		}
		return instance;
	}
	
	public WindowComponents() {
		this.swc = SwingConstans.getInstance();
		this.x = 0;
		this.y = 0;
		this.width = 100;
		this.height = 200;
		this.hegihtSizeDistance = 60;
		this.isLeft = true;
		this.arcStartHeight = 0;
		this.windowHit = new Rectangle(999999,999999,0,0);
	}
	
	public void setLabels (JRNumberTextField jntWidth, JRNumberTextField jntWidth2, JRNumberTextField jntHeight, JRNumberTextField jntHeight2){
		this.jntWidth = jntWidth;
		this.jntWidth2 = jntWidth2;
		this.jntHeight = jntHeight;
		this.jntHeight2 = jntHeight2;
	}
	
	private BufferedImage createImage(String imageName){
		return createImage(imageName,0,0);
	}
	
	private BufferedImage createImage(String imageName,int width, int height){
		File test = new File(swc.iconPath + imageName);
		
		if (!test.exists()) {
			System.out.println ("Image not found: " + swc.iconPath + imageName);
			return null;
		}
		
		ImageIcon ic =  new ImageIcon(swc.iconPath + imageName);
		
		if (width > 0 && height > 0){
			ic = resizeImageIcon(ic, width, height);
		}
		Image image2 = ic.getImage();
		BufferedImage bi = new BufferedImage(ic.getIconWidth(), ic.getIconHeight(),	BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();
		big.drawImage(image2, 0, 0, null);
		return bi;
	}
	
	private ImageIcon resizeImageIcon(ImageIcon im, int width, int height) {
		Image img = im.getImage();
		img = img.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING);
		return (new ImageIcon(img));
	}
	
	public void setPen(float lineWidth){
		g2.setStroke(new BasicStroke(lineWidth));
	}
	
	public void setNormalPen(){
		g2.setStroke(new BasicStroke(1));
	}
	
	public void drawCilinder(){
		drawCilinder(0);
	}
	
	public void drawCilinder(int horizontalModify){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		//Right side when direction is left
		if ((isLeft || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasCilinder.gif"), x+width+10, y+height/2+70+horizontalModify, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width-50, y+202+horizontalModify, x+width+15, y+height/2+85+horizontalModify);
		}//Left side when direction is right
		if ((isRight || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasCilinder.gif"), x+80, y+height/2+70+horizontalModify, null);
			g2.setStroke(dashed);
			g2.drawLine(x+58, y+206+horizontalModify, x+83, y+height/2+82+horizontalModify);
		}
		//Top position
		if (isTopPosition){
			g2.drawImage(createImage("CanvasCilinder.gif"), x+width/2-70, y+50, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width/2-25, y - 7, x+width/2-54, y+58);
		}
		g2.setStroke(new BasicStroke(1));
	}
	
	public void	drawFan(){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		//Right side when direction is left
		if ((isLeft || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasFan.gif"), x+width+15, y-25, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width-25, y + 5, x+width+15, y-20);
		}
		//Left side when direction is right
		if ((isRight || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasFan.gif"), x-35, y-25, null);
			g2.setStroke(dashed);
			g2.drawLine(x-10, y-20, x+25, y+5);
		}
	}
	
	public void drawCatch(boolean isMagnet){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		BufferedImage image = createImage("CanvasCatchRight.gif");
		if (isMagnet){
			image = createImage("CanvasCatchMagnetRight.gif");
		}
		//Right side when direction is left
		if ((isLeft || isOver) && !isTopPosition){
			g2.drawImage(image, x+width+15, y+height/2-55, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width+15, y+height/2-40, x+width-5, y+height/2-15);
		}
		//Left side when direction is right
		if ((isRight || isOver) && !isTopPosition){
			image = createImage("CanvasCatchLeft.gif");
			if (isMagnet){
				image = createImage("CanvasCatchMagnetLeft.gif");
			}
			g2.drawImage(image, x-25, y+height/2-55, null);
			g2.setStroke(dashed);
			g2.drawLine(x-25+image.getWidth(), y+height/2-40, x+5, y+height/2-15);
			
		}
	}
	
	public void drawLiftingMD(){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		//Right side when direction is left
		if ((isLeft || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasLiftingMD.gif"), x+width+15, y+height/2-15, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width-2,y+height/2+25, x+width+15, y+height/2-5);
		}
		//Left side when direction is right
		if ((isRight || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasLiftingMD.gif"), x-25, y+height/2-15, null);
			g2.setStroke(dashed);
			g2.drawLine(x+2, y+height/2+25, x-10, y+height/2-10);
		}
	}
	
	public void drawBar(){
		drawBar("");
	}
	
	public void drawBar(String specification){
		String barSign = swc.getSelectedSelectionBarSign().toLowerCase();
		String barType = "";
	
		if (barSign.equalsIgnoreCase(swc.selectionBarArray[1])) {
			barType = "KSR";
		} else if (barSign.equalsIgnoreCase(swc.selectionBarArray[2])) {
			barType = "M";
		}
		
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		
		BufferedImage image = createImage("CanvasBarRight" + barType + specification +".gif"); 
		if (isClosable){
			image = createImage("CanvasClosableBarRight" + barType + specification +".gif");
		}
		//Right side when direction is left
		if ((isLeft || isOver) && !isTopPosition){
			g2.drawImage(image, x+width-62, y + 25, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width-6, y+height/2+3, x+width-50,  y + 147);
		} 
		//Left side when direction is right
		if ((isRight || isOver) && !isTopPosition){
			image = createImage("CanvasBarLeft" + barType + specification +".gif"); 
			if (isClosable){
				image = createImage("CanvasClosableBarLeft" + barType + specification +".gif");
			}
			
			
			g2.drawImage(image, x+45, y + 25, null);
			g2.setStroke(dashed);
			g2.drawLine(x+6, y+height/2+3, x+45,  y + 146);
		}
		//Top position
		if (isTopPosition){
			image = createImage("CanvasBarTop.gif");
			if (isClosable){
				image = createImage("CanvasClosableBarTop.gif");
			}
			g2.drawImage(image, x+width/2-image.getWidth()/2, y - 25, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width/2, y+8, x+width/2-4, y - 16);
		}
		
		g2.setStroke(new BasicStroke(1));
	}

	
	public void drawHandleDoor(){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		if (isLeft){
			g2.drawOval(x+width-10,y+height/2,6,6);
			
			g2.drawArc(x+width-10, y+height/2+16, 7, 7, -70,300);
			g2.drawArc(x+width-10, y+height/2+22, 7, 7, 70,-300);
			g2.drawImage(createImage("CanvasHandleDoorLeft.gif"), x+width-15, y+height/2+20, null);
			g2.setStroke(dashed);
			g2.drawLine(x+width-7,y+height/2+22, x+width+10, y+height/2+30);

		} else if (isRight){
			g2.drawOval(x+5,y+height/2,6,6);
			
			g2.drawArc(x+5, y+height/2+16, 7, 7, -70,300);
			g2.drawArc(x+5, y+height/2+22, 7, 7, 70,-300);
			g2.drawImage(createImage("CanvasHandleDoorRight.gif"), x-18, y+height/2+20, null);
			g2.setStroke(dashed);
			g2.drawLine(x+8,y+height/2+22, x-12, y+height/2+30);

		}
		
		g2.setStroke(new BasicStroke(1));
	}
	
	public void drawHandle(){
		drawHandle(0);
	}
	
	public void drawHandle(int horizontalModifyCilinderHole){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(1));
		//Right side when direction is left
		if ((isLeft || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasHandle.gif"), x+width+10, y+height/2+15, null);
			g2.drawOval(x+width-10,y+height/2,6,6);
			//Draw garniture if closable bar
			if (isClosable){
				g2.drawArc(x+width-10, y+height/2+16+horizontalModifyCilinderHole, 7, 7, -70,300);
				g2.drawArc(x+width-10, y+height/2+22+horizontalModifyCilinderHole, 7, 7, 70,-300);
				g2.drawImage(createImage("CanvasHandleGarniture.gif"), x+width+6, y+height/2+40+horizontalModifyCilinderHole, null);
				g2.setStroke(dashed);
				g2.drawLine(x+width-7,y+height/2+22+horizontalModifyCilinderHole, x+width+6, y+height/2+45+horizontalModifyCilinderHole);
			}
			g2.setStroke(dashed);
			g2.drawLine(x+width-7,y+height/2+3, x+width+10, y+height/2+17);
			g2.setStroke(new BasicStroke(1));
		}
		//Left side when direction is right
		if ((isRight || isOver) && !isTopPosition){
			g2.drawImage(createImage("CanvasHandle.gif"), x+20, y+height/2+15, null);
			g2.drawOval(x+5,y+height/2,6,6);
			//Draw garniture if closable bar
			if (isClosable){
				g2.drawArc(x+5, y+height/2+16+horizontalModifyCilinderHole, 7, 7, -70,300);
				g2.drawArc(x+5, y+height/2+22+horizontalModifyCilinderHole, 7, 7, 70,-300);
				g2.drawImage(createImage("CanvasHandleGarniture.gif"), x-18, y+height/2+40+horizontalModifyCilinderHole, null);
				g2.setStroke(dashed);
				g2.drawLine(x+8,y+height/2+22+horizontalModifyCilinderHole, x-12, y+height/2+45+horizontalModifyCilinderHole);
			}
			g2.setStroke(dashed);
			g2.drawLine(x+7,y+height/2+3, x+20, y+height/2+17);
		}
		//Top position
		if (isTopPosition){
			g2.drawImage(createImage("CanvasHandleTop.gif"), x+width/2+15, y+30, null);
			g2.drawOval(x+width/2-3,y+5,6,6);
			//Draw garniture if closable bar
			if (isClosable){
				g2.drawArc(x+width/2-22,y+5, 7, 7, 30,290);
				g2.drawArc(x+width/2-15,y+5, 7, 7, 210,290);
				g2.drawImage(createImage("CanvasHandleGarnitureTop.gif"), x+width/2-40, y+35, null);
				g2.setStroke(dashed);
				g2.drawLine(x+width/2-19, y+8, x+width/2-30, y+35);
			}
			g2.setStroke(dashed);
			g2.drawLine(x+width/2, y+8, x+width/2+15, y+30);
		}
		g2.setStroke(new BasicStroke(1));
	}
	

	public void drawFrame(){
		drawFrame(0);
	}
	
	public void drawFrame(float lineWidth){
		//Draw frame
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
		g2.drawRect(x, y, width, height);
		g2.drawRect(x+15, y+15, width-30, height-30);
		g2.setStroke(new BasicStroke(1));
		if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
		g2.drawRect(x+19, y+19, width-38, height-38);
		//Draw lines
		g2.drawLine(x, y, x+19, y+19);
		g2.drawLine(x+width, y, x+width-19, y+19);
		g2.drawLine(x, y+height, x+19, y+height-19);
		g2.drawLine(x+width, y+height, x+width-19, y+height-19);
	}
	
	public void drawLineDashed(float lineWidth,int x1, int y1, int x2, int y2){
		g2.setPaint(Color.WHITE);
		g2.setStroke(new BasicStroke(lineWidth));
		g2.drawLine(x1, y1, x2, y2);
		g2.setPaint(Color.BLACK);
		BasicStroke dashed = new BasicStroke(lineWidth, 
		             BasicStroke.CAP_BUTT, 
		             BasicStroke.JOIN_MITER, 
		             3.0f, dash1, 0.0f);
		g2.setStroke(dashed);
		g2.drawLine(x1, y1, x2, y2);
	}
	
	public void drawFrameDashed(boolean isLeft){
		g2.setStroke(dashedBold);
		g2.setPaint(Color.BLACK);
		
		if (isLeft){
			g2.drawRect(x-55, y, 50, height);
			g2.drawRect(x-55, y+15, 35, height-30);
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(1));
			g2.drawLine(x-55, y, x-55, y+height);
			g2.setPaint(Color.BLACK);
			g2.setStroke(dashedBold);
			g2.drawLine(x-55, y-10, x-55, y+height+10);
		} else {
			g2.drawRect(x + width + 5, y, 50, height);
			g2.drawRect(x + width + 20, y+15, 35, height-30);
			g2.setPaint(Color.WHITE);
			g2.setStroke(new BasicStroke(1));
			g2.drawLine(x + width + 55, y, x + width + 55, y+height);
			g2.setPaint(Color.BLACK);
			g2.setStroke(dashedBold);
			g2.drawLine(x + width + 55, y-10, x + width + 55, y+height+10);
		}
	}
	
	public void drawFrameArc(float lineWidth, String direction){
		drawFrame(lineWidth);
		int arcWidth = width;
		int startAngle = 0;
		int goAngle = 180;
		int locationX = x;
		
		g2.setPaint(Color.WHITE);
		if (direction.toLowerCase().equalsIgnoreCase("center")){
			g2.fillRect(x-2, y-3, width+4, arcStartHeight);
		} else if (direction.toLowerCase().equalsIgnoreCase("left")){
			arcWidth = arcWidth * 2 - 38;
			startAngle = 90;
			goAngle = 90;
			g2.fillRect(x-2, y-3, width-19+2, arcStartHeight);
		} else if (direction.toLowerCase().equalsIgnoreCase("right")){
			arcWidth = arcWidth * 2 - 38;
			startAngle = 0;
			goAngle = 90;
			locationX = locationX - width + 38;
			g2.fillRect(x+20, y-3, width+2, arcStartHeight);
		} 
		
		drawGlass();
		
		g2.setPaint(Color.BLACK);
		
		g2.setStroke(new BasicStroke(2));
		if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
		g2.drawArc(locationX, y, arcWidth, (arcStartHeight)*2, startAngle, goAngle);
		g2.drawArc(locationX+15, y+15, arcWidth-30, (arcStartHeight)*2-15, startAngle, goAngle);
		g2.setStroke(new BasicStroke(1));
		if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
		g2.drawArc(locationX+19, y+19, arcWidth-38, (arcStartHeight)*2-19, startAngle, goAngle);
		
	}
	
	public void drawFrameSegment(float lineWidth, String direction){
		//Draw a normal frame
		drawFrame(lineWidth);
		
		QuadCurve2D q = new QuadCurve2D.Float();
		int arcWidth = width;
		int locationX = x;

		//Hide a piece of the frame
		g2.setPaint(Color.WHITE);
		if (direction.toLowerCase().equalsIgnoreCase("center")){
			g2.fillRect(x-2, y-3, width+4, arcStartHeight+3);
			g2.fillRect(x+4, y-3, width-6, arcStartHeight +18);
			g2.fillRect(x+17, y-3, width-34, arcStartHeight +22);
		} else if (direction.toLowerCase().equalsIgnoreCase("left")){
			//arcWidth = arcWidth * 2 - 38;
			g2.fillRect(x-2, y-3, width-20+2, arcStartHeight+3);
			g2.fillRect(x+4, y-6, width-25, arcStartHeight +18);
			g2.fillRect(x+18, y-5, width-40, arcStartHeight + 20);
		} else if (direction.toLowerCase().equalsIgnoreCase("right")){
			g2.fillRect(x+20, y-3, width+2, arcStartHeight+3);
			g2.fillRect(x+24, y-6, width-25, arcStartHeight +18);
			g2.fillRect(x+26, y-6, width-42, arcStartHeight +21);
		} 
		
		drawGlass();
		
		g2.setPaint(Color.BLACK);
		if (direction.toLowerCase().equalsIgnoreCase("center")){
			g2.setStroke(new BasicStroke(2));
			if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
			q.setCurve(locationX, y+arcStartHeight, x + arcWidth/2, y - arcStartHeight, x + arcWidth, y+arcStartHeight);
			g2.draw(q);
			q.setCurve(locationX+15, y+arcStartHeight+15, x + arcWidth/2, y - arcStartHeight +22, x + arcWidth-15, y+arcStartHeight+15);
			g2.draw(q);
			g2.setStroke(new BasicStroke(1));
			if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
			q.setCurve(locationX+19, y+arcStartHeight+19, x + arcWidth/2, y - arcStartHeight +28, x + arcWidth-19, y+arcStartHeight+19);
			g2.draw(q);
			g2.drawLine(locationX, y+arcStartHeight, locationX+15, y+arcStartHeight+15);
			g2.drawLine(x + arcWidth, y+arcStartHeight,  x + arcWidth-15, y+arcStartHeight+15);
		} else if (direction.toLowerCase().equalsIgnoreCase("left")){
			g2.setStroke(new BasicStroke(2));
			if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
			q.setCurve(locationX, y+arcStartHeight, x + arcWidth/2-15, y, x + arcWidth-15, y);
			g2.draw(q);
			q.setCurve(locationX+15, y+arcStartHeight+12, x + arcWidth/2-10, y+18, x + arcWidth-19, y+15);
			g2.draw(q);
			g2.setStroke(new BasicStroke(1));
			if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
			q.setCurve(locationX+19, y+arcStartHeight+15, x + arcWidth/2-10, y+24, x + arcWidth-19, y+19);
			g2.drawLine(locationX, y+arcStartHeight,locationX+15, y+arcStartHeight+12);
			g2.draw(q);
		} else if (direction.toLowerCase().equalsIgnoreCase("right")){
			g2.setStroke(new BasicStroke(2));
			if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
			
			q.setCurve(locationX+15, y, x + arcWidth/2+15, y, x + arcWidth, y+arcStartHeight);
			g2.draw(q);
			q.setCurve(locationX+15, y+15, x + arcWidth/2+15, y+22, x + arcWidth-15, y+arcStartHeight+12);
			g2.draw(q);
			g2.setStroke(new BasicStroke(1));
			if (lineWidth !=0) g2.setStroke(new BasicStroke(lineWidth));
			q.setCurve(locationX+19, y+19, x + arcWidth/2+15, y+28, x + arcWidth-19, y+arcStartHeight+14);
			g2.drawLine(x + arcWidth, y+arcStartHeight, x + arcWidth-15, y+arcStartHeight+12);
			g2.draw(q);
		}

	}
	
	public void hideSizeComponents(int Component){
		if (Component == 2){
			jntHeight2.setSize(0, 0);
			jntWidth2.setSize(0, 0);
		}
	}
	
	public void drawSizeHeight(int withComponent, boolean isLeft){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(0.3f));
		JRNumberTextField numberField = jntHeight;
		if (withComponent == 2){
			numberField = jntHeight2;
		}
		numberField.setSize(40, 15);
		if (!isLeft){
			g2.drawLine(x+width+hegihtSizeDistance, y, x+width+hegihtSizeDistance, y+height);
			//g2.drawRect(x+width, y, hegihtSizeDistance, height);
			g2.drawLine(x+width+hegihtSizeDistance, y, x+width+hegihtSizeDistance+2, y+5);
			g2.drawLine(x+width+hegihtSizeDistance, y, x+width+hegihtSizeDistance-2, y+5);
			g2.drawLine(x+width+hegihtSizeDistance, y+height, x+width+hegihtSizeDistance+2, y+height-5);
			g2.drawLine(x+width+hegihtSizeDistance, y+height, x+width+hegihtSizeDistance-2, y+height-5);
			numberField.setLocation(x+width+hegihtSizeDistance+5, y+height/2-3);
		} else {
			g2.drawLine(x-hegihtSizeDistance, y, x-hegihtSizeDistance, y+height);
			//g2.drawRect(x-hegihtSizeDistance, y, hegihtSizeDistance, height);
			g2.drawLine(x-hegihtSizeDistance, y, x-hegihtSizeDistance-2, y+5);
			g2.drawLine(x-hegihtSizeDistance, y, x-hegihtSizeDistance+2, y+5);
			g2.drawLine(x-hegihtSizeDistance, y+height, x-hegihtSizeDistance-2, y+height-5);
			g2.drawLine(x-hegihtSizeDistance, y+height, x-hegihtSizeDistance+2, y+height-5);
			numberField.setLocation(x-hegihtSizeDistance-2-jntHeight.getWidth(), y+height/2-3);
		}
	}
	
	public void drawSize(String direction){
		drawSize(direction,false,false);
	}
	
	public void drawSize(String direction, boolean secondWidth, boolean secondHeight){
		drawSize(direction, secondWidth, secondHeight, false);
	}
	
	public void drawSize(String direction, boolean secondWidth, boolean secondHeight, boolean isArc){
		this.jntWidth.setSize(40, 17);
		this.jntHeight.setSize(40, 17);
		if (secondWidth) {
			this.jntWidth2.setSize(40, 17);	
		} else {
			this.jntWidth2.setSize(0, 0);
		}
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(0.3f));
		//Draw width with rectangle if not arc
		if (!isArc) {g2.drawRect(x, y+height, width, 30);}
		else{
			g2.drawLine(x, y+height+30, x+width, y+height+30);	
		}
		g2.drawLine(x, y+height+30, x+5, y+height+28);		
		g2.drawLine(x, y+height+30, x+5, y+height+32);
		g2.drawLine(x+width, y+height+30, x+width-5, y+height+28);
		g2.drawLine(x+width, y+height+30, x+width-5, y+height+32);
		if (secondWidth){
			jntWidth2.setLocation(x + width/2 - jntWidth.getWidth()/2, y+height+11);
		}
		else {
			jntWidth.setLocation(x + width/2 - jntWidth.getWidth()/2, y+height+11);
		}
		//Draw height if not second size (only second width)
		if (!secondWidth){
			if (direction.toLowerCase().trim().equalsIgnoreCase("right")){
				//Draw height with rectangle if not arc
				if (!isArc) {g2.drawRect(x+width, y, hegihtSizeDistance, height);}
				else {
					g2.drawLine(x+width+hegihtSizeDistance, y, x+width+hegihtSizeDistance, y+height);	
					//g2.drawRect(x, y, width+hegihtSizeDistance, height);
				}
				g2.drawLine(x+width+hegihtSizeDistance, y, x+width+hegihtSizeDistance+2, y+5);
				g2.drawLine(x+width+hegihtSizeDistance, y, x+width+hegihtSizeDistance-2, y+5);
				g2.drawLine(x+width+hegihtSizeDistance, y+height, x+width+hegihtSizeDistance+2, y+height-5);
				g2.drawLine(x+width+hegihtSizeDistance, y+height, x+width+hegihtSizeDistance-2, y+height-5);
				jntHeight.setLocation(x+width+hegihtSizeDistance+5, y+height/2-3);
			} else {
				//Draw height with rectangle if not arc
				if (!isArc) {g2.drawRect(x-hegihtSizeDistance, y, hegihtSizeDistance, height);}
				else {
					g2.drawLine(x-hegihtSizeDistance, y, x-hegihtSizeDistance, y+height);
				}
				g2.drawLine(x-hegihtSizeDistance, y, x-hegihtSizeDistance-2, y+5);
				g2.drawLine(x-hegihtSizeDistance, y, x-hegihtSizeDistance+2, y+5);
				g2.drawLine(x-hegihtSizeDistance, y+height, x-hegihtSizeDistance-2, y+height-5);
				g2.drawLine(x-hegihtSizeDistance, y+height, x-hegihtSizeDistance+2, y+height-5);
				jntHeight.setLocation(x-hegihtSizeDistance-2-jntHeight.getWidth(), y+height/2-3);
			}
		}
		g2.setStroke(new BasicStroke(1));
	}
	
	
	public void drawStrap(){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		if (isLeft) {
			g2.drawRect(x-4, y+10, 4, 20);
			g2.drawRect(x-4, y+height-30, 4, 20);
		} else if (isRight){
			g2.drawRect(x+width, y+10, 4, 20);
			g2.drawRect(x+width, y+height-30, 4, 20);
		} else if(isOver){
			g2.drawRect(x+10, y+height, 20, 4);
			g2.drawRect(x+width-30, y+height, 20, 4);
		}
	}
	
	public void drawStrapDoor(){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		if (isLeft) {
			g2.setPaint(Color.WHITE);
			g2.fillRect(x-4, y+30, 14,10);
			g2.setPaint(Color.BLACK);
			g2.drawRect(x-4, y+30, 14,10);
			g2.drawRect(x-4, y+40, 4,10);
			
			g2.setPaint(Color.WHITE);
			g2.fillRect(x-4, y+height/2-5, 14,10);
			g2.setPaint(Color.BLACK);
			g2.drawRect(x-4, y+height/2-5, 14,10);
			g2.drawRect(x-4, y+height/2+5, 4,10);
						
			g2.setPaint(Color.WHITE);
			g2.fillRect(x-4, y+height-40, 14,10);
			g2.setPaint(Color.BLACK);
			g2.drawRect(x-4, y+height-40, 14,10);
			g2.drawRect(x-4, y+height-30, 4, 10);
		} else if (isRight){
			g2.setPaint(Color.WHITE);
			g2.fillRect(x+width-10, y+30, 14,10);
			g2.setPaint(Color.BLACK);
			g2.drawRect(x+width-10, y+30, 14,10);
			g2.drawRect(x+width, y+40, 4,10);
			
			g2.setPaint(Color.WHITE);
			g2.fillRect(x+width-10, y+height/2-5, 14,10);
			g2.setPaint(Color.BLACK);
			g2.drawRect(x+width-10, y+height/2-5, 14,10);
			g2.drawRect(x+width, y+height/2+5, 4,10);
						
			g2.setPaint(Color.WHITE);
			g2.fillRect(x+width-10, y+height-40, 14,10);
			g2.setPaint(Color.BLACK);
			g2.drawRect(x+width-10, y+height-40, 14,10);
			g2.drawRect(x+width, y+height-30, 4, 10);
		} 
	}
		
	public void drawStrapArc(){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(2));
		if (isLeft) {
			g2.drawRect(x-4, y+arcStartHeight, 4, 20);
			g2.drawRect(x-4, y+height-30, 4, 20);
		} else if (isRight){
			g2.drawRect(x+width, y+arcStartHeight, 4, 20);
			g2.drawRect(x+width, y+height-30, 4, 20);
		} else if(isOver){
			g2.drawRect(x+10, y+height, 20, 4);
			g2.drawRect(x+width-30, y+height, 20, 4);
		}
	}
	
	public void drawGlass(){
		//Draw glass
		GradientPaint whitetoblue = new GradientPaint(0,0,Color.WHITE,0, height, swc.colorGlass);
		g2.setPaint(whitetoblue);
		g2.fillRect(x+20, y+20, width-40, height-40);
	}
	
	public void drawDirection(){
		//Draw direction
		g2.setStroke(new BasicStroke(0.2f));
		g2.setPaint(Color.BLACK);
		if (isLeft){
			g2.drawLine(x+19, y+19, x+width-19, y+height/2);
			g2.drawLine(x+width-19, y+height/2, x+19, y+height-19);
		} else if (isRight){
			g2.drawLine(x+width-19, y+19, x+19, y+height/2);
			g2.drawLine(x+19, y+height/2, x+width-19, y+height-19);
		} else if (isOver){
			g2.drawLine(x+19, y+height-19, x+width/2, y+19);
			g2.drawLine(x+width/2, y+19, x+width-19, y+height-19);
		}
	}
	
	public void drawDirectionArc(){
		//Draw direction (arc)
		GradientPaint whitetoblack = new GradientPaint(0,50,Color.WHITE,0, width*2, Color.BLACK);
		g2.setStroke(new BasicStroke(0.3f));
		g2.setPaint(Color.BLACK);
		if (isLeft){
			g2.setPaint(whitetoblack);	
			g2.drawLine(x+19, y+19, x+width-19, y+height/2);
			g2.setPaint(Color.BLACK);
			g2.drawLine(x+width-19, y+height/2, x+19, y+height-19);
		} else if (isRight){
			g2.setPaint(whitetoblack);
			g2.drawLine(x+width-19, y+19, x+19, y+height/2);
			g2.setPaint(Color.BLACK);
			g2.drawLine(x+19, y+height/2, x+width-19, y+height-19);
		} else if (isOver){
			g2.setPaint(whitetoblack);
			g2.drawLine(x+19, y+height-19, x+width/2, y+19);
			g2.drawLine(x+width/2, y+19, x+width-19, y+height-19);
			g2.setPaint(Color.BLACK);
		}
	}
	
	public void drawDirectionArray(){
		//Draw direction array (HKS)
		g2.setStroke(new BasicStroke(4));
		g2.setPaint(Color.BLACK);
		if (isLeft){
			g2.drawLine(x+width/2+10, y+height/2-50, x+width/2+10, y+height/2+50);
			g2.drawLine(x+width/2+10, y+height/2+50, x+width/2+30, y+height/2+50);
			g2.drawLine(x+width/2+25, y+height/2+48, x+width/2+32, y+height/2+50);
			g2.drawLine(x+width/2+25, y+height/2+52, x+width/2+32, y+height/2+50);
		} else if (isRight){
			g2.drawLine(x+width/2-10, y+height/2-50, x+width/2-10, y+height/2+50);
			g2.drawLine(x+width/2-10, y+height/2+50, x+width/2-30, y+height/2+50);
			g2.drawLine(x+width/2-25, y+height/2+48, x+width/2-32, y+height/2+50);
			g2.drawLine(x+width/2-25, y+height/2+52, x+width/2-32, y+height/2+50);
		}
	}
	
	public void drawDirectionArraySimple(){
		//Draw direction array (Simple)
		g2.setStroke(new BasicStroke(4));
		g2.setPaint(Color.BLACK);
		if (isLeft){
			g2.drawLine(x+width/2+10, y+height/2+50, x+width/2+30, y+height/2+50);
			g2.drawLine(x+width/2+25, y+height/2+48, x+width/2+32, y+height/2+50);
			g2.drawLine(x+width/2+25, y+height/2+52, x+width/2+32, y+height/2+50);
		} else if (isRight){
			g2.drawLine(x+width/2-10, y+height/2+50, x+width/2-30, y+height/2+50);
			g2.drawLine(x+width/2-25, y+height/2+48, x+width/2-32, y+height/2+50);
			g2.drawLine(x+width/2-25, y+height/2+52, x+width/2-32, y+height/2+50);
		}
	}
	
	public void drawToolBar(int x, int y){
		g2.setPaint(Color.BLACK);
		g2.setStroke(new BasicStroke(0.3f));
		GeneralPath shape = new GeneralPath();
		int [] xPoints = new int []{0,115,115,120,1024,1024,-3,-3,0};
		int [] yPoints = new int []{800,800,30,26,26,-3,-3,800,800};
		
	    shape.moveTo(xPoints[0],yPoints[0]);
	    for (int i=1; i< xPoints.length; i++){
	    	shape.lineTo(xPoints[i],yPoints[i]);
	    }
	    	    
	    g2.draw(shape);
	    g2.setColor(swc.colorCanvasMenu);
	    g2.fillPolygon(xPoints, yPoints, xPoints.length);
	    
	    g2.setPaint(Color.BLACK);
	    g2.setStroke(new BasicStroke(1));
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
		
		//Set window hit min x and min y
		if (x < windowHit.x) windowHit.x = x;
		if (y < windowHit.y) windowHit.y = y;
	}
	
	public Point getLocation(){
		Point point = new Point(x,y);
		return point;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		//Set arc start for arc windows if 0
		if (this.arcStartHeight == 0) {
			this.arcStartHeight = height/10*4;
		}
		//Set window hit max x and max y
		if (x + width > windowHit.width) windowHit.width = x + width;
		if (y + height > windowHit.height) windowHit.height = y + height;
	}
	
	public Dimension getSize(){
		return new Dimension(width,height);
	}

	public void setGraphic(Graphics2D g2){
		this.g2 = g2;
	}
	
	// Set directions
	public void setLeft(){
		this.isLeft = true;
		this.isRight = false;
		this.isOver = false;
		this.isTopPosition = false;
	}
	
	public void setRight(){
		this.isLeft = false;
		this.isRight = true;
		this.isOver = false;
		this.isTopPosition = false;
	}
	
	public void setOver(){
		setOver("");
	}
	
	public void setOver(String direction){
		this.isLeft = false;
		this.isRight = false;
		this.isOver = true;
		if (direction.toLowerCase().equalsIgnoreCase("top")){
			this.isTopPosition = true;
		} else {
			this.isTopPosition = false;
		}
	}
	
	public void setClosable(boolean isClosable){
		this.isClosable = isClosable;
	}
	
	public int getHegihtSizeDistance() {
		return hegihtSizeDistance;
	}

	public void setHegihtSizeDistance(int hegihtSizeDistance) {
		this.hegihtSizeDistance = hegihtSizeDistance;
	}
	
	public int getArcStartHeight() {
		return arcStartHeight;
	}

	public void setArcStartHeight(int arcStartHeight) {
		this.arcStartHeight = arcStartHeight;
	}
	
	public Rectangle getWindowHit() {
		return windowHit;
	}

	public void setWindowHit(Rectangle windowHit) {
		this.windowHit = windowHit;
	}
	
	public boolean isWindowHit(int x, int y){
		//System.out.println("hitx: " + windowHit.x +"-" + (windowHit.width));
		//System.out.println("hity: " + windowHit.y +"-" + (windowHit.height));
		int corrigateFrame = 10;
		if (x > windowHit.x - corrigateFrame && x < windowHit.width + corrigateFrame &&
			y > windowHit.y - corrigateFrame && y < windowHit.height + corrigateFrame){
			return true;
		} else {
			return false;
		}
	}
}
