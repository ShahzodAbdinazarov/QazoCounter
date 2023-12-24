package org.hamroh.qazo.infra.utils

import android.content.Context
import android.content.SharedPreferences

@Suppress("UNCHECKED_CAST")
class SharedPrefs(context: Context) {

    companion object {
        private const val PREF = "MyAppPrefName"
        private const val ABLUTION = "ABLUTION"
        private const val NAME = "NAME"
    }

    private val sharedPref: SharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun setName(name: String) = put(NAME, name)
    val name = get(NAME, String::class.java)

    fun setAblution(ablution: Boolean) = put(ABLUTION, ablution)
    val ablution = get(ABLUTION, Boolean::class.java)

    fun increase(key: String) = put(key, (get(key, Long::class.java) + 1))
    fun decrease(key: String) = put(key, (get(key, Long::class.java) - 1))

    fun allQazo(): Long {
        return get(PrayType.QAZO.toString() + PrayTime.FAJR.toString(), Long::class.java) +
                get(PrayType.QAZO.toString() + PrayTime.DHUHR.toString(), Long::class.java) +
                get(PrayType.QAZO.toString() + PrayTime.ASR.toString(), Long::class.java) +
                get(PrayType.QAZO.toString() + PrayTime.MAGHRIB.toString(), Long::class.java) +
                get(PrayType.QAZO.toString() + PrayTime.ISHA.toString(), Long::class.java)
    }

    fun allAdo(): Long {
        return get(PrayType.ADO.toString() + PrayTime.FAJR.toString(), Long::class.java) +
                get(PrayType.ADO.toString() + PrayTime.DHUHR.toString(), Long::class.java) +
                get(PrayType.ADO.toString() + PrayTime.ASR.toString(), Long::class.java) +
                get(PrayType.ADO.toString() + PrayTime.MAGHRIB.toString(), Long::class.java) +
                get(PrayType.ADO.toString() + PrayTime.ISHA.toString(), Long::class.java)
    }

    fun allPray(): Long {
        return get(PrayType.PRAY.toString() + PrayTime.FAJR.toString(), Long::class.java) +
                get(PrayType.PRAY.toString() + PrayTime.DHUHR.toString(), Long::class.java) +
                get(PrayType.PRAY.toString() + PrayTime.ASR.toString(), Long::class.java) +
                get(PrayType.PRAY.toString() + PrayTime.MAGHRIB.toString(), Long::class.java) +
                get(PrayType.PRAY.toString() + PrayTime.ISHA.toString(), Long::class.java)
    }

    fun <T> get(key: String, clazz: Class<T>): T = when (clazz) {
        String::class.java -> sharedPref.getString(key, "")
        Boolean::class.java -> sharedPref.getBoolean(key, false)
        Float::class.java -> sharedPref.getFloat(key, 0f)
        Double::class.java -> sharedPref.getFloat(key, 0f)
        Int::class.java -> sharedPref.getInt(key, 0)
        Long::class.java -> sharedPref.getLong(key, 0L)
        else -> null
    } as T

    fun <T> put(key: String, data: T) {
        val editor = sharedPref.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Double -> editor.putFloat(key, data.toFloat())
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
        }
        editor.apply()
    }

//    fun clearAll() = sharedPref.edit().clear().commit()

}
