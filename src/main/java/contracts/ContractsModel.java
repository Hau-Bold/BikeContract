package contracts;

import java.util.List;

import contract.Contract;
import utils.TableModel;

/**
 * the class ContractModelOnlineShop
 *
 */
public class ContractsModel extends TableModel {

	private static final long serialVersionUID = 1L;

	private boolean isEditable = Boolean.FALSE;

	public ContractsModel(String[] nameOfColumns) {
		super(nameOfColumns);
	}

	@Override
	public boolean isCellEditable(int row, int column) {

		return isEditable;
	}

	/** setting default values in table model */
	public void setModelData(List<Contract> lstContract) {

		int rowCount = lstContract.size();

		dataEntries = new Object[rowCount][14];

		for (int i = 0; i < rowCount; i++) {

			Contract contract = lstContract.get(i);

			dataEntries[i][0] = contract.getIdOfContract();
			dataEntries[i][1] = contract.getCreatedAt();
			dataEntries[i][2] = contract.getLastModified();

			/** the uvp parts */
			dataEntries[i][3] = contract.getDeliveryAssemblyFeeUVP();
			dataEntries[i][4] = contract.getServiceCostGarageUVP();
			dataEntries[i][5] = contract.getRedemptionDismantledPartsWheelUVP();
			dataEntries[i][6] = contract.getDownPaymentUVP();
			dataEntries[i][7] = contract.getEstateUVP();

			/** the houseproce parts */
			dataEntries[i][8] = contract.getDeliveryAssemblyFeeHousePrice();
			dataEntries[i][9] = contract.getServiceCostGarageHousePrice();
			dataEntries[i][10] = contract.getRedemptionDismantledPartsWheelHousePrice();
			dataEntries[i][11] = contract.getDownPaymentHousePrice();
			dataEntries[i][12] = contract.getEstateHousePrice();

			dataEntries[i][13] = Integer.compare(contract.getIsOnlineShop(), 0) == 0 ? Boolean.FALSE : Boolean.TRUE;

		}
	}

	/** returns the data types of the columns */
	@Override
	public Class<?> getColumnClass(int column) {

		if (column < 2) {
			return String.class;
		} else if ((column > 1) && (column < 13)) {
			return Double.class;
		} else {
			return Boolean.class;
		}

	}

	@Override
	public void setValueAt(Object value, int row, int column) {

		dataEntries[row][column] = value;
		fireTableDataChanged();

	}

}
