package bikeData;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import constants.IntersportColors;

/** the class ComboboxRenderer */
public class ComboboxRenderer extends JComboBox<String> implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	public ComboboxRenderer(String[] items) {
		super(items);

		this.setBorder(BorderFactory.createRaisedBevelBorder());
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			super.setBackground(IntersportColors.INTERSPORT_WHITE);
		} else {
			setForeground(table.getForeground());
			setBackground(IntersportColors.INTERSPORT_GREEN);
		}
		setSelectedItem(value);
		return this;
	}

}
