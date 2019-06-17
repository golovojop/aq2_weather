package j.s.yarlykov.data.network.model.openweather.geo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    @Expose
    public Integer lon;
    @SerializedName("lat")
    @Expose
    public Integer lat;

}