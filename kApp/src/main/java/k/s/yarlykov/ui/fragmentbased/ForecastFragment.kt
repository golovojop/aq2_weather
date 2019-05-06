package k.s.yarlykov.ui.fragmentbased

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import kotlinx.android.synthetic.main.city_forecast_fragment.*

class ForecastFragment : Fragment() {

    companion object {
        val forecastBundleKey = "forecastKey"
        val indexBundleKey = "indexKey"

        fun create(index: Int, forecast: CityForecast): ForecastFragment {
            return ForecastFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(forecastBundleKey, forecast)
                    putInt(indexBundleKey, index)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.city_forecast_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderForecast(getForecast())
    }

    fun getIndex(): Int {
        return arguments!!.getInt(indexBundleKey, 0)
    }
    private fun getForecast(): CityForecast {
        return arguments!!.getSerializable(forecastBundleKey) as CityForecast
    }

    fun renderForecast(forecast: CityForecast) {
        // Set Weather image
        iv_sky.setImageResource(forecast.imgId)

        // Set City (Uppercase first letter)
        tv_city.setText(forecast.city.capitalize())

        // Set Temperature
        tv_temperature.setText(String.format("%+2d \u2103", forecast.temperature))

        // Set Wind
        tv_wind.setText(String.format("%2d %s", forecast.wind, getResources().getString(R.string.infoWind)))

        // Set Humidity
        tv_humidity.setText(String.format("%2d %%", forecast.humidity))

        // Set Pressure
        val current = resources.configuration.locale
        val isRu = current.country === "RU"
        tv_pressure.setText(String.format("%4d %s", forecast.getPressure(isRu), getResources().getString(R.string.infoPressure)))
    }
}