package k.s.yarlykov.util

import android.util.Log

object Utils {
    fun logI(obj: Any, message: String) {
        val tag = "kWeather"
        Log.i(tag, obj::class.java.simpleName + ": " + message)
    }
}