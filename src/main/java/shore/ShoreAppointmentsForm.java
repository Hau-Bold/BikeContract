package shore;

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
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SortOrder;

import constants.Constants;
import constants.IntersportColors;
import constants.ShoreConstants;
import contractDigitalizer.BikeContract;
import utils.CustomHeaderRenderer;
import utils.Utils;

/** the class ShoreAppointmentsForm */
public class ShoreAppointmentsForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static ShoreAppointmentsForm instance = null;
	private JPanel panelMain, panelNorth, panelSouth;
	private JTable table;
	private static ShoreAppointmentsModel model;
	private JButton buttonBackToMainMenu;
	private static List<Appointment> lstAppointment;

	/**
	 * yields an instance of ShoreAppointmentsForm
	 * 
	 * @return the instance
	 */
	public static ShoreAppointmentsForm getInstance() {

		if (instance == null) {

			instance = new ShoreAppointmentsForm();
		} else {

			setUpAppointments();
		}

		return instance;
	}

	/**
	 * clears the folder of shore appointments, request new appointments and
	 * processes appointments
	 */
	private static void setUpAppointments() {

		ShoreUtils.clearAppointments(ShoreConstants.PATH_TO_SHORE_FOLDER);

		String smbPassword = Utils.getSmbPassword(BikeContract.getDirectoryOfBikeContract());
		try {
			ShoreUtils.requestAppointments(ShoreConstants.PATH_TO_SHORE_APPOINTMENTS_RELEASE, ShoreConstants.USER,
					smbPassword, ShoreConstants.PATH_TO_SHORE_FOLDER);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			lstAppointment = ShoreUtils.getListOfAppointments(ShoreConstants.PATH_TO_SHORE_FOLDER);
		} catch (IOException e) {
			e.printStackTrace();
		}

		lstAppointment = ShoreUtils.removeAppointmentsNotBelongingToCurrentWeek(lstAppointment,
				ShoreUtils.getDateOfMonday(), ShoreUtils.getDateOfSaturday());

		model.initialize(lstAppointment);

	}

	/**
	 * Constructor.
	 */
	public ShoreAppointmentsForm() {
		initComponent();
	}

	/** to display instance */
	public void showFrame() {
		this.setVisible(Boolean.TRUE);
	}

	private void initComponent() {

		this.setTitle(ShoreConstants.SHORE_APPOINTMENTS);
		this.setSize(Constants.WIDTH_OF_SHORE_FRAME, (Constants.HEIGHT_OF_SHORE_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_SHORE_FRAME, Constants.MINIMAL_HEIGHT_OF_SHORE_FRAME));
		this.setIconImage(BikeContract.getImage());
		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				instance.setVisible(Boolean.FALSE);
				BikeContract instanceOfContractDigitalizer = BikeContract.getInstance();
				instanceOfContractDigitalizer.setVisible(Boolean.TRUE);
				instanceOfContractDigitalizer.setState(JFrame.NORMAL);
				instance.dispose();
			}
		});

		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth.setPreferredSize(
				new Dimension(Constants.WIDTH_OF_CUSTOMERS_FRAME, Constants.HEIGHT_OF_TABLE_CUSTOMER));
		panelMain.add(panelNorth, BorderLayout.NORTH);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 7));
		panelSouth.setBackground(IntersportColors.INTERSPORT_YELLOW1);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonBackToMainMenu = new JButton(Constants.MAIN_MENU);
		buttonBackToMainMenu.addActionListener(this);

		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(buttonBackToMainMenu);

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		/** Instantiating the table */
		model = new ShoreAppointmentsModel(ShoreConstants.COLUMNS_OF_DISPLAY_APPOINTMENTS);
		setUpAppointments();

		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.getTableHeader().setReorderingAllowed(Boolean.TRUE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.setRowSorter(Utils.getSorter(table, 0, SortOrder.ASCENDING));

		table.addMouseListener(new ShoreAppointmentsMouseListener());
		table.setDefaultRenderer(String.class, new AppointmentsRenderer());
		table.setDefaultRenderer(Integer.class, new AppointmentsRenderer());

		panelNorth.add(new JScrollPane(table));

		this.getContentPane().add(panelMain);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_YELLOW1));
		}

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonBackToMainMenu)) {

			instance.setVisible(Boolean.FALSE);
			BikeContract.getInstance().setState(JFrame.NORMAL);

			instance.dispose();

		}

	}

}
