package j.s.yarlykov.data.network.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponseModel {
    @SerializedName("coord") public CoordModel coordinates;
    @SerializedName("weather") public WeatherModel[] weather;
    @SerializedName("base") public String base;
    @SerializedName("main") public MainModel main;
    @SerializedName("visibility") public int visibility;
    @SerializedName("wind") public WindModel wind;
    @SerializedName("clouds") public CloudsModel clouds;
    @SerializedName("dt") public long dt;
    @SerializedName("sys") public SysModel sys;
    @SerializedName("id") public  long id;
    @SerializedName("name") public String name;
    @SerializedName("cod") public int cod;
}