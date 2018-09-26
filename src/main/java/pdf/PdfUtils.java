package pdf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
//import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import bikeData.BikeData;
import constants.Constants;
import constants.Fonts;
import constants.IntersportColors;
import contract.Contract;
import contract.SellPosition;
import customer.Customer;
import seller.Seller;
import utils.Utils;

/** the class PdfUtils */
public class PdfUtils {

	static Font fontBoldSub = new Font(FontFamily.HELVETICA, Constants.FONTSIZE_SUB, Font.BOLD);

	public static int countOfCustomerCells;
	public static int countOfBikeDataCells;

	/**
	 * yields the table for the contract
	 * 
	 * @param contract
	 *            - the contract
	 * @param model
	 *            - the model of the contract
	 * @return
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	public static PdfPTable getContractTable(Contract contract, utils.TableModel model) throws DocumentException {

		/** a table with six columns */
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(PdfConstants.WIDTH_OF_WRAPPED_TABLE);
		table.setSpacingBefore(10f);
		table.setWidths(new int[] { 1, 3, 5, 5, 3, 3 });
		// the cell object
		PdfPCell cell;

		/** the quantity: colspan: 3 */
		cell = new HeaderCell(IntersportColors.INTERSPORT_GRAY_DARK, new Phrase(Constants.QUANTITY, Fonts.bodyText),
				Element.ALIGN_CENTER);
		table.addCell(cell);

		/** the article: colspan: 5 */
		cell = new HeaderCell(IntersportColors.INTERSPORT_GRAY_DARK, new Phrase(Constants.EAN, Fonts.bodyText),
				Element.ALIGN_LEFT);
		table.addCell(cell);

		/** the brand: colspan: 4 */
		cell = new HeaderCell(IntersportColors.INTERSPORT_GRAY_DARK, new Phrase(Constants.ARTICLE, Fonts.bodyText),
				Element.ALIGN_LEFT);
		table.addCell(cell);

		/** the sign: colspan: 7 */
		cell = new HeaderCell(IntersportColors.INTERSPORT_GRAY_DARK, new Phrase(Constants.PRODUCER, Fonts.bodyText),
				Element.ALIGN_LEFT);
		table.addCell(cell);

		/** the uvp: colspan: 3 */
		cell = new HeaderCell(IntersportColors.INTERSPORT_GRAY_DARK, new Phrase(Constants.UVP, Fonts.bodyText),
				Element.ALIGN_LEFT);
		table.addCell(cell);

		/** the houseprice: colspan: 5 */
		cell = new HeaderCell(IntersportColors.INTERSPORT_GRAY_DARK, new Phrase(Constants.HOUSE_PRICE, Fonts.bodyText),
				Element.ALIGN_LEFT);
		table.addCell(cell);

		List<SellPosition> lstSellPosition = contract.getSellPositions();

		@SuppressWarnings("unused")
		int counter = 1;

		/** writing sellpositions */
		for (SellPosition sellPosition : lstSellPosition) {

			PdfPCell cellOfQuantity = new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
					new Phrase(String.valueOf(sellPosition.getQuantity()), Fonts.bodyText2));
			cellOfQuantity.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell cellOfEAN = new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
					new Phrase(sellPosition.getEan(), Fonts.bodyText2));
			cellOfEAN.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell cellOfArticle = new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
					new Phrase(sellPosition.getArticleName(), Fonts.bodyText2));
			cellOfArticle.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell cellOfProducer = new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
					new Phrase(sellPosition.getProducer(), Fonts.bodyText2));
			cellOfProducer.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell cellOfUVP = new SellPositionCell(IntersportColors.INTERSPORT_GRAY, new Phrase(String.valueOf(
					sellPosition.getUvp() + Constants.EMPTY_SPACE + Currency.getInstance(Locale.GERMANY).getSymbol()),
					Fonts.bodyText2));
			cellOfUVP.setHorizontalAlignment(Element.ALIGN_LEFT);

			PdfPCell cellOfHousePrice = new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
					new Phrase(String.valueOf(sellPosition.getHousePrice() + Constants.EMPTY_SPACE
							+ Currency.getInstance(Locale.GERMANY).getSymbol()), Fonts.bodyText2));
			cellOfHousePrice.setHorizontalAlignment(Element.ALIGN_LEFT);

			table.addCell(cellOfQuantity);
			table.addCell(cellOfEAN);
			table.addCell(cellOfArticle);
			table.addCell(cellOfProducer);
			table.addCell(cellOfUVP);
			table.addCell(cellOfHousePrice);

			counter++;
		}

		return table;

	}

	/**
	 * null value to string
	 * 
	 * @param value
	 *            - the value
	 * @return null value to string
	 */
	private static String getPrintableResponse(Object value) {

		return value == null ? Constants.EMPTY_STRING : String.valueOf(value);
	}

	/**
	 * yields the customer table depending on contract and customer
	 * 
	 * @param contract
	 *            - the contract
	 * @param customer
	 *            - the customer
	 * @return the customer table
	 * @throws DocumentException
	 */
	private static PdfPTable getCustomerTable(Contract contract, Customer customer) throws DocumentException {

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		response.getDefaultCell().setPadding(0.0f);
		response.setWidthPercentage(50);

		/** customer name and surname */
		response.addCell(createCustomerNameTable(customer.getCustomerName(), customer.getPrename()));// 2 additional
																										// cells
		/** street and number */
		response.addCell(createStreetAndNumberTable(customer.getStreet(), customer.getNumber()));// 2 additional cells
		/** postalcode and place */
		response.addCell(createPostalcodeAndPlaceTable(customer.getPostalCode(), customer.getPlace()));// 2 additional
																										// cells
		/** mobile */
		if (!customer.getMobile().equals(Constants.NA)) {
			response.addCell(createMobileTable(customer.getMobile()));// 2 additional cells - optional
		} else {

		}
		/** email */
		if (!customer.getEmail().equals(Constants.NA)) {
			response.addCell(createEmailTable(customer.getEmail()));// 2 additional cells - optional
		}

		return response;
	}

	/**
	 * yields the bike data table depending on bikedata and number of seller
	 * 
	 * @param bikeData
	 *            - the bikeData
	 * @param sellerNumber
	 *            - the number of the seller
	 * @param pdfWriter
	 *            - the pdfWriter
	 * @return the bike data table
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	public static PdfPTable getBikeDataTable(BikeData bikeData, String sellerNumber, PdfWriter pdfWriter)
			throws DocumentException { // 6
		// +2

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		response.getDefaultCell().setPadding(0.0f);
		response.setWidthPercentage(50);

		/** requesting status */
		String status = getStatusFromBike(bikeData);

		/** date seller and status */
		response.addCell(createDateSellerAndStatusTable(bikeData.getDate(), sellerNumber, status));// 2 additional cells

		response.addCell(getBikeDataWrappedTable(bikeData));// 4 + 2

		return response;

	}

	/**
	 * yields the table containing date seller and status of bike
	 * 
	 * @param date
	 *            - the date
	 * @param seller
	 *            - the seller
	 * @param status
	 *            - the status
	 * @return the table containing date seller and status
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	private static PdfPTable createDateSellerAndStatusTable(String date, String seller, String status)
			throws DocumentException {
		PdfPTable response = new PdfPTable(3);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		response.getDefaultCell().setPadding(0.0f);

		PdfPTable tableDate = new PdfPTable(1);
		tableDate.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableDate.getDefaultCell().setPadding(0.0f);

		PdfPTable tableSeller = new PdfPTable(1);
		tableSeller.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableSeller.getDefaultCell().setPadding(0.0f);

		PdfPTable tableStatus = new PdfPTable(1);
		tableStatus.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		tableStatus.getDefaultCell().setPadding(0.0f);

		/** the date */
		tableDate.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(date, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));

		/** the seller */
		tableSeller.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(seller, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));

		/** the status */
		tableStatus.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(status, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));

		countOfBikeDataCells++;

		/** type = date */
		tableDate.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.DATE, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		/** type = seller */
		tableSeller.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.SELLER, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		/** type = "" */
		tableStatus.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.STATUS, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfBikeDataCells++;

		response.addCell(tableDate);
		response.addCell(tableSeller);
		response.addCell(tableStatus);

		return response;
	}

	/**
	 * yields the wrapped table for the bike data:
	 * Age,Type,FrameNumber,Inspection...
	 * 
	 * @param bikeData
	 * @return
	 */
	private static PdfPTable getBikeDataWrappedTable(BikeData bikeData) { // 4 + 2

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		response.getDefaultCell().setPadding(0.0f);

		String ageOfBike = getAgeOfBike(bikeData.getNew_Bike(), bikeData.getUsed_Bike());
		String typeOfBike = getTypeOfBike(bikeData);
		response.addCell(getBikeDataConcreteTable(ageOfBike, typeOfBike));// 2 additional cells

		/** the frame number */
		response.addCell(getFrameNumerAndFrameHeightTable(bikeData.getFrameNumber(), bikeData.getFrameHeight()));// 2
																													// additional
																													// cells

		/** the inspection */
		int year = bikeData.getYear();

		if (!(Integer.compare(year, 0) == 0)) {
			response.addCell(getInspectionTable(bikeData.getYear(), bikeData.getMechanic()));// 2
		}

		return response;

	}

	/**
	 * yields the table for the inspection.
	 * 
	 * @param year
	 *            - the year
	 * @param mechanic
	 *            - the mechanic
	 * @return the table for the inspection
	 */
	private static PdfPTable getInspectionTable(int year, String mechanic) {
		PdfPTable response = new PdfPTable(4);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		response.getDefaultCell().setPadding(0.0f);

		/** header */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.INSPECTION + Constants.COLON, Fonts.bodyText), 10, Element.ALIGN_MIDDLE));

		/** From */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(String.valueOf("09." + year), Fonts.bodyText), 10, Element.ALIGN_MIDDLE));

		/** To */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(String.valueOf("02." + (year + 1)), Fonts.bodyText), 10, Element.ALIGN_MIDDLE));

		/** mechanic */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(String.valueOf(mechanic), Fonts.bodyText), 10, Element.ALIGN_MIDDLE));
		countOfBikeDataCells++;

		/** time period */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.TIME_PERIOD + Constants.COLON, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		/** from */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.FROM, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		/** to */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.TO, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		/** mechanic */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.MECHANIC, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfBikeDataCells++;

		return response;
	}

	/**
	 * cfreates the table for the frame number.
	 * 
	 * @param frameNumber
	 *            - the frame numeber
	 * 
	 * @param frameHeight
	 *            - the height of the frame
	 * 
	 * @return - the table for the frame number
	 */
	private static PdfPTable getFrameNumerAndFrameHeightTable(String frameNumber, String frameHeight) {
		PdfPTable response = new PdfPTable(3);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		response.getDefaultCell().setPadding(0.0f);

		/** empty cell */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.EMPTY_STRING, Fonts.bodyText), 10, Element.ALIGN_MIDDLE));

		/** framenumber */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(frameNumber, Fonts.bodyText),
				10, Element.ALIGN_MIDDLE));

		/** frameheight */
		String[] frameHeightSplitted = frameHeight.split(" ");
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(frameHeightSplitted[0], Fonts.bodyText), 10, Element.ALIGN_MIDDLE));
		countOfBikeDataCells++;

		/** sub: */
		/** empty cell */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.EMPTY_STRING, Fonts.fontSubGrayDark), 10, Element.ALIGN_TOP));

		/** framenumber */
		String unit = frameHeightSplitted[1];

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.FRAME_NUMBER, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		/** frameheight */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.FRAME_HEIGHT + " [ " + unit + " ]", Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfBikeDataCells++;

		return response;
	}

	/**
	 * yields the table containing age and type of bike
	 * 
	 * @param ageOfBike
	 *            - the age of bike
	 * @param typeOfBike
	 *            - the type of bike
	 * @return the table containing age and type of bike
	 */
	private static PdfPTable getBikeDataConcreteTable(String ageOfBike, String typeOfBike) {

		PdfPTable response = new PdfPTable(3);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		response.getDefaultCell().setPadding(0.0f);

		/** the header */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.BikeData + Constants.COLON, Fonts.bodyText), 10, Element.ALIGN_MIDDLE));

		/** the age */
		if (ageOfBike.equals(Constants.USED)) {
			StringBuilder builder = new StringBuilder();
			String[] ageOfBikeSplitted = ageOfBike.split(Constants.EMPTY_SPACE);

			for (int i = 1; i < ageOfBikeSplitted.length; i++) {
				builder.append(ageOfBikeSplitted[i]);
				builder.append(Constants.EMPTY_SPACE);
			}
			String ageOfBike_Addition = builder.toString();

			Phrase phrase = new Phrase();
			phrase.add(new Chunk(ageOfBikeSplitted[0], Fonts.bodyText));

			phrase.add(new Chunk(Constants.EMPTY_SPACE + ageOfBike_Addition.trim(), Fonts.fontSubGrayDark));

			response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, phrase, 10, Element.ALIGN_MIDDLE));

		} else {
			response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(ageOfBike, Fonts.bodyText),
					10, Element.ALIGN_MIDDLE));
		}

		/** the type */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(typeOfBike, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));
		countOfBikeDataCells++;

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.EMPTY_STRING, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.STATE, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.TYPE, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfBikeDataCells++;

		return response;
	}

	/**
	 * computes the type of the bike
	 * 
	 * @param bikeData
	 * @return - the type of the bike
	 */
	private static String getTypeOfBike(BikeData bikeData) {

		Integer trekking = bikeData.getTrekking();
		Integer cityTour = bikeData.getCityTour();
		Integer mountainBike = bikeData.getMountainBike();
		Integer childsBike = bikeData.getChildsBike();
		Integer racingCycle = bikeData.getRacingCycle();
		Integer cross = bikeData.getCross();
		Integer bmx = bikeData.getBmx();
		Integer eBike = bikeData.geteBike();
		Integer others = bikeData.getOthers();

		if (Integer.compare(trekking, 1) == 0) {
			return Constants.TREKKING;
		}

		else if (Integer.compare(cityTour, 1) == 0) {
			return Constants.CITY_TOUR;
		} else if (Integer.compare(mountainBike, 1) == 0) {
			return Constants.MOUTAINBIKE;
		} else if (Integer.compare(childsBike, 1) == 0) {
			return Constants.CHILDS_BIKE;
		} else if (Integer.compare(racingCycle, 1) == 0) {
			return Constants.RACING_CYCLE;
		} else if (Integer.compare(cross, 1) == 0) {
			return Constants.CROSS;
		} else if (Integer.compare(bmx, 1) == 0) {
			return Constants.BMX;
		} else if (Integer.compare(eBike, 1) == 0) {
			return Constants.E_BIKE;
		} else if (Integer.compare(others, 1) == 0) {
			return Constants.OTHERS;
		}

		return null;
	}

	/**
	 * 
	 * @param new_Bike
	 *            - is bike new
	 * @param used_Bike
	 *            - is bike used
	 * @return Age of Bike as String
	 */
	private static String getAgeOfBike(int new_Bike, int used_Bike) {

		if (Integer.compare(new_Bike, 1) == 0) {
			return Constants.NEW;
		} else if (Integer.compare(used_Bike, 1) == 0) {
			return Constants.USED;
		}

		return null;
	}

	/**
	 * extratcs the status of the bike from it's bikeData
	 * 
	 * @param bikeData
	 *            - the received bike data
	 * @return - the status
	 */
	private static String getStatusFromBike(BikeData bikeData) {

		Integer delivery = bikeData.getDelivery();
		Integer collection = bikeData.getCollection();
		Integer takeAway = bikeData.getTakeAway();
		Integer booked = bikeData.getBooked();

		if (Integer.compare(delivery, 1) == 0) {
			return Constants.DELIVERY;
		} else if (Integer.compare(collection, 1) == 0) {
			return Constants.COLLECTION;
		} else if (Integer.compare(takeAway, 1) == 0) {
			return Constants.TAKEAWAY;
		} else if (Integer.compare(booked, 1) == 0) {
			return Constants.BOOKED;
		}
		return null;

	}

	/**
	 * creates the table for the customer name
	 * 
	 * @param customerName
	 *            - the name of the customer
	 * @param prename
	 *            - the prename
	 * @return - the table for the customer name
	 */
	private static PdfPTable createCustomerNameTable(String customerName, String prename) {
		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		StringBuilder builder = new StringBuilder();

		builder.append(customerName);
		builder.append(Constants.EMPTY_SPACE);
		builder.append(prename);

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(builder.toString(), Fonts.bodyText), 10, Element.ALIGN_MIDDLE));
		countOfCustomerCells++;
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.CUSTOMER_NAME_AND_SURNAME, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfCustomerCells++;

		return response;
	}

	/**
	 * creates the table for the street und the housenumber
	 * 
	 * @param street
	 *            - the street
	 * @param number
	 *            - the housenumber
	 * @return the table
	 */
	private static PdfPTable createStreetAndNumberTable(String street, String number) {

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		StringBuilder builder = new StringBuilder();

		builder.append(street);
		builder.append(Constants.EMPTY_SPACE);
		builder.append(number);

		/** street and number */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(builder.toString(), Fonts.bodyText), 10, Element.ALIGN_MIDDLE));
		countOfCustomerCells++;
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.STREET_AND_NUMBER, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfCustomerCells++;

		return response;

	}

	/**
	 * creates the table for the postalcode und the place
	 * 
	 * @param postalCode
	 *            - the postalCode
	 * @param place
	 *            - the place
	 * @return the table
	 */
	private static PdfPTable createPostalcodeAndPlaceTable(String postalCode, String place) throws DocumentException {
		PdfPTable response = new PdfPTable(2);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		response.setWidths(new int[] { 6, 13 });
		/** postalcode */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(postalCode, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));

		/** place */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(place, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));
		countOfCustomerCells++;

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.POSTAL_CODE, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.PLACE, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfCustomerCells++;

		return response;
	}

	/**
	 * creates a table for mobile number
	 * 
	 * @param mobile
	 *            - the mobile numer
	 * @return the wrapped mobile number
	 */
	private static PdfPTable createMobileTable(String mobile) {

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		/** mobile */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(mobile, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));
		countOfCustomerCells++;

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.MOBILE, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfCustomerCells++;

		return response;
	}

	/**
	 * creates a table for email
	 * 
	 * @param email
	 *            - the email
	 * @return the wrapped email
	 */
	private static PdfPTable createEmailTable(String email) {
		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		/** email */
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE, new Phrase(email, Fonts.bodyText), 10,
				Element.ALIGN_MIDDLE));
		countOfCustomerCells++;

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.EMAIL, Fonts.fontSubGrayDark), 5, Element.ALIGN_TOP));
		countOfCustomerCells++;

		return response;
	}

	/**
	 * brings the information about customer and bike data on the page
	 * 
	 * @param contract
	 *            - the contract
	 * @param customer
	 *            - the customer
	 * @param pdfWriter
	 *            - the pdfWriter
	 * @return information about customer and bike data wrapped in a table
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	public static PdfPTable getCustomerAndBikeDataTable(Contract contract, Customer customer, PdfWriter pdfWriter)
			throws DocumentException {

		PdfPTable response = new PdfPTable(3);
		response.setWidths(new int[] { 9, 2, 9 });
		response.setWidthPercentage(PdfConstants.WIDTH_OF_WRAPPED_TABLE);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		PdfPTable customerTable = getCustomerTable(contract, customer); // 6 + 4

		Seller seller = contract.getSeller();
		if (seller != null) {

			PdfPTable bikeDataAndSignTable = getBikeDataTable(contract.getBikeData(), seller.getNumber(), pdfWriter); // 6
																														// +
																														// 2

			int difference = countOfCustomerCells - countOfBikeDataCells;

			if (Integer.compare(difference, 4) == 0) {

				/** email + mobile, no inspection */

				bikeDataAndSignTable.addCell(getCustomerSignTable(pdfWriter));

				for (int i = 0; i < difference - 2; i++) {
					bikeDataAndSignTable.addCell(getEmptyCell());
				}

			}

			else if (Integer.compare(difference, 2) == 0) {

				/** email or mobile, no inspection */

				bikeDataAndSignTable.addCell(getCustomerSignTable(pdfWriter));

			}

			else if (Integer.compare(difference, 0) == 0) {

				/** no difference of count of cells */
				bikeDataAndSignTable.addCell(getCustomerSignTable(pdfWriter));
				customerTable.addCell(getEmptyCell());

			}

			else if (Integer.compare(difference, -2) == 0) {
				/** left: no optional filled, right: Inspection */
				for (int i = 0; i < -difference - 1; i++) {
					customerTable.addCell(getEmptyCell());
				}

				bikeDataAndSignTable.addCell(getCustomerSignTable(pdfWriter));

			}

			response.addCell(customerTable);
			response.addCell(PdfUtils.getEmptyCell());
			response.addCell(bikeDataAndSignTable);
		}
		return response;
	}

	/**
	 * yields the table with informations about seller, delivery, cunstruction
	 * service....
	 * 
	 * @param contract
	 * @param model
	 * @param pdfWriter
	 * @return
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static PdfPTable getSellerAndContractFooterTable(Contract contract, utils.TableModel model,
			PdfWriter pdfWriter) throws DocumentException, MalformedURLException, IOException {
		/** writing other contract parts */
		PdfPTable response = new PdfPTable(2);
		response.setSpacingBefore(2.0f);
		response.setKeepTogether(Boolean.TRUE);
		response.setWidths(new int[] { 11, 9 });
		response.setWidthPercentage(PdfConstants.WIDTH_OF_WRAPPED_TABLE);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		BikeData bikeData = contract.getBikeData();
		if (bikeData != null) {
			response.addCell(
					getDelivery_ConstructionServiceAndSellerTable(contract.getBikeData().getDateOfAppointment(),
							contract.getBikeData().getTimeOfAppointment(), contract.getSeller()));
		} else {
			response.addCell(new PdfPTable(3));
		}

		/** a table with three columns */

		response.addCell(getOtherContractPartTable(model, pdfWriter));

		return response;
	}

	/**
	 * yields the table for other parts of contract
	 * 
	 * @param model
	 *            - the model of the contract
	 * @param pdfWriter
	 *            - the writer
	 * @return - the table
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	private static PdfPTable getOtherContractPartTable(utils.TableModel model, PdfWriter pdfWriter)
			throws DocumentException {
		PdfPTable response = new PdfPTable(3);
		response.setWidthPercentage(30f);

		@SuppressWarnings("unused")
		int emptyRoCounter = 0;

		response.setWidths(new int[] { 20, 21, 20 });

		Double subtotalUVP = Utils.getSubtotalUVPFromModel(model);
		Double totalUV = Utils.getTotalUVPFromModel(model);
		Double subtotalHousePrice = Utils.getSubtotalHousePriceFromModel(model);
		Double totalHouseprice = Utils.getTotalHousePriceFromModel(model);

		Boolean isUVPEqual = (Double.compare(subtotalUVP, totalUV) == 0);
		Boolean isHousePriceEqual = (Double.compare(subtotalHousePrice, totalHouseprice) == 0);

		if (!isUVPEqual || !isHousePriceEqual) {
			/** subtotalUVP */

			response.addCell(new CustomerCell(IntersportColors.INTERSPORT_GRAY_DARK,
					new Phrase(Constants.SUBTOTAL, Fonts.fontSubBlack), 15, PdfPCell.ALIGN_BOTTOM));

			response.addCell(new CustomerCell(IntersportColors.INTERSPORT_GRAY_DARK,
					new Phrase(getPrintableResponse(Utils.getSubtotalUVPFromModel(model)) + Constants.EMPTY_SPACE
							+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText),
					15, PdfPCell.ALIGN_BOTTOM));

			/** subtotalHousePrice */
			response.addCell(new CustomerCell(IntersportColors.INTERSPORT_GRAY_DARK,
					new Phrase(getPrintableResponse(Utils.getSubtotalHousePriceFromModel(model)) + Constants.EMPTY_SPACE
							+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText),
					15, PdfPCell.ALIGN_BOTTOM));

			/** deliveryAssemblyFee */
			Double deliveryAssemblyFeeUVP = Utils.getDeliveryAssemblyFeeUVPFromModel(model);
			Double deliveryAssemblyFeeHousePrice = Utils.getDeliveryAssemblyFeeHousePriceFromModel(model);

			if (Utils.isStringValid(String.valueOf(deliveryAssemblyFeeUVP))
					|| Utils.isStringValid(String.valueOf(deliveryAssemblyFeeHousePrice))) {

				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(Constants.DELIVERY_ASSEMBLY_FEE, Fonts.fontSubBlack)));

				/** UVP */
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(deliveryAssemblyFeeUVP) + Constants.EMPTY_SPACE
								+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2)));

				/** housePrice */
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(deliveryAssemblyFeeHousePrice) + Constants.EMPTY_SPACE
								+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2)));
			} else {
				emptyRoCounter++;
			}

			/** serviceCostGarage */

			Double serviceCostGarageUVP = Utils.getServiceCostGarageUVPFromModel(model);
			Double serviceCostGarageHousePrice = Utils.getServiceCostGarageHousePriceFromModel(model);
			if (Utils.isStringValid(String.valueOf(serviceCostGarageUVP))
					|| Utils.isStringValid(String.valueOf(serviceCostGarageHousePrice))) {

				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(Constants.SERVICE_COST_GARAGE, Fonts.fontSubBlack)));
				/** UVP */
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(serviceCostGarageUVP) + Constants.EMPTY_SPACE
								+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2)));

				/** HousePrice */
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(serviceCostGarageHousePrice) + Constants.EMPTY_SPACE
								+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2)));
			} else {
				emptyRoCounter++;
			}

			/** REDEMPTION_DISMANTLED_PARTS_WHEEL */
			Double redemptionDismantledPartsWheelUVP = Utils.getRedemptionDismantledPartsWheelUVPFromModel(model);
			Double redemptionDismantledPartsWheelHousePrice = Utils
					.getRedemptionDismantledPartsWheelHousePriceFromModel(model);
			if (Utils.isStringValid(String.valueOf(redemptionDismantledPartsWheelHousePrice))
					|| Utils.isStringValid(String.valueOf(redemptionDismantledPartsWheelUVP))) {
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(Constants.REDEMPTION_DISMANTLED_PARTS_WHEEL, Fonts.fontSubBlack)));
				/** UVP */
				response.addCell(
						new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
								new Phrase(getPrintableResponse(redemptionDismantledPartsWheelUVP)
										+ Constants.EMPTY_SPACE + Currency.getInstance(Locale.GERMANY).getSymbol(),
										Fonts.bodyText2)));
				/** HousePrice */
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(redemptionDismantledPartsWheelHousePrice)
								+ Constants.EMPTY_SPACE + Currency.getInstance(Locale.GERMANY).getSymbol(),
								Fonts.bodyText2)));
			} else {
				emptyRoCounter++;
			}

			/** down payment */
			Double dowmPaymentUVP = Utils.getDowmPaymentUVPFromModel(model);
			Double dowmPaymentHousePrice = Utils.getDowmPaymentHousePriceFromModel(model);
			if (Utils.isStringValid(String.valueOf(dowmPaymentUVP))
					|| Utils.isStringValid(String.valueOf(dowmPaymentHousePrice))) {
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(Constants.DOWN_PAYMENT, Fonts.fontSubBlack)));
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(dowmPaymentUVP) + Constants.EMPTY_SPACE
								+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2)));
				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(getPrintableResponse(dowmPaymentHousePrice) + Constants.EMPTY_SPACE
								+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2)));
			} else {
				emptyRoCounter++;
			}

			/** estate */
			Double estateUVP = Utils.getEstateUVPFromModel(model);
			Double estateHousePrice = Utils.getEstateHousePriceFromModel(model);
			if (Utils.isStringValid(String.valueOf(estateUVP))
					|| Utils.isStringValid(String.valueOf(estateHousePrice))) {

				response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
						new Phrase(Constants.ESTATE, Fonts.fontSubBlack)));
				if ((estateUVP > 0.0) && (estateUVP < 1.0)) {

					estateUVP *= 100;
					response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
							new Phrase(getPrintableResponse(estateUVP) + Constants.EMPTY_SPACE + Constants.PERCENT,
									Fonts.bodyText2)));

				} else {

					response.addCell(
							new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
									new Phrase(
											getPrintableResponse(estateUVP) + Constants.EMPTY_SPACE
													+ Currency.getInstance(Locale.GERMANY).getSymbol(),
											Fonts.bodyText2)));
				}

				if ((estateHousePrice < 1.0) && (estateHousePrice > 0.0)) {

					estateHousePrice *= 100;
					response.addCell(new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
							new Phrase(
									getPrintableResponse(estateHousePrice) + Constants.EMPTY_SPACE + Constants.PERCENT,
									Fonts.bodyText2)));

				} else {

					response.addCell(
							new SellPositionCell(IntersportColors.INTERSPORT_GRAY,
									new Phrase(
											getPrintableResponse(estateHousePrice) + Constants.EMPTY_SPACE
													+ Currency.getInstance(Locale.GERMANY).getSymbol(),
											Fonts.bodyText2)));

				}
			} else {
				emptyRoCounter++;
			}
		}

		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_GRAY_DARK,
				new Phrase(Constants.TOTAL, Fonts.fontSubBlack), 15, PdfPCell.ALIGN_BOTTOM));
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_GRAY_DARK,
				new Phrase(getPrintableResponse(Utils.getTotalUVPFromModel(model)) + Constants.EMPTY_SPACE
						+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2),
				15, PdfPCell.ALIGN_BOTTOM));
		response.addCell(new CustomerCell(IntersportColors.INTERSPORT_GRAY_DARK,
				new Phrase(getPrintableResponse(Utils.getTotalHousePriceFromModel(model)) + Constants.EMPTY_SPACE
						+ Currency.getInstance(Locale.GERMANY).getSymbol(), Fonts.bodyText2),
				15, PdfPCell.ALIGN_BOTTOM));

		/** Seller sign */

		PdfPCell emptySellerSignCell = getEmptyCell();
		emptySellerSignCell.setColspan(3);
		response.addCell(emptySellerSignCell);
		response.addCell(emptySellerSignCell);
		PdfPCell sellerSignCell = new PdfPCell();
		sellerSignCell.setColspan(3);
		sellerSignCell.setBorder(Rectangle.BOTTOM);
		sellerSignCell.setBorderWidthBottom(0.1f);
		response.addCell(sellerSignCell);

		PdfPCell sellerSignCellSuB = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.SELLER_SIGN_AND_ORDER, Fonts.fontSubBlack), 10, Element.ALIGN_TOP);
		sellerSignCellSuB.setPaddingTop(2.0f);
		sellerSignCellSuB.setColspan(3);

		response.addCell(sellerSignCellSuB);

		return response;
	}

	/**
	 * creates the table for delivery, construction and seller
	 * 
	 * @param dateOfAppointment
	 *            - the date of the appointment
	 * @param timeOfAppointment
	 *            - the time of the appointment
	 * @param seller
	 *            - the seller
	 * @return - the table
	 * @throws DocumentException
	 *             - in case of technical error
	 * @throws MalformedURLException
	 *             - in case of technical error
	 * @throws IOException
	 *             - in case of technical error
	 */
	private static PdfPTable getDelivery_ConstructionServiceAndSellerTable(String dateOfAppointment,
			String timeOfAppointment, Seller seller) throws DocumentException, MalformedURLException, IOException {
		PdfPTable response = new PdfPTable(3);
		response.setWidths(new int[] { 1, 1, 1 });
		response.setWidthPercentage(50f);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		response.addCell(getDeliveryServiceTable());
		response.addCell(getConstructionServiceTable());
		response.addCell(getPickUpDateTable(dateOfAppointment, timeOfAppointment));

		// second line
		/** informations about seller: */
		PdfPTable tableOfInformationOfSeller = new PdfPTable(1);

		PdfPCell headerCell = new PdfPCell(new Phrase(PdfConstants.YOU_WERE_SERVED_BY, fontBoldSub));
		headerCell.setBorder(Rectangle.NO_BORDER);
		headerCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableOfInformationOfSeller.addCell(headerCell);

		/** name of seller */
		PdfPCell nameOfSellerCell = new PdfPCell(
				new Phrase(seller.getPrename() + Constants.EMPTY_SPACE + seller.getName(), Fonts.fontSubGrayDark));
		nameOfSellerCell.setBorder(Rectangle.NO_BORDER);
		nameOfSellerCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableOfInformationOfSeller.addCell(getEmptyCell());

		/** mobile of seller */
		PdfPCell mobileOfSellerCell = new PdfPCell(new Phrase(seller.getMobile(), Fonts.fontSubGrayDark));
		mobileOfSellerCell.setBorder(Rectangle.NO_BORDER);
		mobileOfSellerCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		tableOfInformationOfSeller.addCell(getEmptyCell());

		response.addCell(tableOfInformationOfSeller);

		/** image of seller: */
		PdfPTable tableOfImageOfSeller = new PdfPTable(1);
		Image image = Image.getInstance(seller.getByteArray());
		image.scalePercent(Constants.SCALE_Of_SELLER_PHOTO);

		PdfPCell imageCell = new PdfPCell(image);
		imageCell.setBorder(Rectangle.NO_BORDER);
		imageCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		// imageCell.setRowspan(3);
		tableOfImageOfSeller.addCell(imageCell);

		response.addCell(tableOfImageOfSeller);

		response.addCell(getEmptyCell());

		// third row
		response.addCell(getEmptyCell());
		response.addCell(getEmptyCell());
		response.addCell(getEmptyCell());

		return response;

	}

	/**
	 * yields the pickup table depending on date and timne of appointment
	 * 
	 * @param dateOfAppointment
	 *            - the date of the appointment
	 * @param timeOfAppointment
	 *            - the time of the appointment
	 * @return the pickup table
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	private static PdfPTable getPickUpDateTable(String dateOfAppointment, String timeOfAppointment)
			throws DocumentException {
		PdfPTable response = new PdfPTable(2);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		PdfPCell pickUpDateHeader = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.PICKUP_DATE, fontBoldSub), 5, Element.ALIGN_MIDDLE);
		pickUpDateHeader.setColspan(2);
		pickUpDateHeader.setPaddingLeft(10.0f);
		response.addCell(pickUpDateHeader);

		CustomerCell cell21 = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.FINISHED_AT_DATE + Constants.COLON, Fonts.fontSubBlack),
				PdfConstants.HEIGHT_OF_FOOTER_CELL, Element.ALIGN_TOP);
		cell21.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		cell21.setPaddingLeft(10.0f);
		response.addCell(cell21);

		CustomerCell cell22 = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(Constants.FINISHED_AT_TIME + Constants.COLON, Fonts.fontSubBlack),
				PdfConstants.HEIGHT_OF_FOOTER_CELL, Element.ALIGN_TOP);
		cell22.setPaddingLeft(10.0f);
		cell22.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		response.addCell(cell22);

		CustomerCell cell31 = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(dateOfAppointment, Fonts.bodyText2Coral), 10, Element.ALIGN_MIDDLE);
		cell31.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		// cell31.setPaddingTop(2.0f);
		cell31.setPaddingLeft(10.0f);
		response.addCell(cell31);

		CustomerCell cell32 = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(timeOfAppointment, Fonts.bodyText2Coral), 10, Element.ALIGN_MIDDLE);
		cell32.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		// cell32.setPaddingTop(2.0f);
		cell32.setPaddingLeft(10.0f);
		response.addCell(cell32);

		return response;
	}

	/**
	 * yields the table for the construction service
	 * 
	 * @return - the table
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	private static PdfPTable getConstructionServiceTable() throws DocumentException {

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		// response.setWidthPercentage(60f);
		response.setWidths(new int[] { 20 });
		PdfPCell constructionServiceHeaderCell = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.CONSTRUCTION_SERVICE, fontBoldSub), 5, Element.ALIGN_MIDDLE);
		response.addCell(constructionServiceHeaderCell);

		Phrase phrase1 = new Phrase();
		Chunk chunk11 = new Chunk(
				PdfConstants.SEVENTY_NINE + Currency.getInstance(Locale.GERMANY).getSymbol() + PdfConstants.PER_HOUR,
				fontBoldSub);
		Chunk chunk12 = new Chunk(Constants.EMPTY_SPACE + PdfConstants.POWER_STATION, Fonts.fontSubBlack);
		phrase1.add(chunk11);
		phrase1.add(chunk12);
		CustomerCell cell1 = new CustomerCell(IntersportColors.INTERSPORT_WHITE, phrase1, 10, Element.ALIGN_CENTER);
		cell1.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		response.addCell(cell1);

		Phrase phrase2 = new Phrase();
		Chunk chunk21 = new Chunk(PdfConstants.SIXTY_NINE + Currency.getInstance(Locale.GERMANY).getSymbol(),
				fontBoldSub);
		Chunk chunk22 = new Chunk(Constants.EMPTY_SPACE + PdfConstants.HOME_TRAINER, Fonts.fontSubBlack);
		phrase2.add(chunk21);
		phrase2.add(chunk22);

		CustomerCell cell2 = new CustomerCell(IntersportColors.INTERSPORT_WHITE, phrase2, 10, Element.ALIGN_CENTER);
		cell2.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		response.addCell(cell2);

		Phrase phrase3 = new Phrase();
		Chunk chunk31 = new Chunk(PdfConstants.EIGHTY_NINE + Currency.getInstance(Locale.GERMANY).getSymbol(),
				fontBoldSub);
		Chunk chunk32 = new Chunk(Constants.EMPTY_SPACE + PdfConstants.CROSS_TRAINER, Fonts.fontSubBlack);
		phrase3.add(chunk31);
		phrase3.add(chunk32);

		CustomerCell cell3 = new CustomerCell(IntersportColors.INTERSPORT_WHITE, phrase3, 10, Element.ALIGN_CENTER);
		cell3.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		response.addCell(cell3);

		return response;
	}

	/**
	 * yields the table for the delivery service
	 * 
	 * @return - the table
	 * @throws DocumentException
	 *             - in case of technical error
	 */
	private static PdfPTable getDeliveryServiceTable() throws DocumentException {

		PdfPTable response = new PdfPTable(1);
		response.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		PdfPCell deliveryServiceHeader = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.DELIVERY_SERVICE, fontBoldSub), 5, Element.ALIGN_MIDDLE);
		response.addCell(deliveryServiceHeader);

		Phrase phrase1 = new Phrase();
		Chunk chunk11 = new Chunk(PdfConstants.TWENTY_NINE + Currency.getInstance(Locale.GERMANY).getSymbol(),
				fontBoldSub);
		Chunk chunk12 = new Chunk(Constants.EMPTY_SPACE + Constants.BIKE, Fonts.fontSubBlack);
		phrase1.add(chunk11);
		phrase1.add(chunk12);
		CustomerCell cell1 = new CustomerCell(IntersportColors.INTERSPORT_WHITE, phrase1, 1, Element.ALIGN_CENTER);

		cell1.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);

		response.addCell(cell1);

		Phrase phrase2 = new Phrase();
		Chunk chunk21 = new Chunk(PdfConstants.TWENTY_NINE + Currency.getInstance(Locale.GERMANY).getSymbol(),
				fontBoldSub);
		Chunk chunk22 = new Chunk(Constants.EMPTY_SPACE + PdfConstants.FITNESS_INSTRUCTOR, Fonts.fontSubBlack);
		phrase2.add(chunk21);
		phrase2.add(chunk22);
		CustomerCell cell2 = new CustomerCell(IntersportColors.INTERSPORT_WHITE, phrase2, 1, Element.ALIGN_CENTER);

		cell2.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);

		response.addCell(cell2);

		return response;
	}

	/**
	 * generates an empty cell
	 * 
	 * @return an empty cell
	 */
	static PdfPCell getEmptyCell() {

		PdfPCell emptyCell = new PdfPCell(new Phrase(Constants.EMPTY_SPACE));
		emptyCell.setBorder(Rectangle.NO_BORDER);

		return emptyCell;
	}

	/**
	 * yields the customer sign table
	 * 
	 * @param pdfWriter
	 *            - the pdfwriter
	 * @return the customer sign table
	 */
	private static PdfPTable getCustomerSignTable(PdfWriter pdfWriter) {

		PdfPTable response = new PdfPTable(1);
		response.addCell(getEmptyCell());
		PdfPCell customerSignCell = new PdfPCell();
		customerSignCell.setBorder(Rectangle.BOTTOM);
		customerSignCell.setBorderWidthBottom(0.1f);
		response.addCell(customerSignCell);

		PdfPCell customerSignSubCell = new CustomerCell(IntersportColors.INTERSPORT_WHITE,
				new Phrase(PdfConstants.CUSTOMER_SIGN_AND_ORDER, Fonts.fontSubBlack), 10, Element.ALIGN_TOP);
		customerSignSubCell.setPaddingTop(2.0f);

		response.addCell(customerSignSubCell);

		return response;
	}

	/**
	 * 
	 * @param document
	 * @param pdfWriter
	 * @throws DocumentException
	 */
	public static void getHeaderAndFooter(Document document, PdfWriter pdfWriter) throws DocumentException {
		HeaderFooterPageEvent event = new HeaderFooterPageEvent();
		pdfWriter.setPageEvent(event);
	}

}
