package constants;

import java.io.File;

import contractDigitalizer.BikeContract;

public class ShoreConstants {

	public static final String PATH_TO_SHORE_APPOINTMENTS_RELEASE = "smb://raspberrypi/termine/";
	public static final String USER = "pi";

	// Constants for shore:
	public static final String DTSTART = "DTSTART;";
	public static final String DTEND = "DTEND;";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String SUMMARY = "SUMMARY";
	public static final String DAY_OF_WEEK = "Wochentag";
	public static final String START_TIME = "Uhrzeit";
	public static final String SUBJECT = "Betreff";
	public static final String EMPLOYEE = "Mitarbeiter";
	public static final String SERVICE = "Leistung";
	public static final String SHORE = "Shore";
	public static final String SALUTATION = "Anrede";
	public static final String SALUTATION_MALE = "Herr";
	public static final String SALUTATION_FEMALE = "Frau";
	public static final String CUSTOMER_NAME = "Kundenname";
	public static final String PRENAME = "Vorname";
	public static final String SHORE_APPOINTMENTS = "Anzeige Shore Termine";
	public static final String[] COLUMNS_OF_DISPLAY_APPOINTMENTS = { DAY_OF_WEEK, START_TIME, SALUTATION, PRENAME,
			CUSTOMER_NAME, SUBJECT, SERVICE, EMPLOYEE };

	public static final String CUSTOMER_UNKNOWN = "<html>Kunde <font color=\"green\"> %s </font>, <font color=\"green\"> %s </font> unbekannt! <br> Sie können den Kunden nun editieren.</html>";
	public static final String BIKE_COLLECTION = "RADABHOLUNG";
	public static final String CONNECTION_NOT_EXISTING = "Verbindung estiert nicht!";
	public static final String MONDAY = "Montag";
	public static final String TUESDAY = "Dienstag";
	public static final String WEDNESDAY = "Mittwoch";
	public static final String THURSDAY = "Donnerstag";
	public static final String FRIDAY = "Freitag";
	public static final String SATURDAY = "Samstag";
	public static final String SUNDAY = "Sonntag";
	public static final String PATH_TO_SHORE_FOLDER = BikeContract.getDirectoryOfBikeContract() + File.separator
			+ Constants.ASSETS + File.separator + ShoreConstants.SHORE;
}
