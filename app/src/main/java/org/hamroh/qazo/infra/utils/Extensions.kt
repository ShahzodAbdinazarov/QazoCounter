package org.hamroh.qazo.infra.utils

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar


fun getList(page: Int): ArrayList<String> {
    val now = System.currentTimeMillis()
    val today = now - (now % 86400000L)
    val list = arrayListOf<String>()
    repeat(10) {
        list.add("${today - ((it + ((page - 1) * 10)) * 86400000L)}")
        Log.e("TAG", "INDEX: $it - ${today - ((it + ((page - 1) * 10)) * 86400000L)}")
    }
    return list
}

@SuppressLint("SimpleDateFormat")
fun Long.getDate(dateFormat: String?): String? {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(dateFormat).format(calendar.time)
}
