package contracts;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import constants.Constants;
import constants.Fonts;
import constants.IntersportColors;

/** the class ContractsBooleanCellRenderer */
public class ContractsBooleanCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(Fonts.helvetica);

		if (isSelected) {
			label.setForeground(IntersportColors.INTERSPORT_CORAL);

			label.setText(handle(value, Boolean.FALSE));
		} else {

			label.setText(handle(value, Boolean.TRUE));
		}

		if (row % 3 == 0) {
			label.setBackground(IntersportColors.INTERSPORT_WHITE);
		} else {
			label.setBackground(IntersportColors.INTERSPORT_YELLOW);
		}

		return label;
	}

	private String handle(Object value, Boolean mode) {

		Boolean valueAsBoolean = (Boolean) value;
		if (mode) {
			return !valueAsBoolean ? "<html><font color=\"red\">" + Constants.FALSE + "</font></html>"
					: "<html><font color=\"green\">" + Constants.TRUE + "</font></html>";
		} else {
			return !valueAsBoolean ? Constants.FALSE : Constants.TRUE;
		}
	}

}
