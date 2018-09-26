package customer;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import constants.Constants;

public class SaveCustomerListener implements TableModelListener {

	private JTable table;

	public SaveCustomerListener(JTable table) {
		this.table = table;
	}

	@Override
	public void tableChanged(TableModelEvent event) {

		for (int counter = 0; counter < 6; counter++) {

			String temp = (String) table.getValueAt(counter, 1);

			if (temp == null || temp.equals(Constants.EMPTY_STRING)) {

				if (CustomerForm.getButtonSaveCustomer().isVisible()) {
					CustomerForm.getButtonSaveCustomer().setVisible(Boolean.FALSE);
				}
				break;
			}

			if (Integer.compare(counter, 5) == 0) {
				CustomerForm.getButtonSaveCustomer().setVisible(Boolean.TRUE);
			}

		}

	}

}
