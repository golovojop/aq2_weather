package j.s.yarlykov.data.network.model.openweather.geo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    public double temp;
    @SerializedName("humidity")
    @Expose
    public int humidity;
    @SerializedName("pressure")
    @Expose
    public int pressure;
    @SerializedName("temp_min")
    @Expose
    public double tempMin;
    @SerializedName("temp_max")
    @Expose
    public double tempMax;

}