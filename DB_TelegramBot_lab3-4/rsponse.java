package com.example.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class rsponse {
	 static String line;
		static HttpURLConnection connection;
		static URL url;
	    static InputStreamReader isR;
	    static BufferedReader bfR;
	    
	static String response_api(String urlAddress)
	{
		
	        try
	        {
	            url = new URL(urlAddress);
	            connection = (HttpURLConnection) url.openConnection();//возвращение в connection объекта URLConnection

	            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) //удалось установить соединение
	            {
	                isR = new InputStreamReader(connection.getInputStream());//декодирование считанных байтов в символы во входном потоке
	                bfR = new BufferedReader(isR);//буферизация символов с потока для чтения далее
					line = bfR.readLine();
	                
	            } 
	            else 
	            {
	                System.out.printf("Fail %s", connection.getResponseCode());
	            }
	        }
	        catch (IOException e) 
     	{
         e.printStackTrace();
     	} 
	        
	        	return line;
	}
}
