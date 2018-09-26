package administration;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/** the class AdministrationStatusRenderer */
public class AdministrationStatusRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	private AdministrationModel model;

	public AdministrationStatusRenderer(AdministrationModel model) {
		this.model = model;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		setHorizontalAlignment(JLabel.CENTER);
		int selectedColumn = table.getSelectedColumn();

		if ((selectedColumn >= 1) && (selectedColumn <= 2)) {
			Boolean tmp = (Boolean) model.getValueAt(row, selectedColumn);

			model.setValueAt(!tmp, row, selectedColumn);
			adjustModel(row, selectedColumn);
		}

		if (value instanceof String) {

			label.setText(handleValue((String.valueOf(value))));
		}

		return label;
	}

	private String handleValue(String value) {
		return "<html><font color=\"green\">" + value + "</font></html>";
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

		if ((selectedColumn >= 1) && (selectedColumn <= 2)) {
			if (model.getValueAt(row, selectedColumn) == Boolean.TRUE) {
				for (int i = 1; i <= 2; i++) {

					if (i != selectedColumn) {
						model.setValueAt(Boolean.FALSE, row, i);
					}

				}
			}

			/** user isn't allowed to leave this section unselected */
			else if (model.getValueAt(row, selectedColumn) == Boolean.FALSE) {

				for (int i = 1; i <= 2; i++) {

					if (i != selectedColumn) {
						model.setValueAt(Boolean.TRUE, row, i);
					}

				}
			}

			model.fireTableDataChanged();

		}

	}

}
