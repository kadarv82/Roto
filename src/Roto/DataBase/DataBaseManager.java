package Roto.DataBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;

import Roto.Gui.DataBaseTableModel;
import Roto.Gui.Frames;

import Roto.Utils.IOUtil;
import Roto.Utils.MessageUtil;
import Roto.Utils.PriceUtil;
import Roto.Utils.PropertyUtil;
import Roto.Windows.WindowSystems;

public class DataBaseManager {

    private static DataBaseManager instance = null;
    private MessageUtil message;
    private PropertyUtil propertyUtil;
    private IOUtil ioUtil;
    private TextDBTable table;
    private TextDBTable tableEdit;
    private TextDBTable tableStock;
    private TextDBRow tableRow;
    private DataBaseCollection collection;
    private DataBaseCollectionList collectionList;
    private PriceUtil priceUtil;
    private String type;
    private String editingType;
    private String selectedTypeName;
    private String direction;
    private String dataBaseName;
    private List<String> closableBarList;
    private List<String> selectioneBarList;
    private File editingDataBaseFile;

    private int garniture = 0, width = 0, height = 0, width2 = 0, height2 = 0, correctionHeight = 0, correctionWidth =
        0, minWidth = 999999, maxWidth = 0, minHeight = 999999, maxHeight = 0;
    private HashMap<String, Integer> selection;
    private HashMap<String, String> databaseList;
    private boolean isLoaded;
    private boolean resetHasProperties = true; //Keep values when double selection swiched on
    private SwingConstans swc;
    private boolean hasMDK, hasMDF, hasMDS, hasMKIPP, hasMDK2, hasMDF2, hasMDS2, hasMFREI, hasHandle, hasLiftingMD, hasCatchMagnet, hasCatchNormal, hasFan, hasNormalBar, hasClosableBar, hasCilinder, hasSelectionBar;
    private List<String> colorList;

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public DataBaseManager() {
        message = MessageUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        priceUtil = PriceUtil.getInstance();
        ioUtil = IOUtil.getInstance();
        table = new TextDBTable();
        tableEdit = new TextDBTable();
        tableStock = new TextDBTable();
        isLoaded = false;
        swc = SwingConstans.getInstance();
        databaseList = new HashMap<String, String>();
        colorList = new ArrayList<String>();
        selection = new HashMap<String, Integer>();
        collectionList = new DataBaseCollectionList();
        garniture = 1;
        closableBarList = new ArrayList<String>();
        selectioneBarList = new ArrayList<String>();
    }

    private Connection getConnection(String dbURL) {
        try {
            String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
            String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + dbURL;
            String username = "";
            String password = "";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (SQLException e) {
            message.showErrorMessage(e.getMessage(), "Error while connecting to database");
            return null;
        } catch (ClassNotFoundException e2) {
            message.showErrorMessage(e2.getMessage(), "Error while connecting to database");
            return null;
        }
    }

    //Delete collection list
    public void deleteCollectionList() {
        //Store last selected list
        String lastSelectedListType = collectionList.getSelectedListType();
        collectionList = new DataBaseCollectionList();
        collectionList.setSelectedListType(lastSelectedListType);
    }

    //Clear selection
    public void clearSelection() {
        selection = new HashMap<String, Integer>();
    }

    //Get type form database
    private String getTypeFromRow(String line) {
        int From = propertyUtil.getIntProperty(propertyUtil.getDatabaseProperty(), "Database.Row.Type.From");
        int To = propertyUtil.getIntProperty(propertyUtil.getDatabaseProperty(), "Database.Row.Type.To");
        return line.substring(From, To).trim();
    }

    //Load text file, and create a table with row objects
    public void loadTextDataBase(File DataBaseFile, String loadType) {
        TextDBTable textTable = null;

        if (loadType.equals(swc.LOAD_COLLECTION)) {
            textTable = this.table;
        } else if (loadType.equals(swc.LOAD_EDIT)) {
            textTable = this.tableEdit;
        } else if (loadType.equals(swc.LOAD_STOCK)) {
            textTable = this.tableStock;
        }


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DataBaseFile), "UTF8"));

            String line;
            //Remove elements from table
            textTable.removeRows();
            //Skip first row
            if (loadType.equals(swc.LOAD_COLLECTION))
                this.type = getTypeFromRow(br.readLine());
            if (loadType.equals(swc.LOAD_EDIT))
                this.editingType = getTypeFromRow(br.readLine());
            if (loadType.equals(swc.LOAD_STOCK))
                br.readLine();

            //Load file
            while ((line = br.readLine()) != null) {
                if (line.trim().length() > 0) {
                    tableRow = new TextDBRow(line);
                    textTable.addRow(tableRow);
                }
            }
            br.close();

            if (loadType.equals(swc.LOAD_COLLECTION)) {
                setLoaded(true);
                setDatabasename(DataBaseFile.getName());
            }

        } catch (Exception e) {
            setLoaded(false);
            message.showErrorMessage(e.getMessage(), propertyUtil.getLangText("Error.DataBase.Load.Error"));
        }
    }

    // Save text database
    public void saveTextDataBase(File outPutFile, JTable table) {
        DataBaseTableModel dbModel = (DataBaseTableModel) table.getModel();
        tableRow = new TextDBRow();
        String header = Frames.getInstance()
                              .jcbEditDatabaseTyes
                              .getSelectedItem()
                              .toString() + swc.dataBaseHeaer;
        StringBuffer sbContent = new StringBuffer(header + "\r\n");

        for (int row = 0; row < dbModel.getRowCount(); row++) {
            StringBuffer sbLine = new StringBuffer("");
            for (int col = 0; col < dbModel.getColumnCount(); col++) {
                String rowData = dbModel.getValueAt(row, col)
                                        .toString()
                                        .trim();
                if (rowData.equals("true"))
                    rowData = "IGAZ";
                if (rowData.equals("false"))
                    rowData = "HAMIS";
                if ((col == 16 || col == 17) && rowData.length() > 0) {
                    rowData = "M" + rowData;
                }

                sbLine.append(tableRow.createRowItem(swc.propertyColumnNames[col], rowData));

                if (col == 1) {
                    sbLine.append(tableRow.createRowItem("MARTIKEL", ""));
                }
                if (col == 17) {
                    sbLine.append(tableRow.createRowItem("ANZEIGEN", ""));
                }


            }
            sbContent.append(sbLine.toString() + "\r\n");
        }

        //System.out.println(sbContent.toString());

        ioUtil.saveUTF8(outPutFile, sbContent.toString());

        message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.Save.Ready"));
    }

    private void setExistingItems(boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2,
                                  boolean isMDF2, boolean isMDS2, boolean isMFREI, int width, int height) {
        //Reset values in normal selection
        //Keep values in double selection
        if (resetHasProperties) {
            hasMDK = false;
            hasMDF = false;
            hasMDS = false;
            hasMKIPP = false;
            hasMDK2 = false;
            hasMDF2 = false;
            hasMDS2 = false;
            hasMFREI = false;
            hasHandle = false;
            hasLiftingMD = false;
            hasCatchMagnet = false;
            hasCatchNormal = false;
            hasFan = false;
            hasNormalBar = false;
            hasClosableBar = false;
            hasCilinder = false;
            hasSelectionBar = false;
            colorList = new ArrayList<String>();
            closableBarList = new ArrayList<String>();
            selectioneBarList = new ArrayList<String>();
        }

        //Check if the value contained by the column, and the height and width is correct too
        for (int i = 0; i < table.getRowCount(); i++) {
            boolean selectRow = true;
            boolean matchWidthAndHeight = false;

            tableRow = table.getRowAt(i);
            if (tableRow.isMDK())
                hasMDK = true;
            if (tableRow.isMDF())
                hasMDF = true;
            if (tableRow.isMDS())
                hasMDS = true;
            if (tableRow.isMKIPP())
                hasMKIPP = true;
            if (tableRow.isMDK2())
                hasMDK2 = true;
            if (tableRow.isMDF2())
                hasMDF2 = true;
            if (tableRow.isMDS2())
                hasMDS2 = true;
            if (tableRow.isMFREI())
                hasMFREI = true;
            if (tableRow.isColor() && tableRow.getItemCount() > 0) {
                if (!colorList.contains(tableRow.getColor())) {
                    colorList.add(tableRow.getColor());
                }
            }

            if (isMDK && !tableRow.isMDK())
                selectRow = false;
            if (isMDF && !tableRow.isMDF())
                selectRow = false;
            if (isMDS && !tableRow.isMDS())
                selectRow = false;
            if (isMKIPP && !tableRow.isMKIPP())
                selectRow = false;
            if (isMDK2 && !tableRow.isMDK2())
                selectRow = false;
            if (isMDF2 && !tableRow.isMDF2())
                selectRow = false;
            if (isMDS2 && !tableRow.isMDS2())
                selectRow = false;
            if (isMFREI && !tableRow.isMFREI())
                selectRow = false;


            if (selectRow) {

                if ((width > tableRow.getMFFBV() - 1 && width < tableRow.getMFFBB() + 1) &&
                    height > tableRow.getMFFHV() - 1 && height < tableRow.getMFFHB() + 1) {
                    matchWidthAndHeight = true;
                }

                if (tableRow.isHandle() || tableRow.isHandleGarniture())
                    hasHandle = true;
                if (tableRow.isLiftingMD() && matchWidthAndHeight)
                    hasLiftingMD = true;
                if (tableRow.isCatchNormal() && matchWidthAndHeight)
                    hasCatchNormal = true;
                if (tableRow.isCatchMagnet() && matchWidthAndHeight)
                    hasCatchMagnet = true;
                if (tableRow.isFan() && matchWidthAndHeight)
                    hasFan = true;
                if (tableRow.isNormalBar())
                    hasNormalBar = true;

                if (tableRow.isCloasbleBar()) {
                    //Fill found closable bars to menu
                    if (!closableBarList.contains(tableRow.getLock())) {
                        closableBarList.add(tableRow.getLock());
                    }
                    //Check if closable width and height intervall is match with given height and width
                    //BV from BB to
                    if (matchWidthAndHeight) {
                        hasClosableBar = true;
                    }
                }

                if (tableRow.isSelectionBar()) {
                    //Fill found closable bars to menu
                    if (!selectioneBarList.contains(tableRow.getSelectionBar())) {
                        selectioneBarList.add(tableRow.getSelectionBar());
                    }
                    //Check if closable width and height intervall is match with given height and width
                    //BV from BB to
                    if (matchWidthAndHeight) {
                        hasSelectionBar = true;
                    }
                }

                if (tableRow.isCilinder() && matchWidthAndHeight)
                    hasCilinder = true;
            }
        }

    }

    private void setMinMaxValues(TextDBRow row, boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP,
                                 boolean isMDK2, boolean isMDF2, boolean isMDS2, boolean isMFREI) {
        boolean selectRow = true;
        if (isMDK && !row.isMDK())
            selectRow = false;
        if (isMDF && !row.isMDF())
            selectRow = false;
        if (isMDS && !row.isMDS())
            selectRow = false;
        if (isMKIPP && !row.isMKIPP())
            selectRow = false;
        if (isMDK2 && !row.isMDK2())
            selectRow = false;
        if (isMDF2 && !row.isMDF2())
            selectRow = false;
        if (isMDS2 && !row.isMDS2())
            selectRow = false;
        if (isMFREI && !row.isMFREI())
            selectRow = false;
        if (selectRow) {
            if (row.getMFFBV() < minWidth)
                minWidth = row.getMFFBV();
            if (row.getMFFBB() > maxWidth)
                maxWidth = row.getMFFBB();

            if (row.getMFFHV() < minHeight)
                minHeight = row.getMFFHV();
            if (row.getMFFHB() > maxHeight)
                maxHeight = row.getMFFHB();
        }

        //Don't remember the reason of this...
        /*
		if (isMDS2) {
			minWidth = minWidth + 1;
		}*/
    }

    //Close database
    public void closeDataBase() {
        selection = new HashMap<String, Integer>();
    }

    //Select rows from the table object and create a selection
    public void selectCollectionRows(boolean left, boolean right, boolean over, boolean handleSelected,
                                     boolean liftingMD, String colorSign, int width, int height, boolean isMDK,
                                     boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2, boolean isMDF2,
                                     boolean isMDS2, boolean isMFREI, boolean isMDSMDF2, boolean isMDSMDK2,
                                     boolean catcNoneSelected, boolean catcNormalSelected, boolean catcMagnetSelected,
                                     boolean fan, boolean closableBarSelected, String closableBarSize,
                                     boolean normalBarSelected, boolean cilinderSelected, boolean clearSelection,
                                     String selectionBarSign) {

        if (clearSelection) {
            selection = new HashMap<String, Integer>();
        }

        minWidth = 999999;
        maxWidth = 0;
        minHeight = 999999;
        maxHeight = 0;
        String className = WindowSystems.getInstance()
                                        .getWindowSystemByTypeName(getType())
                                        .getSystemName();

        for (int i = 0; i < table.getRowCount(); i++) {
            boolean selectRow = true;
            tableRow = table.getRowAt(i);

            tableRow.setMFFBV(tableRow.getMFFBVORIG());
            tableRow.setMFFBB(tableRow.getMFFBBORIG());

            // Modify with interval, if MDS2 and Class 1
            if (isMDS2 && className.toLowerCase().equalsIgnoreCase(swc.windowSystemArray[0].toLowerCase())) {
                //width min
                tableRow.setMFFBV(tableRow.getMFFBV() * 2 + 12 - 1);
                //width max
                tableRow.setMFFBB(tableRow.getMFFBB() * 2 + 12);
            }

            if (tableRow.getItemCount() < 1)
                selectRow = false;
            if (width < tableRow.getMFFBV() || width > tableRow.getMFFBB())
                selectRow = false;
            if (height < tableRow.getMFFHV() || height > tableRow.getMFFHB())
                selectRow = false;
            if (left && tableRow.isRight())
                selectRow = false;
            if (right && tableRow.isLeft())
                selectRow = false;
            if (!handleSelected && (tableRow.isHandle() || tableRow.isHandleGarniture()))
                selectRow = false;
            if (handleSelected && normalBarSelected && tableRow.isHandleGarniture())
                selectRow = false;
            if (handleSelected && closableBarSelected && tableRow.isHandle())
                selectRow = false;
            if (!liftingMD && tableRow.isLiftingMD())
                selectRow = false;
            if (tableRow.isColor() && !tableRow.getColor().equals(colorSign))
                selectRow = false;
            if (isMDK && !tableRow.isMDK())
                selectRow = false;
            if (isMDF && !tableRow.isMDF())
                selectRow = false;
            if (isMDS && !tableRow.isMDS())
                selectRow = false;
            if (isMKIPP && !tableRow.isMKIPP())
                selectRow = false;
            if (isMDK2 && !tableRow.isMDK2())
                selectRow = false;
            if (isMDF2 && !tableRow.isMDF2())
                selectRow = false;
            if (isMDS2 && !tableRow.isMDS2())
                selectRow = false;
            if (isMFREI && !tableRow.isMFREI())
                selectRow = false;
            if (catcNoneSelected && (tableRow.isCatchMagnet() || tableRow.isCatchNormal()))
                selectRow = false;
            if (catcNoneSelected && (tableRow.isPullHandle()))
                selectRow = false;
            if (catcMagnetSelected && (tableRow.isCatchNormal()))
                selectRow = false;
            if (catcNormalSelected && (tableRow.isCatchMagnet()))
                selectRow = false;
            if (!fan && tableRow.isFan())
                selectRow = false;
            if (tableRow.isCloasbleBar() && closableBarSelected && !tableRow.getLock().equals(closableBarSize))
                selectRow = false;
            if (closableBarSelected && tableRow.isNormalBar())
                selectRow = false;
            if (!closableBarSelected && tableRow.isElolapDornos())
                selectRow = false;
            if (normalBarSelected && tableRow.isCloasbleBar())
                selectRow = false;
            if (tableRow.isSelectionBar() && !tableRow.getSelectionBar().equals(selectionBarSign))
                selectRow = false;

            if (!cilinderSelected && tableRow.isCilinder())
                selectRow = false;

            setMinMaxValues(tableRow, isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI);

            if (selectRow) {
                //Add selected rows

                //Add MSSTK and MSISTK
                if (tableRow.getMSSTK() > 0 && tableRow.getMSTART().length() > 0) {
                    addSelectionRow(tableRow.getMSTART(), (tableRow.getMSSTK() * tableRow.getItemCount() * garniture));
                    //System.out.println("MSSTK: " + row.getMSSTK() * row.getItemCount() + " * " + row.getMSTART());
                }

                if (tableRow.getMSISTK() > 0 && tableRow.getMSISTART().length() > 0) {
                    addSelectionRow(tableRow.getMSISTART(),
                                    (tableRow.getMSISTK() * tableRow.getItemCount() * garniture));
                    //System.out.println("MSISTK: " + row.getMSISTK() * row.getItemCount() + " * " + row.getMSISTART());
                }

                //Add row
                addSelectionRow(String.valueOf(tableRow.getSAP()), tableRow.getItemCount() * garniture);
            }

        }

        //Sort selection
        selection = sortHashMap(selection);

        setExistingItems(isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI, width, height);


        //System.out.println("Count: " + selection.size());
        //System.out.println("Width: " + minWidth + "-" + maxWidth);
        //System.out.println("Height: " + minHeight + "-" + maxHeight);
    }

    //Create database edit table
    public void createDataBaseEdit(JTable jtEditTable, boolean isEmptyTable) {
        DataBaseTableModel dtm = (DataBaseTableModel) jtEditTable.getModel();

        //Remove rows
        while (dtm.getRowCount() > 0) {
            dtm.removeRow(0);
        }

        if (isEmptyTable)
            tableEdit.removeRows();

        //Add rows
        for (int i = 0; i < tableEdit.getRowCount(); i++) {
            tableRow = tableEdit.getRowAt(i);

            dtm.addRow(new Object[] { String.valueOf(tableRow.getItemCount()), String.valueOf(tableRow.getSAP()),
                                      String.valueOf(tableRow.getDirection()),
                                      priceUtil.getListText(String.valueOf(tableRow.getSAP())), tableRow.isMDK(),
                                      tableRow.isMDF(), tableRow.isMDS(), tableRow.isMKIPP(),
                                      String.valueOf(tableRow.getLock()), String.valueOf(tableRow.getColor()),
                                      String.valueOf(tableRow.getMSSTK()), String.valueOf(tableRow.getMSISTK()),
                                      String.valueOf(tableRow.getMFFBV()), String.valueOf(tableRow.getMFFBB()),
                                      String.valueOf(tableRow.getMFFHV()), String.valueOf(tableRow.getMFFHB()),
                                      String.valueOf(tableRow.getMSTART()), String.valueOf(tableRow.getMSISTART()),
                                      tableRow.isMDK2(), tableRow.isMDF2(), tableRow.isMDS2(), tableRow.isMFREI() });
        }
    }

    //Create a new collecton
    public void createCollection(JTable jtCollection) {
        if (this.selection != null) {
            //Create collection
            collection = new DataBaseCollection(this.selection, jtCollection);

            //Set collection properties
            collection.setGarniture(garniture);
            collection.setTypeName(selectedTypeName);
            collection.setHeight(height + correctionHeight);
            collection.setHeight2(height2 + correctionHeight);
            collection.setWidth(width + correctionWidth);
            collection.setWidth2(width2 + correctionWidth);
            collection.setColorName(propertyUtil.getLangText(propertyUtil.getKeysFromValue(propertyUtil.getColorMap(),
                                                                                           swc.getSelectedColorSign())));
            collection.setDirection(direction);
            if (dataBaseName.endsWith(".prn"))
                dataBaseName = dataBaseName.substring(0, dataBaseName.indexOf(".prn"));
            collection.setDatabaseName(dataBaseName);
            //System.out.println("W*H: " + width + "*"+height+", "+width2+"*"+height2);
        } else {
            System.out.println("Selection is null !");
        }
    }

    //Select rows for stock list
    public void selectStockRows(boolean left, boolean right, boolean over, boolean handle, boolean liftingMD,
                                List<String> colorSigns, boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP,
                                boolean isMDK2, boolean isMDF2, boolean isMDS2, boolean isMFREI, boolean catcNone,
                                boolean catcNormal, boolean catcMagnet, boolean fan, boolean closableBar,
                                List<String> barSizes, boolean normalBar, boolean cilinder, boolean clearStockSelection,
                                List<String> barTypes) {

        if (clearStockSelection)
            selection = new HashMap<String, Integer>();
        //Select rows for stock list
        for (int i = 0; i < tableStock.getRowCount(); i++) {
            tableRow = tableStock.getRowAt(i);

            //"OR" relationship, not the same than collection
            boolean selectRow = false;

            if (isMDK && tableRow.isMDK())
                selectRow = true;
            if (isMDF && tableRow.isMDF())
                selectRow = true;
            if (isMDS && tableRow.isMDS())
                selectRow = true;
            if (isMKIPP && tableRow.isMKIPP())
                selectRow = true;
            if (isMDK2 && tableRow.isMDK2())
                selectRow = true;
            if (isMDF2 && tableRow.isMDF2())
                selectRow = true;
            if (isMDS2 && tableRow.isMDS2())
                selectRow = true;
            if (isMFREI && tableRow.isMFREI())
                selectRow = true;

            if (tableRow.getItemCount() < 1)
                selectRow = false;
            if (left && tableRow.isRight())
                selectRow = false;
            if (right && tableRow.isLeft())
                selectRow = false;
            if (!handle && (tableRow.isHandle() || tableRow.isHandleGarniture()))
                selectRow = false;
            if (handle && normalBar && tableRow.isHandleGarniture() && !closableBar)
                selectRow = false;
            if (handle && closableBar && tableRow.isHandle() && !normalBar)
                selectRow = false;
            if (!liftingMD && tableRow.isLiftingMD())
                selectRow = false;

            if (tableRow.isColor() && !colorSigns.contains(tableRow.getColor()))
                selectRow = false;

            if (catcNone && (tableRow.isCatchMagnet() || tableRow.isCatchNormal()))
                selectRow = false;
            if (catcNone && (tableRow.isPullHandle()))
                selectRow = false;
            if (catcMagnet && (tableRow.isCatchNormal()))
                selectRow = false;
            if (catcNormal && (tableRow.isCatchMagnet()))
                selectRow = false;
            if (!fan && tableRow.isFan())
                selectRow = false;

            if (tableRow.isCloasbleBar() && closableBar && !barSizes.contains(tableRow.getLock()))
                selectRow = false;
            if (tableRow.isSelectionBar() && !barTypes.contains(tableRow.getSelectionBar()))
                selectRow = false;

            if (!closableBar && tableRow.isCloasbleBar())
                selectRow = false;
            if (!closableBar && tableRow.isElolapDornos())
                selectRow = false;
            if (!normalBar && tableRow.isNormalBar())
                selectRow = false;

            if (!cilinder && tableRow.isCilinder())
                selectRow = false;
            //Select rows for stock list
            if (selectRow) {
                //Add selected rows

                //Add MSSTK and MSISTK
                if (tableRow.getMSSTK() > 0 && tableRow.getMSTART().length() > 0) {
                    addSelectionRow(tableRow.getMSTART(), (tableRow.getMSSTK() * tableRow.getItemCount() * garniture));
                    //System.out.println("MSSTK: " + row.getMSSTK() * row.getItemCount() + " * " + row.getMSTART());
                }

                if (tableRow.getMSISTK() > 0 && tableRow.getMSISTART().length() > 0) {
                    addSelectionRow(tableRow.getMSISTART(),
                                    (tableRow.getMSISTK() * tableRow.getItemCount() * garniture));
                    //System.out.println("MSISTK: " + row.getMSISTK() * row.getItemCount() + " * " + row.getMSISTART());
                }

                //Add row
                addSelectionRow(String.valueOf(tableRow.getSAP()), tableRow.getItemCount() * garniture);
            }

        }
        //Select rows for stock list
        //Sort selection
        selection = sortHashMap(selection);

    }

    //Create stock collection
    public void createStockCollection(JTable table) {
        createStockCollection(table, null);
    }

    //Create stock collection
    public void createStockCollection(JTable table, HashMap<String, Integer> stockSelection) {
        if (stockSelection == null)
            stockSelection = this.selection;

        if (stockSelection != null) {
            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

            //Remove rows
            while (tableModel.getRowCount() > 0) {
                tableModel.removeRow(0);
            }

            Object[] key = stockSelection.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {
                String SAP = key[i].toString();
                int pieces = stockSelection.get(SAP);

                tableModel.addRow(new Object[] { 1, SAP, priceUtil.getListText(SAP),
                                                 swc.formatter.format(formatDouble(priceUtil.getListPriceCalculated(SAP))) });
            }

        } else {
            System.out.println("Selection is null !");
        }
    }

    //Format double number
    private double formatDouble(Double value) {
        try {
            String Price = swc.formatter.format(value);
            Price = Price.replace(",", ".");
            return Double.parseDouble(Price);
        } catch (Exception e) {
            System.out.println("Can't format value: " + value);
            e.printStackTrace();
            return value;
        }
    }

    //Create collection list item by index
    public void createListItemAt(DefaultTableModel tableModelList, int index) {
        collectionList.createListItemAt(tableModelList, index);
    }

    //Add a row to the selection, handle duplicate items
    private void addSelectionRow(String SAP, int itemCount) {
        //Add item if not exists
        if (!selection.containsKey(SAP)) {
            selection.put(SAP, itemCount);
        } else {
            //Get count of the existing element
            int count = selection.get(SAP);
            //Remove existing element
            selection.remove(SAP);
            //Add aggregated item
            selection.put(SAP, (count + itemCount));
        }
    }

    //Sort selection
    private HashMap<String, Integer> sortHashMap(HashMap<String, Integer> map) {
        HashMap<String, Integer> sortedMap = new HashMap<String, Integer>();
        Object[] key = map.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            sortedMap.put(key[i].toString(), map.get(key[i]));
            //System.out.println(key[i].toString() +"="+ map.get(key[i]));
        }
        return sortedMap;
    }

    //Set database list, create new hashmap for file names
    public void setDataBaseListModel(JList jlList, String directoryPath) {
        DefaultListModel dlListModel = (DefaultListModel) jlList.getModel();
        databaseList = new HashMap<String, String>();
        dlListModel.removeAllElements();
        dlListModel.clear();
        File fDatabase = new File(directoryPath);
        String[] sFiles = fDatabase.list();

        //Get relative from path
        String relativePath = directoryPath.toLowerCase();
        if (relativePath.indexOf("dir") > 0) {
            relativePath = relativePath.substring(relativePath.indexOf("dir"));
        }

        for (int i = 0; i < sFiles.length; i++) {
            String fileName = sFiles[i].toString();
            String filePath = relativePath + "\\" + fileName;

            if (new File(filePath).isFile() && fileName.toUpperCase().endsWith(".PRN")) {
                dlListModel.addElement(fileName);
                databaseList.put(fileName, filePath);
            }
        }

        jlList.setSelectedIndex(0);

    }

    //Add collection to collection list
    public void addCollection() {
        if (selection != null && collection != null && selection.size() > 0) {
            collectionList.addCollection(collection);
        }
    }

    //Remove last collection from collection list
    public void removeLastCollection() {
        if (selection != null && collection != null) {
            collectionList.removeLastCollection();
        }
    }

    //Get total price of the collection list
    public Double getTotalPrice() {
        return collectionList.getTotalPrice();
    }

    //Get collection list
    public DataBaseCollectionList getCollectionList() {
        return collectionList;
    }

    //Get selected items count
    public int getSelectedItemsCount() {
        if (this.selection != null) {
            return selection.size();
        } else {
            return 0;
        }
    }

    //Get file path from database list map
    public String getDataBasePath(String fileName) {
        return databaseList.get(fileName);
    }

    //Create editing collection list
    public void createListItems(JList list) {
        collectionList.createListItems(list);
    }

    //Create detailed list
    public void createListDetailed(DefaultTableModel tableModelList) {
        collectionList.createListDetailed(tableModelList);
    }

    //Create garniture list
    public void createListGarniture(DefaultTableModel tableModelList) {
        collectionList.createListGarniture(tableModelList);
    }

    //Create summarized list
    public void createListSummarized(DefaultTableModel tableModelList) {
        collectionList.createListSummarized(tableModelList);
    }

    //Get database type
    public String getType() {
        return type;
    }

    //Set database type
    public void setType(String type) {
        this.type = type;
    }

    //Get database type for editing
    public String getEditingType() {
        return editingType;
    }

    //Get garniture
    public int getGarniture() {
        return garniture;
    }

    //Set garniture	
    public void setGarniture(int garniture) {
        this.garniture = garniture;
    }

    //Get type name
    public String getSelectedTypeName() {
        return selectedTypeName;
    }

    //Set type name
    public void setSelectedTypeName(String selectedTypeName) {
        this.selectedTypeName = selectedTypeName;
    }

    public int getCollectionCount() {
        return collectionList.getCollectionCount();
    }

    private String formatRight(String text, int length) {
        StringBuffer sbText = new StringBuffer("");
        int addSpaces = length - text.length();
        if (addSpaces < 0)
            addSpaces = 0;

        for (int i = 0; i < addSpaces; i++) {
            sbText.append(" ");
        }

        sbText = sbText.append(text);

        return sbText.toString().substring(0, length);
    }

    //Get cropped database name
    public String getCroppedDatabasename() {
        return formatRight(dataBaseName, 30);
    }

    //Get database name
    public String getDatabasename() {
        return dataBaseName;
    }

    //Set database name
    public void setDatabasename(String databasename) {
        this.dataBaseName = databasename;
    }

    //Set size
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth2() {
        return width2;
    }

    public void setWidth2(int width2) {
        this.width2 = width2;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight2() {
        return height2;
    }

    public void setHeight2(int height2) {
        this.height2 = height2;
    }

    public int getCorrectionHeight() {
        return correctionHeight;
    }

    public void setCorrectionHeight(int correctionHeight) {
        this.correctionHeight = correctionHeight;
    }

    public int getCorrectionWidth() {
        return correctionWidth;
    }

    public void setCorrectionWidth(int correctionWidth) {
        this.correctionWidth = correctionWidth;
    }

    //Get existing values

    public boolean hasMDK() {
        return hasMDK;
    }

    public boolean hasMDF() {
        return hasMDF;
    }

    public boolean hasMDS() {
        return hasMDS;
    }

    public boolean hasMKIPP() {
        return hasMKIPP;
    }

    public boolean hasMDK2() {
        return hasMDK2;
    }

    public boolean hasMDF2() {
        return hasMDF2;
    }

    public boolean hasMDS2() {
        return hasMDS2;
    }

    public boolean hasMFREI() {
        return hasMFREI;
    }

    public boolean hasHandle() {
        return hasHandle;
    }

    public boolean hasLiftingMD() {
        return hasLiftingMD;
    }

    public boolean hasCatchMagnet() {
        return hasCatchMagnet;
    }

    public boolean hasCatchNormal() {
        return hasCatchNormal;
    }

    public boolean hasFan() {
        return hasFan;
    }

    public boolean hasNormalBar() {
        return hasNormalBar;
    }

    public void setHasClosableBar(boolean hasClosableBar) {
        this.hasClosableBar = hasClosableBar;
    }

    public boolean hasClosableBar() {
        return hasClosableBar;
    }

    public boolean hasSelectionBar() {
        return hasSelectionBar;
    }

    public void setHasSelectionBar(boolean hasSelectionBar) {
        this.hasSelectionBar = hasSelectionBar;
    }

    public boolean hasCilinder() {
        return hasCilinder;
    }

    public List<String> getColorList() {
        return colorList;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    //Get min and max values

    public int getMinWidth() {
        return minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }


    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    //Editing database file

    public File getEditingDataBaseFile() {
        return editingDataBaseFile;
    }

    public void setEditingDataBaseFile(File editingDataBaseFile) {
        this.editingDataBaseFile = editingDataBaseFile;
    }

    // Get closable bar types
    public List<String> getClosableBarList() {
        return closableBarList;
    }

    public void setClosableBarList(List<String> closableBarList) {
        this.closableBarList = closableBarList;
    }

    public List<String> getSelectioneBarList() {
        return selectioneBarList;
    }

    public void setSelectioneBarList(List<String> selectioneBarList) {
        this.selectioneBarList = selectioneBarList;
    }

    // Keep or reset has values (keep values in double selection)
    public boolean isResetHasProperties() {
        return resetHasProperties;
    }

    public void setResetHasProperties(boolean resetHasProperties) {
        this.resetHasProperties = resetHasProperties;
    }

}
