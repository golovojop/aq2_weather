package k.s.yarlykov.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ForecastService: Service() {

    val mBinder = ServiceBinder()

    inner class ServiceBinder: Binder() {
        fun getService(): ForecastService = this@ForecastService
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

}