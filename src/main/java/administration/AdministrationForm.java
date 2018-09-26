package administration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import login.LoginForm;
import utils.CustomHeaderRenderer;
import utils.Utils;

/** the class CustomerForm */
public class AdministrationForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static AdministrationForm instance = null;
	/** panels, table, model, actions for customer frame */
	private JPanel panelNorth;
	private static JPanel panelMain, panelSouth;
	private static JTable table;
	private static AdministrationModel model;
	private static JButton buttonChangePassword, buttonupdateAdministration, buttonBackToMainMenu;
	private static TableModelListener updateAdministrationListener;
	private static Administration administration;

	/**
	 * Constructor.
	 */
	private AdministrationForm() {
		initComponent();
	}

	public static AdministrationForm getInstance(Administration administration) {

		AdministrationForm.administration = administration;
		if (instance == null) {
			instance = new AdministrationForm();
		} else {
			model.initialize(administration);

			if (buttonupdateAdministration.isVisible()) {
				buttonupdateAdministration.setVisible(Boolean.FALSE);
			}
		}
		return instance;
	}

	/** Instantiating Customer Frame and its components */
	protected void initComponent() {

		this.setTitle(Constants.ADMINISTRATION);
		this.setSize(Constants.WIDTH_OF_ADMINISTRATION_FRAME, (Constants.HEIGHT_OF_ADMINISTRATION_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(new Dimension(Constants.MINIMAL_WIDTH_OF_ADMINISTRATION_FRAME,
				Constants.MINIMAL_HEIGHT_OF_ADMINISTRATION_FRAME));
		this.setIconImage(BikeContract.getImage());
		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);
		this.setResizable(Boolean.FALSE);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		/** to define the closing reaction of this frame */
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				if (buttonupdateAdministration.isVisible()) {
					int choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.ADMINISTRATION_WAS_EDITED, BikeContract.getIdOfContract()),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {
						handleClosingOfInstance();

					} else {
						return;
					}
					buttonupdateAdministration.setVisible(Boolean.FALSE);
				} else {

					handleClosingOfInstance();
				}

			}

			/** to handle the closing of the instance */
			private void handleClosingOfInstance() {
				instance.setVisible(Boolean.FALSE);

				BikeContract instanceOfBikeContract = BikeContract.getInstance();
				instanceOfBikeContract.setState(JFrame.NORMAL);
				instance.dispose();
			}
		});

		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth.setPreferredSize(
				new Dimension(Constants.WIDTH_OF_ADMINISTRATION_FRAME, Constants.HEIGHT_OF_TABLE_ADMINISTRATION));
		panelMain.add(panelNorth, BorderLayout.NORTH);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 6));
		panelSouth.setBackground(IntersportColors.INTERSPORT_GREEN);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonChangePassword = new JButton(Constants.CHANGE_PASSWORD);
		buttonChangePassword.addActionListener(this);

		buttonupdateAdministration = new JButton(Constants.UPDATE_ADMINISTRATION);
		buttonupdateAdministration.setVisible(Boolean.FALSE);
		buttonupdateAdministration.addActionListener(this);

		buttonBackToMainMenu = new JButton(Constants.MAIN_MENU);
		buttonBackToMainMenu.addActionListener(this);

		panelSouth.add(buttonChangePassword);
		panelSouth.add(buttonupdateAdministration);
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(buttonBackToMainMenu);

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		/** Instantiating the table */
		model = new AdministrationModel(Constants.COLUMNS_OF_ADMINISTRATION);
		model.initialize(administration);

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
		updateAdministrationListener = new UpdateAdministrationModelListener();
		table.getModel().addTableModelListener(updateAdministrationListener);
		table.setDefaultRenderer(Object.class, new AdministrationStatusRenderer(model));

		panelNorth.add(new JScrollPane(table));

		this.getContentPane().add(panelMain);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_GREEN));
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

		if (o.equals(buttonChangePassword)) {

			new Runnable() {

				@Override
				public void run() {
					Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
					LoginForm.getInstance(screenDimension.getWidth() / 2, screenDimension.getHeight() / 2, Boolean.TRUE,
							Boolean.FALSE).showFrame();
				}
			}.run();

		}

		else if (o.equals(buttonBackToMainMenu)) {

			this.setState(JFrame.ICONIFIED);
			BikeContract.getInstance().setState(JFrame.NORMAL);

		}

		else if (o.equals(buttonupdateAdministration)) {

			new Runnable() {

				@Override
				public void run() {
					Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
					LoginForm.getInstance(screenDimension.getWidth() / 2, screenDimension.getHeight() / 2, Boolean.TRUE,
							Boolean.TRUE).showFrame();
				}
			}.run();

			if (BikeContract.isPasswordCorrect) {
				Administration administration = model.getAdministrationFromModel();
				DatabaseLogic.updateAdministrationTable(administration, Boolean.FALSE);

				buttonupdateAdministration.setVisible(Boolean.FALSE);
				instance.setVisible(Boolean.FALSE);

				BikeContract.getInstance().setState(JFrame.NORMAL);
				BikeContract.setAdministration(administration);
				instance.dispose();

				BikeContract.isPasswordCorrect = Boolean.FALSE;
			}

		}

	}

	/**
	 * setting customer's data in the model
	 */
	public void setModelData() {

		// Customer customer = BikeContract.getCustomer();
		//
		// model.setAllowFireTableDataChanged(Boolean.FALSE);
		//
		// model.setValueAt(customer.getCustomerName(), 0, 1);
		// model.setValueAt(customer.getPrename(), 1, 1);
		// model.setValueAt(customer.getStreet(), 2, 1);
		// model.setValueAt(customer.getNumber(), 3, 1);
		// model.setValueAt(customer.getPostalCode(), 4, 1);
		// model.setValueAt(customer.getPlace(), 5, 1);
		// model.setValueAt(customer.getMobile(), 6, 1);
		// model.setValueAt(customer.getEmail(), 7, 1);
		//
		// model.setAllowFireTableDataChanged(Boolean.TRUE);

	}

	// get & set follows below herre
	public static JButton getButtonupdateAdministration() {
		return buttonupdateAdministration;
	}

}
