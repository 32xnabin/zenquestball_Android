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
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


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


	POJO pojo;

     Bitmap backgraaaa;
     Bitmap hole;
     Bitmap background;
     Context context;
     int level;
	public Box2dSurfaceView(Context context,Bitmap ball,POJO pojo,int level) {
		super(context);
		this.context=context;
		this.backgraaaa =ball;
		this.pojo=pojo;
		level=level;
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

		paint = new Paint(Paint.ANTI_ALIAS_FLAG);// �޾��
		paint.setStyle(Style.FILL); // �����ʽ
		paint.setTextSize(12); // �����С


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

////////////////////////////////////////////////////////////////////////////////
	/**
	 * ִ����Ϸ�߼�����
	 */
	public void _update() {
		world.step(dt, velIter, posIter);
	}

	private Rect rect = new Rect();
	public void drawDeclare(Canvas canvas) {
		String text = "��ʹ֮���ʾ��Demo";
		// ��ȡ�ı����
		paint.getTextBounds(text, 0, text.length(), rect);
		// ����Ļ����λ����ʾ�ı�
		paint.setColor(0xfff000f0); // ע�������λ ff ��?��͸���ȣ������õĻ�������ȫ͸���ˣ��������κ�Ч��
		canvas.drawText(text,
				(screenW - rect.width()) / 2, screenH / 2 + rect.height() / 2,
				paint);
	}

	/**
	 * ���û�����ɫ
	 * @param color
	 */
	public void setColor(int color) {
		int red = (color & 0xff0000) >> 16;
		int green = (color & 0x00ff00) >> 8;
		int blue = (color & 0x0000ff);
		paint.setColor(Color.rgb(red, green, blue));
	}

	/**
	 * ����FPS
	 * @param canvas
	 */
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

	/**
	 * BOX2D ���Ի�ͼ
	 * @param canvas
	 */
	public void worldDraw(Canvas canvas) {
		debugDraw.draw(canvas);
		world.drawDebugData();
	}

	/**
	 * �����ؽ�
	 */
	private Vec2 v1 = new Vec2();
	private Vec2 v2 = new Vec2();
	private void mouseJointDraw(Canvas canvas) {
		// �����ؽ�
		if (mouseJoint != null) {
			paint.setColor(0xff00ff00);


			mouseJoint.getAnchorA(v1);
			//ת�����ӽ��е�λ��
			debugDraw.getWorldToScreenToOut(v1, v1);

			mouseJoint.getAnchorB(v2);
			//ת�����ӽ��е�λ��
			debugDraw.getWorldToScreenToOut(v2, v2);

			canvas.drawLine(v1.x, v1.y, v2.x, v2.y, paint);
		}
	}

	/**
	 * ִ����Ϸ����
	 */
	public void _draw(Canvas canvas) {

		//canvas.drawColor(Color.CYAN);

		drawBackground(canvas);
		plot(canvas);


		worldDraw(canvas);
//		mouseJointDraw(canvas);
//		drawFPS(canvas);
//		drawDeclare(canvas);
	}



	public abstract void initTest();
	public abstract String getName();


////////////////////////////////////////////////////////////////////////////////
	/**
	 * ����λ��תΪ��Ļλ��
	 * @param p
	 * @return
	 */
	public static Vec2 world2Scn(Vec2 p) {
		Vec2 out = new Vec2();
		world2ScnOut(p, out);
		return out;
	}

	public static void world2ScnOut(Vec2 p, Vec2 out) {
		out.set(p.x*RATE, p.y*RATE);
	}

	/**
	 * ��Ļλ��תΪ����λ��
	 * @param p
	 * @return
	 */
	public static Vec2 scn2wrold(Vec2 p) {
		Vec2 out = new Vec2();
		scn2wroldOut(p, out);
		return out;
	}

	public static void scn2wroldOut(Vec2 p, Vec2 out) {
		out.set(p.x/RATE, p.y/RATE);
	}


///////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean b = false;

		b = mouseJointEvent(event);
		if(!b)
			b = debugDraw.onTouchEvent(event);
		if(!b)
			return super.onTouchEvent(event);

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

//		mouseDownPoint.set(event.getX(), event.getY());
//		scn2wroldOut(mouseDownPoint, mouseDownPoint);
		//�ӽ��е�λ��
		debugDraw.getScreenToWorldToOut(x, y, mouseDownPoint);

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN : {
				if (mouseJoint != null) {
					world.destroyJoint(mouseJoint);
					mouseJoint = null;
					mouseDragBody = null;
				}

				//��ת��Ϊ�������е�λ�ã���Ϊ�����λ��ֻ�ǿ��������λ�ö���
				queryAABB.lowerBound.set(mouseDownPoint.x - 3/RATE, mouseDownPoint.y - 3/RATE);
				queryAABB.upperBound.set(mouseDownPoint.x + 3/RATE, mouseDownPoint.y + 3/RATE);
				world.queryAABB(callback, queryAABB);

				if (mouseDragBody != null) {

					mouseJointDef.bodyA = m_ground;
					mouseJointDef.bodyB = mouseDragBody;
					mouseJointDef.target.set(mouseDownPoint); // Ŀ�ĵ�
					mouseJointDef.maxForce = 1000f * mouseDragBody.getMass(); // �������
					mouseJoint = (MouseJoint) world.createJoint(mouseJointDef); // ���ؽ�
					mouseDragBody.setAwake(true); // ����body

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



	//��ѯ�ص�
	class TestQueryCallback implements QueryCallback {
		/**
		 * �����ѯ�����false��ֹ��ѯ
		 */
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

///////////////////////////////////////////////////////////////////////////////
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		screenW = getWidth();
		screenH = getHeight();
		mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(mAccelListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		//һ��Ҫ�ڻ�ȡ��Ļ��ߺ����
		initWorld();
		initTest();

		//���߳�
		thread = new Thread(this);
		isRun = true;
		thread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		// ����Ļ��ת��ʱ�����»�ȡ��Ļ���
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

	private boolean isRun; // �߳����б�־
	private int useTime; // ��¼ÿ��ˢ��ʹ�õ�ʱ��
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
				//dropIntoHole(getWorld());
				_update(); // ˢ�½���������Ԫ��

				_draw(canvas); // ���ƽ���Ԫ��

				end = System.currentTimeMillis();
				holder.unlockCanvasAndPost(canvas);
			}
			useTime = (int) (end - start);
			totalTime=totalTime+1;
			}catch(Exception e){}

			if (useTime < GAME_HEART) { // ��֤ÿ��ˢ��ʱ������ͬ
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

public void plot(Canvas canvas){
//	   simulateSmoothCircle(canvas,300);
//	    simulateSmoothCircle(canvas,298);

//		simulateSmoothCircle(canvas,252);
//
//
//		simulateSmoothCircle(canvas,212);
//
//		simulateSmoothCircle(canvas,172);
//
//		simulateSmoothCircle(canvas,132);
//
//		simulateSmoothCircle(canvas,92);
//		simulateSmoothCircle(canvas,52);
//
//		drawLine(canvas,(screenW/2)-210,(screenH/2+10),1);
//		drawLine(canvas,(screenW/2)+172,(screenH/2+36),1);
//		drawLine(canvas,(screenW/2+57),(screenH/2+122+22),0);// horizontal
//		drawLine(canvas,(screenW/2+50),(screenH/2+122+32),0);// horizontal
//		drawLine(canvas,(screenW/2+87),(screenH/2+20),1);
//		drawLine(canvas,(screenW/2-50),(screenH/2+32),1);// inner


	}

	private Bitmap createDynamicGradient(String color) {
		int colors[] = new int[3];
		colors[0] = Color.parseColor(color);
		colors[1] = Color.parseColor("#eee");
		colors[2] = Color.parseColor("#eee");

		LinearGradient gradient = new LinearGradient(0, 0, 800, 1200, Color.RED, Color.TRANSPARENT, Shader.TileMode.CLAMP);
		Paint p = new Paint();
		p.setDither(true);
		p.setShader(gradient);

		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawRect(new RectF(0, 0, getWidth(), getHeight()), p);

		return bitmap;
	}

public void drawBackground(Canvas canvas){

//	int halfWidth = Width/2;
//	int halfHeight = Height/2
//	Rect dstRectForRender = new Rect( X - halfWidth, Y - halfHeight, X + halfWidth, Y + halfHeight );
//	canvas.drawBitmap ( someBitmap, null, dstRectForRender, null );
	canvas.drawBitmap(backgraaaa, v1.x/2,v1.y/2, paint);
//	canvas.drawBitmap(hole, 0f,70.0f, paint);

}

	public void simulateSmoothCircle(Canvas canvas,int radius){
		Paint p=paint;
		p.setColor(Color.parseColor("#66341B"));
//		if(radius==300){
//
////			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wooden);
////			BitmapShader  fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
////			  p.setShader(fillBMPshader);
//			  p.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
//
//			  canvas.drawCircle(screenW/2, screenH/2+30, radius, p);
//				 p.setColor(Color.parseColor("#E6CBB8"));
//				 p.setShader(null);
//		}

		if(radius==298){

			Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.greatvak);
			BitmapShader  fillBMPshader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
			  p.setShader(fillBMPshader);

			  canvas.drawCircle(screenW/2f, screenH/2f, radius, p);
				 p.setColor(Color.parseColor("#E6CBB8"));
				 p.setShader(null);
				 p.setShadowLayer(0f, 0f, 0f, Color.BLACK);
				// canvas.setBitmap(background);

				 //canvas.drawCircle(screenW/2, screenH/2+30, radius-5, p);
		}
	else{
		p.setShadowLayer(0f, 0f, 0f, Color.BLACK);
		if(radius==252){


			p.setColor(Color.parseColor("#000000"));

			 canvas.drawCircle(screenW/2f, screenH/2f, radius, p);
			 p.setColor(Color.parseColor("#E6CBB8"));
			 p.setShader(null);
			// canvas.setBitmap(background);

			 canvas.drawCircle(screenW/2f, screenH/2f, radius-5, p);
		}
		else{
		 p.setColor(Color.parseColor("#66341B"));

		 canvas.drawCircle(screenW/2f, screenH/2f, radius, p);
		 p.setColor(Color.parseColor("#E6CBB8"));
		 p.setShader(null);
		// canvas.setBitmap(background);

		 canvas.drawCircle(screenW/2f, screenH/2f, radius-5, p);
		}
		}








	}

	public void drawLine(Canvas canvas,int sx,int sy,int hv){
		 paint.setShadowLayer(0, 0, 0,Color.parseColor("#66341B"));
		 paint.setColor(Color.parseColor("#E6CBB8"));
		// paint.setColor(Color.parseColor("#000000"));
		 paint.setStrokeWidth(15.0f);

		 try{

		 if(hv==0){ canvas.drawLine(sx-20, sy,sx+20 ,sy, paint);}
		 else{canvas.drawLine(sx, sy-20,sx ,sy+20, paint);}
		 }catch(Exception e){}


	}

	////

	public void dropIntoHole(World world){

		//boolean gameOver=false;

		int gameOver=0;

		int totalDynamicBodies=0;



		for ( Body b = world.getBodyList(); b!=null; b = b.getNext() )
	    {
	      // System.out.println("x----y"+ String.valueOf( b.getPosition().x )+"-----"+String.valueOf( b.getPosition().y )  );

	        if(b.getType().equals(BodyType.DYNAMIC)){


//	        	System.out.println("-----------x-----------"+b.getPosition().x);
//                System.out.println("-----------y-----------"+b.getPosition().y);

//				if(b.getPosition().x<20 ||b.getPosition().x>50 ){
//
//
//
//                        Float vX=(-1)*b.m_linearVelocity.x;
//                        Float vY=b.m_linearVelocity.y;
//
//
//                        b.m_linearVelocity.set(vX,vY);
//
//
//				}
//
//
//
//
//                if(b.getPosition().y<20 && b.getPosition().y>50){
//
//
//
//                    Float vX=b.m_linearVelocity.x;
//                    Float vY=(-1)*b.m_linearVelocity.y;
//
//
//                    b.m_linearVelocity.set(vX,vY);
//
//
//                }




	        	//if((b.getPosition().y<=71.0f&&b.getPosition().y>=69.0f)&&( b.getPosition().x>=-1f&&b.getPosition().x<=1)){

	        // check the balls are in the hole

	        	if(inTheHole(b, 0, 70)||inTheHole(b, 0, 40)||inTheHole(b, -17, 50)||inTheHole(b, 15, 45)){
	        	//	System.out.println("-------------insideeeeeeeee------x---"+b.getPosition().x);
	        	//	System.out.println("-------------insideeeeeeeee------y---"+b.getPosition().y);
	        		 b.setLinearVelocity(new Vec2(0f,0f));
	        		 b.setTransform(new Vec2(0f,82f), 0);
	        		 SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
       	       		 int   mySound = sp.load(context,R.raw.tileclick, 1);
       	       		// sp.play(mySound, 3f, 3f, 0, 0, 1f);
       	       	sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
       	         @Override
       	         public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
       	             soundPool.play(sampleId, 10.0f, 10.0f, 0, 0, 1.0f);
       	         }
       	     });


	        	}

	        	// check the balls are in the center;

	        	if(inTheCenter(b)){


       	     gameOver++;

	        	}
//	        	 // totalDynamicBodies++;
	        }




	    }

		//System.out.println("now----"+gameOver+"--"+totalDynamicBodies);
//		if(gameOver==totalDynamicBodies){
//
//
//
//
//			Activity act=(GameActivity)context;
//			act.startActivity(new Intent(context,MainActivity.class));
//			act.finish();
//
//
//
//			//Toast.makeText(context, "now"+gameOver+"--"+totalDynamicBodies, Toast.LENGTH_SHORT).show();
//
//
//		}

	}

	public Boolean inTheHole(Body b,int x,int y){
		Boolean yes=false;
		if((b.getPosition().y<=71.0f&&b.getPosition().y>=69.0f)&&( b.getPosition().x>=-1f&&b.getPosition().x<=1)){yes=true;}
		//if((b.getPosition().y<=y+1f&&b.getPosition().y>=y-1)&&( b.getPosition().x>=x-1f&&b.getPosition().x<=x+1)){yes=true;}
		//if((b.getPosition().y<=71.0f&&b.getPosition().y>=69.0f)&&( b.getPosition().x>=-1f&&b.getPosition().x<=1)){yes=true;}
		//if((b.getPosition().y<=71.0f&&b.getPosition().y>=69.0f)&&( b.getPosition().x>=-1f&&b.getPosition().x<=1)){yes=true;}
		return yes;
	}

	public Boolean inTheCenter(Body b){
		Boolean yes=false;

//		float upperX=screenW/2+47;
//		float uppperY=screenH/2-17;
//		float lowerX=screenW/2-47;
//		float lowerY=screenH/2+77;

		float upperX=3.3f;
		float uppperY=61f;
		float lowerX=0f;
		float lowerY=55f;



		if((b.getPosition().y<=uppperY&&b.getPosition().y>=lowerY)&&( b.getPosition().x>=lowerX&&b.getPosition().x<=upperX)){

			yes=true;}
		return yes;
	}

	ContactListener cl=new ContactListener() {

		@Override
		public void beginContact(Contact arg0) {
			// TODO Auto-generated method stub

//			 String name1 = (String)arg0.shape1.getBody().getUserData();
//			 String name2 = (String)arg0.shape2.getBody().getUserData();
			String name1 = (String)arg0.getFixtureA().getBody().getUserData();
			 String name2 = (String)arg0.getFixtureB().getBody().getUserData();;

//			 if(name1.equals("static")){
//				// System.out.println("Contact detected between------------------------------- "+ name1 + " and "+ name2);
//				// arg0.getFixtureB().getBody().getPosition().set(0f, 80.0f);
//				// world.destroyBody(arg0.getFixtureB().getBody());
//				// _update();
//				 Body body=arg0.getFixtureB().getBody();
//				 body.setTransform(new Vec2(0f,26f), 0);
//				 _update();
//
//			 }else if(name2.equals("static")){
//				// System.out.println("Contact detected between------------------------------- "+ name1 + " and "+ name2);
//
//				// arg0.getFixtureA().getBody().getPosition().set(0f, 80.0f);
//				// world.destroyBody(arg0.getFixtureA().getBody());
//				// _update();
//				//
//				 Body body=arg0.getFixtureA().getBody();
//				 body.setTransform(new Vec2(0f,26f), 0);
//				 _update();
//			 }



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
