package erp;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

import constants.IntersportColors;
import utils.TableModel;

/** the class BarCodeScanner */
public class BarCodeScanner extends JTextField {

	private static final long serialVersionUID = 1L;
	private utils.TableModel model;

	/**
	 * Constructor.
	 * 
	 * @param model
	 *            - the model of the contract
	 */
	public BarCodeScanner(TableModel model) {

		this.model = model;

		initComponent();
	}

	/** initializes this component */
	private void initComponent() {

		this.setBackground(IntersportColors.INTERSPORT_WHITE);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setEditable(Boolean.FALSE);
		this.getDocument().addDocumentListener(new BarCodeScannerDocumentListener(this, model));

	}

}
