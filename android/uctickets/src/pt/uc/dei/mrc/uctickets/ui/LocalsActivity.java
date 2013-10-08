package pt.uc.dei.mrc.uctickets.ui;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LocalsActivity extends Activity {

	LinearLayout ll;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locals);
		
		ll = (LinearLayout)findViewById(R.id.linear_layout_locals);
		
		createRadioButton();
		
		Button btLogin = (Button)findViewById(R.id.totickets);

		btLogin.setOnClickListener(new View.OnClickListener() 
		{ 
			public void onClick(View v)
			{
			
				Intent i = new Intent(LocalsActivity.this, TicketsActivity.class);
				startActivity(i);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.locals, menu);
		return true;
	}

	private void createRadioButton() {
	    final RadioButton[] rb = new RadioButton[5];
	    final RadioGroup rg = new RadioGroup(this);
	    rg.setOrientation(RadioGroup.VERTICAL);
	    
	    for(int i=1; i<4; i++){
	        rb[i]  = new RadioButton(this);
	        rb[i].setTextColor(Color.rgb(0, 201, 234));
	        rg.addView(rb[i]); 
	        rb[i].setText("Local " + i);
	    }
	    
	    ll.addView(rg);
	    
	    
	}
}