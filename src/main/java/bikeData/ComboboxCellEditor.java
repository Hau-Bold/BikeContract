package bikeData;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/** the class ComboboxCellEditor */
public class ComboboxCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public ComboboxCellEditor(String[] items) {

		super(new JComboBox<String>(items));
	}

}
