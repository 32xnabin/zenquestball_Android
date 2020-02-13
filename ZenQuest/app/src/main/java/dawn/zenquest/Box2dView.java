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
import android.widget.TextView;

import java.lang.reflect.Method;


public class Box2dView extends Box2dSurfaceView {

	int level = 1;
	int mWidthPixels;
	int mHeightPixels;
	TextView message;

	public Box2dView(Context context, Bitmap ball, POJO pojo, int level,TextView message) {
		super(context, ball, pojo, level,message);
		this.level = level;
		this.message=message;
		//getRealScreenDimens();
		//setRealDeviceSizeInPixels();;
	}




	@Override
	public void initTest() {

		//createFourWalls();

		//boxWala();



		createRings();
		createBalls();
		createHoles();

	}




	public void createRings() {

		PolygonShape shape = new PolygonShape();

		generateCircle(160, 4, 55, shape);
		generateCircle(150, 4, 55, shape);
		generateCircle(128, 4, 55, shape);
		generateCircle(103, 4, 55, shape);
		generateCircle(78, 4, 55, shape);
		generateCircle(54, 4, 55, shape);
		generateCircle(30, 4, 25, shape);

	}

	public void createBalls() {

		CircleShape shape1 = new CircleShape();

		shape1.m_radius = 2.1f;

		FixtureDef fd = new FixtureDef();
		fd.shape = shape1;
		fd.density = 2.0f;


		float restitution[] = { 0.2f, 0.2f, 0.2f, 0.2f };

		int ballNo = 1;
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
		ballNo=2;

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
			bd.position.set(0f+i, 75.0f);
			bd.userData = 1 + "body";

			// body.createFixture(fd);

			Body body = getWorld().createBody(bd);

			// fd.restitution = restitution[i];
			fd.restitution = restitution[0];
			body.createFixture(fd);
		}

	}

	public void createHoles() {



		float yMid=(screenW/5)/2f;

		createHolesAt(-15f, yMid-4);// 2
		createHolesAt(-14f, yMid+10);// 2u
		createHolesAt(0f, yMid-12);// 1
		createHolesAt(0f, yMid+22);//3
		createHolesAt(22f, yMid+6);// 3l

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

		//bd1.position.set(0f, 40.0f);
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
		Vec2 rolyCenter = new Vec2(0.00f, (screenW/5)/2f);

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

//			Body ground = getWorld().createBody(bd1);
//			ground.createFixture(shape, 0.0f);

			if (rolyRadius == 150) {

				// if(i!=pices/2){
				Body ground = getWorld().createBody(bd1);
				ground.createFixture(shape, 0.0f);
				// }
			}

			if (rolyRadius == 128) {

				if (i != 32&&i != 33) {
					//	System.out.println("===>"+i);
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 103) {

				if (i != 52&&i!=53) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 78) {

				if (i != 17&&i!=18&&i!=19) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 54) {

				if (i != 0 && i != 1&& i != 2) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
			} else if (rolyRadius == 30) {

				if (i != 12 && i != 13&& i != 14) {
					Body ground = getWorld().createBody(bd1);
					ground.createFixture(shape, 0.0f);
				}
				else{
					//System.out.println("===>"+i);
				}
			}
		}
	}

	@Override
	public String getName() {

		return "RoundMaizeBall";
	}

}
