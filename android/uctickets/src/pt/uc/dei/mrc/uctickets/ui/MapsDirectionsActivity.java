package pt.uc.dei.mrc.uctickets.ui;

import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MapsDirectionsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps_directions);
		
		Bundle extras = getIntent().getExtras();
		

		if (extras != null) {
			ArrayAdapter<String> adapter;
			ArrayList<String> texto = extras.getStringArrayList("textoMap");
			ArrayList<String> aparece = new ArrayList<String>();
			
			ListView apre = (ListView)findViewById(R.id.listView);
			
			String textoApre = "";
			
			for(String dir : texto){
				textoApre = Jsoup.parse(dir).text();
				aparece.add(textoApre);
				
			}
			
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aparece);
			apre.setAdapter(adapter);
			
		}
		
	}
}