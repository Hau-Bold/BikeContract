package pdf;

import java.io.File;
import java.io.IOException;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import constants.Constants;
import constants.Fonts;
import constants.IntersportColors;
import contractDigitalizer.BikeContract;

/** the class HeaderFooterPageEvent */
public class HeaderFooterPageEvent extends PdfPageEventHelper {

	private String pathToImage;

	/**
	 * Constructor.
	 */
	public HeaderFooterPageEvent() {
		pathToImage = BikeContract.getDirectoryOfBikeContract() + File.separator + Constants.ASSETS + File.separator
				+ Constants.IMAGE + File.separator;
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		super.onEndPage(writer, document);

		try {
			addHeader(writer, document);
			addFooter(writer);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	private void addHeader(PdfWriter writer, Document document) throws DocumentException, IOException {

		getHeader(writer, document);
	}

	private void addFooter(PdfWriter writer) throws DocumentException, IOException {
		getFooter(writer);
	}

	// TODO: no space at new page? sth for future
	private void getHeader(PdfWriter writer, Document document) throws DocumentException, IOException {

		PdfPTable header = new PdfPTable(3);
		header.setTotalWidth(PageSize.A4.getWidth() - 2 * PdfConstants.START_OF_TABLES);
		header.setWidths(new int[] { 9, 2, 9 });
		header.setLockedWidth(Boolean.TRUE);
		header.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		header.setSpacingAfter(50.0f);

		Image logoLeft = Image.getInstance(pathToImage + "ContractLeftLogo.jpg");
		logoLeft.scalePercent(30f);

		PdfPCell imageLeft = new PdfPCell(logoLeft);
		imageLeft.setHorizontalAlignment(Element.ALIGN_LEFT);
		imageLeft.setPaddingLeft(35.0f);
		imageLeft.setPaddingTop(20.0f);
		imageLeft.setBackgroundColor(new BaseColor(IntersportColors.INTERSPORT_GRAY.getRGB()));
		imageLeft.setBorder(Rectangle.NO_BORDER);

		header.addCell(imageLeft);

		PdfPCell empty = PdfUtils.getEmptyCell();
		empty.setPaddingTop(20.0f);
		empty.setBackgroundColor(new BaseColor(IntersportColors.INTERSPORT_GRAY.getRGB()));
		header.addCell(empty);

		Image logoRight = Image.getInstance(pathToImage + "ContractRightLogo.jpg");
		logoRight.scalePercent(30f);
		PdfPCell imageRight = new PdfPCell(logoRight);
		imageRight.setPaddingRight(25.0f);
		imageRight.setPaddingTop(20.0f);
		imageRight.setBorder(Rectangle.NO_BORDER);
		imageRight.setHorizontalAlignment(Element.ALIGN_RIGHT);
		imageRight.setBackgroundColor(new BaseColor(IntersportColors.INTERSPORT_GRAY.getRGB()));
		header.addCell(imageRight);

		header.writeSelectedRows(0, -1, PdfConstants.START_OF_TABLES, PageSize.A4.getHeight(),
				writer.getDirectContent());
	}

	private void getFooter(PdfWriter writer) throws DocumentException {
		PdfPTable footer = new PdfPTable(4);
		footer.setTotalWidth(PageSize.A4.getWidth() - 2 * PdfConstants.START_OF_TABLES);
		footer.setLockedWidth(Boolean.TRUE);

		footer.getDefaultCell().setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER);

		FooterCellLeftPadding cell11 = new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN, Fonts.footerText),
				1, Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_HEADER_CELL,
				PdfConstants.FOOTER_PADDING_HEADER_LEFT);
		cell11.setPaddingTop(PdfConstants.FOOTER_PADDING_TOP);
		footer.addCell(cell11);

		FooterCellLeftPadding cell12 = new FooterCellLeftPadding(
				new Phrase(PdfConstants.MANAGER + Constants.COLON, Fonts.footerText), 1, Element.ALIGN_CENTER,
				PdfConstants.HEIGHT_OF_FOOTER_HEADER_CELL, PdfConstants.FOOTER_PADDING_HEADER_MIDDLE);
		cell12.setPaddingTop(PdfConstants.FOOTER_PADDING_TOP);
		footer.addCell(cell12);

		FooterCellLeftPadding cell13 = new FooterCellLeftPadding(new Phrase(PdfConstants.VR_BANC, Fonts.footerText), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_HEADER_CELL,
				PdfConstants.FOOTER_PADDING_HEADER_RIGHT);
		cell13.setPaddingTop(PdfConstants.FOOTER_PADDING_TOP);
		footer.addCell(cell13);

		PdfPCell empty = PdfUtils.getEmptyCell();
		empty.setPaddingTop(PdfConstants.FOOTER_PADDING_TOP);
		empty.setBackgroundColor(new BaseColor(IntersportColors.INTERSPORT_TURQUOISE.getRGB()));
		empty.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_HEADER_CELL);

		footer.addCell(empty);

		PdfPCell emptySub = PdfUtils.getEmptyCell();
		emptySub.setFixedHeight(PdfConstants.HEIGHT_OF_FOOTER_CELL);
		emptySub.setBackgroundColor(new BaseColor(IntersportColors.INTERSPORT_TURQUOISE.getRGB()));
		// 21
		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN_STREET, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_LEFT));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.MANAGER_NAME, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_MIDDLE));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.VR_BANC_IBAN, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_RIGHT));

		footer.addCell(emptySub);

		// 31
		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN_CITY, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_LEFT));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.MANAGER_INFO1, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_MIDDLE));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.VR_BANC_BIC, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_RIGHT));
		footer.addCell(emptySub);

		// 41
		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN_PHONE, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_LEFT));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.MANAGER_INFO2, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_MIDDLE));

		footer.addCell(emptySub);
		footer.addCell(emptySub);

		// end fourth row
		// 51
		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN_FAX, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_LEFT));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.USTD_ID1, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_MIDDLE));

		FooterCellLeftPadding cell53 = new FooterCellLeftPadding(
				new Phrase(PdfConstants.BANC_COBURG_LICHTENFELS, Fonts.footerText), 1, Element.ALIGN_CENTER,
				PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_HEADER_RIGHT);
		footer.addCell(cell53);
		// was removed
		footer.addCell(emptySub);

		// 61
		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN_INFO_MAIL, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_LEFT));

		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.USTD_ID2, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_MIDDLE));

		FooterCellLeftPadding cell63 = new FooterCellLeftPadding(
				new Phrase(PdfConstants.BANC_COBURG_LICHTENFELS_IBAN, Fonts.footerText1), 1, Element.ALIGN_BOTTOM,
				PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_RIGHT);

		footer.addCell(cell63);

		footer.addCell(emptySub);

		// 71
		footer.addCell(new FooterCellLeftPadding(new Phrase(PdfConstants.WOHLLEBEN_HOMEPAGE, Fonts.footerText1), 1,
				Element.ALIGN_CENTER, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_LEFT));

		footer.addCell(emptySub);
		footer.addCell(
				new FooterCellLeftPadding(new Phrase(PdfConstants.BANC_COBURG_LICHTENFELS_BIC, Fonts.footerText1), 1,
						Element.ALIGN_BOTTOM, PdfConstants.HEIGHT_OF_FOOTER_CELL, PdfConstants.FOOTER_PADDING_RIGHT));
		footer.addCell(emptySub);

		// 81
		footer.addCell(emptySub);
		footer.addCell(emptySub);
		footer.addCell(emptySub);

		footer.addCell(emptySub);

		footer.writeSelectedRows(0, -1, PdfConstants.START_OF_TABLES, footer.getTotalHeight(),
				writer.getDirectContent());

	}

}
