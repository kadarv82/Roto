package Roto.DataBase;

import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import Roto.Basic.SwingConstans;

import Roto.Utils.PropertyUtil;

public class TextDBRow {
    int ItemCount;
    int SAP;
    String Direction;
    String Text;
    boolean MDK, MDF, MDS, MKIPP, MDK2, MDF2, MDS2, MFREI;
    String Lock;
    String Color;
    int MSSTK, MSISTK;
    int MFFBV, MFFBB, MFFHV, MFFHB, MFFBVORIG, MFFBBORIG;
    String MSTART, MSISTART;
    PropertyUtil propertyUtil;
    Properties databaseProperty;
    SwingConstans swc;
    String Line;

    public TextDBRow(String Line) {
        this.Line = Line;
        init();
        parseLine();
    }

    public TextDBRow() {
        init();
    }

    private void init() {
        propertyUtil = PropertyUtil.getInstance();
        databaseProperty = propertyUtil.getDatabaseProperty();
        swc = SwingConstans.getInstance();
    }

    // Get the index value by key from database property
    private int getIndex(String name) {
        return propertyUtil.getIntProperty(databaseProperty, "Database.Row." + name);
    }

    // Get Line parsed value by indexes
    private String parse(String name) {
        return Line.substring(getIndex(name + ".From"), getIndex(name + ".To")).trim();
    }

    //Create row item for database saving
    public String createRowItem(String itemName, String rowData) {
        int itemLength = getIndex(itemName + ".To") - getIndex(itemName + ".From");
        StringBuffer sbRowData = new StringBuffer(rowData);

        for (int i = 0; i < itemLength; i++) {
            sbRowData.append(" ");
        }

        rowData = sbRowData.toString().substring(0, itemLength);

        //System.out.println(itemName  + " = " + itemLength + "=>'" + rowData + "'");
        return rowData;
    }

    // Check parsed values
    private void checkParse() {
        Enumeration<Object> enumeration = databaseProperty.keys();
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement().toString();
            if (key.startsWith("Database.Row")) {
                StringTokenizer token = new StringTokenizer(key);
                token.nextToken(".");
                token.nextToken(".");
                String parseValue = token.nextToken(".");
                System.out.println(parseValue + "=" + parse(parseValue) + ";");
            }
        }
    }

    private void parseLine() {
        //checkParse();
        try {
            setColor(parse("Color"));
            setDirection(parse("Direction"));
            setItemCount(Integer.parseInt(parse("Count")));
            setLock(parse("Lock"));
            setMDF(toBoolean(parse("MDF")));
            setMDK(toBoolean(parse("MDK")));
            setMDS(toBoolean(parse("MDS")));
            setMKIPP(toBoolean(parse("MKIPP")));
            setMDF2(toBoolean(parse("MDF2")));
            setMDK2(toBoolean(parse("MDK2")));
            setMDS2(toBoolean(parse("MDS2")));
            setMFREI(toBoolean(parse("MFREI")));
            setMFFBB(Integer.parseInt(parse("MFFBB")));
            setMFFBBORIG(Integer.parseInt(parse("MFFBB")));
            setMFFBV(Integer.parseInt(parse("MFFBV")));
            setMFFBVORIG(Integer.parseInt(parse("MFFBV")));
            setMFFHB(Integer.parseInt(parse("MFFHB")));
            setMFFHV(Integer.parseInt(parse("MFFHV")));
            setMSSTK(Integer.parseInt(parse("MSSTK")));
            setMSTART(parse("MSTART"));
            setMSISTK(Integer.parseInt(parse("MSISSTK")));
            setMSISTART(parse("MSISTART"));
            setSAP(Integer.parseInt(parse("SAP")));
            setText(parse("text"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean toBoolean(String value) {
        if (value.trim().equalsIgnoreCase("igaz")) {
            return true;
        } else {
            return false;
        }
    }

    public String getLine() {
        return Line;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public int getSAP() {
        return SAP;
    }


    public void setSAP(int sap) {
        SAP = sap;
    }

    public String getDirection() {
        return Direction;
    }

    public boolean isRight() {
        if (getDirection().toLowerCase()
                          .trim()
                          .equalsIgnoreCase("r")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLeft() {
        if (getDirection().toLowerCase()
                          .trim()
                          .equalsIgnoreCase("l")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCilinder() {
        if (getColor().toLowerCase()
                      .trim()
                      .equalsIgnoreCase("c")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHandle() {
        if (getDirection().toLowerCase()
                          .trim()
                          .equalsIgnoreCase("k")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isHandleGarniture() {
        if (getDirection().toLowerCase()
                          .trim()
                          .equalsIgnoreCase("g")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isLiftingMD() {
        if (getColor().toLowerCase()
                      .trim()
                      .equalsIgnoreCase("h")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCatchNormal() {
        if (getColor().toLowerCase()
                      .trim()
                      .equalsIgnoreCase("cs")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCatchMagnet() {
        if (getColor().toLowerCase()
                      .trim()
                      .equalsIgnoreCase("mcs")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFan() {
        if (getColor().toLowerCase()
                      .trim()
                      .equalsIgnoreCase("r")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPullHandle() {
        if (getLock().toLowerCase()
                     .trim()
                     .equalsIgnoreCase("kh")) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isColor() {
        boolean isColor = false;
        for (int i = 0; i < swc.colorArray.length; i++) {
            if (getColor().toLowerCase()
                          .trim()
                          .equalsIgnoreCase(swc.colorArray[i]
                                               .toLowerCase()
                                               .trim())) {
                isColor = true;
            }
        }
        return isColor;
    }

    public boolean isCloasbleBar() {
        boolean isClosabeBar = false;
        for (int i = 0; i < swc.closableBarArray.length; i++) {
            if (getLock().toLowerCase()
                         .trim()
                         .equalsIgnoreCase(swc.closableBarArray[i]
                                              .toLowerCase()
                                              .trim())) {
                isClosabeBar = true;
            }
        }
        return isClosabeBar;
    }

    public boolean isSelectionBar() {
        boolean isSelectionBar = false;
        for (int i = 0; i < swc.selectionBarArray.length; i++) {
            if (getColor().toLowerCase()
                          .trim()
                          .equalsIgnoreCase(swc.selectionBarArray[i]
                                               .toLowerCase()
                                               .trim())) {
                isSelectionBar = true;
            }
        }
        return isSelectionBar;
    }

    public boolean isElolapDornos() {
        return getLock().toLowerCase()
                        .trim()
                        .equalsIgnoreCase("ed");
    }

    public boolean isNormalBar() {
        if (getLock().toLowerCase()
                     .trim()
                     .equalsIgnoreCase("0")) {
            return true;
        }
        return false;
    }

    public void setDirection(String direction) {
        Direction = direction;
    }


    public String getText() {
        return Text;
    }


    public void setText(String text) {
        Text = text;
    }


    public boolean isMDK() {
        return MDK;
    }


    public void setMDK(boolean mdk) {
        MDK = mdk;
    }


    public boolean isMDF() {
        return MDF;
    }


    public void setMDF(boolean mdf) {
        MDF = mdf;
    }


    public boolean isMDS() {
        return MDS;
    }


    public void setMDS(boolean mds) {
        MDS = mds;
    }


    public boolean isMKIPP() {
        return MKIPP;
    }


    public void setMKIPP(boolean mkipp) {
        MKIPP = mkipp;
    }


    public boolean isMDK2() {
        return MDK2;
    }


    public void setMDK2(boolean mdk2) {
        MDK2 = mdk2;
    }


    public boolean isMDF2() {
        return MDF2;
    }


    public void setMDF2(boolean mdf2) {
        MDF2 = mdf2;
    }


    public boolean isMDS2() {
        return MDS2;
    }


    public void setMDS2(boolean mds2) {
        MDS2 = mds2;
    }


    public boolean isMFREI() {
        return MFREI;
    }


    public void setMFREI(boolean mfrei) {
        MFREI = mfrei;
    }


    public String getLock() {
        return Lock;
    }


    public void setLock(String lock) {
        Lock = lock;
    }


    public String getColor() {
        return Color;
    }

    public String getSelectionBar() {
        return Color;
    }


    public void setColor(String color) {
        Color = color;
    }


    public int getMSSTK() {
        return MSSTK;
    }


    public void setMSSTK(int msstk) {
        MSSTK = msstk;
    }


    public int getMSISTK() {
        return MSISTK;
    }


    public void setMSISTK(int msistk) {
        MSISTK = msistk;
    }


    public int getMFFBV() {
        return MFFBV;
    }


    public void setMFFBV(int mffbv) {
        MFFBV = mffbv;
    }


    public int getMFFBB() {
        return MFFBB;
    }


    public void setMFFBB(int mffbb) {
        MFFBB = mffbb;
    }


    public int getMFFHV() {
        return MFFHV;
    }


    public void setMFFHV(int mffhv) {
        MFFHV = mffhv;
    }


    public int getMFFHB() {
        return MFFHB;
    }


    public void setMFFHB(int mffhb) {
        MFFHB = mffhb;
    }


    public String getMSTART() {
        if (MSTART != null && MSTART.length() == 7) {
            MSTART = MSTART.substring(1);
        }
        return MSTART;
    }


    public void setMSTART(String mstart) {
        MSTART = mstart;
    }


    public String getMSISTART() {
        if (MSISTART != null && MSISTART.length() == 7) {
            MSISTART = MSISTART.substring(1);
        }
        return MSISTART;
    }


    public void setMSISTART(String msistart) {
        MSISTART = msistart;
    }

    public int getMFFBVORIG() {
        return MFFBVORIG;
    }

    public void setMFFBVORIG(int mffbvorig) {
        MFFBVORIG = mffbvorig;
    }

    public int getMFFBBORIG() {
        return MFFBBORIG;
    }

    public void setMFFBBORIG(int mffbborig) {
        MFFBBORIG = mffbborig;
    }


}
