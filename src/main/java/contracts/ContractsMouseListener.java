package contracts;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;

import constants.Constants;
import contract.Contract;
import contract.ContractForm;
import contract.SellPosition;
import contractDigitalizer.BikeContract;
import customer.Customer;
import database.DatabaseLogic;

/** the class ContractsMouseListener */
public class ContractsMouseListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		JTable table = (JTable) (arg0.getSource());

		int row = table.getSelectedRow();

		if (Integer.compare(row, -1) != 0) {
			int idOfContract = (int) table.getValueAt(row, 0);
			BikeContract.setIdOfContract(idOfContract);

			String createdAt = (String) table.getValueAt(row, 1);
			String lastModified = (String) table.getValueAt(row, 2);

			/** the uvp part of contract */
			Double deliveryAssemblyFeeUVP = (Double) table.getValueAt(row, 3);
			Double serviceCostGarageUVP = (Double) table.getValueAt(row, 4);
			Double redemptionDismantledPartsWheelUVP = (Double) table.getValueAt(row, 5);
			Double downPaymentUVP = (Double) table.getValueAt(row, 6);
			Double estateUVP = (Double) table.getValueAt(row, 7);

			/** the houseprice part of contract */
			Double deliveryAssemblyFeeHousePrice = (Double) table.getValueAt(row, 8);
			Double serviceCostGarageHousePrice = (Double) table.getValueAt(row, 9);
			Double redemptionDismantledPartsWheelHousePrice = (Double) table.getValueAt(row, 10);
			Double downPaymentHousePrice = (Double) table.getValueAt(row, 11);
			Double estateHousePrice = (Double) table.getValueAt(row, 12);

			Boolean isOnlineShop = (Boolean) table.getValueAt(row, 13);
			BikeContract.isOnlineShop = isOnlineShop;

			/** the contract */
			Contract contract = new Contract(idOfContract, createdAt, lastModified, deliveryAssemblyFeeUVP,
					serviceCostGarageUVP, redemptionDismantledPartsWheelUVP, downPaymentUVP, estateUVP,
					deliveryAssemblyFeeHousePrice, serviceCostGarageHousePrice,
					redemptionDismantledPartsWheelHousePrice, downPaymentHousePrice, estateHousePrice,
					isOnlineShop ? 1 : 0);

			/** catching sellpositions */
			List<SellPosition> lstSellposition = null;

			try {
				lstSellposition = DatabaseLogic.getSellPositions(idOfContract);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			contract.setSellPositions(lstSellposition);
			Customer customer = BikeContract.getCustomer();

			ContractsForm.getInstance().setVisible(Boolean.FALSE);

			ContractForm instaceOfContractForm = ContractForm.getInstance(Boolean.TRUE, Boolean.FALSE);
			ContractForm.isBikeDataSaved = Boolean.TRUE;
			ContractForm.contractIsSaved = Boolean.TRUE;
			if (!ContractForm.getButtonSaveContract().isEnabled()) {
				ContractForm.getButtonSaveContract().setEnabled(Boolean.TRUE);
			}
			ContractForm.removeUpdateContractListener();
			instaceOfContractForm.setModelData(contract);

			/** when the model is filled, the updateContractListener may be added */
			ContractForm.addUpdateContractListener();

			JButton buttonUpdateContract = ContractForm.getButtonUpdateContract();

			if (buttonUpdateContract.isVisible()) {
				buttonUpdateContract.setVisible(Boolean.FALSE);
			}

			instaceOfContractForm.setTitle(String.format(Constants.TITLE_DISPLAY_CONTRACT, customer.getCustomerName(),
					customer.getPrename(), idOfContract));
			instaceOfContractForm.setState(JFrame.NORMAL);
			instaceOfContractForm.setVisible(Boolean.TRUE);
			ContractsForm.getInstance().dispose();
		} else {
			return;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
