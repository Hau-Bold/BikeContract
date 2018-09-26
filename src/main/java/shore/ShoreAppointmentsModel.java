package shore;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import constants.Constants;
import utils.TableModel;

/**
 * the class ShoreAppointmentsModel
 *
 */
public class ShoreAppointmentsModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.FALSE;

	public ShoreAppointmentsModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		return isEditable;
	}

	/** setting default values in table model */
	public void initialize(List<Appointment> lstAppointment) {

		int rowCount = lstAppointment.size();

		dataEntries = new Object[rowCount][8];

		for (int i = 0; i < rowCount; i++) {

			Appointment appointment = lstAppointment.get(i);

			String dayOfWeek = appointment.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY);

			int day = ShoreUtils.getDayAsInt(dayOfWeek);

			dataEntries[i][0] = day;
			dataEntries[i][1] = appointment.getStartTime().getHour() + Constants.COLON
					+ appointment.getStartTime().getMinute();
			dataEntries[i][2] = appointment.getCustomer().getSalutation();
			dataEntries[i][3] = appointment.getCustomer().getPreName();
			dataEntries[i][4] = appointment.getCustomer().getCustomerName();
			dataEntries[i][5] = appointment.getSubject();
			dataEntries[i][6] = appointment.getService();
			dataEntries[i][7] = appointment.getEmployee();

		}

		fireTableDataChanged();
	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		if (Integer.compare(column, 0) == 0) {
			return Integer.class;
		} else {
			return String.class;
		}

	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;
		fireTableDataChanged();

	}

}
