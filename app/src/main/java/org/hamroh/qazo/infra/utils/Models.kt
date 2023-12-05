package org.hamroh.qazo.infra.utils

data class Models(
    val int: Int? = 0,
)

data class Day(
    val int: String? = "0",
)

enum class PrayType {
    ADO, QAZO, PRAY, YET
}

enum class PrayTime {
    FAJR, DHUHR, ASR, MAGHRIB, ISHA
}