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

fun Long.startOfMonthInMillis(): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this // Set the time based on the provided timestamp

    // Set the calendar to the middle of the month
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    // Set time to midday (12:00 PM)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Get timestamp in milliseconds
    return calendar.timeInMillis
}

fun Int.getMonthList(timeInMillis: Long): ArrayList<MonthModel> {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    val currentYear = calendar.get(Calendar.YEAR) + this

    val monthList = ArrayList<MonthModel>()
    val selectedMonth = calendar.get(Calendar.MONTH)
    Log.e("TAG", "getMonthList: " +
            "${System.currentTimeMillis().startOfMonthInMillis()
                .getDate("yyyy-MM-dd HH:mm:SS.sss")}")

    for (i in 0 until 12) {
        calendar.set(Calendar.MONTH, (selectedMonth + i) % 12)
        val daysOfMonth = ArrayList<String>()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.e("TAG", "getMonthList $this -> $i ->" +
                " $selectedMonth: $daysInMonth -> " +
                "${calendar.timeInMillis.startOfMonthInMillis()} ->" +
                "${calendar.get(Calendar.MONTH)}")

        for (day in 1..daysInMonth) {
            calendar.set(currentYear, selectedMonth + i, day)
            daysOfMonth.add(calendar.timeInMillis.toString())
        }
        Log.e("TAG", "getMonthList: ${calendar.timeInMillis.getDate("MMMM")}")
        monthList.add(MonthModel(calendar.timeInMillis.startOfMonthInMillis(), daysOfMonth))
    }
    return monthList
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

const val days = 86400000L

fun Int.getDayList(day: Long): ArrayList<String> {
    val list = arrayListOf<String>()
    repeat(10) {
        list.add("${day - ((it + (this * 10)) * days)}")
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
