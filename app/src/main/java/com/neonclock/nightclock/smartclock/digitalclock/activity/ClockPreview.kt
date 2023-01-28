package com.neonclock.nightclock.smartclock.digitalclock.activity

import android.app.ActivityManager
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.neonclock.nightclock.smartclock.digitalclock.clock_service.AnalogClockService1
import com.neonclock.nightclock.smartclock.digitalclock.clock_service.AnalogClockService2
import com.neonclock.nightclock.smartclock.digitalclock.R
import com.neonclock.nightclock.smartclock.digitalclock.activity.analog.AnalogClock1
import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivityClockPreviewBinding
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.clockAssetModel
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.getClockAssetDrawable
import com.vdx.animatedtoast.AnimatedToast

class ClockPreview : AppCompatActivity() {
    private lateinit var binding: ActivityClockPreviewBinding
    private lateinit var clock: AnalogClock1
    var classRunning:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityClockPreviewBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(binding.root)
        clock=binding.analogClockPreview
        clock.background=null
        var mDial = getClockAssetDrawable(applicationContext, "Clocks/Analog Clock/dial_2.webp")
        val clockAssetModel = clockAssetModel!!

        val mHour = getClockAssetDrawable(applicationContext, clockAssetModel!!.mHour)
        val mMinute = getClockAssetDrawable(applicationContext, clockAssetModel!!.mMinute)
        val mSecond = getClockAssetDrawable(applicationContext, clockAssetModel!!.mSecond)
try {
    mDial = getClockAssetDrawable(applicationContext, clockAssetModel!!.mDial)
}catch (e:Exception){}
        clock.changeItems(mDial, mHour,mMinute,mSecond,getDrawable(R.drawable.ic_box))

        binding.setWallpaperBtn.setOnClickListener {
            try {
                setWallpaper()
            }catch (e:Exception){
                Toast.makeText(this,"Please try again, something went wrong!",Toast.LENGTH_SHORT).show()
            }

        }
        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.cancelBtn.setOnClickListener { onBackPressed() }
    }

    private fun setWallpaper() {
        if(isMyServiceRunning(AnalogClockService1::class.java)){
            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@ClockPreview, AnalogClockService2::class.java)
            )
            classRunning = 2
            someActivityResultLauncher.launch(intent)
        }else if(isMyServiceRunning(AnalogClockService2::class.java)){
            val intent = Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
            )
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@ClockPreview, AnalogClockService1::class.java)
            )
            classRunning = 1
            someActivityResultLauncher.launch(intent)
        }else{
            val intent = Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
            )
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@ClockPreview, AnalogClockService1::class.java)
            )
            classRunning = 1
            someActivityResultLauncher.launch(intent)
        }
    }
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
    var someActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            if (classRunning == 1) {
                if (isMyServiceRunning(AnalogClockService2::class.java)) {

                    Log.d("TAG2", " Second is Before")

                    Log.d("TAG2", " First is Running")
                    val intent = Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                    )
                    intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(this@ClockPreview, AnalogClockService2::class.java)
                    )
                    stopService(intent)
                } else {
                    Log.d("TAG2", " Nothing is Before")

                    Log.d("TAG2", " First is Running")
                }
            } else if (classRunning == 2) {
                if (isMyServiceRunning(AnalogClockService1::class.java)) {
                    Log.d("TAG2", " First is Before")

                    Log.d("TAG2", " Second is Running")
                    val intent = Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                    )
                    intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(this@ClockPreview, AnalogClockService1::class.java)
                    )
                    stopService(intent)
                } else {
                    Log.d("TAG2", " Nothing is Before")

                    Log.d("TAG2", " Second is Running")
                }
            }
            AnimatedToast.Success(
                this, "Success", "Wallpaper has been set", Gravity.TOP, Toast.LENGTH_LONG,
                AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE
            )
        } else {
            Log.d("TAG2", " We are here 2")
        }
    }
}