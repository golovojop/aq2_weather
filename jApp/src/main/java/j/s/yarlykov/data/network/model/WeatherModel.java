package j.s.yarlykov.data.network.model;

import com.google.gson.annotations.SerializedName;

public class WeatherModel {
    @SerializedName("id") public int id;
    @SerializedName("main") public String main;
    @SerializedName("description") public String description;
    @SerializedName("icon") public String icon;
}