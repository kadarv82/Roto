package Roto.Gui;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import Roto.Basic.Actions;
import Roto.Basic.SwingConstans;
import Roto.Utils.PropertyUtil;

public class JRFrame extends JFrame implements WindowListener {

    private static final long serialVersionUID = -8720627462574015177L;

    private PropertyUtil propertyUtil;
    private Actions actions;
    private String closingCommand;

    public JRFrame() {
        init(null);
    }

    public JRFrame(String Name) {
        init(Name);
    }

    public void init(String frameName) {
        if (frameName != null) {
            setName(frameName);
        }

        actions = Actions.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        loadWindowPosition();
        addWindowListener(this);
        propertyUtil.getFrameMap().put(frameName, this);
        setIconImage(new ImageIcon(SwingConstans.getInstance().iconPath + "Roto.gif").getImage());
    }

    public void loadWindowPosition() {
        try {
            Properties frameProperties = propertyUtil.getFrameProperty();
            int Width = propertyUtil.getIntProperty(frameProperties, "Roto.Frame." + getName() + ".Width");
            int Height = propertyUtil.getIntProperty(frameProperties, "Roto.Frame." + getName() + ".Height");
            int X = propertyUtil.getIntProperty(frameProperties, "Roto.Frame." + getName() + ".X");
            int Y = propertyUtil.getIntProperty(frameProperties, "Roto.Frame." + getName() + ".Y");
            int ExtendedState =
                propertyUtil.getIntProperty(frameProperties, "Roto.Frame." + getName() + ".ExtendedState");
            if (Width > 0) {
                setSize(Width, Height);
                setLocation(X, Y);
            }
            setExtendedState(ExtendedState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveWindowPosition() {
        if (getExtendedState() != JFrame.MAXIMIZED_BOTH) {
            propertyUtil.setFrameProperty("Roto.Frame." + getName() + ".Width", getWidth() + "");
            propertyUtil.setFrameProperty("Roto.Frame." + getName() + ".Height", getHeight() + "");
            propertyUtil.setFrameProperty("Roto.Frame." + getName() + ".X", getX() + "");
            propertyUtil.setFrameProperty("Roto.Frame." + getName() + ".Y", getY() + "");
        }
        propertyUtil.setFrameProperty("Roto.Frame." + getName() + ".ExtendedState", getExtendedState() + "");

        propertyUtil.storeFrameProperty();
    }

    public void setClosingCommand(String command) {
        this.closingCommand = command;
    }

    public void setFrameSize(int width, int height) {
        if (getWidth() == 0 || getHeight() == 0) {
            setSize(width, height);
        }
    }

    public void setFrameLocation(int X, int Y) {
        if (getX() == 0 || getY() == 0) {
            setLocation(X, Y);
        }
    }

    public void windowActivated(WindowEvent arg0) {
    }

    public void windowClosed(WindowEvent arg0) {
    }

    public void windowClosing(WindowEvent arg0) {
        saveWindowPosition();
        if (this.closingCommand != null && this.closingCommand.length() > 0) {
            actions.doAction(this.closingCommand);
        }
    }

    public void windowDeactivated(WindowEvent arg0) {
        saveWindowPosition();
    }

    public void windowDeiconified(WindowEvent arg0) {
    }

    public void windowIconified(WindowEvent arg0) {
    }

    public void windowOpened(WindowEvent arg0) {
    }

}
