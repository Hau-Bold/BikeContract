package bikeData;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import constants.Constants;
import constants.IntersportColors;
import contract.ContractForm;
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import seller.Seller;
import utils.CustomHeaderRenderer;
import utils.InformationProvider;
import utils.TableModel;
import utils.Utils;

/** the class BikeDataForm */
public class BikeDataForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static BikeDataForm instance = null;

	/** panels, table, model, actions for contract frame */
	private static JPanel panelMainUpperNorth, panelMainLower, panelMainUpperSouth;

	private JPanel panelNorth, panelMiddleNorth, panelMiddle, panelMiddleSouth, panelSouth,
			panelFrameNumerAndFrameHeight, panelInspection, panelMechanics;

	private static JTable table;
	private static TableModel modelTop, modelMiddle, modelDown;
	private static TableModelListener bikeDataTopModelListener, bikeDataMiddleModelListener, bikeDataDownModelListener;
	private static JTextField txtFrameNumber, txtMechanics, datePickerTextField;
	private JLabel labelFrameNumber, labelFrameHeight, labelFinishedDate, labelFinishedTime, labelMechanics;
	private DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
	private static JComboBox<String> frameHeightBox, appointmentBox;
	private static JButton buttonSaveBikeData, buttonClearView, buttonUpdateBikeData;
	private BikeData bikeData;
	private static List<Seller> sellers;
	private String[] sellersToDisplay;
	private Boolean newContractHasBeenEdited = Boolean.FALSE;
	private static String[] appointments;

	/** to listen to the selection of the monthbox */
	private static SelectionChangedListener selectionChangedListener;
	/** to listen to the specifiv values */

	/** to listen to the value of tectfields */
	private static DocumentListener updateTextFieldListener;

	static Boolean clientComesFromDisplayContractsForm;

	/**
	 * Constructor.
	 * 
	 * @param clientComesFromDisplayBikeDataForm
	 * 
	 * @param displayCustomer
	 * 			@throws
	 */
	private BikeDataForm(Boolean clientComesFromDisplayContractsForm, Boolean clientComesFromDisplayBikeDataForm) {

		try {
			initComponent(clientComesFromDisplayContractsForm);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * yields an instance of BikeDataForm
	 * 
	 * @param clientComesFromDisplayContractsForm
	 * @param clientComesFromDisplayBikeDataForm
	 * @throws SQLException
	 *             - in case of technical eror
	 */
	public static BikeDataForm getInstance(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) throws SQLException {

		BikeDataForm.clientComesFromDisplayContractsForm = clientComesFromDisplayContractsForm;
		sellers = DatabaseLogic.getSellers();

		if (instance == null) {
			instance = new BikeDataForm(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);
		} else {

			try {
				handleInstance(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static BikeDataForm getInstance() {
		return instance;
	}

	/**
	 * Instantiating Contract Frame and its components
	 * 
	 * @param clientComesFromDisplayContractsForm
	 * 
	 * @throws SQLException
	 *             in case of technical error
	 */
	private void initComponent(Boolean clientComesFromDisplayContractsForm) throws SQLException {

		try {
			appointments = Utils.loadAppointments();
		} catch (IOException e) {
			e.printStackTrace();
		}

		defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);

		this.setSize(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, Constants.HEIGHT_OF_BIKE_DATA);
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, Constants.HEIGHT_OF_BIKE_DATA));
		this.setIconImage(BikeContract.getImage());
		this.setTitle(Constants.SellerAndBikeData);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				if (buttonUpdateBikeData.isVisible()) {

					/** existing contract was edited */
					int choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.EXISTING_BIKEDATA_WAS_EDITED, BikeContract.getIdOfContract()),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {
						handleClosingOfInstance();

					} else {
						return;
					}
				}

				else if (!newContractHasBeenEdited) {
					JOptionPane.showConfirmDialog(null,
							String.format(Constants.BIKE_DATA_MUST_BE_SAVED, BikeContract.getIdOfContract()),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				} else {
					handleClosingOfInstance();
				}
			}

			private void handleClosingOfInstance() {

				instance.setVisible(Boolean.FALSE);
				ContractForm instanceOfContractForm = ContractForm.getInstance();
				instanceOfContractForm.setState(JFrame.NORMAL);
				instanceOfContractForm.showFrame();
				instance.dispose();
			}
		});

		panelMainLower = new JPanel(new BorderLayout());
		panelMainLower.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the Contract Frame */
		panelMainUpperNorth = new JPanel(new BorderLayout());
		panelMainUpperNorth.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 400));

		panelMainUpperSouth = new JPanel(new GridLayout(1, 7));
		panelMainUpperSouth.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 30));
		panelMainUpperSouth.setBackground(IntersportColors.INTERSPORT_TURQUOISE);
		panelMainUpperSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonSaveBikeData = new JButton(Constants.SAVE_BIKE_DATA);
		buttonSaveBikeData.addActionListener(this);

		buttonClearView = new JButton(Constants.CLEAR_VIEW);
		buttonClearView.addActionListener(this);

		buttonUpdateBikeData = new JButton(Constants.UPDATE_BIKE_DATA);
		buttonUpdateBikeData.setVisible(Boolean.FALSE);
		buttonUpdateBikeData.addActionListener(this);

		if (clientComesFromDisplayContractsForm) {
			/** a contract was selected */
			panelMainUpperSouth.add(buttonUpdateBikeData);
			panelMainUpperSouth.add(Utils.getEmptyPanel());

		} else {
			panelMainUpperSouth.add(buttonSaveBikeData);
			panelMainUpperSouth.add(buttonClearView);
		}
		panelMainUpperSouth.add(Utils.getEmptyPanel());
		panelMainUpperSouth.add(Utils.getEmptyPanel());
		panelMainUpperSouth.add(Utils.getEmptyPanel());
		panelMainUpperSouth.add(Utils.getEmptyPanel());
		panelMainUpperSouth.add(Utils.getEmptyPanel());

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth.setPreferredSize(new Dimension(40, 82));

		/** Instantiating the upper table */
		modelTop = new BikeDataTopModel(Constants.COLUMNS_OF_BIKE_DATA_TOP);
		modelTop.initialize();

		table = new JTable(modelTop);
		table.setPreferredScrollableViewportSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 400));
		table.getTableHeader().setReorderingAllowed(Boolean.FALSE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.setRowSelectionAllowed(Boolean.FALSE);
		table.setCellSelectionEnabled(Boolean.TRUE);

		TableColumn columnOfSeller = table.getColumnModel().getColumn(1);

		sellersToDisplay = getDisplayableResult(sellers);

		columnOfSeller.setCellEditor(new ComboboxCellEditor(sellersToDisplay));
		columnOfSeller.setCellRenderer(new ComboboxRenderer(sellersToDisplay));

		table.setDefaultRenderer(Object.class, new BikeStatusRenderer((BikeDataTopModel) modelTop));

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_GRAY));
		}

		panelNorth.add(new JScrollPane(table));

		/// panelNorth

		panelMiddle = new JPanel(new BorderLayout());
		panelMiddle.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 164));

		panelMiddleNorth = new JPanel(new BorderLayout());
		panelMiddleNorth.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 82));

		/** Instantiating the middle table */
		modelMiddle = new BikeDataMiddleModel(Constants.COLUMNS_OF_BIKE_DATA_MIDDLE);
		modelMiddle.initialize();

		table = new JTable(modelMiddle);
		table.setPreferredScrollableViewportSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 400));
		table.getTableHeader().setReorderingAllowed(Boolean.FALSE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.setRowSelectionAllowed(Boolean.FALSE);
		table.setCellSelectionEnabled(Boolean.TRUE);
		table.setDefaultRenderer(Object.class, new BikeTypeRenderer((BikeDataMiddleModel) modelMiddle));

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_GRAY));
		}

		panelMiddleNorth.add(new JScrollPane(table));

		panelMiddleSouth = new JPanel(new BorderLayout());
		panelMiddleSouth.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 82));

		panelFrameNumerAndFrameHeight = new JPanel();
		panelFrameNumerAndFrameHeight.setLayout(null);
		panelFrameNumerAndFrameHeight.setPreferredSize(new Dimension(20, 10));

		labelFrameNumber = new JLabel(Constants.FRAME_NUMBER + Constants.COLON);
		labelFrameNumber.setBounds(10, 27, 200, 25);

		txtFrameNumber = new JTextField();
		txtFrameNumber.setBounds(220, 27, 200, 25);

		labelFrameHeight = new JLabel(Constants.FRAME_HEIGHT + Constants.COLON);
		labelFrameHeight.setBounds(470, 27, 50, 25);

		// the combobox for selecting frameheight:
		frameHeightBox = new JComboBox<String>(DatabaseLogic.getFrameHeights());
		frameHeightBox.setBackground(Color.GREEN);
		frameHeightBox.setBounds(530, 27, 100, 25);
		frameHeightBox.setBorder(BorderFactory.createRaisedBevelBorder());

		panelFrameNumerAndFrameHeight.add(labelFrameNumber);
		panelFrameNumerAndFrameHeight.add(txtFrameNumber);

		panelFrameNumerAndFrameHeight.add(labelFrameHeight);
		panelFrameNumerAndFrameHeight.add(frameHeightBox);

		labelFinishedDate = new JLabel(Constants.FINISHED_AT_DATE + Constants.COLON);
		labelFinishedDate.setBounds(640, 27, 70, 25);
		panelFrameNumerAndFrameHeight.add(labelFinishedDate);

		/** the date picker */
		UtilDateModel model = new UtilDateModel();
		Properties properties = new Properties();
		properties.put("text.today", "heute");
		properties.put("text.month", "monat");
		properties.put("text.year", "jahr");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, properties);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateComponentFormatter());
		datePicker.setBounds(720, 27, 200, 30);
		JButton changeButton = (JButton) datePicker.getComponent(1);
		changeButton.setText(Constants.DATE);
		changeButton.setPreferredSize(new Dimension(100, 25));

		datePickerTextField = datePicker.getJFormattedTextField();
		datePickerTextField.setEditable(Boolean.FALSE);
		datePickerTextField.setBackground(IntersportColors.INTERSPORT_WHITE);

		Calendar calendar = Calendar.getInstance();
		String dayOfWeek = BikeContract.getDayOfWeek();

		if (dayOfWeek != null) {
			int today = calendar.get(Calendar.DAY_OF_WEEK);
			int dayOfAppointment = Integer.valueOf(dayOfWeek);

			if (dayOfAppointment <= today) {
				calendar.add(Calendar.DAY_OF_WEEK, -(today - dayOfAppointment));
			}
			/** the day of week from shore */

		}

		datePickerTextField.setText(BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime()));
		datePicker.getJFormattedTextField().setPreferredSize(new Dimension(100, 25));
		panelFrameNumerAndFrameHeight.add(datePicker);

		/** label for appointment */
		labelFinishedTime = new JLabel(Constants.FINISHED_AT_TIME + Constants.COLON);
		labelFinishedTime.setBounds(930, 27, 70, 25);
		panelFrameNumerAndFrameHeight.add(labelFinishedTime);

		// the combobox for selecting appointments:
		appointmentBox = new JComboBox<String>(appointments);
		appointmentBox.setBackground(Color.GREEN);
		appointmentBox.setBounds(1000, 27, 100, 25);
		appointmentBox.setBorder(BorderFactory.createRaisedBevelBorder());
		String time = BikeContract.getTime();
		if (time != null) {
			time = Utils.checkTime(time);
			appointmentBox.setSelectedIndex(getIndexOfAppointment(time));
		}
		panelFrameNumerAndFrameHeight.add(appointmentBox);

		panelMiddleSouth.add(panelFrameNumerAndFrameHeight, BorderLayout.CENTER);

		panelMiddle.add(panelMiddleNorth, BorderLayout.NORTH);
		panelMiddle.add(panelMiddleSouth, BorderLayout.SOUTH);

		panelSouth = new JPanel(new BorderLayout());
		panelSouth.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 162));

		panelInspection = new JPanel(new BorderLayout());
		panelInspection.setPreferredSize(new Dimension(15, 80));

		/** Instantiating the table */
		modelDown = new BikeDataDownModel(Utils.generateBikeDataDowmColumns(bikeData));
		modelDown.initialize();

		table = new JTable(modelDown);
		table.setPreferredScrollableViewportSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME, 400));
		table.getTableHeader().setReorderingAllowed(Boolean.FALSE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.setRowSelectionAllowed(Boolean.FALSE);
		table.setCellSelectionEnabled(Boolean.TRUE);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_GRAY));
		}

		table.setDefaultRenderer(Object.class, new InspectionYearRenderer((BikeDataDownModel) modelDown));

		panelInspection.add(new JScrollPane(table), BorderLayout.NORTH);
		panelSouth.add(panelInspection, BorderLayout.NORTH);

		/**
		 * mechanics
		 */
		panelMechanics = new JPanel();
		panelMechanics.setLayout(null);
		panelMechanics.setPreferredSize(new Dimension(Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME / 2, 81));

		labelMechanics = new JLabel(Constants.MECHANIC + Constants.COLON);
		labelMechanics.setBounds(20, 20, 110, 25);
		panelMechanics.add(labelMechanics);

		txtMechanics = new JTextField();
		txtMechanics.setBounds(100, 20, 200, 25);

		panelMechanics.add(txtMechanics);
		panelSouth.add(panelMechanics, BorderLayout.SOUTH);

		panelMainUpperNorth.add(panelNorth, BorderLayout.NORTH);
		panelMainUpperNorth.add(panelMiddle, BorderLayout.CENTER);
		panelMainUpperNorth.add(panelSouth, BorderLayout.SOUTH);

		panelMainLower.add(panelMainUpperNorth, BorderLayout.NORTH);
		panelMainLower.add(panelMainUpperSouth, BorderLayout.SOUTH);

		this.getContentPane().add(panelMainLower);

		if (clientComesFromDisplayContractsForm) {
			/**
			 * a contract was selected, hence there could be some information about the bike
			 */

			bikeData = DatabaseLogic.getBikeData(BikeContract.getIdOfContract());

			if (bikeData != null) {
				setModelTopData(bikeData);

				setModelMiddleData(bikeData);

				setModelDownData(bikeData);

				txtMechanics.setText(bikeData.getMechanic());
				updateTextFieldListener = new UpdateTextFieldListener();
				txtMechanics.getDocument().addDocumentListener(updateTextFieldListener);

				selectionChangedListener = new SelectionChangedListener();

				txtFrameNumber.setText(bikeData.getFrameNumber());
				txtFrameNumber.getDocument().addDocumentListener(updateTextFieldListener);

				frameHeightBox.setSelectedIndex(getIndexOfFrameHeight(bikeData.getFrameHeight()));
				frameHeightBox.addItemListener(selectionChangedListener);
				datePickerTextField.setText(bikeData.getDateOfAppointment());
				appointmentBox.setSelectedIndex(getIndexOfAppointment(bikeData.getTimeOfAppointment()));

				datePickerTextField.getDocument().addDocumentListener(updateTextFieldListener);
				appointmentBox.addItemListener(selectionChangedListener);

			} else {
				modelTop.initialize();
				modelMiddle.initialize();
				modelDown.initialize();

				txtFrameNumber.setText(Constants.EMPTY_STRING);
				txtMechanics.setText(Constants.EMPTY_STRING);
			}

			addUpdateBikeDataTopListener();
			addUpdateBikeDataMiddleListener();
			addUpdateBikeDataDownListener();

		}

	}

	/**
	 * yields the sellers in the form they are represented in the cbx of sellers.
	 * 
	 * @param sellers
	 *            - the sellers
	 * @return edited sellers in a list
	 */
	private String[] getDisplayableResult(List<Seller> sellers) {

		StringBuilder builder = new StringBuilder();
		sellers.forEach(seller -> builder.append(seller.getPrename() + Constants.EMPTY_SPACE + seller.getName()
				+ Constants.EMPTY_SPACE + seller.getNumber() + Constants.SEMICOLON));

		return builder.toString().split(Constants.SEMICOLON);
	}

	/**
	 * yields the index of the frameheight
	 * 
	 * @param frameHeight
	 *            - the frameheight
	 * @return the index
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static int getIndexOfFrameHeight(String frameHeight) throws SQLException {

		int counter = 0;

		String[] frameHeights = DatabaseLogic.getFrameHeights();

		for (String frameHeight_tmp : frameHeights) {
			if (!frameHeight_tmp.equals(frameHeight)) {
				counter++;
			} else {
				break;
			}
		}

		return counter;
	}

	/**
	 * yields the index of the appointment
	 * 
	 * @param appointment
	 *            - the appointment
	 * @return - the index
	 */
	private static int getIndexOfAppointment(String appointment) {
		int counter = 0;
		Boolean indexExists = Boolean.FALSE;

		for (String appointments_tmp : appointments) {
			if (!appointments_tmp.equals(appointment)) {
				counter++;
			} else {
				indexExists = Boolean.TRUE;
				break;
			}
		}

		if (!indexExists) {
			counter = 0;
		}

		return counter;
	}

	/**
	 * setting contract's data in the model
	 * 
	 * @param bikeData
	 * 
	 * @param customer
	 *            - the customer
	 */
	public static void setModelTopData(BikeData bikeData) {

		/** set values of first panel */

		String idOfSeller = bikeData.getSeller();

		Seller seller = null;
		try {
			seller = DatabaseLogic.getSeller(Integer.valueOf(idOfSeller));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		modelTop.setValueAt(bikeData.getDate(), 0, 0);

		modelTop.setValueAt(seller.getPrename() + Constants.EMPTY_SPACE + seller.getName() + Constants.EMPTY_SPACE
				+ seller.getNumber(), 0, 1);
		modelTop.setValueAt(Integer.compare(bikeData.getDelivery(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 2);
		modelTop.setValueAt(Integer.compare(bikeData.getCollection(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 3);
		modelTop.setValueAt(Integer.compare(bikeData.getTakeAway(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 4);
		modelTop.setValueAt(Integer.compare(bikeData.getBooked(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 5);
		modelTop.fireTableDataChanged();

	}

	private static void setModelMiddleData(BikeData bikeData) {
		/** set values of second panel */
		modelMiddle.setValueAt(Integer.compare(bikeData.getNew_Bike(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 1);
		modelMiddle.setValueAt(Integer.compare(bikeData.getUsed_Bike(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 2);
		modelMiddle.setValueAt(Integer.compare(bikeData.getTrekking(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 3);
		modelMiddle.setValueAt(Integer.compare(bikeData.getCityTour(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 4);
		modelMiddle.setValueAt(Integer.compare(bikeData.getMountainBike(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0,
				5);
		modelMiddle.setValueAt(Integer.compare(bikeData.getChildsBike(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 6);
		modelMiddle.setValueAt(Integer.compare(bikeData.getRacingCycle(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 7);
		modelMiddle.setValueAt(Integer.compare(bikeData.getCross(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 8);
		modelMiddle.setValueAt(Integer.compare(bikeData.getBmx(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 9);
		modelMiddle.setValueAt(Integer.compare(bikeData.geteBike(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 10);
		modelMiddle.setValueAt(Integer.compare(bikeData.getOthers(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE, 0, 11);
		modelMiddle.fireTableDataChanged();

	}

	private static void setModelDownData(BikeData bikeData) {
		/** set values of third panel */
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);

		int year = bikeData.getYear();
		if (Integer.compare(year, 0) != 0) {

			if (year < currentYear) {
				modelDown.setValueAt(Boolean.TRUE, 0, 1);
				modelDown.fireTableDataChanged();
			} else {
				int offset = year - currentYear;
				modelDown.setValueAt(Boolean.TRUE, 0, 1 + offset);
				modelDown.fireTableDataChanged();
			}

		}
	}

	/** setting the frame visible */
	public void showFrame() {

		this.setVisible(Boolean.TRUE);
	}

	/**
	 * to handle this instance depending on whether or not a contract was selected
	 * 
	 * @param clientComesFromDisplayBikeDataForm
	 * @param clientComesFromDisplayContractsForm
	 * 
	 * @param clientComesFromDisplayContractsForm
	 *            - clientComesFromDisplayContractsForm
	 * @param
	 * @throws SQLException
	 *             - in case of technical error
	 */
	private static void handleInstance(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) throws SQLException {

		if (!clientComesFromDisplayContractsForm && !clientComesFromDisplayBikeDataForm) {
			/** establishing a new contract */

			// 1) setting values:
			modelTop.initialize();
			modelMiddle.initialize();
			modelDown.initialize();
			txtFrameNumber.setText(Constants.EMPTY_STRING);
			txtMechanics.setText(Constants.EMPTY_STRING);
			frameHeightBox.setSelectedIndex(0);

			String time = BikeContract.getTime();

			if (time != null) {
				time = Utils.checkTime(time);
				appointmentBox.setSelectedIndex(getIndexOfAppointment(time));
			} else {
				appointmentBox.setSelectedIndex(0);
			}

			// 2) No listeners for updates are needed

			// 3) handling the action panel:
			panelMainLower.removeAll();

			panelMainUpperSouth = getActionPanel(clientComesFromDisplayContractsForm,
					clientComesFromDisplayBikeDataForm);
			panelMainLower.add(panelMainUpperNorth, BorderLayout.NORTH);
			panelMainLower.add(panelMainUpperSouth, BorderLayout.SOUTH);
			panelMainLower.revalidate();

		}

		else if (clientComesFromDisplayContractsForm && (!clientComesFromDisplayBikeDataForm)) {

			/** an existing contract was selected */

			// 1) setting values:
			BikeData bikeData = DatabaseLogic.getBikeData(BikeContract.getIdOfContract());

			if (bikeData != null) {
				setModelTopData(bikeData);
				setModelMiddleData(bikeData);
				setModelDownData(bikeData);
			} else {
				modelTop.initialize();
				modelMiddle.initialize();
				modelDown.initialize();
			}

			// 2) adding listeners for updates

			addUpdateBikeDataTopListener();
			addUpdateBikeDataMiddleListener();
			addUpdateBikeDataDownListener();

			if (updateTextFieldListener != null) {
				txtFrameNumber.getDocument().removeDocumentListener(updateTextFieldListener);
				txtMechanics.getDocument().removeDocumentListener(updateTextFieldListener);
				datePickerTextField.getDocument().removeDocumentListener(updateTextFieldListener);
				updateTextFieldListener = null;
				updateTextFieldListener = new UpdateTextFieldListener();
			}

			if (bikeData != null) {
				txtFrameNumber.setText(bikeData.getFrameNumber());
				txtMechanics.setText(bikeData.getMechanic());
				datePickerTextField.setText(bikeData.getDateOfAppointment());
			} else {
				txtFrameNumber.setText(Constants.EMPTY_STRING);
				txtMechanics.setText(Constants.EMPTY_STRING);
				datePickerTextField.setText(Constants.EMPTY_STRING);
			}

			txtFrameNumber.getDocument().addDocumentListener(updateTextFieldListener);
			txtMechanics.getDocument().addDocumentListener(updateTextFieldListener);
			datePickerTextField.getDocument().addDocumentListener(updateTextFieldListener);

			frameHeightBox.removeItemListener(selectionChangedListener);
			appointmentBox.removeItemListener(selectionChangedListener);
			selectionChangedListener = null;

			if (bikeData != null) {
				frameHeightBox.setSelectedIndex(getIndexOfFrameHeight(bikeData.getFrameHeight()));
				appointmentBox.setSelectedIndex(getIndexOfAppointment(bikeData.getTimeOfAppointment()));
			} else {
				frameHeightBox.setSelectedIndex(0);
				appointmentBox.setSelectedIndex(0);
			}

			selectionChangedListener = new SelectionChangedListener();
			frameHeightBox.addItemListener(selectionChangedListener);

			// 3) handling the action panel:
			panelMainLower.removeAll();

			panelMainUpperSouth = getActionPanel(clientComesFromDisplayContractsForm,
					clientComesFromDisplayBikeDataForm);
			panelMainLower.add(panelMainUpperNorth, BorderLayout.NORTH);
			panelMainLower.add(panelMainUpperSouth, BorderLayout.SOUTH);
			panelMainLower.revalidate();
		}

		else if (clientComesFromDisplayContractsForm || clientComesFromDisplayBikeDataForm) {

			/** handle it like an existing contract was selected */

			// 1) setting values:
			BikeData bikeData = DatabaseLogic.getBikeData(BikeContract.getIdOfContract());

			if (bikeData != null) {
				setModelTopData(bikeData);
				setModelMiddleData(bikeData);
				setModelDownData(bikeData);
			}

			// 2) adding listeners for updates

			addUpdateBikeDataTopListener();
			addUpdateBikeDataMiddleListener();
			addUpdateBikeDataDownListener();

			if (updateTextFieldListener != null) {
				txtFrameNumber.getDocument().removeDocumentListener(updateTextFieldListener);
				txtMechanics.getDocument().removeDocumentListener(updateTextFieldListener);
				datePickerTextField.getDocument().removeDocumentListener(updateTextFieldListener);
				updateTextFieldListener = null;
			}
			updateTextFieldListener = new UpdateTextFieldListener();

			if (bikeData != null) {
				txtFrameNumber.setText(bikeData.getFrameNumber());
				txtMechanics.setText(bikeData.getMechanic());
			}

			txtFrameNumber.getDocument().addDocumentListener(updateTextFieldListener);
			txtMechanics.getDocument().addDocumentListener(updateTextFieldListener);
			datePickerTextField.getDocument().addDocumentListener(updateTextFieldListener);

			frameHeightBox.removeItemListener(selectionChangedListener);
			appointmentBox.removeItemListener(selectionChangedListener);

			selectionChangedListener = null;
			if (bikeData != null) {
				frameHeightBox.setSelectedIndex(getIndexOfFrameHeight(bikeData.getFrameHeight()));
				appointmentBox.setSelectedIndex(getIndexOfAppointment(bikeData.getTimeOfAppointment()));
			}
			selectionChangedListener = new SelectionChangedListener();

			frameHeightBox.addItemListener(selectionChangedListener);
			appointmentBox.addItemListener(selectionChangedListener);

			// 3) handling the action panel:
			panelMainLower.removeAll();

			panelMainUpperSouth = getActionPanel(clientComesFromDisplayContractsForm,
					clientComesFromDisplayBikeDataForm);
			panelMainLower.add(panelMainUpperNorth, BorderLayout.NORTH);
			panelMainLower.add(panelMainUpperSouth, BorderLayout.SOUTH);
			panelMainLower.revalidate();

		}

	}

	private static JPanel getActionPanel(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) {

		JPanel actionPanel = new JPanel(new GridLayout(1, 7));
		actionPanel.setBackground(IntersportColors.INTERSPORT_TURQUOISE);

		if ((!clientComesFromDisplayContractsForm) && (!clientComesFromDisplayBikeDataForm)) {
			/** establishing a new Contract */
			actionPanel.add(buttonSaveBikeData);
			actionPanel.add(buttonClearView);
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
		}

		else if (clientComesFromDisplayContractsForm && !(clientComesFromDisplayBikeDataForm)) {

			/** an existing contract was selected */

			if (buttonUpdateBikeData.isVisible()) {
				buttonUpdateBikeData.setVisible(Boolean.FALSE);
			}

			actionPanel.add(buttonUpdateBikeData);
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
		}

		else if (clientComesFromDisplayContractsForm || clientComesFromDisplayBikeDataForm) {

			/** form was closed and should be reopended */

			if (buttonUpdateBikeData.isVisible()) {
				buttonUpdateBikeData.setVisible(Boolean.FALSE);
			}

			actionPanel.add(buttonUpdateBikeData);
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(Utils.getEmptyPanel());
		}

		return actionPanel;
	}

	/**
	 * to add the bikeDataTopModelListener
	 */
	public static void addUpdateBikeDataTopListener() {
		removeBikeDataTopListener();
		bikeDataTopModelListener = new UpDateBikeDataTopModelListener();
		modelTop.addTableModelListener(bikeDataTopModelListener);
	}

	/**
	 * to remove the bikeDataTopModelListener
	 */
	private static void removeBikeDataTopListener() {

		if (bikeDataTopModelListener != null) {
			modelTop.removeTableModelListener(bikeDataTopModelListener);
			bikeDataTopModelListener = null;
		}
	}

	/**
	 * to add the bikeDataMiddleModelListener
	 */
	public static void addUpdateBikeDataMiddleListener() {
		removeBikeDataMiddleListener();
		bikeDataMiddleModelListener = new UpDateBikeDataMiddleModelListener(instance);
		modelMiddle.addTableModelListener(bikeDataMiddleModelListener);
	}

	/**
	 * to remove the bikeDataMiddleModelListener
	 */
	private static void removeBikeDataMiddleListener() {

		if (bikeDataMiddleModelListener != null) {
			modelMiddle.removeTableModelListener(bikeDataMiddleModelListener);
			bikeDataMiddleModelListener = null;
		}
	}

	/**
	 * to add the bikeDataDownModelListener
	 */
	public static void addUpdateBikeDataDownListener() {
		removeBikeDataDownListener();

		bikeDataDownModelListener = new UpDateBikeDataDownModelListener(instance);
		modelDown.addTableModelListener(bikeDataDownModelListener);
	}

	/**
	 * to remove the bikeDataDownModelListener
	 */
	private static void removeBikeDataDownListener() {

		if (bikeDataDownModelListener != null) {
			modelDown.removeTableModelListener(bikeDataDownModelListener);
			bikeDataDownModelListener = null;
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if ((o.equals(buttonSaveBikeData)) || (o.equals(buttonUpdateBikeData))) {

			/** reading Data from ModelTop */
			String date = (String) modelTop.getValueAt(0, 0);
			String seller = (String) modelTop.getValueAt(0, 1);

			Boolean delivery = (Boolean) modelTop.getValueAt(0, 2);// fixed value
			Boolean collection = (Boolean) modelTop.getValueAt(0, 3);// fixed value
			Boolean takeAway = (Boolean) modelTop.getValueAt(0, 4);// fixed value
			Boolean booked = (Boolean) modelTop.getValueAt(0, 5);// fixed value

			/** reading Data from ModelMiddle */
			Boolean bikeIsNew = (Boolean) modelMiddle.getValueAt(0, 1);// fixed value
			Boolean used = (Boolean) modelMiddle.getValueAt(0, 2);// fixed value
			Boolean trekking = (Boolean) modelMiddle.getValueAt(0, 3);// fixed value
			Boolean city_Tour = (Boolean) modelMiddle.getValueAt(0, 4);// fixed value
			Boolean mountainBike = (Boolean) modelMiddle.getValueAt(0, 5);// fixed value
			Boolean childs_Bike = (Boolean) modelMiddle.getValueAt(0, 6);// fixed value
			Boolean racing_Cycle = (Boolean) modelMiddle.getValueAt(0, 7);// fixed value
			Boolean cross = (Boolean) modelMiddle.getValueAt(0, 8);// fixed value
			Boolean bmx = (Boolean) modelMiddle.getValueAt(0, 9);// fixed value
			Boolean e_Bike = (Boolean) modelMiddle.getValueAt(0, 10);// fixed value
			Boolean others = (Boolean) modelMiddle.getValueAt(0, 11);// fixed value

			/** reading frame number */
			String frameNumber = txtFrameNumber.getText();

			/** reading year of inspection */
			int yearOfInspection = Utils.getYearOfInspection((BikeDataDownModel) modelDown);

			/** reading frame height */
			String frameHeight = frameHeightBox.getItemAt(frameHeightBox.getSelectedIndex());

			/** reading mechanics */
			String mechanics = txtMechanics.getText();

			/** the time of the appointment */
			String timeOfAppointment = (String) appointmentBox.getSelectedItem();

			/** the date of the appointment */
			String dateOfAppointment = datePickerTextField.getText();

			Date dateOfAppointmentAsDate = null;

			if (!timeOfAppointment.equals(Constants.MINUS)) {
				try {
					dateOfAppointmentAsDate = BikeContract.SIMPLE_DATE_FORMAT.parse(dateOfAppointment);
					String[] timeOfAppointmentSplitted = timeOfAppointment.split(":");

					dateOfAppointmentAsDate.setHours(Integer.valueOf(timeOfAppointmentSplitted[0]));
					dateOfAppointmentAsDate.setMinutes(
							Integer.valueOf(timeOfAppointmentSplitted[1] == "00" ? "0" : timeOfAppointmentSplitted[1]));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			/** adjusting values */
			/** first panel */
			seller = seller == null ? Constants.EMPTY_STRING : seller;

			int delivery_Data = delivery ? 1 : 0;
			int collection_Data = collection ? 1 : 0;
			int takeAway_Data = takeAway ? 1 : 0;
			int booked_Data = booked ? 1 : 0;
			/** second panel */
			int bikeIsNew_Data = bikeIsNew ? 1 : 0;
			int used_Data = used ? 1 : 0;
			int trekking_Data = trekking ? 1 : 0;
			int city_Tour_Data = city_Tour ? 1 : 0;
			int mountainBike_Data = mountainBike ? 1 : 0;
			int childs_Bike_Data = childs_Bike ? 1 : 0;
			int racing_Cycle_Data = racing_Cycle ? 1 : 0;
			int cross_Data = cross ? 1 : 0;
			int bmx_Data = bmx ? 1 : 0;
			int e_Bike_Data = e_Bike ? 1 : 0;
			int others_Data = others ? 1 : 0;

			frameNumber = frameNumber == null ? Constants.EMPTY_STRING : frameNumber;

			// <error handling: values not set up correctly>
			if (seller.equals(Constants.EMPTY_STRING)) {
				/** handling seller */
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.SELLER_IS_MISSING)
						.run();
				return;
			}

			if (!delivery && !collection && !takeAway && !booked) {
				/** handling status */
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.STATUS_IS_MISSING)
						.run();
				return;
			}

			else if (frameNumber.equals(Constants.EMPTY_STRING)) {
				/** handling frame numer */
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.FRAMENUMBER_IS_MISSING)
						.run();
				return;
			} else if (!(bikeIsNew || used)) {
				/** handling age of bike */
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.AGE_OF_BIKE_IS_MISSING)
						.run();
				return;
			}

			else if (Integer.compare(trekking_Data + city_Tour_Data + mountainBike_Data + childs_Bike_Data
					+ racing_Cycle_Data + cross_Data + bmx_Data + e_Bike_Data + others_Data, 0) == 0) {
				/** handling type of bike */
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE,
						Constants.PROPERTY_OF_BIKE_IS_MISSING).run();
				return;

			}

			else if (!(Integer.compare(yearOfInspection, 0) == 0) || !(mechanics.equals(Constants.EMPTY_STRING))) {
				/** handling inspection */

				Boolean isInspectionSetupCorrectly = (!(Integer.compare(yearOfInspection, 0) == 0))
						&& (!mechanics.equals(Constants.EMPTY_STRING));

				if (!isInspectionSetupCorrectly) {

					String yearOfInspectionAsString = String.valueOf(yearOfInspection);

					new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE,
							Constants.INSPECTION_SETUP_INCORRECT, yearOfInspectionAsString, mechanics).run();

					return;
				}

			}

			else if (timeOfAppointment.equals(Constants.MINUS)) {
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.TIMEPOINT_INCORRECT)
						.run();
				return;
			}

			else if (frameHeight.equals(Constants.MINUS)) {
				new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.FRAME_HEIGHT_INCORRECT)
						.run();
				return;
			}

			else if (dateOfAppointmentAsDate != null) {
				/** handling date of appointment */

				if (!clientComesFromDisplayContractsForm
						&& dateOfAppointmentAsDate.before(Calendar.getInstance().getTime())) {
					/** if date of appointment is in past for new contract */
					new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE,
							Constants.APPOINTMENT_DATE_INCORRECT, dateOfAppointment, timeOfAppointment).run();
					return;
				}

			}

			// </error handling>
			int idOfSeller = 0;
			try {
				idOfSeller = DatabaseLogic.getIdOfSeller(seller);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			BikeContract.setIdOfSeller(idOfSeller);

			try {

				if (o.equals(buttonSaveBikeData)) {

					/** writing data */
					DatabaseLogic.insertIntoBikeDataTable(date, idOfSeller, delivery_Data, collection_Data,
							takeAway_Data, booked_Data, bikeIsNew_Data, used_Data, trekking_Data, city_Tour_Data,
							mountainBike_Data, childs_Bike_Data, racing_Cycle_Data, cross_Data, bmx_Data, e_Bike_Data,
							others_Data, frameNumber, frameHeight, yearOfInspection, mechanics, dateOfAppointment,
							timeOfAppointment);

					ContractForm.isBikeDataSaved = Boolean.TRUE;

				} else {
					/** updating data */
					DatabaseLogic.updateBikeDataTable(idOfSeller, delivery_Data, collection_Data, takeAway_Data,
							booked_Data, bikeIsNew_Data, used_Data, trekking_Data, city_Tour_Data, mountainBike_Data,
							childs_Bike_Data, racing_Cycle_Data, cross_Data, bmx_Data, e_Bike_Data, others_Data,
							frameNumber, frameHeight, yearOfInspection, mechanics, dateOfAppointment,
							timeOfAppointment);

					buttonUpdateBikeData.setVisible(Boolean.FALSE);
				}

			} catch (SQLException e) {

				System.err.println(String.format("Not able to insert into %s", Constants.SellerAndBikeData));
				e.printStackTrace();
			}

			instance.setVisible(Boolean.FALSE);
			/** we have to save its value, otherwise the value gets lost */
			Boolean ignoreInvalidSellPositions = ContractForm.ignoreInvalidSellPositions;
			ContractForm instanceOfContractForm = ContractForm
					.getInstance(ContractForm.clientComesFromDisplayContractsForm, Boolean.TRUE);
			ContractForm.getButtonSaveContract().setEnabled(Boolean.TRUE);
			ContractForm.ignoreInvalidSellPositions = ignoreInvalidSellPositions;
			instanceOfContractForm.setState(JFrame.NORMAL);
			instanceOfContractForm.setVisible(Boolean.TRUE);
			instance.dispose();
		}

		// reset all edited values
		if (o.equals(buttonClearView)) {
			modelTop.initialize();
			modelMiddle.initialize();
			modelDown.initialize();

			modelTop.fireTableDataChanged();
			modelMiddle.fireTableDataChanged();
			modelDown.fireTableDataChanged();

			txtFrameNumber.setText(null);
			txtMechanics.setText(null);
			frameHeightBox.setSelectedIndex(0);
		}
	}

	// get & set follows below here
	public static JButton getButtonUpdateBikeData() {
		return buttonUpdateBikeData;
	}

	public void setNewContractHasBeenEdited(Boolean value) {
		this.newContractHasBeenEdited = value;

	}

}
