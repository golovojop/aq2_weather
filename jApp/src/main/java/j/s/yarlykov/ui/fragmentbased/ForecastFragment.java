package j.s.yarlykov.ui.fragmentbased;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;

public class ForecastFragment extends Fragment {

    public static final String forecastBundleKey = "forecastKey";
    public static final String indexBundleKey = "indexKey";

    private TextView tvCity, tvTemperature, tvWind, tvHumidity, tvPressure;
    private ImageView ivSky;
    private String NO_DATA;

    public static ForecastFragment create(int index, CityForecast forecast) {
        ForecastFragment fragment = new ForecastFragment();

        // Передача параметра
        Bundle args = new Bundle();
        args.putSerializable(forecastBundleKey, forecast);
        args.putInt(indexBundleKey, index);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_forecast_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NO_DATA = getResources().getString(R.string.noData);
        initViews(view);
        renderForecast(getForecast());
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

    /**
     * TODO: Отрисовать прогноз на экране
     */
    private void renderForecast(CityForecast forecast) {

        // TODO: Weather image
        ivSky.setImageResource(forecast.getImgId());

        //TODO: City (Uppercase first letter)
        String city = forecast.getCity();
        tvCity.setText(city.substring(0, 1).toUpperCase() + city.substring(1).toLowerCase());

        // TODO: Temperature
        Formatter fmt = new Formatter();
        fmt.format("%+2d \u2103", forecast.getTemperature());
        tvTemperature.setText(fmt.toString());
        fmt.close();

        // TODO: Wind
        fmt = new Formatter();
        fmt.format("%2d %s", forecast.getWind(), getResources().getString(R.string.infoWind));
        tvWind.setText(fmt.toString());
        fmt.close();

        // TODO: Humidity
        fmt = new Formatter();
        fmt.format("%2d %%", forecast.getHumidity());
        tvHumidity.setText(fmt.toString());
        fmt.close();

        // TODO: Pressure
        Locale current = getResources().getConfiguration().locale;
        boolean isRu = current.getCountry() == "RU";
        fmt = new Formatter();
        fmt.format("%4d %s", (int) forecast.getPressure(isRu), getResources().getString(R.string.infoPressure));
        tvPressure.setText(fmt.toString());
        fmt.close();
    }

}