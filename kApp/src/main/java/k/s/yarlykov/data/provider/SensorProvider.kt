package k.s.yarlykov.data.provider

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

object SensorProvider {

    fun getSensorsList(context: Context): List<Sensor> =
            (context.getSystemService(Context.SENSOR_SERVICE) as SensorManager)
                    .getSensorList(Sensor.TYPE_ALL)
}