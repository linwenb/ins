package hk.ust.ins.op;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class Display{

    public static void display(double newX, double newY, double oldX, double oldY, Bitmap bitmap){
    	Paint paint = new Paint(Paint.DITHER_FLAG);;
    	Canvas canvas = new Canvas(bitmap);

    	//canvas.setBitmap(bitmap);
    	
    	paint.setStyle(Style.STROKE);
    	paint.setStrokeWidth(5);
    	paint.setColor(Color.RED);
    	paint.setAntiAlias(true);
        
    	canvas.drawBitmap(bitmap,0,0,null);
    	
    	//canvas.drawLine((float)oldX, (float)oldY, (float)newX, (float)newY, paint);
    	canvas.drawLine((float)100, (float)100, (float)500, (float)500, paint);
    	//return bitmap;
    }
}




