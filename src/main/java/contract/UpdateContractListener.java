package contract;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/** the class UpdateContractListener */
public class UpdateContractListener implements TableModelListener {

	@Override
	public void tableChanged(TableModelEvent event) {

		JButton updateContract = ContractForm.getButtonUpdateContract();
		JButton saveContract = ContractForm.getButtonSaveContract();

		if (!updateContract.isVisible()) {

			updateContract.setVisible(Boolean.TRUE);

			saveContract.setEnabled(Boolean.FALSE);

			if (ContractForm.ignoreInvalidSellPositions) {
				ContractForm.ignoreInvalidSellPositions = Boolean.FALSE;
			}
		}
	}

}
