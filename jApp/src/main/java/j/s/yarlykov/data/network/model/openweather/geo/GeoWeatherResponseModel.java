package j.s.yarlykov.data.network.model.openweather.geo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoWeatherResponseModel {

    @SerializedName("coord")
    @Expose
    public Coord coord;
    @SerializedName("sys")
    @Expose
    public Sys sys;
    @SerializedName("weather")
    @Expose
    public List<Weather> weather = null;
    @SerializedName("main")
    @Expose
    public Main main;
    @SerializedName("wind")
    @Expose
    public Wind wind;
    @SerializedName("rain")
    @Expose
    public Rain rain;
    @SerializedName("clouds")
    @Expose
    public Clouds clouds;
    @SerializedName("dt")
    @Expose
    public Integer dt;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("cod")
    @Expose
    public Integer cod;

}