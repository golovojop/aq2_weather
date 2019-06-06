package k.s.yarlykov.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*;
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.data.provider.ForecastProvider
import k.s.yarlykov.util.Utils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setOnClickRequestButton()
    }

    private fun initViews() {
        chbWind.isChecked = true
        chbHumidity.isChecked = true
        chbPressure.isChecked = true
    }

    private fun setOnClickRequestButton() {
        btnRequest.setOnClickListener{ button ->
            val city: String = etCity.text.toString()

            if(city.length > 1) {
                val requiredMeteoData: MutableSet<CityForecast.Companion.MeteoData> = mutableSetOf()

                if(chbWind.isChecked) requiredMeteoData.add(CityForecast.Companion.MeteoData.WIND)
                if(chbHumidity.isChecked) requiredMeteoData.add(CityForecast.Companion.MeteoData.HUMIDITY)
                if(chbPressure.isChecked) requiredMeteoData.add(CityForecast.Companion.MeteoData.PRESSURE)

//                ForecastActivity.start(button.context, ForecastProvider.getForecastCustom(city, requiredMeteoData))
            } else Utils.logI(button.context, getString(R.string.incorrectRequest))
        }
    }
}
