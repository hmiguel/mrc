package pt.uc.dei.mrc.uctickets.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	
	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		
		Button btLogin = (Button)findViewById(R.id.enter);

		btLogin.setOnClickListener(new View.OnClickListener() 
		{ 
			public void onClick(View v)
			{
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
			//....
			
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