package customer;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import utils.Utils;

public class CustomerCellRenderer extends JLabel implements TableCellRenderer {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {

		this.setHorizontalTextPosition(SwingConstants.LEFT);
		this.setVerticalTextPosition(SwingConstants.CENTER);

		this.setHorizontalAlignment(SwingConstants.LEFT);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setText(Utils.handleTextWithSpace(String.valueOf(value)));

		return this;
	}

}
