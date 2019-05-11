package k.s.yarlykov.ui.fragmentbased

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import k.s.yarlykov.R

class InfoActivityFr: AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, InfoActivityFr::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_info_fr)
    }
}