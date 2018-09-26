package shore;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import constants.Fonts;
import constants.IntersportColors;
import constants.ShoreConstants;

/** the class AppointmentsRenderer */
public class AppointmentsRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(Fonts.italic);

		if (Integer.compare(column, 0) == 0) {

			int dayAsInt = (int) value;
			label.setText(getDayAsString(dayAsInt));
		}

		if (row % 3 == 0) {
			label.setBackground(IntersportColors.INTERSPORT_WHITE);
		} else {
			label.setBackground(IntersportColors.INTERSPORT_CORAL1);
		}

		label.setForeground(IntersportColors.INTERSPORT_BLACK);

		return label;
	}

	private String getDayAsString(int dayAsInt) {

		if (Integer.compare(dayAsInt, 2) == 0) {
			return ShoreConstants.MONDAY;
		}

		else if (Integer.compare(dayAsInt, 3) == 0) {
			return ShoreConstants.TUESDAY;
		} else if (Integer.compare(dayAsInt, 4) == 0) {
			return ShoreConstants.WEDNESDAY;
		} else if (Integer.compare(dayAsInt, 5) == 0) {
			return ShoreConstants.THURSDAY;
		} else if (Integer.compare(dayAsInt, 6) == 0) {
			return ShoreConstants.FRIDAY;
		} else if (Integer.compare(dayAsInt, 7) == 0) {
			return ShoreConstants.SATURDAY;
		} else if (Integer.compare(dayAsInt, 1) == 0) {
			return ShoreConstants.SUNDAY;
		}

		return null;
	}

}
