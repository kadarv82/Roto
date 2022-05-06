package Roto.DataBase;

import java.util.Properties;

import Roto.Utils.PropertyUtil;

public class TextPriceListRow {
	StringBuffer Line;
	String SAP;
	String Text;
	String Martikel;
	Double Price;
	String Group; 
	PropertyUtil propertyUtil;
	Properties pricelistProperty;
	
	public TextPriceListRow(){
		init();
	}
	
	public TextPriceListRow(String line){
		this.Line = new StringBuffer(line);
		this.Line.append("  ");
		init();
		parseLine();
	}
	
	public String[] getHeaderTitles(String line){
		this.Line = new StringBuffer(line);
		String[] titles = new String[5];
		titles[0] = parse("SAP");
		titles[1] = parse("Text");
		titles[2] = parse("Martikel");
		titles[3] = parse("Price");
		titles[4] = Line.substring(getIndex("Group" + ".From")).trim();
		return titles;
	}
	
	private void init(){
		propertyUtil = PropertyUtil.getInstance();
		pricelistProperty = propertyUtil.getPricelistProperty();
	}
	
	// Get the index value by key from database property
	private int getIndex(String name){
		return propertyUtil.getIntProperty(pricelistProperty, "Pricelist.Row." + name);
	}
	
	// Get Line parsed value by indexes
	private String parse(String name) {
		return Line.substring(getIndex(name + ".From"),getIndex(name + ".To")).trim();
	}
	
	private void parseLine() {
		//checkParse();
		try {
			setSAP(parse("SAP"));
			setText(parse("Text"));
			setMartikel(parse("Martikel"));
			setPrice(Double.parseDouble(parse("Price").replace(",", ".")));
			setGroup(parse("Group"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSAP() {
		return SAP;
	}

	public void setSAP(String sap) {
		SAP = sap;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}

	public String getMartikel() {
		return Martikel;
	}

	public void setMartikel(String martikel) {
		Martikel = martikel;
	}

	public Double getPrice() {
		return Price;
	}

	public void setPrice(Double price) {
		Price = price;
	}

	public String getGroup() {
		return Group;
	}

	public void setGroup(String group) {
		Group = group;
	}

	
}
