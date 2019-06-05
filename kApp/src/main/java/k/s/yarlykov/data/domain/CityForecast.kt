package k.s.yarlykov.data.domain

import java.util.*

class CityForecast(imgId: Int,
                   temperature: Int,
                   wind: Float,
                   humidity: Int,
                   pressureMm: Int,
                   val city: String,
                   val timeStamp: Long = Date().time):
        Forecast(imgId, temperature, wind, humidity, pressureMm) {

    constructor(f: Forecast,
                city: String):
            this(f.imgId, f.temperature, f.wind, f.humidity, f.pressureMm, city)

    constructor(f: Forecast,
                city: String,
                timeStamp: Long):
            this(f.imgId, f.temperature, f.wind, f.humidity, f.pressureMm, city, timeStamp)

    companion object {
        enum class MeteoData {
            WIND, HUMIDITY, PRESSURE
        }
    }

    override fun getPressure(isMm: Boolean): Int {
        if (pressureMm != EMPTY_VAL.toInt()) {
            return if (isMm) pressureMm else mmToMb(pressureMm)
        }
        return EMPTY_VAL.toInt()
    }
}