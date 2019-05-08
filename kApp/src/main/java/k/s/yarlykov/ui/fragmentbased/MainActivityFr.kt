package k.s.yarlykov.ui.fragmentbased

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import k.s.yarlykov.R
import android.view.MenuItem

class MainActivityFr : AppCompatActivity() {
    var currentFragment = ""

    companion object {
        private val FCLASS_NAME = MainActivityFr::class.java.simpleName + ".extra.FCLASS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fr)

        // Первый старт
        if(savedInstanceState == null) {
            loadFragment(CitiesFragment::class.java.name, R.id.mainFrame)
        } else {
            loadFragment(
                    savedInstanceState.getSerializable(FCLASS_NAME) as String,
                    containerId = R.id.mainFrame)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(k.s.yarlykov.R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.actionAbout -> {
                loadFragment(InfoFragment::class.java.name, R.id.mainFrame)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putSerializable(FCLASS_NAME, currentFragment)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Загрузить нужный фрагмент
     */
    private fun loadFragment(clazz: String = CitiesFragment::class.java.name, containerId: Int) {
        val fragment = Class.forName(clazz).newInstance()

        if(fragment is Fragment) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack("")
                    .commit()
        }
        currentFragment = clazz
    }
}