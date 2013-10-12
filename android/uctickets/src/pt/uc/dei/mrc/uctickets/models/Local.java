package pt.uc.dei.mrc.uctickets.models;

import org.json.JSONException;
import org.json.JSONObject;


public class Local {
	

		private String name; // local name
		private int lid; // local id
	
	    
	    public int getLID(){
	    	return lid;
	    }
	    
	    public void setLID(int lid){

	    	this.lid = lid;
	    }

	    public String getName(){
	    	
	    	return name;
	    }
	    
	    public void setName(String name){
	    	
	    	this.name = name;
	    }
	    
	    public Local(){}
	    
	    @Override
	    public String toString() {
	        return this.name;
	    }
	    
	    public Local getLocalObject(JSONObject local) throws JSONException
		{
			this.name =	local.getString("name");
			this.lid =	local.getInt("lid");
			
			return this;
		}
}
