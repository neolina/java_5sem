package lab2_kotlin;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main 
{
	
	public static void main(String args[])
	{
		String exit="f";
		BufferedReader br=null;
		Scanner in = new Scanner(System.in);
		while(exit.equals("0")==false) 
		{ 
			  System.out.println("Введите путь до файла или \"0\" для выхода"); 
			  exit = in.nextLine();
			  if(exit.equals("0")==false)
			  {
				  try 
				  {
					br = new BufferedReader(new FileReader(exit));
				  }
				  catch (FileNotFoundException e) 
				  {
					  System.out.println("Невозможно найти такой файл"); 
					  continue;
				  }
				  if(exit.endsWith("xml")==false&&exit.endsWith("csv")==false)
				  {
					  System.out.println("Неверный формат файла"); 
				  }
				  else
				  {
					  long startTime = System.currentTimeMillis();
					  
					  if(exit.endsWith("xml")==true) 
					  {
						  SAXparser parser = new SAXparser();
						  parser.parse(exit);
					  }
					  if(exit.endsWith("csv")==true)
					  {
						  try 
						  {
							  CSV.parse(br);
						  } 
						  catch (Exception e) 
						  {
							  e.printStackTrace();
						  } 
					  }
					  
					  long endTime = System.currentTimeMillis();
					  
					  if(HashMapping.city_plus_floors.isEmpty()==false)
					  {  
						  System.out.println("\nСтатистика повторов:\n");  
						  for (Map.Entry<String, Integer> entry : HashMapping.copy.entrySet()) 
					  	  {
							  System.out.println("Повтор: " + entry.getKey() + " Количество: " + entry.getValue());
					  	  }
					  	  System.out.println("\nСтатистика этажей:\n"); 
					  	  for (Map.Entry<String,HashMap<Integer,Integer>> entry : HashMapping.city_plus_floors.entrySet()) 
					  	  {
					  		  System.out.printf("Город: %-17s",entry.getKey());
					  		  System.out.printf("Этажи: %-10s\n",entry.getValue().toString().replace("{", "").replace("}", "").replace("=", " = "));
					  	  }
					  	  System.out.println("\nВремя работы: " + (endTime-startTime) +"mls\n"); 
					  }
				  }
			  }
			  HashMapping.city.clear();
			  HashMapping.copy.clear();
			  HashMapping.city_plus_floors.clear();
			  CSV.i=0;
		}
		System.out.println("Окончание работы"); 
		in.close();
    }
}
