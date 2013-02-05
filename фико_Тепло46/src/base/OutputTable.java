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

public class OutputTable
{

	public OutputTable(Vector<Integer> ids, String pathfile)
	{
		WritableWorkbook workbook = null;

		CollectionStrings collectGOR = new ConnectionBD().getGOR(ids);
		CollectionStrings collectPARA = new ConnectionBD().getPARA(ids);

		try
		{
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("ru", "RU"));

			try
			{
				// имя и путь файла
				// создание директории
				if (pathfile.lastIndexOf("/") > 0)
				{
					String dir = pathfile.substring(0, pathfile.lastIndexOf("/"));
					new File(dir).mkdirs();
				}

				workbook = Workbook.createWorkbook(new File(pathfile), ws);

				forCollect(collectPARA, workbook.createSheet("Отпуск ТЭ в паре", 0));
				forCollect(collectGOR, workbook.createSheet("Отпуск ТЭ в гор воде", 1));
			}
			catch (IOException e)
			{
				// TODO Автоматически созданный блок catch
				e.printStackTrace();
			}

			try
			{
				workbook.write();
				workbook.close();
			}
			catch (IOException e)
			{
				// TODO Автоматически созданный блок catch
				e.printStackTrace();
			}
			catch (WriteException e)
			{
				// TODO Автоматически созданный блок catch
				e.printStackTrace();
			}
		}
		catch (WriteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void forCollect(CollectionStrings collect, WritableSheet sheet) throws WriteException
	{
		/*
		 * Основной формат ячеек
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: центр
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы - все
		 * цвет фона - без цвета
		 */
		WritableCellFormat tahoma9pt = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
		tahoma9pt.setAlignment(Alignment.CENTRE);
		tahoma9pt.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9pt.setWrap(true);
		tahoma9pt.setBorder(Border.ALL, BorderLineStyle.MEDIUM);

		/*
		 * формат ячеек зелёного цвета
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: по правому краю
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы - все
		 * цвет фона - легкий зелёный
		 */
		WritableCellFormat tahoma9ptGreen = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
		tahoma9ptGreen.setAlignment(Alignment.RIGHT);
		tahoma9ptGreen.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptGreen.setWrap(true);
		tahoma9ptGreen.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		tahoma9ptGreen.setBackground(Colour.LIGHT_GREEN);

		/*
		 * формат ячеек жёлтого цвета
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: по правому краю
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы - все
		 * цвет фона - легкий жёлтый
		 */
		WritableCellFormat tahoma9ptYellow = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
		tahoma9ptYellow.setAlignment(Alignment.RIGHT);
		tahoma9ptYellow.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptYellow.setWrap(true);
		tahoma9ptYellow.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		tahoma9ptYellow.setBackground(Colour.VERY_LIGHT_YELLOW);

		/*
		 * дополнительный формат ячеек
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: центр
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы - все
		 * цвет фона - 25% серого
		 */
		WritableCellFormat tahoma9ptBoldGray = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));
		tahoma9ptBoldGray.setAlignment(Alignment.CENTRE);
		tahoma9ptBoldGray.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptBoldGray.setWrap(true);
		tahoma9ptBoldGray.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		tahoma9ptBoldGray.setBackground(Colour.GRAY_25);

		/*
		 * Основной жирный
		 * 
		 * Tahoma 9pt, bold
		 * выравнивание по горизонтале: центр
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы: все
		 * цвет фона: без цвета
		 */
		WritableCellFormat tahoma9ptBold = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));
		tahoma9ptBold.setAlignment(Alignment.CENTRE);
		tahoma9ptBold.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptBold.setWrap(true);
		tahoma9ptBold.setBorder(Border.ALL, BorderLineStyle.MEDIUM);

		/*
		 * Основной жирный без рамки
		 * 
		 * Tahoma 9pt, bold
		 * выравнивание по горизонтале: центр
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы: все
		 * цвет фона: без цвета
		 */
		WritableCellFormat tahome9ptBoldNoBorder = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));
		tahome9ptBoldNoBorder.setAlignment(Alignment.CENTRE);
		tahome9ptBoldNoBorder.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahome9ptBoldNoBorder.setWrap(true);
		tahome9ptBoldNoBorder.setBorder(null, null);

		/*
		 * Основной жирный c серым оттенком, по левому краю
		 * 
		 * Tahoma 9pt, bold
		 * выравнивание по горизонтале: по левому краю
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы: все
		 * цвет фона: 25% серого
		 */
		WritableCellFormat tahoma9ptLeftBoldGray = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));
		tahoma9ptLeftBoldGray.setAlignment(Alignment.LEFT);
		tahoma9ptLeftBoldGray.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptLeftBoldGray.setWrap(true);
		tahoma9ptLeftBoldGray.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		tahoma9ptLeftBoldGray.setBackground(Colour.GRAY_25);

		/*
		 * Основной с выравниванием по левому краю
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: по левому краю
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы: все
		 * цвет фона: без цвета
		 */
		WritableCellFormat tahoma9ptLeft = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
		tahoma9ptLeft.setAlignment(Alignment.LEFT);
		tahoma9ptLeft.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptLeft.setWrap(true);
		tahoma9ptLeft.setBorder(Border.ALL, BorderLineStyle.MEDIUM);

		/*
		 * Основной с выравниванием по левому краю без рамки
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: по левому краю
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы: без рамки
		 * цвет фона: без цвета
		 */
		WritableCellFormat tahoma9ptLeftNoBold = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD));
		tahoma9ptLeftNoBold.setAlignment(Alignment.LEFT);
		tahoma9ptLeftNoBold.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptLeftNoBold.setWrap(true);
		tahoma9ptLeftNoBold.setBorder(null, null);

		/*
		 * Основной жирный зелёного цвета с выравниванием по левому
		 * 
		 * Tahoma 9pt, no bold
		 * выравнивание по горизонтале: по левому краю
		 * выравнивание по вертикале: центр
		 * перенос по словам
		 * стиль границы: без рамки
		 * цвет фона: зёлный
		 */
		WritableCellFormat tahoma9ptBoldLeftGreen = new WritableCellFormat(new WritableFont(WritableFont.TAHOMA, 9, WritableFont.BOLD));
		tahoma9ptBoldLeftGreen.setAlignment(Alignment.LEFT);
		tahoma9ptBoldLeftGreen.setVerticalAlignment(VerticalAlignment.CENTRE);
		tahoma9ptBoldLeftGreen.setWrap(true);
		tahoma9ptBoldLeftGreen.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
		tahoma9ptBoldLeftGreen.setBackground(Colour.LIGHT_GREEN);

		/*
		 * шапка
		 */
		sheet.addCell(new Label(0, 6, "Наименование", tahoma9ptBold));
		sheet.addCell(new Label(1, 6, "Код строки", tahoma9ptBold));
		sheet.addCell(new Label(2, 6, "Объем отпуска тепловой энергии за отчетный месяц (год), тыс. Гкал", tahoma9ptBold));
		sheet.addCell(new Label(5, 6, "Стоимость отпущенной тепловой энергии за отчетный месяц (год), тыс. руб.", tahoma9ptBold));
		sheet.addCell(new Label(2, 7, "Всего", tahoma9ptBold));
		sheet.addCell(new Label(3, 7, "в том числе", tahoma9ptBold));
		sheet.addCell(new Label(3, 8, "по приборам учета", tahoma9ptBold));
		sheet.addCell(new Label(4, 8, "по приборам учета", tahoma9ptBold));
		sheet.addCell(new Label(5, 7, "Всего", tahoma9ptBold));
		sheet.addCell(new Label(6, 7, "в том числе", tahoma9ptBold));
		sheet.addCell(new Label(6, 8, "по приборам учета", tahoma9ptBold));
		sheet.addCell(new Label(7, 8, "по приборам учета", tahoma9ptBold));

		// порядковый номер столбца
		for (int i = 0; i < 8; i++)
		{
			sheet.addCell(new Label(i, 9, Integer.toString(i + 1), tahome9ptBoldNoBorder));
		}

		// промежуточный
		sheet.addCell(new Label(0, 10, "Отпуск тепловой энергии потребителям с коллекторов электростанций (котельных)", tahoma9ptLeftBoldGray));
		sheet.addCell(new Label(0, 25, "Отпуск произведенной (приобретенной) тепловой энергии потребителям через тепловую сеть", tahoma9ptLeftBoldGray));

		// наименование
		sheet.addCell(new Label(0, 11, "Промышленные и приравненные к ним потребители", tahoma9ptLeft));
		sheet.addCell(new Label(0, 12, "Население и исполнители коммунальных услуг, всего:", tahoma9ptLeft));
		sheet.addCell(new Label(0, 13, "- население, проживающее в многоквартирных домах", tahoma9ptLeft));
		sheet.addCell(new Label(0, 14, "- население, проживающее в индивидуальных домах", tahoma9ptLeft));
		sheet.addCell(new Label(0, 15, "- на нужды отопления", tahoma9ptLeft));
		sheet.addCell(new Label(0, 16, "- на нужды горячего водоснабжения ", tahoma9ptLeft));
		sheet.addCell(new Label(0, 17, "Бюджетные организации, всего:", tahoma9ptLeft));
		sheet.addCell(new Label(0, 18, "- финансируемые за счет средств федерального бюджета Российской Федерации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 19, "- финансируемые за счет средств бюджета субъекта Российской Федерации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 20, "- финансируемые за счет средств местных бюджетов", tahoma9ptLeft));
		sheet.addCell(new Label(0, 21, "Другие теплосбытовые и теплоснабжающие организации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 22, "На компенсацию потерь тепловой энергии при ее передаче организациями, оказывающими услуги по передаче тепловой энергии", tahoma9ptLeft));
		sheet.addCell(new Label(0, 23, "Прочие потребители", tahoma9ptLeft));
		sheet.addCell(new Label(0, 24, "Собственные и производственные нужды энергоснабжающей организации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 26, "Промышленные и приравненные к ним потребители", tahoma9ptLeft));
		sheet.addCell(new Label(0, 27, "Население и исполнители коммунальных услуг, всего:", tahoma9ptLeft));
		sheet.addCell(new Label(0, 28, "- население, проживающее в многоквартирных домах", tahoma9ptLeft));
		sheet.addCell(new Label(0, 29, "- население, проживающее в многоквартирных домах", tahoma9ptLeft));
		sheet.addCell(new Label(0, 30, "- на нужды отопления", tahoma9ptLeft));
		sheet.addCell(new Label(0, 31, "- на нужды горячего водоснабжения ", tahoma9ptLeft));
		sheet.addCell(new Label(0, 32, "Бюджетные организации, всего:", tahoma9ptLeft));
		sheet.addCell(new Label(0, 33, "- финансируемые за счет средств федерального бюджета Российской Федерации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 34, "- финансируемые за счет средств бюджета субъекта Российской Федерации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 35, "- финансируемые за счет средств местных бюджетов", tahoma9ptLeft));
		sheet.addCell(new Label(0, 36, "Другие теплосбытовые и теплоснабжающие организации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 37, "На компенсацию потерь тепловой энергии при ее передаче организациями, оказывающими услуги по передаче тепловой энергии", tahoma9ptLeft));
		sheet.addCell(new Label(0, 38, "Прочие потребители", tahoma9ptLeft));
		sheet.addCell(new Label(0, 39, "Собственные и производственные нужды энергоснабжающей организации", tahoma9ptLeft));
		sheet.addCell(new Label(0, 40, "Полезный отпуск - всего", tahoma9ptLeft));

		// хз
		sheet.addCell(new Label(0, 5, "Коды по ОКЕИ: тысяча гигакалорий - 234, тысяча рублей - 384", tahoma9ptLeftNoBold));

		// информационные данные
		sheet.addCell(new Label(0, 0, "Отчетный период", tahoma9ptBoldLeftGreen));
		sheet.addCell(new Label(0, 1, "Муниципальный район/Муниципальное образование", tahoma9ptBoldLeftGreen));
		sheet.addCell(new Label(0, 2, "Наименование организаций", tahoma9ptBoldLeftGreen));

		/*
		 * ширина столбцов
		 */
		sheet.setColumnView(0, 65);
		sheet.setColumnView(1, 8);
		sheet.setColumnView(2, 16);
		sheet.setColumnView(3, 16);
		sheet.setColumnView(4, 16);
		sheet.setColumnView(5, 16);
		sheet.setColumnView(6, 16);
		sheet.setColumnView(7, 16);

		/*
		 * высота строк
		 */
		sheet.setRowView(6, 500);
		sheet.setRowView(7, 300);
		sheet.setRowView(8, 700);

		for (int i = 0; i < 6; i++)
		{
			sheet.setRowView(i, 300);
		}

		for (int i = 9; i < 41; i++)
		{
			sheet.setRowView(i, 300);
		}

		sheet.setRowView(18, 600);
		sheet.setRowView(22, 600);
		sheet.setRowView(33, 600);
		sheet.setRowView(37, 600);

		/*
		 * объединение ячеек
		 */
		// шапка
		sheet.mergeCells(0, 6, 0, 8);
		sheet.mergeCells(1, 6, 1, 8);
		sheet.mergeCells(2, 6, 4, 6);
		sheet.mergeCells(5, 6, 7, 6);
		sheet.mergeCells(2, 7, 2, 8);
		sheet.mergeCells(3, 7, 4, 7);
		sheet.mergeCells(5, 7, 5, 8);
		sheet.mergeCells(6, 7, 7, 7);
		// остальное
		sheet.mergeCells(0, 10, 7, 10);
		sheet.mergeCells(0, 25, 7, 25);
		sheet.mergeCells(0, 5, 7, 5);
		sheet.mergeCells(0, 3, 7, 3);

		/*
		 * определяем, какой из двух листов мы редактируем
		 */
		if (sheet.getName().equals("Отпуск ТЭ в паре"))
		{
			/*
			 * лист: 0
			 * title: "Отпуск ТЭ в паре"
			 */

			sheet.addCell(new Label(0, 3, "Полезный отпуск теплоэнергии в паре", tahoma9ptBoldGray));

			sheet.addCell(new Label(1, 11, "110", tahoma9pt));
			sheet.addCell(new Label(1, 12, "120", tahoma9pt));
			sheet.addCell(new Label(1, 13, "121", tahoma9pt));
			sheet.addCell(new Label(1, 14, "122", tahoma9pt));
			sheet.addCell(new Label(1, 15, "123", tahoma9pt));
			sheet.addCell(new Label(1, 16, "124", tahoma9pt));
			sheet.addCell(new Label(1, 17, "130", tahoma9pt));
			sheet.addCell(new Label(1, 18, "131", tahoma9pt));
			sheet.addCell(new Label(1, 19, "132", tahoma9pt));
			sheet.addCell(new Label(1, 20, "133", tahoma9pt));
			sheet.addCell(new Label(1, 21, "140", tahoma9pt));
			sheet.addCell(new Label(1, 22, "150", tahoma9pt));
			sheet.addCell(new Label(1, 23, "160", tahoma9pt));
			sheet.addCell(new Label(1, 24, "170", tahoma9pt));

			sheet.addCell(new Label(1, 26, "180", tahoma9pt));
			sheet.addCell(new Label(1, 27, "190", tahoma9pt));
			sheet.addCell(new Label(1, 28, "191", tahoma9pt));
			sheet.addCell(new Label(1, 29, "192", tahoma9pt));
			sheet.addCell(new Label(1, 30, "193", tahoma9pt));
			sheet.addCell(new Label(1, 31, "194", tahoma9pt));
			sheet.addCell(new Label(1, 32, "200", tahoma9pt));
			sheet.addCell(new Label(1, 33, "201", tahoma9pt));
			sheet.addCell(new Label(1, 34, "202", tahoma9pt));
			sheet.addCell(new Label(1, 35, "203", tahoma9pt));
			sheet.addCell(new Label(1, 36, "210", tahoma9pt));
			sheet.addCell(new Label(1, 37, "220", tahoma9pt));
			sheet.addCell(new Label(1, 38, "230", tahoma9pt));
			sheet.addCell(new Label(1, 39, "240", tahoma9pt));
			sheet.addCell(new Label(1, 40, "250", tahoma9pt));
		}
		else
		{
			/*
			 * лист: 1
			 * title: "Отпуск ТЭ в гор воде"
			 */
			sheet.addCell(new Label(0, 3, "Полезный отпуск теплоэнергии в горячей воде", tahoma9ptBoldGray));

			sheet.addCell(new Label(1, 11, "310", tahoma9pt));
			sheet.addCell(new Label(1, 12, "320", tahoma9pt));
			sheet.addCell(new Label(1, 13, "321", tahoma9pt));
			sheet.addCell(new Label(1, 14, "322", tahoma9pt));
			sheet.addCell(new Label(1, 15, "323", tahoma9pt));
			sheet.addCell(new Label(1, 16, "324", tahoma9pt));
			sheet.addCell(new Label(1, 17, "330", tahoma9pt));
			sheet.addCell(new Label(1, 18, "331", tahoma9pt));
			sheet.addCell(new Label(1, 19, "332", tahoma9pt));
			sheet.addCell(new Label(1, 20, "333", tahoma9pt));
			sheet.addCell(new Label(1, 21, "340", tahoma9pt));
			sheet.addCell(new Label(1, 22, "350", tahoma9pt));
			sheet.addCell(new Label(1, 23, "360", tahoma9pt));
			sheet.addCell(new Label(1, 24, "370", tahoma9pt));

			sheet.addCell(new Label(1, 26, "380", tahoma9pt));
			sheet.addCell(new Label(1, 27, "390", tahoma9pt));
			sheet.addCell(new Label(1, 28, "391", tahoma9pt));
			sheet.addCell(new Label(1, 29, "392", tahoma9pt));
			sheet.addCell(new Label(1, 30, "393", tahoma9pt));
			sheet.addCell(new Label(1, 31, "394", tahoma9pt));
			sheet.addCell(new Label(1, 32, "400", tahoma9pt));
			sheet.addCell(new Label(1, 33, "401", tahoma9pt));
			sheet.addCell(new Label(1, 34, "402", tahoma9pt));
			sheet.addCell(new Label(1, 35, "403", tahoma9pt));
			sheet.addCell(new Label(1, 36, "410", tahoma9pt));
			sheet.addCell(new Label(1, 37, "420", tahoma9pt));
			sheet.addCell(new Label(1, 38, "430", tahoma9pt));
			sheet.addCell(new Label(1, 39, "440", tahoma9pt));
			sheet.addCell(new Label(1, 40, "450", tahoma9pt));
		}

		/*
		 * Первая часть
		 */
		for (int i = 0; i < 14; i++)
		{
			collect.setIndex(i);

			sheet.addCell(new Label(2, 11 + i, collect.getValueVall(), tahoma9ptGreen));
			sheet.addCell(new Label(5, 11 + i, collect.getValueSall(), tahoma9ptGreen));

			if (i == 1 || i == 6)
			{
				sheet.addCell(new Label(3, 11 + i, collect.getValueVpribor(), tahoma9ptGreen));
				sheet.addCell(new Label(4, 11 + i, collect.getValueVraschet(), tahoma9ptGreen));
				sheet.addCell(new Label(6, 11 + i, collect.getValueSpribor(), tahoma9ptGreen));
				sheet.addCell(new Label(7, 11 + i, collect.getValueSraschet(), tahoma9ptGreen));
			}
			else
			{
				sheet.addCell(new Label(3, 11 + i, collect.getValueVpribor(), tahoma9ptYellow));
				sheet.addCell(new Label(4, 11 + i, collect.getValueVraschet(), tahoma9ptYellow));
				sheet.addCell(new Label(6, 11 + i, collect.getValueSpribor(), tahoma9ptYellow));
				sheet.addCell(new Label(7, 11 + i, collect.getValueSraschet(), tahoma9ptYellow));
			}
		}

		/*
		 * Вторая часть
		 */
		for (int i = 14; i < 29; i++)
		{
			collect.setIndex(i);

			sheet.addCell(new Label(2, 12 + i, collect.getValueVall(), tahoma9ptGreen));
			sheet.addCell(new Label(5, 12 + i, collect.getValueSall(), tahoma9ptGreen));

			if (i == 15 || i == 20 || i == 28)
			{
				sheet.addCell(new Label(3, 12 + i, collect.getValueVpribor(), tahoma9ptGreen));
				sheet.addCell(new Label(4, 12 + i, collect.getValueVraschet(), tahoma9ptGreen));
				sheet.addCell(new Label(6, 12 + i, collect.getValueSpribor(), tahoma9ptGreen));
				sheet.addCell(new Label(7, 12 + i, collect.getValueSraschet(), tahoma9ptGreen));
			}
			else
			{
				sheet.addCell(new Label(3, 12 + i, collect.getValueVpribor(), tahoma9ptYellow));
				sheet.addCell(new Label(4, 12 + i, collect.getValueVraschet(), tahoma9ptYellow));
				sheet.addCell(new Label(6, 12 + i, collect.getValueSpribor(), tahoma9ptYellow));
				sheet.addCell(new Label(7, 12 + i, collect.getValueSraschet(), tahoma9ptYellow));
			}
		}
	}
}
