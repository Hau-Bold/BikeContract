package customer;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.apache.commons.lang3.StringUtils;

/** UpdateCustomerListener */
public class UpdateCustomerListener implements TableModelListener {

	private JTable table;

	/**
	 * Constructor.
	 * 
	 * @param table
	 *            - the table
	 */
	public UpdateCustomerListener(JTable table) {
		this.table = table;
	}

	@Override
	public void tableChanged(TableModelEvent event) {

		for (int counter = 0; counter < 6; counter++) {

			String temp = ((String) table.getValueAt(counter, 1)).trim();

			if (temp.equals(StringUtils.EMPTY)) {

				CustomerForm.getButtonUpdateCustomer().setVisible(Boolean.FALSE);
				break;
			}

			if (Integer.compare(counter, 5) == 0) {
				CustomerForm.getButtonUpdateCustomer().setVisible(Boolean.TRUE);
			}

		}

	}

}
