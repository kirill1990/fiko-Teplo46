package base;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class OutputTableV2 {

    private static WritableWorkbook workbook;
    private static WritableSheet sheet_par;
    private static WritableSheet sheet_gor;
    private String year = "";

    WritableFont tahoma9ptBold = new WritableFont(WritableFont.TAHOMA, 9,
	    WritableFont.NO_BOLD);
    WritableCellFormat cellFormat = new WritableCellFormat(tahoma9ptBold);

    public OutputTableV2(String year, String pathfile) {
	// Получаем данные из бд
	Vector<Vector<String>> para = new ConnectionBD()
		.getOutputTableV2_par(year);
	Vector<Vector<String>> gor = new ConnectionBD()
		.getOutputTableV2_gor(year);

	this.year = year;

	// задаём настройки книги
	WorkbookSettings ws = new WorkbookSettings();
	ws.setLocale(new Locale("ru", "RU"));

	try {
	    // создание книги
	    workbook = Workbook.createWorkbook(new File(pathfile), ws);

	    // создание листа
	    sheet_par = workbook.createSheet("Отпуск ТЭ в паре", 0);
	    sheet_gor = workbook.createSheet("Отпуск ТЭ в гор воде", 1);

	    // основной шрифт, используется в шапке

	    // установка шрифта

	    // выравнивание по центру
	    cellFormat.setAlignment(Alignment.CENTRE);
	    // перенос по словам если не помещается
	    cellFormat.setWrap(true);
	    // установить цвет
	    cellFormat.setBackground(Colour.GRAY_25);
	    // рисуем рамку
	    cellFormat.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	    cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

	    // Заполнение шапки
	    shapka(sheet_par);
	    shapka(sheet_gor);

	    sheet_par.addCell(new Label(0, 0, "Полезный отпуск ТЭ в паре.",
		    cellFormat));

	    sheet_gor.addCell(new Label(0, 0,
		    "Полезный отпуск ТЭ в горячей воде.", cellFormat));

	    // x-вправо y-вниз

	    // заполняем данные
	    // список районов Калужской области
	    String[] districts = {
		    "Бабынинский муниципальный район",
		    "Барятинский муниципальный район",
		    "Боровский муниципальный район",
		    "Город Калуга",
		    "Город Обнинск",
		    "Дзержинский муниципальный район",
		    "Думиничский муниципальный район",
		    "Жиздринский муниципальный район",
		    "Жуковский муниципальный район",
		    "Износковский муниципальный район",
		    "Кировский муниципальный район",
		    "Козельский муниципальный район",
		    "Куйбышевский муниципальный район",
		    "Людиновский муниципальный район",
		    "Малоярославецкий муниципальный район",
		    "Медынский муниципальный район",
		    "Мещовский муниципальный район",
		    "Мосальский муниципальный район",
		    "Перемышльский муниципальный район",
		    "Спас-Деменский муниципальный район",
		    "Сухиничский муниципальный район",
		    "Тарусский муниципальный район",
		    "Ульяновский муниципальный район",
		    "Ферзиковский муниципальный район",
		    "Хвастовичский муниципальный район",
		    "Юхновский муниципальный район" };

	    // дополнительные шрифты, разница в цвете и выравнивание
	    WritableCellFormat cellFormat2 = new WritableCellFormat(
		    tahoma9ptBold);
	    // выравнивание по центру
	    cellFormat2.setAlignment(Alignment.LEFT);
	    // перенос по словам если не помещается
	    cellFormat2.setWrap(true);
	    // установить цвет
	    cellFormat2.setBackground(Colour.GRAY_25);
	    // рисуем рамку
	    cellFormat2.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	    cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);

	    WritableCellFormat cellFormat3 = new WritableCellFormat(
		    tahoma9ptBold);
	    // выравнивание по центру
	    cellFormat3.setAlignment(Alignment.CENTRE);
	    // перенос по словам если не помещается
	    cellFormat3.setWrap(true);
	    // установить цвет
	    cellFormat3.setBackground(Colour.LIGHT_GREEN);
	    // рисуем рамку
	    cellFormat3.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	    cellFormat3.setVerticalAlignment(VerticalAlignment.CENTRE);

	    WritableCellFormat cellFormat4 = new WritableCellFormat(
		    tahoma9ptBold);
	    // выравнивание по центру
	    cellFormat4.setAlignment(Alignment.LEFT);
	    // перенос по словам если не помещается
	    cellFormat4.setWrap(true);
	    // установить цвет
	    // cellFormat4.setBackground(Colour.GRAY_25);
	    // рисуем рамку
	    cellFormat4.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	    cellFormat4.setVerticalAlignment(VerticalAlignment.CENTRE);

	    WritableCellFormat cellFormat5 = new WritableCellFormat(
		    tahoma9ptBold);
	    // выравнивание по центру
	    cellFormat5.setAlignment(Alignment.CENTRE);
	    // перенос по словам если не помещается
	    cellFormat5.setWrap(true);
	    // установить цвет
	    cellFormat5.setBackground(Colour.LIGHT_ORANGE);
	    // рисуем рамку
	    cellFormat5.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
	    cellFormat5.setVerticalAlignment(VerticalAlignment.CENTRE);

	    // запись инф в ексель
	    for (int i = 0; i < para.size(); i++) {
		// высота строки
		sheet_par.setRowView(3 + i, 500);

		// запись +
		for (int p = 1; p < para.get(i).size() - 2; p = p + 2) {
		    // не тру((ячейка белая)
		    sheet_par.addCell(new Label(p, 3 + i, para.get(i).get(p)
			    .toString(), cellFormat4));
		    sheet_par.addCell(new Label(p + 1, 3 + i, para.get(i)
			    .get(p + 1).toString(), cellFormat3));
		}

		{
		    int p = para.get(i).size() - 2;

		    Double itog = GetCoor(para.get(i).get(p).toString());
		    Double narast = GetCoor(para.get(i).get(p + 1).toString());

		    if (itog.equals(narast) != true) {
			sheet_par.addCell(new Label(p, 3 + i, Double
				.toString(itog), cellFormat5));
			sheet_par.addCell(new Label(p + 1, 3 + i, Double
				.toString(narast), cellFormat5));
		    } else {
			sheet_par.addCell(new Label(p, 3 + i, Double
				.toString(itog), cellFormat4));
			sheet_par.addCell(new Label(p + 1, 3 + i, Double
				.toString(narast), cellFormat3));
		    }
		}

		Label l1 = null;

		// добавление организаций
		l1 = new Label(0, 3 + i, para.get(i).get(0).toString(),
			cellFormat4);

		for (int p = 0; p < districts.length; p++) {
		    // поиск района
		    if (districts[p].equals(para.get(i).get(0))) {
			// это строка запись о районе
			// записываем строку в серый цвет
			for (int z = 1; z < para.get(i).size(); z++) {
			    sheet_par.addCell(new Label(z, 3 + i, "",
				    cellFormat2));
			}
			l1 = new Label(0, 3 + i, para.get(i).get(0).toString(),
				cellFormat2);
			break;
		    }
		}
		sheet_par.addCell(l1);
	    }

	    // запись инф в ексель
	    for (int i = 0; i < gor.size(); i++) {
		// высота строки
		sheet_gor.setRowView(3 + i, 500);

		// запись +
		for (int p = 1; p < gor.get(i).size() - 2; p = p + 2) {
		    // не тру((ячейка белая)
		    sheet_gor.addCell(new Label(p, 3 + i, gor.get(i).get(p)
			    .toString(), cellFormat4));
		    sheet_gor.addCell(new Label(p + 1, 3 + i, gor.get(i)
			    .get(p + 1).toString(), cellFormat3));
		}

		{
		    int p = gor.get(i).size() - 2;

		    Double itog = GetCoor(gor.get(i).get(p).toString());
		    Double narast = GetCoor(gor.get(i).get(p + 1).toString());

		    if (itog.equals(narast) != true) {
			sheet_gor.addCell(new Label(p, 3 + i, Double
				.toString(itog), cellFormat5));
			sheet_gor.addCell(new Label(p + 1, 3 + i, Double
				.toString(narast), cellFormat5));
		    } else {
			sheet_gor.addCell(new Label(p, 3 + i, Double
				.toString(itog), cellFormat4));
			sheet_gor.addCell(new Label(p + 1, 3 + i, Double
				.toString(narast), cellFormat3));
		    }
		}

		Label l1 = null;

		// добавление организаций
		l1 = new Label(0, 3 + i, gor.get(i).get(0).toString(),
			cellFormat4);

		for (int p = 0; p < districts.length; p++) {
		    // поиск района
		    if (districts[p].equals(gor.get(i).get(0))) {
			// это строка запись о районе
			// записываем строку в серый цвет
			for (int z = 1; z < gor.get(i).size(); z++) {
			    sheet_gor.addCell(new Label(z, 3 + i, "",
				    cellFormat2));
			}
			l1 = new Label(0, 3 + i, gor.get(i).get(0).toString(),
				cellFormat2);
			break;
		    }
		}
		sheet_gor.addCell(l1);
	    }
	    // закрываем книгу
	    workbook.write();
	    workbook.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (WriteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private WritableSheet shapka(WritableSheet sheet_par) {
	try {
	    sheet_par.addCell(new Label(1, 0, year, cellFormat));
	    sheet_par.mergeCells(1, 0, 26, 0);

	    sheet_par
		    .addCell(new Label(
			    0,
			    1,
			    "Организация (всего за мес./ всего с нарастающим итогом в Тыс. Гкал)",
			    cellFormat));
	    sheet_par.mergeCells(0, 1, 0, 2);

	    sheet_par.addCell(new Label(1, 1, "Январь", cellFormat));
	    sheet_par.mergeCells(1, 1, 2, 1);

	    sheet_par.addCell(new Label(3, 1, "Февраль", cellFormat));
	    sheet_par.mergeCells(3, 1, 4, 1);

	    sheet_par.addCell(new Label(5, 1, "Март", cellFormat));
	    sheet_par.mergeCells(5, 1, 6, 1);

	    sheet_par.addCell(new Label(7, 1, "Апрель", cellFormat));
	    sheet_par.mergeCells(7, 1, 8, 1);

	    sheet_par.addCell(new Label(9, 1, "Май", cellFormat));
	    sheet_par.mergeCells(9, 1, 10, 1);

	    sheet_par.addCell(new Label(11, 1, "Июнь", cellFormat));
	    sheet_par.mergeCells(11, 1, 12, 1);

	    sheet_par.addCell(new Label(13, 1, "Июль", cellFormat));
	    sheet_par.mergeCells(13, 1, 14, 1);

	    sheet_par.addCell(new Label(15, 1, "Август", cellFormat));
	    sheet_par.mergeCells(15, 1, 16, 1);

	    sheet_par.addCell(new Label(17, 1, "Сентябрь", cellFormat));
	    sheet_par.mergeCells(17, 1, 18, 1);

	    sheet_par.addCell(new Label(19, 1, "Октябрь", cellFormat));
	    sheet_par.mergeCells(19, 1, 20, 1);

	    sheet_par.addCell(new Label(21, 1, "Ноябрь", cellFormat));
	    sheet_par.mergeCells(21, 1, 22, 1);

	    sheet_par.addCell(new Label(23, 1, "Декабрь", cellFormat));
	    sheet_par.mergeCells(23, 1, 24, 1);

	    sheet_par.addCell(new Label(25, 1, "Год", cellFormat));
	    sheet_par.mergeCells(25, 1, 26, 1);

	    sheet_par.addCell(new Label(25, 2, "За год", cellFormat));
	    sheet_par.addCell(new Label(26, 2, "С нарастающим итогом",
		    cellFormat));

	    for (int i = 0; i < 12; i++) {
		sheet_par.addCell(new Label(1 + i * 2, 2, "за месяц",
			cellFormat));
		sheet_par.addCell(new Label(2 + i * 2, 2,
			"С нарастающим итогом", cellFormat));
	    }

	    // задаём ширину первой колонки
	    sheet_par.setColumnView(0, 65);
	    // sheet_par.setColumnView(1, 65);
	    // sheet_par.setColumnView(2, 65);

	    // задаём высоту строк шапки
	    sheet_par.setRowView(0, 500);
	    sheet_par.setRowView(1, 500);
	    sheet_par.setRowView(2, 1000);
	} catch (RowsExceededException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (WriteException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return sheet_par;
    }

    private Double GetCoor(String value) {
	// проверяем на наличие информации в ячейке
	if (value != null) {
	    // если что-то есть, возвращаем строковок представление
	    value = value.replace(" ", "");
	    value = value.replace(",", ".");
	    if (value.equals("") != true) {
		return Double.parseDouble(value);
	    }
	}
	// если она пустая, то возвращаем пустую строку
	return Double.parseDouble("0");
    }
}
