package k.s.yarlykov.ui.fragmentbased

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import k.s.yarlykov.R
import kotlinx.android.synthetic.main.activity_main_app_bar.*
import kotlinx.android.synthetic.main.activity_main_drawer.*
import kotlinx.android.synthetic.main.activity_main_fr.*

class MainActivityFr : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    val F_KEY = "F_KEY"
    var isLandscape: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drawer)
        setSupportActionBar(toolbarDrawer)
        initSideMenu(toolbarDrawer)

        isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

        // Градиент текста в заголовке Drawer
        val shader = LinearGradient(0f, 0f, 512f, 64f,
                Color.WHITE, Color.BLUE, Shader.TileMode.REPEAT)
        navView.getHeaderView(0).findViewById<TextView>(R.id.appWeather)?.apply {
            paint?.shader = shader
        }

        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction()
            ft.add(R.id.leftFrame, CitiesFragment(),
                    CitiesFragment::class.java.canonicalName).commit()
        } else {
            val fName = savedInstanceState.getString(F_KEY)

            // Если сохраняли имя класса фрагмента, и оно не "CitiesFragment",
            // то скрыть правую панель. Далее система сама восстановит последний
            // фрагмент в левой панели и он займет все окно.
            //
            // Если fName == "CitiesFragment", то система восстановит его в левой панели
            // а в процессе запуска он сделает видимой правую панель.
            fName?.let {
                if (it != CitiesFragment::class.java.canonicalName && isLandscape) {
                    rightFrame?.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.let {
            cleanUpMenu(it, R.id.actionWeek)
        }
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.itemId) {
            R.id.nav_feedback -> renderFragment(FeedbackFragment.create())
            R.id.nav_developer -> renderFragment(DevInfoFragment.create())
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSaveInstanceState(outState: Bundle?) {

        // Будем сохранять фрагмент из левого контейнера. Потом по нему
        // определим как отрисовать окно
        supportFragmentManager.findFragmentById(R.id.leftFrame)?.let {
            outState?.putString(F_KEY, it.javaClass.canonicalName)
        }
        super.onSaveInstanceState(outState)
    }

    private fun initSideMenu(toolbar: Toolbar) {

        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this@MainActivityFr)
    }

    // Метод требуется для того, чтобы убрать ненужные меню
    // при портретной ориентации.
    private fun cleanUpMenu(menu: Menu, vararg items: Int) {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            invalidateOptionsMenu()
            for (i in items) {
                menu.findItem(i)?.setVisible(false)
            }
        }
    }

    private fun renderFragment(leftFragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()

        if (isLandscape) {
            // Если прилетел фрагмент со списком городов, то сделать
            // видимым правый фрейм и для вывода фрагмента с прогнозом по городу
            if (leftFragment is CitiesFragment) {
                ft.replace(R.id.leftFrame, leftFragment, leftFragment.javaClass.canonicalName)

                // Иначе сделать невидимым правый фрейм. Так освобождается место на экране
                // для фрагментов получаемых от SideMenu
            } else {
                rightFrame?.visibility = View.GONE
                ft.replace(R.id.leftFrame, leftFragment, leftFragment.javaClass.canonicalName)
            }
        } else {
            ft.replace(R.id.leftFrame, leftFragment, leftFragment.javaClass.canonicalName)
        }

        ft.addToBackStack(null).commit()
    }

}