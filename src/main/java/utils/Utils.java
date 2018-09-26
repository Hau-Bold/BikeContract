package utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.IOUtils;

import bikeData.BikeData;
import bikeData.BikeDataDownModel;
import constants.Constants;
import constants.ShoreConstants;
import contract.Contract;
import contract.SellPosition;
import contractDigitalizer.BikeContract;
import customer.Customer;
import customer.CustomerModel;
import database.DatabaseLogic;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/** the class Utils */
public class Utils {

	public static Double formatDouble(String receiving) {

		receiving = receiving.replace(",", ".");

		return Double.valueOf(receiving);
	}

	/***
	 * 
	 * @return - empty non opaque panel
	 */
	public static JPanel getEmptyPanel() {

		JPanel emptyPanel = new JPanel();
		emptyPanel.setOpaque(Boolean.FALSE);

		return emptyPanel;
	}

	/**
	 * to emphasize a string
	 * 
	 * @param receiving
	 *            - a string
	 * @return emphasized receiving
	 */
	public static String handleText(String string) {

		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<b>");
		builder.append(string);
		builder.append("</b>");
		builder.append("</html>");

		return builder.toString();
	}

	public static String handleTextWithSpace(String string) {
		StringBuilder builder = new StringBuilder();

		builder.append("<html>");
		builder.append("<b>");
		builder.append("&nbsp;");
		builder.append(string);
		builder.append("</b>");
		builder.append("</html>");

		return builder.toString();
	}

	/**
	 * yields the customer's data from the model
	 * 
	 * @param model
	 *            - the model
	 * @return - the customer
	 */
	public static Customer getCustomer(CustomerModel model) {

		Customer customer = new Customer();
		customer.setCustomerName(String.valueOf(model.getValueAt(0, 1)).trim());
		customer.setPrename(String.valueOf(model.getValueAt(1, 1)).trim());
		customer.setStreet(String.valueOf(model.getValueAt(2, 1)).trim());
		customer.setNumber(String.valueOf(model.getValueAt(3, 1)).trim());
		customer.setPostalCode(String.valueOf(model.getValueAt(4, 1)).trim());
		customer.setPlace(String.valueOf(model.getValueAt(5, 1)).trim());

		// the optional columns
		String mobile = String.valueOf(model.getValueAt(6, 1)).trim();
		String email = String.valueOf(model.getValueAt(7, 1)).trim();
		customer.setMobile((mobile.equals("null") || mobile.equals(Constants.EMPTY_STRING)) ? Constants.NA : mobile);
		customer.setEmail((email.equals("null") || email.equals(Constants.EMPTY_STRING)) ? Constants.NA : email);

		return customer;
	}

	/**
	 * checks whether the receiving is valid or not
	 * 
	 * @param customer
	 *            - incoming customer
	 * @return true if customer is valid, ortherwise false
	 */
	public static boolean isCustomerValid(Customer customer) {

		Boolean response = isStringValid(customer.getCustomerName());
		response = response && isStringValid(customer.getPrename());
		response = response && isStringValid(customer.getNumber());
		response = response && isStringValid(customer.getStreet());
		response = response && isStringValid(customer.getPlace());
		response = response && isStringValid(customer.getPostalCode());

		return response;
	}

	/**
	 * checks whether the receiving is valid or not
	 * 
	 * @param receiving
	 *            - incoming string
	 * @return true if receiving is valid, ortherwise false
	 */
	public static boolean isStringValid(String receiving) {
		return (receiving != null) && (receiving != "") && (receiving != "null");
	}

	/**
	 * yields the contract from the model
	 * 
	 * @param model
	 *            - the contract`s model
	 * @return the contract
	 */
	public static Contract getContract(utils.TableModel model) {

		Calendar calendar = Calendar.getInstance(Locale.GERMAN);
		Date date = calendar.getTime();

		Contract contract = new Contract();

		contract.setCreatedAt(BikeContract.SIMPLE_DATE_FORMAT_TIME.format(date));

		int countOfSellpositions = model.getCountOfSellpositions();

		/** uvp parts of contract */
		Object deliveryAssemblyFeeUVP = model.getValueAt(countOfSellpositions + 1, 5);
		Object serviceCostGarageUVP = model.getValueAt(countOfSellpositions + 2, 5);
		Object redemptionDismantledPartsWheelUVP = model.getValueAt(countOfSellpositions + 3, 5);
		Object downPaymentUVP = model.getValueAt(countOfSellpositions + 4, 5);
		Object estateUVP = model.getValueAt(countOfSellpositions + 5, 5);

		/** HousePrice parts of contract */
		Object deliveryAssemblyFeeHousePrice = model.getValueAt(countOfSellpositions + 1, 6);
		Object serviceCostGarageHousePrice = model.getValueAt(countOfSellpositions + 2, 6);
		Object redemptionDismantledPartsWheelHousePrice = model.getValueAt(countOfSellpositions + 3, 6);
		Object downPaymentHousePrice = model.getValueAt(countOfSellpositions + 4, 6);
		Object estateHousePrice = model.getValueAt(countOfSellpositions + 5, 6);

		/** setting uvp parts */
		if (deliveryAssemblyFeeUVP != null) {
			contract.setDeliveryAssemblyFeeUVP((Double) deliveryAssemblyFeeUVP);
		}

		if (serviceCostGarageUVP != null) {
			contract.setServiceCostGarageUVP((Double) serviceCostGarageUVP);
		}

		if (redemptionDismantledPartsWheelUVP != null) {
			contract.setRedemptionDismantledPartsWheelUVP((Double) redemptionDismantledPartsWheelUVP);
		}

		if (downPaymentUVP != null) {
			contract.setDownPaymentUVP((Double) downPaymentUVP);
		}

		if (estateUVP != null) {
			contract.setEstateUVP((Double) estateUVP);
		}

		/** setting HousePrice parts */
		if (deliveryAssemblyFeeHousePrice != null) {
			contract.setDeliveryAssemblyFeeHousePrice((Double) deliveryAssemblyFeeHousePrice);
		}
		if (serviceCostGarageHousePrice != null) {
			contract.setServiceCostGarageHousePrice((Double) serviceCostGarageHousePrice);
		}
		if (redemptionDismantledPartsWheelHousePrice != null) {
			contract.setRedemptionDismantledPartsWheelHousePrice((Double) redemptionDismantledPartsWheelHousePrice);
		}

		if (downPaymentHousePrice != null) {
			contract.setDownPaymentHousePrice((Double) downPaymentHousePrice);
		}

		if (estateHousePrice != null) {
			contract.setEstateHousePrice((Double) estateHousePrice);
		}

		return contract;
	}

	/**
	 * yiels the sell positions from the model and assigns the ids of customer and
	 * contract
	 * 
	 * @param idOfCustomer
	 *            - the customer's id
	 * @param idOfContract
	 *            -the contract`s id
	 * @param model
	 *            - the model
	 * @return - the sell positions from the model
	 */
	public static List<SellPosition> getSellPositionFromModel(int idOfContract, utils.TableModel model) {

		Boolean isOnlineShop = BikeContract.isOnlineShop;

		List<SellPosition> response = new ArrayList<SellPosition>();

		for (int rowCounter = 0; rowCounter < model.getCountOfSellpositions(); rowCounter++) {

			SellPosition sellPosition = new SellPosition();

			sellPosition.setIdOfContract(idOfContract);

			Object quantity = model.getValueAt(rowCounter, 0);

			Object ean = model.getValueAt(rowCounter, 1);
			Object articleName = model.getValueAt(rowCounter, 2);
			Object producer = model.getValueAt(rowCounter, 3);
			Object uvp = model.getValueAt(rowCounter, 5);
			Object housePrice = model.getValueAt(rowCounter, 6);

			if (quantity != null) {
				sellPosition.setQuantity((int) quantity);
			}

			if (ean != null) {
				sellPosition.setEan((String) ean);
			}

			if (articleName != null) {
				sellPosition.setArticleName((String) articleName);
			}

			if (producer != null) {
				sellPosition.setProducer((String) producer);
			}

			if (uvp != null) {
				sellPosition.setUvp((Double) uvp);
			}

			if (housePrice != null) {
				sellPosition.setHousePrice((Double) housePrice);
			}

			if (!isOnlineShop) {

				Object wwsArticleNumber = model.getValueAt(rowCounter, 7);
				sellPosition.setArticleNumber((String) wwsArticleNumber);
			}

			response.add(sellPosition);
		}

		return response;
	}

	public static Double getSubtotalUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions(), 5);

	}

	public static Double getSubtotalHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions(), 6);
	}

	public static Double getDeliveryAssemblyFeeUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 1, 5);

	}

	public static Double getDeliveryAssemblyFeeHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 1, 6);

	}

	public static Double getServiceCostGarageUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 2, 5);

	}

	public static Double getServiceCostGarageHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 2, 6);

	}

	public static Double getRedemptionDismantledPartsWheelUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 3, 6);

	}

	public static Double getRedemptionDismantledPartsWheelHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 3, 6);

	}

	public static Double getDowmPaymentUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 4, 5);

	}

	public static Double getDowmPaymentHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 4, 6);

	}

	public static Double getEstateUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 5, 5);

	}

	public static Double getEstateHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 5, 6);

	}

	public static Double getTotalUVPFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 6, 5);

	}

	public static Double getTotalHousePriceFromModel(utils.TableModel model) {

		return (Double) model.getValueAt(model.getCountOfSellpositions() + 6, 6);

	}

	/**
	 * yields the customer from a table
	 * 
	 * @param row
	 *            - the selected row
	 * @param table
	 *            - the table
	 * @return - the customer
	 */
	public static Customer getCustomerFromTable(int row, JTable table) {

		String customerName = (String) table.getValueAt(row, 0);
		String surname = (String) table.getValueAt(row, 1);
		String street = (String) table.getValueAt(row, 2);
		String number = (String) table.getValueAt(row, 3);
		String postalcode = (String) table.getValueAt(row, 4);
		String place = (String) table.getValueAt(row, 5);
		String mobile = (String) table.getValueAt(row, 6);
		String email = (String) table.getValueAt(row, 7);

		return new Customer(customerName, surname, street, number, postalcode, place, mobile, email);
	}

	/**
	 * writes the list Of Customers
	 * 
	 * @param bufferedWriter
	 *            - the bufferedWriter
	 * @throws SQLException
	 *             - in case of technical error
	 * @throws IOException
	 *             - in case of technical error
	 * @throws BadPaddingException
	 *             - in case of technical error
	 * @throws IllegalBlockSizeException
	 *             - in case of technical error
	 * @throws NoSuchPaddingException
	 *             - in case of technical error
	 * @throws NoSuchAlgorithmException
	 *             - in case of technical error
	 * @throws InvalidKeyException
	 *             - in case of technical error
	 */
	public static void writeCustomers(BufferedWriter bufferedWriter)
			throws SQLException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {

		List<Customer> lstCustomers = DatabaseLogic.getCustomers();
		StringBuilder builder = new StringBuilder();

		for (Customer customer : lstCustomers) {
			builder.append(customer.getIdOfCustomer());
			builder.append(",");
			builder.append(customer.getCustomerName());
			builder.append(",");
			builder.append(customer.getPrename());
			builder.append(",");
			builder.append(customer.getStreet());
			builder.append(",");
			builder.append(customer.getNumber());
			builder.append(",");
			builder.append(customer.getPostalCode());
			builder.append(",");
			builder.append(customer.getPlace());
			builder.append(",");
			builder.append(customer.getMobile());
			builder.append(",");
			builder.append(customer.getEmail());
			builder.append(",");
			builder.append(customer.getCreatedAt());
			builder.append(",");
			builder.append(customer.getLastModified());

			bufferedWriter.write(builder.toString());
			builder.setLength(0);
			bufferedWriter.newLine();
		}

	}

	/**
	 * generates a customer from a line consisting of csv values
	 * 
	 * @param line
	 *            - the csv values
	 * @return the generated customer
	 */
	public static Customer generateCustomerFromLine(String line) {
		String[] customerAsCSV = line.split(",");

		Customer customer = new Customer();

		customer.setIdOfCustomer(Integer.valueOf(customerAsCSV[0]));
		customer.setCustomerName(customerAsCSV[1]);
		customer.setPrename(customerAsCSV[2]);
		customer.setStreet(customerAsCSV[3]);
		customer.setNumber(customerAsCSV[4]);
		customer.setPostalCode(customerAsCSV[5]);
		customer.setPlace(customerAsCSV[6]);

		customer.setMobile(customerAsCSV[7]);
		customer.setEmail(customerAsCSV[8]);
		customer.setCreatedAt(customerAsCSV[9]);
		customer.setLastModified(customerAsCSV[10]);

		return customer;
	}

	/**
	 * generates a contract from a line consisting of csv values
	 * 
	 * @param line
	 *            - the csv values
	 * @return the generated contract
	 */
	public static Contract generateContractFromLine(String line) {
		String[] contractAsCSV = line.split(",");

		Contract contract = new Contract();

		contract.setIdOfContract(Integer.valueOf(contractAsCSV[0]));
		contract.setCreatedAt(contractAsCSV[1]);
		contract.setLastModified(contractAsCSV[2]);
		contract.setIdOfCustomer(Integer.valueOf(contractAsCSV[3]));

		/** the uvp parts */
		contract.setDeliveryAssemblyFeeUVP(Double.valueOf(contractAsCSV[4]));
		contract.setServiceCostGarageUVP(Double.valueOf(contractAsCSV[5]));
		contract.setRedemptionDismantledPartsWheelUVP(Double.valueOf(contractAsCSV[6]));
		contract.setDownPaymentUVP(Double.valueOf(contractAsCSV[7]));
		contract.setEstateUVP(Double.valueOf(contractAsCSV[8]));

		/** the housePricing parts */
		contract.setDeliveryAssemblyFeeHousePrice(Double.valueOf(contractAsCSV[9]));
		contract.setServiceCostGarageHousePrice(Double.valueOf(contractAsCSV[10]));
		contract.setRedemptionDismantledPartsWheelHousePrice(Double.valueOf(contractAsCSV[11]));
		contract.setDownPaymentHousePrice(Double.valueOf(contractAsCSV[12]));
		contract.setEstateHousePrice(Double.valueOf(contractAsCSV[13]));

		return contract;
	}

	/**
	 * computes the year of the inspection
	 * 
	 * @param model
	 *            - the model
	 * @return the year of the inspection
	 */
	public static int getYearOfInspection(BikeDataDownModel model) {

		int offset = 0;
		Boolean anInspectionYearWasSelected = Boolean.TRUE;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);

		for (int i = 1; i < 6; i++) {
			Boolean tmp = (Boolean) model.getValueAt(0, i);
			if (tmp == Boolean.TRUE) {
				break;
			}

			offset++;

			if (Integer.compare(i, 5) == 0) {
				anInspectionYearWasSelected = Boolean.FALSE;
			}

		}

		return anInspectionYearWasSelected ? year + offset : 0;
	}

	/**
	 * computes the inspection years
	 * 
	 * @param bikeData
	 * 
	 * @return - the header
	 */
	public static String[] generateBikeDataDowmColumns(BikeData bikeData) {

		String[] response = new String[6];
		response[0] = Constants.INSPECTION;
		Calendar calendar = Calendar.getInstance();
		int currentYear = calendar.get(Calendar.YEAR);

		if (bikeData != null) {
			int year = bikeData.getYear();

			if (Integer.compare(year, 0) != 0) {

				if (year < currentYear) {
					/** year is in the past */

					for (int i = 0; i < 5; i++) {
						response[i + 1] = String.valueOf(year + i);
					}
				} else {
					/** year is one of the next following five years */
					for (int i = 0; i < 5; i++) {
						response[i + 1] = String.valueOf(currentYear + i);
					}
				}
			} else {
				/** not bike data, i.e. new contract */
				for (int i = 0; i < 5; i++) {
					response[i + 1] = String.valueOf(currentYear + i);
				}
			}
		} else {
			/** not bike data, i.e. new contract */
			for (int i = 0; i < 5; i++) {
				response[i + 1] = String.valueOf(currentYear + i);
			}
		}
		return response;
	}

	/**
	 * convertes the input File to a Byte Array
	 * 
	 * @param path
	 *            -the path
	 * @return the image as Byte Array
	 */
	public static byte[] convertToByteArray(String path) {

		File file = new File(path);

		byte[] byteArray = new byte[(int) file.length()];

		try (FileInputStream fileInputStream = new FileInputStream(file)) {

			fileInputStream.read(byteArray);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		return byteArray;
	}

	/**
	 * converts a byte array to a buffered image
	 * 
	 * @param byteArray
	 *            - the byte array
	 * @return the buffered image
	 */
	public static BufferedImage convertToBufferedImage(byte[] byteArray) {

		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}

	/**
	 * loads the appointments
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String[] loadAppointments() throws IOException {

		return new String[] { Constants.MINUS, "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45",
				"11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30",
				"13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15",
				"16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "18:15", "18:30" };
	}

	/**
	 * yields the location
	 * 
	 * @return the location
	 */
	public static int[] getLocation(JFrame jFrame) {

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		double width = dimension.getWidth() / 2 - jFrame.getSize().width / 2 - 150;
		String widthAsString = String.valueOf(width);
		String widthRemainer = widthAsString.substring(0, widthAsString.indexOf("."));

		double height = dimension.getHeight() / 2 - jFrame.getSize().height / 2 - 150;
		String heightAsString = String.valueOf(height);
		String heightRemainer = heightAsString.substring(0, heightAsString.indexOf("."));

		return new int[] { Integer.valueOf(widthRemainer), Integer.valueOf(heightRemainer) };
	}

	// no comment
	private static String handleLineOfAccess(String line) {
		{
			return line.split("=")[1].trim();
		}
	}

	/**
	 * removes contracts that are older than days from the directory
	 * 
	 * @param days
	 *            - the count of day for housekeeping
	 */
	public static void doHouseKeepingOfPdfContracts(int days) {

		long now = System.currentTimeMillis();

		long daysInMilliseconds = days * 24 * 3600 * 1000;

		String path = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.CONTRACT;

		File directoryOfContracts = new File(path);
		File[] contracts = directoryOfContracts.listFiles();

		if (contracts != null && contracts.length > 0) {

			for (File file : contracts) {

				long age = now - file.lastModified();

				if (age >= daysInMilliseconds) {
					file.delete();
				}

			}

		}

	}

	/***
	 * tests all sellpositions
	 * 
	 * @param lstSellPosition
	 * @return
	 */
	public static boolean checkSellpositions(List<SellPosition> lstSellPosition) {

		Boolean result = Boolean.TRUE;

		for (SellPosition sellPosition : lstSellPosition) {
			result = (result && testSellposition(sellPosition));
		}

		return result;
	}

	/**
	 * tests the validity of a single sellposition
	 * 
	 * @param sellPosition
	 *            - the sellposition
	 * @return true if sellposition is valid, otherwise false
	 */
	private static boolean testSellposition(SellPosition sellPosition) {

		int quantity = sellPosition.getQuantity();
		String ean = sellPosition.getEan();
		String articleName = sellPosition.getArticleName();
		String producer = sellPosition.getProducer();
		double uvp = sellPosition.getUvp();
		double housePrice = sellPosition.getHousePrice();

		if (quantity <= 0) {
			return Boolean.FALSE;
		} else if (ean.trim().equals(Constants.EMPTY_STRING)) {
			return Boolean.FALSE;
		} else if (articleName.trim().equals(Constants.EMPTY_STRING)) {
			return Boolean.FALSE;
		} else if (producer.trim().equals(Constants.EMPTY_STRING)) {
			return Boolean.FALSE;
		}

		else if (Double.compare(uvp, .0) == 0) {
			return Boolean.FALSE;
		} else if (Double.compare(housePrice, .0) == 0) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * checks the validity of a contract
	 * 
	 * @param contract
	 *            - the contract
	 * @return true if valid, otherwise false
	 */
	public static boolean isContractValid(Contract contract) {

		Double deliveryAssemblyFeeUVP = contract.getDeliveryAssemblyFeeUVP();
		Double deliveryAssemblyFeeHousePrice = contract.getDeliveryAssemblyFeeHousePrice();
		Double downPaymentUVP = contract.getDownPaymentUVP();
		Double downPaymentHousePrice = contract.getDownPaymentHousePrice();
		Double estateUVP = contract.getEstateUVP();
		Double estateHousePrice = contract.getEstateHousePrice();
		Double redemptionDismantledPartsWheelUVP = contract.getRedemptionDismantledPartsWheelUVP();
		Double redemptionDismantledPartsWheelHousePrice = contract.getRedemptionDismantledPartsWheelHousePrice();
		Double serviceCostGarageUVP = contract.getServiceCostGarageUVP();
		Double serviceCostGarageHousePrice = contract.getServiceCostGarageHousePrice();

		if (isContractPartValid(deliveryAssemblyFeeUVP, deliveryAssemblyFeeHousePrice)) {
			if (isContractPartValid(downPaymentUVP, downPaymentHousePrice)) {
				if (isContractPartValid(estateUVP, estateHousePrice)) {
					if (isContractPartValid(redemptionDismantledPartsWheelUVP,
							redemptionDismantledPartsWheelHousePrice)) {
						if (isContractPartValid(serviceCostGarageUVP, serviceCostGarageHousePrice)) {
							return Boolean.TRUE;
						}
					} else {
						return Boolean.FALSE;
					}
				} else {
					return Boolean.FALSE;
				}
			} else {
				return Boolean.FALSE;
			}

		} else {
			return Boolean.FALSE;
		}
		return false;
	}

	/**
	 * chechs if uvp and houseprice are valid
	 * 
	 * @param uvp
	 *            - the uvp
	 * @param houseprice
	 *            - the houseprice
	 * @return true if both valid
	 */
	private static boolean isContractPartValid(Double uvp, Double houseprice) {

		Boolean isUvpValid = Double.compare(uvp, 0.0) != 0;
		Boolean isHousepriceValid = Double.compare(houseprice, 0.0) != 0;

		if (isUvpValid && isHousepriceValid) {

			return Boolean.TRUE;
		} else if (!isUvpValid && !isHousepriceValid) {

			return Boolean.TRUE;
		} else {

			return Boolean.FALSE;
		}
	}

	/**
	 * if format of time is like 13:3 it yields 13:30
	 * 
	 * @param time
	 *            - the time
	 * @return - the reformatted time , if time is not formattable, it yields a
	 *         default value
	 */
	public static String checkTime(String time) {

		Boolean timeIsOk = Boolean.FALSE;
		String response = null;

		if (time.contains(Constants.COLON)) {
			String[] timeSplitted = time.split(Constants.COLON);
			String minutes = timeSplitted[1];
			if (minutes.length() == 1) {
				minutes = minutes.concat("0");
				response = timeSplitted[0].concat(Constants.COLON).concat(minutes);
				timeIsOk = Boolean.TRUE;
			} else if (minutes.length() == 2) {
				response = time;
				timeIsOk = Boolean.TRUE;
			}
		}

		return timeIsOk ? response : "9:30";
	}

	/**
	 * yields a sorter for the column of a table that shoulb be sorted
	 * 
	 * @param table
	 *            - the table
	 * @param column
	 *            - the column
	 * @param sortOrder
	 *            - the sort order
	 * @return
	 */
	public static RowSorter<? extends TableModel> getSorter(JTable table, int column, SortOrder sortOrder) {

		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(column, sortOrder));
		sorter.setSortKeys(sortKeys);

		return sorter;
	}

	/***
	 * removes a customer when customers name not starts with regex
	 * 
	 * @param regex
	 *            - the regex
	 * @return true if starts with regex, otherwise false
	 */
	public static Predicate<Customer> testLikePredicate(String regex) {

		return v -> !v.getCustomerName().toUpperCase().startsWith(regex.toUpperCase());
	}

	/**
	 * reads the file that contains data for access ressources and the key for doing
	 * encryption
	 * 
	 * @param directoryOfContractDigitalizer
	 *            - the directoryOfContractDigitalizer * @return - the data for
	 *            access in a map
	 * @throws FileNotFoundException
	 *             - in case if technical error
	 * @throws IOException
	 *             - in case if technical error
	 */
	public static Map<String, String> readAccess(String directoryOfContractDigitalizer)
			throws FileNotFoundException, IOException {

		String path = directoryOfContractDigitalizer + File.separator + Constants.ASSETS + File.separator
				+ Constants.ACCESS + File.separator + Constants.ACCESS_TXT;

		Map<String, String> access = null;

		File file = new File(path);

		if (file.exists()) {

			access = new HashMap<>();

			String line = null;
			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File(path))))) {

				while ((line = bufferedReader.readLine()) != null) {

					if (line.contains(Constants.USER)) {
						access.put(Constants.USER, handleLineOfAccess(line));
					}

					else if (line.contains(Constants.PASSWORD)) {
						access.put(Constants.PASSWORD, handleLineOfAccess(line));
					}

					else if (line.contains(Constants.SERVER)) {
						access.put(Constants.SERVER, handleLineOfAccess(line));
					}

					else if (line.contains(Constants.PORT)) {
						access.put(Constants.PORT, handleLineOfAccess(line));
					} else if (line.contains(Constants.DIRECTORY_REMOTE)) {
						access.put(Constants.DIRECTORY_REMOTE, handleLineOfAccess(line));
					} else if (line.contains(Constants.ENCRYPTION)) {
						access.put(Constants.ENCRYPTION, handleLineOfAccess(line));
					}

					else if (line.contains(Constants.AUTHORIZATION)) {
						access.put(Constants.AUTHORIZATION, handleLineOfAccess(line) + "==");
					}
				}
			}
		}

		return access;
	}

	public static int getMonth(String countOfMonth) {

		int response = 0;

		switch (countOfMonth) {

		case Constants.ONE_MONTH:

			response = 1;
			break;

		case Constants.TWO_MONTHS:

			response = 2;
			break;

		case Constants.THREE_MONTHS:

			response = 3;
			break;

		case Constants.SIX_MONTHS:

			response = 6;
			break;

		case Constants.NINE_MONTHS:

			response = 9;
			break;

		case Constants.TWELVE_MONTHS:

			response = 12;
			break;

		case Constants.FIFTEEN_MONTHS:

			response = 15;
			break;

		case Constants.EIGHTEEN_MONTHS:

			response = 18;
			break;

		case Constants.TWENTY_ONE_MONTHS:

			response = 21;
			break;

		case Constants.TWENTY_FOUR_MONTHS:

			response = 24;
			break;

		}

		return response;
	}

	/**
	 * to execute the client with the cmd
	 * 
	 * @param path
	 *            - the path
	 * @param path2
	 * @throws IOException
	 *             in case of technical problems
	 */
	public static void runDocument(String directory, String path) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("cmd", "/k", "start " + path);
		pb.directory(new File(directory));
		pb.start();

	}

	/**
	 * yields the sum of the uvp parts of the contract
	 * 
	 * @param contract
	 *            - the contract
	 * @return the sum of the uvp parts of the contract
	 */
	public static Double getTotalUvpOfContract(Contract contract) {
		Double response = .0;

		response += contract.getDeliveryAssemblyFeeUVP();
		response += contract.getServiceCostGarageUVP();
		response += contract.getRedemptionDismantledPartsWheelUVP();
		response += contract.getDownPaymentUVP();
		response += contract.getEstateUVP();

		List<SellPosition> lstSellposition = contract.getSellPositions();

		if (lstSellposition != null) {
			for (SellPosition sellPosition : lstSellposition) {
				response += (sellPosition.getQuantity() * sellPosition.getUvp());
			}
		}

		return response;
	}

	/**
	 * yields the sum of the houseprice parts of the contract
	 * 
	 * @param contract
	 *            - the contract
	 * @return the sum of the houseprice parts of the contract
	 */
	public static Double getTotalHousepriceOfContract(Contract contract) {
		Double response = .0;

		response += contract.getDeliveryAssemblyFeeHousePrice();
		response += contract.getServiceCostGarageHousePrice();
		response += contract.getRedemptionDismantledPartsWheelHousePrice();
		response += contract.getDownPaymentHousePrice();
		response += contract.getEstateHousePrice();
		List<SellPosition> lstSellposition = contract.getSellPositions();

		if (lstSellposition != null) {
			for (SellPosition sellPosition : lstSellposition) {
				response += (sellPosition.getQuantity() * sellPosition.getHousePrice());
			}
		}

		return response;
	}

	/**
	 * request access from Pi
	 * 
	 * @param path
	 *            - the path
	 * @param user
	 *            - the user
	 * @param password
	 *            - the password
	 * @param pathToAccess
	 *            - the path to the access folder
	 * @throws IOException
	 *             - in case of technical error
	 */
	public static void requestAccess(String path, String user, String password, String pathToAccess)
			throws IOException {

		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, user, password);

		SmbFile smbFile = new SmbFile(path, auth);

		if (smbFile.exists()) {

			SmbFile[] files = smbFile.listFiles();

			SmbFileInputStream in = null;
			FileOutputStream out = null;
			File file;
			for (SmbFile smbfile : files) {

				file = new File(pathToAccess + File.separator + smbfile.getName());

				in = new SmbFileInputStream(smbfile);
				out = new FileOutputStream(file);
				IOUtils.copy(in, out);
				break;
			}

			in.close();
			out.close();
		} else {
			new InformationProvider(Constants.WARNING, JOptionPane.WARNING_MESSAGE,
					ShoreConstants.CONNECTION_NOT_EXISTING).run();
		}

	}

	/**
	 * clears the folder access
	 * 
	 * @param directoryOfBikeContract
	 *            - the directory of the bike contract
	 */
	public static void clearAccess(String directoryOfBikeContract) {

		File directoryOfAcces = new File(
				directoryOfBikeContract + File.separator + Constants.ASSETS + File.separator + Constants.ACCESS);

		for (File file : directoryOfAcces.listFiles()) {
			file.delete();
		}

	}

	/***
	 * moves the implacement to the clipboard
	 * 
	 * @param implacement
	 *            - the implacement
	 */
	public static void moveToClipboard(String implacement) {
		StringSelection selection = new StringSelection(implacement);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);

	}

	/***
	 * reads the smb password from the local folder
	 * 
	 * @param directoryOfBikeContract
	 *            - the directory of the bike contract
	 * @return the smb password
	 */
	public static String getSmbPassword(String directoryOfBikeContract) {

		String response = null;
		String path = directoryOfBikeContract + File.separator + Constants.ASSETS + File.separator
				+ Constants.SMB_ACCESS + File.separator + Constants.SMB_ACCESS_TXT;

		try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(path))))) {
			String line = null;

			while ((line = bufferedReader.readLine()) != null) {
				response = line.trim();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
