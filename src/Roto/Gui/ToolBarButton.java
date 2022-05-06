package Roto.Gui;

import java.awt.*;

import javax.swing.*;

import Roto.Basic.SwingConstans;

import Roto.Utils.PropertyUtil;

public class ToolBarButton extends JToggleButton {

    private static final long serialVersionUID = -7701924331884610011L;

    private final Insets margins = new Insets(0, 0, 0, 0);
    private SwingConstans swc;
    private PropertyUtil propertyUtil;
    private String imageText;

    public ToolBarButton(String imagePath) {
        init(imagePath, null, null, null);
    }

    public ToolBarButton(String imagePath, String text) {
        init(imagePath, text, null, null);
    }

    public ToolBarButton(String imagePath, String text, String toolTipTextCode) {
        init(imagePath, text, toolTipTextCode, null);
    }

    public ToolBarButton(String imagePath, String text, Color bgColor) {
        init(imagePath, text, null, bgColor);
    }

    public ToolBarButton(String imagePath, String text, String toolTipTextCode, Color bgColor) {
        init(imagePath, text, toolTipTextCode, bgColor);
    }

    private void init(String imagePath, String text, String toolTipTextCode, Color bgColor) {
        swc = SwingConstans.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        setIcon(new ImageIcon(swc.iconPath + imagePath));
        setText(text);
        setBackground(bgColor);
        if (toolTipTextCode != null) {
            setToolTipText(propertyUtil.getLangText(toolTipTextCode));
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (imageText != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(swc.fontCanvas);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawString(imageText, 1, this.getFont().getSize() - 2);
        }
    }

    public void setEnabledAndSelected(boolean value) {
        //If not enabled, set selected false
        if (!value)
            setSelected(value);
        setEnabled(value);
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
        this.repaint();
    }


}

