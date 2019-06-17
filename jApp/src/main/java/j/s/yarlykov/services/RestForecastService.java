package j.s.yarlykov.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;

import j.s.yarlykov.data.db.TabForecast;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.network.api.OpenWeatherProvider;
import j.s.yarlykov.data.network.model.openweather.WeatherResponseModel;
import j.s.yarlykov.util.Utils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestForecastService extends Service {

    // Для работы с SharedPreferences
    private final String country = "country";
    private final String temperature = "temp";
    private final String wind = "wind";
    private final String pressure = "pressure";
    private final String humidity = "humidity";
    private final String iconId = "icon";

    private WeatherResponseModel model = null;

    private final IBinder mBinder = new RestForecastService.ServiceBinder();

    // Интерфейс должен быть реализован фрагментом, чтобы получать
    // результаты запросов
    public interface RestForecastReceiver {
        void onForecastOnline(CityForecast forecastOnline, Bitmap icon);
        void onForecastOffline(CityForecast forecastOffline);
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

    // Запросить текущий прогноз по имени города (city)
    public void requestForecast(final RestForecastService.RestForecastReceiver receiver,
                                final String city, final String country,
                                final SQLiteDatabase db) {

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
                                    model.sys.country,
                                    (int) model.main.temp,
                                    fetchIconId(getApplicationContext(), model.weather[0].icon),
                                    model.wind.speed,
                                    model.main.humidity,
                                    model.main.pressure);

                            dbSaveForecast(cf, db);
                            requestIcon(receiver, model.weather[0].icon, cf, db);

                        } else {
                            onFailure(call, new Throwable(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponseModel> call, Throwable t) {
                        receiver.onForecastOffline(dbLoadForecast(city, db));
                    }
                });
    }

    // Запросить текущий прогноз погоды по координатам
    public void requestForecast(final RestForecastService.RestForecastReceiver receiver,
                                final int lat, final int lon,
                                final SQLiteDatabase db) {

        OpenWeatherProvider.getInstance().getApi()
                .loadGeoWeather(
                        lat, lon,
                        "metric", "2173331fd3e3226666b27e71abc27974")
                .enqueue(new Callback<WeatherResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherResponseModel> call,
                                           @NonNull Response<WeatherResponseModel> response) {

                        // Code 404: body() == null; isSuccessful() == false
                        if (response.body() != null && response.isSuccessful()) {
                            model = response.body();
                            CityForecast cf = new CityForecast(
                                    model.name,
                                    model.sys.country,
                                    (int) model.main.temp,
                                    fetchIconId(getApplicationContext(), model.weather[0].icon),
                                    model.wind.speed,
                                    model.main.humidity,
                                    model.main.pressure);

                            dbSaveForecast(cf, db);
                            requestIcon(receiver, model.weather[0].icon, cf, db);

                        } else {
                            onFailure(call, new Throwable(response.message()));
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponseModel> call, Throwable t) {
                        receiver.onForecastOffline(dbLoadForecast("local", db));
                    }
                });
    }


    // Получить иконку погоды
    private void requestIcon(final RestForecastService.RestForecastReceiver receiver,
                             final String icon, final CityForecast cf,
                             final SQLiteDatabase db) {

        Call<ResponseBody> call = OpenWeatherProvider.getInstance().getApi()
                .fetchIcon(String.format("https://openweathermap.org/img/w/%s.png", icon));

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
                receiver.onForecastOffline(dbLoadForecast(cf.getCity(), db));
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

    /**
     * Методы для работы с SQLite
     */
    private void dbSaveForecast(CityForecast cf, SQLiteDatabase db) {
        if(TabForecast.editForecast(cf, db) == 0) {
            TabForecast.addForecast(cf, db);
        }
    }

    private CityForecast dbLoadForecast(String city, SQLiteDatabase db) {
        return TabForecast.getCityForecast(city, db);
    }

    /**
     * Методы для работы с SharedPreferences
     */

    // Сохранить прогноз в SharedPreferences
    private void spSaveForecast(CityForecast cf) {
        SharedPreferences shPrefs = getSharedPreferences(cf.getCity(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPrefs.edit();

        editor.putString(country, cf.getCountry());
        editor.putInt(temperature, cf.getTemperature());
        editor.putInt(iconId, cf.getIcon());
        editor.putFloat(wind, cf.getWind());
        editor.putInt(humidity, cf.getHumidity());
        editor.putInt(pressure, cf.getPressure(Utils.isRu()));
        editor.apply();
    }

    // Загрузить прогноз из SharedPreferences
    private CityForecast spLoadForecast(String city) {
        SharedPreferences shPrefs = getSharedPreferences(city, Context.MODE_PRIVATE);
        if (shPrefs.contains(country)) {

            return new CityForecast(city,
                    shPrefs.getString(country, "ru"),
                    shPrefs.getInt(temperature, 0),
                    shPrefs.getInt(iconId, 0),
                    shPrefs.getFloat(wind, 0f),
                    shPrefs.getInt(humidity, 0),
                    shPrefs.getInt(pressure, 0));
        }
        return null;
    }
}
