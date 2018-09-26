package administration;

import javax.swing.JButton;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/** the class UpdateAdministrationModelListener */
public class UpdateAdministrationModelListener implements TableModelListener {

	@Override
	public void tableChanged(TableModelEvent e) {

		JButton updateAdministration = AdministrationForm.getButtonupdateAdministration();

		updateAdministration.setVisible(Boolean.TRUE);
	}

}
