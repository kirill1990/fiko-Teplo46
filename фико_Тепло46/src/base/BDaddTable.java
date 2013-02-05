package base;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@SuppressWarnings("rawtypes")
public class BDaddTable extends SwingWorker
{
	private Sheet				sheet_title			= null;
	private Sheet				sheet_gor			= null;
	private Sheet				sheet_para			= null;

	// Тепло
	private boolean				teplokozelsgorod	= false;
	private boolean				teplokozelsrayon	= false;

	// Ремпутьмаш
	private boolean				rempludin			= false;
	private boolean				remptovar			= false;
	private boolean				rempkaluga			= false;

	private JProgressBar		jProgressBar		= null;

	private DefaultListModel	listPaths			= null;
	private DefaultListModel	listNames			= null;

	private JFrame				frame				= null;

	public void setJProgressBar(JProgressBar jProgressBar)
	{
		this.jProgressBar = jProgressBar;
	}

	public void setListPaths(DefaultListModel listPaths)
	{
		this.listPaths = listPaths;
	}

	public void setListNames(DefaultListModel listNames)
	{
		this.listNames = listNames;
	}

	public void setFrame(JFrame frame)
	{
		this.frame = frame;
	}

	@Override
	protected void done()
	{
		// уничтожаем фрейм
		// вызывает обновление таблицы на гл. фрейме
		frame.dispose();
	}

	@Override
	protected Object doInBackground() throws Exception
	{
		// проссматриваем весь список
		for (int i = 0; i < listPaths.getSize(); i++)
		{
			// запускаем обработку
			runAdd(new File(listPaths.getElementAt(i).toString()));
			// после добавление записи в бд,
			// символизируем...
			jProgressBar.setValue(i + 1);
			// ... о занесение ЭТОЙ записи
			listNames.removeElementAt(0);
		}
		return null;
	}

	// Обработка данных
	private void runAdd(File file)
	{
		try
		{
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("ru", "RU"));
			ws.setSuppressWarnings(false);
			ws.setDrawingsDisabled(false);

			Workbook workbook = Workbook.getWorkbook(file, ws);

			if (getTitle(workbook) && getOtpuskGor(workbook) && getOtpuskPara(workbook))
			{
				if (presenceTable(file))
				{
					excel();
				}
			}
			else
			{

				JOptionPane.showMessageDialog(null, "Ошибка в файле: " + file.getName());
			}

			workbook.close();
		}
		catch (BiffException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean presenceTable(File file)
	{
		// Месяц
		String month = sheet_title.getCell("F10").getContents().toLowerCase();

		// Год
		String year = sheet_title.getCell("F11").getContents().toLowerCase();

		// ИНН
		String inn = sheet_title.getCell("F15").getContents();

		// Муниципальный район
		String district = sheet_title.getCell("F19").getContents();

		if (inn.equals("4004403230") && file.getName().indexOf("Козельск-город") > 0)
		{
			inn += "_Козельск_город";
			district = "Козельский муниципальный район";
			this.teplokozelsgorod = true;
		}
		else
		{
			this.teplokozelsgorod = false;
		}

		if (inn.equals("4004403230") && file.getName().indexOf("Козельск-район") > 0)
		{
			inn += "_Козельск_район";
			district = "Козельский муниципальный район";
			this.teplokozelsrayon = true;
		}
		else
		{
			this.teplokozelsrayon = false;
		}
		
		// Ремпутьмаш
		if (inn.equals("4029032450"))
		{
			if (district.equals("Людиновский муниципальный район"))
			{
				inn += "_Людиновский_филиал";
				this.rempludin = true;
			}
			else
			{
				if (district.equals("Дзержинский муниципальный район"))
				{
					inn += "_Товарковский_филиал";
					this.remptovar = true;
				}
				else
				{
					inn += "_Город_Калуга";
					this.rempkaluga = true;
				}
			}
		}

		int id = new ConnectionBD().presenceTable(month, year, inn, district);

		if (id > -1)
		{
			// Сообщение
			String[] choices = { "Заменить", "Пропустить" };
			int response = JOptionPane.showOptionDialog(null // Center in
																// window.
			, "Данные файла: '" + file.getName() + "' уже записаны\n" + "" + inn + " " + month + " " + year + "\n" + "Путь к файлу: " + file.getAbsolutePath() // Message
			, "" // Title in titlebar
			, JOptionPane.YES_NO_OPTION // Option type
			, JOptionPane.PLAIN_MESSAGE // messageType
			, null // Icon (none)
			, choices // Button text as above.
			, "None of your business" // Default button's labelF
			);

			// определяем полученный ответ от пользователя
			switch (response)
			{
				case 0:
					// производим замену
					// удаляем старую запись
					new ConnectionBD().deleteRow(Integer.toString(id));
					// и записываем новую
					return true;

				case 1:
				case -1:
					// получен отрицательный ответ
					return false;

				default:
					// ... If we get here, something is wrong. Defensive
					// programming.
					JOptionPane.showMessageDialog(null, "Unexpected response " + response);
			}
		}
		else
		{
			// такой записи не было, продолжаем
			return true;
		}

		// неведомая ошибка
		return false;
	}

	// Считываем с excel файла и заносит данные в бд
	private void excel()
	{
		// Строка поиска
		// по ней происходит фильтрация данных на гл.фрейме
		String search = "";

		// собираем данные в одном месте(title)
		ArrayList<String> content_title = new ArrayList<String>();

		// Месяц
		content_title.add(sheet_title.getCell("F10").getContents().toLowerCase());
		search += sheet_title.getCell("F10").getContents().toLowerCase() + " ";

		// Год
		content_title.add(sheet_title.getCell("F11").getContents().toLowerCase());
		search += sheet_title.getCell("F11").getContents().toLowerCase() + " ";

		if (this.teplokozelsgorod || this.teplokozelsrayon)
		{
			if (this.teplokozelsgorod)
			{
				// Наименование орг
				content_title.add(sheet_title.getCell("F13").getContents() + "_Козельск_город");
				search += (sheet_title.getCell("F13").getContents() + "_Козельск_город").toLowerCase() + " ";

				// ИНН
				content_title.add(sheet_title.getCell("F15").getContents() + "_Козельск_город");
			}
			else
			{
				// Наименование орг
				content_title.add(sheet_title.getCell("F13").getContents() + "_Козельск_район");
				search += (sheet_title.getCell("F13").getContents() + "_Козельск_район").toLowerCase() + " ";

				// ИНН
				content_title.add(sheet_title.getCell("F15").getContents() + "_Козельск_район");
			}
		}
		else
		{
			System.out.println(this.rempkaluga+"  "+this.rempludin+"  "+this.remptovar);
			if(this.rempkaluga || this.rempludin || this.remptovar)
			{
				//РЕМПУТЬМАШ
				
				// Наименование орг
				content_title.add(sheet_title.getCell("F13").getContents());
				search += sheet_title.getCell("F13").getContents().toLowerCase() + " ";
				
				String district = sheet_title.getCell("F19").getContents();
				
				if (district.equals("Людиновский муниципальный район"))
				{
					//инн
					content_title.add(sheet_title.getCell("F15").getContents()+"_Людиновский_филиал");
				}
				else
				{
					if (district.equals("Дзержинский муниципальный район"))
					{
						//инн
						content_title.add(sheet_title.getCell("F15").getContents()+"_Товарковский_филиал");
					}
					else
					{
						//инн
						content_title.add(sheet_title.getCell("F15").getContents()+"_Город_Калуга");
					}
				}
			}
			else
			{
				//ОСТАЛЬНЫЕ 
				
				// Наименование орг
				content_title.add(sheet_title.getCell("F13").getContents());
				search += sheet_title.getCell("F13").getContents().toLowerCase() + " ";
	
				// ИНН
				content_title.add(sheet_title.getCell("F15").getContents());
			}
		}

		
		
		// КПП
		content_title.add(sheet_title.getCell("F16").getContents());

		// ОКПО
		content_title.add(sheet_title.getCell("F17").getContents());

		if (this.teplokozelsgorod || this.teplokozelsrayon)
		{

			// Муниципальный район
			content_title.add("Козельский муниципальный район");
			search += ("Козельский муниципальный район").toLowerCase() + " ";

			if (this.teplokozelsgorod)
			{
				// Муниципальное образование
				content_title.add("Город Козельск");
				search += ("Город Козельск").toLowerCase() + " ";

				// ОКТМО
				content_title.add("29616101");
			}
			else
			{
				// Муниципальное образование
				content_title.add("Город Сосенский");
				search += ("Город Сосенский").toLowerCase() + " ";

				// ОКТМО
				content_title.add("29616104");
			}
		}
		else
		{
			// Муниципальный район
			content_title.add(sheet_title.getCell("F19").getContents());
			search += sheet_title.getCell("F19").getContents().toLowerCase() + " ";

			// Муниципальное образование
			content_title.add(sheet_title.getCell("F21").getContents());
			search += sheet_title.getCell("F21").getContents().toLowerCase() + " ";

			// ОКТМО
			content_title.add(sheet_title.getCell("F23").getContents());
		}

		// Юридический адрес
		content_title.add(sheet_title.getCell("F26").getContents());

		// Почтовый адрес
		content_title.add(sheet_title.getCell("F27").getContents());

		// ФИО руководителя
		content_title.add(sheet_title.getCell("F30").getContents());

		// Тел руководителя
		content_title.add(sheet_title.getCell("F31").getContents());

		// ФИО гл бухгалтера
		content_title.add(sheet_title.getCell("F34").getContents());

		// Тел гл бухгалтера
		content_title.add(sheet_title.getCell("F35").getContents());

		// ФИО ответ за форму
		content_title.add(sheet_title.getCell("F38").getContents());

		// Должность ответ за форму
		content_title.add(sheet_title.getCell("F39").getContents());

		// Тел ответ за форму
		content_title.add(sheet_title.getCell("F40").getContents());

		// Емаил ответ за форму
		content_title.add(sheet_title.getCell("F41").getContents());

		// Переменная для поиска
		content_title.add(search);

		// собираем данные в одном месте(gor)
		ArrayList<String> content_gor = new ArrayList<String>();

		// ОТПУСК ГОР
		for (int i = 12; i < 42; i++)
		{
			if (i != 26)
			{
				// Объем отпуска всего
				content_gor.add(getZero(sheet_gor.getCell("F" + Integer.toString(i)).getContents()));

				// Объем отпуска по приборам учета
				content_gor.add(getZero(sheet_gor.getCell("G" + Integer.toString(i)).getContents()));

				// Объем отпуска расчетным методом
				content_gor.add(getZero(sheet_gor.getCell("H" + Integer.toString(i)).getContents()));

				// Стоимость отпущенной всего
				content_gor.add(getZero(sheet_gor.getCell("I" + Integer.toString(i)).getContents()));

				// Стоимость отпущенной по приборам учета
				content_gor.add(getZero(sheet_gor.getCell("J" + Integer.toString(i)).getContents()));

				// Стоимость отпущенной расчетным методом
				content_gor.add(getZero(sheet_gor.getCell("K" + Integer.toString(i)).getContents()));

				// Код строки
				content_gor.add(sheet_gor.getCell("E" + Integer.toString(i)).getContents());
			}
		}

		// собираем данные в одном месте(para)
		ArrayList<String> content_para = new ArrayList<String>();

		// ОТПУСК ПАРА
		for (int i = 12; i < 42; i++)
		{
			if (i != 26)
			{
				// Объем отпуска всего
				content_para.add(getZero(sheet_para.getCell("F" + Integer.toString(i)).getContents()));

				// Объем отпуска по приборам учета
				content_para.add(getZero(sheet_para.getCell("G" + Integer.toString(i)).getContents()));

				// Объем отпуска расчетным методом
				content_para.add(getZero(sheet_para.getCell("H" + Integer.toString(i)).getContents()));

				// Стоимость отпущенной всего
				content_para.add(getZero(sheet_para.getCell("I" + Integer.toString(i)).getContents()));

				// Стоимость отпущенной по приборам учета
				content_para.add(getZero(sheet_para.getCell("J" + Integer.toString(i)).getContents()));

				// Стоимость отпущенной расчетным методом
				content_para.add(getZero(sheet_para.getCell("K" + Integer.toString(i)).getContents()));

				// Код строки
				content_para.add(sheet_para.getCell("E" + Integer.toString(i)).getContents());
			}
		}

		// запись в бд
		new ConnectionBD().addTable(content_title, content_gor, content_para);

		this.teplokozelsgorod = false;
		this.teplokozelsrayon = false;
		
		this.rempkaluga = false;
		this.rempludin = false;
		this.remptovar = false;
	}

	private boolean getTitle(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Титульный"))
			{
				sheet_title = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpuskGor(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск ТЭ в гор воде"))
			{
				sheet_gor = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private boolean getOtpuskPara(Workbook workbook)
	{
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
		{
			if (workbook.getSheet(i).getName().equals("Отпуск ТЭ в паре"))
			{
				sheet_para = workbook.getSheet(i);
				return true;
			}
		}
		return false;
	}

	private String getZero(String _text)
	{
		if (_text == "")
		{
			return "0,000";
		}
		return _text;
	}
}
