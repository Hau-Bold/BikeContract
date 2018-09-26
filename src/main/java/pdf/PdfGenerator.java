package pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

import constants.Constants;
import contract.Contract;
import contractDigitalizer.BikeContract;
import customer.Customer;
import utils.InformationProvider;

/** the class PdfGenerator */
public class PdfGenerator {

	private String path;
	private static String pathToImage;
	private static Contract contract;
	private static Customer customer;
	private static utils.TableModel model;

	/**
	 * 
	 * the inner class PdfViewer
	 */
	private class PdfViewer {

		/**
		 * Constructor
		 * 
		 * @param path
		 *            - the path
		 * @throws IOException
		 *             - in case of technical error
		 */
		public PdfViewer(String path) throws IOException {

			File pdfFile = new File(path);
			if (pdfFile.exists()) {

				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(pdfFile);
				} else {

					new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
							Constants.AWT_DESKTOP_NOT_SUPPORTED).run();

					System.err.println("Awt Desktop is not supported!");
				}
			}
		}

	}

	/**
	 * Constructor.
	 * 
	 * @param path
	 *            - the destination of the pdf
	 * @param contract
	 *            - the contract
	 * @param customer
	 *            - the customer
	 * @param model
	 *            - the ContractModelOnlineShop
	 */
	public PdfGenerator(String path, Contract contract, utils.TableModel model) {
		this.path = path;
		PdfGenerator.contract = contract;
		PdfGenerator.customer = BikeContract.getCustomer();
		PdfGenerator.model = model;

	}

	/**
	 * loads the pathes to image and font
	 */
	public void loadSettings() {

		pathToImage = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.ASSETS
				+ File.separator + Constants.IMAGE;

		loadFonts();

	}

	private void loadFonts() {

		String directory = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.ASSETS
				+ File.separator + Constants.FONT + File.separator;

		FontFactory.register(directory + "Roboto-BlackItalic.ttf", PdfConstants.ROBOTO_BLACK_ITALIC);
		FontFactory.register(directory + "Roboto-Bold.ttf", PdfConstants.ROBOTO_BOLD);
		FontFactory.register(directory + "Roboto-BoldCondensed.ttf", PdfConstants.ROBOTO_BOLD_CONDESED);
		FontFactory.register(directory + "Roboto-BoldItalic.ttf", PdfConstants.ROBOTO_BOLD_ITALIC);
		FontFactory.register(directory + "Roboto-Condensed.ttf", PdfConstants.ROBOTO_CONDENSED);
		FontFactory.register(directory + "Roboto-CondensedItalic.ttf", PdfConstants.ROBOTO_CONDENSED_ITALIC);
		FontFactory.register(directory + "Roboto-Italic.ttf", PdfConstants.ROBOTO_ITALIC);
		FontFactory.register(directory + "Roboto-Light.ttf", PdfConstants.ROBOTO_LIGHT);
		FontFactory.register(directory + "Roboto-LightItalic.ttf", PdfConstants.ROBOTO_LIGHT_ITALIC);
		FontFactory.register(directory + "Roboto-Medium.ttf", PdfConstants.ROBOTO_MEDIUM);
		FontFactory.register(directory + "Roboto-MediumItalic.ttf", PdfConstants.ROBOTO_MEDIUM_ITALIC);
		FontFactory.register(directory + "Roboto-Regular.ttf", PdfConstants.ROBOTO_REGULAR);
		FontFactory.register(directory + "Roboto-Thin.ttf", PdfConstants.ROBOTO_THIN);
		FontFactory.register(directory + "Roboto-ThinItalic.ttf", PdfConstants.ROBOTO_THIN_ITALIC);

	}

	/**
	 * generates the pdf
	 * 
	 * @throws FileNotFoundException
	 *             - in case of technical error
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	public void generate() throws FileNotFoundException, DocumentException {

		File file = new File(path);

		Document document = new Document(PageSize.A4, 25, 20, 100, 100);

		PdfWriter pdfWriter = null;

		try {
			pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));

		} catch (FileNotFoundException ex) {

			new InformationProvider(Constants.WARNING, JOptionPane.ERROR_MESSAGE, Constants.DOCUMENT_IS_OPEN).run();
			return;
		}
		try {
			handleDocument(document, pdfWriter);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void handleDocument(Document document, PdfWriter pdfWriter)
			throws DocumentException, MalformedURLException, IOException {
		document.open();

		PdfUtils.getHeaderAndFooter(document, pdfWriter);

		document.add(new Phrase("\n"));

		/** adding customer (left) and bikedata (right) */
		document.add(PdfUtils.getCustomerAndBikeDataTable(contract, customer, pdfWriter));

		/** adding the table with the sellpositions */
		document.add(PdfUtils.getContractTable(contract, model));
		/** informations about seller and the sums of the contract */
		document.add(PdfUtils.getSellerAndContractFooterTable(contract, model, pdfWriter));

		document.close();

		/** displays the document */
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					new PdfViewer(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	static Image loadImage(String localPath, float percentage) throws IOException, BadElementException {
		String path = pathToImage + File.separator + localPath;

		// BufferedImage bufferedImage = ImageIO.read(getClass().getResource(path));
		// Image image = Image.getInstance(bufferedImage, null);
		Image image = Image.getInstance(path);
		image.scalePercent(percentage);
		return image;
	}

}
