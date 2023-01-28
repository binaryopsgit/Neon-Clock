package com.neonclock.nightclock.smartclock.digitalclock.activity.digital;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.neonclock.nightclock.smartclock.digitalclock.R;


public class DigitalClock5 extends View {
    public DigitalClock5(Context context) {
        super(context);
        mContext=context;
        mBorderRadius=(int)angle;
        redRadius=(int)angle_red;
        yellowRadius=(int)angle_yellow;
        // TODO Auto-generated constructor stub
    }

    private Time mCalendar;
    int mMonth,date,year;
    int dayofWeek;
    String[] monthsArray={"","Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
    String[] daysArray={"","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    Paint paint;

    private int mDialWidth;
    private int mDialHeight;

    private boolean mAttached;
    boolean blink = false;
    private final Handler mHandler = new Handler();
    private float mMinutes;
    private float mHour;
    int mHeight;
    private boolean mChanged;
    float x=0,y=0;
    int delaytime=0;
    boolean willRippleDraw=false;
    float radius=0;
    Context mContext;
    int circleRadius=0,newRadius=70;
    private double angle = 300;
    private double angle_red = 300;
    private double angle_yellow = 300;
    private float mUserPicBorderCenterX,redBorderCenterX,yellowBorderCenterX;
    private float mUserPicBorderCenterY,redBorderCenterY,yellowBorderCenterY;
    private float mUserPicCenterX,redPicCenterX,yellowPicCenterX;
    private float mUserPicCenterY,redPicCenterY,yellowPicCenterY;
    private int mBorderRadius,redRadius,yellowRadius;
    int intervalTime=100;

    public DigitalClock5(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalClock5(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        TypedArray a =
                context.obtainStyledAttributes(
                        attrs, androidx.appcompat.R.styleable.ActionBar, defStyle, 0);
        mContext=context;
        // mDial = a.getDrawable(com.android.internal.R.styleable.AnalogClock_dial);
        // if (mDial == null) {

        //   }

        mCalendar = new Time();
        paint=new Paint();

        paint.setTextSize(20);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            willRippleDraw=true;
            radius=0;
            x=event.getX();
            y=event.getY();
            return true;
        }else{
            return false;
        }

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = new Time();

        // Make sure we update to the current time
        onTimeChanged();
        counter.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            counter.cancel();
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize =  MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);
        mHeight=heightSize;

        float hScale = 1.0f;
        float vScale = 1.0f;


        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float )heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSize((int) (mDialWidth * scale), widthMeasureSpec),
                resolveSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("TAG", "onMeasure: "+getHeight());
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        mHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        //convert to 12hour format from 24 hour format
        mHour = mHour > 12 ? mHour - 12 : mHour;
      //  mMinute = calendar.get(java.util.Calendar.MINUTE);
        mSecond = calendar.get(java.util.Calendar.SECOND);
        boolean changed = mChanged;

        if (blink == false && delaytime>=500) {
            blink = true;

        } else {
            if(delaytime>=1000) {
                delaytime = 0;
                blink = false;

            }

        }


        if (changed) {
            mChanged = false;
        }
        boolean seconds = mSeconds;
        if (seconds ) {
            mSeconds = false;
        }
        paint=new Paint();
        canvas.drawColor(Color.parseColor("#151718"));
        //setSecond(canvas, mSecond);
        String date=calendar.get(java.util.Calendar.DAY_OF_MONTH)+"-"+ java.util.Calendar.MONTH+"-"+calendar.get(java.util.Calendar.YEAR);
        int day=calendar.get(java.util.Calendar.DAY_OF_WEEK);
        int finalRadius=0;
        if(circleRadius<=70){
            circleRadius++;
            finalRadius=circleRadius;
        }if(circleRadius>=70){
            newRadius--;
            finalRadius=newRadius;

            if(newRadius==0){
                newRadius=70;
                circleRadius=0;
            }
        }
        mUserPicCenterX=((getWidth()/2));
        Log.d("widthvalue", "Circle Value Angle: "+getWidth());
        mUserPicCenterY=480;
        redPicCenterX=(getWidth()/2);
        redPicCenterY=480;
        yellowPicCenterX=(getWidth()/2);
        yellowPicCenterY=480;
        setCircle(canvas,finalRadius);
        fillCircleBlue(canvas,finalRadius,"#7E7AFF");//blue
        fillCircleRed(canvas,finalRadius,"#FA4D4D");//red
        fillCircleYellow(canvas,finalRadius,"#ECF062");//yellow
        drawRedcircle(canvas,finalRadius);
        drawBluecircle(canvas,finalRadius);
        drawYellowcircle(canvas,finalRadius);
        //  setRectangle(canvas,finalRadius);
        // setZone(canvas,zone);
        setDay(canvas,daysArray[day]);
        setDate(canvas,date);
        setHours(canvas, (int) ((mHour + calendar.get(java.util.Calendar.MINUTE) / 60) * 5f));
        setMinute(canvas, calendar.get(java.util.Calendar.MINUTE));
        setSecond(canvas, mSecond);

    }



    MyCount counter = new MyCount(100*intervalTime, intervalTime);
    public class MyCount extends CountDownTimer {
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            counter.start();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCalendar.setToNow();

            int hour = mCalendar.hour;
            int minute = mCalendar.minute;
            int second = mCalendar.second;
            mMonth= Calendar.MONTH;
            dayofWeek=mCalendar.weekDay;
            date=mCalendar.monthDay;
            year=mCalendar.year;

            mSecond=6.0f*second;
            mSeconds=true;
            //mChanged = true;
            DigitalClock5.this.invalidate();
            //Toast.makeText(mContext, "text", Toast.LENGTH_LONG).show();
        }
    }
    boolean mSeconds=false;
    float mSecond=0;
    private void onTimeChanged() {
        mCalendar.setToNow();

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;


        mMinutes = minute + second / 60.0f;
        mHour = hour + mMinutes / 60.0f;



        mChanged = true;
    }

    private void drawRedcircle(Canvas canvas, int finalRadius) {
        String color="#FA4D4D";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (mHeight/1513)*7, (mHeight/1513)*80f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle((getWidth()/2)-110, (mHeight/1513)*390,(mHeight/1513)*140,paint);

    }
    private void drawBluecircle(Canvas canvas, int finalRadius) {
        String color="#7E7AFF";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (mHeight/1513)*7, (mHeight/1513)*80f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle((getWidth()/2)+155, (mHeight/1513)*500,(mHeight/1513)*125,paint);

    }
    private void drawYellowcircle(Canvas canvas, int finalRadius) {
        String color="#ECF062";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (mHeight/1513)*7, (mHeight/1513)*80f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle((getWidth()/2)-45, (mHeight/1513)*650,(mHeight/1513)*108,paint);

    }

    public void moveBlue() {
        if (angle > 360) {
            angle = 0;
        }
        angle+=0.5;
        Log.d("Values_", "Circle Value Angle: "+angle);
        updatePositionBlue(angle);
    }
    public void moveRed() {
        if (angle_red > 360) {
            angle_red = 0;
        }
        angle_red+=1;
        updatePositionRed(angle_red);
    }
    public void moveYellow() {
        if (angle_yellow > 360) {
            angle_yellow = 0;
        }
        angle_yellow+=2;
        updatePositionYellow(angle_yellow);
    }
    private void updatePositionBlue(double angle) {
        angle = Math.toRadians(angle);
        mUserPicBorderCenterX = (float) (mUserPicCenterX + Math.cos(angle) * mBorderRadius);
        mUserPicBorderCenterY = (float) (mUserPicCenterY + Math.sin(angle) * mBorderRadius);
    }
    private void updatePositionRed(double angle) {
        angle = Math.toRadians(angle);
        redBorderCenterX = (float) (redPicCenterX + Math.cos(angle) * redRadius);
        redBorderCenterY = (float) (redPicCenterY + Math.sin(angle) * redRadius);
    }
    private void updatePositionYellow(double angle) {
        angle = Math.toRadians(angle);
        yellowBorderCenterX = (float) (yellowPicCenterX + Math.cos(angle) * yellowRadius);
        yellowBorderCenterY = (float) (yellowPicCenterY + Math.sin(angle) * yellowRadius);
    }
    private void fillCircleBlue(Canvas canvas, int circleRadius,String color) {
        moveBlue();
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, (mHeight/1513)*5, (mHeight/1513)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle(mUserPicBorderCenterX, mUserPicBorderCenterY,(mHeight/1513)*25,paint);
        // Log.d("Values_", "Circle Value X: "+blueCircleX+ " Y "+blueCircleY);
    }
    private void fillCircleRed(Canvas canvas, int circleRadius,String color) {
        moveRed();
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, (mHeight/1513)*5, (mHeight/1513)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle(redBorderCenterX, redBorderCenterY,(mHeight/1513)*25,paint);
        //  Log.d("Values_", "Circle Value X: "+blueCircleX+ " Y "+blueCircleY);
    }
    private void fillCircleYellow(Canvas canvas, int circleRadius,String color) {
        moveYellow();
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, (mHeight/1513)*5, (mHeight/1513)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle(yellowBorderCenterX, yellowBorderCenterY,(mHeight/1513)*25,paint);
        //  Log.d("Values_", "Circle Value X: "+blueCircleX+ " Y "+blueCircleY);
    }
    private void setCircle(Canvas canvas,int circleRadius) {
        String color="#5CF8F8";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (mHeight/1513)*5, (mHeight/1513)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        setShader_();
        canvas.drawCircle((getWidth()/2),(mHeight/1513)*480f,(mHeight/1513)*300,paint);
    }
    private void setDay(Canvas canvas, String s) {
        String color="#ffffff";
        setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 25f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawText(s,(getWidth()/2)-245, (mHeight/1513)*550,paint);
    }
    private void setDate(Canvas canvas, String date) {
        String color="#ffffff";
        setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 25f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawText(date,(getWidth()/2)+90, (mHeight/1513)*340,paint);
    }
    public void setHours(Canvas canvas, float location) {
        String shadowcolor="#FA4D4D";
        String color="#FA4D4D";
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 160f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
        int parsevalue = (int) (location / 5);
        int length = String.valueOf(parsevalue).length();
        Log.d("TAGA", "setHour: " + length);
        if (length < 2) {
            canvas.drawText("0" + (int) (location / 5), (getWidth()/2)-220, (mHeight/1513)*430, paint);
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            canvas.drawText("Hours", (getWidth()/2)-155, (mHeight/1513)*470, paint);
        } else {
            canvas.drawText((int) (location / 5) + "", (getWidth()/2)-220, (mHeight/1513)*430, paint);
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            canvas.drawText("Hours", (getWidth()/2)-155, (mHeight/1513)*470, paint);
        }

    }
    public void setMinute(Canvas canvas, float location) {
        String shadowcolor="#7E7AFF";
        String color="#7E7AFF";
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 140f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
        int parsevalue = (int) location;
        int length = String.valueOf(parsevalue).length();
        //  Log.d("TAGA", "setMinute: "+length);
        if (length < 2) {
            canvas.drawText("0" + (int) location, (getWidth()/2)+75, (mHeight/1513)*540, paint);
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            canvas.drawText("Minutes", (getWidth()/2)+75, (mHeight/1513)*580, paint);
        } else {
            canvas.drawText((int) location + "", (getWidth()/2)+85, (mHeight/1513)*540, paint);
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            canvas.drawText("Minutes", (getWidth()/2)+85, (mHeight/1513)*580, paint);
        }
    }
    public void setSecond(Canvas canvas, float location) {
        String shadowcolor="#ECF062";
        String color="#ECF062";
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 125f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
        int parsevalue = (int) location;
        int length = String.valueOf(parsevalue).length();
        // Log.d("TAGA", "setHour: "+length);
        if (length < 2) {
            canvas.drawText("0" + (int) location, (getWidth()/2)-110, (mHeight/1513)*690, paint);
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 25f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            canvas.drawText("Seconds", (getWidth()/2)-105, (mHeight/1513)*730, paint);
        } else {
            canvas.drawText((int) location + "", (getWidth()/2)-110, (mHeight/1513)*690, paint);
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 25f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            canvas.drawText("Seconds", (getWidth()/2)-105, (mHeight/1513)*730, paint);
        }
    }
    private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size,int radius,int shadowcolor,int font) {
        paint.reset();
        paint.setColor(colour);
        paint.setStyle(stroke);

        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setTextSize(size);
        paint.setShadowLayer(radius,0,0,shadowcolor);
        paint.setTypeface(ResourcesCompat.getFont(getContext(), font));
    }
    public void setShader_(){
        float[] pos=null;
        int[] colors={Color.parseColor("#E4BF86"),Color.parseColor("#C167E1"),Color.parseColor("#EC6288")};
        paint.setShader(new LinearGradient(0f, 0f, (float)getWidth(), 0f,colors, pos, Shader.TileMode.MIRROR));
    }
    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }

            onTimeChanged();

            invalidate();
        }
    };
}
