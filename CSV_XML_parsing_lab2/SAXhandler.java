package lab2_kotlin;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.*;
public class SAXhandler extends DefaultHandler
{
	 @Override
     public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException 
	 { 
         int attributeLength = attributes.getLength();
         String attr[] = new String[attributeLength];
         if ("item".equals(qName)) 
         {
             for (int i = 0; i < attributeLength; i++) 
             {
                 attr[i] = attributes.getValue(i);
             }
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
         else if("item".equals(qName)==false&&"root".equals(qName)==false)
         {
        	 System.out.println("Неверная система внутри файла");
        	 return;
         }
     }
 }

