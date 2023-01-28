package com.neonclock.nightclock.smartclock.digitalclock.activity

import android.app.ActivityManager
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.neonclock.nightclock.smartclock.digitalclock.clock_service.DigitalClockService1
import com.neonclock.nightclock.smartclock.digitalclock.clock_service.DigitalClockService2
import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivityDigitalPreviewBinding
import com.neonclock.nightclock.smartclock.digitalclock.activity.digital.DigitalClock1
import com.neonclock.nightclock.smartclock.digitalclock.activity.digital.DigitalClock2
import com.neonclock.nightclock.smartclock.digitalclock.activity.digital.DigitalClock3
import com.neonclock.nightclock.smartclock.digitalclock.activity.digital.DigitalClock4
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils
import com.vdx.animatedtoast.AnimatedToast

class DigitalPreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDigitalPreviewBinding
    private lateinit var digitalClock1: DigitalClock1
    var classRunning:Int=0
    var digitalClockSelected:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDigitalPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        digitalClockSelected= ClockUtils.clockDigital
        val params: LinearLayout.LayoutParams=
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        when(digitalClockSelected){
            1 -> {
                var view: View = DigitalClock1(this)
                view.layoutParams=params
                binding.digitalClockPreview.addView(view)
            }
            2 -> {
                var view: View = DigitalClock2(this)
                view.layoutParams=params
                binding.digitalClockPreview.addView(view)
            }
            3 -> {
                var view: View = DigitalClock3(this)
                view.layoutParams=params
                binding.digitalClockPreview.addView(view)
            }
            4 -> {
                var view: View = DigitalClock4(this)
                view.layoutParams=params
                binding.digitalClockPreview.addView(view)
            }
            5 -> {
                var view: View = DigitalClock1(this)
                view.layoutParams=params
                binding.digitalClockPreview.addView(view)
            }
            6 -> {
                var view: View = DigitalClock1(this)
                view.layoutParams=params
                binding.digitalClockPreview.addView(view)
            }
        }

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
        if(isMyServiceRunning(DigitalClockService1::class.java)){
            val intent = Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
            )
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@DigitalPreviewActivity, DigitalClockService2::class.java)
            )
            classRunning = 2
            someActivityResultLauncher.launch(intent)
        }else if(isMyServiceRunning(DigitalClockService2::class.java)){
            val intent = Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
            )
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@DigitalPreviewActivity, DigitalClockService1::class.java)
            )
            classRunning = 1
            someActivityResultLauncher.launch(intent)
        }else{
            val intent = Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
            )
            intent.putExtra(
                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                ComponentName(this@DigitalPreviewActivity, DigitalClockService1::class.java)
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
                if (isMyServiceRunning(DigitalClockService2::class.java)) {

                    Log.d("TAG2", " Second is Before")

                    Log.d("TAG2", " First is Running")
                    val intent = Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                    )
                    intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(this@DigitalPreviewActivity, DigitalClockService2::class.java)
                    )
                    stopService(intent)
                } else {
                    Log.d("TAG2", " Nothing is Before")

                    Log.d("TAG2", " First is Running")
                }
            } else if (classRunning == 2) {
                if (isMyServiceRunning(DigitalClockService1::class.java)) {
                    Log.d("TAG2", " First is Before")

                    Log.d("TAG2", " Second is Running")
                    val intent = Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER
                    )
                    intent.putExtra(
                        WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        ComponentName(this@DigitalPreviewActivity, DigitalClockService1::class.java)
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