package base;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

public class WinAddExcel extends JFrame
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// Размеры окна:
	// ширина
	public final static int		WIDTH				= 600;
	// высота
	public final static int		HEIGHT				= 250;

	// Списки:
	// имен excel файлов
	private DefaultListModel	listNames			= null;
	// адреса в ФС к excel файлам
	private DefaultListModel	listPaths			= null;

	// необходимые глобальные компоненты
	// реадизации диалога
	private JLabel				jFolderLabel		= null;
	// реализации списка
	private JList				list				= null;
	// реализации прогресс бара
	private JProgressBar		jProgressbar		= null;

	// ссылка на осн окно
	// необходим для реализиации фонового потока
	private JFrame				obj					= null;

	public WinAddExcel()
	{
		// определение размера рабочей области пользователя
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		// выставляем новое окно по центру
		setLocation((screenSize.width - WIDTH) / 2, (screenSize.height - HEIGHT) / 2);
		// задаём титульник
		setTitle("Добавление таблиц");
		// задаём размеры окна
		setSize(WIDTH, HEIGHT);
		// сохраняем ссылку на этот JFrame
		obj = this;

		// Создание диалога выбора файла
		JFileChooser fileChooser = new JFileChooser();
		// Добавление фильтра в диалог выбора файла
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		// Сам диалог
		int returnValue = fileChooser.showOpenDialog(jFolderLabel);
		// Адрес выбранного пути
		File selectedFile = fileChooser.getSelectedFile();

		// Проверяем на нажатие отмены в диалоге
		if (returnValue == JFileChooser.CANCEL_OPTION)
		{
			// была нажата отмена
			dispose();
		}
		else
		{
			// выбрана кнопка "открыть" в диалоге
			setVisible(true);

			// Проверка на существование пути
			if (selectedFile != null)
			{
				// каталог - существует
				// просматриваем его дерево
				Listener listener = new Listener(selectedFile.getAbsolutePath());
				// и возврашаем полученные данные в списки
				listNames = new DefaultListModel();
				listPaths = new DefaultListModel();
				// получаем все файлы с разрешением .xls
				listNames = listener.getListNames();
				// пути к этим файлам
				listPaths = listener.getListPaths();
			}

			// создание формы
			JPanel mainPanel = new JPanel();
			// Задаём отступы
			mainPanel.setLayout(new BorderLayout(5, 5));
			mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

			// организация вывода списка
			JPanel listPanel = new JPanel();
			// отступы
			listPanel.setLayout(new BorderLayout(5, 5));
			listPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			// добавляем к осн панели
			mainPanel.add(listPanel, BorderLayout.CENTER);
			// компонент списка
			list = new JList(listNames);
			// компонент прогресс бар
			jProgressbar = new JProgressBar();
			// компоновка
			listPanel.add(new JScrollPane(list));
			listPanel.add(jProgressbar, BorderLayout.SOUTH);

			// создание панели кнопок
			JPanel buttonsPanel = new JPanel();
			// отступы
			buttonsPanel.setLayout(new GridLayout(1, 2, 5, 0));
			buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
			// добавляем к осн панели
			mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

			// Кнопка "новый путь"
			JButton folderButton = new JButton("Добавить");
			folderButton.setFocusable(false);
			folderButton.addActionListener(new BtActionListener());
			buttonsPanel.add(folderButton);

			// Кнопка "добавить в бд"
			JButton addButton = new JButton("Записать в бд");
			addButton.setFocusable(false);
			addButton.addActionListener(new addActionListener());
			buttonsPanel.add(addButton);

			// Кнопка "удалить"
			JButton removeButton = new JButton("Удалить из списка");
			removeButton.setFocusable(false);
			removeButton.addActionListener(new removeActionListener());
			buttonsPanel.add(removeButton);

			// добавление всех элементов на форму
			getContentPane().add(mainPanel);
			// обновление формы
			validate();
		}
	}

	// Добавление новых файлов в список
	public class BtActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// Создание диалога выбора файла
			JFileChooser fileChooser = new JFileChooser();
			// Добавление фильтра в диалог выбора файла
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			// Сам диалог
			int returnValue = fileChooser.showOpenDialog(jFolderLabel);
			File selectedFile = fileChooser.getSelectedFile();

			// Определяет нажатие кнопки "отмена"
			if (returnValue != JFileChooser.CANCEL_OPTION)
			{
				// не нажата
				// смотри на наличие файла/пути
				if (selectedFile != null)
				{
					// каталог - существует
					// просматриваем его дерево
					Listener listener = new Listener(selectedFile.getAbsolutePath());
					// добавление к основному списку
					for (int i = 0; i < listener.getListNames().getSize(); i++)
					{
						// и получаем все файлы с разрешением .xls
						listNames.addElement(listener.getListNames().getElementAt(i));
						// и пути к этим файлам
						listPaths.addElement(listener.getListPaths().getElementAt(i));
					}
				}
				// принудительное обновление JFrame
				validate();
			}
		}
	}

	// Кнопка "Добавить в БД"
	// Заносит в бд новые таблицы
	public class addActionListener implements ActionListener
	{
		// Клик по кнопке
		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e)
		{
			// Устанавливаем параметры прогресс бара
			jProgressbar.setMaximum(listPaths.getSize());
			jProgressbar.setMinimum(0);
			jProgressbar.setValue(0);

			// Все вычисления выполняем в фоновом потоке
			BDaddTable thread = new BDaddTable();
			// В него передётся ссылки на фрейм, прогрессбар,
			// ссылки на списки с именами excel файлов и их путями
			// определяет процент выполнения
			thread.setJProgressBar(jProgressbar);
			// для уничтожения фрейма obj.dispose()
			thread.setFrame(obj);
			// с listPaths считвается пути к файлам
			thread.setListPaths(listPaths);
			// с listName удаляется в ходе выполнение записи(имена excel
			// файлов),
			// которые занесены в бд (для удобства пользователя)
			thread.setListNames(listNames);
			// запускаем поток
			thread.execute();
			// защита от дурака
			obj.disable();
		}
	}

	// Удаление выбранных элементов
	public class removeActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			// Проссматриваем все выделенные элементы
			// Если не удалять, то всегда будет возвращаться true!
			while (list.isSelectedIndex(list.getSelectedIndex()))
			{
				// удаляем элемент из списка адресса файла
				listPaths.removeElementAt(list.getSelectedIndex());
				// удаляем элемент из списка имен файла
				listNames.removeElementAt(list.getSelectedIndex());
			}
		}
	}
}
