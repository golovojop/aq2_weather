package k.s.yarlykov.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(
        context: Context,
        db: String,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
) :
        SQLiteOpenHelper(context, db, factory, version) {

    companion object {
        private val DB_NAME = "meteo.db"
        private val DB_VERSION = 1

        fun create(context: Context): DBHelper = DBHelper(context,
                DB_NAME, null, DB_VERSION)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        TabForecast.createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}