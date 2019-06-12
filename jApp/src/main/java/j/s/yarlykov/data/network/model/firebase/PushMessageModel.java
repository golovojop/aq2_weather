package j.s.yarlykov.data.network.model.firebase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushMessageModel {
    @SerializedName("to")
    @Expose
    public String to;
    @SerializedName("data")
    @Expose
    public PushDataModel data;

    public PushMessageModel(String to, PushDataModel data) {
        this.to = to;
        this.data = data;
    }
}