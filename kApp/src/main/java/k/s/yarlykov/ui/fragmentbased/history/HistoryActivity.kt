/**
 * Materials:
 * Показывать AppName только когда ToolBar в развернутом состоянии
 * http://qaru.site/questions/62329/show-collapsingtoolbarlayout-title-only-when-collapsed
 */

package k.s.yarlykov.ui.fragmentbased.history

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import k.s.yarlykov.R
import k.s.yarlykov.data.provider.HistoryProvider
import k.s.yarlykov.ui.fragmentbased.InfoActivityFr
import kotlinx.android.synthetic.main.activity_history.*
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class HistoryActivity : AppCompatActivity() {
    companion object {

        private val EXTRA_HISTORY = HistoryActivity::class.java.simpleName + ".extra.HISTORY"
        private val DAYS = 3
        private var customState: CustomState? = null

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

        val city = intent.getSerializableExtra(EXTRA_HISTORY) as String

        val isNotRestored: Boolean = customState?.let {
            it.lastCity != city
        } ?: true

        initViews(city, isNotRestored)

        if(isNotRestored) {
            customState = CustomState(city, LoadTask()).apply {
                loadTask.bind(this@HistoryActivity)
                loadTask.execute(progressBar.max)
            }
        } else {
            pbContainer.visibility = View.GONE
            rvHistory.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        customState?.loadTask?.unbind()
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

    private fun initViews(city: String, isNotRestored: Boolean) {
        tvCity.text = city

        // Загрузить картинку фона для элемента CollapsingToolbarLayout
        resources.obtainTypedArray(R.array.historyBg).apply {
            collapsingToolbar.background = ContextCompat.getDrawable(
                    this@HistoryActivity,
                    getResourceId(Random.nextInt(0, length()), 0))
            recycle()
        }

        // Определить ориентацию и соотв LayoutManager
        val orientationCompatibleLayoutManager = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> LinearLayoutManager(this@HistoryActivity)
            else -> GridLayoutManager(this@HistoryActivity, 2)
        }

        // Сгенерить историю в RecycleView
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

    /**
     * Класс для сохранения состояния
     */
    data class CustomState (val lastCity: String, val loadTask: LoadTask)

    /**
     * Task загрузки истории. Эмулирует долгую работу
     */
    class LoadTask: AsyncTask<Int, Int, Boolean>() {

        var activity: HistoryActivity? = null

        fun bind(ha: HistoryActivity) {
            activity = ha
        }

        fun unbind() {
            activity = null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            activity?.let {
                it.rvHistory.visibility = View.GONE
                it.pbContainer.visibility = View.VISIBLE
            }
        }

        override fun doInBackground(vararg params: Int?): Boolean  {
            for(i in 10..params[0]!! step 5) {
                TimeUnit.MILLISECONDS.sleep(100)
                publishProgress(i)
            }
            return true
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            activity?.let {
                it.progressBar.progress = values[0]!!
            }
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            activity?.let {
                it.pbContainer.visibility = View.GONE
                it.rvHistory.visibility = View.VISIBLE
                unbind()
            }
        }
    }
}
