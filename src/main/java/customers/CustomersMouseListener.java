package customers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JFrame;
import javax.swing.JTable;

import contractDigitalizer.BikeContract;
import customer.Customer;
import customer.CustomerForm;
import database.DatabaseLogic;
import searchCustomer.SearchCustomerForm;
import utils.Utils;

/** the class CustomersMouseListener */
public class CustomersMouseListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent event) {

		JTable table = (JTable) event.getSource();

		int row = table.getSelectedRow();

		if (Integer.compare(row, -1) != 0) {

			Customer customer = Utils.getCustomerFromTable(row, table);

			CustomersForm.getInstance();
			SearchCustomerForm instanceOfSearchCustomerForm = CustomersForm.searchCustomer;

			if (instanceOfSearchCustomerForm != null) {
				instanceOfSearchCustomerForm.setVisible(Boolean.FALSE);
				instanceOfSearchCustomerForm.dispose();
			}

			int idOfCustomer = 0;
			try {
				idOfCustomer = DatabaseLogic.getCustomersIDSpecific(customer);
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

			/** the base knows the customer and his/her id */
			BikeContract.setCustomer(customer);
			BikeContract.setIdOfCustomer(idOfCustomer);

			/** opening CustomerForm's frame */
			CustomerForm displayCustomer = CustomerForm.getInstance(Boolean.TRUE);
			CustomerForm.removeSaveCustomerListener();
			displayCustomer.setState(JFrame.NORMAL);
			displayCustomer.setModelData();
			CustomersForm.getInstance().setVisible(Boolean.FALSE);
			displayCustomer.showFrame();
			displayCustomer.setSelectedRow(row);
			CustomersForm.getInstance().dispose();
		} else {
			return;
		}

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
