package customer;

import constants.Constants;
import contractDigitalizer.BikeContract;
import utils.TableModel;

/**
 * the class ContractModelOnlineShop
 *
 */
public class CustomerModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private Boolean isEditable = Boolean.TRUE;

	public CustomerModel(String[] nameOfColumns, Customer customer) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		if ((Integer.compare(column, 0) == 0)) {
			return Boolean.FALSE;
		}

		return isEditable;
	}

	/** setting default values in table model */

	@Override
	public void initialize() {

		dataEntries = new Object[8][2];
		dataEntries[0][0] = Constants.CUSTOMER_NAME;
		dataEntries[1][0] = Constants.PRENAME;
		dataEntries[2][0] = Constants.STREET;
		dataEntries[3][0] = Constants.NUMBER;
		dataEntries[4][0] = Constants.POSTAL_CODE;
		dataEntries[5][0] = Constants.PLACE;
		dataEntries[6][0] = Constants.MOBILE;
		dataEntries[7][0] = Constants.EMAIL;

		setCustomerValues(BikeContract.getCustomer());

	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		return String.class;

	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;

		if (allowFireTableDataChanged) {
			fireTableDataChanged();
		}

	}

	/**
	 * puts the customers' data into the model
	 * 
	 * @param customer
	 *            - the customer
	 */
	public void setCustomerValues(Customer customer) {
		if (customer != null) {

			dataEntries[0][1] = customer.getCustomerName();
			dataEntries[1][1] = customer.getPrename();
			dataEntries[2][1] = customer.getStreet();
			dataEntries[3][1] = customer.getNumber();
			dataEntries[4][1] = customer.getPostalCode();
			dataEntries[5][1] = customer.getPlace();
			dataEntries[6][1] = customer.getMobile();
			dataEntries[7][1] = customer.getEmail();
		}

	}

	public String getCustomerName() {

		return String.valueOf(dataEntries[0][1]);
	}

	public String getCustomerPrename() {
		return String.valueOf(dataEntries[1][1]);
	}

}
