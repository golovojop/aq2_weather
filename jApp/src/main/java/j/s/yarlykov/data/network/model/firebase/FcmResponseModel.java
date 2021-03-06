package j.s.yarlykov.data.network.model.firebase;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FcmResponseModel {

    @SerializedName("multicast_id")
    @Expose
    public Integer multicastId;
    @SerializedName("success")
    @Expose
    public Integer success;
    @SerializedName("failure")
    @Expose
    public Integer failure;
    @SerializedName("canonical_ids")
    @Expose
    public Integer canonicalIds;
    @SerializedName("results")
    @Expose
    public List<FcmResultModel> results;

    public FcmResponseModel(Integer multicastId, Integer success, Integer failure,
                            Integer canonicalIds, List<FcmResultModel> results) {
        this.multicastId = multicastId;
        this.success = success;
        this.failure = failure;
        this.canonicalIds = canonicalIds;
        this.results = results;
    }
}