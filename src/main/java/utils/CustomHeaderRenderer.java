package utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

/**
 * the class CustomHeaderRenderer
 * 
 */
public class CustomHeaderRenderer extends JLabel implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Color color;

	/**
	 * Constructor
	 * 
	 * @param isCustomer
	 *            - determines the color of the header
	 */
	public CustomHeaderRenderer(Color color) {
		this.color = color;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		String content = String.valueOf(value);

		this.setOpaque(Boolean.TRUE);
		this.setVerticalAlignment(SwingConstants.CENTER);
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setVerticalTextPosition(SwingConstants.CENTER);
		this.setText(Utils.handleText(content));

		this.setBackground(color);
		this.setBorder(BorderFactory.createRaisedBevelBorder());

		return this;
	}

}
