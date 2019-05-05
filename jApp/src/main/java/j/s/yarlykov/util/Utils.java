package j.s.yarlykov.util;

import android.util.Log;

public class Utils {

    public static void logI(Object obj, String message) {
        final String tag = "jWeather";
        Log.i(tag, obj.getClass().getSimpleName() + ": " + message);
    }
}
