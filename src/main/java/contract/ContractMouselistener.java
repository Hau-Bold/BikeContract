package contract;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import constants.Constants;
import contractDigitalizer.BikeContract;

/**
 * 
 * the class ContractMouselistener
 *
 * -it controls the removing of sellpositions from model
 *
 */
public class ContractMouselistener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getClickCount() == 2) {

			/** to remove a single Sellposition */

			JTable table = (JTable) e.getSource();
			utils.TableModel model = (utils.TableModel) table.getModel();
			int row = table.getSelectedRow();
			if ((Integer.compare(row, -1) != 0) && (row < model.getCountOfSellpositions())) {

				int choice;

				int quantity = table.getValueAt(row, 0) == null ? 0 : (int) table.getValueAt(row, 0);
				String articleName = null;
				String producer = null;
				Double uvp = 0.0;
				Double housePrice = 0.0;
				if (BikeContract.isOnlineShop) {
					articleName = table.getValueAt(row, 2) == null ? Constants.EMPTY_STRING
							: (String) table.getValueAt(row, 2);
					String ean = table.getValueAt(row, 1) == null ? Constants.EMPTY_STRING
							: (String) table.getValueAt(row, 1);
					producer = table.getValueAt(row, 3) == null ? Constants.EMPTY_STRING
							: (String) table.getValueAt(row, 3);
					uvp = table.getValueAt(row, 5) == null ? 0.0 : (Double) table.getValueAt(row, 5);
					housePrice = table.getValueAt(row, 6) == null ? 0.0 : (Double) table.getValueAt(row, 6);

					choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.REMOVE_SELLPOSITION, quantity, ean, articleName, producer, uvp,
									housePrice),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				} else {

					articleName = table.getValueAt(row, 2) == null ? Constants.EMPTY_STRING
							: (String) table.getValueAt(row, 2);
					String ean = table.getValueAt(row, 1) == null ? Constants.EMPTY_STRING
							: (String) table.getValueAt(row, 1);
					producer = table.getValueAt(row, 3) == null ? Constants.EMPTY_STRING
							: (String) table.getValueAt(row, 3);
					uvp = table.getValueAt(row, 5) == null ? 0.0 : (Double) table.getValueAt(row, 5);
					housePrice = table.getValueAt(row, 6) == null ? 0.0 : (Double) table.getValueAt(row, 6);

					choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.REMOVE_SELLPOSITION_ADVARICS, quantity, ean, articleName, producer,
									uvp, housePrice),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				}

				if (Integer.compare(choice, 0) == 0) {

					if ((Integer.compare(quantity, 1) == 0) || (Integer.compare(quantity, 0) == 0)) {
						model.removeRow(row);
					} else {

						quantity--;
						model.setValueAt(quantity, row, 0);
					}

				} else {
					return;
				}
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
