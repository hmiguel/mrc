package pt.uc.dei.mrc.uctickets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Login {

	
	private String key; 
	
	public String getKey(){
		return key;
	}
	    
	public void setKey(String key){
		this.key = key;
	}
	
	public Login(){}
	
	public Login getLoginObject(JSONObject login) throws JSONException
	{
		this.key =	login.getString("key");
		
		return this;
	}
}
