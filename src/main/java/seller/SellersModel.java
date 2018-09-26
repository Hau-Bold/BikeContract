package seller;

import java.util.List;

import utils.TableModel;

/**
 * the class SellersModel
 *
 */
public class SellersModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.FALSE;

	public SellersModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		return isEditable;
	}

	/** setting default values in table model */
	public void setModelData(List<Seller> lstSeller) {

		int rowCount = lstSeller.size();

		dataEntries = new Object[rowCount][5];

		for (int i = 0; i < rowCount; i++) {

			Seller seller = lstSeller.get(i);

			dataEntries[i][0] = seller.getPrename();
			dataEntries[i][1] = seller.getName();
			dataEntries[i][2] = seller.getNumber();
			dataEntries[i][3] = seller.getMobile();
			dataEntries[i][4] = seller.getNameOfImage();

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
