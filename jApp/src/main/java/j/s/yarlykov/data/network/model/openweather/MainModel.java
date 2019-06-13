package j.s.yarlykov.data.network.model.openweather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainModel {
    @SerializedName("temp")
    @Expose
    public float temp;
    @SerializedName("pressure")
    @Expose
    public int pressure;
    @SerializedName("humidity")
    @Expose
    public int humidity;
    @SerializedName("temp_min") public float tempMin;
    @SerializedName("temp_max") public float tempMax;
}
