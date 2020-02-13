package dawn.zenquest;


import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.callbacks.QueryCallback;
import org.jbox2d.collision.AABB;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.MouseJoint;
import org.jbox2d.dynamics.joints.MouseJointDef;
import org.json.JSONObject;

//import com.navin.activities.GameActivity;
//import com.navin.activities.MainActivity;
//import com.navin.activities.SharedPreferencesManager;
//import com.navin.debug.TestActivity;
//import com.tszy.jbox2d.testbed.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * ���Ի���
 *
 * @author Navin -- Contact listener and simulated views here
 *
 */

public abstract class Box2dSurfaceView extends SurfaceView implements Callback, Runnable {
	public static final int GAME_HEART = 1000 / 30; // ÿ��ˢ��30��
	public static final float RATE = 30.0f; // �����
	public static final float ACCELEROMETER_SCALE = 25.0f;
	public static int screenW=1, screenH=1;

	private Thread thread;
	private SurfaceHolder holder;

	protected Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);;
	private SensorManager mSensorManager = null;

	protected World world;
	protected MouseJointDef mouseJointDef;
	protected MouseJoint mouseJoint;
	protected Body m_ground;
	private Box2dDebugDraw debugDraw;	//���Ի�ͼ

	private float dt = 1.0f/30.0f; 	//ģ��Ƶ��
	private int velIter = 8;				//�ٶȵ�����
	private int posIter = 3; 				//λ�õ�����
	String timer;
	int num;
	int min;
	int sec;
	Animation animFadein,animFadeout;


	POJO pojo;

	Bitmap backgraaaa;
	Bitmap hole;
	Bitmap background;
	Context context;
	TextView message;
	int level;
	public Box2dSurfaceView(Context context,Bitmap back,POJO pojo,int level,TextView message) {
		super(context);
		this.context=context;
		this.backgraaaa = ((BitmapDrawable) getResources().getDrawable(R.drawable.backgroud)).getBitmap();
		this.pojo=pojo;
		level=level;
		this.message=message;
		Drawable drawable3 =getResources().getDrawable(R.drawable.hole);
		hole = ((BitmapDrawable) drawable3).getBitmap();
		background=((BitmapDrawable) getResources().getDrawable(R.drawable.brik)).getBitmap();

		// TODO Auto-generated constructor stub
		init();
	}

	/////////////////////////////////////////////////////////////////////////
	private void init() {
		ShowTime();
		holder = getHolder();
		holder.addCallback(this);

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setStyle(Style.FILL);
		paint.setTextSize(12);


	}




	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawText(timer, 0f, 80f, paint);
		super.onDraw(canvas);
	}


	private void initWorld() {
		Vec2 gravity = new Vec2(0, -10f);
		world = new World(gravity, true);

		BodyDef bodyDef = new BodyDef();
		m_ground = world.createBody(bodyDef);



		mouseJointDef = new MouseJointDef();

		debugDraw = new Box2dDebugDraw(this,pojo);//DebugDraw.e_aabbBit |
		debugDraw.setFlags( DebugDraw.e_shapeBit
				| DebugDraw.e_jointBit
				| DebugDraw.e_centerOfMassBit);
		world.setDebugDraw(debugDraw);


		world.setContactListener(cl);
	}

	private void destoryWorld(){
		Body body = world.getBodyList();

		for(int i=0; i<world.getBodyCount(); ++i){
			if(body != null)
				world.destroyBody(body);
			body = body.getNext();
		}

		Joint joint = world.getJointList();

		for(int i=0; i<world.getJointCount(); ++i){
			if(joint != null)
				world.destroyJoint(joint);
			joint = joint.getNext();
		}

		world = null;
		debugDraw = null;
		mouseJointDef = null;
		m_ground = null;
	}

	public World getWorld() {
		return world;
	}



	public void _update() {
		world.step(dt, velIter, posIter);
	}

	private Rect rect = new Rect();



	public void setColor(int color) {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		paint.setColor(Color.rgb(red, green, blue));
	}


	private void drawFPS(Canvas canvas) {
		paint.setTextSize(22f);
		setColor(Color.parseColor("#000000"));

		if (useTime > 0){
//			if (useTime > 40){
//			totalTime=totalTime+1;}

			num=totalTime/60;

			sec=sec+num;

			if(sec==5){min=min+1;sec=0;}
			String text=min+"Mins "+sec+"Secs";

			canvas.drawText(text,
					3, 3 + paint.getTextSize(),
					paint);


		}

	}


	public void worldDraw(Canvas canvas) {
		debugDraw.draw(canvas);
		world.drawDebugData();
	}


	private Vec2 v1 = new Vec2();
	private Vec2 v2 = new Vec2();
	private void mouseJointDraw(Canvas canvas) {

		if (mouseJoint != null) {
			paint.setColor(0xff00ff00);


			mouseJoint.getAnchorA(v1);

			debugDraw.getWorldToScreenToOut(v1, v1);

			mouseJoint.getAnchorB(v2);

			debugDraw.getWorldToScreenToOut(v2, v2);

			canvas.drawLine(v1.x, v1.y, v2.x, v2.y, paint);
		}
	}


	public void _draw(Canvas canvas) {



		drawBackground(canvas);
		//                                                                                                                                                                                                                	plot(canvas);


		worldDraw(canvas);

	}



	public abstract void initTest();
	public abstract String getName();



	public static Vec2 world2Scn(Vec2 p) {
		Vec2 out = new Vec2();
		world2ScnOut(p, out);
		return out;
	}

	public static void world2ScnOut(Vec2 p, Vec2 out) {
		out.set(p.x*RATE, p.y*RATE);
	}


	public static Vec2 scn2wrold(Vec2 p) {
		Vec2 out = new Vec2();
		scn2wroldOut(p, out);
		return out;
	}

	public static void scn2wroldOut(Vec2 p, Vec2 out) {
		out.set(p.x/RATE, p.y/RATE);
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean b = false;

//		b = mouseJointEvent(event);
//		if(!b)
//			b = debugDraw.onTouchEvent(event);
//		if(!b)
//			return super.onTouchEvent(event);

		return b;
	}

	//�����ѯ
	private final AABB queryAABB = new AABB();
	private final TestQueryCallback callback = new TestQueryCallback();
	private Body mouseDragBody;	//��ǰ���קס������
	private final Vec2 mouseDownPoint = new Vec2(0, 0);

	private boolean mouseJointEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();


		debugDraw.getScreenToWorldToOut(x, y, mouseDownPoint);

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN : {
				if (mouseJoint != null) {
					world.destroyJoint(mouseJoint);
					mouseJoint = null;
					mouseDragBody = null;
				}


				queryAABB.lowerBound.set(mouseDownPoint.x - 3/RATE, mouseDownPoint.y - 3/RATE);
				queryAABB.upperBound.set(mouseDownPoint.x + 3/RATE, mouseDownPoint.y + 3/RATE);
				world.queryAABB(callback, queryAABB);

				if (mouseDragBody != null) {

					mouseJointDef.bodyA = m_ground;
					mouseJointDef.bodyB = mouseDragBody;
					mouseJointDef.target.set(mouseDownPoint);
					mouseJointDef.maxForce = 1000f * mouseDragBody.getMass();
					mouseJoint = (MouseJoint) world.createJoint(mouseJointDef);
					mouseDragBody.setAwake(true);

					return true;
				}

				break;
			}

			case MotionEvent.ACTION_MOVE : {
				if (mouseJoint != null) {
					mouseJoint.setTarget(mouseDownPoint);
					return true;
				}
				break;
			}

			case MotionEvent.ACTION_UP : {
				if (mouseJoint != null) {
					world.destroyJoint(mouseJoint);
					mouseJoint = null;
					mouseDragBody = null;
					return true;
				}
				break;
			}
		}

		return false;
	}




	class TestQueryCallback implements QueryCallback {

		@Override
		public boolean reportFixture(Fixture fixture) {
			// TODO Auto-generated method stub

			Body body = fixture.getBody();
			AABB aabb = fixture.getAABB();

			if (body.getType() == BodyType.DYNAMIC) {
				if (AABB.testOverlap(aabb, queryAABB)) {
					mouseDragBody = body;
					return false;
				}
			}
			return true;
		}
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		screenW = getWidth();
		screenH = getHeight();
		mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(mAccelListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

		initWorld();
		initTest();


		thread = new Thread(this);
		isRun = true;
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		screenW = getWidth();
		screenH = getHeight();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		isRun = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		thread = null;
		destoryWorld();
	}

	private boolean isRun;
	private int useTime;
	private int totalTime;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long start, end;

		while (isRun) {
			try{
				start = System.currentTimeMillis();
				{

					Canvas canvas = holder.lockCanvas();


					dropIntoHole(getWorld(),canvas);
					_update();

					_draw(canvas);

					end = System.currentTimeMillis();
					holder.unlockCanvasAndPost(canvas);
				}
				useTime = (int) (end - start);
				totalTime=totalTime+1;
			}catch(Exception e){
				e.printStackTrace();
			}

			if (useTime < GAME_HEART) {
				try {
					Thread.sleep(GAME_HEART - useTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}



	////////////////////--------------------/////////////////
	public int getMyRotation()	{
		Display display = ((WindowManager) (getContext().getSystemService(Context.WINDOW_SERVICE)))
				.getDefaultDisplay();

		int rotation = display.getRotation();

		Point size = new Point();

		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);

		size.set(dm.widthPixels, dm.heightPixels);

		int lock = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

		if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)	{
			// if rotation in 0 or 180 and width is greater than height, we have
			// a tablet


			if (size.x > size.y)	{
				if (rotation == Surface.ROTATION_0){
					lock = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				}	else {
					lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				}
			}	else {
				// we have a phone
				if (rotation == Surface.ROTATION_0)	{
					lock = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				}	else {
					lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				}
			}

		}	else {
			// if rotation is 90 or 270 and width is greater than height,
			// we have a phone

			if(size.x > size.y)	{
				if (rotation == Surface.ROTATION_90)	{
					lock = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

				}	else {
					lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;

				}
			}	else {

				//we have a tablet
				if(rotation == Surface.ROTATION_90)	{
					lock = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				}	else {
					lock = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				}

			}
		}


		return lock;

	}


	private final SensorEventListener mAccelListener = new SensorEventListener() {


		@Override
		public void onSensorChanged(SensorEvent event) {
			float xaccel;
			float yaccel;

			int lock = getMyRotation();
			switch(lock) {
				case	ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
					xaccel = (-event.values[0]) * ACCELEROMETER_SCALE;
					yaccel = (-event.values[1]) * ACCELEROMETER_SCALE;
					break;


				case	ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
					xaccel = (-event.values[1]) * ACCELEROMETER_SCALE;
					yaccel = (event.values[0]) * ACCELEROMETER_SCALE;
					break;

				case	ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT :
					xaccel = (event.values[0]) * ACCELEROMETER_SCALE;
					yaccel = (event.values[1]) * ACCELEROMETER_SCALE;
					break;


				case	ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE	:
					xaccel = (event.values[1]) * ACCELEROMETER_SCALE;
					yaccel = (-event.values[0]) * ACCELEROMETER_SCALE;
					break;

				default :
					xaccel = (-event.values[0]) * ACCELEROMETER_SCALE;
					yaccel = (-event.values[1]) * ACCELEROMETER_SCALE;
					break;

			}

			if(getWorld() != null) {
				getWorld().setGravity(new Vec2(xaccel, yaccel));
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}
	};





	public void drawBackground(Canvas canvas){


		try {
			canvas.drawBitmap(backgraaaa, v1.x / 2, v1.y / 2, paint);
		}catch (Exception e){
			e.printStackTrace();
		}


	}




	////

	public void dropIntoHole(World world,Canvas canvas){

		//boolean gameOver=false;

		int gameOver=0;

		int totalDynamicBodies=0;


		int yMid=(screenW/5)/2;

		Map<Integer,Integer> centerMap=new HashMap<Integer,Integer>();

		centerMap.put(yMid,0);
		centerMap.put(yMid+5,0);
		centerMap.put(yMid-5,0);
		centerMap.put(0,yMid-5);
		centerMap.put(1,yMid+5);

//		createHolesAt(-15f, yMid-4);// 2
//		createHolesAt(-14f, yMid+10);// 2u
//		createHolesAt(0f, yMid-12);// 1
//		createHolesAt(0f, yMid+22);//3
//		createHolesAt(22f, yMid+6);// 3l

		Map<Integer,Integer> holesMap=new HashMap<Integer,Integer>();
		//holesMap.put(-16,yMid-4);
		holesMap.put(-15,yMid-4);
		//holesMap.put(-14,yMid-4);

		//holesMap.put(-9,yMid+10);
		holesMap.put(-14,yMid+10);
		//holesMap.put(-11,yMid+10);


		//holesMap.put(-1,yMid-8);
		holesMap.put(0,yMid-12);
		//holesMap.put(1,yMid-8);

		//holesMap.put(2,yMid+20);
		holesMap.put(1,yMid+22);
		//holesMap.put(4,yMid+20);


		//holesMap.put(17,yMid+6);
		holesMap.put(22,yMid+6);
		//holesMap.put(19,yMid+6);

		//System.out.println("x----15-----"+String.valueOf( yMid+6 )  );


		allInCenter(world);






		for ( Body b = world.getBodyList(); b!=null; b = b.getNext() )
		{


			if(b.getType().equals(BodyType.DYNAMIC)){

				//System.out.println("x----y"+ String.valueOf( b.getPosition().x )+"-----"+String.valueOf( b.getPosition().y )  );

				try {

					if(holesMap.containsKey((int)b.getPosition().x)){

						//	System.out.println("----range------------");

						int thisValue=(int)b.getPosition().y;

						int left=holesMap.get((int)b.getPosition().x).intValue()+5;

						int right=holesMap.get((int)b.getPosition().x).intValue()-5;
						//thisValue-115--right------109-------left--119



						if(thisValue>=right&&thisValue<=left){
//								System.out.println("------x-"+ (int)b.getPosition().x);
//								System.out.println("------thisValue-"+ thisValue+"--right------"+right+"-------left--"+left);

							//Toast.makeText(context, "Dropped..H", Toast.LENGTH_LONG).show();

							canvas.drawText("Dropped..H",
									50, 50 ,
									paint);

							b.setLinearVelocity(new Vec2(0f, 0f));
							b.setTransform(new Vec2(0f, 75f), 0);
							SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
							int mySound = sp.load(context, R.raw.tileclick, 1);
							// sp.play(mySound, 3f, 3f, 0, 0, 1f);
							sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
								@Override
								public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
									soundPool.play(sampleId, 10.0f, 10.0f, 0, 0, 1.0f);
								}
							});
						}


					}

					if(centerMap.containsKey((int)b.getPosition().x)||centerMap.containsKey((int)b.getPosition().x)){

						//	System.out.println("----range------------");

						int thisValue=(int)b.getPosition().y;

						int left=holesMap.get((int)b.getPosition().x).intValue();

						int right=holesMap.get((int)b.getPosition().x).intValue();
						//thisValue-115--right------109-------left--119



						if(thisValue>=right&&thisValue<=left){

							canvas.drawText("Dropped..C",
									50, 50 ,
									paint);


//							animFadein = AnimationUtils.loadAnimation(context, R.anim.fadein);
//							animFadeout = AnimationUtils.loadAnimation(context, R.anim.fadein);
//							message.setText("Lust defeated you ! take a deep breath");
//							message.startAnimation(animFadeout);

							//Toast.makeText(context, "Dropped..H", Toast.LENGTH_LONG).show();

							System.out.println("------x-"+ (int)b.getPosition().x);
							System.out.println("------thisValue-"+ thisValue+"--right------"+right+"-------left--"+left);

							b.setLinearVelocity(new Vec2(0f, 0f));
							b.setTransform(new Vec2(0f, 75f), 0);
							SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
							int mySound = sp.load(context, R.raw.tileclick, 1);
							// sp.play(mySound, 3f, 3f, 0, 0, 1f);
							sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
								@Override
								public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
									soundPool.play(sampleId, 10.0f, 10.0f, 0, 0, 1.0f);
								}
							});
						}


					}



				}catch (Exception e){}

			}




		}



	}

	public Boolean inTheHole(Body b,int x,int y){
		Boolean yes=false;
		if((b.getPosition().y<=71.0f&&b.getPosition().y>=69.0f)&&( b.getPosition().x>=-1f&&b.getPosition().x<=1)){yes=true;}

		return yes;
	}

	public void allInCenter(World world){

		new CenterTask(world).execute();
	}



	public class CenterTask extends AsyncTask<Void, Void, Boolean> {
		private String URL;
		World world;


		public CenterTask(World world) {
			this.world = world;

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean all = true;

			for (Body b = world.getBodyList(); b != null; b = b.getNext()) {

				if (b.getType().equals(BodyType.DYNAMIC)) {

					if ((b.getPosition().y<=112&&b.getPosition().y>=103)&&( b.getPosition().x>=1&&b.getPosition().x<=4)) {

						all = true;

					} else {
						all = false;
					}
				}
			}
			return all;

		}

		@Override
		protected void onPostExecute(Boolean s) {
			super.onPostExecute(s);
			if (s) {
				//	Toast.makeText(context, "Done..", Toast.LENGTH_LONG).show();
				animFadein = AnimationUtils.loadAnimation(context, R.anim.fadein);
				animFadeout = AnimationUtils.loadAnimation(context, R.anim.fadein);
				message.setText("Wooow ! Victory");
				message.startAnimation(animFadein);

			}


		}
	}



	public Boolean inTheCenter(Body b){
		Boolean yes=false;




		if((b.getPosition().y<=112&&b.getPosition().y>=103)&&( b.getPosition().x>=1&&b.getPosition().x<=4)){

			yes=true;
		}
		return yes;
	}

	ContactListener cl=new ContactListener() {

		@Override
		public void beginContact(Contact arg0) {
			// TODO Auto-generated method stub


			String name1 = (String)arg0.getFixtureA().getBody().getUserData();
			String name2 = (String)arg0.getFixtureB().getBody().getUserData();;




		}

		@Override
		public void endContact(Contact arg0) {
			// TODO Auto-generated method stub


		}

		@Override
		public void postSolve(Contact arg0, ContactImpulse arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void preSolve(Contact arg0, Manifold arg1) {
			// TODO Auto-generated method stub

		}


	};

	public void ShowTime(){


		new CountDownTimer(60000, 1000) {

			public void onTick(long millisUntilFinished) {
				timer = String.valueOf(millisUntilFinished / 1000);
			}

			public void onFinish() {
			}
		}.start();



	}



}
