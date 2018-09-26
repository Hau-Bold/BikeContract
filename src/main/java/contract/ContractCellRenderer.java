package contract;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import constants.Fonts;
import constants.IntersportColors;

/** the class ContractCellRenderer */
public class ContractCellRenderer extends DefaultTableCellRenderer {

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
		label.setFont(Fonts.italic);

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

		List<Boolean> isRowFromErpData = ((utils.TableModel) table.getModel()).getIsRowFromErpData();

		if (!isRowFromErpData.isEmpty() && row < isRowFromErpData.size()) {
			if (isRowFromErpData.get(row)) {
				label.setForeground(Color.BLACK);
			} else {
				label.setForeground(Color.RED);
			}
		} else {
			label.setForeground(Color.BLACK);
		}

		return label;
	}

	public static void setLstStatusSellposition(List<Boolean> lstStatusSellposition) {

		ContractCellRenderer.lstStatusSellposition = lstStatusSellposition;

	}

}
