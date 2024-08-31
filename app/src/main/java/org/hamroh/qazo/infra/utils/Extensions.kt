package org.hamroh.qazo.infra.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
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

const val pageSize = 48
const val millisInDay = 86400000L

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

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.getStreak(today: Long): Int {
    var prayTime = today
    var count = 0

    while (prayTime > 0) {
        for (prayer in listOf(PrayTime.ISHA, PrayTime.MAGHRIB, PrayTime.ASR, PrayTime.DHUHR, PrayTime.FAJR)) {
            val result = checkPrayerStreak(prayTime, prayer, count) ?: return count
            count = result
        }
        prayTime -= millisInDay
    }

    return count
}

private fun Context.checkPrayerStreak(prayTime: Long, prayer: PrayTime, count: Int): Int? {
    val pray = SharedPrefs(this).get("$prayTime$prayer", String::class.java)
    return when (pray) {
        PrayType.PRAY.toString(), PrayType.QAZO.toString() -> null
        PrayType.ADO.toString() -> count + 1
        else -> if (count == 0) 0 else null
    }
}
