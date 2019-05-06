package k.s.yarlykov.data.provider

import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.data.domain.Forecast
import java.util.*

object ForecastProvider {

    val forecasts = listOf(
        Forecast(R.drawable.rain, 12, 10, 89, 750),
        Forecast(R.drawable.sunny, 25, 5, 73, 771),
        Forecast(R.drawable.sun, 18, 4, 85, 769),
        Forecast(R.drawable.cloud2, -7, 2, 51, 749),
        Forecast(R.drawable.snow, -3, 1, 59, 741),
        Forecast(R.drawable.cloud1, 11, 6, 64, 742)
    )

    fun getForecastFull(city: String) = CityForecast(city, forecasts.get(index()))
    fun getForecastCustom(city: String, request: Set<CityForecast.Companion.MeteoData>) = CityForecast(city, forecasts.get(index())).apply { clearUnused(request) }

    private fun index(): Int {
        val randomForecast = Random()
        return randomForecast.nextInt(forecasts.size)
    }
}