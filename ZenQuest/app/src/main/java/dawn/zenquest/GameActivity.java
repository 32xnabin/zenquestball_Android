package dawn.zenquest;

import java.util.zip.Inflater;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.SurfaceView;


//import com.navin.test.Domino;


import android.widget.LinearLayout;
import android.widget.Toast;


//import com.navin.test.SliderCrankTest;
//import com.navin.test.VaryingRestitution;


public class GameActivity extends Activity {

	int level;
	SurfaceView view;
	//@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Drawable myDrawable =getResources().getDrawable(R.drawable.backgroud1);
		Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();

		POJO pojo=new POJO();

		Drawable drawable1 =getResources().getDrawable(R.drawable.small);
		Bitmap ball1 = ((BitmapDrawable) drawable1).getBitmap();

		Drawable drawable2 =getResources().getDrawable(R.drawable.wooden);
		Bitmap ball2 = ((BitmapDrawable) drawable2).getBitmap();

		Drawable drawable3 =getResources().getDrawable(R.drawable.brik);
		Bitmap ball3 = ((BitmapDrawable) drawable3).getBitmap();

		Drawable drawable4 =getResources().getDrawable(R.drawable.hole);
		Bitmap ball4 = ((BitmapDrawable) drawable4).getBitmap();

		pojo.setBall1(ball1);
		pojo.setBall2(ball2);
		pojo.setBall3(ball3);
		pojo.setBall4(ball4);

		//level=sm.getLevel();
		SharedPreferencesManager sm=new SharedPreferencesManager(getApplicationContext());
		int level=sm.getLevel();
		Toast.makeText(this, "level---"+level, Toast.LENGTH_LONG).show();

		//  view=new Box2dView(this,createDynamicGradient("#cccccc"),pojo,level);

		view=new Box2dView(this,myLogo,pojo,level);



		setContentView(R.layout.game_activity);
		LinearLayout ll=(LinearLayout)findViewById(R.id.gamePart);
		ll.addView(view);

		//getActionBar().hide();

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
		super.onBackPressed();

	}
}
