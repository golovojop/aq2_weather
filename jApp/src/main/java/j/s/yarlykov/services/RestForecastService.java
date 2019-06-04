package j.s.yarlykov.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.network.api.OpenWeatherProvider;
import j.s.yarlykov.data.network.model.WeatherResponseModel;
import j.s.yarlykov.data.provider.ForecastProvider;
import j.s.yarlykov.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestForecastService extends Service {

    // Для работы с SharedPreferences
    private final String temperature = "temp";
    private final String wind = "wind";
    private final String pressure = "pressure";
    private final String humidity = "humidity";
    private final String timeStamp = "time";
    private final String iconId = "icon";

    private WeatherResponseModel model = null;

    private final IBinder mBinder = new RestForecastService.ServiceBinder();

    public interface RestForecastReceiver {
        void onForecastOnline(Forecast forecastOnline);
        void onForecastOffline(Forecast forecastOffline);
        void onIconReady();
    }

    public void requestForecast(final RestForecastService.RestForecastReceiver receiver,
                                final String city) {

        OpenWeatherProvider.getInstance().getApi().loadWeather(
                city + ",ru",
                "2173331fd3e3226666b27e71abc27974",
                "metric")
                .enqueue(new Callback<WeatherResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponseModel> call,
                                           @NonNull Response<WeatherResponseModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            model = response.body();

                            CityForecast cf = new CityForecast(
                                    model.name,
                                    0,
                                    (int) model.main.temp,
                                    model.wind.speed,
                                    model.main.humidity,
                                    model.main.pressure);

                            saveForecast(cf);
                            receiver.onForecastOnline(cf);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponseModel> call, Throwable t) {
                        receiver.onForecastOffline(loadForecast(city));
                    }
                });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class ServiceBinder extends Binder {
        public RestForecastService getService() {
            return RestForecastService.this;
        }
    }

    // Сохранить прогноз
    private void saveForecast(CityForecast cf) {
        SharedPreferences shPrefs = getSharedPreferences(cf.getCity(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPrefs.edit();

        editor.putLong(timeStamp, cf.getTimestamp());
        editor.putInt(temperature, cf.getTemperature());
        editor.putFloat(wind, cf.getWind());
        editor.putInt(pressure, cf.getPressure(Utils.isRu()));
        editor.putInt(humidity, cf.getHumidity());
        editor.putInt(iconId, cf.getImgId());
        editor.apply();
    }

    private CityForecast loadForecast(String city) {
        SharedPreferences shPrefs = getSharedPreferences(city, Context.MODE_PRIVATE);
        if (shPrefs.contains(timeStamp)) {
            return new CityForecast(city,
                    shPrefs.getInt(iconId, 0),
                    shPrefs.getInt(temperature, 0),
                    shPrefs.getFloat(wind, 0f),
                    shPrefs.getInt(humidity, 0),
                    shPrefs.getInt(pressure, 0),
                    shPrefs.getLong(timeStamp, 0));
        }
        return null;
    }
}
