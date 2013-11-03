package pt.uc.dei.mrc.uctickets.ui;


import org.json.JSONException;
import org.json.JSONObject;

import pt.uc.dei.mrc.uctickets.apiclient.Job;
import pt.uc.dei.mrc.uctickets.models.Login;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	int uid;
	String token;
	
	JSONObject ticketobj;
	JSONObject l;
	
	private ProgressDialog dialog;
	private SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		String loginlist[] = {"Entrar"}; //opcoes menu login
		
		ticketobj = new JSONObject();
		
		ListView list = (ListView) findViewById(R.id.Loginlist);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.login_list, loginlist);
        
		list.setAdapter(adapter);
		
		list.setBackgroundColor(Color.rgb(0, 201, 234));
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
		    	
		    	//Verify Login
		    	new LoginTask().execute();
		    	
		    }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate the user.
	 */
	private class LoginTask extends AsyncTask<String, Void, Boolean> 
	{
		protected boolean doLogin()
		{
			EditText email = (EditText)findViewById(R.id.email_login);
			EditText password = (EditText)findViewById(R.id.psw_login);
			
			JSONObject login = new JSONObject();
			
			try {
				login.put("email", email.getText().toString());
				login.put("password", password.getText().toString());
				
			} catch (JSONException e) {
				
				return false;
			}
			
			l = Job.LoginKey(login.toString());
					
			if (l.length() > 1){
					
					return true;
			}
				
			return false;
		}

		protected Boolean doInBackground(String... params) 
		{
			if (doLogin())
			{
				return true;
			}else
			{
				return false;
			}
		}      

		@Override
		protected void onPostExecute(Boolean result) 
		{
			if (result)
			{
				
				Intent i = new Intent(LoginActivity.this, HomeActivity.class);
				
				String json; 
	    		
		    	try {
		    		ticketobj.put("uid", l.getInt("uid")); // add UID
					ticketobj.put("token", l.getString("token")); //add Token
					json = ticketobj.toString();
										
					i.putExtra("ticket", json );
					
					prefs = getSharedPreferences("ucfrontdesk", Context.MODE_PRIVATE);
					
					SharedPreferences.Editor prefsEdit = prefs.edit();
					prefsEdit.putString("ucfrontdesk.token", l.getString("token"));
					prefsEdit.commit();

					startActivity(i);
					
					dialog.dismiss();
					
		    	} catch (JSONException e) {
					// TODO Auto-generated catch block
		    		Log.w("UCFRONTDESK", "Content: " + ticketobj.toString() ); 
				}
				
				
				
			}else
			{
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, "Erro ao efectuar login!", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(LoginActivity.this, "A Processar...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   

}