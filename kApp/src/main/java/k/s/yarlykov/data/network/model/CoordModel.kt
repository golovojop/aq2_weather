package k.s.yarlykov.data.network.model

import com.google.gson.annotations.SerializedName

class CoordModel {
    @SerializedName("lon")
    var lon: Float = 0f
    @SerializedName("lat")
    var lat: Float = 0f
}
