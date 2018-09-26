package bikeData;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/** the class SelectionChangedListener */
public class SelectionChangedListener implements ItemListener {

	@Override
	public void itemStateChanged(ItemEvent e) {
		BikeDataForm.getButtonUpdateBikeData().setVisible(Boolean.TRUE);
	}

}
