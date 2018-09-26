package constants;

import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;

/**
 * the class Constants
 */
public class Constants {

	// miscellaneous
	// public static final Charset CHARSET = Charset.forName("Cp1252");// this is
	// ANSI
	public final static String AUTHORIZATION = "Authorization";
	public static final Charset CHARSET = Charset.forName("ISO-8859-1");
	public static final String BIN = "Bin";
	public static final int LENGTH_OF_BARCODE = 13;
	public static final String NA = "n/a";
	public static final String ID = "ID";
	public static final String INFORMATION = "Information";
	public static final String ERROR = "Error";
	public static final String CUSTOMER_SAVED_MESSAGE = "<html>Kunde <font color=\"green\"> %s </font>, <font color=\"green\">%s</font> wurde erfolgreich gespeichert.</html>";
	public static final String CUSTOMER_DELETED_MESSAGE = "<html>Kunde <font color=\"green\"> %s </font>, <font color=\"green\">%s</font> und zugehörige <font color=\"red\"> %s </font> Verträge wurden erfolgreich entfernt.</html>";
	public static final String CUSTOMER_DELETED_MESSAGE_1 = "Kunde <font color=\"green\">%s</font>, <font color=\"green\">%s</font> und  <font color=\"red\"> %s </font> Vertrag wurden erfolgreich entfernt.";
	public static final String CONTRACT_DELETED_MESSAGE = "<html>Vertrag <font color = \"red\">%s</font> wurde erfolgreich entfernt.</html>";
	public static final String SELLER_SAVED_MESSAGE = "<html>Verkäufer <font color=\"green\">%s</font>, <font color=\"green\">%s</font> wurde erfolgreich gespeichert.</html>";
	public static final String SELLER_IS_MISSING = "Verkäufer ist nicht gesetzt.";
	public static final String SELLER_UPDATED_MESSAGE = "<html>Verkäufer <font color=\"green\">%s</font>, <font color=\"green\">%s</font> wurde erfolgreich aktualisiert.</html>";
	public static final String FRAMENUMBER_IS_MISSING = "Rahmennummer ist nicht gesetzt.";
	public static final String STATUS_IS_MISSING = "Status ist nicht gesetzt.";
	public static final String INSPECTION_SETUP_INCORRECT = "Inspection Jahr  < %s > mit Mechaniker < %s > nicht durchführbar.";
	public static final String APPOINTMENT_DATE_INCORRECT = "Datum %s : %s liegt in der Vergangenheit.";
	public static final String FRAME_HEIGHT_INCORRECT = "Rahmenhöhe nicht gesetzt";
	public static final String CONTRACT_SAVED_MESSAGE = "<html>Vertrag <font color = \"red\"> %s </font> erfolgreich gespeichert.</html>";
	public static final String BIKE_DATA_SAVED_MESSAGE = "Fahrrad-Daten wurden erfolgreich gespeichert.";
	public static final String BIKE_DATA_UPDATED_MESSAGE = "Fahrrad-Daten wurden erfolgreich aktualisiert.";
	public static final String NO_CUSTOMER_SELECTED_MESSAGE = "Es wurde noch kein Kunde gewählt.";
	public static final String CUSTOMER_INVALID = "Bitte Kunden überprüfen";
	public static final String CUSTOMER_UPDATED_MESSAGE = "<html>Kunde <font color=\"green\"> %s </font>, <font color=\"green\">%s</font> wurde erfolgreich aktualisiert.";
	public static final String ADMINISTRATION_UPDATED_MESSAGE = "Administration wurde erfolgreich aktualisiert.";
	public static final String AGE_OF_BIKE_IS_MISSING = "Alter Fahrrad nicht gesetzt.";
	public static final String PROPERTY_OF_BIKE_IS_MISSING = "Typ Fahrrad nicht gesetzt.";
	public static final String BARCODE_MISSING = "<html>Barcode %s nicht in Warenwirtschaft enthalten. <br> Editierbare Zeile anfügen?</html>";
	public static final String TIMEPOINT_INCORRECT = "Uhrzeit nicht gesetzt";
	public static final String LAST_MODIFIED_DATA = "LETZTE_AENDERUNG";
	public static final String CONTRACT_UPDATED_MESSAGE = "<html>Vertrag <font color=\"red\"> %s </font> wurde erfolgreich aktualisiert.</html>";
	public static final String BIKE_DATA_MUST_BE_SAVED = "<html>Fahrad-Daten müssen zunächst abgespeichert werden! <br> Anschließend kann die Form geschlossen werden</html>";
	public static final String ERP_CSV = "erp.csv";
	public static final String ERP_ONLINESHOP = "ERP";
	public static final String SELLPOSTIONS_NOT_VALID = "<html>Verkaufspositionen sollten überprüft werden! <br> Trotzdem fortfahren? <br> Falls ja, <font color = \"green\">aktualiseren </font> Sie danach erneut!</html>";
	public static final String CONTRACT_NOT_VALID = "Vertrag muss überprüft werden!";
	public static final String REMOVE_SELLPOSITION = "Verkaufsposition Anzahl = %s; ean = %s; Artikel = %s; Hersteller = %s; uvp = %s; Hauspreis = %s; entfernen?";
	public static final String REMOVE_SELLPOSITION_ADVARICS = "Verkaufsposition Anzahl = %s; ean = %s; Artikel = %s; Hersteller = %s; uvp = %s; Hauspreis = %s; entfernen?";
	public static final String CONTRACT_HAS_TO_BE_SAVED = "Vertrag muß zunächst abgespeichert werden!";
	public static final String BIKEDATA_HAS_TO_BE_SAVED = "Fahrrad-Daten müssen zunächst bearbeitet werden!";
	public static final String FALSE = "false";
	public static final String TRUE = "true";
	public static final String STATISTCS = "Statistik";
	public static final String SALES_STATISTCS = "Umsatzstatistik";
	public static final String MONTHS = "Monate";
	public static final String COUNT_OF_ITEMS = "Anzahl";
	public static final String SUMS_OF_CONTRACTS = " Gesamtsumme (in "
			+ Currency.getInstance(Locale.GERMANY).getSymbol() + ")";
	public static final String CONTRACTED = "Abgeschlossene Verträge";
	public static final String NEW_CUSTOMERS = "Neukunden";

	public static final String ON_DELETE_CASCADE = "ON DELETE CASCADE";
	public static final String ON_UPDATE_CASCADE = "ON UPDATE CASCADE";
	public static final String NULL = "null";
	public static final String CSV_IMPORT_HEADER = "Kunden importieren";
	public static final String CSV_EXPORT = "csv export";
	public static final String CUSTOMERS = "Kunden";
	public static final String CUSTOMERS_CSV = "Kunden.csv";
	public static final String SELECT_LOCATION_TO_SAVE = "Speicherort wählen";
	public static final String SELECT_LOCATION_TO_IMPORT = "Selektiere Pfad zu Importverzeichnis";
	public static final String EXPORT = "Export";
	public static final String QUESTION_TO_OVERWRITE_EXISTING_DATA = "Das Verzeichnis existiert bereits. Sollen die Daten überschrieben werden?";
	public static final String IMPORT_DIRECTORY_NOT_CORRECT = "Das gewählte Verzeichnis ist nicht mit dem exportierten Verzeichnis kompatibel";
	public static final String IMPORT_SUCCESS = "Daten wurden erfolgreich importiert.";
	public static final String MAIN_MENU = "Hauptmenu";
	public static final String BikeContract = "BikeContract";
	public static final String DOCUMENT_IS_OPEN = "Das Dokument wird verwendet, schließen Sie das Dokument und erstellen es erneut.";
	public static final String FONT = "Font";
	public static final String ASSETS = "Assets";
	public static final String IMAGE = "Image";
	public static final String EMPTY_STRING = "";
	public static final String EMPTY_SPACE = " ";
	public static final String COMMA = ",";
	public static final String SEMICOLON = ";";
	public static final String MINUS = "-";
	public static final String COLON = ":";
	public static final String PIPE = "|";
	public static final String PERCENT = "%";
	public static final String QUESTIONMARK = "?";
	public static final String PROPERTIES = "Eigenschaften";
	public static final String STATUS = "Status";
	public static final String STATE = "Zustand";
	public static final String TYPE = "Art";
	public static final String NAME_OF_IMAGE = "NAME_IMG";
	public static final float SCALE_Of_SELLER_PHOTO = 20f;
	public static final String EXISTING_SELLER_WAS_EDITED = "<html>Verkäufer %s %s wurde geändert. <br> Die bearbeiteten Daten werden nicht gespeichert.</html>";
	public static final String EXISTING_CONTRACT_WAS_EDITED = "<html>Vertrag %s wurde geändert. <br> Die bearbeiteten Daten werden nicht gespeichert.</html>";
	public static final String CUSTOMER_WAS_EDITED = "<html>Kunde <font color=\"green\"> %s, %s </font>wurde geändert. <br> Die bearbeiteten Daten werden nicht gespeichert.</html>";
	public static final String EXISTING_BIKEDATA_WAS_EDITED = "<html>Fahraddaten zu Vertrag %s wurden geändert. <br> Die bearbeiteten Daten werden nicht gespeichert.</html>";
	public static final String ADMINISTRATION_WAS_EDITED = "<html>Administration wurde geändert. <br> Die bearbeiteten Daten werden nicht gespeichert.</html>";
	public static final String ADMINISTRATION_WAS_EDITED_RESTART_NOW_QUESTION = "<html>Die Änderung der Administration wird erst bei Neustart ersichtlich. <br> Jetzt beenden ?</html>";
	public static final String APPLY_NEW_CUSTOMER_QUESTION = "<html>Möglicherweise ist Kunde <font color=\"green\"> %s </font>, <font color=\"green\"> %s </font> nicht angelegt.<br> Soll der Kunde neu angelegt werden ?</html>";
	public static final String PATH_TO_ACCESS_BIKECONTRACT_RELEASE = "smb://raspberrypi/access/";
	public static final Object PASSWORD_FORGOTTEN_QUESTION = "Haben Sie Ihr Passwort vergessen?";
	public static final String ENCRYPTED_PASSWORD_FOR_CLIENT = "<html>Wenden Sie sich bitte mit dem verschlüsselten Password <br><font color=\"red\"> %s </font><br> an Ihren Systemadministrator! <br> Es befindet sich in der Zwischenablage und kann <br> in eine Textdatei eingefügt werden.</html>";

	public static final String NON_EXISTING_DATA = "Die bearbeiteten Daten werden nicht gespeichert.";
	public static final String EXIT_SYSTEM = "Soll die Anwendung beendet werden?";

	public static final String AWT_DESKTOP_NOT_SUPPORTED = "Awt Desktop is not supported!";

	/** location of database */
	private static final Currency currency = Currency.getInstance(Locale.getDefault());
	public static final String symbol = currency.getSymbol();

	// Constants for start frame
	public static final int WIDTH_MAINFRAME = 625;
	public static final int HEIGHT_MAINFRAME = 50;
	public static final int HEIGHT_OF_TABLE = 450;
	public static final int MINIMAL_WIDTH_MAINFRAME = 100;
	public static final int MINIMAL_HEIGHT_MAINFRAME = 100;

	// Constants for shore appointments frame

	public static final int HEIGHT_OF_SHORE_FRAME = 430;
	public static final int WIDTH_OF_SHORE_FRAME = 900;
	public static final int MINIMAL_HEIGHT_OF_SHORE_FRAME = 430;
	public static final int MINIMAL_WIDTH_OF_SHORE_FRAME = 600;

	// Constants for customer frame
	public static final int HEIGHT_OF_CUSTOMER_FRAME = 430;
	public static final int WIDTH_OF_CUSTOMER_FRAME = 900;
	public static final int MINIMAL_HEIGHT_OF_CUSTOMER_FRAME = 430;
	public static final int MINIMAL_WIDTH_OF_CUSTOMER_FRAME = 600;
	public static final int HEIGHT_OF_TABLE_CUSTOMER = 362;

	// settings for customers frame
	public static final int HEIGHT_OF_CUSTOMERS_FRAME = 500;
	public static final int WIDTH_OF_CUSTOMERS_FRAME = 1000;
	public static final int MINIMAL_WIDTH_OF_CUSTOMERS_FRAME = 100;
	public static final int MINIMAL_HEIGHT_OF_CUSTOMERS_FRAME = 100;

	// settings contract frame
	public static final int HEIGHT_OF_TABLE_CONTRACT = 482;
	public static final int MINIMAL_HEIGHT_OF_CONTRACT_FRAME = 452;
	public static final int MINIMAL_WIDTH_OF_CONTRACT_FRAME = 500;
	public static final int HEIGHT_OF_DISPLAY_CONTRACT_FRAME = 552;
	public static final int WIDTH_OF_DISPLAY_CONTRACT_FRAME = 1500;

	// setting for seller frame
	public static final int HEIGHT_OF_SELLER_FRAME = 430;
	public static final int WIDTH_OF_SELLER_FRAME = 600;
	public static final int MINIMAL_HEIGHT_OF_SELLER_FRAME = 430;
	public static final int MINIMAL_WIDTH_OF_SELLER_FRAME = 600;
	public static final int HEIGHT_OF_TABLE_SELLER = 362;

	// Constants for customer frame
	public static final int HEIGHT_OF_ADMINISTRATION_FRAME = 140;
	public static final int WIDTH_OF_ADMINISTRATION_FRAME = 900;
	public static final int MINIMAL_HEIGHT_OF_ADMINISTRATION_FRAME = 140;
	public static final int MINIMAL_WIDTH_OF_ADMINISTRATION_FRAME = 600;
	public static final int HEIGHT_OF_TABLE_ADMINISTRATION = 82;

	// constants for seller frame
	public static final String NAME = "Name";
	public static final String SELECT_IMAGE = "Foto";
	public static final String SELLERNUMBER = "Verkäufer(Nr.)";
	public static final String SELLERNUMBER_DATA = "Verkäufer_NR";
	public static final String DISPLAY_SELLERS = "Verkäufer anzeigen";
	public static final String DISPLAY_SELLERS_TITLE = "Anzeige Verkäufer";
	public static final String SELECT_PHOTO_TO_LOAD = "Lade Foto";
	public static final String SAVE_SELLER = "Verkäufer speichern";
	public static final String UPDATE_SELLER = "Verkäufer aktualisieren";
	public static final String SELLER_ID = "ID_VERKAEUFER";

	// constants for display sellers frame
	public static final String[] COLUMNS_OF_DISPLAY_SELLERS = { Constants.PRENAME, NAME, SELLERNUMBER, Constants.MOBILE,
			Constants.NAME_OF_IMAGE };
	public static final String BACK_TO_SELLER = "Zurück zu Verkäufer";

	// Constants for table of contract
	/** size of headers */
	public static final int TABLE_HEADER_WIDTH = 100;
	public static final int TABLE_HEADER_HEIGHT = 40;
	public static final int ROW_HEIGHT = 40;

	// Constants for start frame
	public static final String NEW_CUSTOMER = "Neuer Kunde";
	public static final String SHOW_CUSTOMERS = "<html>Kunden </br> anzeigen</html>";
	public static final String EDIT_SELLERS = "<html>Verkäufer </br> bearbeiten</html>";
	public static final String CSV_IMPORT = "<html>Kunden </br> importieren</html>";
	public static final String SHOW_APPOINTMENTS = "<html>Termine </br> anzeigen</html>";
	public static final String ADMINISTRATION = "Administration";

	// Constants for contract
	/** column headers for the table - onlineshop */
	public final static String[] COLUMNS_OF_CONTRACT_ONLINESHOP = { Constants.QUANTITY, Constants.EAN,
			Constants.ARTICLE, Constants.PRODUCER, Constants.EMPTY_STRING, Constants.UVP + "/" + symbol,
			Constants.HOUSE_PRICE + "/" + symbol };

	/** column headers for the table - all */
	public final static String[] COLUMNS_OF_CONTRACT_ADVARICS = { Constants.QUANTITY, Constants.EAN, Constants.ARTICLE,
			Constants.PRODUCER, Constants.EMPTY_STRING, Constants.UVP + "/" + symbol,
			Constants.HOUSE_PRICE + "/" + symbol, Constants.ARTICLE_NUMBER };

	public static final String CONTRACT = "Kaufvertrag";
	public static final String QUANTITY = "Stück";
	public static final String ARTICLE = "Artikel";
	public static final String UVP = "UVP";
	public static final String HOUSE_PRICE = "Hauspreis";
	public static final String SUBTOTAL = "Zwischensumme Ware";

	/** the column item */
	public static final String BIKE = "Fahrrad";
	public static final String PADLOCK = "Schloss";
	public static final String LIGHTING = "Beleuchtung";
	public static final String SPEEDO = "Tacho";
	public static final String PANNIER_RACK = "Gepäckträger";
	public static final String POCKETS = "Taschen";
	public static final String SPLASHGUARD = "Schutzblech";
	public static final String DRINKING_BOTTLE = "Trinkflasche";
	public static final String KICKSTAND = "Ständer";
	public static final String BELL = "Klingel";
	public static final String HELM = "Helm";
	public static final String ERGONOMICS = "Ergonomie";
	public static final String FOOT_WEAR = "Schuhe";
	public static final String TEXTILE = "Textil";

	/** costs */
	public static final String DELIVERY_ASSEMBLY_FEE = "Liefer- / Aufbauschale";
	public static final String SERVICE_COST_GARAGE = "Service-Kosten / Werkstatt";
	public static final String REDEMPTION_DISMANTLED_PARTS_WHEEL = "Rücknahme abgebauter Teile / Rad";
	public static final String DOWN_PAYMENT = "Anzahlung / iz";
	public static final String ESTATE = "NA";
	public static final String TOTAL = "Gesamtbetrag";

	// display customer
	/** constants for display customer */
	public final static String[] COLUMNS_OF_CUSTOMER = { Constants.CUSTOMER, Constants.CUSTOMER_DATA };
	private static final String CUSTOMER_DATA = "Kundendaten";
	public static final String CUSTOMER = "Kunde";
	public static final String CUSTOMER_NAME = "Kundenname";
	public static final String PRENAME = "Vorname";
	public static final String STREET = "Straße";
	public static final String NUMBER = "Hausnummer";
	public static final String POSTAL_CODE = "PLZ";
	public static final String PLACE = "ORT";
	public static final String MOBILE = "Handy";
	public static final String EMAIL = "Email";

	// <display customer>
	/** constants for customer actions */
	public static final String BACK_TO_CUSTOMERS = "Zurück zu Kunden";
	public static final String SAVE_CUSTOMER = "Kunde speichern";
	public static final String CLEAR_VIEW = "Felder leeren";
	public static final String NEW_CONTRACT = "Neuer Vertrag";
	public static final String SHOW_CUSTOMER_CONTRACTS = "Verträge anzeigen";
	public static final String BACK_TO_CONTRACT = "Zurück zum Vertrag";
	public static final String UPDATE_CUSTOMER = "Kunde aktualisieren";
	public static final String DELETE_CUSOMER = "Kunde entfernen";
	// </display customer>

	// <display customers>
	/** Constants for display customers: */
	public static final String[] COLUMNS_OF_DISPLAY_CUSTOMERS = { Constants.CUSTOMER_NAME, Constants.PRENAME,
			Constants.STREET, Constants.NUMBER, Constants.POSTAL_CODE, Constants.PLACE, Constants.MOBILE,
			Constants.EMAIL, Constants.CREATED_AT, Constants.LAST_MODIFIED };
	public static final String DISPLAY_CUSTOMERS = "Anzeige Kunden";

	/** action for display customers */
	public static final String SEARCH_CUSTOMER = "Kunde suchen";
	// </display customers>

	// <display access>
	public static final String LOGIN = "Anmeldung";
	public static final String ENTER_PASSWORT = "Eingabe Passwort";
	public static final String ENTER_NEW_PASSWORT = "Eingabe neues Passwort";
	public static final String REPEAT_PASSWORD = "Wiederholen Sie das Passwort";
	public static final String LOGIN_FAILED = "<html><font color = \"red\">Login fehlgeschlagen!</font> <br> Bitte versuchen Sie es erneut.</html>";
	public static final String CONFIRM_PASSWORD_FAILED = "<html>Bestätigung des Passwortes fehlgeschlagen! <br> Bitte versuchen Sie es erneut.</html>";
	public static final String PASSWORD_NOT_CORRECT = "<html>Das eingegebene Passwort ist falsch.<br> Bitte versuchen Sie es erneut.</html>";
	public static final String PASSWORD_SAVED_MESSAGE = "Passwort erfolgreich gespeichert.";
	// </display access>

	// <display contract>
	/** constants for contract actions */
	public static final String WRITE_CONTRACT = "Vertrag generieren";
	public static final String SAVE_CONTRACT = "Vertrag speichern";
	public static final String BACK_TO_CUSTOMER = "Zurück zu Kunde";
	public static final String UPDATE_CONTRACT = "Vertrag aktualisieren";
	public static final String DELETE_CONTRACT = "Vertrag löschen";
	public static final String BACK_TO_CONTRACTS = "Zurück zu Verträgen";

	/** constants for table contract */
	public static final String DELIVERY_ASSEMBLY_FEE_UVP = "LIEFER_AUFBAUPAUSCHALE_UVP";
	public static final String SERVICE_COST_GARAGE_UVP = "SERVICE_KOSTEN_WERKSTATT_UVP";
	public static final String REDEMPTION_DISMANTLED_PARTS_WHEEL_UVP = "RUECKNAHME_ABGEBAUTER_TEILE_RAD_UVP";
	public static final String DOWN_PAYMENT_UVP = "ANZAHLUNG_IZ_UVP";
	public static final String ESTATE_UVP = "NACHLASS_UVP";
	public static final String TITLE_DISPLAY_CONTRACT = "%s , %s ; Vertrag %s ";

	public static final String DELIVERY_ASSEMBLY_FEE_HOUSEPRICE = "LIEFER_AUFBAUPAUSCHALE_HOUSEPRICE";
	public static final String SERVICE_COST_GARAGE_HOUSEPRICE = "SERVICE_KOSTEN_WERKSTATT_HOUSEPRICE";
	public static final String REDEMPTION_DISMANTLED_PARTS_WHEEL_HOUSEPRICE = "RUECKNAHME_ABGEBAUTER_TEILE_RAD_HOUSEPRICE";
	public static final String DOWN_PAYMENT_HOUSEPRICE = "ANZAHLUNG_IZ_HOUSEPRICE";
	public static final String ESTATE_HOUSEPRICE = "NACHLASS_HOUSEPRICE";
	public static final String NEW_CONTRACT_PERSONALIZED = "<html>Neuer Vertrag: <font color = \"green\"> %s </font>, <font color = \"green\"> %s </font></html>";
	public static final String EXIT = "Beenden";
	public static final String IS_ONLINE_SHOP = "IS_ONLINE_SHOP";
	// </display contract>

	/** constants for the database */
	public static final String BIKE_CONTRACT_DATA = "BIKE_CONTRACT_DATA";
	public static final String CREATED_AT = "angelegt am";
	public static final String CREATED_AT_DATA = "angelegt_am";
	public static final String LAST_MODIFIED = "letzte Änderung";

	// <display contracts>
	/** constants for display contracts */
	public static final String[] COLUMNS_OF_DISPLAY_CONTRACTS = { Constants.CONTRACT_ID, Constants.CREATED_AT,
			Constants.LAST_MODIFIED, Constants.DELIVERY_ASSEMBLY_FEE_UVP, Constants.SERVICE_COST_GARAGE_UVP,
			Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL_UVP, Constants.DOWN_PAYMENT_UVP, Constants.ESTATE_UVP,
			Constants.DELIVERY_ASSEMBLY_FEE_HOUSEPRICE, Constants.SERVICE_COST_GARAGE_HOUSEPRICE,
			Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL_HOUSEPRICE, Constants.DOWN_PAYMENT_HOUSEPRICE,
			Constants.ESTATE_HOUSEPRICE, Constants.IS_ONLINE_SHOP };

	/** actions for display contracts */
	public static final String CSV_EXPORT_CONTRACTS = "Verträge exportieren";

	public static final String DISPLAY_CONTRACTS = "Verträge von %s, %s ";
	// </display contracts>

	// <search customer>
	public static final int WIDTH_OF_SEARCH_CUSTOMER_FRAME = 250;
	public static final int HEIGHT_OF_SEARCH_CUSTOMER_FRAME = 100;
	// </search customer>

	/** constants for JOptionsPane */
	public static final String WARNING = "Warnung";

	/** constants for table sellposition */
	public static final String SELL_POSITION = "Verkaufsposition";
	public static final String CUSTOMER_ID = "ID_KUNDE";
	public static final String CONTRACT_ID = "ID_VERTRAG";
	public static final String ARTICLE_NAME = "Artikelname";// also in erp data set!

	/** constants for table frameheight */
	public static final String FRAME_HEIGHT_ID = "ID_RAHMENHOEHE";

	// INCHES
	public static final String INCH = "Zoll";
	public static final String CM = "cm";
	public static final String INCHES_13_56 = "13.56 ";
	public static final String INCHES_14_01 = "14.01 ";
	public static final String INCHES_14_46 = "14.46 ";
	public static final String INCHES_14_92 = "14.92 ";
	public static final String INCHES_15_37 = "15.37 ";
	public static final String INCHES_15_82 = "15.82 ";
	public static final String INCHES_16_27 = "16.27 ";
	public static final String INCHES_16_72 = "16.72 ";
	public static final String INCHES_17_18 = "17.18 ";
	public static final String INCHES_17_63 = "17.63 ";
	public static final String INCHES_18_08 = "18.08 ";
	public static final String INCHES_18_53 = "18.53 ";
	public static final String INCHES_18_98 = "18.98 ";
	public static final String INCHES_19_44 = "19.44 ";
	public static final String INCHES_19_89 = "19.89 ";
	public static final String INCHES_20_34 = "20.34 ";
	public static final String INCHES_20_79 = "20.79 ";
	public static final String INCHES_21_24 = "21.24 ";

	// CM-MOUNTAINBIKE
	public static final String CM_34_44 = "34.44 ";
	public static final String CM_35_59 = "35.59 ";
	public static final String CM_36_74 = "36.74 ";
	public static final String CM_37_89 = "37.89 ";
	public static final String CM_39_03 = "39.03 ";
	public static final String CM_40_18 = "40.18 ";
	public static final String CM_41_33 = "41.33 ";
	public static final String CM_42_48 = "42.48 ";
	public static final String CM_43_63 = "43.63 ";
	public static final String CM_44_78 = "44.78 ";
	public static final String CM_45_92 = "45.92 ";
	public static final String CM_47_07 = "47.07 ";
	public static final String CM_48_22 = "48.22 ";
	public static final String CM_49_37 = "49.37 ";
	public static final String CM_50_52 = "50.52 ";
	public static final String CM_51_66 = "51.66 ";
	public static final String CM_52_81 = "52.81 ";
	public static final String CM_53_96 = "53.96 ";

	// CM-RACING_CYCLE
	public static final String CM_39_90 = "39.90 ";
	public static final String CM_41_32 = "41.23 ";
	public static final String CM_42_56 = "42.56 ";
	public static final String CM_43_89 = "43.89 ";
	public static final String CM_45_22 = "45.22 ";
	public static final String CM_46_55 = "46.55 ";
	public static final String CM_47_88 = "47.88 ";
	public static final String CM_49_21 = "49.21 ";
	public static final String CM_50_54 = "50.54 ";
	public static final String CM_51_87 = "51.87 ";
	public static final String CM_53_20 = "53.20 ";
	public static final String CM_54_53 = "54.53 ";
	public static final String CM_55_86 = "55.86 ";
	public static final String CM_57_19 = "57.19 ";
	public static final String CM_58_52 = "58.52 ";
	public static final String CM_59_85 = "59.85 ";
	public static final String CM_61_18 = "61.18 ";
	public static final String CM_62_51 = "62.51 ";

	// <BikeDataDialog>
	public static final int HEIGHT_OF_BIKE_DATA = 470;
	public static final String SellerAndBikeData = "Verkäufer/Fahrrad-Daten";
	public static final String BikeData_DATA = "Fahrrad_Daten";
	public static final String BikeData = "Fahrrad-Daten";
	public static final String FRAME_NUMBER = "Rahmennummer";
	public static final String FRAME_NUMBER_DATA = "Rahmennummer";
	/** column headers for the table */
	public static final String NEW = "neu";
	public static final String USED = "gebraucht";
	public static final String USED_DATA = "gebraucht ";
	public static final String TREKKING = "Trekking";
	public static final String CITY_TOUR = "City/Tour";
	public static final String CITY_TOUR_DATA = "City_Tour";
	public static final String MOUTAINBIKE = "Mountainbike";
	public static final String CHILDS_BIKE = "Kinder-/Jugendrad";
	public static final String CHILDS_BIKE_DATA = "Kinder_Jugendrad";
	public static final String RACING_CYCLE = "Rennrad";
	public static final String CROSS = "Cross";
	public static final String BMX = "BMX";
	public static final String E_BIKE = "E-Bike/Pedelec";
	public static final String E_BIKE_DATA = "E_Bike_Pedelec";
	public static final String OTHERS = "Andere";
	public static final String INSPECTION = "Inspektion";
	public static final String YEAR = "JAHR";
	public static final String MONTH = "Monat";
	public static final String MECHANIC = "Mechaniker";
	public static final String[] COLUMNS_OF_BIKE_DATA_MIDDLE = { PROPERTIES, NEW, USED, TREKKING, CITY_TOUR,
			MOUTAINBIKE, CHILDS_BIKE, RACING_CYCLE, CROSS, BMX, E_BIKE, OTHERS };
	public final static String DATE = "Datum";
	public final static String SELLER = "Verkäufer";
	public final static String DELIVERY = "Lieferung";
	public final static String COLLECTION = "Abholung";
	public final static String TAKEAWAY = "Mitnahme";
	public final static String BOOKED = "Reserviert";
	public final static String[] COLUMNS_OF_BIKE_DATA_TOP = { DATE, SELLER, DELIVERY, COLLECTION, TAKEAWAY, BOOKED };
	public static final String SAVE_BIKE_DATA = "Speichern";
	public static final String UPDATE_BIKE_DATA = "Fahrrad-Daten aktualisieren";

	// </BikeDataDialog>

	// <pdf of contract0>
	public final static float SPACING_BEFORE_CONTRACT_TABLE = 15f;
	public final static String CUSTOMER_NAME_AND_SURNAME = CUSTOMER_NAME + " und " + PRENAME;
	public final static String STREET_AND_NUMBER = STREET + COMMA + EMPTY_STRING + NUMBER;
	public static final String FRAME_HEIGHT = "RH";
	public static final String FINISHED_AT_DATE = "Datum";
	public static final String FINISHED_AT_DATE_DATA = "fertig_Datum";
	public static final String FINISHED_AT_TIME = "Uhrzeit";
	public static final String FRAME_HEIGHT_DATA = "Rahmenhöhe";
	public static final float FONTSIZE_SUB = 6;
	public static final float FONTSIZE_NORMAL = 6;
	public static final float FONTSIZE_FOOTER = 5f;
	public static final float FONTSIZE_FOOTER_SUB = 4;
	// </pdf of contract0>

	// Constants for erp system
	public static final String EAN = "EAN";
	public static final String UVP_ERP = "UVP";
	public static final String SELLPRICE_FROM_ERP = "Verkaufspreis_Aus_Warenwirtschaft";
	public static final String ARTICLE_NUMBER = "ARTIKEL_NUMMER";
	public static final String PRODUCER = "Hersteller";
	public static final String PRODUCTS_ESL1 = "products_esl1";
	public static final String PRODUCTS_ESL2 = "products_esl2";
	public static final String PRODUCTS_ESL3 = "products_esl3";
	public static final String PRODUCTS_URL_KEY = "products_url_key";

	// Constants for Access
	public static final String ACCESS = "Access";
	public static final String ACCESS_TXT = "Access.txt";
	public static final String USER = "USER";
	public static final String PASSWORD = "PASSWORD";
	public static final String SERVER = "SERVER";
	public static final String PORT = "PORT";
	public static final String DIRECTORY_REMOTE = "DIRECTORY_REMOTE";
	public static final String ENCRYPTION = "ENCRYPTION";
	public static final String ILLEGAL_CONFIGURATION = "Configuration %s nicht erlaubt.";

	/** constants for Statistics */
	public static final String ONE_MONTH = "1 Monat";
	public static final String TWO_MONTHS = "2 Monate";
	public static final String THREE_MONTHS = "3 Monate";
	public static final String SIX_MONTHS = "6 Monate";
	public static final String NINE_MONTHS = "9 Monate";
	public static final String TWELVE_MONTHS = "12 Monate";
	public static final String FIFTEEN_MONTHS = "15 Monate";
	public static final String EIGHTEEN_MONTHS = "18 Monate";
	public static final String TWENTY_ONE_MONTHS = "21 Monate";
	public static final String TWENTY_FOUR_MONTHS = "24 Monate";

	/** the icons */
	public static final String ICON_CONFIRM = "confirm.png";
	public static final String LOGO = "logo.png";

	/** constants for administration */
	public static final String HOUSEKEEPING_PDF = "HOUSEKEEPING Pdf (Tage)";
	public static final String HOUSEKEEPING_PDF_DATA = "HOUSEKEEPING_Pdf";
	public static final String HOUSEKEEPING_CONTRACTS = "HOUSEKEEPING Kaufvertrag (Jahre)";
	public static final String HOUSEKEEPING_CONTRACTS_DATA = "HOUSEKEEPING_Kaufvertrag";
	public static final String HOUSEKEEPING_DATA = "HOUSEKEEPING_DATA";
	public static final String ERP_LAST_READED = "ERP_LAST_READED";
	public static final String INVALID_VALUE = "Parameter < %s > mit Wert < %s > nicht zulässig";
	public static final String SRC_OF_ERP_DATA = "Warenwirtschaft";
	public static final String ONLINE_SHOP = "ONLINESHOP";
	public static final String ONLINESHOP = "OnlineShop";
	public static final String ADVARICS = "Advarics";
	public static final String DATA_READED = "Daten gelesen am";
	public static final String DO_HOUSEKEEPING = "Housekeeping";
	public static final String USE_PASSWORD = "Passwort";
	public final static String[] COLUMNS_OF_ADMINISTRATION = { DATA_READED, ADVARICS, ONLINE_SHOP, DO_HOUSEKEEPING,
			HOUSEKEEPING_CONTRACTS, HOUSEKEEPING_PDF, USE_PASSWORD };
	public static final String UPDATE_ADMINISTRATION = "aktualisieren";
	public static final String CHANGE_PASSWORD = "Password ändern";
	public static final String EXECUTING_HOUSEKEEPING_PDF_NOT_POSSIBLE = "Housekeeping für %s Tage nicht möglich";
	public static final String EXECUTING_HOUSEKEEPING_CONTRACTS_NOT_POSSIBLE = "Housekeeping für %s Jahre nicht möglich";
	public static final String SMB_ACCESS = "SmbAccess";
	public static final String SMB_ACCESS_TXT = "smbAccess.txt";

}
