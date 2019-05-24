/**
 * Иконки сенсоров
 * https://pngtree.com/free-icon/sensor-device_542247
 */

package k.s.yarlykov.ui.fragmentbased

import java.util.*
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import k.s.yarlykov.R
import k.s.yarlykov.data.provider.SensorProvider
import kotlinx.android.synthetic.main.sensors_list.*

class SensorsFragment : Fragment() {

    companion object {
        fun create(): SensorsFragment {
            return SensorsFragment()
        }
    }

    private val KEY_IMAGE_ID = "image"
    private val KEY_SENSOR = "sensor"

    lateinit var sensorProvider: SensorProvider

    interface SensorsAdapter {
        fun getSensorProvider(): SensorProvider
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            val adapter = context as SensorsAdapter?
            sensorProvider = adapter!!.getSensorProvider()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sensors_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {
        val data = ArrayList<Map<String, Any>>()
        val sensors = sensorProvider!!.getSensorsList(activity as Context)

        for (sensor in sensors) {
            val map = mapOf(
                    KEY_IMAGE_ID to R.drawable.sensor,
                    KEY_SENSOR to sensor.name)
            data.add(map)
        }

        val from = arrayOf(KEY_IMAGE_ID, KEY_SENSOR)
        val to = intArrayOf(R.id.ivSensor, R.id.tvSensor)

        val sAdapter = SimpleAdapter(
                activity,
                data,
                R.layout.sensors_list_item,
                from,
                to)

        sensorsList!!.adapter = sAdapter
    }
}
