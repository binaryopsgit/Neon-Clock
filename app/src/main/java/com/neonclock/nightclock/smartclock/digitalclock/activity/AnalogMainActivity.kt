package com.neonclock.nightclock.smartclock.digitalclock.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.neonclock.nightclock.smartclock.digitalclock.R
import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivityAnalogMainBinding
import com.neonclock.nightclock.smartclock.digitalclock.model.ClockAssetModel
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils

class AnalogMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalogMainBinding
    private lateinit var clockThumbPreview: ImageView
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAnalogMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAnalogClock(1)
        clockThumbPreview=binding.clockPreviewImg
        binding.clock1.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_analog_clock_thumb_1),1)
        }
        binding.clock2.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_analog_clock_thumb_2),2)
        }
        binding.clock3.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_analog_clock_thumb_3),3)
        }
        binding.clock4.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_analog_clock_thumb_4),4)
        }
        binding.clock5.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_analog_clock_thumb_5),5)
        }
        binding.clock6.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_analog_clock_thumb_6),6)
        }
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, ClockPreview::class.java))
        }
    }
    private fun setImageToClockPreview(imgDrawable: Drawable?, clockPosition:Int) {
        clockThumbPreview.setImageDrawable(imgDrawable)
        setAnalogClock(clockPosition)
    }

    private fun setAnalogClock(clockPosition:Int,clockCat:String="Analog_Clock") {
        val mDial= ClockUtils.ANALOG_CLOCKS_DIAL[clockPosition-1]
        val mHour= ClockUtils.ANALOG_CLOCKS_HOUR[clockPosition-1]
        val mMinute= ClockUtils.ANALOG_CLOCKS_MINUTE[clockPosition-1]
        val mSecond= ClockUtils.ANALOG_CLOCKS_SECOND[clockPosition-1]
        var model: ClockAssetModel = ClockAssetModel(mDial,mHour,mMinute,mSecond)
        ClockUtils.clockAssetModel =model
    }

}