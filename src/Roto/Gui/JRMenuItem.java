package Roto.Gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import Roto.Utils.PropertyUtil;

public class JRMenuItem extends JMenuItem {
    private static final long serialVersionUID = -1498936375510055178L;

    public JRMenuItem(String textCode) {
        this(textCode, null);
    }

    public JRMenuItem(String TextCode, String ImagePath) {
        PropertyUtil propertyUtil = PropertyUtil.getInstance();
        setText(propertyUtil.getLangText(TextCode));
        setIcon(new ImageIcon("Bin/etc/Images/Icon/" + ImagePath));
        setBackground(Color.WHITE);
    }
}
