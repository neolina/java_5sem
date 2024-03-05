package lab2_kotlin;
import java.io.BufferedReader;
import java.io.IOException;

public class CSV 
{
	public static int i=0;
	public static void parse(BufferedReader br) throws NumberFormatException, IOException
	{
		String line=null;
		while ((line = br.readLine()) != null) 
		{
			if(i!=0)
			{ 
				line = line.replaceAll("\"", "");
				String[] attr = line.split(";");
				try
				{
					HashMapping.building(attr[0],attr[1],Integer.parseInt(attr[2]), Integer.parseInt(attr[3]));
				}
				catch(Exception e)
				{
					System.out.println("Неверная система внутри файла");
					return;
				}
			}
			else
			i++;
		}
	}

}
