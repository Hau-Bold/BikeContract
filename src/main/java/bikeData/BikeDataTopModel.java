package bikeData;

import java.util.Calendar;

import constants.ShoreConstants;
import contractDigitalizer.BikeContract;
import utils.TableModel;

public class BikeDataTopModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.TRUE;

	public BikeDataTopModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (Integer.compare(column, 0) == 0) {
			return Boolean.FALSE;
		}
		return isEditable;
	}

	/** setting default values in table model */
	@Override
	public void initialize() {

		dataEntries = new Object[1][6];
		setDate();

		String service = BikeContract.getService();
		if (service != null) {
			service = service.trim().toUpperCase();

			if (service.equals(ShoreConstants.BIKE_COLLECTION)) {

				dataEntries[0][2] = Boolean.FALSE;
				dataEntries[0][3] = Boolean.TRUE;

				for (int i = 4; i < 6; i++) {
					dataEntries[0][i] = Boolean.FALSE;
				}
			} else {
				dataEntries[0][2] = Boolean.TRUE;

				for (int i = 3; i < 6; i++) {
					dataEntries[0][i] = Boolean.FALSE;
				}
			}

		} else {
			dataEntries[0][2] = Boolean.TRUE;

			for (int i = 3; i < 6; i++) {
				dataEntries[0][i] = Boolean.FALSE;
			}
		}

	}

	private void setDate() {

		Calendar calendar = Calendar.getInstance();
		dataEntries[0][0] = BikeContract.SIMPLE_DATE_FORMAT.format(calendar.getTime());
		fireTableDataChanged();

	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		if ((column == 0) || (column == 1)) {
			return String.class;
		}
		return Boolean.class;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;
		fireTableDataChanged();
	}

	@Override
	public void setValues(Boolean value) {

		for (int i = 2; i < 6; i++) {
			dataEntries[0][i] = value;
		}
		fireTableDataChanged();

	}

	@Override
	public void setDefaultValues() {

		dataEntries[0][2] = Boolean.TRUE;

		for (int i = 3; i < 6; i++) {
			dataEntries[0][i] = Boolean.FALSE;
		}
		fireTableDataChanged();
	}

}
