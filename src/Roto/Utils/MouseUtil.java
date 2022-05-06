package Roto.Utils;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;

public class MouseUtil {
	private static MouseUtil instance = null;
	public static MouseUtil getInstance() {
		if (instance == null) {
			instance = new MouseUtil();
		}
		return instance;
	}
	
	public Point getCursorPosition(){
		PointerInfo pointerInfo = MouseInfo.getPointerInfo();
		Point point = pointerInfo.getLocation();
		return point;
	}
	
	public int getCursorX(){
		return (int)getCursorPosition().getX();
	}
	
	public int getCursorY(){
		return (int)getCursorPosition().getY();
	}
}
