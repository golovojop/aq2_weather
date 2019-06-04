package j.s.yarlykov.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.provider.ForecastProvider;
import j.s.yarlykov.util.Utils;

public class CityForecastService extends Service {

    // Для работы с SharedPreferences
    private final String temperature = "temp";
    private final String wind = "wind";
    private final String pressure = "pressure";
    private final String humidity = "humidity";
    private final String timeStamp = "time";
    private final String iconId = "icon";
    private final int attempts = 3;

    private final IBinder mBinder = new ServiceBinder();

    public interface ForecastReceiver {
        void onForecastReady(Forecast forecast);
    }

    public void requestForecast(final ForecastReceiver receiver, final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ForecastProvider provider = ForecastProvider.getInstance();
                CityForecast cf = null;
                boolean success = false;

                // Три попытки подучить данные онлайн
                for(int i = 0; i < attempts; i++) {
                    cf = provider.getRealForecast(getApplicationContext(), city);

                    if (cf != null) {
                        saveForecast(cf);
                        success = true;
                        break;
                    } else {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Получить данные офлайн
                if(!success) {
                    cf = loadForecast(city);
                }

                receiver.onForecastReady(cf);
            }
        }).start();
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
        public CityForecastService getService() {
            return CityForecastService.this;
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
