package com.neonclock.nightclock.smartclock.digitalclock.clock_service;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;


import com.neonclock.nightclock.smartclock.digitalclock.R;
import com.neonclock.nightclock.smartclock.digitalclock.model.ClockAssetModel;
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils;

import java.util.Calendar;



public class AnalogClockService2 extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        Drawable mDial= ClockUtils.Companion.getClockAssetDrawable(getApplicationContext(),"Clocks/Analog Clock/dial_2.webp");

        ClockAssetModel clockAssetModel=ClockUtils.Companion.getClockAssetModel();
        assert clockAssetModel != null;
        Drawable mHour= ClockUtils.Companion.getClockAssetDrawable(getApplicationContext(),clockAssetModel.getMHour());
        Drawable mMinute= ClockUtils.Companion.getClockAssetDrawable(getApplicationContext(),clockAssetModel.getMMinute());
        Drawable mSecond= ClockUtils.Companion.getClockAssetDrawable(getApplicationContext(),clockAssetModel.getMSecond());
        try{
            mDial= ClockUtils.Companion.getClockAssetDrawable(getApplicationContext(),clockAssetModel.getMDial());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new AnalogClock1(mDial,mHour
                ,mMinute,mSecond);

//        return new AnalogClock1(mDial,getResources().getDrawable(R.drawable.ic_hour_clock6)
//                ,getResources().getDrawable(R.drawable.ic_minutes_clock6),getResources().getDrawable(R.drawable.ic_seconds_clock6));




        //        int position = 1;
//        if (position == 0) {
//            return new AnalogClock1(getResources().getDrawable(R.drawable.ic_dial_clock5),getResources().getDrawable(R.drawable.ic_hour_clock6)
//                    ,getResources().getDrawable(R.drawable.ic_minutes_clock6),getResources().getDrawable(R.drawable.ic_seconds_clock6));
//        } else if (position == 2) {
//            return new AnalogClock1(getResources().getDrawable(R.drawable.ic_dial_clock5),getResources().getDrawable(R.drawable.ic_hour_clock6)
//                    ,getResources().getDrawable(R.drawable.ic_minutes_clock6),getResources().getDrawable(R.drawable.ic_seconds_clock6));
//        } else if (position == 1) {
//            return new AnalogClock1(getResources().getDrawable(R.drawable.ic_dial_clock5),getResources().getDrawable(R.drawable.ic_hour_clock6)
//                    ,getResources().getDrawable(R.drawable.ic_minutes_clock6),getResources().getDrawable(R.drawable.ic_seconds_clock6));
//        } else if (position == 3) {
//            return new AnalogClock1(getResources().getDrawable(R.drawable.ic_dial_clock5),getResources().getDrawable(R.drawable.ic_hour_clock6)
//                    ,getResources().getDrawable(R.drawable.ic_minutes_clock6),null);
//        } else if (position == 4) {
//            return new AnalogClock1(getResources().getDrawable(R.drawable.ic_dial_clock5),getResources().getDrawable(R.drawable.ic_hour_clock6)
//                    ,getResources().getDrawable(R.drawable.ic_minutes_clock6),getResources().getDrawable(R.drawable.ic_seconds_clock6));
//        } else {
//            return null;
//        }
    }

    private class AnalogClock1 extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                try{
                    draw();
                }catch (Exception e){}

            }

        };
        // private List<MyPoint> circles;
        private int mScreenWidth=0;
        private int mScreenHeight=0;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        private boolean isCricleDraw;
        private float mMinute;
        private float mSecond;
        private int mFontSize;
        boolean blink = false;
        int myX = 0, myY = 0;
        int mMonth, date, year;
        int dayofWeek;
        String[] monthsArray = {"", "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] daysArray = {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Saturday"};
        View view;

        private final Handler mHandler = new Handler();
        private float mMinutes;
        private float mHour;
        boolean mSeconds = false;
        private boolean mChanged;
        //  Compass.CompassListener cl;
        // Compass compass;
        //  private float currentAzimuth;
        SaadClock clock;

        public AnalogClock1(Drawable dial,Drawable hour,Drawable minute,Drawable second) {
            Log.d("TAG", "Width: " + width);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(AnalogClockService2.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "40"));
            touchEnabled = prefs.getBoolean("touch", false);
            // circles = new ArrayList<MyPoint>();
            mFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                    getResources().getDisplayMetrics());
            AttributeSet attributeSet = null;
            clock = new SaadClock(AnalogClockService2.this, attributeSet, 0);
            clock.changeItems(dial,hour,minute,second);



            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }

    /*    private Compass.CompassListener getCompassListener() {
            return  new Compass.CompassListener() {
                @Override
                public void onNewAzimuth (float azimuth) {
                    adjustArrow(azimuth);
                }
            };
        }
        private void adjustArrow(float azimuth) {
            Log.d("TAG", "will set rotation from " + currentAzimuth + " to "
                    + azimuth);

            Animation an = new RotateAnimation(-currentAzimuth, -azimuth,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            currentAzimuth = azimuth;

            an.setDuration(500);
            an.setRepeatCount(0);
            an.setFillAfter(true);

            clock.startAnimation(an);
        }*/


        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d("NEWTAG", "onSurfaceChanged: " + format);
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            Log.d("NEWTAG", "onSurfaceDestroyed: " + holder.getSurface());
            this.visible = false;
            handler.removeCallbacks(drawRunner);

        }

        private void draw() {
            mScreenWidth=ClockUtils.Companion.getMobileScreenWidth();
            mScreenHeight=ClockUtils.Companion.getMobileScreenHeight();

            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                   /* if (circles.size() >= maxNumber) {
                        circles.clear();
                    }*/
                    myX += 10;
                    myY += 10;
                    Calendar calendar = Calendar.getInstance();
                    mHour = calendar.get(Calendar.HOUR_OF_DAY);
                    mMonth = Calendar.MONTH;
                    date = calendar.get(Calendar.DAY_OF_MONTH);
                    year = calendar.get(Calendar.YEAR);
                    dayofWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    //convert to 12hour format from 24 hour format
                    mHour = mHour > 12 ? mHour - 12 : mHour;
                    mMinute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);

                    mSecond = 6.0f * second;
                    // mMinutes / 60.0f * 360.0f;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        canvas.drawColor(getColor(R.color.bg_color));
                    }else {
                        canvas.drawColor(Color.BLACK);
                    }
                   /* compass = new Compass(this);
                    compass.start();
                    cl = getCompassListener();
                    compass.setListener(cl);*/

                    //  Log.d("azimuthValue", "draw: "+currentAzimuth);
                    Log.d("Time_Cuurent","Hours "+mHour);
                    Log.d("Time_Cuurent","Minutes "+mMinute);
                    Log.d("Time_Cuurent","Seconds "+mSecond);

                    float mmHour=mHour / 12.0f * 360.0f;
                    float mmMinute=mMinute / 60.0f * 30.0f;
                    float mmmHour=mmHour+mmMinute;
                    Log.d("Time_Cuurent","Angle hour "+(mmHour));
                    Log.d("Time_Cuurent","Angle minute "+(mmMinute));
                    Log.d("Time_Cuurent","Angle hour new"+(mmmHour));

                    Log.d("Time_", "draw: Second" + 6.0f * second + " Mintutes " + (mMinute / 60.0f * 360.0f) + " Hour " + (mHour / 12.0f * 360.0f) + "Coming Hour " + mHour);
                    clock.setTime(6.0f * second, (mmmHour), (mMinute / 60.0f * 360.0f), monthsArray[mMonth], date, year, daysArray[dayofWeek], height, canvas);
                    // clock.onDraw(canvas);

                    Bitmap bitmap = getBitmapFromView(clock);
                    float[] allvalues = new float[9];
                    allvalues[0] = 1.5f;
                    allvalues[1] = 0.0f;
                    allvalues[3] = 0.0f;
                    allvalues[4] = 1.5f;
                    allvalues[2] = 25f;
                    allvalues[5] = (height / 1600) * 395f;
                    allvalues[6] = 0.0f;
                    allvalues[7] = 0.0f;
                    allvalues[8] = 1.0f;
                    Matrix matrix = new Matrix();
                    matrix.setValues(allvalues);

                    float ratioX = mScreenWidth / (float) bitmap.getWidth();
                    float ratioY = mScreenHeight / (float) bitmap.getHeight();
                    float middleX = mScreenWidth / 2.0f;
                    float middleY = mScreenHeight / 2.0f;
                    Matrix scaleMatrix = new Matrix();
                    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
//                    Canvas canvas2 = new Canvas(bitmap);
                    canvas.setMatrix(scaleMatrix);
                    canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, paint);


//                    canvas.drawBitmap(bitmap, matrix, paint);

                    /*drawDial(canvas,mSecond);
                    drawSeconds(canvas,mSecond);*/
                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 1000);
            }
        }

        public Bitmap getBitmapFromView(View view) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            Bitmap bitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight,
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, mScreenWidth, mScreenHeight);
            view.draw(canvas);
            return bitmap;
        }


        public class SaadClock extends View {
            public SaadClock(Context context) {
                super(context);
                // TODO Auto-generated constructor stub
            }

            private Time mCalendar;
            int date, year, mHeight;
            String mMonth, dayofWeek;


            private Drawable mHourHand;
            private Drawable mMinuteHand;
            private Drawable mSecondHand;
            private Drawable mDial;
            private Drawable mBox;

            Paint paint2;
            private int mDialWidth;
            private int mDialHeight;

            private boolean mAttached;

            private final Handler mHandler = new Handler();
            private float mMinutes;
            private float mHour, currentazimuth;
            private boolean mChanged;
            Rect mBounds;
            float mCenterX, mCenterY, mRadius;


            Context mContext;


            public SaadClock(Context context, AttributeSet attrs) {
                this(context, attrs, 0);
            }

            public SaadClock(Context context, AttributeSet attrs,
                             int defStyle) {
                super(context, attrs, defStyle);
                Resources r = context.getResources();
                TypedArray a =
                        context.obtainStyledAttributes(
                                attrs, androidx.appcompat.R.styleable.ActionBar, defStyle, 0);
                mContext = context;
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
                mBox = r.getDrawable(R.drawable.ic_box);
                //    mCompass=r.getDrawable(R.drawable.ic_compass_300);
                //   }

                mCalendar = new Time();
                paint2 = new Paint();
                paint2.setColor(Color.BLACK);
                paint2.setTypeface(ResourcesCompat.getFont(getContext(), R.font.montserrat_semi_bold));

                mDialWidth = mDial.getIntrinsicWidth();
                mDialHeight = mDial.getIntrinsicHeight();
            }
            public void changeItems(Drawable dial,Drawable hour,Drawable minute,Drawable second){
                mDial=dial;
                mHourHand=hour;
                mMinuteHand=minute;
                mSecondHand=second;
            }
            public void setTime(float second, float hour, float minute, String mMonth, int date, int year, String dayofWeek, int mHeight, Canvas canvas) {
                mMinutes = minute;
                mSecond = second;
                mHour = hour;
                this.mMonth = mMonth;
                this.date = date;
                this.year = year;
                this.dayofWeek = dayofWeek;
                this.mHeight = mHeight;

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
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightMode = MeasureSpec.getMode(heightMeasureSpec);
                int heightSize = MeasureSpec.getSize(heightMeasureSpec);

                float hScale = 1.0f;
                float vScale = 1.0f;

                if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
                    hScale = (float) widthSize / (float) mDialWidth;
                }

                if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
                    vScale = (float) heightSize / (float) mDialHeight;
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
                canvas.drawColor(Color.parseColor("#061130"));
                boolean changed = mChanged;
                if (changed) {
                    Log.d("Time_", "Changed: " + changed);
                    mChanged = false;
                }
                boolean seconds = mSeconds;
                if (seconds) {
                    Log.d("Time_", "Seconds: " + changed);
                    mSeconds = false;
                }
                final Drawable box = mBox;
                int box_w = box.getIntrinsicWidth();
                int box_h = box.getIntrinsicHeight();


                int availableWidth = getWidth() / 2;
                int availableHeight = getHeight() / 2;

//                int x = availableWidth / 2;
//                int y = availableHeight / 2;


                /*box.setBounds(x - (box_w / 2), (y - (box_h / 2)) + 90, x + (box_w / 2), y + (box_h / 2) + 90);
                box.draw(canvas);
                float size = (mHeight / 1600f) * 15f;
                paint2.setTextSize(size);
                Log.d("Available_width", "onDraw: " + size + " Height " + getHeight());
                float xval = (mHeight / 1600) * 5;
                float yval_text1 = (mHeight / 1600) * 110;
                float yval_text2 = (mHeight / 1600) * 135;
                canvas.drawText(mMonth + " " + date + " " + year, (x - (box_w / 2)) + xval, (y - (box_h / 2)) + yval_text1, paint2);
                canvas.drawText(dayofWeek, (x - (box_w / 2)) + xval, (y - (box_h / 2)) + yval_text2, paint2);*/
                final Drawable dial = mDial;
                int w = dial.getIntrinsicWidth()*2;
                int h = dial.getIntrinsicHeight()*2;

//                int x = dial.getIntrinsicWidth() / 2;
//                int y = dial.getIntrinsicHeight() / 2;
                int x = 0 ;
                int y = 0;

                boolean scaled = false;
                scaled = true;
                float scale = Math.min((float) (mScreenWidth/2) / (float) mScreenWidth,
                        (float) (mScreenHeight/2) / (float) mScreenHeight);
                canvas.save();
//                    canvas.scale(scale, scale, x, y);
                canvas.scale(0.5f, 0.5f, mScreenWidth, mScreenHeight);

//                if(availableWidth>420) {
//                    x=availableWidth/2+10;
//                    y=availableHeight/2;
//                }
//
//                if (availableWidth < w || availableHeight < h) {
//                    scaled = true;
//                    float scale = Math.min((float) availableWidth / (float) w,
//                            (float) availableHeight / (float) h);
//                    canvas.save();
//                    canvas.scale(scale, scale, x, y);Log.d("Canvas_clock", "availableWidth: " + availableWidth);
//
//                    Log.d("Canvas_clock", "w: " + w);
//                    Log.d("Canvas_clock", "h: " + h);
//                    Log.d("Canvas_clock", "scale: " + scale);
//                    Log.d("Canvas_clock", "x: " + x);
//                    Log.d("Canvas_clock", "y: " + y);
//
//                }
//                else {
////                    w=w/ 4;
////                    h=h/ 4;
////                    x=w/8;
////                    y=y/8;
//
//                    scaled = true;
//                    float scale = Math.min((float) availableWidth / (float) w,
//                            (float) availableHeight / (float) h);
//                    canvas.save();
//                    canvas.scale(scale, scale, x, y);
//
//                    Log.d("Canvas_clock", "new availableWidth: " + availableWidth);
//                    Log.d("Canvas_clock", "new w: " + w);
//                    Log.d("Canvas_clock", "new h: " + h);
//                    Log.d("Canvas_clock", "new scale: " + scale);
//                    Log.d("Canvas_clock", "new x: " + x);
//                    Log.d("Canvas_clock", "new y: " + y);
//                }


                if (changed) {
                    dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
                }
                dial.draw(canvas);

                canvas.save();
                canvas.rotate(mHour, x, y);//(mHour / 12.0f * 360.0f, x, y);
                Log.d("Time_Cuurent","Angle hour canvas"+(mHour));

                final Drawable hourHand = mHourHand;
                if (changed) {
                    w = hourHand.getIntrinsicWidth()*2;
                    h = hourHand.getIntrinsicHeight()*2;
//                    if(availableWidth>420){
//                        w=w/2;
//                        h=h/2;
//                    }
                    hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
                }
                hourHand.draw(canvas);
                canvas.restore();

                canvas.save();
                canvas.rotate(mMinutes, x, y);
                //canvas.rotate(mSecond, x, y);
                final Drawable minuteHand = mMinuteHand;
                if (changed) {
                    w = minuteHand.getIntrinsicWidth()*2;
                    h = minuteHand.getIntrinsicHeight()*2;
//                    if(availableWidth>420){
//                        w=w/2;
//                        h=h/2;
//                    }
                    minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
                }
                minuteHand.draw(canvas);
                canvas.restore();
                canvas.save();
                canvas.rotate(mSecond, x, y);

                //minuteHand = mMinuteHand;
                // if (seconds) {
                if(mSecondHand!=null) {
                    w = mSecondHand.getIntrinsicWidth()*2;
                    h = mSecondHand.getIntrinsicHeight()*2;
//                    if(availableWidth>420){
//                        w=w/2;
//                        h=h/2;
//                    }
                    mSecondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
                    // }
                    mSecondHand.draw(canvas);

                }
                canvas.restore();
                /*final Drawable compass=mCompass;
                int compass_w=compass.getIntrinsicWidth();
                int compass_h=compass.getIntrinsicHeight();



                int availableWidthCompass = getWidth()/2;
                int availableHeightCompass = getHeight()/2;

                int xcompass = availableWidthCompass / 2;
                int ycompass = availableHeightCompass / 2;
                canvas.rotate(currentazimuth, xcompass, ycompass);

                compass.setBounds(xcompass - (compass_w / 2), (ycompass - (compass_h / 2)), xcompass + (compass_w / 2), ycompass + (compass_h / 2));
                compass.draw(canvas);*/
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

                    //mSecond=6.0f*second;
                    Log.d("Time_", "onTick: " + second);
                    mSeconds = true;
                    //mChanged = true;
                    SaadClock.this.invalidate();
                    //Toast.makeText(mContext, "text", Toast.LENGTH_LONG).show();
                }
            }

            boolean mSeconds = false;

            private void onTimeChanged() {
                mCalendar.setToNow();

                int hour = mCalendar.hour;
                int minute = mCalendar.minute;
                int second = mCalendar.second;
                Log.d("Time_", "onTick: " + second);
                mSeconds = true;
                // mMinutes = minute + second / 60.0f;
                // mHour = hour + mMinutes / 60.0f;
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

    }
}
