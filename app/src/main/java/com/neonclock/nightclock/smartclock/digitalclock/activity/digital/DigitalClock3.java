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


public class DigitalClock3 extends View {
    public DigitalClock3(Context context) {
        super(context);
        mContext=context;
        // TODO Auto-generated constructor stub
    }

    private Time mCalendar;
    int mMonth,date,year;
    int dayofWeek;
    String[] monthsArray={"","Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
    String[] daysArray={"","Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
    Paint paint;

    private int mDialWidth;
    private int mDialHeight;

    private boolean mAttached;
    boolean blink = false;
    private final Handler mHandler = new Handler();
    private float mMinutes;
    private float mHour;
    private boolean mChanged;
    float x=0,y=0;
    int delaytime=0;
    boolean willRippleDraw=false;
    float radius=0;
    Context mContext;
    int linelength=-20;
    float linex=0,lineTwox=0,lineTwoy=0,liney=0,lineThreex=0,lineThreey=0;
    float lineFourx=0,lineFoury=0,lineFivex=0,lineFivey=0,lastLine=0;
    int intervalTime=10;

    public DigitalClock3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalClock3(Context context, AttributeSet attrs,
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
        canvas.drawColor(Color.parseColor("#061130"));
        paint=new Paint();
        //setSecond(canvas, mSecond);
        String date=calendar.get(java.util.Calendar.DAY_OF_MONTH)+"-"+ (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(java.util.Calendar.YEAR);
        int day=calendar.get(java.util.Calendar.DAY_OF_WEEK);
        String zone="none";
        if(calendar.get(java.util.Calendar.AM_PM)== java.util.Calendar.PM){
            zone="PM";
        }else{
            zone="AM";
        }


        contineousLine(canvas);
        setZone(canvas,zone);
        setDay(canvas,daysArray[day]);
        setDate(canvas,date);
        Log.d("Date_Clock","Clock 3 : format -> "+date);
        setHours(canvas, (int) ((mHour + calendar.get(java.util.Calendar.MINUTE) / 60) * 5f));
        setMinute(canvas, calendar.get(java.util.Calendar.MINUTE));
        setColun(canvas);

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
            DigitalClock3.this.invalidate();
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

    private void contineousLine(Canvas canvas) {
        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 70f,20);
        if(linelength<-50){
            linelength=getWidth();
        }else if(linelength==650) {
            if(linex==0 && liney==0){
                linex=linelength;
                liney=1000;
            }else if(liney>=1100){
                if(lineTwox==0 && lineTwoy==0) {
                    lineTwox = linex;
                    lineTwoy = liney;
                }else if(lineTwoy<900){
                    if(lineThreex==0 && lineThreey==0){
                        lineThreex=lineTwox;
                        lineThreey=lineTwoy;
                    }else if(lineThreey>=1200){
                        if(lineFourx==0 && lineFoury==0){
                            lineFourx=lineThreex;
                            lineFoury=lineThreey;
                        }else if(lineFoury<750) {

                            if(lineFivex==0 && lineFivey==0){
                                lineFivex=lineFourx;
                                lineFivey=lineFoury;
                            }else if(lineFivey>=1000){
                                if(lastLine==0){
                                    lastLine=lineFivex;
                                }else if(lastLine<=-50){
                                    linelength=getWidth();
                                    linex=0;liney=0;
                                    lineTwox=0;lineTwoy=0;
                                    lineThreex=0;lineThreey=0;
                                    lineFourx=0;lineFoury=0;
                                    lineFivex=0;lineFivey=0;
                                    lastLine=0;
                                    return;
                                }else{
                                    lastLine-=5;
                                }
                                canvas.drawLine(lastLine,1000,lineFivex+3,998,paint);
                                canvas.drawLine(lineFivex,lineFivey,lineFourx+1,lineFoury+2,paint);
                                //   canvas.drawLine(lineFourx,lineFoury,lineThreex+1,lineThreey+2,paint);
                                //  canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                                //   canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                                //   canvas.drawLine(linex,liney,linelength+3,998,paint);
                                //    canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);

                            }else {
                                lineFivex--;
                                lineFivey+=5;
                            }

                            canvas.drawLine(lineFivex,lineFivey,lineFourx+1,lineFoury+2,paint);
                            canvas.drawLine(lineFourx,lineFoury,lineThreex+1,lineThreey+2,paint);
                            //  canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                            //   canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                            //   canvas.drawLine(linex,liney,linelength+3,998,paint);
                            //  canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);
                        }else {
                            lineFourx--;
                            lineFoury-=5;
                        }

                        canvas.drawLine(lineFourx,lineFoury,lineThreex+1,lineThreey+2,paint);
                        canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                        //  canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                        //   canvas.drawLine(linex,liney,linelength+3,998,paint);
                        // canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);
                    }else{
                        lineThreex--;
                        lineThreey+=5;
                    }
                    canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                    canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                    //  canvas.drawLine(linex,liney,linelength+3,998,paint);
                    //  canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);
                }else {
                    lineTwox--;
                    lineTwoy-=5;
                }
                canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                canvas.drawLine(linex,liney,linelength+3,998,paint);
                // canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);
            }else{
                linex--;
                liney+=5;
            }

            canvas.drawLine(linex,liney,linelength+3,998,paint);
            canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);
        }else{
            linelength-=5;
            canvas.drawLine(linelength,1000,getWidth()+50,1000,paint);
        }
        Log.d("Line_Lenth", "contineousLine: "+linelength);

    }

    private void setZone(Canvas canvas, String am) {
        setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 70f,20);
        canvas.drawText(am,(getWidth()/2)+250,300,paint);
    }

    private void setDay(Canvas canvas, String s) {
        setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 80f,20);
        canvas.drawText(s,(getWidth()/2)-70,620,paint);
    }

    private void setDate(Canvas canvas, String date) {

        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 80f,20);
        canvas.drawText(date,(getWidth()/2)-150,500,paint);
    }

    private void drawRipple(Canvas canvas, float x, float y, java.util.Calendar calendar) {
        // if (surfaceHolder.getSurface().isValid()) {
        //    Canvas canvas = surfaceHolder.lockCanvas();
        setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f,50);
        String zone="none";
        if(java.util.Calendar.AM_PM== java.util.Calendar.PM){
            zone="PM";
        }else{
            zone="AM";
        }
        contineousLine(canvas);
        setZone(canvas,zone);
        int day=calendar.get(java.util.Calendar.DAY_OF_WEEK);
        setDay(canvas,daysArray[day]);
        String date=calendar.get(java.util.Calendar.DAY_OF_MONTH)+"-"+ (calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(java.util.Calendar.YEAR);
        setDate(canvas,date);
        Log.d("Date_Clock","Clock 3 : format -> "+date);
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
        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 250f,50);
        int parsevalue = (int) (location / 5);
        int length = String.valueOf(parsevalue).length();
        Log.d("TAGA", "setHour: " + length);
        if (length < 2) {
            canvas.drawText("0" + (int) (location / 5), (getWidth() / 2) - 280, 400, paint);
        } else {
            canvas.drawText((int) (location / 5) + "", (getWidth() / 2) - 280, 400, paint);
        }

    }

    public void setColun(Canvas canvas) {
        setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.STROKE, 10, 150f,50);
        canvas.drawText(" ", (getWidth() / 2)-35, 400 - 10, paint);
        if (blink)
            canvas.drawText(":", (getWidth() / 2)-35 , 400 - 10, paint);
    }

    public void setMinute(Canvas canvas, float location) {
        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 250f,50);
        int parsevalue = (int) location;
        int length = String.valueOf(parsevalue).length();
        //  Log.d("TAGA", "setMinute: "+length);
        if (length < 2) {
            canvas.drawText("0" + (int) location, (getWidth() / 2) , 400, paint);
        } else {
            canvas.drawText((int) location + "", (getWidth() / 2),400, paint);
        }
    }

    private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size,int radius) {
        paint.reset();
        paint.setColor(colour);
        paint.setStyle(stroke);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setTextSize(size);
//        paint.setShadowLayer(radius,0,0,Color.RED);
        paint.setTypeface(ResourcesCompat.getFont(getContext(), R.font.digital_italic));
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
