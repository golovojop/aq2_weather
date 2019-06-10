package j.s.yarlykov.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import j.s.yarlykov.data.domain.CityForecast;

public class TabForecast {

    private final static String TAB_NAME = "forecast";
    private final static String COL_ID = "id";
    private final static String COL_CITY = "city";
    private final static String COL_COUNTRY = "country";
    private final static String COL_TEMP = "temperature";
    private final static String COL_ICON = "icon";
    private final static String COL_WIND = "wind";
    private final static String COL_HUMIDITY = "humidity";
    private final static String COL_PRESSURE = "pressure";

    public static void createTable(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TAB_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY NOT NULL, " +
                COL_CITY + " TEXT NOT NULL, " +
                COL_COUNTRY + " TEXT  NOT NULL DEFAULT 'ru', " +
                COL_TEMP + " INTEGER NOT NULL DEFAULT (0), " +
                COL_ICON + " INTEGER NOT NULL DEFAULT (0), " +
                COL_WIND + " REAL NOT NULL DEFAULT (0.0), " +
                COL_HUMIDITY + " INTEGER NOT NULL DEFAULT (0), " +
                COL_PRESSURE + " INTEGER NOT NULL DEFAULT (0))";

        db.execSQL(query);
    }

    public static long addForecast(CityForecast cf, SQLiteDatabase db) {
        return db.insert(TAB_NAME, null, initContentValues(cf));
    }

    public static int editForecast(CityForecast cf, SQLiteDatabase db) {
        return db.update(TAB_NAME, initContentValues(cf),
                COL_CITY + "='" + cf.getCity() + "'", null);
    }

    public static CityForecast getCityForecast(String city, SQLiteDatabase db) {

        try(Cursor cursor = db.rawQuery("SELECT * FROM " + TAB_NAME + " WHERE " +
                COL_CITY + "=? LIMIT 1", new String[]{city})) {

            if (cursor.moveToFirst()) {
                return extractForecast(cursor);
            }
        }
        return null;
    }

    private static ContentValues initContentValues(CityForecast cf) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CITY, cf.getCity());
        cv.put(COL_COUNTRY, cf.getCountry());
        cv.put(COL_ICON, cf.getIcon());
        cv.put(COL_TEMP, cf.getTemperature());
        cv.put(COL_WIND, cf.getWind());
        cv.put(COL_HUMIDITY, cf.getHumidity());
        cv.put(COL_PRESSURE, cf.getPressure());
        return cv;
    }

    private static CityForecast extractForecast(Cursor cursor) {
        int idxId = cursor.getColumnIndex(COL_ID);
        int idxCity = cursor.getColumnIndex(COL_CITY);
        int idxCountry = cursor.getColumnIndex(COL_COUNTRY);
        int idxTemp = cursor.getColumnIndex(COL_TEMP);
        int idxIcon = cursor.getColumnIndex(COL_ICON);
        int idxWind = cursor.getColumnIndex(COL_WIND);
        int idxHumidity = cursor.getColumnIndex(COL_HUMIDITY);
        int idxPressure = cursor.getColumnIndex(COL_PRESSURE);

        return new CityForecast(
                cursor.getString(idxCity),
                cursor.getString(idxCountry),
                cursor.getInt(idxTemp),
                cursor.getInt(idxIcon),
                cursor.getFloat(idxWind),
                cursor.getInt(idxHumidity),
                cursor.getInt(idxPressure));
    }
}
