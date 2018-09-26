package erp;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.simple.parser.ParseException;

import advarics.RequestAdvaricsData;
import constants.Constants;
import contract.ContractForm;
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import utils.TableModel;

/** the class BarCodeScannerDocumentListener */
public class BarCodeScannerDocumentListener implements DocumentListener {

	private BarCodeScanner barCodeScanner;
	private utils.TableModel model;

	/**
	 * Constructor.
	 * 
	 * @param barCodeScanner
	 *            - the barcodescanner
	 * @param model
	 *            - the model of the contract
	 */
	public BarCodeScannerDocumentListener(BarCodeScanner barCodeScanner, TableModel model) {

		this.barCodeScanner = barCodeScanner;
		this.model = model;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {

		String barCode = barCodeScanner.getText();

		if (BikeContract.isOnlineShop) {
			if (Integer.compare(barCode.length(), Constants.LENGTH_OF_BARCODE) == 0) {
				requestErpDataSetViaEAN(barCode);
			}
		} else {

			/** using advarics */
			if (Integer.compare(barCode.length(), Constants.LENGTH_OF_BARCODE) == 0) {
				requestErpDataSetFromAdvarics(barCode);
			}
		}

	}

	@Override
	public void removeUpdate(DocumentEvent e) {
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	/**
	 * requests the erpdata from table
	 * 
	 * @param barCode
	 *            - the identifier
	 */
	private void requestErpDataSetViaEAN(String barCode) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				barCodeScanner.setText(Constants.EMPTY_STRING);

				try {
					ErpDataSet erpDataSet = DatabaseLogic.getErpData(barCode);

					if (erpDataSet != null) {

						model.setAllowFireTableDataChanged(Boolean.FALSE);
						model.appendErpDataToErpData(erpDataSet, Boolean.TRUE);
						model.setAllowFireTableDataChanged(Boolean.TRUE);
					}

					else {

						/** barCode is not to find in WaWi */

						List<String> lstEan = model.getLstIdentifier();

						ErpDataSet additionalRow = new ErpDataSet(barCode, "", "0.0", "0.0", "", "", "", "", "");

						if (!lstEan.contains(barCode)) {
							int choice = JOptionPane.showConfirmDialog(null,
									String.format(Constants.BARCODE_MISSING, barCode), Constants.INFORMATION,
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

							if (Integer.compare(choice, 0) == 0) {

								ContractForm.removeContractValuesListener();
								ContractForm.getModel().appendErpDataToErpData(additionalRow, Boolean.FALSE);
								ContractForm.addContractValuesListener();
							}
						} else {

							model.setAllowFireTableDataChanged(Boolean.FALSE);
							model.appendErpDataToErpData(additionalRow, Boolean.TRUE);
							model.setAllowFireTableDataChanged(Boolean.TRUE);

						}

					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * requests the ErpDataSet from advarics api
	 * 
	 * @param barCode
	 *            - the identifier
	 */
	private void requestErpDataSetFromAdvarics(String barCode) {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				try {

					ErpDataSet erpDataSet = RequestAdvaricsData.getErpData(barCode);

					if (erpDataSet != null) {

						model.setAllowFireTableDataChanged(Boolean.FALSE);
						model.appendErpDataToErpData(erpDataSet, Boolean.TRUE);
						model.setAllowFireTableDataChanged(Boolean.TRUE);

					}

					else {

						/** barCode is not to find in WaWi */

						List<String> lstIdentifier = model.getLstIdentifier();

						ErpDataSet additionalRow = new ErpDataSet(barCode, "", "0.0", "0.0", "", "");

						if (!lstIdentifier.contains(barCode)) {
							int choice = JOptionPane.showConfirmDialog(null,
									String.format(Constants.BARCODE_MISSING, barCode), Constants.INFORMATION,
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

							if (Integer.compare(choice, 0) == 0) {

								ContractForm.removeContractValuesListener();
								ContractForm.getModel().appendErpDataToErpData(additionalRow, Boolean.FALSE);
								ContractForm.addContractValuesListener();
							}
						} else {

							model.setAllowFireTableDataChanged(Boolean.FALSE);
							model.appendErpDataToErpData(additionalRow, Boolean.TRUE);
							model.setAllowFireTableDataChanged(Boolean.TRUE);

						}

					}

					barCodeScanner.setText(Constants.EMPTY_STRING);

				} catch (ParseException e) {
					e.printStackTrace();
				}

			}
		});

	}

}
