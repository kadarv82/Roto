package Roto.DataBase;

import java.text.NumberFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;

import Roto.Basic.SwingConstans;

import Roto.Gui.Frames;

import Roto.Utils.PriceUtil;
import Roto.Utils.PropertyUtil;

public class DataBaseCollectionList {

    private List<DataBaseCollection> databaseCollectionList;
    private HashMap<String, Integer> selectionSummarized;
    private PriceUtil priceUtil;
    private NumberFormat formatter;
    private SwingConstans swc;
    private PropertyUtil propertyUtil;
    private double priceList, priceNet, priceNetSum;
    private String selectedListType;

    public DataBaseCollectionList() {
        databaseCollectionList = new ArrayList<DataBaseCollection>();
        priceUtil = PriceUtil.getInstance();
        swc = SwingConstans.getInstance();
        formatter = swc.formatter;
        propertyUtil = PropertyUtil.getInstance();
        selectionSummarized = new HashMap<String, Integer>();
    }

    public void addCollection(DataBaseCollection collection) {
        databaseCollectionList.add(collection);
    }

    public void removeLastCollection() {
        if (databaseCollectionList.size() > 0) {
            databaseCollectionList.remove(databaseCollectionList.size() - 1);
        }
    }

    public List<DataBaseCollection> getDataBaseCollectionList() {
        return databaseCollectionList;
    }

    public int getCollectionCount() {
        return databaseCollectionList.size();
    }


    //Clear table model
    private void removeRows(DefaultTableModel tablemodelList) {
        while (tablemodelList.getRowCount() > 0) {
            tablemodelList.removeRow(0);
        }
    }

    //Create editing collection list
    public void createListItems(JList list) {
        DefaultListModel listModel = (DefaultListModel) list.getModel();
        listModel.removeAllElements();
        int counter = 0;
        //Iterate on cellections in the list
        for (DataBaseCollection collection : databaseCollectionList) {

            counter++;
            String listItem = counter + ". " + propertyUtil.getLangText("InternalFrame.CollectionEdit.Item") + ": ";
            listItem += collection.getGarniture() + " * " + collection.getDatabaseName();
            listItem += " - " + collection.getTypeName() + " ( ";
            listItem += collection.getDirection() + ", ";
            listItem += collection.getColorName() + ", ";
            listItem += collection.getWidth() + " * " + collection.getHeight();
            if (collection.getHeight2() > 0) {
                listItem += ", " + collection.getWidth2() + " * " + collection.getHeight2();
            }
            listItem += " )";

            //Show only database name if summarized list
            if (collection.getTypeName().equals(swc.LIST_SUMMARIZED)) {
                listItem =
                    counter + ". " + propertyUtil.getLangText("InternalFrame.CollectionEdit.Item") + ": 1 * " +
                    collection.getDatabaseName();
            }

            listModel.addElement(listItem);
        }

        if (listModel.getSize() > 0) {
            list.setSelectedIndex(0);
        }
    }

    //Remove an item from collection in the collection list
    public void removeCollectionItem(int collectionIndex, String SAP) {
        DataBaseCollection collection = databaseCollectionList.get(collectionIndex);
        collection.removeCollectionItem(SAP);
    }

    //Add an item to collection in the collection list
    public void addCollectionItem(int collectionIndex, String SAP) {
        DataBaseCollection collection = databaseCollectionList.get(collectionIndex);
        collection.addCollectionItem(SAP);
    }

    //Increase or decrease an item count of a collection in the collection list
    public void modifyCollectionItemCount(int collectionIndex, String SAP, int modifyValue) {
        DataBaseCollection collection = databaseCollectionList.get(collectionIndex);
        collection.modifyCollectionItemCount(SAP, modifyValue);
    }

    //set item count of a collection in the collection list
    public void setCollectionItemCount(int collectionIndex, String SAP, int itemCount) {
        DataBaseCollection collection = databaseCollectionList.get(collectionIndex);
        collection.setCollectionItemCount(SAP, itemCount);
    }

    //Remove collection from the collection list
    public void removeCollection(int collectionIndex) {
        databaseCollectionList.remove(collectionIndex);
    }

    //Create collection list item by index
    public void createListItemAt(DefaultTableModel tableModelList, int index) {
        if (index < 0)
            return;

        //Clear table model
        removeRows(tableModelList);

        DataBaseCollection collection = databaseCollectionList.get(index);

        //Get the selection
        HashMap<String, Integer> selection = collection.getSelection();

        //Iterate on selection (ordered)
        Object[] key = selection.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {

            String SAP = key[i].toString();
            int pieces = selection.get(SAP);

            //Add rows to detailed collection list
            tableModelList.addRow(new Object[] { selection.get(SAP).toString(), SAP, priceUtil.getListText(SAP),
                                                 formatter.format(priceUtil.getListPriceMultiplied(SAP)),
                                                 formatter.format(priceUtil.getListPriceCalculated(SAP)),
                                                 formatter.format(formatDouble(priceUtil.getListPriceCalculated(SAP) *
                                                                               pieces)) });
        }
    }

    public void createListSummarized(DefaultTableModel tableModelList) {

        if (databaseCollectionList.size() == 0)
            return;

        priceList = 0;
        priceNet = 0;
        priceNetSum = 0;
        setSelectedListType(swc.LIST_SUMMARIZED);

        //Clear table model
        removeRows(tableModelList);

        //Clear summarized list
        selectionSummarized = new HashMap<String, Integer>();

        //Add column headers
        tableModelList.addRow(new Object[] { propertyUtil.getLangText("InternalFrame.CollectionEdit.Count"),
                                             propertyUtil.getLangText("InternalFrame.CollectionEdit.SAP"),
                                             propertyUtil.getLangText("InternalFrame.CollectionEdit.Name"),
                                             propertyUtil.getLangText("InternalFrame.CollectionEdit.ListPrice"),
                                             propertyUtil.getLangText("InternalFrame.CollectionEdit.NetPrice"),
                                             propertyUtil.getLangText("InternalFrame.CollectionEdit.NetSum") });
        //Add currency name
        tableModelList.addRow(new Object[] { "", "", "", priceUtil.getCurrency(), priceUtil.getCurrency(),
                                             priceUtil.getCurrency() });


        tableModelList.addRow(new Object[] { });

        //Iterate on cellections in the list
        for (DataBaseCollection collection : databaseCollectionList) {

            //Get the selection
            HashMap<String, Integer> selection = collection.getSelection();

            //Iterate on selection (ordered)
            Object[] key = selection.keySet().toArray();
            for (int i = 0; i < key.length; i++) {

                String SAP = key[i].toString();
                int pieces = selection.get(SAP);

                addToSummarizedList(SAP, pieces);
            }

        }

        //Iterate on summarized list (ordered)
        Object[] key = selectionSummarized.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {

            String SAP = key[i].toString();
            int pieces = selectionSummarized.get(SAP);

            String ID = SAP;

            if (Frames.getInstance()
                      .jrPrintMartikel
                      .isSelected()) {
                ID = priceUtil.getMartikel(SAP);
                if (ID.length() == 0) {
                    ID = SAP;
                }
            }

            //Add rows to summarized collection list
            tableModelList.addRow(new Object[] { selectionSummarized.get(SAP).toString(), ID,
                                                 priceUtil.getListText(SAP),
                                                 formatter.format(priceUtil.getListPriceMultiplied(SAP)),
                                                 formatter.format(priceUtil.getListPriceCalculated(SAP)),
                                                 formatter.format(formatDouble(priceUtil.getListPriceCalculated(SAP) *
                                                                               pieces)) });

            priceList += priceUtil.getListPriceMultiplied(SAP);
            priceNet += priceUtil.getListPriceCalculated(SAP);
            priceNetSum += formatDouble(priceUtil.getListPriceCalculated(SAP) * pieces);

        }

        //Add summary
        if (selectionSummarized.size() > 0) {
            tableModelList.addRow(new Object[] { });
            tableModelList.addRow(new Object[] { "", "",
                                                 propertyUtil.getLangText("InternalFrame.CollectionList.Sum") +
                                                 propertyUtil.getLangText("InternalFrame.CollectionList.Vat") + ":", "",
                                                 "", formatter.format(priceNetSum), });
        }
    }

    public void createListGarniture(DefaultTableModel tableModelList) {
        setSelectedListType(swc.LIST_GARNITURE);
        createList(tableModelList, true);
    }

    public void createListDetailed(DefaultTableModel tableModelList) {
        setSelectedListType(swc.LIST_DETAILED);
        createList(tableModelList, false);
    }

    private void createList(DefaultTableModel tableModelList, boolean isGarnitureList) {
        int counter = 0;
        //Clear table model
        removeRows(tableModelList);

        //Iterate on cellections in the list
        for (DataBaseCollection collection : databaseCollectionList) {

            if (collection.getSelection().size() == 0)
                return;

            priceList = 0;
            priceNet = 0;
            priceNetSum = 0;
            counter++;
            tableModelList.addRow(new Object[] { });

            //Add header (detailed type)
            if (!collection.getTypeName().equals(swc.LIST_SUMMARIZED)) {

                String description =
                    collection.getTypeName() + ", " + collection.getDirection() + " ," + collection.getColorName();
                String size2 = "";
                if (collection.getWidth2() != 0)
                    size2 = collection.getWidth2() + "*" + collection.getHeight2();

                tableModelList.addRow(new Object[] { propertyUtil.getLangText("InternalFrame.CollectionList.Item") +
                                                     ":", counter + ".", collection.getDatabaseName(),
                                                     collection.getWidth() + "*" + collection.getHeight(), size2,
                                                     propertyUtil.getLangText("InternalFrame.CollectionList.Garniture") +
                                                     ": " + collection.getGarniture() });

                tableModelList.addRow(new Object[] { null, null, description, null, null, null });
            }
            //Add header (summarized type)
            else {
                List<String> descriptions = collection.getDescriptions();
                tableModelList.addRow(new Object[] { propertyUtil.getLangText("InternalFrame.CollectionList.Item") +
                                                     ":", counter + ".", collection.getDatabaseName(), null, null,
                                                     null });
                if (descriptions != null) {
                    for (String description : descriptions) {
                        tableModelList.addRow(new Object[] { null, null, description, null, null, null });
                    }
                }
            }

            if (!isGarnitureList) {
                tableModelList.addRow(new Object[] { });

                //Add column headers 			
                tableModelList.addRow(new Object[] { propertyUtil.getLangText("InternalFrame.CollectionEdit.Count"),
                                                     propertyUtil.getLangText("InternalFrame.CollectionEdit.SAP"),
                                                     propertyUtil.getLangText("InternalFrame.CollectionEdit.Name"),
                                                     propertyUtil.getLangText("InternalFrame.CollectionEdit.ListPrice"),
                                                     propertyUtil.getLangText("InternalFrame.CollectionEdit.NetPrice"),
                                                     propertyUtil.getLangText("InternalFrame.CollectionEdit.NetSum") });

                //Add currency name
                tableModelList.addRow(new Object[] { "", "", "", priceUtil.getCurrency(), priceUtil.getCurrency(),
                                                     priceUtil.getCurrency() });

                tableModelList.addRow(new Object[] { });
            }

            //Get the selection
            HashMap<String, Integer> selection = collection.getSelection();

            //Iterate on selection (ordered)
            Object[] key = selection.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {

                String SAP = key[i].toString();
                int pieces = selection.get(SAP);

                String ID = SAP;

                if (Frames.getInstance()
                          .jrPrintMartikel
                          .isSelected()) {
                    ID = priceUtil.getMartikel(SAP);
                    if (ID.length() == 0) {
                        ID = SAP;
                    }
                }

                if (!isGarnitureList) {
                    //Add rows to detailed collection list
                    tableModelList.addRow(new Object[] { selection.get(SAP).toString(), ID, priceUtil.getListText(SAP),
                                                         formatter.format(priceUtil.getListPriceMultiplied(SAP)),
                                                         formatter.format(priceUtil.getListPriceCalculated(SAP)),
                                                         formatter.format(formatDouble(priceUtil.getListPriceCalculated(SAP) *
                                                                                       pieces)) });
                }

                priceList += priceUtil.getListPriceMultiplied(SAP);
                priceNet += priceUtil.getListPriceCalculated(SAP);
                priceNetSum += formatDouble(priceUtil.getListPriceCalculated(SAP) * pieces);
            }

            //Add summary
            if (!isGarnitureList)
                tableModelList.addRow(new Object[] { });
            tableModelList.addRow(new Object[] { "", "",
                                                 propertyUtil.getLangText("InternalFrame.CollectionList.Sum") +
                                                 propertyUtil.getLangText("InternalFrame.CollectionList.Vat") + ":", "",
                                                 "", formatter.format(priceNetSum) });
        }

    }

    //Get total price of the collection list
    public double getTotalPrice() {
        double totalPrice = 0;

        //Iterate on cellections in the list
        for (DataBaseCollection collection : databaseCollectionList) {
            //Get the selection
            HashMap<String, Integer> selection = collection.getSelection();

            //Iterate on selection (ordered)
            Object[] key = selection.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {
                String SAP = key[i].toString();
                int pieces = selection.get(SAP);

                totalPrice += formatDouble(priceUtil.getListPriceCalculated(SAP) * pieces);
            }
        }

        return totalPrice;
    }

    //Get navison list to save as text file
    public String getNavisonCollectionList() {
        //This will generate the selectionSummarized list
        createListSummarized(new DefaultTableModel());

        StringBuffer sbList = new StringBuffer("");
        for (String key : selectionSummarized.keySet()) {

            String pieces = String.valueOf(selectionSummarized.get(key));
            String ID = key;
            if (Frames.getInstance()
                      .jrPrintMartikel
                      .isSelected()) {
                ID = priceUtil.getMartikel(key);
                if (ID.length() == 0) {
                    ID = key;
                }
            }

            sbList.append(ID + ";" + pieces + "\r\n");
        }

        return sbList.toString();
    }

    //Get project list to save as text file
    public String getProjectCollectionList() {
        //This will generate the selectionSummarized list
        createListSummarized(new DefaultTableModel());

        StringBuffer sbList = new StringBuffer("");

        //Iterate on summarized list (ordered)
        Object[] key = selectionSummarized.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            String SAP = key[i].toString();
            int pieces = selectionSummarized.get(SAP);
            String Text = priceUtil.getListText(SAP);
            String listPrice = String.valueOf(formatDouble(priceUtil.getListPriceMultiplied(SAP))).replaceAll(",", ".");
            String netPrice = String.valueOf(formatDouble(priceUtil.getListPriceCalculated(SAP))).replaceAll(",", ".");
            String netSum = String.valueOf(formatDouble((priceUtil.getListPriceCalculated(SAP) * pieces)));

            sbList.append(String.format("%-7s", pieces).substring(0, 7) + String.format("%-2s", "*") +
                          String.format("%-8s", SAP).substring(0, 8) + String.format("%-42s", Text).substring(0, 42) +
                          String.format("%12s", listPrice).substring(0, 12) +
                          String.format("%11s", netPrice).substring(0, 11) +
                          String.format("%13s", netSum).substring(0, 13) + "\n");
        }

        return sbList.toString();
    }


    /*	String SAP = key[i].toString();
	int pieces = selectionSummarized.get(SAP);
	String Text = priceUtil.getListText(SAP);
	String listPrice = String.valueOf(formatter.format(priceUtil.getListPriceMultiplied(SAP))).replaceAll(",", ".");
	String netPrice = String.valueOf(formatter.format(priceUtil.getListPriceCalculated(SAP))).replaceAll(",", ".");
	String netSum = String.valueOf(formatter.format(formatDouble(priceUtil.getListPriceCalculated(SAP) * pieces))).replaceAll(",", ".");

	sbList.append(String.format("%-7s", pieces).substring(0,7) + String.format("%-2s", "*") +
	String.format("%-8s", SAP).substring(0,8) +
	String.format("%-42s", Text).substring(0,42) +
	String.format("%12s", listPrice).substring(0,12) +
	String.format("%11s", netPrice).substring(0,11) +
	String.format("%13s", netSum).substring(0,13) + "\n");
System.out.println(netSum);*/

    //Get serializable collection list to save
    public List<SerializableDataBaseCollection> getSerializedCollectionList(String listType) {
        List<SerializableDataBaseCollection> collectionList = new ArrayList<SerializableDataBaseCollection>();
        SerializableDataBaseCollection serializedCollection;
        //Create detailed list
        if (listType.equals(swc.LIST_DETAILED)) {
            for (DataBaseCollection collection : databaseCollectionList) {
                serializedCollection = new SerializableDataBaseCollection();
                serializedCollection.setColorName(collection.getColorName());
                serializedCollection.setDatabaseName(collection.getDatabaseName());
                serializedCollection.setDirection(collection.getDirection());
                serializedCollection.setGarniture(collection.getGarniture());
                serializedCollection.setHeight(collection.getHeight());
                serializedCollection.setHeight2(collection.getHeight2());
                serializedCollection.setSelection(collection.getSelection());
                serializedCollection.setTypeName(collection.getTypeName());
                serializedCollection.setWidth(collection.getWidth());
                serializedCollection.setWidth2(collection.getWidth2());

                collectionList.add(serializedCollection);
            }
        }
        //Create summarized list
        else if (listType.equals(swc.LIST_SUMMARIZED)) {
            serializedCollection = new SerializableDataBaseCollection();
            List<String> descriptions = new ArrayList<String>();

            //Get database names
            for (DataBaseCollection collection : databaseCollectionList) {
                String description = collection.getGarniture() + " * " + collection.getDatabaseName();

                if (!collection.getTypeName().equals(swc.LIST_SUMMARIZED)) {
                    description +=
                        " - " + collection.getTypeName() + "," + collection.getDirection() + "," +
                        collection.getWidth() + "*" + collection.getHeight();
                    if (collection.getWidth2() > 0) {
                        description += " + " + collection.getWidth2() + "*" + collection.getHeight2();
                    }
                }

                descriptions.add(description);
                //A meglevo description-okat is hozza kell adni az uj descriptionhoz, azaz ha egy
                //mar summarizalt listat summarizalunk masokkal, akkor annak a tartalmat is hozzaadjuk
                if (collection.getDescriptions() != null && collection.getDescriptions().size() > 0) {
                    List<String> oldDescriptions = collection.getDescriptions();
                    for (String oldDescription : oldDescriptions) {
                        descriptions.add(oldDescription);
                    }
                    descriptions.add("");
                }
            }
            //This will generate the selectionSummarized list
            createListSummarized(new DefaultTableModel());

            serializedCollection.setDescriptions(descriptions);
            serializedCollection.setSelection(selectionSummarized);
            serializedCollection.setGarniture(1);
            serializedCollection.setTypeName(swc.LIST_SUMMARIZED);
            serializedCollection.setDatabaseName(propertyUtil.getLangText("InternalFrame.CollectionList.Sumarized"));

            collectionList.add(serializedCollection);
        }

        return collectionList;
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

    //Add a row to the summarized list handle duplicate items
    private void addToSummarizedList(String SAP, int itemCount) {
        //Add item if not exists
        if (!selectionSummarized.containsKey(SAP)) {
            selectionSummarized.put(SAP, itemCount);
        } else {
            //Get count of the existing element
            int count = selectionSummarized.get(SAP);
            //Remove existing element
            selectionSummarized.remove(SAP);
            //Add aggregated item
            selectionSummarized.put(SAP, (count + itemCount));
        }
    }

    //Get last selected list type (this used to refresh list frame if visible, when there were any modifications)
    public String getSelectedListType() {
        return selectedListType;
    }

    //Set last selected list type (this used to refresh list frame if visible, when there were any modifications)
    public void setSelectedListType(String selectedListType) {
        this.selectedListType = selectedListType;
    }
}
