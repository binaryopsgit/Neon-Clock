package com.neonclock.nightclock.smartclock.digitalclock

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivitySplashBinding
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.mobileScreenHeight
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.mobileScreenWidth


class Splash : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val displayMetrics = DisplayMetrics()

        windowManager.defaultDisplay.getMetrics(displayMetrics)
//        pxToDp(displayMetrics.widthPixels)

//        mobileScreenWidth = pxToDp(displayMetrics.widthPixels)
//        mobileScreenHeight = pxToDp(displayMetrics.heightPixels)
        mobileScreenWidth = displayMetrics.widthPixels
        mobileScreenHeight = displayMetrics.heightPixels

        binding.getStarted.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


//        var Measuredwidth = 0
//        var Measuredheight = 0
//        val size = Point()
//        val w = windowManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            w.defaultDisplay.getSize(size)
//            Measuredwidth = size.x
//            Measuredheight = size.y
//        } else {
//            val d: Display = w.defaultDisplay
//            Measuredwidth = d.getWidth()
//            Measuredheight = d.getHeight()
//        }
//        Toast.makeText(this,"Width : $mobileScreenWidth , Height : $mobileScreenHeight",Toast.LENGTH_LONG).show()

    }
    fun Context.pxToDp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }
    override fun onBackPressed() {

    }
}