package shore;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import constants.Constants;
import constants.ShoreConstants;
import contractDigitalizer.BikeContract;
import customer.Customer;
import customer.CustomerForm;
import customers.CustomersForm;
import database.DatabaseLogic;
import utils.InformationProvider;

/** the class ShoreAppointmentsMouseListener */
public class ShoreAppointmentsMouseListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		JTable table = (JTable) event.getSource();

		int row = table.getSelectedRow();

		if (Integer.compare(row, -1) != 0) {

			String prename = ShoreUtils.getPreNameFromTable(row, table);
			String customerName = ShoreUtils.getCustomerNameFromTable(row, table);
			String service = ShoreUtils.getServiceFromTable(row, table);
			Integer dayOfWeek = ShoreUtils.getDayOfWeekFromTable(row, table);
			String time = ShoreUtils.getTimeFromTable(row, table);

			BikeContract.setService(service);
			BikeContract.setDayOfWeek(ShoreUtils.getDayAsString(dayOfWeek));
			BikeContract.setTime(time);

			/**
			 * the shore.Customer must be told to BikeContract to control CustomersForm, the
			 * selected Customer may be a listed customer or not
			 */
			BikeContract.setShoreCustomer(new shore.Customer(prename, customerName));

			List<customer.Customer> lst_Customer = null;
			try {
				lst_Customer = DatabaseLogic.getCustomers(prename, customerName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ShoreAppointmentsForm instanceOfshoreAppointmentsForm = ShoreAppointmentsForm.getInstance();
			instanceOfshoreAppointmentsForm.setVisible(Boolean.FALSE);

			if (lst_Customer.size() > 0) {
				CustomersForm customersForm = CustomersForm.getInstance(lst_Customer);
				customersForm.setVisible(Boolean.TRUE);
			} else {

				new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
						ShoreConstants.CUSTOMER_UNKNOWN, customerName, prename).run();

				customer.Customer customer = new Customer();
				customer.setPrename(prename);
				customer.setCustomerName(customerName);
				BikeContract.setCustomer(customer);

				CustomerForm instanceOfCustomerForm = CustomerForm.getInstance(Boolean.FALSE);
				CustomerForm.getButtonNewContract().setEnabled(Boolean.FALSE);
				CustomerForm.getButtonNewContract().setText(Constants.NEW_CONTRACT);
				CustomerForm.removeSaveCustomerListener();
				CustomerForm.addSaveCustomerListener();
				instanceOfCustomerForm.showFrame();

			}

			instanceOfshoreAppointmentsForm.dispose();

		} else {
			return;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
