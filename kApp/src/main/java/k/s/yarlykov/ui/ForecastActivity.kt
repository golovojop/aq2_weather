package k.s.yarlykov.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_forecast.*;
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.data.domain.Forecast.Companion.EMPTY_VAL
import k.s.yarlykov.util.Utils.isRu

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
        NO_DATA = getResources().getString(R.string.noData)
    }

    override fun onResume() {
        super.onResume()
        renderForecast()
    }

    fun renderForecast() {

        // Set Weather image
        ivSkyA.setImageResource(forecast.imgId)

        //Set City (Uppercase first letter)
        tvCityA.text = (forecast.city.capitalize())

        // Set Temperature
        tvTemperatureA.text = (String.format("%+2d \u2103", forecast.temperature))

        // Set Wind
        if (forecast.wind != EMPTY_VAL) {
            tvWindA.text = (String.format("%2d %s", forecast.wind, getResources().getString(R.string.infoWind)))
        } else tvWindA.text = (NO_DATA)

        // Set Humidity
        if (forecast.humidity != EMPTY_VAL.toInt()) {
            tvHumidityA.text = (String.format("%2d %%", forecast.humidity))
        } else tvHumidityA.text = (NO_DATA)

        // Set Pressure
        if (forecast.pressureMm != EMPTY_VAL.toInt()) {
            tvPressureA.text = (String.format("%4d %s", forecast.getPressure(isRu()), getResources().getString(R.string.infoPressure)))
        } else tvPressureA.text = (NO_DATA)
    }
}