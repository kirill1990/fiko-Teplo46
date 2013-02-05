package base;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CollectionStrings
{
	private CollectionString[]	strings	= new CollectionString[29];
	private int					index	= 0;

	public CollectionStrings()
	{
		for (int i = 0; i < strings.length; i++)
		{
			strings[i] = new CollectionString();
		}
	}

	//Задаём номер заполняемой строки
	public void setIndex(int i)
	{
		if (i < 29)
		{
			index = i;
		}
		else
		{
			index = 0;
		}
	}

	//Задаём значения строк
	//Объём
	public void setValueVall(double value)
	{
		strings[index].vall += value;
		strings[index].vall = new BigDecimal(strings[index].vall).setScale(3, RoundingMode.HALF_UP).doubleValue();

	}

	public void setValueVpribor(double value)
	{
		strings[index].vpribor += value;
		strings[index].vpribor = new BigDecimal(strings[index].vpribor).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}

	public void setValueVraschet(double value)
	{
		strings[index].vraschet += value;
		strings[index].vraschet = new BigDecimal(strings[index].vraschet).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	//Стоимость
	public void setValueSall(double value)
	{
		strings[index].sall += value;
		strings[index].sall = new BigDecimal(strings[index].sall).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}

	public void setValueSpribor(double value)
	{
		strings[index].spribor += value;
		strings[index].spribor = new BigDecimal(strings[index].spribor).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}

	public void setValueSraschet(double value)
	{
		strings[index].sraschet += value;
		strings[index].sraschet = new BigDecimal(strings[index].sraschet).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	//Задаём значения строк
	//Объём
	public String getValueVall()
	{
		return Double.toString(strings[index].vall);
	}

	public String getValueVpribor()
	{
		return Double.toString(strings[index].vpribor);
	}

	public String getValueVraschet()
	{
		return Double.toString(strings[index].vraschet);
	}
	
	//Стоимость
	public String getValueSall()
	{
		return Double.toString(strings[index].sall);
	}

	public String getValueSpribor()
	{
		return Double.toString(strings[index].spribor);
	}

	public String getValueSraschet()
	{
		return Double.toString(strings[index].sraschet);
	}
}
