package pt.uc.dei.mrc.uctickets.ui;


import org.json.JSONException;
import org.json.JSONObject;

import pt.uc.dei.mrc.uctickets.apiclient.Job;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TicketsActivity extends Activity {
	
	JSONObject ticketobj;
	JSONObject init;
	JSONObject gt;
	boolean job;
	
	ListView list;
	String scene;
	
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		job = false;
		
		setContentView(R.layout.activity_tickets);
		
		try {
			Intent intent = getIntent(); //get intent
			String ticket = intent.getStringExtra("ticket"); //get ticket json string
			
			ticketobj = new JSONObject(ticket);
			
			//Set Title
			String title = ticketobj.getString("title");
			final TextView textViewToChange = (TextView) findViewById(R.id.titulo);
			textViewToChange.setText(title);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//
		}
		
		//Load Button
		
		
		new TicketInitTask().execute();
		
		list = (ListView) findViewById(R.id.Ticketlist);
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	 	
		    	// Gerar Ticket (Task)
		    	String name = (String) parent.getItemAtPosition(position);
		    	
		    	if(name.equals("Gerar Senha")){
		    		new TicketCreationTask().execute();
		    		
		    	}else if(name.equals("Remover Senha")){
		    		new TicketRemotionTask().execute();
		    	}
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tickets, menu);
		return true;
	}
	
	
	public void createbtn(String scene){
			
		String loginlist[] = {scene}; //opcoes menu login
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.login_list, loginlist);
        
		list.setAdapter(adapter);
		
		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
	}
	
	
	/**
	 * Represents an asynchronous task 
	 */
	private class TicketCreationTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean CreateTicket()
		{
			try{
				
				gt = Job.generateTicket(ticketobj.toString());
				//Log.w("UCFRONTDESK", ">asa " + ticketobj.toString() ); 
				
				if (gt.length() > 1){
					
					return true;
				}
										
			}catch(Exception e){
				//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
				
			}
		
			return false;
		}

		protected Boolean doInBackground(String... params) 
		{
			if (CreateTicket())
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
				//Modificar Butao Geracao (id e nome)
				
				createbtn("Remover Senha");
				dialog.dismiss();
				new TicketInitTask().execute();
				
			}else{
				
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(TicketsActivity.this, "A Processar...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
			
		}
	}   
	
	
	/**
	 * Represents an asynchronous task 
	 */
	
	private class TicketRemotionTask extends AsyncTask<String, Void, Boolean> 
	{
		
		protected boolean RemoveTicket()
		{
			try{
				
				gt = Job.removeTicket(ticketobj.toString());
				
				Log.w("UCFRONTDESK", ">Remotion_TASK " + gt.length() ); 
				
				if (gt.length() > 1){
					
					return true;
					
				}
										
			}catch(Exception e){
				//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
				Log.w("UCFRONTDESK", ">Remotion_TASK_Exception " + e.toString() ); 
			}
		
			return false;
		}

		protected Boolean doInBackground(String... params) 
		{
			if (RemoveTicket())
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
				//Modificar Butao Geracao (id e nome)
				
				createbtn("Gerar Senha");
				dialog.dismiss();
				new TicketInitTask().execute();
				
			}else{
				
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(TicketsActivity.this, "A Processar...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   
	
	
	/**
	 * Represents an asynchronous task- Ticket Activity Init
	 */
	private class TicketInitTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean LoadTicket() // funcao que faz load da situacao actual
		{
			try{
							
				// Load Ticket Current Situation
				
				init = Job.loadTicket(ticketobj.toString());
				
				if (init.length() > 0){
					
					return true;
				
				}else{
					return false;
				}
								
			}catch(Exception e){
				Log.w("UCFRONTDESK", ">TICKETLOAD_TASK_Exception " + e.toString() ); 
				return false;
			}
		}

		protected Boolean doInBackground(String... params) 
		{
			if (LoadTicket())
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
				try {
					int waiting = init.getInt("waiting");
					int current = init.getInt("current");
					int own = init.getInt("own");
					int until = init.getInt("untilyou"); // Quando tem senha...até ele;
					
					String user_ticket = "---";
							
					if (own > 0){
						createbtn("Remover Senha");
						user_ticket = ""+ own;
						
						//Aqui mostra o until
						waiting = until;
					}else if(own == -1){
						//Aqui mostra o waiting
						createbtn("Gerar Senha");
						
					}
					
					//int minutes = init.getInt("minutes");
					int minutes = 5 * waiting;
					
					job = true;
					
					final TextView current_view = (TextView) findViewById(R.id.current_ticket);
					current_view.setText(""+current);
					
					final TextView userticket_view = (TextView) findViewById(R.id.user_ticket);
					userticket_view.setText(""+user_ticket);
					
					final TextView minutes_view = (TextView) findViewById(R.id.time_wait_minutes);
					minutes_view.setText(""+minutes+  " minutos");
					
					final TextView waitt_view = (TextView) findViewById(R.id.waiting_tickets);
					waitt_view.setText(""+waiting);
					
					
				} catch (JSONException e) {
					Log.w("UCFRONTDESK", ">JSONEXCEPT_TICKETSTASK_Exception " + e.toString() ); 
					job = false;
				}
				
				dialog.dismiss();
				
			}else
			{
				
				job = false;
				dialog.dismiss();
				//Toast.makeText(getApplicationContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(TicketsActivity.this, "A Processar...", "Aguarde...", true);
		}


	
		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   
}