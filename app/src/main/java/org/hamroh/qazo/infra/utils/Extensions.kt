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

val days = 86400000L

fun getList(page: Int, day: Long): ArrayList<String> {
    val list = arrayListOf<String>()
    repeat(10) {
        list.add("${day - ((it + (page * 10)) * days)}")
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
