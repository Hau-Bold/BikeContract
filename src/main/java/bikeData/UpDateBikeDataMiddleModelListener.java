package bikeData;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import contract.ContractForm;

/** the class UpDateBikeDataMiddleModelListener */
public class UpDateBikeDataMiddleModelListener implements TableModelListener {

	private BikeDataForm instance;

	public UpDateBikeDataMiddleModelListener(BikeDataForm instance) {

		this.instance = instance;
	}

	@Override
	public void tableChanged(TableModelEvent e) {

		JButton updateBikeData = BikeDataForm.getButtonUpdateBikeData();

		if (BikeDataForm.clientComesFromDisplayContractsForm || ContractForm.clientComesFromDisplayBikeDataForm) {
			if (!updateBikeData.isVisible()) {
				updateBikeData.setVisible(Boolean.TRUE);
			}
		}

		else {

			instance.setNewContractHasBeenEdited(Boolean.TRUE);

		}

	}

}
