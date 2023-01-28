package com.neonclock.nightclock.smartclock.digitalclock.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.neonclock.nightclock.smartclock.digitalclock.R
import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivityDigitalMainBinding
import com.neonclock.nightclock.smartclock.digitalclock.util.ClockUtils

class DigitalMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDigitalMainBinding
    private lateinit var clockThumbPreview: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDigitalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDigitalClock(1)
        clockThumbPreview=binding.clockPreviewImg

        binding.clock1.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_digital_clock_thumb_1),1)
        }
        binding.clock2.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_digital_clock_thumb_2),2)
        }
        binding.clock3.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_digital_clock_thumb_3),3)
        }
        binding.clock4.setOnClickListener {
            setImageToClockPreview(getDrawable(R.drawable.ic_digital_clock_thumb_4),4)
        }

//        binding.clock5.setOnClickListener {
//            setImageToClockPreview(getDrawable(R.drawable.ic_digital_clock_thumb_1),5)
//        }
//        binding.clock6.setOnClickListener {
//            setImageToClockPreview(getDrawable(R.drawable.ic_digital_clock_thumb_1),6)
//        }

        binding.backBtn.setOnClickListener { onBackPressed() }
        binding.nextBtn.setOnClickListener { startActivity(Intent(this, DigitalPreviewActivity::class.java)) }
    }
    private fun setImageToClockPreview(imgDrawable: Drawable?, clockPosition:Int) {
        clockThumbPreview.setImageDrawable(imgDrawable)
        setDigitalClock(clockPosition)
    }

    private fun setDigitalClock(clockPosition:Int,clockCat:String="DIGITAL_Clock") {
        ClockUtils.clockDigital =clockPosition
    }
}