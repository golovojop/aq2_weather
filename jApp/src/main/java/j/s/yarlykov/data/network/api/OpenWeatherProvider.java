package j.s.yarlykov.data.network.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OpenWeatherProvider {

    private static OpenWeatherProvider instance = null;
    private OpenWeather api;

    private OpenWeatherProvider() {
        api = createAdapter();
    }

    public static OpenWeatherProvider getInstance() {
        if(instance == null) {
            instance = new OpenWeatherProvider();
        }
        return instance;
    }

    public OpenWeather getApi() {
        return api;
    }

    private static OpenWeather createAdapter(){

        // Установить таймауты
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();

        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return adapter.create(OpenWeather.class);
    }
}
