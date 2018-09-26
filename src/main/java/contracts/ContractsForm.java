package contracts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import constants.Constants;
import constants.IntersportColors;
import contract.Contract;
import contractDigitalizer.BikeContract;
import customer.CustomerForm;
import database.DatabaseLogic;
import utils.CustomHeaderRenderer;
import utils.Utils;

/** the class ContractsForm */
public class ContractsForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static ContractsForm instance = null;
	private static ContractsModel model;

	/** panels, table, model, actions for customer frame */
	private JPanel panelMain, panelNorth, panelSouth;
	private JTable table;
	private JButton buttonBackToCustomers;

	/**
	 * Constructor.
	 * 
	 * @param contractDigitalizer
	 * 
	 * @param contractDigitalizer
	 */
	private ContractsForm() {
		initComponent();
	}

	/**
	 * yields an instance of CustomerForm
	 * 
	 * @param contractDigitalizer
	 * 
	 * @param contractDigitalizer
	 */
	public static ContractsForm getInstance() {
		if (instance == null) {
			instance = new ContractsForm();
		} else {
			List<Contract> lstContract = null;

			try {
				lstContract = DatabaseLogic.getContracts(BikeContract.getIdOfCustomer());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			setModelData(lstContract);
		}
		return instance;
	}

	/** Instantiating Customer Frame and its components */
	protected void initComponent() {
		this.setSize(Constants.WIDTH_OF_CUSTOMERS_FRAME, (Constants.HEIGHT_OF_CUSTOMERS_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CUSTOMER_FRAME, Constants.MINIMAL_HEIGHT_OF_CUSTOMER_FRAME));
		this.setIconImage(BikeContract.getImage());
		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);

		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth
				.setPreferredSize(new Dimension(Constants.WIDTH_OF_CUSTOMER_FRAME, Constants.HEIGHT_OF_TABLE_CUSTOMER));
		panelMain.add(panelNorth, BorderLayout.NORTH);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 7));
		panelSouth.setBackground(IntersportColors.INTERSPORT_CORAL);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonBackToCustomers = new JButton(Constants.BACK_TO_CUSTOMER);
		buttonBackToCustomers.addActionListener(this);

		panelSouth.add(buttonBackToCustomers);
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		/** Instantiating the table */
		model = new ContractsModel(Constants.COLUMNS_OF_DISPLAY_CONTRACTS);

		// if this is in init: contracts don't change
		List<Contract> lstContract = null;

		try {
			lstContract = DatabaseLogic.getContracts(BikeContract.getIdOfCustomer());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.setModelData(lstContract);

		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(200, 400));
		table.getTableHeader().setReorderingAllowed(Boolean.TRUE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.addMouseListener(new ContractsMouseListener());

		table.setDefaultRenderer(String.class, new ContractsStringCellRenderer());
		table.setDefaultRenderer(Double.class, new ContractsDoubleCellRenderer());
		table.setDefaultRenderer(Boolean.class, new ContractsBooleanCellRenderer());

		panelNorth.add(new JScrollPane(table));

		this.getContentPane().add(panelMain);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_CORAL));
		}

	}

	/** setting the frame visible */
	public void showFrame() {
		this.setVisible(Boolean.TRUE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonBackToCustomers)) {

			CustomerForm displayCustomer = CustomerForm.getInstance(Boolean.TRUE);
			CustomerForm.getModel().setCustomerValues(BikeContract.getCustomer());
			displayCustomer.setState(JFrame.NORMAL);
			ContractsForm.getInstance().setVisible(Boolean.FALSE);
			displayCustomer.showFrame();
			ContractsForm.getInstance().dispose();
		}

	}

	/**
	 * setting the contracts of customer
	 *
	 * @param lstContractCreatedAt
	 *            - the list of contracts
	 */
	public static void setModelData(List<Contract> lstContract) {

		model.setModelData(lstContract);

	}

	// get & set follows below here
}
