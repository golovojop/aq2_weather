package j.s.yarlykov.data.network.api;

import j.s.yarlykov.data.network.model.WeatherResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherResponseModel> loadWeather(@Query("q") String city,
                                           @Query("appid") String keyApi,
                                           @Query("units") String units);
}
