package k.s.yarlykov.data.network.model

import com.google.gson.annotations.SerializedName

class WindModel {
    @SerializedName("speed")
    var speed: Float = 0f
    @SerializedName("deg")
    var deg: Float = 0f
}