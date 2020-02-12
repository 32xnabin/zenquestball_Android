package dawn.zenquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;

public class PaintHelper {
	
	
	public Paint getDonut(Paint paint){
		
		int	mColors[]=new int[]{0xFFFF0000,0xFFFF00FF,0xFF0000FF,0xFF00FFFF,0xFF00FF00,0xFF000000,0xFFFFFFFF,0xFFFFFF00,0xFFFF0000};
		  Shader s=new SweepGradient(0,0,mColors,null);
		//  paint=new Paint(Paint.ANTI_ALIAS_FLAG);
		  paint.setShader(s);
		  paint.setStyle(Paint.Style.STROKE);
		  paint.setStrokeWidth(32);
		
		return paint;
	}
	
	public Paint getGradient(Paint paint,float x,float y){
		
		 
           
             paint.setColor(Color.BLACK);
             paint.setStrokeWidth(x);
             paint.setStyle(Paint.Style.FILL_AND_STROKE);
             paint.setShader(new RadialGradient(x / 2, y / 2,
                     y/ 3, Color.TRANSPARENT, Color.BLACK, TileMode.MIRROR));
         
		
		return paint;
	}
	
	public Paint getGradient1(Paint paint,float x,float y,float radius){
		
		
	//	RadialGradient rg=new RadialGradient(100, 50, radius, Color.GREEN, Color.BLACK, TileMode.MIRROR);// green black
		RadialGradient rg=new RadialGradient(100, 50, radius, Color.parseColor("#329419"), Color.parseColor("#41C121"), TileMode.MIRROR);
		paint.setShader(rg);
		  return paint;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
