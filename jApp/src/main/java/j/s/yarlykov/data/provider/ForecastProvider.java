package j.s.yarlykov.data.provider;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.domain.Forecast;
import static j.s.yarlykov.data.domain.CityForecast.*;

public class ForecastProvider {

    List<Forecast> forecasts = Arrays.asList(
            new Forecast(R.drawable.rain,12, 10, 89, 750),
            new Forecast(R.drawable.sunny,25, 5, 73, 771),
            new Forecast(R.drawable.sun,18, 4, 85, 769),
            new Forecast(R.drawable.cloud2, -7, 2, 51, 749),
            new Forecast(R.drawable.snow, -3, 1, 59, 741),
            new Forecast(R.drawable.cloud1, 11, 6, 64, 742)
            );
    /**
     * TODO: Получить полный прогноз для города city
     */
    public CityForecast getForecastFull(String city) {
        return new CityForecast(city, forecasts.get(index()));
    }

    /**
     * TODO: Получить прогноз с интересующими данными
     */
    public CityForecast getForecastCustom(String city, Set<MeteoData> request){
        return new CityForecast(city, forecasts.get(index())).clearUnused(request);
    }

    /**
     * TODO: Сгенерить рандомный индекс массива
     */
    private int index(){
        Random randomForecast = new Random();
        return randomForecast.nextInt(forecasts.size());
    }
}
