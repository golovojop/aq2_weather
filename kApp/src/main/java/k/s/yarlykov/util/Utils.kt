package k.s.yarlykov.util

import android.content.res.Resources
import android.support.v4.os.ConfigurationCompat
import android.util.Log

object Utils {
    fun logI(obj: Any, message: String) {
        val tag = "kWeather"
        Log.i(tag, obj::class.java.simpleName + ": " + message)
    }

    fun isRu() : Boolean {
        val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration).get(0)
        return locale.language.toUpperCase() == "RU"
    }
}