package j.s.yarlykov.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;

import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.network.api.OpenWeatherProvider;
import j.s.yarlykov.data.network.model.WeatherResponseModel;
import j.s.yarlykov.util.Utils;
import okhttp3.ResponseBody;
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
        void onForecastOnline(Forecast forecastOnline, Bitmap icon);

        void onForecastOffline(Forecast forecastOffline);
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

    // Запросить теукщий прогноз погоды для города city
    public void requestForecast(final RestForecastService.RestForecastReceiver receiver,
                                final String city, final String country) {

        OpenWeatherProvider.getInstance().getApi().loadWeather(
                city + "," + country,
                "2173331fd3e3226666b27e71abc27974",
                "metric")
                .enqueue(new Callback<WeatherResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponseModel> call,
                                           @NonNull Response<WeatherResponseModel> response) {

                        // Code 404: body() == null; isSuccessful() == false
                        if (response.body() != null && response.isSuccessful()) {
                            model = response.body();
                            CityForecast cf = new CityForecast(
                                    model.name,
                                    fetchIconId(getApplicationContext(), model.weather[0].icon),
                                    (int) model.main.temp,
                                    model.wind.speed,
                                    model.main.humidity,
                                    model.main.pressure);
                            saveForecast(cf);
                            requestIcon(receiver, model.weather[0].icon, cf);

                        } else {
                            onFailure(call, new Throwable(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponseModel> call, Throwable t) {
                        receiver.onForecastOffline(loadForecast(city));
                    }
                });
    }

    // Получить иконку погоды
    private void requestIcon(final RestForecastService.RestForecastReceiver receiver,
                             final String icon, final CityForecast cf) {

        Call<ResponseBody> call = OpenWeatherProvider.getInstance().getApi()
                .fetchIcon(String.format("http://openweathermap.org/img/w/%s.png", icon));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    receiver.onForecastOnline(cf, bmp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                receiver.onForecastOffline(loadForecast(cf.getCity()));
            }
        });
    }

    // Конвертировать имя иконки в ID ресурса картинки
    private int fetchIconId(Context context, String iconName) {
        String icon = "ow" + iconName;
        Resources resources = context.getResources();
        return resources.getIdentifier(icon, "drawable",
                context.getPackageName());
    }

    // Сохранить прогноз в SharedPreferences
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

    // Загрузить прогноз из SharedPreferences
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
