package net.harutiro.mygeofence.utils

import android.icu.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object{
        fun getNowDate(): String {
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
            val date = Date(System.currentTimeMillis())
            return df.format(date)
        }

        fun getTimeStamp():Long {
            return System.currentTimeMillis()
        }
    }
}