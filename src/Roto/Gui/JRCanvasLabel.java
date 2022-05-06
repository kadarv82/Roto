package Roto.Gui;

import java.awt.Graphics;

import javax.swing.JLabel;

public class JRCanvasLabel extends JLabel {

	private static final long serialVersionUID = -4377615268940442063L;
	
	String imageText = null;
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		
		if (imageText != null) {
			g.drawString(imageText, 10, 10);
		}
	}

	public String getImageText() {
		return imageText;
	}

	public void setImageText(String imageText) {
		this.imageText = imageText;
	}

}
