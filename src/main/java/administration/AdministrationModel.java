package administration;

import utils.TableModel;

/** the class AdministrationModel */
public class AdministrationModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private Boolean isEditable = Boolean.TRUE;

	public AdministrationModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		return isEditable;
	}

	/**
	 * setting default values in table model
	 * 
	 * @param administration
	 *            - the administration
	 */

	public void initialize(Administration administration) {

		dataEntries = new Object[1][7];
		dataEntries[0][0] = administration.getErpLastReaded();
		dataEntries[0][1] = administration.getIsAll();
		dataEntries[0][2] = administration.getIsOnlineShop();
		dataEntries[0][3] = administration.getUseHousekeeping();
		dataEntries[0][4] = administration.getHousekeepingContracts();
		dataEntries[0][5] = administration.getHousekeepingPdf();
		dataEntries[0][6] = administration.getUsePassword();

	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		if ((Integer.compare(column, 0) == 0)) {
			return String.class;
		} else if ((Integer.compare(column, 4) == 0) || (Integer.compare(column, 5) == 0)) {
			return Integer.class;
		} else if ((Integer.compare(column, 1) == 0) || (Integer.compare(column, 2) == 0)
				|| (Integer.compare(column, 3) == 0)) {
			return Boolean.class;
		} else if ((Integer.compare(column, 6) == 0)) {
			return Boolean.class;
		} else {
			return Object.class;
		}

	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;

		if (allowFireTableDataChanged) {
			fireTableDataChanged();
		}

	}

	/**
	 * yields the Administration from the model
	 * 
	 * @return the Administration
	 */
	public Administration getAdministrationFromModel() {

		String erpLastReaded = (String) dataEntries[0][0];
		Boolean all = (Boolean) dataEntries[0][1];
		Boolean onlineShop = (Boolean) dataEntries[0][2];
		Boolean housekeeping = (Boolean) dataEntries[0][3];
		Integer housekeepingContracts = (Integer) dataEntries[0][4];
		Integer housekeepingPdf = (Integer) dataEntries[0][5];
		Boolean usePassword = (Boolean) dataEntries[0][6];

		return new Administration(erpLastReaded, usePassword, housekeeping, housekeepingPdf, housekeepingContracts,
				onlineShop, all);
	}

}
