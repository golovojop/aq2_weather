package j.s.yarlykov.util;

import android.content.res.Resources;
import android.support.v4.os.ConfigurationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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

    public static String capitalize(String text) {
        StringBuilder sb = new StringBuilder();
        for(String s : text.split("\\s")) {
            String tmp = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
            sb.append(tmp);
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    public static int posRandom(int until) {
        return new Random().nextInt(Math.abs(until - 1));
    }

    // Получить дату в формате "dd/MM" на days дней назад
    public static String daysAgo(Date date, int days) {
        return daysThrough(date, days, true);
    }

    // Получить дату в формате "dd/MM" на days дней вперед
    public static String daysAhead(Date date, int days) {
        return daysThrough(date, days, false);
    }

    // Дата в формате "dd/MM" на shift дней вперед/назад
    private static String daysThrough(Date date, int shift, boolean isAgo) {
        String format = "dd/MM";
        int days = Math.abs(shift) * (isAgo ? -1 : 1);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, days);

        return new SimpleDateFormat(format, Locale.getDefault()).format(calendar.getTime());
    }
}
