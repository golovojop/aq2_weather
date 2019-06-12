package j.s.yarlykov.data.network.model.firebase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import j.s.yarlykov.data.domain.CityForecast;

public class PushDataModel {

    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("temperature")
    @Expose
    public String temperature;
    @SerializedName("wind")
    @Expose
    public String wind;
    @SerializedName("humidity")
    @Expose
    public String humidity;
    @SerializedName("pressure")
    @Expose
    public String pressure;

    public PushDataModel(String city, String country, String temperature,
                         String wind, String humidity, String pressure) {
        this.city = city;
        this.country = country;
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
    }

    public static PushDataModel createFrom(CityForecast cf) {
        return new PushDataModel(
                cf.getCity(),
                cf.getCountry(),
                String.valueOf(cf.getTemperature()),
                String.valueOf(cf.getWind()),
                String.valueOf(cf.getHumidity()),
                String.valueOf(cf.getPressure()));

    }
}
