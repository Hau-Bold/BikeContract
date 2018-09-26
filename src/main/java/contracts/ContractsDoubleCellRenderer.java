package contracts;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import constants.Fonts;
import constants.IntersportColors;

/** the class ContractsDoubleCellRenderer */
public class ContractsDoubleCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(Fonts.helvetica);

		if (isSelected) {
			label.setForeground(IntersportColors.INTERSPORT_CORAL);
		} else {
			label.setForeground(IntersportColors.INTERSPORT_BLACK);
		}

		if (row % 3 == 0) {
			label.setBackground(IntersportColors.INTERSPORT_WHITE);
		} else {
			label.setBackground(IntersportColors.INTERSPORT_YELLOW);
		}

		return label;
	}

}
