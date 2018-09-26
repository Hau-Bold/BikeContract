package login;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import constants.Constants;
import constants.IntersportColors;
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import utils.IconButton;
import utils.InformationProvider;
import utils.TextFieldListener;
import utils.Utils;

/** the class LoginForm */
public class LoginForm extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static LoginForm instance = null;
	private JPanel panelMain;

	private static JPasswordField txtEnterPassword;
	public static JButton buttonSubmitPassword;

	private static List<String> lstPassword;

	private static Boolean confirmPassword, doOnlyConfirm;

	private String password;
	private Boolean isBeforeFirstUse, repeatPassword = Boolean.FALSE, clientComesFromAdministrationForm = Boolean.FALSE;

	private TextFieldListener passwordTxtListener;

	/**
	 * yields an instance of LoginForm
	 * 
	 * @param confirmPassword
	 *            - defines the mode
	 * 
	 * @param isBeforeFirstUse
	 *            - is Application starting up?
	 * @param doOnlyConfirm
	 *            - the mode: there are severalk use cases: change Password, confirm
	 *            password, set password.
	 */
	public static LoginForm getInstance(double d, double e, Boolean confirmPassword, Boolean doOnlyConfirm) {

		LoginForm.confirmPassword = confirmPassword;
		LoginForm.doOnlyConfirm = doOnlyConfirm;

		try {
			lstPassword = DatabaseLogic.getLogins();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (!confirmPassword) {
			Boolean isBeforeFirstUse = Integer.compare(lstPassword.size(), 0) == 0;

			if (instance == null) {
				instance = new LoginForm(isBeforeFirstUse);
			}

		} else {
			/** change password */
			if (instance == null) {
				instance = new LoginForm(lstPassword.size() > 0);
			}
			instance.setTitle(Constants.ENTER_PASSWORT);

		}

		instance.setLocation((int) d, (int) e);

		if (txtEnterPassword != null) {
			txtEnterPassword.setText(Constants.EMPTY_STRING);
		}

		return instance;
	}

	/**
	 * Constructor.
	 * 
	 * @param isBeforeFirstUse
	 */
	private LoginForm(Boolean isBeforeFirstUse) {

		this.isBeforeFirstUse = isBeforeFirstUse;
		initComponent();
	}

	private void initComponent() {

		this.setTitle(Constants.LOGIN);
		this.setSize(Constants.WIDTH_OF_SEARCH_CUSTOMER_FRAME, (Constants.HEIGHT_OF_SEARCH_CUSTOMER_FRAME));
		this.setResizable(Boolean.FALSE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_CUSTOMERS_FRAME, Constants.MINIMAL_HEIGHT_OF_CUSTOMERS_FRAME));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setModal(true);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				super.windowClosing(e);
				instance.setVisible(Boolean.FALSE);
				BikeContract.isPasswordCorrect = Boolean.FALSE;
			}
		});

		panelMain = new JPanel();
		panelMain.setLayout(null);
		panelMain.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		panelMain.setBackground(IntersportColors.INTERSPORT_GRAY);
		panelMain.setOpaque(Boolean.TRUE);

		txtEnterPassword = new JPasswordField();
		txtEnterPassword.setBounds(this.getWidth() / 2 - 120, this.getHeight() / 2 - 25, 120, 25);
		txtEnterPassword.setBackground(IntersportColors.INTERSPORT_CORAL);
		txtEnterPassword.getDocument().putProperty(Constants.LOGIN, txtEnterPassword);
		passwordTxtListener = new TextFieldListener(txtEnterPassword);
		txtEnterPassword.getDocument().addDocumentListener(passwordTxtListener);

		panelMain.add(txtEnterPassword);

		buttonSubmitPassword = new IconButton(Constants.ICON_CONFIRM, this.getWidth() / 2 + 5,
				this.getHeight() / 2 - 25);
		buttonSubmitPassword.addActionListener(this);
		buttonSubmitPassword.setVisible(Boolean.FALSE);
		panelMain.add(buttonSubmitPassword);

		this.getContentPane().add(panelMain);

	}

	public void showFrame() {

		this.setVisible(Boolean.TRUE);

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonSubmitPassword)) {
			if (!doOnlyConfirm) {

				if (!confirmPassword) {

					if (isBeforeFirstUse) {
						password = new String(txtEnterPassword.getPassword());
						repeatPassword = Boolean.TRUE;

						this.setTitle(Constants.REPEAT_PASSWORD);
						resetPasswordField();
						isBeforeFirstUse = Boolean.FALSE;
					} else {

						if (repeatPassword) {

							String passwordRepetation = new String(txtEnterPassword.getPassword());
							if (passwordRepetation.equals(password)) {

								/** Login correct */

								if (!clientComesFromAdministrationForm) {
									try {
										DatabaseLogic.writeLogin(password);
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else {
									try {
										DatabaseLogic.updateLogin(password);
									} catch (Exception e) {
										e.printStackTrace();
									}
									clientComesFromAdministrationForm = Boolean.FALSE;
								}

								BikeContract.isPasswordCorrect = Boolean.TRUE;

								this.dispose();

							} else {

								/** Login failed */
								new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
										Constants.LOGIN_FAILED).run();

								this.setTitle(Constants.LOGIN);
								password = null;
								repeatPassword = Boolean.FALSE;
								isBeforeFirstUse = Boolean.TRUE;

								resetPasswordField();
							}

						} else {

							String password = lstPassword.get(0);

							String requested = new String(txtEnterPassword.getPassword());

							if (requested.equals(password)) {

								BikeContract.isPasswordCorrect = Boolean.TRUE;

								this.dispose();

							} else {
								/** Login failed */
								new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
										Constants.LOGIN_FAILED).run();

								int choice = JOptionPane.showConfirmDialog(null, Constants.PASSWORD_FORGOTTEN_QUESTION,
										Constants.INFORMATION, JOptionPane.YES_NO_OPTION,
										JOptionPane.INFORMATION_MESSAGE);

								if (Integer.compare(choice, 0) == 0) {
									List<String> lstpassword = null;

									try {
										lstpassword = DatabaseLogic.getEncryptedLogins();
									} catch (Exception e) {
										e.printStackTrace();
									}

									new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
											Constants.ENCRYPTED_PASSWORD_FOR_CLIENT, lstpassword.get(0)).run();

									Utils.moveToClipboard(lstpassword.get(0));

								} else {

									resetPasswordField();

								}
							}
						}

					}
				} else {
					/** confirm password */
					String password = lstPassword.get(0);

					String requested = new String(txtEnterPassword.getPassword());

					if (requested.equals(password)) {

						confirmPassword = Boolean.FALSE;
						isBeforeFirstUse = Boolean.TRUE;
						this.password = null;
						this.setTitle(Constants.ENTER_NEW_PASSWORT);
						clientComesFromAdministrationForm = Boolean.TRUE;

					} else {
						/** password not correct */
						new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
								Constants.PASSWORD_NOT_CORRECT).run();

					}
					resetPasswordField();

				}
			}

			else {

				/** confirm password */
				String password = lstPassword.get(0);

				String requested = new String(txtEnterPassword.getPassword());

				if (requested.equals(password)) {
					instance.dispose();

					BikeContract.isPasswordCorrect = Boolean.TRUE;
				} else {

					/** Login failed */
					new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
							Constants.CONFIRM_PASSWORD_FAILED).run();

					int choice = JOptionPane.showConfirmDialog(null, Constants.PASSWORD_FORGOTTEN_QUESTION,
							Constants.INFORMATION, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {
						List<String> lstpassword = null;

						try {
							lstpassword = DatabaseLogic.getEncryptedLogins();
						} catch (Exception e) {
							e.printStackTrace();
						}

						new InformationProvider(Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE,
								Constants.ENCRYPTED_PASSWORD_FOR_CLIENT, lstpassword.get(0)).run();

						Utils.moveToClipboard(lstpassword.get(0));

					} else {
						resetPasswordField();
					}

				}

			}
		}
	}

	private void resetPasswordField() {
		txtEnterPassword.getDocument().removeDocumentListener(passwordTxtListener);
		txtEnterPassword.setText(Constants.EMPTY_STRING);
		txtEnterPassword.setBackground(IntersportColors.INTERSPORT_CORAL);
		buttonSubmitPassword.setVisible(Boolean.FALSE);
		txtEnterPassword.getDocument().addDocumentListener(passwordTxtListener);

	}

}
