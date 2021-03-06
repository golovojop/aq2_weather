package k.s.yarlykov.data.network.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeatherModel {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("main")
    var main: String? = null
    @SerializedName("description")
    var description: String? = null
    @SerializedName("icon")
    @Expose
    var icon: String? = null
}