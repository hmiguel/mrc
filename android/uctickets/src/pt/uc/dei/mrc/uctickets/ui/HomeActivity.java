package pt.uc.dei.mrc.uctickets.ui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		
		Button btLogin = (Button)findViewById(R.id.new_service);

		btLogin.setOnClickListener(new View.OnClickListener() 
		{ 
			public void onClick(View v)
			{
			
				Intent i = new Intent(HomeActivity.this, ServicesActivity.class);
				startActivity(i);
				
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