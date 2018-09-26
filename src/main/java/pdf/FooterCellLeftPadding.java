package pdf;

import com.itextpdf.text.Phrase;

import constants.IntersportColors;

public class FooterCellLeftPadding extends CustomerCell {

	public FooterCellLeftPadding(Phrase phrase, float minHeight, int alignVertical, float fixedHeight, float padding) {
		super(IntersportColors.INTERSPORT_TURQUOISE, phrase, minHeight, alignVertical);
		this.setFixedHeight(fixedHeight);
		this.setPaddingLeft(padding);
	}

}
