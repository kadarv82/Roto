package Roto.Gui.Tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

import Roto.Basic.SwingConstans;

public class CheckRenderer extends JPanel implements TreeCellRenderer {
    private static final long serialVersionUID = 3523182000059940961L;
    protected JCheckBox check;
    protected TreeLabel label;
    private SwingConstans swc;

    public CheckRenderer() {
        swc = SwingConstans.getInstance();
        setLayout(null);
        check = new JCheckBox();
        add(check);
        add(label = new TreeLabel());
        check.setBackground(UIManager.getColor("Tree.textBackground"));
        label.setForeground(UIManager.getColor("Tree.textForeground"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,

                                                  boolean isSelected, boolean expanded, boolean leaf, int row,

                                                  boolean hasFocus) {

        String stringValue = tree.convertValueToText(value, isSelected, expanded, leaf, row, hasFocus);
        setEnabled(tree.isEnabled());
        CheckNode checkNode = (CheckNode) value;

        //Auto check selection by selecting the node
        if (!checkNode.isCheckBoxManualSelection()) {
            check.setSelected(checkNode.isSelected());
        }
        //Manual selection (setCheckBoxSelected have to be used)
        else {
            check.setSelected(checkNode.isCheckBoxSelected());
        }

        //Hide checkbox if needed
        if (!checkNode.isCheckBoxEnabled()) {
            check.setVisible(false);
        } else {
            check.setVisible(true);
        }

        if (checkNode.getFontColor() != null)
            label.setForeground(checkNode.getFontColor());

        label.setFont(swc.fontTree);
        label.setText(stringValue);
        label.setSelected(isSelected);
        label.setFocus(hasFocus);

        if (leaf) {
            label.setIcon(UIManager.getIcon("Tree.leafIcon"));
            if (checkNode.getImageIconDefault() != null)
                label.setIcon(checkNode.getImageIconDefault());
        } else if (expanded) {
            label.setIcon(UIManager.getIcon("Tree.openIcon"));
            if (checkNode.getImageIconOpened() != null)
                label.setIcon(checkNode.getImageIconOpened());
        } else {
            label.setIcon(UIManager.getIcon("Tree.closedIcon"));
            if (checkNode.getImageIconClosed() != null)
                label.setIcon(checkNode.getImageIconClosed());
        }

        return this;
    }

    public Dimension getPreferredSize() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();
        return new Dimension(d_check.width + d_label.width,
                             (d_check.height < d_label.height ? d_label.height : d_check.height));
    }

    public void doLayout() {
        Dimension d_check = check.getPreferredSize();
        Dimension d_label = label.getPreferredSize();

        int y_check = 0;
        int y_label = 0;
        if (d_check.height < d_label.height) {
            y_check = (d_label.height - d_check.height) / 2;
        } else {
            y_label = (d_check.height - d_label.height) / 2;

        }
        check.setLocation(0, y_check);
        check.setBounds(0, y_check, d_check.width, d_check.height);
        label.setLocation(check.isVisible() ? d_check.width : 0, y_label);
        label.setBounds(check.isVisible() ? d_check.width : 0, y_label, d_label.width, d_label.height);
    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
    }

    public class TreeLabel extends JLabel {
        boolean isSelected;
        boolean hasFocus;

        public TreeLabel() {

        }

        public void setBackground(Color color) {
            if (color instanceof ColorUIResource)
                color = null;
            super.setBackground(color);
        }

        public void paint(Graphics g) {
            String str;
            if ((str = getText()) != null) {
                if (0 < str.length()) {
                    if (isSelected) {
                        g.setColor(UIManager.getColor("Tree.selectionBackground"));
                    } else {
                        g.setColor(UIManager.getColor("Tree.textBackground"));
                    }

                    Dimension d = getPreferredSize();
                    int imageOffset = 0;
                    Icon currentI = getIcon();

                    if (currentI != null) {

                        imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);

                    }

                    g.fillRect(imageOffset, 0, d.width - 1 - imageOffset, d.height);

                    if (hasFocus) {

                        g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                        g.drawRect(imageOffset, 0, d.width - 1 - imageOffset, d.height - 1);
                    }

                }

            }
            super.paint(g);
        }

        public Dimension getPreferredSize() {
            Dimension retDimension = super.getPreferredSize();
            if (retDimension != null) {
                retDimension = new Dimension(retDimension.width + 3, retDimension.height);
            }
            return retDimension;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public void setFocus(boolean hasFocus) {
            this.hasFocus = hasFocus;
        }

    }

}
