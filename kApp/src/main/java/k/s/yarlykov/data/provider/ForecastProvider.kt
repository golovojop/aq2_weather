package k.s.yarlykov.data.provider

import k.s.yarlykov.R
import java.io.Serializable

class Forecast (
        val imgId: Int,
        internal var temperature: Int,
        internal var wind: Float,
        internal var humidity: Int,
        internal var pressureMm: Int) : Serializable {

    fun mmToMb(mm: Int) = (mm * 1.333f).toInt()
    fun mbToMm(mb: Int) = (mb * 0.75006f).toInt()
}

object ForecastProvider {

    private val forecasts = listOf(
            Forecast(R.drawable.rain, 12, 10f, 89, 750),
            Forecast(R.drawable.sunny, 25, 5f, 73, 771),
            Forecast(R.drawable.sun, 18, 4f, 85, 769),
            Forecast(R.drawable.snow, -3, 1f, 59, 741),
            Forecast(R.drawable.cloud2, 7, 2f, 51, 749),
            Forecast(R.drawable.cloud1, 11, 6f, 64, 742)
    )

    // По индексу из списка
    fun getForecastByIndex(num: Int): Forecast {
        var index = Math.abs(num)
        if (index >= forecasts.size) index = forecasts.size % index
        return forecasts[index]
    }
}