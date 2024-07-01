package com.example.frndassignment.utils

import android.annotation.SuppressLint
import com.example.frndassignment.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


fun parseDate(timeInMillis: Long = Calendar.getInstance().timeInMillis, pattern: String = Constants.month): String {
    return getSimpleDateFormat(pattern).format(Date(timeInMillis))
}

@SuppressLint("SimpleDateFormat")
fun getSimpleDateFormat(pattern: String): SimpleDateFormat {
    return SimpleDateFormat(pattern)
}
