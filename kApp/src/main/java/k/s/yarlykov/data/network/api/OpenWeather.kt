package k.s.yarlykov.data.network.api

import k.s.yarlykov.data.network.model.WeatherResponseModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface OpenWeather {
    @GET("data/2.5/weather")
    fun loadWeather(@Query("q") city: String,
                    @Query("appid") keyApi: String,
                    @Query("units") units: String): Call<WeatherResponseModel>

    @GET
    fun fetchIcon(@Url url: String): Call<ResponseBody>
}
