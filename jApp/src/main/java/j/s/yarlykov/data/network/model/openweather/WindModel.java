package j.s.yarlykov.data.network.model.openweather;

import com.google.gson.annotations.SerializedName;

public class WindModel {
    @SerializedName("speed") public float speed;
    @SerializedName("deg") public float deg;
}