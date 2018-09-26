package pdf;

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import constants.IntersportColors;

/** the class SellPositionCell */
public class SellPositionCell extends PdfPCell {

	private BaseColor baseColor;

	public SellPositionCell(Color color, Phrase phrase) {

		this.baseColor = new BaseColor(color.getRGB());
		this.setPhrase(phrase);

		initCell();

	}

	public SellPositionCell(Color color, Phrase phrase, int colspan) {
		this(color, phrase);
		this.setColspan(colspan);
	}

	private void initCell() {
		this.setMinimumHeight(18);
		this.setBackgroundColor(baseColor);

		this.setUseVariableBorders(Boolean.TRUE);
		this.setUseBorderPadding(Boolean.TRUE);
		this.setUseAscender(Boolean.TRUE);

		this.setBorderWidthLeft(0.25f);
		this.setBorderWidthRight(0.25f);

		this.setBorderWidthTop(0.5f);
		this.setBorderWidthBottom(0.5f);

		this.setBorderColorLeft(new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));
		this.setBorderColorRight(new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));

		this.setBorderColorTop(new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));
		this.setBorderColorBottom(new BaseColor(IntersportColors.INTERSPORT_WHITE.getRGB()));

		this.setVerticalAlignment(Element.ALIGN_MIDDLE);

	}

}
