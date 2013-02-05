package base;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JOptionPane;

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

public class ToExcelKOyear
{

	private static WritableWorkbook	workbook;
	private static WritableSheet	sheet;

	public ToExcelKOyear(String year)
	{
		//Получаем данные из бд
		Vector<Vector<String>> values = new ConnectionBD().getDataFromDB_Year(year);

		//задаём настройки книги
		WorkbookSettings ws = new WorkbookSettings();
		ws.setLocale(new Locale("ru", "RU"));

		try
		{
			//создание книги
			workbook = Workbook.createWorkbook(new File("Калужская область " + year + ".xls"), ws);
			// создание листа
			sheet = workbook.createSheet(year, 0 );

			// основной шрифт, используется в шапке
			
			// установка шрифта
			WritableFont tahoma9ptBold = new WritableFont(WritableFont.TAHOMA, 9, WritableFont.NO_BOLD);
			WritableCellFormat cellFormat = new WritableCellFormat(tahoma9ptBold);
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
			
			
			sheet.addCell(new Label(0, 1, "Организация", cellFormat));

			sheet.addCell(new Label(1, 0, year, cellFormat));

			sheet.addCell(new Label(0, 0, "", cellFormat));

			sheet.addCell(new Label(1, 1, "Январь", cellFormat));

			sheet.addCell(new Label(2, 1, "Февраль", cellFormat));

			sheet.addCell(new Label(3, 1, "Март", cellFormat));

			sheet.addCell(new Label(4, 1, "Апрель", cellFormat));

			sheet.addCell(new Label(5, 1, "Май", cellFormat));

			sheet.addCell(new Label(6, 1, "Июнь", cellFormat));

			sheet.addCell(new Label(7, 1, "Июль", cellFormat));

			sheet.addCell(new Label(8, 1, "Август", cellFormat));

			sheet.addCell(new Label(9, 1, "Сентябрь", cellFormat));

			sheet.addCell(new Label(10, 1, "Октябрь", cellFormat));

			sheet.addCell(new Label(11, 1, "Ноябрь", cellFormat));

			sheet.addCell(new Label(12, 1, "Декабрь", cellFormat));
			
			sheet.addCell(new Label(13, 1, "Год", cellFormat));

			//склеиваем ячейки
			sheet.mergeCells(1, 0, 13, 0);
			
			//задаём ширину первой колонки
			sheet.setColumnView(0, 65);

			//задаём высоту строк шапки
			sheet.setRowView(0, 500);
			sheet.setRowView(1, 500);

			//заполняем данные
			//список районов Калужской области
			String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };

			//дополнительные шрифты, разница в цвете и выравнивание
			WritableCellFormat cellFormat2 = new WritableCellFormat(tahoma9ptBold);
			// выравнивание по центру
			cellFormat2.setAlignment(Alignment.LEFT);
			// перенос по словам если не помещается
			cellFormat2.setWrap(true);
			// установить цвет
			cellFormat2.setBackground(Colour.GRAY_25);
			// рисуем рамку
			cellFormat2.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);

			WritableCellFormat cellFormat3 = new WritableCellFormat(tahoma9ptBold);
			// выравнивание по центру
			cellFormat3.setAlignment(Alignment.CENTRE);
			// перенос по словам если не помещается
			cellFormat3.setWrap(true);
			// установить цвет
			cellFormat3.setBackground(Colour.LIGHT_GREEN);
			// рисуем рамку
			cellFormat3.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			cellFormat3.setVerticalAlignment(VerticalAlignment.CENTRE);
			
			WritableCellFormat cellFormat4 = new WritableCellFormat(tahoma9ptBold);
			// выравнивание по центру
			cellFormat4.setAlignment(Alignment.LEFT);
			// перенос по словам если не помещается
			cellFormat4.setWrap(true);
			// установить цвет
			//cellFormat4.setBackground(Colour.GRAY_25);
			// рисуем рамку
			cellFormat4.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
			cellFormat4.setVerticalAlignment(VerticalAlignment.CENTRE);

			//запись инф в ексель
			for (int i = 0; i < values.size(); i++)
			{
				//высота строки
				sheet.setRowView(2 + i, 500);
				
				// запись +
				for (int p = 1; p < values.get(i).size(); p++)
				{
					//определяем содержимое
					if(values.get(i).get(p).equals("+"))
					{
						// тру (ячейка зелёная)
						sheet.addCell(new Label(p, 2 + i, values.get(i).get(p).toString(), cellFormat3));
					}
					else
					{
						// не тру((ячейка белая)
						sheet.addCell(new Label(p, 2 + i, values.get(i).get(p).toString(), cellFormat4));						
					}
				}
				Label l1 = null;
				
				//добавление организаций
				l1 = new Label(0, 2 + i, values.get(i).get(0).toString(), cellFormat4);
				
				for (int p = 0; p < districts.length; p++)
				{
					//поиск района
					if (districts[p].equals(values.get(i).get(0)))
					{
						//это строка запись о районе
						//записываем строку в серый цвет
						for (int z = 1; z < values.get(i).size(); z++)
						{
							sheet.addCell(new Label(z, 2 + i, "", cellFormat2));
						}
						l1 = new Label(0, 2 + i, values.get(i).get(0).toString(), cellFormat2);
						break;
					}
				}
				sheet.addCell(l1);
			}
			//закрываем книгу
			workbook.write();
			workbook.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (WriteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "finish");
	}
}
