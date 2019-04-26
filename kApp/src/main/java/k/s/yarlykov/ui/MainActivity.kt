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
        initView()
    }

    fun initView() {
        chb_wind.setChecked(true)
        chb_humidity.setChecked(true)
        chb_pressure.setChecked(true)

        bt_request.setOnClickListener{ button ->
            val city: String? = et_city.text.toString()

            if(city != null && city.length > 1) {
                val meteoData: MutableSet<CityForecast.Companion.MeteoData> = mutableSetOf()

                if(chb_wind.isChecked()) meteoData.add(CityForecast.Companion.MeteoData.WIND)
                if(chb_humidity.isChecked()) meteoData.add(CityForecast.Companion.MeteoData.HUMIDITY)
                if(chb_pressure.isChecked()) meteoData.add(CityForecast.Companion.MeteoData.PRESSURE)

                ForecastActivity.start(button.context, ForecastProvider.getForecastCustom(city, meteoData))
            } else Utils.logI(button.context, "Empty or incorrect request")
        }
    }
}
