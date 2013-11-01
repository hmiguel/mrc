package pt.uc.dei.mrc.uctickets.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import pt.uc.dei.mrc.uctickets.apiclient.Job;
import pt.uc.dei.mrc.uctickets.models.ActiveTicket;
import pt.uc.dei.mrc.uctickets.service.ServiceMain;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	ListView listview;
	ListView tlist;
	
	
	JSONObject ticketobj;
	volatile boolean stop = false;
	volatile boolean bdialog = true; 
	
	private int uid;
	
	private Thread background;
	
	private ProgressDialog dialog;
	 	
	private List<ActiveTicket> atlist;
	
	private final Handler myHandler = new Handler();
    
    final Runnable updateRunnable = new Runnable() {
        public void run() {
            //call the task
        	new ActiveTicketsLoadTask().execute();
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		try {
			Intent intent = getIntent(); //get intent
			
			String ticket = intent.getStringExtra("ticket"); //get ticket json string
			
			ticketobj = new JSONObject(ticket);
		
			
		}catch(Exception e){
			
		
		}
		
		//Menu 
		String homelist[] = {"Gerar Senha"};
		ListView list = (ListView) findViewById(R.id.Homelist);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, homelist);
		list.setAdapter(adapter);
		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
				
		// BACKGROUND ACTIVITY THREAD
				
		background = new Thread(new Task());
		
		background.start();
		
		// BACKGROUND SYSTEM SERVICE
		
		Intent service = new Intent(this, ServiceMain.class); 
		
		service.putExtra("ticket",ticketobj.toString());
		
		startService(service);
		
		// Buttons Interation Menu
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	
		    	Intent i = new Intent(HomeActivity.this, ServicesActivity.class);;
		    	   		
		    	String json; 
		    	
			    	
			    try {
						
					ticketobj.put("active", 1); // add ACTIVE
					json = ticketobj.toString();
					i.putExtra("ticket", json );
					
					background.interrupt();
					
					startActivity(i);
					
			    } catch (JSONException e) {
					// TODO Auto-generated catch block
					//
				}
		    }	
		});
		
		tlist = (ListView) findViewById(R.id.Ticketlistview);
		
		// Buttons Interation Menu
		tlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
				    	
					Intent i = new Intent(HomeActivity.this, TicketsActivity.class);;
				    		    		
				    String json; 
				    	    	
					try {
						if (atlist.size() > 0){
							ActiveTicket t = atlist.get(position);
							
							ticketobj.put("active", 1); 	// add ACTIVE
							ticketobj.put("lid", t.getLID());	// add LID
							ticketobj.put("sid", t.getSID());	// add SID
							ticketobj.put("title", t.getServiceName() + " - " + t.getLocalName());	// add TITLE
				
							json = ticketobj.toString(); 
							i.putExtra("ticket", json );
							background.interrupt();
							startActivity(i);
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//
					}
				}	
			});
	}
	
	private int counter = 0;
	
	
	// Thread Actualização da Lista de Senhas
	class Task implements Runnable {
		
		@Override
		public void run() {
			while(!stop) {
				
				try {
					myHandler.post(updateRunnable);
					Thread.sleep(30000);
					counter++;
					
					//Verifica se há alterações;
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//update  
				Log.w("UCFRONTDESK", "Thread " + counter);
			}
		}
		

	}

	
	//lista de senhas_activas -- refresh da thread direcionado para esta funcao.
	private void populateListView(){
				   
				tlist.setBackgroundColor(Color.rgb(0, 201, 234)); //setbckgrundcolor
				
				List<Map<String, String>> data = new ArrayList<Map<String, String>>();
				
				// For each item in object...
				for(int i=0; i < atlist.size(); i++){
					Map<String, String> datum = new HashMap<String, String>(2);
					ActiveTicket a = atlist.get(i);
					String t1 = a.getServiceName() + " - " + a.getLocalName();
					String t2 = "Senha: " + a.getOwnTicket() + " | Actual: " + a.getCurrentTicket();
					
					datum.put("local", t1); // Titles
					datum.put("info", t2); //	Subtitle
					
					data.add(datum);
				}
				
				SimpleAdapter adapter2 = new SimpleAdapter(this, data,
				                                      android.R.layout.simple_list_item_2,
				                                      new String[] {"local", "info"},
				                                      new int[] {android.R.id.text1, android.R.id.text2}){

							        	public View getView(int position, View convertView, ViewGroup parent) {
							        		View view = super.getView(position, convertView, parent);
							        		TextView text2 = (TextView) view.findViewById(android.R.id.text2);
							        		text2.setTextColor(Color.WHITE);
							        		return view;
							        	};
				};
		       
				tlist.setAdapter(adapter2);
			
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		
		return true;
	}
	
	/**
	 * Represents an asynchronous local task used to get locals
	 */
	private class ActiveTicketsLoadTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean loadTickets()
		{
			try{
				//locallist = Job.localslist(ticketobj.getInt("sid"));
				List <ActiveTicket> new_atlist = Job.activeticketlist(ticketobj.getInt("uid"));
				
				//Log.w("UCFRONTDESK", "Thread_na " + new_atlist.size());
				
				if (atlist != null){
					if	(!atlist.equals(new_atlist) && new_atlist.size() > 0){
						atlist = new_atlist;
					
						return true;
						
					}else{
						
						return false;
					}
					
				}else{
					
					if (new_atlist.size() > 0){
						atlist = new_atlist;
					
						return true; // Se forem iguais não actualiza;
						
					}else{
						
					
						return false;
						
					}
				}
				
			}catch(Exception e){
				Log.w("UCFRONTDESK", e.toString());
				return false;
			}
					
		}

		protected Boolean doInBackground(String... params) 
		{
			if (loadTickets())
			{
				
				//Novos Tickets - Do Color
				
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
				//dialog.dismiss();
				
				
				populateListView();
				
			}
			else
			{
				//dialog.dismiss();
				//Toast.makeText(getApplicationContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			// = ProgressDialog.show(HomeActivity.this, "A Verificar Senhas Activas...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   

}