package j.s.yarlykov.data.network.api;

import j.s.yarlykov.data.network.model.WeatherResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<WeatherResponseModel> loadWeather(@Query("q") String city,
                                           @Query("appid") String keyApi,
                                           @Query("units") String units);
    @GET
    Call<ResponseBody> fetchIcon(@Url String url);
}
