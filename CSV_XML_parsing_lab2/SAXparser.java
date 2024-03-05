package lab2_kotlin;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SAXparser 
{
	public void parse(String file_path)
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXhandler hndl = new SAXhandler();
		SAXParser parser = null;
		try 
		{
			parser = factory.newSAXParser();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		File file = new File(file_path);
		try 
		{
			parser.parse(file, hndl);
		} 
		catch (Exception e) 
		{
			 System.out.println("Неверный путь файла или файл создан неверно");
			 return;
		}
	}
}
