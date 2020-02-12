package dawn.zenquest;




import android.support.v4.app.Fragment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {

	//@SuppressLint("NewApi")
	int fullLevel=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		SharedPreferencesManager sm=new SharedPreferencesManager(getApplicationContext());
		int level=sm.getLevel();
		//Toast.makeText(this, "now level wasssss----"+level+"--", Toast.LENGTH_SHORT).show();
		int fullLevel=1;
		if(level<3){
		 fullLevel=level+1;	}
		else{fullLevel=3;}
		
		sm.setLevel(fullLevel);
		TextView tv=(TextView)findViewById(R.id.textViewMain);
		
		tv.setText("Level "+level+" Completed \n Press Continue to go to level :"+fullLevel+"....");
		
		Button continu=(Button)findViewById(R.id.continue1);
		final int lvl=level;
		continu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(MainActivity.this,GameActivity.class);
				i.putExtra("fullLevel", lvl);
				startActivity(i);
				
			}
		});
		
		
		
		//getActionBar().hide();

		
	}

	

	
}
