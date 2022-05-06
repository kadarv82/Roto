package Roto.DataBase;

import java.text.NumberFormat;

import java.util.HashMap;
import java.util.Vector;

import Roto.Basic.SwingConstans;

import Roto.Utils.PropertyUtil;

public class TextPriceList {
    Vector<TextPriceListRow> vPriceList;
    HashMap<String, Double> mapPrice;
    HashMap<String, String> mapText;
    HashMap<String, String> mapMartikel;
    HashMap<String, String> mapGroup;
    Double Multiplier;
    NumberFormat formatter;
    SwingConstans swc;
    PropertyUtil propertyUtil;

    public TextPriceList() {
        vPriceList = new Vector<TextPriceListRow>();
        mapPrice = new HashMap<String, Double>();
        mapText = new HashMap<String, String>();
        mapGroup = new HashMap<String, String>();
        mapMartikel = new HashMap<String, String>();
        Multiplier = 1.0;
        swc = SwingConstans.getInstance();
        formatter = swc.formatter;
        propertyUtil = PropertyUtil.getInstance();
    }

    public void addRow(TextPriceListRow row) {
        vPriceList.add(row);
        mapPrice.put(row.getSAP(), row.getPrice());
        mapText.put(row.getSAP(), row.getText());
        mapGroup.put(row.getSAP(), row.getGroup());
        mapMartikel.put(row.getSAP(), row.getMartikel());
    }

    public void removeRows() {
        vPriceList.removeAllElements();
        vPriceList = new Vector<TextPriceListRow>();
        mapPrice = new HashMap<String, Double>();
        mapText = new HashMap<String, String>();
        mapGroup = new HashMap<String, String>();
        mapMartikel = new HashMap<String, String>();
    }

    public Vector<TextPriceListRow> getRows() {
        return vPriceList;
    }

    public int getColumnCount() {
        return vPriceList.size();
    }

    public TextPriceListRow getRowAt(int index) {
        return vPriceList.get(index);
    }

    public Double getPrice(String SAP) {
        if (mapPrice.get(SAP) == null) {
            return 0.0;
        } else {
            return formatDouble(mapPrice.get(SAP));
        }
    }

    public Double getPriceMultiplied(String SAP) {
        if (mapPrice.get(SAP) == null) {
            return 0.0;
        } else {
            return formatDouble(mapPrice.get(SAP) * Multiplier);
        }
    }

    public boolean isSAPExists(String SAP) {
        return !(mapPrice.get(SAP) == null);
    }

    public String getText(String SAP) {
        if (mapText.get(SAP) == null) {
            return propertyUtil.getLangText("PriceList.MissingItem");
        } else {
            return mapText.get(SAP);
        }
    }

    public String getGroup(String SAP) {
        if (mapGroup.get(SAP) == null) {
            return "";
        } else {
            return mapGroup.get(SAP);
        }
    }

    public String getMartikel(String SAP) {
        if (mapMartikel.get(SAP) == null) {
            return "";
        } else {
            return mapMartikel.get(SAP);
        }
    }

    public Double getMultiplier() {
        return Multiplier;
    }

    public void setMultiplier(Double multiplier) {
        Multiplier = multiplier;
    }

    private double formatDouble(Double value) {
        try {
            String Price = formatter.format(value);
            Price = Price.replace(",", ".");
            return Double.parseDouble(Price);
        } catch (Exception e) {
            System.out.println("Can't format value: " + value);
            e.printStackTrace();
            return value;
        }
    }

}
