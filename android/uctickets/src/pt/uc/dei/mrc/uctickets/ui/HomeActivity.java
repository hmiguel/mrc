package pt.uc.dei.mrc.uctickets.ui;


import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends Activity {

	ListView listview;
	
	JSONObject ticketobj;
	 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		ticketobj = new JSONObject(); 
		
		String homelist[] = {"Criar Senha", "Gerir Senhas"};
		
		ListView list = (ListView) findViewById(R.id.Homelist);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, homelist);
        
		list.setAdapter(adapter);

		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	
		    	Intent i;
		    			    	
		    	if(position == 0){
		    		
		    		String json; 
		    		i = new Intent(HomeActivity.this, ServicesActivity.class);
			    	
			    	try {
						ticketobj.accumulate("uid", 1); // add UID
						ticketobj.accumulate("active", 1); // add ACTIVE
						json = ticketobj.toString();
						i.putExtra("ticket", json );
						
						startActivity(i);
			    	} catch (JSONException e) {
						// TODO Auto-generated catch block
						//
					}
			    	
			    	
			    	
		    	}else if(position == 1){
		    		
		    		i = new Intent(HomeActivity.this, ServicesActivity.class);
		    		startActivity(i);
		    	}
		    	
		    	//Intent to Next Activity
		    	
		    	
		    	//Local l = (Local)parent.getAdapter().getItem(position); // Local Object
		    	
		    	//Toast.makeText(getApplicationContext(), ">"+l.getLID(), Toast.LENGTH_SHORT).show();
		    	
		    	//i.putExtra("local", Local l );
		    	
		    	//Construir Objecto Ticket para enviar
				
		    	
		    	
		       // Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}