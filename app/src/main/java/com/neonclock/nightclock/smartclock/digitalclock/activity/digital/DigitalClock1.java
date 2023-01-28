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


public class DigitalClock1 extends View {
    public DigitalClock1(Context context) {
        super(context);
        mContext=context;
        // TODO Auto-generated constructor stub
    }

    private Time mCalendar;
    int mMonth,date,year;
    int dayofWeek;
    String[] monthsArray={"","Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
    String[] daysArray={"","Monday","Tuesday","Wednesday","Thursday","Saturday","Sunday"};
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
    int intervalTime=10;


    public DigitalClock1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalClock1(Context context, AttributeSet attrs,
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paint.setColor(context.getColor(R.color.bg_color));
        }else {
            paint.setColor(Color.BLACK);
        }
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
    public void updateView(int hour,int minute,int seconds){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        mHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        //convert to 12hour format from 24 hour format
        mHour = mHour > 12 ? mHour - 12 : mHour;
        mMinutes = calendar.get(java.util.Calendar.MINUTE);
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
        canvas.drawColor(Color.parseColor("#051031"));
        paint=new Paint();
        paint.setColor(Color.LTGRAY);
        setSecond(canvas, mSecond);
        setHours(canvas, (int) ((mHour + calendar.get(java.util.Calendar.MINUTE) / 60) * 5f));
        setMinute(canvas,mMinutes);
        setColun(canvas);

        delaytime+=10;
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
            DigitalClock1.this.invalidate();
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
    public void setSecond(Canvas canvas, float location) {

        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 100f);
        int parsevalue = (int) location;
        int length = String.valueOf(parsevalue).length();
        // Log.d("TAGA", "setHour: "+length);
        if (length < 2) {
            canvas.drawText("0" + (int) location, (getWidth() / 2) + 250, (getHeight() / 2), paint);
        } else {
            canvas.drawText((int) location + "", (getWidth() / 2) + 250, (getHeight() / 2), paint);
        }
    }
    public void setHours(Canvas canvas, float location) {
        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 200f);
        int parsevalue = (int) (location / 5);
        int length = String.valueOf(parsevalue).length();
        Log.d("TAGA", "setHour: " + length);
        if (length < 2) {
            canvas.drawText("0" + (int) (location / 5), (getWidth() / 2) - 300, getHeight() / 2, paint);
        } else {
            canvas.drawText((int) (location / 5) + "", (getWidth() / 2) - 300, getHeight() / 2, paint);
        }

    }

    public void setColun(Canvas canvas) {
        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.STROKE, 20, 200f);
        canvas.drawText(" ", (getWidth() / 2) - 50, (getHeight() / 2) - 10, paint);
        if (blink)
            canvas.drawText(":", (getWidth() / 2) - 50, (getHeight() / 2) - 10, paint);
    }

    public void setMinute(Canvas canvas, float location) {
        setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 200f);
        int parsevalue = (int) location;
        int length = String.valueOf(parsevalue).length();
        //  Log.d("TAGA", "setMinute: "+length);
        if (length < 2) {
            canvas.drawText("0" + (int) location, (getWidth() / 2) + 20, (getHeight() / 2), paint);
        } else {
            canvas.drawText((int) location + "", (getWidth() / 2) + 20, (getHeight() / 2), paint);
        }
    }

    private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size) {
        paint.reset();
        paint.setColor(colour);
        paint.setStyle(stroke);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(true);
        paint.setTextSize(size);
        paint.setTypeface(ResourcesCompat.getFont(mContext, R.font.ds_digital));
    }
    private void drawRipple(Canvas canvas, float x, float y, java.util.Calendar calendar) {
        // if (surfaceHolder.getSurface().isValid()) {
        //    Canvas canvas = surfaceHolder.lockCanvas();

        setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f);
        setSecond(canvas, mSecond);
        setHours(canvas, (int) ((mHour + calendar.get(java.util.Calendar.MINUTE) / 60) * 5f));
        setMinute(canvas, calendar.get(java.util.Calendar.MINUTE));
        setColun(canvas);
        canvas.drawCircle(x, y, radius, paint);

        if(radius<= 400F){
            radius+=5F;
        }
        else{
            radius = 0F;
            willRippleDraw=false;

        }

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
