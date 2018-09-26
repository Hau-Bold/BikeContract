package searchCustomer;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.Constants;
import constants.IntersportColors;

/** the class SearchCustomerForm */
public class SearchCustomerForm extends JFrame {
	private static final long serialVersionUID = 1L;

	private static SearchCustomerForm instance = null;
	private JPanel panelMain;

	private static JTextField txtSearchCustomer;
	private static AutoCompleteCustomerListener autoCompleteCustomerListener = null;

	public static SearchCustomerForm getInstance(float f, float g) {

		if (instance == null) {
			instance = new SearchCustomerForm();
		}

		instance.setLocation((int) f, (int) g);

		if (txtSearchCustomer != null) {
			txtSearchCustomer.setText(Constants.EMPTY_STRING);
		}

		if (autoCompleteCustomerListener != null) {
			txtSearchCustomer.getDocument().removeDocumentListener(autoCompleteCustomerListener);
			autoCompleteCustomerListener = null;
		}

		addAutoCompleteListener();

		return instance;
	}

	/**
	 * Constructor.
	 */
	private SearchCustomerForm() {
		initComponent();
	}

	private void initComponent() {

		this.setTitle(Constants.SEARCH_CUSTOMER);
		this.setSize(Constants.WIDTH_OF_SEARCH_CUSTOMER_FRAME, (Constants.HEIGHT_OF_SEARCH_CUSTOMER_FRAME));
		this.setResizable(Boolean.FALSE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CUSTOMERS_FRAME, Constants.MINIMAL_HEIGHT_OF_CUSTOMERS_FRAME));
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				super.windowClosing(e);
				instance.setVisible(Boolean.FALSE);
				SearchCustomerForm.txtSearchCustomer.setText(Constants.EMPTY_STRING);
				instance.dispose();
			}
		});

		panelMain = new JPanel();
		panelMain.setLayout(null);
		panelMain.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		panelMain.setBackground(IntersportColors.INTERSPORT_GRAY);
		panelMain.setOpaque(Boolean.TRUE);

		txtSearchCustomer = new JTextField();
		txtSearchCustomer.setBounds(this.getWidth() / 2 - 120, this.getHeight() / 2 - 25, 120, 25);

		panelMain.add(txtSearchCustomer);

		this.getContentPane().add(panelMain);

	}

	private static void addAutoCompleteListener() {

		autoCompleteCustomerListener = new AutoCompleteCustomerListener(txtSearchCustomer);

		txtSearchCustomer.getDocument().addDocumentListener(autoCompleteCustomerListener);
	}

	public void showFrame() {

		this.setVisible(Boolean.TRUE);

	}

}
