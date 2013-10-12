package pt.uc.dei.mrc.uctickets.ui;


import pt.uc.dei.mrc.uctickets.apiclient.Job;
import pt.uc.dei.mrc.uctickets.models.Login;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		
		String loginlist[] = {"Entrar"}; //opcoes menu login
		
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
			
			//busca token no servidor
			Log.w("UCFRONTDESK", "EMAIL: " + email.getText().toString());
			
			//Login l = Job.LoginKey(email.getText().toString(), password.getText().toString());
			
			//String key = l.getKey();
			
			//Log.w("UCFRONTDESK", "KEY: " + key);
			
			String token = "abc";
			
			if (token != null)
			{
				return true;
			}else
			{
				return false;
			}
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
				dialog.dismiss();
				Intent i = new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(i);
			}else
			{
				dialog.dismiss();
				Toast.makeText(LoginActivity.this, "Erro ao efectuar login!", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() 
		{
			dialog = ProgressDialog.show(LoginActivity.this, "A efectuar login...", "Aguarde...", true);
		}

		@Override
		protected void onProgressUpdate(Void... values) 
		{
		}
	}   

}