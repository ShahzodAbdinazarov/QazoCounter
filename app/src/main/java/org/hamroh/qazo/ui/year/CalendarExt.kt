package org.hamroh.qazo.ui.year

import android.view.View
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)}, ${this.year}"
}

fun Month.displayText(short: Boolean = false): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    val displayName = getDisplayName(style, Locale.ENGLISH).lowercase().correctMonthName()
    return displayName.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

fun String.correctMonthName(): String {
    return when {
        this == "мая" -> "май"
        this.endsWith("я") -> this.dropLast(1) + "ь"
        this.endsWith("а") -> this.dropLast(1)
        else -> this
    }
}
