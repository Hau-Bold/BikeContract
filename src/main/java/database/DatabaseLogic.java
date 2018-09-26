package database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;

import administration.Administration;
import bikeData.BikeData;
import constants.Constants;
import contract.Contract;
import contract.SellPosition;
import contractDigitalizer.BikeContract;
import cryption.CryptionUtils;
import customer.Customer;
import erp.ErpDataSet;
import seller.Seller;
import utils.InformationProvider;
import utils.Utils;

/**
 * contains the used standard queries INSERT, UPDATE, DROP,CREATE
 *
 */
public class DatabaseLogic {

	private static DataBaseConnection connection;
	private static String dataBaseName;

	/**
	 * Constructor
	 * 
	 * @param dataBaseName
	 *            - the name of the database
	 */
	public DatabaseLogic(String dataBaseName) {
		DatabaseLogic.dataBaseName = dataBaseName;
	}

	/**
	 * to establish a connection with auto commit
	 * 
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void connectInAutoCommitMode() throws SQLException {

		if (connection != null) {
			connection.getConnection().close();
			connection = null;
		}

		connection = new DataBaseConnection(BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.BIN
				+ File.separator + dataBaseName);
		connection.getConnection().setAutoCommit(Boolean.TRUE);
	}

	/**
	 * to establish a connection without auto commit
	 * 
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void connectNotInAutoCommitMode() throws SQLException {

		if (connection != null) {
			connection.getConnection().close();
			connection = null;
		}
		connection = new DataBaseConnection(BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.BIN
				+ File.separator + dataBaseName);
		connection.getConnection().setAutoCommit(Boolean.FALSE);
	}

	/**
	 * to terminate connection:
	 */
	public static void disconnect() {

		try {
			if (connection != null) {
				connection.getConnection().close();
			}
		} catch (SQLException e) {
			System.out.println("DATABASE: Status: Not able to disconnect");
			e.printStackTrace();
		}

	}

	public static void disconnectAndDoCommit() {

		try {
			if (connection != null) {
				connection.getConnection().commit();
				connection.getConnection().close();
			}
		} catch (SQLException e) {
			System.out.println("DATABASE: Status: Not able to disconnect");
			e.printStackTrace();
		}

	}

	/**
	 * writes the customers form the csv-Fiel to the database
	 * 
	 * @param path
	 *            - the path of the csv-File
	 * @param createdAt
	 *            - createdAt
	 * @throws FileNotFoundException
	 *             - in case of technical error
	 * @throws IOException-
	 *             in case of technical error
	 * @throws SQLException-
	 *             in case of technical error
	 * @throws InvalidKeyException-
	 *             in case of technical error
	 * @throws NoSuchAlgorithmException-
	 *             in case of technical error
	 * @throws NoSuchPaddingException-
	 *             in case of technical error
	 * @throws IllegalBlockSizeException-
	 *             in case of technical error
	 * @throws BadPaddingException-
	 *             in case of technical error
	 */
	public static void writeCustomerData(String path, String createdAt)
			throws FileNotFoundException, IOException, SQLException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		connectNotInAutoCommitMode();
		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path)), Constants.CHARSET))) {
			String line;
			int counter = 0;
			long start = System.currentTimeMillis();
			PreparedStatement preparedStatement = null;
			String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.CUSTOMER
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);

			while ((line = bufferedReader.readLine()) != null) {

				line = line.replace(";", "; ");
				String[] customerData = line.split(";");
				String customerName = customerData[5].trim();
				String prename = customerData[4].trim();
				String email = customerData[12].trim();
				email = email.equals(Constants.EMPTY_STRING) ? Constants.NA : email;
				String mobile = customerData[9].trim();
				mobile = mobile.equals(Constants.EMPTY_STRING) ? Constants.NA : mobile;
				String postalcode = customerData[14].trim();
				String place = customerData[15].trim();

				String streetAndNumber = customerData[13];
				String[] streetAndNumberSplitted = streetAndNumber.split(" ");
				int length = streetAndNumberSplitted.length;

				String number = streetAndNumberSplitted[length - 1].trim();
				String street = Constants.EMPTY_STRING;
				for (int i = 0; i < length - 1; i++) {
					street = street.concat(streetAndNumberSplitted[i]);
					street = street.concat(Constants.EMPTY_SPACE);
				}

				counter++;

				preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
				preparedStatement.setString(2, CryptionUtils.encrypt(customerName));
				preparedStatement.setString(3, CryptionUtils.encrypt(prename));
				preparedStatement.setString(4, CryptionUtils.encrypt(street.trim()));
				preparedStatement.setString(5, CryptionUtils.encrypt(number));
				preparedStatement.setString(6, CryptionUtils.encrypt(postalcode));
				preparedStatement.setString(7, CryptionUtils.encrypt(place));
				preparedStatement.setString(8, CryptionUtils.encrypt(mobile));
				preparedStatement.setString(9, CryptionUtils.encrypt(email));
				preparedStatement.setString(10, createdAt);
				preparedStatement.setString(11, createdAt);
				preparedStatement.addBatch();

				if (counter % 1000 == 0) {
					preparedStatement.executeBatch();
				}
			}

			preparedStatement.executeBatch();
			preparedStatement.close();
			double duration = System.currentTimeMillis() - start;
			duration = duration / 1000.0;

			System.err.println(String.format("INFO: %s datasets were written in %s seconds", counter, duration));
		}
		disconnectAndDoCommit();
	}

	/**
	 * creates table Administration
	 */
	public void createTableAdministration() {

		/** sql command string to execute: */
		String sqlCommandString = "CREATE TABLE IF NOT EXISTS "

				+ Constants.ADMINISTRATION + " (" + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ Constants.ERP_LAST_READED + " STRING DEFAULT ('')" + "," + Constants.ADVARICS + " INTEGER DEFAULT 1, "
				+ Constants.ONLINE_SHOP + " INTEGER DEFAULT 0, " + Constants.DO_HOUSEKEEPING + " INTEGER DEFAULT 0, "
				+ Constants.HOUSEKEEPING_CONTRACTS_DATA + " INTEGER DEFAULT 0, " + Constants.HOUSEKEEPING_PDF_DATA
				+ " INTEGER  DEFAULT 0, " + Constants.USE_PASSWORD + " INTEGER DEFAULT 1"

				+ ");";

		Statement statement = null;
		try {
			statement = connection.getConnection().createStatement();
			statement.executeUpdate(sqlCommandString);
			statement.close();

		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * yields the administration
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Administration getAdministration() throws SQLException {

		connectInAutoCommitMode();
		Administration response = null;
		/** sql command string to execute: */
		String sql = "SELECT * FROM " + Constants.ADMINISTRATION + ";";
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sql);

		if (resultSet != null) {
			while (resultSet.next()) {

				String erpLastReaded = resultSet.getString(2);
				Boolean all = Integer.compare(resultSet.getInt(3), 0) == 0 ? Boolean.FALSE : Boolean.TRUE;
				Boolean onlineShop = Integer.compare(resultSet.getInt(4), 0) == 0 ? Boolean.FALSE : Boolean.TRUE;
				Boolean useHousekeeping = Integer.compare(resultSet.getInt(5), 0) == 0 ? Boolean.FALSE : Boolean.TRUE;
				Integer houseKeepingContracts = resultSet.getInt(6);
				Integer houseKeepingPdf = resultSet.getInt(7);
				Boolean usePassword = Integer.compare(resultSet.getInt(8), 0) == 0 ? Boolean.FALSE : Boolean.TRUE;

				response = new Administration(erpLastReaded, usePassword, useHousekeeping, houseKeepingPdf,
						houseKeepingContracts, onlineShop, all);
			}
		}
		resultSet.close();
		statement.close();
		disconnect();
		return response;

	}

	/**
	 * to insert values into Administratiom table
	 * 
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public void insertIntoAdministrationTable() throws SQLException {

		Calendar calendar = Calendar.getInstance();

		Date now = calendar.getTime();
		String lastReaded = BikeContract.SIMPLE_DATE_FORMAT.format(now);

		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.ADMINISTRATION
				+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setString(2, lastReaded);
			preparedStatement.setInt(3, 1);
			preparedStatement.setInt(4, 0);
			preparedStatement.setInt(5, 0);
			preparedStatement.setInt(6, 2);
			preparedStatement.setInt(7, 10);
			preparedStatement.setInt(8, 1);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		}
	}

	public static void updateAdministrationTable(Administration administration, Boolean isStartUp) {
		PreparedStatement preparedStatement = null;

		String sqlCommandString = "UPDATE " + Constants.ADMINISTRATION + " SET " + Constants.ERP_LAST_READED + "="
				+ "\'" + administration.getErpLastReaded() + "\'" + "," + Constants.ADVARICS + "=" + "\'"
				+ (administration.getIsAll() ? 1 : 0) + "\'" + "," + Constants.ONLINE_SHOP + "=" + "\'"
				+ (administration.getIsOnlineShop() ? 1 : 0) + "\'" + "," + Constants.DO_HOUSEKEEPING + "=" + "\'"
				+ (administration.getUseHousekeeping() ? 1 : 0) + "\'" + "," + Constants.HOUSEKEEPING_CONTRACTS_DATA
				+ "=" + "\'" + administration.getHousekeepingContracts() + "\'" + "," + Constants.HOUSEKEEPING_PDF_DATA
				+ "=" + "\'" + administration.getHousekeepingPdf() + "\'" + "," + Constants.USE_PASSWORD + "=" + "\'"
				+ (administration.getUsePassword() ? 1 : 0) + "\'" + " WHERE " + Constants.ID + "=" + "\'" + "1" + "\'"
				+ ";";

		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		}

		if (!isStartUp) {
			/** inform client */
			new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.ADMINISTRATION_UPDATED_MESSAGE).run();

			int choice = JOptionPane.showConfirmDialog(null,
					String.format(Constants.ADMINISTRATION_WAS_EDITED_RESTART_NOW_QUESTION,
							BikeContract.getIdOfContract()),
					Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if (Integer.compare(choice, 0) == 0) {

				Runtime.getRuntime().exit(0);
			} else {

				return;
			}
		}

	}

	/**
	 * creates table Login
	 */
	public void createTableLogin() {
		/** sql command string to execute: */
		String sqlCommandString = "CREATE TABLE IF NOT EXISTS " + Constants.LOGIN + " ( " + Constants.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.PASSWORD + " STRING DEFAULT ('') " + ");";

		Statement statement = null;
		try {
			statement = connection.getConnection().createStatement();
			statement.executeUpdate(sqlCommandString);
			statement.close();

		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * yields the logins savend in database in a list
	 * 
	 * @return the logins in a list
	 * @throws InvalidKeyException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error (Encryption)
	 * @throws BadPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws SQLException
	 *             - in case of technical error (Query)
	 */
	public static List<String> getLogins() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, SQLException {
		List<String> response = new ArrayList<String>();
		String sqlCommandString = "SELECT * FROM " + Constants.LOGIN + ";";

		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		while (resultSet.next()) {

			String password = CryptionUtils.decrypt(resultSet.getString(2));

			response.add(password);
		}

		disconnect();

		return response;
	}

	/**
	 * yields the encrypted logins savend in database in a list
	 * 
	 * @return the logins in a list
	 * @throws InvalidKeyException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error (Encryption)
	 * @throws BadPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws SQLException
	 *             - in case of technical error (Query)
	 */
	public static List<String> getEncryptedLogins() throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SQLException {
		List<String> response = new ArrayList<String>();
		String sqlCommandString = "SELECT * FROM " + Constants.LOGIN + ";";

		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		while (resultSet.next()) {

			String password = resultSet.getString(2);

			response.add(password);
		}

		disconnect();

		return response;
	}

	/**
	 * write the login to the database
	 * 
	 * @param password
	 *            - the login
	 * @throws SQLException
	 *             - in case of technical error (Query)
	 * @throws InvalidKeyException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error (Encryption)
	 * @throws BadPaddingException
	 *             - in case of technical error (Encryption)
	 */
	public static void writeLogin(String password) throws SQLException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.LOGIN + " values (?, ?)";

		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setString(2, CryptionUtils.encrypt(password));
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();
			/** inform client */
			SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.PASSWORD_SAVED_MESSAGE));

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * to update the password in the login table
	 * 
	 * @param password
	 *            - the password
	 * @throws BadPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchPaddingException
	 *             - in case of technical error (Encryption)
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error (Encryption)
	 * @throws InvalidKeyException
	 *             - in case of technical error (Encryption)
	 */
	public static void updateLogin(String password) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		PreparedStatement preparedStatement = null;

		String sqlCommandString = "UPDATE " + Constants.LOGIN + " SET " + Constants.PASSWORD + "=" + "\'"
				+ CryptionUtils.encrypt(password) + "\'" + " WHERE " + Constants.ID + "=" + "\'" + "1" + "\'" + ";";

		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		}

		/** inform client */
		SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
				Constants.PASSWORD_SAVED_MESSAGE));

	}

	/**
	 * creates the table of customers
	 * 
	 */
	public void createTableCustomer() {

		/** sql command string to execute: */
		String sql = "CREATE TABLE IF NOT EXISTS " + Constants.CUSTOMER + "(" + Constants.CUSTOMER_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.CUSTOMER_NAME + " STRING DEFAULT ('') NOT NULL, "
				+ Constants.PRENAME + " STRING DEFAULT ('') NOT NULL, " + Constants.STREET
				+ " STRING DEFAULT ('') NOT NULL, " + Constants.NUMBER + " CHAR DEFAULT ('') NOT NULL, "
				+ Constants.POSTAL_CODE + " CHAR DEFAULT ('') NOT NULL, " + Constants.PLACE
				+ " STRING DEFAULT ('') NOT NULL, " + Constants.MOBILE + " CHAR DEFAULT (''), " + Constants.EMAIL
				+ " STRING DEFAULT (''), " + Constants.CREATED_AT_DATA + " STRING DEFAULT ('') NOT NULL, "
				+ Constants.LAST_MODIFIED_DATA + " STRING DEFAULT ('') NOT NULL, " + "UNIQUE(" + Constants.CUSTOMER_NAME
				+ "," + Constants.PRENAME + "," + Constants.STREET + "," + Constants.NUMBER + ","
				+ Constants.POSTAL_CODE + ")" + ");";
		try {
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sql));
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * deletes the customer
	 * 
	 * @param id
	 *            - the id of the customer
	 * @param customer
	 *            - the customer
	 */
	public static void removeCustomer(int id, Customer customer) {

		Integer countOfCustomersContracts = 0;

		String sqlCommandString = "DELETE FROM " + Constants.CUSTOMER + " WHERE " + Constants.CUSTOMER_ID + "=" + id;
		try {
			connectInAutoCommitMode();
			countOfCustomersContracts = getCountOfContracts(id);
			Statement statement = connection.getConnection().createStatement();
			statement.execute(sqlCommandString);
			statement.close();
			disconnect();
		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(ex.getMessage());
		}

		/** inform client */
		if (Integer.compare(countOfCustomersContracts, 1) != 0) {
			new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.CUSTOMER_DELETED_MESSAGE, customer.getCustomerName(), customer.getPrename(),
					String.valueOf(countOfCustomersContracts)).run();
		} else {
			new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.CUSTOMER_DELETED_MESSAGE_1, customer.getCustomerName(), customer.getPrename(),
					String.valueOf(countOfCustomersContracts)).run();
		}

	}

	/**
	 * updates the customer
	 * 
	 * @param id
	 *            - the id of the customer who is whised to update
	 * @param customer
	 *            - the customer
	 * @param lastModified
	 *            - the date of the update
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static void updateCustomer(int id, Customer customer, String lastModified)
			throws SQLException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {

		String sql = null;
		sql = "UPDATE " + Constants.CUSTOMER + " SET " + Constants.CUSTOMER_NAME + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getCustomerName()) + "\'" + "," + Constants.PRENAME + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getPrename()) + "\'" + "," + Constants.STREET + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getStreet()) + "\'" + "," + Constants.NUMBER + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getNumber()) + "\'" + "," + Constants.POSTAL_CODE + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getPostalCode()) + "\'" + "," + Constants.PLACE + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getPlace()) + "\'" + "," + Constants.MOBILE + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getMobile()) + "\'" + "," + Constants.EMAIL + "=" + "\'"
				+ CryptionUtils.encrypt(customer.getEmail()) + "\'" + "," + Constants.LAST_MODIFIED_DATA + "=" + "\'"
				+ lastModified + "\'" + " WHERE " + Constants.CUSTOMER_ID + "=" + "\'" + id + "\'" + ";";
		connectInAutoCommitMode();

		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.getConnection().prepareStatement(sql);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			/** inform client */
			SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.CUSTOMER_UPDATED_MESSAGE, customer.getCustomerName(), customer.getPrename()));

		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query %s", sql));
			e.printStackTrace();
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * to drop a table
	 *
	 * @param name
	 *            - name of the table
	 */
	public static void dropTable(String name) {
		String sql = "DROP TABLE IF EXISTS " + name;
		Statement statement = null;
		try {
			connectInAutoCommitMode();
			statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();
			disconnect();
			System.err.println(String.format("table %s was dropped", name));
		} catch (SQLException e) {
			System.err.println(String.format("dropping table %s was impossible", name));
			e.printStackTrace();
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * to insert values into customer's table
	 * 
	 * @param customer
	 *            - the customer
	 * @param createdAt
	 *            - created at
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static void insertIntoCustomerTable(Customer customer, String createdAt)
			throws SQLException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.CUSTOMER
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setString(2, CryptionUtils.encrypt(customer.getCustomerName()));
			preparedStatement.setString(3, CryptionUtils.encrypt(customer.getPrename()));
			preparedStatement.setString(4, CryptionUtils.encrypt(customer.getStreet()));
			preparedStatement.setString(5, CryptionUtils.encrypt(customer.getNumber()));
			preparedStatement.setString(6, CryptionUtils.encrypt(customer.getPostalCode()));
			preparedStatement.setString(7, CryptionUtils.encrypt(customer.getPlace()));
			preparedStatement.setString(8, CryptionUtils.encrypt(customer.getMobile()));
			preparedStatement.setString(9, CryptionUtils.encrypt(customer.getEmail()));
			preparedStatement.setString(10, createdAt);
			preparedStatement.setString(11, createdAt);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();
			/** inform client */
			SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.CUSTOMER_SAVED_MESSAGE, customer.getCustomerName(), customer.getPrename()));

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	/**
	 * to insert values into customer's table (csv import)
	 * 
	 * @param customer
	 *            - the customer
	 * @param createdAt
	 *            - created at
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void insertIntoCustomerTable(Customer customer) throws SQLException {
		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.CUSTOMER
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setInt(1, customer.getIdOfCustomer());
			preparedStatement.setString(2, customer.getCustomerName());
			preparedStatement.setString(3, customer.getPrename());
			preparedStatement.setString(4, customer.getStreet());
			preparedStatement.setString(5, customer.getNumber());
			preparedStatement.setString(6, customer.getPostalCode());
			preparedStatement.setString(7, customer.getPlace());
			preparedStatement.setString(8, customer.getMobile());
			preparedStatement.setString(9, customer.getEmail());
			preparedStatement.setString(10, customer.getCreatedAt());
			preparedStatement.setString(11, customer.getLastModified());
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		}
	}

	/**
	 * creates the table of contracts
	 */
	public void createTableContract() {

		/** String sqlCommandString to execute: */
		String sql = "CREATE TABLE IF NOT EXISTS " + Constants.CONTRACT + "(" + Constants.CONTRACT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.CREATED_AT_DATA + " STRING DEFAULT ('') NOT NULL, "
				+ Constants.LAST_MODIFIED_DATA + " STRING DEFAULT ('') NOT NULL, " + Constants.CUSTOMER_ID
				+ " INTEGER NOT NULL, " + Constants.DELIVERY_ASSEMBLY_FEE_UVP + " DOUBLE DEFAULT 0.0, "
				+ Constants.SERVICE_COST_GARAGE_UVP + " DOUBLE DEFAULT 0.0, "
				+ Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL_UVP + " DOUBLE DEFAULT 0.0, " + Constants.DOWN_PAYMENT_UVP
				+ " DOUBLE DEFAULT 0.0, " + Constants.ESTATE_UVP + " DOUBLE DEFAULT 0.0, "
				+ Constants.DELIVERY_ASSEMBLY_FEE_HOUSEPRICE + " DOUBLE DEFAULT 0.0, "
				+ Constants.SERVICE_COST_GARAGE_HOUSEPRICE + " DOUBLE DEFAULT 0.0, "
				+ Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL_HOUSEPRICE + " DOUBLE DEFAULT 0.0, "
				+ Constants.DOWN_PAYMENT_HOUSEPRICE + " DOUBLE DEFAULT 0.0, " + Constants.ESTATE_HOUSEPRICE
				+ " DOUBLE DEFAULT 0.0, " + Constants.IS_ONLINE_SHOP + " INTEGER DEFAULT 0, " + "UNIQUE("
				+ Constants.CREATED_AT_DATA + "," + Constants.CUSTOMER_ID + ")" + " FOREIGN KEY ("
				+ Constants.CUSTOMER_ID + ") REFERENCES " + Constants.CUSTOMER + " (" + Constants.CUSTOMER_ID + ") "
				+ Constants.ON_DELETE_CASCADE + ");";
		try {
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sql));
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * deletes the contract
	 * 
	 * @param id
	 *            - the id of the contract
	 */
	public static void removeContract(int id) {
		String sqlCommandString = "DELETE FROM " + Constants.CONTRACT + " WHERE " + Constants.CONTRACT_ID + "=" + id;
		try {
			connectInAutoCommitMode();
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sqlCommandString);
			statement.close();
			disconnect();
		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(ex.getMessage());
		}

		/** inform client */
		new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
				Constants.CONTRACT_DELETED_MESSAGE, String.valueOf(id)).run();
	}

	/**
	 * to insert values into customer's table
	 * 
	 * @param id
	 *            - the id of the contract
	 * 
	 * @param customer
	 *            - the customer
	 * @param createdAt
	 *            - created at
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void insertIntoContractTable(int id, Contract contract) throws SQLException {
		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.CONTRACT
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		connectInAutoCommitMode();
		preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
		preparedStatement.setString(2, contract.getCreatedAt());
		preparedStatement.setString(3, contract.getCreatedAt());
		preparedStatement.setInt(4, id);
		preparedStatement.setDouble(5, contract.getDeliveryAssemblyFeeUVP());
		preparedStatement.setDouble(6, contract.getServiceCostGarageUVP());
		preparedStatement.setDouble(7, contract.getRedemptionDismantledPartsWheelUVP());
		preparedStatement.setDouble(8, contract.getDownPaymentUVP());
		preparedStatement.setDouble(9, contract.getEstateUVP());

		preparedStatement.setDouble(10, contract.getDeliveryAssemblyFeeHousePrice());
		preparedStatement.setDouble(11, contract.getServiceCostGarageHousePrice());
		preparedStatement.setDouble(12, contract.getRedemptionDismantledPartsWheelHousePrice());
		preparedStatement.setDouble(13, contract.getDownPaymentHousePrice());
		preparedStatement.setDouble(14, contract.getEstateHousePrice());
		preparedStatement.setInt(15, BikeContract.isOnlineShop ? 1 : 0);

		preparedStatement.executeUpdate();
		preparedStatement.close();

		disconnect();

	}

	/**
	 * to insert values into customer's table (csv import)
	 * 
	 * @param contract
	 *            - the contract
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static void insertIntoContractTable(Contract contract) throws SQLException {
		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.CONTRACT
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
		preparedStatement.setInt(1, contract.getIdOfContract());
		preparedStatement.setString(2, contract.getCreatedAt());
		preparedStatement.setString(3, contract.getLastModified());
		preparedStatement.setInt(4, contract.getIdOfCustomer());
		preparedStatement.setDouble(5, contract.getDeliveryAssemblyFeeUVP());
		preparedStatement.setDouble(6, contract.getServiceCostGarageUVP());
		preparedStatement.setDouble(7, contract.getRedemptionDismantledPartsWheelUVP());
		preparedStatement.setDouble(8, contract.getDownPaymentUVP());
		preparedStatement.setDouble(9, contract.getEstateUVP());

		preparedStatement.setDouble(10, contract.getDeliveryAssemblyFeeHousePrice());
		preparedStatement.setDouble(11, contract.getServiceCostGarageHousePrice());
		preparedStatement.setDouble(12, contract.getRedemptionDismantledPartsWheelHousePrice());
		preparedStatement.setDouble(13, contract.getDownPaymentHousePrice());
		preparedStatement.setDouble(14, contract.getEstateHousePrice());
		preparedStatement.setInt(15, BikeContract.isOnlineShop ? 1 : 0);

		preparedStatement.executeUpdate();
		preparedStatement.close();

	}

	/**
	 * to update the contract
	 * 
	 * @param id
	 *            - the id of the contract
	 * @param contract
	 *            - the contract
	 * @param lastModified
	 *            - the date of last modified
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void updateContract(int id, Contract contract, String lastModified) throws SQLException {
		PreparedStatement preparedStatement = null;

		String sqlCommandString = "UPDATE " + Constants.CONTRACT + " SET " + Constants.LAST_MODIFIED_DATA + "=" + "\'"
				+ lastModified + "\'" + "," + Constants.DELIVERY_ASSEMBLY_FEE_UVP + "=" + "\'"
				+ contract.getDeliveryAssemblyFeeUVP() + "\'" + "," + Constants.SERVICE_COST_GARAGE_UVP + "=" + "\'"
				+ contract.getServiceCostGarageUVP() + "\'" + "," + Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL_UVP
				+ "=" + "\'" + contract.getRedemptionDismantledPartsWheelUVP() + "\'" + "," + Constants.DOWN_PAYMENT_UVP
				+ "=" + "\'" + contract.getDownPaymentUVP() + "\'" + "," + Constants.ESTATE_UVP + "=" + "\'"
				+ contract.getEstateUVP() + "\'" + "," + Constants.DELIVERY_ASSEMBLY_FEE_HOUSEPRICE + "=" + "\'"
				+ contract.getDeliveryAssemblyFeeHousePrice() + "\'" + "," + Constants.SERVICE_COST_GARAGE_HOUSEPRICE
				+ "=" + "\'" + contract.getServiceCostGarageHousePrice() + "\'" + ","
				+ Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL_HOUSEPRICE + "=" + "\'"
				+ contract.getRedemptionDismantledPartsWheelHousePrice() + "\'" + ","
				+ Constants.DOWN_PAYMENT_HOUSEPRICE + "=" + "\'" + contract.getDownPaymentHousePrice() + "\'" + ","
				+ Constants.ESTATE_HOUSEPRICE + "=" + "\'" + contract.getEstateHousePrice() + "\'" + " WHERE "
				+ Constants.CONTRACT_ID + "=" + "\'" + id + "\'" + ";";

		connectInAutoCommitMode();
		preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
		preparedStatement.executeUpdate();
		preparedStatement.close();

	}

	/**
	 * creates the table of articles
	 */
	public void createTableFrameHeight() {

		/** String sqlCommandString to execute: */
		String sql = "CREATE TABLE IF NOT EXISTS " + Constants.FRAME_HEIGHT_DATA + "(" + Constants.FRAME_HEIGHT_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.FRAME_HEIGHT_DATA
				+ " STRING DEFAULT ('') NOT NULL ," + "UNIQUE(" + Constants.FRAME_HEIGHT_DATA + ")" + ");";
		try {
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();

		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sql));
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * initialize frameheights
	 * 
	 * @throws SQLException
	 */
	public void initializeFrameHeights() throws SQLException {

		// INCHES
		insertIntoFrameHeightTable(Constants.MINUS);
		insertIntoFrameHeightTable(Constants.INCHES_13_56 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_14_01 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_14_46 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_14_92 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_15_37 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_15_82 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_16_27 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_16_72 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_17_18 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_17_63 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_18_08 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_18_53 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_18_98 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_19_44 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_19_89 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_20_34 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_20_79 + Constants.INCH);
		insertIntoFrameHeightTable(Constants.INCHES_21_24 + Constants.INCH);

		// CM-MOUNTAIN_BIKE
		insertIntoFrameHeightTable(Constants.CM_34_44 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_35_59 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_36_74 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_37_89 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_39_03 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_40_18 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_41_33 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_42_48 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_43_63 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_44_78 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_45_92 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_47_07 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_48_22 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_49_37 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_50_52 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_51_66 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_52_81 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_53_96 + Constants.CM);

		// CM-RACING_CYCLE
		insertIntoFrameHeightTable(Constants.CM_39_90 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_41_32 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_42_56 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_43_89 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_45_22 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_46_55 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_47_88 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_49_21 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_50_54 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_51_87 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_53_20 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_54_53 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_55_86 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_57_19 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_58_52 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_59_85 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_61_18 + Constants.CM);
		insertIntoFrameHeightTable(Constants.CM_62_51 + Constants.CM);
	}

	/**
	 * to insert an article into Table
	 * 
	 * @param article
	 *            - the article
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static void insertIntoFrameHeightTable(String frameheight) throws SQLException {
		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.FRAME_HEIGHT_DATA + " values (?, ?)";

		try {

			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setString(2, frameheight);

			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		}
	}

	/**
	 * creates the table of shorts
	 */
	public void createTableSellPosition() {

		/** String sqlCommandString to execute: */
		String sql = "CREATE TABLE IF NOT EXISTS " + Constants.SELL_POSITION + "(" + Constants.CONTRACT_ID
				+ " INTEGER NOT NULL, " + Constants.EAN + " String DEFAULT (''),  "

				+ Constants.ARTICLE_NAME + " STRING DEFAULT (''), " + Constants.QUANTITY + " INTEGER DEFAULT 0 ,"
				+ Constants.PRODUCER + " STRING DEFAULT ('') ," + Constants.UVP + " DOUBLE DEFAULT NULL, "
				+ Constants.HOUSE_PRICE + " DOUBLE DEFAULT NULL, " + Constants.ARTICLE_NUMBER
				+ " STRING DEFAULT ('') , "

				+ " FOREIGN KEY (" + Constants.CONTRACT_ID + ") REFERENCES " + Constants.CONTRACT + " ("
				+ Constants.CONTRACT_ID + ")" + Constants.ON_DELETE_CASCADE + ", " + " UNIQUE(" + Constants.CONTRACT_ID
				+ "," + Constants.EAN + ")" + ");";
		try {
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();

		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sql));
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * removes all sellpositions with same contract id
	 * 
	 * @param idOfContract
	 *            - the id of the contract
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void removeSellPosition(int idOfContract) throws SQLException {

		DatabaseLogic.connectInAutoCommitMode();
		String sqlCommandString = "DELETE FROM " + Constants.SELL_POSITION + " WHERE " + Constants.CONTRACT_ID + "="
				+ "\'" + idOfContract + "\'";

		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.executeUpdate();
			preparedStatement.close();

		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			e.printStackTrace();
		}

		DatabaseLogic.disconnect();

	}

	/**
	 * to insert values into sellposition's table
	 * 
	 * @param lstSellPosition
	 *            - the list of sellpositions
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void insertIntoSellPositionTable(List<SellPosition> lstSellPosition) throws SQLException {

		connectInAutoCommitMode();

		for (SellPosition sellPosition : lstSellPosition) {

			if (!(sellPosition.getProducer() == null) || !(sellPosition.getArticleName() == null)) {

				PreparedStatement preparedStatement = null;
				String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.SELL_POSITION
						+ " values (?, ?, ?, ?, ?, ?, ?, ?)";

				preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
				preparedStatement.setInt(1, sellPosition.getIdOfContract());
				preparedStatement.setString(2, sellPosition.getEan());
				preparedStatement.setString(3, sellPosition.getArticleName());
				preparedStatement.setInt(4, sellPosition.getQuantity());
				preparedStatement.setString(5, sellPosition.getProducer());
				preparedStatement.setDouble(6, sellPosition.getUvp());
				preparedStatement.setDouble(7, sellPosition.getHousePrice());
				preparedStatement.setString(8, sellPosition.getArticleNumber());
				preparedStatement.executeUpdate();
				preparedStatement.close();
			}
		}

		disconnect();

	}

	/**
	 * creates the table of SellerAndBikeData
	 */
	public void createTableBikeData() {

		/** sql command string to execute */

		String sql = "CREATE TABLE IF NOT EXISTS " + Constants.BikeData_DATA + "(" + Constants.CONTRACT_ID
				+ " INTEGER NOT NULL, "
				/** first panel */
				+ Constants.DATE + " STRING DEFAULT (''), " + Constants.SELLER_ID + " INTEGER NOT NULL, "
				+ Constants.DELIVERY + " INTEGER DEFAULT 0, " + Constants.COLLECTION + " INTEGER DEFAULT 0, "
				+ Constants.TAKEAWAY + " INTEGER DEFAULT 0, " + Constants.BOOKED + " INTEGER DEFAULT 0, "
				/** second panel */
				+ Constants.NEW + " INTEGER DEFAULT 0, " + Constants.USED_DATA + " INTEGER DEFAULT 0, "
				+ Constants.TREKKING + " INTEGER DEFAULT 0, " + Constants.CITY_TOUR_DATA + " INTEGER DEFAULT 0, "
				+ Constants.MOUTAINBIKE + " INTEGER DEFAULT 0, " + Constants.CHILDS_BIKE_DATA + " INTEGER DEFAULT 0, "
				+ Constants.RACING_CYCLE + " INTEGER DEFAULT 0, " + Constants.CROSS + " INTEGER DEFAULT 0, "
				+ Constants.BMX + " INTEGER DEFAULT 0, " + Constants.E_BIKE_DATA + " INTEGER DEFAULT 0, "
				+ Constants.OTHERS + " INTEGER DEFAULT 0, "
				/** third panel */
				+ Constants.FRAME_NUMBER_DATA + " CHAR DEFAULT (''), " + Constants.FRAME_HEIGHT
				+ " STRING DEFAULT (''), " + Constants.YEAR + " INTEGER DEFAULT 0, " + Constants.MECHANIC
				+ " STRING DEFAULT (''), " + Constants.FINISHED_AT_DATE_DATA + " STRING DEFAULT (''), "
				+ Constants.FINISHED_AT_TIME + " STRING DEFAULT (''), " + " FOREIGN KEY (" + Constants.CONTRACT_ID
				+ ") REFERENCES " + Constants.CONTRACT + " (" + Constants.CONTRACT_ID + ")"
				+ Constants.ON_DELETE_CASCADE + "," + " FOREIGN KEY (" + Constants.SELLER_ID + ") REFERENCES "
				+ Constants.SELLER + " (" + Constants.SELLER_ID + ")" + Constants.ON_UPDATE_CASCADE

				+ ");";

		try {
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();
		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sql));
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * inserts into bikedata's table
	 * 
	 * @param date
	 *            - the date
	 * @param seller
	 *            - the seller
	 * @param delivery
	 *            - the delivery
	 * @param collection
	 *            - the collection
	 * @param takeAway
	 *            - the take away
	 * @param booked
	 *            - the booked
	 * @param new_Bike
	 *            - bike is new
	 * @param used_Bike
	 *            - bike is used
	 * @param trekking
	 *            - trekking
	 * @param cityTour
	 *            - city tour
	 * @param mountainBike
	 *            - mountainbike
	 * @param childsBike
	 *            - childs bike
	 * @param racingCycle
	 *            - racing cycle
	 * @param cross
	 *            - cross
	 * @param bmx
	 *            - bmx
	 * @param eBike
	 *            - eBike
	 * @param others
	 *            - others
	 * @param frameNumber
	 *            - the frame numer
	 * @param frameheight
	 *            - the frame height
	 * 
	 * @param year
	 *            - the year of inspection
	 * @param mechanic
	 *            - the choosed mechanic
	 * @param dateOfAppointment
	 *            - the date of the appointment
	 * @param timeeOfAppointment
	 *            - the time of the appointment
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void insertIntoBikeDataTable(String date, int id_seller, int delivery, int collection, int takeAway,
			int booked, int new_Bike, int used_Bike, int trekking, int cityTour, int mountainBike, int childsBike,
			int racingCycle, int cross, int bmx, int eBike, int others, String frameNumber, String frameheight,
			int year, String mechanic, String dateOfAppointment, String timeOfAppointment) throws SQLException {

		connectInAutoCommitMode();

		PreparedStatement preparedStatement = null;
		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.BikeData_DATA + " values ("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? " + ")";

		preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
		int id = BikeContract.getIdOfContract();
		preparedStatement.setInt(1, id);
		/** first panel */
		preparedStatement.setString(2, date);
		preparedStatement.setInt(3, id_seller);
		preparedStatement.setInt(4, delivery);
		preparedStatement.setInt(5, collection);
		preparedStatement.setInt(6, takeAway);
		preparedStatement.setInt(7, booked);
		/** second panel */
		preparedStatement.setInt(8, new_Bike);
		preparedStatement.setInt(9, used_Bike);
		preparedStatement.setInt(10, trekking);
		preparedStatement.setInt(11, cityTour);
		preparedStatement.setInt(12, mountainBike);
		preparedStatement.setInt(13, childsBike);
		preparedStatement.setInt(14, racingCycle);
		preparedStatement.setInt(15, cross);
		preparedStatement.setInt(16, bmx);
		preparedStatement.setInt(17, eBike);
		preparedStatement.setInt(18, others);
		/** third panel */
		preparedStatement.setString(19, frameNumber);
		preparedStatement.setString(20, frameheight);
		preparedStatement.setInt(21, year);
		preparedStatement.setString(22, mechanic);
		preparedStatement.setString(23, dateOfAppointment);
		preparedStatement.setString(24, timeOfAppointment);

		preparedStatement.executeUpdate();
		preparedStatement.close();

		disconnect();
		SwingUtilities.invokeLater(new InformationProvider(Constants.BIKE_DATA_SAVED_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE, Constants.INFORMATION, StringUtils.EMPTY));
	}

	/**
	 * updates the bike data
	 * 
	 * @param idOfSeller
	 *            - the id of the seller
	 * @param delivery
	 *            - the delivery
	 * @param collection
	 *            - the collection
	 * @param takeAway
	 *            - the takeaway
	 * @param booked
	 *            - the booked
	 * @param bikeIsNew
	 *            - is bike new
	 * @param bikeIsOld
	 *            - is bike old
	 * @param trekking
	 *            - is trecking
	 * @param cityTour
	 *            - is citytour
	 * @param mountainBike
	 *            - is mountainbike
	 * @param childsBike
	 *            is childsbike
	 * @param racingCycle
	 *            is racingcycle
	 * @param cross
	 *            - is cross
	 * @param bmx
	 *            - is bmx
	 * @param eBike
	 *            - is ebike
	 * @param others
	 *            - is others
	 * @param frameNumber
	 *            - the framenumer
	 * @param frameheight
	 *            - the frame height
	 * @param year
	 *            - the year
	 * @param month
	 *            - the month
	 * @param mechanic
	 *            - the mechanic
	 * @param dateOfAppointment
	 *            - the date of the appointment
	 * @param timeOfAppointment
	 *            - the time of the appointment
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void updateBikeDataTable(int idOfSeller, int delivery, int collection, int takeAway, int booked,
			int bikeIsNew, int bikeIsOld, int trekking, int cityTour, int mountainBike, int childsBike, int racingCycle,
			int cross, int bmx, int eBike, int others, String frameNumber, String frameheight, int year,
			String mechanic, String dateOfAppointment, String timeOfAppointment) throws SQLException {

		int id = BikeContract.getIdOfContract();
		/**
		 * initially, we have to check whether or not id of contract is in BikeDataTable
		 */
		Boolean isIsInBikeDataTable = idIsInBikeDataTable(id);

		if (isIsInBikeDataTable) {
			String sqlCommandString = "UPDATE " + Constants.BikeData_DATA + " SET " + Constants.SELLER_ID + "=" + "\'"
					+ idOfSeller + "\'" + "," + Constants.DELIVERY + "=" + "\'" + delivery + "\'" + ","
					+ Constants.COLLECTION + "=" + "\'" + collection + "\'" + "," + Constants.TAKEAWAY + "=" + "\'"
					+ takeAway + "\'" + "," + Constants.BOOKED + "=" + "\'" + booked + "\'" + ","

					+ Constants.NEW + "=" + "\'" + bikeIsNew + "\'" + "," + Constants.USED_DATA + "=" + "\'" + bikeIsOld
					+ "\'" + "," + Constants.TREKKING + "=" + "\'" + trekking + "\'" + "," + Constants.CITY_TOUR_DATA
					+ "=" + "\'" + cityTour + "\'" + "," + Constants.MOUTAINBIKE + "=" + "\'" + mountainBike + "\'"
					+ "," + Constants.CHILDS_BIKE_DATA + "=" + "\'" + childsBike + "\'" + "," + Constants.RACING_CYCLE
					+ "=" + "\'" + racingCycle + "\'" + "," + Constants.CROSS + "=" + "\'" + cross + "\'" + ","
					+ Constants.BMX + "=" + "\'" + bmx + "\'" + "," + Constants.E_BIKE_DATA + "=" + "\'" + eBike + "\'"
					+ "," + Constants.OTHERS + "=" + "\'" + others + "\'" + ","

					+ Constants.FRAME_NUMBER_DATA + "=" + "\'" + frameNumber + "\'" + "," + Constants.FRAME_HEIGHT + "="
					+ "\'" + frameheight + "\'" + "," + Constants.YEAR + "=" + "\'" + year + "\'" + ","
					+ Constants.MECHANIC + "=" + "\'" + mechanic + "\'" + "," + Constants.FINISHED_AT_DATE_DATA + "="
					+ "\'" + dateOfAppointment + "\'" + "," + Constants.FINISHED_AT_TIME + "=" + "\'"
					+ timeOfAppointment + "\'"

					+ " WHERE " + Constants.CONTRACT_ID + "=" + "\'" + id + "\'" + ";";
			PreparedStatement preparedStatement = null;

			connectInAutoCommitMode();

			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			disconnect();
			SwingUtilities.invokeLater(new InformationProvider(Constants.BIKE_DATA_UPDATED_MESSAGE,
					JOptionPane.INFORMATION_MESSAGE, Constants.INFORMATION, StringUtils.EMPTY));

			updateLastModifiedOfContract();
		} else {

			Calendar calendar = Calendar.getInstance();
			String date = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());

			insertIntoBikeDataTable(date, idOfSeller, delivery, collection, takeAway, booked, bikeIsNew, bikeIsOld,
					trekking, cityTour, mountainBike, childsBike, racingCycle, cross, bmx, eBike, others, frameNumber,
					frameheight, year, mechanic, dateOfAppointment, timeOfAppointment);
		}
	}

	/***
	 * checks whether or not id of contract is used in BikeDataTable
	 * 
	 * @param id
	 *            - the id of the contract
	 * @return true if contract is used, otherwise false
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static Boolean idIsInBikeDataTable(int id) throws SQLException {

		connectInAutoCommitMode();

		String sqlCommandString = "SELECT * FROM " + Constants.BikeData_DATA + " WHERE " + Constants.CONTRACT_ID + " = "
				+ "" + id + ";";
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		Boolean check = Boolean.FALSE;

		if (resultSet != null) {
			while (resultSet.next()) {

				check = resultSet.next();
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return check;
	}

	/**
	 * updates the last modified date of the contract
	 * 
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static void updateLastModifiedOfContract() throws SQLException {

		Calendar calendar = Calendar.getInstance(Locale.GERMAN);
		Date date = calendar.getTime();
		String lastModified = String.valueOf(date);

		int id = BikeContract.getIdOfContract();

		String sqlCommandString = "UPDATE " + Constants.CONTRACT + " SET " + Constants.LAST_MODIFIED_DATA + "=" + "\'"
				+ lastModified + "\'" + " WHERE " + Constants.CONTRACT_ID + "=" + "\'" + id + "\'" + ";";

		connectInAutoCommitMode();

		PreparedStatement preparedStatement = null;

		preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
		preparedStatement.executeUpdate();
		preparedStatement.close();

		disconnect();
	}

	/**
	 * yields the bikedata of a contract
	 * 
	 * @param idOfContract
	 *            - the id of the contract
	 * @return - the bikedata
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static BikeData getBikeData(int idOfContract) throws SQLException {
		Statement statement = null;
		ResultSet resultSet = null;
		BikeData response = null;
		String sql = "SELECT * FROM " + Constants.BikeData_DATA + " WHERE " + Constants.CONTRACT_ID + "=" + "'"
				+ idOfContract + "'" + ";";
		connectInAutoCommitMode();
		statement = connection.getConnection().createStatement();
		resultSet = statement.executeQuery(sql);

		if (resultSet != null) {
			while (resultSet.next()) {

				/** first panel */
				String date = resultSet.getString(2);
				String seller = resultSet.getString(3);
				int delivery = resultSet.getInt(4);
				int collection = resultSet.getInt(5);
				int takeAway = resultSet.getInt(6);
				int booked = resultSet.getInt(7);
				/** second panel */
				int new_Bike = resultSet.getInt(8);
				int used_Bike = resultSet.getInt(9);
				int trekking = resultSet.getInt(10);
				int cityTour = resultSet.getInt(11);
				int mountainBike = resultSet.getInt(12);
				int childsBike = resultSet.getInt(13);
				int racingCycle = resultSet.getInt(14);
				int cross = resultSet.getInt(15);
				int bmx = resultSet.getInt(16);
				int eBike = resultSet.getInt(17);
				int others = resultSet.getInt(18);
				/** third panel */
				String frameNumber = resultSet.getString(19);
				String frameHeight = resultSet.getString(20);

				int year = resultSet.getInt(21);
				String mechanic = resultSet.getString(22);
				String dateOfAppointment = resultSet.getString(23);
				String timeOfAppointment = resultSet.getString(24);

				response = new BikeData(date, seller, frameNumber, frameHeight, mechanic, delivery, collection,
						takeAway, booked, new_Bike, used_Bike, trekking, cityTour, mountainBike, childsBike,
						racingCycle, cross, bmx, eBike, others, year, dateOfAppointment, timeOfAppointment);
			}
		}
		resultSet.close();
		statement.close();
		disconnect();

		return response;
	}

	/**
	 * yields the customers with prename and customerName in a list
	 * 
	 * @param prename
	 *            - the prename
	 * 
	 * @param customerName
	 *            - the customer name
	 * 
	 * @return the customers in a list
	 * 
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static List<Customer> getCustomers(String prename, String customerName)
			throws SQLException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {

		List<Customer> response = new ArrayList<Customer>();
		String sqlCommandString = "SELECT * FROM " + Constants.CUSTOMER + " WHERE " + Constants.PRENAME + " = " + "\'"
				+ CryptionUtils.encrypt(prename) + "\'" + " AND " + Constants.CUSTOMER_NAME + " = " + "\'"
				+ CryptionUtils.encrypt(customerName) + "\';";

		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		while (resultSet.next()) {

			int idOfCustomer = resultSet.getInt(1);
			String street = CryptionUtils.decrypt(resultSet.getString(4));
			String number = CryptionUtils.decrypt(resultSet.getString(5));
			String postalCode = CryptionUtils.decrypt(resultSet.getString(6));
			String place = CryptionUtils.decrypt(resultSet.getString(7));
			String mobile = CryptionUtils.decrypt(resultSet.getString(8));
			String email = CryptionUtils.decrypt(resultSet.getString(9));
			String createdAt = resultSet.getString(10);
			String lastModifier = resultSet.getString(11);

			response.add(new Customer(idOfCustomer, customerName, prename, street, number, postalCode, place, mobile,
					email, createdAt, lastModifier));
		}

		disconnect();

		return response;

	}

	/**
	 * yields the customers in a list
	 * 
	 * @param withID
	 * 
	 * @return the customers in a list
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static List<Customer> getCustomers() throws SQLException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		Statement statement = null;
		ResultSet resultSet = null;
		List<Customer> response = new ArrayList<Customer>();
		Customer customer;
		String sql = "SELECT * FROM " + Constants.CUSTOMER + ";";
		connectInAutoCommitMode();
		statement = connection.getConnection().createStatement();
		resultSet = statement.executeQuery(sql);

		if (resultSet != null) {
			while (resultSet.next()) {

				int idOfCustomer = resultSet.getInt(1);
				String customerName = CryptionUtils.decrypt(resultSet.getString(2));
				String prename = CryptionUtils.decrypt(resultSet.getString(3));
				String street = CryptionUtils.decrypt(resultSet.getString(4));
				String number = CryptionUtils.decrypt(resultSet.getString(5));
				String postalCode = CryptionUtils.decrypt(resultSet.getString(6));
				String place = CryptionUtils.decrypt(resultSet.getString(7));
				String mobile = CryptionUtils.decrypt(resultSet.getString(8));
				String email = CryptionUtils.decrypt(resultSet.getString(9));
				String createdAt = resultSet.getString(10);
				String lastModified = resultSet.getString(11);
				customer = new Customer(idOfCustomer, customerName, prename, street, number, postalCode, place, mobile,
						email, createdAt, lastModified);
				response.add(customer);
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return response;
	}

	/**
	 * returns the count of contracts which were contracted with the customer
	 * corresponding to the id
	 * 
	 * @param id
	 *            - the id of the customer
	 * @return the count of contracts
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static Integer getCountOfContracts(int id) throws SQLException {

		Statement statement = null;
		ResultSet resultSet = null;
		Integer response = 0;
		String sql = "SELECT COUNT(*) FROM " + Constants.CONTRACT + " WHERE " + Constants.CUSTOMER_ID + "=" + "'" + id
				+ "'" + ";";
		statement = connection.getConnection().createStatement();
		resultSet = statement.executeQuery(sql);

		if (resultSet != null) {
			while (resultSet.next()) {

				response = resultSet.getInt(1);
			}
		}
		resultSet.close();
		statement.close();
		return response;
	}

	/**
	 * returns the contracts of a customer in a list
	 * 
	 * @param idOfCustomer
	 *            - the id of the customer
	 * @return the contracts of the customer in a list
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static List<Contract> getContracts(int idOfCustomer) throws SQLException {

		Statement statement = null;
		ResultSet resultSet = null;
		List<Contract> response = new ArrayList<Contract>();
		Contract contract;
		String sql = "SELECT * FROM " + Constants.CONTRACT + " WHERE " + Constants.CUSTOMER_ID + "=" + "'"
				+ idOfCustomer + "'" + ";";
		connectInAutoCommitMode();
		statement = connection.getConnection().createStatement();
		resultSet = statement.executeQuery(sql);

		if (resultSet != null) {
			while (resultSet.next()) {
				int idOfContract = resultSet.getInt(1);
				String createdAt = resultSet.getString(2);
				String lastModified = resultSet.getString(3);

				/** uvp parts of contract */
				Double deliveryAssemblyFeeUVP = resultSet.getDouble(5);
				Double serviceCostGarageUVP = resultSet.getDouble(6);
				Double redemptionDismantledPartsWheelUVP = resultSet.getDouble(7);
				Double downPaymentUVP = resultSet.getDouble(8);
				Double estateUVP = resultSet.getDouble(9);

				/** houseprice parts of contract */
				Double deliveryAssemblyFeeHousePrice = resultSet.getDouble(10);
				Double serviceCostGarageHousePrice = resultSet.getDouble(11);
				Double redemptionDismantledPartsWheelHousePrice = resultSet.getDouble(12);
				Double downPaymentHousePrice = resultSet.getDouble(13);
				Double estateHousePrice = resultSet.getDouble(14);

				Integer isOnlineShop = resultSet.getInt(15);

				contract = new Contract(idOfContract, createdAt, lastModified, deliveryAssemblyFeeUVP,
						serviceCostGarageUVP, redemptionDismantledPartsWheelUVP, downPaymentUVP, estateUVP,
						deliveryAssemblyFeeHousePrice, serviceCostGarageHousePrice,
						redemptionDismantledPartsWheelHousePrice, downPaymentHousePrice, estateHousePrice,
						isOnlineShop);
				response.add(contract);
			}
		}
		resultSet.close();
		statement.close();
		disconnect();
		return response;
	}

	/**
	 * yields the contract belonging to the id of contract and the id of customer
	 * 
	 * @param idContract
	 *            - the id of the contract
	 * 
	 * @param idCustomer
	 *            - the id of the customer
	 * 
	 * @return the contract
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static Contract getContract(int idContract, int idCustomer) throws SQLException {
		Statement statement = null;
		ResultSet resultSet = null;
		Contract contract = null;
		String sql = "SELECT * FROM " + Constants.CONTRACT + " WHERE " + Constants.CONTRACT_ID + "=" + "'" + idContract
				+ "'" + " AND " + Constants.CUSTOMER_ID + "=" + "'" + idCustomer + "'" + ";";
		connectInAutoCommitMode();
		statement = connection.getConnection().createStatement();
		resultSet = statement.executeQuery(sql);

		if (resultSet != null) {
			while (resultSet.next()) {
				int idOfContract = resultSet.getInt(1);
				String createdAt = resultSet.getString(2);
				String lastModified = resultSet.getString(3);

				/** uvp parts of contract */
				Double deliveryAssemblyFeeUVP = resultSet.getDouble(5);
				Double serviceCostGarageUVP = resultSet.getDouble(6);
				Double redemptionDismantledPartsWheelUVP = resultSet.getDouble(7);
				Double downPaymentUVP = resultSet.getDouble(8);
				Double estateUVP = resultSet.getDouble(9);

				/** houseprice parts of contract */
				Double deliveryAssemblyFeeHousePrice = resultSet.getDouble(10);
				Double serviceCostGarageHousePrice = resultSet.getDouble(11);
				Double redemptionDismantledPartsWheelHousePrice = resultSet.getDouble(12);
				Double downPaymentHousePrice = resultSet.getDouble(13);
				Double estateHousePrice = resultSet.getDouble(14);

				Integer isOnlineShop = resultSet.getInt(15);

				contract = new Contract(idOfContract, createdAt, lastModified, deliveryAssemblyFeeUVP,
						serviceCostGarageUVP, redemptionDismantledPartsWheelUVP, downPaymentUVP, estateUVP,
						deliveryAssemblyFeeHousePrice, serviceCostGarageHousePrice,
						redemptionDismantledPartsWheelHousePrice, downPaymentHousePrice, estateHousePrice,
						isOnlineShop);
			}
		}
		resultSet.close();
		statement.close();
		disconnect();
		return contract;
	}

	/**
	 * returns the contracts in a list
	 * 
	 * @return the contracts in a list
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static List<Contract> getContracts() throws SQLException {

		List<Contract> response = new ArrayList<Contract>();
		Contract contract;
		String sqlCommandString = "SELECT * FROM " + Constants.CONTRACT + ";";
		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		if (resultSet != null) {
			while (resultSet.next()) {
				int idOfContract = resultSet.getInt(1);
				String createdAT = resultSet.getString(2);
				String lastModified = resultSet.getString(3);
				int idOfCustomer = resultSet.getInt(4);

				/** the uvp parts */
				double deliveryAssemblyFeeUVP = resultSet.getDouble(5);
				double serviceCostGarageUVP = resultSet.getDouble(6);
				double redemptionDismatledPartsWheelUVP = resultSet.getDouble(7);
				double downPaymentUVP = resultSet.getDouble(8);
				double estateUVP = resultSet.getDouble(9);

				/** the housepricing parts */
				double deliveryAssemblyFeeHousePrice = resultSet.getDouble(10);
				double serviceCostGarageHousePrice = resultSet.getDouble(11);
				double redemptionDismatledPartsWheelHousePrice = resultSet.getDouble(12);
				double downPaymentHousePrice = resultSet.getDouble(13);
				double estateHousePrice = resultSet.getDouble(14);

				Integer isOnlineShop = resultSet.getInt(15);

				contract = new Contract(idOfContract, createdAT, lastModified, deliveryAssemblyFeeUVP,
						serviceCostGarageUVP, redemptionDismatledPartsWheelUVP, downPaymentUVP, estateUVP,
						deliveryAssemblyFeeHousePrice, serviceCostGarageHousePrice,
						redemptionDismatledPartsWheelHousePrice, downPaymentHousePrice, estateHousePrice, isOnlineShop);

				contract.setIdOfCustomer(idOfCustomer);

				response.add(contract);
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return response;
	}

	/**
	 * returns the createdAt Date of all contracted contracts of the last month in a
	 * list
	 * 
	 * @param month
	 *            - the last month
	 * @return the createdAt Date of all during the last month contracted contracts
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static List<Contract> getContractsOfLastMonth(int month) throws SQLException {

		String sqlCommandString = null;

		Calendar calendar = Calendar.getInstance();
		Date current = calendar.getTime();
		String now = BikeContract.SIMPLE_DATE_FORMAT.format(current);
		String[] nowSplitted = now.split("\\.");
		String currentMonth = nowSplitted[1].trim();
		String currentYear = nowSplitted[2].trim();

		calendar.add(Calendar.MONTH, -month);
		String startDate = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());
		String[] startDateSplitted = startDate.split("\\.");
		String startDateMonth = startDateSplitted[1].trim();
		String startDateYear = startDateSplitted[2].trim();

		Integer currentYearAsInt = Integer.valueOf(currentYear);
		Integer startDateYearAsInt = Integer.valueOf(startDateYear);

		if (Integer.compare(currentYearAsInt, startDateYearAsInt) == 0) {

			/** case filtering not across annaul limits */
			sqlCommandString = "SELECT * FROM " + Constants.CONTRACT + " WHERE SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",7,4)" + " = " + "\'" + currentYear + "\'" + " AND " + "SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",4,2)" + " >= " + "\'" + startDateMonth + "\'" + ";";

		} else if (Integer.compare(currentYearAsInt - 1, startDateYearAsInt) == 0) {

			/** case filtering across annaul limits */
			sqlCommandString = "SELECT * FROM " + Constants.CONTRACT + " WHERE " + "(SUBSTR("
					+ Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + currentYear + "\'" + " AND " + "SUBSTR("
					+ Constants.CREATED_AT_DATA + ",4,2)" + " <= " + "\'" + currentMonth + "\')" + " OR " +
					/** last vear */
					"(SUBSTR(" + Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + startDateYear + "\'" + " AND "
					+ "SUBSTR(" + Constants.CREATED_AT_DATA + ",4,2)" + " >= " + "\'" + startDateMonth + "\'" + ");";
		}

		else if (Integer.compare(currentYearAsInt - 2, startDateYearAsInt) == 0) {

			/** case filtering across annaul limits */
			sqlCommandString = "SELECT * FROM " + Constants.CONTRACT + " WHERE " + "(SUBSTR("
					+ Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + currentYear + "\'" + " AND " + "SUBSTR("
					+ Constants.CREATED_AT_DATA + ",4,2)" + " <= " + "\'" + currentMonth + "\')" + " OR " +
					/** two years ago */
					"(SUBSTR(" + Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + startDateYear + "\'" + " AND "
					+ "SUBSTR(" + Constants.CREATED_AT_DATA + ",4,2)" + " >= " + "\'" + startDateMonth + "\')" + " OR "
					+
					/** last year */
					"(SUBSTR(" + Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'"
					+ String.valueOf(currentYearAsInt - 1) + "\'" + ");";
		}

		List<Contract> response = new ArrayList<Contract>();
		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);
		Contract contract = null;
		if (resultSet != null) {
			while (resultSet.next()) {
				int idOfContract = resultSet.getInt(1);
				String createdAT = resultSet.getString(2);
				String lastModified = resultSet.getString(3);
				int idOfCustomer = resultSet.getInt(4);

				/** the uvp parts */
				double deliveryAssemblyFeeUVP = resultSet.getDouble(5);
				double serviceCostGarageUVP = resultSet.getDouble(6);
				double redemptionDismatledPartsWheelUVP = resultSet.getDouble(7);
				double downPaymentUVP = resultSet.getDouble(8);
				double estateUVP = resultSet.getDouble(9);

				/** the housepricing parts */
				double deliveryAssemblyFeeHousePrice = resultSet.getDouble(10);
				double serviceCostGarageHousePrice = resultSet.getDouble(11);
				double redemptionDismatledPartsWheelHousePrice = resultSet.getDouble(12);
				double downPaymentHousePrice = resultSet.getDouble(13);
				double estateHousePrice = resultSet.getDouble(14);

				Integer isOnlineShop = resultSet.getInt(15);

				contract = new Contract(idOfContract, createdAT, lastModified, deliveryAssemblyFeeUVP,
						serviceCostGarageUVP, redemptionDismatledPartsWheelUVP, downPaymentUVP, estateUVP,
						deliveryAssemblyFeeHousePrice, serviceCostGarageHousePrice,
						redemptionDismatledPartsWheelHousePrice, downPaymentHousePrice, estateHousePrice, isOnlineShop);

				contract.setIdOfCustomer(idOfCustomer);

				response.add(contract);
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return response;
	}

	/**
	 * returns the customers of the last month in a list
	 * 
	 * @param month
	 *            - the last month
	 * @return the contracts of the last month in a list
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static List<String> getCustomersOfLastMonth(int month) throws SQLException {

		String sqlCommandString = null;

		Calendar calendar = Calendar.getInstance();
		Date current = calendar.getTime();
		String now = BikeContract.SIMPLE_DATE_FORMAT.format(current);
		String[] nowSplitted = now.split("\\.");
		String currentMonth = nowSplitted[1].trim();
		String currentYear = nowSplitted[2].trim();

		calendar.add(Calendar.MONTH, -month);
		String startDate = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());
		String[] startDateSplitted = startDate.split("\\.");
		String startDateMonth = startDateSplitted[1].trim();
		String startDateYear = startDateSplitted[2].trim();

		Integer currentYearAsInt = Integer.valueOf(currentYear);
		Integer startDateYearAsInt = Integer.valueOf(startDateYear);

		if (Integer.compare(currentYearAsInt, startDateYearAsInt) == 0) {

			/** case filtering not across annaul limits */
			sqlCommandString = "SELECT * FROM " + Constants.CUSTOMER + " WHERE SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",7,4)" + " = " + "\'" + currentYear + "\'" + " AND " + "SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",4,2)" + " >= " + "\'" + startDateMonth + "\'" + ";";

		} else if (Integer.compare(currentYearAsInt - 1, startDateYearAsInt) == 0) {

			/** case filtering across annaul limits */
			sqlCommandString = "SELECT * FROM " + Constants.CUSTOMER + " WHERE " + "(SUBSTR("
					+ Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + currentYear + "\'" + " AND " + "SUBSTR("
					+ Constants.CREATED_AT_DATA + ",4,2)" + " <= " + "\'" + currentMonth + "\')" + " OR " +
					/** last vear */
					"(SUBSTR(" + Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + startDateYear + "\'" + " AND "
					+ "SUBSTR(" + Constants.CREATED_AT_DATA + ",4,2)" + " >= " + "\'" + startDateMonth + "\'" + ");";
		}

		else if (Integer.compare(currentYearAsInt - 2, startDateYearAsInt) == 0) {

			/** case filtering across annaul limits */
			sqlCommandString = "SELECT * FROM " + Constants.CUSTOMER + " WHERE " + "(SUBSTR("
					+ Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + currentYear + "\'" + " AND " + "SUBSTR("
					+ Constants.CREATED_AT_DATA + ",4,2)" + " <= " + "\'" + currentMonth + "\')" + " OR " +
					/** two years ago */
					"(SUBSTR(" + Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'" + startDateYear + "\'" + " AND "
					+ "SUBSTR(" + Constants.CREATED_AT_DATA + ",4,2)" + " >= " + "\'" + startDateMonth + "\')" + " OR "
					+
					/** last year */
					"(SUBSTR(" + Constants.CREATED_AT_DATA + ",7,4)" + " = " + "\'"
					+ String.valueOf(currentYearAsInt - 1) + "\'" + ");";
		}

		List<String> response = new ArrayList<String>();
		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		if (resultSet != null) {
			while (resultSet.next()) {
				String createdAT = resultSet.getString(10);

				response.add(createdAT);
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return response;
	}

	/**
	 * yields the sellpositions belonging to contract's id and customer's id
	 * 
	 * @param idOfContract
	 *            - the contract's id
	 * @return sellpositions as list
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static List<SellPosition> getSellPositions(int idOfContract) throws SQLException {

		Statement statement = null;
		ResultSet resultSet = null;
		List<SellPosition> response = new ArrayList<SellPosition>();
		String sql = "SELECT * FROM " + Constants.SELL_POSITION + " WHERE " + Constants.CONTRACT_ID + "=" + "'"
				+ idOfContract + "'" + ";";
		connectInAutoCommitMode();
		statement = connection.getConnection().createStatement();
		resultSet = statement.executeQuery(sql);

		SellPosition sellPosition = null;

		if (resultSet != null) {
			while (resultSet.next()) {

				String ean = resultSet.getString(2);
				String articleName = resultSet.getString(3);
				int quantity = resultSet.getInt(4);
				String producer = resultSet.getString(5);
				Double uvp = resultSet.getDouble(6);
				Double housePrice = resultSet.getDouble(7);
				String articleNumber = resultSet.getString(8);

				sellPosition = new SellPosition(ean, quantity, articleName, producer, uvp, housePrice);

				sellPosition.setArticleNumber(articleNumber);

				response.add(sellPosition);
			}
		}
		resultSet.close();
		statement.close();
		disconnect();

		return response;
	}

	/**
	 * to check if a table exists
	 * 
	 * @param nameOfTable
	 *            - the name of the table
	 * @return true or false
	 * @throws SQLException
	 */
	public static boolean checkIfTableexists(String nameOfTable, String suffix) throws SQLException {
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name= '" + nameOfTable + suffix + "';";
		Statement statement = null;
		ResultSet resultset = null;
		boolean check = false;
		try {
			statement = connection.getConnection().createStatement();
			resultset = statement.executeQuery(sql);
			check = resultset.next();
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sql));
			e.printStackTrace();
			resultset.close();
			statement.close();
		} finally {
			resultset.close();
			statement.close();
		}
		return check;

	}

	/**
	 * to check if a table exists
	 * 
	 * @param nameOfTable
	 *            - the name of the table
	 * @return true or false
	 * @throws SQLException
	 */
	public static boolean checkIfTableexists(String nameOfTable) throws SQLException {
		String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name= '" + nameOfTable + "';";
		Statement statement = null;
		ResultSet resultset = null;
		boolean check = false;
		try {
			statement = connection.getConnection().createStatement();
			resultset = statement.executeQuery(sql);
			check = resultset.next();
			resultset.close();
			statement.close();
		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sql));
			e.printStackTrace();
			resultset.close();
			statement.close();
		} finally {
			resultset.close();
			statement.close();
		}
		return check;

	}

	/**
	 * yields the id of the contract (since doing auto increment, it's enough to
	 * select max)
	 * 
	 * @return the id of the contract
	 */
	public static int getContractId() {

		String sqlCommandString = "SELECT MAX(" + Constants.CONTRACT_ID + ") FROM " + Constants.CONTRACT + ";";
		Statement statement = null;
		ResultSet resultset = null;
		int response = 0;

		try {
			connectInAutoCommitMode();
			statement = connection.getConnection().createStatement();
			resultset = statement.executeQuery(sqlCommandString);
			response = resultset.getInt(1);
			disconnect();
		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}

			e.printStackTrace();
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

		return response;
	}

	/**
	 * yields the specific customer's id
	 * 
	 * @param customer
	 *            - the customer
	 * 
	 * @return the id of the customer
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static int getCustomersIDSpecific(Customer customer) throws SQLException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String sqlCommandString = "SELECT " + Constants.CUSTOMER_ID + " FROM " + Constants.CUSTOMER + " WHERE "
				+ Constants.CUSTOMER_NAME + "=" + "'" + CryptionUtils.encrypt(customer.getCustomerName()) + "'"
				+ " AND " + Constants.PRENAME + "=" + "'" + CryptionUtils.encrypt(customer.getPrename()) + "'" + " AND "
				+ Constants.STREET + "=" + "'" + CryptionUtils.encrypt(customer.getStreet()) + "'" + " AND "
				+ Constants.NUMBER + "=" + "'" + CryptionUtils.encrypt(customer.getNumber()) + "'" + " AND "
				+ Constants.POSTAL_CODE + " = " + "'" + CryptionUtils.encrypt(customer.getPostalCode()) + "'" + ";";

		connectInAutoCommitMode();
		Statement statement = null;
		ResultSet resultSet = null;
		int response = 0;

		try {
			statement = connection.getConnection().createStatement();
			resultSet = statement.executeQuery(sqlCommandString);

			if (resultSet != null) {
				while (resultSet.next()) {
					response = resultSet.getInt(1);
				}

			}
			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}

			e.printStackTrace();
		}

		disconnect();

		return response;

	}

	/**
	 * yields the id of the customer (since doing auto increment, it's enough to
	 * select max)
	 * 
	 * @return the id of the customer
	 */
	public static int getCustomerId() {

		String sqlCommandString = "SELECT MAX(" + Constants.CUSTOMER_ID + ") FROM " + Constants.CUSTOMER + ";";
		Statement statement = null;
		ResultSet resultset = null;
		int response = 0;

		try {
			connectInAutoCommitMode();
			statement = connection.getConnection().createStatement();
			resultset = statement.executeQuery(sqlCommandString);
			response = resultset.getInt(1);
			disconnect();
		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}

			e.printStackTrace();
		}

		return response;
	}

	/**
	 * imports the customers
	 * 
	 * @param fileOfCustomer
	 *            - the file of customers
	 * @throws IOException
	 *             - in case of technical error
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void importCustomers(File fileOfCustomer) throws IOException, SQLException {

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileOfCustomer)));
		List<Customer> lstCustomer = new ArrayList<Customer>();

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			lstCustomer.add(Utils.generateCustomerFromLine(line));
		}

		bufferedReader.close();

		connectInAutoCommitMode();

		for (Customer customer : lstCustomer) {
			insertIntoCustomerTable(customer);
		}

		disconnect();
	}

	/**
	 * imports the contracts
	 * 
	 * @param fileOfContracts
	 *            - the file of contracts
	 * @throws IOException
	 *             - in case of technical error
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void importContracts(File fileOfContracts) throws IOException, SQLException {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileOfContracts)));
		List<Contract> lstContract = new ArrayList<Contract>();

		String line;
		while ((line = bufferedReader.readLine()) != null) {

			lstContract.add(Utils.generateContractFromLine(line));
		}

		bufferedReader.close();

		connectInAutoCommitMode();

		for (Contract contract : lstContract) {
			insertIntoContractTable(contract);
		}

		disconnect();
	}

	/**
	 * yields the customers with a name 'like' regex
	 * 
	 * @param regex
	 *            - the regex
	 * @return the customers
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static List<Customer> getCustomerWithCustomerNameLike(String regex) throws SQLException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

		List<Customer> response = new ArrayList<Customer>();
		String sqlCommandString = "SELECT * FROM " + Constants.CUSTOMER;

		// + " WHERE " + Constants.CUSTOMER_NAME + " LIKE "
		// + "\'" + regex + "%\'";

		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		while (resultSet.next()) {

			int idOfCustomer = resultSet.getInt(1);
			String customerName = CryptionUtils.decrypt(resultSet.getString(2));
			String surname = CryptionUtils.decrypt(resultSet.getString(3));
			String street = CryptionUtils.decrypt(resultSet.getString(4));
			String number = CryptionUtils.decrypt(resultSet.getString(5));
			String postalCode = CryptionUtils.decrypt(resultSet.getString(6));
			String place = CryptionUtils.decrypt(resultSet.getString(7));
			String mobile = CryptionUtils.decrypt(resultSet.getString(8));
			String email = CryptionUtils.decrypt(resultSet.getString(9));
			String createdAt = resultSet.getString(10);
			String lastModifier = resultSet.getString(11);

			response.add(new Customer(idOfCustomer, customerName, surname, street, number, postalCode, place, mobile,
					email, createdAt, lastModifier));
		}

		response.removeIf(Utils.testLikePredicate(regex));

		disconnect();

		return response;
	}

	// get & set follows below here

	public static String getDataBaseName() {
		return dataBaseName;
	}

	/**
	 * to receive the actual connection
	 * 
	 * @return - the connection
	 */
	public DataBaseConnection getConnection() {
		return connection;
	}

	/**
	 * yields the saved frame heights
	 * 
	 * @return the frame heights
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static String[] getFrameHeights() throws SQLException {

		StringBuilder builder = new StringBuilder();
		String sqlCommandString = "SELECT * FROM " + Constants.FRAME_HEIGHT_DATA + ";";
		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		if (resultSet != null) {
			while (resultSet.next()) {
				String frameHeight = resultSet.getString(2);
				builder.append(frameHeight);
				builder.append(",");
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return builder.toString().split(",");
	}

	/**
	 * creates table seller
	 */
	public void createTableSeller() {
		/** sql command string to execute: */
		String sqlCommandString = "CREATE TABLE IF NOT EXISTS " + Constants.SELLER + " ( " + Constants.SELLER_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.PRENAME + " STRING DEFAULT (''), " + Constants.NAME
				+ " STRING DEFAULT (''), " + Constants.SELLERNUMBER_DATA + " CHAR DEFAULT (''), " + Constants.MOBILE
				+ " CHAR DEFAULT (''), " + " IMAGE BLOB, " + Constants.NAME_OF_IMAGE + " STRING DEFAULT ('') ,"
				+ "UNIQUE(" + Constants.PRENAME + "," + Constants.NAME + "," + Constants.SELLERNUMBER_DATA + ","
				+ Constants.MOBILE + "," + Constants.NAME_OF_IMAGE + ")" + ");";

		Statement statement = null;
		try {
			statement = connection.getConnection().createStatement();
			statement.executeUpdate(sqlCommandString);
			statement.close();

		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(ex.getMessage());
		}

	}

	public static void insertIntoTableSeller(String prename, String name, String sellerNumber, String mobile,
			byte[] byteArray, String nameOfImage) {

		String sqlCommandString = " INSERT OR IGNORE INTO " + Constants.SELLER + " values (?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = null;
		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setString(2, prename);
			preparedStatement.setString(3, name);
			preparedStatement.setString(4, sellerNumber);
			preparedStatement.setString(5, mobile);
			preparedStatement.setBytes(6, byteArray);
			preparedStatement.setString(7, nameOfImage);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();
			/** inform client */
			SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.SELLER_SAVED_MESSAGE, prename, name));

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * yields the sellers as list
	 * 
	 * @return - the sellers as list
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static List<Seller> getSellers() throws SQLException {
		List<Seller> response = new ArrayList<Seller>();
		Seller seller;
		String sqlCommandString = "SELECT * FROM " + Constants.SELLER + ";";
		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		if (resultSet != null) {
			while (resultSet.next()) {
				int id = resultSet.getInt(1);

				String prename = resultSet.getString(2);
				String name = resultSet.getString(3);
				String sellerNumber = resultSet.getString(4);
				String mobile = resultSet.getString(5);
				String nameOfImage = resultSet.getString(7);

				seller = new Seller(id, prename, name, sellerNumber, mobile, nameOfImage);

				response.add(seller);
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return response;
	}

	/**
	 * yields the id of the seller
	 * 
	 * @param seller
	 *            - the seller
	 * @return - the id of the seller
	 */
	public static int getIdOfSeller(Seller seller) {
		String sqlCommandString = "SELECT " + Constants.SELLER_ID + " FROM " + Constants.SELLER + " WHERE "
				+ Constants.PRENAME + "=" + "'" + seller.getPrename() + "'" + " AND " + Constants.NAME + "=" + "'"
				+ seller.getName() + "'" + " AND " + Constants.SELLERNUMBER_DATA + "=" + "'" + seller.getNumber() + "'"
				+ " AND " + Constants.MOBILE + "=" + "'" + seller.getMobile() + "'" + " AND " + Constants.NAME_OF_IMAGE
				+ "=" + "'" + seller.getNameOfImage() + "'" + ";";

		Statement statement = null;
		ResultSet resultset = null;
		int response = 0;

		try {
			connectInAutoCommitMode();
			statement = connection.getConnection().createStatement();
			resultset = statement.executeQuery(sqlCommandString);
			response = resultset.getInt(1);
			disconnect();
		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * updates table seller
	 * 
	 * @param prename
	 *            - the prename
	 * @param name
	 *            - the name
	 * @param number
	 *            - the number
	 * @param mobile
	 *            - the mobile
	 * @param byteArray
	 *            - the byte array
	 * @param nameOfPhoto
	 *            - the name of the photo
	 * @param id
	 *            - the id of the seller
	 */
	public static void updateTableSeller(String prename, String name, String number, String mobile, byte[] byteArray,
			String nameOfPhoto, int id) {

		String sqlCommandString = "UPDATE " + Constants.SELLER + " SET " + Constants.PRENAME + "="
				+ Constants.QUESTIONMARK + "," + Constants.NAME + "=" + Constants.QUESTIONMARK + ","
				+ Constants.SELLERNUMBER_DATA + "=" + Constants.QUESTIONMARK + "," + Constants.MOBILE + "="
				+ Constants.QUESTIONMARK + "," + Constants.IMAGE + "=" + Constants.QUESTIONMARK + ","
				+ Constants.NAME_OF_IMAGE + "=" + Constants.QUESTIONMARK + " WHERE " + Constants.SELLER_ID + "="
				+ Constants.QUESTIONMARK + ";";

		PreparedStatement preparedStatement = null;
		try {
			connectInAutoCommitMode();
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);
			preparedStatement.setString(1, prename);
			preparedStatement.setString(2, name);
			preparedStatement.setString(3, number);
			preparedStatement.setString(4, mobile);
			preparedStatement.setBytes(5, byteArray);
			preparedStatement.setString(6, nameOfPhoto);
			preparedStatement.setInt(7, id);
			preparedStatement.executeUpdate();
			preparedStatement.close();
			disconnect();
			/** inform client */
			SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
					Constants.SELLER_UPDATED_MESSAGE, prename, name));

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			System.out.println(e.getMessage());
		} finally {
			try {
				if (!connection.getConnection().isClosed()) {
					disconnect();
				}
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * yields the id of the seller
	 * 
	 * @param seller
	 *            - String: name, prename1,prename2,..., sellernummer
	 * @return the id of the seller
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static int getIdOfSeller(String receiving) throws SQLException {

		String[] sellerSplitted = receiving.split(Constants.EMPTY_SPACE);

		int length = sellerSplitted.length;

		String number = sellerSplitted[length - 1];

		StringBuilder builder = new StringBuilder();

		for (int position = 0; position < length - 2; position++) {
			builder.append(sellerSplitted[position].trim());
			builder.append(Constants.EMPTY_SPACE);
		}

		String prename = builder.toString().trim();
		String name = sellerSplitted[length - 2];

		String sqlCommandString = "SELECT " + Constants.SELLER_ID + " FROM " + Constants.SELLER + " WHERE "
				+ Constants.PRENAME + "=" + "'" + prename + "'" + " AND " + Constants.NAME + "=" + "'" + name + "'"
				+ " AND " + Constants.SELLERNUMBER_DATA + "=" + "'" + number + "'" + ";";

		connectInAutoCommitMode();
		Statement statement = null;
		ResultSet resultset = null;
		int response = 0;

		try {
			statement = connection.getConnection().createStatement();
			resultset = statement.executeQuery(sqlCommandString);
			response = resultset.getInt(1);
		} catch (SQLException e) {
			System.err.println(String.format("not able to execute query: %s", sqlCommandString));
			if (connection != null) {
				try {
					connection.getConnection().rollback();
				} catch (SQLException excep) {
				}
			}
			e.printStackTrace();
		}

		disconnect();
		return response;
	}

	/**
	 * yields the seller corresponding to the id
	 * 
	 * @param id
	 *            - the id of the seller
	 * @return the seller
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static Seller getSeller(int idOfSeller) throws SQLException {
		Seller response = null;
		String sqlCommandString = "SELECT * FROM " + Constants.SELLER + " WHERE " + Constants.SELLER_ID + "=" + "'"
				+ idOfSeller + "'" + ";";
		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		if (resultSet != null) {
			while (resultSet.next()) {

				String surname = resultSet.getString(2);
				String prename = resultSet.getString(3);
				String sellerNumber = resultSet.getString(4);
				String mobile = resultSet.getString(5);
				byte[] byteArray = resultSet.getBytes(6);

				response = new Seller(surname, prename, sellerNumber, mobile, byteArray);
			}
		}
		resultSet.close();
		statement.close();

		disconnect();

		return response;
	}

	/**
	 * creates the table for the erp data of the online shop
	 * 
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void createTableErpOnlineShopData() throws SQLException {

		/** sql command string to execute: */
		String sql = "CREATE TABLE IF NOT EXISTS " + Constants.ERP_ONLINESHOP + "(" + Constants.ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + Constants.EAN + " STRING DEFAULT (''), "
				+ Constants.ARTICLE_NAME + " STRING DEFAULT (''), " + Constants.UVP_ERP + " STRING DEFAULT (''), "
				+ Constants.SELLPRICE_FROM_ERP + " STRING DEFAULT (''), " + Constants.PRODUCER
				+ " STRING DEFAULT (''), " + Constants.PRODUCTS_ESL1 + " STRING DEFAULT (''), "
				+ Constants.PRODUCTS_ESL2 + " STRING DEFAULT (''), " + Constants.PRODUCTS_ESL3
				+ " STRING DEFAULT (''), " + Constants.PRODUCTS_URL_KEY + " STRING DEFAULT ('') " + ");";
		try {
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sql);
			statement.close();

		} catch (Exception ex) {
			System.err.println(String.format("not able to execute query: %s", sql));
			System.out.println(ex.getMessage());
		}

	}

	/**
	 * writes the erp data of online shop or all to the database scheme
	 * 
	 * @throws FileNotFoundException
	 *             - in case of technical error
	 * @throws IOException
	 *             - in case of technical error
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static void writeErpData() throws FileNotFoundException, IOException, SQLException {

		connectNotInAutoCommitMode();

		Charset charset = null;

		String path = null;

		path = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.ASSETS + File.separator
				+ Constants.ONLINESHOP + File.separator + Constants.ERP_CSV;
		charset = Charset.defaultCharset();

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path)), charset))) {
			String line;
			int counter = 0;
			long start = System.currentTimeMillis();
			PreparedStatement preparedStatement = null;
			String sqlCommandString = null;
			sqlCommandString = " INSERT INTO " + Constants.ERP_ONLINESHOP + " values (?,?,?,?,?,?,?,?,?,?)";
			preparedStatement = connection.getConnection().prepareStatement(sqlCommandString);

			while ((line = bufferedReader.readLine()) != null) {

				line = line.replace(";", "; ");
				String[] erpData = line.split(";");

				counter++;

				preparedStatement.setString(2, erpData[1].trim());
				preparedStatement.setString(3, erpData[2].trim());
				preparedStatement.setString(4, erpData[3].trim());
				preparedStatement.setString(5, erpData[4].trim());
				preparedStatement.setString(6, erpData[5].trim());
				preparedStatement.setString(7, erpData[6].trim());
				preparedStatement.setString(8, erpData[7].trim());
				preparedStatement.setString(9, erpData[8].trim());
				preparedStatement.addBatch();

				if (counter % 1000 == 0) {
					preparedStatement.executeBatch();
				}
			}
			preparedStatement.executeBatch();
			preparedStatement.close();
			double duration = System.currentTimeMillis() - start;
			duration = duration / 1000.0;

			System.err.println(String.format("INFO: %s datasets were written in %s seconds", counter, duration));
		}
		disconnectAndDoCommit();
	}

	/**
	 * yields the erpData identified with identifier from table
	 * 
	 * @param identifier
	 *            - the identifier
	 * @return the erpData identified with identifier
	 * @throws SQLException
	 *             - in case of technical error
	 */
	public static ErpDataSet getErpData(String identifier) throws SQLException {

		ErpDataSet response = null;
		String sqlCommandString = null;
		sqlCommandString = "SELECT * FROM " + Constants.ERP_ONLINESHOP + " WHERE " + Constants.EAN + " = " + "'"
				+ identifier + "'" + ";";

		connectInAutoCommitMode();
		Statement statement = connection.getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(sqlCommandString);

		if (resultSet != null) {
			while (resultSet.next()) {

				String articleName = resultSet.getString(3);
				String uvpErp = resultSet.getString(4);
				String sellPriceErp = resultSet.getString(5);
				String producer = resultSet.getString(6);
				String productsEsl1 = resultSet.getString(7);
				String productsEsl2 = resultSet.getString(8);
				String productsEsl3 = resultSet.getString(9);
				String productsUrlKey = resultSet.getString(10);

				response = new ErpDataSet(identifier, articleName, uvpErp, sellPriceErp, producer, productsEsl1,
						productsEsl2, productsEsl3, productsUrlKey);
			}
		}

		resultSet.close();
		statement.close();

		disconnect();

		return response;

	}

	/**
	 * does the housekeeping for the contracts
	 * 
	 * @param countOfYears
	 *            - the count of years
	 */
	public void doHouseKeepingOfContracts(String countOfYears) {

		Calendar calendar = Calendar.getInstance(Locale.GERMAN);
		Date current = calendar.getTime();
		String now = BikeContract.SIMPLE_DATE_FORMAT.format(current);
		String[] nowSplitted = now.split("\\.");
		String currentMonth = nowSplitted[1].trim();
		String currentYear = nowSplitted[2].trim();

		int years = 0;
		try {
			years = Integer.parseInt(countOfYears);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,
					String.format(Constants.INVALID_VALUE, Constants.HOUSEKEEPING_DATA, countOfYears));
			return;
		}

		calendar.add(Calendar.YEAR, -years);
		Date boundForHousekeeping = calendar.getTime();
		String houseKeepingBound = BikeContract.SIMPLE_DATE_FORMAT_TIME_2.format(boundForHousekeeping);
		String yearInPast = houseKeepingBound.substring(6, 10);

		Integer currentYearAsInt = Integer.valueOf(currentYear);
		Integer yearInPastAsInt = Integer.valueOf(yearInPast);

		String sqlCommandString = null;

		if (Integer.compare(currentYearAsInt, yearInPastAsInt) != 0) {

			/** case filtering across annual limits, housekeeping */
			sqlCommandString = "DELETE FROM " + Constants.CONTRACT + " WHERE " + "((SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",7,4)" + " < " + "\'" + yearInPastAsInt + "\')" + " OR " + "(SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",7,4)" + " = " + "\'" + yearInPastAsInt + "\'" + " AND " + "SUBSTR(" + Constants.CREATED_AT_DATA
					+ ",4,2)" + " < " + "\'" + currentMonth + "\')" + ");";
		}

		try {
			connectInAutoCommitMode();
			Statement statement = connection.getConnection().createStatement();
			statement.executeUpdate(sqlCommandString);
			statement.close();

			disconnect();
		} catch (SQLException e) {

			System.err.println(sqlCommandString);
			e.printStackTrace();
		}

	}

}
