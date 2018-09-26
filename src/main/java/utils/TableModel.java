package utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import contract.SellPosition;
import erp.ErpDataSet;

/** the class TableModel */
public class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] nameOfColumns;
	protected Boolean allowFireTableDataChanged = Boolean.TRUE;
	protected int countOfSellpositions = 0;
	protected static List<String> lstIdentifier = new ArrayList<String>();
	protected static List<Boolean> isRowFromErpData = new ArrayList<Boolean>();

	/**
	 * Constructor.
	 * 
	 * @param nameOfColumns
	 *            - the name of the columns
	 */
	public TableModel(String[] nameOfColumns) {
		this.nameOfColumns = nameOfColumns;
	}

	protected Object[][] dataEntries = null;

	public Object[][] getDataEntries() {
		return dataEntries;
	}

	/**
	 * yields the index of the column with name = columnName
	 * 
	 * @param columnName
	 *            - the name of the column
	 * @return the index
	 */
	public int getIndexOfColumn(String columnName) {
		int index = -1;

		for (int column = 0; column < nameOfColumns.length; column++) {
			if (nameOfColumns[column].equals(columnName)) {
				index = column;
				break;
			}
		}
		return index;
	}

	/**
	 * returns count of columns
	 */
	@Override
	public int getColumnCount() {
		return nameOfColumns.length;
	}

	@Override
	public int getRowCount() {
		if (dataEntries != null) {
			return dataEntries.length;
		}
		return 0;
	}

	@Override
	public Object getValueAt(int row, int column) {

		if (row < getRowCount() && column < getColumnCount()) {
			return dataEntries[row][column];
		}
		return null;
	}

	public ArrayList<Object> getColumnDataAsList(int column) {

		ArrayList<Object> response = new ArrayList<Object>();

		for (int row = 0; row < dataEntries.length; row++) {
			response.add(getValueAt(row, column));
		}

		return response;
	}

	@Override
	public String getColumnName(int column) {
		return nameOfColumns[column];
	}

	/**
	 * to append a new row ton the model
	 */
	public void appendNewRow() {

		Object[][] newData;
		if (dataEntries == null) {
			newData = new Object[1][nameOfColumns.length];
			newData[0][0] = 1;
		} else {
			newData = new Object[dataEntries.length + 1][nameOfColumns.length];

			for (int row = 0; row < dataEntries.length; row++) {
				for (int column = 0; column < nameOfColumns.length; column++) {
					newData[row][column] = dataEntries[row][column];
				}
			}
			newData[dataEntries.length][0] = dataEntries.length + 1;
		}
		dataEntries = newData;
		this.fireTableDataChanged();
	}

	/**
	 * to remove entries from model
	 */
	public void clear() {
		if (dataEntries == null) {
			return;
		} else {
			Object[][] newData = null;
			dataEntries = newData;
		}
		this.fireTableDataChanged();
	}

	public void deleteSelectedRow(int[] row) {
		if (dataEntries != null) {
			Object[][] newData = new Object[dataEntries.length - row.length][nameOfColumns.length];
			int rowCounter = 0;
			// Kopiere den oberen Teil:
			for (int index = 0; index < row[0]; index++) {
				rowCounter++;
				for (int column = 0; column < nameOfColumns.length; column++) {
					newData[index][column] = dataEntries[index][column];
				}
			}
			// Kopiere den unteren Teil
			for (int index = row[row.length - 1] + 1; index < dataEntries.length; index++) {
				rowCounter++;
				newData[index - row.length][0] = rowCounter;
				for (int column = 1; column < nameOfColumns.length; column++) {
					newData[index - row.length][column] = dataEntries[index][column];
				}
			}
			dataEntries = newData;

			this.fireTableDataChanged();
		} else {
			return;
		}
	}

	// is overwritten by tablemodels that inherit this class
	public void initialize() {
	}

	// is overwritten by tablemodel that inherit this class
	public void setValues(Boolean value) {
	}

	// is overwritten by tablemodel that inherit this class
	public void setDefaultValues() {
	}

	// is overwritten by tablemodel that inherit this class
	public void appendSellPositions(List<SellPosition> lstSellPosition) {
	}

	public void reset() {
		initialize();
		fireTableDataChanged();

	}

	public void resetIdentifier() {
		lstIdentifier.clear();
	}

	// is overwritten by tablemodel that inherit this class
	public void removeRow(int selectedRow) {

	}

	// set & get follows below here
	// get & set follows below here

	public Boolean getAllowFireTableDataChanged() {
		return allowFireTableDataChanged;
	}

	public void setAllowFireTableDataChanged(Boolean allowFireTableDataChanged) {
		this.allowFireTableDataChanged = allowFireTableDataChanged;
	}

	// is overwritten by tablemodel that inherit this class
	public void appendErpDataToErpData(ErpDataSet erpDataSet, Boolean value) {
	}

	public int getCountOfSellpositions() {
		return countOfSellpositions;
	}

	public List<String> getLstIdentifier() {
		return lstIdentifier;
	}

	public void setCountOfSellpositions(int countOfSellpositions) {
		this.countOfSellpositions = countOfSellpositions;
	}

	public List<Boolean> getIsRowFromErpData() {
		return isRowFromErpData;
	}

}
