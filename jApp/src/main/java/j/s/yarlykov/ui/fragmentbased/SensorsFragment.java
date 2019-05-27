/**
 * Иконки сенсоров
 * https://pngtree.com/free-icon/sensor-device_542247
 */

package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import j.s.yarlykov.R;

public class SensorsFragment extends Fragment {

    public static SensorsFragment create() {
        return new SensorsFragment();
    }

    SensorManager sensorManager = null;
    private ListView listView;
    private List<Sensor> sensors;

    private final String KEY_IMAGE_ID = "image";
    private final String KEY_SENSOR = "sensor";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sensors_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = view.findViewById(R.id.sensorsList);
        initList();
    }

    private void initList() {
        List<Map<String, Object>> data = new ArrayList<>();
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        for (int i = 0; i < sensors.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(KEY_IMAGE_ID, R.drawable.sensor);
            map.put(KEY_SENSOR, sensors.get(i).getName());
            data.add(map);
        }

        String[] from = {KEY_IMAGE_ID, KEY_SENSOR};
        int[] to = {R.id.ivSensor, R.id.tvSensor};

        SimpleAdapter sAdapter = new SimpleAdapter(
                getActivity(),
                data,
                R.layout.sensors_list_item,
                from,
                to);
        listView.setAdapter(sAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSensorInfo(sensors.get(position));
            }
        });
    }

    private void showSensorInfo(Sensor s) {
        StringBuilder sb = new StringBuilder();

        sb.append("Name: " + s.getName() + "\n");
        sb.append("Type: " + s.getType() + "\n");
        sb.append("Vendor: " + s.getVendor() + "\n");
        sb.append("Version: " + s.getVersion() + "\n");

        SensorInfoDialog dialog = SensorInfoDialog.create(sb.toString());
        dialog.show(getChildFragmentManager(), null);
    }
}
