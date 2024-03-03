package org.hamroh.qazo.infra.utils

enum class PrayType {
    ADO, QAZO, PRAY, YET
}

enum class PrayTime {
    FAJR, DHUHR, ASR, MAGHRIB, ISHA
}

data class Month(
    val millisStartOfMonth: Long? = null,
    val millisStartOfDayInMonth: ArrayList<String>? = null
)
