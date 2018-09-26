package bikeData;

import utils.TableModel;

public class BikeDataMiddleModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.TRUE;

	public BikeDataMiddleModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column > 0 && (column < 12)) {
			return isEditable;
		}
		return Boolean.FALSE;
	}

	/** setting default values in table model */
	@Override
	public void initialize() {

		dataEntries = new Object[1][12];

		setValueAt(Boolean.TRUE, 0, 1);
		setValueAt(Boolean.FALSE, 0, 2);
		setValueAt(Boolean.TRUE, 0, 3);

		for (int i = 4; i < 12; i++) {
			setValueAt(Boolean.FALSE, 0, i);
		}

	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		if (column == 0) {
			return String.class;
		} else {
			return Boolean.class;
		}
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		dataEntries[row][column] = value;
	}

	@Override
	public void setValues(Boolean value) {

		for (int i = 1; i < 12; i++) {
			dataEntries[0][i] = value;
		}

		fireTableDataChanged();

	}

	@Override
	public void setDefaultValues() {

		dataEntries[0][1] = Boolean.TRUE;
		dataEntries[0][2] = Boolean.FALSE;
		dataEntries[0][3] = Boolean.TRUE;

		for (int i = 4; i < 12; i++) {
			dataEntries[0][i] = Boolean.FALSE;
		}
		fireTableDataChanged();
	}

}
