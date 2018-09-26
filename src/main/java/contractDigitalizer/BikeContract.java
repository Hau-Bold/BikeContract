package contractDigitalizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import administration.Administration;
import administration.AdministrationForm;
import constants.Constants;
import constants.IntersportColors;
import constants.ShoreConstants;
import customer.Customer;
import customer.CustomerForm;
import customers.CustomersForm;
import database.DatabaseLogic;
import ftp.FtpClient;
import login.LoginForm;
import seller.SellerForm;
import shore.ShoreAppointmentsForm;
import utils.InformationProvider;
import utils.Utils;

/***
 * the class BikeContract
 *
 */
public class BikeContract extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** panel for start frame */
	private JPanel panelMain;
	/** actions for start frame */
	private JButton buttonNewCustomer, buttonShowCustomers, buttonEditSellers, buttonImportCustomerCSV,
			buttonShowAppointments, buttonAdministration;
	private static Administration administration;
	public static Map<String, String> access = null;
	private static Image image;
	public static DatabaseLogic database = null;
	private static String dataBaseName = Constants.BIKE_CONTRACT_DATA, directoryOfContractDigitalizer;

	/** customer's id */
	private static int idOfCustomer, idOfContract, idOfSeller;

	/** the customer and the correspond form */
	private static Customer customer;
	private CustomerForm customerForm;
	private static BikeContract instance = null;

	private static String dayOfWeek = null, service = null, time = null;
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT_TIME = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	public static final SimpleDateFormat SIMPLE_DATE_FORMAT_TIME_2 = new SimpleDateFormat("dd.MM.yyyy");
	public static Boolean isOnlineShop, isPasswordCorrect;
	private static shore.Customer shoreCustomer;

	public static BikeContract getInstance(String directory) throws FileNotFoundException, IOException, SQLException {

		directoryOfContractDigitalizer = directory;

		if (instance == null) {
			try {
				instance = new BikeContract(directory);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return instance;
	}

	/***
	 * Constructor.
	 * 
	 * @param directory
	 *            - the directory of the bike contract
	 * @throws IOException
	 *             - in case of technical error (reading access)
	 * @throws FileNotFoundException
	 *             - in case of technical error (reading access)
	 * @throws SQLException
	 *             - in case of technical error (Sql)
	 * @throws BadPaddingException
	 *             - in case of technical error (cryption)
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error (cryption)
	 * @throws NoSuchPaddingException
	 *             - in case of technical error (cryption)
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error (cryption)
	 * @throws InvalidKeyException
	 *             - in case of technical error (cryption)
	 */
	private BikeContract(String directory)
			throws FileNotFoundException, IOException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SQLException {

		BikeContract.directoryOfContractDigitalizer = directory;

		try {
			/** init the database, also values of administration are requested */
			initDataBase();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String smbpassword = Utils.getSmbPassword(BikeContract.getDirectoryOfBikeContract());

		access = Utils.readAccess(BikeContract.getDirectoryOfBikeContract());

		if (access == null) {

			try {
				Utils.requestAccess(Constants.PATH_TO_ACCESS_BIKECONTRACT_RELEASE, ShoreConstants.USER, smbpassword,
						directoryOfContractDigitalizer + File.separator + Constants.ASSETS + File.separator
								+ Constants.ACCESS);
			} catch (IOException e) {
				e.printStackTrace();
			}

			access = Utils.readAccess(BikeContract.getDirectoryOfBikeContract());

			Utils.clearAccess(BikeContract.getDirectoryOfBikeContract());

		}

		/** first of all the password has to be requested */

		Boolean usePassword = administration.getUsePassword();

		if (usePassword) {

			new Runnable() {

				@Override
				public void run() {
					Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
					LoginForm.getInstance(screenDimension.getWidth() / 2, screenDimension.getHeight() / 2,
							Boolean.FALSE, Boolean.FALSE).showFrame();
				}
			}.run();

			/** this parameter is set in modal dialog LoginForm */
			if (!isPasswordCorrect) {

				Runtime.getRuntime().exit(0);
			} else {
				/**
				 * since this parameter is used in several usecases, i.e like deleting contracts
				 * or update administration, it must be set to Boolean.False after each use
				 */

				isPasswordCorrect = Boolean.FALSE;
			}
		}

		try {
			executeAdministration(administration);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		setCustomLayOut();
		initComponent();

	}

	private void initComponent() {

		ImageIcon imageIcon = new ImageIcon(directoryOfContractDigitalizer + File.separator + Constants.ASSETS
				+ File.separator + Constants.IMAGE + File.separator + Constants.LOGO);

		image = imageIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		this.setIconImage(image);
		int[] location = Utils.getLocation(this);

		// the main frame
		this.setSize(Constants.WIDTH_MAINFRAME, (Constants.HEIGHT_MAINFRAME));
		this.setLocation(location[0], location[1]);
		this.setResizable(Boolean.FALSE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(new Dimension(Constants.MINIMAL_WIDTH_MAINFRAME, Constants.MINIMAL_HEIGHT_MAINFRAME));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(Constants.CONTRACT);

		panelMain = new JPanel(new GridLayout(1, 6));
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		buttonNewCustomer = new JButton(Constants.NEW_CUSTOMER);
		buttonNewCustomer.addActionListener(this);

		buttonShowCustomers = new JButton(Constants.SHOW_CUSTOMERS);
		buttonShowCustomers.addActionListener(this);

		buttonEditSellers = new JButton(Constants.EDIT_SELLERS);
		buttonEditSellers.addActionListener(this);

		buttonImportCustomerCSV = new JButton(Constants.CSV_IMPORT);
		buttonImportCustomerCSV.addActionListener(this);

		buttonShowAppointments = new JButton(Constants.SHOW_APPOINTMENTS);
		buttonShowAppointments.addActionListener(this);

		buttonAdministration = new JButton(Constants.ADMINISTRATION);
		buttonAdministration.addActionListener(this);

		panelMain.add(buttonNewCustomer);
		panelMain.add(buttonShowCustomers);
		panelMain.add(buttonEditSellers);
		panelMain.add(buttonImportCustomerCSV);
		panelMain.add(buttonShowAppointments);
		panelMain.add(buttonAdministration);

		this.getContentPane().add(panelMain);

		/**
		 * listens to window actions
		 */
		this.addWindowListener(new WindowAdapter() {

			@SuppressWarnings("unused")
			public void windowIsClosing() {
				System.exit(0);
			}

		});

	}

	/** Setting the Layout */
	private void setCustomLayOut() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}

	}

	/**
	 * initializes the database connection and its tables, also requests the erp
	 * data
	 * 
	 * @throws SQLException
	 *             in case of technical error
	 */
	private void initDataBase() throws SQLException {

		database = new DatabaseLogic(dataBaseName);

		DatabaseLogic.connectInAutoCommitMode();
		database.createTableLogin();
		database.createTableAdministration();
		DatabaseLogic.disconnect();

		administration = database.getAdministration();
		if (administration == null) {
			database.insertIntoAdministrationTable();
			administration = database.getAdministration();
		}

		DatabaseLogic.connectInAutoCommitMode();
		database.createTableCustomer();
		database.createTableContract();
		database.createTableSellPosition();
		database.createTableBikeData();
		database.createTableFrameHeight();
		database.initializeFrameHeights();
		database.createTableSeller();
		DatabaseLogic.disconnect();

		// database.dropTable(Constants.CUSTOMER);
		// database.dropTable(Constants.CONTRACT);
		// database.dropTable(Constants.SELL_POSITION);
		// database.dropTable(Constants.BikeData_DATA);
		// database.dropTable(Constants.FRAME_HEIGHT_DATA);
		// database.dropTable(Constants.SELLER);
		// database.dropTable(Constants.ERP_ONLINESHOP);
		// database.dropTable(Constants.LOGIN);
		// database.dropTable(Constants.ADMINISTRATION);
	}

	/**
	 * to execute the parameters of administration: read erp data and execute
	 * housekeeping
	 * 
	 * @param administration
	 *            - the administration
	 * @throws FileNotFoundException
	 *             - in case of technical error
	 * @throws IOException
	 *             - in case of technical error
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private void executeAdministration(Administration administration)
			throws FileNotFoundException, IOException, SQLException {

		String date = SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime());

		String erpLastReaded = administration.getErpLastReaded();
		String srcErpData = administration.getIsAll() ? Constants.ADVARICS : Constants.ONLINE_SHOP;

		if (!(erpLastReaded.equals(Constants.EMPTY_STRING))) {
			if ((!(date.equals(erpLastReaded)))) {
				/** date has changed, renew the last readed */
				erpLastReaded = date;

				if (srcErpData.equals(Constants.ONLINE_SHOP)) {
					/** read erp data from online shop again */

					isOnlineShop = Boolean.TRUE;

					administration.setErpLastReaded(erpLastReaded);

					FtpClient ftpClient = new FtpClient(BikeContract.getDirectoryOfBikeContract(),
							access.get(Constants.USER), access.get(Constants.PASSWORD),
							Integer.valueOf(access.get(Constants.PORT)), access.get(Constants.SERVER),
							access.get(Constants.DIRECTORY_REMOTE));

					ftpClient.establishFTPConnection();
					ftpClient.downloadFTPDirectory();

					DatabaseLogic.dropTable(Constants.ERP_ONLINESHOP);
					DatabaseLogic.connectInAutoCommitMode();
					DatabaseLogic.createTableErpOnlineShopData();
					DatabaseLogic.disconnect();
					DatabaseLogic.writeErpData();
				}

				else if (srcErpData.equals(Constants.ADVARICS)) {

					isOnlineShop = Boolean.FALSE;
				}

				else {
					new InformationProvider(Constants.ERROR, JOptionPane.ERROR_MESSAGE, Constants.ILLEGAL_CONFIGURATION,
							Constants.SRC_OF_ERP_DATA).run();
					System.exit(0);
				}
			} else {
				/** date hasn't changed */
				/** not necessary **/
				Boolean isAllused = srcErpData.equals(Constants.ADVARICS);
				Boolean isOnlineShopused = srcErpData.equals(Constants.ONLINE_SHOP);

				if (!isAllused && !isOnlineShopused) {
					new InformationProvider(Constants.ERROR, JOptionPane.ERROR_MESSAGE, Constants.ILLEGAL_CONFIGURATION,
							Constants.SRC_OF_ERP_DATA).run();
					Runtime.getRuntime().exit(0);
				} else {
					isOnlineShop = isOnlineShopused;
				}

			}

		} else {
			new InformationProvider(Constants.ERROR, JOptionPane.ERROR_MESSAGE, Constants.ILLEGAL_CONFIGURATION,
					Constants.ERP_LAST_READED).run();
			System.exit(0);
		}

		Boolean doHouseKeeping = administration.getUseHousekeeping();

		if (doHouseKeeping) {

			int housekeepingPdf = Integer.valueOf(administration.getHousekeepingPdf());
			int housekeepingContracts = Integer.valueOf(administration.getHousekeepingContracts());

			if (!(housekeepingPdf <= 0)) {
				Utils.doHouseKeepingOfPdfContracts(housekeepingPdf);
			} else {
				new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
						Constants.EXECUTING_HOUSEKEEPING_PDF_NOT_POSSIBLE, String.valueOf(housekeepingPdf)).run();

				return;
			}

			if (!(housekeepingContracts <= 0)) {
				database.doHouseKeepingOfContracts(String.valueOf(housekeepingContracts));
			} else {
				new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
						Constants.EXECUTING_HOUSEKEEPING_CONTRACTS_NOT_POSSIBLE, String.valueOf(housekeepingContracts))
								.run();

				return;
			}
		}

		/** renew administration each time */

		DatabaseLogic.updateAdministrationTable(administration, Boolean.TRUE);

	}

	/** setting the frame visible */
	public void showFrame() {

		this.setVisible(Boolean.TRUE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		/** new customer */
		if (o.equals(buttonNewCustomer)) {

			if (BikeContract.getCustomer() != null) {
				/** necessary for display the model in correct mode. */
				BikeContract.setCustomer(new Customer());
				CustomerForm.getModel().initialize();
				CustomerForm.getButtonNewContract().setText(Constants.NEW_CONTRACT);
				CustomerForm.getButtonNewContract().setEnabled(Boolean.FALSE);
			}
			customerForm = CustomerForm.getInstance(Boolean.FALSE);
			CustomerForm.removeSaveCustomerListener();
			CustomerForm.addSaveCustomerListener();
			customerForm.showFrame();
			this.setState(JFrame.ICONIFIED);

		}

		/** display customers */
		else if (o.equals(buttonShowCustomers)) {

			CustomersForm displayCustomers = CustomersForm.getInstance();
			displayCustomers.setState(JFrame.NORMAL);
			displayCustomers.showFrame();
			this.setState(JFrame.ICONIFIED);

		}

		else if (o.equals(buttonEditSellers)) {
			SellerForm displaySellers = SellerForm.getInstance();
			displaySellers.showFrame();
			this.setState(JFrame.ICONIFIED);

		}

		else if (o.equals(buttonImportCustomerCSV)) {

			JFileChooser fileChooser = new JFileChooser(
					BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.CUSTOMERS + File.separator);

			fileChooser.setDialogTitle(Constants.CSV_IMPORT_HEADER);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			String path = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.CUSTOMERS
					+ File.separator + Constants.CUSTOMERS_CSV;

			File fileOfImport = new File(path);

			fileChooser.setSelectedFile(fileOfImport);
			fileChooser.setBackground(Color.WHITE);

			int response = fileChooser.showOpenDialog(this);

			if (response == JFileChooser.APPROVE_OPTION) {

				Calendar calendar = Calendar.getInstance();
				Date date = calendar.getTime();

				try {
					DatabaseLogic.writeCustomerData(path, SIMPLE_DATE_FORMAT_TIME.format(date));

				} catch (IOException | SQLException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}

			}

			instance.setState(JFrame.NORMAL);

		}

		else if (o.equals(buttonShowAppointments)) {
			ShoreAppointmentsForm displayAppointments = ShoreAppointmentsForm.getInstance();
			displayAppointments.showFrame();
			this.setState(JFrame.ICONIFIED);
		}

		else if (o.equals(buttonAdministration)) {

			try {
				administration = database.getAdministration();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			AdministrationForm administrationForm = AdministrationForm.getInstance(administration);
			administrationForm.showFrame();
		}

	}

	// get & set follows below here

	public static int getIdOfCustomer() {
		return idOfCustomer;
	}

	public static void setIdOfCustomer(int idOfCustomer) {
		BikeContract.idOfCustomer = idOfCustomer;
	}

	public static void setCustomer(Customer customer) {
		BikeContract.customer = customer;

	}

	public static Customer getCustomer() {
		return customer;
	}

	public static shore.Customer getShoreCustomer() {
		return shoreCustomer;
	}

	public static void setShoreCustomer(shore.Customer shoreCustomer) {
		BikeContract.shoreCustomer = shoreCustomer;
	}

	public static void setIdOfContract(int idOfContract) {
		BikeContract.idOfContract = idOfContract;
	}

	public static int getIdOfContract() {
		return idOfContract;
	}

	public static Image getImage() {
		return image;
	}

	public static String getDirectoryOfBikeContract() {
		return directoryOfContractDigitalizer;
	}

	public static int getIdOfSeller() {
		return idOfSeller;
	}

	public static void setIdOfSeller(int idOfSeller) {
		BikeContract.idOfSeller = idOfSeller;
	}

	public static void setService(String service) {
		BikeContract.service = service;
	}

	public static String getService() {
		return service;
	}

	public static void setDayOfWeek(String dayOfWeek) {
		if (dayOfWeek.equals(ShoreConstants.MONDAY)) {
			BikeContract.dayOfWeek = "2";
		} else if (dayOfWeek.equals(ShoreConstants.TUESDAY)) {
			BikeContract.dayOfWeek = "3";
		} else if (dayOfWeek.equals(ShoreConstants.WEDNESDAY)) {
			BikeContract.dayOfWeek = "4";
		} else if (dayOfWeek.equals(ShoreConstants.THURSDAY)) {
			BikeContract.dayOfWeek = "5";
		} else if (dayOfWeek.equals(ShoreConstants.FRIDAY)) {
			BikeContract.dayOfWeek = "6";
		} else if (dayOfWeek.equals(ShoreConstants.SATURDAY)) {
			BikeContract.dayOfWeek = "7";
		} else if (dayOfWeek.equals(ShoreConstants.SUNDAY)) {
			BikeContract.dayOfWeek = "1";
		}
	}

	public static String getDayOfWeek() {
		return dayOfWeek;
	}

	public static void setTime(String time) {
		BikeContract.time = time;
	}

	public static String getTime() {
		return time;
	}

	public static BikeContract getInstance() {
		return instance;
	}

	public static void setAdministration(Administration administration) {

		BikeContract.administration = administration;
	}

	public static Administration getAdministration() {
		return administration;
	}

}
