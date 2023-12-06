package org.hamroh.qazo.infra.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun getList(page: Int): ArrayList<String> {
    val today = getToday()
    val list = arrayListOf<String>()
    repeat(10) {
        list.add("${today - ((it + ((page - 1) * 10)) * 86400000L)}")
    }
    return list
}

fun getToday(): Long {
    val now = System.currentTimeMillis()
    return now - ((now.getDate("HH")?.toLong() ?: 0L) * 3600000) -
            ((now.getDate("mm")?.toLong() ?: 0L) * 60000) -
            ((now.getDate("ss")?.toLong() ?: 0L) * 1000) -
            ((now.getDate("SSS")?.toLong() ?: 0L))
}

@SuppressLint("SimpleDateFormat")
fun Long.getDate(dateFormat: String?): String? {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(dateFormat, Locale.forLanguageTag("uz")).format(calendar.time)
}
