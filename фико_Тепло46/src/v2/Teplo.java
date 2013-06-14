/**
 * 
 */
package v2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.UnsupportedLookAndFeelException;

import v2.ui.MainPanel;

import jxl.read.biff.BiffException;

/**
 * @author kirill
 * 
 */
public class Teplo {

    public static String PATHTODB = "teplo_test.db";

    /**
     * @param args
     * @throws IOException
     * @throws BiffException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     * @throws UnsupportedLookAndFeelException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public static void main(String[] args) throws BiffException, IOException,
	    ClassNotFoundException, SQLException, InterruptedException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

	Class.forName("org.sqlite.JDBC");
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Teplo.PATHTODB);

	Statement stat = conn.createStatement();
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS presence(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name STRING, inn STRING, district STRING, city STRING);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, presenceid INTEGER, year STRING, month STRING);");
	stat.executeUpdate("CREATE TABLE IF NOT EXISTS otpusk(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, titleid INTEGER, code STRING, vall STRING, vpribor STRING, vraschet STRING, sall STRING, spribor STRING, sraschet STRING);");
	stat.close();

//	DefaultListModel listPaths = new DefaultListModel();
//	Listener listener = new Listener("2013");
//	// добавление к основному списку
//	for (int i = 0; i < listener.getListNames().getSize(); i++) {
//	    // и получаем все файлы с разрешением .xls
//	    // listNames.addElement(listener.getListNames().getElementAt(i));
//	    // и пути к этим файлам
//	    listPaths.addElement(listener.getListPaths().getElementAt(i));
//	}
//
//	long time = System.currentTimeMillis();
//
//	for (int i = 0; i < 20; i++) {
//	    Thread aa = new Read46(conn,
//		    new File(listPaths.get(i++).toString()));
//	    aa.start();
//	    aa.join();
//	}
//	System.out.println(System.currentTimeMillis() - time);
	conn.close();
	
	new MainPanel();

	// System.exit(0);
    }

}
