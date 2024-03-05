package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Schedule_functions {
	static String[] dayweek = {" Понедельник "," Вторник "," Среда "," Четверг "," Пятница "," Суббота "," Воскресенье "};
	 
	
	public static String week()
	{
	
		Date start = null;

	    try {
	        start = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2023"); 
	    } catch (ParseException exc) {
	        exc.printStackTrace();
	    }

	    long delay = System.currentTimeMillis() - start.getTime(); 
	    long week = 1000 * 60 * 60 * 24 * 7; 
	    delay %= week * 2; 

	    if (delay <= week) 
	    	return "\"1\""; 
	    else 
	    	return "\"2\"";
	}
	public static String get_near(JsonArray[] schedule)
	{
		 LocalDate date = LocalDate.now();
		 int hours= LocalTime.now().getHour();
		 int minutes =LocalTime.now().getMinute();
		int dow=date.getDayOfWeek().getValue()-1;
		 String quanity = week();
		 JsonArray needed_day=schedule[dow];
		 JsonObject lesson = new JsonObject();
		 boolean found = false;

	for(int i=0;i<needed_day.size();i++)
	{
		
		lesson = needed_day.get(i).getAsJsonObject();
		String endTime = lesson.getAsJsonPrimitive("end_time").toString();
		endTime=endTime.replace("\"", "");
		String[] parts = endTime.split(":");
		int lhour=Integer.valueOf(parts[0]);
		int lmin=Integer.valueOf(parts[1]);
		if(hours<lhour||(hours==lhour&&minutes<=lmin)&&((quanity==lesson.getAsJsonPrimitive("week").toString())||(lesson.getAsJsonPrimitive("week").toString().equals("\"3\""))))
		{
			found=true;
			break;
		}
		
	}
	
	  while(found==false) 
	  { dow=(dow+1)%7; 
	  if(dow==0) 
	  { 
		  if(quanity.equals("\"1\""))
	  quanity="\"2\""; 
		  else 
			  quanity="\"1\""; 
		  } 
	  needed_day=schedule[dow];
	  if(needed_day.isEmpty()==false) 
	  { 
		  for(int i=0;i<needed_day.size();i++) 
		  {
	  lesson = needed_day.get(i).getAsJsonObject();
	  if((quanity.equals(lesson.getAsJsonPrimitive("week").toString()))||(lesson. getAsJsonPrimitive("week").toString().equals("3")==true)) { found=true;
	  break; } }
	  
	  } }
	  String fin;
	  fin=dayweek[dow] +"\nНазвание: "+ lesson.getAsJsonPrimitive("name").toString()+"\n"+"Тип: "+lesson.getAsJsonPrimitive("subjectType").toString()+"\nВремя: "+lesson.getAsJsonPrimitive("start_time").toString() + " - " + lesson.getAsJsonPrimitive("end_time").toString()+"\n";
		if(lesson.getAsJsonPrimitive("room").toString().equals("\"\"")==false)
			fin+="Аудитория: "+lesson.getAsJsonPrimitive("room").toString();
	return fin;

	}
	public static String get_day(JsonArray[] schedule,int day)
	{
		JsonArray needed_day=schedule[day];
		if(needed_day.isEmpty()==false)
		{ String fin=dayweek[day]+"\n";
		for(int i=0;i<needed_day.size();i++)
		{
			
			JsonObject lesson = needed_day.get(i).getAsJsonObject();	
			fin+="Название: "+lesson.getAsJsonPrimitive("name").toString()+"\n";
			fin+="Тип: "+lesson.getAsJsonPrimitive("subjectType").toString()+"\n";
			if(lesson.getAsJsonPrimitive("week").toString().equals("\"3\"")==false)
				 fin+="Четность недели : "+lesson.getAsJsonPrimitive("week").toString()+"\n";
			fin+="Время: "+lesson.getAsJsonPrimitive("start_time").toString() + " - " + lesson.getAsJsonPrimitive("end_time").toString()+"\n";
			if(lesson.getAsJsonPrimitive("room").toString().equals("\"\"")==false)
			fin+="Аудитория: "+lesson.getAsJsonPrimitive("room").toString()+"\n";
			
			
		}
		return fin;
		}
		else
			return dayweek[day]+"\n"+"Нет занятий в этот день!";
	}
	public static String get_tomorrow(JsonArray[] schedule)
	{
		 LocalDate date = LocalDate.now();
		int dow=date.getDayOfWeek().getValue()-1;
		 String quanity = week();
		dow=dow+1;
		JsonArray needed_day=schedule[dow];
		while(needed_day.isEmpty()==true)
		{
			dow=(dow+1)%7;
			 if(dow==0) 
			  { 
				  if(quanity.equals("\"1\""))
			  quanity="\"2\""; 
				  else 
					  quanity="\"1\""; 
				  } 
			needed_day=schedule[dow];
		}
		String fin=dayweek[dow]+"\n";
		for(int i=0;i<needed_day.size();i++)
		{
			
			JsonObject lesson = needed_day.get(i).getAsJsonObject();	
			if(lesson.getAsJsonPrimitive("week").toString().equals("\"3\"")||quanity.equals(lesson.getAsJsonPrimitive("week").toString()))
				{
				fin+="Название: "+lesson.getAsJsonPrimitive("name").toString()+"\n";
				fin+="Время: "+lesson.getAsJsonPrimitive("start_time").toString() + " - " + lesson.getAsJsonPrimitive("end_time").toString()+"\n";
				fin+="Тип: "+lesson.getAsJsonPrimitive("subjectType").toString()+"\n";
				if(lesson.getAsJsonPrimitive("room").toString().equals("\"\"")==false)
				fin+="Аудитория: "+lesson.getAsJsonPrimitive("room").toString()+"\n";
				}
			
			
		}
		return fin;
	}
	public static String get_week(JsonArray[] schedule)
	{
		String fin = new String();
		for(int i=0;i<7;i++)
		{
			JsonArray needed_day=schedule[i];
			if(needed_day.isEmpty()==false)
			 fin+=(dayweek[i]);
		for(int j=0;j<needed_day.size();j++)
		{
			if(needed_day.isEmpty()==false)
			{
				 JsonObject lesson = needed_day.get(j).getAsJsonObject();
					fin+="Название: "+lesson.getAsJsonPrimitive("name").toString()+"\n";
					fin+="Тип: "+lesson.getAsJsonPrimitive("subjectType").toString()+"\n";
				if(lesson.getAsJsonPrimitive("week").toString().equals("\"3\"")==false)
				 fin+="Четность недели : "+lesson.getAsJsonPrimitive("week").toString()+"\n";
				fin+="Время: "+lesson.getAsJsonPrimitive("start_time").toString() + " - " + lesson.getAsJsonPrimitive("end_time").toString()+"\n";
				if(lesson.getAsJsonPrimitive("room").toString().equals("\"\"")==false)
				fin+="Аудитория: "+lesson.getAsJsonPrimitive("room").toString()+"\n";
				
			}
			
		}
		}
		return fin;
	}
}
