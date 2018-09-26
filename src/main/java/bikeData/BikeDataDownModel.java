package bikeData;

import utils.TableModel;

public class BikeDataDownModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.TRUE;

	public BikeDataDownModel(String[] nameOfColumns) {
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

		for (int i = 1; i < 6; i++) {

			setValueAt(Boolean.FALSE, 0, i);
		}
	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {
		if ((column == 0)) {
			return String.class;
		}

		return Boolean.class;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;
	}

	@Override
	public void setValues(Boolean value) {

		for (int i = 1; i < 6; i++) {
			dataEntries[0][i] = value;
		}

		fireTableDataChanged();

	}

	@Override
	public void setDefaultValues() {

		for (int i = 1; i < 6; i++) {
			dataEntries[0][i] = Boolean.FALSE;
		}

		fireTableDataChanged();
	}

}
