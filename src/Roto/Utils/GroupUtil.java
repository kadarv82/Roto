package Roto.Utils;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultListModel;

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

import Roto.Material.MaterialGroup;

public class GroupUtil {
    private static GroupUtil instance = null;
    private SwingConstans swc;
    private MessageUtil message;
    private PropertyUtil propertyUtil;
    //Store the data modell for key <group name (99)> and value <material group object>
    private HashMap<String, MaterialGroup> materialGroupMap;
    //Store only the key <group name (99)> and value <group (C101)>
    private HashMap<String, String> groupMap;
    private DefaultListModel groupListModel;

    public static GroupUtil getInstance() {
        if (instance == null) {
            instance = new GroupUtil();
        }
        return instance;
    }

    public GroupUtil() {
        swc = SwingConstans.getInstance();
        message = MessageUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        groupMap = new HashMap<String, String>();
        materialGroupMap = new HashMap<String, MaterialGroup>();
    }

    public String getGroupNameByGroup(String group) {
        return groupMap.get(group);
    }

    public void saveGroups() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("Roto");
            document.appendChild(rootElement);

            for (int i = 0; i < groupListModel.size(); i++) {
                String groupName = groupListModel.get(i).toString();

                Element MaterialGroupElement = document.createElement("MaterialGroup");
                rootElement.appendChild(MaterialGroupElement);
                Element NameElement = document.createElement("Name");
                NameElement.setTextContent(groupName);
                MaterialGroupElement.appendChild(NameElement);

                DefaultListModel dlGroups = getGroupListModel(groupName);
                for (int y = 0; y < dlGroups.size(); y++) {
                    String group = dlGroups.get(y).toString();

                    Element GroupElement = document.createElement("Group");
                    GroupElement.setTextContent(group);
                    MaterialGroupElement.appendChild(GroupElement);
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(swc.MaterialGroupPath));

            transformer.transform(source, result);

            message.showInformationMessage(propertyUtil.getLangText("Information.GroupName.Save.Ready"));

        } catch (Exception e) {
            message.showErrorMessage(propertyUtil.getLangText("Error.Group.Save.Error"), "");
        }
    }

    public void loadGroups() {
        try {
            groupListModel = new DefaultListModel();
            List<String> groupNames = new ArrayList<String>();
            File groupFile = new File(swc.MaterialGroupPath);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(groupFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("Anyagcsoport");
            if (nodeList.getLength() == 0) {
                nodeList = doc.getElementsByTagName("MaterialGroup");
            }
            String groupName = null;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NodeList childNodeList = node.getChildNodes();
                for (int y = 0; y < childNodeList.getLength(); y++) {
                    Node childNode = childNodeList.item(y);
                    if (childNode.getNodeName().equals("Nev") || childNode.getNodeName().equals("Name")) {
                        groupName = childNode.getTextContent().trim();
                        groupNames.add(groupName);
                        //groupListModel.addElement(groupName);
                    } else if (childNode.getNodeName().equals("Csoport") || childNode.getNodeName().equals("Group")) {

                        groupMap.put(childNode.getTextContent().trim(), groupName);

                        MaterialGroup materialGroup = new MaterialGroup();
                        materialGroup.setName(childNode.getTextContent().trim());
                        materialGroupMap.put(childNode.getTextContent().trim(), materialGroup);
                    }
                }

            }

            //Sort list
            //   Collections.sort(groupNames);

            //Add items to model
            for (String group : groupNames) {
                groupListModel.addElement(group);
            }

        } catch (Exception e) {
            message.showErrorMessage(propertyUtil.getLangText("Error.Group.Load.Error"), "");
        }
    }

    public boolean isGroupExist(String group) {
        if (groupMap.containsKey(group)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGroupNameExist(String groupName) {
        if (groupMap.containsValue(groupName)) {
            return true;
        } else {
            return false;
        }
    }

    public void addGroupName(String groupName) {
        groupListModel.addElement(groupName);
    }

    public void addGroup(String groupName, String newGroup) {
        groupMap.put(newGroup, groupName);

        MaterialGroup materialGroup = new MaterialGroup();
        materialGroup.setName(newGroup);
        materialGroupMap.put(newGroup, materialGroup);

    }

    public void removeGroup(String group) {
        groupMap.remove(group);
        materialGroupMap.remove(group);
    }

    public void modifyGroup(String groupName, String groupToModify, String newGroup) {
        removeGroup(groupToModify);
        addGroup(groupName, newGroup);
    }

    public void removeGroupName(String groupName) {
        Set ref = groupMap.keySet();
        Iterator it = ref.iterator();
        String key = "";

        Vector<String> vKeys = new Vector<String>();
        while (it.hasNext()) {
            Object o = it.next();
            if (groupMap.get(o).equals(groupName)) {
                key = String.valueOf(o);
                vKeys.add(key);
            }
        }
        for (int i = 0; i < vKeys.size(); i++) {
            removeGroup(vKeys.get(i).toString());
        }
    }

    public void modifyGroupName(String groupName, String newGroupName) {
        Set ref = groupMap.keySet();
        Iterator it = ref.iterator();
        String key = "";

        while (it.hasNext()) {
            Object o = it.next();
            if (groupMap.get(o).equals(groupName)) {
                key = String.valueOf(o);
                groupMap.put(key, newGroupName);
                //List modified in action !
            }
        }
    }

    public DefaultListModel getGroupNameListModel() {
        return groupListModel;
    }

    public DefaultListModel getGroupListModel(String groupName) {
        DefaultListModel groupListModel = new DefaultListModel();
        Set<String> ref = groupMap.keySet();
        Iterator<String> it = ref.iterator();
        String key = "";
        List<String> groupItems = new ArrayList<String>();
        while (it.hasNext()) {
            Object o = it.next();
            if (groupMap.get(o).equals(groupName)) {
                key = String.valueOf(o);
                groupItems.add(key);
            }
        }

        //Sort list
        Collections.sort(groupItems);

        //Add items to model
        for (String group : groupItems) {
            groupListModel.addElement(group);
        }

        return groupListModel;
    }

    public String getGroupListAsString(String groupName, String separator) {
        String groupList = "";
        Set<String> ref = groupMap.keySet();
        Iterator<String> it = ref.iterator();
        String key = "";
        while (it.hasNext()) {
            Object o = it.next();
            if (groupMap.get(o).equals(groupName)) {
                key = String.valueOf(o);
                groupList += key + separator;
            }
        }
        if (groupList.length() > 0) {
            groupList = groupList.substring(0, groupList.length() - separator.length());
        }
        return groupList;
    }

    public List<String> getGroupList(String groupName) {
        Set<String> ref = groupMap.keySet();
        Iterator<String> it = ref.iterator();
        String key = "";
        List<String> groupList = new ArrayList<String>();
        while (it.hasNext()) {
            Object o = it.next();
            if (groupMap.get(o).equals(groupName)) {
                key = String.valueOf(o);
                groupList.add(key);
            }
        }

        //Sort list
        Collections.sort(groupList);

        return groupList;
    }
}
