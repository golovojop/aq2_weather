package k.s.yarlykov.ui.fragmentbased

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import k.s.yarlykov.R
import k.s.yarlykov.data.db.DBHelper
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.services.RestForecastService
import k.s.yarlykov.ui.fragmentbased.history.HistoryActivity
import k.s.yarlykov.util.Utils.isRu
import kotlinx.android.synthetic.main.city_forecast_fragment.*

class ForecastFragment : Fragment(), RestForecastService.RestForecastReceiver {

    lateinit var dataBase: SQLiteDatabase

    companion object {
        val forecastBundleKey = "forecastKey"
        val placeBundleKey = "cityKey"
        val binderBundleKey = "binderKey"
        val indexBundleKey = "indexKey"

        fun create(index: Int, forecast: CityForecast): ForecastFragment {
            return ForecastFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(forecastBundleKey, forecast)
                    putInt(indexBundleKey, index)
                }
            }
        }

        fun create(binder: IBinder?, city: String, index: Int): ForecastFragment {
            return ForecastFragment().apply {
                arguments = Bundle().also { bundle ->
                    bundle.putBinder(binderBundleKey, binder)
                    bundle.putString(placeBundleKey, city)
                    bundle.putInt(indexBundleKey, index)
                }
            }
        }
    }

    private var forecastService: RestForecastService? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dataBase = DBHelper.create(context!!).writableDatabase
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        getServiceBinder()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.city_forecast_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        forecastService?.requestForecast(this, getCity(), getCountry(), dataBase)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.week, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionWeek -> {
                loadHistory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onForecastOnline(forecastOnline: CityForecast, icon: Bitmap) {
        renderForecast(forecastOnline, true, icon)
    }

    override fun onForecastOffline(forecastOffline: CityForecast?) {
        if (forecastOffline != null) {
            renderForecast(forecastOffline, false, null)
        } else {
            onlineStatus.setBackgroundResource(android.R.color.transparent)
            pbfContainer.visibility = View.GONE
            forecastContainer.visibility = View.GONE
            AlertNoData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dataBase.apply { close() }
    }

    private fun initViews() {
        pbfContainer.visibility = (View.VISIBLE)
        forecastContainer.visibility = (View.GONE)
        tvCityF.text = getCity()
    }

    private fun getServiceBinder() {
        forecastService =
                (arguments!!.getBinder(binderBundleKey) as RestForecastService.ServiceBinder)
                        .service
    }

    private fun getPlace(): String? {
        return arguments!!.getString(placeBundleKey)
    }

    private fun getCity(): String {
        val arr: List<String> = getPlace()!!.split(",".toRegex(), 2)
        return arr[0]
    }

    private fun getCountry(): String {
        val arr: List<String> = getPlace()!!.split(",".toRegex(), 2)
        return arr[1]
    }

    fun getIndex(): Int {
        return arguments!!.getInt(indexBundleKey, 0)
    }

    fun renderForecast(forecast: CityForecast, isOnline: Boolean, icon: Bitmap?) {

        val drawableId = if (isOnline) R.drawable.green_circle else R.drawable.red_circle
        onlineStatus.setBackgroundResource(drawableId)

        // Set Weather image
        if (icon != null) {
            ivSkyF.setImageBitmap(icon)
        } else {
            ivSkyF.setImageResource(forecast.icon)
        }

        // Set City (Uppercase first letter)
        tvCityF.text = forecast.city.capitalize()

        // Set Temperature
        tvTemperatureF.text = String.format("%+2d \u2103", forecast.temperature)

        // Set Wind
        tvWindF.text = String.format("%.1f %s", forecast.wind, getResources().getString(R.string.infoWind))

        // Set Humidity
        tvHumidityF.text = String.format("%2d %%", forecast.humidity)

        // Set Pressure
        tvPressureF.text = String.format("%4d %s", forecast.getPressure(isRu()), getResources().getString(R.string.infoPressure))

        pbfContainer.visibility = View.GONE
        forecastContainer.visibility = View.VISIBLE
    }

    // Эмуляция длительной работы в AsyncTask
    private fun loadHistory() {
        HistoryActivity.start(requireContext(), tvCityF.text as String)
    }

    private fun AlertNoData() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.no_data_dialog, null)
        AlertDialog.Builder(context!!).apply {
            setTitle(getString(R.string.connectivity_alert))
            setPositiveButton(getString(R.string.buttonClose)) { dialog, _ -> dialog.dismiss() }
            setView(dialogView)
            show()
        }
    }
}