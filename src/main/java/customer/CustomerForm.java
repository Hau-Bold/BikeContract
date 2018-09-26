package customer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import javax.swing.event.TableModelListener;

import constants.Constants;
import constants.IntersportColors;
import contract.Contract;
import contract.ContractForm;
import contract.SellPosition;
import contractDigitalizer.BikeContract;
import contracts.ContractsForm;
import customers.CustomersForm;
import database.DatabaseLogic;
import login.LoginForm;
import utils.CustomHeaderRenderer;
import utils.Utils;

/** the class CustomerForm */
public class CustomerForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static CustomerForm instance = null;
	/** panels, table, model, actions for customer frame */
	private JPanel panelNorth;
	private static JPanel panelMain, panelSouth;
	private static JTable table;
	private static CustomerModel model;
	private static JButton buttonSaveCustomer, buttonNewContract, buttonShowContracts, buttonDeleteCustomer,
			buttonClearView, buttonUpdateCustomer, buttonBackToCustomers, buttonBackToMainMenu;
	private static TableModelListener updateCustomerListener, saveCustomerListener;
	private ContractForm contractForm = null;
	private Boolean clientComesFromDisplayCustomersForm = Boolean.FALSE;
	private static Boolean isCustomerSaved = Boolean.FALSE;
	private int selectedRow;

	/**
	 * Constructor.
	 * 
	 * @param clientComesFromDisplayCustomersForm
	 * 
	 * @param clientComesFromDisplayCustomersForm
	 *            - the customer
	 */
	private CustomerForm(Boolean clientComesFromDisplayCustomersForm) {
		this.clientComesFromDisplayCustomersForm = clientComesFromDisplayCustomersForm;
		initComponent();
	}

	/**
	 * yields an instance of CustomerForm (calling this method means to create a new
	 * customer, hence the button to display contracts must be invisible)
	 * 
	 * @param clientComesFromDisplayCustomersForm
	 *            to control button show Contracts etc, some actions depends on this
	 *            param
	 */
	public static CustomerForm getInstance(Boolean clientComesFromDisplayCustomersForm) {

		if (instance == null) {
			instance = new CustomerForm(clientComesFromDisplayCustomersForm);
		} else {
			model.initialize();
			model.setCustomerValues(BikeContract.getCustomer());
			handleInstance(clientComesFromDisplayCustomersForm);

			instance.clientComesFromDisplayCustomersForm = clientComesFromDisplayCustomersForm;
		}

		return instance;
	}

	/**
	 * yields the current instance
	 * 
	 * @return - the instance
	 */
	public static CustomerForm getInstance() {
		return instance;
	}

	/** Instantiating Customer Frame and its components */
	protected void initComponent() {

		this.setTitle(Constants.CUSTOMER);
		this.setSize(Constants.WIDTH_OF_CUSTOMER_FRAME, (Constants.HEIGHT_OF_CUSTOMER_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CUSTOMER_FRAME, Constants.MINIMAL_HEIGHT_OF_CUSTOMER_FRAME));
		this.setIconImage(BikeContract.getImage());
		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		/** to define the closing reaction of this frame */
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				if (buttonUpdateCustomer.isVisible()) {
					/** startet to edit an existing customer */

					Customer customer = BikeContract.getCustomer();
					int choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.CUSTOMER_WAS_EDITED, customer.getCustomerName(),
									customer.getPrename()),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {
						handleClosingOfInstance();
					} else {
						return;
					}
				} else if (buttonSaveCustomer.isVisible()) {
					/** startet to create a new customer */

					int choice = JOptionPane.showConfirmDialog(null, Constants.NON_EXISTING_DATA, Constants.INFORMATION,
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {
						handleClosingOfInstance();
					} else {
						return;
					}
				} else {
					/** other cases */

					handleClosingOfInstance();
				}

			}

			private void handleClosingOfInstance() {

				instance.setVisible(Boolean.FALSE);
				BikeContract.getInstance().setState(JFrame.NORMAL);
				instance.dispose();
			}
		});

		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth
				.setPreferredSize(new Dimension(Constants.WIDTH_OF_CUSTOMER_FRAME, Constants.HEIGHT_OF_TABLE_CUSTOMER));
		panelMain.add(panelNorth, BorderLayout.NORTH);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 7));
		panelSouth.setBackground(IntersportColors.INTERSPORT_GREEN);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonNewContract = new JButton();
		buttonNewContract.setText(Constants.NEW_CONTRACT);
		buttonNewContract.setVisible(Boolean.TRUE);
		buttonNewContract.addActionListener(this);

		buttonShowContracts = new JButton(Constants.SHOW_CUSTOMER_CONTRACTS);
		buttonShowContracts.addActionListener(this);

		buttonUpdateCustomer = new JButton(Constants.UPDATE_CUSTOMER);
		buttonUpdateCustomer.addActionListener(this);
		buttonUpdateCustomer.setVisible(Boolean.FALSE);

		buttonDeleteCustomer = new JButton(Constants.DELETE_CUSOMER);
		buttonDeleteCustomer.addActionListener(this);

		buttonClearView = new JButton(Constants.CLEAR_VIEW);
		buttonClearView.addActionListener(this);

		buttonSaveCustomer = new JButton(Constants.SAVE_CUSTOMER);
		buttonSaveCustomer.addActionListener(this);
		buttonSaveCustomer.setVisible(Boolean.FALSE);

		buttonBackToCustomers = new JButton(Constants.BACK_TO_CUSTOMERS);
		buttonBackToCustomers.addActionListener(this);

		buttonBackToMainMenu = new JButton(Constants.MAIN_MENU);
		buttonBackToMainMenu.addActionListener(this);

		panelSouth.add(buttonNewContract);

		if (clientComesFromDisplayCustomersForm) {
			panelSouth.add(buttonShowContracts);
			panelSouth.add(buttonBackToCustomers);
			panelSouth.add(buttonDeleteCustomer);
			panelSouth.add(buttonUpdateCustomer);
		} else {
			buttonNewContract.setEnabled(Boolean.FALSE);
			panelSouth.add(buttonClearView);
			panelSouth.add(buttonSaveCustomer);
			panelSouth.add(Utils.getEmptyPanel());
			panelSouth.add(Utils.getEmptyPanel());
		}
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(buttonBackToMainMenu);

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		/** Instantiating the table */
		model = new CustomerModel(Constants.COLUMNS_OF_CUSTOMER, BikeContract.getCustomer());
		model.initialize();

		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.getTableHeader().setReorderingAllowed(Boolean.FALSE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.setRowSelectionAllowed(Boolean.FALSE);
		table.setCellSelectionEnabled(Boolean.TRUE);
		table.getColumnModel().getColumn(0).setCellRenderer(new CustomerOverviewRenderer());
		table.getColumnModel().getColumn(1).setCellRenderer(new CustomerRenderer());

		if (clientComesFromDisplayCustomersForm) {
			isCustomerSaved = Boolean.TRUE;

			if (updateCustomerListener == null) {
				/** to show acion "update customer" */

				updateCustomerListener = new UpdateCustomerListener(table);
				table.getModel().addTableModelListener(updateCustomerListener);
			} else {

				table.getModel().removeTableModelListener(updateCustomerListener);
				updateCustomerListener = new UpdateCustomerListener(table);
				table.getModel().addTableModelListener(updateCustomerListener);
			}
		} else {

			isCustomerSaved = Boolean.FALSE;

			if (updateCustomerListener != null) {
				/** new customer was selected, hence action "update customer" isn't necessary */
				table.getModel().removeTableModelListener(updateCustomerListener);
			}

			removeSaveCustomerListener();
			addSaveCustomerListener();
		}

		panelNorth.add(new JScrollPane(table));

		this.getContentPane().add(panelMain);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_GREEN));
		}

	}

	/** adds the listener which decides when it is allowed to save the customer */
	public static void addSaveCustomerListener() {

		saveCustomerListener = new SaveCustomerListener(table);
		table.getModel().addTableModelListener(saveCustomerListener);

	}

	/**
	 * removes the listener which decides when it is allowed to save the customer
	 */
	public static void removeSaveCustomerListener() {

		if (saveCustomerListener != null) {
			table.getModel().removeTableModelListener(saveCustomerListener);

		}

	}

	/** setting the frame visible */
	public void showFrame() {

		if (this.getState() == JFrame.ICONIFIED) {

			this.setState(JFrame.NORMAL);
		}

		this.setVisible(Boolean.TRUE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonSaveCustomer)) {

			/** access to action only for new customer */

			/** save Customer */
			Customer customer = Utils.getCustomer(model);

			if (Utils.isCustomerValid(customer)) {

				Calendar calendar = Calendar.getInstance(Locale.GERMAN);
				Date date = calendar.getTime();
				try {

					/** writing customer */
					try {
						DatabaseLogic.insertIntoCustomerTable(customer,
								BikeContract.SIMPLE_DATE_FORMAT_TIME.format(date));
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

				} catch (SQLException e) {

					System.err.println(String.format("Not able to insert customer %s , %s", customer.getCustomerName(),
							customer.getPrename()));
					e.printStackTrace();
				}

				buttonNewContract.setEnabled(Boolean.TRUE);
				buttonSaveCustomer.setVisible(Boolean.FALSE);
				isCustomerSaved = Boolean.TRUE;

				/** the base knows the customer and his/her id */
				BikeContract.setCustomer(customer);
				BikeContract.setIdOfCustomer(DatabaseLogic.getCustomerId());

				if (BikeContract.getShoreCustomer() != null) {
					BikeContract.setShoreCustomer(null);
				}

			} else {

				JOptionPane pane = new JOptionPane();

				javax.swing.border.Border z = BorderFactory.createLineBorder(Color.RED, 10);
				pane.setBorder(z);

				JOptionPane.showMessageDialog(null, "Bitte Kunden überprüfen", Constants.WARNING,
						JOptionPane.WARNING_MESSAGE);
			}

		} else if (o.equals(buttonNewContract)) {

			// buttonNewContract has also text "back to contract"

			if (buttonNewContract.getText() == Constants.NEW_CONTRACT) {

				BikeContract.setIdOfContract(0);
				/** new contractForm */
				contractForm = ContractForm.getInstance(Boolean.FALSE, Boolean.FALSE);
				ContractForm.isBikeDataSaved = Boolean.FALSE;
				ContractForm.contractIsSaved = Boolean.FALSE;
				ContractForm.getModel().reset();

				contractForm.setTitle(String.format(Constants.NEW_CONTRACT_PERSONALIZED,
						BikeContract.getCustomer().getCustomerName(), BikeContract.getCustomer().getPrename()));
				model.getLstIdentifier().clear();
				model.getIsRowFromErpData().clear();

				if (!ContractForm.getButtonSaveContract().isEnabled()) {
					ContractForm.getButtonSaveContract().setEnabled(Boolean.TRUE);
				}
			} else {

				/** back to contract */
				int idOfContract = BikeContract.getIdOfContract();

				if (Integer.compare(idOfContract, 0) != 0) {
					Contract contract = null;
					try {
						contract = DatabaseLogic.getContract(BikeContract.getIdOfContract(),
								BikeContract.getIdOfCustomer());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					List<SellPosition> lstSellpostion = null;
					try {
						lstSellpostion = DatabaseLogic.getSellPositions(BikeContract.getIdOfContract());
					} catch (SQLException e) {
						e.printStackTrace();
					}
					contract.setSellPositions(lstSellpostion);
					Customer customer = BikeContract.getCustomer();
					contractForm = ContractForm.getInstance(Boolean.TRUE, Boolean.FALSE);
					ContractForm.removeUpdateContractListener();
					contractForm.setModelData(contract);
					/** when the model is filled, the updateContractListener my be added */
					ContractForm.addUpdateContractListener();
					JButton buttonUpdateContract = ContractForm.getButtonUpdateContract();
					if (buttonUpdateContract.isVisible()) {
						buttonUpdateContract.setVisible(Boolean.FALSE);
					}
					contractForm.setTitle(String.format(Constants.TITLE_DISPLAY_CONTRACT, customer.getCustomerName(),
							customer.getPrename(), BikeContract.getIdOfContract()));
					ContractForm.getButtonSaveContract().setText(Constants.WRITE_CONTRACT);

					if (!ContractForm.getButtonShowBikeDataDialog().isEnabled()) {
						ContractForm.getButtonShowBikeDataDialog().setEnabled(Boolean.TRUE);
					}
					if (!ContractForm.getButtonSaveContract().isEnabled()) {
						ContractForm.getButtonSaveContract().setEnabled(Boolean.TRUE);
					}
					ContractForm.contractIsSaved = Boolean.TRUE;

					ContractForm.addUpdateContractListener();

				}
			}

			contractForm.setState(JFrame.NORMAL);
			this.setVisible(Boolean.FALSE);
			contractForm.showFrame();
			this.dispose();

		} else if (o.equals(buttonShowContracts)) {

			List<Contract> lstContract = null;

			try {
				lstContract = DatabaseLogic.getContracts(BikeContract.getIdOfCustomer());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			/** display contracts */

			Customer customer = BikeContract.getCustomer();

			ContractsForm displayContracts = ContractsForm.getInstance();
			ContractsForm.setModelData(lstContract);
			displayContracts.setState(JFrame.NORMAL);
			displayContracts.setTitle(
					String.format(Constants.DISPLAY_CONTRACTS, customer.getCustomerName(), customer.getPrename()));
			CustomerForm.getInstance().setVisible(Boolean.FALSE);
			displayContracts.showFrame();
			CustomerForm.getInstance().dispose();

		} else if (o.equals(buttonDeleteCustomer)) {

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
				/** delete customer */
				DatabaseLogic.removeCustomer(BikeContract.getIdOfCustomer(), BikeContract.getCustomer());
				instance.setVisible(Boolean.FALSE);
				CustomersForm instanceOfCustomer = CustomersForm.getInstance();
				instanceOfCustomer.setState(JFrame.NORMAL);
				instanceOfCustomer.setVisible(Boolean.TRUE);
				model.removeTableModelListener(updateCustomerListener);
				model.fireTableDataChanged();
				instance.dispose();
				BikeContract.isPasswordCorrect = Boolean.FALSE;
			} else {
				return;
			}
		}

		else if (o.equals(buttonClearView)) {

			model.reset();
			buttonSaveCustomer.setVisible(Boolean.FALSE);

		}

		else if (o.equals(buttonUpdateCustomer)) {

			/** updating customer */
			Customer customer = Utils.getCustomer(model);
			BikeContract.setCustomer(customer);
			int idOfCustomer = BikeContract.getIdOfCustomer();

			Calendar calendar = Calendar.getInstance(Locale.GERMAN);
			Date date = calendar.getTime();

			try {
				DatabaseLogic.updateCustomer(idOfCustomer, customer, BikeContract.SIMPLE_DATE_FORMAT_TIME.format(date));
			} catch (Exception e) {
				e.printStackTrace();
			}

			buttonUpdateCustomer.setVisible(Boolean.FALSE);

		}

		else if (o.equals(buttonBackToCustomers)) {

			clientComesFromDisplayCustomersForm = Boolean.FALSE;
			CustomersForm displayCustomers = CustomersForm.getInstance();
			CustomersForm.setSelected(selectedRow);
			displayCustomers.setState(JFrame.NORMAL);
			instance.setVisible(Boolean.FALSE);
			displayCustomers.showFrame();
			instance.dispose();
		}

		else if (o.equals(buttonBackToMainMenu)) {

			if (buttonUpdateCustomer.isVisible() || !isCustomerSaved) {
				// an existing contract was selected
				int choice = JOptionPane.showConfirmDialog(null,
						String.format(Constants.CUSTOMER_WAS_EDITED, model.getCustomerName(),
								model.getCustomerPrename(), null),

						Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (Integer.compare(choice, 0) != 0) {
					return;
				} else {
					if (BikeContract.getShoreCustomer() != null) {
						BikeContract.setShoreCustomer(null);
					}
				}
			}

			BikeContract instanceOfContractDigitalizer = null;
			instanceOfContractDigitalizer = BikeContract.getInstance();
			this.setVisible(Boolean.FALSE);
			instanceOfContractDigitalizer.setState(JFrame.NORMAL);
			instance = null;
			dispose();

			this.setState(JFrame.ICONIFIED);
			BikeContract.getInstance().setState(JFrame.NORMAL);
		}

	}

	/**
	 * setting customer's data in the model
	 */
	public void setModelData() {

		Customer customer = BikeContract.getCustomer();

		model.setAllowFireTableDataChanged(Boolean.FALSE);

		model.setValueAt(customer.getCustomerName(), 0, 1);
		model.setValueAt(customer.getPrename(), 1, 1);
		model.setValueAt(customer.getStreet(), 2, 1);
		model.setValueAt(customer.getNumber(), 3, 1);
		model.setValueAt(customer.getPostalCode(), 4, 1);
		model.setValueAt(customer.getPlace(), 5, 1);
		model.setValueAt(customer.getMobile(), 6, 1);
		model.setValueAt(customer.getEmail(), 7, 1);

		model.setAllowFireTableDataChanged(Boolean.TRUE);

	}

	/**
	 * handles the Instance to controll wheather or not client cames from
	 * CustomersForm form
	 * 
	 * @param clientComesFromDisplayCustomersForm
	 *            - the param to control
	 */
	private static void handleInstance(Boolean clientComesFromDisplayCustomersForm) {

		panelMain.remove(panelSouth);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 7));
		panelSouth.setBackground(IntersportColors.INTERSPORT_GREEN);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		panelSouth.add(buttonNewContract);

		if (clientComesFromDisplayCustomersForm) {
			isCustomerSaved = Boolean.TRUE;

			panelSouth.add(buttonShowContracts);
			panelSouth.add(buttonBackToCustomers);
			panelSouth.add(buttonDeleteCustomer);
			panelSouth.add(buttonUpdateCustomer);
			panelSouth.add(buttonSaveCustomer);
			panelSouth.add(buttonBackToMainMenu);

			buttonNewContract.setText(Constants.NEW_CONTRACT);
			buttonNewContract.setEnabled(Boolean.TRUE);
			buttonShowContracts.setEnabled(Boolean.TRUE);
			buttonSaveCustomer.setVisible(Boolean.FALSE);
			buttonUpdateCustomer.setVisible(Boolean.FALSE);
		} else {

			panelSouth.add(buttonClearView);
			panelSouth.add(buttonSaveCustomer);
			panelSouth.add(Utils.getEmptyPanel());
			panelSouth.add(Utils.getEmptyPanel());
			panelSouth.add(Utils.getEmptyPanel());
			panelSouth.add(buttonBackToMainMenu);
		}

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		if (clientComesFromDisplayCustomersForm) {

			if (updateCustomerListener == null) {
				updateCustomerListener = new UpdateCustomerListener(table);
				table.getModel().addTableModelListener(updateCustomerListener);
			} else {
				table.getModel().removeTableModelListener(updateCustomerListener);
				updateCustomerListener = new UpdateCustomerListener(table);
				table.getModel().addTableModelListener(updateCustomerListener);
			}
		} else {
			// button new customer was pressed
			if (updateCustomerListener != null) {
				table.getModel().removeTableModelListener(updateCustomerListener);
				model.setCustomerValues(new Customer());
				model.clear();
				model.initialize();
			}
			isCustomerSaved = Boolean.FALSE;

		}

	}

	// get & set follows below here

	public static JButton getButtonNewContract() {
		return buttonNewContract;
	}

	public void setButtonNewContract(JButton buttonNewContract) {
		CustomerForm.buttonNewContract = buttonNewContract;
	}

	public static JButton getButtonUpdateCustomer() {
		return buttonUpdateCustomer;
	}

	public static void setButtonUpdateCustomer(JButton buttonUpdateCustomer) {
		CustomerForm.buttonUpdateCustomer = buttonUpdateCustomer;
	}

	public static CustomerModel getModel() {
		return model;
	}

	public static void setModel(CustomerModel model) {
		CustomerForm.model = model;
	}

	public static JButton getButtonSaveCustomer() {
		return buttonSaveCustomer;
	}

	public static void setButtonSaveCustomer(JButton buttonSave) {
		CustomerForm.buttonSaveCustomer = buttonSave;
	}

	public void setSelectedRow(int row) {
		this.selectedRow = row;

	}

}
