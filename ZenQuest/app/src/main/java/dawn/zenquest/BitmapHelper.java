package dawn.zenquest;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Base64;

public class BitmapHelper {
	
	public  Bitmap getRoundedRectBitmap1(Bitmap bitmap, int pixels) {
		Bitmap result = null;
		try {
		result = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
		Config.ARGB_8888);
		Canvas canvas = new Canvas(result);

		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		int roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		} catch (NullPointerException e) {
		// return bitmap;
		} catch (OutOfMemoryError o){}
		return result;
		}
	
	
	
	public  Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
	    Bitmap result = null;
	    try {
	        result = Bitmap.createBitmap(200, 200, Config.ARGB_8888);
	        Canvas canvas = new Canvas(result);

	        int color = 0xff424242;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, 200, 200);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawCircle(50, 50, 50, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	    } catch (NullPointerException e) {
	    } catch (OutOfMemoryError o) {
	    }
	    return result;
	}
	
	@SuppressLint("NewApi")
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int radius) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = radius;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		// output = Bitmap.createScaledBitmap(output, 400,400, true);
		// output=output.getScaledWidth("");
		return output;
	}

	@SuppressLint("NewApi")
	public static Bitmap getRoundedCornerBitmapWithBorder(Bitmap bitmap,int border) {

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		//if(w>50 || h>50){w=50;h=50;}

		int a, b;

		System.out.println("-------" + bitmap.getByteCount() + "--" + h + "---"+ w);
				

		int radius = Math.min(h / 2, w / 2);
		a = Math.max(w, h);
		Bitmap output = Bitmap.createBitmap(a, a, Config.ARGB_8888);

		Paint p = new Paint();
		p.setAntiAlias(true);

		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		p.setStyle(Style.FILL);

		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
       
		c.drawBitmap(bitmap, 4, 4, p);
		p.setXfermode(null);
		p.setStyle(Style.STROKE);
		p.setColor(Color.parseColor("#61445C"));
		p.setStrokeWidth(border);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
		
		output = Bitmap.createScaledBitmap(output,150, 150, true);
		return output;

	}
	
	
	
	@SuppressLint("NewApi")
	public static Bitmap getRoundedCornerBitmapWithBorderSmall(Bitmap bitmap,int border) {

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		
		//if(w>50 || h>50){w=50;h=50;}

		int a, b;

		//System.out.println("-------" + bitmap.getByteCount() + "--" + h + "---"+ w);
				

		int radius = Math.min(h / 2, w / 2);
		a = Math.max(w, h);
		Bitmap output = Bitmap.createBitmap(a, a, Config.ARGB_8888);

		Paint p = new Paint();
		p.setAntiAlias(true);

		Canvas c = new Canvas(output);
		c.drawARGB(0, 0, 0, 0);
		p.setStyle(Style.FILL);

		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

		p.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		c.drawBitmap(bitmap, 4, 4, p);
		p.setXfermode(null);
		p.setStyle(Style.STROKE);
		p.setColor(Color.parseColor("#61445C"));
		p.setStrokeWidth(border);
		c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);
		// output = Bitmap.createScaledBitmap(output, 200, 200, true);
		//output=getResizedBitmap(output, 200,200) ;
		//Bitmap yourBitmap;
		 output = Bitmap.createScaledBitmap(output,100,100, true);
		return output;

	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	public static Bitmap getBitmapFromUrl(URL myUrl) {

		Bitmap myBitmap = null;
		try {
			myBitmap = BitmapFactory.decodeStream(myUrl.openConnection()
					.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myBitmap;
	}
	


public static Bitmap getBitmapFromHttpURL(String src) {
	Bitmap bitmap=null;
	AsyncTask<String,Void, Bitmap> ac=new AsyncTask<String,Void, Bitmap>() {

		@Override
		protected Bitmap doInBackground(String... params) {
			 try {
			        URL url = new URL(params[0]);
			        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			        connection.setDoInput(true);
			        connection.connect();
			        InputStream input = connection.getInputStream();
			        Bitmap myBitmap = BitmapFactory.decodeStream(input);
			        myBitmap = Bitmap.createScaledBitmap(myBitmap,200,200, true);
			        
			        return myBitmap;
			    } catch (IOException e) {
			        e.printStackTrace();
			        return null;
			    }
			
		}
	};
	try {
		bitmap=ac.execute(src).get();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	//bitmap = Bitmap.createScaledBitmap(bitmap,200,200, true);
		
	return bitmap;
   
}



	public Bitmap getBitmapFromStringBase64(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			return bitmap;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		 // RECREATE THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
		matrix, false);
		return resizedBitmap;
		 }

}
