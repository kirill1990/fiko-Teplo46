/**
 * 
 */
package v2;

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

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * @author kirill
 * 
 */
public class Test {

    public Test() throws BiffException, IOException, ClassNotFoundException,
	    SQLException {

	Class.forName("org.sqlite.JDBC");

	WorkbookSettings wbSettings = new WorkbookSettings();
	wbSettings.setLocale(new Locale("en", "EN"));
	wbSettings.setSuppressWarnings(true);

	Long time = System.currentTimeMillis();
	Workbook workbook = Workbook.getWorkbook(new File("test.xls"),
		wbSettings);
	System.out.println("Time start: " + (System.currentTimeMillis() - time));

	Sheet sheet_title = workbook.getSheet("Титульный");
	System.out.println("Time титульный: " + (System.currentTimeMillis() - time));

	Sheet sheet_gor = workbook.getSheet("Отпуск ТЭ в гор воде");
	System.out.println("Time в гор: " + (System.currentTimeMillis() - time));

	Sheet sheet_para = workbook.getSheet("Отпуск ТЭ в паре");
	System.out.println("Time в паре: " + (System.currentTimeMillis() - time));

//	if (sheet_title == null || sheet_gor == null || sheet_para == null)
//	    System.out.println("Pizdec");
//	else {

	    String name = sheet_title.getCell("F13").getContents();
		    
	    String month = sheet_title.getCell("F10").getContents()
		    .toLowerCase();

	    System.out.println("M: " + month + "   -   "
		    + sheet_title.getCell(5, 9).getContents());

	    // Год
	    String year = sheet_title.getCell("F11").getContents()
		    .toLowerCase();

	    // ИНН
	    String inn = sheet_title.getCell("F15").getContents();

	    // Муниципальный район
	    String district = sheet_title.getCell("F19").getContents();
	    
	    // Муниципальный район
	    String city = sheet_title.getCell("F21").getContents();
//	}

	System.out.println("Time инф с титульного: " + (System.currentTimeMillis() - time));

	// собираем данные в одном месте(gor)
	ArrayList<String> content_gor = new ArrayList<String>();

	long time2 = System.currentTimeMillis();

	// ОТПУСК ГОР
	for (int i = 12; i < 42; i++) {
	    if (i != 26) {
		// Объем отпуска всего
		// content_gor.add(getZero(sheet_gor.getCell(
		// "F" + Integer.toString(i)).getContents()));
		//
		// // Объем отпуска по приборам учета
		// content_gor.add(getZero(sheet_gor.getCell(
		// "G" + Integer.toString(i)).getContents()));
		//
		// // Объем отпуска расчетным методом
		// content_gor.add(getZero(sheet_gor.getCell(
		// "H" + Integer.toString(i)).getContents()));
		//
		// // Стоимость отпущенной всего
		// content_gor.add(getZero(sheet_gor.getCell(
		// "I" + Integer.toString(i)).getContents()));
		//
		// // Стоимость отпущенной по приборам учета
		// content_gor.add(getZero(sheet_gor.getCell(
		// "J" + Integer.toString(i)).getContents()));
		//
		// // Стоимость отпущенной расчетным методом
		// content_gor.add(getZero(sheet_gor.getCell(
		// "K" + Integer.toString(i)).getContents()));
		//
		// // Код строки
		// content_gor.add(sheet_gor.getCell("E" +
		// Integer.toString(i)).getContents());
		

		// Код строки
		content_gor.add(sheet_gor.getCell(4, i - 1).getContents());

		// Объем отпуска всего
		content_gor.add(getZero(sheet_gor.getCell(5, i - 1)
			.getContents()));

		// Объем отпуска по приборам учета
		content_gor.add(getZero(sheet_gor.getCell(6, i - 1)
			.getContents()));

		// Объем отпуска расчетным методом
		content_gor.add(getZero(sheet_gor.getCell(7, i - 1)
			.getContents()));

		// Стоимость отпущенной всего
		content_gor.add(getZero(sheet_gor.getCell(8, i - 1)
			.getContents()));

		// Стоимость отпущенной по приборам учета
		content_gor.add(getZero(sheet_gor.getCell(9, i - 1)
			.getContents()));

		// Стоимость отпущенной расчетным методом
		content_gor.add(getZero(sheet_gor.getCell(10, i - 1)
			.getContents()));

	    }
	}

	System.out.println("Time2: " + (System.currentTimeMillis() - time2));
	System.out.println("Time в горе: " + (System.currentTimeMillis() - time));
	// собираем данные в одном месте(para)
	ArrayList<String> content_para = new ArrayList<String>();

	// ОТПУСК ПАРА
	for (int i = 12; i < 42; i++) {
	    if (i != 26) {
		// Код строки
		content_para.add(sheet_para.getCell("E" + Integer.toString(i))
			.getContents());
		
		// Объем отпуска всего
		content_para.add(getZero(sheet_para.getCell(
			"F" + Integer.toString(i)).getContents()));

		// Объем отпуска по приборам учета
		content_para.add(getZero(sheet_para.getCell(
			"G" + Integer.toString(i)).getContents()));

		// Объем отпуска расчетным методом
		content_para.add(getZero(sheet_para.getCell(
			"H" + Integer.toString(i)).getContents()));

		// Стоимость отпущенной всего
		content_para.add(getZero(sheet_para.getCell(
			"I" + Integer.toString(i)).getContents()));

		// Стоимость отпущенной по приборам учета
		content_para.add(getZero(sheet_para.getCell(
			"J" + Integer.toString(i)).getContents()));

		// Стоимость отпущенной расчетным методом
		content_para.add(getZero(sheet_para.getCell(
			"K" + Integer.toString(i)).getContents()));
	    }
	}

	System.out.println("Time конец считавания с excel: " + (System.currentTimeMillis() - time));

	Connection conn = DriverManager
		.getConnection("jdbc:sqlite:" + Teplo.PATHTODB);
	System.out.println("Time only conn: " + (System.currentTimeMillis() - time));
	Statement stat = conn.createStatement();
	
	System.out.println("Time stat - conn: " + (System.currentTimeMillis() - time));

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS presence(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name STRING, inn STRING, district STRING, city STRING);");

	stat.executeUpdate("CREATE TABLE IF NOT EXISTS title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, presenceid INTEGER, year STRING, month STRING);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS otpusk(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, titleid INTEGER, code STRING, vall STRING, vpribor STRING, vraschet STRING, sall STRING, spribor STRING, sraschet STRING);");

	System.out.println("Time create table: " + (System.currentTimeMillis() - time));
	
	
	/*
	 * Наличие в presence
	 */

	ResultSet presence = stat.executeQuery("SELECT id FROM presence WHERE inn LIKE '" + inn + "' AND district LIKE '" + district + "' AND city LIKE '" + city + "';");
	
	int id = -1;
	if(presence.next())
	    id = presence.getInt("id");
	presence.close();
	System.out.println(id);
	System.out.println("Time существование в presence: " + (System.currentTimeMillis() - time));
	
	if(id>-1){
	
	    ResultSet rs = stat.executeQuery("SELECT id FROM title WHERE year LIKE '" + year + "' AND month LIKE '" + month + "' AND presenceid LIKE '"+id+"';");
	    if(rs.next()){
		// замена или далее
	    }else{
		//пишем
	    }
		System.out.println("est");
	    
	    rs.close();
	    System.out.println("Time rs: " + (System.currentTimeMillis() - time));
		
	}else{
	    PreparedStatement pst = conn.prepareStatement("INSERT INTO presence VALUES (?, ?, ?, ?, ?);");

	    pst.setString(2, name);
	    pst.setString(3, inn);
	    pst.setString(4, district);
	    pst.setString(5, city);
	    
	    pst.addBatch();

	    pst.executeBatch();
	    pst.close();
	    System.out.println("Time insert in presence: " + (System.currentTimeMillis() - time));
	    
	    ResultSet rs = stat.executeQuery("SELECT last_insert_rowid();");
	    int current_id_presence = rs.getInt(1);
	    rs.close();
	    
	    System.out.println("Time get current_id_presence: " + (System.currentTimeMillis() - time));
	    
	    PreparedStatement title = conn.prepareStatement("INSERT INTO title VALUES (?, ?, ?, ?);");
	    
	    title.setInt(2, current_id_presence);
	    title.setString(3, year);
	    title.setString(4, month);

	    title.addBatch();

	    title.executeBatch();
	    title.close();
	    
	    System.out.println("Time insert title: " + (System.currentTimeMillis() - time));
	    
	    ResultSet rs2 = stat.executeQuery("SELECT last_insert_rowid();");
	    int current_id_title = rs2.getInt(1);
	    rs2.close();
	    System.out.println("Time get current_id_title: " + (System.currentTimeMillis() - time));
	    
	    pst = conn.prepareStatement("INSERT INTO otpusk VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

	    for (int i = 0; i < 29; i++){
		pst.setInt(2, current_id_title);
		for (int p = 0; p < 7; p++){
			pst.setString(p + 3, content_para.get(i * 7 + p));
		}

		pst.addBatch();
	    }
	    System.out.println("Time para: " + (System.currentTimeMillis() - time));
	    
	    for (int i = 0; i < 29; i++){
		pst.setInt(2, current_id_title);
		for (int p = 0; p < 7; p++){
			pst.setString(p + 3, content_gor.get(i * 7 + p));
		}

		pst.addBatch();
	    }
	    System.out.println("Time gor: " + (System.currentTimeMillis() - time));
	    
	    pst.executeBatch();
	    pst.close();
	    System.out.println("Time insert otpusk: " + (System.currentTimeMillis() - time));
		
	}
	
	
	
	
	stat.close();
	conn.close();

	System.out.println("Time close: " + (System.currentTimeMillis() - time));

	workbook.close();
    }

    private String getZero(String _text) {
	if (_text == "")
	    return "0,000";

	return _text;
    }

}
