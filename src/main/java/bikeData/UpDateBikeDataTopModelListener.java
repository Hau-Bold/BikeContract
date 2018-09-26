package bikeData;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import contract.ContractForm;

/** the class UpDateBikeDataTopModelListener */
public class UpDateBikeDataTopModelListener implements TableModelListener {

	@Override
	public void tableChanged(TableModelEvent e) {

		JButton updateBikeData = BikeDataForm.getButtonUpdateBikeData();

		if (BikeDataForm.clientComesFromDisplayContractsForm || ContractForm.clientComesFromDisplayBikeDataForm) {
			if (!updateBikeData.isVisible()) {
				updateBikeData.setVisible(Boolean.TRUE);
			}
		}

		else {
			BikeDataForm.getInstance().setNewContractHasBeenEdited(Boolean.TRUE);
		}

	}

}
