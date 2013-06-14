/**
 * 
 */
package v2.excel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import v2.Teplo;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * Обработка Excel документа с занесением данных в базу данных
 * 
 * @author kirill
 * 
 */
public class Read46 extends SwingWorker<Object, Object> {

    /**
     * Подключение к базе данных
     */
    private Connection conn;
    
    /**
     * Список путей к документам
     */
    private DefaultListModel listPaths;

    /**
     * Конструктор. Устанавливает связь с базой данных и забирает считываемый
     * документ.
     * 
     * @param listPaths
     * 
     * @param conn
     *            - Связь с базой данной
     * @param excel
     *            - Документ(Excel файл) с которого необходимо считать данные
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Read46(DefaultListModel listPaths)
	    throws ClassNotFoundException, SQLException {
	Class.forName("org.sqlite.JDBC");
	conn = DriverManager.getConnection("jdbc:sqlite:" + Teplo.PATHTODB);

	this.listPaths = listPaths;
    }

    /**
     * Запуск обработки потока в фоне
     */
    @Override
    protected Object doInBackground() throws Exception {

	try {
	    // no comments

	    for (int i = 0; i < this.listPaths.size(); i++) {
		this.action(new File((String) this.listPaths.get(i)));
		this.setProgress((i%2==0)?1:2);
	    }
	    this.setProgress(3);

	} catch (BiffException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Вся веселуха тут, ПОЕХАЛИ!
     * 
     * @param excel
     *            - Обрабатываем документ Excel
     * @throws BiffException
     * @throws IOException
     * @throws SQLException
     */
    private void action(File excel) throws BiffException, IOException,
	    SQLException {

	/**
	 * настройка Excel файла - убирает вывод ошибок
	 */
	WorkbookSettings wbSettings = new WorkbookSettings();
	wbSettings.setLocale(new Locale("en", "EN"));
	wbSettings.setSuppressWarnings(true);

	/**
	 * читает excel файл<br>
	 * следующие 4 строчки кода занимают по времени<br>
	 * ~200-250ms т.к. файлы ужасные(
	 */
	Workbook workbook = Workbook.getWorkbook(excel, wbSettings);

	/**
	 * Инициализации листов
	 */
	Sheet sheet_title = workbook.getSheet("Титульный");
	Sheet sheet_gor = workbook.getSheet("Отпуск ТЭ в гор воде");
	Sheet sheet_para = workbook.getSheet("Отпуск ТЭ в паре");

	/**
	 * Проверяет наличие листов в excel документе<br>
	 * Если нет хотя бы одного листа, прекрашает дальнейшее выполнение
	 * метода
	 */
	if (sheet_title == null || sheet_gor == null || sheet_para == null) {
	    /**
	     * Один лист не найден, выдаётся сообщение ошибки и прекращает
	     * работу с этим документом
	     */
	    JOptionPane.showMessageDialog(null,"Ошибка в файле: "
		    + excel.getAbsoluteFile());
	    return;
	}

	/**
	 * Считывание данных с титульного листа
	 */
	// Наименование организации
	String name = sheet_title.getCell("F13").getContents();
	// ИНН организации
	String inn = sheet_title.getCell("F15").getContents();
	// Муниципальный район, в котором находится организация
	String district = sheet_title.getCell("F19").getContents();
	// Город, в котором находится организация
	String city = sheet_title.getCell("F21").getContents();
	// Месяц подачи документа
	String month = sheet_title.getCell("F10").getContents().toLowerCase();
	// Год подачи документа
	String year = sheet_title.getCell("F11").getContents().toLowerCase();

	/**
	 * Сбор данных с пары и гор воды
	 */

	// Хранит все числовые данные(было лень заморачиваться с массивами)
	ArrayList<String> content = new ArrayList<String>(406);

	/**
	 * Сбор ОТПУСК ПАРА последовательность пара и гор лучше НЕ МЕНЯТЬ, т.к.
	 * пара начинается с кода 110 по 250, а гор начинается с кода 310 по
	 * 450,
	 */
	content.addAll(initData(sheet_para));
	/*
	 * Сбор ОТПУСК ГОР
	 */
	content.addAll(initData(sheet_gor));

	/**
	 * Проверка наличия организации в базе<br>
	 * сверка осуществляется по ИНН, РАЙОНУ и ГОРОДУ
	 */
	Statement stat = conn.createStatement();
	ResultSet presence = stat
		.executeQuery("SELECT id FROM presence WHERE inn LIKE '" + inn
			+ "' AND district LIKE '" + district
			+ "' AND city LIKE '" + city + "';");

	int id = -1;
	if (presence.next())
	    id = presence.getInt("id");
	presence.close();

	if (id > -1) {
	    /**
	     * Организация уже существует в базе
	     */

	    // Разрешение на запись данных в базу
	    boolean isRecord = true;

	    /**
	     * Проверяет наличия у этой организации записи с ГОДом и МЕСЯЦем
	     * считавемого документа
	     */
	    ResultSet title = stat
		    .executeQuery("SELECT id FROM title WHERE year LIKE '"
			    + year + "' AND month LIKE '" + month
			    + "' AND presenceid LIKE '" + id + "';");

	    if (title.next()) {
		/**
		 * Такая запись существует
		 */

		// сбрасывает разрешение на запись(не портим данные)
		isRecord = false;

		/**
		 * Спрашивается у пользователя, что будем делать?<br>
		 * Два варианта:<br>
		 * - Заменить<br>
		 * - Пропустить
		 */
		String[] choices = { "Заменить", "Пропустить" };
		int response = JOptionPane.showOptionDialog(
			null,
			"Данные файла: '" + excel.getName()
				+ "' уже записаны\n" + name + "\n" + district
				+ "  " + city + "\n" + inn + " " + month + " "
				+ year + "\n" + "Путь к файлу: "
				+ excel.getAbsolutePath() // Message
			, "" // Title in titlebar
			, JOptionPane.YES_NO_OPTION // Option type
			, JOptionPane.PLAIN_MESSAGE // messageType
			, null // Icon (none)
			, choices // Button text as above.
			, "None of your business" // Default button's labelF
		);

		/**
		 * Обработка ответа пользователя:<br>
		 * 0 - заменить, т.е. удаление старой записи, запись новой<br>
		 * 1 - пропустить(конец сценария)<br>
		 * -1 - был нажат крестик(пропускаем)<br>
		 * default - случилась какая-та хрень(лучше пропустить)
		 */
		switch (response) {
		case 0:
		    this.delete(title.getInt(1));
		    isRecord = true;
		    break;
		case 1:
		case -1:
		    break;
		default:
		    JOptionPane.showMessageDialog(null, "Unexpected response "
			    + response);
		}
	    }
	    title.close();

	    /**
	     * Если не было команды пропустить - записывает данные в базу
	     */
	    if (isRecord)
		this.recOtpusk(content, id, year, month);

	    /*
	     * конец ;-)
	     */
	} else {
	    /**
	     * Организация ещё не встречалась<br>
	     */

	    /**
	     * Созадние новой записи с новой организацией
	     */
	    PreparedStatement pst = conn
		    .prepareStatement("INSERT INTO presence VALUES (?, ?, ?, ?, ?);");

	    // Наименование организации
	    pst.setString(2, name);
	    // ИНН организации
	    pst.setString(3, inn);
	    // Муниципальный район орг
	    pst.setString(4, district);
	    // Город организации
	    pst.setString(5, city);

	    pst.addBatch();
	    pst.executeBatch();
	    pst.close();

	    /**
	     * Достаем id занесённой записи
	     */
	    ResultSet get_new_id = stat
		    .executeQuery("SELECT last_insert_rowid();");
	    int new_id_presence = get_new_id.getInt(1);
	    get_new_id.close();

	    /**
	     * Запись данных в базу
	     */
	    this.recOtpusk(content, new_id_presence, year, month);

	    /**
	     * Конец ...
	     */
	}

	/**
	 * Правило хорошего тона, открыл тег - закрыл тег
	 */
	stat.close();
	workbook.close();
    }

    /**
     * Запись документа в базу
     * 
     * @param content
     *            - числовые данные
     * @param id
     *            - id организации в таблие presence(presenceid)
     * @param year
     *            - Год документа
     * @param month
     *            - Месяц документа
     * @throws SQLException
     */
    private void recOtpusk(ArrayList<String> content, int id, String year,
	    String month) throws SQLException {
	/**
	 * Запись титульника одного документа
	 */
	PreparedStatement title = conn
		.prepareStatement("INSERT INTO title VALUES (?, ?, ?, ?);");

	// presenceid - id Организации в presence
	title.setInt(2, id);
	// Год документа
	title.setString(3, year);
	// Месяц документа
	title.setString(4, month);

	title.addBatch();
	title.executeBatch();
	title.close();

	/**
	 * Достается id новой записи в title
	 */
	Statement stat = conn.createStatement();
	ResultSet get_new_id_title = stat
		.executeQuery("SELECT last_insert_rowid();");
	int new_id_title = get_new_id_title.getInt(1);
	get_new_id_title.close();
	stat.close();

	/**
	 * Запись контента
	 */
	PreparedStatement pst = conn
		.prepareStatement("INSERT INTO otpusk VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

	/**
	 * лень модифицировать, так сойдет(
	 */
	for (int i = 0; i < 58; i++) {

	    // titleid
	    pst.setInt(2, new_id_title);
	    for (int p = 0; p < 7; p++)
		// все и по немного)
		pst.setString(p + 3, content.get(i * 7 + p));
	    // В КУЧУ ИХ!!!!
	    pst.addBatch();
	}

	// заносим разом большую куча)
	pst.executeBatch();
	pst.close();
    }

    /**
     * Удаление документа из базы(данные удаляются из title и otpusk)
     * 
     * @param titleId
     *            - id удаляемого документа в title
     * @throws SQLException
     */
    private void delete(int titleId) throws SQLException {
	Statement stat = conn.createStatement();
	stat.executeUpdate("DELETE FROM title WHERE id = '" + titleId + "';");
	stat.executeUpdate("DELETE FROM otpusk WHERE titleid = '" + titleId
		+ "';");
	stat.close();
    }

    /**
     * Считывание числовых данных с документа
     * 
     * @param sheet
     *            - лист, с которого будет считываться(пара или гор)
     * @return упорядочный массив изи числовых данных
     */
    private ArrayList<String> initData(Sheet sheet) {

	ArrayList<String> content = new ArrayList<String>(203);

	for (int i = 12; i < 42; i++) {
	    // пропуск строки
	    if (i != 26) {
		// Код строки
		content.add(sheet.getCell(4, i - 1).getContents());
		// Объем отпуска всего
		content.add(getZero(sheet.getCell(5, i - 1).getContents()));
		// Объем отпуска по приборам учета
		content.add(getZero(sheet.getCell(6, i - 1).getContents()));
		// Объем отпуска расчетным методом
		content.add(getZero(sheet.getCell(7, i - 1).getContents()));
		// Стоимость отпущенной всего
		content.add(getZero(sheet.getCell(8, i - 1).getContents()));
		// Стоимость отпущенной по приборам учета
		content.add(getZero(sheet.getCell(9, i - 1).getContents()));
		// Стоимость отпущенной расчетным методом
		content.add(getZero(sheet.getCell(10, i - 1).getContents()));
	    }
	}

	return content;
    }

    /**
     * Возвращает строку из нулей(0,000), если ячейка была пуста<br>
     * Сделанно для упрощения дальнейших вычислений
     * 
     * @param _text
     *            - текст ячейки
     * @return - правильное значение
     */
    private String getZero(String _text) {
	if (_text == "")
	    return "0";

	return _text;
    }
}
