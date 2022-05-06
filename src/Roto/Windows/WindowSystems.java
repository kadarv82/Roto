package Roto.Windows;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

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

import Roto.Utils.MessageUtil;
import Roto.Utils.PropertyUtil;

public class WindowSystems {
    private static WindowSystems instance = null;
    private MessageUtil message;
    private PropertyUtil propertyUtil;
    private SwingConstans swc;

    public static WindowSystems getInstance() {
        if (instance == null) {
            instance = new WindowSystems();
        }
        return instance;
    }

    private List<WindowSystem> windowSystemList;

    public WindowSystems() {
        windowSystemList = new ArrayList<WindowSystem>();
        message = MessageUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        swc = SwingConstans.getInstance();
    }

    public void addWinowSystem(WindowSystem windowSystem) {
        windowSystemList.add(windowSystem);
    }

    public void removeWindowSystem(String typeName) {
        WindowSystem windowSystem = getWindowSystemByTypeName(typeName);
        windowSystemList.remove(windowSystem);
    }

    public Boolean modifyWindowSytem(String typeName, String systemName, WindowTableModel windowTableModel) {
        if (isSystemTypeExit(typeName)) {
            WindowSystem windowSystem = getWindowSystemByTypeName(typeName);

            windowSystem.setWindowTableModel(windowTableModel);
            windowSystem.setSystemName(systemName);
            windowSystem.setSystemType(typeName);

            return true;
        } else {
            return false;
        }
    }

    public WindowSystem getWindowSystemBySystemName(String systemName) {
        WindowSystem system = null;
        for (WindowSystem windowSystem : windowSystemList) {
            if (windowSystem.getSystemName()
                            .toLowerCase()
                            .trim()
                            .equals(systemName.toLowerCase().trim())) {
                system = windowSystem;
            }
        }
        return system;
    }

    public List<String> getAllWindowSystemTypes() {
        List<String> typeList = new ArrayList<String>();
        for (WindowSystem windowSystem : windowSystemList) {
            typeList.add(windowSystem.getSystemType());
        }
        return typeList;
    }

    public WindowSystem getWindowSystemByTypeName(String typeName) {
        WindowSystem system = null;
        for (WindowSystem windowSystem : windowSystemList) {
            if (windowSystem.getSystemType()
                            .toLowerCase()
                            .trim()
                            .equals(typeName.toLowerCase().trim())) {
                system = windowSystem;
            }
        }
        return system;
    }

    public Boolean isSystemNameExit(String systemName) {
        if (getWindowSystemBySystemName(systemName) != null)
            return true;
        else
            return false;
    }

    public Boolean isSystemTypeExit(String typeName) {
        if (getWindowSystemByTypeName(typeName) != null)
            return true;
        else
            return false;
    }

    public void clearList() {
        windowSystemList.clear();
        windowSystemList = new ArrayList<WindowSystem>();
        Frames.getInstance()
              .jcbWinowTypes
              .removeAllItems();
    }

    public void load(String filePath) {
        try {
            //Clear list
            clearList();

            //Create xml document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath);
            doc.getDocumentElement().normalize();

            //Create nodelists
            NodeList windowSystemList = doc.getElementsByTagName("WindowSystem");
            for (int i = 0; i < windowSystemList.getLength(); i++) {

                NodeList windowSystemItemList = windowSystemList.item(i).getChildNodes();

                //Create windowSystem object
                WindowSystem windowSystem = new WindowSystem();

                //Iterate on window systems
                for (int j = 0; j < windowSystemItemList.getLength(); j++) {

                    Node windowSystemNode = windowSystemItemList.item(j);

                    if (windowSystemNode.getNodeName().equals("SystemClass")) {
                        windowSystem.setSystemName(windowSystemNode.getTextContent());
                    }
                    if (windowSystemNode.getNodeName().equals("SystemType")) {
                        windowSystem.setSystemType(windowSystemNode.getTextContent());
                    }

                    if (windowSystemNode.getNodeName().equals("SystemData")) {
                        //Iterate on system data
                        NodeList windowSystemDataList = windowSystemNode.getChildNodes();
                        for (int k = 0; k < windowSystemDataList.getLength(); k++) {
                            Node dataNode = windowSystemDataList.item(k);
                            int row = Integer.parseInt(dataNode.getAttributes()
                                                               .getNamedItem("x")
                                                               .getTextContent());
                            int col = Integer.parseInt(dataNode.getAttributes()
                                                               .getNamedItem("y")
                                                               .getTextContent());

                            Object data = dataNode.getTextContent();
                            if (data.toString().equals("true") || data.toString().equals("false")) {
                                data = Boolean.valueOf(String.valueOf(data));
                            }

                            windowSystem.getWindowTableModelArray()[row][col] = data;
                        }
                    }

                    if (windowSystemNode.getNodeName().equals("SystemConditionsData")) {
                        //Iterate on system conditions data
                        NodeList windowSystemDataList = windowSystemNode.getChildNodes();
                        for (int k = 0; k < windowSystemDataList.getLength(); k++) {
                            Node dataNode = windowSystemDataList.item(k);
                            int row = Integer.parseInt(dataNode.getAttributes()
                                                               .getNamedItem("x")
                                                               .getTextContent());
                            int col = Integer.parseInt(dataNode.getAttributes()
                                                               .getNamedItem("y")
                                                               .getTextContent());

                            Object data = dataNode.getTextContent();
                            if (data.toString().equals("true") || data.toString().equals("false")) {
                                data = Boolean.valueOf(String.valueOf(data));
                            }

                            windowSystem.getWindowLimitTableModel().setValueAt(data, row, col);
                        }
                    }

                }

                //Add windowSystem object to list
                addWinowSystem(windowSystem);
                Frames.getInstance()
                      .jcbWinowTypes
                      .addItem(windowSystem.getSystemType());

            }

        } catch (Exception e) {
            message.showErrorMessage(propertyUtil.getLangText("Error.WindowSystems.Open") + "\n" + e.getMessage(), "");
        }
    }

    public void save(String filePath) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("WindowSystems");

            for (WindowSystem windowSystem : windowSystemList) {

                Element rootElementWindowSystem = document.createElement("WindowSystem");
                rootElement.appendChild(rootElementWindowSystem);

                Element elementSystemClass = document.createElement("SystemClass");
                elementSystemClass.setTextContent(windowSystem.getSystemName());
                rootElementWindowSystem.appendChild(elementSystemClass);

                Element elementSystemType = document.createElement("SystemType");
                elementSystemType.setTextContent(windowSystem.getSystemType());
                rootElementWindowSystem.appendChild(elementSystemType);

                Element elementData = document.createElement("SystemData");
                rootElementWindowSystem.appendChild(elementData);

                Object[][] dataArray = windowSystem.getWindowTableModelArray();

                for (int i = 0; i < swc.windowTypeArray.length; i++) {
                    for (int j = 0; j < 7; j++) {
                        Element elementDataItem = document.createElement("Item");
                        elementDataItem.setAttribute("x", String.valueOf(i));
                        elementDataItem.setAttribute("y", String.valueOf(j));
                        Object value = dataArray[i][j];
                        elementDataItem.setTextContent(String.valueOf(value));
                        elementData.appendChild(elementDataItem);
                    }
                }

                elementData = document.createElement("SystemConditionsData");
                rootElementWindowSystem.appendChild(elementData);

                dataArray = windowSystem.getWindowLimitTableModelArray(true);

                for (int row = 0; row < swc.windowTypeArray.length - 2; row++) {
                    for (int col = 0; col < 9; col++) {
                        Element elementDataItem = document.createElement("Item");
                        elementDataItem.setAttribute("x", String.valueOf(row));
                        elementDataItem.setAttribute("y", String.valueOf(col));
                        Object value = dataArray[row][col];
                        elementDataItem.setTextContent(String.valueOf(value));
                        elementData.appendChild(elementDataItem);
                    }
                }

            }

            document.appendChild(rootElement);

            //Save
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

        } catch (Exception e) {
            message.showErrorMessage(propertyUtil.getLangText("Error.WindowSystems.Save") + "\n" + e.getMessage(), "");
        }
    }
}
