package k.s.yarlykov.ui.fragmentbased

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.ui.fragmentbased.history.HistoryActivity
import k.s.yarlykov.util.Utils.isRu
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.city_forecast_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnHistory.setOnClickListener {
            HistoryActivity.start(this@ForecastFragment.requireContext(), tvCityF.text as String)
        }

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
        ivSkyF.setImageResource(forecast.imgId)

        // Set City (Uppercase first letter)
        tvCityF.text = forecast.city.capitalize()

        // Set Temperature
        tvTemperatureF.text = String.format("%+2d \u2103", forecast.temperature)

        // Set Wind
        tvWindF.text = String.format("%2d %s", forecast.wind, getResources().getString(R.string.infoWind))

        // Set Humidity
        tvHumidityF.text = String.format("%2d %%", forecast.humidity)

        // Set Pressure
        tvPressureF.text = String.format("%4d %s", forecast.getPressure(isRu()), getResources().getString(R.string.infoPressure))
    }
}