package dawn.zenquest;


import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.MotionEvent;


/**
 * DebugDraw ʵ����
 * ����BOX2D�ڲ���ͼ
 *
 * @author JianbinZhu
 *
 */
public class Box2dDebugDraw extends DebugDraw {
	private OBBViewportTransform transform;
	private Paint paint;
	private Canvas canvas;
	private Path path;
	String timer="";
	@SuppressWarnings("unused")
	private Box2dSurfaceView testbedView;
	Bitmap bmp;
	Bitmap bmp1;
	public Box2dDebugDraw(Box2dSurfaceView testbedView,POJO pojo) {
		super(new OBBViewportTransform());
		this.bmp=pojo.getBackground();
		this.bmp1=pojo.getBall();
		this.testbedView = testbedView;

		path = new Path();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		transform = (OBBViewportTransform) viewportTransform;


		scale = 10;
		camera_x = -(Box2dSurfaceView.screenW/2) / scale;
		camera_y = (Box2dSurfaceView.screenH - 10) / scale;

		//
		setCamera();

	}

	private float camera_x, camera_y, scale;
	public void setCamera() {
		transform.setYFlip(true);

		transform.setCamera(camera_x, camera_y, scale);
	}

	public void setCamera(float x, float y) {
		camera_x = x;
		camera_y = y;
		setCamera();
	}

	public void setScale(float scale) {
		this.scale = scale;
		setCamera();
	}

	/**
	 * �ƶ��ӽ�
	 */
	private float down_x, down_y;
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();

//		switch(action){
//			case MotionEvent.ACTION_DOWN:{
//				System.out.println("ACTION_DOWN");
//				down_x = x;
//				down_y = y;
//				break;
//			}
//
//			case MotionEvent.ACTION_MOVE:{
//				System.out.println("ACTION_MOVE");
//				camera_x -= (x-down_x)/TestbedView.RATE;
//				camera_y += (y-down_y)/TestbedView.RATE;
//				down_x = x;
//				down_y = y;
//				setCamera();
//				break;
//			}
//
//			//case MotionEvent.
//		}

		return true;
	}

	/**
	 * ���û�����ɫ
	 *
	 * @param color
	 */
	public void setColor(Color3f color) {
		int r = (int) (0xff * color.x);
		int g = (int) (0xff * color.y);
		int b = (int) (0xff * color.z);

		paint.setColor(Color.rgb(r, g, b));
	}

	public void setBitmap(Color3f color) {
		int r = (int) (0xff * color.x);
		int g = (int) (0xff * color.y);
		int b = (int) (0xff * color.z);

		paint.setColor(Color.rgb(r, g, b));
	}

	private Rect r_text = new Rect();
	public void draw(Canvas canvas) {
		this.canvas = canvas;

		String text = "cx="+camera_x + " cy=" + camera_y;
		paint.getTextBounds(text, 0, text.length(), r_text);
		paint.setColor(0xff00ff00);


		try{

			canvas.drawText(text, Box2dSurfaceView.screenW-r_text.width(), 3+r_text.height(), paint);
		}catch(Exception e){}
	}

	private final Vec2 sp1 = new Vec2();
	private final Vec2 sp2 = new Vec2();

	@Override
	public void drawPoint(Vec2 argPoint, float argRadiusOnScreen, Color3f argColor) {
		// TODO Auto-generated method stub
		getWorldToScreenToOut(argPoint, sp1);

		setColor(argColor);
		paint.setStyle(Style.STROKE);
		canvas.drawCircle(sp1.x, sp1.y, argRadiusOnScreen, paint);
	}

	/**
	 * ��ʵ�Ķ����
	 */
	private final Vec2 temp = new Vec2();
	@Override
	public void drawSolidPolygon(Vec2[] vertices, int vertexCount, Color3f color) {
		// TODO Auto-generated method stub
		setColor(Color3f.RED);
		paint.setStyle(Style.FILL);

		path.reset();
		for (int i = 0; i < vertexCount; i++) {
			getWorldToScreenToOut(vertices[i], temp);

			if (i == 0)
				path.moveTo(temp.x, temp.y);
			else
				path.lineTo(temp.x, temp.y);
		}
		path.close();
//		BitmapShader fillBMPshader = new BitmapShader(bmp, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//		paint.setShader(fillBMPshader);
		//paint.setShadowLayer(3, 3, 3, Color.RED);



//        paint.setDither(true);
//        paint.setColor(0xFFFFFF00);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setAntiAlias(true);
//        paint.setStrokeWidth(3);

		//Initialize the bitmap object by loading an image from the resources folder
//       Bitmap fillBMP = bmp;
//        //Initialize the BitmapShader with the Bitmap object and set the texture tile mode
//       BitmapShader fillBMPshader = new BitmapShader(fillBMP, TileMode.REPEAT, Shader.TileMode.REPEAT);
//
//        //Initialize the fillPaint object
//
//       paint.setShader(fillBMPshader);
		// canvas.setBitmap(bmp);
		//	paint.setShader(new LinearGradient(0, 0, 0, 5, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));
		//paint.setShader(new LinearGradient(1, 1, 1, 3, Color.BLACK, Color.WHITE, Shader.TileMode.CLAMP));
		Paint p=new Paint();
		p.setShader(new LinearGradient(5, 0, 0, 5, Color.parseColor("#66341B"),Color.parseColor("#66341B"), Shader.TileMode.REPEAT));
		//p.setShader(new LinearGradient(5, 0, 0, 5, Color.TRANSPARENT,Color.TRANSPARENT, Shader.TileMode.REPEAT));
		//p.setShadowLayer(0.3f, 0, 0,Color.parseColor("#66341B"));
		//p.setShadowLayer(4.5f, 0.0f, 7.0f, Color.parseColor("#000000"));

		// Important for certain APIs
		//  setLayerType(Paint.LAYER_TYPE_SOFTWARE, p);
		//canvas.drawCircle(sp1.x, sp1.y, radius*scale, p);

		canvas.drawPath(path, p);

	}

	/**
	 * ������Բ
	 */
	@Override
	public void drawCircle(Vec2 center, float radius, Color3f color) {
		// TODO Auto-generated method stub
		getWorldToScreenToOut(center, sp1);

		setColor(color);
		paint.setStyle(Style.FILL);
		//paint.setShader(new LinearGradient(0, 0, 0, 5, Color.WHITE, Color.RED, Shader.TileMode.MIRROR));
		canvas.drawCircle(sp1.x, sp1.y, radius*scale, paint);
		//BitmapHelper helper=new BitmapHelper();

		//canvas.drawBitmap(rounded, sp1.x, sp1.y, paint);
	}

	/**
	 * ��ʵ��Բ
	 */
	@Override
	public void drawSolidCircle(Vec2 center, float radius, Vec2 axis, Color3f color) {
		// TODO Auto-generated method stub
		getWorldToScreenToOut(center, sp1);
		Color3f cc=new Color3f();
		cc.x=0.3f;
		cc.y=0.3f;
		cc.z=0.3f;
		//setColor(Color3f.RED);
		//paint.setStyle(Style.FILL);
//		paint.setStyle(Style.FILL);
		BitmapHelper helper=new BitmapHelper();
		Bitmap rounded=helper.getResizedBitmap(bmp,(int)(radius*scale)*2,(int)(radius*scale)*2);
		Bitmap hole=helper.getResizedBitmap(bmp1,(int)(radius*scale)*2,(int)(radius*scale)*2);
		rounded=helper.getRoundedCornerBitmap(rounded,(int)(radius*scale));
		//System.out.println("----------------C---radius------"+radius);
//		System.out.println("----------------C------axis---"+axis);
//		System.out.println("----------------C------center---"+center);
		if(radius==2.1f){
////		PaintHelper helper=new PaintHelper();
////		paint=helper.getGradient1(paint,sp1.x, sp1.y,radius*scale);
			Paint p=new Paint();
			p.setShader(new LinearGradient(0, 0, 0, 5, Color.RED, Color.GRAY, Shader.TileMode.MIRROR));
			//canvas.drawCircle(sp1.x, sp1.y, radius*scale, p);
			canvas.drawBitmap(rounded, sp1.x-(radius*scale), sp1.y-(radius*scale), paint);
		}
		else if(radius==2.3f){
////		PaintHelper helper=new PaintHelper();
////		paint=helper.getGradient1(paint,sp1.x, sp1.y,radius*scale);
			Paint p=new Paint();
			p.setShader(new LinearGradient(0, 0, 0, 5, Color.RED, Color.GRAY, Shader.TileMode.MIRROR));
			//canvas.drawCircle(sp1.x, sp1.y, radius*scale, p);
			canvas.drawBitmap(hole, sp1.x-(radius*scale), sp1.y-(radius*scale), paint);
		}
		else{
			Paint p=paint;
			p.setShader(new LinearGradient(0, 0, 0, 5, Color.GRAY, Color.GRAY, Shader.TileMode.MIRROR));
			canvas.drawCircle(sp1.x, sp1.y, radius*scale, p);
		}



	}

	/**
	 * ���߶�
	 */
	@Override
	public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {
		// TODO Auto-generated method stub
		getWorldToScreenToOut(p1, sp1);
		getWorldToScreenToOut(p2, sp2);

		setColor(color);
		paint.setStyle(Style.STROKE);
		canvas.drawLine(sp1.x, sp1.y, sp2.x, sp2.y, paint);
	}

	private final Vec2 temp2 = new Vec2();
	@Override
	// will show the center of objects
	public void drawTransform(Transform xf) {
		// TODO Auto-generated method stub
//		getWorldToScreenToOut(xf.position, temp);
//		temp2.setZero();
//		float k_axisScale = 0.4f;
//		temp2.x = xf.position.x + k_axisScale * xf.R.col1.x;
//		temp2.y = xf.position.y + k_axisScale * xf.R.col1.y;
//		getWorldToScreenToOut(temp2, temp2);
//
//		paint.setColor(Color.rgb(0,0,0));
//		canvas.drawLine(temp.x, temp.y, temp2.x, temp2.y, paint);
//
//		temp2.x = xf.position.x + k_axisScale * xf.R.col2.x;
//		temp2.y = xf.position.y + k_axisScale * xf.R.col2.y;
//		getWorldToScreenToOut(temp2, temp2);
//
//		paint.setColor(Color.rgb(0,0,0));
//		canvas.drawLine(temp.x, temp.y, temp2.x, temp2.y, paint);
	}

	/**
	 * ���ַ�
	 */
	@Override
	public void drawString(float x, float y, String s, Color3f color) {
		// TODO Auto-generated method stub
		setColor(color);
		canvas.drawText(s, x, y, paint);
	}




}
