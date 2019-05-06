package k.s.yarlykov.data.domain

class CityForecast(val city: String, imgId: Int, temperature: Int, wind: Int, humidity: Int, pressureMm: Int):
        Forecast(imgId, temperature, wind, humidity, pressureMm) {

    companion object {
        enum class MeteoData {
            WIND, HUMIDITY, PRESSURE
        }
    }

    constructor(city: String, f: Forecast) : this(city, f.imgId, f.temperature, f.wind, f.humidity, f.pressureMm)

    override fun getPressure(isMm: Boolean): Int {
        if(pressureMm != EMPTY_VAL) {
            return if(isMm) pressureMm else mmToMb(pressureMm)
        }
        return EMPTY_VAL
    }

    /**
     * TODO: Удалить не требуемые данные
     */
    fun clearUnused(interestingSet: Set<MeteoData>): CityForecast {
        if(!interestingSet.contains(MeteoData.WIND)) wind = EMPTY_VAL;
        if(!interestingSet.contains(MeteoData.HUMIDITY)) humidity = EMPTY_VAL;
        if(!interestingSet.contains(MeteoData.PRESSURE)) pressureMm = EMPTY_VAL;
        return this
    }
}