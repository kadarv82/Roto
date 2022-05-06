package Roto.Gui;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Toolkit;

import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import Roto.Basic.SwingConstans;

import Roto.Utils.MessageUtil;
import Roto.Utils.PropertyUtil;
import Roto.Utils.SystemUtil;

public class GuiBuilder {
    Toolkit toolkit;
    Frames frames;
    SwingConstans swc;
    MessageUtil message;
    PropertyUtil propertyUtil;
    SystemUtil systemUtil;
    Viewer viewer, viewerLoading;
    private static GuiBuilder instance = null;

    public GuiBuilder() {
        init();
    }

    public static GuiBuilder getInstance() {
        if (instance == null) {
            instance = new GuiBuilder();
        }
        return instance;
    }

    private void init() {
        setLookAndFeel();
        frames = Frames.getInstance();
        swc = SwingConstans.getInstance();
        message = MessageUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        systemUtil = SystemUtil.getInstance();
        viewer = new Viewer(new ImageIcon(swc.viewerPath + "Start.jpg"), new ImageIcon(swc.viewerPath + "Loading.gif"));
    }

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            JFrame.setDefaultLookAndFeelDecorated(true);

            /*
			for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(laf.getName())) {
					UIManager.setLookAndFeel(laf.getClassName());
				}
			}
			*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Check if java version at least than 1.6
    private void checkVersion() {
        if (!systemUtil.checkVersion()) {
            message.showErrorMessage(propertyUtil.getLangText("Error.Start.Version"), "");
        }
    }

    public void startUp(boolean isStartup) {
        if (isStartup) {
            //viewer.setLocation(300,300);
            viewer.showStartUpImage();
            //viewerLoading.setLocation(300,300);
            int x = viewer.getLocation().x + viewer.getSize().width - 28;
            int y = viewer.getLocation().y + viewer.getSize().height - 28;
        } else {
            viewer.shutDown(2000);
        }
    }

    public void buildGui() {
        startUp(true);

        //Load frame settings
        frames.createMainFrame();
        frames.createFrame_Resize();
        frames.createInternalFrame_Menus();
        frames.createInternalFrame_PriceListView();
        frames.createInternalFrame_PriceListSelect();
        frames.createInternalFrame_MaterialGroups();
        frames.createInternalFrame_DatabaseListEditor();
        frames.createInternalFrame_Rebate();
        frames.createInternalFrame_AdminLogin();
        frames.createInternalFrame_Windows();
        frames.createInternalFrame_CollectionList();
        frames.createInternalFrame_CollectionEdit();
        frames.createInternalFrame_ExcelList();
        frames.createInternalFrame_EditDataBase();
        frames.createInternalFrame_ReplaceData();
        frames.createInternalFrame_Currency();
        frames.createInternalFrame_StockList();
        frames.createInternalFrame_Languages();
        frames.createInternalFrame_LanguagesEdit();
        frames.createInternalFrame_Register();
        frames.createInternalFrame_RegisterInfo();
        frames.createInternalFrame_Correction();
        frames.createInternalFrame_Profiles();
        frames.createInternalFrame_FontSize();
        frames.createInternalFrame_About();

        checkVersion();
        frames.loadStartUpSettings();

        startUp(false);
    }

}
