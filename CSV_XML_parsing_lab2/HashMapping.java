package lab2_kotlin;

import java.util.HashMap;

public class HashMapping 
{
	static HashMap<String,HashMap<String,HashMap<Integer,Integer>>> city = new HashMap<String,HashMap<String,HashMap<Integer,Integer>>>();
	static HashMap<String, Integer> copy = new HashMap<String,Integer>();
	static HashMap<String,HashMap<Integer,Integer>> city_plus_floors = new HashMap<String,HashMap<Integer,Integer>>();
	
	public static void building(String cityN, String streetN, Integer houseN, Integer floorN)
	{ 

		HashMap<String,HashMap<Integer,Integer>> street = new HashMap<String,HashMap<Integer,Integer>>();
		HashMap<Integer,Integer> house = new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> each_floor  = new HashMap<Integer,Integer>();
		
		if(city.isEmpty()==false&&city.get(cityN)!=null)//city exists:)
		{
			
			if(city.get(cityN).get(streetN)==null)//no street at the moment:(
			{
				street = city.get(cityN);
				house.put(houseN, floorN);
				street.put(streetN, house);
				city.put(cityN, street);
			
			}
			else//this street exists:)
			{
				if(city.get(cityN).get(streetN).get(houseN)==floorN) //copy:(
				{
					if(copy.isEmpty()==true||copy.get(cityN+' '+streetN+' '+houseN+' '+floorN)==null)//first copy
					{
						copy.put(cityN+' '+streetN+' '+houseN+' '+floorN, 2);
					}
					else//number'th copy
					{
						Integer repeat =copy.get(cityN+' '+streetN+' '+houseN+' '+floorN)+1;
						copy.put(cityN+' '+streetN+' '+houseN+' '+floorN, repeat);
					}
				}
				else//no copy:)
				{
					street = city.get(cityN);
					house = street.get(streetN);
					house.put(houseN, floorN);
					street.put(streetN, house);
					city.put(cityN, street);
				}
			}
		}
		else//city doesn't exist:(
		{
			house.put(houseN, floorN);
			street.put(streetN, house);
			city.put(cityN, street);
		}
	
		if(copy.get(cityN+' '+streetN+' '+houseN+' '+floorN)==null)//this counts if it's not a copy:)
		{
			if(city_plus_floors.isEmpty()==false&&city_plus_floors.get(cityN)!=null)//not first counter for city
			{
				Integer fl=city_plus_floors.get(cityN).get(floorN);
				each_floor = city_plus_floors.get(cityN);
				fl++;
				each_floor.put(floorN, fl);
				city_plus_floors.put(cityN,each_floor);
			}
			else
			{
				for(int j =1;j<=5;j++)
				{
					each_floor.put(j, 0);
				}
				each_floor.put(floorN, 1);
				city_plus_floors.put(cityN, each_floor);
			}
		}
	}
}
