package com.example.demo;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
@Component
public class TG extends TelegramLongPollingBot
{
	final config configs;
	String regex ="\\d{4}";
	String group=null;
	String login=null;
	JsonObject stobj;
	JsonArray[] schedule = new JsonArray[7]; 
	List<Commands> binding = new ArrayList<Commands>();
	static Connection dbCon;
	
		@SuppressWarnings("deprecation")
		public TG(config configs)
	{
		this.configs=configs;
	}
	 	
		@Override
	    public String getBotUsername() 
	 	{
	        return configs.getBotName();
	    }
		
	    @Override
	    public String getBotToken() 
	    {
	        return configs.getToken();
	    }
	    
	    public static Connection getConnection()
	  throws SQLException, ClassNotFoundException
	  {		String connectionString = "jdbc:mysql://localhost:3306/Storage";

		Class.forName("com.mysql.cj.jdbc.Driver");

		Connection dbConnection = DriverManager.getConnection(connectionString, "root", "SolThe1stOfficer");

		return dbConnection;
	  }

		@Override
		public void onUpdateReceived(Update update) 
		{
			if (update.hasMessage() && update.getMessage().hasText())
			{
	            String messageText = update.getMessage().getText();
	            long chatId = update.getMessage().getChatId();
	            if(!binding.contains(Commands.LOGGED))//проверка на логин
	            {
	            		if(Commands.START.equals(messageText))
	            		{
	            			startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
	            			binding.add(Commands.START);
	            		}
	            		else if(Commands.REGISTRATION.equals(messageText)&&binding.contains(Commands.START))
	            		{
	            			regCommandReceived(chatId);
	            			binding.clear();
	            			binding.add(Commands.REGISTRATION);
	            		}
	            		else if(Commands.LOGIN.equals(messageText)&&binding.contains(Commands.START))
	            		{
	            			regCommandReceived(chatId);
	            			binding.clear();
	            			binding.add(Commands.LOGIN);
	            		}
	            		else if(binding.contains(Commands.REGISTRATION)||binding.contains(Commands.LOGIN))
	            		{
	            			login=messageText;
	            	
	            				interaction_with_bd();
	            				if(dbCon!=null)
	            				{
	            					Statement st=null;
	            					ResultSet rs=null;
	            					String Sql = "select ugroup from users_info where login='"+login+"'"; 
	            					try 
	            					{
	            						st = dbCon.createStatement();
	            						rs = st.executeQuery(Sql);
	            						while(rs.next())
	            							group=rs.getString("ugroup");
	            					} 
	            					catch (SQLException e) 
	            					{
								
	            						e.printStackTrace();
	            					}
	            					if(!binding.contains(Commands.REGISTRATION))
	            					{if(group!=null)
	            					{
	            						binding.clear();
	            						binding.add(Commands.LOGGED);
	            						send(chatId,"Вы успешно авторизованы!");
	            						String urlAddress = "https://digital.etu.ru/api/mobile/schedule?weekDay=&subjectType=&joinWeeks=true&year=2023&groupNumber="+ URLEncoder.encode(group, StandardCharsets.UTF_8);  
	    	            			  	String line="";
	    	            			  	line= rsponse.response_api(urlAddress);
	    	            			  	stobj= JsonParser.parseString(line).getAsJsonObject();
		            					schedule = Parsing.output(stobj,group);
	            						send(keyboard_after_log(chatId));
	            						
	            					}
	            					else if(group==null)
	            					{
	            						send(chatId,"Такого логина нет в базе данных! Проверьте ввод и повторите попытку или выберите 'Регистрация'");
	            						binding.add(Commands.START);
	            						send(keyboard_after_start(chatId));
	            						
	    	            				
	    	            				
	            					}}
	            					else
	            					{
	            						if(group==null)
		            					{
	            							binding.clear();
	            							binding.add(Commands.LOGIN_INPUTED);
		            						send(chatId,"Введите номер группы.");
		            					}
		            					else if(group!=null)
		            					{
		            						send(chatId,"Такой пользователь уже существует!Повторите ввод логина или выберите 'Вход'");
		    	            				binding.add(Commands.START);
		    	            				send(keyboard_after_start(chatId));
		    	            				group=null;
		            					}
	            					}
	            				}
	            				else
	            					send(chatId,"Не удалось установить соединение с базой данных!");
	            		
	            			

	            		}
	            		else if(binding.contains(Commands.LOGIN_INPUTED))
	            		{
	            				if(group==null)
	            				  group=messageText;            			  
	            				 
	            			  	String urlAddress = "https://digital.etu.ru/api/mobile/schedule?weekDay=&subjectType=&joinWeeks=true&year=2023&groupNumber="+ URLEncoder.encode(group, StandardCharsets.UTF_8);  
	            			  	String line="";
	            				line= rsponse.response_api(urlAddress);
	            				
	            				if(group.matches(regex)&&!line.equals("{}"))
	            				{
	            					if(binding.contains(Commands.LOGIN_INPUTED))
	            					{
	            						binding.clear();
	            						binding.add(Commands.LOGGED);
	            						String sql = "insert into users_info values('"+login+"','"+group+"')";
	            						try 
	            						{
	            							PreparedStatement pst = dbCon.prepareStatement(sql);
	            							pst.executeUpdate();
	            						} 
	            						catch (SQLException e) 
	            						{
	            							e.printStackTrace();
	            						}
	            						send(chatId,"Вы успешно зарегистрированы!");
	            					}
	            					stobj= JsonParser.parseString(line).getAsJsonObject();
	            					schedule = Parsing.output(stobj,group);
	            					send(keyboard_after_log(chatId));
	            				}
	            				else 
	            				{
	            					send(chatId,"Такой группы в расписании не существует! Повторите ввод!");
	            					group=null;
	            				}
	            			
	            		}
	            	}
	            	else
	            	{
	            		if(!binding.contains(Commands.THE_DAY))
	            		{
	            			if(Commands.NEAR_DAY.equals(messageText))
	            			{
	            				String mes=Schedule_functions.get_tomorrow(schedule);
	            				send(chatId,mes);
	            			}
	            			else if(Commands.ALL_WEEK.equals(messageText))
	            			{
	            				String mes=Schedule_functions.get_week(schedule);
	            				send(chatId,mes);
	            			}
	            			else if(Commands.NEAR_LESSON.equals(messageText))
	            			{
	            				String mes=Schedule_functions.get_near(schedule);
	            				send(chatId,mes);
	            			}
	            			else if(Commands.THE_DAY.equals(messageText))
	            			{
	            				send(keyboard_after_day(chatId));
	            				binding.add(Commands.THE_DAY);
	            			}
	            			else if(Commands.EXIT.equals(messageText))
	            			{
	            				send(chatId,"Вы вышли из своего аккаунта!");
	            				startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
	            				binding.clear();
	            				binding.add(Commands.START);
	            			}
	            		}
	            		else
	            		{
	            			the_day_case(messageText,chatId);
	            			binding.remove(Commands.THE_DAY);
	            		}
	            	}
	        }
				
		 }
		
	    public void the_day_case(String messageText,long chatId)
	         {
	        	 int wday=0;
     			switch(messageText)
     			{
     			case "Понедельник":
     			{
     				wday=0;
     				break;
     			}
     			case "Вторник":
     			{
     				wday=1;
     				break;
     			}
     			case "Среда":
     			{
     				wday=2;
     				break;
     			}
     			case "Четверг":
     			{
     				wday=3;
     				break;
     			}
     			case "Пятница":
     			{
     				wday=4;
     				break;
     			}
     			case "Суббота":
     			{
     				wday=5;
     				break;
     			}
     			}
     			String mes=Schedule_functions.get_day(schedule,wday);
     			send(chatId,mes);
     			send(keyboard_after_log(chatId));
	         }
		
	    public static void interaction_with_bd()
		{
			try 
			{
				dbCon = getConnection();
			} 	
			catch (SQLException e) 
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	    public static SendMessage keyboard_after_start (long chat_id) 
		{
			SendMessage message = new SendMessage();
	        message.setChatId(chat_id);
	        message.setText("Для продолжения работы войдите или зарегистрируйтесь");
	        ReplyKeyboardMarkup markupInline = new ReplyKeyboardMarkup();
	        markupInline.setSelective(true);
	        markupInline.setResizeKeyboard(true);
	        markupInline.setOneTimeKeyboard(true);
	        List<KeyboardRow> keyboard = new ArrayList<>();
	        KeyboardRow keyboardFirstRow = new KeyboardRow();
	        keyboardFirstRow.add("Вход");
	        keyboardFirstRow.add("Регистрация");
	        keyboard.add(keyboardFirstRow);
	        markupInline.setKeyboard(keyboard);
	        message.setReplyMarkup(markupInline);
			return message;
		}
		
	    public static SendMessage keyboard_after_day (long chat_id) 
		{
			SendMessage message = new SendMessage();
	        message.setChatId(chat_id);
	        message.setText("Какое расписание необходимо вывести?");
	        ReplyKeyboardMarkup markupR = new ReplyKeyboardMarkup();
	        markupR.setSelective(true);
	        markupR.setResizeKeyboard(true);
	        markupR.setOneTimeKeyboard(true);
	        List<KeyboardRow> keyboard = new ArrayList<>();
	        KeyboardRow k1Row = new KeyboardRow();
	        k1Row.add("Понедельник");
	        KeyboardRow k2Row = new KeyboardRow();
	        k2Row.add("Вторник");
	        KeyboardRow k3Row = new KeyboardRow();
	        k3Row.add("Среда");
	        KeyboardRow k4Row = new KeyboardRow();
	        k4Row.add("Четверг");
	        KeyboardRow k5Row = new KeyboardRow();
	        k5Row.add("Пятница");
	        KeyboardRow k6Row = new KeyboardRow();
	        k6Row.add("Суббота");
	        keyboard.add(k1Row);
	        keyboard.add(k2Row);
	        keyboard.add(k3Row);
	        keyboard.add(k4Row);
	        keyboard.add(k5Row);
	        keyboard.add(k6Row);
	        markupR.setKeyboard(keyboard);
	        message.setReplyMarkup(markupR);
			return message;
		}
		
	    public static SendMessage keyboard_after_log (long chat_id) 
		{
			SendMessage message = new SendMessage();
	        message.setChatId(chat_id);
	        message.setText("Какое расписание необходимо вывести?");
	        ReplyKeyboardMarkup markupR = new ReplyKeyboardMarkup();
	        markupR.setSelective(true);
	        markupR.setResizeKeyboard(true);
	        markupR.setOneTimeKeyboard(false);
	        List<KeyboardRow> keyboard = new ArrayList<>();
	        KeyboardRow k1Row = new KeyboardRow();
	        k1Row.add("Ближайшее занятие");
	        KeyboardRow k2Row = new KeyboardRow();
	        k2Row.add("Следующий учебный день");
	        KeyboardRow k3Row = new KeyboardRow();
	        k3Row.add("Вся неделя");
	        KeyboardRow k4Row = new KeyboardRow();
	        k4Row.add("Выбранный день");
	        KeyboardRow k5Row = new KeyboardRow();
	        k5Row.add("Выход");
	        keyboard.add(k1Row);
	        keyboard.add(k2Row);
	        keyboard.add(k3Row);
	        keyboard.add(k4Row);
	        keyboard.add(k5Row);
	        markupR.setKeyboard(keyboard);
	        message.setReplyMarkup(markupR);
			return message;
		}
		
	    private void startCommandReceived(long chatId, String name) 
		{


	        String answer = "Привет, " + name + ", это Бот для получения расписания!\nДля того, чтобы начать, Вам необходимо авторизоваться.";
	        send(chatId, answer);
			send(keyboard_after_start(chatId));
			
	    }
		
	    private void regCommandReceived(long chatId) {


	        String answer = "Введите Ваш логин.";
	        send(chatId, answer);
	    }
		
	    private void send(long chatId, String textToSend) 
			{
		        SendMessage message = new SendMessage();
		        message.setChatId(String.valueOf(chatId));
		        message.setText(textToSend);
		        try {
		        	execute(message);
		        } catch (TelegramApiException e) {
		        	e.printStackTrace();
		        }
		    }
		
	    private void send(SendMessage k) 
		{
	        try {
	        	execute(k);
	        } catch (TelegramApiException e) {
	        	e.printStackTrace();
	        }
	    }
}
