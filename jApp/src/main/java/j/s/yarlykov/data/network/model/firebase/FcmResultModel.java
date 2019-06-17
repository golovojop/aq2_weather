package j.s.yarlykov.data.network.model.firebase;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FcmResultModel {
    @SerializedName("message_id")
    @Expose
    public String messageId;
}
