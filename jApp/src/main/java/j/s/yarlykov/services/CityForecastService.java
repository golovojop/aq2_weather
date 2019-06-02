package j.s.yarlykov.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.TimeUnit;

import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.provider.ForecastProvider;

public class CityForecastService extends Service {

    private final IBinder mBinder = new ServiceBinder();

    public interface ForecastReceiver {
        void onForecastReady(Forecast forecast);
    }

    public void requestForecast(final ForecastReceiver receiver, final String city) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ForecastProvider provider = ForecastProvider.getInstance();
                Forecast f = null;

                // Ждать подключения к интернет
                while(f == null) {
                    f = provider.getRealForecast(getApplicationContext(), city);
                    if(f != null) break;

                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                receiver.onForecastReady(f);
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
}
