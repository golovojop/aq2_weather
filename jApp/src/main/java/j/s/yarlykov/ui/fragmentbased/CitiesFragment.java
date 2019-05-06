/**
 * Materials:
 * http://developer.alexanderklimov.ru/android/listfragment.php
 *
 */

package j.s.yarlykov.ui.fragmentbased;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.provider.ForecastProvider;

public class CitiesFragment extends ListFragment {

    boolean isLandscape;
    int selectedPosition = 0;
    final String selectedPositionKey = "SelectedCity";
    ListView listView;

    final String KEY_IMAGE_ID = "image";
    final String KEY_CITY = "city";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cities_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getListView();
        initList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if(savedInstanceState != null){
            selectedPosition = savedInstanceState.getInt(selectedPositionKey, 0);
        }

        if(isLandscape){
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showForecast();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(selectedPositionKey, selectedPosition);
        super.onSaveInstanceState(outState);
    }

    private void initList() {
        ArrayList<Map<String, Object>> data = new ArrayList<>();
        TypedArray images = getResources().obtainTypedArray(R.array.armsImages);
        String[] cities = getResources().getStringArray(R.array.cities);

        Map<String, Object> map;

        for(int i = 0; i < cities.length; i++) {
            map = new HashMap<>();
            map.put(KEY_IMAGE_ID, images.getResourceId(i, 0));
            map.put(KEY_CITY, cities[i]);
            data.add(map);
        }
        images.recycle();

        String[] from = {KEY_IMAGE_ID, KEY_CITY};
        int[] to = {R.id.ivArms, R.id.tvCity};

        SimpleAdapter sAdapter = new SimpleAdapter(
                getActivity(),
                data,
                R.layout.cities_list_item,
                from,
                to);

        setListAdapter(sAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                showForecast();
            }
        });
    }

    private void showForecast(){
        ForecastProvider provider = ForecastProvider.getInstance();

        String citySelected = getResources().getStringArray(R.array.cities)[selectedPosition];

        CityForecast forecast = new CityForecast(citySelected,
                provider.getForecastByIndex(selectedPosition));

        if(isLandscape) {
            listView.setItemChecked(selectedPosition, true);

            ForecastFragment forecastFragment = (ForecastFragment)getFragmentManager()
                    .findFragmentById(R.id.forecastContainer);

            if(forecastFragment == null || forecastFragment.getIndex() != selectedPosition) {
                forecastFragment = ForecastFragment.create(selectedPosition, forecast);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.forecastContainer, forecastFragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(String.format("%s", forecastFragment.hashCode()));
                ft.commit();
            }
        } else {
            ForecastActivityFr.start(getContext(), forecast);
        }
    }
}
