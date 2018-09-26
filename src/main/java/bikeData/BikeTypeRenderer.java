package bikeData;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/** the class BikeTypeRenderer */
public class BikeTypeRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private BikeDataMiddleModel model;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		setHorizontalAlignment(JLabel.CENTER);

		int selectedColumn = table.getSelectedColumn();

		if (selectedColumn > 0) {

			Boolean tmp = (Boolean) model.getValueAt(row, selectedColumn);
			model.setValueAt(!tmp, row, selectedColumn);
			adjustModel(row, selectedColumn);
			model.fireTableDataChanged();
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

		/** first two boolean columns */
		if (selectedColumn < 3) {

			model.setValueAt(Boolean.FALSE, row, 3 - selectedColumn);
		}

		else {

			/** deriving remaining columns */
			for (int i = 3; i < selectedColumn; i++) {

				model.setValueAt(Boolean.FALSE, row, i);
			}

			for (int i = selectedColumn + 1; i < 12; i++) {
				model.setValueAt(Boolean.FALSE, row, i);
			}
		}

	}

	/**
	 * Constructor
	 * 
	 * @param model
	 *            - the bike data middle model
	 */
	public BikeTypeRenderer(BikeDataMiddleModel model) {
		this.model = model;

	}

}
