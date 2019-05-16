package k.s.yarlykov.ui.fragmentbased

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import k.s.yarlykov.R

class MainActivityFr : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fr)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            cleanUpMenu(it, R.id.actionWeek)
        }
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

    // Метод требуется для того, чтобы убрать ненужные меню
    // при портретной ориентации.
    private fun cleanUpMenu(menu: Menu, vararg items: Int) {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            invalidateOptionsMenu()
            for(i in items) {
                menu.findItem(i)?.setVisible(false)
            }
        }
    }
}