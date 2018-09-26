package searchCustomer;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import customer.Customer;
import customers.CustomersForm;
import database.DatabaseLogic;

public class AutoCompleteCustomerListener implements DocumentListener {

	private JTextField textFieldToListen;
	String completed = null;

	/**
	 * Constructor.
	 * 
	 * @param textFieldToListen
	 *            - the textfield to listen to
	 * @param model
	 */
	public AutoCompleteCustomerListener(JTextField textFieldToListen) {

		this.textFieldToListen = textFieldToListen;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {

	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {

		String text = textFieldToListen.getText();
		String toComplete = text;

		if (!(toComplete.equals(completed))) {
			doAutoComplete(toComplete);
		}
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		String text = textFieldToListen.getText();

		String toComplete = text;

		if (!(toComplete.equals(completed))) {
			doAutoComplete(toComplete);
		}

	}

	private void doAutoComplete(String toComplete) {
		Runnable AutoComplete = new Runnable() {
			List<Customer> lstCustomer;

			@Override
			public void run() {

				try {

					if (toComplete.length() > 0) {
						lstCustomer = DatabaseLogic.getCustomerWithCustomerNameLike(toComplete);
					} else {
						lstCustomer = DatabaseLogic.getCustomers();
					}
					CustomersForm.getModel().initialize(lstCustomer);
					CustomersForm.getModel().fireTableDataChanged();

					completed = toComplete;

				} catch (SQLException e) {
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

		};
		SwingUtilities.invokeLater(AutoComplete);
	}

}
