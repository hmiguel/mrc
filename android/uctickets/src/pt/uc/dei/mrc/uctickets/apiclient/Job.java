package pt.uc.dei.mrc.uctickets.apiclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


import pt.uc.dei.mrc.uctickets.models.Service;
import pt.uc.dei.mrc.uctickets.models.Local;
import pt.uc.dei.mrc.uctickets.models.Login;


/*
 * Job contém metodos para chamar API da UCFRONTDESK
 *  
 */
public class Job {
	
	
	public static JSONObject loadTicket(String data){
		
		JSONObject ticket = ServerAPI.post("ticket/current", data);
		
		return ticket;
	}
	
	
	public static JSONObject LoginKey(String data){
				
		JSONObject login = ServerAPI.post("login", data);
		
		return login;
		
	}
	
	public static ArrayList<Service> serviceslist()
	{	
		ArrayList<Service> serviceslist = new ArrayList<Service>();
		
		try
		{
			JSONObject services = ServerAPI.get("services");

			JSONArray list = services.getJSONArray("services"); /* From Array JSON List */

			for(int i=0; i<list.length();i++)
			{ 
				Service service = new Service(); 
				service.getLocalObject(list.getJSONObject(i));
				serviceslist.add(service);
			}
			
			//Log.w("UCFRONTDESK", ">>> " + localslist.get(0).getName() ); OK
			
		}catch (JSONException e)
		{
			Log.w("UCFRONTDESK", e.toString());
			return null;
		}catch (Exception e){
			Log.w("UCFRONTDESK", e.toString());
			return null;
		}
	
		
		return serviceslist;
	}

	
	public static ArrayList<Local> localslist()
	{	
		ArrayList<Local> localslist = new ArrayList<Local>();
		
		try
		{
			JSONObject locals = ServerAPI.get("locals");

			JSONArray list = locals.getJSONArray("locals"); /* From Array JSON List */

			for(int i=0; i<list.length();i++)
			{ 
				Local local = new Local(); 
				local.getLocalObject(list.getJSONObject(i));
				localslist.add(local);
			}
			
			//Log.w("UCFRONTDESK", ">>> " + localslist.get(0).getName() ); OK
			
		}catch (JSONException e)
		{
			Log.w("UCFRONTDESK", e.toString());
			return null;
		}catch (Exception e){
			Log.w("UCFRONTDESK", e.toString());
			return null;
		}
	
		
		return localslist;
	}
	
}
