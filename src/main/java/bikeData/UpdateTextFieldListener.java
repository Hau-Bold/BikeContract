package bikeData;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** the class UpdateTextFieldListener */
public class UpdateTextFieldListener implements DocumentListener {

	@Override
	public void insertUpdate(DocumentEvent e) {
		BikeDataForm.getButtonUpdateBikeData().setVisible(Boolean.TRUE);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		BikeDataForm.getButtonUpdateBikeData().setVisible(Boolean.TRUE);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}
