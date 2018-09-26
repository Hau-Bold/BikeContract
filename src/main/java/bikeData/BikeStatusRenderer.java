package bikeData;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/** the class BikeStatusRenderer */
public class BikeStatusRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private BikeDataTopModel model;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		setHorizontalAlignment(JLabel.CENTER);
		int selectedColumn = table.getSelectedColumn();

		if (selectedColumn > 1) {
			Boolean tmp = (Boolean) model.getValueAt(row, selectedColumn);

			model.setValueAt(!tmp, row, selectedColumn);
			adjustModel(row, selectedColumn);
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	}

	/**
	 * adjusts the model: only one true value is allowed
	 * 
	 * @param row
	 *            - the row
	 * @param selectedColumn
	 *            - the selected column
	 */
	private void adjustModel(int row, int selectedColumn) {

		if (selectedColumn > 1) {
			if (model.getValueAt(row, selectedColumn) == Boolean.TRUE) {
				for (int i = 2; i < 6; i++) {

					if (i != selectedColumn) {
						model.setValueAt(Boolean.FALSE, row, i);
					}

				}
				model.fireTableDataChanged();
			}

			else if (model.getValueAt(row, selectedColumn) == Boolean.FALSE) {
				Boolean result = Boolean.TRUE;

				for (int i = 2; i < 6; i++) {

					result = (result && (Boolean) model.getValueAt(row, i));
				}

				/** nothing is selected */
				if (!result) {
					model.setValueAt(Boolean.TRUE, row, 2);
				}

			}

		}

	}

	public BikeStatusRenderer(BikeDataTopModel model) {
		this.model = model;
	}

}
