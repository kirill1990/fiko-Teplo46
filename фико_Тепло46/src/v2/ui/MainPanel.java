package v2.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import jxl.write.WriteException;

import v2.excel.Read46;
import v2.output.Year;
import v2.support.Listener;

import v2.Teplo;

public class MainPanel extends JFrame {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 640789545784970867L;

    /*
     * ширина окна в px
     */
    public final int WIDTH = 1100;

    /*
     * высота окна в px
     */
    public final int HEIGHT = 500;

    private JPanel mainPanel = null;

    private int[] years = { 2012, 2013, 2014, 2015, 2016, 2017, 2018 };

    private JTabbedPane tabbedPane;

    public static String[] districts = {
	    "Бабынинский муниципальный район",
	    "Барятинский муниципальный район",
	    "Боровский муниципальный район",
	    "Дзержинский муниципальный район",
	    "Думиничский муниципальный район",
	    "Жиздринский муниципальный район",
	    "Жуковский муниципальный район",
	    "Износковский муниципальный район",
	    "Кировский муниципальный район",
	    "Козельский муниципальный район",
	    "Куйбышевский муниципальный район",
	    "Людиновский муниципальный район",
	    "Малоярославецкий муниципальный район",
	    "Медынский муниципальный район",
	    "Мещовский муниципальный район",
	    "Мосальский муниципальный район",
	    "Перемышльский муниципальный район",
	    "Спас-Деменский муниципальный район",
	    "Сухиничский муниципальный район",
	    "Тарусский муниципальный район",
	    "Ульяновский муниципальный район",
	    "Ферзиковский муниципальный район",
	    "Хвастовичский муниципальный район",
	    "Юхновский муниципальный район",
	    "Город Калуга",
	    "Город Обнинск" };

    public static String[] months = {
	    "январь",
	    "февраль",
	    "март",
	    "апрель",
	    "май",
	    "июнь",
	    "июль",
	    "август",
	    "сентябрь",
	    "октябрь",
	    "ноябрь",
	    "декабрь",
	    "год" };

    public MainPanel() throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedLookAndFeelException,
	    SQLException {

	/**
	 * Подстраиваемся под интерфейс ос Windows
	 */
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

	/**
	 * Задание свойст окна
	 */

	/**
	 * 1. размерность окна
	 */
	this.setSize(this.WIDTH, this.HEIGHT);

	/**
	 * 2. окно ставится по середине рабочий области пользователя
	 */
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	this.setLocation((screenSize.width - this.WIDTH) / 2,
		(screenSize.height - this.HEIGHT) / 2);

	/*
	 * 3. Заголовок окна
	 */
	this.setTitle("Тепловая энергия по 46 форме");

	/*
	 * при закрытие окна принудительное завершение работы программы
	 */
	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	this.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
		System.exit(0);
	    }
	});

	/**
	 * заполнение формы
	 */
	getContentPane().add(this.mainPanel = initMainPanel());

	/**
	 * 4. показывает форму пользователю...
	 */
	this.setVisible(true);
    }

    private JPanel initMainPanel() throws SQLException {
	// TODO комменты

	JPanel panel = new JPanel();

	panel.setLayout(new BorderLayout(5, 5));
	panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

	tabbedPane = new JTabbedPane();
	tabbedPane.setFont(new Font("Verdana", Font.PLAIN, 12));

	String current_year = new SimpleDateFormat("yyyy").format(new Date(
		System.currentTimeMillis()));

	for (int i = 0; i < years.length; i++) {

	    tabbedPane.addTab(Integer.toString(years[i]),
		    initYearPanel(years[i]));

	    if (Integer.toString(years[i]).equals(current_year))
		tabbedPane.setSelectedIndex(i);
	}

	JPanel btnPanel = new JPanel();
	btnPanel.setLayout(new GridLayout(1, 2));
	btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

	JButton btnAdd = new JButton("Добавить Документ");
	btnAdd.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		addPanel();
	    }
	});

	JButton btnYear = new JButton("Отчет за год");
	btnYear.addActionListener(new ActionListener() {

	    public void actionPerformed(ActionEvent arg0) {
		try {
		    new Year(years[tabbedPane.getSelectedIndex()]);
		    JOptionPane.showMessageDialog(null, "Отчет за "
			    + years[tabbedPane.getSelectedIndex()] + " создан");
		} catch (WriteException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	});

	btnPanel.add(btnAdd);
	btnPanel.add(btnYear);
	panel.add(tabbedPane, BorderLayout.CENTER);
	panel.add(btnPanel, BorderLayout.SOUTH);

	return panel;
    }

    private void addPanel() {
	// TODO комменты

	/**
	 * диалоговое окно.<br>
	 * фильтр установлен на ПАПКИ
	 */
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

	if (fileChooser.showOpenDialog(new JLabel()) != JFileChooser.CANCEL_OPTION) {

	    /*
	     * выбранная директория
	     */
	    File selectedFile = fileChooser.getSelectedFile();

	    final DefaultListModel listNames = new DefaultListModel();
	    final DefaultListModel listPaths = new DefaultListModel();

	    Listener listener = new Listener(selectedFile.getAbsolutePath());

	    for (int i = 0; i < listener.getListNames().size(); i++) {
		/*
		 * количество listNames совпадает с количеством listPaths
		 */
		listNames.addElement(listener.getListNames().get(i));
		listPaths.addElement(listener.getListPaths().get(i));
	    }

	    /*
	     * создание панели
	     */
	    final JPanel panel = new JPanel();
	    panel.setLayout(new BorderLayout(5, 5));
	    panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	    /*
	     * панель вывода списка
	     */
	    JPanel listPanel = new JPanel();
	    listPanel.setLayout(new BorderLayout(5, 5));
	    listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	    panel.add(listPanel, BorderLayout.CENTER);

	    /*
	     * прогресс бар показывает количество внесённых в бд записей
	     */
	    final JProgressBar jProgressbar = new JProgressBar();
	    listPanel.add(jProgressbar, BorderLayout.SOUTH);

	    /*
	     * компонент списка со скроллом
	     */
	    final JList list = new JList(listNames);
	    listPanel.add(new JScrollPane(list));

	    /*
	     * панель кнопок
	     */
	    JPanel buttonsPanel = new JPanel();
	    buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
	    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
	    panel.add(buttonsPanel, BorderLayout.SOUTH);

	    /*
	     * Кнопка "Добавить"
	     */
	    JButton folderButton = new JButton("Добавить");
	    folderButton.setFocusable(false);
	    buttonsPanel.add(folderButton);
	    folderButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    /**
		     * диалоговое окно фильтр установлен на ПАПКИ
		     */
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser
			    .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		    /*
		     * с помошью returnValue определим отклик пользователя была
		     * отмена или выбрана директория
		     */
		    int returnValue = fileChooser.showOpenDialog(new JLabel());

		    /*
		     * выбранная директория
		     */
		    File selectedFile = fileChooser.getSelectedFile();

		    /*
		     * далее только с правильной директорией
		     */
		    if (returnValue != JFileChooser.CANCEL_OPTION) {
			if (selectedFile != null) {
			    Listener listener = new Listener(selectedFile
				    .getAbsolutePath());

			    /*
			     * добавление элементов к предыдушему списку
			     */
			    for (int i = 0; i < listener.getListNames()
				    .getSize(); i++) {
				/*
				 * listNames - название файлов listPaths - пути
				 * к файлам listNames[i] <=> listPaths[i]
				 */
				listNames.addElement(listener.getListNames()
					.getElementAt(i));
				listPaths.addElement(listener.getListPaths()
					.getElementAt(i));
			    }
			}

			validate();
		    }
		}
	    });

	    /*
	     * Кнопка "Внести"
	     */
	    JButton addButton = new JButton("Внести");
	    addButton.setFocusable(false);
	    buttonsPanel.add(addButton);
	    addButton.addActionListener(new ActionListener() {
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
		    disable();

		    jProgressbar.setMaximum(listPaths.getSize());
		    jProgressbar.setMinimum(0);
		    jProgressbar.setValue(0);

		    Read46 aa;
		    try {
			aa = new Read46(listPaths);

			aa.execute();

			aa.addPropertyChangeListener(new PropertyChangeListener() {
			    public void propertyChange(PropertyChangeEvent evt) {

				if ("progress".equals(evt.getPropertyName())) {

				    int e = (Integer) evt.getNewValue();

				    switch (e) {
				    case 1:
				    case 2:

					jProgressbar.setValue(jProgressbar
						.getValue() + 1);
					listNames.remove(0);
					break;
				    case 3:
					enable();
					getContentPane().removeAll();
					try {
					    getContentPane()
						    .add(mainPanel = initMainPanel());
					} catch (SQLException e1) {
					    e1.printStackTrace();
					}
					validate();
					repaint();
					break;
				    }
				}
			    }
			});

		    } catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		    } catch (SQLException e2) {
			e2.printStackTrace();
		    }

		}
	    });

	    /*
	     * Кнопка "удалить из списка"
	     */
	    JButton removeButton = new JButton("Удалить из списка");
	    removeButton.setFocusable(false);
	    buttonsPanel.add(removeButton);

	    /**
	     * Удаление документа из списка на внесение
	     */
	    removeButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    /*
		     * Если не удалять, то всегда будет возвращаться true!
		     */
		    while (list.isSelectedIndex(list.getSelectedIndex())) {
			/**
			 * 1. удаляем элемент из списка адресса файла<br>
			 * 2. удаляем элемент из списка имен файла
			 */
			listPaths.removeElementAt(list.getSelectedIndex());
			listNames.removeElementAt(list.getSelectedIndex());
		    }
		}
	    });

	    /*
	     * Кнопка "назад"
	     */
	    JButton preButton = new JButton("Назад");
	    preButton.setFocusable(false);
	    buttonsPanel.add(preButton);
	    preButton.addActionListener(new ActionListener() {
		/*
		 * Очишает форму и возврашает к основной вкладке (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java .awt
		 * .event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
		    getContentPane().removeAll();
		    getContentPane().add(mainPanel);
		    validate();
		}
	    });

	    getContentPane().removeAll();
	    getContentPane().add(panel);
	    validate();
	    repaint();
	}

    }

    /**
     * Формирует таблицу организаций подавших документов
     * 
     * @param year
     *            - год подачи документа
     * @return панель с таблицей
     * @throws SQLException
     */
    private Component initYearPanel(int year) throws SQLException {

	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout(5, 5));
	panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	/**
	 * Таблица с запретом на редактирование ячеек
	 */
	final JTable jtable = new JTable() {

	    private static final long serialVersionUID = 2176392794023662702L;

	    // Запрет на редактирование ячеек
	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};

	jtable.setCellSelectionEnabled(true);

	panel.add(new JScrollPane(jtable), BorderLayout.CENTER);

	/**
	 * Получение данных из базы
	 */

	/**
	 * хранит строки таблицы
	 */
	Vector<Vector<String>> values = new Vector<Vector<String>>();

	/**
	 * Ищем все организации в базе за "year" год
	 */
	final Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Teplo.PATHTODB);
	Statement stat = conn.createStatement();

	for (String district : districts) {
	    /**
	     * Просматриваем каждый район Калужской области + Калуга и Обнинск
	     */

	    /**
	     * Хранит строки организаций "district" района.<br>
	     * Если будет хотя бы одна организация, temp склеивается с values
	     */
	    Vector<Vector<String>> temp = new Vector<Vector<String>>();

	    /**
	     * Поиск организаций, которые находятся в "district"
	     */
	    ResultSet presence_district = stat
		    .executeQuery("SELECT id, name, city FROM presence WHERE district LIKE '"
			    + district + "';");

	    while (presence_district.next()) {

		/**
		 * Организация была найдена
		 */

		/**
		 * Колонки строки.<br>
		 * 1. Наименование организации<br>
		 * 2. Остальные - это месяца и итог(год). Заполняются временно
		 * пустыми, далее ищутся совпадения документов по месяцам...
		 */
		Vector<String> element = new Vector<String>(16);
		element.add(presence_district.getString("name"));
		element.add(presence_district.getString("id"));
		element.add(presence_district.getString("city"));
		for (int i = 0; i < 13; i++)
		    element.add("");

		/**
		 * Поиск документов найденной организации за "year" год
		 */
		Statement stat2 = conn.createStatement();
		ResultSet title = stat2
			.executeQuery("SELECT year, month FROM title WHERE presenceid LIKE '"
				+ presence_district.getString("id") + "';");

		while (title.next()) {
		    /**
		     * Найден(ы) документ(ы), ставим знак "+" в месяце, за
		     * который был подан документ
		     */
		    for (int i = 0; i < months.length; i++) {
			if (title.getString("year").equals(
				Integer.toString(year))
				&& title.getString("month").equals(months[i])) {
			    /**
			     * Знак "+" - документ найден
			     */
			    element.set(i + 3, "+");
			}
		    }
		}

		title.close();
		stat2.close();

		temp.add(element);
	    }

	    presence_district.close();

	    /**
	     * Если были организации в районе, склеиваем данные с основном
	     * хранилищем(ну и выдумал, ппс)
	     */
	    if (temp.size() > 0) {
		/**
		 * Вставляем строчку с наименование района
		 */
		Vector<String> district_element = new Vector<String>(15);
		district_element.add(district);
		for (int i = 0; i < 15; i++)
		    district_element.add("");
		values.add(district_element);

		/**
		 * Склеиваем данные
		 */
		values.addAll(temp);
	    }
	}

	conn.close();

	/**
	 * "Шапка таблицы"
	 */
	Vector<String> header = new Vector<String>();
	header.add("Организация");
	header.add("id");
	header.add("Город");
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

	/**
	 * Внесение данных в таблицу
	 */
	DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
	dtm.setDataVector(values, header);

	/**
	 * Установка ширины столбцов
	 */
	for (int i = 0; i < header.size(); i++) {
	    int max = 200;
	    int min = 50;

	    switch (i) {
	    case 0:
		/**
		 * Наименование организаци
		 */
		max = 1000;
		min = 300;
		break;
	    case 1:
		/**
		 * Id
		 */
		max = 200;
		min = 25;
		break;
	    case 2:
		/**
		 * Наименование города, в котором находится организация
		 */
		max = 300;
		min = 100;
		break;
	    }

	    jtable.getColumnModel().getColumn(i).setMaxWidth(max);
	    jtable.getColumnModel().getColumn(i).setMinWidth(min);
	}

	// jtable.setSelectionMode(year);

	/**
	 * Задание цветов ячеек<br>
	 * дополнительное редактирование ячеек
	 */
	jtable.setDefaultRenderer(jtable.getColumnClass(1),
		new DefaultTableCellRenderer() {

		    /**
		     * serialVersionUID
		     */
		    private static final long serialVersionUID = -8385589526561785995L;

		    public Component getTableCellRendererComponent(
			    JTable table, Object value, boolean isSelected,
			    boolean hasFocus, int row, int column) {
			if (column < 1) {
			    /**
			     * название организации<br>
			     * выравние по левому краю
			     */
			    super.setHorizontalAlignment(SwingConstants.LEFT);

			    /**
			     * Если строка наименование района, то строка
			     * окрашивается в серый цвет
			     */
			    for (String district : districts) {
				if (district.equals(value)) {
				    super.setBackground(Color.LIGHT_GRAY);
				    break;
				}
			    }
			} else {
			    /**
			     * в ячейках месяцев
			     */

			    /**
			     * выравние по центру
			     */
			    super.setHorizontalAlignment(SwingConstants.CENTER);

			    /**
			     * определение содержимого ячейки
			     */
			    if (value.equals("+"))
				/**
				 * Документ есть
				 */
				super.setBackground(Color.GREEN);
			    else
				/**
				 * Документа не было
				 */
				super.setBackground(Color.WHITE);
			}

			super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

			return this;
		    }

		});

	jtable.addMouseListener(new MouseAdapter() {
	    public void mouseReleased(MouseEvent Me) {
		/**
		 * Условие:<br>
		 * 1. Выделено более одной строчки<br>
		 * 2. Была нажата правая кнопка мыши<br>
		 * 3. Первая строчка является организацией<br>
		 */
		if (0 < jtable.getSelectedRows().length
			&& Me.isMetaDown()
			&& jtable.getValueAt(jtable.getSelectedRows()[0], 1)
				.equals("") != true) {

		    JPopupMenu Pmenu = new JPopupMenu();

		    JMenuItem delPresence = new JMenuItem("Удалить организацию");
		    JMenuItem delTitle = new JMenuItem("Удалить документ");

		    delPresence.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    for (int row : jtable.getSelectedRows()) {
				if (jtable.getValueAt(row, 1).equals("") != true) {
				    String presence_id = (String) jtable
					    .getValueAt(row, 1).toString();

				    try {
					delPresence(presence_id, Integer
						.toString(years[tabbedPane
							.getSelectedIndex()]));
				    } catch (SQLException e) {
					e.printStackTrace();
				    }
				}
			    }

			    try {
				getContentPane().removeAll();
				getContentPane().add(
					mainPanel = initMainPanel());
				validate();
				repaint();
			    } catch (SQLException e) {
				e.printStackTrace();
			    }
			}
		    });

		    delTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			    for (int row : jtable.getSelectedRows()) {
				if (jtable.getValueAt(row, 1).equals("") != true) {
				    String presence_id = (String) jtable
					    .getValueAt(row, 1).toString();
				    for (int column : jtable
					    .getSelectedColumns()) {
					try {
					    delTitle(
						    presence_id,
						    months[column - 3],
						    Integer.toString(years[tabbedPane
							    .getSelectedIndex()]));
					} catch (SQLException e) {
					    e.printStackTrace();
					}
				    }
				}
			    }

			    try {
				getContentPane().removeAll();
				getContentPane().add(
					mainPanel = initMainPanel());
				validate();
				repaint();
			    } catch (SQLException e) {
				e.printStackTrace();
			    }
			}

		    });

		    Pmenu.add(delPresence);

		    if (jtable.getSelectedColumn() > 2)
			Pmenu.add(delTitle);

		    Pmenu.show(Me.getComponent(), Me.getX(), Me.getY());
		}
	    }
	});

	return panel;
    }

    private void delPresence(String presenceid, String year)
	    throws SQLException {

	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Teplo.PATHTODB);

	Statement stat = conn.createStatement();
	ResultSet title = stat
		.executeQuery("SELECT id FROM title WHERE presenceid LIKE '"
			+ presenceid + "' AND year LIKE '" + year + "';");

	Statement stat2 = conn.createStatement();
	while (title.next()) {
	    stat2.executeUpdate("DELETE FROM otpusk WHERE titleid = '"
		    + title.getString("id") + "';");
	}

	stat2.close();
	title.close();

	stat.executeUpdate("DELETE FROM title WHERE presenceid = '"
		+ presenceid + "';");

	stat.executeUpdate("DELETE FROM presence WHERE id = '" + presenceid
		+ "';");

	stat.close();
	conn.close();
    }

    private void delTitle(String presence_id, String month, String year)
	    throws SQLException {
	Connection conn = DriverManager.getConnection("jdbc:sqlite:"
		+ Teplo.PATHTODB);
	Statement stat = conn.createStatement();
	ResultSet title = stat
		.executeQuery("SELECT id FROM title WHERE presenceid LIKE '"
			+ presence_id + "' AND month LIKE '" + month
			+ "' AND year LIKE '" + year + "';");

	Statement stat2 = conn.createStatement();
	while (title.next()) {
	    stat2.executeUpdate("DELETE FROM otpusk WHERE titleid = '"
		    + title.getString("id") + "';");
	}

	stat.executeUpdate("DELETE FROM title WHERE presenceid = '"
		+ presence_id + "' AND month LIKE '" + month
		+ "' AND year LIKE '" + year + "';");

	stat2.close();
	title.close();

	stat.close();
	conn.close();
    }
}
