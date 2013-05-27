package base;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultListModel;

public class Listener
{
	private DefaultListModel	listNames	= null;//Список имен файлов
	private DefaultListModel	listPaths	= null;//Список пути к файлу

	public Listener(String _pathname)
	{
		listNames	= new DefaultListModel();
		listPaths	= new DefaultListModel();
		tree(_pathname);
	}
	
	private File[] getFiles(String pathname)
	{
		File			path;
		File[]			files;
		
		path = new File(pathname);
		if (!path.exists())
		{
			try
			{
				throw new IOException("Cannot access " + pathname
						+ ": No such file or directory");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		if (path.isFile())
		{
			files = new File[] { path };
		}
		else
		{
			files = path.listFiles();
			Arrays.sort(files, new FilesComparator());
		}
		return files;
	}
	
	//Ищем файлы в дереве каталогов
	private void tree(String pathname)
	{
		//определяем список файлов
		File[] files =  getFiles(pathname);
		//определяем путь каталога
		File path = new File(pathname);
		//проверяем все найденые элементы
		for (File f : files)
		{
			if (f.isDirectory())
			{
				//если элемент - это директория
				//просматриваем след ветвь
				tree(path.getAbsolutePath() + "/" + f.getName());
			}
			else
			{
				//если элемент - это файл

				if(f.getName().lastIndexOf(".xls")>0 && f.getName().lastIndexOf(".xlsm")==-1 && f.getName().lastIndexOf("~")==-1)
				{
					listNames.addElement(f.getName());
					listPaths.addElement(f.getAbsolutePath());
				}
			}
		}
	}
	
	
	private class FilesComparator implements Comparator<File>
	{
		public int compare(File f1, File f2)
		{
			if (f1.isDirectory() && f2.isFile())
			{
				return -1;
			}
			if (f1.isFile() && f2.isDirectory())
			{
				return 1;
			}
			return f1.compareTo(f2);
		}
	}
	
	
	public DefaultListModel getListNames()
	{
		return listNames;
	}
	
	public DefaultListModel getListPaths()
	{
		return listPaths;
	}
}
