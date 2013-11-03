package pt.uc.dei.mrc.uctickets.ui;


import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import pt.uc.dei.mrc.uctickets.apiclient.Job;
import pt.uc.dei.mrc.uctickets.models.Local;
import pt.uc.dei.mrc.uctickets.models.Service;


import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LocalsActivity extends Activity {

	private ArrayList<Local> locallist;
	
	ListView listview;
	
	JSONObject ticketobj;
	 
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_locals);
		
		try {
			Intent intent = getIntent(); //get intent
			String ticket = intent.getStringExtra("ticket"); //get ticket json string
			ticketobj = new JSONObject(ticket);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//
		}
		
		
		new LocalsLoadTask().execute();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locals, menu);
		return true;
	}

	private void populateListView() {
	   
		ListView list = (ListView) findViewById(R.id.Locallist);
		
		ArrayAdapter<Local> adapter = new ArrayAdapter<Local>(this, android.R.layout.simple_list_item_1, locallist);
        
		list.setAdapter(adapter);

		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	
		    	//Intent to Next Activity
		    	Intent i = new Intent(LocalsActivity.this, TicketsActivity.class);
		    	
		    	Local l = (Local)parent.getAdapter().getItem(position); // Local Object
		    	
		    			    	
		    	String json; 
		    	
		    	try {
		    		// update title
		    		String[] b = ticketobj.getString("title").split("-");
		    		String title = b[0] + "- " + l.getName();
		    		
		    		ticketobj.put("title", title);  
					ticketobj.put("lid", l.getLID()); // add LID to json object
					json = ticketobj.toString();
					i.putExtra("ticket", json );
					startActivity(i);
					
				} catch (JSONException e) {
					
				}
		    	
		
		    }
		});
	}
	
	
	/**
	 * Represents an asynchronous local task used to get locals
	 */
	private class LocalsLoadTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean loadLocals()
		{
			try{
				locallist = Job.localslist(ticketobj.getInt("sid"));
				
				//Log.w("UCFRONTDESK", ">>> " + locallist.get(0).getName() ); 
				
			}catch(Exception e){
				Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			}
			
			if (locallist == null){	
				Toast.makeText(getApplicationContext(), "Erro ao Carregar Locais!", Toast.LENGTH_SHORT).show();
				return false;
			}
			
			return true;
			
		}

		protected Boolean doInBackground(String... params) 
		{
			if (loadLocals())
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
			dialog = ProgressDialog.show(LocalsActivity.this, "A Carregar Locais...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   
}