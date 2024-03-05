import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Parsing 
{

	static String[] output(JsonObject stobj) 
	{
		//создание объектов json по кодовым словам
		 JsonObject dataobj = stobj.getAsJsonObject("query");
         JsonArray search = dataobj.getAsJsonArray("search");
         String[] pageid =new String[search.size()];//строка для хранения pageid
         if(pageid.length!=0)
         {
         System.out.print("\nВыберите нужную статью, введя ее номер: \n");
         for(int i=0;i<search.size();i++)
         {
         	JsonObject temp = search.get(i).getAsJsonObject();//получение i-того поля массива
         	System.out.printf("Статья (%d): %s\n",i+1, temp.getAsJsonPrimitive("title").toString());
         	pageid[i]= temp.getAsJsonPrimitive("pageid").toString();
         }
         }
         
      return pageid;   
	}
}
