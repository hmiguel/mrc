package pt.uc.dei.mrc.uctickets.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import pt.uc.dei.mrc.uctickets.apiclient.Job;
import pt.uc.dei.mrc.uctickets.models.Local;
import pt.uc.dei.mrc.uctickets.models.Service;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ServicesActivity extends Activity {
	
	private ArrayList<Service> servicelist;
	
	ListView listview;
	
	JSONObject ticketobj;
	 
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_services);
			
		try {
			Intent intent = getIntent(); //get intent
			String ticket = intent.getStringExtra("ticket"); //get ticket json string
			ticketobj = new JSONObject(ticket);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//
		}
		
		
		new ServicesLoadTask().execute();
	
	
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.services, menu);
		return true;
	}
	
	private void populateListView() {
		   
		ListView list = (ListView) findViewById(R.id.Servicelist);
		
		ArrayAdapter<Service> adapter = new ArrayAdapter<Service>(this, android.R.layout.simple_list_item_1, servicelist);
        
		list.setAdapter(adapter);

		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	
		    	//Intent to Next Activity
		    	Intent i = new Intent(ServicesActivity.this, LocalsActivity.class);
		    	
		    	Service s = (Service)parent.getAdapter().getItem(position); // Service Object
		    	
		    	//Toast.makeText(getApplicationContext(), ">"+l.getLID(), Toast.LENGTH_SHORT).show();
		    	
		    	String json; 
		    	
		    	try {
					ticketobj.accumulate("sid", s.getSID()); // add SID to json object
					ticketobj.accumulate("title", s.getName()); // add SID to json object
					
					json = ticketobj.toString();
					i.putExtra("ticket", json );
					startActivity(i);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
		    	
		    	
		    	//Construir Objecto Ticket para enviar
				
		    	
		    	
		       // Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
		    }
		});
	}
	
	/**
	 * Represents an asynchronous local task used to get locals
	 */
	private class ServicesLoadTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean loadServices()
		{
			try{
				servicelist = Job.serviceslist();
				
				//Log.w("UCFRONTDESK", ">>> " + servicelist.get(0).getName() ); 
				
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
			
			if (servicelist == null){	
				//Toast.makeText(getApplicationContext(), servicelist.size(), Toast.LENGTH_SHORT).show();
				return false;
			}
			
			return true;
			
		}

		protected Boolean doInBackground(String... params) 
		{
			if (loadServices())
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
				dialog.dismiss();
				
				populateListView();
				
				
				
				
			}else
			{
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Ocorreu um Erro", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(ServicesActivity.this, "A Carregar Serviços...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   
	
	

}
