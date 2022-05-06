package Roto.Gui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JMenu;

import Roto.Utils.PropertyUtil;

public class JRMenu extends JMenu {

    private static final long serialVersionUID = -2555302717217556284L;

    public JRMenu(String TextCode, String ImagePath) {
        PropertyUtil propertyUtil = PropertyUtil.getInstance();
        setText(propertyUtil.getLangText(TextCode));
        setIcon(new ImageIcon("Bin/etc/Images/Icon/" + ImagePath));
        setBackground(Color.WHITE);
    }

}
