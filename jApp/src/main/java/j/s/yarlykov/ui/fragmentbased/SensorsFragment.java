/**
 * Иконки сенсоров
 * https://pngtree.com/free-icon/sensor-device_542247
 */

package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import j.s.yarlykov.R;
import j.s.yarlykov.data.provider.SensorProvider;

public class SensorsFragment extends Fragment {

    public interface SensorsAdapter {
        SensorProvider getSensorProvider();
    }

    public static SensorsFragment create() {
        return new SensorsFragment();
    }

    private SensorProvider sensorProvider = null;
    private ListView listView;

    private final String KEY_IMAGE_ID = "image";
    private final String KEY_SENSOR = "sensor";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            SensorsAdapter adapter = (SensorsAdapter) context;
            sensorProvider = adapter.getSensorProvider();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
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
        List<Sensor> sensors = sensorProvider.getSensorsList();

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
    }
}
