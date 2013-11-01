package pt.uc.dei.mrc.uctickets.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import pt.uc.dei.mrc.uctickets.apiclient.Job;
import pt.uc.dei.mrc.uctickets.models.ActiveTicket;
import pt.uc.dei.mrc.uctickets.models.AlarmTicket;
import pt.uc.dei.mrc.uctickets.ui.HomeActivity;
import pt.uc.dei.mrc.uctickets.ui.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class ServiceMain extends Service
{
	private static final String TAG = "UCFRONTDESK";

	private List<ActiveTicket> atlist;
	
	private JSONObject obj;
	
	private int NOTIF_ID = 1234;
	
	/**
	 * Function to be ran when we call startService()
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand()");
				
		String ticket = intent.getStringExtra("ticket");
		
		
		//Log.d(TAG, "ticket_service>" + ticket);
		
		try {
			obj = new JSONObject(ticket);
			
		} catch (JSONException e) {
			
			Log.w(TAG, "SERVICE : " + e.toString());
			
		}
		
		try {
			
			new Thread(new Runnable(){
				
				public void run(){
						
					while(true){
							
							/* Check Ticket List */
							
								try {
									Thread.sleep(30000);
								} catch (InterruptedException e) {
									Log.w(TAG, "SERVICE : " + e.toString());
									
								}
								Log.d(TAG, "Service Wake UP");
								new ActiveTicketsLoadTask().execute();
					}
				}
			}).start();
		
		} catch (Exception e) {
			Log.w(TAG, "SERVICE : " + e.toString());
		}
	
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null; /* Not a bound service */
	}
	
	@SuppressWarnings("deprecation")
	public void showNotification(Context context, JSONObject j){
       
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ServiceMain.class), 0);
         
        Notification notification;

        
        int n = -1;
    	String title = "UC FrontDesk";
    	String text = "";
    	       
        try {
			n = j.getInt("waiting");
			
			
			NOTIF_ID = j.getInt("lid") + j.getInt("sid") + 1234;
		
        	title = "[" + j.getString("service") + " - " + j.getString("local") + "]";
        	
        	if (n == 0){
        		text = "É o próximo!";
        	
           	}	
        	else if (n == 1){
        		text = "Tem apenas uma pessoa à sua frente!";
        		
            }else if(n > 1 && n < 4){
              	
            	text = "Tem apenas " + n + " pessoas à sua frente!";
            
            }else{
            	
            	return;
            	
            }
        	
        	
        	  NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
     			.setContentIntent(contentIntent)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(text)
		      ;
     
     
		      notification = builder.getNotification();
		      
		      notification.flags |= Notification.FLAG_AUTO_CANCEL;
		             
		      NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		  
		      notificationManager.notify(NOTIF_ID, builder.build());
        	
        
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		
		}
                  
      
        	
    }
	
	/*
	 * Represents an asynchronous local task used to get locals
	 */
	private class ActiveTicketsLoadTask extends AsyncTask<String, Void, Boolean> 
	{
		private List <ActiveTicket> tickets;
		
		protected boolean loadTickets()
		{
			try{
			
				tickets = Job.activeticketlist(obj.getInt("uid"));
				
				// Por cada ticket, verifica se o seu ticket - actual < 4
				
				if (tickets.size() > 0){
					
					return true; //Create New Notification
					
				}else{
					
					return false; 
				}
				
				
			}catch(Exception e){
				Log.w("UCFRONTDESK", "SERVICE: " + e.toString());
				return false;
			}
					
		}

		protected Boolean doInBackground(String... params) 
		{
			if (loadTickets())
			{
				return true;
			}
			else
			{
				return false;
			}
		}      

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if (result)
			{
					for (ActiveTicket ticket : tickets) { // Percorre lista de tickets
					
					//Verifica se existe algum proximo do limite (<4)
					if (ticket.getCurrentTicket() - ticket.getOwnTicket() < 4){
						
						try {
							int lid = ticket.getLID();
							int sid = ticket.getSID();
							int uid = obj.getInt("uid");
								
							// Get Data
							JSONObject j = Job.WaitingPeople(sid, lid, uid);
							
							int wait = j.getInt("waiting");
							
							if (wait > -1 && wait < 4){				
								//Create Notification
								showNotification(getApplicationContext(), j);
								
							}
							
						} catch (JSONException e) {
							//
						}
					}
				
				}
				/* Notification */
				//populateListView();
				
			}
		
		}

		@Override
		protected void onPreExecute() 
		{
			
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   
	
}