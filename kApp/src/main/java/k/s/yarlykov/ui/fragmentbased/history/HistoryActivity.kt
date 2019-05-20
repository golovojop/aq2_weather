/**
 * Materials:
 * Показывать AppName только когда ToolBar в развернутом состоянии
 * http://qaru.site/questions/62329/show-collapsingtoolbarlayout-title-only-when-collapsed
 */

package k.s.yarlykov.ui.fragmentbased.history

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import k.s.yarlykov.R
import k.s.yarlykov.data.provider.HistoryProvider
import k.s.yarlykov.ui.fragmentbased.InfoActivityFr
import kotlinx.android.synthetic.main.activity_history.*
import kotlin.random.Random

class HistoryActivity : AppCompatActivity() {
    companion object {

        private val EXTRA_HISTORY = HistoryActivity::class.java.simpleName + ".extra.HISTORY"
        private val DAYS = 3
        private var lastCity = ""
        private var lastBgIdx = 0

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
        setSupportActionBar(toolbar)
        initViews()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        saveLastCity()
    }

    override fun onPause() {
        super.onPause()
        saveLastCity()
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

    private fun initViews() {

        tvCity.text = intent.getSerializableExtra(EXTRA_HISTORY) as String
        val isNotRestored = lastCity != tvCity.text

        // Определить ориентацию
        val orientationCompatibleLayoutManager = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(this@HistoryActivity)
            else -> GridLayoutManager(this@HistoryActivity, 2)
        }

        // Загрузить картинку фона для элемента CollapsingToolbarLayout
        resources.obtainTypedArray(R.array.historyBg).apply {
            collapsingToolbar.background = ContextCompat.getDrawable(
                    this@HistoryActivity,
                    getResourceId(Random.nextInt(0, length()), 0))
            recycle()
        }

        // Сгенерить историю в воде RecycleView
        rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = orientationCompatibleLayoutManager
            adapter = HistoryRVAdapter(HistoryProvider.build(this@HistoryActivity, DAYS, isNotRestored))
            itemAnimator = DefaultItemAnimator()
        }

        // Обработчки клика на FAB
        fab.setOnClickListener {
            HistoryProvider.oneMoreDay(this@HistoryActivity)
            rvHistory.adapter?.notifyDataSetChanged()
        }
    }

    // Сохранить название текущего города
    private fun saveLastCity() {
        lastCity = tvCity.text as String
    }
}
