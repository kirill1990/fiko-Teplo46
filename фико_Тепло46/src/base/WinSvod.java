package base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class WinSvod extends JFrame
{
	public final static int	WIDTH	= 1000;
	public final static int	HEIGHT	= 650;

	public WinSvod()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);

		setTitle("Поставщики");
		setSize(WIDTH, HEIGHT);

		// создание формы
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// создание панели с таблицой
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		// mainPanel.add(tablePanel, BorderLayout.CENTER);

		// Создание панели вкладок
		JTabbedPane tabbedPane = new JTabbedPane();
		// шрифт вкладок
		tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));

		@SuppressWarnings("rawtypes") Vector years = new ConnectionBD().getYears();

		for (int i = 0; i < years.size(); i++)
		{
			// добавление панелей во вкладки
			tabbedPane.addTab(years.get(i).toString(), getYearPanel(years.get(i)));
		}

		mainPanel.add(tabbedPane);
		// добавление всех элементов на форму
		getContentPane().add(mainPanel);
		// обновление формы
		validate();
		// показ формы пользователю
		setVisible(true);
	}

	private JPanel getYearPanel(Object year)
	{
		// создание панели поиска
		JPanel tablePanel = new JPanel();
		//tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		tablePanel.setLayout(new BorderLayout(5, 5));

		// создание панели кнопок
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		tablePanel.add(buttonsPanel, BorderLayout.SOUTH);

		// Добавление таблиц
		JButton addButton = new JButton("Сохранить в Excel " + year.toString() + " год.");

		KeyPressTableToExcel asdsad = new KeyPressTableToExcel();
		asdsad.setYear(year.toString());
		addButton.addActionListener(asdsad);

		buttonsPanel.add(addButton, null);

		JTable jtable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		// jtable.setRowSelectionAllowed(true);
		tablePanel.add(new JScrollPane(jtable), BorderLayout.CENTER);

		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_Year(year);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("Организация");
		header.add("Январь");
		header.add("Февраль");
		header.add("Март");
		header.add("Апрель");
		header.add("Май");
		header.add("Июнь");
		header.add("Июль");
		header.add("Август");
		header.add("Сентябрь");
		header.add("Октябрь");
		header.add("Ноябрь");
		header.add("Декабрь");
		header.add("Год");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);

		// наименование
		jtable.getColumnModel().getColumn(0).setMaxWidth(1000);
		jtable.getColumnModel().getColumn(0).setMinWidth(300);
		// месяцы
		for (int i = 1; i < 14; i++)
		{
			jtable.getColumnModel().getColumn(i).setMaxWidth(200);
			jtable.getColumnModel().getColumn(i).setMinWidth(50);
		}

		// дополнительное редактирование ячеек
		jtable.setDefaultRenderer(jtable.getColumnClass(1), new DefaultTableCellRenderer()
		{
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
			{
				String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };

				if (column < 1)
				{
					// название организации
					// выравние по центру
					super.setHorizontalAlignment(SwingConstants.LEFT);

					for (int i = 0; i < districts.length; i++)
					{
						if (districts[i].equals(value))
						{
							super.setBackground(Color.LIGHT_GRAY);
							break;
						}
					}
				}
				else
				{
					// в ячейках месяцев

					// выравние по центру
					super.setHorizontalAlignment(SwingConstants.CENTER);

					// определение содержимого ячейки
					if (value.equals("+"))
					{
						// если содержит знак +
						super.setBackground(Color.GREEN);
					}
					else
					{
						// ничего в ячейке нету
						super.setBackground(Color.WHITE);
					}
				}

				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				return this;
			}

		});
		return tablePanel;
	}

	// Кнопка "Добавить"
	// вызывает окно добавления новых записей в бд
	private class KeyPressTableToExcel implements ActionListener
	{
		String	year	= null;

		public void actionPerformed(ActionEvent e)
		{
			if (year != null)
			{
				new ToExcelKOyear(year);
			}
		}

		public void setYear(String year)
		{
			this.year = year;
		}
	}
}
