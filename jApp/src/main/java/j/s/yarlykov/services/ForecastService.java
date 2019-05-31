package j.s.yarlykov.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.provider.ForecastProvider;
import j.s.yarlykov.ui.fragmentbased.CitiesFragment;

public class ForecastService extends Service implements CitiesFragment.ForecastSource {

    private final IBinder mBinder = new ServiceBinder();

    @Override
    public Forecast getForecastById(int id) {
        ForecastProvider forecastProvider = ForecastProvider.getInstance();
        return forecastProvider.getForecastByIndex(id);
    }

    @Override
    public Forecast getForecastByCity(String city) {
        return null;
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
        public ForecastService getService() {
            return ForecastService.this;
        }
    }
}
