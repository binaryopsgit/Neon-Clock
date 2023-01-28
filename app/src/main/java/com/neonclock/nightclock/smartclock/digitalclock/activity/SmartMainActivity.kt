package com.neonclock.nightclock.smartclock.digitalclock.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.neonclock.nightclock.smartclock.digitalclock.R

import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivitySmartMainBinding
import com.neonclock.nightclock.smartclock.digitalclock.model.ClockAssetModel
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.SMART_CLOCKS_DIAL
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.SMART_CLOCKS_HOUR
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.SMART_CLOCKS_MINUTE
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.SMART_CLOCKS_SECOND
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils.Companion.clockAssetModel

class SmartMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySmartMainBinding
    private lateinit var clockThumbPreview: ImageView
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySmartMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSmartClock(1)
        clockThumbPreview=binding.clockPreviewImg

        binding.clock1.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_smart_clock_thumb_1),1)
        }
        binding.clock2.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_smart_clock_thumb_2),2)
        }
        binding.clock3.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_smart_clock_thumb_3),3)
        }
        binding.clock4.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_smart_clock_thumb_4),4)
        }
        binding.clock5.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_smart_clock_thumb_5),5)
        }
        binding.clock6.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_smart_clock_thumb_6),6)
        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.nextBtn.setOnClickListener {
            setClockAssetModel()
            startActivity(Intent(this, ClockPreview::class.java))
        }
    }

    private fun setClockAssetModel() {

    }

    private fun setImageToClockPreview(imgDrawable: Drawable?,clockPosition:Int) {
        clockThumbPreview.setImageDrawable(imgDrawable)
        setSmartClock(clockPosition)
    }

    private fun setSmartClock(clockPosition:Int,clockCat:String="Smart_Clock") {
        val mDial= SMART_CLOCKS_DIAL[clockPosition-1]
        val mHour= SMART_CLOCKS_HOUR[clockPosition-1]
        val mMinute= SMART_CLOCKS_MINUTE[clockPosition-1]
        val mSecond= SMART_CLOCKS_SECOND[clockPosition-1]
        var model: ClockAssetModel = ClockAssetModel(mDial,mHour,mMinute,mSecond)
        clockAssetModel=model
    }

}