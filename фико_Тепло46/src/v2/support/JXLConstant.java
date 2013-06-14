package v2.support;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class JXLConstant {

    public WritableCellFormat tahoma9ptBoldMedion = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));

    public WritableCellFormat tahoma14ptBold = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 14, WritableFont.BOLD));

    public WritableCellFormat tahomaLabelToplivo = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));

    public WritableCellFormat tahomaLabelTitle = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));

    public WritableCellFormat tahomaValue = new WritableCellFormat(
	    NumberFormats.FORMAT5);
    
    public WritableCellFormat tahomaValue_w = new WritableCellFormat(
	    NumberFormats.FLOAT);
    
    public WritableCellFormat tahomaValue_green = new WritableCellFormat(
	    NumberFormats.FORMAT5);

    public WritableCellFormat tahomaValue_red = new WritableCellFormat(
	    NumberFormats.FLOAT);

    public WritableCellFormat tahomaValue_blue = new WritableCellFormat(
	    NumberFormats.FLOAT);

    public WritableCellFormat tahomaValue_white = new WritableCellFormat(
	    NumberFormats.FLOAT);

    public WritableCellFormat tahomaValuePer = new WritableCellFormat(
	    NumberFormats.PERCENT_FLOAT);

    public WritableCellFormat tahomaMonth = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
    
    public WritableCellFormat tahomaTitle = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
    
    public WritableCellFormat tahomaOrg = new WritableCellFormat(
	    new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));

    public JXLConstant() throws WriteException {
	/*
	 * Основной формат ячеек Tahoma 9pt, no bold выравнивание по
	 * горизонтале: центр выравнивание по вертикале: центр перенос по словам
	 * стиль границы - все цвет фона - без цвета
	 */
	tahoma9ptBoldMedion.setAlignment(Alignment.CENTRE);
	tahoma9ptBoldMedion.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahoma9ptBoldMedion.setWrap(true);
	tahoma9ptBoldMedion.setBorder(Border.ALL, BorderLineStyle.THIN);

	tahomaValue.setAlignment(Alignment.CENTRE);
	tahomaValue.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValue.setWrap(true);
	tahomaValue.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaValue.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD));
	
	tahomaValue_w.setAlignment(Alignment.CENTRE);
	tahomaValue_w.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValue_w.setWrap(true);
	tahomaValue_w.setBorder(Border.NONE, BorderLineStyle.THIN);
	tahomaValue_w.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD));
	
	tahomaValue_green.setAlignment(Alignment.CENTRE);
	tahomaValue_green.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValue_green.setWrap(true);
	tahomaValue_green.setBackground(Colour.LIGHT_GREEN);
	tahomaValue_green.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaValue_green.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD));

	tahomaValue_red.setAlignment(Alignment.CENTRE);
	tahomaValue_red.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValue_red.setWrap(true);
	tahomaValue_red.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaValue_red.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD));
	// tahomaValue_red.setFont(new WritableFont(WritableFont.TAHOMA, 9,
	// WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED));

	tahomaValue_blue.setAlignment(Alignment.CENTRE);
	tahomaValue_blue.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValue_blue.setWrap(true);
	tahomaValue_blue.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaValue_blue.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD));
	// tahomaValue_blue.setFont(new WritableFont(WritableFont.TAHOMA, 9,
	// WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLUE));

	tahomaValue_white.setAlignment(Alignment.CENTRE);
	tahomaValue_white.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValue_white.setWrap(true);
	tahomaValue_white.setBorder(Border.NONE, BorderLineStyle.THIN);
	tahomaValue_white.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,
		Colour.WHITE));

	tahomaLabelTitle.setAlignment(Alignment.CENTRE);
	tahomaLabelTitle.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaLabelTitle.setWrap(true);
	tahomaLabelTitle.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaLabelTitle.setOrientation(Orientation.PLUS_90);

	tahomaValuePer.setAlignment(Alignment.CENTRE);
	tahomaValuePer.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaValuePer.setWrap(true);
	tahomaValuePer.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaValuePer.setFont(new WritableFont(WritableFont.TAHOMA, 9,
		WritableFont.NO_BOLD));

	tahomaLabelToplivo.setAlignment(Alignment.LEFT);
	tahomaLabelToplivo.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahomaLabelToplivo.setWrap(true);
	tahomaLabelToplivo.setBorder(Border.ALL, BorderLineStyle.THIN);

	tahoma14ptBold.setAlignment(Alignment.CENTRE);
	tahoma14ptBold.setVerticalAlignment(VerticalAlignment.CENTRE);
	tahoma14ptBold.setWrap(true);
	tahoma14ptBold.setBorder(Border.NONE, BorderLineStyle.THIN);

	tahomaMonth.setAlignment(Alignment.CENTRE);
	tahomaMonth.setWrap(true);
	tahomaMonth.setBackground(Colour.GRAY_25);
	tahomaMonth.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaMonth.setVerticalAlignment(VerticalAlignment.CENTRE);

	tahomaOrg.setAlignment(Alignment.LEFT);
	tahomaOrg.setWrap(true);
	tahomaOrg.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaOrg.setVerticalAlignment(VerticalAlignment.CENTRE);
	
	tahomaTitle.setAlignment(Alignment.LEFT);
	tahomaTitle.setWrap(true);
	tahomaTitle.setBackground(Colour.GRAY_25);
	tahomaTitle.setBorder(Border.ALL, BorderLineStyle.THIN);
	tahomaTitle.setVerticalAlignment(VerticalAlignment.CENTRE);
    }

}
