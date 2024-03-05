package com.example.demo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Parsing {
	static JsonArray[] output(JsonObject stobj,String number) 
	{
		//создание объектов json по кодовым словам
		 JsonObject group = stobj.getAsJsonObject(number);
		 JsonObject days = group.getAsJsonObject("days");
		 JsonObject[] week = new JsonObject[7];
		 JsonArray[] lessons = new JsonArray[7];
			  for(Integer i=0;i<7;i++) 
			  { 
				  week[i]=days.getAsJsonObject(i.toString());
				  lessons[i]=week[i].getAsJsonArray("lessons");
			  }
			 
		return lessons;
		 
	}
}
