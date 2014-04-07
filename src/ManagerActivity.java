package hk.ust.ins;

import hk.ust.ins.data.ThreeDimensionalDouble;
import hk.ust.ins.op.Alignment;
import hk.ust.ins.op.Calculation;
import hk.ust.ins.op.Calibration;
import hk.ust.ins.op.Computation;
import hk.ust.ins.op.Correction;
import hk.ust.ins.op.LinearWeightedMovingAverage;
import hk.ust.ins.op.Update;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManagerActivity extends Activity implements SensorEventListener {
		
	private Bitmap bitmap;
    private ImageView image;
	private Paint paint;
	private Canvas canvas;
	
	private ThreeDimensionalDouble lastPosition;
	private ThreeDimensionalDouble lastVelocity;
	private ThreeDimensionalDouble lastAlignmentResult;
	private ThreeDimensionalDouble lastRotationAngle;
	private ThreeDimensionalDouble lastRotationValue;
	private long lastTimeStampLinear;
	private long lastTimeStampRotation;
	
	private SensorManager mSensorManager;
	private Sensor mAcceleromter;
	//private Sensor mLinearAcceleromter;
    private Sensor mRotationalVelocity;
    
    private ThreeDimensionalDouble flag = new ThreeDimensionalDouble(0,0,0);
    private TextView tv;
    
    private TextView mTextView01;
    private TextView mTextView02;
    private TextView mTextView03;
    private TextView mTextView04;
    private TextView mTextView05;
    
    private int ma_len;
    private int cnt_len;
    private int count;
    private int cal_len;
    private int cal_cnt;
    private ThreeDimensionalDouble cal_val;
    
    private Calibration calibration;
    
    private LinearWeightedMovingAverage ma_accx;
	private LinearWeightedMovingAverage ma_accy;
	private LinearWeightedMovingAverage ma_accz;
    
	private LinearWeightedMovingAverage ma_gyrox;
	private LinearWeightedMovingAverage ma_gyroy;
	private LinearWeightedMovingAverage ma_gyroz;
	
    @SuppressLint("FloatMath")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAcceleromter = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //mLinearAcceleromter = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mRotationalVelocity = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        
        Initialization.init();
        lastPosition = Initialization.position;
    	lastVelocity = Initialization.velocity;
    	lastAlignmentResult = Initialization.linearValue;
    	lastRotationValue = Initialization.rotationValue;
    	lastRotationAngle = Initialization.rotationAngle;
    	lastTimeStampLinear = 0;
    	lastTimeStampRotation = 0;
    	
    	ma_len = 6;
    	cnt_len = 20;
    	count = 0;
    	cal_len = 100;
    	cal_cnt = 0;
    	cal_val = new ThreeDimensionalDouble();
    	calibration = new Calibration();
    	
    	ma_accx = new LinearWeightedMovingAverage(ma_len);
    	ma_accy = new LinearWeightedMovingAverage(ma_len);
    	ma_accz = new LinearWeightedMovingAverage(ma_len);

    	ma_gyrox = new LinearWeightedMovingAverage(ma_len);
    	ma_gyroy = new LinearWeightedMovingAverage(ma_len);
    	ma_gyroz = new LinearWeightedMovingAverage(ma_len);
    	
    	setTv((TextView) findViewById(R.id.TextView01));
    	Log.v("INS", "Create Manager Activity");
    	
    	Resources res = getResources();
    	int id = this.getIntent().getExtras().getInt("id"); 
    	switch(id) {
    		case 1:bitmap = BitmapFactory.decodeResource(res, R.drawable.test);break;
    		case 2:bitmap = BitmapFactory.decodeResource(res, R.drawable.test0);break;
    		case 3:bitmap = BitmapFactory.decodeResource(res, R.drawable.test1);break;
    		case 4:bitmap = BitmapFactory.decodeResource(res, R.drawable.test2);break;
    		case 5:bitmap = BitmapFactory.decodeResource(res, R.drawable.test3);break;
    	}
    	
    	bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    	
    	canvas = new Canvas();
    	canvas.setBitmap(bitmap);
    	canvas.drawBitmap(bitmap,(float)0,(float)0,null);
    	
    	paint = new Paint(Paint.DITHER_FLAG);
    	paint.setStyle(Style.STROKE);
    	paint.setStrokeWidth(5);
    	paint.setColor(Color.RED);
    	paint.setAntiAlias(true);    	
    	
    	image = (ImageView) findViewById(R.id.output);
    	image.setLayoutParams(new LinearLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
    	image.setImageBitmap(bitmap);
    	
		image.setOnTouchListener(new OnTouchListener() {
        	private int mx;
        	private int my;
        	private final int NONE = 0;
            private final int MOVE = 1;  
            private final int ZOOM = 2; 
            private int mode;
            private double oldDistance;
   
            @Override
    		public boolean onTouch(View arg0, MotionEvent event) {
    			
    			int x = (int)event.getRawX();
    			int y = (int)event.getRawY();
    			int width = arg0.getWidth();
    			int height = arg0.getHeight();  
    			
    			switch(event.getAction() & MotionEvent.ACTION_MASK) {
    			case MotionEvent.ACTION_DOWN:
    				mx = (int)event.getX();
    				my = (int)(y-arg0.getTop());
    				arg0.bringToFront();
    				arg0.postInvalidate();
    				mode = MOVE;
    				break;
    				
    			case MotionEvent.ACTION_POINTER_UP:  
                    mode = NONE;  
                    break;
                   
    			case MotionEvent.ACTION_POINTER_DOWN:  
                    oldDistance = (float)Math.sqrt((event.getX(0)-event.getX(1))*(event.getX(0)-event.getX(1))+(event.getY(0)-event.getY(1))*(event.getY(0)-event.getY(1)));  
                    if (oldDistance > 10f) {
                        mode = ZOOM;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                	if(mode == MOVE) {
	                    int left;
	                    int top;
	                       
	                    if(/*layoutWidth >= width || */x >= mx) {
	                    	left = 0;
	                    }
	                    else {
	                    	left = x - mx;/*
	                    	if(left < layoutWidth - width) {
	                    		left = layoutWidth - width;
	                    	}*/
	                    }
	         
	                    if(/*layoutHeight >= height ||*/ y >= my) {
	                    	top = 0;
	                    }
	                    else {
	                    	top = y - my;/*
	                    	if(left < layoutHeight - height) {
	                    		left = layoutHeight - height;
	                    	}*/
	                    }
	                    
	                    int right = left + width;
	                    int bottom = top + height;  
	 
	                    arg0.layout(left, top, right, bottom); 
	                    arg0.postInvalidate();
                	}
                	else if(mode == ZOOM) {
                		double newDistance = Math.sqrt((event.getX(0)-event.getX(1))*(event.getX(0)-event.getX(1))+(event.getY(0)-event.getY(1))*(event.getY(0)-event.getY(1)));  
                        if(newDistance > 10f) {  
                            double scale = Math.sqrt(newDistance/oldDistance);  
                            oldDistance = newDistance;
                            arg0.setLayoutParams(new LinearLayout.LayoutParams((int)(width*scale), (int)(height*scale)));
                            //arg0.layout(left, top, right, bottom);
                        }
                	}
                    break;
                }
    			return true;
    		}
        });
		
		mTextView01 = (TextView) findViewById(R.id.TextView01);
		mTextView02 = (TextView) findViewById(R.id.TextView02);
		mTextView03 = (TextView) findViewById(R.id.TextView03);
		mTextView04 = (TextView) findViewById(R.id.TextView04);
		mTextView05 = (TextView) findViewById(R.id.TextView05);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_manager, menu);
        return true;
    }
    
    public void runRotation(ThreeDimensionalDouble rotationvalue, long timeStamp) {
    	
    	if(lastTimeStampRotation == 0) {
			lastTimeStampRotation = timeStamp;
			return;
		}
    	
    	double timeInterval = (timeStamp - lastTimeStampRotation) / 1000000000.0;
		
    	// calibration
		ThreeDimensionalDouble rotationCorrectionResult = Correction.RotationCorrection(rotationvalue, calibration.rotationValue);
		
		// moving average
		ThreeDimensionalDouble rotationAfterFilter = new ThreeDimensionalDouble();
		ma_gyrox.pushValue(rotationCorrectionResult.x);
		rotationAfterFilter.x = ma_gyrox.getAverageValue();
		ma_gyroy.pushValue(rotationCorrectionResult.y);
		rotationAfterFilter.y = ma_gyroy.getAverageValue();
		ma_gyroz.pushValue(rotationCorrectionResult.z);
		rotationAfterFilter.z = ma_gyroz.getAverageValue();
		
		lastRotationAngle = Calculation.execute_update(lastRotationAngle, lastRotationValue, rotationAfterFilter,  timeInterval);
		lastRotationValue = rotationAfterFilter;
		lastTimeStampRotation = timeStamp;
    }
    
    public void runLinear(ThreeDimensionalDouble accValue, long timeStamp) {
    	if(lastTimeStampLinear == 0) {
			lastTimeStampLinear = timeStamp;
			return;
		}

    	double timeInterval = (timeStamp - lastTimeStampLinear) / 1000000000.0;
    	
    	ThreeDimensionalDouble accCorrectionResult = Correction.LinearCorrection(accValue, calibration.linearValue); 
    	
    	accCorrectionResult.z = 0;
    	
    	ThreeDimensionalDouble accAfterFilter = new ThreeDimensionalDouble();
    	
		if(Math.abs(accCorrectionResult.x) < 0.1)
			accCorrectionResult.x = 0;
		if(Math.abs(accCorrectionResult.y) < 0.1)
			accCorrectionResult.y = 0;
		if(Math.abs(accCorrectionResult.z) < 0.1)
			accCorrectionResult.z = 0;
		
		String str;
		
    	ma_accx.pushValue(accCorrectionResult.x);
    	ma_accy.pushValue(accCorrectionResult.y);
    	ma_accz.pushValue(accCorrectionResult.z);
    	accAfterFilter.x = ma_accx.getAverageValue();
    	accAfterFilter.y = ma_accy.getAverageValue();
    	accAfterFilter.z = ma_accz.getAverageValue();
    	
     	str = "LinearWeightedMovingAverage \n\t\t\t\t\tx: " + accAfterFilter.x + "\n\t\t\t\t\ty: " + accAfterFilter.y + "\n\t\t\t\t\tz: " + accAfterFilter.z;
		mTextView02.setText(str);
    	
		ThreeDimensionalDouble alignmentResult = new ThreeDimensionalDouble();
		ThreeDimensionalDouble velocity = new ThreeDimensionalDouble();
		
		if(Math.abs(accAfterFilter.x - flag.x) > 0.1 || Math.abs(accAfterFilter.y - flag.y) > 0.1){
			
			flag.set_using_three(accAfterFilter);
    		
        	alignmentResult = Alignment.execute(accAfterFilter, lastRotationAngle);
        	velocity = Computation.calculateVelocity(lastVelocity, lastAlignmentResult, alignmentResult, timeInterval);
    		ThreeDimensionalDouble computationResult = Computation.excute(lastVelocity, velocity, timeInterval);		
    		ThreeDimensionalDouble position = Update.excute(lastPosition, computationResult);
    		display(position.x, position.y, lastPosition.x, lastPosition.y);
    		
    		lastTimeStampLinear = timeStamp;
    		lastVelocity = velocity;
    		lastAlignmentResult = alignmentResult;
    		lastPosition = position;
    		
    		count = 0;
    	}
    	else{
    		++count;

    		flag.set_using_double(	(flag.x * (count - 1) + accAfterFilter.x) / count, 
    								(flag.y * (count - 1) + accAfterFilter.y) / count,
    								(flag.z * (count - 1) + accAfterFilter.z) / count);
    		
    		if(count > cnt_len){
    			
    			alignmentResult = Alignment.execute(new ThreeDimensionalDouble(0,0,0), lastRotationAngle);
    			velocity.set_using_double(0, 0, 0);
    			ThreeDimensionalDouble computationResult = Computation.excute(lastVelocity, velocity, timeInterval);		
        		ThreeDimensionalDouble position = Update.excute(lastPosition, computationResult);
        		display(position.x, position.y, lastPosition.x, lastPosition.y);
        		
        		lastTimeStampLinear = timeStamp;
        		lastVelocity = velocity;
        		lastAlignmentResult = alignmentResult;
        		lastPosition = position;
    		}
    	}
		
		str = "Position \n\t\t\t\t\tx: " + lastPosition.x + "\n\t\t\t\t\ty: " + lastPosition.y + "\n\t\t\t\t\tz: " + lastPosition.z;
		mTextView01.setText(str);
		
		str = "Alignment \n\t\t\t\t\tx: " + lastAlignmentResult.x + "\n\t\t\t\t\ty: " + lastAlignmentResult.y + "\n\t\t\t\t\tz: " + lastAlignmentResult.z;
		mTextView03.setText(str);
		
		str = "velocity \n\t\t\t\t\tx: " + lastVelocity.x + "\n\t\t\t\t\ty: " + lastVelocity.y + "\n\t\t\t\t\tz: " + lastVelocity.z;
		mTextView04.setText(str);
    }
    
    public void display(double newX, double newY, double oldX, double oldY){
    	int scale = 15; // 15 == 1m
    	canvas.drawLine((float)(485 + newX * scale), (float)(620 - newY * scale),
    			(float)(485 + oldX * scale), (float)(620 - oldY * scale), paint);
    	
    	image.setImageBitmap(bitmap);
    }
    
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener((SensorEventListener) this, mAcceleromter, SensorManager.SENSOR_DELAY_GAME);
        //mSensorManager.registerListener((SensorEventListener) this, mLinearAcceleromter, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener((SensorEventListener) this, mRotationalVelocity, SensorManager.SENSOR_DELAY_GAME);
    }
    
    protected void onPause() {
    	super.onPause();
	    mSensorManager.unregisterListener(this);
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	
    }
    
    public void onSensorChanged(SensorEvent event) {
    	if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
    		if(cal_cnt < cal_len)
    		{
    			++ cal_cnt;
    			cal_val.x += event.values[0];
    			cal_val.y += event.values[1];
    			cal_val.z += 0;
    			if(cal_cnt == cal_len)
    			{
    				cal_val.x /= cal_len;
    				cal_val.y /= cal_len;
    				cal_val.z /= cal_len;
    				calibration.linearValue.set_using_three(cal_val);
    				String str;
    				str = "acc calibration \n\t\t\t\t\tx: " + calibration.linearValue.x + "\n\t\t\t\t\ty: " + calibration.linearValue.y + "\n\t\t\t\t\tz: " + calibration.linearValue.z;
    				mTextView05.setText(str);
    			}
        	}
    		else
    		{
    			runLinear(new ThreeDimensionalDouble(event.values[0],event.values[1],event.values[2]), event.timestamp);
    		}
    	}
    	else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
	    	runRotation(new ThreeDimensionalDouble(event.values[0],event.values[1],event.values[2]), event.timestamp);
    	}
    	else {
    		
    	}
    }

	public TextView getTv() {
		return tv;
	}

	public void setTv(TextView tv) {
		this.tv = tv;
	}
}