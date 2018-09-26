package constants;

import java.awt.Font;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;

import pdf.PdfConstants;

public class Fonts {

	public static final Font timesRoman = new Font("TimesRoman", Font.PLAIN, 12);
	public static final Font helvetica = new Font("Helvetica", Font.BOLD, 11);
	public static final Font italic = new Font("TimesRoman", Font.ITALIC, 12);

	public static com.itextpdf.text.Font fontSubGrayDark = FontFactory.getFont(PdfConstants.ROBOTO_BOLD,
			PdfConstants.FONTSIZE_BODY2, new BaseColor(IntersportColors.INTERSPORT_GRAY_DARK.getRGB()));
	public static com.itextpdf.text.Font fontSubBlack = FontFactory.getFont(PdfConstants.ROBOTO_BOLD,
			Constants.FONTSIZE_SUB, new BaseColor(IntersportColors.INTERSPORT_BLACK.getRGB()));

	public static com.itextpdf.text.Font fontFooterWhite = FontFactory.getFont("Helvetica", Constants.FONTSIZE_FOOTER,
			Font.BOLD, new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));

	public static com.itextpdf.text.Font bodyText = FontFactory.getFont(PdfConstants.ROBOTO_REGULAR,
			PdfConstants.FONTSIZE_BODY, new BaseColor(IntersportColors.INTERSPORT_BLACK.getRGB()));

	public static com.itextpdf.text.Font bodyText2 = FontFactory.getFont(PdfConstants.ROBOTO_REGULAR,
			PdfConstants.FONTSIZE_BODY2, new BaseColor(IntersportColors.INTERSPORT_BLACK.getRGB()));

	public static com.itextpdf.text.Font bodyText2Coral = FontFactory.getFont(PdfConstants.ROBOTO_REGULAR,
			PdfConstants.FONTSIZE_BODY2, new BaseColor(IntersportColors.INTERSPORT_CORAL.getRGB()));

	public static com.itextpdf.text.Font footerText = FontFactory.getFont(PdfConstants.ROBOTO_BOLD_ITALIC,
			PdfConstants.FONTSIZE_FOOTER, new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));

	public static com.itextpdf.text.Font footerText1 = FontFactory.getFont(PdfConstants.ROBOTO_BOLD_ITALIC,
			PdfConstants.FONTSIZE_FOOTER1, new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));

}
