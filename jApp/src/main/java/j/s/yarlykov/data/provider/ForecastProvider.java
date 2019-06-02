package j.s.yarlykov.data.provider;

import android.content.Context;
import android.content.res.Resources;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.network.WeatherDataLoader;
import j.s.yarlykov.util.Utils;

import static j.s.yarlykov.data.domain.CityForecast.*;

public class ForecastProvider {

    private static ForecastProvider instance;

    public static ForecastProvider getInstance() {
        if(instance == null) {
            instance = new ForecastProvider();
        }
        return instance;
    }

    public CityForecast getRealForecast(Context context, String city) {
        final JSONObject jsonObject = WeatherDataLoader.getJSONData(city);

        if(jsonObject != null) {
            try {
                JSONObject details = jsonObject.getJSONArray("weather").getJSONObject(0);
                JSONObject main = jsonObject.getJSONObject("main");
                JSONObject wind = jsonObject.getJSONObject("wind");

                return new CityForecast(
                        extractPlaceName(jsonObject),
                        extractIconResourceId(context, details),
                        (int)extractCurrentTemp(main),
                        (int)extractWindSpeed(wind),
                        extractHumidity(main),
                        extractPressure(main)
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String extractPlaceName(JSONObject jsonObject) throws JSONException {
        return jsonObject.getString("name");
    }

    private double extractCurrentTemp(JSONObject main) throws JSONException {
        return main.getDouble("temp");
    }

    private int extractHumidity(JSONObject main)throws JSONException {
        return main.getInt("humidity");
    }

    private int extractPressure(JSONObject main)throws JSONException {
        return main.getInt("pressure");
    }

    private double extractWindSpeed(JSONObject wind)throws JSONException {
        return wind.getDouble("speed");
    }

    private String extractWindDirect(JSONObject wind)throws JSONException {
        int deg = wind.getInt("deg");
        return "Stub";
    }

    private int extractIconResourceId(Context context, JSONObject details) throws JSONException {
        String icon = "ow" + details.getString("icon");
        Resources resources = context.getResources();
        return resources.getIdentifier(icon, "drawable",
                context.getPackageName());

    }

    /**
     * Старые функции для работы со статическими данными
     */

    private List<Forecast> forecasts;

    private ForecastProvider() {
        forecasts = Arrays.asList(
                new Forecast(R.drawable.rain,12, 10, 89, 750),
                new Forecast(R.drawable.sunny,25, 5, 73, 771),
                new Forecast(R.drawable.sun,18, 4, 85, 769),
                new Forecast(R.drawable.snow, -3, 1, 59, 741),
                new Forecast(R.drawable.cloud2, 7, 2, 51, 749),
                new Forecast(R.drawable.cloud1, 11, 6, 64, 742)
        );
    }

    /**
     * TODO: Получить полный прогноз для города city.
     * TODO: Использовался в первом ДЗ.
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
     * TODO: Получить прогноз по номеру. Номер "сворачивается", если
     * TODO: превышает размер списка прогнозов.
     */
    public Forecast getForecastByIndex(int num) {
        int index = Math.abs(num);
        if (index >= forecasts.size()) index = forecasts.size() % index;
        return forecasts.get(index);
    }

    /**
     * TODO: Сгенерить рандомный индекс массива
     */
    private int index(){
        Random randomForecast = new Random();
        return randomForecast.nextInt(forecasts.size());
    }


}
