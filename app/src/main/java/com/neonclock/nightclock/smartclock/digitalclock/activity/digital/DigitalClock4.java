package com.neonclock.nightclock.smartclock.digitalclock.activity.digital;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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


public class DigitalClock4 extends View {
    public DigitalClock4(Context context) {

        super(context);
        mContext=context;
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
    int intervalTime=10;

    public DigitalClock4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalClock4(Context context, AttributeSet attrs,
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

    public void changeInterval(int intervalTime){
        this.intervalTime=intervalTime;
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
        if(mHeight==0){
            mHeight=getHeight();
            Log.d("TAG", "onMeasure: "+mHeight);
        }
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
        Log.d("TAG", "onDraw: "+delaytime+" Blink= "+blink);


        if (changed) {
            mChanged = false;
        }
        boolean seconds = mSeconds;
        if (seconds ) {
            mSeconds = false;
        }
        canvas.drawColor(Color.parseColor("#061130"));
        //setSecond(canvas, mSecond);
        String date=calendar.get(java.util.Calendar.DAY_OF_MONTH)+"-"+ (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(java.util.Calendar.YEAR);
        int day=calendar.get(java.util.Calendar.DAY_OF_WEEK);
        String zone="none";
        if(calendar.get(java.util.Calendar.AM_PM)== java.util.Calendar.PM){
            zone="PM";
        }else{
            zone="AM";
        }


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
        paint=new Paint();
        setCircle(canvas,finalRadius);
        setRectangle(canvas,finalRadius);
        setDay(canvas,daysArray[day]);
        setDate(canvas,date);
        Log.d("Date_Clock","Clock 4 : format -> "+date);
        setHours(canvas, (int) ((mHour + calendar.get(java.util.Calendar.MINUTE) / 60) * 5f));
        setMinute(canvas, calendar.get(java.util.Calendar.MINUTE));
        setColun(canvas);
        delaytime=delaytime+10;

    }



    MyCount counter = new MyCount(10*intervalTime, intervalTime);
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
            DigitalClock4.this.invalidate();
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

    private void setRectangle(Canvas canvas, int finalRadius) {
        String color="#5CF8F8";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, 12, 100f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawRoundRect(new RectF((getWidth()/2)+220, 570, (getWidth()/2)-220, 370), 10, 6, paint);
    }

    private void setCircle(Canvas canvas,int circleRadius) {
        String color="#5CF8F8";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (mHeight/1513)*15, (mHeight/1513)*100f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawCircle((getWidth()/2),(mHeight/1513)*480f,(mHeight/1513)*300,paint);
    }

    private void setDay(Canvas canvas, String s) {
        String color="#5CF8F8";
        setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 40f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawText(s,(getWidth()/2)-50,350,paint);
    }

    private void setDate(Canvas canvas, String date) {
        String color="#5CF8F8";
        setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 40f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
        canvas.drawText(date,(getWidth()/2)-70,620,paint);
    }

    private void drawRipple(Canvas canvas, float x, float y, java.util.Calendar calendar) {
        // if (surfaceHolder.getSurface().isValid()) {
        //    Canvas canvas = surfaceHolder.lockCanvas();

        setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f,50,Color.RED, R.font.digital_italic);
        String zone="none";
        if(java.util.Calendar.AM_PM== java.util.Calendar.PM){
            zone="PM";
        }else{
            zone="AM";
        }
        //  setZone(canvas,zone);
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
        setCircle(canvas,finalRadius);
        setRectangle(canvas,finalRadius);
        int day=calendar.get(java.util.Calendar.DAY_OF_WEEK);
        setDay(canvas,daysArray[day]);
        String date=calendar.get(java.util.Calendar.DAY_OF_MONTH)+"-"+ (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(java.util.Calendar.YEAR);
        setDate(canvas,date);
        Log.d("Date_Clock","Clock 4 : format -> "+date);
        // setSecond(canvas, mSecond);
        setHours(canvas, (int) ((mHour + calendar.get(java.util.Calendar.MINUTE) / 60) * 5f));
        setMinute(canvas, calendar.get(java.util.Calendar.MINUTE));
        setColun(canvas);
        canvas.drawCircle(x, y, radius, paint);

        if(radius<= 100F){
            radius+=5F;
        }
        else{
            radius = 0F;
            willRippleDraw=false;

        }

    }
    public void setHours(Canvas canvas, float location) {
        String shadowcolor="#5CF8F8";
        String color="#ffffff";
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 150f,20,Color.parseColor(shadowcolor), R.font.digital_italic);
        int parsevalue = (int) (location / 5);
        int length = String.valueOf(parsevalue).length();
        if (length < 2) {
            canvas.drawText("0" + (int) (location / 5), (getWidth() / 2) - 190, 520, paint);
        } else {
            canvas.drawText((int) (location / 5) + "", (getWidth() / 2) - 190, 520, paint);
        }

    }

    public void setColun(Canvas canvas) {
        String shadowcolor="#5CF8F8";
        String color="#ffffff";
        setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, 10, 100f,20,Color.parseColor(shadowcolor), R.font.digital_italic);
        canvas.drawText(" ", (getWidth() / 2)-15, 510 - 10, paint);
        if (blink)
            canvas.drawText(":", (getWidth() / 2)-15 , 510 - 10, paint);
    }

    public void setMinute(Canvas canvas, float location) {
        String shadowcolor="#5CF8F8";
        String color="#ffffff";
        setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 150f,20,Color.parseColor(shadowcolor), R.font.digital_italic);
        int parsevalue = (int) location;
        int length = String.valueOf(parsevalue).length();
        //  Log.d("TAGA", "setMinute: "+length);
        if (length < 2) {
            canvas.drawText("0" + (int) location, (getWidth() / 2)+50 , 520, paint);
        } else {
            canvas.drawText((int) location + "", (getWidth() / 2)+50,520, paint);
        }
    }

    private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size,int radius,int shadowcolor,int font) {
        paint.reset();
        paint.setColor(colour);
        paint.setStyle(stroke);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setTextSize(size);
//        paint.setShadowLayer(radius,0,0,shadowcolor);
        paint.setTypeface(ResourcesCompat.getFont(getContext(), font));
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
