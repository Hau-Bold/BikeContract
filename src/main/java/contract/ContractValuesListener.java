package contract;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import contractDigitalizer.BikeContract;
import utils.IntegerCellRender;
import utils.TableModel;

/** the class ContractValuesListener */
public class ContractValuesListener implements TableModelListener {

	private TableModel model;
	private final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
	List<Boolean> lstStatusSellposition = new ArrayList<Boolean>();
	Boolean isOnlineShop = null;

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            - the model
	 * @param contractTableModelChangedListener
	 */
	public ContractValuesListener(utils.TableModel model) {
		this.model = model;
		isOnlineShop = BikeContract.isOnlineShop;
	}

	@Override
	public void tableChanged(TableModelEvent event) {
		model.setAllowFireTableDataChanged(Boolean.FALSE);
		lstStatusSellposition.clear();

		ArrayList<Object> quantityColumnAsList = model.getColumnDataAsList(0);
		// ArrayList<Object> uVPColumnAsList = isOnlineShop ?
		// model.getColumnDataAsList(5) : model.getColumnDataAsList(6);
		// ArrayList<Object> housePriceColumnAsList = isOnlineShop ?
		// model.getColumnDataAsList(6)
		// : model.getColumnDataAsList(7);
		ArrayList<Object> uVPColumnAsList = model.getColumnDataAsList(5);
		ArrayList<Object> housePriceColumnAsList = model.getColumnDataAsList(6);

		double housePrice = 0.0;
		double uvp = 0.0;
		double estateUVP = 0.0;
		double estateHousePrice = 0.0;

		/** processing sellpositions */
		for (int i = 0; i < model.getCountOfSellpositions(); i++) {

			Integer quantity = (Integer) quantityColumnAsList.get(i);
			Double uvpTemp = (Double) uVPColumnAsList.get(i);
			Double housePriceTemp = (Double) housePriceColumnAsList.get(i);

			if (((uvpTemp == null) || (housePriceTemp == null))
					|| ((Double.compare(uvpTemp, 0.0) == 0) || (Double.compare(housePriceTemp, 0.0) == 0))) {
				lstStatusSellposition.add(Boolean.FALSE);
			} else {
				lstStatusSellposition.add(Boolean.TRUE);
			}

			quantity = quantity == null ? 1 : quantity;
			/** not beeing in line of subtotal */

			if (uvpTemp != null) {

				uvp += quantity * uvpTemp;
			}

			if (housePriceTemp != null) {

				housePrice += quantity * housePriceTemp;
			}

		}

		ContractCellRenderer.setLstStatusSellposition(lstStatusSellposition);
		ContractOverviewStringCellRenderer.setLstStatusSellposition(lstStatusSellposition);
		IntegerCellRender.setLstStatusSellposition(lstStatusSellposition);
		/** setting subtotal */

		uvp = format(uvp);
		housePrice = format(housePrice);

		model.setValueAt(uvp, model.getCountOfSellpositions(), 5);
		model.setValueAt(housePrice, model.getCountOfSellpositions(), 6);

		uvp = .0;
		housePrice = .0;
		uVPColumnAsList = model.getColumnDataAsList(5);
		housePriceColumnAsList = model.getColumnDataAsList(6);

		/** processing contract parts */
		for (int row = model.getCountOfSellpositions(); row < model.getRowCount() - 1; row++) {

			Double uvpTemp = (Double) uVPColumnAsList.get(row);
			Double housePriceTemp = (Double) housePriceColumnAsList.get(row);

			/** not beeing in line of subtotal */
			if (uvpTemp != null) {

				if (row < model.getRowCount() - 3) {
					uvp += uvpTemp;
				}

				else if (Integer.compare(row, model.getRowCount() - 3) == 0) {

					uvp -= uvpTemp;
				}

				else if (Integer.compare(row, model.getRowCount() - 2) == 0) {
					/* percent or real Number? **/
					estateUVP = uvpTemp;
				}

			}

			if (housePriceTemp != null) {

				if (row < model.getRowCount() - 3) {
					housePrice += housePriceTemp;
				}

				else if (Integer.compare(row, model.getRowCount() - 3) == 0) {
					housePrice -= housePriceTemp;
				}

				else if (Integer.compare(row, model.getRowCount() - 2) == 0) {
					/* percent or real Number? **/
					estateHousePrice = housePriceTemp;
				}
			}

		}

		/** setting total */
		if ((estateUVP > 0.0) && (estateUVP < 1.0)) {
			uvp *= (1 - estateUVP);
		} else {
			uvp -= estateUVP;
		}
		uvp = format(uvp);

		if ((estateHousePrice > 0.0) && (estateHousePrice < 1.0)) {
			housePrice *= (1 - estateHousePrice);
		} else {
			housePrice -= estateHousePrice;
		}

		housePrice = format(housePrice);

		model.setValueAt(uvp, model.getRowCount() - 1, 5);
		model.setValueAt(housePrice, model.getRowCount() - 1, 6);
		model.setAllowFireTableDataChanged(Boolean.TRUE);
	}

	private double format(double receiving) {

		String receivingAsString = DECIMAL_FORMAT.format(receiving);
		receivingAsString = receivingAsString.replaceAll(",", ".");
		return Double.valueOf(receivingAsString);
	}

}
