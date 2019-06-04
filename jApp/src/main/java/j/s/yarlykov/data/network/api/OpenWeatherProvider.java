package j.s.yarlykov.data.network.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherAdapter {

    private static OpenWeatherAdapter instance = null;
    private OpenWeather api;

    private OpenWeatherAdapter() {
    }

    private static OpenWeather createAdapter(){
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(OpenWeather.class);
    }
}
