package com.example.masterproject.ui.tools

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatUnixTimeMillis(milliseconds: Long): String {
    val date = Date(milliseconds)
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(date)
}