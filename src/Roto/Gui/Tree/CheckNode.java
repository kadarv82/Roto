package Roto.Gui.Tree;

import java.awt.Color;

import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;



public class CheckNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = 5843511090827493401L;
    public final static int SINGLE_SELECTION = 0;
    public final static int DIG_IN_SELECTION = 4;
    protected int selectionMode;
    protected boolean isSelected;
    protected boolean isCheckBoxSelected;
    private boolean isCheckBoxManualSelection;

    private String title;
    private boolean checkBoxEnabled;
    private boolean checkBoxGroup;
    private ImageIcon imageIconDefault;
    private ImageIcon imageIconOpened;
    private ImageIcon imageIconClosed;
    private Color fontColor;


    public CheckNode() {
        this(null);
    }

    public CheckNode(Object userObject) {
        this(userObject, true, false);
    }

    public CheckNode(Object userObject, boolean allowsChildren, boolean isSelected) {
        super(userObject, allowsChildren);
        this.isSelected = isSelected;
        this.title = userObject.toString();
        this.checkBoxEnabled = true;
        setSelectionMode(DIG_IN_SELECTION);
    }

    public void setSelectionMode(int mode) {
        selectionMode = mode;
    }

    public int getSelectionMode() {
        return selectionMode;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;

        if (parent != null) {

            CheckNode parentNode = (CheckNode) parent;

            //If parent node is group type set children nodes false
            if (parentNode.isCheckBoxGroup()) {
                this.isSelected = true;
                Enumeration e = parent.children();
                while (e.hasMoreElements()) {
                    CheckNode node = (CheckNode) e.nextElement();
                    if (!node.equals(this)) {
                        node.isSelected = false;
                    }
                }
            }
        }

        //Set childrens to same value when openig or closing branch
        /*if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
			Enumeration e = children.elements();
			while (e.hasMoreElements()) {
				CheckNode node = (CheckNode) e.nextElement();
				node.setSelected(isSelected);
			}
		}*/

    }

    public void setCheckBoxSelected(boolean isCheckBoxSelected) {
        this.isCheckBoxSelected = isCheckBoxSelected;

        if (parent != null) {

            CheckNode parentNode = (CheckNode) parent;

            //If parent node is group type set children nodes false
            if (parentNode.isCheckBoxGroup()) {

                this.isCheckBoxSelected = true;

                Enumeration e = parent.children();
                while (e.hasMoreElements()) {
                    CheckNode node = (CheckNode) e.nextElement();
                    if (!node.equals(this)) {
                        node.isCheckBoxSelected = false;
                    }
                }
            }

        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isCheckBoxEnabled() {
        return checkBoxEnabled;
    }

    public void setCheckBoxEnabled(boolean checkBoxEnabled) {
        this.checkBoxEnabled = checkBoxEnabled;
    }

    public boolean isCheckBoxGroup() {
        return checkBoxGroup;
    }

    public void setCheckBoxGroup(boolean checkBoxGroup) {
        this.checkBoxGroup = checkBoxGroup;
    }

    public ImageIcon getImageIconDefault() {
        return imageIconDefault;
    }

    public void setImageIconDefault(ImageIcon imageIconDefault) {
        this.imageIconDefault = imageIconDefault;
    }

    public ImageIcon getImageIconOpened() {
        return imageIconOpened;
    }

    public void setImageIconOpened(ImageIcon imageIconOpened) {
        this.imageIconOpened = imageIconOpened;
    }

    public ImageIcon getImageIconClosed() {
        return imageIconClosed;
    }

    public void setImageIconClosed(ImageIcon imageIconClosed) {
        this.imageIconClosed = imageIconClosed;
    }

    public boolean isCheckBoxSelected() {
        return isCheckBoxSelected;
    }

    public boolean isCheckBoxManualSelection() {
        return isCheckBoxManualSelection;
    }

    public void setCheckBoxManualSelection(boolean isCheckBoxManualSelection) {
        this.isCheckBoxManualSelection = isCheckBoxManualSelection;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    // If you want to change "isSelected" by CellEditor,
    /*
	 public void setUserObject(Object obj) { if (obj instanceof Boolean) {
	
	 setSelected(((Boolean)obj).booleanValue()); } else {
	
	 super.setUserObject(obj); }
	 }*/


}
