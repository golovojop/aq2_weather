package k.s.yarlykov.data.provider

import k.s.yarlykov.R
import k.s.yarlykov.data.domain.History
import k.s.yarlykov.extensions.daysAgo
import java.util.*

object HistoryProvider {

    // Генерит прогноз за последние daysAgo дней
    fun build(daysAgo: Int): List<History> {
        val list = mutableListOf<History>()
        val today = Date()

        for(i in 1..daysAgo) {
            list.add(History(today.daysAgo(i), R.drawable.h_cloud_sun, "25 ~ 10"))
        }
        return list
    }

//    private fun index(): Int {
//        val randomForecast = Random()
//        return randomForecast.nextInt(ForecastProvider.forecasts.size)
//    }
}