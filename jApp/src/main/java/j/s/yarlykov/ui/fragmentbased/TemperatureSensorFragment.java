package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import j.s.yarlykov.R;

public class TemperatureSensorFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensorTemperature = null;
    private Sensor sensorHumidity = null;
    private TextView tvT, tvH;

    public static TemperatureSensorFragment create() {
        TemperatureSensorFragment fragment = new TemperatureSensorFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorTemperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorHumidity = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.current_temperature_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvT = view.findViewById(R.id.tvT);
        tvH = view.findViewById(R.id.tvH);

        String notSupported = getString(R.string.sensorNotSupported);

        if (sensorTemperature == null) {
            tvT.setText(notSupported);
        }

        if (sensorHumidity == null) {
            tvH.setText(notSupported);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorTemperature,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorHumidity,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                tvT.setText(String.valueOf(event.values[0]) + " \u2103");
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                tvH.setText(String.valueOf(event.values[0]));

                break;
            default:

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
