package k.s.yarlykov.ui.fragmentbased

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import k.s.yarlykov.R
import k.s.yarlykov.data.domain.CityForecast

class ForecastActivityFr : AppCompatActivity() {

    lateinit var forecast: CityForecast

    companion object {
        private val EXTRA_FORECAST = ForecastActivityFr::class.java.simpleName + ".extra.FORECAST"

        fun start(context: Context?, forecast: CityForecast) {
            val intent = Intent(context, ForecastActivityFr::class.java).apply {
                putExtra(EXTRA_FORECAST, forecast)
            }
            context?.startActivity(intent)
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
            forecast = intent.getSerializableExtra(EXTRA_FORECAST) as CityForecast

            val forecastFragment = ForecastFragment.create(0, forecast)

            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.forecastContainer, forecastFragment).commit()
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

