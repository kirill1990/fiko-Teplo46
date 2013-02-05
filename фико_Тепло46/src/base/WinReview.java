package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class WinReview extends JFrame
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public final static int		WIDTH				= 950;
	public final static int		HEIGHT				= 650;

	private String				current_id			= null;
	public JLabel				jFolderLabel		= null;

	public WinReview(Object _id)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);

		setTitle("");
		setSize(WIDTH, HEIGHT);

		current_id = _id.toString();

		// создание формы
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(5, 5));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		// создание панели кнопок
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
		// mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		// создание панели с таблицой
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));
		// mainPanel.add(tablePanel, BorderLayout.CENTER);

		// Создание панели вкладок
		JTabbedPane tabbedPane = new JTabbedPane();
		// шрифт вкладок
		Font font = new Font("Verdana", Font.PLAIN, 12);
		tabbedPane.setFont(font);
		// доавбление панелей во вкладки
		tabbedPane.addTab("Титульник", getTitlePanel());
		tabbedPane.addTab("Отпуск ТЭ в паре", getParaPanel());
		tabbedPane.addTab("Отпуск ТЭ в гор воде", getGorPanel());

		mainPanel.add(tabbedPane);
		// добавление всех элементов на форму
		getContentPane().add(mainPanel);
		// обновление формы
		validate();
		// показ формы пользователю
		setVisible(true);
	}

	private JPanel getGorPanel()
	{
		// создание панели поиска
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));

		@SuppressWarnings("serial") JTable jtable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		jtable.setRowSelectionAllowed(true);
		tablePanel.add(new JScrollPane(jtable), null);

		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_GOR(current_id);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("Код");
		header.add("Наименование");
		header.add("всего");
		header.add("по приборам учета");
		header.add("определенный расчетным методом");
		header.add("всего");
		header.add("по приборам учета");
		header.add("определенный расчетным методом");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);

		// код
		jtable.getColumnModel().getColumn(0).setMaxWidth(30);
		jtable.getColumnModel().getColumn(0).setMinWidth(30);
		// расшифровка кода
		jtable.getColumnModel().getColumn(1).setMaxWidth(1000);
		jtable.getColumnModel().getColumn(1).setMinWidth(400);
		// информация
		for (int i = 2; i < 8; i++)
		{
			jtable.getColumnModel().getColumn(i).setMaxWidth(200);
			jtable.getColumnModel().getColumn(i).setMinWidth(80);
		}

		return tablePanel;
	}

	private JPanel getParaPanel()
	{
		// создание панели поиска
		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new GridLayout(1, 2, 5, 0));

		@SuppressWarnings("serial") JTable jtable = new JTable()
		{
			// Запрет на редактирование ячеек
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		jtable.setRowSelectionAllowed(true);
		tablePanel.add(new JScrollPane(jtable), null);

		// Получаю данные из БД
		@SuppressWarnings("rawtypes") Vector values = new ConnectionBD().getDataFromDB_PARA(current_id);

		// "Шапка" - т.е. имена полей
		Vector<String> header = new Vector<String>();
		header.add("Код");
		header.add("Наименование");
		header.add("всего");
		header.add("по приборам учета");
		header.add("определенный расчетным методом");
		header.add("всего");
		header.add("по приборам учета");
		header.add("определенный расчетным методом");

		// Помещаю в модель таблицы данные
		DefaultTableModel dtm = (DefaultTableModel) jtable.getModel();
		// Сначала данные, потом шапка
		dtm.setDataVector(values, header);

		// код
		jtable.getColumnModel().getColumn(0).setMaxWidth(30);
		jtable.getColumnModel().getColumn(0).setMinWidth(30);
		// расшифровка кода
		jtable.getColumnModel().getColumn(1).setMaxWidth(1000);
		jtable.getColumnModel().getColumn(1).setMinWidth(400);
		// информация
		for (int i = 2; i < 8; i++)
		{
			jtable.getColumnModel().getColumn(i).setMaxWidth(200);
			jtable.getColumnModel().getColumn(i).setMinWidth(80);
		}

		return tablePanel;
	}

	private JPanel getTitlePanel()
	{
		// создание панели поиска
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new GridLayout(22, 2));

		for (int i = 0; i < 22; i++)
		{
			titlePanel.add(getLabelPanel(i), null);
		}

		return titlePanel;
	}

	private JPanel getLabelPanel(int index)
	{
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BorderLayout(5, 0));
		// new ConnectionBD()
		@SuppressWarnings("rawtypes") 
		Vector values = new ConnectionBD().getDataFromDB_title(current_id);

		//Заголовок окна с названием предприятия и отчетного периода
		this.setTitle(values.get(3).toString() + " - " + values.get(1) + " " + values.get(2));
		
		JTextField textField = new JTextField("");
		textField.setEditable(false);
		textField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		// textField.setHorizontalAlignment(JTextField.RIGHT);
		JLabel jLabel = new JLabel("");

		switch (index)
		{
			case 0:
			{
				textField.setText(values.get(3).toString());
				textField.setHorizontalAlignment(JTextField.CENTER);
				Font font = new Font("", Font.BOLD, 12);
				textField.setFont(font);
				labelPanel.add(textField, null);
				break;
			}
			case 1:
			{
				jLabel.setText("Отчетный период: ");
				textField.setText(values.get(1) + " " + values.get(2));
				break;
			}
			case 2:
			{
				jLabel.setText("Муниципальный район");
				textField.setText(values.get(7).toString());
				break;
			}
			case 3:
			{
				jLabel.setText("Муниципальное образование: ");
				textField.setText(values.get(8).toString());
				break;
			}
			case 4:
			{
				jLabel.setText("ОКТМО: ");
				textField.setText(values.get(9).toString());
				break;
			}
			case 5:
			{
				jLabel.setText("ИНН: ");
				textField.setText(values.get(4).toString());
				break;
			}
			case 6:
			{
				jLabel.setText("КПП: ");
				textField.setText(values.get(5).toString());
				break;
			}
			case 7:
			{
				jLabel.setText("Код по ОКПО: ");
				textField.setText(values.get(6).toString());
				break;
			}
			case 8:
			{
				textField.setText("Адрес организации");
				Font font = new Font("", Font.BOLD, 12);
				textField.setFont(font);
				textField.setHorizontalAlignment(JTextField.CENTER);
				break;
			}
			case 9:
			{
				jLabel.setText("Юридический адрес: ");
				textField.setText(values.get(10).toString());
				break;
			}
			case 10:
			{
				jLabel.setText("Почтовый адрес: ");
				textField.setText(values.get(11).toString());
				break;
			}
			case 11:
			{
				textField.setText("Руководитель");
				Font font = new Font("", Font.BOLD, 12);
				textField.setFont(font);
				textField.setHorizontalAlignment(JTextField.CENTER);
				break;
			}
			case 12:
			{
				jLabel.setText("Фамилия, имя, отчество: ");
				textField.setText(values.get(12).toString());
				break;
			}
			case 13:
			{
				jLabel.setText("Контактный телефон: ");
				textField.setText(values.get(13).toString());
				break;
			}
			case 14:
			{
				textField.setText("Главный бухгалтер");
				Font font = new Font("", Font.BOLD, 12);
				textField.setFont(font);
				textField.setHorizontalAlignment(JTextField.CENTER);
				break;
			}
			case 15:
			{
				jLabel.setText("Фамилия, имя, отчество: ");
				textField.setText(values.get(14).toString());
				break;
			}
			case 16:
			{
				jLabel.setText("Контактный телефон: ");
				textField.setText(values.get(15).toString());
				break;
			}
			case 17:
			{
				textField.setText("Должностное лицо, ответственное за составление формы");
				Font font = new Font("", Font.BOLD, 12);
				textField.setFont(font);
				textField.setHorizontalAlignment(JTextField.CENTER);
				break;
			}
			case 18:
			{
				jLabel.setText("Фамилия, имя, отчество: ");
				textField.setText(values.get(16).toString());
				break;
			}
			case 19:
			{
				jLabel.setText("Должность: ");
				textField.setText(values.get(17).toString());
				break;
			}
			case 20:
			{
				jLabel.setText("Контактный телефон: ");
				textField.setText(values.get(18).toString());
				break;
			}
			case 21:
			{
				jLabel.setText("e-mail: ");
				textField.setText(values.get(19).toString());
				break;
			}
		}
		labelPanel.add(jLabel, BorderLayout.WEST);
		labelPanel.add(textField, null);

		return labelPanel;
	}
}
