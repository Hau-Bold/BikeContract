package customers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;

import constants.Constants;
import constants.IntersportColors;
import contractDigitalizer.BikeContract;
import customer.Customer;
import customer.CustomerForm;
import database.DatabaseLogic;
import login.LoginForm;
import searchCustomer.SearchCustomerForm;
import statistics.ShowSalesStatistics;
import statistics.ShowStatistics;
import utils.CustomHeaderRenderer;
import utils.Utils;

/** the class CustomersForm */
public class CustomersForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static CustomersForm instance = null;

	/** panels, table, model, actions for customer frame */
	private static JPanel panelMain, panelSouth;
	private JPanel panelNorth;
	private static JTable table;
	private static CustomersModel model;
	private static JButton buttonSearchCustomer, buttonBackToMainMenu, buttonStatistics, buttonSalesStatistics;
	public static SearchCustomerForm searchCustomer;
	private static List<Customer> lstCustomer = null;

	/**
	 * Constructor.
	 */
	private CustomersForm() {
		initComponent();
	}

	/**
	 * yields an instance of CustomerForm
	 */
	@SafeVarargs
	public static CustomersForm getInstance(List<Customer>... varargs) {

		if (varargs.length == 0) {
			try {
				lstCustomer = DatabaseLogic.getCustomers();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			lstCustomer = varargs[0];
		}

		if (instance == null) {
			instance = new CustomersForm();

		}
		model.initialize(lstCustomer);

		shore.Customer shoreCustomer = BikeContract.getShoreCustomer();

		if (shoreCustomer != null) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					int choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.APPLY_NEW_CUSTOMER_QUESTION, shoreCustomer.getPreName(),
									shoreCustomer.getCustomerName()),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {

						BikeContract
								.setCustomer(new Customer(shoreCustomer.getCustomerName(), shoreCustomer.getPreName(),
										Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING,
										Constants.EMPTY_STRING, Constants.EMPTY_STRING, Constants.EMPTY_STRING));

						BikeContract.setShoreCustomer(null);

						instance.setVisible(Boolean.FALSE);

						CustomerForm instanceOfCustomerForm = CustomerForm.getInstance(Boolean.FALSE);
						instanceOfCustomerForm.setVisible(Boolean.TRUE);
						instanceOfCustomerForm.setState(JFrame.NORMAL);
						instance.dispose();

					} else {

						BikeContract.setShoreCustomer(null);
						return;
					}

				}
			});
		}

		instance.setState(JFrame.NORMAL);
		return instance;

	}

	private static JPanel getActionPanel() {
		/** Instantiating the panel for customer's actions */
		JPanel response = new JPanel(new GridLayout(1, 7));
		response.setBackground(IntersportColors.INTERSPORT_GREEN);
		response.setOpaque(Boolean.TRUE);
		response.setBorder(BorderFactory.createRaisedBevelBorder());

		response.add(buttonSearchCustomer);
		response.add(buttonBackToMainMenu);
		response.add(Utils.getEmptyPanel());
		response.add(Utils.getEmptyPanel());
		response.add(Utils.getEmptyPanel());
		response.add(buttonSalesStatistics);
		response.add(buttonStatistics);

		return response;
	}

	/** Instantiating Customer Frame and its components */
	protected void initComponent() {
		this.setTitle(Constants.DISPLAY_CUSTOMERS);
		this.setSize(Constants.WIDTH_OF_CUSTOMERS_FRAME, (Constants.HEIGHT_OF_CUSTOMERS_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CUSTOMERS_FRAME, Constants.MINIMAL_HEIGHT_OF_CUSTOMERS_FRAME));
		this.setIconImage(BikeContract.getImage());
		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				instance.setVisible(Boolean.FALSE);
				BikeContract instanceOfContractDigitalizer = null;
				instanceOfContractDigitalizer = BikeContract.getInstance();
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

		buttonSearchCustomer = new JButton(Constants.SEARCH_CUSTOMER);
		buttonSearchCustomer.addActionListener(this);

		buttonBackToMainMenu = new JButton(Constants.MAIN_MENU);
		buttonBackToMainMenu.addActionListener(this);

		buttonSalesStatistics = new JButton(Constants.SALES_STATISTCS);
		buttonSalesStatistics.addActionListener(this);

		buttonStatistics = new JButton(Constants.STATISTCS);
		buttonStatistics.addActionListener(this);

		panelSouth = getActionPanel();

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		/** Instantiating the table */
		model = new CustomersModel(Constants.COLUMNS_OF_DISPLAY_CUSTOMERS);

		List<Customer> lstCustomer = null;

		try {
			lstCustomer = DatabaseLogic.getCustomers();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			e1.printStackTrace();
		}

		model.initialize(lstCustomer);

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
		table.addMouseListener(new CustomersMouseListener());
		table.setDefaultRenderer(String.class, new CustomersRenderer());
		table.setRowSorter(Utils.getSorter(table, 0, SortOrder.ASCENDING));
		panelNorth.add(new JScrollPane(table));

		this.getContentPane().add(panelMain);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_GREEN));
		}

	}

	/** setting the frame visible */
	public void showFrame() {

		this.setVisible(Boolean.TRUE);

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonSearchCustomer)) {
			searchCustomer = SearchCustomerForm.getInstance(this.getX() + (this.getWidth() / 2),
					this.getY() + (this.getHeight() / 2));
			searchCustomer.showFrame();

		}

		else if (o.equals(buttonBackToMainMenu)) {

			this.setState(JFrame.ICONIFIED);
			BikeContract.getInstance().setState(JFrame.NORMAL);

		}

		else if (o.equals(buttonSalesStatistics)) {

			new Runnable() {
				/** request password */

				@Override
				public void run() {
					Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
					LoginForm.getInstance(screenDimension.getWidth() / 2, screenDimension.getHeight() / 2, Boolean.TRUE,
							Boolean.TRUE).showFrame();
				}
			}.run();

			if (BikeContract.isPasswordCorrect) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						ShowSalesStatistics dialog = new ShowSalesStatistics(Constants.SIX_MONTHS);
						dialog.setVisible(Boolean.TRUE);
					}
				});
				BikeContract.isPasswordCorrect = Boolean.FALSE;

			} else {

				return;
			}
		}

		else if (o.equals(buttonStatistics)) {

			new Runnable() {

				@Override
				public void run() {
					Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
					LoginForm.getInstance(screenDimension.getWidth() / 2, screenDimension.getHeight() / 2, Boolean.TRUE,
							Boolean.TRUE).showFrame();
				}
			}.run();

			if (BikeContract.isPasswordCorrect) {

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {

						ShowStatistics dialog = new ShowStatistics(Constants.SIX_MONTHS);
						dialog.setVisible(Boolean.TRUE);
					}
				});

				BikeContract.isPasswordCorrect = Boolean.FALSE;

			} else {

				return;
			}

		}

	}

	// get & set follows below here
	public static CustomersModel getModel() {
		return model;
	}

	public static void setModel(CustomersModel model) {
		CustomersForm.model = model;
	}

	public static void setSelected(int selectedRow) {

		table.setRowSelectionInterval(selectedRow, selectedRow);

	}

}
