package k.s.yarlykov.ui.fragmentbased

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import k.s.yarlykov.R
import kotlinx.android.synthetic.main.current_temperature_fragment.*

class TemperatureSensorFragment : Fragment(), SensorEventListener {
    lateinit var sensorManager: SensorManager
    var sensorTemperature: Sensor? = null
    var sensorHumidity: Sensor? = null

    companion object {
        fun create(): TemperatureSensorFragment {
            return TemperatureSensorFragment()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        sensorManager = context!!.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.current_temperature_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notSupported = getString(R.string.sensorNotSupported)

        if (sensorTemperature == null) {
            tvT.text = notSupported
        }
        if (sensorHumidity == null) {
            tvH.text = notSupported
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                val sT = "${event.values[0]} ℃"
                tvT.text = sT
            }
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                val sH = "${event.values[0]} %"
                tvH.text = sH
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

}
