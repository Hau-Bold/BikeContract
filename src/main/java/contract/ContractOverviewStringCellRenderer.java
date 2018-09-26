package contract;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import constants.Constants;
import constants.Fonts;
import constants.IntersportColors;

/** the class ContractOverviewStringCellRenderer */
public class ContractOverviewStringCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private static List<Boolean> lstStatusSellposition = new ArrayList<Boolean>();

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Boolean status = null;

		if (lstStatusSellposition.size() > row) {
			status = lstStatusSellposition.get(row);
		}

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setHorizontalAlignment(JLabel.CENTER);
		label.setFont(Fonts.helvetica);

		if (!(row % 3 == 0)) {

			if (status != null) {
				if (!status) {
					label.setBackground(IntersportColors.INTERSPORT_GRAY);
				} else {
					label.setBackground(IntersportColors.INTERSPORT_YELLOW);
				}
			} else {

				label.setBackground(IntersportColors.INTERSPORT_YELLOW);
			}
		} else {
			if (status != null) {
				if (!status) {
					label.setBackground(IntersportColors.INTERSPORT_GRAY);
				} else {
					label.setBackground(IntersportColors.INTERSPORT_WHITE);
				}
			} else {

				label.setBackground(IntersportColors.INTERSPORT_WHITE);
			}
		}

		if (value != null) {

			String valueAsString = (String) value;
			label.setText(valueAsString);
		} else {
			label.setText(Constants.EMPTY_STRING);
		}

		return label;
	}

	// get & set follows below here
	public static void setLstStatusSellposition(List<Boolean> lstStatusSellposition) {
		ContractOverviewStringCellRenderer.lstStatusSellposition = lstStatusSellposition;
	}

}
