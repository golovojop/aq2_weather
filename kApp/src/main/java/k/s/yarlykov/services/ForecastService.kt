package k.s.yarlykov.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import k.s.yarlykov.data.domain.Forecast
import k.s.yarlykov.data.provider.ForecastProvider
import k.s.yarlykov.ui.fragmentbased.CitiesFragment

class ForecastService: Service(), CitiesFragment.ForecastSource {

    val mBinder = ServiceBinder()

    inner class ServiceBinder: Binder() {
        fun getService(): ForecastService = this@ForecastService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun getForecastById(id: Int): Forecast = ForecastProvider.getForecastByIndex(id)
    override fun getForecastByCity(city: String): Forecast? = null
}