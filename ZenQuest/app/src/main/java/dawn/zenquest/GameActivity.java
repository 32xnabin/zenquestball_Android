package dawn.zenquest;

import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.SurfaceView;


//import com.navin.test.Domino;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;


//import com.navin.test.SliderCrankTest;
//import com.navin.test.VaryingRestitution;


public class GameActivity extends Activity {

	int level;
	SurfaceView view;

	TextView textTimer,topText,message;

	int count=0;
	Timer T;
	Animation animFadein,animFadeout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Drawable myDrawable =getResources().getDrawable(R.drawable.backgroud);
		Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();

		POJO pojo=new POJO();



		Drawable drawable0 =getResources().getDrawable(R.drawable.wooden);
		Bitmap carpet = ((BitmapDrawable) drawable0).getBitmap();



		Drawable drawable1 =getResources().getDrawable(R.drawable.hole);
		Bitmap ball4 = ((BitmapDrawable) drawable1).getBitmap();


		pojo.setBackground(carpet);
		pojo.setBall(ball4);


		//  view=new Box2dView(this,createDynamicGradient("#cccccc"),pojo,level);
		topText = (TextView) findViewById(R.id.topText);
		message =(TextView) findViewById(R.id.message);

		view=new Box2dView(this,myLogo,pojo,level,message);



		setContentView(R.layout.game_activity);
		LinearLayout ll=(LinearLayout)findViewById(R.id.gamePart);
		ll.addView(view);


		textTimer = (TextView)findViewById(R.id.time);
		T=new Timer();
		T.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				int min=count/60;
				int sec=count%60;

				textTimer.setText(min+":"+sec);
				count++;
			}
		}, 1000, 1000);



		animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
		animFadeout = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
		Update();
	}

	public void Update(){
		TextView lblEstadoPuerta = (TextView) findViewById(R.id.topText);

		lblEstadoPuerta.startAnimation(animFadeout);
		textTimer.startAnimation(animFadeout);

	}
	public String checkDigit(int number) {
		return number <= 9 ? "0" + number : String.valueOf(number);
	}




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
		//this.getWindowManager().removeView(view);




		//setContentView(null);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();

	}
}
