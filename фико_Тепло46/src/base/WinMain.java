package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class WinMain extends JFrame
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public final static int		WIDTH				= 700;	// ширина окна в px
	public final static int		HEIGHT				= 500;	// высота окна в px

	private JTable				jDataTable			= null; // таблица для
															// предоставление
															// данные и работы с
															// ними

	private JTextField			jSearchTextField	= null; // объект строки
															// поиска

	private String				sqlsearch			= "";	// строка поиска

	// Конструктор остновного окна
	@SuppressWarnings("serial")
	public WinMain()
	{

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		
		// задаём размеры
		setSize(WIDTH, HEIGHT);
		// получаем данные о разрешение экрана
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// ставим окно по середине
		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);
		// Титульная строка
		setTitle("Диадок - Тепло");

		// определяем сценарий при закрытие окна
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter()
		{
			// закрываем программу
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		// создание формы
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// создание панели поиска
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout(5, 0));
		mainPanel.add(searchPanel, BorderLayout.NORTH);

		// Label строки поиска
		JLabel jLabel = new JLabel("Строка поиска:");
		// Строка поиска
		jSearchTextField = new JTextField();
		jSearchTextField.getDocument().addDocumentListener(new SearchDocumentListener());

		searchPanel.add(jLabel, BorderLayout.WEST);
		searchPanel.add(jSearchTextField);

		// создание панели кнопок
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		// Добавление таблиц
		JButton addButton = new JButton("Добавить");
		addButton.addActionListener(new KeyPressAddTableActionListener());
		addButton.setToolTipText("Добавление отчетов в программу");
		buttonsPanel.add(addButton, null);
		
		// Поставщики
		JButton svodButton = new JButton("Поставщики");
		svodButton.addActionListener(new SvodTableActionListener());
		svodButton.setToolTipText("Список поставщиков, подавщих отчёты");
		buttonsPanel.add(svodButton, null);

		// пока не определена
		// JButton jButton = new JButton("-");
		// jButton.addActionListener(new OutputTableActionListener());
		// buttonsPanel.add(jButton, null);

		// создание панели с таблицой
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		mainPanel.add(tablePanel, BorderLayout.CENTER);

		// создание таблицы
		jDataTable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		// добавление таблицы на панель вместе со скролл баром
		tablePanel.add(new JScrollPane(jDataTable), null);

		// обновляем информацию таблицы
		refreshTable();

		// Открытие подробной информации о записи
		// двойной клик мыши по строчке
		jDataTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				// ждём 2 кликов
				if (e.getClickCount() == 2)
				{
					// пользователь сделал 2 клика

					// получаем инф о выбранной таблице
					JTable target = (JTable) e.getSource();

					// создаём окно просмотра
					new WinReview(jDataTable.getValueAt(target.getSelectedRow(), 0));
				}
			}
		});

		// Реализация PopUp Menu
		jDataTable.addMouseListener(new MouseAdapter()
		{
			public void mouseReleased(MouseEvent Me)
			{
				if (0 < jDataTable.getSelectedRows().length && Me.isMetaDown())
				{
					JPopupMenu Pmenu = new JPopupMenu();

					// количество выделенных записей
					// для удобства пользователей
					//JMenuItem numberRecords = new JMenuItem("Выделено: " + jDataTable.getSelectedRows().length);
					//Pmenu.add(numberRecords);

					// тут должен быть свод
					if (jDataTable.getSelectedRows().length < 2)
					{
						JMenuItem svod = new JMenuItem("Создать свод за " + jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 2) + " " + jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 1));
						Pmenu.add(svod);

						//
						svod.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								String month = jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 2).toString();
								String year = jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 1).toString();

								String[] districts = { "Бабынинский муниципальный район", "Барятинский муниципальный район", "Боровский муниципальный район", "Город Калуга", "Город Обнинск", "Дзержинский муниципальный район", "Думиничский муниципальный район", "Жиздринский муниципальный район", "Жуковский муниципальный район", "Износковский муниципальный район", "Кировский муниципальный район", "Козельский муниципальный район", "Куйбышевский муниципальный район", "Людиновский муниципальный район", "Малоярославецкий муниципальный район", "Медынский муниципальный район", "Мещовский муниципальный район", "Мосальский муниципальный район", "Перемышльский муниципальный район", "Спас-Деменский муниципальный район", "Сухиничский муниципальный район", "Тарусский муниципальный район", "Ульяновский муниципальный район", "Ферзиковский муниципальный район", "Хвастовичский муниципальный район", "Юхновский муниципальный район" };

								Vector<Integer> full_ids = new Vector<Integer>();

								for (int p = 0; p < districts.length; p++)
								{
									Vector<Integer> ids = new ConnectionBD().getIDS(year, month, districts[p]);

									if (ids.size() > 0)
									{
										full_ids.addAll(ids);
										new OutputTable(ids, "Свод за " + month + " " + year + "/" + districts[p] + ".xls");
									}
								}
								new OutputTable(full_ids, "Свод за " + month + " " + year + "/Свод по КО.xls");

								JOptionPane.showMessageDialog(null, "finish");
							}
						});
						
						
						JMenuItem svod2 = new JMenuItem("Создать свод за " + jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 1));
						Pmenu.add(svod2);

						//
						svod2.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								String year = jDataTable.getValueAt(jDataTable.getSelectedRows()[0], 1).toString();

								new OutputTableV2(year, "Свод за " + year + ".xls");

								JOptionPane.showMessageDialog(null, "finish");
							}
						});
					}
					else
					{
						JMenuItem svod = new JMenuItem("Создать свод из " + jDataTable.getSelectedRows().length + " выделенных");
						Pmenu.add(svod);

						//
						svod.addActionListener(new ActionListener()
						{
							public void actionPerformed(ActionEvent e)
							{
								Vector<Integer> ids = new Vector<Integer>();
								for (int i = 0; i < jDataTable.getSelectedRows().length; i++)
								{
									ids.addElement(new Integer(jDataTable.getValueAt(jDataTable.getSelectedRows()[i], 0).toString()));
								}
								new OutputTable(ids, "свод.xls");
								JOptionPane.showMessageDialog(null, "finish");
							}
						});
					}

					// удаляем выделенные элементы
					JMenuItem delRecords = new JMenuItem("Удалить " + jDataTable.getSelectedRows().length + " эл.");
					Pmenu.add(delRecords);

					// показываем PopUp меню
					Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());

					// удаление записей
					delRecords.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							// Сообщение

							// варианты ответа пользователя
							String[] choices = { "Да", "Нет" };

							// создание сообщения
							int response = JOptionPane.showOptionDialog(null // В
																				// центре
																				// окна
							, "Вы уверены, что хотите удалить " + jDataTable.getSelectedRows().length + " элементов?" // Сообщение
							, "" // Титульник сообщения
							, JOptionPane.YES_NO_OPTION // Option type
							, JOptionPane.PLAIN_MESSAGE // messageType
							, null // Icon (none)
							, choices // Button text as above.
							, "" // Default button's labelF
							);

							// обработка ответа пользователя
							switch (response)
							{
								case 0:
									// удаление
									for (int i = 0; i < jDataTable.getSelectedRows().length; i++)
									{
										new ConnectionBD().deleteRow(jDataTable.getValueAt(jDataTable.getSelectedRows()[i], 0).toString());
									}
									// обновляем таблицу
									refreshTable();
									break;
								case 1:
									// ничего не удаляем
									break;
								case -1:
									// окно было закрыто - ничего не удаляем
								default:
									break;
							}

						}
					});
				}
			}
		});

		// добавление всех элементов на форму
		getContentPane().add(mainPanel);
		// обновление формы
		validate();
	}

	// Фильтр
	public class SearchDocumentListener implements DocumentListener
	{
		public void changedUpdate(DocumentEvent e)
		{
			updateSearchString();
		}

		public void removeUpdate(DocumentEvent e)
		{
			updateSearchString();
		}

		public void insertUpdate(DocumentEvent e)
		{
			updateSearchString();
		}

		public void updateSearchString()
		{
			// Обнуляем запрос поиска
			sqlsearch = " ";
			// разбиваем поиск на слова

			String[] result = jSearchTextField.getText().split(" ");
			// проверяем на пустую строку
			if (result.length > 0)
			{
				// если не пустая, создаём запрос
				sqlsearch += " where  search like '%" + result[0].toLowerCase() + "%' ";
				// и если не 1 параметр запроса
				for (int i = 1; i < result.length; i++)
				{
					// то добавляем ост параметры к запросу
					sqlsearch += " and search like '%" + result[i].toLowerCase() + "%' ";
				}
			}
			// обновялем данные в таблице
			refreshTable();

			validate();
		}
	}

	// Обновляем таблицу
	private void refreshTable()
	{
		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB(sqlsearch);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("id");
		header.add("Год");
		header.add("Месяц");
		header.add("Наименование");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jDataTable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);
		// задаем ширину каждого столбца, кроме наименования
		// id
		jDataTable.getColumnModel().getColumn(0).setMaxWidth(50);
		// год
		jDataTable.getColumnModel().getColumn(1).setMaxWidth(40);
		// месяц
		jDataTable.getColumnModel().getColumn(2).setMaxWidth(80);
		// Под название организации отводится всё оставшиеся пространство
	}

	// Кнопка "Добавить"
	// вызывает окно добавления новых записей в бд
	public class KeyPressAddTableActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			WinAddExcel winadd = new WinAddExcel();
			winadd.addWindowListener(new WindowAdapter()
			{
				public void windowClosed(WindowEvent e)
				{
					// после закрытия, обновляем данные таблицы
					refreshTable();
				}
			});
		}
	}

	// Кнопка "Добавить"
	// вызывает окно добавления новых записей в бд
	public class SvodTableActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			new WinSvod();
		}
	}

	public class OutputTableActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Vector<Integer> ids = new Vector<Integer>();
			for (int i = 0; i < jDataTable.getSelectedRows().length; i++)
			{
				ids.addElement(new Integer(jDataTable.getValueAt(jDataTable.getSelectedRows()[i], 0).toString()));
			}
			new OutputTable(ids, "свод.xls");
		}
	}
}
