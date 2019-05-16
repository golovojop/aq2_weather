package k.s.yarlykov.ui.fragmentbased.history

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import k.s.yarlykov.R
import k.s.yarlykov.data.provider.HistoryProvider
import k.s.yarlykov.ui.fragmentbased.InfoActivityFr
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    companion object {

        private val EXTRA_HISTORY = HistoryActivity::class.java.simpleName + ".extra.HISTORY"
        private val WEEK = 7

        fun start(context: Context, city: String) {
            val intent = Intent(context, HistoryActivity::class.java).apply {
                putExtra(EXTRA_HISTORY, city)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        tvCity.text = intent.getSerializableExtra(EXTRA_HISTORY) as String

        val orientationCompatibleLayoutManager = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(this@HistoryActivity)
            else -> GridLayoutManager(this@HistoryActivity, 2)
        }

        rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = orientationCompatibleLayoutManager
            adapter = HistoryRVAdapter(HistoryProvider.build(this@HistoryActivity, WEEK))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionAbout -> {
                InfoActivityFr.start(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
