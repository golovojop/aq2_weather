package j.s.yarlykov.util;

import android.content.res.Resources;
import android.support.v4.os.ConfigurationCompat;
import android.util.Log;

import java.util.Locale;

public class Utils {

    public static void logI(Object obj, String message) {
        final String tag = "jWeather";
        Log.i(tag, obj.getClass().getSimpleName() + ": " + message);
    }

    public static boolean isRu() {
        Locale locale = ConfigurationCompat
                .getLocales(Resources.getSystem().getConfiguration())
                .get(0);
        return locale.getLanguage().toUpperCase().equals("RU");
    }
}
