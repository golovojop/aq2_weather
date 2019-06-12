package k.s.yarlykov.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import k.s.yarlykov.data.domain.CityForecast

object TabForecast {

    val TAB_NAME = "forecast"
    val COL_ID = "id"
    val COL_CITY = "city"
    val COL_COUNTRY = "country"
    val COL_TEMP = "temperature"
    val COL_ICON = "icon"
    val COL_WIND = "wind"
    val COL_HUMIDITY = "humidity"
    val COL_PRESSURE = "pressure"

    fun createTable(db: SQLiteDatabase?) {

        val query = "CREATE TABLE $TAB_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY NOT NULL, " +
                "$COL_CITY STRING NOT NULL, " +
                "$COL_COUNTRY STRING  NOT NULL DEFAULT 'ru', " +
                "$COL_TEMP INTEGER NOT NULL DEFAULT (0), " +
                "$COL_ICON INTEGER NOT NULL DEFAULT (0), " +
                "$COL_WIND REAL NOT NULL DEFAULT (0.0), " +
                "$COL_HUMIDITY INTEGER NOT NULL DEFAULT (0), " +
                "$COL_PRESSURE INTEGER NOT NULL DEFAULT (0))"

        db?.execSQL(query)
    }

    // Добавить запись
    fun addForecast(f: CityForecast, db: SQLiteDatabase) : Long {
        return db.insert(TAB_NAME, null, initContentValues(f))
    }

    // Изменить запись
    fun editForecast(f: CityForecast, db: SQLiteDatabase): Int {
        return db.update(TAB_NAME, initContentValues(f), "$COL_CITY='${f.city}'", null)
    }

    fun deleteById(id: Int, db: SQLiteDatabase) {
        db.delete(TAB_NAME, "$COL_ID=$id", null)
    }

    fun deleteByCity(city: String, db: SQLiteDatabase) {
        db.delete(TAB_NAME, "$COL_CITY=$city", null)
    }

    fun deleteAll(db: SQLiteDatabase) {
        db.delete(TAB_NAME, null, null)
    }

    fun getCityForecast(city: String, db: SQLiteDatabase) : CityForecast? {

        var forecast: CityForecast? = null
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TAB_NAME WHERE $COL_CITY=? limit 1", arrayOf(city))

        if(cursor.moveToFirst()) {
            forecast = extractForecast(cursor)
        }

        cursor.close()
        return forecast
    }

    fun readAll(db: SQLiteDatabase): List<CityForecast> {
        val list = mutableListOf<CityForecast>()

        val cursor = db.query(TAB_NAME, null, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do{
                list.add(extractForecast(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    private fun extractForecast(cursor: Cursor) : CityForecast {

        val idxId = cursor.getColumnIndex(COL_ID)
        val idxCity = cursor.getColumnIndex(COL_CITY)
        val idxCountry = cursor.getColumnIndex(COL_COUNTRY)
        val idxTemp = cursor.getColumnIndex(COL_TEMP)
        val idxIcon = cursor.getColumnIndex(COL_ICON)
        val idxWind = cursor.getColumnIndex(COL_WIND)
        val idxHumidity = cursor.getColumnIndex(COL_HUMIDITY)
        val idxPressure = cursor.getColumnIndex(COL_PRESSURE)

        return CityForecast(
                cursor.getInt(idxId),
                cursor.getString(idxCity),
                cursor.getString(idxCountry),
                cursor.getInt(idxTemp),
                cursor.getInt(idxIcon),
                cursor.getFloat(idxWind),
                cursor.getInt(idxHumidity),
                cursor.getInt(idxPressure))
    }

    private fun initContentValues(f: CityForecast): ContentValues {
        return ContentValues().apply {
            put(COL_CITY, f.city)
            put(COL_COUNTRY, f.country)
            put(COL_ICON, f.icon)
            put(COL_TEMP, f.temperature)
            put(COL_WIND, f.wind)
            put(COL_HUMIDITY, f.humidity)
            put(COL_PRESSURE, f.pressure)
        }
    }
}