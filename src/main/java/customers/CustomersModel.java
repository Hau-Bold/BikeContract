package customers;

import java.util.List;

import customer.Customer;
import utils.TableModel;

/**
 * the class ContractModelOnlineShop
 *
 */
public class CustomersModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.FALSE;

	public CustomersModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		return isEditable;
	}

	/** setting default values in table model */
	public void initialize(List<Customer> lstCustomer) {

		int rowCount = lstCustomer.size();

		dataEntries = new Object[rowCount][10];

		for (int i = 0; i < rowCount; i++) {

			Customer customer = lstCustomer.get(i);

			dataEntries[i][0] = customer.getCustomerName();
			dataEntries[i][1] = customer.getPrename();
			dataEntries[i][2] = customer.getStreet();
			dataEntries[i][3] = customer.getNumber();
			dataEntries[i][4] = customer.getPostalCode();
			dataEntries[i][5] = customer.getPlace();
			dataEntries[i][6] = customer.getMobile();
			dataEntries[i][7] = customer.getEmail();
			dataEntries[i][8] = customer.getCreatedAt();
			dataEntries[i][9] = customer.getLastModified();

		}

		fireTableDataChanged();
	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		return String.class;

	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;
		fireTableDataChanged();

	}

}
