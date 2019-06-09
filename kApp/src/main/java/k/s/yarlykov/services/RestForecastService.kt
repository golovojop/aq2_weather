package k.s.yarlykov.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import k.s.yarlykov.data.db.TabForecast
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.data.network.api.OpenWeatherProvider
import k.s.yarlykov.data.network.model.WeatherResponseModel
import k.s.yarlykov.util.Utils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestForecastService : Service() {

    // Для работы с SharedPreferences
    private val country = "country"
    private val temperature = "temp"
    private val iconId = "icon"
    private val wind = "wind"
    private val pressure = "pressure"
    private val humidity = "humidity"

    private val mBinder = ServiceBinder()

    interface RestForecastReceiver {
        fun onForecastOnline(forecastOnline: CityForecast, icon: Bitmap)
        fun onForecastOffline(forecastOffline: CityForecast?)
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    inner class ServiceBinder : Binder() {
        val service: RestForecastService
            get() = this@RestForecastService
    }

    // Запросить текущий прогноз погоды для города city
    fun requestForecast(receiver: RestForecastReceiver,
                        city: String, country: String,
                        db: SQLiteDatabase) {

        OpenWeatherProvider.api.loadWeather(
                "$city,$country",
                "2173331fd3e3226666b27e71abc27974",
                "metric")
                .enqueue(object : Callback<WeatherResponseModel> {
                    override fun onResponse(call: Call<WeatherResponseModel>,
                                            response: Response<WeatherResponseModel>) {

                        // Code 404: body() == null; isSuccessful() == false
                        if (response.body() != null && response.isSuccessful) {
                            val model: WeatherResponseModel? = response.body()

                            val cf = CityForecast(
                                    0,
                                    model!!.name!!,
                                    model.sys!!.country!!,
                                    model.main!!.temp.toInt(),
                                    fetchIconId(applicationContext, model.weather!![0].icon),
                                    model.wind!!.speed,
                                    model.main!!.humidity,
                                    model.main!!.pressure
                            )

                            dbSaveForecast(db, cf)
//                            saveForecast(cf)
                            requestIcon(receiver, model.weather!![0].icon!!, cf, db)
                        } else {
                            onFailure(call, Throwable(response.message()))
                        }
                    }

                    override fun onFailure(call: Call<WeatherResponseModel>, t: Throwable) {
//                        receiver.onForecastOffline(loadForecast(city))
                        receiver.onForecastOffline(dbLoadForecast(db, city))

                    }
                })
    }

    // Получить иконку погоды
    private fun requestIcon(receiver: RestForecastReceiver,
                            icon: String, cf: CityForecast,
                            db: SQLiteDatabase) {

        val call = OpenWeatherProvider.api
                .fetchIcon("https://openweathermap.org/img/w/${icon}.png")

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null && response.isSuccessful) {
                    val bmp = BitmapFactory.decodeStream(response.body()!!.byteStream())
                    receiver.onForecastOnline(cf, bmp)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                receiver.onForecastOffline(loadForecast(cf.city))
                receiver.onForecastOffline(dbLoadForecast(db, cf.city))
            }
        })
    }

    // Конвертировать имя иконки в ID ресурса картинки
    private fun fetchIconId(context: Context, iconName: String?): Int {
        val icon = "ow$iconName"
        val resources = context.resources
        return resources.getIdentifier(icon, "drawable",
                context.packageName)
    }

    private fun dbSaveForecast(db: SQLiteDatabase, cf: CityForecast) {
        if(TabForecast.editForecast(cf, db) == 0) {
            TabForecast.addForecast(cf, db)
        }
    }

    private fun dbLoadForecast(db: SQLiteDatabase, city: String) : CityForecast? =
        TabForecast.getCityForecast(city, db)


    // Сохранить прогноз в SharedPreferences
    private fun saveForecast(cf: CityForecast) {
        val shPrefs = getSharedPreferences(cf.city, Context.MODE_PRIVATE)
        val editor = shPrefs.edit()

        editor.putString(country, cf.country)
        editor.putInt(temperature, cf.temperature)
        editor.putInt(iconId, cf.icon)
        editor.putFloat(wind, cf.wind)
        editor.putInt(humidity, cf.humidity)
        editor.putInt(pressure, cf.getPressure(Utils.isRu()))
        editor.apply()
    }

    // Загрузить прогноз из SharedPreferences
    private fun loadForecast(city: String): CityForecast? {
        val shPrefs = getSharedPreferences(city, Context.MODE_PRIVATE)

        return if (shPrefs.contains(country)) {
            CityForecast(
                    0,
                    city,
                    shPrefs.getString(country, "ru")!!,
                    shPrefs.getInt(temperature, 0),
                    shPrefs.getInt(iconId, 0),
                    shPrefs.getFloat(wind, 0f),
                    shPrefs.getInt(humidity, 0),
                    shPrefs.getInt(pressure, 0))
        } else null
    }
}
