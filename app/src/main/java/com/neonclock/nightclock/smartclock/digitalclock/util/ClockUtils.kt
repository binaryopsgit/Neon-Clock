package com.neonclock.nightclock.smartclock.digitalclock.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.neonclock.nightclock.smartclock.digitalclock.model.ClockAssetModel
import java.io.IOException
import java.io.InputStream


class ClockUtils {
    companion object{
        var mobileScreenWidth : Int=400
        var mobileScreenHeight : Int=600
        var clockAssetModel: ClockAssetModel?=null
        var clockDigital:Int=1
        //Analog Clock
        val ANALOG_CLOCKS_DIAL = listOf(
            "Clocks/Analog Clock/dial_1.webp",
            "Clocks/Analog Clock/dial_2.webp",
            "Clocks/Analog Clock/dial_3.webp",
            "Clocks/Analog Clock/dial_4.webp",
            "Clocks/Analog Clock/dial_5.webp",
            "Clocks/Analog Clock/dial_6.webp"
        )
        val ANALOG_CLOCKS_HOUR = listOf(
            "Clocks/Analog Clock/hour_1.webp",
            "Clocks/Analog Clock/hour_2.webp",
            "Clocks/Analog Clock/hour_3.webp",
            "Clocks/Analog Clock/hour_4.webp",
            "Clocks/Analog Clock/hour_5.webp",
            "Clocks/Analog Clock/hour_6.webp"
        )
        val ANALOG_CLOCKS_MINUTE = listOf(
            "Clocks/Analog Clock/minute_1.webp",
            "Clocks/Analog Clock/minute_2.webp",
            "Clocks/Analog Clock/minute_3.webp",
            "Clocks/Analog Clock/minute_4.webp",
            "Clocks/Analog Clock/minute_5.webp",
            "Clocks/Analog Clock/minute_6.webp"
        )
        val ANALOG_CLOCKS_SECOND = listOf(
            "Clocks/Analog Clock/second_1.webp",
            "Clocks/Analog Clock/second_2.webp",
            "Clocks/Analog Clock/second_3.webp",
            "Clocks/Analog Clock/second_4.webp",
            "Clocks/Analog Clock/second_5.webp",
            "Clocks/Analog Clock/second_6.webp"
        )

        //Animated Clock
        val ANIMATED_CLOCKS_DIAL = listOf(
            "Clocks/Animated Clock/dial_1.webp",
            "Clocks/Animated Clock/dial_2.webp",
            "Clocks/Animated Clock/dial_3.webp",
            "Clocks/Animated Clock/dial_4.webp",
            "Clocks/Animated Clock/dial_5.webp",
            "Clocks/Animated Clock/dial_6.webp"
        )
        val ANIMATED_CLOCKS_HOUR = listOf(
            "Clocks/Animated Clock/hour_1.webp",
            "Clocks/Animated Clock/hour_2.webp",
            "Clocks/Animated Clock/hour_3.webp",
            "Clocks/Animated Clock/hour_4.webp",
            "Clocks/Animated Clock/hour_5.webp",
            "Clocks/Animated Clock/hour_6.webp"
        )
        val ANIMATED_CLOCKS_MINUTE = listOf(
            "Clocks/Animated Clock/minute_1.webp",
            "Clocks/Animated Clock/minute_2.webp",
            "Clocks/Animated Clock/minute_3.webp",
            "Clocks/Animated Clock/minute_4.webp",
            "Clocks/Animated Clock/minute_5.webp",
            "Clocks/Animated Clock/minute_6.webp"
        )
        val ANIMATED_CLOCKS_SECOND = listOf(
            "Clocks/Animated Clock/second_1.webp",
            "Clocks/Animated Clock/second_2.webp",
            "Clocks/Animated Clock/second_3.webp",
            "Clocks/Animated Clock/second_4.webp",
            "Clocks/Animated Clock/second_5.webp",
            "Clocks/Animated Clock/second_6.webp"
        )

        //Kid Clock
        val KID_CLOCKS_DIAL = listOf(
            "Clocks/Kids Clock/dial_1.webp",
            "Clocks/Kids Clock/dial_2.webp",
            "Clocks/Kids Clock/dial_3.webp",
            "Clocks/Kids Clock/dial_4.webp",
            "Clocks/Kids Clock/dial_5.webp",
            "Clocks/Kids Clock/dial_6.webp"
        )
        val KID_CLOCKS_HOUR = listOf(
            "Clocks/Kids Clock/hour_1.webp",
            "Clocks/Kids Clock/hour_2.webp",
            "Clocks/Kids Clock/hour_3.webp",
            "Clocks/Kids Clock/hour_4.webp",
            "Clocks/Kids Clock/hour_5.webp",
            "Clocks/Kids Clock/hour_6.webp"
        )
        val KID_CLOCKS_MINUTE = listOf(
            "Clocks/Kids Clock/minute_1.webp",
            "Clocks/Kids Clock/minute_2.webp",
            "Clocks/Kids Clock/minute_3.webp",
            "Clocks/Kids Clock/minute_4.webp",
            "Clocks/Kids Clock/minute_5.webp",
            "Clocks/Kids Clock/minute_6.webp"
        )
        val KID_CLOCKS_SECOND = listOf(
            "Clocks/Kids Clock/second_1.webp",
            "Clocks/Kids Clock/second_2.webp",
            "Clocks/Kids Clock/second_3.webp",
            "Clocks/Kids Clock/second_4.webp",
            "Clocks/Kids Clock/second_5.webp",
            "Clocks/Kids Clock/second_6.webp"
        )

        //Smart Clock
        val SMART_CLOCKS_DIAL = listOf(
            "Clocks/Smart Clock/dial_1.webp",
            "Clocks/Smart Clock/dial_2.webp",
            "Clocks/Smart Clock/dial_3.webp",
            "Clocks/Smart Clock/dial_4.webp",
            "Clocks/Smart Clock/dial_5.webp",
            "Clocks/Smart Clock/dial_6.webp"
        )
        val SMART_CLOCKS_HOUR = listOf(
            "Clocks/Smart Clock/hour_1.webp",
            "Clocks/Smart Clock/hour_2.webp",
            "Clocks/Smart Clock/hour_3.webp",
            "Clocks/Smart Clock/hour_4.webp",
            "Clocks/Smart Clock/hour_5.webp",
            "Clocks/Smart Clock/hour_6.webp"
        )
        val SMART_CLOCKS_MINUTE = listOf(
            "Clocks/Smart Clock/minute_1.webp",
            "Clocks/Smart Clock/minute_2.webp",
            "Clocks/Smart Clock/minute_3.webp",
            "Clocks/Smart Clock/minute_4.webp",
            "Clocks/Smart Clock/minute_5.webp",
            "Clocks/Smart Clock/minute_6.webp"
        )
        val SMART_CLOCKS_SECOND = listOf(
            "Clocks/Smart Clock/second_1.webp",
            "Clocks/Smart Clock/second_2.webp",
            "Clocks/Smart Clock/second_3.webp",
            "Clocks/Smart Clock/second_4.webp",
            "Clocks/Smart Clock/second_5.webp",
            "Clocks/Smart Clock/second_6.webp"
        )

        //LED Clock
        val LED_CLOCKS_DIAL = listOf(
            "Clocks/LED Clock/dial_1.webp",
            "Clocks/LED Clock/dial_2.webp",
            "Clocks/LED Clock/dial_3.webp",
            "Clocks/LED Clock/dial_4.webp",
            "Clocks/LED Clock/dial_5.webp",
            "Clocks/LED Clock/dial_6.webp"
        )
        val LED_CLOCKS_HOUR = listOf(
            "Clocks/LED Clock/hour_1.webp",
            "Clocks/LED Clock/hour_2.webp",
            "Clocks/LED Clock/hour_3.webp",
            "Clocks/LED Clock/hour_4.webp",
            "Clocks/LED Clock/hour_5.webp",
            "Clocks/LED Clock/hour_6.webp"
        )
        val LED_CLOCKS_MINUTE = listOf(
            "Clocks/LED Clock/minute_1.webp",
            "Clocks/LED Clock/minute_2.webp",
            "Clocks/LED Clock/minute_3.webp",
            "Clocks/LED Clock/minute_4.webp",
            "Clocks/LED Clock/minute_5.webp",
            "Clocks/LED Clock/minute_6.webp"
        )
        val LED_CLOCKS_SECOND = listOf(
            "Clocks/LED Clock/second_1.webp",
            "Clocks/LED Clock/second_2.webp",
            "Clocks/LED Clock/second_3.webp",
            "Clocks/LED Clock/second_4.webp",
            "Clocks/LED Clock/second_5.webp",
            "Clocks/LED Clock/second_6.webp"
        )

        //Girl Clock
        val GIRL_CLOCKS_DIAL = listOf(
            "Clocks/Girls Clock/dial_1.webp",
            "Clocks/Girls Clock/dial_2.webp",
            "Clocks/Girls Clock/dial_3.webp",
            "Clocks/Girls Clock/dial_4.webp",
            "Clocks/Girls Clock/dial_5.webp",
            "Clocks/Girls Clock/dial_6.webp"
        )
        val GIRL_CLOCKS_HOUR = listOf(
            "Clocks/Girls Clock/hour_1.webp",
            "Clocks/Girls Clock/hour_2.webp",
            "Clocks/Girls Clock/hour_3.webp",
            "Clocks/Girls Clock/hour_4.webp",
            "Clocks/Girls Clock/hour_5.webp",
            "Clocks/Girls Clock/hour_6.webp"
        )
        val GIRL_CLOCKS_MINUTE = listOf(
            "Clocks/Girls Clock/minute_1.webp",
            "Clocks/Girls Clock/minute_2.webp",
            "Clocks/Girls Clock/minute_3.webp",
            "Clocks/Girls Clock/minute_4.webp",
            "Clocks/Girls Clock/minute_5.webp",
            "Clocks/Girls Clock/minute_6.webp"
        )
        val GIRL_CLOCKS_SECOND = listOf(
            "Clocks/Girls Clock/second_1.webp",
            "Clocks/Girls Clock/second_2.webp",
            "Clocks/Girls Clock/second_3.webp",
            "Clocks/Girls Clock/second_4.webp",
            "Clocks/Girls Clock/second_5.webp",
            "Clocks/Girls Clock/second_6.webp"
        )



        fun getClockAssetDrawable(ctx: Context, assetPath: String):Drawable {
            var bitmap:Bitmap?=null
            var istr: InputStream? = null
            try {
                istr = ctx.assets.open(assetPath)
                bitmap=BitmapFactory.decodeStream(istr)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return BitmapDrawable(ctx.resources, bitmap)
        }
    }


}