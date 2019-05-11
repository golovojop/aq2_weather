package k.s.yarlykov.ui.fragmentbased

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.util.Utils.isRu
import kotlinx.android.synthetic.main.city_forecast_fragment.*
import k.s.yarlykov.util.Utils.logI

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI(this, "onCreate")
//        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        logI(this, "onCreateView")
        return inflater.inflate(R.layout.city_forecast_fragment, container, false)
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        logI(this, "onCreateOptionsMenu")
//        inflater?.inflate(R.menu.fragment_menu, menu)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logI(this, "onViewCreated")
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