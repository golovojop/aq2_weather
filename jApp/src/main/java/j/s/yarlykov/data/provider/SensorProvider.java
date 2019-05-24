package j.s.yarlykov.data.provider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

public class SensorProvider {

    private static SensorProvider instance = null;
    public static SensorProvider SensorProviderFabric(Context context) {

        if(instance == null) {
            instance = new SensorProvider(context);
        }
        return instance;
    }

    private SensorManager sensorManager;

    private SensorProvider(Context context) {
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    public List<Sensor> getSensorsList() {
        return sensorManager.getSensorList(Sensor.TYPE_ALL);
    }

}
