package base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

public class ConnectionBD
{
	private Connection			conn	= null;
	private Statement			stat	= null;
	private ResultSet			rs		= null;
	private PreparedStatement	pst		= null;

	public ConnectionBD()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:teplo.db");
			stat = conn.createStatement();
			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "month STRING, year STRING, name STRING, inn STRING, kpp STRING, okpo STRING," + "district STRING, city STRING, oktmo STRING, uraddress STRING, postaddress STRING," + "rukfio STRING, ruktel STRING, buhfio STRING, buhtel STRING," + "formfio STRING, formjob STRING, formtel STRING, formemail STRING," + "search STRING);");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS otpuskgor(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "titleid INTEGER REFERENCES title(id) ON UPDATE CASCADE ON DELETE CASCADE," + "vall STRING, vpribor STRING, vraschet STRING," + "sall STRING, spribor STRING, sraschet STRING," + "code STRING);");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS otpuskpara(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "titleid INTEGER REFERENCES title(id) ON UPDATE CASCADE ON DELETE CASCADE," + "vall STRING, vpribor STRING, vraschet STRING," + "sall STRING, spribor STRING, sraschet STRING," + "code STRING);");

			
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS presence(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name STRING, inn STRING, district STRING);");
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS available(id INTEGER NOT NULL PRIMARY KEY , presenceid INTEGER REFERENCES presence(id) ON UPDATE CASCADE ON DELETE CASCADE, year STRING, jan STRING, feb STRING, mar STRING, apr STRING, may STRING, jun STRING, jul STRING, aug STRING, sep STRING, oct STRING, nov STRING, dec STRING, itog STRING);");

		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Получение таблицы по запросу "строки поиска"
	 * 
	 * @param sqlsearch
	 * @return { { id, год, месяц, наименование },..}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDataFromDB(String sqlsearch)
	{
		// переменная под результат
		Vector result = new Vector();
		try
		{
			// проверка на наличие таблицы
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "month STRING, year STRING, name STRING, inn STRING, kpp STRING, okpo STRING," + "district STRING, city STRING, oktmo STRING, uraddress STRING, postaddress STRING," + "rukfio STRING, ruktel STRING, buhfio STRING, buhtel STRING," + "formfio STRING, formjob STRING, formtel STRING, formemail STRING," + "search STRING);");

			// Выполняем запрос, который у нас в переменной query
			rs = stat.executeQuery("SELECT id,year,month,name FROM title " + sqlsearch + ";");

			while (rs.next())
			{
				// переменная под элементы строки
				Vector<String> element = new Vector<String>();

				// id
				element.add(rs.getString(1));
				// год
				element.add(rs.getString(2));
				// месяц
				element.add(rs.getString(3));
				// наименование
				element.add(rs.getString(4));

				// Присоединяем список к результату
				result.add(element);
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
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

		return result;
	}

	/**
	 * Удаление записи из бд
	 * 
	 * @param id
	 *            id удаляемой записи
	 */
	public void deleteRow(String id)
	{
		try
		{
			// определяем инн, год и месяц удаляемой записи
			rs = stat.executeQuery("SELECT inn,year,month FROM title WHERE id LIKE '" + id + "';");

			if (rs.next())
			{
				// инн удаляемой записи
				String title_inn = rs.getString(1);
				// год удаляемой записи
				String title_year = rs.getString(2);
				// месяц удаляемой записи
				String title_month = rs.getString(3);

				// определяем id таблицы с инн(presence)
				rs = stat.executeQuery("SELECT id FROM presence WHERE inn LIKE '" + title_inn + "';");

				if (rs.next())
				{
					// id таблицы с инн(presence)
					int presence_id = rs.getInt(1);

					/*
					 * определяем id таблицы available по: presence_id - id инн
					 * title_year - год записи
					 */
					rs = stat.executeQuery("SELECT id FROM available WHERE presenceid LIKE '" + presence_id + "' AND year LIKE '" + title_year + "';");

					if (rs.next())
					{
						// id записи в available
						int available_id = rs.getInt(1);

						// список названий месяцев
						// в виде данных
						String[] months = { "январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь" , "год"};
						// в виде названия поля
						String[] months_eng = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec" , "itog"};

						// определяем месяц, который нужно обновить
						for (int i = 0; i < months.length; i++)
						{
							if (title_month.equals(months[i]))
							{
								// обновляем запись
								stat.executeUpdate("UPDATE available SET " + months_eng[i] + " = '' WHERE id LIKE '" + available_id + "';");
								break;
							}
						}
					}
				}
			}

			// удаление записи
			// с title
			stat.executeUpdate("DELETE FROM title WHERE id = '" + id + "';");
			// с otpuskpara
			stat.executeUpdate("DELETE FROM otpuskpara WHERE titleid = '" + id + "';");
			// с otpuskgor
			stat.executeUpdate("DELETE FROM otpuskgor WHERE titleid = '" + id + "';");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				stat.close();
				conn.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Проверяет вхождение в БД таблицы с "этими параметрами"
	 * 
	 * @param month
	 *            месяц
	 * @param year
	 *            год
	 * @param inn
	 *            инн
	 * @return id найденой записи, -1 если запись не найдена
	 */
	public int presenceTable(String month, String year, String inn, String district)
	{
		// district - НЕ УЧАСТВУЕТ
		try
		{
			//
			rs = stat.executeQuery("SELECT id FROM title WHERE year LIKE '" + year + "' AND month LIKE '" + month + "' AND inn LIKE '" + inn + "';");

			if (rs.next())
			{
				// возрашаем ай ди найденной записи
				return rs.getInt(1);
			}
			else
			{
				// такой записи не было
				return -1;
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
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

		return -1;
	}

	/**
	 * Добавляет новую запись
	 * 
	 * @param content_title
	 * @param content_gor
	 * @param content_para
	 */
	public void addTable(ArrayList<String> content_title, ArrayList<String> content_gor, ArrayList<String> content_para)
	{
		try
		{
			// добавление записи в таблицу title
			pst = conn.prepareStatement("INSERT INTO title VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			// считываем данные с контейнера
			for (int i = 0; i < content_title.size(); i++)
			{
				// 1 параметр - автоинкремент(id)
				pst.setString(i + 2, content_title.get(i));
			}

			pst.addBatch();

			pst.executeBatch();

			// Определяем id последней записи
			rs = stat.executeQuery("SELECT last_insert_rowid();");
			int current_id = rs.getInt(1);

			// добавление записи в таблицу otpuskgor
			pst = conn.prepareStatement("INSERT INTO otpuskgor VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

			for (int i = 0; i < 29; i++)
			{
				pst.setInt(2, current_id);
				for (int p = 0; p < 7; p++)
				{
					pst.setString(p + 3, content_gor.get(i * 7 + p));
				}

				pst.addBatch();

			}

			pst.executeBatch();

			// добавление записи в таблицу otpuskpara
			pst = conn.prepareStatement("INSERT INTO otpuskpara VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

			for (int i = 0; i < 29; i++)
			{
				pst.setInt(2, current_id);
				for (int p = 0; p < 7; p++)
				{
					pst.setString(p + 3, content_para.get(i * 7 + p));
				}
				pst.addBatch();
			}

			pst.executeBatch();

			/*
			 * Работа с таблицами presence и available
			 */

			// список названий месяцев
			// в виде данных
			String[] months = { "январь", "февраль", "март", "апрель", "май", "июнь", "июль", "август", "сентябрь", "октябрь", "ноябрь", "декабрь" , "год"};
			// в виде названия поля
			String[] months_eng = { "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec", "itog" };

			// определяем нахождения инн в presence
			rs = stat.executeQuery("SELECT id FROM presence WHERE inn LIKE '" + content_title.get(3) + "';");

			if (rs.next())
			{
				// инн уже включён

				// сохраняем тек id инн
				int presence_id = rs.getInt(1);

				// ищем год новый записи в уже сохранённой записи
				rs = stat.executeQuery("SELECT id FROM available WHERE year LIKE '" + content_title.get(1) + "' AND presenceid LIKE '" + presence_id + "';");

				if (rs.next())
				{
					// год существует

					// получаем id этого года
					int available_id = rs.getInt(1);

					// определяем месяц, который нужно обновить
					for (int i = 0; i < months.length; i++)
					{
						if (content_title.get(0).equals(months[i]))
						{
							// обновляем запись
							stat.executeUpdate("UPDATE available SET " + months_eng[i] + " = '+' WHERE id LIKE '" + available_id + "';");
							break;
						}
					}
				}
				else
				{
					// необходимо добавить новый год, т.к. этого нету

					pst = conn.prepareStatement("INSERT INTO available VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

					// presence_id - id инн
					pst.setString(2, Integer.toString(presence_id));
					// год
					pst.setString(3, content_title.get(1));

					// заполняем строку
					for (int i = 0; i < months.length; i++)
					{
						if (content_title.get(0).equals(months[i]))
						{
							// нужный месяц
							pst.setString(4 + i, "+");
						}
						else
						{
							// все ост месяца заполняются пустой строкой
							pst.setString(4 + i, "");
						}
					}

					// добавляем в бд
					pst.addBatch();
					pst.executeBatch();
				}

			}
			else
			{
				// инн ещё не включали

				// создаём новую запись инн
				pst = conn.prepareStatement("INSERT INTO presence VALUES (?, ?, ?, ?);");
				// наименование организации
				pst.setString(2, content_title.get(2));
				// инн
				pst.setString(3, content_title.get(3));
				// район
				pst.setString(4, content_title.get(6));
				// добавление записи в бд
				pst.addBatch();
				pst.executeBatch();

				// Определяем id последней записи
				rs = stat.executeQuery("SELECT last_insert_rowid();");
				int presence_id = rs.getInt(1);

				// создаём новый год для нового инн
				pst = conn.prepareStatement("INSERT INTO available VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
				// presence_id - id инн
				pst.setString(2, Integer.toString(presence_id));
				// год
				pst.setString(3, content_title.get(1));

				// заполняем строку
				for (int i = 0; i < months.length; i++)
				{
					if (content_title.get(0).equals(months[i]))
					{
						// нужный месяц
						pst.setString(4 + i, "+");
					}
					else
					{
						// все ост месяца заполняются пустой строкой
						pst.setString(4 + i, "");
					}
				}

				// добавляем в бд
				pst.addBatch();
				pst.executeBatch();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				pst.close();
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

	/**
	 * Года, которые находятся в бд
	 * 
	 * @return список годов
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getYears()
	{
		// переменная под результат
		Vector result = new Vector();

		try
		{
			// Выполняем запрос, который у нас в переменной query
			rs = stat.executeQuery("SELECT year FROM available;");

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

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector<Vector<String>> getDataFromDB_Year(Object year)
	{
		// переменная под результат
		Vector<Vector<String>> result = new Vector();

		try
		{
			// проверка на наличие таблиц(нахождение в бд)
			
			String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };

			for (int p = 0; p < districts.length; p++)
			{

				// Выполняем запрос, который у нас в переменной query
				rs = stat.executeQuery("SELECT m1.name,m2.* FROM presence AS m1, available AS m2 WHERE m2.year like '" + year.toString() + "' AND m1.id LIKE m2.presenceid AND m1.district LIKE '" + districts[p] + "';");

				while (rs.next())
				{
					if (rs.getRow() == 1)
					{
						// Первая запись
						// добавляем строка района
						Vector<String> element2 = new Vector<String>();

						// название района
						element2.add(districts[p]);

						// заполняем ост ячейки пустым значениями
						for (int i = 0; i < 13; i++)
						{
							element2.add("");
						}

						result.add(element2);
					}

					Vector<String> element = new Vector<String>();

					// Код
					element.add(rs.getString(1));
					// объём
					for (int i = 0; i < 13; i++)
					{
						element.add(rs.getString(i + 5));
					}

					// Присоединяем список к результату
					result.add(element);
				}
			}
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
		
//		for(int i=0;i<result.size();i++)
//		{
//			for(int p=0;p<result.get(i).size();p++)
//			{
//				System.out.print(result.get(i).get(p)+" ");
//			}
//			System.out.println();
//			System.out.println();
//			
//		}

		return result;
	}

	public Vector<Vector<String>> getOutputTableV2_par(String year)
	{
		Vector<Vector<String>> result = new Vector<Vector<String>>();
		
		try
		{
			// проверка на наличие таблиц(нахождение в бд)
			
			String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };
			String[] months = {"январь","февраль","март","апрель","май","июнь","июль","август","сентябрь","октябрь","ноябрь","декабрь", "год"};
			
			
			for (int p = 0; p < districts.length; p++)
			{
				// Первая запись
				// добавляем строка района
				Vector<String> element2 = new Vector<String>();

				// название района
				element2.add(districts[p]);

				// заполняем ост ячейки пустым значениями
				for (int i = 0; i < 26; i++)
				{
					element2.add("");
				}

				result.add(element2);
				

				// Выполняем запрос, который у нас в переменной query
				//rs = stat.executeQuery("SELECT m1.name,m2.* FROM presence AS m1, available AS m2 WHERE m2.year like '" + year.toString() + "' AND m1.id LIKE m2.presenceid AND m1.district LIKE '" + districts[p] + "';");
				rs = stat.executeQuery("SELECT m1.inn, m1.name FROM presence AS m1, available AS m2 WHERE m2.year like '" + year.toString() + "' AND m1.id LIKE m2.presenceid AND m1.district LIKE '" + districts[p] + "';");
				
				Vector<String> inn = new Vector<String>();
				Vector<String> name = new Vector<String>();
				
				while (rs.next())
				{
					inn.add(rs.getString(1));
					name.add(rs.getString(2));
				}
				
				//System.out.println(districts[p]+"   "+inn.size());
				
				for(int i=0;i<inn.size();i++)
				{
					double itog = 0.0;
					
					Vector<String> element = new Vector<String>();
					
					element.add(name.get(i));
					
					for(int month=0;month<months.length;month++)
					{
						String forMonth = "";
						rs = stat.executeQuery("SELECT id FROM title WHERE inn like '" + inn.get(i).toString() + "' AND year like '" + year.toString() + "' AND month LIKE '" + months[month] +"' AND district LIKE '" + districts[p] + "';");
						
						String id = null;
						
						if (rs.next())
						{
							id = rs.getString(1);
						}
						
						if(id != null)
						{
							rs = stat.executeQuery("SELECT vall FROM otpuskpara WHERE code LIKE '250' AND titleid LIKE '" + id + "';");
							
							if (rs.next())
							{
								if(month != months.length-1)
								{
									itog += GetCoor(rs.getString(1));
									itog = new BigDecimal(itog).setScale(3, RoundingMode.HALF_UP).doubleValue();
								}
								forMonth = rs.getString(1);
							}
						}
						
						element.add(forMonth);
						element.add(GetCoorItog(Double.toString(itog)));
					}
					
					result.add(element);
				}
			}
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
		
		return result;
	}
	
	public Vector<Vector<String>> getOutputTableV2_gor(String year)
	{
		Vector<Vector<String>> result = new Vector<Vector<String>>();
		
		try
		{
			// проверка на наличие таблиц(нахождение в бд)
			
			String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };
			String[] months = {"январь","февраль","март","апрель","май","июнь","июль","август","сентябрь","октябрь","ноябрь","декабрь","год"};
			
			
			for (int p = 0; p < districts.length; p++)
			{
				// Первая запись
				// добавляем строка района
				Vector<String> element2 = new Vector<String>();

				// название района
				element2.add(districts[p]);

				// заполняем ост ячейки пустым значениями
				for (int i = 0; i < 26; i++)
				{
					element2.add("");
				}

				result.add(element2);
				

				// Выполняем запрос, который у нас в переменной query
				//rs = stat.executeQuery("SELECT m1.name,m2.* FROM presence AS m1, available AS m2 WHERE m2.year like '" + year.toString() + "' AND m1.id LIKE m2.presenceid AND m1.district LIKE '" + districts[p] + "';");
				rs = stat.executeQuery("SELECT m1.inn, m1.name FROM presence AS m1, available AS m2 WHERE m2.year like '" + year.toString() + "' AND m1.id LIKE m2.presenceid AND m1.district LIKE '" + districts[p] + "';");
				
				Vector<String> inn = new Vector<String>();
				Vector<String> name = new Vector<String>();
				
				while (rs.next())
				{
					inn.add(rs.getString(1));
					name.add(rs.getString(2));
				}
				
				//System.out.println(districts[p]+"   "+inn.size());
				
				for(int i=0;i<inn.size();i++)
				{
					double itog = 0.0;
					
					Vector<String> element = new Vector<String>();
					
					element.add(name.get(i));
					
					for(int month=0;month<months.length;month++)
					{
						String forMonth = "";
						rs = stat.executeQuery("SELECT id FROM title WHERE inn like '" + inn.get(i).toString() + "' AND year like '" + year.toString() + "' AND month LIKE '" + months[month] +"' AND district LIKE '" + districts[p] + "';");
						
						String id = null;
						
						if (rs.next())
						{
							id = rs.getString(1);
						}
						
						if(id != null)
						{
							rs = stat.executeQuery("SELECT vall FROM otpuskgor WHERE code LIKE '450' AND titleid LIKE '" + id + "';");
							
							if (rs.next())
							{
								if(month != months.length-1)
								{
									itog += GetCoor(rs.getString(1));
									itog = new BigDecimal(itog).setScale(3, RoundingMode.HALF_UP).doubleValue();
								}
								forMonth = rs.getString(1);
							}
						}
						
						element.add(forMonth);
						element.add(GetCoorItog(Double.toString(itog)));
					}
					
					result.add(element);
				}
			}
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
		
		return result;
	}
	
	// Получаем данные таблицы title в векторной формем
	// current_id - id получаемой записи
	@SuppressWarnings({ "rawtypes" })
	public Vector getDataFromDB_title(String current_id)
	{
		// переменная под результат
		Vector<String> element = new Vector<String>();

		try
		{// проверка на созданные таблицы
			// таблица - title
			stat.executeUpdate("CREATE TABLE IF NOT EXISTS title(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "month STRING, year STRING, name STRING, inn STRING, kpp STRING, okpo STRING," + "district STRING, city STRING, oktmo STRING, uraddress STRING, postaddress STRING," + "rukfio STRING, ruktel STRING, buhfio STRING, buhtel STRING," + "formfio STRING, formjob STRING, formtel STRING, formemail STRING," + "search STRING);");

			// Выполняем запрос, который у нас в переменной query
			rs = stat.executeQuery("SELECT * FROM title where id like '" + current_id + "';");

			while (rs.next())
			{
				for (int i = 1; i < 21; i++)
				{
					element.add(rs.getString(i));
				}
			}
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

		return element;
	}

	/**
	 * Получаем данные таблицы ГОР в векторной форме
	 * 
	 * @param current_id
	 *            id получаемой записи
	 * @return данные таблицы гор
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDataFromDB_GOR(String current_id)
	{
		// переменная под результат
		Vector result = new Vector();

		try
		{
			// Выполняем запрос, который у нас в переменной query
			rs = stat.executeQuery("SELECT code,vall,vpribor,vraschet,sall,spribor,sraschet FROM otpuskgor where titleid like '" + current_id + "';");

			while (rs.next())
			{
				Vector<String> element = new Vector<String>();

				if (rs.getString(1).equals("310"))
				{
					Vector<String> element2 = new Vector<String>();
					element2.add("");
					element2.add("Отпуск тепловой энергии потребителям с коллекторов электростанций (котельных)");
					result.add(element2);
				}

				if (rs.getString(1).equals("380"))
				{
					Vector<String> element3 = new Vector<String>();
					element3.add("");
					element3.add("Отпуск произведенной (приобретенной) тепловой энергии потребителям через тепловую сеть");
					result.add(element3);
				}

				// Код
				element.add(rs.getString(1));
				// Расшифровка кода
				element.add(getStringCode(rs.getString(1)));
				// объём
				element.add(rs.getString(2));
				element.add(rs.getString(3));
				element.add(rs.getString(4));
				// стоимость
				element.add(rs.getString(5));
				element.add(rs.getString(6));
				element.add(rs.getString(7));

				// Присоединяем список к результату
				result.add(element);
			}
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

		return result;
	}

	/**
	 * Получаем данные таблицы ПАРА в векторной форме
	 * 
	 * @param current_id
	 *            id получаемой записи
	 * @return данные таблицы пара
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDataFromDB_PARA(String current_id)
	{
		// переменная под результат
		Vector result = new Vector();

		try
		{
			// Выполняем запрос, который у нас в переменной query
			rs = stat.executeQuery("SELECT code,vall,vpribor,vraschet,sall,spribor,sraschet FROM otpuskpara where titleid like '" + current_id + "';");

			while (rs.next())
			{
				Vector<String> element = new Vector<String>();

				if (rs.getString(1).equals("110"))
				{
					Vector<String> element2 = new Vector<String>();
					element2.add("");
					element2.add("Отпуск тепловой энергии потребителям с коллекторов электростанций (котельных)");
					result.add(element2);
				}

				if (rs.getString(1).equals("180"))
				{
					Vector<String> element3 = new Vector<String>();
					element3.add("");
					element3.add("Отпуск произведенной (приобретенной) тепловой энергии потребителям через тепловую сеть");
					result.add(element3);
				}

				// Код
				element.add(rs.getString(1));
				// Расшифровка кода
				element.add(getStringCode(rs.getString(1)));
				// объём
				element.add(rs.getString(2));
				element.add(rs.getString(3));
				element.add(rs.getString(4));
				// стоимость
				element.add(rs.getString(5));
				element.add(rs.getString(6));
				element.add(rs.getString(7));

				// Присоединяем список к результату
				result.add(element);
			}
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

		return result;
	}

	public CollectionStrings getGOR(Vector<Integer> ids)
	{
		CollectionStrings collectGOR = new CollectionStrings();
		try
		{			
			for (int i = 0; i < ids.size(); i++)
			{
				stat.executeUpdate("CREATE TABLE IF NOT EXISTS otpuskgor(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "titleid INTEGER REFERENCES title(id) ON UPDATE CASCADE ON DELETE CASCADE," + "vall STRING, vpribor STRING, vraschet STRING," + "sall STRING, spribor STRING, sraschet STRING," + "code STRING);");

				// Выполняем запрос, который у нас в переменной query
				rs = stat.executeQuery("SELECT * from otpuskgor where titleid like '" + Integer.toString(ids.get(i)) + "';");

				int index = 0;

				while (rs.next())
				{
					collectGOR.setIndex(index++);

					collectGOR.setValueVall(GetCoor(rs.getString(3)));
					collectGOR.setValueVpribor(GetCoor(rs.getString(4)));
					collectGOR.setValueVraschet(GetCoor(rs.getString(5)));

					collectGOR.setValueSall(GetCoor(rs.getString(6)));
					collectGOR.setValueSpribor(GetCoor(rs.getString(7)));
					collectGOR.setValueSraschet(GetCoor(rs.getString(8)));
				}
			}
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

		return collectGOR;
	}

	public CollectionStrings getPARA(Vector<Integer> ids)
	{
		CollectionStrings collectPARA = new CollectionStrings();
		try
		{
			for (int i = 0; i < ids.size(); i++)
			{
				// Выполняем запрос, который у нас в переменной query
				rs = stat.executeQuery("SELECT * from otpuskpara where titleid like '" + Integer.toString(ids.get(i)) + "';");

				int index = 0;

				while (rs.next())
				{
					collectPARA.setIndex(index++);

					collectPARA.setValueVall(GetCoor(rs.getString(3)));
					collectPARA.setValueVpribor(GetCoor(rs.getString(4)));
					collectPARA.setValueVraschet(GetCoor(rs.getString(5)));

					collectPARA.setValueSall(GetCoor(rs.getString(6)));
					collectPARA.setValueSpribor(GetCoor(rs.getString(7)));
					collectPARA.setValueSraschet(GetCoor(rs.getString(8)));
				}
			}
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

		return collectPARA;
	}

	public Vector<Integer> getIDS(String year, String month, String district)
	{
		Vector<Integer> ids = new Vector<Integer>();
		try
		{
			// Выполняем запрос, который у нас в переменной query
			rs = stat.executeQuery("SELECT id FROM title WHERE year LIKE '" + year + "' AND month LIKE '" + month + "' AND district like '" + district + "';");

			int i = 0;

			while (rs.next())
			{
				i++;
			}
			if (i > 0)
			{
				rs = stat.executeQuery("SELECT id FROM title WHERE year LIKE '" + year + "' AND month LIKE '" + month + "' AND district like '" + district + "';");

				while (rs.next())
				{
					ids.addElement(rs.getInt(1));
				}
			}
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

		return ids;
	}

	private Double GetCoor(String value)
	{
		// проверяем на наличие информации в ячейке
		if (value != null)
		{
			// если что-то есть, возвращаем строковок представление
			value = value.replace(" ", "");
			value = value.replace(",", ".");
			return Double.parseDouble(value);
		}
		// если она пустая, то возвращаем пустую строку
		return Double.parseDouble("0");
	}
	
	private String GetCoorItog(String value)
	{
		// проверяем на наличие информации в ячейке
		if (value != null)
		{
			// если что-то есть, возвращаем строковок представление
			value = value.replace(" ", "");
			value = value.replace(".", ",");
			return value;
		}
		// если она пустая, то возвращаем пустую строку
		return "";
	}

	/**
	 * Расшифровка кода строки шаблона
	 * 
	 * @param _code
	 *            номер строки
	 * @return текст строки
	 */
	private String getStringCode(String _code)
	{
		int code = Integer.parseInt(_code);
		switch (code)
		{
			case 110:
			case 310:
			{
				return "Промышленные и приравненные к ним потребители";
			}
			case 120:
			case 320:
			{
				return "Население и исполнители коммунальных услуг, всего:";
			}
			case 121:
			case 321:
			{
				return "- население, проживающее в многоквартирных домах";
			}
			case 122:
			case 322:
			{
				return "- население, проживающее в индивидуальных домах";
			}
			case 123:
			case 323:
			{
				return "- на нужды отопления";
			}
			case 124:
			case 324:
			{
				return "- на нужды горячего водоснабжения ";
			}
			case 130:
			case 330:
			{
				return "Бюджетные организации, всего:";
			}
			case 131:
			case 331:
			{
				return "- финансируемые за счет средств федерального бюджета Российской Федерации";
			}
			case 132:
			case 332:
			{
				return "- финансируемые за счет средств бюджета субъекта Российской Федерации";
			}
			case 133:
			case 333:
			{
				return "- финансируемые за счет средств местных бюджетов";
			}
			case 140:
			case 340:
			{
				return "Другие теплосбытовые и теплоснабжающие организации";
			}
			case 150:
			case 350:
			{
				return "На компенсацию потерь тепловой энергии при ее передаче организациями, оказывающими услуги по передаче тепловой энергии";
			}
			case 160:
			case 360:
			{
				return "Прочие потребители";
			}
			case 170:
			case 370:
			{
				return "Собственные и производственные нужды энергоснабжающей организации";
			}
			case 180:
			case 380:
			{
				return "Промышленные и приравненные к ним потребители";
			}
			case 190:
			case 390:
			{
				return "Население и исполнители коммунальных услуг, всего:";
			}
			case 191:
			case 391:
			{
				return "- население, проживающее в многоквартирных домах";
			}
			case 192:
			case 392:
			{
				return "- население, проживающее в индивидуальных домах";
			}
			case 193:
			case 393:
			{
				return "- на нужды отопления";
			}
			case 194:
			case 394:
			{
				return "- на нужды горячего водоснабжения ";
			}
			case 200:
			case 400:
			{
				return "Бюджетные организации, всего:";
			}
			case 201:
			case 401:
			{
				return "- финансируемые за счет средств федерального бюджета Российской Федерации";
			}
			case 202:
			case 402:
			{
				return "- финансируемые за счет средств бюджета субъекта Российской Федерации";
			}
			case 203:
			case 403:
			{
				return "- финансируемые за счет средств местных бюджетов";
			}
			case 210:
			case 410:
			{
				return "Другие теплосбытовые и теплоснабжающие организации";
			}
			case 220:
			case 420:
			{
				return "На компенсацию потерь тепловой энергии при ее передаче организациями, оказывающими услуги по передаче тепловой энергии";
			}
			case 230:
			case 430:
			{
				return "Прочие потребители";
			}
			case 240:
			case 440:
			{
				return "Собственные и производственные нужды энергоснабжающей организации";
			}
			case 250:
			case 450:
			{
				return "Полезный отпуск - всего";
			}
		}
		return _code;
	}
}
