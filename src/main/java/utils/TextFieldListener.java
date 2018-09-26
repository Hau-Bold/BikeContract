package utils;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import constants.Constants;
import constants.IntersportColors;
import login.LoginForm;
import seller.SellerForm;

/** the class TextFieldListener */
public class TextFieldListener implements DocumentListener {

	private JTextField textField;

	public TextFieldListener(JTextField textField) {
		this.textField = textField;
	}

	@Override
	public void insertUpdate(DocumentEvent event) {

		Object seller = event.getDocument().getProperty(Constants.SELLER);
		Object login = event.getDocument().getProperty(Constants.LOGIN);

		if (seller != null) {
			textField.setBackground(IntersportColors.INTERSPORT_WHITE);

			if (textField == SellerForm.txtPrename) {
				/** surname of seller is set */
				SellerForm.isSurnameOfSellerSet = Boolean.TRUE;
			} else if (textField == SellerForm.txtName) {
				/** prename seller is set */
				SellerForm.isPrenameOfSellerSet = Boolean.TRUE;
			}

			else if (textField == SellerForm.txtPathToPhoto) {
				/** path to photo is set */
				SellerForm.isPathSet = Boolean.TRUE;
			}

			else if (textField == SellerForm.txtNumber) {
				/** seller number is set */
				SellerForm.isSellerNumberSet = Boolean.TRUE;
			}

			else if (textField == SellerForm.txtMobile) {
				/** seller number is set */
				SellerForm.isMobileSet = Boolean.TRUE;
			}

			if (SellerForm.isPathSet && SellerForm.isSurnameOfSellerSet && SellerForm.isPrenameOfSellerSet
					&& SellerForm.isSellerNumberSet && SellerForm.isMobileSet) {
				/** check if it is possible to save */
				SellerForm.buttonSaveSeller.setVisible(Boolean.TRUE);
			}
		}

		else if (login != null) {
			textField.setBackground(IntersportColors.INTERSPORT_WHITE);
			LoginForm.buttonSubmitPassword.setVisible(Boolean.TRUE);
		}

	}

	@Override
	public void removeUpdate(DocumentEvent event) {

		Object seller = event.getDocument().getProperty(Constants.SELLER);
		Object login = event.getDocument().getProperty(Constants.LOGIN);

		if (seller != null) {

			if (textField.getText().equals(Constants.EMPTY_STRING)) {

				if (textField == SellerForm.txtPrename) {

					SellerForm.isSurnameOfSellerSet = Boolean.FALSE;
					textField.setBackground(IntersportColors.INTERSPORT_CORAL);
				}

				else if (textField == SellerForm.txtName) {
					SellerForm.isPrenameOfSellerSet = Boolean.FALSE;
					textField.setBackground(IntersportColors.INTERSPORT_CORAL);
				}

				else if (textField == SellerForm.txtPathToPhoto) {

					textField.setVisible(Boolean.FALSE);
					SellerForm.isPathSet = Boolean.FALSE;
				}

				else if (textField == SellerForm.txtNumber) {

					SellerForm.isSellerNumberSet = Boolean.FALSE;
					textField.setBackground(IntersportColors.INTERSPORT_CORAL);
				}

				else if (textField == SellerForm.txtMobile) {

					SellerForm.isMobileSet = Boolean.FALSE;
					textField.setBackground(IntersportColors.INTERSPORT_CORAL);
				}

				if (SellerForm.buttonSaveSeller.isVisible()) {

					SellerForm.buttonSaveSeller.setVisible(Boolean.FALSE);

				}

			} else {

				if (textField == SellerForm.txtPrename) {
					/** surname of seller is set */
					SellerForm.isSurnameOfSellerSet = Boolean.TRUE;
				} else if (textField == SellerForm.txtName) {
					/** prename seller is set */
					SellerForm.isPrenameOfSellerSet = Boolean.TRUE;
				}

				else if (textField == SellerForm.txtPathToPhoto) {
					/** path to photo is set */
					SellerForm.isPathSet = Boolean.TRUE;
				}

				else if (textField == SellerForm.txtNumber) {
					/** seller number is set */
					SellerForm.isSellerNumberSet = Boolean.TRUE;
				}

				else if (textField == SellerForm.txtMobile) {
					/** seller number is set */
					SellerForm.isMobileSet = Boolean.TRUE;
				}

				if (SellerForm.isPathSet && SellerForm.isSurnameOfSellerSet && SellerForm.isPrenameOfSellerSet
						&& SellerForm.isSellerNumberSet && SellerForm.isMobileSet) {
					/** check if it is possible to save */
					SellerForm.buttonSaveSeller.setVisible(Boolean.TRUE);
				}

			}

		} else if (login != null) {

			if (textField.getText().equals(Constants.EMPTY_STRING)) {
				textField.setBackground(IntersportColors.INTERSPORT_CORAL);
				LoginForm.buttonSubmitPassword.setVisible(Boolean.FALSE);
			}
		}

	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

}
