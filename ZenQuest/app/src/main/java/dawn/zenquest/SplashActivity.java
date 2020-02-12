package dawn.zenquest;




import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_splash);
		

        Thread background1 = new Thread(){

            public void run() {

                try {

                    // Thread will sleep for 5 seconds
                    sleep(2 * 2000);





                    // After 5 seconds redirect to another intent
                    Intent i = new Intent(getBaseContext(), GameActivity.class);
                    startActivity(i);

                   // Remove activity{
                    finish();

                } catch (Exception e) {

                }
            }
        };
//
//// start thread
        background1.start();



		
	}

	

}
