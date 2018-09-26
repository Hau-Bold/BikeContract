package seller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JTable;

import constants.Constants;
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;

/** the class SellersMouseListener */
public class SellersMouseListener implements MouseListener {

	private JTable table;

	/**
	 * Constructor.
	 * 
	 * @param table
	 *            - table
	 * @param customer
	 *            - the customer
	 */
	public SellersMouseListener(JTable table) {
		this.table = table;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		int row = table.getSelectedRow();

		if (Integer.compare(row, -1) != 0) {

			String prename = (String) table.getValueAt(row, 0);
			String name = (String) table.getValueAt(row, 1);
			String number = (String) table.getValueAt(row, 2);
			String mobile = (String) table.getValueAt(row, 3);
			String nameOfImage = (String) table.getValueAt(row, 4);
			Seller seller = new Seller(prename, name, number, mobile, nameOfImage);

			SellersForm instanceOfSellers = SellersForm.getInstance();
			instanceOfSellers.setVisible(Boolean.FALSE);

			SellerForm instanceOfSeller = SellerForm.getInstance();
			instanceOfSeller.removeDocumentListeners();
			instanceOfSeller.setSeller(seller);
			instanceOfSeller.setSentinelBooleans(Boolean.TRUE);
			instanceOfSeller.addDocumentListeners();
			SellerForm.getButtonSaveSeller().setVisible(Boolean.FALSE);
			SellerForm.getButtonSaveSeller().setText(Constants.UPDATE_SELLER);
			BikeContract.setIdOfSeller(DatabaseLogic.getIdOfSeller(seller));

			instanceOfSeller.setState(JFrame.NORMAL);

			instanceOfSellers.dispose();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
