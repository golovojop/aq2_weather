package k.s.yarlykov.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_forecast.*;
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.data.domain.Forecast.Companion.EMPTY_VAL

class ForecastActivity : AppCompatActivity() {

    lateinit var NO_DATA: String
    lateinit var forecast: CityForecast

    companion object {
        private val EXTRA_FORECAST = ForecastActivity::class.java.simpleName + ".extra.FORECAST"

        fun start(context: Context, forecast: CityForecast) {
            val intent = Intent(context, ForecastActivity::class.java).apply {
                putExtra(EXTRA_FORECAST, forecast)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        forecast = intent.getSerializableExtra(EXTRA_FORECAST) as CityForecast
        initView()
    }

    override fun onResume() {
        super.onResume()
        renderForecast()
    }

    fun initView() {
        NO_DATA = getResources().getString(R.string.noData)
    }

    fun renderForecast() {

        // TODO: Weather image
        iv_sky.setImageResource(forecast.imgId)

        //TODO: City (Uppercase first letter)
        tv_city.setText(forecast.city.capitalize())

        // TODO: Temperature
        tv_temperature.setText(String.format("%+2d \u2103", forecast.temperature))

        // TODO: Wind
        if (forecast.wind != EMPTY_VAL) {
            tv_wind.setText(String.format("%2d %s", forecast.wind, getResources().getString(R.string.infoWind)))
        } else tv_wind.setText(NO_DATA)

        // TODO: Humidity
        if (forecast.humidity != EMPTY_VAL) {
            tv_humidity.setText(String.format("%2d %%", forecast.humidity))
        } else tv_humidity.setText(NO_DATA)

        // TODO: Pressure
        if (forecast.pressureMm != EMPTY_VAL) {
            val current = resources.configuration.locale
            val isRu = current.country === "RU"
            tv_pressure.setText(String.format("%4d %s", forecast.getPressure(isRu), getResources().getString(R.string.infoPressure)))
        } else tv_pressure.setText(NO_DATA)
    }
}