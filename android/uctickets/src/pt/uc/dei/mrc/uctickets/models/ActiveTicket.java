package pt.uc.dei.mrc.uctickets.models;


public class ActiveTicket {
	
	//Objecto para lista do menu principal
	
	private String lname; // local name
	private String sname; // service name	
	private int lid; // local id
	private int sid;
	private int current;
	private int own;
	
    
    public int getLID(){
    	return lid;
    }
    
    public void setLID(int lid){

    	this.lid = lid;
    }
    
    public int getSID(){
    	return sid;
    }
    
    public void setSID(int sid){

    	this.sid = sid;
    }

    public String getLocalName(){
    	
    	return lname;
    }
    
    public void setLocalName(String name){
    	
    	this.lname = name;
    }
    
    public String getServiceName(){
    	
    	return sname;
    }
    
    public void setServiceName(String name){
    	
    	this.sname = name;
    }
    
    public void setCurrentTicket(int t){

    	this.current = t;
    }

    public int getCurrentTicket(){
    	
    	return current;
    }
    
    public int getOwnTicket(){
    	
    	return own;
    }
    
    public void setOwnTicket(int t){

    	this.own = t;
    }

   
    
    public ActiveTicket(){}
    

}
