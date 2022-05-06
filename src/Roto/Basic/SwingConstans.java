package Roto.Basic;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class SwingConstans {
	private static SwingConstans instance = null;
	// Fix variables (never change)
	public final String iconPath = "Bin/etc/Images/Icon/";
	public final String demoUserPath = "Bin/etc/Images/Icon/DemoUser.jpg";
	public final String flagPath = "Bin/etc/Images/Flags/";
	public final String viewerPath = "Bin/etc/Images/Viewer/";
	public final String profilePath = "Bin/Properties/Profiles/";
	public final String basicPath = "Bin/Properties/Basic/";
	public final String dataBasePath = "Dir/Database";
	public final String priceListPath = "Dir/Pricelist";
	public final String setupPath = "Setup/";
	public final String exelListPath = "Dir/Excel";
	public final String listPath = "Dir/List";
	public final String stockListPath = "Dir/StockList";
	public final String defaultLicencePath = "Bin/Properties/Basic/Program.properties";
	public final String MaterialGroupPath = "Dir/Xml/MaterialGroups.xml";
	public final String RebatePath = "Dir/Rebate/";
	public final String registrationPath = "Dir/Registration/";
	public final String windowSystemsPath = "Dir/Xml/WindowSystems.xml";
	public final String xmlPath = "Dir/Xml/";
	public final String [] closableBarArray = {"25","30","35","40","45","50"};
	public final String [] selectionBarArray = {"ko","ksr","m"};
	public final String [] ntArray = {"n3","n6","nt"};
	public final String [] nxArray = {"x3","x6","xe","x"};
	public final String [] colorArray = {"nsz","f","b","u","ne","a","ma","br","t","ob","szb","kbr","sbr","kf","fe"};
	public final String [] colorTitles = {"None","White","Brown","NewSilver","NatureSilver","Gold",
										  "MatGold","Brass","Titan","OliveBrown","GreyBrown","MiddleBrass","DarkBrass","CreamWhite","JetBlack"};
	public final String [] directionArray = {"L","R","k","g"};
	public final String [] directionTitles = {"Left","Right","Handle","HandleGarniture"};
	public final String [] windowTypeArray = {"MDK","MDF","MDS","MKIPP","MDK2","MDF2","MDS2","MFREI","MDSMDK2","MDSMDF2"};
	public final String [] windowSystemArray = {"Class 1","Class 2","Class 3","Class 4","Class 5","Class 6","Class 7","Class 8"};
	public final String [] headerDataOrder = {"Addressee","Customer","Subject.Order","Deadline","Email","PricelistDate","Total","Date"};
	public final String [] headerDataOffer = {"CompanyName","Addressee","Subject.Offer","Deadline","Email","PricelistDate","Total","Date"};
	public final String [] propertyColumnNames = {"Count","SAP","Direction","Text","MDK","MDF","MDS","MKIPP","Lock","Color","MSSTK","MSISSTK",
												  "MFFBV","MFFBB","MFFHV","MFFHB","MSTART","MSISTART","MDK2","MDF2","MDS2","MFREI"};
	public final String [] currencies = {"HUF","EUR","USD","BGL","CAD","CHF","CZK","GBP","JPY","PLZ","RON","SIT","SKK","TRY","UAH","RUB","HRK","RSD","BGN"};
	public final String dataBaseHeaer = "NSAP     MARTIKEL     IRMTEXT                                        MDK     MDF     MDS     "+
										"MKIPP   Zďż˝Szďż˝MSSTK   MSISSTK MFFBV   MFFBB   MFFHV   MFFHB   MSTART      MSISTART    ANZEIGENMDK2    MDF2    MDS2    MFREIHUF";
	public final String excelDataBaseHeaer = "N|SAP|MARTIKEL|IR|MTEXT|MDK|MDF|MDS|MKIPP|Zďż˝|Szďż˝|MSSTK|MSISSTK|"+
											 "MFFBV|MFFBB|MFFHV|MFFHB|MSTART|MSISTART|ANZEIGEN|MDK2|MDF2|MDS2|MFREI|HUF";

	public final NumberFormat formatter =  new DecimalFormat("#0.00");
	public final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
	public final Integer tabLogin = 0;
	public final Integer tabResize = 1;
	public final Integer tabCollection = 2;
	public final Integer tabMenus = 3;
	public final Color colorExcelHeader = new Color(236,233,216);
	public final Color colorGlass = new Color(211,229,246);
	public final Color colorShadow = new Color(141,141,141);
	public final Color colorCanvasMenu = new Color(219,233,247);
	public final Color colorViewerMessage = new Color(255,255,225);
	public final Color colorError = new Color(255,153,153);
	public final Font fontExcelHeader = new Font("Times",java.awt.Font.BOLD, 12);
	public final Font fontTree = new Font("Times",java.awt.Font.BOLD, 12);
	public final Font fontCanvasResize = new Font("Times",java.awt.Font.BOLD, 16);
	public final Font fontCanvas = new Font("Times",java.awt.Font.BOLD, 11);
	public final Font fontCanvasSmall = new Font("Times",java.awt.Font.PLAIN, 10);
	public final Font fontPrint = new Font("Courier New",Font.PLAIN,8);
	public final Font fontKey = new Font("Times",Font.BOLD,13);
	public final Font fontExcelComment = new Font("Courier New",Font.PLAIN,14);
	public final Font fontRegisterInfo = new Font("Times",Font.BOLD,12);
	public final Font fontExportInfo = new Font("Times",Font.BOLD,12);
	public final int menuToolbarHeight = 30 * 10;
	public final int menuToolbarWidth = 130;
	public final String LIST_DETAILED = "detailed";
	public final String LIST_SUMMARIZED = "summarized";
	public final String LIST_NAVISON = "navison";
	public final String LIST_PROJECT = "project";
	public final String LIST_GARNITURE = "garniture";
	public final String LIST_ACTION_PRINT = "print";
	public final String LIST_ACTION_EXCEL = "excel";
	public final String LIST_ACTION_PDF = "pdf";
	public final String HEADER_ORDER = "order";
	public final String HEADER_OFFER = "offer";
	public final String LOAD_COLLECTION = "Collection";
	public final String LOAD_EDIT = "Edit";
	public final String LOAD_STOCK = "Stock";
	public final String REGISTRY_DEMO_USER = "DemoUserEnabled";
	public final String[] flags = {"Hungary","UnitedKingdom","Germany","Turkey","Romania","Bulgaria","CzechRepublic",
									"Slovakia","Slovenia","Serbia","Spain","Ukraine","Russia","Croatia","Poland","Other"};
	public final String[] flagTitles = {"Hungarian","English","German","Turkish","Romanian","Bulgarian","Czech",
									"Slovakian","Slovenian","Serbian","Spanish","Ukrainian","Russian","Croatian","Polish","Other"};
	public final String[] langCodes = {"hu","en","ge","tu","ro","bu","ez","lo","si","sr","es","ua","ru","cr","pl","od"};
	public final int printContentColumnLengthsForStockList[] = {8,8,42,15};
	public final int printContentColumnLengthsForList[] = {7,15,38,14,14,14};
	public final int printHeaderColumnLengthsForList[] = {25,90};
	public final int rebateTitleInfoLength = 30;
	public final String version = " 1.2.9 ";
	public final String nodeRabateAdmin = "Admin";
	public final String passWordAdmin = "19560124";
	public final String passWordOperator = "l9461d4025";
	public HashMap<String, String> localisedColors = new HashMap<String, String>();
	
    //Dynamic variables (change while the program is running)
	private String selectedColorSign = "nsz";
	private int selectedLanguageLeft = 0;
	private int selectedLanguageRight = 1;
	private String selectedSelectionBarSign = selectionBarArray[0];
	private String selectedNTSign = ntArray[0];
	private String selectedNXSign = nxArray[0];

	public static SwingConstans getInstance() {
		if (instance == null) {
			instance = new SwingConstans();
		}
		return instance;
	}
	
	public int getSelectedLanguageLeft() {
		return selectedLanguageLeft;
	}

	public void setSelectedLanguageLeft(int selectedLanguageLeft) {
		this.selectedLanguageLeft = selectedLanguageLeft;
	}

	public int getSelectedLanguageRight() {
		return selectedLanguageRight;
	}

	public void setSelectedLanguageRight(int selectedLanguageRight) {
		this.selectedLanguageRight = selectedLanguageRight;
	}

	public String getSelectedColorSign() {
		return selectedColorSign;
	}

	public void setSelectedColorSign(String selectedColorSign) {
		this.selectedColorSign = selectedColorSign;
	}
	
	public HashMap<String, String> getLocalisedColors() {
	     return localisedColors;
	}

	public String getSelectedSelectionBarSign() {
		return selectedSelectionBarSign;
	}

	public void setSelectedSelectionBarSign(String selectedSelectionBarSign) {
		this.selectedSelectionBarSign = selectedSelectionBarSign;
	}

	public String getSelectedNTSign() {
		return selectedNTSign;
	}

	public void setSelectedNTSign(String selectedNTSign) {
		this.selectedNTSign = selectedNTSign;
	}

	public String getSelectedNXSign() {
		return selectedNXSign;
	}

	public void setSelectedNXSign(String selectedNXSign) {
		this.selectedNXSign = selectedNXSign;
	}

	
}
