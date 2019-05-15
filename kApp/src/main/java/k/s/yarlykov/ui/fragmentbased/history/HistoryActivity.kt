package k.s.yarlykov.ui.fragmentbased.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import k.s.yarlykov.R
import k.s.yarlykov.data.provider.HistoryProvider
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    companion object {

        private val EXTRA_HISTORY = HistoryActivity::class.java.simpleName + ".extra.HISTORY"


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

        rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryActivity)
//            adapter = HistoryRVAdapter(listOf(
//                    History("""10/05""", R.drawable.h_cloud, "19 ~ 5"),
//                    History("""11/05""", R.drawable.h_sun, "21 ~ 8"),
//                    History("""12/05""", R.drawable.h_cloud_sun, "20 ~ 7"),
//                    History("""13/05""", R.drawable.h_roar, "17 ~ 4")))

            adapter = HistoryRVAdapter(HistoryProvider.build(10))
        }
    }
}
