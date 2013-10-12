package pt.uc.dei.mrc.uctickets.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Service {
	
	//private int tid;  // ticket id
		private String name; // ticket hash number
		private int sid; // ticket number
	
	    
	    public int getSID(){
	    	return sid;
	    }
	    
	    public void setSID(int sid){

	    	this.sid = sid;
	    }

	    public String getName(){
	    	
	    	return name;
	    }
	    
	    public void setName(String name){
	    	
	    	this.name = name;
	    }
	    
	    public Service(){}
	    
	    @Override
	    public String toString() {
	        return this.name;
	    }
	    
	    public Service getLocalObject(JSONObject service) throws JSONException
		{
			this.name =	service.getString("name");
			this.sid =	service.getInt("sid");
			
			return this;
		}
}
