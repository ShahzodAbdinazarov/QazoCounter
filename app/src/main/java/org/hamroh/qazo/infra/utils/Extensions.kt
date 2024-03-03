package org.hamroh.qazo.infra.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.hamroh.qazo.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun String.getDayOfWeek(): Int {
    val calendar = Calendar.getInstance()
    if (this.isEmpty()) return 0
    calendar.timeInMillis = this.toLong()
    return (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
}

fun Long.startOfMonthInMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}

fun ViewGroup.hide() {
    this.visibility = View.GONE
}

fun ViewGroup.show() {
    this.visibility = View.VISIBLE
}

fun Fragment.showSnackbar(message: String, timeLength: Int = Snackbar.LENGTH_SHORT, param: String = "CoordinatorLayout") {
    if (view != null) {
        val snackbar = Snackbar.make(view!!, message, timeLength)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.qazo))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        when (param) {
            "FrameLayout" -> {
                val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
                params.gravity = Gravity.TOP
                snackbar.view.layoutParams = params
                snackbar.show()
            }

            "CoordinatorLayout" -> {
                val params = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
                params.gravity = Gravity.TOP
                snackbar.view.layoutParams = params
                snackbar.show()
            }
        }
    } else Log.e("TAG", "showSnackbar: $message")
}

fun Activity.closeKeyboard(editText: EditText) {
    val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
}

private fun Long.days(): Int {
    val month = (this.timeFormat("M") ?: "0").toInt()
    val year = (this.timeFormat("yyyy") ?: "2").toInt()
    return when (month) {
        1 -> 31
        2 -> if (year % 4 == 0) 29 else 28
        3 -> 31
        4 -> 30
        5 -> 31
        6 -> 30
        7 -> 31
        8 -> 31
        9 -> 30
        10 -> 31
        11 -> 30
        else -> 31
    }
}

const val pageSize = 48
const val millisInDay = 86400000L
const val millisIn4Year = millisInDay * (4L * 365L + 1L)

fun Int.getMonthList(month: Long): ArrayList<Month> {
    var startMonth = month.startOfMonthInMillis() + this * millisIn4Year
    val list = arrayListOf<Month>()
    repeat(pageSize) {
        var startDay = startMonth
        val daysOfMonth = arrayListOf<String>()
        repeat(startMonth.days()) { daysOfMonth.add("$startDay"); startDay += millisInDay }
        list.add(Month(startMonth, daysOfMonth))
        startMonth = startDay
    }
    return list
}

fun Int.getDayList(day: Long): ArrayList<String> {
    val list = arrayListOf<String>()
    repeat(pageSize) { list.add("${day + ((it + (this * pageSize)) * millisInDay)}") }
    return list
}

fun getToday(): Long {
    val now = System.currentTimeMillis()
    return now - ((now.timeFormat("HH")?.toLong() ?: 0L) * 3600000) -
            ((now.timeFormat("mm")?.toLong() ?: 0L) * 60000) -
            ((now.timeFormat("ss")?.toLong() ?: 0L) * 1000) -
            ((now.timeFormat("SSS")?.toLong() ?: 0L))
}

@SuppressLint("SimpleDateFormat")
fun Long.timeFormat(dateFormat: String? = "dd-MMM-yyyy HH:mm:SS.sss"): String? {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat(dateFormat, Locale.forLanguageTag("uz")).format(calendar.time)
}
