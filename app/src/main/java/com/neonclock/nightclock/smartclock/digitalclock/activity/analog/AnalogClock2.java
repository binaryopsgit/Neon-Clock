package com.neonclock.nightclock.smartclock.digitalclock.activity.analog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.neonclock.nightclock.smartclock.digitalclock.R;


public class AnalogClock2 extends View {
    public AnalogClock2(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private Time mCalendar;
    int mMonth,date,year;
    int dayofWeek;
    String[] monthsArray={"","Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
    String[] daysArray={"","Monday","Tuesday","Wednesday","Thursday","Saturday","Sunday"};
    private Drawable mHourHand;
    private Drawable mMinuteHand;
    private Drawable mSecondHand;
    private Drawable mDial;
    private Drawable mBox;
    Paint paint;

    private int mDialWidth;
    private int mDialHeight;

    private boolean mAttached;

    private final Handler mHandler = new Handler();
    private float mMinutes;
    private float mHour;
    private boolean mChanged;
    Rect mBounds;
    float mCenterX,mCenterY,mRadius;


    Context mContext;


    public AnalogClock2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnalogClock2(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        Resources r = context.getResources();
        TypedArray a =
                context.obtainStyledAttributes(
                        attrs, androidx.appcompat.R.styleable.ActionBar, defStyle, 0);
        mContext=context;
        // mDial = a.getDrawable(com.android.internal.R.styleable.AnalogClock_dial);
        // if (mDial == null) {
        mDial = r.getDrawable(R.drawable.ic_dial_clock5);
        // }

        //  mHourHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_hour);
        //  if (mHourHand == null) {
        mHourHand = r.getDrawable(R.drawable.ic_hour_clock6);
        //  }

        //   mMinuteHand = a.getDrawable(com.android.internal.R.styleable.AnalogClock_hand_minute);
        //   if (mMinuteHand == null) {
        mMinuteHand = r.getDrawable(R.drawable.ic_minutes_clock6);
        mSecondHand = r.getDrawable(R.drawable.ic_seconds_clock6);
        mBox=r.getDrawable(R.drawable.ic_dot_clock3);
        //   }

        mCalendar = new Time();
        paint=new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight =mDial.getIntrinsicHeight();
    }

    public void changeItems(Drawable dial,Drawable hour,Drawable minute,Drawable second,Drawable box){
        mDial=dial;
        mHourHand=hour;
        mMinuteHand=minute;
        mSecondHand=second;
        mBox=box;
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

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }
        boolean seconds = mSeconds;
        if (seconds ) {
            mSeconds = false;
        }

        final Drawable box=mBox;
        int box_w=box.getIntrinsicWidth();
        int box_h=box.getIntrinsicHeight();

        int availableWidth = getWidth();///2;
        int availableHeight = getHeight();///2;

        int x = availableWidth / 2;
        int y = availableHeight / 2;

       // box.setBounds(x - (box_w / 2), (y - (box_h / 2))+150, x + (box_w / 2), y + (box_h / 2)+150);
       // box.draw(canvas);
      //  canvas.drawText(monthsArray[mMonth]+" "+date+" "+year,(getWidth()/2)-50,(getHeight()/2)+150,paint);
      //  canvas.drawText(daysArray[dayofWeek],(getWidth()/2)-50,(getHeight()/2)+180,paint);

        final Drawable dial = mDial;
        int w = dial.getIntrinsicWidth();
        int h = dial.getIntrinsicHeight();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        if (changed) {
            dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        dial.draw(canvas);

        canvas.save();

        canvas.rotate(mHour / 12.0f * 360.0f, x, y);

//        canvas.rotate((mHour / 12.0f * 360.0f) + (mMinutes / 60.0f * 30.0f), x, y);

        final Drawable hourHand = mHourHand;
        if (changed) {
            w = hourHand.getIntrinsicWidth();
            h = hourHand.getIntrinsicHeight();
            hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        hourHand.draw(canvas);
        canvas.restore();

        canvas.save();
        canvas.rotate(mSecond, x, y);


        //minuteHand = mMinuteHand;
        if (seconds) {
            w = mSecondHand.getIntrinsicWidth();
            h = mSecondHand.getIntrinsicHeight();
            mSecondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }
        mSecondHand.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);
        //canvas.rotate(mSecond, x, y);
        final Drawable minuteHand = mMinuteHand;
        if (changed) {
            w = minuteHand.getIntrinsicWidth();
            h = minuteHand.getIntrinsicHeight();
            minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
        }

        minuteHand.draw(canvas);
        canvas.restore();
        canvas.save();

        canvas.restore();
        if (scaled) {
            canvas.restore();
        }
    }
    MyCount counter = new MyCount(10000, 1000);
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
            AnalogClock2.this.invalidate();
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
