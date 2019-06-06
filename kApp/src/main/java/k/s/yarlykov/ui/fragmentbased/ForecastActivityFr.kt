package k.s.yarlykov.ui.fragmentbased

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast
import k.s.yarlykov.services.RestForecastService

class ForecastActivityFr : AppCompatActivity() {

    lateinit var forecast: CityForecast

    companion object {
        private val EXTRA_FORECAST = ForecastActivityFr::class.java.simpleName + ".extra.FORECAST"
        val cityBundleKey = "cityKey"
        val binderBundleKey = "binderKey"
        val indexBundleKey = "indexKey"

        fun start(context: Context?, forecast: CityForecast) {
            val intent = Intent(context, ForecastActivityFr::class.java).apply {
                putExtra(EXTRA_FORECAST, forecast)
            }
            context?.startActivity(intent)
        }

        fun start(context: Context?, binder: IBinder?, city: String, index: Int) {
            context?.startActivity(Intent(context, ForecastActivityFr::class.java).also {intent ->
                intent.putExtras(Bundle().also {bundle ->
                    bundle.putBinder(binderBundleKey, binder)
                    bundle.putString(cityBundleKey, city)
                    bundle.putInt(indexBundleKey, index)
                })
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_fr)

        // Продолжать работать только в портретной ориентации
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish()
            return
        }

        if(savedInstanceState == null) {
            val extras = intent.extras
            val service = extras.getBinder(binderBundleKey)
            val city = extras.getString(cityBundleKey)
            val index = extras.getInt(indexBundleKey)

            val forecastFragment = ForecastFragment.create(service, city, index)

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.rightFrame, forecastFragment).commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(k.s.yarlykov.R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionAbout -> {
                InfoActivityFr.start(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

