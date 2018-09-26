package pdf;

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

public class CustomerCell extends PdfPCell {

	private BaseColor baseColor;
	private Boolean useBorder = Boolean.FALSE;

	public CustomerCell(Color color, Phrase phrase, float minHeight, int alignVertical) {

		this.baseColor = new BaseColor(color.getRGB());
		this.setPhrase(phrase);
		this.setFixedHeight(minHeight);
		this.setVerticalAlignment(alignVertical);
		this.setFixedHeight(12.0f);

		initCell();

	}

	public CustomerCell(Color color, Phrase phrase, int minHeight, int alignVertical, Boolean useBorder) {
		this.useBorder = useBorder;
		this.baseColor = new BaseColor(color.getRGB());
		this.setPhrase(phrase);
		this.setFixedHeight(minHeight);
		this.setVerticalAlignment(alignVertical);
		this.setFixedHeight(12.0f);

		initCell();

	}

	private void initCell() {

		this.setBackgroundColor(baseColor);
		this.setPadding(0.0f);

		if (!useBorder) {
			this.setBorder(Rectangle.NO_BORDER);
			this.setUseVariableBorders(Boolean.TRUE);
			this.setUseAscender(Boolean.TRUE);
			this.setUseDescender(Boolean.TRUE);
		}

	}

}
