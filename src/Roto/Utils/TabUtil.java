package Roto.Utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import Roto.Gui.JRFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TabUtil implements ActionListener, WindowListener {
    private static TabUtil instance = null;
    private HashMap<Integer, Component> componentList;
    private HashMap<Integer, JTabbedPane> tabbedPaneList;
    private HashMap<Integer, Boolean> isTabInFrameList;
    private HashMap<Integer, JRFrame> frameList;
    private PropertyUtil propertyUtil;
    private boolean isDragging;
    private int moved = 0;

    public static TabUtil getInstance() {
        if (instance == null) {
            instance = new TabUtil();
        }
        return instance;
    }

    public TabUtil() {
        componentList = new HashMap<Integer, Component>();
        tabbedPaneList = new HashMap<Integer, JTabbedPane>();
        isTabInFrameList = new HashMap<Integer, Boolean>();
        frameList = new HashMap<Integer, JRFrame>();
        propertyUtil = PropertyUtil.getInstance();
    }

    public void moveTabToFrame(JTabbedPane tabbedPane, int tabIndex, String frameName, String Title, Point location) {
        if (!isTabInWindow(tabIndex)) {

            String actionCommand = "moveback_" + frameName + "_" + tabIndex;
            JButton btMoveBack = new JButton();
            Component component = tabbedPane.getComponentAt(tabIndex);
            componentList.put(tabIndex, component);
            tabbedPaneList.put(tabIndex, tabbedPane);

            int selectedIndex = tabbedPane.getSelectedIndex();
            tabbedPane.setSelectedIndex(tabIndex);

            //Add items to tabbedpane
            JPanel pnTab = new JPanel();
            btMoveBack = new JButton(propertyUtil.getLangText("Frame.Tab.Reset"));


            btMoveBack.addActionListener(this);
            btMoveBack.setActionCommand(actionCommand);

            pnTab.setLayout(new FormLayout("center:default:grow", "default:grow"));
            pnTab.add(btMoveBack, new CellConstraints().xy(1, 1));

            tabbedPane.setComponentAt(tabIndex, pnTab);
            tabbedPane.getComponentAt(tabIndex).setEnabled(false);

            //Create frame
            JRFrame frame = new JRFrame(frameName);
            frame.setSize(component.getWidth(), component.getHeight());
            frame.setLocation(location.x - frame.getWidth() / 2, location.y);
            frame.setLayout(new BorderLayout());
            frame.setTitle(Title);
            frame.setName(actionCommand);
            frame.add(component);
            frame.setVisible(true);
            frame.addWindowListener(this);

            frameList.put(tabIndex, frame);

            tabbedPane.setSelectedIndex(selectedIndex);

            isTabInFrameList.put(tabIndex, true);
        }

    }

    public void resetTab(String cmd) {

        StringTokenizer tokenizer = new StringTokenizer(cmd, "_");
        tokenizer.nextToken();
        String frameName = tokenizer.nextToken();
        String sIndex = tokenizer.nextToken();
        int tabIndex = Integer.parseInt(sIndex);

        JTabbedPane tabbedPane = tabbedPaneList.get(tabIndex);
        Component component = componentList.get(tabIndex);
        tabbedPane.setComponentAt(tabIndex, component);
        propertyUtil.getFrameMap()
                    .get(frameName)
                    .setVisible(false);

        isTabInFrameList.put(tabIndex, false);
        frameList.remove(tabIndex);
    }

    public boolean isTabInWindow(int tabIndex) {
        if (isTabInFrameList.get(tabIndex) != null) {
            return isTabInFrameList.get(tabIndex);
        } else {
            return false;
        }
    }

    public void actionPerformed(ActionEvent ev) {
        if (ev.getActionCommand().contains("moveback")) {
            String cmd = ev.getActionCommand();
            resetTab(cmd);
        }

    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean isDragging) {
        this.isDragging = isDragging;
    }

    public int getMoved() {
        return moved;
    }

    public void setMoved(int moved) {
        this.moved = moved;
    }

    public HashMap<Integer, JRFrame> getFrameList() {
        return frameList;
    }

    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowClosing(WindowEvent ev) {
        if (ev.getSource().getClass() == JRFrame.class) {
            JRFrame frame = (JRFrame) ev.getSource();
            resetTab(frame.getName());
        }

    }

    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

}
