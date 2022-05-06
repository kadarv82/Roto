package Roto.Basic;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import Roto.DataBase.DataBaseCollection;
import Roto.DataBase.DataBaseCollectionList;
import Roto.DataBase.DataBaseManager;
import Roto.DataBase.SerializableDataBaseCollection;
import Roto.DataBase.SerializedStockList;
import Roto.DataBase.TextDBRow;

import Roto.Gui.DataBaseTableModel;
import Roto.Gui.DisplayCanvas;
import Roto.Gui.Frames;
import Roto.Gui.Header;
import Roto.Gui.JRFileChooser;
import Roto.Gui.JRFrame;
import Roto.Gui.JRMenuItem;
import Roto.Gui.JRNumberTextField;
import Roto.Gui.ReplaceTableModel;
import Roto.Gui.SingleFileSystemView;
import Roto.Gui.Viewer;
import Roto.Gui.Tree.CheckNode;

import Roto.Print.Printer;

import Roto.Utils.AdminUtil;
import Roto.Utils.EncodedProperties;
import Roto.Utils.ExcelUtil;
import Roto.Utils.ExportThread;
import Roto.Utils.GroupUtil;
import Roto.Utils.IOUtil;
import Roto.Utils.MessageUtil;
import Roto.Utils.MouseUtil;
import Roto.Utils.PriceUtil;
import Roto.Utils.ProfileUtil;
import Roto.Utils.PropertyUtil;
import Roto.Utils.RebateUtil;
import Roto.Utils.RegisterUtil;
import Roto.Utils.TabUtil;
import Roto.Utils.UserUtil;

import Roto.Windows.WindowSystem;
import Roto.Windows.WindowSystems;
import Roto.Windows.WindowTableModel;
import Roto.Windows.WindowTypes;

public class Actions {
    private static Actions instance = null;
    private Frames frames;
    private PropertyUtil propertyUtil;
    private SwingConstans swc;
    private MessageUtil message;
    private ProfileUtil profileUtil;
    private UserUtil userUtil;
    private JRFileChooser fileChooser;
    private DataBaseManager dataBase;
    private MouseUtil mouse;
    private PriceUtil priceUtil;
    private GroupUtil groupUtil;
    private AdminUtil adminUtil;
    private RebateUtil rebateUtil;
    private WindowTypes windowTypes;
    private WindowSystems windowSytems;
    private TabUtil tabUtil;
    private IOUtil ioUtil;
    private ExcelUtil excelUtil;
    private RegisterUtil registerUtil;
    private Printer printer;

    public static Actions getInstance() {
        if (instance == null) {
            instance = new Actions();
        }
        return instance;
    }

    public Actions() {
        frames = Frames.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        swc = SwingConstans.getInstance();
        message = MessageUtil.getInstance();
        profileUtil = ProfileUtil.getInstance();
        userUtil = UserUtil.getInstance();
        dataBase = DataBaseManager.getInstance();
        mouse = MouseUtil.getInstance();
        priceUtil = PriceUtil.getInstance();
        groupUtil = GroupUtil.getInstance();
        adminUtil = AdminUtil.getInstance();
        rebateUtil = RebateUtil.getInstance();
        windowTypes = WindowTypes.getInstance();
        windowSytems = WindowSystems.getInstance();
        tabUtil = TabUtil.getInstance();
        ioUtil = IOUtil.getInstance();
        excelUtil = ExcelUtil.getInstance();
        printer = Printer.getInstance();
        registerUtil = RegisterUtil.getInstance();
    }

    public void setCursor(int CursorType) {
        frames.jrfMain.setCursor(new Cursor(CursorType));
    }

    //List list listeners
    public void doListAction(ListSelectionEvent lse) {
        if (lse.getSource() == frames.jlGroupNames) {
            groupNameSelected();
        } else if (lse.getSource() == frames.jlGroups) {
            groupSelected();
        } else if (lse.getSource() == frames.jlCollectionEdit) {
            collectionListItemSelected();
        }
    }

    //Mouse listeners - pressed
    public void doMousePressedAction(MouseEvent me) {

    }

    //Mouse listeners - dragged
    public void doMouseDraggedAction(MouseEvent me) {
        //Remove tab to window
        if (me.getSource() == frames.mainTabPane) {
            setCursor(Cursor.MOVE_CURSOR);
            tabUtil.setDragging(true);
            tabUtil.setMoved(tabUtil.getMoved() + 1);
        }
    }

    //Mouse listeners - released
    public void doMouseReleasedAction(MouseEvent me) {
        if (me.getSource() == frames.mainTabPane) {
            setCursor(Cursor.DEFAULT_CURSOR);

            if (tabUtil.isDragging() && tabUtil.getMoved() > 5) {
                int index = frames.mainTabPane.getSelectedIndex();

                tabUtil.moveTabToFrame(frames.mainTabPane, index, "tab" + frames.mainTabPane.getTitleAt(index),
                                       frames.mainTabPane.getTitleAt(index), me.getLocationOnScreen());

                tabUtil.setDragging(false);
                tabUtil.setMoved(0);
            }
        } else if (me.getSource() == frames.jtRebate) {
            rebateSelected();
        } else if (me.getSource() == frames.jtRebateGroups) {
            rebateCheckGroupItemTextChangeEnabled();
        }

    }

    //Mouse listeners - entered
    public void doMouseEnteredAction(MouseEvent me) {

    }

    //Mouse listeners - exited
    public void doMouseExitedAction(MouseEvent me) {
        if (me.getSource() == frames.jtRebateGroups && me.getClickCount() == 0) {
            rebateGroupItemModified();
        }
    }

    //Mouse listeners - clicked
    public void doMouseClickAction(MouseEvent me) {
        if (me.getSource() == frames.jtWindows) {
            windowSystemKeySelected();
        }
        if (me.getSource() == frames.tbClosableBar) {
            if (me.getButton() != MouseEvent.BUTTON1) {
                popupClosableBarShow();
            }
        } else if (me.getSource() == frames.tbSelectionBar) {
            if (me.getButton() != MouseEvent.BUTTON1) {
                popupSelectionBarShow();
            }
        } else if (me.getSource() == frames.tbNT) {
            if (me.getButton() != MouseEvent.BUTTON1) {
                popupNTShow();
            }
        } else if (me.getSource() == frames.tbNX) {
            if (me.getButton() != MouseEvent.BUTTON1) {
                popupNXShow();
            }
        } else if (me.getSource() == frames.jbColors) {
            if (me.getButton() != MouseEvent.BUTTON1) {
                popupColorsShow();
            }
        } else if (me.getSource() == frames.jtPriceListSelect) {
            if (me.getClickCount() > 1) {
                doAction(frames.jbPriceListFilterAdd.getActionCommand());
            }
        } else if (me.getSource() == frames.jntWidth) {
            showDataBaseWidthInfo();
        } else if (me.getSource() == frames.jntHeight) {
            showDataBaseHeightInfo();
        } else if (me.getSource() == frames.jtCollectionEdit) {
            //Bullshit solution to refresh data, to avoid non enter pressed false values in item count cell
            if (me.getClickCount() == 1) {
                if (frames.jtCollectionEdit.getName() != null && frames.jtCollectionEdit
                                                                       .getName()
                                                                       .equals("modified")) {
                    collectionListItemModify();
                }
                frames.jtCollectionEdit.setName("");
            }

        } else if (me.getSource() == frames.profileTree) {
            if (me.getButton() != MouseEvent.BUTTON1) {
                popupProfilesShow();
            }
        } else if (me.getSource() == frames.rebateTree) {
            rebateTreeNodeSelected();
        } else if (me.getSource() == frames.jlRotoIcon) {
            showAbout();
        }
    }

    //Key listeners
    public void doKeyAction(KeyEvent ke) {
        if (ke.getSource() == frames.jntFilterSAP) {
            filterPriceList();
        } else if (ke.getSource() == frames.tfFilterText) {
            filterPriceList();
        } else if (ke.getSource()
                     .getClass()
                     .getSimpleName()
                     .equals("JRNumberTextField")) {
            JRNumberTextField numberTextField = (JRNumberTextField) ke.getSource();
            String cmd = numberTextField.getName()
                                        .toLowerCase()
                                        .trim();
            if (cmd.equals("collectionedititemmodify")) {
                //Bullshit solution to refresh data, to avoid non enter pressed false values in item count cell
                frames.jtCollectionEdit.setName("modified");
            }
        } else if (ke.getSource() == frames.jtCollectionEdit) {
            //Bullshit solution to refresh data, to avoid non enter pressed false values in item count cell
            frames.jtCollectionEdit.setName("modified");
        }

    }

    //Action listeners
    public void doAction(String ActionCommand) {
        String cmd = ActionCommand.toLowerCase().trim();
        setCursor(Cursor.WAIT_CURSOR);
        //System.out.println(cmd);
        if (cmd.equals("loginuser")) {
            loginUser();
        } else if (cmd.equals("logoutuser")) {
            logoutUser();
        } else if (cmd.equals("loginprofile")) {
            loginProfile();
        } else if (cmd.equals("logoutprofile")) {
            logoutProfile();
        } else if (cmd.equals("showmenus")) {
            showMenus();
        } else if (cmd.startsWith("popupbarpressed_")) {
            popupClosableBarPressed(cmd);
        } else if (cmd.startsWith("popupselectionbarpressed_")) {
            popupSelectionBarPressed(cmd);
        } else if (cmd.equals("selectionbarpressed")) {
            popupSelectionBarShow();
        } else if (cmd.equals("ntpressed")) {
            popupNTShow();
        } else if (cmd.startsWith("popupntpressed_")) {
            popupNTPressed(cmd);
        } else if (cmd.equals("nxpressed")) {
            popupNXShow();
        } else if (cmd.startsWith("popupnxpressed_")) {
            popupNXPressed(cmd);
        } else if (cmd.equals("colorspressed")) {
            popupColorsShow();
        } else if (cmd.equals("savemenu")) {
            saveMenuSettings();
        } else if (cmd.equals("loadstartupsettings")) {
            loadStartUpSettings();
        } else if (cmd.equals("profileselected")) {
            profileSelected();
        } else if (cmd.equals("opendatabase")) {
            openDataBase();
        } else if (cmd.startsWith("selectedcolor_")) {
            colorSelected(cmd);
        } else if (cmd.equals("selectdatabaserows")) {
            selectDatabaseRows();
        } else if (cmd.equals("selectedtype")) {
            selectDatabaseRows();
        } else if (cmd.equals("barselected")) {
            barSelected();
        } else if (cmd.equals("pricelistview")) {
            priceListView();
        } else if (cmd.equals("openpricelist")) {
            openPriceList();
        } else if (cmd.equals("savepricelist")) {
            savePriceList();
        } else if (cmd.equals("groupsview")) {
            groupsView();
        } else if (cmd.equals("addmaterialgroup")) {
            groupsAdd();
        } else if (cmd.equals("addmaterialgroupname")) {
            groupNameAdd();
        } else if (cmd.equals("modifymaterialgroup")) {
            groupsModify();
        } else if (cmd.equals("deletematerialgroup")) {
            groupsDelete();
        } else if (cmd.equals("reloadmaterialgroup")) {
            loadGroups();
        } else if (cmd.equals("modifymaterialgroupname")) {
            groupNameModify();
        } else if (cmd.equals("deletematerialgroupname")) {
            groupNameDelete();
        } else if (cmd.equals("groupssave")) {
            saveGroups();
        } else if (cmd.equals("editdatabaselist")) {
            showDataBaseList(true);
        } else if (cmd.equals("databaselistbrowse")) {
            openDataBaseDirectory();
        } else if (cmd.equals("databaselistopen")) {
            openDataBaseFromList();
        } else if (cmd.equals("moveright")) {
            moveToRight(false);
        } else if (cmd.equals("moveleft")) {
            moveToLeft(false);
        } else if (cmd.equals("moverightall")) {
            moveToRight(true);
        } else if (cmd.equals("moveleftall")) {
            moveToLeft(true);
        } else if (cmd.equals("opendatabaselist")) {
            showDataBaseList(false);
        } else if (cmd.equals("showrebate")) {
            showRebate();
        } else if (cmd.equals("adminloginselected")) {
            adminLoginSelected();
        } else if (cmd.equals("adminprofileselected")) {
            adminProfileSelected();
        } else if (cmd.equals("pricelistfilter")) {
            filterPriceList();
        } else if (cmd.equals("custompricelistadditem")) {
            frames.jriPriceListSelect.setVisibleToFocus();
            frames.jbPriceListFilterAdd.setActionCommand("additemtocustompricelist");
        } else if (cmd.equals("additemtocustompricelist")) {
            addItemToCustomPriceList();
        } else if (cmd.equals("adminrebatesave")) {

        } else if (cmd.equals("adminrebateopen")) {
            adminRebateOpen();
        } else if (cmd.equals("custompricelistremoveitem")) {
            removeItemFromCustomPriceList();
        } else if (cmd.equals("rebatesave")) {
            saveRebate();
        } else if (cmd.equals("adminshowlogin")) {
            showAdminLogin();
        } else if (cmd.equals("adminlogin")) {
            adminLogin();
        } else if (cmd.equals("adminlogout")) {
            adminLogout();
        } else if (cmd.equals("showwindowtypes")) {
            showWindowTypes();
        } else if (cmd.equals("windowsytemmodify")) {
            windowSystemModify();
        } else if (cmd.equals("windowsytemadd")) {
            windowSystemAdd();
        } else if (cmd.equals("windowsytemdelete")) {
            windowSystemDelete();
        } else if (cmd.equals("windowsystemselected")) {
            windowSystemSelected();
        } else if (cmd.equals("windowsystemclassselected")) {
            windowSystemClassSelected();
        } else if (cmd.equals("windowsystemsave")) {
            saveWindowSystem();
        } else if (cmd.equals("windowsystemmoveleft")) {
            windowSystemMoveLeft();
        } else if (cmd.equals("windowsystemmoveright")) {
            windowSystemMoveRight();
        } else if (cmd.equals("popupclosablebarshow")) {
            //Called from display canvas when closable bar right clicked.
            popupClosableBarShow();
        } else if (cmd.equals("addcollection")) {
            collectionAdd();
        } else if (cmd.equals("removecollection")) {
            collectionRemove();
        } else if (cmd.equals("showlistdetailed")) {
            showListDetailed();
        } else if (cmd.equals("showlistgarniture")) {
            showListGarniture();
        } else if (cmd.equals("showlistsummarized")) {
            showListSummarized();
        } else if (cmd.equals("showlistitems")) {
            showListItems();
        } else if (cmd.equals("collectioneditadd_showpricelist")) {
            if (frames.jlCollectionEdit.getSelectedIndex() > -1) {
                frames.jriPriceListSelect.setVisibleToFocus();
                frames.jbPriceListFilterAdd.setActionCommand("collectioneditadd");
            }
        } else if (cmd.equals("collectioneditadd")) {
            collectionListAddItem();
        } else if (cmd.equals("collectioneditremove")) {
            collectionListRemoveItem();
        } else if (cmd.equals("collectionremove")) {
            collectionListRemove();
        } else if (cmd.equals("collectioneditincrease")) {
            collectionListItemModify(+1);
        } else if (cmd.equals("collectioneditdecrease")) {
            collectionListItemModify(-1);
        } else if (cmd.equals("collectionedititemmodify")) {
            collectionListItemModify();
        } else if (cmd.equals("deletelist")) {
            listDelete(false);
        } else if (cmd.equals("savelistdetailed")) {
            listSave(swc.LIST_DETAILED);
        } else if (cmd.equals("savelistsummarized")) {
            listSave(swc.LIST_SUMMARIZED);
        } else if (cmd.equals("savelistnavison")) {
            listSave(swc.LIST_NAVISON);
        } else if (cmd.equals("savelistproject")) {
            listSave(swc.LIST_PROJECT);
        } else if (cmd.equals("opendetailedlist")) {
            listOpen(false);
        } else if (cmd.equals("concatdetailedlist")) {
            listOpen(true);
        } else if (cmd.equals("showlistexceldetailed")) {
            list_Excel_Print_PDF_Show(swc.LIST_DETAILED, swc.LIST_ACTION_EXCEL);
        } else if (cmd.equals("showlistexcelsummarized")) {
            list_Excel_Print_PDF_Show(swc.LIST_SUMMARIZED, swc.LIST_ACTION_EXCEL);
        } else if (cmd.equals("showlistexcelgarniture")) {
            list_Excel_Print_PDF_Show(swc.LIST_GARNITURE, swc.LIST_ACTION_EXCEL);
        } else if (cmd.equals("saveexcellist")) {
            listExcelSave();
        } else if (cmd.equals("showprintdetailed")) {
            list_Excel_Print_PDF_Show(swc.LIST_DETAILED, swc.LIST_ACTION_PRINT);
        } else if (cmd.equals("showprintgarniture")) {
            list_Excel_Print_PDF_Show(swc.LIST_GARNITURE, swc.LIST_ACTION_PRINT);
        } else if (cmd.equals("showprintsummarized")) {
            list_Excel_Print_PDF_Show(swc.LIST_SUMMARIZED, swc.LIST_ACTION_PRINT);
        } else if (cmd.equals("orderselected")) {
            headerSelected(swc.HEADER_ORDER);
        } else if (cmd.equals("offerselected")) {
            headerSelected(swc.HEADER_OFFER);
        } else if (cmd.equals("printlist")) {
            printList();
        } else if (cmd.equals("opendatabaseedit")) {
            openDataBaseEdit();
        } else if (cmd.equals("editdbclearsorter")) {
            frames.jtEditDataBase.setAutoCreateRowSorter(true);
            frames.jriEditDataBase.setVisibleToFocus();
        } else if (cmd.equals("editdatabasesetsap")) {
            editDataBaseSetSAP();
        } else if (cmd.equals("editdbtypeselected")) {
            editDataBaseTypeSelected();
        } else if (cmd.equals("editdbadditem_showpricelist")) {
            frames.jriPriceListSelect.setVisibleToFocus();
            frames.jbPriceListFilterAdd.setActionCommand("editdbadditem");
        } else if (cmd.equals("editdbadditem")) {
            editDataBaseAddItem();
        } else if (cmd.equals("editdbremoveitem")) {
            editDataBaseRemoveItem();
        } else if (cmd.equals("editdbmoveup")) {
            editDataBaseMoveItem(-1);
        } else if (cmd.equals("editdbmovedown")) {
            editDataBaseMoveItem(+1);
        } else if (cmd.equals("opennewdatabaseedit")) {
            openNewDataBaseEdit();
        } else if (cmd.equals("closedatabaseedit")) {
            closeDataBaseEdit();
        } else if (cmd.equals("saveasdatabaseedit")) {
            saveAsDataBaseEdit();
        } else if (cmd.equals("savedatabaseedit")) {
            saveDataBaseEdit();
        } else if (cmd.equals("savedatabaseexcel")) {
            saveDataBaseExcel();
        } else if (cmd.equals("programexit")) {
            programExit();
        } else if (cmd.equals("showreplacedata")) {
            showReplaceData();
        } else if (cmd.equals("replacedatasetsap")) {
            replaceDataSetSAP();
        } else if (cmd.equals("replacedataadditem")) {
            replaceDataAddItem();
        } else if (cmd.equals("replacedataremoveitem")) {
            replaceDataRemoveItem();
        } else if (cmd.equals("replacelistadditem")) {
            replaceListAddItem();
        } else if (cmd.equals("replacelistremoveitem")) {
            replaceListRemoveItem();
        } else if (cmd.equals("replacedata")) {
            replaceData();
        } else if (cmd.equals("databaseclose")) {
            closeDataBase();
        } else if (cmd.equals("showcurrencies")) {
            showCurrencies();
        } else if (cmd.equals("savecurrency")) {
            saveCurrency();
        } else if (cmd.equals("showstocklist")) {
            showStockList();
        } else if (cmd.equals("stocklistadditem_showpricelist")) {
            frames.jriPriceListSelect.setVisibleToFocus();
            frames.jbPriceListFilterAdd.setActionCommand("stocklistadditem");
        } else if (cmd.equals("stocklistadditem")) {
            stockListAddItem();
        } else if (cmd.equals("stocklistremoveitem")) {
            stockListRemoveItem();
        } else if (cmd.equals("stocklistaddfile")) {
            stockListAddFile();
        } else if (cmd.equals("stocklistremovefile")) {
            stockListRemoveFile();
        } else if (cmd.equals("stocklistcollect")) {
            stockListCollect();
        } else if (cmd.equals("stocklistsave")) {
            stockListSave();
        } else if (cmd.equals("stocklistopen")) {
            stockListOpen();
        } else if (cmd.equals("stocklistsaveexcel")) {
            stockSaveExcel();
        } else if (cmd.equals("stocklistprint")) {
            stockListPrint();
        } else if (cmd.equals("stocklistcollectdelete")) {
            stockListCollectDelete();
        } else if (cmd.equals("showlanguages")) {
            showLanguages();
        } else if (cmd.equals("languagesave")) {
            saveLanguages();
        } else if (cmd.equals("languageload")) {
            loadLanguages();
        } else if (cmd.equals("showeditlanguages")) {
            showEditLanguages();
        } else if (cmd.equals("languagecomboselected")) {
            languageComboSelected();
        } else if (cmd.equals("languageeditorsave")) {
            saveEditorLanguages();
        } else if (cmd.equals("languageeditoradd")) {
            addEditorLanguages();
        } else if (cmd.equals("showregisterprogram")) {
            showRegisterProgram();
        } else if (cmd.equals("registersaveform")) {
            saveRegisterForm();
        } else if (cmd.equals("registeropenform")) {
            openRegisterForm();
        } else if (cmd.equals("registernewkey")) {
            registerNewKey();
        } else if (cmd.equals("registerprogram")) {
            registerProgram();
        } else if (cmd.equals("registerprogramdemo")) {
            registerProgramDemo();
        } else if (cmd.equals("showregisterdata")) {
            showRegisterData();
        } else if (cmd.equals("showcorrection")) {
            showCorrection();
        } else if (cmd.equals("correctionsave")) {
            correctionSave();
        } else if (cmd.equals("showprofiles")) {
            showProfiles();
        } else if (cmd.equals("profileassembly")) {
            profileAssemply();
        } else if (cmd.equals("profileexport")) {
            profileExport();
        } else if (cmd.equals("profileopendirectory")) {
            profileOpenDirectory();
        } else if (cmd.equals("profiledelete")) {
            profileDelete();
        } else if (cmd.equals("profilemodifyshow")) {
            profileModifyShow("profilemodify", true);
        } else if (cmd.equals("profilemodify")) {
            profileModify();
        } else if (cmd.equals("profileaddnewusershow")) {
            profileModifyShow("profileaddnewuser", false);
        } else if (cmd.equals("profileaddnewuser")) {
            profileAddNewUser();
        } else if (cmd.equals("profileaddnewprofileshow")) {
            profileModifyShow("profileaddnewprofile", false);
        } else if (cmd.equals("profileaddnewprofile")) {
            profileAddNewProfile();
        } else if (cmd.equals("rebatetreeset")) {
            rebateTreeSetDefaultRebate();
        } else if (cmd.equals("rebatetreeopen")) {
            rebateTreeOpenSelected();
        } else if (cmd.equals("rebatetreedelete")) {
            rebateTreeDelete();
        } else if (cmd.equals("showfontsize")) {
            showFontSize();
        } else if (cmd.equals("fontsizesave")) {
            fontSizeSave();
        } else if (cmd.equals("rebategroupitemmodified")) {
            rebateGroupItemModified();
        } else if (cmd.equals("showabout")) {
            showAbout();
        }

        setCursor(Cursor.DEFAULT_CURSOR);
    }

    //Show about
    private void showAbout() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriAbout.setVisibleToFocus();
    }

    //If user is not an admin, rebate group item text is not enabled to changed
    private void rebateCheckGroupItemTextChangeEnabled() {
        int selectedRowIndex = frames.jtRebateGroups.getSelectedRow();
        int selectedColumnIndex = frames.jtRebateGroups.getSelectedColumn();

        if (selectedColumnIndex == 1 && !(adminUtil.isAdminLoggedIn() || adminUtil.isOperatorLoggedIn())) {
            frames.jtRebateGroups.editCellAt(selectedRowIndex, 0);
        }
    }

    //Modify all rebate group item when mouse exited from jtable
    private void rebateGroupItemModified() {
        int selectedRow = frames.jtRebateGroups.getSelectedRow();
        if (selectedRow > -1) {

            frames.jtRebateGroups.editCellAt(0, 0);
            for (int i = 0; i < frames.jtRebateGroups.getRowCount(); i++) {
                String selectedGroupItemName = frames.jtRebateGroups
                                                     .getValueAt(i, 0)
                                                     .toString();
                String selectedGroupItemText = frames.jtRebateGroups
                                                     .getValueAt(i, 1)
                                                     .toString();
                String selectedGroupItemRebate = frames.jtRebateGroups
                                                       .getValueAt(i, 2)
                                                       .toString();

                rebateUtil.modifyGroupItem(selectedGroupItemName, selectedGroupItemText, selectedGroupItemRebate);

                //System.out.println("Modify: " + selectedGroupItemName + " to " + selectedGroupItemText + "," + selectedGroupItemRebate);
            }

        }
    }

    //Set rebate groups when a rebate selected
    private void rebateSelected() {
        int selectedRow = frames.jtRebate.getSelectedRow();
        if (selectedRow > -1) {
            String selectedGroupName = frames.jtRebate
                                             .getValueAt(selectedRow, 0)
                                             .toString();
            rebateUtil.setRebateGroupItems(selectedGroupName);
        }

    }

    private void loadStartUpSettings() {
        //Set logout GUI
        adminUtil.logout();
        //LoadSettings
        loadSettings();
        //Check registration (load settings run every time in login/logout)
        checkRegistration();
    }

    //Load font size
    private void loadFontSize() {
        Properties rotoProperty = propertyUtil.getRotoProperty();
        int fontSize = propertyUtil.getIntProperty(rotoProperty, "FontSize.Height");
        if (fontSize != 0) {
            frames.jcbFontSize.setSelectedItem(fontSize);

            frames.jtCollection.setFont(new Font(frames.jtCollection
                                                       .getFont()
                                                       .getFamily(), frames.jtCollection
                                                                           .getFont()
                                                                           .getStyle(), fontSize));
            frames.jtCollection.setRowHeight(fontSize + 2);

            frames.jtCollectionList.setFont(new Font(frames.jtCollectionList
                                                           .getFont()
                                                           .getFamily(), frames.jtCollection
                                                                               .getFont()
                                                                               .getStyle(), fontSize));
            frames.jtCollectionList.setRowHeight(fontSize + 2);

            frames.jlCollectionEdit.setFont(new Font(frames.jtCollectionEdit
                                                           .getFont()
                                                           .getFamily(), frames.jtCollection
                                                                               .getFont()
                                                                               .getStyle(), fontSize));
            frames.jtCollectionEdit.setFont(new Font(frames.jlCollectionEdit
                                                           .getFont()
                                                           .getFamily(), frames.jtCollection
                                                                               .getFont()
                                                                               .getStyle(), fontSize));
            frames.jtCollectionEdit.setRowHeight(fontSize + 2);

            frames.jtPriceListView.setFont(new Font(frames.jtPriceListView
                                                          .getFont()
                                                          .getFamily(), frames.jtCollection
                                                                              .getFont()
                                                                              .getStyle(), fontSize));
            frames.jtPriceListView.setRowHeight(fontSize + 2);

            frames.jtPriceListSelect.setFont(new Font(frames.jtPriceListSelect
                                                            .getFont()
                                                            .getFamily(), frames.jtCollection
                                                                                .getFont()
                                                                                .getStyle(), fontSize));
            frames.jtPriceListSelect.setRowHeight(fontSize + 2);

            frames.jtEditDataBase.setFont(new Font(frames.jtEditDataBase
                                                         .getFont()
                                                         .getFamily(), frames.jtCollection
                                                                             .getFont()
                                                                             .getStyle(), fontSize));
            frames.jtEditDataBase.setRowHeight(fontSize + 2);

            frames.jtStockList.setFont(new Font(frames.jtStockList
                                                      .getFont()
                                                      .getFamily(), frames.jtCollection
                                                                          .getFont()
                                                                          .getStyle(), fontSize));
            frames.jtStockList.setRowHeight(fontSize + 2);

            frames.jtExcelList.setFont(new Font(frames.jtExcelList
                                                      .getFont()
                                                      .getFamily(), frames.jtCollection
                                                                          .getFont()
                                                                          .getStyle(), fontSize));
            frames.jtExcelList.setRowHeight(fontSize + 2);
        }
    }

    //Save font size
    private void fontSizeSave() {
        Properties rotoProperty = propertyUtil.getRotoProperty();
        rotoProperty.setProperty("FontSize.Height", String.valueOf(frames.jcbFontSize.getSelectedItem()));
        propertyUtil.saveRotoProperty();
        //Refresh font size
        loadFontSize();
        message.showInformationMessage(propertyUtil.getLangText("Information.FontSize.Saved"));

    }

    //Show profiles
    private void showFontSize() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriFontSize.setVisibleToFocus();
    }

    //Delete a rebate file from rebate tree
    private void rebateTreeDelete() {
        DefaultTreeModel rebateModel = (DefaultTreeModel) frames.rebateTree.getModel();
        CheckNode rebateRoot = frames.nodeRootRebate;

        CheckNode selectedNode = (CheckNode) frames.rebateTree.getLastSelectedPathComponent();
        String title = selectedNode.getTitle();

        if (title.toLowerCase().endsWith(".xml")) {
            CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
            String profileName = selectedParentNode.getTitle();

            //Can't delete if default file
            if (selectedNode.isCheckBoxSelected()) {
                message.showInformationMessage(propertyUtil.getLangText("Information.Rebate.Delete.DefaultNotDeletable"));
            }
            //Delete file
            else if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.Rebate.TreeDelete") + " ( " + title +
                                               " )")) {
                File filePath = new File(profileUtil.getProfileDirectory(profileName) + "\\Rebate\\" + title);

                try {
                    //Delete from system
                    filePath.delete();

                    //Remove from tree
                    selectedNode.removeFromParent();

                    //Refresh profile tree
                    rebateModel.setRoot(rebateRoot);

                    TreePath path = new TreePath(selectedParentNode.getPath());
                    frames.rebateTree.expandPath(path);
                    frames.rebateTree.setSelectionPath(path);
                    frames.rebateTree.scrollPathToVisible(path);

                } catch (Exception e) {
                    message.showErrorMessage(propertyUtil.getLangText("Error.Rebate.Delete") + " : " + e.getMessage(),
                                             "");
                }
            }

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Rebate.Delete.NotSelected"));
        }


    }

    //Set profile and user name, when tree node changed
    private void rebateTreeNodeSelected() {
        CheckNode selectedNode = (CheckNode) frames.rebateTree.getLastSelectedPathComponent();
        if (!selectedNode.isRoot()) {
            CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
            String title = selectedNode.getTitle();

            if (title.toLowerCase().endsWith("xml")) {
                //Rabat selected
                String profileName = selectedParentNode.getTitle();
                String userName = ((CheckNode) selectedParentNode.getParent()).getTitle();
                frames.tfRebateProfileLocation.setText(userName + " / " + profileName + " / ");
            } else if (!((CheckNode) selectedNode.getParent()).isRoot()) {
                //Profile selected
                String profileName = selectedNode.getTitle();
                String userName = selectedParentNode.getTitle();
                frames.tfRebateProfileLocation.setText(userName + " / " + profileName + " / ");
            } else {
                frames.tfRebateProfileLocation.setText("");
            }
        }
    }

    //Set rebate as default
    private void rebateTreeOpenSelected() {
        CheckNode selectedNode = (CheckNode) frames.rebateTree.getLastSelectedPathComponent();
        CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
        String title = selectedNode.getTitle();
        if (title.toLowerCase().endsWith("xml")) {

            String profileName = selectedParentNode.getTitle();

            if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.Rebate.TreeOpen") + " ( " + title + " )")) {
                String rebatePath = profileUtil.getProfileDirectory(profileName) + "/Rebate/" + title;
                //Load rebate from admin
                if (profileName.equals(swc.nodeRabateAdmin)) {
                    rebatePath = swc.RebatePath + title;
                }
                //Load rebate from profiles	
                File rebateFile = new File(rebatePath);
                rebateUtil.loadRebate(rebateFile);

                //Refresh collection
                selectDatabaseRows();
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Rebate.Open.RebateNotSelected"));
        }
    }

    //Set rebate as default
    private void rebateTreeSetDefaultRebate() {
        CheckNode selectedNode = (CheckNode) frames.rebateTree.getLastSelectedPathComponent();
        CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
        String title = selectedNode.getTitle();
        if (selectedNode.isCheckBoxEnabled() && title.toLowerCase().endsWith("xml") &&
            message.showConfirmDialog(propertyUtil.getLangText("Confirm.Rebate.TreeSet") + " ( " + title + " )")) {

            //Set property to default rebate for profile
            String profileName = selectedParentNode.getTitle().toLowerCase();
            String filePath = profileUtil.getProfileDirectory(profileName);
            propertyUtil.setProfileProperty("Profiles." + profileName + ".RebateFile", filePath + "\\Rebate\\" + title);

            selectedNode.setCheckBoxSelected(true);
            selectedNode.setCheckBoxSelected(true);
            //Refresh path
            TreePath path = new TreePath(selectedParentNode);
            frames.rebateTree.collapsePath(path);
            frames.rebateTree.expandPath(path);
        }

    }

    //Add new profile
    private void profileAddNewProfile() {
        String newProfileName = frames.tfProfilesInput
                                      .getText()
                                      .toLowerCase()
                                      .trim();

        if (newProfileName.length() == 0) {
            return;
        }

        if (!userUtil.isProfileExists(newProfileName)) {

            DefaultTreeModel profileModel = (DefaultTreeModel) frames.profileTree.getModel();
            CheckNode profileRoot = frames.nodeRootProfiles;
            CheckNode selectedNode = (CheckNode) frames.profileTree.getLastSelectedPathComponent();
            CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();


            if (!selectedParentNode.isRoot()) { //Set user to parent if profile selected
                selectedNode = selectedParentNode;
            }

            //Create new node for profile
            CheckNode newNode = new CheckNode(newProfileName);
            newNode.setSelectionMode(CheckNode.SINGLE_SELECTION);
            newNode.setCheckBoxEnabled(true);
            newNode.setImageIconDefault(new ImageIcon(swc.iconPath + ("TreeProfile.jpg")));
            newNode.setImageIconClosed(new ImageIcon(swc.iconPath + ("TreeProfile.jpg")));
            newNode.setImageIconOpened(new ImageIcon(swc.iconPath + ("TreeProfile.jpg")));

            //Add new profile to tree
            profileModel.insertNodeInto(newNode, selectedNode, 0);

            //Refresh profile tree
            profileModel.setRoot(profileRoot);

            //Add new profile to system
            profileUtil.createProfile(newNode.getTitle(), selectedNode.getTitle());

            //Select new user
            TreePath path = new TreePath(newNode.getPath());
            frames.profileTree.setSelectionPath(path);
            frames.profileTree.expandPath(path);

            frames.jpProfilesInput.setVisible(false);

            //Refresh rebate tree on rebate frame if visible
            refreshRebateTree();

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Profiles.ProfileExists"));
        }

    }


    //Add new user
    private void profileAddNewUser() {
        String newUserName = frames.tfProfilesInput
                                   .getText()
                                   .toLowerCase()
                                   .trim();

        if (newUserName.length() == 0) {
            return;
        }

        if (!userUtil.isUserExists(newUserName)) {

            DefaultTreeModel profileModel = (DefaultTreeModel) frames.profileTree.getModel();
            CheckNode profileRoot = frames.nodeRootProfiles;

            //Create new node
            CheckNode newNode = new CheckNode(newUserName);
            newNode.setSelectionMode(CheckNode.SINGLE_SELECTION);
            newNode.setCheckBoxEnabled(false);
            newNode.setImageIconDefault(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
            newNode.setImageIconClosed(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));
            newNode.setImageIconOpened(new ImageIcon(swc.iconPath + ("TreeProfiles.jpg")));

            //Add new user to tree
            profileModel.insertNodeInto(newNode, (MutableTreeNode) profileRoot, 0);

            //Refresh profile tree
            profileModel.setRoot(profileRoot);

            //Add new user to system
            userUtil.addNewUser(newUserName);

            //Select new user
            TreePath path = new TreePath(newNode.getPath());
            frames.profileTree.setSelectionPath(path);
            frames.profileTree.expandPath(path);

            frames.jpProfilesInput.setVisible(false);

            //Refresh rebate tree on rebate frame if visible
            refreshRebateTree();

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Profiles.UserExists"));
        }

    }

    //Modify node name
    private void profileModify() {
        DefaultTreeModel profileModel = (DefaultTreeModel) frames.profileTree.getModel();
        CheckNode profileRoot = frames.nodeRootProfiles;
        CheckNode selectedNode = (CheckNode) frames.profileTree.getLastSelectedPathComponent();
        CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
        String newProfileName = frames.tfProfilesInput.getText();

        if (newProfileName.length() == 0) {
            return;
        }

        //Modify profile
        if (!selectedParentNode.isRoot()) {
            //System.out.println("Modify profile.");
            if (!userUtil.isProfileExists(newProfileName)) {
                userUtil.modifyProfileName(selectedNode.getTitle(), newProfileName);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.Profiles.ProfileExists"));
            }
        }
        //Modify user
        else {
            //System.out.println("Modify user.");
            if (!userUtil.isUserExists(newProfileName)) {
                userUtil.modifyUserName(selectedNode.getTitle(), newProfileName);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.Profiles.UserExists"));
            }
        }

        //Rebuild profile tree
        userUtil.buildProfileRoot(profileRoot);
        profileModel.setRoot(profileRoot);

        //Select root
        TreePath path = new TreePath(profileRoot.getPath());
        frames.profileTree.setSelectionPath(path);
        frames.profileTree.expandPath(path);

        //Rebuild export tree to show modifications, if there were any selection earlier
        CheckNode exportRoot = frames.nodeRootExport;
        if (exportRoot.getChildCount() > 0) {
            profileAssemply();
        }

        frames.jpProfilesInput.setVisible(false);

        //Refresh rebate tree on rebate frame if visible
        refreshRebateTree();
    }


    //Show profile input popup
    private void profileModifyShow(String actionCommand, boolean addDefaultValue) {
        CheckNode selectedNode = (CheckNode) frames.profileTree.getLastSelectedPathComponent();

        frames.jpProfilesInput.setVisible(true);

        //Try to get resize frame from tab util, to add popup menu to the removed frame if exist
        JRFrame frameMenus = tabUtil.getFrameList().get(swc.tabMenus);
        if (frameMenus == null) {
            frames.jpProfilesInput.show(frames.jrfMain, mouse.getCursorX(), mouse.getCursorY());
        } else {
            frames.jpProfilesInput.show(frameMenus, mouse.getCursorX(), mouse.getCursorY());
        }

        frames.jpProfilesInput.setLocation(mouse.getCursorPosition());

        if (addDefaultValue) {
            frames.tfProfilesInput.setText(selectedNode.getTitle());
        } else {
            frames.tfProfilesInput.setText("");
        }

        frames.tfProfilesInput.requestFocus();
        frames.tfProfilesInput.setActionCommand(actionCommand);

    }

    //Delete selected profile
    private void profileDelete() {

        CheckNode selectedNode = (CheckNode) frames.profileTree.getLastSelectedPathComponent();
        CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
        DefaultTreeModel profileModel = (DefaultTreeModel) frames.profileTree.getModel();
        CheckNode profileRoot = frames.nodeRootProfiles;

        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.Profiles.Delete") + " ( " +
                                      selectedNode.getTitle() + " )")) {

            //Delete profile from file system and property
            //User can be a leaf if it doesn't has any child, so we have to check the root to be sure its a profile object
            if (!selectedParentNode.isRoot()) {
                //System.out.println("Deleting profile from system: " +  selectedNode.getTitle());
                userUtil.deleteProfile(selectedNode.getTitle());
            }
            //Delete user from tree (that means parent is the root
            else {
                //System.out.println("Deleting user from system: " +  selectedNode.getTitle());
                userUtil.deleteUser(selectedNode.getTitle());
            }

            //Remove selected node from profile tree
            selectedNode.removeFromParent();

            //Refresh profile tree
            profileModel.setRoot(profileRoot);

            TreePath path = new TreePath(selectedParentNode.getPath());
            frames.profileTree.expandPath(path);
            frames.profileTree.setSelectionPath(path);
            frames.profileTree.scrollPathToVisible(path);


            //Rebuild export tree to show modifications, if there were any selection earlier
            CheckNode exportRoot = frames.nodeRootExport;
            if (exportRoot.getChildCount() > 0) {
                profileAssemply();
            }

            //Refresh rebate tree on rebate frame if visible
            refreshRebateTree();
        }

        frames.jpProfilesInput.setVisible(false);
    }

    //Show profile popup
    private void popupProfilesShow() {
        CheckNode selectedNode = (CheckNode) frames.profileTree.getLastSelectedPathComponent();
        frames.jmiProfileNew.setEnabled(!selectedNode.isRoot());
        frames.jmiProfileDelete.setEnabled(!selectedNode.isRoot());
        frames.jmiProfileModify.setEnabled(!selectedNode.isRoot());

        frames.jpProfiles.setVisible(true);

        //Try to get resize frame from tab util, to add popup menu to the removed frame if exist
        JRFrame frameMenus = tabUtil.getFrameList().get(swc.tabMenus);
        if (frameMenus == null) {
            frames.jpProfiles.show(frames.jrfMain, mouse.getCursorX() - 50, mouse.getCursorY());
        } else {
            frames.jpProfiles.show(frameMenus, mouse.getCursorX() - 50, mouse.getCursorY());
        }

        frames.jpProfiles.setLocation(mouse.getCursorPosition());
    }

    //Open export directory
    private void profileOpenDirectory() {
        fileChooser =
            new JRFileChooser("...", "Directories", propertyUtil.getLangText("Dialog.File.Title.Open"), "C:/");
        fileChooser.setFileSelectionMode(JRFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            frames.tfExportDirectory.setText(fileChooser.getSelectedFile().getAbsolutePath());

            //Set next sub directory, if exists
            frames.tfSubDirectory.setText("\\" +
                                          ioUtil.getNextDirectory(fileChooser.getSelectedFile().getAbsolutePath(),
                                                                  "Roto"));

        }
    }
    //Export profiles
    private void profileExport() {
        if (frames.nodeRootExport.getChildCount() > 0) {
            File exportDirectory = new File(frames.tfExportDirectory.getText());
            if (exportDirectory.exists()) {

                //Set subdirectory name for export
                exportDirectory = new File(exportDirectory.getAbsolutePath() + frames.tfSubDirectory.getText());

                frames.jbProfileExport.setEnabled(false);
                frames.jbExportDirectory.setIcon(new ImageIcon(swc.viewerPath + "Loading.gif"));

                //start export thread
                ExportThread export = new ExportThread(exportDirectory);
                export.start();

            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.Export.InvalidDirectory"));
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Export.Empty"));
        }
    }

    //Assembly export tree
    private void profileAssemply() {
        CheckNode profileRoot = frames.nodeRootProfiles;
        CheckNode exportRoot = frames.nodeRootExport;

        DefaultTreeModel exportModel = (DefaultTreeModel) frames.exportTree.getModel();
        exportModel.setRoot(null);
        exportRoot.removeAllChildren();

        Enumeration e = profileRoot.children();
        while (e.hasMoreElements()) {
            //Add users to export tree
            CheckNode profileUserNode = (CheckNode) e.nextElement();

            CheckNode exportUserNode = new CheckNode(profileUserNode.getTitle());
            exportUserNode.setCheckBoxEnabled(profileUserNode.isCheckBoxEnabled());
            exportUserNode.setImageIconDefault(profileUserNode.getImageIconDefault());
            exportUserNode.setImageIconOpened(profileUserNode.getImageIconOpened());
            exportUserNode.setImageIconClosed(profileUserNode.getImageIconClosed());

            exportRoot.add(exportUserNode);

            Enumeration en = profileUserNode.children();
            while (en.hasMoreElements()) {
                //Add profiles to export tree
                CheckNode profileProfileNode = (CheckNode) en.nextElement();

                if (profileProfileNode.isSelected()) {
                    CheckNode exportProfileNode = new CheckNode(profileProfileNode.getTitle());
                    exportProfileNode.setCheckBoxEnabled(false);
                    exportProfileNode.setImageIconDefault(profileProfileNode.getImageIconDefault());
                    exportProfileNode.setImageIconOpened(profileProfileNode.getImageIconOpened());
                    exportProfileNode.setImageIconClosed(profileProfileNode.getImageIconClosed());

                    exportUserNode.add(exportProfileNode);
                }
            }

            //Remove users if no profiles found
            if (exportUserNode.getChildCount() == 0) {
                exportUserNode.removeFromParent();
            }
        }
        exportModel.setRoot(exportRoot);
        frames.exportTree.revalidate();
        frames.exportTree.repaint();
    }

    //Show profiles
    private void showProfiles() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriProfiles.setVisibleToFocus();
        DefaultTreeModel profileModel = (DefaultTreeModel) frames.profileTree.getModel();

        //Fill profile tree
        CheckNode root = frames.nodeRootProfiles;
        //Build root
        userUtil.buildProfileRoot(root);

        profileModel.setRoot(root);
        frames.profileTree.setSelectionRow(0);
        TreePath currentSelection = frames.profileTree.getSelectionPath();
        frames.profileTree.expandPath(currentSelection);

    }

    //Save correction
    private void correctionSave() {
        Properties rotoProperty = propertyUtil.getRotoProperty();
        rotoProperty.setProperty("Correction.Width", String.valueOf(frames.jntCorrectionSaveWidth.getText()));
        rotoProperty.setProperty("Correction.Height", String.valueOf(frames.jntCorrectionSaveHeight.getText()));
        frames.jntCorrectionWidth.setText(frames.jntCorrectionSaveWidth.getText());
        frames.jntCorrectionHeight.setText(frames.jntCorrectionSaveHeight.getText());
        propertyUtil.saveRotoProperty();
        message.showInformationMessage(propertyUtil.getLangText("Information.Correction.Saved"));
    }

    //Load correction
    private void loadCorrection() {
        frames.jcbCurrency.setSelectedIndex(0);
        Properties rotoProperty = propertyUtil.getRotoProperty();
        String correctionWidth = propertyUtil.getStringProperty(rotoProperty, "Correction.Width");
        String correctionHeight = propertyUtil.getStringProperty(rotoProperty, "Correction.Height");

        if (correctionWidth.length() > 0) {
            frames.jntCorrectionSaveWidth.setText(correctionWidth);
            frames.jntCorrectionWidth.setText(correctionWidth);
        } else {
            frames.jntCorrectionSaveWidth.setText("108");
            frames.jntCorrectionWidth.setText("108");
        }
        if (correctionHeight.length() > 0) {
            frames.jntCorrectionSaveHeight.setText(correctionHeight);
            frames.jntCorrectionHeight.setText(correctionHeight);
        } else {
            frames.jntCorrectionSaveHeight.setText("108");
            frames.jntCorrectionHeight.setText("108");
        }
    }

    //Show correction
    private void showCorrection() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriCorrection.setVisibleToFocus();
    }

    //Show registration info
    private void showRegisterData() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriRegisterInfo.setVisibleToFocus();
        registerUtil.setRegistrationInfo(frames.jtpRegisterInfo);
    }

    //Register program (demo)
    private void registerProgramDemo() {
        registerUtil.registerProgram(true);
        message.showInformationMessage(propertyUtil.getLangText("Information.Registration.Success"));
    }

    //Register program
    private void registerProgram() {
        String code = frames.tfCode1.getText() + frames.tfCode2.getText() + frames.tfCode3.getText();
        String key =
            registerUtil.getCodeByKey(frames.tfKey1.getText(), frames.tfKey2.getText(), frames.tfKey3.getText());

        if (code.equals(key)) {
            registerUtil.registerProgram(false);
            message.showInformationMessage(propertyUtil.getLangText("Information.Registration.Success"));
        } else {
            message.showErrorMessage(propertyUtil.getLangText("Error.Registration.InvalidKey"), "");
        }

    }

    //Create new registration key
    private void registerNewKey() {
        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.Registration.NewKey"))) {
            registerUtil.generateKey(frames.tfKey1, frames.tfKey2, frames.tfKey3);
            frames.tfCode1.setText("");
            frames.tfCode2.setText("");
            frames.tfCode3.setText("");
            registerUtil.saveRegistration(new File(swc.defaultLicencePath));
        }
    }

    //Load registration
    private void loadRegistration() {
        registerUtil.loadOrCreateCurrentRegistration();
    }

    //Check registration
    private void checkRegistration() {
        registerUtil.checkRegistration();
    }

    //Open register
    private void openRegisterForm() {
        fileChooser =
            new JRFileChooser(".LIC", "*.LIC", propertyUtil.getLangText("Dialog.File.Title.Open"),
                              swc.registrationPath);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            registerUtil.openRegistration(fileChooser.getSelectedFile());
        }

    }

    //Save register
    private void saveRegisterForm() {
        fileChooser =
            new JRFileChooser(".LIC", "*.LIC", propertyUtil.getLangText("Dialog.File.Title.Save"),
                              swc.registrationPath);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            File outPut = fileChooser.getSelectedFile();
            String filePath = outPut.getAbsolutePath();

            if (!filePath.toLowerCase()
                         .trim()
                         .endsWith(".lic")) {
                filePath += ".lic";
                outPut = new File(filePath);
            }

            if (!outPut.exists() ||
                (outPut.exists() &&
                 message.showConfirmDialog(propertyUtil.getLangText("Confirm.Registration.SaveRegistration.Exists")))) {

                registerUtil.saveRegistration(outPut);

            }
        }
    }

    //Show program registration
    private void showRegisterProgram() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriRegister.setVisibleToFocus();
    }

    //Language combo changed
    private void languageComboSelected() {
        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.LanguageEditor.LangChange"))) {
            loadLanguageProperties(frames.jtLanguagesEdit);
            swc.setSelectedLanguageLeft(frames.jcbLanguageLeft.getSelectedIndex());
            swc.setSelectedLanguageRight(frames.jcbLanguageRight.getSelectedIndex());
        } else {
            frames.jcbLanguageLeft.setActionCommand("");
            frames.jcbLanguageRight.setActionCommand("");
            frames.jcbLanguageLeft.setSelectedIndex(swc.getSelectedLanguageLeft());
            frames.jcbLanguageRight.setSelectedIndex(swc.getSelectedLanguageRight());
            frames.jcbLanguageLeft.setActionCommand("languagecomboselected");
            frames.jcbLanguageRight.setActionCommand("languagecomboselected");
        }

    }


    //Add row languages
    private void addEditorLanguages() {

        frames.tmLanguagesEdit.addRow(new Object[] { "", "", "" });
        Rectangle rect = frames.jtLanguagesEdit.getCellRect(0, 0, true);
        frames.jtLanguagesEdit.scrollRectToVisible(rect);
        frames.jtLanguagesEdit.clearSelection();
        frames.tmLanguagesEdit.fireTableDataChanged(); // notify the model
        frames.jtLanguagesEdit.setRowSelectionInterval(0, 0);
    }

    //Save languages
    private void saveEditorLanguages() {
        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.LanguageEditor.Save"))) {
            frames.jtLanguagesEdit.editCellAt(0, 0);

            String langCodeLeft = swc.langCodes[frames.jcbLanguageLeft.getSelectedIndex()];
            String langCodeRight = swc.langCodes[frames.jcbLanguageRight.getSelectedIndex()];

            EncodedProperties propertyLeft =
                propertyUtil.loadUTF8Property(swc.basicPath + "Language_" + langCodeLeft + ".properties");
            EncodedProperties propertyRight =
                propertyUtil.loadUTF8Property(swc.basicPath + "Language_" + langCodeRight + ".properties");

            for (int row = 0; row < frames.jtLanguagesEdit.getRowCount(); row++) {
                String key = "";
                if (frames.jtLanguagesEdit.getValueAt(row, 0) != null) {
                    key = frames.jtLanguagesEdit
                                .getValueAt(row, 0)
                                .toString()
                                .trim();
                }

                String leftValue = "";
                if (frames.jtLanguagesEdit.getValueAt(row, 1) != null) {
                    leftValue = frames.jtLanguagesEdit
                                      .getValueAt(row, 1)
                                      .toString()
                                      .trim();
                }
                String rightValue = "";
                if (frames.jtLanguagesEdit.getValueAt(row, 2) != null) {
                    rightValue = frames.jtLanguagesEdit
                                       .getValueAt(row, 2)
                                       .toString()
                                       .trim();
                }

                if (key.length() != 0) {
                    propertyLeft.setProperty(key, leftValue);
                    propertyRight.setProperty(key, rightValue);
                }
            }

            try {
                propertyUtil.saveUTF8Property(propertyLeft, swc.basicPath + "Language_" + langCodeLeft + ".properties");
                propertyUtil.saveUTF8Property(propertyRight,
                                              swc.basicPath + "Language_" + langCodeRight + ".properties");

            } catch (Exception e) {
                e.printStackTrace();
            }

            message.showInformationMessage(propertyUtil.getLangText("Information.LanguagesEditor.Saved"));
        }
    }

    //Show languages
    private void showEditLanguages() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriLanguagesEdit.setVisibleToFocus();

        loadLanguageProperties(frames.jtLanguagesEdit);
    }

    //Fill languges editor
    public void loadLanguageProperties(JTable table) {

        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        String langCodeLeft = swc.langCodes[frames.jcbLanguageLeft.getSelectedIndex()];
        String langCodeRight = swc.langCodes[frames.jcbLanguageRight.getSelectedIndex()];

        frames.jlLanguageLeft.setIcon(new ImageIcon(swc.flagPath +
                                                    swc.flags[frames.jcbLanguageLeft.getSelectedIndex()] + ".jpg"));
        frames.jlLanguageRight.setIcon(new ImageIcon(swc.flagPath +
                                                     swc.flags[frames.jcbLanguageRight.getSelectedIndex()] + ".jpg"));

        table.getColumnModel()
             .getColumn(1)
             .setHeaderValue(swc.flagTitles[frames.jcbLanguageLeft.getSelectedIndex()]);
        table.getColumnModel()
             .getColumn(2)
             .setHeaderValue(swc.flagTitles[frames.jcbLanguageRight.getSelectedIndex()]);

        Properties propertyKey = propertyUtil.loadUTF8Property(swc.basicPath + "Language_hu.properties");

        Properties propertyLeft =
            propertyUtil.loadUTF8Property(swc.basicPath + "Language_" + langCodeLeft + ".properties");
        Properties propertyRight =
            propertyUtil.loadUTF8Property(swc.basicPath + "Language_" + langCodeRight + ".properties");

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        Iterator it = propertyKey.keySet().iterator();
        while (it.hasNext()) {
            String key = String.valueOf(it.next());
            String valueLeft = propertyLeft.getProperty(key);
            String valueRight = propertyRight.getProperty(key);
            if (!key.startsWith("#")) {
                tableModel.addRow(new Object[] { key, valueLeft, valueRight });
            }

        }


    }

    //Save language
    private void saveLanguages() {
        Enumeration e = frames.groupFlags.getElements();
        while (e.hasMoreElements()) {
            JRadioButton jrFlag = (JRadioButton) e.nextElement();

            if (jrFlag.isSelected()) {
                Properties langProperty = propertyUtil.getLanguageProperty();
                langProperty.setProperty("Language.Selected", jrFlag.getName());
                propertyUtil.saveLanguageProperty();
            }
        }
        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.Languages.Saved"))) {
            programRestart();
        }
    }

    //Load language
    private void loadLanguages() {
        //This method set only the selected radio button. Language set in Guibilder
        Properties langProperty = propertyUtil.getLanguageProperty();
        String selectedLanguage = langProperty.getProperty("Language.Selected");

        Enumeration e = frames.groupFlags.getElements();
        while (e.hasMoreElements()) {
            JRadioButton jrFlag = (JRadioButton) e.nextElement();
            if (jrFlag.getName().equals(selectedLanguage)) {
                jrFlag.setSelected(true);
            }
        }

    }

    //Show languages
    private void showLanguages() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriLanguages.setVisibleToFocus();
    }

    //Delete stock list
    private void stockListCollectDelete() {
        if (frames.dlStockFiles.size() > 0) {
            if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.StockList.DeleteList"))) {
                while (frames.tmStockList.getRowCount() > 0) {
                    frames.tmStockList.removeRow(0);
                }
                frames.dlStockFiles.removeAllElements();
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.EmptyInput"));
        }
    }

    //Print stock list
    private void stockListPrint() {
        if (frames.tmStockList.getRowCount() > 0) {
            printer.createPrinterContentForStockList(frames.jtStockList, frames.dlStockFiles);
            printer.printContent();
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.EmptyStock"));
        }
    }

    //Save stock list to excel
    private void stockSaveExcel() {
        if (frames.tmStockList.getRowCount() > 0) {
            fileChooser =
                new JRFileChooser(".XLS", "*.XLS", propertyUtil.getLangText("Dialog.File.Title.Save"),
                                  swc.exelListPath);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                String sFile = fileChooser.getSelectedFile().getAbsolutePath();
                if (!sFile.toLowerCase()
                          .trim()
                          .endsWith(".xls")) {
                    sFile += ".xls";
                }

                File fOut = new File(sFile);
                if (!fOut.exists() ||
                    (fOut.exists() && message.showConfirmDialog(propertyUtil.getLangText("Confirm.FileExists")))) {
                    excelUtil.saveExcelStockList(new File(sFile), frames.jtStockList, frames.dlStockFiles);
                }
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.EmptyStock"));
        }
    }

    //Open stock list
    private void stockListOpen() {
        fileChooser =
            new JRFileChooser(".SLT", "*.SLT", propertyUtil.getLangText("Dialog.File.Title.Open"), swc.stockListPath);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            HashMap<String, Integer> stockSelection = new HashMap<String, Integer>();
            SerializedStockList serializedStockList = new SerializedStockList();

            serializedStockList = (SerializedStockList) ioUtil.openObject(file);

            frames.dlStockFiles = serializedStockList.getFileList();
            frames.jlStockFiles.setModel(frames.dlStockFiles);

            dataBase.createStockCollection(frames.jtStockList, serializedStockList.getSelection());
        }

    }

    //Save stock list
    private void stockListSave() {
        DefaultTableModel tableModel = (DefaultTableModel) frames.jtStockList.getModel();
        HashMap<String, Integer> stockSelection = new HashMap<String, Integer>();
        SerializedStockList serializedStockList = new SerializedStockList();
        if (tableModel.getRowCount() > 0) {

            fileChooser =
                new JRFileChooser(".SLT", "*.SLT", propertyUtil.getLangText("Dialog.File.Title.Save"),
                                  swc.stockListPath);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                String sFile = fileChooser.getSelectedFile().getAbsolutePath();
                if (!sFile.toLowerCase()
                          .trim()
                          .endsWith(".slt")) {
                    sFile += ".slt";
                }

                File fileOut = new File(sFile);
                if (!fileOut.exists() ||
                    (fileOut.exists() &&
                     message.showConfirmDialog(propertyUtil.getLangText("Confirm.StockList.Exists")))) {

                    //Copy items to hashmap
                    for (int row = 0; row < tableModel.getRowCount(); row++) {
                        String SAP = tableModel.getValueAt(row, 1).toString();
                        int count = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                        stockSelection.put(SAP, count);
                    }
                    //Create object for saving
                    serializedStockList.setSelection(stockSelection);
                    serializedStockList.setFileList(frames.dlStockFiles);

                    ioUtil.saveObject(serializedStockList, new File(sFile));

                    message.showInformationMessage(propertyUtil.getLangText("Information.StockList.Saved"));
                }
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.EmptyStock"));
        }
    }

    //Collect stock list
    private void stockListCollect() {
        //Set default values
        if (!frames.nodeHandleYes.isSelected() && !frames.nodeHandleNo.isSelected())
            frames.nodeHandleNo.setSelected(true);
        if (!frames.nodeLiftingMdYes.isSelected() && !frames.nodeLiftingMdNo.isSelected())
            frames.nodeLiftingMdNo.setSelected(true);
        if (!frames.nodeFanYes.isSelected() && !frames.nodeFanNo.isSelected())
            frames.nodeFanNo.setSelected(true);
        if (!frames.nodeRootClosableBar.isSelected() && !frames.nodeNormalBar.isSelected())
            frames.nodeNormalBar.setSelected(true);
        if (!frames.nodeCatchNormal.isSelected() && !frames.nodeCatchMagnet.isSelected())
            frames.nodeCatchNone.setSelected(true);
        if (!frames.nodeCilinderYes.isSelected() && !frames.nodeCilinderNo.isSelected())
            frames.nodeCilinderNo.setSelected(true);
        if (!frames.nodeBarKo.isSelected() && !frames.nodeBarKsr.isSelected() && !frames.nodeBarM.isSelected())
            frames.nodeBarKo.setSelected(true);

        if (frames.dlStockFiles.size() > 0) {
            //Get options from tree
            boolean left = frames.nodeLeft.isSelected();
            boolean right = frames.nodeRight.isSelected();
            boolean over = frames.nodeOver.isSelected();
            boolean handle = frames.nodeHandleYes.isSelected();
            boolean liftingMD = frames.nodeLiftingMdYes.isSelected();
            boolean isMDK = frames.nodeMDK.isSelected();
            boolean isMDF = frames.nodeMDF.isSelected();
            boolean isMDS = frames.nodeMDS.isSelected();
            boolean isMKIPP = frames.nodeMKIPP.isSelected();
            boolean isMDK2 = frames.nodeMDK2.isSelected();
            boolean isMDF2 = frames.nodeMDF2.isSelected();
            boolean isMDS2 = frames.nodeMDS2.isSelected();
            boolean isMFREI = frames.nodeMFREI.isSelected();
            boolean catchNone = frames.nodeCatchNone.isSelected();
            boolean catchNormal = frames.nodeCatchNormal.isSelected();
            boolean catchMagnet = frames.nodeCatchMagnet.isSelected();
            boolean fan = frames.nodeFanYes.isSelected();
            boolean closableBar = frames.nodeRootClosableBar.isSelected();
            boolean normalBar = frames.nodeNormalBar.isSelected();
            boolean cilinder = frames.nodeCilinderYes.isSelected();

            //Get selected bar types
            List<String> selectedBarSizes = new ArrayList<String>();
            if (closableBar) {

                Enumeration e = frames.nodeRootClosableBar.children();
                while (e.hasMoreElements()) {
                    CheckNode node = (CheckNode) e.nextElement();
                    if (node.isSelected()) {
                        selectedBarSizes.add(node.getTitle());
                        //System.out.println(node.getTitle());
                    }
                }
            }

            //Get selected color types
            List<String> selectedColors = new ArrayList<String>();

            Enumeration e = frames.nodeRootColor.children();
            while (e.hasMoreElements()) {
                CheckNode node = (CheckNode) e.nextElement();
                if (node.isSelected()) {
                    String title = node.getTitle();
                    String colorSign = title.substring(title.indexOf("[") + 1, title.indexOf("]")).trim();
                    selectedColors.add(colorSign);
                }
            }
            //Get selected bar types
            List<String> selectedBarTypes = new ArrayList<String>();

            Enumeration bare = frames.nodeRootSelectionBar.children();
            while (bare.hasMoreElements()) {
                CheckNode node = (CheckNode) bare.nextElement();
                if (node.isSelected()) {
                    int index = frames.nodeRootSelectionBar.getIndex(node);
                    String barSign = swc.selectionBarArray[index];
                    //System.out.println(barSign);
                    selectedBarTypes.add(barSign);
                }
            }

            //Iterate on stock files
            for (int i = 0; i < frames.dlStockFiles.size(); i++) {
                boolean clearStockSelection = (i == 0);

                File stockFile = (File) frames.dlStockFiles.get(i);

                dataBase.loadTextDataBase(stockFile, swc.LOAD_STOCK);

                dataBase.selectStockRows(left, right, over, handle, liftingMD, selectedColors, isMDK, isMDF, isMDS,
                                         isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI, catchNone, catchNormal, catchMagnet,
                                         fan, closableBar, selectedBarSizes, normalBar, cilinder, clearStockSelection,
                                         selectedBarTypes);
            }

            dataBase.createStockCollection(frames.jtStockList);
            dataBase.clearSelection();

            if (frames.tmStockList.getRowCount() == 0) {
                message.showInformationMessage(propertyUtil.getLangText("Information.StockList.NoResult"));
            }
            frames.jriItemList.repaint();
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.EmptyInput"));
        }

    }


    //Remove file from stock file list
    private void stockListRemoveFile() {
        Object[] selectedValues = frames.jlStockFiles.getSelectedValues();
        int selectedIndex = frames.jlStockFiles.getSelectedIndex();
        boolean deleteConfirmed = false;

        if (selectedValues != null && selectedValues.length > 0) {
            deleteConfirmed = frames.jcStockFileDeleteConfirm.isSelected();
            if (!deleteConfirmed) {
                deleteConfirmed = message.showConfirmDialog(propertyUtil.getLangText("Confirm.StockList.DeleteFiles"));
            }
            if (deleteConfirmed) {
                for (int i = 0; i < selectedValues.length; i++) {
                    frames.dlStockFiles.removeElement(selectedValues[i]);
                }

                if (frames.dlStockFiles.getSize() < selectedIndex || frames.dlStockFiles.getSize() == selectedIndex) {
                    selectedIndex = frames.dlStockFiles.getSize() - 1;
                }
                frames.jlStockFiles.setSelectedIndex(selectedIndex);
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.NotSelected"));
        }
    }

    //Add file to stock file list
    private void stockListAddFile() {
        fileChooser =
            new JRFileChooser(".PRN", "*.PRN", propertyUtil.getLangText("Dialog.File.Title.Open"), swc.dataBasePath);
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                frames.dlStockFiles.addElement(files[i]);
            }

        }
    }

    //Remove item to stock list
    private void stockListRemoveItem() {
        DefaultTableModel dtm = frames.tmStockList;

        //Get selected rows
        int selectedJTableNumRows = frames.jtStockList
                                          .getSelectedRows()
                                          .length;
        boolean deleteConfirmed = false;

        if (selectedJTableNumRows > 0 && dtm.getRowCount() > 0) {
            deleteConfirmed = frames.jcStockItemDeleteConfirm.isSelected();
            if (!deleteConfirmed) {
                deleteConfirmed = message.showConfirmDialog(propertyUtil.getLangText("Confirm.StockList.Delete"));
            }
            if (deleteConfirmed) {

                int selectedRowIndex = 0;
                for (int i = 0; i < selectedJTableNumRows; i++) {
                    //Set selected row index to change selection to an existing item
                    selectedRowIndex = frames.jtStockList.getSelectedRow();

                    //Convert sorted jtable row index to table model
                    int selectedModelRowIndex =
                        frames.jtStockList.convertRowIndexToModel(frames.jtStockList.getSelectedRow());
                    dtm.removeRow(selectedModelRowIndex);
                }

                if (dtm.getRowCount() == selectedRowIndex) {
                    selectedRowIndex--;
                }

                //Change selection
                Rectangle rect = frames.jtStockList.getCellRect(selectedRowIndex, 0, true);
                frames.jtStockList.scrollRectToVisible(rect);
                frames.jtStockList.clearSelection();
                if (frames.jtStockList.getRowCount() > 0) {
                    frames.jtStockList.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
                    dtm.fireTableDataChanged(); // notify the model
                    frames.jtStockList
                          .getSelectionModel()
                          .setSelectionInterval(selectedRowIndex, selectedRowIndex);
                }
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.StockList.NotSelected"));
        }
    }

    //Add item to stock list
    private void stockListAddItem() {
        frames.jriPriceListSelect.setVisible(false);
        //Swich off sorting
        frames.jtStockList.setAutoCreateRowSorter(true);

        DefaultTableModel dtm = frames.tmStockList;

        int selectedRowIndex = frames.jtStockList.getSelectedRow();

        //Get selected item from price list
        String priceListSAP = frames.jtPriceListSelect
                                    .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 0)
                                    .toString();
        String priceListText = frames.jtPriceListSelect
                                     .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 1)
                                     .toString();

        //Add new row
        dtm.addRow(new Object[] { "1", priceListSAP, priceListText,
                                  swc.formatter.format(priceUtil.getListPriceCalculated(priceListSAP)) });

        if (selectedRowIndex == -1) {
            selectedRowIndex = dtm.getRowCount() - 1;
        } else {
            selectedRowIndex++;
        }

        Rectangle rect = frames.jtStockList.getCellRect(selectedRowIndex, 0, true);
        frames.jtStockList.scrollRectToVisible(rect);
        frames.jtStockList.clearSelection();
        if (frames.jtStockList.getRowCount() > 0) {
            frames.jtStockList.setRowSelectionInterval(selectedRowIndex, selectedRowIndex);
            dtm.fireTableDataChanged(); // notify the model
            frames.jtStockList
                  .getSelectionModel()
                  .setSelectionInterval(selectedRowIndex, selectedRowIndex);
        }

    }

    //Show currencies
    private void showStockList() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriItemList.setVisibleToFocus();
        //Reset sorting
        frames.jtStockList.setAutoCreateRowSorter(true);
    }

    //Refresh list frame if visible, when there were any modifications
    private void refreshLists() {
        dataBase.createListItems(frames.jlCollectionEdit);
        String lastSelectedListType = dataBase.getCollectionList().getSelectedListType();
        if (lastSelectedListType != null && frames.jriCollectionList.isVisible()) {
            if (lastSelectedListType.equals(swc.LIST_DETAILED)) {

                dataBase.createListDetailed((DefaultTableModel) frames.jtCollectionList.getModel());

            } else if (lastSelectedListType.equals(swc.LIST_GARNITURE)) {

                dataBase.createListGarniture((DefaultTableModel) frames.jtCollectionList.getModel());

            } else if (lastSelectedListType.equals(swc.LIST_SUMMARIZED)) {

                dataBase.createListSummarized((DefaultTableModel) frames.jtCollectionList.getModel());

            }
        }
    }

    //Refresh main sum price
    private void refreshMainSumPrice() {
        frames.tfMainSumPrice.setText(swc.formatter.format(dataBase.getTotalPrice()) + " " + priceUtil.getCurrency());
    }

    //Show currencies
    private void showCurrencies() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriCurrency.setVisibleToFocus();
    }

    //Close database
    private void closeDataBase() {
        dataBase.closeDataBase();

        frames.jtLeftToolBar.setVisible(false);
        frames.mainSplitPane.setDividerLocation(0);
        frames.jlMainDatabaseName.setText("...");
        frames.jlMainDatabaseName.setToolTipText("...");
        DefaultTableModel tm = (DefaultTableModel) frames.jtCollection.getModel();
        while (frames.jtCollection.getRowCount() > 0) {
            tm.removeRow(0);
        }
        dataBase.setType("");
        dataBase.setLoaded(false);

        frames.jntWidth.setSize(0, 0);
        frames.jntWidth2.setSize(0, 0);
        frames.jntHeight.setSize(0, 0);
        frames.jntHeight2.setSize(0, 0);

        windowTypes.setClassification("");
        frames.canvas.repaint();
    }

    //Replace data
    private void replaceData() {
        ReplaceTableModel tm = (ReplaceTableModel) frames.jtReplaceData.getModel();
        String messageText = propertyUtil.getLangText("Information.ReplaceData.EmptyInput.Ready");
        frames.jtReplaceInfo.setText("");
        int allHits = 0;
        frames.jtReplaceData.editCellAt(0, 0);

        try {
            //Iterate on files
            for (int i = 0; i < frames.dlReplaceFiles.getSize(); i++) {

                frames.jlReplaceFies.setSelectedIndex(i);

                String filePath = ((File) frames.dlReplaceFiles.getElementAt(i)).getAbsolutePath();

                String fileContent = ioUtil.loadUTF8(filePath);

                //Set false all check boxes
                for (int row = 0; row < tm.getRowCount(); row++) {
                    tm.setValueAt(false, row, 0);
                }

                //Replace SAP
                if (frames.jrReplaceSAP.isSelected()) {

                    //Iterate on replace values
                    for (int row = 0; row < tm.getRowCount(); row++) {
                        String replaceFrom = tm.getValueAt(row, 1)
                                               .toString()
                                               .trim();
                        String replaceTo = tm.getValueAt(row, 2)
                                             .toString()
                                             .trim();


                        if (replaceFrom.length() == 6 && replaceTo.length() == 6 &&
                            priceUtil.isSAPExists(replaceFrom) && priceUtil.isSAPExists(replaceTo)) {

                            //Get replace count for info
                            int hits = 0;
                            StringTokenizer tokenizer = new StringTokenizer(fileContent.toString());
                            while (tokenizer.hasMoreTokens()) {
                                String token = tokenizer.nextToken();
                                if (token.contains(replaceFrom)) {
                                    hits++;
                                }
                            }

                            allHits += hits;
                            //Add comment
                            frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceFrom") +
                                                        ": " + replaceFrom + "   =>   ");
                            frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceTo") +
                                                        ": " + replaceTo + "    ");
                            frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceCount") +
                                                        ": " + hits + " ");
                            frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.Piece") +
                                                        ".\n");

                            fileContent = fileContent.replaceAll(replaceFrom, replaceTo);

                            tm.setValueAt(true, row, 0);
                        } else {
                            System.out.println(!priceUtil.isSAPExists(replaceFrom));
                            if (!priceUtil.isSAPExists(replaceFrom))
                                frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.InvalidSAP") +
                                                            ": " + replaceFrom + "\n");
                            if (!priceUtil.isSAPExists(replaceTo))
                                frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.InvalidSAP") +
                                                            ": " + replaceTo + "\n");

                        }

                    }

                    fileContent = fileContent.replaceAll("\n", "\r\n");

                    messageText = propertyUtil.getLangText("Information.ReplaceData.SAP.Ready");
                }

                //Replace text
                else {
                    StringReader reader = new StringReader(fileContent.trim());
                    BufferedReader in = new BufferedReader(reader);
                    String line;
                    StringBuffer content = new StringBuffer();
                    int SAPFrom =
                        propertyUtil.getIntProperty(propertyUtil.getDatabaseProperty(), "Database.Row.SAP.From");
                    int SAPTo = propertyUtil.getIntProperty(propertyUtil.getDatabaseProperty(), "Database.Row.SAP.To");
                    int TextFrom =
                        propertyUtil.getIntProperty(propertyUtil.getDatabaseProperty(), "Database.Row.Text.From");
                    int TextTo =
                        propertyUtil.getIntProperty(propertyUtil.getDatabaseProperty(), "Database.Row.Text.To");
                    int counter = 1;
                    TextDBRow dbRow = new TextDBRow();

                    while ((line = in.readLine()) != null) {
                        if (line.length() > 0) {
                            StringBuffer sbLine = new StringBuffer(line);

                            if (counter > 1) {
                                String SAP = line.substring(SAPFrom, SAPTo).trim();
                                String NewText = dbRow.createRowItem("Text", priceUtil.getListText(SAP));

                                //Add comment
                                frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceFrom") +
                                                            ": " + sbLine.toString()
                                                                         .substring(TextFrom, TextTo)
                                                                         .trim() + "   =>   ");
                                frames.jtReplaceInfo.append(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceTo") +
                                                            ": " + NewText.trim() + "\n");

                                sbLine = sbLine.replace(TextFrom, TextTo, NewText);

                                allHits++;
                            }

                            content.append(sbLine.toString() + "\r\n");
                        }
                        counter++;
                    }

                    fileContent = content.toString();

                    messageText = propertyUtil.getLangText("Information.ReplaceData.Text.Ready");
                }

                //Save file
                ioUtil.saveUTF8(filePath, fileContent);

            }

            frames.jtReplaceInfo.append("\n" + propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceSum") + ": " +
                                        allHits);
            message.showInformationMessage(messageText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Remove item from replace list
    private void replaceListRemoveItem() {
        Object[] selectedValues = frames.jlReplaceFies.getSelectedValues();
        int selectedIndex = frames.jlReplaceFies.getSelectedIndex();

        if (selectedValues != null && selectedValues.length > 0) {
            for (int i = 0; i < selectedValues.length; i++) {
                frames.dlReplaceFiles.removeElement(selectedValues[i]);
            }

            if (frames.dlReplaceFiles.getSize() < selectedIndex || frames.dlReplaceFiles.getSize() == selectedIndex) {
                selectedIndex = frames.dlReplaceFiles.getSize() - 1;
            }
            frames.jlReplaceFies.setSelectedIndex(selectedIndex);
        }

    }

    //Add item to replace list
    private void replaceListAddItem() {
        fileChooser =
            new JRFileChooser(".PRN", "*.PRN", propertyUtil.getLangText("Dialog.File.Title.Open"), swc.dataBasePath);
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (int i = 0; i < files.length; i++) {
                frames.dlReplaceFiles.addElement(files[i]);
            }

        }
    }

    //Add item to replace data
    private void replaceDataRemoveItem() {
        int selectedRowIndex = frames.jtReplaceData.getSelectedRow();
        if (selectedRowIndex > -1) {
            ReplaceTableModel tm = (ReplaceTableModel) frames.jtReplaceData.getModel();
            tm.removeRow(selectedRowIndex);
            if (tm.getRowCount() == selectedRowIndex) {
                selectedRowIndex--;
            }
            tm.showCell(selectedRowIndex, 0);
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.ReplaceData.NotSelected"));
        }
    }

    //Add item to replace data
    private void replaceDataAddItem() {
        ReplaceTableModel tm = (ReplaceTableModel) frames.jtReplaceData.getModel();
        tm.addRow(new Object[] { false, "", "" });
    }

    //Show replace data
    private void showReplaceData() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriReplaceData.setVisibleToFocus();
    }

    //Exit from program
    private void programExit() {
        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.Program.Exit"))) {

            Viewer viewer =
                new Viewer(propertyUtil.getLangText("Frame.Main.Commercial"),
                           new ImageIcon(swc.viewerPath + "Commercial.jpg"));
            frames.jrfMain.setVisible(false);
            viewer.showExitImage();
            viewer.setExitOnShutDown(true);
            viewer.shutDown(5000);
        }
    }

    //Restart program
    private void programRestart() {
        Runtime runtime = Runtime.getRuntime();
        File location = new File(".\\");
        String path = location.getAbsolutePath();
        String commandBat = path.substring(0, path.lastIndexOf("\\")) + "\\Roto.bat";
        String commandExe = path.substring(0, path.lastIndexOf("\\")) + "\\Roto.exe";
        frames.jrfMain.setVisible(false);
        try {
            if (new File(commandExe).exists()) {
                runtime.exec(commandExe);
            } else if (new File(commandBat).exists()) {
                runtime.exec(commandBat);
            }
        } catch (IOException e) {
            message.showErrorMessage(e.getMessage(), "");
        }

        Timer timer = new Timer(5000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                System.gc();
                System.exit(0);
            }
        });
        timer.start();
    }

    //Move item in edit database list
    private void editDataBaseMoveItem(int direction) {
        DataBaseTableModel dtm = (DataBaseTableModel) frames.jtEditDataBase.getModel();
        int selectedRowIndex = frames.jtEditDataBase.getSelectedRow();
        if (selectedRowIndex > -1 && dtm.getRowCount() > 0 && selectedRowIndex + direction < dtm.getRowCount() &&
            selectedRowIndex + direction > -1) {
            dtm.moveRow(selectedRowIndex, selectedRowIndex, selectedRowIndex + direction);
            dtm.showCell(selectedRowIndex + direction, 0);
        }
    }

    //Remove item from edit database list
    private void editDataBaseRemoveItem() {
        DataBaseTableModel dtm = (DataBaseTableModel) frames.jtEditDataBase.getModel();
        dtm.setEditCellValue(null);
        int selectedRowIndex = frames.jtEditDataBase.getSelectedRow();
        boolean deleteConfirmed = false;
        if (selectedRowIndex > -1 && dtm.getRowCount() > 0) {
            deleteConfirmed = frames.jcDbEditDeleteConfirm.isSelected();
            if (!deleteConfirmed) {
                deleteConfirmed = message.showConfirmDialog(propertyUtil.getLangText("Confirm.DataBaseEdit.Delete"));
            }
            if (deleteConfirmed) {
                dtm.removeRow(selectedRowIndex);
                if (dtm.getRowCount() == selectedRowIndex) {
                    selectedRowIndex--;
                }
                dtm.showCell(selectedRowIndex, 0);
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.NotSelected"));
        }

    }

    //Add new item to edit database list
    private void editDataBaseAddItem() {
        frames.jriPriceListSelect.setVisible(false);

        DataBaseTableModel dtm = (DataBaseTableModel) frames.jtEditDataBase.getModel();
        dtm.setEditCellValue(null);

        int selectedRowIndex = frames.jtEditDataBase.getSelectedRow();

        //Get selected item from price list
        String priceListSAP = frames.jtPriceListSelect
                                    .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 0)
                                    .toString();
        String priceListText = frames.jtPriceListSelect
                                     .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 1)
                                     .toString();

        //Add new row
        dtm.addRow(new Object[] { "1", priceListSAP, "", priceListText, true, true, true, true, "", "", "0", "0", "0",
                                  "0", "0", "0", "", "", true, true, true, true });

        if (selectedRowIndex == -1) {
            selectedRowIndex = dtm.getRowCount() - 1;
        } else {
            selectedRowIndex++;
        }
        dtm.showCell(selectedRowIndex, 0);

        //Move row to selected position
        dtm.moveRow(frames.jtEditDataBase.getRowCount() - 1, frames.jtEditDataBase.getRowCount() - 1, selectedRowIndex);


        frames.jtEditDataBase.repaint();
        frames.jtEditDataBase
              .getSelectionModel()
              .setSelectionInterval(selectedRowIndex, selectedRowIndex);

    }

    //Check text values
    private String checkHeaderValue(String value) {
        if (value.trim().length() == 0) {
            return "   -   ";
        } else {
            return value;
        }
    }

    //Set database headers by selected types
    private void editDataBaseTypeSelected() {
        if (frames.jcbEditDatabaseTyes.getItemCount() > 0) {
            String type = frames.jcbEditDatabaseTyes
                                .getSelectedItem()
                                .toString();
            WindowSystem ws = windowSytems.getWindowSystemByTypeName(type);
            WindowTableModel tm = ws.getWindowTableModel();

            int widthMultiplier = 9;

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(4)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(0)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(4)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(0)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(5)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(1)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(5)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(1)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(6)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(2)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(6)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(2)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(7)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(3)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(7)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(3)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(18)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(4)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(18)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(4)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(19)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(5)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(19)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(5)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(20)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(6)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(20)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(6)).length() * widthMultiplier);

            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(21)
                  .setHeaderValue(checkHeaderValue(tm.getMenuText(7)));
            frames.jtEditDataBase
                  .getColumnModel()
                  .getColumn(21)
                  .setPreferredWidth(checkHeaderValue(tm.getMenuText(7)).length() * widthMultiplier);

            frames.jriEditDataBase.repaint();
        }
    }

    //Set SAP for replace
    private void replaceDataSetSAP() {
        frames.jriPriceListSelect.setVisible(false);

        int selectedRowIndex = frames.jtReplaceData.getSelectedRow();
        int selectedCoumnIndex = frames.jtReplaceData.getSelectedColumn();

        //Get selected item from price list
        String priceListSAP = frames.jtPriceListSelect
                                    .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 0)
                                    .toString();

        frames.jtReplaceData.editCellAt(selectedRowIndex, 0);
        frames.jtReplaceData.setValueAt(priceListSAP, selectedRowIndex, selectedCoumnIndex);
    }

    //Set SAP for edit database
    private void editDataBaseSetSAP() {
        frames.jriPriceListSelect.setVisible(false);
        DataBaseTableModel dt = (DataBaseTableModel) frames.jtEditDataBase.getModel();

        int selectedRowIndex = frames.jtEditDataBase.getSelectedRow();
        int selectedCoumnIndex = frames.jtEditDataBase.getSelectedColumn();

        //Get selected item from price list
        String priceListSAP = frames.jtPriceListSelect
                                    .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 0)
                                    .toString();
        String priceListText = frames.jtPriceListSelect
                                     .getValueAt(frames.jtPriceListSelect.getSelectedRow(), 1)
                                     .toString();

        //Set cell value
        dt.setEditCellValue(priceListSAP);

        frames.jtEditDataBase.editCellAt(selectedRowIndex, 0);
        frames.jtEditDataBase.setValueAt(priceListSAP, selectedRowIndex, selectedCoumnIndex);
        if (selectedCoumnIndex == 1) {
            frames.jtEditDataBase.setValueAt(priceListText, selectedRowIndex, 3);
        }
        frames.jtEditDataBase.editCellAt(selectedRowIndex, selectedCoumnIndex);

    }


    //Save database Excel
    private void saveDataBaseExcel() {
        if (frames.jtEditDataBase.getRowCount() > 0) {
            fileChooser =
                new JRFileChooser(".XLS", "*.XLS", propertyUtil.getLangText("Dialog.File.Title.Save"),
                                  swc.exelListPath);
            String fileName = null;
            try {
                fileName = dataBase.getEditingDataBaseFile().getName();
                fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".xls";
                fileChooser.setSelectedFile(new File(fileName));
            } catch (Exception e) {
            }

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File outPut = fileChooser.getSelectedFile();
                String filePath = outPut.getAbsolutePath();

                if (!filePath.toLowerCase()
                             .trim()
                             .endsWith(".xls")) {
                    filePath += ".xls";
                    outPut = new File(filePath);
                }

                if (!outPut.exists() ||
                    (outPut.exists() &&
                     message.showConfirmDialog(propertyUtil.getLangText("Confirm.OpenList.Exists")))) {

                    excelUtil.saveExcelDataBase(outPut, frames.jtEditDataBase);
                }
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.Save.Empty"));
        }
    }

    //Save database edit
    private void saveDataBaseEdit() {
        if (frames.jtEditDataBase.getRowCount() > 0) {

            File outPut = dataBase.getEditingDataBaseFile();

            String filePath = outPut.getAbsolutePath();

            if (!filePath.toLowerCase()
                         .trim()
                         .endsWith(".prn")) {
                filePath += ".prn";
                outPut = new File(filePath);
            }

            if (message.showConfirmDialog(propertyUtil.getLangText("Information.DataBaseEdit.Save.Confirm"))) {
                dataBase.saveTextDataBase(outPut, frames.jtEditDataBase);
            }

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.Save.Empty"));
        }
    }

    //Save as database edit
    private void saveAsDataBaseEdit() {
        if (frames.jtEditDataBase.getRowCount() > 0) {
            fileChooser =
                new JRFileChooser(".PRN", "*.PRN", propertyUtil.getLangText("Dialog.File.Title.Save"),
                                  swc.dataBasePath);
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File outPut = fileChooser.getSelectedFile();
                String filePath = outPut.getAbsolutePath();

                if (!filePath.toLowerCase()
                             .trim()
                             .endsWith(".prn")) {
                    filePath += ".prn";
                    outPut = new File(filePath);
                }

                if (!outPut.exists() ||
                    (outPut.exists() && !outPut.getName()
                                                                   .toLowerCase()
                                                                   .endsWith(" .prn") &&
                     message.showConfirmDialog(propertyUtil.getLangText("Information.DataBaseEdit.Save.Exists")))) {
                    dataBase.saveTextDataBase(outPut, frames.jtEditDataBase);
                }
                // Mustn't overwrite files that has a space before dot
                else if (outPut.getName()
                               .toLowerCase()
                               .endsWith(" .prn")) {
                    message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.Save.Protected"));
                }
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.DataBaseEdit.Save.Empty"));
        }
    }

    //Close database edit
    private void closeDataBaseEdit() {
        if (frames.jtEditDataBase.getRowCount() == 0 ||
            (frames.jtEditDataBase.getRowCount() > 0 &&
             message.showConfirmDialog(propertyUtil.getLangText("Confirm.DataBaseEdit.ConfirmOpen")))) {

            DataBaseTableModel dtModel = (DataBaseTableModel) frames.jtEditDataBase.getModel();
            dtModel.setEditCellValue(null);

            dataBase.setEditingDataBaseFile(new File(""));
            frames.jlEditingDbName.setText(dataBase.getEditingDataBaseFile().getName());

            frames.jtEditDataBase.setEditingRow(0);

            while (dtModel.getRowCount() > 0) {
                dtModel.removeRow(0);
            }

            frames.jriEditDataBase.setVisible(false);


        }
    }

    //Open an empty database for edit
    private void openNewDataBaseEdit() {
        if (frames.jtEditDataBase.getRowCount() == 0 ||
            (frames.jtEditDataBase.getRowCount() > 0 &&
             message.showConfirmDialog(propertyUtil.getLangText("Confirm.DataBaseEdit.ConfirmOpen")))) {

            frames.jbEditDbSave.setEnabled(false);
            frames.jmiSave.setEnabled(false);

            dataBase.setEditingDataBaseFile(new File(""));
            frames.jlEditingDbName.setText(dataBase.getEditingDataBaseFile().getName());

            DataBaseTableModel dtModel = (DataBaseTableModel) frames.jtEditDataBase.getModel();
            dtModel.setEditCellValue(null);

            frames.jtEditDataBase.setEditingRow(0);

            frames.mainTabPane.setSelectedIndex(swc.tabMenus);
            frames.jriEditDataBase.setVisibleToFocus();

            String defaultType = null;
            //Load window types to combo box
            List<String> typeList = windowSytems.getAllWindowSystemTypes();
            frames.jcbEditDatabaseTyes.removeAllItems();
            for (String type : typeList) {
                frames.jcbEditDatabaseTyes.addItem(type);
                if (defaultType == null)
                    defaultType = type;
            }

            dataBase.createDataBaseEdit(frames.jtEditDataBase, true);

            //Set editing type
            frames.jcbEditDatabaseTyes.setSelectedItem(defaultType);

        }
    }

    //Open database for edit
    private void openDataBaseEdit() {
        if (frames.jtEditDataBase.getRowCount() == 0 ||
            (frames.jtEditDataBase.getRowCount() > 0 &&
             message.showConfirmDialog(propertyUtil.getLangText("Confirm.DataBaseEdit.ConfirmOpen")))) {


            DataBaseTableModel dtModel = (DataBaseTableModel) frames.jtEditDataBase.getModel();
            dtModel.setEditCellValue(null);

            frames.jtEditDataBase.setEditingRow(0);

            while (dtModel.getRowCount() > 0) {
                dtModel.removeRow(0);
            }

            fileChooser =
                new JRFileChooser(".PRN", "*.PRN", propertyUtil.getLangText("Dialog.File.Title.Open"),
                                  swc.dataBasePath);
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                frames.jbEditDbSave.setEnabled(true);
                frames.jmiSave.setEnabled(true);
                //Disable save if file name contains a space before dot
                if (fileChooser.getSelectedFile()
                               .getName()
                               .toLowerCase()
                               .endsWith(" .prn")) {
                    frames.jbEditDbSave.setEnabled(false);
                    frames.jmiSave.setEnabled(false);
                }

                dataBase.setEditingDataBaseFile(fileChooser.getSelectedFile());
                frames.jlEditingDbName.setText(dataBase.getEditingDataBaseFile().getName());

                //Reset sorter
                //frames.jtEditDataBase.setAutoCreateRowSorter(false);
                //frames.jtEditDataBase.setAutoCreateRowSorter(true);
                dataBase.loadTextDataBase(fileChooser.getSelectedFile(), swc.LOAD_EDIT);
                frames.mainTabPane.setSelectedIndex(swc.tabMenus);
                frames.jriEditDataBase.setVisibleToFocus();
                //Load window types to combo box
                List<String> typeList = windowSytems.getAllWindowSystemTypes();
                frames.jcbEditDatabaseTyes.removeAllItems();
                for (String type : typeList) {
                    frames.jcbEditDatabaseTyes.addItem(type);
                }
                dataBase.createDataBaseEdit(frames.jtEditDataBase, false);
                //Set editing type
                frames.jcbEditDatabaseTyes.setSelectedItem(dataBase.getEditingType());

            }
        }
    }

    //Show database data
    private void showDataBaseWidthInfo() {
        Viewer viewer =
            new Viewer(propertyUtil.getLangText("Error.Width") + ": " +
                       (dataBase.getMinWidth() + dataBase.getCorrectionWidth()) + " - " +
                       (dataBase.getMaxWidth() + dataBase.getCorrectionWidth()));
        viewer.startUp();
        viewer.setLocation(mouse.getCursorX(), mouse.getCursorY() - 50);
        viewer.shutDown(3500);
    }

    //Show database data
    private void showDataBaseHeightInfo() {
        //Show message
        Viewer viewer =
            new Viewer(propertyUtil.getLangText("Error.Height") + ": " +
                       (dataBase.getMinHeight() + dataBase.getCorrectionHeight()) + " - " +
                       (dataBase.getMaxHeight() + dataBase.getCorrectionHeight()));
        viewer.startUp();
        viewer.setLocation(mouse.getCursorX(), mouse.getCursorY() - 50);
        viewer.shutDown(3500);
    }

    //Print List
    private void printList() {
        printer.createPrinterContentForList(frames.jtExcelList, frames.jtExcelHeader, frames.jtExcelComment
                                                                                            .getText()
                                                                                            .trim());
        printer.printContent();
    }

    //Change header type
    private void headerSelected(String headerType) {
        Header header = new Header();
        header.createExcelHeader(frames.jtExcelHeader, headerType);
    }

    //Save collection list to excel
    private void listExcelSave() {
        fileChooser =
            new JRFileChooser(".XLS", "*.XLS", propertyUtil.getLangText("Dialog.File.Title.Save"), swc.exelListPath);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            String sFile = fileChooser.getSelectedFile().getAbsolutePath();
            if (!sFile.toLowerCase()
                      .trim()
                      .endsWith(".xls")) {
                sFile += ".xls";
            }

            File fOut = new File(sFile);
            if (!fOut.exists() ||
                (fOut.exists() && message.showConfirmDialog(propertyUtil.getLangText("Confirm.FileExists")))) {
                excelUtil.saveExcelList(new File(sFile), frames.jtExcelList, frames.jtExcelHeader, frames.jtExcelComment
                                                                                                         .getText()
                                                                                                         .trim());
            }
        }
    }

    //Show excel list to save
    private void list_Excel_Print_PDF_Show(String listType, String action) {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriExcelList.setVisibleToFocus();

        frames.jbSaveExcelList.setVisible(action == swc.LIST_ACTION_EXCEL);
        frames.jbPrintExcelList.setVisible(action == swc.LIST_ACTION_PRINT);
        //frames.pnHeaderSelector.setVisible(action == swc.LIST_ACTION_PRINT);

        //Create excel header
        Header header = new Header();
        String headerType = swc.HEADER_ORDER;
        if (frames.jrOffer.isSelected())
            headerType = swc.HEADER_OFFER;

        header.createExcelHeader(frames.jtExcelHeader, headerType);

        if (listType.equals(swc.LIST_DETAILED)) {
            dataBase.createListDetailed((DefaultTableModel) frames.jtExcelList.getModel());

            //Set SAP column to second place
            int index = frames.jtExcelList
                              .getColumnModel()
                              .getColumnIndex(propertyUtil.getLangText("InternalFrame.CollectionList.SAP"));
            frames.jtExcelList.moveColumn(index, 1);

        } else if (listType.equals(swc.LIST_SUMMARIZED)) {
            dataBase.createListSummarized((DefaultTableModel) frames.jtExcelList.getModel());

            //Set SAP column to first place
            int index = frames.jtExcelList
                              .getColumnModel()
                              .getColumnIndex(propertyUtil.getLangText("InternalFrame.CollectionList.SAP"));
            frames.jtExcelList.moveColumn(index, 0);

        } else if (listType.equals(swc.LIST_GARNITURE)) {
            dataBase.createListGarniture((DefaultTableModel) frames.jtExcelList.getModel());

            //Set SAP column to second place
            int index = frames.jtExcelList
                              .getColumnModel()
                              .getColumnIndex(propertyUtil.getLangText("InternalFrame.CollectionList.SAP"));
            frames.jtExcelList.moveColumn(index, 1);
        }

    }


    //Save collection list
    private void listSave(String listType) {
        String extension = ".LST";
        String description = "*.LST";
        if (listType.equals(swc.LIST_NAVISON)) {
            extension = ".TXT";
            description = "*.TXT";
        } else if (listType.equals(swc.LIST_PROJECT)) {
            extension = ".PRO";
            description = "*.PRO";
        }

        fileChooser =
            new JRFileChooser(extension, description, propertyUtil.getLangText("Dialog.File.Title.Save"), swc.listPath);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            DataBaseCollectionList collectionList = dataBase.getCollectionList();

            String sFile = fileChooser.getSelectedFile().getAbsolutePath();
            if (!sFile.toLowerCase()
                      .trim()
                      .endsWith(extension.toLowerCase())) {
                sFile += extension.toLowerCase();
            }

            File fileOut = new File(sFile);

            if (!fileOut.exists() ||
                (fileOut.exists() && message.showConfirmDialog(propertyUtil.getLangText("Confirm.OpenList.Exists")))) {
                if (listType.equals(swc.LIST_PROJECT)) {
                    ioUtil.saveSimpleText(fileOut, collectionList.getProjectCollectionList());
                } else if (listType.equals(swc.LIST_NAVISON)) {
                    ioUtil.saveUTF8(fileOut, collectionList.getNavisonCollectionList());
                } else {
                    ioUtil.saveObject(collectionList.getSerializedCollectionList(listType), new File(sFile));
                }
            }
        }
    }

    //Open collection list
    private void listOpen(boolean isConcat) {
        if (dataBase.getCollectionCount() == 0 || isConcat ||
            (dataBase.getCollectionCount() > 0 &&
             message.showConfirmDialog(propertyUtil.getLangText("Confirm.OpenList.ConfirmOpen")))) {
            fileChooser =
                new JRFileChooser(".LST", "*.LST", propertyUtil.getLangText("Dialog.File.Title.Open"), swc.listPath);
            //Set multi selection if concat
            fileChooser.setMultiSelectionEnabled(isConcat);

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                File[] files = fileChooser.getSelectedFiles();

                if (!isConcat) {
                    dataBase.deleteCollectionList();
                    files = new File[1];
                    files[0] = fileChooser.getSelectedFile();
                }

                //Iterate on selected files (only one if not concat)
                for (int i = 0; i < files.length; i++) {

                    //Concat or add list
                    List<SerializableDataBaseCollection> serializedCollectionList =
                        new ArrayList<SerializableDataBaseCollection>();
                    serializedCollectionList = (List<SerializableDataBaseCollection>) ioUtil.openObject(files[i]);

                    for (SerializableDataBaseCollection serializedCollection : serializedCollectionList) {

                        DataBaseCollection collection =
                            new DataBaseCollection(serializedCollection.getSelection(), frames.jtCollection);

                        //Set collection properties
                        collection.setDescriptions(serializedCollection.getDescriptions());
                        collection.setGarniture(serializedCollection.getGarniture());
                        collection.setTypeName(serializedCollection.getTypeName());
                        collection.setHeight(serializedCollection.getHeight());
                        collection.setHeight2(serializedCollection.getHeight2());
                        collection.setWidth(serializedCollection.getWidth());
                        collection.setWidth2(serializedCollection.getWidth2());

                        collection.setColorName(serializedCollection.getColorName());
                        collection.setDirection(serializedCollection.getDirection());
                        collection.setDatabaseName(serializedCollection.getDatabaseName());
                        dataBase.getCollectionList().addCollection(collection);
                    }
                }

                //Refresh main sum price
                refreshMainSumPrice();

                //Refresh lists
                refreshLists();
            }
        }
    }

    //Delete collection list
    private void listDelete(boolean confirmed) {
        if (confirmed || message.showConfirmDialog(propertyUtil.getLangText("Confirm.CollectionList.Delete"))) {
            dataBase.deleteCollectionList();
            //Refresh data
            removeTableItems(frames.jtCollectionEdit);
            removeTableItems(frames.jtCollectionList);
            frames.dlCollectionEdit.removeAllElements();

            //Refresh main sum price
            refreshMainSumPrice();

            //Refresh lists
            refreshLists();
        }
    }

    //Remove all items from a JTable
    private void removeTableItems(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    //Show and edit collection list items
    private void showListItems() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriCollectionEdit.setVisibleToFocus();
        dataBase.createListItems(frames.jlCollectionEdit);
    }

    //Show summarized collection list
    private void showListSummarized() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriCollectionList.setVisibleToFocus();
        dataBase.createListSummarized((DefaultTableModel) frames.jtCollectionList.getModel());
    }

    //Show garniture collection list
    private void showListGarniture() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriCollectionList.setVisibleToFocus();
        dataBase.createListGarniture((DefaultTableModel) frames.jtCollectionList.getModel());
    }

    //Show detailed collection list
    private void showListDetailed() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriCollectionList.setVisibleToFocus();
        dataBase.createListDetailed((DefaultTableModel) frames.jtCollectionList.getModel());
    }


    //Modify collection item count
    private void collectionListItemModify(int modifyValue) {
        collectionListItemModify(modifyValue, false);
    }

    //Modify collection item count
    private void collectionListItemModify() {
        collectionListItemModify(0, true);
    }

    //Modify collection item count
    private void collectionListItemModify(int modifyValue, boolean setTableValue) {
        int collectionRowIndex = frames.jtCollectionEdit.getSelectedRow();
        int collectionListIndex = frames.jlCollectionEdit.getSelectedIndex();

        if (collectionListIndex > -1 && collectionRowIndex > -1) {
            frames.jtCollectionEdit.editCellAt(0, 1);
            String SAP = frames.tmCollectionEdit
                               .getValueAt(collectionRowIndex, 1)
                               .toString();
            String itemValue = frames.tmCollectionEdit
                                     .getValueAt(collectionRowIndex, 0)
                                     .toString();

            DataBaseCollectionList collectionList = dataBase.getCollectionList();

            if (!setTableValue) {
                //Increase or decrease item count
                collectionList.modifyCollectionItemCount(collectionListIndex, SAP, modifyValue);
            } else {
                //Set item count
                collectionList.setCollectionItemCount(collectionListIndex, SAP, Integer.parseInt(itemValue));
            }

            //Refresh list
            dataBase.createListItemAt(frames.tmCollectionEdit, collectionListIndex);

            //Refresh main sum price
            refreshMainSumPrice();

            //Refresh lists
            refreshLists();

            frames.jlCollectionEdit.setSelectedIndex(collectionListIndex);
            frames.jtCollectionEdit
                  .getSelectionModel()
                  .setSelectionInterval(collectionRowIndex, collectionRowIndex);
        }
    }

    //Remove collection from list
    private void collectionListRemove() {
        int collectionListIndex = frames.jlCollectionEdit.getSelectedIndex();
        if (collectionListIndex > -1 &&
            message.showConfirmDialog(propertyUtil.getLangText("Confirm.Collection.Delete"))) {
            DataBaseCollectionList collectionList = dataBase.getCollectionList();
            collectionList.removeCollection(collectionListIndex);

            //Refresh list
            collectionList.createListItems(frames.jlCollectionEdit);

            if (frames.dlCollectionEdit.getSize() > collectionListIndex) {
                frames.jlCollectionEdit.setSelectedIndex(collectionListIndex);
            } else {
                frames.jlCollectionEdit.setSelectedIndex(collectionListIndex - 1);
            }

            //Remove table items if no items left
            if (frames.dlCollectionEdit.getSize() == 0) {
                while (frames.tmCollectionEdit.getRowCount() > 0) {
                    frames.tmCollectionEdit.removeRow(0);
                }
            }

            //Refresh main sum price
            refreshMainSumPrice();

            //Refresh lists
            refreshLists();

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Collection.NotSelected"));
        }
    }

    //Add new item to a collection in the collection list
    private void collectionListAddItem() {
        int collectionListIndex = frames.jlCollectionEdit.getSelectedIndex();
        int selectedPriceListRowIndex = frames.jtPriceListSelect.getSelectedRow();
        int collectionRowIndex = frames.jtCollectionEdit.getSelectedRow();

        if (collectionListIndex > -1) {
            //Get selected item from price list
            String SAP = frames.jtPriceListSelect
                               .getValueAt(selectedPriceListRowIndex, 0)
                               .toString();

            DataBaseCollectionList collectionList = dataBase.getCollectionList();
            collectionList.addCollectionItem(collectionListIndex, SAP);

            //Refresh list
            dataBase.createListItemAt(frames.tmCollectionEdit, collectionListIndex);

            frames.jriPriceListSelect.setVisible(false);

            //Refresh main sum price
            refreshMainSumPrice();

            //Refresh lists
            refreshLists();

            frames.jlCollectionEdit.setSelectedIndex(collectionListIndex);
            frames.jtCollectionEdit
                  .getSelectionModel()
                  .setSelectionInterval(collectionRowIndex, collectionRowIndex);
        }
    }

    //Remove item from a collection in the collection list
    private void collectionListRemoveItem() {
        int collectionRowIndex = frames.jtCollectionEdit.getSelectedRow();
        int collectionListIndex = frames.jlCollectionEdit.getSelectedIndex();
        if (collectionRowIndex > -1 && collectionListIndex > -1 &&
            message.showConfirmDialog(propertyUtil.getLangText("Confirm.CollectionItem.Delete"))) {
            String SAP = frames.tmCollectionEdit
                               .getValueAt(collectionRowIndex, 1)
                               .toString();

            DataBaseCollectionList collectionList = dataBase.getCollectionList();
            collectionList.removeCollectionItem(collectionListIndex, SAP);

            //Refresh list
            dataBase.createListItemAt(frames.tmCollectionEdit, collectionListIndex);

            //Refresh main sum price
            refreshMainSumPrice();

            //Refresh lists
            refreshLists();

            frames.jlCollectionEdit.setSelectedIndex(collectionListIndex);
            frames.jtCollectionEdit
                  .getSelectionModel()
                  .setSelectionInterval(collectionRowIndex, collectionRowIndex);

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.CollectionEdit.NotSelected"));
        }
    }

    //Add collection to list
    private void collectionAdd() {
        //Refresh selection to create new collection object to add collection list
        selectDatabaseRows();

        //Add collection
        dataBase.addCollection();

        frames.jbRemoveCollection.setEnabled(true);

        //Refresh main sum price
        refreshMainSumPrice();

        //Refresh lists
        refreshLists();

        //Show addition message
        Viewer viewer =
            new Viewer(dataBase.getCollectionCount() + ". " +
                       propertyUtil.getLangText("Viewer.Message.CollectionAdded"));
        viewer.setShowAlways(true);
        viewer.setLocation(mouse.getCursorX(), mouse.getCursorY() - frames.jbAddCollection.getHeight());
        viewer.startUp();
        viewer.shutDown(1500);

    }

    //Remove collection from list
    private void collectionRemove() {

        //Remove collection
        dataBase.removeLastCollection();

        //Refresh main sum price
        refreshMainSumPrice();

        //Refresh lists
        refreshLists();

        frames.jbRemoveCollection.setEnabled(dataBase.getCollectionCount() > 0);

        //Show addition message
        Viewer viewer =
            new Viewer((dataBase.getCollectionCount() + 1) + ". " +
                       propertyUtil.getLangText("Viewer.Message.CollectionRemoved"));
        viewer.setShowAlways(true);
        viewer.setLocation(mouse.getCursorX(), mouse.getCursorY() - frames.jbAddCollection.getHeight());
        viewer.startUp();
        viewer.shutDown(1500);

    }

    //Collection list item selected
    private void collectionListItemSelected() {
        if (frames.dlCollectionEdit.getSize() > 0) {
            dataBase.createListItemAt(frames.tmCollectionEdit, frames.jlCollectionEdit.getSelectedIndex());
        }
    }

    //Canvas selection
    public void canvasSelected(String labelName) {
        //System.out.println(labelName);
        if (labelName.equals("jlLeft")) {
            frames.tbLeft.setSelected(true);
        } else if (labelName.equals("jlRight")) {
            frames.tbRight.setSelected(true);
        } else if (labelName.equals("jlOver")) {
            frames.tbOver.setSelected(true);
        } else if (labelName.equals("jl_CatchNone")) {
            frames.tbCatchNone.setSelected(true);
        } else if (labelName.equals("jl_CatchMagnet")) {
            frames.tbCatchMagnet.setSelected(true);
        } else if (labelName.equals("jl_Catch")) {
            frames.tbCatch.setSelected(true);
        } else if (labelName.equals("jl_Handle")) {
            frames.tbHandle.setSelected(true);
        } else if (labelName.equals("jl_HandleNone")) {
            frames.tbHandle.setSelected(false);
        } else if (labelName.equals("jl_LiftingMdNone")) {
            frames.tbLiftingMD.setSelected(false);
        } else if (labelName.equals("jl_LiftingMd")) {
            frames.tbLiftingMD.setSelected(true);
        } else if (labelName.equals("jl_Fan")) {
            frames.tbFan.setSelected(true);
        } else if (labelName.equals("jl_FanNone")) {
            frames.tbFan.setSelected(false);
        } else if (labelName.equals("jl_Bar")) {
            frames.tbBar.setSelected(true);
            frames.tbCilinder.setSelected(false);
            frames.tbCilinder.setEnabled(false);
            barSelected();
        } else if (labelName.equals("jl_ClosableBar")) {
            frames.tbClosableBar.setSelected(true);
            frames.tbCilinder.setEnabled(true);
            barSelected();
        } else if (labelName.equals("jl_CilinderNone")) {
            frames.tbCilinder.setSelected(false);
        } else if (labelName.equals("jl_Cilinder")) {
            if (frames.tbClosableBar.isSelected()) {
                frames.tbCilinder.setSelected(true);
            }
        } else if (labelName.toLowerCase().contains("color")) {
            colorSelected(labelName);
        } else if (labelName.equals("jl_NX")) {
            frames.tbNX.setSelected(true);
        } else if (labelName.equals("jl_NT")) {
            frames.tbNT.setSelected(true);
        }

        selectDatabaseRows();

    }

    private void windowSystemMoveLeft() {
        try {
            int index = Integer.parseInt(frames.jlWindowReview.getName());
            if (index > 1) {
                index--;
            }

            String fileName = swc.viewerPath + frames.jcbWinowSystems
                                                     .getSelectedItem()
                                                     .toString()
                                                     .replace(" ", "") + "_" + index + ".jpg";
            String prviousFileName = swc.viewerPath + frames.jcbWinowSystems
                                                            .getSelectedItem()
                                                            .toString()
                                                            .replace(" ", "") + "_" + (index - 1) + ".jpg";

            frames.jbWindowsLeft.setEnabled(new File(prviousFileName).exists());
            frames.jbWindowsRight.setEnabled(true);

            frames.jlWindowReview.setName(String.valueOf(index));
            frames.jlWindowReview.setIcon(new ImageIcon(fileName));

        } catch (Exception e) {
            System.out.println("Error loading image for viewer: \n" + e.getMessage());
        }
    }

    private void windowSystemMoveRight() {
        try {
            int index = Integer.parseInt(frames.jlWindowReview.getName());
            String checkFileName = swc.viewerPath + frames.jcbWinowSystems
                                                          .getSelectedItem()
                                                          .toString()
                                                          .replace(" ", "") + "_" + (index + 1) + ".jpg";

            if (new File(checkFileName).exists()) {
                index++;
            }

            String selectedFileName = swc.viewerPath + frames.jcbWinowSystems
                                                             .getSelectedItem()
                                                             .toString()
                                                             .replace(" ", "") + "_" + index + ".jpg";
            String nextFileName = swc.viewerPath + frames.jcbWinowSystems
                                                         .getSelectedItem()
                                                         .toString()
                                                         .replace(" ", "") + "_" + (index + 1) + ".jpg";

            frames.jbWindowsRight.setEnabled(new File(nextFileName).exists());
            frames.jbWindowsLeft.setEnabled(true);

            frames.jlWindowReview.setIcon(new ImageIcon(selectedFileName));
            frames.jlWindowReview.setName(String.valueOf(index));

        } catch (Exception e) {
            System.out.println("Error loading image for viewer: \n" + e.getMessage());
        }
    }

    //Class selected, set image to class
    private void windowSystemClassSelected() {
        frames.jlWindowReview.setName("1");
        frames.jbWindowsRight.setEnabled(true);
        frames.jbWindowsLeft.setEnabled(false);
        windowSystemMoveLeft();
    }

    private void saveWindowSystem() {
        if (message.showConfirmDialog(propertyUtil.getLangText("Confirm.WindowSystems.Save"))) {
            windowSystemModify();
            windowSytems.save(swc.windowSystemsPath);
        }
    }

    private void loadWindowSystems() {
        windowSytems.load(swc.windowSystemsPath);
    }

    private void windowSystemKeySelected() {
        int rows = frames.jtWindows.getRowCount();
        int keyColumn = 5;

        for (int i = 0; i < rows; i++) {
            String key = frames.jtWindows
                               .getValueAt(i, keyColumn)
                               .toString();
            if (key.trim().length() > 0) {
                frames.jtWindows.setValueAt(propertyUtil.getLangText(key), i, keyColumn + 1);
            }
        }

        //Auto modify
        windowSystemModify();
    }

    private void windowSystemSelected() {
        if (frames.jcbWinowTypes.getSelectedItem() != null) {
            String type = (frames.jcbWinowTypes
                                 .getSelectedItem()
                                 .toString()
                                 .trim() + " ").substring(0, 1);
            if (windowSytems.isSystemTypeExit(type)) {
                WindowSystem windowSystem = windowSytems.getWindowSystemByTypeName(type);
                frames.windowTableModel.setModelArray(windowSystem.getWindowTableModelArray());
                frames.windowLimitTableModel.setModelArray(windowSystem.getWindowLimitTableModelArray(),
                                                           frames.windowTableModel);
                frames.tfWindowTypes.setText(windowSystem.getSystemType());
                frames.jcbWinowSystems.setSelectedItem(windowSystem.getSystemName());

            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.WindowTypes.TypeNotExists") + " " +
                                               type);
            }
        } else {
            frames.tfWindowTypes.setText("");
        }
    }

    private void windowSystemDelete() {
        if (frames.jcbWinowTypes.getItemCount() > 0) {
            String type = (frames.jcbWinowTypes
                                 .getSelectedItem()
                                 .toString()
                                 .trim() + " ").substring(0, 1);
            if (windowSytems.isSystemTypeExit(type)) {
                windowSytems.removeWindowSystem(type);
                frames.jcbWinowTypes.removeItemAt(0);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.WindowTypes.TypeNotExists") + " " +
                                               type);
            }
        }
    }

    private void windowSystemAdd() {
        String type = (frames.tfWindowTypes
                             .getText()
                             .trim() + " ").substring(0, 1);
        if (type.trim().length() > 0) {
            if (!windowSytems.isSystemTypeExit(type)) {
                WindowSystem windowSystem = new WindowSystem();
                windowSystem.setWindowTableModel(frames.windowTableModel);
                windowSystem.setWindowLimitTableModel(frames.windowLimitTableModel);
                windowSystem.setSystemName(frames.jcbWinowSystems
                                                 .getSelectedItem()
                                                 .toString());
                windowSystem.setSystemType(type);

                windowSytems.addWinowSystem(windowSystem);
                frames.jcbWinowTypes.addItem(windowSystem.getSystemType());
                frames.jcbWinowTypes.setSelectedItem(windowSystem.getSystemType());
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.WindowTypes.TypeExists") + " " +
                                               type);
            }
        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.WindowTypes.TypeEmpty") + " " + type);
        }
    }

    private void windowSystemModify() {
        if (frames.jcbWinowTypes.getItemCount() > 0) {
            String type = (frames.jcbWinowTypes
                                 .getSelectedItem()
                                 .toString()
                                 .trim() + " ").substring(0, 1);
            if (windowSytems.isSystemTypeExit(type)) {
                WindowSystem windowSystem = windowSytems.getWindowSystemByTypeName(type);
                windowSystem.setWindowTableModel(frames.windowTableModel);
                windowSystem.setWindowLimitTableModel(frames.windowLimitTableModel);
                windowSystem.setSystemName(frames.jcbWinowSystems
                                                 .getSelectedItem()
                                                 .toString());
                windowSystem.setSystemType(type);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.WindowTypes.TypeNotExists") + " " +
                                               type);
            }
        }
    }

    private void adminLogout() {
        adminUtil.logout();
    }

    private void adminLogin() {
        adminUtil.login(frames.tfPassWord.getPassword());
    }

    private void removeItemFromCustomPriceList() {
        int selectedRowIndex = frames.jtCustomPriceList.getSelectedRow();
        DefaultTableModel defaultTableModel = (DefaultTableModel) frames.jtCustomPriceList.getModel();
        if (selectedRowIndex > -1) {
            String SAP = frames.jtPriceListSelect
                               .getValueAt(selectedRowIndex, 0)
                               .toString();
            //Remove item from jlist
            defaultTableModel.removeRow(selectedRowIndex);
            //Remove item from map
            rebateUtil.removeCustomPriceListItem(SAP);

            if (selectedRowIndex + 1 > defaultTableModel.getRowCount()) {
                selectedRowIndex--;
            }
            ListSelectionModel selectionModel = frames.jtCustomPriceList.getSelectionModel();
            selectionModel.setSelectionInterval(selectedRowIndex, selectedRowIndex);
        }
    }

    //Add price list item to rebate custom pricelist
    private void addItemToCustomPriceList() {
        int selectedRowIndex = frames.jtPriceListSelect.getSelectedRow();
        DefaultTableModel defaultTableModel = (DefaultTableModel) frames.jtCustomPriceList.getModel();

        //Get selected item from price list
        String SAP = frames.jtPriceListSelect
                           .getValueAt(selectedRowIndex, 0)
                           .toString();
        String Text = frames.jtPriceListSelect
                            .getValueAt(selectedRowIndex, 1)
                            .toString();

        //Add item to custom price list if not exists yet
        if (!rebateUtil.isCustomPriceListItemExist(SAP)) {
            //Add item to jlist
            defaultTableModel.addRow(new Object[] { SAP, Text, priceUtil.getListPrice(SAP).toString() });

            //Add item to rebate map
            rebateUtil.addCustomPriceListItem(SAP, priceUtil.getListPrice(SAP).toString());

            frames.jbPriceListFilterAdd.setActionCommand("");
            frames.jriPriceListSelect.setVisible(false);
            frames.jriRebate.setVisibleToFocus();
            //Set selected value in jlist
            ListSelectionModel selectionModel = frames.jtCustomPriceList.getSelectionModel();
            selectionModel.setSelectionInterval(defaultTableModel.getRowCount() - 1,
                                                defaultTableModel.getRowCount() - 1);

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.CustomPriceList.Exists") + " " + SAP);
        }

    }

    //Rebate admin open
    private void adminRebateOpen() {
        fileChooser =
            new JRFileChooser(".XML", "Rebate file (*.xml)", propertyUtil.getLangText("Dialog.File.Title.Save"),
                              swc.RebatePath);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            rebateUtil.loadRebate(fileChooser.getSelectedFile());
        }
    }

    //Rebate profile save
    private void saveRebate() {
        CheckNode selectedNode = (CheckNode) frames.rebateTree.getLastSelectedPathComponent();
        CheckNode selectedParentNode = (CheckNode) selectedNode.getParent();
        DefaultTreeModel rebateModel = (DefaultTreeModel) frames.rebateTree.getModel();

        String profileName = selectedNode.getTitle();

        if (rebateUtil.isSelectedNodeProfileInRebateTree()) {
            //Get profile name
            if (selectedNode.getTitle()
                            .toLowerCase()
                            .endsWith(".xml")) {
                profileName = ((CheckNode) selectedNode.getParent()).getTitle();
            }

            //Get profile path
            String profilePath = profileUtil.getProfileDirectory(profileName) + "/Rebate/";
            if (profileName.equals(swc.nodeRabateAdmin)) {
                profilePath = swc.RebatePath;
            }

            //Save rebate to profile path
            fileChooser =
                new JRFileChooser(".XML", "Rebate file (*.xml)", propertyUtil.getLangText("Dialog.File.Title.Save"),
                                  profilePath);
            fileChooser.setDirectoryChangeEnabled(false);

            if (!rebateUtil.isCustomPriceListInputError() && !rebateUtil.isRebateInputError() &&
                fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

                String selectedFileName = fileChooser.getSelectedFile().getName();
                if (!selectedFileName.toLowerCase()
                                     .trim()
                                     .endsWith(".xml")) {
                    selectedFileName += ".xml";
                }

                File rebateFile = new File(profilePath + "\\" + selectedFileName);

                if ((rebateFile.exists() && message.showConfirmDialog(propertyUtil.getLangText("Confirm.FileExists")) ||
                     !rebateFile.exists())) {

                    //Create new node if not overwrite
                    if (!rebateFile.exists()) {
                        CheckNode newNode = new CheckNode(rebateFile.getName());
                        newNode.setCheckBoxManualSelection(true);
                        //Disable checkbox if Admin profile selected
                        newNode.setCheckBoxEnabled(!profileName.equals(swc.nodeRabateAdmin));

                        //Insert new node
                        if (selectedNode.getTitle()
                                        .toLowerCase()
                                        .endsWith("xml")) {
                            //If a rebate file selected
                            rebateModel.insertNodeInto(newNode, selectedParentNode, 0);
                        } else {
                            //If profile is selected
                            rebateModel.insertNodeInto(newNode, selectedNode, 0);
                        }

                        //Refresh tree
                        TreePath path = new TreePath(selectedParentNode);
                        frames.rebateTree.collapsePath(path);
                        frames.rebateTree.expandPath(path);
                    }

                    //Save file to system
                    //System.out.println(rebateFile.getAbsolutePath() + " = "+ profileName);
                    rebateUtil.saveRebate(rebateFile, profileName);

                    //Set new filename on admin
                    frames.tfAdminFileName.setText(selectedFileName);

                    //Refresh collection
                    selectDatabaseRows();
                }
            }


        } else {
            message.showInformationMessage(propertyUtil.getLangText("Information.Rebate.Save.RebateNotSelected"));
        }

    }

    //Rebate profile combo box changed
    private void adminProfileSelected() {
        if (frames.jcbAdminProfiles.getSelectedItem() != null) {
            String profileName = frames.jcbAdminProfiles
                                       .getSelectedItem()
                                       .toString()
                                       .trim();
            //Get file name by selected profile
            Properties profileProperty = propertyUtil.getProfileProperty();
            String FileName = profileProperty.getProperty("Profiles." + profileName.toLowerCase() + ".RebateFile");
            if (FileName != null && FileName.length() > 0) {
                File rebateFile = new File(FileName);
                frames.tfAdminFileName.setText(rebateFile.getName());
            } else {
                frames.tfAdminFileName.setText("");
            }
        }
    }

    //Rebate login combo box changed
    private void adminLoginSelected() {
        if (frames.jcbAdminLogin.getSelectedItem() != null) {
            String loginName = frames.jcbAdminLogin
                                     .getSelectedItem()
                                     .toString()
                                     .trim();
            adminUtil.fillProfileCombo(loginName);
            if (frames.jcbAdminLogin.getSelectedIndex() == 0) {
                frames.jcbAdminProfiles.setEnabled(false);
            } else {
                frames.jcbAdminProfiles.setEnabled(true);
            }
        }
    }

    //Move items form left to right
    private void moveToRight(boolean moveall) {
        Object[] items = frames.jlDatabases.getSelectedValues();
        Properties property = propertyUtil.getDatabaseListProperty();

        if (moveall) {
            items = frames.dlDatabases.toArray();
        }
        if (!frames.pnDatabases.isVisible()) {
            items = new Object[0];
        }

        for (int i = 0; i < items.length; i++) {
            String item = items[i].toString();
            if (property.getProperty(item) == null) {
                propertyUtil.setDataBaseListProperty(item, dataBase.getDataBasePath(item));
                frames.dlDatabaseFilter.addElement(item);
                frames.jlDatabaseFilter.setSelectedValue(item, true);
            }
        }

        if (moveall) {
            frames.jlDatabaseFilter.setSelectedIndex(0);
        }

        propertyUtil.saveDataBaseListProperty();
    }

    //Move items from right
    private void moveToLeft(boolean moveall) {
        Object[] items = frames.jlDatabaseFilter.getSelectedValues();

        if (moveall) {
            items = frames.dlDatabaseFilter.toArray();
        }

        for (int i = 0; i < items.length; i++) {
            String item = items[i].toString();

            int index = frames.jlDatabaseFilter.getSelectedIndex();
            propertyUtil.removeDataBaseListProperty(item);
            frames.dlDatabaseFilter.removeElement(item);

            if (index >= frames.dlDatabaseFilter.size()) {
                index = frames.dlDatabaseFilter.size() - 1;
            } else if (index < 0) {
                index = 0;
            }

            frames.jlDatabaseFilter.setSelectedIndex(index);
        }

        propertyUtil.saveDataBaseListProperty();
    }

    //Open database from list
    private void openDataBaseFromList() {
        if (frames.jlDatabaseFilter.getSelectedValue() != null) {
            String selectedFile = frames.jlDatabaseFilter
                                        .getSelectedValue()
                                        .toString();
            String selectedFilePath = propertyUtil.getDatabaseListProperty().getProperty(selectedFile);
            dataBase.loadTextDataBase(new File(selectedFilePath), swc.LOAD_COLLECTION);
            selectDatabaseRows();
            frames.mainTabPane.setSelectedIndex(swc.tabCollection);
            frames.jtLeftToolBar.setVisible(true);
            frames.mainSplitPane.setDividerLocation(swc.menuToolbarWidth);
            frames.jlMainDatabaseName.setText(dataBase.getCroppedDatabasename());
            frames.jlMainDatabaseName.setToolTipText(dataBase.getDatabasename());
            frames.mainTabPane.setSelectedIndex(swc.tabResize);
        }
    }

    //Open database directory
    private void openDataBaseDirectory() {
        FileSystemView fsv = new SingleFileSystemView(new File(new File(swc.dataBasePath).getAbsolutePath()));
        JFileChooser fileChooser = new JFileChooser(fsv);
        fileChooser.setFileSelectionMode(JRFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            showDataBaseList(fileChooser.getSelectedFile().getAbsolutePath(), true);
        }
    }

    //Set groups when user group name is selected
    private void groupSelected() {
        if (frames.jlGroups.getSelectedValue() != null) {
            String userGroup = frames.jlGroups
                                     .getSelectedValue()
                                     .toString()
                                     .trim();
            frames.tfGroup.setText(userGroup);
        }
    }

    //Set group name when user group name is selected
    private void groupNameSelected() {
        if (frames.jlGroupNames.getSelectedValue() != null) {
            String userGroupName = frames.jlGroupNames
                                         .getSelectedValue()
                                         .toString()
                                         .trim();
            frames.tfGroupName.setText(userGroupName);
            DefaultListModel groupList = groupUtil.getGroupListModel(userGroupName);
            frames.jlGroups.setModel(groupList);
            if (groupList != null && groupList.size() > 0) {
                frames.jlGroups.setSelectedIndex(0);
            }
        }
    }

    //Save price list on settings
    private void savePriceList() {
        Properties rotoProperty = propertyUtil.getRotoProperty();
        rotoProperty.setProperty("Pricelist.Path", swc.priceListPath + "/" + priceUtil.getPriceListFile().getName());
        if (frames.jntMultiplier
                  .getText()
                  .length() == 0 || frames.jntMultiplier.getNumberDouble() < 0) {
            frames.jntMultiplier.setText("1");
        }
        rotoProperty.setProperty("Pricelist.Multiplier", frames.jntMultiplier.getText());
        rotoProperty.setProperty("Pricelist.PrintMartikel", String.valueOf(Frames.getInstance()
                                                                                 .jrPrintMartikel
                                                                                 .isSelected()));
        rotoProperty.setProperty("Pricelist.PrintSAP", String.valueOf(Frames.getInstance()
                                                                            .jrPrintSap
                                                                            .isSelected()));

        propertyUtil.saveRotoProperty();
        //Refresh price list with multiplier
        loadPriceList();
        //Refresh collection
        selectDatabaseRows();
        message.showInformationMessage(propertyUtil.getLangText("Information.PriceList.Save.Ready"));
    }

    //Filter price list
    private void filterPriceList() {
        priceUtil.copyPriceList(frames.jtPriceListSelect, frames.jntFilterSAP.getText(), frames.tfFilterText.getText());
    }

    //Open price list on settings
    private void openPriceList() {
        fileChooser =
            new JRFileChooser(new String[] { ".PRN", ".XLS" }, "Price lists (*.prn,*.xls)",
                              propertyUtil.getLangText("Dialog.File.Title.Open"), swc.priceListPath);
        fileChooser.setDirectoryChangeEnabled(false);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            frames.jriPriceListView.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            if (selectedFile.getName()
                            .toLowerCase()
                            .endsWith(".prn")) {
                priceUtil.loadTextPriceList(fileChooser.getSelectedFile(), frames.jtPriceListView,
                                            frames.jntMultiplier.getNumberDouble());
            } else if (selectedFile.getName()
                                   .toLowerCase()
                                   .endsWith(".xls")) {
                priceUtil.loadExcelPriceList(fileChooser.getSelectedFile(), frames.jtPriceListView,
                                             frames.jntMultiplier.getNumberDouble());
            }
            frames.tfPriceListName.setText(fileChooser.getSelectedFile().getName());

            //Copy price list to selection list
            priceUtil.copyPriceList(frames.jtPriceListSelect);

            //Check price list
            priceUtil.checkPriceListGroups();

            frames.jriPriceListView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    //Show material groups from menu
    private void groupsView() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriMaterialGroups.setVisibleToFocus();
    }

    //Delete an existing group name
    private void groupNameDelete() {
        if (groupUtil.getGroupNameListModel().size() > 0) {
            String groupName = frames.jlGroupNames
                                     .getSelectedValue()
                                     .toString()
                                     .trim();
            if (groupName.length() > 0) {
                int index = frames.jlGroupNames.getSelectedIndex();
                groupUtil.removeGroupName(groupName);
                groupUtil.getGroupNameListModel().removeElementAt(index);
                if (index > groupUtil.getGroupNameListModel().size() - 1)
                    index--;
                frames.jlGroupNames.setSelectedIndex(index);
            }
        }
    }

    //Modify an existing group name
    private void groupNameModify() {
        String groupName = frames.jlGroupNames
                                 .getSelectedValue()
                                 .toString()
                                 .trim();
        String newGroupName = frames.tfGroupName
                                    .getText()
                                    .trim();

        if (groupName.length() > 0) {
            if (!groupUtil.isGroupNameExist(newGroupName)) {
                groupUtil.modifyGroupName(groupName, newGroupName);
                groupUtil.getGroupNameListModel().setElementAt(newGroupName, frames.jlGroupNames.getSelectedIndex());
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.GroupName.Exists") + " " +
                                               groupName);
            }
        }
    }

    //Add a new group name
    private void groupNameAdd() {
        String groupName = frames.tfGroupName
                                 .getText()
                                 .trim();

        if (groupName.length() > 0) {
            if (!groupUtil.isGroupNameExist(groupName)) {
                groupUtil.addGroupName(groupName);
                frames.jlGroupNames.setSelectedIndex(0);
                frames.jlGroupNames.setSelectedValue(groupName, true);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.GroupName.Exists") + " " +
                                               groupName);
            }
        }

    }


    //Delete an existing group
    private void groupsDelete() {
        String groupName = frames.jlGroupNames
                                 .getSelectedValue()
                                 .toString()
                                 .trim();
        if (groupUtil.getGroupListModel(groupName).size() > 0) {
            String groupToDelete = frames.jlGroups
                                         .getSelectedValue()
                                         .toString()
                                         .trim();

            if (groupToDelete.length() > 0) {
                groupUtil.removeGroup(groupToDelete);
                frames.jlGroups.setModel(groupUtil.getGroupListModel(groupName));
                frames.jlGroups.setSelectedIndex(0);
            }
        }
    }

    //Modify an existing group
    private void groupsModify() {
        String groupName = frames.jlGroupNames
                                 .getSelectedValue()
                                 .toString()
                                 .trim();
        String groupToModify = frames.jlGroups
                                     .getSelectedValue()
                                     .toString()
                                     .trim();
        String newGroup = frames.tfGroup
                                .getText()
                                .trim();

        if (newGroup.length() > 0) {
            if (!groupUtil.isGroupExist(newGroup)) {
                groupUtil.modifyGroup(groupName, groupToModify, newGroup);
                frames.jlGroups.setModel(groupUtil.getGroupListModel(groupName));
                frames.jlGroups.setSelectedValue(newGroup, true);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.Group.Exists") + " " + newGroup);
            }
        }
    }

    //Add a new group to group name
    private void groupsAdd() {
        String groupName = frames.jlGroupNames
                                 .getSelectedValue()
                                 .toString()
                                 .trim();
        String group = frames.tfGroup
                             .getText()
                             .trim();
        if (group.length() > 0) {
            if (!groupUtil.isGroupExist(group)) {
                groupUtil.addGroup(groupName, group);
                frames.jlGroups.setModel(groupUtil.getGroupListModel(groupName));
                frames.jlGroups.setSelectedValue(group, true);
            } else {
                message.showInformationMessage(propertyUtil.getLangText("Information.Group.Exists") + " " + group);
            }
        }
    }

    //Show price list from menu
    private void priceListView() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriPriceListView.setVisibleToFocus();
    }

    private void barSelected() {

        if (frames.tbBar.isSelected()) {
            frames.tbCilinder.setEnabled(false);
            frames.tbCilinder.setSelected(false);

            frames.tbSelectionBar.setIcon(frames.tbBar.getIcon());
            frames.canvas
                  .jlSelectionBar
                  .setIcon(frames.canvas
                                 .jlBar
                                 .getIcon());

        } else if (frames.tbClosableBar.isSelected()) {
            frames.tbCilinder.setEnabled(true);

            frames.tbSelectionBar.setIcon(frames.tbClosableBar.getIcon());
            frames.canvas
                  .jlSelectionBar
                  .setIcon(frames.canvas
                                 .jlClosableBar
                                 .getIcon());
        }

        selectDatabaseRows();
    }

    private void colorSelected(String cmd) {
        String sColor = cmd.substring(cmd.indexOf("_") + 1).toLowerCase();
        frames.jbColors.setIcon(new ImageIcon(swc.iconPath + sColor + ".jpg"));
        frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "Handle" + sColor + ".jpg"));
        if (sColor.toLowerCase().equalsIgnoreCase("colornone"))
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "Handle.jpg"));
        if (sColor.equals("colornone"))
            swc.setSelectedColorSign(swc.colorArray[0]);
        if (sColor.equals("colorwhite"))
            swc.setSelectedColorSign(swc.colorArray[1]);
        if (sColor.equals("colorbrown"))
            swc.setSelectedColorSign(swc.colorArray[2]);
        if (sColor.equals("colornewsilver"))
            swc.setSelectedColorSign(swc.colorArray[3]);
        if (sColor.equals("colornaturesilver"))
            swc.setSelectedColorSign(swc.colorArray[4]);
        if (sColor.equals("colorgold"))
            swc.setSelectedColorSign(swc.colorArray[5]);
        if (sColor.equals("colormatgold"))
            swc.setSelectedColorSign(swc.colorArray[6]);
        if (sColor.equals("colorbrass"))
            swc.setSelectedColorSign(swc.colorArray[7]);
        if (sColor.equals("colortitan"))
            swc.setSelectedColorSign(swc.colorArray[8]);
        if (sColor.equals("colorolivebrown"))
            swc.setSelectedColorSign(swc.colorArray[9]);
        if (sColor.equals("colorgreybrown"))
            swc.setSelectedColorSign(swc.colorArray[10]);
        if (sColor.equals("colormiddlebrass"))
            swc.setSelectedColorSign(swc.colorArray[11]);
        if (sColor.equals("colordarkbrass"))
            swc.setSelectedColorSign(swc.colorArray[12]);
        if (sColor.equals("colorcreamwhite"))
            swc.setSelectedColorSign(swc.colorArray[13]);
        if (sColor.equals("colorjetblack"))
            swc.setSelectedColorSign(swc.colorArray[14]);
        //System.out.println("Selected color: " + sColor + " sign: " + swc.getSelectedColorSign());

        selectDatabaseRows();
    }

    private void typeSelected() {
        selectDatabaseRows();
    }

    //Get selected type index from type menu
    private int getSelectedTypeIndex() {
        int index = 0;
        if (frames.tbMDK.isSelected()) {
            index = 0;
        } else if (frames.tbMDF.isSelected()) {
            index = 1;
        } else if (frames.tbMDS.isSelected()) {
            index = 2;
        } else if (frames.tbMKIPP.isSelected()) {
            index = 3;
        } else if (frames.tbMDK2.isSelected()) {
            index = 4;
        } else if (frames.tbMDF2.isSelected()) {
            index = 5;
        } else if (frames.tbMDS2.isSelected()) {
            index = 6;
        } else if (frames.tbMFREI.isSelected()) {
            index = 7;
        } else if (frames.tbMDSMDK2.isSelected()) {
            index = 8;
        } else if (frames.tbMDSMDF2.isSelected()) {
            index = 9;
        }
        return index;
    }

    //Get selected direction text
    private String getSelectedDirectionText() {
        String direction = null;
        if (frames.tbLeft.isSelected()) {
            direction = propertyUtil.getLangText("ToolBarButton.Left.ToolTipText");
        } else if (frames.tbRight.isSelected()) {
            direction = propertyUtil.getLangText("ToolBarButton.Right.ToolTipText");
        } else if (frames.tbOver.isSelected()) {
            direction = propertyUtil.getLangText("ToolBarButton.Over.ToolTipText");
        }
        return direction;
    }

    //Get selected type name from type menu
    private String getSelectedTypeName() {
        WindowSystem windowSystem = windowSytems.getWindowSystemByTypeName(dataBase.getType());
        if (windowSystem != null)
            return windowSystem.getWindowTableModel().getMenuText(getSelectedTypeIndex());
        else
            return null;
    }

    //Set menu rules by window systems
    private void setMenuRules() {
        //Set add collection button

        //frames.jbAddCollection.setEnabled(dataBase.getSelectedItemsCount()>0);
        frames.jbRemoveCollection.setEnabled(dataBase.getCollectionCount() > 0);

        //Set toolbar and menus by database existing items

        frames.tbMDK.setEnabledAndSelected(dataBase.hasMDK());
        frames.tbMDF.setEnabledAndSelected(dataBase.hasMDF());
        frames.tbMDS.setEnabledAndSelected(dataBase.hasMDS());
        frames.tbMKIPP.setEnabledAndSelected(dataBase.hasMKIPP());
        frames.tbMDK2.setEnabledAndSelected(dataBase.hasMDK2());
        frames.tbMDF2.setEnabledAndSelected(dataBase.hasMDF2());
        frames.tbMDS2.setEnabledAndSelected(dataBase.hasMDS2());
        frames.tbMFREI.setEnabledAndSelected(dataBase.hasMFREI());

        //Set closable bar types

        MenuElement[] menuItems = frames.jpBar.getSubElements();
        for (int i = 0; i < menuItems.length; i++) {
            JMenuItem menuItem = (JMenuItem) menuItems[i];
            menuItem.setEnabled(false);
        }

        if (dataBase.hasClosableBar()) {
            for (String closableBarSize : dataBase.getClosableBarList()) {
                for (int i = 0; i < menuItems.length; i++) {
                    JMenuItem menuItem = (JMenuItem) menuItems[i];
                    if (menuItem.getText().equals(closableBarSize))
                        menuItem.setEnabled(true);
                }
            }

            //Set another closable bar value, if the selected value is not exist in database
            boolean needChangeValue = false;
            String firstEnabledValue = "";

            for (int i = 0; i < menuItems.length; i++) {
                JMenuItem menuItem = (JMenuItem) menuItems[i];
                if (!menuItem.isEnabled() && menuItem.getText().equals(frames.tbClosableBar.getImageText())) {
                    needChangeValue = true;
                }
                if (menuItem.isEnabled() && firstEnabledValue.length() == 0) {
                    firstEnabledValue = menuItem.getText();
                }
            }

            if (frames.tbClosableBar.isSelected() && needChangeValue) {
                if (firstEnabledValue.length() == 0) {
                    frames.tbBar.setSelected(true);
                } else {
                    frames.tbClosableBar.setImageText(firstEnabledValue);
                    frames.tbClosableBar.setSelected(true);
                }
                //Refresh data
                selectDatabaseRows();
            }
        }

        //Set default selection, and reselect if the selected type is not enabled
        boolean reSelect = false;
        if (frames.tbMDK.isSelected() && !frames.tbMDK.isEnabled()) {
            frames.tbMDF.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMDF.isSelected() && !frames.tbMDF.isEnabled()) {
            frames.tbMDS.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMDS.isSelected() && !frames.tbMDS.isEnabled()) {
            frames.tbMKIPP.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMKIPP.isSelected() && !frames.tbMKIPP.isEnabled()) {
            frames.tbMDK2.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMDK2.isSelected() && !frames.tbMDK2.isEnabled()) {
            frames.tbMDF2.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMDF2.isSelected() && !frames.tbMDF2.isEnabled()) {
            frames.tbMDS2.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMDS2.isSelected() && !frames.tbMDS2.isEnabled()) {
            frames.tbMFREI.setSelected(true);
            reSelect = true;
        }
        if (frames.tbMFREI.isSelected() && !frames.tbMFREI.isEnabled()) {
            frames.tbMDK.setSelected(true);
            reSelect = true;
        }
        //Handle never ending loop
        if (!frames.tbMDK.isEnabled() && !frames.tbMDF.isEnabled() && !frames.tbMDS.isEnabled() &&
            !frames.tbMKIPP.isEnabled() && !frames.tbMDK2.isEnabled() && !frames.tbMDF2.isEnabled() &&
            !frames.tbMDS2.isEnabled() && !frames.tbMFREI.isEnabled()) {
            reSelect = false;
        }
        if (reSelect)
            selectDatabaseRows();


        frames.tbBar.setEnabledAndSelected(dataBase.hasNormalBar());
        frames.tbClosableBar.setEnabledAndSelected(dataBase.hasClosableBar());
        if (frames.tbClosableBar.isSelected()) {
            frames.tbCilinder.setEnabledAndSelected(dataBase.hasCilinder());
        }
        if (!dataBase.hasClosableBar()) {
            frames.tbBar.setSelected(true);
        }

        if (!dataBase.hasSelectionBar()) {
            //set back bar type to "ko" if no any type found.
            if (!swc.getSelectedSelectionBarSign().equalsIgnoreCase(swc.selectionBarArray[0])) {
                frames.jmiBarKo.doClick();
            }
        }

        frames.tbSelectionBar.setEnabled(dataBase.hasSelectionBar());
        frames.canvas
              .jlSelectionBar
              .setEnabled(dataBase.hasSelectionBar());
        frames.jmiBarKo.setEnabled(dataBase.getSelectioneBarList().contains(swc.selectionBarArray[0]));
        frames.jmiBarKsr.setEnabled(dataBase.getSelectioneBarList().contains(swc.selectionBarArray[1]));
        frames.jmiBarM.setEnabled(dataBase.getSelectioneBarList().contains(swc.selectionBarArray[2]));

        frames.tbCatch.setEnabled(dataBase.hasCatchNormal());
        frames.tbCatchMagnet.setEnabled(dataBase.hasCatchMagnet());
        if (!dataBase.hasCatchMagnet() && !dataBase.hasCatchNormal()) {
            frames.tbCatchNone.setEnabled(true);
        }
        frames.tbFan.setEnabled(dataBase.hasFan());
        frames.tbLiftingMD.setEnabled(dataBase.hasLiftingMD());
        frames.tbHandle.setEnabledAndSelected(dataBase.hasHandle());

        frames.jmiColorNone.setEnabled(dataBase.getColorList().contains(swc.colorArray[0]));
        frames.jmiColorWhite.setEnabled(dataBase.getColorList().contains(swc.colorArray[1]));
        frames.jmiColorBrown.setEnabled(dataBase.getColorList().contains(swc.colorArray[2]));
        frames.jmiColorNewSilver.setEnabled(dataBase.getColorList().contains(swc.colorArray[3]));
        frames.jmiColorNatureSilver.setEnabled(dataBase.getColorList().contains(swc.colorArray[4]));
        frames.jmiColorGold.setEnabled(dataBase.getColorList().contains(swc.colorArray[5]));
        frames.jmiColorMatGold.setEnabled(dataBase.getColorList().contains(swc.colorArray[6]));
        frames.jmiColorBrass.setEnabled(dataBase.getColorList().contains(swc.colorArray[7]));
        frames.jmiColorTitan.setEnabled(dataBase.getColorList().contains(swc.colorArray[8]));
        frames.jmiColorOliveBrown.setEnabled(dataBase.getColorList().contains(swc.colorArray[9]));
        frames.jmiColorGreyBrown.setEnabled(dataBase.getColorList().contains(swc.colorArray[10]));
        frames.jmiColorMiddleBrass.setEnabled(dataBase.getColorList().contains(swc.colorArray[11]));
        frames.jmiColorDarkBrass.setEnabled(dataBase.getColorList().contains(swc.colorArray[12]));
        frames.jmiColorCreamWhite.setEnabled(dataBase.getColorList().contains(swc.colorArray[13]));
        frames.jmiColorJetBlack.setEnabled(dataBase.getColorList().contains(swc.colorArray[14]));

        //Set canvas settings by the menu and toolbars
        WindowSystem windowSystem = windowSytems.getWindowSystemByTypeName(dataBase.getType());
        if (windowSystem != null) {
            int index = getSelectedTypeIndex();
            WindowTableModel tableModel = windowSystem.getWindowTableModel();

            windowTypes.setClassification(windowSystem.getSystemName());

            //Set type menu enabled if exists in database
            frames.tbMDK.setEnabledAndSelected(tableModel.isMenuEnabled(0) && dataBase.hasMDK());
            frames.tbMDF.setEnabledAndSelected(tableModel.isMenuEnabled(1) && dataBase.hasMDF());
            frames.tbMDS.setEnabledAndSelected(tableModel.isMenuEnabled(2) && dataBase.hasMDS());
            frames.tbMKIPP.setEnabledAndSelected(tableModel.isMenuEnabled(3) && dataBase.hasMKIPP());
            frames.tbMDK2.setEnabledAndSelected(tableModel.isMenuEnabled(4) && dataBase.hasMDK2());
            frames.tbMDF2.setEnabledAndSelected(tableModel.isMenuEnabled(5) && dataBase.hasMDF2());
            frames.tbMDS2.setEnabledAndSelected(tableModel.isMenuEnabled(6) && dataBase.hasMDS2());
            frames.tbMFREI.setEnabledAndSelected(tableModel.isMenuEnabled(7) && dataBase.hasMFREI());
            frames.tbMDSMDK2.setEnabledAndSelected(tableModel.isMenuEnabled(8) && dataBase.hasMDS() &&
                                                   dataBase.hasMDK2());
            frames.tbMDSMDF2.setEnabledAndSelected(tableModel.isMenuEnabled(9) && dataBase.hasMDS() &&
                                                   dataBase.hasMDF2());

            //Set type menu text
            frames.tbMDK.setText(tableModel.getMenuText(0));
            frames.tbMDF.setText(tableModel.getMenuText(1));
            frames.tbMDS.setText(tableModel.getMenuText(2));
            frames.tbMKIPP.setText(tableModel.getMenuText(3));
            frames.tbMDK2.setText(tableModel.getMenuText(4));
            frames.tbMDF2.setText(tableModel.getMenuText(5));
            frames.tbMDS2.setText(tableModel.getMenuText(6));
            frames.tbMFREI.setText(tableModel.getMenuText(7));
            frames.tbMDSMDK2.setText(tableModel.getMenuText(8));
            frames.tbMDSMDF2.setText(tableModel.getMenuText(9));

            //Set tooltiptext
            /*
			frames.tbMDK.setToolTipText(tableModel.getMenuText(0));
			frames.tbMDF.setToolTipText(tableModel.getMenuText(1));
			frames.tbMDS.setToolTipText(tableModel.getMenuText(2));
			frames.tbMKIPP.setToolTipText(tableModel.getMenuText(3));
			frames.tbMDK2.setToolTipText(tableModel.getMenuText(4));
			frames.tbMDF2.setToolTipText(tableModel.getMenuText(5));
			frames.tbMDS2.setToolTipText(tableModel.getMenuText(6));
			frames.tbMFREI.setToolTipText(tableModel.getMenuText(7));
			frames.tbMDSMDK2.setToolTipText(tableModel.getMenuText(8));
			frames.tbMDSMDF2.setToolTipText(tableModel.getMenuText(9));
			*/

            //Set direction menu enabled
            boolean isLeftEnabled = tableModel.isLeftEnabled(index);
            boolean isRightEnabled = tableModel.isRightEnabled(index);
            boolean isOverEnabled = tableModel.isOverEnabled(index);

            //Handle dead loop
            if (!isLeftEnabled && !isRightEnabled && !isOverEnabled) {
                isLeftEnabled = true;
            }

            frames.tbLeft.setEnabled(isLeftEnabled);
            frames.tbRight.setEnabled(isRightEnabled);
            frames.tbOver.setEnabled(isOverEnabled);

            if (frames.tbLeft.isSelected() && !isLeftEnabled) {
                frames.tbRight.setSelected(true);
                selectDatabaseRows();
            }
            if (frames.tbRight.isSelected() && !isRightEnabled) {
                frames.tbOver.setSelected(true);
                selectDatabaseRows();
            }
            if (frames.tbOver.isSelected() && !isOverEnabled) {
                frames.tbLeft.setSelected(true);
                selectDatabaseRows();
            }

            //Set special properties by classifications
            frames.canvas.setLabelImage("jl_ClosableBar", new ImageIcon(swc.iconPath + "ClosableBar.jpg"));
            frames.canvas
                  .getLabelViewImages()
                  .put("jl_ClosableBar", "ClosableBar_view.jpg");

            if (windowTypes.getClassification().equals("Class 2")) {
                //Set door type
                frames.tbBar.setEnabled(false);
                frames.canvas.setLabelProperties("jl_Bar", frames.tbBar);
                if (frames.tbMDF.isSelected() || frames.tbMDS.isSelected()) {
                    frames.canvas
                          .getLabelViewImages()
                          .put("jl_ClosableBar", "ClosableBarDoor_3_view.jpg");
                    frames.canvas.setLabelImage("jl_ClosableBar",
                                                new ImageIcon(swc.iconPath + "ClosableBarDoor_3.jpg"));
                } else {
                    frames.canvas
                          .getLabelViewImages()
                          .put("jl_ClosableBar", "ClosableBarDoor_4_view.jpg");
                    frames.canvas.setLabelImage("jl_ClosableBar",
                                                new ImageIcon(swc.iconPath + "ClosableBarDoor_4.jpg"));
                }
                frames.tbClosableBar.setSelected(true);
            }

            //Set canvas labels
            DisplayCanvas dc = frames.canvas;
            dc.setLabelProperties("jlLeft", frames.tbLeft);
            dc.setLabelProperties("jlRight", frames.tbRight);
            dc.setLabelProperties("jlOver", frames.tbOver);
            dc.setLabelProperties("jl_Cilinder", frames.tbCilinder);

            dc.setLabelProperties("jl_CatchNone", frames.tbCatchNone);
            dc.setLabelProperties("jl_Catch", frames.tbCatch);
            dc.setLabelProperties("jl_CatchMagnet", frames.tbCatchMagnet);
            dc.setLabelProperties("jl_Handle", frames.tbHandle);
            dc.setLabelProperties("jl_LiftingMd", frames.tbLiftingMD);
            dc.setLabelProperties("jl_Fan", frames.tbFan);
            dc.setLabelProperties("jl_ClosableBar", frames.tbClosableBar);

            dc.setLabelProperties("jl_colornone", frames.jmiColorNone);
            dc.setLabelProperties("jl_colorwhite", frames.jmiColorWhite);
            dc.setLabelProperties("jl_colorbrown", frames.jmiColorBrown);
            dc.setLabelProperties("jl_colornewsilver", frames.jmiColorNewSilver);
            dc.setLabelProperties("jl_colornaturesilver", frames.jmiColorNatureSilver);
            dc.setLabelProperties("jl_colorgold", frames.jmiColorGold);
            dc.setLabelProperties("jl_colormatgold", frames.jmiColorMatGold);
            dc.setLabelProperties("jl_colorbrass", frames.jmiColorBrass);
            dc.setLabelProperties("jl_colortitan", frames.jmiColorTitan);
            dc.setLabelProperties("jl_colorolivebrown", frames.jmiColorOliveBrown);
            dc.setLabelProperties("jl_colorgreybrown", frames.jmiColorGreyBrown);
            dc.setLabelProperties("jl_colormiddlebrass", frames.jmiColorMiddleBrass);
            dc.setLabelProperties("jl_colordarkbrass", frames.jmiColorDarkBrass);
            dc.setLabelProperties("jl_colorcreamwhite", frames.jmiColorCreamWhite);
            dc.setLabelProperties("jl_colorjetblack", frames.jmiColorJetBlack);

        } else {
            message.showInformationMessage(propertyUtil.getLangText("Alert.SetMenuRoles.Error.Message") + " " +
                                           dataBase.getType());
            windowTypes.setClassification("");
        }
    }

    //Open database for collection
    private void openDataBase() {
        fileChooser =
            new JRFileChooser(".PRN", "*.PRN", propertyUtil.getLangText("Dialog.File.Title.Open"), swc.dataBasePath);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            dataBase.loadTextDataBase(fileChooser.getSelectedFile(), swc.LOAD_COLLECTION);
            selectDatabaseRows();
            frames.jtLeftToolBar.setVisible(true);
            frames.mainSplitPane.setDividerLocation(swc.menuToolbarWidth);
            frames.jlMainDatabaseName.setText(dataBase.getCroppedDatabasename());
            frames.jlMainDatabaseName.setToolTipText(dataBase.getDatabasename());
            frames.mainTabPane.setSelectedIndex(swc.tabResize);
        }
    }

    private void selectDatabaseRows() {
        //System.out.println("selectDatabaseRows");
        if (dataBase.isLoaded()) {
            boolean hasErrors = false;
            boolean left = frames.tbLeft.isSelected();
            boolean right = frames.tbRight.isSelected();
            boolean over = frames.tbOver.isSelected();
            boolean handle = frames.tbHandle.isSelected();
            boolean liftingMD = frames.tbLiftingMD.isSelected();
            boolean isMDK = frames.tbMDK.isSelected();
            boolean isMDF = frames.tbMDF.isSelected();
            boolean isMDS = frames.tbMDS.isSelected();
            boolean isMKIPP = frames.tbMKIPP.isSelected();
            boolean isMDK2 = frames.tbMDK2.isSelected();
            boolean isMDF2 = frames.tbMDF2.isSelected();
            boolean isMDS2 = frames.tbMDS2.isSelected();
            boolean isMFREI = frames.tbMFREI.isSelected();
            boolean isMDSMDF2 = frames.tbMDSMDF2.isSelected();
            boolean isMDSMDK2 = frames.tbMDSMDK2.isSelected();
            boolean catchNone = frames.tbCatchNone.isSelected();
            boolean catchNormal = frames.tbCatch.isSelected();
            boolean catchMagnet = frames.tbCatchMagnet.isSelected();
            boolean fan = frames.tbFan.isSelected();
            int correctionWidth = frames.jntCorrectionWidth.getNumber();
            int correctionHeight = frames.jntCorrectionHeight.getNumber();

            //Reset correction if isMDSMDK2 || isMDSMDF2
            if (isMDSMDK2 || isMDSMDF2) {
                correctionHeight = 0;
                correctionWidth = 0;
            }

            frames.jntCorrectionWidth.setEnabled(!(isMDSMDK2 || isMDSMDF2));
            frames.jntCorrectionHeight.setEnabled(!(isMDSMDK2 || isMDSMDF2));

            int width = frames.jntWidth.getNumber() - correctionWidth;
            int width2 = frames.jntWidth2.getNumber() - correctionWidth;
            int height = frames.jntHeight.getNumber() - correctionHeight;
            int height2 = frames.jntHeight2.getNumber() - correctionHeight;
            boolean closableBar = frames.tbClosableBar.isSelected();
            String closableBarSize = frames.tbClosableBar.getImageText();
            boolean normalBar = frames.tbBar.isSelected();
            boolean cilinder = frames.tbCilinder.isSelected();

            int garniture = frames.jntGarniture.getNumber();
            boolean clearSelection = true;


            //Set garniture
            if (garniture < 1) {
                garniture = 1;
            }
            if (garniture > 999) {
                try {
                    garniture = Integer.parseInt(frames.jntGarniture
                                                       .getText()
                                                       .substring(0, 3));
                } catch (Exception e) {
                    garniture = 1;
                }
            }

            frames.jntGarniture.setText(String.valueOf(garniture));
            dataBase.setGarniture(garniture);

            //Set type name
            dataBase.setSelectedTypeName(getSelectedTypeName());
            //Set width 				
            dataBase.setWidth(width);
            //Set height
            dataBase.setHeight(height);
            //Set correction width
            dataBase.setCorrectionWidth(correctionWidth);
            //Set correction height
            dataBase.setCorrectionHeight(correctionHeight);
            //Set direction
            dataBase.setDirection(getSelectedDirectionText());
            //Reset width2 and height2 if not that type
            dataBase.setWidth2(0);
            dataBase.setHeight2(0);

            //Reset values in normal selection
            //Keep values in double selection
            dataBase.setResetHasProperties(true);

            //Handle double selection
            if (isMDSMDK2 || isMDSMDF2) {
                /**	!direction,width2 (height2) <- MDS MDK2 ->width,direction
				  * !direction,width2 (hegiht2)<- MDS MDF2 ->width,direction
				  */

                int windowHeight = height;
                //Use second height with class 4 and class 6
                if (windowTypes.getClassification().equals(swc.windowSystemArray[3]) ||
                    windowTypes.getClassification().equals(swc.windowSystemArray[5])) {
                    windowHeight = height2;
                }

                //Set width2 				
                dataBase.setWidth2(width2);
                //Set height2
                dataBase.setHeight2(windowHeight);

                //Create first selection with width2 and opposite direction
                isMDS = true;
                dataBase.selectCollectionRows(!left, !right, over, handle, liftingMD, swc.getSelectedColorSign(),
                                              width2, windowHeight, isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2,
                                              isMDS2, isMFREI, false, false, catchNone, catchNormal, catchMagnet, fan,
                                              closableBar, closableBarSize, normalBar, cilinder, clearSelection,
                                              swc.getSelectedSelectionBarSign());
                isMDS = false;

                //Set type for second selection				
                if (isMDSMDK2) {
                    isMDK2 = true;
                } else if (isMDSMDF2) {
                    isMDF2 = true;
                }

                isMDSMDK2 = false;
                isMDSMDF2 = false;

                //Don't clear first selection
                clearSelection = false;

                //Check size
                hasErrors = checkWindowSize(2);

                //Keep values in double selection
                dataBase.setResetHasProperties(false);
            }

            //Create selection
            dataBase.selectCollectionRows(left, right, over, handle, liftingMD, swc.getSelectedColorSign(), width,
                                          height, isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI,
                                          isMDSMDF2, isMDSMDK2, catchNone, catchNormal, catchMagnet, fan, closableBar,
                                          closableBarSize, normalBar, cilinder, clearSelection,
                                          swc.getSelectedSelectionBarSign());

            // Create collection from selection
            dataBase.createCollection(frames.jtCollection);

            //Set menu rules
            setMenuRules();

            //Get values again if menu rules set any values disabled		
            left = frames.tbLeft.isSelected();
            right = frames.tbRight.isSelected();
            over = frames.tbOver.isSelected();
            handle = frames.tbHandle.isSelected();
            liftingMD = frames.tbLiftingMD.isSelected() && frames.tbLiftingMD.isEnabled();
            catchNormal = frames.tbCatch.isSelected() && frames.tbCatch.isEnabled();
            catchMagnet = frames.tbCatchMagnet.isSelected() && frames.tbCatchMagnet.isEnabled();
            //Set catchNone true if selected, or magnet or normal selected but disabled
            catchNone =
                frames.tbCatchNone.isSelected() || (frames.tbCatch.isSelected() && !frames.tbCatch.isEnabled()) ||
                (frames.tbCatchMagnet.isSelected() && !frames.tbCatchMagnet.isEnabled());
            fan = frames.tbFan.isSelected() && frames.tbFan.isEnabled();
            closableBar = frames.tbClosableBar.isSelected() && frames.tbClosableBar.isEnabled();
            normalBar = frames.tbBar.isSelected();
            cilinder = frames.tbCilinder.isSelected() && frames.tbCilinder.isEnabled();
            isMDK = frames.tbMDK.isSelected();
            isMDF = frames.tbMDF.isSelected();
            isMDS = frames.tbMDS.isSelected();
            isMKIPP = frames.tbMKIPP.isSelected();
            isMDK2 = frames.tbMDK2.isSelected();
            isMDF2 = frames.tbMDF2.isSelected();
            isMDS2 = frames.tbMDS2.isSelected();
            isMFREI = frames.tbMFREI.isSelected();
            isMDSMDF2 = frames.tbMDSMDF2.isSelected();
            isMDSMDK2 = frames.tbMDSMDK2.isSelected();

            //Draw window (mofified by menu roles)
            windowTypes.setSelection(left, right, over, handle, liftingMD, swc.getSelectedColorSign(), width, height,
                                     isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI, isMDSMDF2,
                                     isMDSMDK2, catchNone, catchNormal, catchMagnet, fan, closableBar, closableBarSize,
                                     normalBar, cilinder);

            frames.canvas.repaint();


            //Check size
            if (!hasErrors)
                hasErrors = checkWindowSize(1);
            else
                checkWindowSize(1);

            frames.jbAddCollection.setEnabled(!hasErrors);
            //Check width and height warnings
            if (!isMDSMDF2 && !isMDSMDK2) {
                checkSizeWarnings(isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI);
            }
        }
    }

    //Check width and height warnings
    private void checkSizeWarnings(boolean isMDK, boolean isMDF, boolean isMDS, boolean isMKIPP, boolean isMDK2,
                                   boolean isMDF2, boolean isMDS2, boolean isMFREI) {
        WindowSystem windowSystem = windowSytems.getWindowSystemByTypeName(dataBase.getType());
        if (windowSystem != null) {
            String warningMessage =
                windowSystem.getWindowLimitTableModel()
                .getWarningMessage(isMDK, isMDF, isMDS, isMKIPP, isMDK2, isMDF2, isMDS2, isMFREI);

            frames.canvas.showWarning(warningMessage != windowSystem.getWindowLimitTableModel().NO_WARNING_MESSAGE,
                                      warningMessage);
        }
    }

    //Ketszer kell kulon futtatni, mert a type 1 -nel a dataBase.getMaxWidth() egy masik tipusra ad eredmenyt !,
    //Mivel a min max ot tipus szerint figyeljuk, az elso esetben mas a tipus
    private boolean checkWindowSize(int type) {
        int shutDown = 3000;
        boolean hasErrors = false;

        if (type == 1) {

            //Check width
            if (dataBase.getWidth() > dataBase.getMaxWidth() || dataBase.getWidth() < dataBase.getMinWidth()) {
                frames.mainTabPane.setSelectedIndex(swc.tabResize);
                frames.jntWidth.setBackground(swc.colorError);
                hasErrors = true;

                //Show message
                Viewer viewer =
                    new Viewer(propertyUtil.getLangText("Error.Width") + ": " +
                               (dataBase.getMinWidth() + dataBase.getCorrectionWidth()) + " - " +
                               (dataBase.getMaxWidth() + dataBase.getCorrectionWidth()));
                viewer.setShowAlways(true);
                viewer.setAnimation(false);
                viewer.startUp();
                viewer.setLocation(frames.jntWidth
                                         .getLocationOnScreen()
                                         .x - viewer.getWidth() / 2, frames.jntWidth
                                                                           .getLocationOnScreen()
                                                                           .y - 50);
                viewer.shutDown(shutDown);
            } else {
                frames.jntWidth.setBackground(Color.WHITE);
            }

            //Check height
            if (dataBase.getHeight() > dataBase.getMaxHeight() || dataBase.getHeight() < dataBase.getMinHeight()) {
                frames.mainTabPane.setSelectedIndex(swc.tabResize);
                frames.jntHeight.setBackground(swc.colorError);
                hasErrors = true;

                //Show message
                Viewer viewer =
                    new Viewer(propertyUtil.getLangText("Error.Height") + ": " +
                               (dataBase.getMinHeight() + dataBase.getCorrectionHeight()) + " - " +
                               (dataBase.getMaxHeight() + dataBase.getCorrectionHeight()));
                viewer.setShowAlways(true);
                viewer.setAnimation(false);
                viewer.startUp();
                viewer.setLocation(frames.jntHeight
                                         .getLocationOnScreen()
                                         .x - viewer.getWidth() / 2, frames.jntHeight
                                                                           .getLocationOnScreen()
                                                                           .y - 50);
                viewer.shutDown(shutDown);
            } else {
                frames.jntHeight.setBackground(Color.WHITE);
            }

        } else if (type == 2) {

            if (frames.jntWidth2.getWidth() > 0) {
                //Check width2
                if (dataBase.getWidth2() > dataBase.getMaxWidth() || dataBase.getWidth2() < dataBase.getMinWidth()) {
                    frames.mainTabPane.setSelectedIndex(swc.tabResize);
                    frames.jntWidth2.setBackground(swc.colorError);
                    hasErrors = true;

                    //Show message
                    Viewer viewer =
                        new Viewer(propertyUtil.getLangText("Error.Width") + ": " +
                                   (dataBase.getMinWidth() + dataBase.getCorrectionWidth()) + " - " +
                                   (dataBase.getMaxWidth() + dataBase.getCorrectionWidth()));
                    viewer.setShowAlways(true);
                    viewer.setAnimation(false);
                    viewer.startUp();
                    viewer.setLocation(frames.jntWidth2
                                             .getLocationOnScreen()
                                             .x - viewer.getWidth() / 2, frames.jntWidth2
                                                                               .getLocationOnScreen()
                                                                               .y - 50);
                    viewer.shutDown(shutDown);
                } else {
                    frames.jntWidth2.setBackground(Color.WHITE);
                }
            }

            if (frames.jntHeight2.getWidth() >
                0) {
                //Check height2
                if (dataBase.getHeight2() > dataBase.getMaxHeight() ||
                    dataBase.getHeight2() < dataBase.getMinHeight()) {
                    frames.mainTabPane.setSelectedIndex(swc.tabResize);
                    frames.jntHeight2.setBackground(swc.colorError);
                    hasErrors = true;
                    //Show message
                    Viewer viewer =
                        new Viewer(propertyUtil.getLangText("Error.Height") + ": " +
                                   (dataBase.getMinHeight() + dataBase.getCorrectionHeight()) + " - " +
                                   (dataBase.getMaxHeight() + dataBase.getCorrectionHeight()));
                    viewer.setShowAlways(true);
                    viewer.setAnimation(false);
                    viewer.startUp();
                    viewer.setLocation(frames.jntHeight2
                                             .getLocationOnScreen()
                                             .x - viewer.getWidth() / 2, frames.jntHeight2
                                                                               .getLocationOnScreen()
                                                                               .y - 50);
                    viewer.shutDown(shutDown);
                } else {
                    frames.jntHeight2.setBackground(Color.WHITE);
                }
            }
        }
        return hasErrors;
    }

    private void loginUser() {
        userUtil.loginUser(frames.tfUser
                                 .getText()
                                 .trim());
    }

    private void logoutUser() {
        userUtil.logoutUser();
    }

    private void loginProfile() {
        profileUtil.loginProfile(frames.tfProfile
                                       .getText()
                                       .trim(), frames.tfUser
                                                      .getText()
                                                      .trim());
        loadSettings();
        selectDatabaseRows();
    }

    private void logoutProfile() {
        profileUtil.logoutProfile();
        loadSettings();
        selectDatabaseRows();
    }

    private void showMenus() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriMenus.setVisibleToFocus();
    }

    private void showRebate() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriRebate.setVisibleToFocus();

        //frames.pnRebateAdmin.setVisible(adminUtil.isLoggedIn());

        refreshRebateTree();

        if (rebateUtil.getDefaultProfileNode() != null) {
            frames.rebateTree.setSelectionPath(new TreePath(rebateUtil.getDefaultProfileNode().getPath()));
            frames.rebateTree.expandPath(new TreePath(rebateUtil.getDefaultProfileNode().getPath()));
        } else if (rebateUtil.getDefaultUserNode() != null) {
            frames.rebateTree.setSelectionPath(new TreePath(rebateUtil.getDefaultUserNode().getPath()));
            frames.rebateTree.expandPath(new TreePath(rebateUtil.getDefaultUserNode().getPath()));
        }

        rebateTreeNodeSelected();
    }

    //Refresh rebate tree on rebate frame if visible
    private void refreshRebateTree() {
        if (frames.jriRebate.isVisible()) {
            DefaultTreeModel rebateModel = (DefaultTreeModel) frames.rebateTree.getModel();
            CheckNode rebateRoot = frames.nodeRootRebate;
            rebateUtil.buildRebateRoot(rebateRoot);
            rebateModel.setRoot(rebateRoot);

            frames.rebateTree.setSelectionPath(new TreePath(rebateRoot.getPath()));
        }
    }

    private void expandTreeNodes(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }


    private void showAdminLogin() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriAdminLogin.setVisibleToFocus();
    }

    //Use home directory to list, swc.databasepath path will be used (not full path)
    private void showDataBaseList(boolean editable) {
        showDataBaseList(swc.dataBasePath, editable);
    }

    //Use other directory to list, full path will be used
    private void showDataBaseList(String directoryPath, boolean editable) {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        dataBase.setDataBaseListModel(frames.jlDatabases, directoryPath);
        frames.jriDbListEditor.setVisibleToFocus();

        frames.pnDataBaseButtons.setVisible(editable);
        frames.pnDatabases.setVisible(editable);

    }

    private void showWindowTypes() {
        frames.mainTabPane.setSelectedIndex(swc.tabMenus);
        frames.jriWindows.setVisibleToFocus();
    }

    private void popupClosableBarPressed(String cmd) {
        try {
            String sLength = cmd.substring(cmd.indexOf("_") + 1);
            frames.tbClosableBar.setImageText(sLength);
            frames.canvas
                  .jlClosableBar
                  .setText(sLength);
            selectDatabaseRows();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popupSelectionBarPressed(String cmd) {
        try {
            String sSelection = cmd.substring(cmd.indexOf("_") + 1).toUpperCase();
            frames.tbSelectionBar.setImageText(sSelection);
            frames.canvas
                  .jlSelectionBar
                  .setText(sSelection);
            if (sSelection.toLowerCase().equals("k")) {
                swc.setSelectedSelectionBarSign(swc.selectionBarArray[0]);
            } else if (sSelection.toLowerCase().equals("ksr")) {
                swc.setSelectedSelectionBarSign(swc.selectionBarArray[1]);
            } else if (sSelection.toLowerCase().equals("m")) {
                swc.setSelectedSelectionBarSign(swc.selectionBarArray[2]);
            }

            selectDatabaseRows();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popupNTPressed(String cmd) {
        try {
            String sSelection = cmd.substring(cmd.indexOf("_") + 1).toUpperCase();
            frames.tbNT.setImageText(sSelection);
            frames.canvas
                  .jlNT
                  .setText(sSelection);
            if (sSelection.toLowerCase().equals("3/100")) {
                swc.setSelectedNTSign(swc.ntArray[0]);
            } else if (sSelection.toLowerCase().equals("6/100")) {
                swc.setSelectedNTSign(swc.ntArray[1]);
            }

            selectDatabaseRows();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popupNXPressed(String cmd) {
        try {
            String sSelection = cmd.substring(cmd.indexOf("_") + 1).toUpperCase();
            frames.tbNX.setImageText(sSelection);
            frames.canvas
                  .jlNX
                  .setText(sSelection);
            if (sSelection.toLowerCase().equals("3/130")) {
                swc.setSelectedNXSign(swc.nxArray[0]);
            } else if (sSelection.toLowerCase().equals("6/130")) {
                swc.setSelectedNXSign(swc.nxArray[1]);
            } else if (sSelection.toLowerCase().equals("6/150")) {
                swc.setSelectedNXSign(swc.nxArray[2]);
            }
            selectDatabaseRows();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popupClosableBarShow() {
        if (frames.tbClosableBar.isEnabled()) {
            frames.jpBar.setVisible(true);

            //Try to get resize frame from tab util, to add popup menu to the removed frame if exist
            JRFrame frameResize = tabUtil.getFrameList().get(swc.tabResize);
            if (frameResize == null) {
                frames.jpBar.show(frames.jrfMain, mouse.getCursorX() - 50, mouse.getCursorY());
            } else {
                frames.jpBar.show(frameResize, mouse.getCursorX() - 50, mouse.getCursorY());
            }

            frames.jpBar.setLocation(mouse.getCursorPosition());
        }
    }

    private void popupSelectionBarShow() {
        frames.tbSelectionBar.setSelected(false);
        if (frames.tbSelectionBar.isEnabled()) {
            frames.jpSelectionBar.show(frames.jrfMain, mouse.getCursorX() - 50, mouse.getCursorY());
            frames.jpSelectionBar.setLocation(mouse.getCursorPosition());
        }
    }

    private void popupNTShow() {
        if (frames.tbNT.isEnabled()) {
            frames.jpNT.show(frames.jrfMain, mouse.getCursorX() - 50, mouse.getCursorY());
            frames.jpNT.setLocation(mouse.getCursorPosition());
        }
    }

    private void popupNXShow() {
        if (frames.tbNX.isEnabled()) {
            frames.jpNX.show(frames.jrfMain, mouse.getCursorX() - 50, mouse.getCursorY());
            frames.jpNX.setLocation(mouse.getCursorPosition());
        }
    }

    private void popupColorsShow() {
        frames.jpColors.show(frames.jrfMain, mouse.getCursorX() - 50, mouse.getCursorY());
        frames.jpColors.setLocation(mouse.getCursorPosition());
    }

    //Save menu positions and settings
    private void saveMenuSettings() {
        Properties rotoProperty = propertyUtil.getRotoProperty();
        rotoProperty.setProperty("Menus.Direction.Left", String.valueOf(frames.jrLeft.isSelected()));
        rotoProperty.setProperty("Menus.Direction.Right", String.valueOf(frames.jrRight.isSelected()));
        rotoProperty.setProperty("Menus.Direction.Over", String.valueOf(frames.jrOver.isSelected()));
        rotoProperty.setProperty("Menus.Handle", String.valueOf(frames.jcHandle.isSelected()));
        rotoProperty.setProperty("Menus.LiftingMD", String.valueOf(frames.jcLiftingMD.isSelected()));
        rotoProperty.setProperty("Menus.Catch", String.valueOf(frames.jcCatch.isSelected()));
        rotoProperty.setProperty("Menus.Catch.Normal", String.valueOf(frames.jrNormalCatch.isSelected()));
        rotoProperty.setProperty("Menus.Catch.Magnet", String.valueOf(frames.jrMagnetCatch.isSelected()));
        rotoProperty.setProperty("Menus.Fan", String.valueOf(frames.jcFan.isSelected()));
        rotoProperty.setProperty("Menus.Bar", String.valueOf(frames.jrBar.isSelected()));
        rotoProperty.setProperty("Menus.Lock", String.valueOf(frames.jcCilinder.isSelected()));
        rotoProperty.setProperty("Menus.ClosableBar", String.valueOf(frames.jrClosableBar.isSelected()));
        rotoProperty.setProperty("Menus.ClosableBar.Type", String.valueOf(frames.jcbBar.getSelectedItem()));
        rotoProperty.setProperty("Menus.SelectionBar.Type", String.valueOf(frames.jcbSelectionBar.getSelectedIndex()));
        rotoProperty.setProperty("Menus.Color", String.valueOf(frames.jcColor.isSelected()));
        rotoProperty.setProperty("Menus.Color.Type", swc.getLocalisedColors().get(frames.jcbColors.getSelectedItem()));
        propertyUtil.saveRotoProperty();
        message.showInformationMessage(propertyUtil.getLangText("Information.Menus.Save.Ready"));
    }

    //Load menu positions and settings
    private void loadMenuSettings() {
        Properties rotoProperty = propertyUtil.getRotoProperty();
        frames.jrLeft.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Direction.Left"));
        frames.tbLeft.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Direction.Left"));
        frames.jrRight.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Direction.Right"));
        frames.tbRight.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Direction.Right"));
        frames.jrOver.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Direction.Over"));
        frames.tbOver.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Direction.Over"));
        frames.jcHandle.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Handle"));
        frames.tbHandle.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Handle"));
        frames.jcLiftingMD.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.LiftingMD"));
        frames.tbLiftingMD.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.LiftingMD"));
        frames.jcCatch.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Catch"));
        frames.tbCatchNone.setSelected(!propertyUtil.getBooleanProperty(rotoProperty, "Menus.Catch"));
        if (frames.jcCatch.isSelected()) {
            frames.tbCatch.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Catch.Normal"));
            frames.tbCatchMagnet.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Catch.Magnet"));
        }
        frames.jrNormalCatch.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Catch.Normal"));
        frames.jrMagnetCatch.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Catch.Magnet"));
        frames.jcFan.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Fan"));
        frames.tbFan.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Fan"));

        frames.tbBar.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Bar"));
        frames.jrClosableBar.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.ClosableBar"));
        frames.tbClosableBar.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.ClosableBar"));
        if (frames.tbClosableBar.isSelected()) {
            frames.tbCilinder.setEnabled(true);
        }
        if (propertyUtil.getStringProperty(rotoProperty, "Menus.ClosableBar.Type").length() > 0) {
            frames.tbClosableBar.setImageText(propertyUtil.getStringProperty(rotoProperty, "Menus.ClosableBar.Type"));
            frames.canvas
                  .jlClosableBar
                  .setText(propertyUtil.getStringProperty(rotoProperty, "Menus.ClosableBar.Type"));
        }
        frames.jcbBar.setSelectedItem(propertyUtil.getStringProperty(rotoProperty, "Menus.ClosableBar.Type"));
        frames.jcbSelectionBar.setSelectedIndex(propertyUtil.getIntProperty(rotoProperty, "Menus.SelectionBar.Type"));

        List<JRMenuItem> sBarList = new ArrayList<JRMenuItem>();
        sBarList.add(frames.jmiBarKo);
        sBarList.add(frames.jmiBarKsr);
        sBarList.add(frames.jmiBarM);
        sBarList.get(propertyUtil.getIntProperty(rotoProperty, "Menus.SelectionBar.Type")).doClick();

        frames.jcCilinder.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Lock"));
        frames.tbCilinder.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Lock"));

        String selectedColor =
            propertyUtil.getColorMap().get(propertyUtil.getStringProperty(rotoProperty, "Menus.Color.Type"));

        if (selectedColor == null || selectedColor.length() == 0)
            selectedColor = swc.colorArray[0];
        frames.jcColor.setSelected(propertyUtil.getBooleanProperty(rotoProperty, "Menus.Color"));
        //System.out.println("Selected: " + propertyUtil.getLangText(propertyUtil.getStringProperty(rotoProperty, "Menus.Color.Type")));
        frames.jcbColors.setSelectedItem(propertyUtil.getLangText(propertyUtil.getStringProperty(rotoProperty,
                                                                                                 "Menus.Color.Type")));

        if (selectedColor.equals(swc.colorArray[0])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorNone.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorWhite.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[1])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorWhite.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorWhite.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[2])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorBrown.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorBrown.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[3])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorNewSilver.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorNewSilver.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[4])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorNatureSilver.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorNatureSilver.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[5])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorGold.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorGold.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[6])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorMatGold.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorMatGold.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[7])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorBrass.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorBrass.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[8])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorTitan.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorTitan.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[9])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorOliveBrown.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorOliveBrown.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[10])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorGreyBrown.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorGreyBrown.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[11])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorMiddleBrass.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorMiddleBrass.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[12])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorDarkBrass.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorDarkBrass.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[13])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorCreamWhite.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorCreamWhite.jpg"));
        }
        if (selectedColor.equals(swc.colorArray[14])) {
            frames.jbColors.setIcon(new ImageIcon(swc.iconPath + "ColorJetBlack.jpg"));
            frames.tbHandle.setIcon(new ImageIcon(swc.iconPath + "HandleColorJetBlack.jpg"));
        }

        swc.setSelectedColorSign(selectedColor);

    }

    //Load administrator settings
    private void loadAdminSettings() {
        adminUtil.fillLoginCombo();
    }

    //Load rebate settings
    private void loadDefaultRebate() {
        rebateUtil.loadDefaultRebate(profileUtil.getProfileName());
    }

    //Load currency
    private void loadCurrency() {
        frames.jcbCurrency.setSelectedIndex(0);
        Properties rotoProperty = propertyUtil.getRotoProperty();
        String priceListCurrency = propertyUtil.getStringProperty(rotoProperty, "Pricelist.Currency");
        if (priceListCurrency.length() > 0) {
            frames.jcbCurrency.setSelectedItem(priceListCurrency);
            priceUtil.setCurrency(priceListCurrency);
            frames.tfCurrency.setText(priceListCurrency);
        } else {
            frames.jcbCurrency.setSelectedIndex(0);
            priceUtil.setCurrency(swc.currencies[0]);
            frames.tfCurrency.setText(swc.currencies[0]);
        }
    }

    private void saveCurrency() {

        Properties rotoProperty = propertyUtil.getRotoProperty();
        rotoProperty.setProperty("Pricelist.Currency", frames.jcbCurrency
                                                             .getSelectedItem()
                                                             .toString());
        propertyUtil.saveRotoProperty();
        priceUtil.setCurrency(frames.jcbCurrency
                                    .getSelectedItem()
                                    .toString());
        frames.tfCurrency.setText(frames.jcbCurrency
                                        .getSelectedItem()
                                        .toString());

        message.showInformationMessage(propertyUtil.getLangText("Information.Currencies.Save.Ready"));
    }

    //Load price list by property
    private void loadPriceList() {
        frames.jriPriceListView.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        Properties rotoProperty = propertyUtil.getRotoProperty();
        String PriceListPath = propertyUtil.getStringProperty(rotoProperty, "Pricelist.Path");
        String PriceListCurrency = propertyUtil.getStringProperty(rotoProperty, "Pricelist.Currency");
        String MartikelSelected = propertyUtil.getStringProperty(rotoProperty, "Pricelist.PrintMartikel");

        if (MartikelSelected.length() > 0 && MartikelSelected.equals("true")) {
            Frames.getInstance()
                  .jrPrintMartikel
                  .setSelected(true);
        } else {
            Frames.getInstance()
                  .jrPrintSap
                  .setSelected(true);
        }

        if (PriceListPath.length() == 0) {
            //Get default pricelist as excel
            PriceListPath = swc.priceListPath + "\\Pricelist_HUF.xls";
            //Get default pricelist as prn if excel is not exists
            if (!(new File(PriceListPath)).exists()) {
                PriceListPath = swc.priceListPath + "\\Pricelist_HUF.prn";
            }
        }

        if (PriceListCurrency.length() == 0) {
            PriceListCurrency = "HUF";
        }

        Double Multiplier = propertyUtil.getDoubleProperty(rotoProperty, "Pricelist.Multiplier");
        if (Multiplier < 0.0 || Multiplier == 0) {
            Multiplier = 1.0;
        }
        File fPriceList = new File(PriceListPath);
        if (fPriceList.getName()
                      .toUpperCase()
                      .endsWith(".PRN")) {
            priceUtil.loadTextPriceList(fPriceList, frames.jtPriceListView, Multiplier);
        } else if (fPriceList.getName()
                             .toUpperCase()
                             .endsWith(".XLS")) {
            priceUtil.loadExcelPriceList(fPriceList, frames.jtPriceListView, Multiplier);
        }
        frames.tfPriceListName.setText(fPriceList.getName());
        frames.tfCurrency.setText(PriceListCurrency);
        frames.jntMultiplier.setText(String.valueOf(Multiplier));

        // Copy price list to select list
        priceUtil.copyPriceList(frames.jtPriceListSelect);

        //Refresh collection
        selectDatabaseRows();

        //Check price list groups
        priceUtil.checkPriceListGroups();

        frames.jriPriceListView.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    //Reload material groups
    private void loadGroups() {
        groupUtil.loadGroups();
        frames.jlGroupNames.setModel(groupUtil.getGroupNameListModel());
        if (groupUtil.getGroupNameListModel().size() > 0) {
            frames.jlGroupNames.setSelectedIndex(0);
        }
    }

    //Load database list
    private void loadDatabaseList() {
        Properties property = propertyUtil.getDatabaseListProperty();
        Enumeration<Object> enumeration = property.keys();


        frames.dlDatabaseFilter.removeAllElements();
        frames.dlDatabaseFilter.clear();

        while (enumeration.hasMoreElements()) {
            frames.dlDatabaseFilter.addElement(enumeration.nextElement().toString());
        }

        //Sort items
        Object[] values = frames.dlDatabaseFilter.toArray();
        Arrays.sort(values);

        frames.dlDatabaseFilter.removeAllElements();
        frames.dlDatabaseFilter.clear();

        for (int i = 0; i < values.length; i++) {
            frames.dlDatabaseFilter.addElement(values[i].toString());
        }

        //Set index
        if (frames.dlDatabaseFilter.size() > 0) {
            frames.jlDatabaseFilter.setSelectedIndex(0);
        }
    }

    //Save material groups
    private void saveGroups() {
        //Save groups
        groupUtil.saveGroups();
        //Refresh rebate to handle modified groups
        loadDefaultRebate();
    }

    //Load settings after gui built
    private void loadSettings() {
        //Load menu settings
        loadMenuSettings();
        //Load admin settings;
        loadAdminSettings();
        //Load currency
        loadCurrency();
        //Load material goups
        loadGroups();
        //Load price list
        loadPriceList();
        //Load database list
        loadDatabaseList();
        //Load rebate
        loadDefaultRebate();
        //Load window systems
        loadWindowSystems();
        //Load selected language
        loadLanguages();
        //Load correcnt registration
        loadRegistration();
        //Load correctioon
        loadCorrection();
        //Load font size
        loadFontSize();
    }

    // Set profile name to textfield from the profile combo
    private void profileSelected() {
        if (frames.jcbProfiles.getItemCount() > 0) {
            frames.tfProfile.setText(frames.jcbProfiles
                                           .getSelectedItem()
                                           .toString()
                                           .trim());
        }
    }

}
