package Roto.DataBase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SerializableDataBaseCollection implements Serializable {

	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> selection;
	private int garniture,width,height,width2,height2;
	private String typeName,colorName,direction,databaseName;
	private List<String> descriptions;
	
	public List<String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	public int getGarniture() {
		return garniture;
	}
	public void setGarniture(int garniture) {
		this.garniture = garniture;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth2() {
		return width2;
	}
	public void setWidth2(int width2) {
		this.width2 = width2;
	}
	public int getHeight2() {
		return height2;
	}
	public void setHeight2(int height2) {
		this.height2 = height2;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public HashMap<String, Integer> getSelection() {
		return selection;
	}
	public void setSelection(HashMap<String, Integer> selection) {
		this.selection = selection;
	}
	
	
	
}
