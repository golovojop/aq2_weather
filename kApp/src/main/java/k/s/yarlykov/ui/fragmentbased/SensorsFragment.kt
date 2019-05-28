/**
 * Иконки сенсоров
 * https://pngtree.com/free-icon/sensor-device_542247
 */

package k.s.yarlykov.ui.fragmentbased

import java.util.*
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import k.s.yarlykov.R
import kotlinx.android.synthetic.main.sensors_list.*

class SensorsFragment : Fragment() {

    lateinit var sensorManager: SensorManager

    companion object {
        fun create(): SensorsFragment {
            return SensorsFragment()
        }
    }

    private val KEY_IMAGE_ID = "image"
    private val KEY_SENSOR = "sensor"

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sensorManager = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
        val sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)

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

        sensorsList!!.apply {
            adapter = sAdapter
            onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                showSensorInfo(sensors[position])
            }
        }
    }

    private fun showSensorInfo(s: Sensor) {
        val sb = StringBuilder()

        sb.append("Name: ${s.name}\n")
        sb.append("Type: ${Integer.toString(s.type)}\n")
        sb.append("Vendor: ${s.vendor}\n")
        sb.append("Version: ${Integer.toString(s.version)}\n")

        val dialog = SensorInfoDialog.create(sb.toString())
        dialog.show(childFragmentManager, null)
    }
}
