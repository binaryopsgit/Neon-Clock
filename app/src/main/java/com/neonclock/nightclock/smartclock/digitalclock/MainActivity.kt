package com.neonclock.nightclock.smartclock.digitalclock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.neonclock.nightclock.smartclock.digitalclock.databinding.ActivityMainBinding
import com.neonclock.nightclock.smartclock.digitalclock.activity.*
import com.vdx.animatedtoast.AnimatedToast

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.smartClockBtn.setOnClickListener {
            startActivity(Intent(this, SmartMainActivity::class.java))
        }

        binding.analogClockBtn.setOnClickListener {
            startActivity(Intent(this, AnalogMainActivity::class.java))
        }

        binding.animatedClockBtn.setOnClickListener {
            AnimatedToast.Success(
                this, "Animated Clock", "Not yet Implemented!", Gravity.BOTTOM, Toast.LENGTH_LONG,
                AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_BLINK
            )
        }

        binding.ledClockBtn.setOnClickListener {
            startActivity(Intent(this, LEDMainActivity::class.java))
        }

        binding.digitalClockBtn.setOnClickListener {
            startActivity(Intent(this, DigitalMainActivity::class.java))

        }

        binding.kidsClockBtn.setOnClickListener {
            startActivity(Intent(this, KidsMainActivity::class.java))
        }

        binding.girlsClockBtn.setOnClickListener {
            startActivity(Intent(this, GirlMainActivity::class.java))
        }

    }
}