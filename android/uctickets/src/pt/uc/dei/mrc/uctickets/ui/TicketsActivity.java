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
	
	boolean job;
	
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
				
		
		String loginlist[] = {"Gerar Senha"}; //opcoes menu login
		
		ListView list = (ListView) findViewById(R.id.Ticketlist);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.login_list, loginlist);
        
		list.setAdapter(adapter);
		
		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
		new TicketInitTask().execute();
		
		
		
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	
		    	// Gerar Ticket (Task)
		    	
		    	new TicketCreationTask().execute();
		    	//Toast.makeText(getApplicationContext(), ticketobj.toString(), Toast.LENGTH_SHORT).show();
		    }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tickets, menu);
		return true;
	}
	
	
	/**
	 * Represents an asynchronous task 
	 */
	private class TicketCreationTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean CreateTicket()
		{
			try{
								
				
			}catch(Exception e){
				//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
			
			
			return true;
			
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
				//Toast.makeText(getApplicationContext(), ticketobj.toString(), Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				
			}else
			{
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
				
				//Log.w("UCFRONTDESK", ">asa " + init.toString() ); 
				
				if (init.length() > 0){
					return true;
				}else{
					return false;
				}
								
			}catch(Exception e){
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
					//int waiting = init.getInt("waiting");
					int current = init.getInt("current");
					int minutes = init.getInt("minutes");
					
					job = true;
					
					final TextView current_view = (TextView) findViewById(R.id.current_ticket);
					current_view.setText(""+current);
					
					final TextView minutes_view = (TextView) findViewById(R.id.time_wait_minutes);
					minutes_view.setText(""+minutes+  " minutos");
					
				} catch (JSONException e) {
					job = false;
				}

				//Modificar Butao Geracao (id e nome)
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
