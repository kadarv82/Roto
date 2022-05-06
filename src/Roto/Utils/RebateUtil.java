package Roto.Utils;


import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Roto.Basic.SwingConstans;

import Roto.Gui.Frames;
import Roto.Gui.Tree.CheckNode;

import Roto.Material.MaterialGroup;

public class RebateUtil {
    private static RebateUtil instance = null;
    private HashMap<String, String> customPriceListMap;
    private HashMap<String, String> rebateMap;
    private HashMap<String, MaterialGroup> customRebateMap;
    private Frames frames;
    private MessageUtil message;
    private PropertyUtil propertyUtil;
    private ProfileUtil profileUtil;
    private GroupUtil groupUtil;
    private UserUtil userUtil;
    private AdminUtil adminUtil;
    private SwingConstans swc;
    private File rebateFile;
    private CheckNode defaultProfileNode;
    private CheckNode defaultUserNode;

    public static RebateUtil getInstance() {
        if (instance == null) {
            instance = new RebateUtil();
        }
        return instance;
    }

    public RebateUtil() {
        customPriceListMap = new HashMap<String, String>();
        rebateMap = new HashMap<String, String>();
        customRebateMap = new HashMap<String, MaterialGroup>();
        frames = Frames.getInstance();
        message = MessageUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        groupUtil = GroupUtil.getInstance();
        userUtil = userUtil.getInstance();
        swc = SwingConstans.getInstance();
        profileUtil = ProfileUtil.getInstance();
        adminUtil = AdminUtil.getInstance();
    }

    //Add item to rebate custom pricelist map, handle duplicate items
    public void addCustomPriceListItem(String SAP, String Net) {
        //Add item if not exists
        if (!customPriceListMap.containsKey(SAP)) {
            Net = Net.replace(",", ".");
            customPriceListMap.put(SAP, Net);
        }
    }

    //Remove item from rebate custom pricelist map
    public void removeCustomPriceListItem(String SAP) {
        //Remove item
        customPriceListMap.remove(SAP);
    }

    //Get if custom price list item exists
    public boolean isCustomPriceListItemExist(String SAP) {
        if (customPriceListMap.get(SAP) == null) {
            return false;
        } else {
            return true;
        }
    }

    //Remove all rows from rebate custom pricelist map
    private void removeAllCustomPriceListItems() {
        customPriceListMap.clear();
        customPriceListMap = new HashMap<String, String>();
    }

    //Remove all rows from rebate map
    private void removeAllRebateItems() {
        rebateMap.clear();
        rebateMap = new HashMap<String, String>();
    }


    //Check custom price list net fields
    public boolean isCustomPriceListInputError() {
        frames.jtCustomPriceList.editCellAt(0, 0);
        String SAP = "";
        String Net = "";
        for (int i = 0; i < frames.jtCustomPriceList.getRowCount(); i++) {
            try {
                SAP = frames.jtCustomPriceList
                            .getValueAt(i, 0)
                            .toString();
                Net = frames.jtCustomPriceList
                            .getValueAt(i, 2)
                            .toString()
                            .trim();
                Double.parseDouble(Net.replace(",", "."));
            } catch (Exception e) {
                message.showInformationMessage(propertyUtil.getLangText("Error.Rebate.CustomPriceList") + " (" + SAP +
                                               " = '" + Net + "')");
                return true;
            }
        }
        return false;
    }

    //Check rebate input fields
    public boolean isRebateInputError() {
        String Group = "";
        String Rebate = "";
        frames.jtRebate.editCellAt(0, 0);
        for (int i = 0; i < frames.jtRebate.getRowCount(); i++) {
            try {
                Group = frames.jtRebate
                              .getValueAt(i, 0)
                              .toString();
                Rebate = frames.jtRebate
                               .getValueAt(i, 1)
                               .toString()
                               .trim();
                if (Rebate == null || Rebate.length() == 0) {
                    frames.jtRebate.setValueAt("0", i, 1);
                    Rebate = "0";
                }
                Double.parseDouble(Rebate.replace(",", "."));
            } catch (Exception e) {
                message.showInformationMessage(propertyUtil.getLangText("Error.Rebate.RebateList") + " (" + Group +
                                               " = '" + Rebate + "')");
                return true;
            }
        }
        return false;
    }

    private void clearTableModel(DefaultTableModel tableModel) {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }


    //Load rebate file by profile name
    public void loadDefaultRebate(String ProfileName) {
        loadRebate(ProfileName, null);
    }

    //Load rebate file by file name
    public void loadRebate(File rebateFile) {
        loadRebate(null, rebateFile);
    }

    //Load rebate file by profile name or file name
    private void loadRebate(String ProfileName, File rebateFile) {
        try {
            removeAllCustomPriceListItems();
            customRebateMap = new HashMap<String, MaterialGroup>();

            //Get file name by selected profile if selectedFile is null
            if (rebateFile == null) {
                Properties profileProperty = propertyUtil.getProfileProperty();
                String FileName = profileProperty.getProperty("Profiles." + ProfileName.toLowerCase() + ".RebateFile");
                if (FileName == null || FileName.length() == 0) {
                    FileName = swc.RebatePath + "Default.xml";
                }
                rebateFile = new File(FileName);
            }

            //Create xml document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(rebateFile);
            doc.getDocumentElement().normalize();

            //Create nodelists
            NodeList priceItemList = doc.getElementsByTagName("PriceListItem");
            NodeList rebateItemList = doc.getElementsByTagName("RebateItem");

            DefaultTableModel priceTableModel = (DefaultTableModel) frames.jtCustomPriceList.getModel();
            clearTableModel(priceTableModel);

            //Fill custom price list map
            for (int i = 0; i < priceItemList.getLength(); i++) {
                NodeList priceItemNode = priceItemList.item(i).getChildNodes();
                String SAP = "";
                String Net = "";
                String Text = "";
                for (int y = 0; y < priceItemNode.getLength(); y++) {
                    Node item = priceItemNode.item(y);
                    if (item != null) {
                        if (item.getNodeName()
                                .toLowerCase()
                                .trim()
                                .equalsIgnoreCase("sap")) {
                            SAP = item.getTextContent();
                        } else if (item.getNodeName()
                                       .toLowerCase()
                                       .trim()
                                       .equalsIgnoreCase("text")) {
                            Text = item.getTextContent();
                        } else if (item.getNodeName()
                                       .toLowerCase()
                                       .trim()
                                       .equalsIgnoreCase("net")) {
                            Net = item.getTextContent();
                        }
                    }
                }
                //Add items to map
                customPriceListMap.put(SAP, Net);

                //Add items to jtable
                priceTableModel.addRow(new Object[] { SAP, Text, Net });
            }

            if (priceTableModel.getRowCount() > 0) {
                //Set selected value in custom price list
                ListSelectionModel selectionModel = frames.jtCustomPriceList.getSelectionModel();
                selectionModel.setSelectionInterval(0, 0);
            }

            //Fill rebate list map
            for (int i = 0; i < rebateItemList.getLength(); i++) {
                NodeList rebateItemNode = rebateItemList.item(i).getChildNodes();
                String groupName = "";
                String rebate = "";
                String groupItemName = "";
                String groupItemText = "";
                String groupItemRebate = "";

                for (int y = 0; y < rebateItemNode.getLength(); y++) {
                    Node item = rebateItemNode.item(y);
                    if (item != null) {

                        if (item.getNodeName()
                                .toLowerCase()
                                .trim()
                                .equalsIgnoreCase("group")) {
                            groupName = item.getTextContent();
                        } else if (item.getNodeName()
                                       .toLowerCase()
                                       .trim()
                                       .equalsIgnoreCase("rebate")) {
                            rebate = item.getTextContent()
                                         .toLowerCase()
                                         .replace(",", ".");
                        }

                        else if (item.getNodeName()
                                     .toLowerCase()
                                     .trim()
                                     .equalsIgnoreCase("groupitems") && item.hasChildNodes()) {
                            NodeList groupItemListNode = item.getChildNodes();
                            for (int z = 0; z < groupItemListNode.getLength(); z++) {
                                Node groupItemNode = groupItemListNode.item(z);

                                if (groupItemNode.getNodeName()
                                                 .toLowerCase()
                                                 .trim()
                                                 .equalsIgnoreCase("groupitem")) {
                                    //Get attributes
                                    Element element = (Element) groupItemListNode.item(z);
                                    groupItemName = element.getAttribute("name");
                                    groupItemText = element.getAttribute("text");
                                    groupItemRebate = element.getAttribute("rebate")
                                                             .toLowerCase()
                                                             .replace(",", ".");

                                    //Check if the current group contains the found group item
                                    if (groupUtil.getGroupList(groupName).contains(groupItemName)) {

                                        MaterialGroup materialGroup = new MaterialGroup();
                                        materialGroup.setName(groupItemName);
                                        materialGroup.setText(groupItemText);
                                        materialGroup.setCustomRebate(groupItemRebate);

                                        customRebateMap.put(groupItemName, materialGroup);

                                    }
                                }

                            }
                        }

                    }
                }
                rebateMap.put(groupName, rebate);
            }


            //Set rebate list model by maps
            DefaultTableModel rebateTableModel = (DefaultTableModel) frames.jtRebate.getModel();
            clearTableModel(rebateTableModel);

            DefaultListModel dlGroupNames = groupUtil.getGroupNameListModel();
            for (int i = 0; i < dlGroupNames.size(); i++) {
                rebateTableModel.addRow(new Object[] { dlGroupNames.get(i),
                                                       rebateMap.get(dlGroupNames.get(i).toString()) });
            }


            //Set rebate info
            NodeList infoList = doc.getElementsByTagName("RebateInfo");
            Node info = infoList.item(0);
            String infoText = info.getTextContent();
            if (infoText == null) {
                infoText = "";
            }

            frames.jtRebateInfo.setText(infoText);
            //Set info to title
            if (infoText.trim().length() > 0) {
                frames.jrfMain.setTitle(propertyUtil.getLangText("Frame.Main.Title") + swc.version + " - " +
                                        extend(infoText, swc.rebateTitleInfoLength).trim());
            } else {
                frames.jrfMain.setTitle(propertyUtil.getLangText("Frame.Main.Title") + swc.version);
            }

            //Set loaded rebate file
            this.rebateFile = rebateFile;

            //Set file name on text field
            frames.tfRebateFileName.setText(rebateFile.getName());

        } catch (Exception e) {
            message.showErrorMessage(propertyUtil.getLangText("Error.Rebate.Open") + "\n" + e.getMessage(), "");
            e.printStackTrace();
        }
    }
    //Set rebate group items by selected group name
    public void setRebateGroupItems(String groupName) {
        List<String> groupItems = groupUtil.getGroupList(groupName);
        while (frames.tmRebateGroups.getRowCount() > 0) {
            frames.tmRebateGroups.removeRow(0);
        }

        for (String groupItem : groupItems) {
            MaterialGroup materialGroup = customRebateMap.get(groupItem);

            //set default values, if not loaded from xml
            if (materialGroup == null) {
                frames.tmRebateGroups.addRow(new Object[] { groupItem, "", "" });
            } else {
                frames.tmRebateGroups.addRow(new Object[] { materialGroup.getName(), materialGroup.getText(),
                                                            materialGroup.getCustomRebate() });
            }

        }

    }

    public boolean isSelectedNodeProfileInRebateTree() {
        CheckNode selectedNode = (CheckNode) frames.rebateTree.getLastSelectedPathComponent();
        boolean isProfile = false;
        //Child contains xml
        if (selectedNode.getChildCount() > 0) {
            if (((CheckNode) selectedNode.getChildAt(0)).getTitle()
                                                        .toLowerCase()
                                                        .endsWith("xml")) {
                isProfile = true;
            }
        }
        //Node title contains xml
        if (selectedNode.getTitle()
                        .toLowerCase()
                        .endsWith("xml")) {
            isProfile = true;
        }
        //Check root
        if (!selectedNode.isRoot() && !((CheckNode) selectedNode.getParent()).isRoot()) {
            isProfile = true;
        }

        return isProfile;
    }

    //Build root for rebate modul
    public CheckNode buildRebateRoot(CheckNode root) {
        defaultUserNode = null;
        defaultProfileNode = null;
        root.removeAllChildren();
        root.setImageIconDefault(new ImageIcon(swc.iconPath + ("Profiles.jpg")));
        root.setImageIconClosed(new ImageIcon(swc.iconPath + ("Profiles.jpg")));
        root.setImageIconOpened(new ImageIcon(swc.iconPath + ("Profiles.jpg")));

        //Create root user to show admin rebate files

        CheckNode adminUserNode = new CheckNode(swc.nodeRabateAdmin);
        adminUserNode.setSelectionMode(CheckNode.SINGLE_SELECTION);
        adminUserNode.setCheckBoxEnabled(false);
        adminUserNode.setImageIconDefault(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
        adminUserNode.setImageIconClosed(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
        adminUserNode.setImageIconOpened(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
        //adminUserNode.setFontColor(Color.RED);

        File rebateDirectory = new File(swc.RebatePath);
        String[] fileList = rebateDirectory.list();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].toString().endsWith(".xml")) {
                CheckNode rebateNode = new CheckNode(fileList[i].toString());
                rebateNode.setCheckBoxEnabled(false);
                adminUserNode.add(rebateNode);
            }
        }

        //Add admin root, if admin is loged in
        if (adminUtil.isAdminLoggedIn())
            root.add(adminUserNode);

        //Get users
        List<String> userList = userUtil.getUserList();

        for (String user : userList) {

            CheckNode userNode = new CheckNode(user);
            userNode.setSelectionMode(CheckNode.SINGLE_SELECTION);
            userNode.setCheckBoxEnabled(false);
            userNode.setImageIconDefault(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
            userNode.setImageIconClosed(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
            userNode.setImageIconOpened(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));

            //Add user to tree if admin is logged in, or the user node belongs to the user
            if (adminUtil.isAdminLoggedIn() || userUtil.getUserName()
                                                       .toLowerCase()
                                                       .equalsIgnoreCase(userNode.getTitle().toLowerCase())) {
                root.add(userNode);
            }

            //Get profiles by users
            List<String> profileList = userUtil.getProfileList(user);
            for (String profile : profileList) {
                CheckNode profileNode = new CheckNode(profile);
                profileNode.setCheckBoxEnabled(false);
                profileNode.setSelectionMode(CheckNode.SINGLE_SELECTION);
                profileNode.setImageIconDefault(new ImageIcon(swc.iconPath + ("TreeProfile.jpg")));
                profileNode.setImageIconClosed(new ImageIcon(swc.iconPath + ("TreeProfile.jpg")));
                profileNode.setImageIconOpened(new ImageIcon(swc.iconPath + ("TreeProfile.jpg")));
                profileNode.setCheckBoxGroup(true);

                /*Profiles can log in to another one, if they are under the same user, so its deprected

				//Add profile to tree if admin is logged in, or the user node belongs to the user
				if (adminUtil.isLoggedIn() || profileUtil.getProfileName().toLowerCase().equals(profileNode.getTitle().toLowerCase())) {
					userNode.add(profileNode);
				}
				*/

                userNode.add(profileNode);

                //Get rebate file names
                List<String> rebateList = profileUtil.getProfileRebateFileNames(profile);
                for (String rebate : rebateList) {
                    CheckNode rebateNode = new CheckNode(rebate);
                    rebateNode.setImageIconDefault(null);
                    rebateNode.setCheckBoxManualSelection(true);

                    //Set checkbox default value
                    rebateNode.setCheckBoxSelected(profileUtil.getProfileDefaultRebateFilName(profile)
                                                              .toLowerCase()
                                                              .equalsIgnoreCase(rebate.toLowerCase()));

                    profileNode.add(rebateNode);
                }

                if (user.equals(userUtil.getUserName()) && profile.equals(profileUtil.getProfileName())) {
                    this.defaultProfileNode = profileNode;
                }
            }

            if (user.equals(userUtil.getUserName())) {
                this.defaultUserNode = userNode;
            }
        }
        return root;
    }


    public String extend(String text, int length) {
        StringBuffer bText = new StringBuffer(text + " ");
        for (int i = 0; i < length; i++) {
            bText.append(" ");
        }
        text = bText.toString().substring(0, length);

        return text;
    }

    public void saveRebate(File RebateFile, String ProfileName) {
        //Check and save custom price list

        if (!isCustomPriceListInputError() && !isRebateInputError()) {
            try {
                //Remove hashmap values to set net values by custom pricelist
                removeAllCustomPriceListItems();
                //Remove hashmap values to set % values by rebate list
                removeAllRebateItems();
                //Create xml
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                Element rootElement = document.createElement("Rebate");
                Element rootElementCustomPriceList = document.createElement("CustomPriceList");
                Element rootElementRebateList = document.createElement("RebateList");
                Element rootElementRebateInfo = document.createElement("RebateInfo");
                document.appendChild(rootElement);
                rootElement.appendChild(rootElementCustomPriceList);
                rootElement.appendChild(rootElementRebateList);
                rootElement.appendChild(rootElementRebateInfo);


                DefaultTableModel defaultTabelModel = (DefaultTableModel) frames.jtCustomPriceList.getModel();
                //Custom price list
                for (int i = 0; i < defaultTabelModel.getRowCount(); i++) {
                    String SAP = defaultTabelModel.getValueAt(i, 0).toString();
                    String Text = defaultTabelModel.getValueAt(i, 1).toString();
                    String Net = defaultTabelModel.getValueAt(i, 2)
                                                  .toString()
                                                  .replace(",", ".")
                                                  .trim();
                    Element itemElement = document.createElement("PriceListItem");
                    Element sapElement = document.createElement("SAP");
                    Element textElement = document.createElement("Text");
                    Element netElement = document.createElement("Net");

                    //Add element to custom pricelist map
                    customPriceListMap.put(SAP, Net);

                    //Create item
                    rootElementCustomPriceList.appendChild(itemElement);
                    //Create SAP
                    sapElement.setTextContent(SAP);
                    itemElement.appendChild(sapElement);
                    //Create Text
                    textElement.setTextContent(Text);
                    itemElement.appendChild(textElement);
                    //Create Net
                    netElement.setTextContent(Net);
                    itemElement.appendChild(netElement);
                }

                defaultTabelModel = (DefaultTableModel) frames.jtRebate.getModel();
                frames.jtRebate.editCellAt(0, 0);

                //Rebate list
                for (int i = 0; i < defaultTabelModel.getRowCount(); i++) {
                    String groupName = defaultTabelModel.getValueAt(i, 0).toString();
                    String rebate = defaultTabelModel.getValueAt(i, 1)
                                                     .toString()
                                                     .trim()
                                                     .replace(",", ".");
                    Element rebateItemElement = document.createElement("RebateItem");
                    Element groupItemsElement = document.createElement("GroupItems");
                    Element groupElement = document.createElement("Group");
                    Element rebateElement = document.createElement("Rebate");

                    //Add element to rebate map
                    rebateMap.put(groupName, rebate);

                    //Create item
                    rootElementRebateList.appendChild(rebateItemElement);
                    //Create group
                    groupElement.setTextContent(groupName);
                    rebateItemElement.appendChild(groupElement);
                    //Create rebate
                    rebateElement.setTextContent(rebate);
                    rebateItemElement.appendChild(rebateElement);
                    //Create rebate group items
                    rebateItemElement.appendChild(groupItemsElement);

                    List<String> groupItems = groupUtil.getGroupList(groupName);
                    for (String groupItem : groupItems) {
                        Element groupItemElement = document.createElement("GroupItem");

                        MaterialGroup materialGroup = customRebateMap.get(groupItem);

                        if (materialGroup != null) {
                            groupItemElement.setAttribute("name", materialGroup.getName());
                            groupItemElement.setAttribute("text", materialGroup.getText());
                            groupItemElement.setAttribute("rebate", materialGroup.getCustomRebate()
                                                                                 .toString()
                                                                                 .trim()
                                                                                 .replace(",", "."));
                            groupItemsElement.appendChild(groupItemElement);
                        }
                    }

                }

                // Set info
                rootElementRebateInfo.setTextContent(frames.jtRebateInfo
                                                           .getText()
                                                           .trim());

                //Save
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                StreamResult result = new StreamResult(RebateFile);

                transformer.transform(source, result);

            } catch (Exception e) {
                message.showErrorMessage(propertyUtil.getLangText("Error.Rebate.Save") + "\n" + e.getMessage(), "");
                e.printStackTrace();
            }
        }
    }

    public void modifyGroupItem(String groupItemName, String text, String rebate) {

        MaterialGroup materialGroup = customRebateMap.get(groupItemName);

        if (materialGroup == null)
            materialGroup = new MaterialGroup();
        if (text == null)
            text = "";

        materialGroup.setName(groupItemName);
        materialGroup.setText(text);
        materialGroup.setCustomRebate(rebate);

        customRebateMap.put(groupItemName, materialGroup);

    }

    //Get default rebate by group name
    public Double getRebateByGroupName(String groupName) {
        try {


            return Double.parseDouble(rebateMap.get(groupName)
                                               .toString()
                                               .trim());
        } catch (Exception e) {
            System.out.println("Error getting rebate by group name '" + groupName + "'\n" + e.getMessage());
            return 0.0;
        }
    }

    //Get custom rebate by group (return -1 if not found, or in case of error)
    public Double getCustomRebateByGroup(String group) {
        try {
            MaterialGroup materialGroup = customRebateMap.get(group);
            if (materialGroup == null)
                return -1.0;
            else {
                return Double.parseDouble(materialGroup.getCustomRebate());
            }
        } catch (Exception e) {
            return -1.0;
        }
    }


    //Get custom price by SAP
    public String getCustomPriceBySAP(String SAP) {
        return customPriceListMap.get(SAP);
    }

    public CheckNode getDefaultUserNode() {
        return this.defaultUserNode;
    }

    public CheckNode getDefaultProfileNode() {
        return this.defaultProfileNode;
    }

    //Get loaded rebate file
    public File getRebateFile() {
        return rebateFile;
    }
}
