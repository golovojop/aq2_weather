package j.s.yarlykov.data.network.model.openweather;

import com.google.gson.annotations.SerializedName;

public class CoordModel {
    @SerializedName("lon") public float lon;
    @SerializedName("lat") public float lat;
}
