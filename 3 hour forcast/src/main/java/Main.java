import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

	//Your api key
	public static final String API_KEY = "e9fd3e1bfd51207c1b99b1ecfad25d4f";
	
	//Url to website with json String
	public static URL connectURL;
	
	//Keys for array
	public static final String LIST_KEY = "\"list\":";
	public static final String CITY_KEY = ",\"city\":";
	
	//Method for getting city id
	public static int getCityId(String name)
	{
		int result = -1;
		
		StringBuffer json = new StringBuffer();
		
		//File in wich all cities are located in json
		File data = new File("citys/city.list.json");
				
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(data.toURL().openStream()));
			
			String currentLine;
			
			while((currentLine = reader.readLine()) != null)
			{
				json.append(currentLine);
			}
			
			reader.close();			
			
			Location[] locations = new Gson().fromJson(json.toString(), Location[].class);
			
			for(int i = 0; i < locations.length; i++)
			{
				if(locations[i].name.equals(name))
				{
					result = locations[i].id;
				}
			
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	//Main constructor
	public static void main(String[] args)
	{
		String location = "";
	
		location = JOptionPane.showInputDialog("Input location");
		
		String urlString = "https://api.openweathermap.org/data/2.5/forecast?id="+ getCityId(location) + "&appid=" + API_KEY;
		
		try {
			connectURL = new URL(urlString);
			
			//Things we will need to read data from website
			URLConnection con = connectURL.openConnection();
			
			StringBuffer result = new StringBuffer();
			
			String currentLine;
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			while((currentLine = reader.readLine()) != null)
			{
				result.append(currentLine);
			}
			
			System.out.println(result);
			
			reader.close();
			
			//index from start to finish of the array
			int startIndex = result.indexOf(LIST_KEY) + LIST_KEY.length();
			
			int finIndex = result.indexOf(CITY_KEY);
			
			String weatherDatas = result.toString().substring(startIndex, finIndex);
			
			System.out.println(weatherDatas);
			
			//read list array from result wich contains data
			Gson gson = new Gson();
			
			//Array with all items in the json array
			WeatherItem[] items = gson.fromJson(weatherDatas, WeatherItem[].class);
			
			//print the results
			for(int i = 0; i < items.length; i++)
			{
				System.out.println(items[i].dt);
				System.out.println(items[i].main);
				//weather always only contains 1 data 
				System.out.println(items[i].weather[0].id);
				System.out.println(items[i].weather[0].main);
				System.out.println(items[i].weather[0].description);
				System.out.println(items[i].weather[0].icon);
				System.out.println(items[i].clouds);
				System.out.println(items[i].wind);
				System.out.println(items[i].sys);
				System.out.println(items[i].dt_txt);
			}
			
			//If you want to hava a data from a map you hava to get them with map.get("key")
		} catch (MalformedURLException e) {
			// TODO Handle exception
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Handle exception
			e.printStackTrace();
		}

	}
	
	//Classes to handle the json arrays
	public class WeatherItem
	{
		int dt;
		Map<String, Object> main;
		Weather[] weather;
		Map<String, Object> clouds;
		Map<String, Object> wind;
		Map<String, Object> sys;
		String dt_txt;
	}
	
	public class Weather
	{
		int id;
		String main;
		String description;
		String icon;
	}
	
	//Location for finding City id so that yout can check if the location acctually exists
	public class Location
	{
		int id;
		String name;
		String country;
	}
}
