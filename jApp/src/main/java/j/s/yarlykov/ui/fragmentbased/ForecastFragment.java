package j.s.yarlykov.ui.fragmentbased;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Formatter;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.ui.fragmentbased.history.HistoryActivity;
import j.s.yarlykov.util.Utils;

import static j.s.yarlykov.util.Utils.isRu;

public class ForecastFragment extends Fragment {

    public static final String forecastBundleKey = "forecastKey";
    public static final String indexBundleKey = "indexKey";

    private TextView tvCity, tvTemperature, tvWind, tvHumidity, tvPressure;
    private ImageView ivSky;

    public static ForecastFragment create(int index, CityForecast forecast) {
        ForecastFragment fragment = new ForecastFragment();

        // Передача параметра
        Bundle args = new Bundle();
        args.putSerializable(forecastBundleKey, forecast);
        args.putInt(indexBundleKey, index);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        renderForecast(getForecast());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.week, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionWeek:
                HistoryActivity.start(requireContext(), tvCity.getText().toString());
                break;
                default:
        }
        return super.onOptionsItemSelected(item);
    }

    public int getIndex() {
        return getArguments().getInt(indexBundleKey, 0);
    }

    private CityForecast getForecast() {
        return (CityForecast) getArguments().getSerializable(forecastBundleKey);
    }

    private void initViews(View parent) {
        ivSky = parent.findViewById(R.id.iv_sky);
        tvCity = parent.findViewById(R.id.tv_city);
        tvTemperature = parent.findViewById(R.id.tv_temperature);
        tvWind = parent.findViewById(R.id.tv_wind);
        tvHumidity = parent.findViewById(R.id.tv_humidity);
        tvPressure = parent.findViewById(R.id.tv_pressure);
    }

    // Отрисовать прогноз на экране
    private void renderForecast(CityForecast forecast) {

        // Set Weather image
        ivSky.setImageResource(forecast.getImgId());

        //Set City (Uppercase first letter)
        String city = forecast.getCity();
        tvCity.setText(Utils.capitalize(city));

        // Set Temperature
        Formatter fmt = new Formatter();
        fmt.format("%+2d \u2103", forecast.getTemperature());
        tvTemperature.setText(fmt.toString());
        fmt.close();

        // Set Wind
        fmt = new Formatter();
        fmt.format("%2d %s", forecast.getWind(), getResources().getString(R.string.infoWind));
        tvWind.setText(fmt.toString());
        fmt.close();

        // Set Humidity
        fmt = new Formatter();
        fmt.format("%2d %%", forecast.getHumidity());
        tvHumidity.setText(fmt.toString());
        fmt.close();

        // Set Pressure
        fmt = new Formatter();
        fmt.format("%4d %s", (int) forecast.getPressure(isRu()), getResources().getString(R.string.infoPressure));
        tvPressure.setText(fmt.toString());
        fmt.close();
    }
}