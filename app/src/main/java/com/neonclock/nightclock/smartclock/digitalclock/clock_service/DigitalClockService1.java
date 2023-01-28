package com.neonclock.nightclock.smartclock.digitalclock.clock_service;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;


import com.neonclock.nightclock.smartclock.digitalclock.R;
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils;


import java.util.Calendar;


public class DigitalClockService1 extends WallpaperService {
    @Override
    public Engine onCreateEngine() {
        int  position= ClockUtils.Companion.getClockDigital();
        if(position==1){
            return new DigitalClock();
        }else  if(position==2){
            return new DigitalClock2();
        }else  if(position==3){
            return new DigitalClock3();
        }else  if(position==4){
            return new DigitalClock4();
        }else  if(position==5){
            return new DigitalClock();
        } else  if(position==6){
            return new DigitalClock();
        }else{
            return null;
        }

    }

    private class DigitalClock extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };
        float radius=0;
       // private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        private boolean isCricleDraw;
        private float mHour;
        private float mMinute;
        private float mSecond;
        private int mFontSize;
        boolean blink = false;
        int myX = 0, myY = 0;
        float x=0,y=0;
        int delaytime=0;
        boolean willRippleDraw=false;
        SharedPreferences spRipple;
        boolean isRipple;

        public DigitalClock() {
            Log.d("TAG", "Width: " + width);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DigitalClockService1.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "40"));
            touchEnabled = prefs.getBoolean("touch", false);
         //   circles = new ArrayList<MyPoint>();
            mFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                    getResources().getDisplayMetrics());

            spRipple= PreferenceManager.getDefaultSharedPreferences(DigitalClockService1.this);
            isRipple=spRipple.getBoolean("isripple",false);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                willRippleDraw=true;
                radius=0;
                x=event.getX();
                y=event.getY();

            }
            Log.d("Error_", "onTouchEvent: "+willRippleDraw);
        }

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
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        private void draw() {
            Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            //convert to 12hour format from 24 hour format
            mHour = mHour > 12 ? mHour - 12 : mHour;
            mMinute = calendar.get(Calendar.MINUTE);
            mSecond = calendar.get(Calendar.SECOND);

            if (blink == false && delaytime>=500) {
                blink = true;

            } else {
                if(delaytime>=1000) {
                    delaytime = 0;
                    blink = false;

                }

            }
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                   /* if (circles.size() >= maxNumber) {
                        circles.clear();
                    }*/

                    if ((myX + myY) < width) {
                        myX++;
                        myY++;
                    } else {
                        myX = 0;
                        myY = 0;
                    }

                    Log.d("TAG", "X: " + myX);
                    Log.d("TAG", "Y: " + myY);
                    /*int x = (int) (width * Math.random());
                    int y = (int) (height * Math.random());
                    Log.d("TAG", "X: "+myX);
                    Log.d("TAG", "Y: "+myY);
                    circles.add(new MyPoint(String.valueOf(circles.size() + 1),
                            x, y));*/
                    // drawCircles(canvas, new MyPoint(String.valueOf(circles.size() + 1),width/2, height/2),myX,myY);

                    // View v= LayoutInflater.from(DigitalClockService.this).inflate(R.layout.activity_analog_clock, null);
                    /*canvas.drawColor(Color.BLACK);
                    Bitmap bitmap=getBitmapFromView(clock);
                    Log.d("TAG", "Y: " + playerW);
                    canvas.drawBitmap(bitmap, width/2, height/2, paint);*/
                    Log.d("Error_", "Before Coming in Ripple: "+willRippleDraw);

                    canvas.drawColor(Color.parseColor("#061130"));

                    setSecond(canvas, mSecond);
                    setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
                    setMinute(canvas, mMinute);
                    setColun(canvas);
                    if (isRipple) {
                        if(willRippleDraw){
//                            drawRipple(canvas,x,y,calendar);
                            Log.d("Error_", "After Coming in Ripple: "+willRippleDraw);
                        }
                    }

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 10);
                delaytime+=10;
            }
        }
        private void drawRipple(Canvas canvas,float x,float y,Calendar calendar) {
            // if (surfaceHolder.getSurface().isValid()) {
            //    Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.parseColor("#2680EB"));
            setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f);
            setSecond(canvas, mSecond);
            setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
            setMinute(canvas, mMinute);
            setColun(canvas);
            canvas.drawCircle(x, y, radius, paint);

            if(radius<= 400F){
                radius+=5F;
            }
            else{
                radius = 0F;
                willRippleDraw=false;
                canvas.drawColor(Color.BLACK);
            }

        }
        public void setHours(Canvas canvas, float location) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 200f);
            int parsevalue = (int) (location / 5);
            int length = String.valueOf(parsevalue).length();
            Log.d("TAGA", "setHour: " + length);
            if (length < 2) {
                canvas.drawText("0" + (int) (location / 5), (width / 2) - 300, height / 2, paint);
            } else {
                canvas.drawText((int) (location / 5) + "", (width / 2) - 300, height / 2, paint);
            }

        }

        public void setColun(Canvas canvas) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.STROKE, 20, 200f);
            canvas.drawText(" ", (width / 2) - 50, (height / 2) - 10, paint);
            if (blink)
                canvas.drawText(":", (width / 2) - 50, (height / 2) - 10, paint);
        }

        public void setMinute(Canvas canvas, float location) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 200f);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            //  Log.d("TAGA", "setMinute: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2) + 20, (height / 2), paint);
            } else {
                canvas.drawText((int) location + "", (width / 2) + 20, (height / 2), paint);
            }
        }

        public void setSecond(Canvas canvas, float location) {
//            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 100f);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            // Log.d("TAGA", "setHour: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2) + 250, (height / 2), paint);
            } else {
                canvas.drawText((int) location + "", (width / 2) + 250, (height / 2), paint);
            }
        }

        private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size) {
            paint.reset();
            paint.setColor(colour);
            paint.setStyle(stroke);
            paint.setStrokeWidth(strokeWidth);
            paint.setAntiAlias(true);
            paint.setTextSize(size);
            paint.setTypeface(ResourcesCompat.getFont(DigitalClockService1.this, R.font.ds_digital));
        }

        // Surface view requires that all elements are drawn completely
        public Bitmap getBitmapFromView(View view) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(canvas);
            return bitmap;
        }
    }

    private class DigitalClock2 extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };
        float radius=0;
      //  private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        private boolean isCricleDraw;
        private float mHour;
        private float mMinute;
        private float mSecond;
        private int mFontSize;
        boolean blink = false;
        int myX = 0, myY = 0;
        float x=0,y=0;
        int delaytime=0;
        boolean willRippleDraw=false;
        String[] daysArray={"","Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
        SharedPreferences spRipple;
        boolean isRipple;

        public DigitalClock2() {
            Log.d("TAG", "Width: " + width);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DigitalClockService1.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "40"));
            touchEnabled = prefs.getBoolean("touch", false);
           // circles = new ArrayList<MyPoint>();
            mFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                    getResources().getDisplayMetrics());

            spRipple= PreferenceManager.getDefaultSharedPreferences(DigitalClockService1.this);
            isRipple=spRipple.getBoolean("isripple",false);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                willRippleDraw=true;
                radius=0;
                x=event.getX();
                y=event.getY();

            }
            Log.d("Error_", "onTouchEvent: "+willRippleDraw);
        }

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
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        private void draw() {
            Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            //convert to 12hour format from 24 hour format
            mHour = mHour > 12 ? mHour - 12 : mHour;
            mMinute = calendar.get(Calendar.MINUTE);
            mSecond = calendar.get(Calendar.SECOND);

            if (blink == false && delaytime>=500) {
                blink = true;

            } else {
                if(delaytime>=1000) {
                    delaytime = 0;
                    blink = false;

                }

            }
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    /*if (circles.size() >= maxNumber) {
                        circles.clear();
                    }*/

                    if ((myX + myY) < width) {
                        myX++;
                        myY++;
                    } else {
                        myX = 0;
                        myY = 0;
                    }

                    Log.d("TAG", "X: " + myX);
                    Log.d("TAG", "Y: " + myY);
                    Log.d("Error_", "Before Coming in Ripple: "+willRippleDraw);

//                    canvas.drawColor(Color.BLACK);
                    canvas.drawColor(Color.parseColor("#061130"));

                    //setSecond(canvas, mSecond);
                    String date=calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1);
                    int day=calendar.get(Calendar.DAY_OF_WEEK);
                    setDay(canvas,daysArray[day]);
                    setDate(canvas,date);
//                    setSecond(canvas, mSecond);
                    setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
                    setMinute(canvas, mMinute);

                    setColun(canvas);
                    if (isRipple) {
                        if(willRippleDraw){
                            drawRipple(canvas,x,y,calendar);
                            Log.d("Error_", "After Coming in Ripple: "+willRippleDraw);
                        }
                    }

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 10);
                delaytime+=10;
            }
        }

        private void setDay(Canvas canvas, String s) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 80f,20);
            canvas.drawText(s,(width/2)-70,750,paint);
        }

        private void setDate(Canvas canvas, String date) {

            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 80f,20);
            canvas.drawText(date,(width/2)-70,400,paint);
        }

        private void drawRipple(Canvas canvas,float x,float y,Calendar calendar) {
            // if (surfaceHolder.getSurface().isValid()) {
            //    Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 100f,50);
            int day=calendar.get(Calendar.DAY_OF_WEEK);
            setDay(canvas,daysArray[day]);
            String date=calendar.get(Calendar.DAY_OF_MONTH)+"/"+Calendar.MONTH;;
            setDate(canvas,date);
//            setSecond(canvas, mSecond);
            setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
            setMinute(canvas, mMinute);
//            setSecond(canvas, mSecond);
            setColun(canvas);
            canvas.drawCircle(x, y, radius, paint);

            if(radius<= 100F){
                radius+=5F;
            }
            else{
                radius = 0F;
                willRippleDraw=false;
                canvas.drawColor(Color.BLACK);
            }

        }
        public void setHours(Canvas canvas, float location) {
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 200f,50);
            int parsevalue = (int) (location / 5);
            int length = String.valueOf(parsevalue).length();
            Log.d("TAGA", "setHour: " + length);
            if (length < 2) {
                canvas.drawText("0" + (int) (location / 5), (width / 2) - 250, 600, paint);
            } else {
                canvas.drawText((int) (location / 5) + "", (width / 2) - 250, 600, paint);
            }

        }

        public void setColun(Canvas canvas) {
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.STROKE, 10, 130f,50);
            canvas.drawText(" ", (width / 2) - 30, 600 - 10, paint);
            if (blink)
                canvas.drawText(":", (width / 2) - 30, 600 - 10, paint);
        }

        public void setMinute(Canvas canvas, float location) {
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 200f,50);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            //  Log.d("TAGA", "setMinute: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2)+20 , 600, paint);
            } else {
                canvas.drawText((int) location + "", (width / 2)+20,600, paint);
            }
        }

        public void setSecond(Canvas canvas, float location) {
            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 100f,50);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            // Log.d("TAGA", "setHour: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2) + 250, (height / 2), paint);
            } else {
                canvas.drawText((int) location + "", (width / 2) + 250, (height / 2), paint);
            }
        }

        private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size,int radius) {
            paint.reset();
            paint.setColor(colour);
            paint.setStyle(stroke);
            paint.setStrokeWidth(strokeWidth);
            paint.setAntiAlias(true);
            paint.setTextSize(size);
//            paint.setShadowLayer(radius,0,0,Color.RED);
            paint.setTypeface(ResourcesCompat.getFont(DigitalClockService1.this, R.font.digital_italic));
        }

        // Surface view requires that all elements are drawn completely
        public Bitmap getBitmapFromView(View view) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(canvas);
            return bitmap;
        }
    }

    private class DigitalClock3 extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };
        float radius=0;
       // private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        private boolean isCricleDraw;
        private float mHour;
        private float mMinute;
        private float mSecond;
        private int mFontSize;
        boolean blink = false;
        int myX = 0, myY = 0;
        float x=0,y=0;
        int delaytime=0;
        int linelength=-20;
        float linex=0,lineTwox=0,lineTwoy=0,liney=0,lineThreex=0,lineThreey=0;
        float lineFourx=0,lineFoury=0,lineFivex=0,lineFivey=0,lastLine=0;
        boolean willRippleDraw=false;
        String[] daysArray={"","Sun","Mon","Tue","Wed","Thur","Fri","Sat"};
        SharedPreferences spRipple;
        boolean isRipple;

        public DigitalClock3() {
            Log.d("TAG", "Width: " + width);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DigitalClockService1.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "40"));
            touchEnabled = prefs.getBoolean("touch", false);
           // circles = new ArrayList<MyPoint>();
            mFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                    getResources().getDisplayMetrics());
            spRipple= PreferenceManager.getDefaultSharedPreferences(DigitalClockService1.this);
            isRipple=spRipple.getBoolean("isripple",false);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                willRippleDraw=true;
                radius=0;
                x=event.getX();
                y=event.getY();

            }
            Log.d("Error_", "onTouchEvent: "+willRippleDraw);
        }

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
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        private void draw() {
            Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            //convert to 12hour format from 24 hour format
            mHour = mHour > 12 ? mHour - 12 : mHour;
            mMinute = calendar.get(Calendar.MINUTE);
            mSecond = calendar.get(Calendar.SECOND);

            if (blink == false && delaytime>=500) {
                blink = true;

            } else {
                if(delaytime>=1000) {
                    delaytime = 0;
                    blink = false;

                }

            }
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                   /* if (circles.size() >= maxNumber) {
                        circles.clear();
                    }*/

                    if ((myX + myY) < width) {
                        myX++;
                        myY++;
                    } else {
                        myX = 0;
                        myY = 0;
                    }

                    Log.d("TAG", "X: " + myX);
                    Log.d("TAG", "Y: " + myY);
                    Log.d("Error_", "Before Coming in Ripple: "+willRippleDraw);

//                    canvas.drawColor(Color.BLACK);
                    canvas.drawColor(Color.parseColor("#061130"));

                    //setSecond(canvas, mSecond);
                    String date=calendar.get(Calendar.DAY_OF_MONTH)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                    int day=calendar.get(Calendar.DAY_OF_WEEK);
                    String zone="none";
                    if(calendar.get(Calendar.AM_PM)==Calendar.PM){
                        zone="PM";
                    }else{
                        zone="AM";
                    }


                    contineousLine(canvas);
                    setZone(canvas,zone);
                    setDay(canvas,daysArray[day]);
                    setDate(canvas,date);
                    setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
                    setMinute(canvas, mMinute);
                    setColun(canvas);
                    if (isRipple) {
                        if(willRippleDraw){
                            drawRipple(canvas,x,y,calendar);
                            Log.d("Error_", "After Coming in Ripple: "+willRippleDraw);
                        }
                    }

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 10);
                delaytime+=10;
            }
        }

        private void contineousLine(Canvas canvas) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 70f,20);
            if(linelength<-50){
                linelength=width;
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
                                        linelength=width;
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
                                    //    canvas.drawLine(linelength,1000,width+50,1000,paint);

                                }else {
                                    lineFivex--;
                                    lineFivey+=5;
                                }

                                canvas.drawLine(lineFivex,lineFivey,lineFourx+1,lineFoury+2,paint);
                                canvas.drawLine(lineFourx,lineFoury,lineThreex+1,lineThreey+2,paint);
                                //  canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                                //   canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                                //   canvas.drawLine(linex,liney,linelength+3,998,paint);
                                //  canvas.drawLine(linelength,1000,width+50,1000,paint);
                            }else {
                                lineFourx--;
                                lineFoury-=5;
                            }

                            canvas.drawLine(lineFourx,lineFoury,lineThreex+1,lineThreey+2,paint);
                            canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                            //  canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                            //   canvas.drawLine(linex,liney,linelength+3,998,paint);
                            // canvas.drawLine(linelength,1000,width+50,1000,paint);
                        }else{
                            lineThreex--;
                            lineThreey+=5;
                        }
                        canvas.drawLine(lineThreex,lineThreey,lineTwox+1,lineTwoy-2,paint);
                        canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                        //  canvas.drawLine(linex,liney,linelength+3,998,paint);
                        //  canvas.drawLine(linelength,1000,width+50,1000,paint);
                    }else {
                        lineTwox--;
                        lineTwoy-=5;
                    }
                    canvas.drawLine(lineTwox,lineTwoy,linex+1,liney-2,paint);
                    canvas.drawLine(linex,liney,linelength+3,998,paint);
                    // canvas.drawLine(linelength,1000,width+50,1000,paint);
                }else{
                    linex--;
                    liney+=5;
                }

                canvas.drawLine(linex,liney,linelength+3,998,paint);
                canvas.drawLine(linelength,1000,width+50,1000,paint);
            }else{
                linelength-=5;
                canvas.drawLine(linelength,1000,width+50,1000,paint);
            }
            Log.d("Line_Lenth", "contineousLine: "+linelength);

        }

        private void setZone(Canvas canvas, String am) {
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 70f,20);
            canvas.drawText(am,(width/2)+250,300,paint);
        }

        private void setDay(Canvas canvas, String s) {
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.FILL, 10, 80f,20);
            canvas.drawText(s,(width/2)-70,620,paint);
        }

        private void setDate(Canvas canvas, String date) {

            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 80f,20);
            canvas.drawText(date,(width/2)-150,500,paint);
        }

        private void drawRipple(Canvas canvas,float x,float y,Calendar calendar) {
            // if (surfaceHolder.getSurface().isValid()) {
            //    Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 100f,50);
            String zone="none";
            if(Calendar.AM_PM==Calendar.PM){
                zone="PM";
            }else{
                zone="AM";
            }
            contineousLine(canvas);
            setZone(canvas,zone);
            int day=calendar.get(Calendar.DAY_OF_WEEK);
            setDay(canvas,daysArray[day]);
            String date=calendar.get(Calendar.DAY_OF_MONTH)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
            setDate(canvas,date);
            // setSecond(canvas, mSecond);
            setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
            setMinute(canvas, mMinute);
            setColun(canvas);
            canvas.drawCircle(x, y, radius, paint);

            if(radius<= 100F){
                radius+=5F;
            }
            else{
                radius = 0F;
                willRippleDraw=false;
                canvas.drawColor(Color.BLACK);
            }

        }
        public void setHours(Canvas canvas, float location) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 250f,50);
            int parsevalue = (int) (location / 5);
            int length = String.valueOf(parsevalue).length();
            Log.d("TAGA", "setHour: " + length);
            if (length < 2) {
                canvas.drawText("0" + (int) (location / 5), (width / 2) - 280, 400, paint);
            } else {
                canvas.drawText((int) (location / 5) + "", (width / 2) - 280, 400, paint);
            }

        }

        public void setColun(Canvas canvas) {
            setPaintAttributes(Color.parseColor("#5CE111"), Paint.Style.STROKE, 10, 150f,50);
            canvas.drawText(" ", (width / 2)-35, 400 - 10, paint);
            if (blink)
                canvas.drawText(":", (width / 2)-35 , 400 - 10, paint);
        }

        public void setMinute(Canvas canvas, float location) {
            setPaintAttributes(Color.parseColor("#2680EB"), Paint.Style.FILL, 10, 250f,50);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            //  Log.d("TAGA", "setMinute: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2) , 400, paint);
            } else {
                canvas.drawText((int) location + "", (width / 2),400, paint);
            }
        }

        public void setSecond(Canvas canvas, float location) {
            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f,50);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            // Log.d("TAGA", "setHour: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2) + 250, (height / 2), paint);
            } else {
                canvas.drawText((int) location + "", (width / 2) + 250, (height / 2), paint);
            }
        }

        private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size,int radius) {
            paint.reset();
            paint.setColor(colour);
            paint.setStyle(stroke);
            paint.setStrokeWidth(strokeWidth);
            paint.setAntiAlias(true);
            paint.setTextSize(size);
//            paint.setShadowLayer(radius,0,0,Color.RED);
            paint.setTypeface(ResourcesCompat.getFont(DigitalClockService1.this, R.font.digital_italic));
        }

        // Surface view requires that all elements are drawn completely
        public Bitmap getBitmapFromView(View view) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(canvas);
            return bitmap;
        }
    }

    private class DigitalClock4 extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };
        float radius=0;
     //   private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        private boolean isCricleDraw;
        private float mHour;
        private float mMinute;
        private float mSecond;
        private int mFontSize;
        boolean blink = false;
        int myX = 0, myY = 0;
        float x=0,y=0;
        int delaytime=0;
        int circleRadius=0,newRadius=70;
        boolean willRippleDraw=false;
        String[] daysArray={"","Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        SharedPreferences spRipple;
        boolean isRipple;

        public DigitalClock4() {
            Log.d("TAG", "Width: " + width);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DigitalClockService1.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "40"));
            touchEnabled = prefs.getBoolean("touch", false);
         //   circles = new ArrayList<MyPoint>();
            mFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                    getResources().getDisplayMetrics());
            spRipple= PreferenceManager.getDefaultSharedPreferences(DigitalClockService1.this);
            isRipple=spRipple.getBoolean("isripple",false);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                willRippleDraw=true;
                radius=0;
                x=event.getX();
                y=event.getY();

            }
            Log.d("Error_", "onTouchEvent: "+willRippleDraw);
        }

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
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        private void draw() {
            Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            //convert to 12hour format from 24 hour format
            mHour = mHour > 12 ? mHour - 12 : mHour;
            mMinute = calendar.get(Calendar.MINUTE);
            mSecond = calendar.get(Calendar.SECOND);

            if (blink == false && delaytime>=500) {
                blink = true;

            } else {
                if(delaytime>=1000) {
                    delaytime = 0;
                    blink = false;

                }

            }
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                   /* if (circles.size() >= maxNumber) {
                        circles.clear();
                    }*/

                    if ((myX + myY) < width) {
                        myX++;
                        myY++;
                    } else {
                        myX = 0;
                        myY = 0;
                    }

                    Log.d("TAG", "X: " + myX);
                    Log.d("TAG", "Y: " + myY);
                    Log.d("Error_", "Before Coming in Ripple: "+willRippleDraw);

//                    canvas.drawColor(Color.BLACK);
                    canvas.drawColor(Color.parseColor("#061130"));

                    //setSecond(canvas, mSecond);
                    String date=calendar.get(Calendar.DAY_OF_MONTH)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
                    int day=calendar.get(Calendar.DAY_OF_WEEK);
                    String zone="none";
                    if(calendar.get(Calendar.AM_PM)==Calendar.PM){
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
                    setCircle(canvas,finalRadius);
                    setRectangle(canvas,finalRadius);
                    // setZone(canvas,zone);
                    setDay(canvas,daysArray[day]);
                    setDate(canvas,date);
                    setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
                    setMinute(canvas, mMinute);
                    setColun(canvas);
                    if (isRipple) {
                        if(willRippleDraw){
//                            drawRipple(canvas,x,y,calendar);
                            Log.d("Error_", "After Coming in Ripple: "+willRippleDraw);
                        }
                    }

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 10);
                delaytime+=10;
            }
        }


        private void setRectangle(Canvas canvas, int finalRadius) {
            String color="#5CF8F8";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, 12, 100f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawRoundRect(new RectF((width/2)+220, 570, (width/2)-220, 370), 10, 6, paint);
        }

        private void setCircle(Canvas canvas,int circleRadius) {
            String color="#5CF8F8";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (height/1600)*15, (height/1600)*100f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle((width/2),(height/1600)*480f,(height/1600)*300,paint);
        }


        private void setZone(Canvas canvas, String am) {
            // setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 70f,20);
            canvas.drawText(am,(width/2)+250,300,paint);
        }

        private void setDay(Canvas canvas, String s) {
            String color="#5CF8F8";
            setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 40f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawText(s,(width/2)-50,350,paint);
        }

        private void setDate(Canvas canvas, String date) {
            String color="#5CF8F8";
            setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 40f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawText(date,(width/2)-70,620,paint);
        }

        private void drawRipple(Canvas canvas,float x,float y,Calendar calendar) {
            // if (surfaceHolder.getSurface().isValid()) {
            //    Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f,50,Color.RED, R.font.digital_italic);
            String zone="none";
            if(Calendar.AM_PM==Calendar.PM){
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
            int day=calendar.get(Calendar.DAY_OF_WEEK);
            setDay(canvas,daysArray[day]);
            String date=calendar.get(Calendar.DAY_OF_MONTH)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.YEAR);
            setDate(canvas,date);
            // setSecond(canvas, mSecond);
            setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
            setMinute(canvas, mMinute);
            setColun(canvas);
            canvas.drawCircle(x, y, radius, paint);

            if(radius<= 100F){
                radius+=5F;
            }
            else{
                radius = 0F;
                willRippleDraw=false;
                canvas.drawColor(Color.BLACK);
            }

        }
        public void setHours(Canvas canvas, float location) {
            String shadowcolor="#5CF8F8";
            String color="#ffffff";
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 150f,20,Color.parseColor(shadowcolor), R.font.digital_italic);
            int parsevalue = (int) (location / 5);
            int length = String.valueOf(parsevalue).length();
            Log.d("TAGA", "setHour: " + length);
            if (length < 2) {
                canvas.drawText("0" + (int) (location / 5), (width / 2) - 190, 520, paint);
            } else {
                canvas.drawText((int) (location / 5) + "", (width / 2) - 190, 520, paint);
            }

        }

        public void setColun(Canvas canvas) {
            String shadowcolor="#5CF8F8";
            String color="#ffffff";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, 10, 100f,20,Color.parseColor(shadowcolor), R.font.digital_italic);
            canvas.drawText(" ", (width / 2)-15, 510 - 10, paint);
            if (blink)
                canvas.drawText(":", (width / 2)-15 , 510 - 10, paint);
        }

        public void setMinute(Canvas canvas, float location) {
            String shadowcolor="#5CF8F8";
            String color="#ffffff";
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 150f,20,Color.parseColor(shadowcolor), R.font.digital_italic);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            //  Log.d("TAGA", "setMinute: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2)+50 , 520, paint);
            } else {
                canvas.drawText((int) location + "", (width / 2)+50,520, paint);
            }
        }

      /*  public void setSecond(Canvas canvas, float location) {
            canvas.drawColor(Color.BLACK);
            setPaintAttributes(Color.RED, Paint.Style.FILL, 10, 100f,50);
            int parsevalue = (int) location;
            int length = String.valueOf(parsevalue).length();
            // Log.d("TAGA", "setHour: "+length);
            if (length < 2) {
                canvas.drawText("0" + (int) location, (width / 2) + 250, (height / 2), paint);
            } else {
                canvas.drawText((int) location + "", (width / 2) + 250, (height / 2), paint);
            }
        }*/

        private void setPaintAttributes(int colour, Paint.Style stroke, int strokeWidth, float size,int radius,int shadowcolor,int font) {
            paint.reset();
            paint.setColor(colour);
            paint.setStyle(stroke);
            paint.setStrokeWidth(strokeWidth);
            paint.setAntiAlias(true);
            paint.setTextSize(size);
            paint.setShadowLayer(radius,0,0,shadowcolor);
            paint.setTypeface(ResourcesCompat.getFont(DigitalClockService1.this, font));
        }

        // Surface view requires that all elements are drawn completely
        public Bitmap getBitmapFromView(View view) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(canvas);
            return bitmap;
        }
    }

    private class DigitalClock5 extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }

        };
        float radius=0;
       // private List<MyPoint> circles;
        private Paint paint = new Paint();
        private int width;
        int height;
        private boolean visible = true;
        private int maxNumber;
        private boolean touchEnabled;
        private boolean isCricleDraw;
        private float mHour;
        private float mMinute;
        private float mSecond;
        private int mFontSize;
        boolean blink = false;
        int myX = 0, myY = 0;
        float x=0,y=0;
        int delaytime=0;
        int circleRadius=0,newRadius=70;
        boolean willRippleDraw=false;
        private double angle = 300;
        private double angle_red = 300;
        private double angle_yellow = 300;
        private float mUserPicBorderCenterX,redBorderCenterX,yellowBorderCenterX;
        private float mUserPicBorderCenterY,redBorderCenterY,yellowBorderCenterY;
        private float mUserPicCenterX,redPicCenterX,yellowPicCenterX;
        private float mUserPicCenterY,redPicCenterY,yellowPicCenterY;
        private int mBorderRadius,redRadius,yellowRadius;
        String[] daysArray={"","Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        SharedPreferences spRipple;
        boolean isRipple;

        public DigitalClock5() {
            Log.d("TAG", "Width: " + width);
            SharedPreferences prefs = PreferenceManager
                    .getDefaultSharedPreferences(DigitalClockService1.this);
            maxNumber = Integer
                    .valueOf(prefs.getString("numberOfCircles", "40"));
            touchEnabled = prefs.getBoolean("touch", false);
          //  circles = new ArrayList<MyPoint>();
            // mUserPicCenterX=((width/2)+540);
            mBorderRadius=(int)angle;
            redRadius=(int)angle_red;
            yellowRadius=(int)angle_yellow;
            mFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                    getResources().getDisplayMetrics());
            spRipple= PreferenceManager.getDefaultSharedPreferences(DigitalClockService1.this);
            isRipple=spRipple.getBoolean("isripple",true);
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(10f);
            handler.post(drawRunner);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                willRippleDraw=true;
                radius=0;
                x=event.getX();
                y=event.getY();

            }
            Log.d("Error_", "onTouchEvent: "+willRippleDraw);
        }

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
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        private void draw() {
            Calendar calendar = Calendar.getInstance();
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            //convert to 12hour format from 24 hour format
            mHour = mHour > 12 ? mHour - 12 : mHour;
            mMinute = calendar.get(Calendar.MINUTE);
            mSecond = calendar.get(Calendar.SECOND);

            if (blink == false && delaytime>=500) {
                blink = true;

            } else {
                if(delaytime>=1000) {
                    delaytime = 0;
                    blink = false;

                }

            }
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                /*    if (circles.size() >= maxNumber) {
                        circles.clear();
                    }*/

                    if ((myX + myY) < width) {
                        myX++;
                        myY++;
                    } else {
                        myX = 0;
                        myY = 0;
                    }

                    Log.d("TAG", "X: " + myX);
                    Log.d("TAG", "Y: " + myY);
                    Log.d("Error_", "Before Coming in Ripple: "+willRippleDraw);

//                    canvas.drawColor(Color.BLACK);
                    canvas.drawColor(Color.parseColor("#061130"));

                    //setSecond(canvas, mSecond);
                    String date=calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
                    int day=calendar.get(Calendar.DAY_OF_WEEK);
                    String zone="none";
                    if(calendar.get(Calendar.AM_PM)==Calendar.PM){
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
                    mUserPicCenterX=((width/2));
                    mUserPicCenterY=480;
                    redPicCenterX=(width/2);
                    redPicCenterY=480;
                    yellowPicCenterX=(width/2);
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
                    setHours(canvas, (int) ((mHour + calendar.get(Calendar.MINUTE) / 60) * 5f));
                    setMinute(canvas, mMinute);
                    setSecond(canvas, mSecond);
                    //  setColun(canvas);
                    if (isRipple) {
                        if(willRippleDraw){
                            //  drawRipple(canvas,x,y,calendar);
                            Log.d("Error_", "After Coming in Ripple: "+willRippleDraw);
                        }
                    }

                }
            } finally {
                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
            handler.removeCallbacks(drawRunner);
            if (visible) {
                handler.postDelayed(drawRunner, 70);
                delaytime+=70;
            }
        }

        private void drawRedcircle(Canvas canvas, int finalRadius) {
            String color="#FA4D4D";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (height/1600)*7, (height/1600)*80f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle((width/2)-110, (height/1600)*390,(height/1600)*140,paint);

        }
        private void drawBluecircle(Canvas canvas, int finalRadius) {
            String color="#7E7AFF";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (height/1600)*7, (height/1600)*80f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle((width/2)+155, (height/1600)*500,(height/1600)*125,paint);

        }
        private void drawYellowcircle(Canvas canvas, int finalRadius) {
            String color="#ECF062";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (height/1600)*7, (height/1600)*80f,finalRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle((width/2)-45, (height/1600)*650,(height/1600)*108,paint);

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
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, (height/1600)*5, (height/1600)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle(mUserPicBorderCenterX, mUserPicBorderCenterY,(height/1600)*25,paint);
            // Log.d("Values_", "Circle Value X: "+blueCircleX+ " Y "+blueCircleY);
        }
        private void fillCircleRed(Canvas canvas, int circleRadius,String color) {
            moveRed();
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, (height/1600)*5, (height/1600)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle(redBorderCenterX, redBorderCenterY,(height/1600)*25,paint);
            //  Log.d("Values_", "Circle Value X: "+blueCircleX+ " Y "+blueCircleY);
        }
        private void fillCircleYellow(Canvas canvas, int circleRadius,String color) {
            moveYellow();
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, (height/1600)*5, (height/1600)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawCircle(yellowBorderCenterX, yellowBorderCenterY,(height/1600)*25,paint);
            //  Log.d("Values_", "Circle Value X: "+blueCircleX+ " Y "+blueCircleY);
        }
        private void setCircle(Canvas canvas,int circleRadius) {
            String color="#5CF8F8";
            setPaintAttributes(Color.parseColor(color), Paint.Style.STROKE, (height/1600)*5, (height/1600)*80f,circleRadius,Color.parseColor(color), R.font.montserrat_semi_bold);
            setShader_();
            canvas.drawCircle((width/2),(height/1600)*480f,(height/1600)*300,paint);
        }
        private void setDay(Canvas canvas, String s) {
            String color="#ffffff";
            setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 25f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawText(s,(width/2)-245, (height/1600)*550,paint);
        }
        private void setDate(Canvas canvas, String date) {
            String color="#ffffff";
            setPaintAttributes(Color.parseColor("#ffffff"), Paint.Style.FILL, 10, 25f,20,Color.parseColor(color), R.font.montserrat_semi_bold);
            canvas.drawText(date,(width/2)+90, (height/1600)*340,paint);
        }
        public void setHours(Canvas canvas, float location) {
            String shadowcolor="#FA4D4D";
            String color="#FA4D4D";
            setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 160f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
            int parsevalue = (int) (location / 5);
            int length = String.valueOf(parsevalue).length();
            Log.d("TAGA", "setHour: " + length);
            if (length < 2) {
                canvas.drawText("0" + (int) (location / 5), (width/2)-220, (height/1600)*430, paint);
                setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
                canvas.drawText("Hours", (width/2)-155, (height/1600)*470, paint);
            } else {
                canvas.drawText((int) (location / 5) + "", (width/2)-220, (height/1600)*430, paint);
                setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
                canvas.drawText("Hours", (width/2)-155, (height/1600)*470, paint);
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
                canvas.drawText("0" + (int) location, (width/2)+75, (height/1600)*540, paint);
                setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
                canvas.drawText("Minutes", (width/2)+75, (height/1600)*580, paint);
            } else {
                canvas.drawText((int) location + "", (width/2)+85, (height/1600)*540, paint);
                setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 30f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
                canvas.drawText("Minutes", (width/2)+85, (height/1600)*580, paint);
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
                canvas.drawText("0" + (int) location, (width/2)-110, (height/1600)*690, paint);
                setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 25f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
                canvas.drawText("Seconds", (width/2)-105, (height/1600)*730, paint);
            } else {
                canvas.drawText((int) location + "", (width/2)-110, (height/1600)*690, paint);
                setPaintAttributes(Color.parseColor(color), Paint.Style.FILL, 10, 25f,20,Color.parseColor(shadowcolor), R.font.montserrat_semi_bold);
                canvas.drawText("Seconds", (width/2)-105, (height/1600)*730, paint);
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
            paint.setTypeface(ResourcesCompat.getFont(DigitalClockService1.this, font));
        }
        public void setShader_(){
            float[] pos=null;
            int[] colors={Color.parseColor("#E4BF86"),Color.parseColor("#C167E1"),Color.parseColor("#EC6288")};
            paint.setShader(new LinearGradient(0f, 0f, (float)width, 0f,colors, pos, Shader.TileMode.MIRROR));
        }

        // Surface view requires that all elements are drawn completely
        public Bitmap getBitmapFromView(View view) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            view.draw(canvas);
            return bitmap;
        }
    }

}
