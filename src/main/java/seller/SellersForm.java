package seller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import utils.CustomHeaderRenderer;
import utils.Utils;

/** the class SellersForm */
public class SellersForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static SellersForm instance = null;

	/** panels, table, model, actions for customer frame */
	private JPanel panelMain, panelNorth, panelSouth;
	private JTable table;
	private static SellersModel model;
	private JButton buttonBackToSellers;

	/**
	 * Constructor.
	 * 
	 * @param contractDigitalizer
	 * 
	 * @param contractDigitalizer
	 */
	private SellersForm() {
		initComponent();
	}

	/**
	 * yields an instance of CustomerForm
	 * 
	 * @param contractDigitalizer
	 * 
	 * @param contractDigitalizer
	 */
	public static SellersForm getInstance() {
		if (instance == null) {
			instance = new SellersForm();
		} else {
			List<Seller> lstSeller = null;

			try {
				lstSeller = DatabaseLogic.getSellers();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			model.setModelData(lstSeller);
		}
		return instance;
	}

	/** Instantiating Customer Frame and its components */
	protected void initComponent() {
		this.setSize(Constants.WIDTH_OF_CUSTOMERS_FRAME, (Constants.HEIGHT_OF_CUSTOMERS_FRAME));
		int[] location = Utils.getLocation(this);
		this.setLocation(location[0], location[1]);
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(new Dimension(Constants.MINIMAL_WIDTH_OF_CUSTOMER_FRAME,
				Constants.MINIMAL_HEIGHT_OF_CUSTOMER_FRAME));
		this.setIconImage(BikeContract.getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setTitle(Constants.DISPLAY_SELLERS_TITLE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				instance.setVisible(Boolean.FALSE);
				SellerForm.getInstance().setState(JFrame.NORMAL);
				instance.dispose();
			}
		});

		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(new BorderLayout());
		panelNorth.setPreferredSize(
				new Dimension(Constants.WIDTH_OF_CUSTOMER_FRAME, Constants.HEIGHT_OF_TABLE_CUSTOMER));
		panelMain.add(panelNorth, BorderLayout.NORTH);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 7));
		panelSouth.setBackground(IntersportColors.INTERSPORT_TURQUOISE);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonBackToSellers = new JButton(Constants.BACK_TO_SELLER);
		buttonBackToSellers.addActionListener(this);

		panelSouth.add(buttonBackToSellers);
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());
		panelSouth.add(Utils.getEmptyPanel());

		panelMain.add(panelSouth, BorderLayout.SOUTH);

		/** Instantiating the table */
		model = new SellersModel(Constants.COLUMNS_OF_DISPLAY_SELLERS);

		// if this is in init: contracts don't change
		List<Seller> lstSeller = null;

		try {
			lstSeller = DatabaseLogic.getSellers();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		model.setModelData(lstSeller);

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
		table.addMouseListener(new SellersMouseListener(table));
		table.setDefaultRenderer(String.class, new SellersRenderer());

		panelNorth.add(new JScrollPane(table));

		this.getContentPane().add(panelMain);

		for (int col = 0; col < table.getColumnCount(); col++) {
			table.getColumnModel().getColumn(col)
					.setHeaderRenderer(new CustomHeaderRenderer(IntersportColors.INTERSPORT_TURQUOISE));
		}

	}

	/** setting the frame visible */
	public void showFrame() {

		this.setVisible(Boolean.TRUE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonBackToSellers)) {

			this.setVisible(Boolean.FALSE);
			SellerForm instance = SellerForm.getInstance();
			instance.setState(JFrame.NORMAL);
			dispose();
		}

	}

	/**
	 * setting the sellers
	 *
	 * @param lstSeller
	 *            - the list of sellers
	 */
	public void setModelData(List<Seller> lstSeller) {

		model.setModelData(lstSeller);

	}

	// get & set follows below here
}
