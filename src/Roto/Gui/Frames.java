package Roto.Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.TreeSelectionModel;

import Roto.Basic.Actions;
import Roto.Basic.SwingConstans;

import Roto.Gui.Tree.CheckNode;
import Roto.Gui.Tree.CheckRenderer;
import Roto.Gui.Tree.NodeSelectionListener;

import Roto.Utils.PropertyUtil;

import Roto.Windows.WindowLimitTableModel;
import Roto.Windows.WindowTableModel;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class Frames extends JInternalFrame implements InternalFrameListener, ActionListener, MouseListener,
                                                      ListSelectionListener, DragSourceListener, DragGestureListener,
                                                      KeyListener {
    private static final long serialVersionUID = -3978786598428121894L;


    private JDesktopPane mainDeskTop;
    private PropertyUtil propertyUtil;
    private SwingConstans swc;
    private static Frames instance = null;

    public JRFrame jrfMain, jrfResize;
    public JRInternalFrame jriMenus, jriPriceListView, jriPriceListSelect, jriMaterialGroups, jriDbListEditor, jriRebate, jriAdminLogin, jriWindows, jriCollectionList, jriCollectionEdit, jriExcelList, jriEditDataBase, jriReplaceData, jriCurrency, jriItemList, jriLanguages, jriLanguagesEdit, jriRegister, jriRegisterInfo, jriCorrection, jriProfiles, jriFontSize, jriAbout;
    public JMenuBar mainMenuBar;
    public JRMenu jmDb, jmEdit, jmExit, jmList, jmOptions, jmProtection, jmView, jmPrintPreview, jmPrint, jmRegister, jmTypes, jmAdmin, jmListSave, jmListSaveExcel;
    public JRMenuItem jmiOpen, jmiClose, jmiStockList, jmiLang, jmiExit, jmiExitSave, jmiOpenEdit, jmiOpenNewDb, jmiCloseDb, jmiSave, jmiSaveAs, jmiSaveExcel, jmiReplaceData, jmiListDelete, jmiListDetailedSave, jmiListSummarizedSave, jmiListDetailedSaveExcel, jmiListSummarizedSaveExcel, jmiListGarnitureSaveExcel, jmiListNavisonSave, jmiListOpen, jmiLanguage, jmiCorrection, jmiPricelist, jmiCurrency, jmiRabate, jmiGroups, jmiTypes, jmiMenus, jmiListProjectSave, jmiItems, jmiPrintPreviewSummarized, jmiPrintPreviewDetailed, jmiPrintPreviewGarniture, jmiProjectDatabase, jmiPrintDetailed, jmiPrintProject, jmiPrintGarniture, jmiRegisterData, jmiRegisterProgram, jmiFontSize, jmiDataBaseList, jmiEditDataBaseList, jmiAdminLogin, jmiAdminLogout, jmiListDetailedConcat, jmiProfiles;
    public JRMenuItem jmiColorNone, jmiColorWhite, jmiColorBrown, jmiColorNatureSilver, jmiColorNewSilver, jmiColorGold, jmiColorMatGold, jmiColorBrass, jmiColorTitan, jmiColorOliveBrown, jmiColorGreyBrown, jmiColorMiddleBrass, jmiColorDarkBrass, jmiColorCreamWhite, jmiColorJetBlack, jmiProfileNewUser, jmiProfileNew, jmiProfileModify, jmiProfileDelete, jmiAdminProfiles, jmiAdminLang, jmiAdminReplaceData, jmiAdminTypes, jmiAbout, jmiBarKo, jmiBarKsr, jmiBarM, jmiNTn3, jmiNTn6, jmiNXx3, jmiNXx6, jmiNXxe;

    public ToolBarButton tbLeft, tbRight, tbOver, tbCatch, tbCatchMagnet, tbMDK, tbMDF, tbMDS, tbMKIPP, tbMDK2, tbMDF2, tbMDS2, tbMFREI, tbMDSMDK2, tbMDSMDF2, tbBar, tbClosableBar, tbSelectionBar, tbCatchNone, tbHandle, tbLiftingMD, tbFan, tbCilinder, tbViewer, tbNX, tbNT;
    public JTextField tfProfile, tfUser, tfPriceListName, tfGroup, tfGroupName, tfAdminFileName, tfFilterText, tfRebateFileName, tfWindowTypes, tfCurrency, tfMainSumPrice, tfKey1, tfKey2, tfKey3, tfCode1, tfCode2, tfCode3, tfRegCompany, tfRegAddress, tfRegOperator, tfRegEmail, tfRegSalesRep, tfRegComment, tfRegCompanyTitle, tfRegAddressTitle, tfRegOperatorTitle, tfRegEmailTitle, tfRegSalesRepTitle, tfRegCommentTitle, tfExportDirectory, tfSubDirectory, tfProfilesInput, tfRebateProfileLocation, tfCollectionSum;
    public ButtonGroup groupDirection, groupBar, groupCatch, groupTypes, groupMenuBar, groupFlags, groupNTNX;
    public JTabbedPane jtpLogin;
    public JPanel pnMainLeft, pnMainTop, pnLoginTab, pnLogin, pnResize, pnDatabases, pnDataBaseButtons, pnRebateAdmin, pnHeaderSelector;
    public JToolBar jtTopLeftToolBar, jtTopCenterToolBar, jtTopRightToolBar, jtLeftToolBar;
    public JButton jbLogin, jbLogout, jbMenus, jbMenuSave, jbColors, jbPriceListOpen, jbPriceListSave, jbGroupModify, jbGroupAdd, jbGroupDelete, jbGroupNameModify, jbGroupNameAdd, jbGroupNameDelete, jbGroupSave, jbGroupReload, jbMoveLeft, jbMoveLeftAll, jbMoveRight, jbMoveRightAll, jbDbListBrowse, jbDbListOpen, jbCustomPriceListAdd, jbCustomPriceListRemove, jbPriceListFilter, jbPriceListFilterAdd, jbAdminRebateSave, jbAdminRebateOpen, jbRebateSave, jbAdminLogin, jbAdminLogout, jbWindowsAdd, jbWindowsModify, jbWindowsDelete, jbWindowsSave, jbWindowsLeft, jbWindowsRight, jbAddCollection, jbRemoveCollection, jbCollectionEditAdd, jbCollectionEditRemove, jbCollectionRemove, jbCollectionEditIncrease, jbCollectionEditDecrease, jbSaveExcelList, jbPrintExcelList, jbEditDbClearSorter, jbEditDbAddItem, jbEditDbRemoveItem, jbEditDbMoveUp, jbEditDbMoveDown, jbEditDbSave, jbEditDbSaveAs, jbEditDbSaveExcel, jbEditDbOpen, jbEditDbNew, jbEditDbClose, jbOpenDb, jbOpenListDb, jbReplaceDataAdd, jbReplaceDataRemove, jbReplaceListAdd, jbReplaceListRemove, jbReplace, jbCurrencySave, jbPriceListCurrency, jbStockRemoveItem, jbStockAddItem, jbStockAddFile, jbStockRemoveFile, jbStockListOpen, jbStockListSave, jbStockListCollect, jbStockListCollectDelete, jbStockListPrint, jbStockListSaveExcel, jbLanguagesSave, jbLanguageShowEmpty, jbLanguageEditorAdd, jbLanguageEditorSave, jbRegisterSaveForm, jbRegisterOpenForm, jbRegisterDemo, jbRegisterNewKey, jbRegisterExit, jbRegister, jbCorrectionSave, jbProfileAssembly, jbProfileExport, jbExportDirectory, jbRebateTreeSet, jbRebateTreeOpen, jbRebateTreeDelete, jbFontSizeSave, jbLoginStatus;
    public JCheckBox jcHandle, jcLiftingMD, jcCatch, jcFan, jcColor, jcCilinder, jcDbEditDeleteConfirm, jcStockItemDeleteConfirm, jcStockFileDeleteConfirm;
    public JRadioButton jrNormalCatch, jrMagnetCatch, jrLeft, jrRight, jrOver, jrBar, jrClosableBar, jrOrder, jrOffer, jrReplaceSAP, jrReplaceText, jrPrintSap =
        new JRadioButton("SAP", true), jrPrintMartikel = new JRadioButton("Martikel");
    public JLabel jlLogin, jlPc, jlWindowReview, jlEditingDbName, jlMainDatabaseName, jlLanguageLeft, jlLanguageRight, jlExportLoaderLabel, jlRotoIcon;
    public JTabbedPane mainTabPane, windowTabPane;
    public JComboBox jcbBar, jcbColors, jcbProfiles, jcbAdminLogin, jcbAdminProfiles, jcbWinowTypes, jcbWinowSystems, jcbEditDatabaseTyes, jcbCurrency, jcbLanguageLeft, jcbLanguageRight, jcbFontSize, jcbSelectionBar;
    public JPopupMenu jpBar, jpSelectionBar, jpColors, jpProfiles, jpProfilesInput, jpNT, jpNX;
    public JTable jtCollection, jtPriceListView, jtPriceListSelect, jtCustomPriceList, jtRebate, jtRebateGroups, jtWindows, jtCollectionList, jtCollectionEdit, jtExcelList, jtExcelHeader, jtEditDataBase, jtReplaceData, jtStockList, jtLanguagesEdit, jtWindowLimits;
    public DefaultTableModel tmCollection, tmPriceListView, tmPriceListSelect, tmCustomPriceList, tmRebate, tmRebateGroups, tmWindows, tmCollectionList, tmCollectionEdit, tmExcelList, tmExcelHeader, tmStockList, tmLanguagesEdit;
    public DataBaseTableModel tmdbEditDataBase;
    public ReplaceTableModel tmrReplaceData;
    public JRNumberTextField jntWidth, jntWidth2, jntHeight, jntHeight2, jntCorrectionWidth, jntCorrectionHeight, jntMultiplier, jntFilterSAP, jntGarniture, jntCorrectionSaveWidth, jntCorrectionSaveHeight;
    public DefaultListModel dlGroups, dlGroupNames, dlDatabases, dlDatabaseFilter, dlRebate, dlCustomPriceList, dlCollectionEdit, dlReplaceFiles, dlStockFiles;
    public JList jlGroups, jlGroupNames, jlDatabases, jlDatabaseFilter, jlRebate, jlCustomPriceList, jlCollectionEdit, jlReplaceFies, jlStockFiles;
    public DragSource dragSource;
    public StringSelection transferable;
    public JTextArea jtRebateInfo, jtExcelComment, jtReplaceInfo, jtExportInfo;
    public JTextPane jtpRegisterInfo;
    public DisplayCanvas canvas;
    public WindowTableModel windowTableModel;
    public WindowLimitTableModel windowLimitTableModel;
    public JSplitPane mainSplitPane;
    public JTree profileTree, exportTree, rebateTree;
    public CheckNode nodeRoot, nodeRootDirection, nodeLeft, nodeRight, nodeOver, nodeRootHandle, nodeHandleYes, nodeHandleNo, nodeRootLiftingMd, nodeLiftingMdYes, nodeLiftingMdNo, nodeRootCatch, nodeCatchNone, nodeCatchNormal, nodeCatchMagnet, nodeRootFan, nodeFanYes, nodeFanNo, nodeRootBar, nodeNormalBar, nodeRootClosableBar, nodeRootCilinder, nodeCilinderYes, nodeCilinderNo, nodeRootColor, nodeRootColumn, nodeMDK, nodeMDS, nodeMDF, nodeMKIPP, nodeMDK2, nodeMDS2, nodeMDF2, nodeMFREI;
    public CheckNode nodeRootProfiles, nodeRootExport, nodeRootRebate, nodeRootSelectionBar, nodeBarKo, nodeBarKsr, nodeBarM;
    public JPasswordField tfPassWord;

    public static Frames getInstance() {
        if (instance == null) {
            instance = new Frames();
        }
        return instance;
    }

    public Frames() {
        init();
    }

    private void init() {
        mainDeskTop = new JDesktopPane();
        propertyUtil = PropertyUtil.getInstance();
        swc = SwingConstans.getInstance();
        dragSource = new DragSource();
        canvas = new DisplayCanvas();
    }

    public void loadStartUpSettings() {
        Actions.getInstance().doAction("loadstartupsettings");
    }

    public void createFrame_Resize() {
        jrfResize = new JRFrame("ResizeFrame");
        jrfResize.setFrameSize(400, 400);
        jrfResize.setFrameLocation(100, 100);
        jrfResize.setLayout(new BorderLayout());
    }

    public void createMainFrame() {
        //Create main frame

        //Don't change Main name, this used on display canvas to get position
        jrfMain = new JRFrame("Main");
        jrfMain.setFrameSize(1024, 680);
        jrfMain.setFrameLocation(100, 100);
        jrfMain.setJMenuBar(mainMenuBar = new JMenuBar());
        jrfMain.setLayout(new BorderLayout());
        jrfMain.setTitle(propertyUtil.getLangText("Frame.Main.Title") + swc.version);
        jrfMain.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        jrfMain.setClosingCommand("programexit");
        // Create menus

        // Database
        mainMenuBar.add(jmDb = new JRMenu("Menu.Database", "Database.jpg"));
        mainMenuBar.setBackground(Color.WHITE);
        jmDb.add(jmiOpen = new JRMenuItem("MenuItem.Database.Open", "Open.jpg"));
        jmDb.add(jmiDataBaseList = new JRMenuItem("MenuItem.Database.List", "DatabaseList.jpg"));
        jmDb.add(jmiClose = new JRMenuItem("MenuItem.Database.Close", "Close.jpg"));
        jmDb.addSeparator();
        jmDb.add(jmiStockList = new JRMenuItem("MenuItem.Database.StockList", "List.jpg"));
        jmDb.add(jmEdit = new JRMenu("Menu.Database.Edit", "Edit.jpg"));
        jmDb.add(jmiLang = new JRMenuItem("MenuItem.Database.Languages", "Languages.jpg"));
        jmDb.addSeparator();
        // Database - Exit
        jmDb.add(jmiExit = new JRMenuItem("Menu.Database.Exit", "Exit.jpg"));
        // Database - Edit
        jmEdit.add(jmiOpenEdit = new JRMenuItem("MenuItem.Database.Open", "Open.jpg"));
        jmEdit.addSeparator();
        jmEdit.add(jmiOpenNewDb = new JRMenuItem("MenuItem.Database.Edit.New", "DatabaseNew.jpg"));
        jmEdit.add(jmiCloseDb = new JRMenuItem("MenuItem.Database.Edit.Close", "DatabaseClose.jpg"));
        jmEdit.addSeparator();
        jmEdit.add(jmiSave = new JRMenuItem("MenuItem.Database.Edit.Save", "Save.jpg"));
        jmEdit.add(jmiSaveAs = new JRMenuItem("MenuItem.Database.Edit.SaveAs", "SaveAs.jpg"));
        jmEdit.add(jmiSaveExcel = new JRMenuItem("MenuItem.Database.Edit.SaveExcel", "Excel.jpg"));
        jmEdit.addSeparator();
        jmEdit.add(jmiReplaceData = new JRMenuItem("MenuItem.Database.Edit.DataReplace", "DataReplace.jpg"));
        // List
        mainMenuBar.add(jmList = new JRMenu("Menu.List", "CollateList.jpg"));
        // List - Open
        jmList.add(jmiListOpen = new JRMenuItem("Menu.List.Open", "Open.jpg"));
        jmList.add(jmiListDetailedConcat = new JRMenuItem("Menu.List.Concat", "ListDetailedConcat.jpg"));
        jmList.addSeparator();
        // List - Save list
        jmList.add(jmListSave = new JRMenu("Menu.List.Save", "Save.jpg"));
        jmListSave.add(jmiListSummarizedSave = new JRMenuItem("MenuItem.List.Save.Summarized", "ListProject.jpg"));
        jmListSave.add(jmiListDetailedSave = new JRMenuItem("MenuItem.List.Save.Detailed", "ListDetailed.jpg"));
        // List - Save excel
        jmList.add(jmListSaveExcel = new JRMenu("Menu.List.Save.Excel", "Excel.jpg"));
        jmListSaveExcel.add(jmiListDetailedSaveExcel =
                            new JRMenuItem("MenuItem.List.Save.Detailed", "ListDetailed.jpg"));
        jmListSaveExcel.add(jmiListSummarizedSaveExcel =
                            new JRMenuItem("MenuItem.List.Save.Summarized", "ListProject.jpg"));
        jmListSaveExcel.add(jmiListGarnitureSaveExcel =
                            new JRMenuItem("MenuItem.List.Save.Garniture", "ListGarniture.jpg"));
        jmList.addSeparator();
        // List - Save Navison
        jmList.add(jmiListNavisonSave = new JRMenuItem("MenuItem.List.Save.Navison", "ListGarniture.jpg"));
        // List - Save Project
        jmList.add(jmiListProjectSave = new JRMenuItem("MenuItem.List.Save.Project", "ListGarniture.jpg"));
        jmList.addSeparator();
        // List - Delete
        jmList.add(jmiListDelete = new JRMenuItem("MenuItem.List.Delete", "ListDelete.jpg"));

        // Options
        mainMenuBar.add(jmOptions = new JRMenu("Menu.Options", "Options.jpg"));
        jmOptions.add(jmiLanguage = new JRMenuItem("MenuItem.Options.Language", "Languages.jpg"));
        jmOptions.add(jmiMenus = new JRMenuItem("Menu.Options.Menus", "Menus.jpg"));
        jmOptions.add(jmiFontSize = new JRMenuItem("MenuItem.Options.FontSize", "FontSize.jpg"));
        jmOptions.addSeparator();
        jmOptions.add(jmiEditDataBaseList = new JRMenuItem("MenuItem.Options.Databases", "DatabaseList.jpg"));
        jmOptions.add(jmiTypes = new JRMenuItem("MenuItem.Options.Types", "Types.jpg"));
        jmOptions.add(jmiCorrection = new JRMenuItem("MenuItem.Options.Correction", "Correction.jpg"));
        jmOptions.addSeparator();
        jmOptions.add(jmiPricelist = new JRMenuItem("MenuItem.Options.Pricelist", "Pricelist.jpg"));
        jmOptions.add(jmiCurrency = new JRMenuItem("MenuItem.Options.Currency", "Money.jpg"));
        jmOptions.add(jmiRabate = new JRMenuItem("MenuItem.Options.Rabate", "Rabate.jpg"));
        jmOptions.add(jmiGroups = new JRMenuItem("MenuItem.Options.Groups", "Groups.jpg"));
        jmOptions.addSeparator();
        jmOptions.add(jmiProfiles = new JRMenuItem("MenuItem.Admin.Profiles", "Profiles.jpg"));

        // Options

        // View
        mainMenuBar.add(jmView = new JRMenu("Menu.View", "View.jpg"));
        jmView.add(jmPrintPreview = new JRMenu("Menu.View.PrintPreview", "PrintPreview.jpg"));
        jmView.add(jmiItems = new JRMenuItem("MenuItem.View.Items", "Items.jpg"));
        // View - Print Preview		
        jmPrintPreview.add(jmiPrintPreviewSummarized =
                           new JRMenuItem("MenuItem.View.PrintPreview.Summarized", "ListProject.jpg"));
        jmPrintPreview.add(jmiPrintPreviewDetailed =
                           new JRMenuItem("MenuItem.View.PrintPreview.Detailed", "ListDetailed.jpg"));
        jmPrintPreview.add(jmiPrintPreviewGarniture =
                           new JRMenuItem("MenuItem.View.PrintPreview.Garniture", "ListGarniture.jpg"));
        // Print
        mainMenuBar.add(jmPrint = new JRMenu("Menu.Print", "Print.jpg"));
        jmPrint.add(jmiPrintProject = new JRMenuItem("MenuItem.Print.Summarized", "ListProject.jpg"));
        jmPrint.add(jmiPrintDetailed = new JRMenuItem("MenuItem.Print.Detailed", "ListDetailed.jpg"));
        jmPrint.add(jmiPrintGarniture = new JRMenuItem("MenuItem.Print.Garniture", "ListGarniture.jpg"));
        // Register
        mainMenuBar.add(jmRegister = new JRMenu("Menu.Register", "Register.jpg"));
        jmRegister.add(jmiRegisterData = new JRMenuItem("MenuItem.Register.Data", "Data.jpg"));
        jmRegister.add(jmiRegisterProgram = new JRMenuItem("MenuItem.Register.Program", "RegisterProgram.jpg"));
        jmRegister.add(jmiAbout = new JRMenuItem("MenuItem.Register.About", "About.jpg"));
        // Admin
        mainMenuBar.add(jmAdmin = new JRMenu("Menu.Admin", "Admin.jpg"));
        jmAdmin.add(jmiAdminLogin = new JRMenuItem("MenuItem.Admin.Login", "Protection.jpg"));
        jmAdmin.add(jmiAdminLogout = new JRMenuItem("MenuItem.Admin.Logout", "Login.jpg"));
        jmAdmin.addSeparator();
        jmAdmin.add(jmiAdminProfiles = new JRMenuItem("MenuItem.Admin.Profiles", "Profiles.jpg"));
        jmAdmin.add(jmiAdminLang = new JRMenuItem("MenuItem.Database.Languages", "Languages.jpg"));
        jmAdmin.add(jmiAdminReplaceData = new JRMenuItem("MenuItem.Database.Edit.DataReplace", "DataReplace.jpg"));
        jmAdmin.add(jmiAdminTypes = new JRMenuItem("MenuItem.Options.Types", "Types.jpg"));

        //Database name
        JPanel pnMenuPanel = new JPanel();
        pnMenuPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnMenuPanel.add(jlMainDatabaseName =
                        new JLabel("...", new ImageIcon(swc.iconPath + "DataBase.jpg"), JLabel.RIGHT));
        mainMenuBar.add(pnMenuPanel);
        //Icon
        jlRotoIcon = new JLabel(new ImageIcon(swc.iconPath + "Roto.jpg"));
        mainMenuBar.add(jlRotoIcon);
        jlRotoIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jlRotoIcon.setBackground(Color.WHITE);

        // Left toolbar
        pnMainLeft = new JPanel();
        pnMainLeft.setLayout(new BorderLayout());
        pnMainLeft.add(jtLeftToolBar = new JToolBar(), "Center");
        pnMainLeft.setBackground(Color.WHITE);
        pnMainLeft.setBorder(BorderFactory.createRaisedBevelBorder());
        jtLeftToolBar.setBackground(Color.WHITE);
        jtLeftToolBar.setLayout(new GridLayout(17, 1));

        groupTypes = new ButtonGroup();
        tbMDK = new ToolBarButton("", "MDK");
        groupTypes.add(tbMDK);
        tbMDF = new ToolBarButton("", "MDF");
        groupTypes.add(tbMDF);
        tbMDS = new ToolBarButton("", "MDS");
        groupTypes.add(tbMDS);
        tbMKIPP = new ToolBarButton("", "MKIPP");
        groupTypes.add(tbMKIPP);
        tbMDK2 = new ToolBarButton("", "MDK2");
        groupTypes.add(tbMDK2);
        tbMDF2 = new ToolBarButton("", "MDF2");
        groupTypes.add(tbMDF2);
        tbMDS2 = new ToolBarButton("", "MDS2");
        groupTypes.add(tbMDS2);
        tbMFREI = new ToolBarButton("", "MFREI");
        groupTypes.add(tbMFREI);
        tbMDSMDK2 = new ToolBarButton("", "MDSMDK2");
        groupTypes.add(tbMDSMDK2);
        tbMDSMDF2 = new ToolBarButton("", "MDSMDF2");
        groupTypes.add(tbMDSMDF2);

        jtLeftToolBar.add(new JLabel("", new ImageIcon(swc.iconPath + "Types.jpg"), JLabel.CENTER));
        jtLeftToolBar.add(tbMDK);
        jtLeftToolBar.add(tbMDF);
        jtLeftToolBar.add(tbMDS);
        jtLeftToolBar.add(tbMKIPP);
        jtLeftToolBar.add(tbMDK2);
        jtLeftToolBar.add(tbMDF2);
        jtLeftToolBar.add(tbMDS2);
        jtLeftToolBar.add(tbMFREI);
        jtLeftToolBar.add(tbMDSMDK2);
        jtLeftToolBar.add(tbMDSMDF2);

        //		jtLeftToolBar.setPreferredSize(new Dimension(swc.menuToolbarWidth, swc.menuToolbarHeight));
        jtLeftToolBar.setMinimumSize(new Dimension(0, 0));
        pnMainLeft.setMinimumSize(new Dimension(0, 0));

        tbMDK.setSelected(true);
        jtLeftToolBar.setVisible(false);

        // Top left toolbar

        pnMainTop = new JPanel();
        pnMainTop.add(jtTopLeftToolBar = new JToolBar());
        pnMainTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnMainTop.setBackground(Color.WHITE);
        jtTopLeftToolBar.setBackground(Color.WHITE);
        jtTopLeftToolBar.addSeparator();

        tbLeft = new ToolBarButton("Left.jpg", null, "ToolBarButton.Left.ToolTipText");
        tbRight = new ToolBarButton("Right.jpg", null, "ToolBarButton.Right.ToolTipText");
        tbOver = new ToolBarButton("Over.jpg", null, "ToolBarButton.Over.ToolTipText");
        tbCatch = new ToolBarButton("Catch.jpg", null, "ToolBarButton.Catch.ToolTipText");
        tbCatchMagnet = new ToolBarButton("CatchMagnet.jpg", null, "ToolBarButton.CatchMagnet.ToolTipText");
        tbCatchNone = new ToolBarButton("CatchNone.jpg", null, "ToolBarButton.CatchNone.ToolTipText");
        tbHandle = new ToolBarButton("Handle.jpg", null, "ToolBarButton.Handle.ToolTipText");
        tbLiftingMD = new ToolBarButton("LiftingMD.jpg", null, "ToolBarButton.LiftingMD.ToolTipText");
        tbClosableBar = new ToolBarButton("ClosableBar.jpg", null, "ToolBarButton.ClosableBar.ToolTipText");
        tbClosableBar.setImageText("25");
        tbSelectionBar = new ToolBarButton("Bar.jpg", null, "ToolBarButton.SelectionBar.ToolTipText");
        tbSelectionBar.setImageText("K");
        tbBar = new ToolBarButton("Bar.jpg", null, "ToolBarButton.Bar.ToolTipText");
        tbFan = new ToolBarButton("Fan.jpg", null, "ToolBarButton.Fan.ToolTipText");
        tbCilinder = new ToolBarButton("Lock.jpg", null, "ToolBarButton.Cilinder.ToolTipText");
        tbNT = new ToolBarButton("NT.jpg", null, "ToolBarButton.NT.ToolTipText");
        tbNT.setImageText("3/100");
        tbNX = new ToolBarButton("NX.jpg", null, "ToolBarButton.NX.ToolTipText");
        tbNX.setImageText("3/130");

        jbColors = new JButton(new ImageIcon(swc.iconPath + "ColorNone.jpg"));
        jbColors.setToolTipText(propertyUtil.getLangText("Frame.Button.Color.ToolTipText"));
        jbColors.addActionListener(this);
        jbColors.addMouseListener(this);
        jbColors.setActionCommand("colorspressed");

        groupBar = new ButtonGroup();
        groupBar.add(tbBar);
        groupBar.add(tbClosableBar);

        groupDirection = new ButtonGroup();
        groupDirection.add(tbRight);
        groupDirection.add(tbLeft);
        groupDirection.add(tbOver);

        tbLeft.setSelected(true);

        groupCatch = new ButtonGroup();
        groupCatch.add(tbCatch);
        groupCatch.add(tbCatchMagnet);
        groupCatch.add(tbCatchNone);

        groupNTNX = new ButtonGroup();
        groupNTNX.add(tbNX);
        groupNTNX.add(tbNT);

        tbNT.setSelected(true);

        jtTopLeftToolBar.add(tbLeft);
        jtTopLeftToolBar.add(tbRight);
        jtTopLeftToolBar.add(tbOver);
        jtTopLeftToolBar.add(tbHandle);
        jtTopLeftToolBar.add(tbLiftingMD);
        jtTopLeftToolBar.add(tbCatchNone);
        jtTopLeftToolBar.add(tbCatch);
        jtTopLeftToolBar.add(tbCatchMagnet);
        jtTopLeftToolBar.add(tbFan);
        jtTopLeftToolBar.add(tbBar);
        jtTopLeftToolBar.add(tbClosableBar);
        jtTopLeftToolBar.add(tbSelectionBar);
        jtTopLeftToolBar.add(tbNT);
        jtTopLeftToolBar.add(tbNX);
        jtTopLeftToolBar.add(tbCilinder);
        jtTopLeftToolBar.add(jbColors);

        tbCatchNone.setSelected(true);
        tbBar.setSelected(true);
        tbCilinder.setEnabled(false);

        tbClosableBar.addMouseListener(this);
        tbSelectionBar.addMouseListener(this);
        tbNT.addMouseListener(this);
        tbNX.addMouseListener(this);

        // Popup for bar
        jpBar = new JPopupMenu();
        for (int i = 0; i < swc.closableBarArray.length; i++) {
            JMenuItem menuItem = new JMenuItem(swc.closableBarArray[i]);
            jpBar.add(menuItem);
            menuItem.setBackground(Color.WHITE);
            menuItem.addActionListener(this);
            menuItem.setActionCommand("popupbarpressed_" + swc.closableBarArray[i]);
        }

        // Popup for selection bar
        jpSelectionBar = new JPopupMenu();
        jpSelectionBar.add(jmiBarKo = new JRMenuItem("MenuItem.Bar.Ko"));
        jpSelectionBar.add(jmiBarKsr = new JRMenuItem("MenuItem.Bar.Ksr"));
        jpSelectionBar.add(jmiBarM = new JRMenuItem("MenuItem.Bar.M"));

        // Popup for NT
        jpNT = new JPopupMenu();
        jpNT.add(jmiNTn3 = new JRMenuItem("MenuItem.NT.n3"));
        jpNT.add(jmiNTn6 = new JRMenuItem("MenuItem.NT.n6"));

        // Popup for NX
        jpNX = new JPopupMenu();
        jpNX.add(jmiNXx3 = new JRMenuItem("MenuItem.NX.x3"));
        jpNX.add(jmiNXx6 = new JRMenuItem("MenuItem.NX.x6"));
        jpNX.add(jmiNXxe = new JRMenuItem("MenuItem.NX.xe"));

        // Top center toolbar

        pnMainTop.add(jtTopCenterToolBar = new JToolBar());
        jtTopCenterToolBar.setBackground(Color.WHITE);
        jbMenus = new JButton(new ImageIcon(swc.iconPath + "Menus.jpg"));
        tbViewer = new ToolBarButton("Viewer.jpg", null, "ToolBarButton.Viewer.ToolTipText");
        jbOpenDb = new JButton(new ImageIcon(swc.iconPath + "Open.jpg"));
        jbOpenListDb = new JButton(new ImageIcon(swc.iconPath + "DatabaseList.jpg"));
        jbLoginStatus = new JButton(new ImageIcon(swc.iconPath + "LoginDefault.jpg"));

        jbMenus.setToolTipText(propertyUtil.getLangText("ToolBarButton.Menus.ToolTipText"));
        jtTopCenterToolBar.add(jbOpenListDb);
        jtTopCenterToolBar.add(jbOpenDb);
        jtTopCenterToolBar.add(jbMenus);
        jtTopCenterToolBar.add(tbViewer);
        jtTopCenterToolBar.add(jbLoginStatus);

        tbViewer.setSelected(true);

        jbOpenDb.setToolTipText(propertyUtil.getLangText("ToolBarButton.OpenDataBase.ToolTipText"));
        jbOpenListDb.setToolTipText(propertyUtil.getLangText("ToolBarButton.OpenDataBaseList.ToolTipText"));
        jbLoginStatus.setToolTipText(propertyUtil.getLangText("Frame.Main.LoginStatus"));

        // Top right toolbar

        pnMainTop.add(jtTopRightToolBar = new JToolBar());
        jtTopRightToolBar.setBackground(Color.WHITE);
        jtTopRightToolBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        jtTopRightToolBar.add(new JLabel(" "));
        jtTopRightToolBar.add(new JLabel(new ImageIcon(swc.iconPath + "Money.jpg")));
        jtTopRightToolBar.add(tfMainSumPrice = new JTextField("0", 10));
        tfMainSumPrice.setFont(swc.fontExcelHeader);
        tfMainSumPrice.setEditable(false);
        tfMainSumPrice.setMaximumSize(new Dimension(140, 20));
        jtTopRightToolBar.setPreferredSize(new Dimension(175, 41));

        // Popup for colors

        jpColors = new JPopupMenu();
        jpColors.add(jmiColorNone = new JRMenuItem("ToolBarButton.ColorNone.ToolTipText", "ColorNone.jpg"));
        jpColors.add(jmiColorWhite = new JRMenuItem("ToolBarButton.White.ToolTipText", "ColorWhite.jpg"));
        jpColors.add(jmiColorBrown = new JRMenuItem("ToolBarButton.Brown.ToolTipText", "ColorBrown.jpg"));
        jpColors.add(jmiColorOliveBrown =
                     new JRMenuItem("ToolBarButton.OliveBrown.ToolTipText", "ColorOliveBrown.jpg"));
        jpColors.add(jmiColorGreyBrown = new JRMenuItem("ToolBarButton.GreyBrown.ToolTipText", "ColorGreyBrown.jpg"));
        jpColors.add(jmiColorMatGold = new JRMenuItem("ToolBarButton.MatGold.ToolTipText", "ColorMatGold.jpg"));
        jpColors.add(jmiColorGold = new JRMenuItem("ToolBarButton.Gold.ToolTipText", "ColorGold.jpg"));
        jpColors.add(jmiColorTitan = new JRMenuItem("ToolBarButton.Titan.ToolTipText", "ColorTitan.jpg"));
        jpColors.add(jmiColorBrass = new JRMenuItem("ToolBarButton.Brass.ToolTipText", "ColorBrass.jpg"));
        jpColors.add(jmiColorMiddleBrass =
                     new JRMenuItem("ToolBarButton.MiddleBrass.ToolTipText", "ColorMiddleBrass.jpg"));
        jpColors.add(jmiColorNatureSilver =
                     new JRMenuItem("ToolBarButton.NatureSilver.ToolTipText", "ColorNatureSilver.jpg"));
        jpColors.add(jmiColorNewSilver = new JRMenuItem("ToolBarButton.NewSilver.ToolTipText", "ColorNewSilver.jpg"));
        jpColors.add(jmiColorDarkBrass = new JRMenuItem("ToolBarButton.DarkBrass.ToolTipText", "ColorDarkBrass.jpg"));
        jpColors.add(jmiColorJetBlack = new JRMenuItem("ToolBarButton.JetBlack.ToolTipText", "ColorJetBlack.jpg"));
        jpColors.add(jmiColorCreamWhite =
                     new JRMenuItem("ToolBarButton.CreamWhite.ToolTipText", "ColorCreamWhite.jpg"));

        // Create collection panel
        tmCollection = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tmCollection.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Collate.Count"));
        tmCollection.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Collate.SAP"));
        tmCollection.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Collate.Name"));
        tmCollection.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Collate.ListPrice"));
        tmCollection.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Collate.NetPrice"));
        tmCollection.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Collate.NetSum"));

        JPanel pnCollate = new JPanel();
        JPanel pnGrid = new JPanel();
        JPanel pnSum = new JPanel();
        pnCollate.setLayout(new BorderLayout());
        pnGrid.setLayout(new BorderLayout());

        pnGrid.add(jtCollection = new JTable(tmCollection), "Center");
        pnGrid.add(new JScrollPane(jtCollection));
        pnGrid.setBackground(Color.WHITE);

        pnCollate.add(pnGrid, "Center");
        pnCollate.add(pnSum, "South");

        pnSum.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pnSum.add(new JLabel(propertyUtil.getLangText("InternalFrame.CollectionList.Sum") + ": "));
        pnSum.add(tfCollectionSum = new JTextField("", 20));
        pnSum.setBackground(Color.WHITE);
        tfCollectionSum.setEditable(false);

        jtCollection.getColumnModel()
                    .getColumn(0)
                    .setPreferredWidth(30);
        jtCollection.getColumnModel()
                    .getColumn(1)
                    .setPreferredWidth(50);
        jtCollection.getColumnModel()
                    .getColumn(2)
                    .setPreferredWidth(350);
        jtCollection.setBorder(BorderFactory.createEtchedBorder());
        jtCollection.setAutoCreateRowSorter(true);
        jtCollection.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtCollection.getTableHeader().setFont(swc.fontExcelHeader);
        jtCollection.getTableHeader().setBackground(swc.colorExcelHeader);
        //jtCollection.setToolTipText(propertyUtil.getLangText("Frame.Main.Tab.Collate.ToolTipText"));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jtCollection.getColumnModel()
                    .getColumn(3)
                    .setCellRenderer(renderer);
        jtCollection.getColumnModel()
                    .getColumn(4)
                    .setCellRenderer(renderer);
        jtCollection.getColumnModel()
                    .getColumn(5)
                    .setCellRenderer(renderer);

        // Login panel
        pnLoginTab = new JPanel();
        pnLogin = new JPanel();
        JPanel pnLoginLeft = new JPanel();
        JPanel pnLoginButtons = new JPanel();
        //tfProfile = new JTextField(propertyUtil.getStringProperty(propertyUtil.getProfileProperty(), "Profiles.LastLogin"),15);
        tfProfile = new JTextField(15);
        tfUser = new JTextField(15);

        CellConstraints cc = new CellConstraints();
        pnLoginTab.setLayout(new FormLayout("center:default:grow", "default:grow"));

        pnLogin.setBackground(Color.WHITE);
        pnLoginLeft.setBackground(Color.WHITE);
        pnLoginButtons.setBackground(Color.WHITE);
        pnLoginTab.setBackground(Color.WHITE);
        tfProfile.setBorder(BorderFactory.createEtchedBorder());
        tfUser.setBorder(BorderFactory.createLoweredBevelBorder());
        tfUser.requestFocus();
        pnLogin.setBorder(BorderFactory.createEtchedBorder());

        pnLoginLeft.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        pnLoginButtons.add(jbLogin = new JButton(propertyUtil.getLangText("Frame.Button.Login")));
        pnLoginButtons.add(jbLogout = new JButton(propertyUtil.getLangText("Frame.Button.Logout")));
        jbLogin.setPreferredSize(new Dimension(100, 25));
        jbLogout.setPreferredSize(new Dimension(100, 25));

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pnLoginLeft.add(new JLabel(propertyUtil.getLangText("Frame.Label.UserName") + "  ",
                                   new ImageIcon(swc.iconPath + "TreeProfiles.jpg"), JLabel.LEFT), c);
        c.gridx = 1;
        pnLoginLeft.add(tfUser, c);
        c.gridy = 1;
        c.gridx = 0;
        pnLoginLeft.add(new JLabel(" " + propertyUtil.getLangText("Frame.Label.ProfileName") + "  ",
                                   new ImageIcon(swc.iconPath + "TreeProfile.jpg"), JLabel.LEFT), c);
        c.gridx = 1;
        pnLoginLeft.add(tfProfile, c);
        c.gridy = 2;
        c.gridx = 0;
        pnLoginLeft.add(new JLabel(" ", new ImageIcon(swc.iconPath + "Profiles.jpg"), JLabel.RIGHT), c);
        c.gridx = 1;
        pnLoginLeft.add(jcbProfiles = new JComboBox(), c);
        c.gridy = 3;
        c.gridx = 0;
        pnLoginLeft.add(jlLogin = new JLabel(" ", new ImageIcon(swc.iconPath + "Login.jpg"), JLabel.RIGHT), c);
        c.gridx = 1;
        pnLoginLeft.add(pnLoginButtons, c);

        tfUser.setText(propertyUtil.getStringProperty(propertyUtil.getUserProperty(), "Users.LastLogin"));
        jcbProfiles.setPreferredSize(new Dimension(170, 20));
        jcbProfiles.setEnabled(false);
        tfProfile.setEnabled(false);
        jbLogout.setEnabled(false);

        pnLogin.add(pnLoginLeft);
        pnLogin.add(jlPc = new JLabel("", new ImageIcon(swc.iconPath + "LoginPC.jpg"), JLabel.LEFT), "Center");

        pnLoginTab.add(pnLogin, cc.xy(1, 1));

        //Resizing tab
        pnResize = new JPanel();
        pnResize.setLayout(new BorderLayout());

        //Reset canvas (need when restart program)
        canvas.removeAll();
        canvas = new DisplayCanvas();

        //Add canvas
        JScrollPane scroller = new JScrollPane(canvas);
        pnResize.add(scroller, "Center");
        //Add resizing fields to canvas

        //Height
        canvas.add(jntHeight = new JRNumberTextField(1710, 10));
        jntHeight.setLocation(100, 10);
        jntHeight.setFont(swc.fontCanvasResize);
        jntHeight.setMaxLength(4);

        //Width
        canvas.add(jntWidth = new JRNumberTextField(1000, 10));
        jntWidth.setLocation(100, 50);
        jntWidth.setFont(swc.fontCanvasResize);
        jntWidth.setMaxLength(4);

        //Width 2
        canvas.add(jntWidth2 = new JRNumberTextField(1000, 10));
        jntWidth2.setLocation(100, 100);
        jntWidth2.setFont(swc.fontCanvasResize);
        jntWidth2.setMaxLength(4);
        //Height 2
        canvas.add(jntHeight2 = new JRNumberTextField(2100, 10));
        jntHeight2.setLocation(100, 150);
        jntHeight2.setFont(swc.fontCanvasResize);
        jntHeight2.setMaxLength(4);
        JPanel pnCorrection = new JPanel();
        pnCorrection.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnCorrection.add(new JLabel(propertyUtil.getLangText("Frame.Main.Tab.Resize.Garniture") + ":        "));
        pnCorrection.add(jntGarniture = new JRNumberTextField(1, 3));
        pnCorrection.add(new JLabel("               "));
        pnCorrection.add(new JLabel(propertyUtil.getLangText("Frame.Main.Tab.Resize.Correction") + ":        "));
        pnCorrection.add(new JLabel(propertyUtil.getLangText("Frame.Main.Tab.Resize.Correction.Width") + ": "));
        pnCorrection.add(jntCorrectionWidth = new JRNumberTextField(108, 3));
        pnCorrection.add(new JLabel(propertyUtil.getLangText("Frame.Main.Tab.Resize.Correction.Height") + ": "));
        pnCorrection.add(jntCorrectionHeight = new JRNumberTextField(108, 3));
        pnCorrection.setOpaque(false);
        jntCorrectionWidth.setMaxLength(3);
        jntCorrectionHeight.setMaxLength(3);

        canvas.add(pnCorrection);
        pnCorrection.setBounds(125, 0, 700, 25);

        //Add collection button
        canvas.add(jbAddCollection = new JButton(propertyUtil.getLangText("Frame.Canvas.Button.AddCollection")));
        jbAddCollection.setBounds(5, 445, 105, 35);
        jbAddCollection.setMargin(new Insets(0, 0, 0, 0));
        jbAddCollection.setEnabled(false);
        jbAddCollection.setToolTipText(propertyUtil.getLangText("Frame.Canvas.Button.AddCollection.ToolTipText"));

        //Remove collection button
        canvas.add(jbRemoveCollection = new JButton(propertyUtil.getLangText("Frame.Canvas.Button.RemoveCollection")));
        jbRemoveCollection.setBounds(5, 483, 105, 23);
        jbRemoveCollection.setMargin(new Insets(0, 0, 0, 0));
        jbRemoveCollection.setEnabled(false);
        jbRemoveCollection.setToolTipText(propertyUtil.getLangText("Frame.Canvas.Button.RemoveCollection.ToolTipText"));

        //Set labels for canvas
        canvas.setLabels(jntWidth, jntWidth2, jntHeight, jntHeight2, jntCorrectionWidth, jntCorrectionHeight,
                         jntGarniture);

        //Create tabs
        mainTabPane = new JTabbedPane();
        mainTabPane.addTab(propertyUtil.getLangText("Frame.Main.Tab.Login"), new ImageIcon(swc.iconPath + "Login.jpg"),
                           pnLoginTab);
        mainTabPane.addTab(propertyUtil.getLangText("Frame.Main.Tab.Resize"),
                           new ImageIcon(swc.iconPath + "Resize.jpg"), pnResize);
        mainTabPane.addTab(propertyUtil.getLangText("Frame.Main.Tab.Collate"),
                           new ImageIcon(swc.iconPath + "Collate.jpg"), pnCollate);
        mainTabPane.addTab(propertyUtil.getLangText("Frame.Main.Tab.Menus"),
                           new ImageIcon(swc.iconPath + "TabMenu.jpg"), mainDeskTop);

        //Create a split pane for main
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnMainLeft, mainTabPane);
        mainSplitPane.setOneTouchExpandable(true);
        mainSplitPane.setDividerLocation(0);


        //jrfMain.add(mainTabPane,"Center");
        //jrfMain.add(pnMainLeft,"West");

        jrfMain.add(pnMainTop, "North");
        jrfMain.add(mainSplitPane, "Center");

        //Set actions - other
        mainTabPane.addMouseMotionListener(new MouseMotionHandler());
        mainTabPane.addMouseListener(this);
        jntWidth.addActionListener(this);
        jntWidth.addMouseListener(this);
        jntWidth.setActionCommand("selectdatabaserows");
        jntWidth2.addActionListener(this);
        jntWidth2.setActionCommand("selectdatabaserows");
        jntHeight.addActionListener(this);
        jntHeight.addMouseListener(this);
        jntHeight.setActionCommand("selectdatabaserows");
        jntHeight2.addActionListener(this);
        jntHeight2.setActionCommand("selectdatabaserows");
        jntGarniture.addActionListener(this);
        jntGarniture.setActionCommand("selectdatabaserows");
        jntCorrectionHeight.addActionListener(this);
        jntCorrectionHeight.setActionCommand("selectdatabaserows");
        jntCorrectionWidth.addActionListener(this);
        jntCorrectionWidth.setActionCommand("selectdatabaserows");
        jbAddCollection.addActionListener(this);
        jbAddCollection.setActionCommand("addcollection");
        jbRemoveCollection.addActionListener(this);
        jbRemoveCollection.setActionCommand("removecollection");
        jlRotoIcon.addMouseListener(this);


        //Set actions - login
        jbLogin.addActionListener(this);
        jbLogin.setActionCommand("loginuser");
        jbLogout.addActionListener(this);
        jbLogout.setActionCommand("logoutuser");
        jcbProfiles.addActionListener(this);
        jcbProfiles.setActionCommand("profileselected");
        tfProfile.addActionListener(this);
        tfProfile.setActionCommand("loginprofile");
        tfUser.addActionListener(this);
        tfUser.setActionCommand("loginuser");

        //Set actions - tool bars
        tbBar.addActionListener(this);
        tbBar.setActionCommand("barselected");
        tbClosableBar.addActionListener(this);
        tbClosableBar.setActionCommand("barselected");
        tbSelectionBar.addActionListener(this);
        tbSelectionBar.setActionCommand("selectionbarpressed");
        tbNX.setActionCommand("tbnxpressed");
        tbNT.setActionCommand("tbntpressed");
        tbCatch.addActionListener(this);
        tbCatch.setActionCommand("selectdatabaserows");
        tbCatchMagnet.addActionListener(this);
        tbCatchMagnet.setActionCommand("selectdatabaserows");
        tbCatchNone.addActionListener(this);
        tbCatchNone.setActionCommand("selectdatabaserows");
        tbFan.addActionListener(this);
        tbFan.setActionCommand("selectdatabaserows");
        tbHandle.addActionListener(this);
        tbHandle.setActionCommand("selectdatabaserows");
        tbLeft.addActionListener(this);
        tbLeft.setActionCommand("selectdatabaserows");
        tbRight.addActionListener(this);
        tbRight.setActionCommand("selectdatabaserows");
        tbOver.addActionListener(this);
        tbOver.setActionCommand("selectdatabaserows");
        tbLiftingMD.addActionListener(this);
        tbLiftingMD.setActionCommand("selectdatabaserows");
        tbCilinder.addActionListener(this);
        tbCilinder.setActionCommand("selectdatabaserows");
        jbMenus.addActionListener(this);
        jbMenus.setActionCommand("showmenus");
        jbOpenDb.addActionListener(this);
        jbOpenDb.setActionCommand("opendatabase");
        jbOpenListDb.addActionListener(this);
        jbOpenListDb.setActionCommand("opendatabaselist");

        //Set actions - colors
        jmiColorNone.addActionListener(this);
        jmiColorNone.setActionCommand("selectedcolor_ColorNone");
        jmiColorWhite.addActionListener(this);
        jmiColorWhite.setActionCommand("selectedcolor_ColorWhite");
        jmiColorBrown.addActionListener(this);
        jmiColorBrown.setActionCommand("selectedcolor_ColorBrown");
        jmiColorOliveBrown.addActionListener(this);
        jmiColorOliveBrown.setActionCommand("selectedcolor_ColorOliveBrown");
        jmiColorGreyBrown.addActionListener(this);
        jmiColorGreyBrown.setActionCommand("selectedcolor_ColorGreyBrown");
        jmiColorGold.addActionListener(this);
        jmiColorGold.setActionCommand("selectedcolor_ColorGold");
        jmiColorMatGold.addActionListener(this);
        jmiColorMatGold.setActionCommand("selectedcolor_ColorMatGold");
        jmiColorTitan.addActionListener(this);
        jmiColorTitan.setActionCommand("selectedcolor_ColorTitan");
        jmiColorBrass.addActionListener(this);
        jmiColorBrass.setActionCommand("selectedcolor_ColorBrass");
        jmiColorMiddleBrass.addActionListener(this);
        jmiColorMiddleBrass.setActionCommand("selectedcolor_ColorMiddleBrass");
        jmiColorNatureSilver.addActionListener(this);
        jmiColorNatureSilver.setActionCommand("selectedcolor_ColorNatureSilver");
        jmiColorNewSilver.addActionListener(this);
        jmiColorNewSilver.setActionCommand("selectedcolor_ColorNewSilver");
        jmiColorDarkBrass.setActionCommand("selectedcolor_ColorDarkBrass");
        jmiColorDarkBrass.addActionListener(this);
        jmiColorCreamWhite.setActionCommand("selectedcolor_ColorCreamWhite");
        jmiColorCreamWhite.addActionListener(this);
        jmiColorJetBlack.setActionCommand("selectedcolor_ColorJetBlack");
        jmiColorJetBlack.addActionListener(this);

        //Set actions - types
        tbMDK.addActionListener(this);
        tbMDK.setActionCommand("selectedtype");
        tbMDF.addActionListener(this);
        tbMDF.setActionCommand("selectedtype");
        tbMDS.addActionListener(this);
        tbMDS.setActionCommand("selectedtype");
        tbMKIPP.addActionListener(this);
        tbMKIPP.setActionCommand("selectedtype");
        tbMDF2.addActionListener(this);
        tbMDF2.setActionCommand("selectedtype");
        tbMDK2.addActionListener(this);
        tbMDK2.setActionCommand("selectedtype");
        tbMDS2.addActionListener(this);
        tbMDS2.setActionCommand("selectedtype");
        tbMFREI.addActionListener(this);
        tbMFREI.setActionCommand("selectedtype");
        tbMDSMDK2.addActionListener(this);
        tbMDSMDK2.setActionCommand("selectedtype");
        tbMDSMDF2.addActionListener(this);
        tbMDSMDF2.setActionCommand("selectedtype");

        //Set actions - menus
        jmiMenus.addActionListener(this);
        jmiMenus.setActionCommand("showmenus");
        jmiOpen.addActionListener(this);
        jmiOpen.setActionCommand("opendatabase");
        jmiDataBaseList.addActionListener(this);
        jmiDataBaseList.setActionCommand("opendatabaselist");
        jmiPricelist.addActionListener(this);
        jmiPricelist.setActionCommand("pricelistview");
        jmiGroups.addActionListener(this);
        jmiGroups.setActionCommand("groupsview");
        jmiEditDataBaseList.addActionListener(this);
        jmiEditDataBaseList.setActionCommand("editdatabaselist");
        jmiRabate.addActionListener(this);
        jmiRabate.setActionCommand("showrebate");
        jmiAdminLogin.addActionListener(this);
        jmiAdminLogin.setActionCommand("adminshowlogin");
        jmiAdminLogout.addActionListener(this);
        jmiAdminLogout.setActionCommand("adminlogout");
        jmiTypes.addActionListener(this);
        jmiTypes.setActionCommand("showwindowtypes");
        jmiPrintPreviewDetailed.addActionListener(this);
        jmiPrintPreviewDetailed.setActionCommand("showlistdetailed");
        jmiPrintPreviewGarniture.addActionListener(this);
        jmiPrintPreviewGarniture.setActionCommand("showlistgarniture");
        jmiPrintPreviewSummarized.addActionListener(this);
        jmiPrintPreviewSummarized.setActionCommand("showlistsummarized");
        jmiItems.addActionListener(this);
        jmiItems.setActionCommand("showlistitems");
        jmiListDelete.addActionListener(this);
        jmiListDelete.setActionCommand("deletelist");
        jmiListDetailedSave.addActionListener(this);
        jmiListDetailedSave.setActionCommand("savelistdetailed");
        jmiListSummarizedSave.addActionListener(this);
        jmiListSummarizedSave.setActionCommand("savelistsummarized");
        jmiListNavisonSave.addActionListener(this);
        jmiListNavisonSave.setActionCommand("savelistnavison");
        jmiListProjectSave.addActionListener(this);
        jmiListProjectSave.setActionCommand("savelistproject");
        jmiListOpen.addActionListener(this);
        jmiListOpen.setActionCommand("opendetailedlist");
        jmiListDetailedConcat.addActionListener(this);
        jmiListDetailedConcat.setActionCommand("concatdetailedlist");
        jmiListDetailedSaveExcel.addActionListener(this);
        jmiListDetailedSaveExcel.setActionCommand("showlistexceldetailed");
        jmiListSummarizedSaveExcel.addActionListener(this);
        jmiListSummarizedSaveExcel.setActionCommand("showlistexcelsummarized");
        jmiListGarnitureSaveExcel.addActionListener(this);
        jmiListGarnitureSaveExcel.setActionCommand("showlistexcelgarniture");
        jmiPrintDetailed.addActionListener(this);
        jmiPrintDetailed.setActionCommand("showprintdetailed");
        jmiPrintGarniture.addActionListener(this);
        jmiPrintGarniture.setActionCommand("showprintgarniture");
        jmiPrintProject.addActionListener(this);
        jmiPrintProject.setActionCommand("showprintsummarized");
        jmiOpenEdit.addActionListener(this);
        jmiOpenEdit.setActionCommand("opendatabaseedit");
        jmiOpenNewDb.addActionListener(this);
        jmiOpenNewDb.setActionCommand("opennewdatabaseedit");
        jmiCloseDb.addActionListener(this);
        jmiCloseDb.setActionCommand("closedatabaseedit");
        jmiSave.addActionListener(this);
        jmiSave.setActionCommand("savedatabaseedit");
        jmiSaveAs.addActionListener(this);
        jmiSaveAs.setActionCommand("saveasdatabaseedit");
        jmiSaveExcel.addActionListener(this);
        jmiSaveExcel.setActionCommand("savedatabaseexcel");
        jmiExit.addActionListener(this);
        jmiExit.setActionCommand("programexit");
        jmiReplaceData.addActionListener(this);
        jmiReplaceData.setActionCommand("showreplacedata");
        jmiClose.addActionListener(this);
        jmiClose.setActionCommand("databaseclose");
        jmiCurrency.addActionListener(this);
        jmiCurrency.setActionCommand("showcurrencies");
        jmiStockList.addActionListener(this);
        jmiStockList.setActionCommand("showstocklist");
        jmiLang.addActionListener(this);
        jmiLang.setActionCommand("showeditlanguages");
        jmiLanguage.addActionListener(this);
        jmiLanguage.setActionCommand("showlanguages");
        jmiRegisterProgram.addActionListener(this);
        jmiRegisterProgram.setActionCommand("showregisterprogram");
        jmiRegisterData.addActionListener(this);
        jmiRegisterData.setActionCommand("showregisterdata");
        jmiCorrection.addActionListener(this);
        jmiCorrection.setActionCommand("showcorrection");
        jmiProfiles.addActionListener(this);
        jmiProfiles.setActionCommand("showprofiles");
        jmiFontSize.addActionListener(this);
        jmiFontSize.setActionCommand("showfontsize");
        jmiAdminLang.addActionListener(this);
        jmiAdminLang.setActionCommand("showeditlanguages");
        jmiAdminProfiles.addActionListener(this);
        jmiAdminProfiles.setActionCommand("showprofiles");
        jmiAdminReplaceData.addActionListener(this);
        jmiAdminReplaceData.setActionCommand("showreplacedata");
        jmiAdminTypes.addActionListener(this);
        jmiAdminTypes.setActionCommand("showwindowtypes");
        jmiAbout.addActionListener(this);
        jmiAbout.setActionCommand("showabout");
        jmiBarKo.addActionListener(this);
        jmiBarKo.setActionCommand("popupselectionbarpressed_k");
        jmiBarKsr.addActionListener(this);
        jmiBarKsr.setActionCommand("popupselectionbarpressed_ksr");
        jmiBarM.addActionListener(this);
        jmiBarM.setActionCommand("popupselectionbarpressed_m");
        jmiNTn3.addActionListener(this);
        jmiNTn3.setActionCommand("popupntpressed_3/100");
        jmiNTn6.addActionListener(this);
        jmiNTn6.setActionCommand("popupntpressed_6/100");
        jmiNXx3.addActionListener(this);
        jmiNXx3.setActionCommand("popupnxpressed_3/130");
        jmiNXx6.addActionListener(this);
        jmiNXx6.setActionCommand("popupnxpressed_6/130");
        jmiNXxe.addActionListener(this);
        jmiNXxe.setActionCommand("popupnxpressed_6/150");

        jrfMain.setVisible(true);

    }

    public void createInternalFrame_PriceListSelect() {
        jriPriceListSelect = new JRInternalFrame("PriceListSelect", "InternalFrame.PriceListSelect.Title", mainDeskTop);
        JPanel pnPriceListSelect = new JPanel();
        JPanel pnPriceListFilter = new JPanel();
        pnPriceListSelect.setLayout(new BorderLayout());
        tmPriceListSelect = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                if (col < 2)
                    return false;
                return true;
            }
        };

        //Filter panel
        pnPriceListFilter.add(jbPriceListFilterAdd = new JButton(new ImageIcon(swc.iconPath + "Plus.jpg")));
        pnPriceListFilter.add(jbPriceListFilter = new JButton(new ImageIcon(swc.iconPath + "Filter.jpg")));
        pnPriceListFilter.add(jntFilterSAP = new JRNumberTextField());
        pnPriceListFilter.add(tfFilterText = new JTextField(35));
        jntFilterSAP.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListSelect.Filter.SAP.ToolTipText"));
        jntFilterSAP.setColumns(4);
        tfFilterText.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListSelect.Filter.Text.ToolTipText"));
        jbPriceListFilterAdd.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListSelect.AddItem.ToolTipText"));
        jbPriceListFilter.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListSelect.Filter.ToolTipText"));

        Dimension dimension = new Dimension(22, 22);
        jbPriceListFilter.setPreferredSize(dimension);
        jbPriceListFilterAdd.setPreferredSize(dimension);

        //Price list panel
        tmPriceListSelect.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.SAP"));
        tmPriceListSelect.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Text"));

        pnPriceListSelect.add(jtPriceListSelect = new JTable(tmPriceListSelect), "Center");
        pnPriceListSelect.add(new JScrollPane(jtPriceListSelect));
        pnPriceListSelect.add(pnPriceListFilter, "North");

        jtPriceListSelect.getTableHeader().setFont(swc.fontExcelHeader);
        jtPriceListSelect.getTableHeader().setBackground(swc.colorExcelHeader);
        jtPriceListSelect.setAutoCreateRowSorter(true);
        jtPriceListSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtPriceListSelect.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListSelect.ToolTipText"));
        jtPriceListSelect.setCursor(new Cursor(Cursor.HAND_CURSOR));

        jtPriceListSelect.getColumnModel()
                         .getColumn(0)
                         .setPreferredWidth(50);
        jtPriceListSelect.getColumnModel()
                         .getColumn(1)
                         .setPreferredWidth(380);

        jriPriceListSelect.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriPriceListSelect.add(pnPriceListSelect, new CellConstraints().xy(1, 1));

        //Add actions
        jtPriceListSelect.addMouseListener(this);
        jbPriceListFilter.addActionListener(this);
        jbPriceListFilter.setActionCommand("pricelistfilter");
        jbPriceListFilterAdd.addActionListener(this); //Command is depend on calling method
        jntFilterSAP.addKeyListener(this);
        tfFilterText.addKeyListener(this);

    }

    public void createInternalFrame_PriceListView() {
        jriPriceListView = new JRInternalFrame("PriceListView", "InternalFrame.PriceListView.Title", mainDeskTop);
        JPanel pnPriceListView = new JPanel();
        pnPriceListView.setLayout(new BorderLayout());

        // Create price list panel
        tmPriceListView = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tmPriceListView.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.SAP"));
        tmPriceListView.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Martikel"));
        tmPriceListView.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Text"));
        tmPriceListView.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Price"));
        tmPriceListView.addColumn(propertyUtil.getLangText("Frame.Main.Tab.Menus.PriceList.Group"));

        // Create open panel
        JPanel pnOpen = new JPanel();
        pnOpen.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnOpen.add(new JLabel(propertyUtil.getLangText("InternalFrame.PriceListView.Label.Multiplier")));
        pnOpen.add(jntMultiplier = new JRNumberTextField(1, 5, true));
        jntMultiplier.addBadChars("-");
        jntMultiplier.setMaxLength(5);
        pnOpen.add(new JLabel(propertyUtil.getLangText("InternalFrame.PriceListView.Label.Currency")));
        pnOpen.add(tfCurrency = new JTextField("", 5));
        pnOpen.add(new JLabel(propertyUtil.getLangText("InternalFrame.PriceListView.Label.Pricelist")));
        pnOpen.add(tfPriceListName = new JTextField("", 20));
        pnOpen.add(jbPriceListOpen = new JButton(new ImageIcon(swc.iconPath + "Open.jpg")));
        pnOpen.add(jbPriceListCurrency = new JButton(new ImageIcon(swc.iconPath + "Money.jpg")));
        pnOpen.add(jbPriceListSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));
        tfPriceListName.setEditable(false);
        tfCurrency.setEditable(false);

        // Create selection panel for printing SAP or Martikel
        ButtonGroup bgPrint = new ButtonGroup();
        JPanel pnSelection = new JPanel();
        pnSelection.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnSelection.add(new JLabel(new ImageIcon(swc.iconPath + "Print.jpg")));
        pnSelection.add(jrPrintSap);
        pnSelection.add(jrPrintMartikel);
        bgPrint.add(jrPrintMartikel);
        bgPrint.add(jrPrintSap);

        jbPriceListOpen.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListView.Button.Open"));
        jbPriceListSave.setToolTipText(propertyUtil.getLangText("InternalFrame.PriceListView.Button.Save"));
        jbPriceListCurrency.setToolTipText(propertyUtil.getLangText("InternalFrame.Currency.Title"));
        jbPriceListOpen.setPreferredSize(new Dimension(28, 28));
        jbPriceListSave.setPreferredSize(new Dimension(28, 28));
        jbPriceListCurrency.setPreferredSize(new Dimension(28, 28));

        // Create grid panel
        JPanel pnGrid = new JPanel();
        pnGrid.setLayout(new BorderLayout());
        pnGrid.add(jtPriceListView = new JTable(tmPriceListView), "Center");
        pnGrid.add(new JScrollPane(jtPriceListView));

        pnPriceListView.add(pnOpen, "North");
        pnPriceListView.add(pnGrid, "Center");
        pnPriceListView.add(pnSelection, "South");

        jriPriceListView.setLayout(new BorderLayout());
        jriPriceListView.add(pnPriceListView, "Center");

        jtPriceListView.getTableHeader().setFont(swc.fontExcelHeader);
        jtPriceListView.getTableHeader().setBackground(swc.colorExcelHeader);
        jtPriceListView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jtPriceListView.setAutoCreateRowSorter(true);

        //Set actions
        jbPriceListOpen.addActionListener(this);
        jbPriceListOpen.setActionCommand("openpricelist");
        jbPriceListSave.addActionListener(this);
        jbPriceListSave.setActionCommand("savepricelist");
        jbPriceListCurrency.addActionListener(this);
        jbPriceListCurrency.setActionCommand("showcurrencies");

    }

    public void createInternalFrame_MaterialGroups() {
        jriMaterialGroups = new JRInternalFrame("Rebate", "InternalFrame.MaterialGroups.Title", mainDeskTop);

        dlGroups = new DefaultListModel();
        dlGroupNames = new DefaultListModel();

        JPanel pnGroupLists = new JPanel();
        pnGroupLists.setLayout(new BorderLayout());

        JPanel pnGroupsButtons = new JPanel();
        JPanel pnGroupName = new JPanel();
        pnGroupName.setLayout(new BorderLayout());
        JPanel pnGroupNameButtons = new JPanel();
        pnGroupNameButtons.setLayout(new GridLayout(4, 1));
        JPanel pnGroup = new JPanel();
        pnGroup.setLayout(new BorderLayout());
        JPanel pnGroupButtons = new JPanel();
        pnGroupButtons.setLayout(new GridLayout(4, 1));

        //Left panel group names
        pnGroupNameButtons.add(tfGroupName = new JTextField(""));
        tfGroupName.setFont(new Font("Courier New", Font.BOLD, 14));
        tfGroupName.setBorder(BorderFactory.createLoweredBevelBorder());
        pnGroupNameButtons.add(jbGroupNameAdd =
                               new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.GroupName.Add")));
        pnGroupNameButtons.add(jbGroupNameModify =
                               new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.GroupName.Modify")));
        pnGroupNameButtons.add(jbGroupNameDelete =
                               new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.GroupName.Delete")));

        pnGroupName.add(jlGroupNames = new JList(dlGroupNames), "Center");
        pnGroupName.add(new JScrollPane(jlGroupNames));
        pnGroupName.add(pnGroupNameButtons, "South");
        pnGroupName.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.MaterialGroups.Border.GroupName")));

        jlGroupNames.setFont(new Font("Courier New", Font.BOLD, 14));
        jlGroupNames.setFixedCellWidth(210);

        //Right panel groups
        pnGroupButtons.add(tfGroup = new JTextField(""));
        tfGroup.setFont(new Font("Courier New", Font.BOLD, 14));
        tfGroup.setBorder(BorderFactory.createLoweredBevelBorder());
        pnGroupButtons.add(jbGroupAdd =
                           new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.Group.Add")));
        pnGroupButtons.add(jbGroupModify =
                           new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.Group.Modify")));
        pnGroupButtons.add(jbGroupDelete =
                           new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.Group.Delete")));

        pnGroup.add(jlGroups = new JList(dlGroups), "Center");
        pnGroup.add(new JScrollPane(jlGroups));
        pnGroup.add(pnGroupButtons, "South");
        pnGroup.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.MaterialGroups.Border.Group")));
        jlGroups.setFont(new Font("Courier New", Font.BOLD, 14));
        jlGroups.setFixedCellWidth(210);
        //Bottom panel
        pnGroupsButtons.add(jbGroupReload =
                            new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.Group.Reload")));
        pnGroupsButtons.add(jbGroupSave =
                            new JButton(propertyUtil.getLangText("InternalFrame.MaterialGroups.Button.Group.Save")));

        //jbGroupSave.setEnabled(false);

        //Add left, right and bottom panels
        pnGroupLists.add(pnGroupName, "West");
        pnGroupLists.add(pnGroup, "East");
        pnGroupLists.add(pnGroupsButtons, "South");
        pnGroupLists.setBorder(BorderFactory.createEtchedBorder());

        jriMaterialGroups.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriMaterialGroups.add(pnGroupLists, new CellConstraints().xy(1, 1));

        //Add actions
        jlGroups.addListSelectionListener(this);
        jlGroupNames.addListSelectionListener(this);
        jbGroupAdd.addActionListener(this);
        jbGroupAdd.setActionCommand("addmaterialgroup");
        jbGroupNameAdd.addActionListener(this);
        jbGroupNameAdd.setActionCommand("addmaterialgroupname");
        jbGroupModify.addActionListener(this);
        jbGroupModify.setActionCommand("modifymaterialgroup");
        jbGroupDelete.addActionListener(this);
        jbGroupDelete.setActionCommand("deletematerialgroup");
        jbGroupNameModify.addActionListener(this);
        jbGroupNameModify.setActionCommand("modifymaterialgroupname");
        jbGroupNameDelete.addActionListener(this);
        jbGroupNameDelete.setActionCommand("deletematerialgroupname");
        jbGroupReload.addActionListener(this);
        jbGroupReload.setActionCommand("reloadmaterialgroup");
        jbGroupSave.addActionListener(this);
        jbGroupSave.setActionCommand("groupssave");
    }


    public void createInternalFrame_Menus() {
        jriMenus = new JRInternalFrame("Menus", "InternalFrame.Menus.Title", mainDeskTop);
        JPanel pnMenus = new JPanel();
        JPanel pnDirectionTop = new JPanel();
        JPanel pnDirectionBottom = new JPanel();
        JPanel pnHandle = new JPanel();
        JPanel pnLiftingMD = new JPanel();
        JPanel pnCatchTop = new JPanel();
        JPanel pnCatchBottom = new JPanel();
        JPanel pnFan = new JPanel();
        JPanel pnBar = new JPanel();
        JPanel pnSelectionBar = new JPanel();
        JPanel pnLock = new JPanel();
        JPanel pnColor = new JPanel();
        JPanel pnSave = new JPanel();

        pnDirectionTop.setBackground(Color.WHITE);
        pnDirectionTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnDirectionTop.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Direction"),
                                      new ImageIcon(swc.iconPath + "Types.jpg"), JLabel.LEFT));

        pnDirectionBottom.setBackground(Color.WHITE);
        pnDirectionBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnDirectionBottom.add(new JLabel(new ImageIcon(swc.iconPath + "Left.jpg"), JLabel.CENTER));
        pnDirectionBottom.add(jrLeft = new JRadioButton(propertyUtil.getLangText("InternalFrame.Menus.Left")));
        jrLeft.setBackground(Color.WHITE);
        pnDirectionBottom.add(new JLabel(new ImageIcon(swc.iconPath + "Right.jpg"), JLabel.CENTER));
        pnDirectionBottom.add(jrRight = new JRadioButton(propertyUtil.getLangText("InternalFrame.Menus.Right")));
        jrRight.setBackground(Color.WHITE);
        pnDirectionBottom.add(new JLabel(new ImageIcon(swc.iconPath + "Over.jpg"), JLabel.CENTER));
        pnDirectionBottom.add(jrOver = new JRadioButton(propertyUtil.getLangText("InternalFrame.Menus.Over"), true));
        jrOver.setBackground(Color.WHITE);

        ButtonGroup bgDirection = new ButtonGroup();
        bgDirection.add(jrLeft);
        bgDirection.add(jrRight);
        bgDirection.add(jrOver);

        pnHandle.setBackground(Color.WHITE);
        pnHandle.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnHandle.add(jcHandle = new JCheckBox());
        jcHandle.setBackground(Color.WHITE);
        pnHandle.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Handle"),
                                new ImageIcon(swc.iconPath + "Handle.jpg"), JLabel.LEFT));
        pnLiftingMD.setBackground(Color.WHITE);
        pnLiftingMD.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnLiftingMD.add(jcLiftingMD = new JCheckBox());
        jcLiftingMD.setBackground(Color.WHITE);
        pnLiftingMD.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.LiftingMD"),
                                   new ImageIcon(swc.iconPath + "LiftingMD.jpg"), JLabel.LEFT));

        pnCatchTop.setBackground(Color.WHITE);
        pnCatchTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnCatchTop.add(jcCatch = new JCheckBox());
        jcCatch.setBackground(Color.WHITE);
        pnCatchTop.add(new JLabel(new ImageIcon(swc.iconPath + "Catch.jpg"), JLabel.LEFT));
        pnCatchTop.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Catch.Normal"),
                                  new ImageIcon(swc.iconPath + "CatchMagnet.jpg"), JLabel.LEFT));

        pnCatchBottom.setBackground(Color.WHITE);
        pnCatchBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnCatchBottom.add(new JLabel(new ImageIcon(swc.iconPath + "CatchMagnet.jpg"), JLabel.LEFT));
        pnCatchBottom.add(jrMagnetCatch =
                          new JRadioButton(propertyUtil.getLangText("InternalFrame.Menus.Catch.Magnet")));

        jrMagnetCatch.setBackground(Color.WHITE);
        pnCatchBottom.add(new JLabel(new ImageIcon(swc.iconPath + "Catch.jpg"), JLabel.LEFT));
        pnCatchBottom.add(jrNormalCatch =
                          new JRadioButton(propertyUtil.getLangText("InternalFrame.Menus.Catch.Normal"), true));
        jrNormalCatch.setBackground(Color.WHITE);

        ButtonGroup bgCatch = new ButtonGroup();
        bgCatch.add(jrNormalCatch);
        bgCatch.add(jrMagnetCatch);
        pnFan.setBackground(Color.WHITE);
        pnFan.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnFan.add(jcFan = new JCheckBox());
        jcFan.setBackground(Color.WHITE);
        pnFan.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Fan"),
                             new ImageIcon(swc.iconPath + "Fan.jpg"), JLabel.LEFT));

        pnBar.setBackground(Color.WHITE);
        pnBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnBar.add(jrBar = new JRadioButton());
        jrBar.setBackground(Color.WHITE);
        pnBar.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Bar"),
                             new ImageIcon(swc.iconPath + "Bar.jpg"), JLabel.LEFT));
        pnBar.add(jrClosableBar = new JRadioButton());
        jrClosableBar.setBackground(Color.WHITE);
        pnBar.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.ClosableBar"),
                             new ImageIcon(swc.iconPath + "ClosableBar.jpg"), JLabel.LEFT));

        groupMenuBar = new ButtonGroup();
        groupMenuBar.add(jrBar);
        groupMenuBar.add(jrClosableBar);
        jrBar.setSelected(true);

        jcbBar = new JComboBox<String>();
        for (int i = 0; i < swc.closableBarArray.length; i = i + 1) {
            jcbBar.addItem(swc.closableBarArray[i]);
        }

        pnBar.add(jcbBar);

        pnSelectionBar.setBackground(Color.WHITE);
        pnSelectionBar.setLayout(new FlowLayout(FlowLayout.LEFT));

        pnSelectionBar.add(new JLabel(new ImageIcon(swc.iconPath + "Bar.jpg"), JLabel.LEFT));
        pnSelectionBar.add(new JLabel(new ImageIcon(swc.iconPath + "ClosableBar.jpg"), JLabel.LEFT));
        pnSelectionBar.add(new JLabel(propertyUtil.getLangText("MenuItem.Bar.Type")));

        jcbSelectionBar = new JComboBox<String>();
        jcbSelectionBar.addItem(propertyUtil.getLangText("MenuItem.Bar.Ko"));
        jcbSelectionBar.addItem(propertyUtil.getLangText("MenuItem.Bar.Ksr"));
        jcbSelectionBar.addItem(propertyUtil.getLangText("MenuItem.Bar.M"));
        pnSelectionBar.add(jcbSelectionBar);

        pnLock.setBackground(Color.WHITE);
        pnLock.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnLock.add(jcCilinder = new JCheckBox());
        jcCilinder.setBackground(Color.WHITE);
        pnLock.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Lock"),
                              new ImageIcon(swc.iconPath + "Lock.jpg"), JLabel.LEFT));

        pnColor.setBackground(Color.WHITE);
        pnColor.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnColor.add(jcColor = new JCheckBox());
        jcColor.setBackground(Color.WHITE);
        pnColor.add(new JLabel(propertyUtil.getLangText("InternalFrame.Menus.Colors"),
                               new ImageIcon(swc.iconPath + "Colors.jpg"), JLabel.LEFT));
        jcbColors = new JComboBox();
        pnColor.add(jcbColors);

        Iterator it = propertyUtil.getColorMap()
                                  .entrySet()
                                  .iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String localisedName = propertyUtil.getLangText(pairs.getKey().toString());
            jcbColors.addItem(localisedName);
            //System.out.println("Localisedname: " + localisedName);
            //Add color keys and name to localised colors, to save it later with the keys from the menu settings.
            swc.getLocalisedColors().put(localisedName, pairs.getKey().toString());
            //System.out.println(pairs.getKey() + " = " + pairs.getValue());
        }

        pnSave.setBackground(Color.WHITE);
        pnSave.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pnSave.add(new JLabel(new ImageIcon(swc.iconPath + "Save.jpg")));
        pnSave.add(jbMenuSave = new JButton(propertyUtil.getLangText("Frame.Button.Menus.Save")));
        jbMenuSave.addActionListener(this);
        jbMenuSave.setActionCommand("savemenu");

        pnMenus.setBorder(BorderFactory.createEtchedBorder());
        pnMenus.setLayout(new GridLayout(12, 1));
        pnMenus.add(pnDirectionTop);
        pnMenus.add(pnDirectionBottom);
        pnMenus.add(pnHandle);
        pnMenus.add(pnLiftingMD);
        pnMenus.add(pnCatchTop);
        pnMenus.add(pnCatchBottom);
        pnMenus.add(pnFan);
        pnMenus.add(pnBar);
        pnMenus.add(pnSelectionBar);
        pnMenus.add(pnLock);
        pnMenus.add(pnColor);
        pnMenus.add(pnSave);

        jriMenus.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriMenus.add(pnMenus, new CellConstraints().xy(1, 1));
    }

    public void createInternalFrame_Rebate() {
        jriRebate = new JRInternalFrame("Rebate", "InternalFrame.Rebate.Title", mainDeskTop);
        JPanel pnRebate = new JPanel();
        pnRebate.setLayout(new GridLayout(3, 1));
        JPanel pnInfo = new JPanel();
        JPanel pnInfoButtons = new JPanel();
        JPanel pnCustomPriceListGrid = new JPanel();
        JPanel pnCustomPriceListButtons = new JPanel();
        JPanel pnRebateGrid = new JPanel();
        JPanel pnRebateGroupGrid = new JPanel();
        JPanel pnContents = new JPanel();
        pnRebateAdmin = new JPanel();

        //Custom price list panel
        pnCustomPriceListGrid.setLayout(new BorderLayout());
        tmCustomPriceList = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                if (col == 0 || col == 1)
                    return false;
                return true;
            }
        };

        tmCustomPriceList.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.CustomPriceList.Column.SAP"));
        tmCustomPriceList.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.CustomPriceList.Column.Text"));
        tmCustomPriceList.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.CustomPriceList.Column.Net"));

        pnCustomPriceListGrid.add(jtCustomPriceList = new JTable(tmCustomPriceList), "Center");
        pnCustomPriceListGrid.add(new JScrollPane(jtCustomPriceList));

        jtCustomPriceList.getTableHeader().setFont(swc.fontExcelHeader);
        jtCustomPriceList.getTableHeader().setBackground(swc.colorExcelHeader);

        jtCustomPriceList.getColumnModel()
                         .getColumn(0)
                         .setPreferredWidth(100);
        jtCustomPriceList.getColumnModel()
                         .getColumn(1)
                         .setPreferredWidth(350);
        jtCustomPriceList.getColumnModel()
                         .getColumn(2)
                         .setPreferredWidth(80);
        jtCustomPriceList.setAutoCreateRowSorter(true);

        //Custom price list panel - left buttons		
        pnCustomPriceListButtons.setLayout(new BorderLayout());
        pnCustomPriceListButtons.add(jbCustomPriceListAdd = new JButton(new ImageIcon(swc.iconPath + "\\Plus.jpg")),
                                     "North");
        pnCustomPriceListButtons.add(jbCustomPriceListRemove = new JButton(new ImageIcon(swc.iconPath + "\\Minus.jpg")),
                                     "South");
        Dimension dimension = new Dimension(18, 18);
        jbCustomPriceListRemove.setPreferredSize(dimension);
        jbCustomPriceListAdd.setPreferredSize(dimension);

        pnCustomPriceListGrid.add(pnCustomPriceListButtons, "West");
        pnCustomPriceListGrid.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Rebate.CustomPriceList.Title")));

        //Rebate panel
        pnRebateGrid.setLayout(new BorderLayout());
        tmRebate = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                if (col == 0)
                    return false;
                return true;
            }
        };

        tmRebate.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.RebateList.Column.MaterialGroupName"));
        tmRebate.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.RebateList.Column.RebateValue"));

        pnRebateGrid.add(jtRebate = new JTable(tmRebate), "Center");
        pnRebateGrid.add(new JScrollPane(jtRebate));

        jtRebate.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(60);
        jtRebate.getColumnModel()
                .getColumn(1)
                .setMaxWidth(40);

        jtRebate.getTableHeader().setFont(swc.fontExcelHeader);
        jtRebate.getTableHeader().setBackground(swc.colorExcelHeader);
        jtRebate.setAutoCreateRowSorter(true);
        jtRebate.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Set number field for rebate panel
        JRNumberTextField rebate = new JRNumberTextField();
        rebate.setBorder(BorderFactory.createLoweredBevelBorder());
        rebate.setDubleInputEnabled();
        rebate.setMaxLength(5);
        rebate.setInterVal(0, 99);
        rebate.addBadChars("-");
        DefaultCellEditor cellEditor = new DefaultCellEditor(rebate);
        jtRebate.getColumnModel()
                .getColumn(1)
                .setCellEditor(cellEditor);
        jtRebate.setFocusable(false);
        jtRebate.getTableHeader().setReorderingAllowed(false);

        //Group panel
        pnRebateGroupGrid.setLayout(new BorderLayout());
        tmRebateGroups = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                if (col < 1)
                    return false;
                return true;
            }
        };
        tmRebateGroups.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.RebateList.Column.MaterialGroups"));
        tmRebateGroups.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.RebateList.Column.MaterialGroupsComment"));
        tmRebateGroups.addColumn(propertyUtil.getLangText("InternalFrame.Rebate.RebateList.Column.RebateValue"));

        pnRebateGroupGrid.add(jtRebateGroups = new JTable(tmRebateGroups), "West");
        pnRebateGroupGrid.add(new JScrollPane(jtRebateGroups));

        jtRebateGroups.getColumnModel()
                      .getColumn(1)
                      .setPreferredWidth(130);
        jtRebateGroups.getColumnModel()
                      .getColumn(2)
                      .setMaxWidth(40);

        jtRebateGroups.getTableHeader().setFont(swc.fontExcelHeader);
        jtRebateGroups.getTableHeader().setBackground(swc.colorExcelHeader);
        jtRebateGroups.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtRebateGroups.setFocusable(false);
        jtRebateGroups.getTableHeader().setReorderingAllowed(false);

        //Set number field for group panel
        JRNumberTextField customRebate = new JRNumberTextField();
        customRebate.setBorder(BorderFactory.createLoweredBevelBorder());
        customRebate.setDubleInputEnabled();
        customRebate.setMaxLength(5);
        customRebate.setInterVal(0, 99);
        customRebate.addBadChars("-");
        DefaultCellEditor groupCellEditor = new DefaultCellEditor(customRebate);
        jtRebateGroups.getColumnModel()
                      .getColumn(2)
                      .setCellEditor(groupCellEditor);

        //Info panel
        pnInfo.setLayout(new BorderLayout());
        pnInfo.add(jtRebateInfo = new JTextArea(3, 40), "Center");
        pnInfo.add(new JScrollPane(jtRebateInfo));
        //Info - button panel
        pnInfoButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnInfoButtons.add(tfRebateProfileLocation = new JTextField(15));
        pnInfoButtons.add(tfRebateFileName = new JTextField(22));
        tfRebateFileName.setEditable(false);
        tfRebateFileName.setFont(swc.fontExcelHeader);
        tfRebateFileName.setBackground(swc.colorExcelHeader);
        tfRebateProfileLocation.setEditable(false);
        tfRebateProfileLocation.setFont(swc.fontExcelHeader);
        tfRebateProfileLocation.setBackground(swc.colorExcelHeader);

        pnInfoButtons.add(jbRebateSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));
        jbRebateSave.setPreferredSize(new Dimension(28, 28));
        jbRebateSave.setToolTipText(propertyUtil.getLangText("InternalFrame.Rebate.Save.ToolTipText"));
        pnInfo.add(pnInfoButtons, "South");
        pnInfo.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Rebate.Info.Title")));


        JSplitPane rebateSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnRebateGrid, pnRebateGroupGrid);
        rebateSplitPane.setOneTouchExpandable(true);
        rebateSplitPane.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Rebate.RebateList.Title")));
        rebateSplitPane.setDividerLocation(200);

        pnRebateGrid.setPreferredSize(new Dimension(300, 100));
        pnRebateGroupGrid.setPreferredSize(new Dimension(100, 100));

        //Add panels to rebate panel
        pnRebate.add(pnCustomPriceListGrid);
        pnRebate.add(rebateSplitPane);
        pnRebate.add(pnInfo);

        //Admin panel

        JPanel pnRebateTree = new JPanel();
        pnRebateTree.setLayout(new BorderLayout());

        pnRebateAdmin.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pnRebateAdmin.add(new JLabel("Login name:"), c);
        c.gridy = 1;
        pnRebateAdmin.add(jcbAdminLogin = new JComboBox(), c);
        c.gridy = 2;
        pnRebateAdmin.add(new JLabel("Profile name:"), c);
        c.gridy = 3;
        pnRebateAdmin.add(jcbAdminProfiles = new JComboBox(), c);
        c.gridy = 4;
        pnRebateAdmin.add(new JLabel("File name:"), c);
        c.gridy = 5;
        pnRebateAdmin.add(tfAdminFileName = new JTextField(12), c);
        c.gridy = 6;
        pnRebateAdmin.add(new JLabel(" "), c);
        c.gridy = 7;
        pnRebateAdmin.add(jbAdminRebateSave = new JButton(propertyUtil.getLangText("InternalFrame.Rebate.Admin.Save")),
                          c);
        c.gridy = 8;
        pnRebateAdmin.add(jbAdminRebateOpen = new JButton(propertyUtil.getLangText("InternalFrame.Rebate.Admin.Open")),
                          c);
        pnRebateAdmin.setBorder(BorderFactory.createTitledBorder("Admin:"));
        tfAdminFileName.setEditable(false);
        tfAdminFileName.setFont(swc.fontExcelHeader);
        tfAdminFileName.setBackground(swc.colorExcelHeader);


        //Create root
        nodeRootRebate = new CheckNode(propertyUtil.getLangText("InternalFrame.Rebate.Node.Profile"));
        nodeRootRebate.setCheckBoxEnabled(false);
        nodeRootRebate.setSelectionMode(CheckNode.SINGLE_SELECTION);
        nodeRootRebate.setSelected(true);
        nodeRootRebate.setImageIconDefault(new ImageIcon(swc.iconPath + ("Profiles.jpg")));
        nodeRootRebate.setImageIconClosed(new ImageIcon(swc.iconPath + ("Profiles.jpg")));
        nodeRootRebate.setImageIconOpened(new ImageIcon(swc.iconPath + ("Profiles.jpg")));


        rebateTree = new JTree(nodeRootRebate);
        rebateTree.setCellRenderer(new CheckRenderer());
        rebateTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        rebateTree.putClientProperty("JTree.lineStyle", "Angled");
        rebateTree.addMouseListener(new NodeSelectionListener(rebateTree));

        //Tree buttons
        JPanel pnTreeButtons = new JPanel();

        pnTreeButtons.add(jbRebateTreeSet = new JButton(new ImageIcon(swc.iconPath + "Check.jpg")));
        pnTreeButtons.add(jbRebateTreeOpen = new JButton(new ImageIcon(swc.iconPath + "Open.jpg")));
        pnTreeButtons.add(jbRebateTreeDelete = new JButton(new ImageIcon(swc.iconPath + "ListDelete.jpg")));

        Dimension treeDimension = new Dimension(28, 28);
        jbRebateTreeSet.setPreferredSize(treeDimension);
        jbRebateTreeOpen.setPreferredSize(treeDimension);
        jbRebateTreeDelete.setPreferredSize(treeDimension);

        pnRebateTree.add(new JScrollPane(rebateTree), "Center");
        pnRebateTree.add(pnTreeButtons, "South");
        pnRebateTree.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Rebate.ProfileView")));
        //pnRebateTree.add(pnRebateAdmin,"North");
        pnRebateTree.setPreferredSize(new Dimension(230, 100));

        //Add panels to main panel
        pnContents.setLayout(new BorderLayout());
        JSplitPane contentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnRebate, pnRebateTree);
        contentSplitPane.setOneTouchExpandable(true);

        pnContents.add(contentSplitPane, "Center");
        pnContents.setBorder(BorderFactory.createEtchedBorder());

        //pnRebateAdmin.setVisible(false);

        jriRebate.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriRebate.add(pnContents, new CellConstraints().xy(1, 1));

        //Add to action
        jcbAdminLogin.addActionListener(this);
        jcbAdminLogin.setActionCommand("adminloginselected");
        jcbAdminProfiles.addActionListener(this);
        jcbAdminProfiles.setActionCommand("adminprofileselected");
        jbCustomPriceListAdd.addActionListener(this);
        jbCustomPriceListAdd.setActionCommand("custompricelistadditem");
        jbCustomPriceListRemove.addActionListener(this);
        jbCustomPriceListRemove.setActionCommand("custompricelistremoveitem");
        jbAdminRebateSave.addActionListener(this);
        jbAdminRebateSave.setActionCommand("adminrebatesave");
        jbAdminRebateOpen.addActionListener(this);
        jbAdminRebateOpen.setActionCommand("adminrebateopen");
        jbRebateSave.addActionListener(this);
        jbRebateSave.setActionCommand("rebatesave");
        jbRebateTreeSet.addActionListener(this);
        jbRebateTreeSet.setActionCommand("rebatetreeset");
        jbRebateTreeOpen.addActionListener(this);
        jbRebateTreeOpen.setActionCommand("rebatetreeopen");
        jbRebateTreeDelete.addActionListener(this);
        jbRebateTreeDelete.setActionCommand("rebatetreedelete");
        rebateTree.addMouseListener(this);
        jtRebate.addMouseListener(this);
        jtRebateGroups.addMouseListener(this);
        customRebate.addActionListener(this);
        customRebate.setActionCommand("rebategroupitemmodified");
    }

    public void createInternalFrame_DatabaseListEditor() {
        jriDbListEditor =
            new JRInternalFrame("DataBaseListEditor", "InternalFrame.DatabaseListEditor.Title", mainDeskTop);
        JPanel pnDbList = new JPanel();
        pnDatabases = new JPanel();
        JPanel pnDatabaseFilter = new JPanel();
        pnDataBaseButtons = new JPanel();
        pnDbList.setLayout(new BorderLayout());
        pnDatabases.setLayout(new BorderLayout());
        pnDatabaseFilter.setLayout(new BorderLayout());

        //Left list
        dlDatabases = new DefaultListModel();
        pnDatabases.add(new JScrollPane(jlDatabases = new JList(dlDatabases)), "Center");
        jlDatabases.setFixedCellWidth(350);
        pnDatabases.add(jbDbListBrowse =
                        new JButton(propertyUtil.getLangText("InternalFrame.DatabaseListEditor.Button.Browse")),
                        "South");

        //Right list
        dlDatabaseFilter = new DefaultListModel();
        pnDatabaseFilter.add(new JScrollPane(jlDatabaseFilter = new JList(dlDatabaseFilter)), "Center");
        jlDatabaseFilter.setFixedCellWidth(350);
        pnDatabaseFilter.add(jbDbListOpen =
                             new JButton(propertyUtil.getLangText("InternalFrame.DatabaseListEditor.Button.Open")),
                             "South");

        //Center buttons
        pnDataBaseButtons.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        Dimension dimension = new Dimension(34, 34);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pnDataBaseButtons.add(jbMoveRight = new JButton(new ImageIcon(swc.iconPath + "MoveRight.jpg")), c);
        jbMoveRight.setPreferredSize(dimension);
        c.gridy = 1;
        pnDataBaseButtons.add(jbMoveLeft = new JButton(new ImageIcon(swc.iconPath + "MoveLeft.jpg")), c);
        jbMoveLeft.setPreferredSize(dimension);
        c.gridy = 2;
        pnDataBaseButtons.add(jbMoveRightAll = new JButton(new ImageIcon(swc.iconPath + "MoveRightAll.jpg")), c);
        jbMoveRightAll.setPreferredSize(dimension);
        c.gridy = 3;
        pnDataBaseButtons.add(jbMoveLeftAll = new JButton(new ImageIcon(swc.iconPath + "MoveLeftAll.jpg")), c);
        jbMoveLeftAll.setPreferredSize(dimension);

        pnDbList.add(pnDatabases, "West");
        pnDbList.add(pnDatabaseFilter, "East");
        pnDbList.add(pnDataBaseButtons, "Center");
        pnDbList.setBorder(BorderFactory.createEtchedBorder());

        jriDbListEditor.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriDbListEditor.add(pnDbList, new CellConstraints().xy(1, 1));

        //jriDbListEditor.setLayout(new BorderLayout());
        //jriDbListEditor.add(pnDbList,"South");

        //Add to action listener
        jbDbListBrowse.addActionListener(this);
        jbDbListBrowse.setActionCommand("databaselistbrowse");
        jbDbListOpen.addActionListener(this);
        jbDbListOpen.setActionCommand("databaselistopen");
        jbMoveLeft.addActionListener(this);
        jbMoveLeft.setActionCommand("moveleft");
        jbMoveLeftAll.addActionListener(this);
        jbMoveLeftAll.setActionCommand("moveleftall");
        jbMoveRight.addActionListener(this);
        jbMoveRight.setActionCommand("moveright");
        jbMoveRightAll.addActionListener(this);
        jbMoveRightAll.setActionCommand("moverightall");

        //Add dragListener
        jlDatabases.setName("databaselist");
        jlDatabases.setTransferHandler(new ListHandler("moveleft"));
        jlDatabaseFilter.setName("databasefilterlist");
        jlDatabaseFilter.setTransferHandler(new ListHandler("moveright"));
        dragSource.createDefaultDragGestureRecognizer(jlDatabases, DnDConstants.ACTION_COPY, this);
        dragSource.createDefaultDragGestureRecognizer(jlDatabaseFilter, DnDConstants.ACTION_COPY, this);
        pnDatabases.setVisible(false);
    }

    public void createInternalFrame_AdminLogin() {
        jriAdminLogin =
            new JRInternalFrame("AdminLogin", propertyUtil.getLangText("InternalFrame.AdminLogin.Title"), mainDeskTop);
        JPanel pnAdminLogin = new JPanel();
        JPanel pnPassWord = new JPanel();
        JPanel pnButtons = new JPanel();

        pnPassWord.add(new JLabel(propertyUtil.getLangText("InternalFrame.AdminLogin.Login") + ": "));
        pnPassWord.add(tfPassWord = new JPasswordField(13));

        pnButtons.add(jbAdminLogin = new JButton("Login"));
        pnButtons.add(jbAdminLogout = new JButton("Logout"));

        pnAdminLogin.setLayout(new GridLayout(2, 1));
        pnAdminLogin.add(pnPassWord);
        pnAdminLogin.add(pnButtons);

        pnAdminLogin.setBorder(BorderFactory.createEtchedBorder());
        jbAdminLogout.setEnabled(false);

        //Add actions
        jbAdminLogin.addActionListener(this);
        jbAdminLogin.setActionCommand("adminlogin");
        tfPassWord.addActionListener(this);
        tfPassWord.setActionCommand("adminlogin");
        jbAdminLogout.addActionListener(this);
        jbAdminLogout.setActionCommand("adminlogout");
        jbAdminLogin.setPreferredSize(new Dimension(100, 25));
        jbAdminLogout.setPreferredSize(new Dimension(100, 25));

        jriAdminLogin.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriAdminLogin.add(pnAdminLogin, new CellConstraints().xy(1, 1));
    }

    public void createInternalFrame_Correction() {
        jriCorrection = new JRInternalFrame("Correction", "InternalFrame.Correction.Title", mainDeskTop);
        JPanel pnCorrection = new JPanel();
        pnCorrection.add(new JLabel(propertyUtil.getLangText("InternalFrame.Correction.Width") + ":  "));
        pnCorrection.add(jntCorrectionSaveWidth = new JRNumberTextField(108, 3, false));
        pnCorrection.add(new JLabel(propertyUtil.getLangText("InternalFrame.Correction.Height") + ":  "));
        pnCorrection.add(jntCorrectionSaveHeight = new JRNumberTextField(108, 3, false));
        pnCorrection.add(jbCorrectionSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));
        jbCorrectionSave.setPreferredSize(new Dimension(28, 28));
        jntCorrectionSaveWidth.addBadChars("-");
        jntCorrectionSaveWidth.setMaxLength(3);
        jntCorrectionSaveHeight.addBadChars("-");
        jntCorrectionSaveHeight.setMaxLength(3);
        pnCorrection.setBorder(BorderFactory.createEtchedBorder());

        jriCorrection.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriCorrection.add(pnCorrection, new CellConstraints().xy(1, 1));

        //Add actions
        jbCorrectionSave.addActionListener(this);
        jbCorrectionSave.setActionCommand("correctionsave");
    }

    public void createInternalFrame_FontSize() {
        jriFontSize = new JRInternalFrame("Fontsize", "InternalFrame.FontSize.Title", mainDeskTop);
        JPanel pnFontSize = new JPanel();
        pnFontSize.add(new JLabel(propertyUtil.getLangText("InternalFrame.FontSize.Size") + ":  "));
        pnFontSize.add(jcbFontSize = new JComboBox());
        pnFontSize.add(jbFontSizeSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));

        for (int i = 8; i < 20; i++) {
            jcbFontSize.addItem(i);
        }

        jcbFontSize.setSelectedItem(14);

        jbFontSizeSave.setPreferredSize(new Dimension(28, 28));
        pnFontSize.setBorder(BorderFactory.createEtchedBorder());

        jriFontSize.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriFontSize.add(pnFontSize, new CellConstraints().xy(1, 1));

        //Add actions
        jbFontSizeSave.addActionListener(this);
        jbFontSizeSave.setActionCommand("fontsizesave");
    }

    public void createInternalFrame_Windows() {
        jriWindows = new JRInternalFrame("Windows", "InternalFrame.Windows.Title", mainDeskTop);
        JPanel pnWindows = new JPanel();
        JPanel pnWindowSystems = new JPanel();
        JPanel pnWindowSystemsCombo = new JPanel();
        JPanel pnWindowSystemsPreview = new JPanel();
        JPanel pnWindowSystemsDirections = new JPanel();
        JPanel pnBottom = new JPanel();
        JPanel pnTypes = new JPanel();
        pnWindows.setBorder(BorderFactory.createEtchedBorder());
        pnWindows.setLayout(new BorderLayout());
        pnBottom.setLayout(new BorderLayout());

        windowTableModel = new WindowTableModel();
        windowLimitTableModel = new WindowLimitTableModel();
        jtWindowLimits = new JTable(windowLimitTableModel);
        windowLimitTableModel.setProperties(jtWindowLimits);
        // Window types panel
        windowTabPane = new JTabbedPane();
        jtWindows = new JTable(windowTableModel);

        windowTabPane.addTab(propertyUtil.getLangText("InternalFrame.Windows.System.Title"),
                             new JScrollPane(jtWindows));
        windowTabPane.addTab(propertyUtil.getLangText("InternalFrame.Windows.Limits"), new JScrollPane(jtWindowLimits));
        pnWindows.add(windowTabPane, "Center");

        jtWindows.getColumnModel()
                 .getColumn(0)
                 .setPreferredWidth(100);
        jtWindows.getColumnModel()
                 .getColumn(5)
                 .setPreferredWidth(230);
        jtWindows.getColumnModel()
                 .getColumn(6)
                 .setPreferredWidth(180);

        jtWindows.getTableHeader().setFont(swc.fontExcelHeader);
        jtWindows.getTableHeader().setBackground(swc.colorExcelHeader);
        jtWindows.setFont(swc.fontCanvas);
        jtWindowLimits.getTableHeader().setFont(swc.fontExcelHeader);
        jtWindowLimits.getTableHeader().setBackground(swc.colorExcelHeader);
        jtWindowLimits.setFont(swc.fontCanvas);
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        jtWindows.getColumnModel()
                 .getColumn(1)
                 .setCellRenderer(renderer);
        jtWindows.getColumnModel()
                 .getColumn(6)
                 .setCellRenderer(renderer);
        jtWindowLimits.getColumnModel()
                      .getColumn(1)
                      .setCellRenderer(renderer);

        //Button panel
        pnTypes.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        pnTypes.add(tfWindowTypes = new JTextField(""), c);
        c.gridx = 1;
        pnTypes.add(jcbWinowTypes = new JComboBox(), c);
        c.gridy = 1;
        c.gridx = 0;
        pnTypes.add(jbWindowsAdd = new JButton(propertyUtil.getLangText("InternalFrame.Windows.Add")), c);
        c.gridy = 2;
        pnTypes.add(jbWindowsModify = new JButton(propertyUtil.getLangText("InternalFrame.Windows.Modify")), c);
        c.gridy = 3;
        pnTypes.add(jbWindowsDelete = new JButton(propertyUtil.getLangText("InternalFrame.Windows.Delete")), c);
        c.gridy = 4;
        pnTypes.add(jbWindowsSave = new JButton(propertyUtil.getLangText("InternalFrame.Windows.Save")), c);
        pnTypes.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Windows.Type.Title")));

        //Window systems panel
        pnWindowSystems.setLayout(new BorderLayout());
        pnWindowSystemsCombo.setLayout(new BorderLayout());
        pnWindowSystemsDirections.setLayout(new BorderLayout());
        pnWindowSystemsPreview.setLayout(new GridBagLayout());

        pnWindowSystemsCombo.add(jcbWinowSystems = new JComboBox(), "North");

        pnWindowSystemsDirections.add(jbWindowsLeft = new JButton(new ImageIcon(swc.iconPath + "MoveLeft.jpg")),
                                      "West");
        pnWindowSystemsDirections.add(jbWindowsRight = new JButton(new ImageIcon(swc.iconPath + "MoveRight.jpg")),
                                      "East");
        jbWindowsRight.setPreferredSize(new Dimension(30, 30));

        pnWindowSystemsCombo.add(pnWindowSystemsDirections, "South");
        jbWindowsLeft.setPreferredSize(new Dimension(30, 30));

        c.gridx = 0;
        c.gridy = 0;

        pnWindowSystemsPreview.add(jlWindowReview = new JLabel(), c);
        jlWindowReview.setName("1");
        jlWindowReview.setPreferredSize(new Dimension(300, 180));

        for (int i = 0; i < swc.windowSystemArray.length; i++) {
            jcbWinowSystems.addItem(swc.windowSystemArray[i]);
        }

        pnWindowSystems.add(pnWindowSystemsCombo, "West");
        pnWindowSystems.add(pnWindowSystemsPreview, "Center");
        pnWindowSystems.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Windows.System.Title")));

        pnBottom.add(pnTypes, "West");
        pnBottom.add(pnWindowSystems, "Center");

        jriWindows.setLayout(new BorderLayout());
        jriWindows.add(pnWindows, "Center");
        jriWindows.add(pnBottom, "South");

        //Add actions
        jtWindows.addMouseListener(this);
        jbWindowsAdd.addActionListener(this);
        jbWindowsAdd.setActionCommand("windowsytemadd");
        jbWindowsModify.addActionListener(this);
        jbWindowsModify.setActionCommand("windowsytemmodify");
        jbWindowsDelete.addActionListener(this);
        jbWindowsDelete.setActionCommand("windowsytemdelete");
        jcbWinowTypes.addActionListener(this);
        jcbWinowTypes.setActionCommand("windowsystemselected");
        jcbWinowSystems.addActionListener(this);
        jcbWinowSystems.setActionCommand("windowsystemclassselected");
        jbWindowsSave.addActionListener(this);
        jbWindowsSave.setActionCommand("windowsystemsave");
        jbWindowsLeft.addActionListener(this);
        jbWindowsLeft.setActionCommand("windowsystemmoveleft");
        jbWindowsRight.addActionListener(this);
        jbWindowsRight.setActionCommand("windowsystemmoveright");

    }

    public void createInternalFrame_ExcelList() {
        jriExcelList = new JRInternalFrame("ExcelList", "InternalFrame.ExcelList.Title", mainDeskTop);

        JPanel pnList = new JPanel();
        JPanel pnHeader = new JPanel();
        JPanel pnHeaderGrid = new JPanel();
        JPanel pnHeaderComment = new JPanel();
        JPanel pnButtons = new JPanel();
        pnHeaderSelector = new JPanel();

        pnList.setLayout(new BorderLayout());
        pnHeader.setLayout(new BorderLayout());
        pnHeaderGrid.setLayout(new BorderLayout());
        pnHeaderComment.setLayout(new BorderLayout());
        pnButtons.setLayout(new FlowLayout());

        pnHeader.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.ExcelList.Header")));
        pnList.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.ExcelList.Content")));

        //Create buttons
        pnButtons.add(jbSaveExcelList =
                      new JButton(propertyUtil.getLangText("InternalFrame.ExcelList.Buttons.Save"),
                                  new ImageIcon(swc.iconPath + "Excel.jpg")));
        jbSaveExcelList.setMargin(new Insets(0, 2, 0, 2));
        pnButtons.add(jbPrintExcelList =
                      new JButton(propertyUtil.getLangText("InternalFrame.ExcelList.Buttons.Print"),
                                  new ImageIcon(swc.iconPath + "Print.jpg")));
        jbPrintExcelList.setMargin(new Insets(0, 2, 0, 2));

        pnHeaderGrid.add(jtExcelHeader = new JTable(tmExcelHeader), "Center");

        //pnHeaderGrid.add(new JScrollPane(jtExcelHeader));
        pnHeaderGrid.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.ExcelList.Data")));

        pnHeaderComment.add(jtExcelComment = new JTextArea(3, 20), "Center");
        pnHeaderComment.add(new JScrollPane(jtExcelComment));
        pnHeaderComment.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.ExcelList.Comment")));

        //Deprecated !
        //jtExcelComment.setDocument(new CustomDocument(jtExcelComment));

        jtExcelComment.setFont(swc.fontExcelComment);
        jtExcelComment.setLineWrap(true);
        jtExcelComment.setWrapStyleWord(true);

        //Create header types
        pnHeaderSelector.add(jrOrder =
                             new JRadioButton(propertyUtil.getLangText("InternalFrame.ExcelList.Header.Subject.Order.Default"),
                                              true));
        pnHeaderSelector.add(jrOffer =
                             new JRadioButton(propertyUtil.getLangText("InternalFrame.ExcelList.Header.Subject.Offer.Default")));
        ButtonGroup headerGroup = new ButtonGroup();
        headerGroup.add(jrOrder);
        headerGroup.add(jrOffer);
        pnHeaderComment.add(pnHeaderSelector, "South");


        //Create a split pane for header
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnHeaderGrid, pnHeaderComment);
        splitPane.setOneTouchExpandable(true);
        //splitPane.setDividerLocation(150);

        pnHeader.add(splitPane, "Center");

        //Create excel list
        tmExcelList = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tmExcelList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.Count"));
        tmExcelList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.SAP"));
        tmExcelList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.Name"));
        tmExcelList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.ListPrice"));
        tmExcelList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.NetPrice"));
        tmExcelList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.NetSum"));

        pnList.add(jtExcelList = new JTable(tmExcelList), "Center");
        pnList.add(new JScrollPane(jtExcelList));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);

        jtExcelList.getColumnModel()
                   .getColumn(0)
                   .setPreferredWidth(30);
        jtExcelList.getColumnModel()
                   .getColumn(1)
                   .setPreferredWidth(50);
        jtExcelList.getColumnModel()
                   .getColumn(2)
                   .setPreferredWidth(350);

        jtExcelList.getColumnModel()
                   .getColumn(3)
                   .setCellRenderer(renderer);
        jtExcelList.getColumnModel()
                   .getColumn(4)
                   .setCellRenderer(renderer);
        jtExcelList.getColumnModel()
                   .getColumn(5)
                   .setCellRenderer(renderer);

        jriExcelList.setLayout(new BorderLayout());

        //Add panels
        jriExcelList.add(pnHeader, "North");
        jriExcelList.add(pnList, "Center");
        jriExcelList.add(pnButtons, "South");

        jtExcelList.getTableHeader().setFont(swc.fontExcelHeader);
        jtExcelList.getTableHeader().setBackground(swc.colorExcelHeader);
        jtExcelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtExcelList.getTableHeader().setReorderingAllowed(false);

        //Add actions
        jbSaveExcelList.addActionListener(this);
        jbSaveExcelList.setActionCommand("saveexcellist");
        jbPrintExcelList.addActionListener(this);
        jbPrintExcelList.setActionCommand("printlist");
        jrOffer.addActionListener(this);
        jrOffer.setActionCommand("offerselected");
        jrOrder.addActionListener(this);
        jrOrder.setActionCommand("orderselected");
    }

    public void createInternalFrame_CollectionList() {
        jriCollectionList = new JRInternalFrame("CollectionList", "InternalFrame.CollectionList.Title", mainDeskTop);
        JPanel pnList = new JPanel();

        pnList.setLayout(new BorderLayout());

        tmCollectionList = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tmCollectionList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.Count"));
        tmCollectionList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.SAP"));
        tmCollectionList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.Name"));
        tmCollectionList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.ListPrice"));
        tmCollectionList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.NetPrice"));
        tmCollectionList.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.NetSum"));

        pnList.add(jtCollectionList = new JTable(tmCollectionList), "Center");
        pnList.add(new JScrollPane(jtCollectionList));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);

        jtCollectionList.getColumnModel()
                        .getColumn(0)
                        .setPreferredWidth(30);
        jtCollectionList.getColumnModel()
                        .getColumn(1)
                        .setPreferredWidth(50);
        jtCollectionList.getColumnModel()
                        .getColumn(2)
                        .setPreferredWidth(350);

        jtCollectionList.getColumnModel()
                        .getColumn(3)
                        .setCellRenderer(renderer);
        jtCollectionList.getColumnModel()
                        .getColumn(4)
                        .setCellRenderer(renderer);
        jtCollectionList.getColumnModel()
                        .getColumn(5)
                        .setCellRenderer(renderer);

        jriCollectionList.setLayout(new BorderLayout());
        jriCollectionList.add(pnList, "Center");

        jtCollectionList.getTableHeader().setFont(swc.fontExcelHeader);
        jtCollectionList.getTableHeader().setBackground(swc.colorExcelHeader);
        jtCollectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    }

    public void createInternalFrame_CollectionEdit() {
        jriCollectionEdit = new JRInternalFrame("CollectionEdit", "InternalFrame.CollectionEdit.Title", mainDeskTop);
        JPanel pnList = new JPanel();
        JPanel pnCollectionList = new JPanel();
        JPanel pnCollectionListButtons = new JPanel();
        JPanel pnCollectionButtons = new JPanel();
        JPanel pnCollectionCounterButtons = new JPanel();

        pnList.setLayout(new BorderLayout());
        pnCollectionList.setLayout(new BorderLayout());
        pnCollectionListButtons.setLayout(new BorderLayout());
        pnCollectionButtons.setLayout(new BorderLayout());
        pnCollectionCounterButtons.setLayout(new BorderLayout());

        //Create table grid
        tmCollectionEdit = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                if (col > 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

        tmCollectionEdit.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.Count"));
        tmCollectionEdit.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.SAP"));
        tmCollectionEdit.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.Name"));
        tmCollectionEdit.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.ListPrice"));
        tmCollectionEdit.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.NetPrice"));
        tmCollectionEdit.addColumn(propertyUtil.getLangText("InternalFrame.CollectionList.NetSum"));

        pnList.add(jtCollectionEdit = new JTable(tmCollectionEdit), "Center");
        pnList.add(new JScrollPane(jtCollectionEdit));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);

        //DefaultCellEditor dce = new DefaultCellEditor(new JComboBox());

        //Create number field to modify item count in the collection list
        JRNumberTextField itemCount = new JRNumberTextField();
        itemCount.addBadChars("-");
        itemCount.setMaxLength(4);
        itemCount.setBorder(BorderFactory.createLoweredBevelBorder());


        DefaultCellEditor cellEditor = new DefaultCellEditor(itemCount);
        jtCollectionEdit.getColumnModel()
                        .getColumn(0)
                        .setCellEditor(cellEditor);

        jtCollectionEdit.getColumnModel()
                        .getColumn(0)
                        .setPreferredWidth(30);
        jtCollectionEdit.getColumnModel()
                        .getColumn(1)
                        .setPreferredWidth(50);
        jtCollectionEdit.getColumnModel()
                        .getColumn(2)
                        .setPreferredWidth(340);

        jtCollectionEdit.getColumnModel()
                        .getColumn(3)
                        .setCellRenderer(renderer);
        jtCollectionEdit.getColumnModel()
                        .getColumn(4)
                        .setCellRenderer(renderer);
        jtCollectionEdit.getColumnModel()
                        .getColumn(5)
                        .setCellRenderer(renderer);

        jtCollectionEdit.getTableHeader().setFont(swc.fontExcelHeader);
        jtCollectionEdit.getTableHeader().setBackground(swc.colorExcelHeader);
        jtCollectionEdit.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        jtCollectionEdit.setToolTipText(propertyUtil.getLangText("InternalFrame.CollectionEdit.ToolTipText"));
        jtCollectionEdit.setFocusable(false);

        Dimension dimension = new Dimension(18, 18);

        //Create buttons on list
        pnCollectionButtons.add(jbCollectionRemove = new JButton(new ImageIcon(swc.iconPath + "\\Minus.jpg")), "North");
        pnCollectionList.add(pnCollectionButtons, "West");
        jbCollectionRemove.setPreferredSize(dimension);

        //Create buttons on table gird
        pnCollectionListButtons.add(jbCollectionEditAdd = new JButton(new ImageIcon(swc.iconPath + "\\Plus.jpg")),
                                    "North");
        pnCollectionListButtons.add(jbCollectionEditRemove = new JButton(new ImageIcon(swc.iconPath + "\\Minus.jpg")),
                                    "South");

        //Create counter buttons on table gird
        pnCollectionCounterButtons.add(jbCollectionEditIncrease =
                                       new JButton(new ImageIcon(swc.iconPath + "\\Increase.jpg")), "North");
        pnCollectionCounterButtons.add(jbCollectionEditDecrease =
                                       new JButton(new ImageIcon(swc.iconPath + "\\Decrease.jpg")), "South");
        jbCollectionEditIncrease.setPreferredSize(dimension);
        jbCollectionEditDecrease.setPreferredSize(dimension);


        pnCollectionListButtons.add(pnCollectionCounterButtons, "Center");

        pnList.add(pnCollectionListButtons, "West");


        jbCollectionEditAdd.setPreferredSize(dimension);
        jbCollectionEditRemove.setPreferredSize(dimension);

        //Create list
        dlCollectionEdit = new DefaultListModel();
        pnCollectionList.add(new JScrollPane(jlCollectionEdit = new JList(dlCollectionEdit)), "Center");

        jlCollectionEdit.addListSelectionListener(this);

        //Add panels
        jriCollectionEdit.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnCollectionList, pnList);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        jriCollectionEdit.add(splitPane, "Center");

        //Add actions
        jtCollectionEdit.addMouseListener(this);
        jtCollectionEdit.addKeyListener(this);

        jbCollectionEditAdd.addActionListener(this);
        jbCollectionEditAdd.setActionCommand("collectioneditadd_showpricelist");
        jbCollectionEditRemove.addActionListener(this);
        jbCollectionEditRemove.setActionCommand("collectioneditremove");
        jbCollectionRemove.addActionListener(this);
        jbCollectionRemove.setActionCommand("collectionremove");
        jbCollectionEditIncrease.addActionListener(this);
        jbCollectionEditIncrease.setActionCommand("collectioneditincrease");
        jbCollectionEditDecrease.addActionListener(this);
        jbCollectionEditDecrease.setActionCommand("collectioneditdecrease");
        itemCount.addActionListener(this);
        itemCount.addKeyListener(this);
        itemCount.setName("collectionedititemmodify");
        itemCount.setActionCommand("collectionedititemmodify");

    }


    public void createInternalFrame_EditDataBase() {
        jriEditDataBase = new JRInternalFrame("EditDataBase", "InternalFrame.EditDataBase.Title", mainDeskTop);
        jriEditDataBase.setLayout(new BorderLayout());
        JPanel pnTop = new JPanel();
        JPanel pnCenter = new JPanel();
        JPanel pnGrid = new JPanel();
        JPanel pnGridLeft = new JPanel();
        JPanel pnButtonsTop = new JPanel();
        JPanel pnButtonsBottom = new JPanel();

        //Top panel
        pnTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnTop.add(new JLabel(propertyUtil.getLangText("InternalFrame.EditDataBase.Type") + ": "));
        pnTop.add(jcbEditDatabaseTyes = new JComboBox());
        pnTop.add(new JLabel("     "));
        pnTop.add(jbEditDbOpen = new JButton(new ImageIcon(swc.iconPath + "Open.jpg")));
        pnTop.add(jbEditDbNew = new JButton(new ImageIcon(swc.iconPath + "DatabaseNew.jpg")));
        pnTop.add(jbEditDbClose = new JButton(new ImageIcon(swc.iconPath + "DatabaseClose.jpg")));
        pnTop.add(jbEditDbSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));
        pnTop.add(jbEditDbSaveAs = new JButton(new ImageIcon(swc.iconPath + "SaveAs.jpg")));
        pnTop.add(jbEditDbSaveExcel = new JButton(new ImageIcon(swc.iconPath + "Excel.jpg")));
        pnTop.add(new JLabel("   " + propertyUtil.getLangText("InternalFrame.EditDataBase.DataBase") + ": "));
        pnTop.add(jlEditingDbName = new JLabel(""));

        jbEditDbOpen.setPreferredSize(new Dimension(30, 30));
        jbEditDbNew.setPreferredSize(new Dimension(30, 30));
        jbEditDbClose.setPreferredSize(new Dimension(30, 30));
        jbEditDbSave.setPreferredSize(new Dimension(30, 30));
        jbEditDbSaveAs.setPreferredSize(new Dimension(30, 30));
        jbEditDbSaveExcel.setPreferredSize(new Dimension(30, 30));

        //Left panel
        pnGridLeft.setLayout(new BorderLayout());
        pnButtonsTop.setLayout(new GridLayout(3, 1));
        //pnButtonsTop.add(jbEditDbClearSorter = new JButton(new ImageIcon(swc.iconPath + "ClearSorter.jpg")),"North");
        pnButtonsTop.add(jbEditDbAddItem = new JButton(new ImageIcon(swc.iconPath + "Plus.jpg")));
        pnButtonsTop.add(jbEditDbMoveUp = new JButton(new ImageIcon(swc.iconPath + "MoveUp.jpg")));
        pnButtonsTop.add(jbEditDbMoveDown = new JButton(new ImageIcon(swc.iconPath + "MoveDown.jpg")));
        jbEditDbMoveUp.setPreferredSize(new Dimension(22, 22));
        jbEditDbMoveDown.setPreferredSize(new Dimension(22, 22));
        jbEditDbAddItem.setPreferredSize(new Dimension(22, 22));


        pnButtonsBottom.setLayout(new BorderLayout());
        pnButtonsBottom.add(jcDbEditDeleteConfirm = new JCheckBox(), "North");
        pnButtonsBottom.add(jbEditDbRemoveItem = new JButton(new ImageIcon(swc.iconPath + "Minus.jpg")), "South");
        jbEditDbRemoveItem.setPreferredSize(new Dimension(22, 22));

        pnGridLeft.add(pnButtonsTop, "North");
        pnGridLeft.add(pnButtonsBottom, "South");

        //Center panel
        pnCenter.setLayout(new BorderLayout());
        pnGrid.setLayout(new BorderLayout());

        tmdbEditDataBase = new DataBaseTableModel();

        pnGrid.add(jtEditDataBase = new JTable(tmdbEditDataBase), "Center");
        pnGrid.add(new JScrollPane(jtEditDataBase));
        pnCenter.add(pnGrid, "Center");
        pnCenter.add(pnGridLeft, "West");

        tmdbEditDataBase.setProperties(jtEditDataBase);

        jriEditDataBase.add(pnTop, "North");
        jriEditDataBase.add(pnCenter, "Center");

        //ToolTiptext
        jbEditDbAddItem.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.AddItem.ToolTipText"));
        jbEditDbRemoveItem.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.RemoveItem.ToolTipText"));
        jbEditDbMoveUp.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.MoveItemUp.ToolTipText"));
        jbEditDbMoveDown.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.MoveItemDown.ToolTipText"));
        jcDbEditDeleteConfirm.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.RemoveItem.Confirm.ToolTipText"));
        jbEditDbOpen.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.Open.ToolTipText"));
        jbEditDbClose.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.Close.ToolTipText"));
        jbEditDbNew.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.New.ToolTipText"));
        jbEditDbSave.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.Save.ToolTipText"));
        jbEditDbSaveAs.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.SaveAs.ToolTipText"));
        jbEditDbSaveExcel.setToolTipText(propertyUtil.getLangText("InternalFrame.EditDataBase.SaveExcel.ToolTipText"));

        //Actions

        //jbEditDbClearSorter.addActionListener(this);
        //jbEditDbClearSorter.setActionCommand("editdbclearsorter");
        jcbEditDatabaseTyes.addActionListener(this);
        jcbEditDatabaseTyes.setActionCommand("editdbtypeselected");
        jbEditDbAddItem.addActionListener(this);
        jbEditDbAddItem.setActionCommand("editdbadditem_showpricelist");
        jbEditDbRemoveItem.addActionListener(this);
        jbEditDbRemoveItem.setActionCommand("editdbremoveitem");
        jbEditDbMoveUp.addActionListener(this);
        jbEditDbMoveUp.setActionCommand("editdbmoveup");
        jbEditDbMoveDown.addActionListener(this);
        jbEditDbMoveDown.setActionCommand("editdbmovedown");
        jbEditDbSave.addActionListener(this);
        jbEditDbSave.setActionCommand("savedatabaseedit");
        jbEditDbSaveAs.addActionListener(this);
        jbEditDbSaveAs.setActionCommand("saveasdatabaseedit");
        jbEditDbNew.addActionListener(this);
        jbEditDbNew.setActionCommand("opennewdatabaseedit");
        jbEditDbOpen.addActionListener(this);
        jbEditDbOpen.setActionCommand("opendatabaseedit");
        jbEditDbClose.addActionListener(this);
        jbEditDbClose.setActionCommand("closedatabaseedit");
        jbEditDbSaveExcel.addActionListener(this);
        jbEditDbSaveExcel.setActionCommand("savedatabaseexcel");

    }

    public void createInternalFrame_ReplaceData() {
        jriReplaceData = new JRInternalFrame("ReplaceData", "InternalFrame.ReplaceData.Title", mainDeskTop);
        jriReplaceData.setLayout(new BorderLayout());

        JPanel pnBottom = new JPanel();
        JPanel pnCenter = new JPanel();
        JPanel pnList = new JPanel();
        JPanel pnGrid = new JPanel();
        JPanel pnButtonsList = new JPanel();
        JPanel pnButtonsGrid = new JPanel();
        JPanel pnInfo = new JPanel();

        pnGrid.setLayout(new BorderLayout());
        pnCenter.setLayout(new BorderLayout());
        pnButtonsGrid.setLayout(new BorderLayout());
        pnList.setLayout(new BorderLayout());
        pnButtonsList.setLayout(new BorderLayout());
        pnInfo.setLayout(new BorderLayout());

        tmrReplaceData = new ReplaceTableModel();

        //Create list for files
        pnButtonsList.add(jbReplaceListAdd = new JButton(new ImageIcon(swc.iconPath + "Plus.jpg")), "North");
        pnButtonsList.add(jbReplaceListRemove = new JButton(new ImageIcon(swc.iconPath + "Minus.jpg")), "South");
        jbReplaceListAdd.setPreferredSize(new Dimension(18, 18));
        jbReplaceListRemove.setPreferredSize(new Dimension(18, 18));

        dlReplaceFiles = new DefaultListModel();
        pnList.add(jlReplaceFies = new JList(dlReplaceFiles), "Center");
        pnList.add(new JScrollPane(jlReplaceFies));
        pnList.add(pnButtonsList, "West");
        pnList.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.ReplaceData.Files.Title")));

        //Create grid for SAP
        pnButtonsGrid.add(jbReplaceDataAdd = new JButton(new ImageIcon(swc.iconPath + "Plus.jpg")), "North");
        pnButtonsGrid.add(jbReplaceDataRemove = new JButton(new ImageIcon(swc.iconPath + "Minus.jpg")), "South");
        jbReplaceDataAdd.setPreferredSize(new Dimension(18, 18));
        jbReplaceDataRemove.setPreferredSize(new Dimension(18, 18));

        pnGrid.add(jtReplaceData = new JTable(tmrReplaceData), "Center");
        pnGrid.add(new JScrollPane(jtReplaceData));
        pnGrid.add(pnButtonsGrid, "West");

        pnGrid.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.ReplaceData.SAP.Title")));

        tmrReplaceData.setProperties(jtReplaceData);

        //Create infopanel
        pnInfo.add(jtReplaceInfo = new JTextArea(5, 20), "Center");
        pnInfo.add(new JScrollPane(jtReplaceInfo));
        jtReplaceInfo.setEditable(false);
        jtReplaceInfo.setFont(swc.fontExcelHeader);

        //Create bottom panel
        ButtonGroup group = new ButtonGroup();
        pnBottom.add(jrReplaceText =
                     new JRadioButton(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceText.Title")));
        pnBottom.add(jrReplaceSAP =
                     new JRadioButton(propertyUtil.getLangText("InternalFrame.ReplaceData.ReplaceSAP.Title"), true));
        pnBottom.add(jbReplace = new JButton(propertyUtil.getLangText("InternalFrame.ReplaceData.Replace.Title")));

        group.add(jrReplaceText);
        group.add(jrReplaceSAP);

        //Add panels
        pnGrid.add(pnInfo, "South");
        pnCenter.add(pnGrid, "East");
        pnCenter.add(pnList, "Center");
        pnCenter.add(pnBottom, "South");
        jriReplaceData.add(pnCenter, "Center");

        //Add actions
        jbReplaceDataAdd.addActionListener(this);
        jbReplaceDataAdd.setActionCommand("replacedataadditem");
        jbReplaceDataRemove.addActionListener(this);
        jbReplaceDataRemove.setActionCommand("replacedataremoveitem");
        jbReplaceListAdd.addActionListener(this);
        jbReplaceListAdd.setActionCommand("replacelistadditem");
        jbReplaceListRemove.addActionListener(this);
        jbReplaceListRemove.setActionCommand("replacelistremoveitem");
        jbReplace.addActionListener(this);
        jbReplace.setActionCommand("replacedata");

    }

    public void createInternalFrame_LanguagesEdit() {
        jriLanguagesEdit = new JRInternalFrame("LanguagesEdit", "InternalFrame.LanguagesEdit.Title", mainDeskTop);

        JPanel pnEditor = new JPanel();
        JPanel pnGrid = new JPanel();
        JPanel pnBottom = new JPanel();

        JPanel pnTop = new JPanel();
        pnEditor.setLayout(new BorderLayout());
        pnGrid.setLayout(new BorderLayout());

        pnTop.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.LanguagesEdit.Languages.Title")));
        pnGrid.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.LanguagesEdit.Editor.Title")));

        jcbLanguageLeft = new JComboBox();
        jcbLanguageRight = new JComboBox();

        pnTop.add(new JLabel(propertyUtil.getLangText("InternalFrame.LanguagesEdit.LanguageSelector")));
        pnTop.add(new JLabel("    "));
        pnTop.add(jlLanguageLeft = new JLabel());
        pnTop.add(jcbLanguageLeft);
        pnTop.add(new JLabel("    "));
        pnTop.add(jlLanguageRight = new JLabel());
        pnTop.add(jcbLanguageRight);
        jlLanguageLeft.setPreferredSize(new Dimension(55, 55));
        jlLanguageRight.setPreferredSize(new Dimension(55, 55));

        //Create table grid
        tmLanguagesEdit = new DefaultTableModel() {


            public boolean isCellEditable(int row, int col) {
                String value = "";
                if (getValueAt(row, col) != null) {
                    value = getValueAt(row, col).toString();
                }

                if (col == 0) {
                    return propertyUtil.getLangText(value).equals(value);
                } else {
                    return true;
                }
            }

        };

        tmLanguagesEdit.addColumn(propertyUtil.getLangText("InternalFrame.LanguagesEdit.Key"));
        tmLanguagesEdit.addColumn("");
        tmLanguagesEdit.addColumn("");

        pnBottom.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(2, 2, 5, 2);
        pnBottom.add(jbLanguageEditorAdd =
                     new JButton(propertyUtil.getLangText("Frame.Canvas.Button.AddCollection"),
                                 new ImageIcon(swc.iconPath + "Plus.jpg")), c);
        c.gridx = 1;
        pnBottom.add(jbLanguageEditorSave =
                     new JButton(propertyUtil.getLangText("InternalFrame.LanguagesEdit.Save"),
                                 new ImageIcon(swc.iconPath + "Save.jpg")), c);

        jtLanguagesEdit = new JTable(tmLanguagesEdit);

        jtLanguagesEdit.getTableHeader().setFont(swc.fontExcelHeader);
        jtLanguagesEdit.getTableHeader().setBackground(swc.colorExcelHeader);
        jtLanguagesEdit.getTableHeader().setReorderingAllowed(false);
        jtLanguagesEdit.setAutoCreateRowSorter(true);
        jtLanguagesEdit.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtLanguagesEdit.getRowSorter().toggleSortOrder(0);

        pnGrid.add(new JScrollPane(jtLanguagesEdit), "Center");
        pnEditor.add(pnTop, "North");
        pnEditor.add(pnGrid, "Center");
        pnEditor.add(pnBottom, "South");
        jriLanguagesEdit.add(pnEditor, "Center");

        for (int i = 0; i < swc.flagTitles.length; i++) {
            jcbLanguageLeft.addItem(swc.flagTitles[i]);
            jcbLanguageRight.addItem(swc.flagTitles[i]);
        }

        jcbLanguageRight.setSelectedIndex(1);

        //Add actions
        jcbLanguageLeft.addActionListener(this);
        jcbLanguageLeft.setActionCommand("languagecomboselected");
        jcbLanguageRight.addActionListener(this);
        jcbLanguageRight.setActionCommand("languagecomboselected");
        jbLanguageEditorSave.addActionListener(this);
        jbLanguageEditorSave.setActionCommand("languageeditorsave");
        jbLanguageEditorAdd.addActionListener(this);
        jbLanguageEditorAdd.setActionCommand("languageeditoradd");
    }

    public void createInternalFrame_Languages() {
        jriLanguages = new JRInternalFrame("Languages", "InternalFrame.Languages.Title", mainDeskTop);
        JPanel pnLanguages = new JPanel();
        JPanel pnButtons = new JPanel();
        JPanel pnFlags = new JPanel();

        pnFlags.setLayout(new GridLayout(6, 3));
        pnLanguages.setLayout(new BorderLayout());
        pnLanguages.setBorder(BorderFactory.createEtchedBorder());
        pnLanguages.setBackground(Color.WHITE);
        pnFlags.setBackground(Color.WHITE);
        pnButtons.setBackground(Color.WHITE);
        groupFlags = new ButtonGroup();

        for (int i = 0; i < swc.flags.length; i++) {
            JPanel pnFlag = new JPanel();
            pnFlag.setBackground(Color.WHITE);
            pnFlag.setLayout(new FlowLayout(FlowLayout.LEFT));

            JRadioButton jbFlag = new JRadioButton();
            jbFlag.setSelected(i == 0);
            jbFlag.setName(swc.flagTitles[i]);
            jbFlag.setBackground(Color.WHITE);

            groupFlags.add(jbFlag);
            pnFlag.add(jbFlag);

            JLabel flagLabel = new JLabel(swc.flagTitles[i]);
            flagLabel.setPreferredSize(new Dimension(100, 40));

            pnFlag.add(jbFlag);
            pnFlag.add(new JLabel(new ImageIcon(swc.flagPath + swc.flags[i] + ".jpg")));
            pnFlag.add(flagLabel);
            pnFlags.add(pnFlag);
        }


        pnButtons.add(jbLanguagesSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));
        jbLanguagesSave.setPreferredSize(new Dimension(28, 28));
        jbLanguagesSave.setToolTipText(propertyUtil.getLangText("InternalFrame.Languages.Save.ToolTipText"));

        JScrollPane scroll = new JScrollPane(pnFlags);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(true);
        pnLanguages.add(scroll, "Center");
        pnLanguages.add(pnButtons, "South");

        jriLanguages.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriLanguages.add(pnLanguages, new CellConstraints().xy(1, 1));

        //Add actions
        jbLanguagesSave.addActionListener(this);
        jbLanguagesSave.setActionCommand("languagesave");
    }

    public void createInternalFrame_StockList() {
        jriItemList = new JRInternalFrame("ItemList", "InternalFrame.StockList.Title", mainDeskTop);


        nodeRoot = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.StockList"));
        nodeRootDirection = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Direction"));
        nodeLeft = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Left"));
        nodeRight = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Right"));
        nodeOver = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Over"));
        nodeRootHandle = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Handle"));
        nodeHandleYes = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Yes"));
        nodeHandleNo = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.No"));
        nodeRootLiftingMd = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.LiftingMd"));
        nodeLiftingMdYes = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Yes"));
        nodeLiftingMdNo = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.No"));
        nodeRootCatch = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Catch"));
        nodeCatchNone = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.No"));
        nodeCatchNormal = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Catch"));
        nodeCatchMagnet = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.CatchMagnet"));
        nodeRootFan = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Fan"));
        nodeFanYes = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Yes"));
        nodeFanNo = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.No"));
        nodeRootBar = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Bar"));
        nodeNormalBar = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.NormalBar"));
        nodeRootClosableBar = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.ClosableBar"));
        nodeRootSelectionBar = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.SelectionBar"));
        nodeBarKo = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.BarKo"));
        nodeBarKsr = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.BarKsr"));
        nodeBarM = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.BarM"));
        nodeRootCilinder = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Cilinder"));
        nodeCilinderYes = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Yes"));
        nodeCilinderNo = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.No"));
        nodeRootColor = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Colors"));
        nodeRootColumn = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.Column"));
        nodeMDK = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MDK"));
        nodeMDF = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MDF"));
        nodeMDS = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MDS"));
        nodeMKIPP = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MKIPP"));
        nodeMDK2 = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MDK2"));
        nodeMDF2 = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MDF2"));
        nodeMDS2 = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MDS2"));
        nodeMFREI = new CheckNode(propertyUtil.getLangText("InternalFrame.StockList.Node.MFREI"));

        for (int i = 0; i < swc.closableBarArray.length; i++) {
            CheckNode node = new CheckNode(swc.closableBarArray[i]);
            node.setSelected(i == 0);
            nodeRootClosableBar.add(node);
        }

        for (int i = 0; i < swc.colorTitles.length; i++) {
            CheckNode node =
                new CheckNode(propertyUtil.getLangText("Color." + swc.colorTitles[i]) + " [" + swc.colorArray[i] + "]");
            node.setSelected(i == 0);
            nodeRootColor.add(node);
        }

        //Node properties
        nodeRoot.setCheckBoxEnabled(false);
        nodeRoot.setSelected(true);
        nodeRootDirection.setCheckBoxEnabled(false);
        nodeRootDirection.setCheckBoxGroup(true);
        nodeRootDirection.setSelected(true);
        nodeOver.setSelected(true);
        nodeRootHandle.setCheckBoxEnabled(false);
        nodeRootHandle.setCheckBoxGroup(true);
        nodeHandleNo.setSelected(true);
        nodeRootLiftingMd.setCheckBoxEnabled(false);
        nodeRootLiftingMd.setCheckBoxGroup(true);
        nodeLiftingMdNo.setSelected(true);
        nodeRootCatch.setCheckBoxEnabled(false);
        nodeRootCatch.setCheckBoxGroup(true);
        nodeCatchNone.setSelected(true);
        nodeRootFan.setCheckBoxEnabled(false);
        nodeRootFan.setCheckBoxGroup(true);
        nodeFanNo.setSelected(true);
        nodeRootBar.setCheckBoxEnabled(false);
        nodeRootSelectionBar.setCheckBoxEnabled(false);
        nodeBarKo.setSelected(true);
        nodeNormalBar.setSelected(true);
        nodeRootCilinder.setCheckBoxEnabled(false);
        nodeRootCilinder.setCheckBoxGroup(true);
        nodeCilinderNo.setSelected(true);
        nodeRootColor.setCheckBoxEnabled(false);
        nodeRootColumn.setCheckBoxEnabled(false);

        //Column
        nodeRootColumn.add(nodeMDK);
        nodeRootColumn.add(nodeMDF);
        nodeRootColumn.add(nodeMDS);
        nodeRootColumn.add(nodeMKIPP);
        nodeRootColumn.add(nodeMDK2);
        nodeRootColumn.add(nodeMDF2);
        nodeRootColumn.add(nodeMDS2);
        nodeRootColumn.add(nodeMFREI);
        nodeMDK.setSelected(true);
        nodeMDF.setSelected(true);
        nodeMDS.setSelected(true);
        nodeMKIPP.setSelected(true);
        nodeMDK2.setSelected(true);
        nodeMDS2.setSelected(true);
        nodeMDF2.setSelected(true);
        nodeMFREI.setSelected(true);
        //Direction
        nodeRootDirection.add(nodeLeft);
        nodeRootDirection.add(nodeRight);
        nodeRootDirection.add(nodeOver);
        //Handle
        nodeRootHandle.add(nodeHandleYes);
        nodeRootHandle.add(nodeHandleNo);
        //Liftin md
        nodeRootLiftingMd.add(nodeLiftingMdYes);
        nodeRootLiftingMd.add(nodeLiftingMdNo);
        //Catch
        nodeRootCatch.add(nodeCatchNone);
        nodeRootCatch.add(nodeCatchNormal);
        nodeRootCatch.add(nodeCatchMagnet);
        //Fan
        nodeRootFan.add(nodeFanYes);
        nodeRootFan.add(nodeFanNo);
        //Bar
        nodeRootBar.add(nodeNormalBar);
        nodeRootBar.add(nodeRootClosableBar);

        //Selection Bar
        nodeRootSelectionBar.add(nodeBarKo);
        nodeRootSelectionBar.add(nodeBarKsr);
        nodeRootSelectionBar.add(nodeBarM);
        nodeRootBar.add(nodeRootSelectionBar);

        //cilinder
        nodeRootCilinder.add(nodeCilinderNo);
        nodeRootCilinder.add(nodeCilinderYes);

        //Root
        nodeRoot.add(nodeRootColumn);
        nodeRoot.add(nodeRootDirection);
        nodeRoot.add(nodeRootHandle);
        nodeRoot.add(nodeRootLiftingMd);
        nodeRoot.add(nodeRootCatch);
        nodeRoot.add(nodeRootFan);
        nodeRoot.add(nodeRootBar);
        nodeRoot.add(nodeRootCilinder);
        nodeRoot.add(nodeRootColor);

        JTree tree = new JTree(nodeRoot);
        tree.setCellRenderer(new CheckRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.putClientProperty("JTree.lineStyle", "Angled");
        tree.addMouseListener(new NodeSelectionListener(tree));
        JScrollPane sp = new JScrollPane(tree);

        //Create panels
        JPanel pnTree = new JPanel();
        JPanel pnGrid = new JPanel();
        JPanel pnButtons = new JPanel();
        JPanel pnGridButtons = new JPanel();
        JPanel pnGridButtonsDelete = new JPanel();
        JPanel pnFileList = new JPanel();
        JPanel pnFileListButtons = new JPanel();
        JPanel pnFileListButtonsDelete = new JPanel();

        pnGrid.setLayout(new BorderLayout());
        pnGridButtons.setLayout(new BorderLayout());
        pnGridButtonsDelete.setLayout(new BorderLayout());
        pnTree.setLayout(new BorderLayout());
        pnFileList.setLayout(new BorderLayout());
        pnFileListButtons.setLayout(new BorderLayout());
        pnFileListButtonsDelete.setLayout(new BorderLayout());
        pnButtons.setLayout(new FlowLayout());
        pnTree.add(sp, "Center");


        //Create a split pane for header
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnTree, pnGrid);
        splitPane.setOneTouchExpandable(true);

        //Create table grid
        tmStockList = new DefaultTableModel() {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tmStockList.addColumn(propertyUtil.getLangText("InternalFrame.StockList.Count"));
        tmStockList.addColumn(propertyUtil.getLangText("InternalFrame.StockList.SAP"));
        tmStockList.addColumn(propertyUtil.getLangText("InternalFrame.StockList.Text"));
        tmStockList.addColumn(propertyUtil.getLangText("InternalFrame.StockList.NetPrice"));

        pnGrid.add(jtStockList = new JTable(tmStockList), "Center");
        pnGrid.add(new JScrollPane(jtStockList));

        jtStockList.getTableHeader().setFont(swc.fontExcelHeader);
        jtStockList.getTableHeader().setBackground(swc.colorExcelHeader);
        jtStockList.setAutoCreateRowSorter(true);
        jtStockList.getColumnModel()
                   .getColumn(0)
                   .setMaxWidth(55);
        jtStockList.getColumnModel()
                   .getColumn(1)
                   .setMaxWidth(60);
        jtStockList.getColumnModel()
                   .getColumn(3)
                   .setMaxWidth(120);
        jtStockList.getTableHeader().setReorderingAllowed(false);

        pnGrid.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.StockList.ItemList")));

        //Create left grid buttons
        pnGridButtons.add(jbStockAddItem = new JButton(new ImageIcon(swc.iconPath + "Plus.jpg")), "North");
        jbStockAddItem.setPreferredSize(new Dimension(18, 18));
        pnGridButtonsDelete.add(jcStockItemDeleteConfirm = new JCheckBox(), "North");
        pnGridButtonsDelete.add(jbStockRemoveItem = new JButton(new ImageIcon(swc.iconPath + "Minus.jpg")), "South");
        jbStockRemoveItem.setPreferredSize(new Dimension(18, 18));
        pnGridButtons.add(pnGridButtonsDelete, "South");

        //Create file list
        dlStockFiles = new DefaultListModel();
        pnFileList.add(jlStockFiles = new JList(dlStockFiles), "Center");
        pnFileList.add(new JScrollPane(jlStockFiles));
        pnFileList.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.StockList.FileList")));

        //Create file list buttons
        pnFileListButtons.add(jbStockAddFile = new JButton(new ImageIcon(swc.iconPath + "Plus.jpg")), "North");
        jbStockAddFile.setPreferredSize(new Dimension(18, 18));
        pnFileListButtonsDelete.add(jcStockFileDeleteConfirm = new JCheckBox(), "North");
        pnFileListButtonsDelete.add(jbStockRemoveFile = new JButton(new ImageIcon(swc.iconPath + "Minus.jpg")),
                                    "South");
        pnFileListButtons.add(pnFileListButtonsDelete, "South");
        jbStockRemoveFile.setPreferredSize(new Dimension(18, 18));


        //Create buttons
        Dimension dimension = new Dimension(28, 28);
        pnButtons.add(jbStockListCollect = new JButton(new ImageIcon(swc.iconPath + "CollateList.jpg")));
        pnButtons.add(jbStockListOpen = new JButton(new ImageIcon(swc.iconPath + "Open.jpg")));
        pnButtons.add(jbStockListSave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")));
        pnButtons.add(jbStockListSaveExcel = new JButton(new ImageIcon(swc.iconPath + "Excel.jpg")));
        pnButtons.add(jbStockListPrint = new JButton(new ImageIcon(swc.iconPath + "Print.jpg")));
        pnButtons.add(jbStockListCollectDelete = new JButton(new ImageIcon(swc.iconPath + "ListDelete.jpg")));

        jbStockListOpen.setPreferredSize(dimension);
        jbStockListCollect.setPreferredSize(dimension);
        jbStockListCollectDelete.setPreferredSize(dimension);
        jbStockListPrint.setPreferredSize(dimension);
        jbStockListSave.setPreferredSize(dimension);
        jbStockListSaveExcel.setPreferredSize(dimension);

        jbStockListOpen.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.Open.ToolTiptext"));
        jbStockListSave.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.Save.ToolTiptext"));
        jbStockListSaveExcel.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.SaveExcel.ToolTiptext"));
        jbStockListCollect.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.Collect.ToolTiptext"));
        jbStockListCollectDelete.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.CollectDelete.ToolTiptext"));
        jbStockListPrint.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.Print.ToolTiptext"));
        jbStockAddFile.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.AddFile.ToolTiptext"));
        jbStockRemoveFile.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.RemoveFile.ToolTiptext"));
        jbStockAddItem.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.AddItem.ToolTiptext"));
        jbStockRemoveItem.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.RemoveItem.ToolTiptext"));

        jcStockItemDeleteConfirm.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.RemoveItemConfirm.ToolTiptext"));
        jcStockFileDeleteConfirm.setToolTipText(propertyUtil.getLangText("InternalFrame.StockList.RemoveFileConfirm.ToolTiptext"));

        //Add panels
        pnGrid.add(pnFileList, "South");
        pnGrid.add(pnGridButtons, "West");
        pnFileList.add(pnFileListButtons, "West");
        jriItemList.setLayout(new BorderLayout());
        jriItemList.add(splitPane, "Center");
        jriItemList.add(pnButtons, "South");

        //Add actions
        jbStockAddItem.addActionListener(this);
        jbStockAddItem.setActionCommand("stocklistadditem_showpricelist");
        jbStockRemoveItem.addActionListener(this);
        jbStockRemoveItem.setActionCommand("stocklistremoveitem");
        jbStockAddFile.addActionListener(this);
        jbStockAddFile.setActionCommand("stocklistaddfile");
        jbStockRemoveFile.addActionListener(this);
        jbStockRemoveFile.setActionCommand("stocklistremovefile");
        jbStockListOpen.addActionListener(this);
        jbStockListOpen.setActionCommand("stocklistopen");
        jbStockListSave.addActionListener(this);
        jbStockListSave.setActionCommand("stocklistsave");
        jbStockListSaveExcel.addActionListener(this);
        jbStockListSaveExcel.setActionCommand("stocklistsaveexcel");
        jbStockListPrint.addActionListener(this);
        jbStockListPrint.setActionCommand("stocklistprint");
        jbStockListCollect.addActionListener(this);
        jbStockListCollect.setActionCommand("stocklistcollect");
        jbStockListCollectDelete.addActionListener(this);
        jbStockListCollectDelete.setActionCommand("stocklistcollectdelete");
    }

    public void createInternalFrame_Profiles() {
        jriProfiles = new JRInternalFrame("Profiles", "InternalFrame.Profiles.Title", mainDeskTop);
        JPanel pnProfiles = new JPanel();
        JPanel pnExport = new JPanel();
        JPanel pnProfilesLeft = new JPanel();
        JPanel pnProfilesRight = new JPanel();
        JPanel pnDirectory = new JPanel();
        JPanel pnExportInfo = new JPanel();
        pnProfiles.setLayout(new BorderLayout());
        pnProfilesLeft.setLayout(new BorderLayout());
        pnProfilesRight.setLayout(new BorderLayout());
        pnExport.setLayout(new BorderLayout());
        pnExportInfo.setLayout(new BorderLayout());

        //Profile tree
        nodeRootProfiles = new CheckNode(propertyUtil.getLangText("InternalFrame.Profiles.Node.Profiles"));
        nodeRootProfiles.setCheckBoxEnabled(false);
        nodeRootProfiles.setSelectionMode(CheckNode.SINGLE_SELECTION);
        nodeRootProfiles.setSelected(true);

        profileTree = new JTree(nodeRootProfiles);
        profileTree.setCellRenderer(new CheckRenderer());
        profileTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        profileTree.putClientProperty("JTree.lineStyle", "Angled");
        profileTree.addMouseListener(new NodeSelectionListener(profileTree));
        profileTree.setToolTipText(propertyUtil.getLangText("InternalFrame.Profiles.ToolTipText.ProfileTree"));

        //Export tree
        nodeRootExport = new CheckNode(propertyUtil.getLangText("InternalFrame.Profiles.Node.Export"));
        nodeRootExport.setCheckBoxEnabled(false);
        nodeRootExport.setSelectionMode(CheckNode.SINGLE_SELECTION);
        nodeRootExport.setSelected(true);
        nodeRootExport.setImageIconDefault(new ImageIcon(swc.iconPath + ("Profiles.jpg")));
        nodeRootExport.setImageIconClosed(new ImageIcon(swc.iconPath + ("Profiles.jpg")));
        nodeRootExport.setImageIconOpened(new ImageIcon(swc.iconPath + ("Profiles.jpg")));


        exportTree = new JTree(nodeRootExport);
        exportTree.setCellRenderer(new CheckRenderer());
        exportTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        exportTree.putClientProperty("JTree.lineStyle", "Angled");

        //Directory panel
        pnDirectory.add(new JLabel(propertyUtil.getLangText("InternalFrame.Profiles.Button.Directory") + ": "));
        pnDirectory.add(tfExportDirectory = new JTextField(16));
        tfSubDirectory = new JTextField("\\Roto", 4);
        pnDirectory.add(tfSubDirectory);
        tfSubDirectory.setEditable(false);
        pnDirectory.add(jbExportDirectory = new JButton(new ImageIcon(swc.iconPath + "Open.jpg")));
        tfExportDirectory.setEditable(false);
        jbExportDirectory.setPreferredSize(new Dimension(28, 28));

        //Info panel
        pnExportInfo.add(new JScrollPane(jtExportInfo = new JTextArea()), "Center");
        jtExportInfo.setEditable(false);
        jtExportInfo.setFont(swc.fontExportInfo);

        //Left panel
        pnProfilesLeft.add(new JScrollPane(profileTree), "Center");
        pnProfilesLeft.add(jbProfileAssembly =
                           new JButton(propertyUtil.getLangText("InternalFrame.Profiles.Button.Assembly")), "South");
        jbProfileAssembly.setPreferredSize(new Dimension(175, 25));

        //Left panel
        pnProfilesRight.add(new JScrollPane(exportTree), "Center");
        pnProfilesRight.add(jbProfileExport =
                            new JButton(propertyUtil.getLangText("InternalFrame.Profiles.Button.Export")), "South");
        jbProfileExport.setPreferredSize(new Dimension(200, 25));

        //Popup menu
        jpProfiles = new JPopupMenu();
        jpProfiles.add(jmiProfileModify = new JRMenuItem("MenuItem.Profiles.Modify", "Plus.jpg"));
        jpProfiles.add(jmiProfileDelete = new JRMenuItem("MenuItem.Profiles.Delete", "Minus.jpg"));
        jpProfiles.addSeparator();
        jpProfiles.add(jmiProfileNew = new JRMenuItem("MenuItem.Profiles.New", "TreeProfile.jpg"));
        jpProfiles.add(jmiProfileNewUser = new JRMenuItem("MenuItem.Profiles.NewUser", "TreeProfiles.jpg"));

        //Popup input
        jpProfilesInput = new JPopupMenu();
        JPanel pnPopupInput = new JPanel();
        tfProfilesInput = new JTextField(10);
        pnPopupInput.add(tfProfilesInput);
        jpProfilesInput.add(pnPopupInput);

        //Add panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnProfilesLeft, pnProfilesRight);
        splitPane.setOneTouchExpandable(true);
        pnProfiles.add(splitPane, "Center");

        pnExport.add(pnDirectory, "North");
        pnExport.add(pnExportInfo, "Center");

        jriProfiles.setLayout(new BorderLayout());
        jriProfiles.add(pnProfiles, "West");
        jriProfiles.add(pnExport, "Center");

        //Add actions
        jbProfileAssembly.addActionListener(this);
        jbProfileAssembly.setActionCommand("profileassembly");
        jbProfileExport.addActionListener(this);
        jbProfileExport.setActionCommand("profileexport");
        jbExportDirectory.addActionListener(this);
        jbExportDirectory.setActionCommand("profileopendirectory");
        jmiProfileDelete.addActionListener(this);
        jmiProfileDelete.setActionCommand("profiledelete");
        jmiProfileModify.addActionListener(this);
        jmiProfileModify.setActionCommand("profilemodifyshow");
        jmiProfileNewUser.addActionListener(this);
        jmiProfileNewUser.setActionCommand("profileaddnewusershow");
        jmiProfileNew.addActionListener(this);
        jmiProfileNew.setActionCommand("profileaddnewprofileshow");
        tfProfilesInput.addActionListener(this);

        profileTree.addMouseListener(this);

    }

    public void createInternalFrame_Currency() {
        jriCurrency = new JRInternalFrame("Currency", "InternalFrame.Currency.Title", mainDeskTop);
        JPanel pnCurrency = new JPanel();
        pnCurrency.setLayout(new BorderLayout());
        pnCurrency.setMinimumSize(new Dimension(300, 30));

        pnCurrency.add(jcbCurrency = new JComboBox(), "West");
        pnCurrency.add(new JLabel("   "));
        pnCurrency.add(jbCurrencySave = new JButton(new ImageIcon(swc.iconPath + "Save.jpg")), "East");

        jbCurrencySave.setPreferredSize(new Dimension(28, 28));
        jbCurrencySave.setToolTipText(propertyUtil.getLangText("InternalFrame.Currency.Save.TooltipText"));

        jriCurrency.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriCurrency.add(pnCurrency, new CellConstraints().xy(1, 1));

        for (int i = 0; i < swc.currencies.length; i++) {
            jcbCurrency.addItem(swc.currencies[i]);
        }

        jbCurrencySave.addActionListener(this);
        jbCurrencySave.setActionCommand("savecurrency");
    }

    public void createInternalFrame_RegisterInfo() {
        jriRegisterInfo = new JRInternalFrame("RegisterInfo", "InternalFrame.RegisterInfo.Title", mainDeskTop);
        JPanel pnRegisterInfo = new JPanel();

        pnRegisterInfo.add(jtpRegisterInfo = new JTextPane(), "Center");
        pnRegisterInfo.add(new JScrollPane(jtpRegisterInfo));
        jtpRegisterInfo.setEditable(false);
        jtpRegisterInfo.setFont(swc.fontRegisterInfo);
        jtpRegisterInfo.setPreferredSize(new Dimension(350, 220));

        jriRegisterInfo.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriRegisterInfo.add(pnRegisterInfo, new CellConstraints().xy(1, 1));
    }

    public void createInternalFrame_About() {
        jriAbout = new JRInternalFrame("About", "InternalFrame.About.Title", mainDeskTop);
        JPanel pnAbout = new JPanel();

        JTextPane jtAbout;
        pnAbout.add(jtAbout = new JTextPane(), "Center");
        pnAbout.add(new JScrollPane(jtAbout));

        jtAbout.setPreferredSize(new Dimension(350, 220));

        StyledDocument doc = jtAbout.getStyledDocument();

        //  Set alignment to be centered for all paragraphs
        MutableAttributeSet standard = new SimpleAttributeSet();
        StyleConstants.setAlignment(standard, StyleConstants.ALIGN_CENTER);

        StyleConstants.setBold(standard, true);
        StyleConstants.setItalic(standard, true);
        StyleConstants.setFontSize(standard, 14);
        StyleConstants.setFontFamily(standard, "Arial");
        StyleConstants.setForeground(standard, Color.BLACK);

        doc.setParagraphAttributes(0, 0, standard, true);

        StringBuffer content = new StringBuffer("");
        content.append("Roto Simplex\n");
        content.append("Version: " + swc.version + "\n\n");
        content.append("This program was developed by\n");
        content.append("Viktor Kadar\n");
        content.append("If you have any problems please \n");
        content.append("send your email to\n");
        content.append("email\n");

        jtAbout.setText(content.toString());

        jriAbout.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriAbout.add(pnAbout, new CellConstraints().xy(1, 1));
    }

    public void createInternalFrame_Register() {
        jriRegister = new JRInternalFrame("Register", "InternalFrame.Register.Title", mainDeskTop);
        JPanel pnRegister = new JPanel();
        JPanel pnLeft = new JPanel();
        JPanel pnRight = new JPanel();
        JPanel pnRightTop = new JPanel();
        JPanel pnRightCenter = new JPanel();
        JPanel pnRightBottom = new JPanel();
        JPanel pnKey = new JPanel();
        JPanel pnCode = new JPanel();

        pnRegister.setLayout(new BorderLayout());
        pnLeft.setLayout(new GridLayout(11, 1));
        pnRight.setLayout(new BorderLayout());
        pnRightTop.setLayout(new GridLayout(3, 1));
        pnRightBottom.setLayout(new GridLayout(6, 2));
        pnRightCenter.setLayout(new BorderLayout());
        pnKey.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnCode.setLayout(new FlowLayout(FlowLayout.CENTER));

        pnLeft.add(jbRegisterSaveForm =
                   new JButton(propertyUtil.getLangText("InternalFrame.Register.SaveForm"),
                               new ImageIcon(swc.iconPath + "Save.jpg")));
        pnLeft.add(jbRegisterOpenForm =
                   new JButton(propertyUtil.getLangText("InternalFrame.Register.OpenForm"),
                               new ImageIcon(swc.iconPath + "Open.jpg")));
        pnLeft.add(new JLabel());
        pnLeft.add(new JLabel());
        pnLeft.add(new JLabel());
        pnLeft.add(jbRegisterDemo =
                   new JButton(propertyUtil.getLangText("InternalFrame.Register.Demo"),
                               new ImageIcon(swc.iconPath + "Demo.jpg")));
        pnLeft.add(jbRegisterNewKey =
                   new JButton(propertyUtil.getLangText("InternalFrame.Register.NewKey"),
                               new ImageIcon(swc.iconPath + "NewKey.jpg")));
        pnLeft.add(jbRegister =
                   new JButton(propertyUtil.getLangText("InternalFrame.Register.Registration"),
                               new ImageIcon(swc.iconPath + "Registration.jpg")));
        pnLeft.add(new JLabel());
        pnLeft.add(new JLabel());
        pnLeft.add(jbRegisterExit =
                   new JButton(propertyUtil.getLangText("InternalFrame.Register.Exit"),
                               new ImageIcon(swc.iconPath + "Exit.jpg")));

        Dimension dimension = new Dimension(200, 28);
        Insets insets = new Insets(0, 0, 0, 10);
        jbRegisterSaveForm.setHorizontalAlignment(JButton.LEFT);
        jbRegisterSaveForm.setMargin(insets);
        jbRegisterOpenForm.setHorizontalAlignment(JButton.LEFT);
        jbRegisterOpenForm.setMargin(insets);
        jbRegisterDemo.setHorizontalAlignment(JButton.LEFT);
        jbRegisterDemo.setMargin(insets);
        jbRegisterNewKey.setHorizontalAlignment(JButton.LEFT);
        jbRegisterNewKey.setMargin(insets);
        jbRegister.setHorizontalAlignment(JButton.LEFT);
        jbRegister.setMargin(insets);
        jbRegisterExit.setHorizontalAlignment(JButton.LEFT);
        jbRegisterExit.setMargin(insets);

        JTextField textFieldEmail = new JTextField(propertyUtil.getLangText("InternalFrame.Register.Email"), 35);
        JTextField textFieldTel = new JTextField(propertyUtil.getLangText("InternalFrame.Register.Tel"), 35);
        JTextField textFieldFax = new JTextField(propertyUtil.getLangText("InternalFrame.Register.Fax"), 35);

        textFieldEmail.setEditable(false);
        textFieldTel.setEditable(false);
        textFieldFax.setEditable(false);

        pnRightTop.add(textFieldEmail);
        pnRightTop.add(textFieldTel);
        pnRightTop.add(textFieldFax);


        pnKey.add(tfKey1 = new JTextField(5));
        pnKey.add(new JLabel("-"));
        pnKey.add(tfKey2 = new JTextField(5));
        pnKey.add(new JLabel("-"));
        pnKey.add(tfKey3 = new JTextField(5));

        tfKey1.setEditable(false);
        tfKey2.setEditable(false);
        tfKey3.setEditable(false);
        tfKey1.setFont(swc.fontKey);
        tfKey2.setFont(swc.fontKey);
        tfKey3.setFont(swc.fontKey);

        pnCode.add(tfCode1 = new JTextField(5));
        pnCode.add(new JLabel("-"));
        pnCode.add(tfCode2 = new JTextField(5));
        pnCode.add(new JLabel("-"));
        pnCode.add(tfCode3 = new JTextField(5));

        tfCode1.setFont(swc.fontKey);
        tfCode2.setFont(swc.fontKey);
        tfCode3.setFont(swc.fontKey);
        tfCode1.setBorder(BorderFactory.createLoweredBevelBorder());
        tfCode2.setBorder(BorderFactory.createLoweredBevelBorder());
        tfCode3.setBorder(BorderFactory.createLoweredBevelBorder());

        pnRightBottom.add(tfRegCompanyTitle =
                          new JTextField(propertyUtil.getLangText("InternalFrame.Register.Form.CompanyName") + ":"));
        pnRightBottom.add(tfRegCompany = new JTextField(""));
        pnRightBottom.add(tfRegAddressTitle =
                          new JTextField(propertyUtil.getLangText("InternalFrame.Register.Form.Address") + ":"));
        pnRightBottom.add(tfRegAddress = new JTextField(""));
        pnRightBottom.add(tfRegOperatorTitle =
                          new JTextField(propertyUtil.getLangText("InternalFrame.Register.Form.Operator") + ":"));
        pnRightBottom.add(tfRegOperator = new JTextField(""));
        pnRightBottom.add(tfRegEmailTitle =
                          new JTextField(propertyUtil.getLangText("InternalFrame.Register.Form.Email") + ":"));
        pnRightBottom.add(tfRegEmail = new JTextField(""));
        pnRightBottom.add(tfRegSalesRepTitle =
                          new JTextField(propertyUtil.getLangText("InternalFrame.Register.Form.SalesRep") + ":"));
        pnRightBottom.add(tfRegSalesRep = new JTextField(""));
        pnRightBottom.add(tfRegCommentTitle =
                          new JTextField(propertyUtil.getLangText("InternalFrame.Register.Form.Comment") + ":"));
        pnRightBottom.add(tfRegComment = new JTextField(""));

        tfRegCompanyTitle.setEditable(false);
        tfRegAddressTitle.setEditable(false);
        tfRegOperatorTitle.setEditable(false);
        tfRegEmailTitle.setEditable(false);
        tfRegSalesRepTitle.setEditable(false);
        tfRegCommentTitle.setEditable(false);

        pnRight.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Register.Form")));
        pnRightTop.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Register.RegistrationAddress")));
        pnRightBottom.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Register.CompanyInfo")));
        pnKey.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Register.Key")));
        pnCode.setBorder(BorderFactory.createTitledBorder(propertyUtil.getLangText("InternalFrame.Register.Code")));

        pnRight.add(pnRightTop, "North");
        pnRight.add(pnRightCenter, "Center");
        pnRight.add(pnRightBottom, "South");

        pnRightCenter.add(pnKey, "North");
        pnRightCenter.add(pnCode, "South");

        pnRegister.add(pnLeft, "West");
        pnRegister.add(pnRight, "East");

        jriRegister.add(pnRegister);
        jriRegister.setLayout(new FormLayout("center:default:grow", "default:grow"));
        jriRegister.add(pnRegister, new CellConstraints().xy(1, 1));

        //Add actions
        jbRegisterSaveForm.addActionListener(this);
        jbRegisterSaveForm.setActionCommand("registersaveform");
        jbRegisterOpenForm.addActionListener(this);
        jbRegisterOpenForm.setActionCommand("registeropenform");
        jbRegisterNewKey.addActionListener(this);
        jbRegisterNewKey.setActionCommand("registernewkey");
        jbRegister.addActionListener(this);
        jbRegister.setActionCommand("registerprogram");
        jbRegisterExit.addActionListener(this);
        jbRegisterExit.setActionCommand("programexit");
        jbRegisterDemo.addActionListener(this);
        jbRegisterDemo.setActionCommand("registerprogramdemo");
    }

    public void internalFrameActivated(InternalFrameEvent arg0) {

    }

    public void internalFrameClosed(InternalFrameEvent arg0) {


    }

    public void internalFrameClosing(InternalFrameEvent arg0) {


    }

    public void internalFrameDeactivated(InternalFrameEvent arg0) {

    }

    public void internalFrameDeiconified(InternalFrameEvent arg0) {

    }

    public void internalFrameIconified(InternalFrameEvent arg0) {

    }

    public void internalFrameOpened(InternalFrameEvent arg0) {

    }

    public void actionPerformed(ActionEvent evt) {
        Actions.getInstance().doAction(evt.getActionCommand());
    }

    public void mouseClicked(MouseEvent me) {
        Actions.getInstance().doMouseClickAction(me);
    }

    public void valueChanged(ListSelectionEvent lse) {
        Actions.getInstance().doListAction(lse);
    }

    public void mouseEntered(MouseEvent me) {
        Actions.getInstance().doMouseEnteredAction(me);
    }

    public void mouseExited(MouseEvent me) {
        Actions.getInstance().doMouseExitedAction(me);
    }

    public void mousePressed(MouseEvent me) {
        Actions.getInstance().doMousePressedAction(me);
    }

    public void mouseReleased(MouseEvent me) {
        Actions.getInstance().doMouseReleasedAction(me);
    }

    public void dragDropEnd(DragSourceDropEvent arg0) {

    }

    public void dragEnter(DragSourceDragEvent dsde) {

    }

    public void dragExit(DragSourceEvent dse) {

    }

    public void dragOver(DragSourceDragEvent dsde) {

    }

    public void dropActionChanged(DragSourceDragEvent dsde) {

    }

    public void dragGestureRecognized(DragGestureEvent dge) {
        if (dge.getComponent()
               .getName()
               .toLowerCase()
               .equalsIgnoreCase("databaselist")) {
            transferable = new StringSelection(jlDatabases.getSelectedValue().toString());
            dge.getDragSource().startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
        } else if (dge.getComponent()
                      .getName()
                      .toLowerCase()
                      .equalsIgnoreCase("databasefilterlist") && dlDatabaseFilter.size() > 0) {
            transferable = new StringSelection(jlDatabaseFilter.getSelectedValue().toString());
            dge.getDragSource().startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
        Actions.getInstance().doKeyAction(e);
    }

    public void keyTyped(KeyEvent e) {
    }

    class MouseMotionHandler extends MouseMotionAdapter {

        /* public void mouseMoved(MouseEvent e){
			 if (e.getSource().getClass().equals(new JLabel().getClass())){
					JLabel selectedLabel = (JLabel) e.getSource();
					selectedLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				}
		 }  */

        public void mouseDragged(MouseEvent me) {
            Actions.getInstance().doMouseDraggedAction(me);
        }

    }
}
