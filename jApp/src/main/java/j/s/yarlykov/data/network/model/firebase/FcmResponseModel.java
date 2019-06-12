package j.s.yarlykov.data.network.model;

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
    public List<FcmResultModel> results = null;