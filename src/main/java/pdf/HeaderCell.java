package pdf;

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;

/** the class HeaderCell */
public class HeaderCell extends PdfPCell {

	private int horizontalAlignMent;

	HeaderCell(Color color, Phrase phrase, int horizontalAlignMent) {
		this.setBackgroundColor(new BaseColor(color.getRGB()));
		this.horizontalAlignMent = horizontalAlignMent;
		this.setPhrase(phrase);

		initCell();
	}

	private void initCell() {

		this.setUseAscender(Boolean.TRUE);

		this.setBorder(Rectangle.NO_BORDER);
		this.setPadding(3f);
		this.setUseBorderPadding(Boolean.TRUE);
		this.setHorizontalAlignment(horizontalAlignMent);
		this.setVerticalAlignment(Element.ALIGN_BOTTOM);

		this.setMinimumHeight(15);

	}

}
