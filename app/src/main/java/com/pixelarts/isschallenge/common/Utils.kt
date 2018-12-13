package com.pixelarts.isschallenge.common

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun timestampToDateTime(timestamp: Long): String{
        val calender = Calendar.getInstance()
        val timeZone = calender.timeZone
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
        calender.timeInMillis = timestamp * 1000L
        dateFormat.timeZone = timeZone

        return dateFormat.format(calender.time)
    }

    fun secondsToTime(seconds: Long):String{
        val date = Date(seconds * 1000L)
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        timeFormat.timeZone = TimeZone.getTimeZone("GMT")

        return timeFormat.format(date)
    }
}