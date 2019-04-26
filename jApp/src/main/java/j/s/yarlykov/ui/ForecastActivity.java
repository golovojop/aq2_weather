package j.s.yarlykov.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;

public class ForecastActivity extends AppCompatActivity {

    public static final String EXTRA_FORECAST = ForecastActivity.class.getSimpleName() + ".extra.FORECAST";

    private String NO_DATA;
    private CityForecast forecast;
    private TextView tvCity, tvTemperature, tvWind, tvHumidity, tvPressure;
    private ImageView ivSky;

    public static void start(Context context, CityForecast forecast) {
        Intent intent = new Intent(context, ForecastActivity.class);
        intent.putExtra(EXTRA_FORECAST, forecast);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        forecast = (CityForecast) getIntent().getSerializableExtra(EXTRA_FORECAST);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderForecast();
    }

    /**
     * TODO: Find views
     */
    private void initView() {
        NO_DATA = getResources().getString(R.string.noData);
        ivSky = findViewById(R.id.iv_sky);
        tvCity = findViewById(R.id.tv_city);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvWind = findViewById(R.id.tv_wind);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvPressure = findViewById(R.id.tv_pressure);
    }

    /**
     * TODO: Отрисовать прогноз на экране
     */
    private void renderForecast() {

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
        if(forecast.getWind() != CityForecast.EMPTY_VAL) {
            fmt = new Formatter();
            fmt.format("%2d %s", forecast.getWind(), getResources().getString(R.string.infoWind));
            tvWind.setText(fmt.toString());
            fmt.close();
        } else {
            tvWind.setText(NO_DATA);
        }

        // TODO: Humidity
        if(forecast.getHumidity() != CityForecast.EMPTY_VAL) {
            fmt = new Formatter();
            fmt.format("%2d %%", forecast.getHumidity());
            tvHumidity.setText(fmt.toString());
            fmt.close();
        } else {
            tvHumidity.setText(NO_DATA);
        }

        // TODO: Pressure
        Locale current = getResources().getConfiguration().locale;
        boolean isRu = current.getCountry() == "RU";
        if(forecast.getPressure(isRu) != CityForecast.EMPTY_VAL) {
            fmt = new Formatter();
            fmt.format("%4d %s", (int) forecast.getPressure(isRu), getResources().getString(R.string.infoPressure));
            tvPressure.setText(fmt.toString());
            fmt.close();
        } else {
            tvPressure.setText(NO_DATA);
        }
    }
}
