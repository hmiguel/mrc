package pt.uc.dei.mrc.uctickets.ui;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MapsDirectionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_directions);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps_directions, menu);
		return true;
	}

}
