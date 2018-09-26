package contract;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelListener;

import com.itextpdf.text.DocumentException;

import bikeData.BikeData;
import bikeData.BikeDataForm;
import constants.Constants;
import constants.IntersportColors;
import contractDigitalizer.BikeContract;
import contracts.ContractsForm;
import customer.Customer;
import customer.CustomerForm;
import database.DatabaseLogic;
import erp.BarCodeScanner;
import login.LoginForm;
import pdf.PdfGenerator;
import pdf.PdfUtils;
import seller.Seller;
import utils.CustomHeaderRenderer;
import utils.InformationProvider;
import utils.IntegerCellRender;
import utils.TableModel;
import utils.Utils;

/** the class ContractForm */
public class ContractForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static ContractForm instance = null;

	/** panels, table, model, actions for contract frame */
	private static JPanel panelMain, panelSouth, panelNorth;
	private static BarCodeScanner barCodeScanner;
	private static JTable table;
	private static TableModel model;

	private static JButton buttonSaveContract, buttonBackToCustomer, buttonDeleteContract, buttonClearView,
			buttonUpdateContract, buttonBackToContracts, buttonShowBikeDataDialog, buttonExit, buttonBackToMainMenu;
	private static UpdateContractListener updateContractListener;
	private Contract contract;
	private int idOfContract;
	private static ContractCellRenderer contractCellRenderer;
	private static TableModelListener contractValuesListener;
	public static Boolean clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm,
			contractIsSaved = Boolean.FALSE, isBikeDataSaved = Boolean.FALSE,
			ignoreInvalidSellPositions = Boolean.FALSE;

	/**
	 * Constructor.
	 * 
	 * @param displayCustomer
	 */
	private ContractForm(Boolean clientComesFromDisplayContractsForm, Boolean clientComesFromDisplayBikeDataForm) {

		initComponent(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);
	}

	public static ContractForm getInstance() {
		return instance;
	}

	/**
	 * yields an instance of BikeDataForm
	 * 
	 * @param clientComesFromDisplayContractsForm
	 */
	public static ContractForm getInstance(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) {
		ContractForm.clientComesFromDisplayContractsForm = clientComesFromDisplayContractsForm;
		ContractForm.clientComesFromDisplayBikeDataForm = clientComesFromDisplayBikeDataForm;

		if (clientComesFromDisplayContractsForm) {
			ignoreInvalidSellPositions = Boolean.TRUE;
		} else {
			ignoreInvalidSellPositions = Boolean.FALSE;
		}

		if (instance == null) {
			instance = new ContractForm(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);
		} else {

			handleInstance(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				barCodeScanner.requestFocus();
			}
		});

		return instance;

	}

	/**
	 * to add the listener for the values of the contract
	 */
	public static void addContractValuesListener() {

		if (contractValuesListener == null) {
			contractValuesListener = new ContractValuesListener(model);
			model.addTableModelListener(contractValuesListener);
		}
	}

	/**
	 * to remove the listener for the values of the contract
	 */
	public static void removeContractValuesListener() {

		if (contractValuesListener != null) {
			model.removeTableModelListener(contractValuesListener);
			contractValuesListener = null;
		}

	}

	/**
	 * Instantiating Contract Frame and its components
	 * 
	 * @param clientComesFromDisplayBikeDataForm
	 * @param clientComesFromDisplayContractsForm
	 */
	private void initComponent(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		this.setSize(
				(int) (Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME > screenSize.getWidth() ? screenSize.getWidth()
						: Constants.WIDTH_OF_DISPLAY_CONTRACT_FRAME),
				(int) (Constants.HEIGHT_OF_DISPLAY_CONTRACT_FRAME > screenSize.getHeight() ? screenSize.getHeight()
						: Constants.HEIGHT_OF_DISPLAY_CONTRACT_FRAME));

		this.setLocation(0, 0);
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CONTRACT_FRAME, Constants.MINIMAL_HEIGHT_OF_CONTRACT_FRAME));
		this.setIconImage(BikeContract.getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				if (clientComesFromDisplayContractsForm) {
					if (buttonUpdateContract.isVisible()) {
						// an existing contract was selected
						int choice = JOptionPane.showConfirmDialog(null,
								String.format(Constants.EXISTING_CONTRACT_WAS_EDITED, BikeContract.getIdOfContract()),

								Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

						if (Integer.compare(choice, 0) == 0) {
							handleClosingOfInstance(Boolean.TRUE);

						} else {
							return;
						}
					} else {
						handleClosingOfInstance(Boolean.TRUE);
					}
				}

				else {

					if (!contractIsSaved) {
						int choice = JOptionPane.showConfirmDialog(null, String.format(Constants.NON_EXISTING_DATA),

								Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (Integer.compare(choice, 0) == 0) {
							handleClosingOfInstance(Boolean.FALSE);
						} else {
							return;
						}
					}

					handleClosingOfInstance(Boolean.FALSE);
				}

				super.windowClosing(e);
			}

			private void handleClosingOfInstance(Boolean isContractExisting) {

				if (isContractExisting) {
					instance.setVisible(Boolean.FALSE);
					CustomerForm instanceOfCustomerForm = CustomerForm.getInstance();
					instanceOfCustomerForm.setState(JFrame.NORMAL);
					instanceOfCustomerForm.showFrame();
					instance.dispose();
				} else {

					instance.setVisible(Boolean.FALSE);
					ContractsForm instanceOfContractsForm = ContractsForm.getInstance();
					instanceOfContractsForm.setState(JFrame.NORMAL);
					instanceOfContractsForm.showFrame();
					contractIsSaved = Boolean.FALSE;
					instance.dispose();
				}

			}
		});

		/** </the renderers> */

		/** Instantiating the panel for the Contract Frame */
		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth.setPreferredSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CONTRACT_FRAME, Constants.HEIGHT_OF_TABLE_CONTRACT));
		panelMain.add(panelNorth, BorderLayout.NORTH);

		buttonSaveContract = new JButton();
		buttonSaveContract.setBorder(BorderFactory.createDashedBorder(IntersportColors.INTERSPORT_GREEN));
		buttonSaveContract.addActionListener(this);

		buttonBackToCustomer = new JButton(Constants.BACK_TO_CUSTOMER);
		buttonBackToCustomer.addActionListener(this);

		buttonDeleteContract = new JButton(Constants.DELETE_CONTRACT);
		buttonDeleteContract.addActionListener(this);

		buttonUpdateContract = new JButton(Constants.UPDATE_CONTRACT);
		buttonUpdateContract.addActionListener(this);
		buttonUpdateContract.setVisible(Boolean.FALSE);
		buttonClearView = new JButton(Constants.CLEAR_VIEW);
		buttonClearView.addActionListener(this);

		buttonBackToContracts = new JButton(Constants.BACK_TO_CONTRACTS);
		buttonBackToContracts.addActionListener(this);

		buttonShowBikeDataDialog = new JButton(Constants.SellerAndBikeData);
		buttonShowBikeDataDialog.addActionListener(this);
		buttonShowBikeDataDialog.setEnabled(Boolean.FALSE);

		buttonBackToMainMenu = new JButton(Constants.MAIN_MENU);
		buttonBackToMainMenu.addActionListener(this);

		buttonExit = new JButton(Constants.EXIT);
		buttonExit.addActionListener(this);

		/** Instantiating the table */
		model = BikeContract.isOnlineShop ? new ContractModelOnlineShop(Constants.COLUMNS_OF_CONTRACT_ONLINESHOP)
				: new ContractModelAdvarics(Constants.COLUMNS_OF_CONTRACT_ADVARICS);
		model.initialize();

		if (clientComesFromDisplayContractsForm) {

			model.resetIdentifier();
		}

		barCodeScanner = new BarCodeScanner(model);

		table = getTable(model);

		panelNorth.add(new JScrollPane(table));

		panelSouth = getActionPanel(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		this.getContentPane().add(panelMain);

	}

	private static JTable getTable(utils.TableModel model) {

		JTable table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(200, 200));
		table.getTableHeader().setReorderingAllowed(Boolean.FALSE);
		table.getTableHeader()
				.setPreferredSize(new Dimension(Constants.TABLE_HEADER_WIDTH, Constants.TABLE_HEADER_HEIGHT));
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setRowHeight(Constants.ROW_HEIGHT);
		table.setRowSelectionAllowed(Boolean.FALSE);
		table.setCellSelectionEnabled(Boolean.TRUE);
		table.addMouseListener(new ContractMouselistener());

		/** rendering */
		/** colunn 0 */
		table.getColumnModel().getColumn(0).setCellRenderer(new IntegerCellRender());
		/** default */
		contractCellRenderer = new ContractCellRenderer();

		int indexOfNameLessColumn = model.getIndexOfColumn(Constants.EMPTY_STRING);

		for (int i = 1; i < model.getColumnCount(); i++) {
			if (i != indexOfNameLessColumn) {
				table.getColumnModel().getColumn(i).setCellRenderer(contractCellRenderer);
			}
		}
		table.getColumnModel().getColumn(indexOfNameLessColumn)
				.setCellRenderer(new ContractOverviewStringCellRenderer());

		contractValuesListener = new ContractValuesListener(model);
		table.getModel().addTableModelListener(contractValuesListener);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_CORAL));
		}

		return table;
	}

	/**
	 * handles the Instance to control wheather or not client cames from
	 * ContractsForm form
	 * 
	 * @param clientComesFromDisplayBikeDataForm
	 *            - to controll client
	 * @param clientComesFromDisplayContractsForm
	 *            - to controll client
	 * 
	 */
	private static void handleInstance(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) {

		if (!ContractForm.clientComesFromDisplayBikeDataForm) {
			panelMain.remove(panelNorth);
			/** Instantiating the panel for the table */
			panelNorth = new JPanel(new BorderLayout());
			panelNorth.setPreferredSize(
					new Dimension(Constants.MINIMAL_WIDTH_OF_CONTRACT_FRAME, Constants.HEIGHT_OF_TABLE_CONTRACT));

			model = BikeContract.isOnlineShop ? new ContractModelOnlineShop(Constants.COLUMNS_OF_CONTRACT_ONLINESHOP)
					: new ContractModelAdvarics(Constants.COLUMNS_OF_CONTRACT_ADVARICS);

			model.resetIdentifier();

			table = getTable(model);

			panelNorth.add(new JScrollPane(table));

			panelMain.remove(panelSouth);
			/** Instantiating the panel for contracts actions */
			panelSouth = getActionPanel(clientComesFromDisplayContractsForm, clientComesFromDisplayBikeDataForm);

			panelMain.add(panelNorth, BorderLayout.NORTH);
			panelMain.add(panelSouth, BorderLayout.SOUTH);

			panelMain.revalidate();
		}

		if ((!clientComesFromDisplayBikeDataForm) && (!clientComesFromDisplayContractsForm)) {
			/** new contract */

			removeContractValuesListener();
			model.clear();
			model.resetIdentifier();
			model.initialize();

			addContractValuesListener();
		} else {
			addUpdateContractListener();
		}
	}

	/**
	 * setting contract's data in the model
	 * 
	 * @param customer
	 *            - the customer
	 */
	public void setModelData(Contract contract) {

		// initially model has sto be resized correctly:

		List<SellPosition> lstSellPosition = contract.getSellPositions();

		int rowCounter = lstSellPosition.size();

		model.resetIdentifier();
		model.getIsRowFromErpData().clear();
		model.appendSellPositions(lstSellPosition);

		lstSellPosition.forEach(sellPosition -> model.getIsRowFromErpData().add(Boolean.TRUE));

		rowCounter++;

		/** setting uvp parts */

		Double deliveryAssemblyFeeUVP = contract.getDeliveryAssemblyFeeUVP();
		Double serviceCostGarageUVP = contract.getServiceCostGarageUVP();
		Double redemptionDismantledPartsWheelUVP = contract.getRedemptionDismantledPartsWheelUVP();
		Double downPaymentUVP = contract.getDownPaymentUVP();
		Double estateUVP = contract.getEstateUVP();

		if (Double.compare(deliveryAssemblyFeeUVP, 0.0) != 0) {
			model.setValueAt(deliveryAssemblyFeeUVP, rowCounter, 5);
		}
		if (Double.compare(serviceCostGarageUVP, 0.0) != 0) {
			model.setValueAt(serviceCostGarageUVP, rowCounter + 1, 5);
		}
		if (Double.compare(redemptionDismantledPartsWheelUVP, 0.0) != 0) {
			model.setValueAt(redemptionDismantledPartsWheelUVP, rowCounter + 2, 5);
		}
		if (Double.compare(downPaymentUVP, 0.0) != 0) {
			model.setValueAt(downPaymentUVP, rowCounter + 3, 5);
		}
		if (Double.compare(estateUVP, 0.0) != 0) {
			model.setValueAt(estateUVP, rowCounter + 4, 5);
		}

		/** setting houseprice parts */
		Double deliveryAssemblyFeeHousePrice = contract.getDeliveryAssemblyFeeHousePrice();
		Double serviceCostGarageHousePrice = contract.getServiceCostGarageHousePrice();
		Double redemptionDismantledPartsWheelHousePrice = contract.getRedemptionDismantledPartsWheelHousePrice();
		Double downPaymentHousePrice = contract.getDownPaymentHousePrice();
		Double estateHousePrice = contract.getEstateHousePrice();

		if (Double.compare(deliveryAssemblyFeeHousePrice, 0.0) != 0) {
			model.setValueAt(deliveryAssemblyFeeHousePrice, rowCounter, 6);
		}
		if (Double.compare(serviceCostGarageHousePrice, 0.0) != 0) {
			model.setValueAt(serviceCostGarageHousePrice, rowCounter + 1, 6);
		}
		if (Double.compare(redemptionDismantledPartsWheelHousePrice, 0.0) != 0) {
			model.setValueAt(redemptionDismantledPartsWheelHousePrice, rowCounter + 2, 6);
		}
		if (Double.compare(downPaymentHousePrice, 0.0) != 0) {
			model.setValueAt(downPaymentHousePrice, rowCounter + 3, 6);
		}
		if (Double.compare(estateHousePrice, 0.0) != 0) {
			model.setValueAt(estateHousePrice, rowCounter + 4, 6);
		}

	}

	/** setting the frame visible */
	public void showFrame() {

		this.setVisible(Boolean.TRUE);
	}

	private static JPanel getActionPanel(Boolean clientComesFromDisplayContractsForm,
			Boolean clientComesFromDisplayBikeDataForm) {
		JPanel actionPanel = new JPanel(new GridLayout(1, 9));
		actionPanel.setBackground(IntersportColors.INTERSPORT_CORAL);
		actionPanel.setOpaque(Boolean.TRUE);
		actionPanel.setBorder(BorderFactory.createRaisedBevelBorder());

		barCodeScanner = new BarCodeScanner(model);

		buttonSaveContract.setText(
				(clientComesFromDisplayContractsForm || clientComesFromDisplayBikeDataForm) ? Constants.WRITE_CONTRACT
						: Constants.SAVE_CONTRACT);

		// a new Contract should be established
		if ((!clientComesFromDisplayContractsForm) && (!clientComesFromDisplayBikeDataForm)) {

			buttonShowBikeDataDialog.setEnabled(Boolean.FALSE);
			actionPanel.add(buttonSaveContract);
			actionPanel.add(buttonBackToCustomer);
			actionPanel.add(buttonShowBikeDataDialog);
			actionPanel.add(buttonBackToContracts);
			actionPanel.add(buttonClearView);
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(buttonBackToMainMenu);
			actionPanel.add(buttonExit);
			actionPanel.add(barCodeScanner);

		}

		else if (clientComesFromDisplayContractsForm && (!clientComesFromDisplayBikeDataForm)) {

			buttonShowBikeDataDialog.setEnabled(Boolean.TRUE);
			actionPanel.add(buttonSaveContract);
			actionPanel.add(buttonBackToCustomer);
			actionPanel.add(buttonShowBikeDataDialog);
			actionPanel.add(buttonBackToContracts);
			actionPanel.add(buttonUpdateContract);
			actionPanel.add(buttonDeleteContract);
			actionPanel.add(buttonBackToMainMenu);
			actionPanel.add(buttonExit);
			actionPanel.add(barCodeScanner);

		}

		else if (clientComesFromDisplayContractsForm || clientComesFromDisplayBikeDataForm) {
			// this case:
			//
			buttonShowBikeDataDialog.setEnabled(Boolean.TRUE);
			actionPanel.add(buttonSaveContract);
			actionPanel.add(buttonBackToCustomer);
			actionPanel.add(buttonShowBikeDataDialog);
			actionPanel.add(buttonBackToContracts);
			actionPanel.add(buttonUpdateContract);
			actionPanel.add(Utils.getEmptyPanel());
			actionPanel.add(buttonBackToMainMenu);
			actionPanel.add(buttonExit);
			actionPanel.add(barCodeScanner);
		}

		return actionPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonBackToCustomer)) {

			int idOfContract = BikeContract.getIdOfContract();
			if (Integer.compare(idOfContract, 0) == 0) {

				JOptionPane.showMessageDialog(this, Constants.CONTRACT_HAS_TO_BE_SAVED);
				return;
			} else if (!isBikeDataSaved) {

				JOptionPane.showMessageDialog(this, Constants.BIKEDATA_HAS_TO_BE_SAVED);
				return;
			} else if (buttonUpdateContract.isVisible()) {
				int choice = JOptionPane.showOptionDialog(this,
						String.format(Constants.EXISTING_CONTRACT_WAS_EDITED, idOfContract), "Auswahl",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, o);
				if (Integer.compare(choice, 0) != 0) {
					return;
				}
			}

			removeUpdateContractListener();
			CustomerForm instanceOfCustomerForm = null;
			if (clientComesFromDisplayContractsForm) {
				instanceOfCustomerForm = CustomerForm.getInstance();
			} else {
				instanceOfCustomerForm = CustomerForm.getInstance(Boolean.TRUE);
			}
			CustomerForm.getButtonNewContract().setText(Constants.BACK_TO_CONTRACT);
			instanceOfCustomerForm.setState(JFrame.NORMAL);
			this.setVisible(Boolean.FALSE);
			instanceOfCustomerForm.showFrame();
			this.dispose();

		}

		else if (o.equals(buttonShowBikeDataDialog)) {
			/** displaying bike data */

			BikeDataForm instanceOfBikeDataForm = null;
			try {
				instanceOfBikeDataForm = BikeDataForm.getInstance(clientComesFromDisplayContractsForm,
						clientComesFromDisplayBikeDataForm);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			instanceOfBikeDataForm.setNewContractHasBeenEdited(clientComesFromDisplayContractsForm);
			instanceOfBikeDataForm.showFrame();
			instanceOfBikeDataForm.setState(JFrame.NORMAL);

		}

		else if (o.equals(buttonClearView)) {

			model.getIsRowFromErpData().clear();
			model.getLstIdentifier().clear();
			model.reset();

		}

		else if (o.equals(buttonSaveContract)) {

			if (!clientComesFromDisplayContractsForm && !clientComesFromDisplayBikeDataForm) {

				/** new contract */

				contract = Utils.getContract(model);
				List<SellPosition> lstSellPosition = Utils.getSellPositionFromModel(idOfContract, model);
				idOfContract = 0;

				if (Utils.isContractValid(contract)) {
					if (Utils.checkSellpositions(lstSellPosition) || ignoreInvalidSellPositions) {

						int idOfCustomer = BikeContract.getIdOfCustomer();
						if (Integer.compare(idOfCustomer, 0) == 0) {

							JOptionPane.showMessageDialog(null, Constants.NO_CUSTOMER_SELECTED_MESSAGE,
									Constants.WARNING, JOptionPane.WARNING_MESSAGE);

						} else {

							/** a customer was selected */

							if (!clientComesFromDisplayContractsForm) {

								/** a new contract should be established */
								try {
									/** inserting contract */
									DatabaseLogic.insertIntoContractTable(idOfCustomer, contract);
									/** getting id of saved contract */
									idOfContract = DatabaseLogic.getContractId();
									BikeContract.setIdOfContract(idOfContract);
								} catch (SQLException e) {
									e.printStackTrace();
								}

								/** inform client */
								SwingUtilities.invokeLater(
										new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
												Constants.CONTRACT_SAVED_MESSAGE, String.valueOf(idOfContract)));

							} else {
								/** a contract was selected */
								idOfContract = BikeContract.getIdOfContract();
							}

							lstSellPosition.forEach(sellPosition -> sellPosition.setIdOfContract(idOfContract));

							contract.setSellPositions(lstSellPosition);

							if (!clientComesFromDisplayContractsForm) {
								/** inserting sellpositions */
								try {
									DatabaseLogic.insertIntoSellPositionTable(lstSellPosition);
								} catch (SQLException e) {
									e.printStackTrace();
								}

								panelMain.remove(panelSouth);

								/** Instantiating the panel for contracts actions */
								panelSouth = getActionPanel(Boolean.TRUE, Boolean.FALSE);
								panelMain.add(panelSouth, BorderLayout.SOUTH);
								panelMain.revalidate();

								buttonShowBikeDataDialog.setEnabled(Boolean.TRUE);
								buttonSaveContract.setText(Constants.WRITE_CONTRACT);
								buttonSaveContract.setEnabled(Boolean.FALSE);

								addUpdateContractListener();

							}
						}
						contractIsSaved = Boolean.TRUE;
					} else {

						int choice = JOptionPane.showConfirmDialog(null, Constants.SELLPOSTIONS_NOT_VALID,
								Constants.INFORMATION, JOptionPane.YES_NO_OPTION);

						if (Integer.compare(choice, 0) != 0) {
							return;
						}
						ignoreInvalidSellPositions = Boolean.TRUE;
					}
				} else {

					JOptionPane.showMessageDialog(null, Constants.CONTRACT_NOT_VALID);

					return;
				}
			}

			else if (clientComesFromDisplayContractsForm || clientComesFromDisplayBikeDataForm) {

				/** an existing contract was selected and eventually edited */
				/** generating pdf of contract */
				String path = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.CONTRACT;

				Seller seller = null;
				contract = Utils.getContract(model);
				Customer customer = BikeContract.getCustomer();

				/** the sellpositions */
				List<SellPosition> lstSellPosition = Utils.getSellPositionFromModel(idOfContract, model);

				if (Utils.isContractValid(contract)) {

					if (Utils.checkSellpositions(lstSellPosition) || ignoreInvalidSellPositions) {
						contract.setSellPositions(lstSellPosition);

						/** the bike data */
						BikeData bikeData = null;
						try {
							bikeData = DatabaseLogic.getBikeData(BikeContract.getIdOfContract());
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						/** sending bike data to contract */
						if (bikeData != null) {
							contract.setBikeData(bikeData);
							try {
								seller = DatabaseLogic.getSeller(Integer.valueOf(bikeData.getSeller()));
							} catch (SQLException e2) {
								e2.printStackTrace();
							}
							contract.setSeller(seller);
						}

						Calendar calendar = Calendar.getInstance(Locale.GERMAN);
						Date date = calendar.getTime();
						String dateOfPrint = BikeContract.SIMPLE_DATE_FORMAT.format(date);

						StringBuilder builder = new StringBuilder();
						builder.append(path);
						builder.append(File.separator);
						builder.append(customer.getCustomerName());
						builder.append("_");
						builder.append(customer.getPrename());
						builder.append("_");
						builder.append(idOfContract);
						builder.append("_");
						builder.append(dateOfPrint);
						builder.append(".pdf");
						path = builder.toString();

						PdfGenerator pdfGenerator = new PdfGenerator(path, contract, model);
						pdfGenerator.loadSettings();
						PdfUtils.countOfBikeDataCells = 0;
						PdfUtils.countOfCustomerCells = 0;
						try {
							pdfGenerator.generate();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (DocumentException e) {
							e.printStackTrace();
						}

						/** finished */
					}
				} else {

					int choice = JOptionPane.showConfirmDialog(null, Constants.SELLPOSTIONS_NOT_VALID,
							Constants.INFORMATION, JOptionPane.YES_NO_OPTION);

					if (Integer.compare(choice, 0) != 0) {
						return;
					}
					ignoreInvalidSellPositions = Boolean.TRUE;
				}

			} else {

				JOptionPane.showMessageDialog(null, Constants.CONTRACT_NOT_VALID);

				return;
			}

		}

		else if (o.equals(buttonUpdateContract)) {

			Calendar calendar = Calendar.getInstance(Locale.GERMAN);
			Date date = calendar.getTime();

			Contract contract = Utils.getContract(model);

			if (Utils.isContractValid(contract)) {

				List<SellPosition> lstSellPosition = Utils.getSellPositionFromModel(BikeContract.getIdOfContract(),
						model);

				if (Utils.checkSellpositions(lstSellPosition) || ignoreInvalidSellPositions) {
					try {
						DatabaseLogic.updateContract(BikeContract.getIdOfContract(), contract, String.valueOf(date));
					} catch (SQLException e) {
						e.printStackTrace();
					}
					try {

						DatabaseLogic.removeSellPosition(BikeContract.getIdOfContract());

						DatabaseLogic.insertIntoSellPositionTable(lstSellPosition);

						/** inform client */
						SwingUtilities.invokeLater(new InformationProvider(Constants.INFORMATION,
								JOptionPane.INFORMATION_MESSAGE, Constants.CONTRACT_UPDATED_MESSAGE,
								String.valueOf(BikeContract.getIdOfContract())));

					} catch (SQLException e) {
						e.printStackTrace();
					}
					buttonUpdateContract.setVisible(Boolean.FALSE);

					if (isBikeDataSaved) {
						buttonSaveContract.setEnabled(Boolean.TRUE);
					}

				} else {

					int choice = JOptionPane.showConfirmDialog(null, Constants.SELLPOSTIONS_NOT_VALID,
							Constants.INFORMATION, JOptionPane.YES_NO_OPTION);
					if (Integer.compare(choice, 0) != 0) {
						return;
					}

					ignoreInvalidSellPositions = Boolean.TRUE;

				}
			} else {
				JOptionPane.showMessageDialog(null, Constants.CONTRACT_NOT_VALID);
				return;
			}
		}

		else if (o.equals(buttonDeleteContract)) {

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
				removeUpdateContractListener();
				DatabaseLogic.removeContract(BikeContract.getIdOfContract());
				model.reset();
				returnToContracts();
				BikeContract.isPasswordCorrect = Boolean.FALSE;
			} else {
				return;
			}
		}

		else if (o.equals(buttonBackToContracts)) {

			if (!contractIsSaved) {
				int choice = JOptionPane.showOptionDialog(this, Constants.NON_EXISTING_DATA, "Auswahl",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, o);

				if (Integer.compare(choice, 0) != 0) {
					return;
				}

			} else if (!isBikeDataSaved || buttonUpdateContract.isVisible()) {

				int choice = JOptionPane.showOptionDialog(this,
						String.format(Constants.EXISTING_CONTRACT_WAS_EDITED, BikeContract.getIdOfContract()),
						"Auswahl", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, o);
				if (Integer.compare(choice, 0) != 0) {
					return;
				}
			}
			removeUpdateContractListener();

			returnToContracts();
		}

		else if (o.equals(buttonBackToMainMenu)) {

			if (buttonUpdateContract.isVisible() || !contractIsSaved) {
				// an existing contract was selected
				int choice = JOptionPane.showConfirmDialog(null,
						String.format(Constants.EXISTING_CONTRACT_WAS_EDITED, BikeContract.getIdOfContract()),

						Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (Integer.compare(choice, 0) != 0) {
					return;
				}
			}

			BikeContract instanceOfContractDigitalizer = null;
			instanceOfContractDigitalizer = BikeContract.getInstance();
			this.setVisible(Boolean.FALSE);
			instanceOfContractDigitalizer.setState(JFrame.NORMAL);
			instance = null;
			dispose();
		}

		else if (o.equals(buttonExit)) {
			if (buttonUpdateContract.isVisible()) {
				int choice = JOptionPane.showOptionDialog(this,
						String.format(Constants.EXISTING_CONTRACT_WAS_EDITED, BikeContract.getIdOfContract()),
						"Auswahl", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, o);
				if (Integer.compare(choice, 0) == 0) {
					System.exit(0);
				} else {
					return;
				}
			}

			else {
				int choice = JOptionPane.showOptionDialog(this,
						String.format(Constants.EXIT_SYSTEM, BikeContract.getIdOfContract()), "Auswahl",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, o);
				if (Integer.compare(choice, 0) == 0) {
					System.exit(0);
				} else {
					return;
				}
			}
		}

	}

	/***
	 * instanciate the contracts form dependent of customer.
	 */
	private void returnToContracts() {
		ContractsForm displayContracts = ContractsForm.getInstance();
		/** title has to be set again */

		String title = instance.getTitle();

		String prename = null;
		String surname = null;

		if (title.contains(Constants.NEW_CONTRACT)) {
			String[] titleSplitted = title.split(",");

			prename = titleSplitted[1];
			surname = titleSplitted[0].split(" ")[2];
		} else {
			String[] titleSplitted = title.split(";");

			String[] name = titleSplitted[0].split(",");
			prename = name[1];
			surname = name[0];
		}

		displayContracts.setTitle(String.format(Constants.DISPLAY_CONTRACTS, surname, prename));

		List<Contract> lstContract = null;

		try {
			lstContract = DatabaseLogic.getContracts(BikeContract.getIdOfCustomer());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		ContractsForm.setModelData(lstContract);
		displayContracts.setState(JFrame.NORMAL);
		this.setVisible(Boolean.FALSE);
		displayContracts.showFrame();
		this.dispose();

	}

	/**
	 * to add the updateContractListener
	 */
	public static void addUpdateContractListener() {
		removeUpdateContractListener();
		updateContractListener = new UpdateContractListener();
		model.addTableModelListener(updateContractListener);
	}

	/**
	 * to remove the updateContractListener
	 */
	public static void removeUpdateContractListener() {

		if (updateContractListener != null) {
			model.removeTableModelListener(updateContractListener);
			updateContractListener = null;
		}
	}

	// get & set follows below here
	public static JButton getButtonUpdateContract() {
		return buttonUpdateContract;
	}

	public static void setButtonUpdateContract(JButton buttonUpdateContract) {
		ContractForm.buttonUpdateContract = buttonUpdateContract;
	}

	public static JButton getButtonSaveContract() {
		return buttonSaveContract;
	}

	public static JButton getButtonShowBikeDataDialog() {
		return buttonShowBikeDataDialog;
	}

	public static utils.TableModel getModel() {
		return model;
	}

}
