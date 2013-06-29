/**
 * 
 */
package v2.output;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import v2.Teplo;
import v2.support.JXLConstant;
import v2.ui.MainPanel;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * @author kirill
 * 
 */
public class Year {

    /**
     * Наименование листов книги:<br>
     * [0] - Наименование листа.<br>
     * [1] - Наименование на листе.
     */
    private String[][] sheet_title = {
	    { "Отпуск ТЭ в паре", "Полезный отпуск ТЭ в паре." },
	    { "Отпуск ТЭ в гор воде", "Полезный отпуск ТЭ в горячей воде." } };

    /**
     * Список районов Калужской области.
     */
    private String[] districts = MainPanel.districts;

    /**
     * Список месяцев года + итог(год)
     */
    private String[] months = MainPanel.months;

    /**
     * Шрифты для ячеек
     */
    private JXLConstant font = new JXLConstant();

    /**
     * Конструктор
     * 
     * @param year
     *            - год, за который создаётся отчет
     * @throws IOException
     * @throws WriteException
     * @throws SQLException
     */
    public Year(int year) throws IOException, WriteException, SQLException {

	/**
	 * настройки книги
	 */
	WorkbookSettings ws = new WorkbookSettings();
	ws.setLocale(new Locale("ru", "RU"));

	/**
	 * Создание книги в каталоге с программой
	 */
	WritableWorkbook workbook = Workbook.createWorkbook(new File(
		"Полезный отпуск за " + Integer.toString(year) + ".xls"), ws);

	/**
	 * Занесение данных в листы
	 */
	for (int num_sheet = 0; num_sheet < sheet_title.length; num_sheet++) {

	    /**
	     * создание листа
	     */
	    WritableSheet sheet = workbook.createSheet(
		    sheet_title[num_sheet][0], num_sheet);

	    /**
	     * Описание шапки листа
	     */

	    /**
	     * наименование на листе
	     */
	    sheet.addCell(new Label(0, 0, sheet_title[num_sheet][1],
		    font.tahomaMonth));
	    sheet.mergeCells(0, 0, 0, 2);

	    /**
	     * Колонки с наименованиями организаций и районов
	     */
	    sheet.setColumnView(0, 65);

	    /**
	     * строки шапки
	     */
	    // год
	    sheet.setRowView(0, 500);
	    // месяц
	    sheet.setRowView(1, 500);
	    // что за цифра(месяц или нарастающая)
	    sheet.setRowView(2, 1000);

	    /**
	     * Запись года отчета
	     */
	    sheet.addCell(new Label(1, 0, Integer.toString(year),
		    font.tahomaMonth));
	    sheet.mergeCells(1, 0, months.length * 4, 0);

	    /**
	     * Заполнение шапки месяцев
	     */
	    for (int i = 0; i < months.length; i++) {
		/**
		 * Увеличение ширины строки для читаемости больших цифр
		 */
		sheet.setColumnView(i + 1, 12);

		/**
		 * наименование месца
		 */
		sheet.addCell(new Label(1 + i * 4, 1, months[i],
			font.tahomaMonth));
		sheet.mergeCells(1 + i * 4, 1, 4 + i * 4, 1);

		/**
		 * Что за цифра
		 */
		String[] title = {
			"Объем отпуска за месяц",
			"Стоимость за месяц" };

		for (int p = 0; p < 2; p++) {
		    //
		    sheet.addCell(new Label(1 + i * 4 + p * 2, 2, title[p],
			    font.tahomaMonth));
		    sheet.addCell(new Label(2 + i * 4 + p * 2, 2,
			    "С нарастающим итогом", font.tahomaMonth));
		}
	    }

	    /**
	     * Ищем все организации в базе за "year" год
	     */
	    Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		    + Teplo.PATHTODB);
	    Statement stat = conn.createStatement();

	    /**
	     * Текущая строчка для записи
	     */
	    int row = 3;
	    /**
	     * строчка, в которую записывалось последнее наименование района
	     */
	    int old_row = row;

	    for (String district : districts) {
		/**
		 * Просматриваем каждый район Калужской области + Калуга и
		 * Обнинск
		 */

		/**
		 * Записывает наименование района(Разделение организаций по
		 * районам).<br>
		 * Если организаций нет в районе, то на место i района будет
		 * записан i+1 район
		 */

		if (row != old_row)
		    row++;

		sheet.addCell(new Label(0, row, district, font.tahomaTitle));

		for (int i = 1; i < 53; i++)
		    sheet.addCell(new Label(i, row, "", font.tahomaTitle));

		old_row = row;

		/**
		 * Поиск организаций, которые находятся в "district"
		 */
		ResultSet presence_district = stat
			.executeQuery("SELECT id, name FROM presence WHERE district LIKE '"
				+ district + "';");

		while (presence_district.next()) {
		    row++;

		    sheet.addCell(new Label(0, row, presence_district
			    .getString("name"), font.tahomaOrg));
		    /**
		     * Организация была найдена
		     */

		    /**
		     * Поиск документов найденной организации за "year" год
		     */
		    Statement stat2 = conn.createStatement();
		    ResultSet title = stat2
			    .executeQuery("SELECT year, month, id FROM title WHERE presenceid LIKE '"
				    + presence_district.getString("id") + "';");

		    while (title.next()) {
			/**
			 * Найден(ы) документ(ы), запись данных в Excel
			 */
			for (int i = 0; i < months.length; i++) {
			    if (title.getString("year").equals(
				    Integer.toString(year))
				    && title.getString("month").equals(
					    months[i])) {

				/**
				 * Разделение данных по листам.<br>
				 * 250 - Отпуск ТЭ в паре.<br>
				 * 450 - Отпуск ТЭ в гор воде.
				 */
				String code = "250";
				String titleid = title.getString("id");

				if (num_sheet > 0)
				    code = "450";

				/**
				 * Получение данных документа за тек год и месяц
				 */

				String sql = "SELECT vall, sall FROM otpusk WHERE titleid LIKE '"
					+ titleid
					+ "' AND code LIKE '"
					+ code
					+ "';";

				Statement stat3 = conn.createStatement();
				ResultSet otpusk = stat3.executeQuery(sql);

				if (otpusk.next()) {

				    sheet.addCell(new Number(1 + i * 4, row,
					    GetCoor(otpusk.getString("vall")),
					    font.tahomaValue));
				    sheet.addCell(new Number(3 + i * 4, row,
					    GetCoor(otpusk.getString("sall")),
					    font.tahomaValue));

				}
				otpusk.close();
				stat3.close();
			    }
			}
		    }

		    title.close();
		    stat2.close();

		    /**
		     * Формулы
		     */

		    for (int month_index = 0; month_index < months.length; month_index++) {
			/**
			 * 2 колонки:<br>
			 * 1. Объем<br>
			 * 2. Стоимость
			 */
			for (int formula_num = 1; formula_num < 4; formula_num = formula_num + 2) {
			    String formulaV = "";

			    switch (month_index) {
			    case 0:
				/**
				 * Первые месяц с предедушем не складывается
				 */
				formulaV = toColumnExcel(month_index * 4
					+ formula_num)
					+ (row + 1);
				break;
			    case 12:
				/**
				 * Последней месяц - это итог, в нарастающую он
				 * не включается
				 */
				formulaV = toColumnExcel(month_index * 4 - 3
					+ formula_num)
					+ (row + 1);
				break;
			    default:
				/**
				 * Промежуточные месяца(фев-дек)<br>
				 * данные текующего месяца + нарастающие за
				 * предыдущие
				 */
				formulaV = toColumnExcel(month_index * 4
					+ formula_num)
					+ (row + 1)
					+ ", "
					+ toColumnExcel(month_index * 4 - 3
						+ formula_num) + (row + 1);
				break;

			    }

			    sheet.addCell(new Formula(formula_num + 1
				    + month_index * 4, row, "SUM(" + formulaV
				    + ")", font.tahomaValue_green));
			}
		    }

		    /**
		     * Формула проверки правильности ввода итога по сравнению с
		     * нарастающей за все месяца
		     */
		    int erro_column = months.length * 4;

		    /**
		     * Так сойдет
		     */
		    int formula_num = 1;
		    String f = "OR(";

		    f += toColumnExcel(erro_column - 4 + formula_num)
			    + (row + 1) + " <> "
			    + toColumnExcel(erro_column - 3 + formula_num)
			    + (row + 1);
		    f += ",";
		    formula_num = formula_num + 2;
		    f += toColumnExcel(erro_column - 4 + formula_num)
			    + (row + 1) + " <> "
			    + toColumnExcel(erro_column - 3 + formula_num)
			    + (row + 1);
		    f += ")";

		    /**
		     * Запись формулы
		     */
		    sheet.addCell(new Formula(1 + erro_column, row, "IF(" + f
			    + ",\"Ошибка\", \"\")", font.tahomaValue_w));
		}

		presence_district.close();
	    }

	    conn.close();
	}

	workbook.write();
	workbook.close();
    }

    /**
     * Приведение текстового значение к значению с точкой
     * 
     * @param value
     *            - текстовое представление числа с точкой
     * @return - число с точкой
     */
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
	return 0.0;
    }

    /**
     * Вычисляет символьное представления индекса колонки
     * 
     * @param value
     *            - цифровой индекс колонки(33)
     * @return текстовый индекс колонки(AF)
     */
    private String toColumnExcel(Integer value) {
	// промежуточный результат
	String result = "";
	// для определения первого символа
	boolean first = true;

	while (value / 26 > 0) {
	    if (first) {
		result += (char) (65 + value % 26);
		first = false;
	    } else {
		result += (char) (64 + value % 26);
	    }

	    value = value / 26;
	}

	if (first) {
	    result += (char) (65 + value % 26);
	} else {
	    result += (char) (64 + value % 26);
	}

	// переварачиваем результат EFA = > AFE
	String res = "";

	for (int i = 0; i < result.length(); i++) {
	    res += result.substring(result.length() - i - 1, result.length()
		    - i);
	}

	return res;
    }
}
