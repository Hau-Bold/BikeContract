package bikeData;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/** the class InspectionYearRenderer */
public class InspectionYearRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private BikeDataDownModel model;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		int selectedColumn = table.getSelectedColumn();

		if (selectedColumn > 0) {
			Boolean tmp = (Boolean) model.getValueAt(row, selectedColumn);
			model.setValueAt(!tmp, row, selectedColumn);
		}

		adjustModel(row, selectedColumn);

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

		if (selectedColumn > 0) {
			if (model.getValueAt(row, selectedColumn) == Boolean.TRUE) {
				for (int i = 1; i < 6; i++) {

					if (i != selectedColumn) {
						model.setValueAt(Boolean.FALSE, row, i);
					}

				}
				model.fireTableDataChanged();
			}
		}

	}

	public InspectionYearRenderer(BikeDataDownModel model) {
		this.model = model;
	}

}
