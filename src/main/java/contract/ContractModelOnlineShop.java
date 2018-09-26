package contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import constants.Constants;
import erp.ErpDataSet;
import utils.TableModel;
import utils.Utils;

/**
 * the class ContractModelOnlineShop
 *
 */
public class ContractModelOnlineShop extends TableModel {

	private static final long serialVersionUID = 1L;
	private boolean isEditable = Boolean.TRUE;
	private List<Boolean> isRowFromErpData = new ArrayList<Boolean>();

	/**
	 * Constructor.
	 * 
	 * @param nameOfColumns
	 *            - the name of the columns
	 */
	public ContractModelOnlineShop(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		if (row != -1) {
			/**
			 * quantity, ean, articel, producer aren't allowed to be editable, except the
			 * data set is not from erp data
			 */
			if (row < getCountOfSellpositions()) {

				Boolean isFromErpData = isRowFromErpData.get(row);
				if (isFromErpData) {
					return Boolean.FALSE;
				} else {
					if (column < 2) {
						return Boolean.FALSE;
					}

					else if (column >= 2 && column <= 3) {
						return Boolean.TRUE;
					}

					else if (Integer.compare(column, 4) == 0) {
						return Boolean.FALSE;
					}

					else {
						return Boolean.TRUE;
					}
				}
			}

			else {

				if (column > 4) {

					if (Integer.compare(row, getCountOfSellpositions()) == 0) {
						/** the subtotal */
						return Boolean.FALSE;
					}

					else if (Integer.compare(row, getCountOfSellpositions() + 7 - 1) < 0) {

						if (column > 4) {
							return Boolean.TRUE;
						}

					}

					else if (Integer.compare(row, getCountOfSellpositions() + 7 - 1) == 0) {
						/** the total */
						return Boolean.FALSE;
					}

				} else {
					return Boolean.FALSE;
				}

			}
		}
		return isEditable;
	}

	/** setting default values in table model */
	@Override
	public void initialize() {

		dataEntries = new Object[7][7];
		dataEntries[0][4] = Constants.SUBTOTAL;
		dataEntries[0][5] = 0.0;
		dataEntries[0][6] = 0.0;
		dataEntries[1][4] = Constants.DELIVERY_ASSEMBLY_FEE;
		dataEntries[2][4] = Constants.SERVICE_COST_GARAGE;
		dataEntries[3][4] = Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL;
		dataEntries[4][4] = Constants.DOWN_PAYMENT;
		dataEntries[5][4] = Constants.ESTATE;
		dataEntries[6][4] = Constants.TOTAL;
		dataEntries[6][5] = 0.0;
		dataEntries[6][6] = 0.0;

	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {
		if (column == 0) {
			return Integer.class;
		}

		else if ((column == 5) || (column == 6)) {
			return Double.class;
		}

		return String.class;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;

		if (allowFireTableDataChanged) {
			fireTableDataChanged();
		}

	}

	@Override
	public void appendErpDataToErpData(ErpDataSet erpDataSet, Boolean value) {

		Object[][] newData = null;

		/** append new erp dataset */

		String ean = erpDataSet.getEan();
		if (!lstIdentifier.contains(ean)) {

			/** for reacting on scanning same article */
			lstIdentifier.add(ean);

			/** instantiate new object with one more row */

			newData = new Object[getRowCount() + 1][getColumnCount()];

			/** copy old erp datasets */
			//
			for (int row = 0; row < getCountOfSellpositions(); row++) {
				for (int column = 0; column < getColumnCount(); column++) {
					newData[row][column] = dataEntries[row][column];
				}
			}

			/** quantity */
			newData[getCountOfSellpositions()][0] = 1;

			/** ean */
			newData[getCountOfSellpositions()][1] = erpDataSet.getEan();

			/** article */
			newData[getCountOfSellpositions()][2] = erpDataSet.getArticleName();
			/** brand =? producer */
			newData[getCountOfSellpositions()][3] = erpDataSet.getProducer();

			/** uvp */
			newData[getCountOfSellpositions()][5] = Utils.formatDouble(erpDataSet.getUvpErp());

			/** price from erp */
			newData[getCountOfSellpositions()][6] = Utils.formatDouble(erpDataSet.getSellPriceErp());

			// int ct = getCountOfSellpositions();

			isRowFromErpData.add(value);

			/** append rest of old data: */
			for (int row = getCountOfSellpositions(); row < dataEntries.length; row++) {
				for (int column = 0; column < getColumnCount(); column++) {
					newData[row + 1][column] = dataEntries[row][column];
				}
			}
			/** overwrite old entries */
			dataEntries = newData;
			setCountOfSellpositions(dataEntries.length - 7);
		} else {
			int rowOfDataSet = getRowExistingErpDataset(ean);

			int quantity = (int) dataEntries[rowOfDataSet][0];
			quantity++;
			dataEntries[rowOfDataSet][0] = quantity;

		}

		// Datatable refresh:
		this.fireTableDataChanged();
	}

	/***
	 * computes the row of the existing ean
	 * 
	 * @param ean
	 *            - the ean
	 * @return - the row of the ean in the model
	 */
	private int getRowExistingErpDataset(String ean) {
		int row = 0;

		while (!lstIdentifier.get(row).equals(ean)) {
			row++;
		}

		return row;
	}

	@Override
	public void appendSellPositions(List<SellPosition> lstSellPosition) {

		initialize();

		Object[][] newData = null;
		int coutOfSellpositions = lstSellPosition.size();

		// copy old values:
		newData = new Object[getRowCount() + coutOfSellpositions][getColumnCount()];

		/** copy initialized values to bottom: */
		for (int row = 0; row < getRowCount(); row++) {
			for (int column = 0; column < getColumnCount(); column++) {
				newData[row + coutOfSellpositions][column] = dataEntries[row][column];
			}
		}

		for (int row = 0; row < coutOfSellpositions; row++) {

			SellPosition sellPosition = lstSellPosition.get(row);

			String ean = sellPosition.getEan();
			if (!lstIdentifier.contains(ean)) {
				lstIdentifier.add(ean);
			}

			String producer = sellPosition.getProducer();
			String articleName = sellPosition.getArticleName();

			if (!(producer.equals(StringUtils.EMPTY)) || !(articleName.equals(StringUtils.EMPTY))) {

				int quantity = sellPosition.getQuantity();
				Double uvp = sellPosition.getUvp();
				Double housePrice = sellPosition.getHousePrice();

				newData[row][0] = quantity;
				newData[row][1] = ean;
				newData[row][2] = articleName;
				newData[row][3] = producer;
				newData[row][5] = uvp;
				newData[row][6] = housePrice;
			}
		}

		// overwriting data
		dataEntries = newData;
		setCountOfSellpositions(dataEntries.length - 7);
		fireTableDataChanged();
	}

	@Override
	public List<Boolean> getIsRowFromErpData() {
		return isRowFromErpData;
	}

	@Override
	public void reset() {
		lstIdentifier.clear();
		isRowFromErpData.clear();
		initialize();
		setCountOfSellpositions(dataEntries.length - 7);
		fireTableDataChanged();

	}

	@Override
	public void removeRow(int selectedRow) {
		if (dataEntries != null) {

			String ean = (String) getValueAt(selectedRow, 1);
			lstIdentifier.remove(ean);

			isRowFromErpData.remove(selectedRow);

			ContractForm.removeContractValuesListener();

			Object[][] newData = new Object[dataEntries.length - 1][getColumnCount()];

			/** copy upper part */

			for (int row = 0; row < selectedRow; row++) {

				for (int column = 0; column < getColumnCount(); column++) {
					newData[row][column] = dataEntries[row][column];
				}
			}

			/** copy lower part */
			for (int row = selectedRow + 1; row < dataEntries.length; row++) {

				for (int column = 0; column < getColumnCount(); column++) {
					newData[row - 1][column] = dataEntries[row][column];
				}
			}

			dataEntries = newData;
			countOfSellpositions--;

			if (Integer.compare(countOfSellpositions, 0) == 0) {
				clear();
				initialize();
			}

			ContractForm.addContractValuesListener();

			this.fireTableDataChanged();
		} else {
			return;
		}

	}

}
