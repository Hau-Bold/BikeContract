package seller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import constants.Constants;
import constants.IntersportColors;
import contractDigitalizer.BikeContract;
import database.DatabaseLogic;
import utils.TextFieldListener;
import utils.Utils;

/**
 * the class SellerForm
 *
 */
public class SellerForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static SellerForm instance = null;
	private JPanel panelMain, panelSouth, panelNorth;
	public static JButton buttonSaveSeller, buttonDisplaySellers, buttonLoadImage, buttonBackToMainMenu;
	private JLabel labelPrename, labelName, labelSellerNumber, labelMobile;
	public static JTextField txtPrename, txtName, txtPathToPhoto, txtNumber, txtMobile;
	public static Boolean isPathSet = Boolean.FALSE, isSurnameOfSellerSet = Boolean.FALSE,
			isPrenameOfSellerSet = Boolean.FALSE, isSellerNumberSet = Boolean.FALSE, isMobileSet = Boolean.FALSE;
	private TextFieldListener prenameTxtListener, nameTxtListener, nameOfImageTxtListener, numberTxtListener,
			mobileTxtListener;

	/**
	 * yields an instance of SellerForm
	 * 
	 * @return
	 */
	public static SellerForm getInstance() {

		if (instance == null) {
			instance = new SellerForm();
		}
		SellerForm.buttonSaveSeller.setText(Constants.SAVE_SELLER);
		return instance;
	}

	private SellerForm() {
		initComponent();
	}

	private void initComponent() {
		this.setTitle(Constants.SELLER);
		int[] location = Utils.getLocation(this);
		this.setBounds(location[0], location[1], Constants.WIDTH_OF_SELLER_FRAME, (Constants.HEIGHT_OF_SELLER_FRAME));
		this.setResizable(Boolean.TRUE);
		this.getContentPane().setBackground(Color.WHITE);
		this.setMinimumSize(
				new Dimension(Constants.MINIMAL_WIDTH_OF_SELLER_FRAME, Constants.MINIMAL_HEIGHT_OF_SELLER_FRAME));
		this.setIconImage(BikeContract.getImage());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				if ((buttonSaveSeller.getText().equals(Constants.UPDATE_SELLER)) && (buttonSaveSeller.isVisible())) {
					int choice = JOptionPane.showConfirmDialog(null,
							String.format(Constants.EXISTING_SELLER_WAS_EDITED, txtPrename.getText(),
									txtName.getText()),
							Constants.INFORMATION, JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (Integer.compare(choice, 0) == 0) {
						handleClosingOfInstance();

					} else {
						return;
					}
				} else {

					handleClosingOfInstance();
				}

			}

			/**
			 * closes the instance correctly
			 */
			private void handleClosingOfInstance() {

				instance.setVisible(Boolean.FALSE);
				BikeContract.getInstance().setState(JFrame.NORMAL);
				instance.dispose();

			}
		});

		panelMain = new JPanel(new BorderLayout());
		panelMain.setBackground(IntersportColors.INTERSPORT_WHITE);

		/** Instantiating the panel for the table */
		panelNorth = new JPanel(null);
		panelNorth.setPreferredSize(new Dimension(Constants.WIDTH_OF_SELLER_FRAME, Constants.HEIGHT_OF_TABLE_SELLER));

		labelPrename = new JLabel(Constants.PRENAME + Constants.COLON);
		labelPrename.setBounds(50, 30, 50, 25);
		panelNorth.add(labelPrename);

		txtPrename = new JTextField();
		txtPrename.setBounds(100, 30, 80, 25);
		txtPrename.setBackground(IntersportColors.INTERSPORT_CORAL);
		txtPrename.getDocument().putProperty(Constants.SELLER, txtPrename);
		prenameTxtListener = new TextFieldListener(txtPrename);
		txtPrename.getDocument().addDocumentListener(prenameTxtListener);

		panelNorth.add(txtPrename);

		labelName = new JLabel(Constants.NAME + Constants.COLON);
		labelName.setBounds(195, 30, 35, 25);
		panelNorth.add(labelName);

		txtName = new JTextField();
		txtName.setBounds(230, 30, 80, 25);
		txtName.setBackground(IntersportColors.INTERSPORT_CORAL);
		txtName.getDocument().putProperty(Constants.SELLER, txtName);
		nameTxtListener = new TextFieldListener(txtName);
		txtName.getDocument().addDocumentListener(nameTxtListener);

		panelNorth.add(txtName);

		buttonLoadImage = new JButton(Constants.SELECT_IMAGE);
		buttonLoadImage.setBounds(330, 30, 70, 25);
		buttonLoadImage.addActionListener(this);
		panelNorth.add(buttonLoadImage);

		txtPathToPhoto = new JTextField();
		txtPathToPhoto.setBounds(410, 30, 100, 25);
		txtPathToPhoto.setBackground(IntersportColors.INTERSPORT_WHITE);
		txtPathToPhoto.setVisible(Boolean.FALSE);
		txtPathToPhoto.setEditable(Boolean.FALSE);
		txtPathToPhoto.getDocument().putProperty(Constants.SELLER, txtPathToPhoto);
		nameOfImageTxtListener = new TextFieldListener(txtPathToPhoto);
		txtPathToPhoto.getDocument().addDocumentListener(nameOfImageTxtListener);
		panelNorth.add(txtPathToPhoto);

		labelSellerNumber = new JLabel(Constants.SELLERNUMBER + Constants.COLON);
		labelSellerNumber.setBounds(20, 80, 80, 25);
		panelNorth.add(labelSellerNumber);

		txtNumber = new JTextField();
		txtNumber.setBounds(100, 80, 80, 25);
		txtNumber.setBackground(IntersportColors.INTERSPORT_CORAL);
		txtNumber.getDocument().putProperty(Constants.SELLER, txtNumber);
		numberTxtListener = new TextFieldListener(txtNumber);
		txtNumber.getDocument().addDocumentListener(numberTxtListener);
		panelNorth.add(txtNumber);

		labelMobile = new JLabel(Constants.MOBILE + Constants.COLON);
		labelMobile.setBounds(190, 80, 80, 25);
		panelNorth.add(labelMobile);

		txtMobile = new JTextField();
		txtMobile.setBounds(230, 80, 80, 25);
		txtMobile.setBackground(IntersportColors.INTERSPORT_CORAL);
		txtMobile.getDocument().putProperty(Constants.SELLER, txtMobile);
		mobileTxtListener = new TextFieldListener(txtMobile);
		txtMobile.getDocument().addDocumentListener(mobileTxtListener);
		panelNorth.add(txtMobile);

		panelMain.add(panelNorth, BorderLayout.NORTH);

		/** Instantiating the panel for customer's actions */
		panelSouth = new JPanel(new GridLayout(1, 4));
		panelSouth.setBackground(IntersportColors.INTERSPORT_TURQUOISE);
		panelSouth.setOpaque(Boolean.TRUE);
		panelSouth.setBorder(BorderFactory.createRaisedBevelBorder());

		buttonBackToMainMenu = new JButton(Constants.MAIN_MENU);
		buttonBackToMainMenu.addActionListener(this);

		buttonSaveSeller = new JButton(Constants.SAVE_SELLER);
		buttonSaveSeller.setVisible(Boolean.FALSE);
		buttonSaveSeller.addActionListener(this);

		buttonDisplaySellers = new JButton();
		buttonDisplaySellers.setText(Constants.DISPLAY_SELLERS);
		buttonDisplaySellers.setVisible(Boolean.TRUE);
		buttonDisplaySellers.addActionListener(this);

		panelSouth.add(buttonDisplaySellers);
		panelSouth.add(buttonBackToMainMenu);
		panelSouth.add(buttonSaveSeller);
		panelSouth.add(Utils.getEmptyPanel());

		panelMain.add(panelSouth, BorderLayout.SOUTH);
		this.getContentPane().add(panelMain);
	}

	public void showFrame() {
		this.setVisible(Boolean.TRUE);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(buttonLoadImage)) {
			JFileChooser fileChooser = new JFileChooser(
					BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.SELLER);

			fileChooser.setDialogTitle(Constants.SELECT_PHOTO_TO_LOAD);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");
			fileChooser.setFileFilter(filter);

			fileChooser.setBackground(Color.WHITE);

			int response = fileChooser.showOpenDialog(this);

			if (response == JFileChooser.APPROVE_OPTION) {

				String pathToPhoto = fileChooser.getSelectedFile().getPath();

				txtPathToPhoto.setText(pathToPhoto);
				txtPathToPhoto.setVisible(Boolean.TRUE);
			}

		}

		else if (o.equals(buttonBackToMainMenu)) {
			BikeContract instanceOfContractDigitalizer = null;
			instanceOfContractDigitalizer = BikeContract.getInstance();
			this.setVisible(Boolean.FALSE);
			instanceOfContractDigitalizer.setState(JFrame.NORMAL);
			instance = null;
			dispose();
		}

		else if (o.equals(buttonSaveSeller)) {

			String prename = txtPrename.getText();
			String name = txtName.getText();

			String number = txtNumber.getText();
			String mobile = txtMobile.getText();
			String pathToPhoto = txtPathToPhoto.getText();
			byte[] byteArray = Utils.convertToByteArray(pathToPhoto);

			String[] pathToPhotoSplitted = pathToPhoto.split(Pattern.quote(File.separator));
			String nameOfPhoto = pathToPhotoSplitted[pathToPhotoSplitted.length - 1];

			String textOfButton = buttonSaveSeller.getText();

			if (textOfButton.equals(Constants.SAVE_SELLER)) {
				DatabaseLogic.insertIntoTableSeller(prename, name, number, mobile, byteArray, nameOfPhoto);
			}

			else if (textOfButton.equals(Constants.UPDATE_SELLER)) {

				DatabaseLogic.updateTableSeller(prename, name, number, mobile, byteArray, nameOfPhoto,
						BikeContract.getIdOfSeller());
			}

			this.setVisible(Boolean.FALSE);
			BikeContract.getInstance().setState(JFrame.NORMAL);
			instance = null;
			dispose();

		}

		else if (o.equals(buttonDisplaySellers)) {

			this.setState(JFrame.ICONIFIED);
			SellersForm instance = SellersForm.getInstance();
			instance.setVisible(Boolean.TRUE);
		}

	}

	public void setSeller(Seller seller) {
		txtPrename.setText(seller.getPrename());
		txtPrename.setBackground(IntersportColors.INTERSPORT_WHITE);
		txtName.setText(seller.getName());
		txtName.setBackground(IntersportColors.INTERSPORT_WHITE);
		txtPathToPhoto.setText(BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.SELLER
				+ File.separator + seller.getNameOfImage());
		txtPathToPhoto.setVisible(Boolean.TRUE);
		txtPathToPhoto.setBackground(IntersportColors.INTERSPORT_WHITE);
		txtNumber.setText(seller.getNumber());
		txtNumber.setBackground(IntersportColors.INTERSPORT_WHITE);
		txtMobile.setText(seller.getMobile());
		txtMobile.setBackground(IntersportColors.INTERSPORT_WHITE);

	}

	public void removeDocumentListeners() {

		txtPrename.getDocument().removeDocumentListener(prenameTxtListener);
		txtName.getDocument().removeDocumentListener(nameTxtListener);
		txtPathToPhoto.getDocument().removeDocumentListener(nameOfImageTxtListener);
		txtNumber.getDocument().removeDocumentListener(numberTxtListener);
		txtMobile.getDocument().removeDocumentListener(mobileTxtListener);
	}

	public void addDocumentListeners() {

		txtPrename.getDocument().addDocumentListener(prenameTxtListener);
		txtName.getDocument().addDocumentListener(nameTxtListener);
		txtPathToPhoto.getDocument().addDocumentListener(nameOfImageTxtListener);
		txtNumber.getDocument().addDocumentListener(numberTxtListener);
		txtMobile.getDocument().addDocumentListener(mobileTxtListener);
	}

	public void setSentinelBooleans(Boolean value) {

		isPathSet = value;
		isSurnameOfSellerSet = value;
		isPrenameOfSellerSet = value;
		isSellerNumberSet = value;
		isMobileSet = value;
	}

	public static JButton getButtonSaveSeller() {
		return buttonSaveSeller;
	}

}
