package j.s.yarlykov.util;

import android.util.Log;

public class Utils {

    public static void logI(Object obj, String message) {
        final String tag = "jWeather";
        Log.d(tag, obj.getClass().getSimpleName() + ": " + message);
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
