package pt.uc.dei.mrc.uctickets.apiclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


import pt.uc.dei.mrc.uctickets.models.ActiveTicket;
import pt.uc.dei.mrc.uctickets.models.Service;
import pt.uc.dei.mrc.uctickets.models.Local;
import pt.uc.dei.mrc.uctickets.models.Login;


/*
 * Job contém metodos para chamar API da UCFRONTDESK
 *  
 */
public class Job {
		
	public static JSONObject generateTicket(String data){
		
		JSONObject ticket = ServerAPI.post("ticket/generate", data);
		
		return ticket;
	}
	
	public static JSONObject WaitingPeople(int sid, int lid, int uid){
		
		JSONObject w;
		
		try {
			w = ServerAPI.get("ticket/waiting/" + sid + "/" + lid + "/" + uid );
			w.put("lid", lid);
			w.put("sid", sid);
			return w;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
		}
		
		return new JSONObject();
	}
	
	
	
	public static JSONObject loadTicket(String data){
		
		JSONObject ticket = ServerAPI.post("ticket/current", data);
		
		return ticket;
	}
	
	
	public static JSONObject LoginKey(String data){
					
		JSONObject login = ServerAPI.post("login", data);
		
		return login;
	}
	
	public static JSONObject removeTicket(String data){
				
		JSONObject remove = ServerAPI.post("ticket/remove", data);
			
		return remove;
		
	}
	
	public static JSONObject desks()
	{	
		JSONObject desks = null;
		try
		{
			desks = ServerAPI.get("desks");

		}
		catch (JSONException e)
		{
			Log.w("UCFRONTDESK", e.toString());
			return null;
			
		}catch (Exception e){
			Log.w("UCFRONTDESK", e.toString());
			return null;
		}
	
		
		return desks;
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

	
	public static ArrayList<Local> localslist(int sid)
	{	
		ArrayList<Local> localslist = new ArrayList<Local>();
		
		try
		{
			JSONObject locals = ServerAPI.get("locals/" + sid);

			JSONArray list = locals.getJSONArray("locals"); /* From Array JSON List */

			for(int i=0; i<list.length();i++)
			{ 
				Local local = new Local(); 
				local.getLocalObject(list.getJSONObject(i));
				localslist.add(local);
			}
						
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


	public static List<ActiveTicket> activeticketlist(int uid) {
		
		ArrayList<ActiveTicket> atlist = new ArrayList<ActiveTicket>();
		
		try
		{
			JSONObject js = ServerAPI.get("ticket/all/" + uid); //get json object response

			//Log.w("UCFRONTDESK","js_lenght " +  js.toString());
			
			JSONArray ja = js.getJSONArray("atickets"); //get array
			
			for(int i=0; i<ja.length();i++)
			{ 
				ActiveTicket at = new ActiveTicket(); 
				
				JSONObject t = ja.getJSONObject(i);
				
				//Actions Here
				at.setLocalName(t.getString("localName")); // Local Name
				at.setServiceName(t.getString("serviceName")); // Service Name
				at.setLID(t.getInt("lid"));
				at.setSID(t.getInt("sid"));
				at.setOwnTicket(t.getInt("ownTicket"));
				at.setCurrentTicket(t.getInt("currentTicket"));
				
				atlist.add(at);	
			}
						
		}catch (JSONException e)
		{
			Log.w("UCFRONTDESK", e.toString());
			
		}
		
		
		return atlist;
	}
	
}
