package base;

import javax.swing.JProgressBar;

public class ThreadPrgogress extends Thread
{
	private JProgressBar jProgressbar = null;
	
	@Override
	public void run()
	{
	}
	public void  setProgressBar(JProgressBar jProgressbar)
	{
		this.jProgressbar = jProgressbar;
	}
	public void  setValue(int value)
	{
		this.jProgressbar.setValue(value);
	}
}
