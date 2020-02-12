package dawn.zenquest;



import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;


public class Box2dView extends Box2dSurfaceView {

	int level = 1;
	int mWidthPixels;
	int mHeightPixels;

	public Box2dView(Context context, Bitmap ball, POJO pojo, int level) {
		super(context, ball, pojo, level);
		this.level = level;
		// TODO Auto-generated constructor stub
		getRealScreenDimens();
		setRealDeviceSizeInPixels();;
	}


	public void getRealScreenDimens(){

		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		int realWidth;
		int realHeight;

		if (Build.VERSION.SDK_INT >= 17){
			//new pleasant way to get real metrics
			DisplayMetrics realMetrics = new DisplayMetrics();
			display.getRealMetrics(realMetrics);
			realWidth = realMetrics.widthPixels;
			realHeight = realMetrics.heightPixels;

		} else if (Build.VERSION.SDK_INT >= 14) {
			//reflection for this weird in-between time
			try {
				Method mGetRawH = Display.class.getMethod("getRawHeight");
				Method mGetRawW = Display.class.getMethod("getRawWidth");
				realWidth = (Integer) mGetRawW.invoke(display);
				realHeight = (Integer) mGetRawH.invoke(display);
			} catch (Exception e) {
				//this may not be 100% accurate, but it's all we've got
				realWidth = display.getWidth();
				realHeight = display.getHeight();
				//Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
			}

		} else {
			//This should be close, as lower API devices should not have window navigation bars
			realWidth = display.getWidth();
			realHeight = display.getHeight();
		}


		//	System.out.println("-width---------wwwwww------->>>>>>>>>>>"+realWidth);
		//	System.out.println("-width----------hhhhhh--------->>>>>>>>>>>"+realHeight);
	}

	private void setRealDeviceSizeInPixels() {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);


		// since SDK_INT = 1;
		mWidthPixels = displayMetrics.widthPixels;
		mHeightPixels = displayMetrics.heightPixels;

		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
			try {
				mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
				mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
			} catch (Exception ignored) {
			}
		}

		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17) {
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
				mWidthPixels = realSize.x;
				mHeightPixels = realSize.y;
			} catch (Exception ignored) {
			}
		}

		mWidthPixels = 300;
		mHeightPixels = 300;
		//System.out.println("-width----------KKKKKKKKKKKKKKKKK--actualllllll--------->>>>>>>>>>>"+mWidthPixels);
		//System.out.println("-width----------KKKKKKKKKKKKKKKKK--actualllllll--------->>>>>>>>>>>"+mHeightPixels);

	}

	@Override
	public void initTest() {

		//createFourWalls();

		//boxWala();



		createRings();
		createBalls();
		createHoles();

	}


	public void boxWala(){



		BodyDef bodyDef = new BodyDef();

		bodyDef.position = new Vec2(-35.0f,0.0f);
		bodyDef.angle = 0.0f;

		bodyDef.fixedRotation = false;
		bodyDef.active = true;
		bodyDef.bullet = false;
		bodyDef.allowSleep = true;
		bodyDef.inertiaScale = 1.0f;
		bodyDef.linearDamping = 0.0f;
		bodyDef.angularDamping = 0.0f;
		bodyDef.userData = "wall";
		bodyDef.type = BodyType.KINEMATIC;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.5f, 500f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.userData = null;
		fixtureDef.friction = 0.35f;
		fixtureDef.restitution = 0.05f;
		fixtureDef.density = 0.75f;
		fixtureDef.isSensor = false;

		Body body = getWorld().createBody(bodyDef);
		body.createFixture(fixtureDef);



		BodyDef bodyDef1 = new BodyDef();

		bodyDef1.position = new Vec2(0.0f,0.0f);
		bodyDef1.angle = 0.0f;

		bodyDef1.fixedRotation = false;
		bodyDef1.active = true;
		bodyDef1.bullet = false;
		bodyDef1.allowSleep = true;
		bodyDef1.inertiaScale = 1.0f;
		bodyDef1.linearDamping = 0.0f;
		bodyDef1.angularDamping = 0.0f;
		bodyDef1.userData = "wall";
		bodyDef1.type = BodyType.KINEMATIC;

		PolygonShape shape1 = new PolygonShape();
		shape1.setAsBox(500f, 0.5f);

		FixtureDef fixtureDef1 = new FixtureDef();
		fixtureDef1.shape = shape1;
		fixtureDef1.userData = null;
		fixtureDef1.friction = 0.35f;
		fixtureDef1.restitution = 0.05f;
		fixtureDef1.density = 0.75f;
		fixtureDef1.isSensor = false;

		Body body1 = getWorld().createBody(bodyDef1);
		body1.createFixture(fixtureDef1);




		BodyDef bodyDef3 = new BodyDef();

		bodyDef3.position = new Vec2(35.0f,0.0f);
		bodyDef3.angle = 0.0f;

		bodyDef3.fixedRotation = false;
		bodyDef3.active = true;
		bodyDef3.bullet = false;
		bodyDef3.allowSleep = true;
		bodyDef3.inertiaScale = 1.0f;
		bodyDef3.linearDamping = 0.0f;
		bodyDef3.angularDamping = 0.0f;
		bodyDef3.userData = "wall";
		bodyDef3.type = BodyType.KINEMATIC;

		PolygonShape shape3 = new PolygonShape();
		shape3.setAsBox(0.5f, 500f);

		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.shape = shape3;
		fixtureDef3.userData = null;
		fixtureDef3.friction = 0.35f;
		fixtureDef3.restitution = 0.05f;
		fixtureDef3.density = 0.75f;
		fixtureDef.isSensor = false;

		Body body3 = getWorld().createBody(bodyDef3);
		body3.createFixture(fixtureDef3);




		BodyDef bodyDef4 = new BodyDef();

		bodyDef4.position = new Vec2(0.0f,120.0f);
		bodyDef4.angle = 0.0f;

		bodyDef4.fixedRotation = false;
		bodyDef4.active = true;
		bodyDef4.bullet = false;
		bodyDef4.allowSleep = true;
		bodyDef4.inertiaScale = 1.0f;
		bodyDef4.linearDamping = 0.0f;
		bodyDef4.angularDamping = 0.0f;
		bodyDef4.userData = "wall";
		bodyDef4.type = BodyType.KINEMATIC;

		PolygonShape shape4 = new PolygonShape();
		shape4.setAsBox(500f, 0.5f);

		FixtureDef fixtureDef4 = new FixtureDef();
		fixtureDef4.shape = shape1;
		fixtureDef4.userData = null;
		fixtureDef4.friction = 0.35f;
		fixtureDef4.restitution = 0.05f;
		fixtureDef4.density = 0.75f;
		fixtureDef4.isSensor = false;

		Body body4 = getWorld().createBody(bodyDef4);
		body4.createFixture(fixtureDef4);
	}


	public  void createFourWalls(){


		System.out.println("---x-----"+mWidthPixels+"---------y---"+mHeightPixels);

		BodyDef bodyDef = new BodyDef();

		bodyDef.position = new Vec2(30f, 0f);
		bodyDef.angle = 0.0f;
		bodyDef.linearVelocity = new Vec2(0.0f,0.0f);
		bodyDef.angularVelocity = 0.0f;
		bodyDef.fixedRotation = false;
		bodyDef.active = true;
		bodyDef.bullet = false;
		bodyDef.allowSleep = true;
		bodyDef.inertiaScale = 1.0f;
		bodyDef.linearDamping = 0.0f;
		bodyDef.angularDamping = 0.0f;
		bodyDef.userData = "wall";
		bodyDef.type = BodyType.KINEMATIC;

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.05f,500);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.userData = null;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.05f;
		fixtureDef.density = 1.0f;
		fixtureDef.isSensor = false;

		Body body = getWorld().createBody(bodyDef);
		body.createFixture(fixtureDef);







		BodyDef bodyDef1 = new BodyDef();

		bodyDef1.position = new Vec2(-20f, 0f);
		bodyDef1.angle = 0.0f;
		bodyDef1.linearVelocity = new Vec2(0.0f,0.0f);
		bodyDef1.angularVelocity = 0.0f;
		bodyDef1.fixedRotation = false;
		bodyDef1.active = true;
		bodyDef1.bullet = false;
		bodyDef1.allowSleep = true;
		bodyDef1.inertiaScale = 1.0f;
		bodyDef1.linearDamping = 0.0f;
		bodyDef1.angularDamping = 0.0f;
		bodyDef1.userData = "wall";
		bodyDef1.type = BodyType.KINEMATIC;

		PolygonShape shape1 = new PolygonShape();
		shape1.setAsBox(0.05f,500f);

		FixtureDef fixtureDef1 = new FixtureDef();
		fixtureDef1.shape = shape;
		fixtureDef1.userData = null;
		fixtureDef1.friction = 0.5f;
		fixtureDef1.restitution = 0.05f;
		fixtureDef1.density = 1.0f;
		fixtureDef1.isSensor = false;

		Body body1 = getWorld().createBody(bodyDef1);
		body1.createFixture(fixtureDef1);





		BodyDef bodyDef3 = new BodyDef();

		bodyDef3.position = new Vec2(0f, 0f);
		bodyDef3.angle = 0.0f;
		bodyDef3.linearVelocity = new Vec2(0.0f,0.0f);
		bodyDef3.angularVelocity = 0.0f;
		bodyDef3.fixedRotation = false;
		bodyDef3.active = true;
		bodyDef3.bullet = false;
		bodyDef3.allowSleep = true;
		bodyDef3.inertiaScale = 1.0f;
		bodyDef3.linearDamping = 0.0f;
		bodyDef3.angularDamping = 0.0f;
		bodyDef3.userData = "wall";
		bodyDef3.type = BodyType.KINEMATIC;

		PolygonShape shape3 = new PolygonShape();
		shape3.setAsBox(500f,0.05f);

		FixtureDef fixtureDef3 = new FixtureDef();
		fixtureDef3.shape = shape3;
		fixtureDef3.userData = null;
		fixtureDef3.friction = 0.5f;
		fixtureDef3.restitution = 0.05f;
		fixtureDef3.density = 1.0f;
		fixtureDef3.isSensor = false;

		Body body3 = getWorld().createBody(bodyDef3);
		body3.createFixture(fixtureDef3);



		BodyDef bodyDef4 = new BodyDef();
		bodyDef4.position = new Vec2(0.0f, 100.0f);
		bodyDef4.angle = 0.0f;
		bodyDef4.linearVelocity = new Vec2(0.0f,0.0f);
		bodyDef4.angularVelocity = 0.0f;
		bodyDef4.fixedRotation = false;
		bodyDef4.active = true;
		bodyDef4.bullet = false;
		bodyDef4.allowSleep = true;
		bodyDef4.inertiaScale = 1.0f;
		bodyDef4.linearDamping = 0.0f;
		bodyDef4.angularDamping = 0.0f;
		bodyDef4.userData = "wall";
		bodyDef4.type = BodyType.KINEMATIC;
		PolygonShape shape4 = new PolygonShape();
		shape4.setAsBox(500f,0.05f);

		FixtureDef fixtureDef4 = new FixtureDef();
		fixtureDef4.shape = shape4;
		fixtureDef4.userData = null;
		fixtureDef4.friction = 0.5f;
		fixtureDef4.restitution = 0.05f;
		fixtureDef4.density = 1.0f;
		fixtureDef4.isSensor = false;
		Body body4 = getWorld().createBody(bodyDef4);
		body4.createFixture(fixtureDef4);




	}

	public void createRings() {

		PolygonShape shape = new PolygonShape();

		generateCircle(160, 4, 25, shape);
		generateCircle(150, 4, 25, shape);
		generateCircle(128, 4, 25, shape);
		generateCircle(103, 4, 25, shape);
		generateCircle(78, 4, 25, shape);
		generateCircle(54, 4, 25, shape);
		generateCircle(30, 4, 15, shape);

	}

	public void createBalls() {

		CircleShape shape1 = new CircleShape();

		shape1.m_radius = 1.6f;

		FixtureDef fd = new FixtureDef();
		fd.shape = shape1;
		fd.density = 4.0f;


		float restitution[] = { 0.2f, 0.2f, 0.2f, 0.2f };

		int ballNo = 4;
		switch (level) {

			case 1:
				ballNo = 2;
				break;
			case 2:
				ballNo = 2;
				break;
			case 3:
				ballNo = 3;
				break;
			case 4:
				ballNo = 3;
				break;
			case 5:
				ballNo = 3;
				break;

		}

		for (int i = 0; i < ballNo; ++i) {
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DYNAMIC;
			bd.linearDamping = 0.8f;
			bd.angularDamping = 0.01f + (float) (Math.random() * 0.02f);

			// bd.linearDamping = 0.0f;
			// bd.angularDamping = 0.0f;

			float xVel = (float) (Math.random() * 10 + 10);
			float yVel = (float) (Math.random() * 10 + 10);
			if (Math.random() > 0.5) {
				xVel *= -1;
			}
			if (Math.random() > 0.5) {
				yVel *= -1;
			}

			bd.linearVelocity = new Vec2(xVel, yVel);
			bd.angularVelocity = (float) Math.PI / (60 * 15);
			bd.position.set(0f, 82.0f);
			bd.userData = 1 + "body";

			// body.createFixture(fd);

			Body body = getWorld().createBody(bd);

			// fd.restitution = restitution[i];
			fd.restitution = restitution[0];
			body.createFixture(fd);
		}

	}

	public void createHoles() {

//		switch (level) {
//
//		case 1:
//			createHolesAt(-17f, 50.0f);// 1
//			createHolesAt(15f, 45.0f);// 2
//			break;
//		case 2:
//			createHolesAt(-17f, 50.0f);// 1
//			createHolesAt(15f, 45.0f);// 2
//			createHolesAt(0f, 70.0f);// 3
//			break;
//		case 3:
//			createHolesAt(-17f, 50.0f);// 1
//			createHolesAt(15f, 45.0f);// 2
//			createHolesAt(0f, 70.0f);// 3
//			createHolesAt(0f, 40.0f);// 4
//			break;
//		case 4:
//			createHolesAt(-17f, 50.0f);// 1
//			createHolesAt(15f, 45.0f);// 2
//			createHolesAt(0f, 70.0f);// 3
//			createHolesAt(0f, 40.0f);// 4
//			createHolesAt(-11f, 55.0f);//5
//			break;
//		case 5:
//			createHolesAt(-17f, 50.0f);// 1
//			createHolesAt(15f, 45.0f);// 2
//			createHolesAt(0f, 70.0f);// 3
//			createHolesAt(0f, 40.0f);// 4
//			createHolesAt(-11f, 55.0f);//5
//			break;
//		 default:
//			 createHolesAt(-17f, 50.0f);// 1
//				createHolesAt(15f, 45.0f);// 2
//				createHolesAt(0f, 70.0f);// 3
//				createHolesAt(0f, 40.0f);// 4
//				createHolesAt(-11f, 55.0f);//5
//
//
//		}

		createHolesAt(-19f, 70.0f);// 1
		createHolesAt(-14f, 90.0f);// 5
//		createHolesAt(0f, 70.0f);// 3
//		createHolesAt(0f, 40.0f);// 4
//		createHolesAt(15f, 45.0f);// 2

	}

	public void createHolesAt(float x, float y) {

		CircleShape shape2 = new CircleShape();
		shape2.m_radius = 2.3f;

		FixtureDef fd1 = new FixtureDef();
		fd1.shape = shape2;
		fd1.isSensor = true;

		BodyDef bd = new BodyDef();
		bd.type = BodyType.STATIC;

		bd.position.set(x, y);
		// bd.
		bd.userData = "static";

		Body body = getWorld().createBody(bd);

		body.createFixture(fd1);
		// creating another hole
		BodyDef bd1 = new BodyDef();
		bd1.type = BodyType.STATIC;

		bd1.position.set(0f, 40.0f);
		// bd.
		bd1.userData = "static";

		Body body1 = getWorld().createBody(bd1);

		body1.createFixture(fd1);
	}

	public void generateCircle(int rolyRadius, int scale, int pices,
							   PolygonShape shape) {

		//System.out.println("---------hhhhhhhhh-----------" + screenW);
		//	System.out.println("----------hhhhhhhhh----------" + screenH);

		//Vec2 rolyCenter = new Vec2(0.00f, 57.6f);//mWidthPixels

		//Vec2 rolyCenter = new Vec2(0.00f, (screenW/scale)/2f);
		Vec2 rolyCenter = new Vec2(0.00f, (screenW/6)/2f);

		//	System.out.println("---------xxxxxx-----------" + rolyCenter.x);
		//	System.out.println("----------yyyy----------" + rolyCenter.y);


		float centerAngle = (float) (2 * Math.PI / pices);
		float rolySide = (float) (rolyRadius * Math.tan(centerAngle / 2) / scale);
		rolyCenter.mul(1 / scale);

		for (int i = 0; i < pices; i++) {

			float angle = (float) (2 * Math.PI / pices * i);

			shape.setAsBox((float) 0.1, rolySide, new Vec2((float) (rolyRadius
					/ scale * Math.cos(angle)),
					(float) (rolyRadius / scale * Math.sin(angle))), angle);

			BodyDef bd1 = new BodyDef();
			bd1.position.set(rolyCenter);
			bd1.userData = "wall";

			// bd1.userData=null;

			if (rolyRadius == 150) {

				// if(i!=pices/2){
				Body ground = getWorld().createBody(bd1);
				ground.createFixture(shape, 0.0f);
				// }
			}

			if (rolyRadius == 128) {

				if (i != pices / 2) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 103) {

				if (i != 0) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 78) {

				if (i != 20&&i!=21) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 54) {

				if (i != 0 && i != 1) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 30) {

				if (i != pices / 2 && i != (pices / 2 + 1)) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			}
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RoundMaizeBall";
	}

}
