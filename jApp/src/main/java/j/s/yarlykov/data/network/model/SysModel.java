package j.s.yarlykov.data.network.model;

import com.google.gson.annotations.SerializedName;

class SysModel {
    @SerializedName("type") public int type;
    @SerializedName("id") public int id;
    @SerializedName("message") public float message;
    @SerializedName("country") public String country;
    @SerializedName("sunrise") public long sunrise;
    @SerializedName("sunset") public long sunset;
}
