package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainClass
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		WinMain win = new WinMain();
		win.setVisible(true);
		
		//test();
	}
	
	public static void test()
	{
		Connection conn = null;
		ResultSet rs = null;
		Statement stat = null;


		try
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:sample1.db");

			stat = conn.createStatement();
			//stat.executeUpdate("UPDATE available SET mar = '+' WHERE id LIKE '1';");
			//stat.executeUpdate("drop table if exists title;");
			//stat.executeUpdate("drop table if exists otpuskpara;");
			//stat.executeUpdate("drop table if exists otpuskgor;");
			// Выполняем запрос, который у нас в переменной query
			//int id = 13;
			//stat.executeQuery("DELETE FROM title WHERE id = '"+id+"';");
			//stat.executeUpdate("DELETE FROM otpuskpara WHERE titleid = '"+id+"';");
			//stat.executeUpdate("DELETE FROM otpuskgor WHERE titleid = '"+id+"';");
			// проверка на созданные таблицы
						// Выполняем запрос, который у нас в переменной query
			/*
			Vector result = new Vector();
						rs = stat.executeQuery("SELECT district FROM title;");

						// запись первой даты
						if (rs.next())
						{
							result.add(rs.getString(1));
						}

						// добавление ост годов
						continuebreak:
						while (rs.next())
						{
							// ищем год, который не находится в result
							for (int i = 0; i < result.size(); i++)
							{
								if (result.get(i).equals(rs.getString(1)))
								{
									// переход к след записи
									continue continuebreak;
								}
							}
							result.add(rs.getString(1));
						}
			int p=0;
			
			for(int i=0;i<result.size();i++)
			{
				System.out.print(", \""+result.get(i)+"\"");
			}
			*/
			rs = stat.executeQuery("SELECT id FROM title where district like 'Вязниковский муниципальный район';");

			while (rs.next())
			{
				System.out.println(rs.getString(1)+"  "+rs.getString(1)+"  "+rs.getString(1));
			}
			System.out.println("   p="+0);
			String value = " 8 309,053";
			value = value.replace(" ", "");
			value = value.replace(",", ".");
			System.out.println("   kontr="+Float.parseFloat(value)+";    ");
			

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				rs.close();
				stat.close();
				conn.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
