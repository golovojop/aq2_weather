package k.s.yarlykov.ui.fragmentbased

import android.content.res.Configuration
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.FragmentTransaction
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.SimpleAdapter
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.data.domain.Forecast
import k.s.yarlykov.services.ForecastService
import java.util.*

class CitiesFragment : ListFragment() {

    companion object {
        val KEY_BINDER = "binder"

        fun create(binder: IBinder): CitiesFragment {
            return CitiesFragment().apply {
                arguments = Bundle().apply {
                    putBinder(KEY_BINDER, binder)
                }
            }
        }
    }

    interface ForecastSource {
        fun getForecastById(id: Int): Forecast
        fun getForecastByCity(city: String): Forecast?
    }

    private  val selectedPositionKey = "SelectedCity"
    private  val KEY_IMAGE_ID = "image"
    private  val KEY_CITY = "city"

    private var isLandscape: Boolean = false
    private var selectedPosition: Int = 0
    private var forecastSource: IBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forecastSource =  arguments?.getBinder(KEY_BINDER)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.cities_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        savedInstanceState?.let {
            selectedPosition = it.getInt(selectedPositionKey, 0)
        }

        if (isLandscape) {
            listView.choiceMode = ListView.CHOICE_MODE_SINGLE
            listView.setItemChecked(selectedPosition, true)
            showForecast()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(selectedPositionKey, selectedPosition)
        super.onSaveInstanceState(outState)
    }

    private fun initList() {
        val data = ArrayList<Map<String, Any>>()
        val images = resources.obtainTypedArray(R.array.armsImages)
        val cities = resources.getStringArray(R.array.cities)

        for ((i, city) in cities.withIndex()) {
            val map = mapOf(
                    KEY_IMAGE_ID to images.getResourceId(i, 0),
                    KEY_CITY to city)
            data.add(map)
        }
        images.recycle()

        val from = arrayOf(KEY_IMAGE_ID, KEY_CITY)
        val to = intArrayOf(R.id.ivArms, R.id.tvCity)

        listAdapter = SimpleAdapter(
                activity,
                data,
                R.layout.cities_list_item,
                from,
                to)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedPosition = position
            showForecast()
        }
    }

    private fun showForecast() {
        val citySelected = resources.getStringArray(R.array.cities_openweather)[selectedPosition]

        if (isLandscape) {

            activity!!.findViewById<FrameLayout>(R.id.rightFrame).visibility = View.VISIBLE
            listView.setItemChecked(selectedPosition, true)

            var forecastFragment = fragmentManager!!
                    .findFragmentById(R.id.rightFrame) as ForecastFragment?

            if (forecastFragment == null || forecastFragment.getIndex() != selectedPosition) {
//                forecastFragment = ForecastFragment.create(forecastSource, citySelected, selectedPosition)

                val ft = fragmentManager!!.beginTransaction()
//                ft.replace(R.id.rightFrame, forecastFragment)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                ft.commit()
            }
        } else {
            ForecastActivityFr.start(context, forecastSource, citySelected, selectedPosition)
        }
    }
}