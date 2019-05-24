package j.s.yarlykov.ui.fragmentbased;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import j.s.yarlykov.R;
import j.s.yarlykov.util.Utils;

public class MainActivityFr extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String F_KEY = "F_KEY";
    FrameLayout leftFrame, rightFrame;
    boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.logI(this, "onCreate. savedInstanceState is null: " + (savedInstanceState == null));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        leftFrame = findViewById(R.id.leftFrame);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            rightFrame = findViewById(R.id.rightFrame);
        }

        Toolbar toolbar = findViewById(R.id.toolbarDrawer);
        setSupportActionBar(toolbar);
        initSideMenu(toolbar);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.leftFrame, CitiesFragment.create(),
                    CitiesFragment.class.getCanonicalName()).commit();
        } else {
            String fName = savedInstanceState.getString(F_KEY);

            // Если сохраняли имя класса фрагмента, и оно не "CitiesFragment",
            // то скрыть правую панель. Далее система сама восстановит последний
            // фрагмент в левой панели и он займет все окно.
            //
            // Если fName == "CitiesFragment", то система восстановит его в левой панели
            // а в процессе запуска он сделает видимой правую панель.
            if (fName != null
                    && !fName.equals(CitiesFragment.class.getCanonicalName())
                    && isLandscape) {
                    rightFrame.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionAbout:
                InfoActivityFr.start(this);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            cleanUpMenu(menu, R.id.actionWeek);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Utils.logI(this, "onBackPressed");

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_feedback:
                renderFragment(FeedbackFragment.create());
                break;
            case R.id.nav_developer:
                renderFragment(DevInfoFragment.create());
                break;
            default:

        }

        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        // Будем сохранять фрагмент из левого контейнера. Потом по нему
        // определим как отрисовать окно
        Fragment fr = getSupportFragmentManager().findFragmentById(R.id.leftFrame);
        if (fr != null) {
            outState.putString(F_KEY, fr.getClass().getCanonicalName());
        }
        super.onSaveInstanceState(outState);
    }

    private void initSideMenu(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Метод требуется для того, чтобы убрать ненужные меню
    // при портретной ориентации.
    private void cleanUpMenu(Menu menu, int... items) {
        boolean isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;
        if (isPortrait) {
            invalidateOptionsMenu();
            for (int item : items) {
                try {
                    menu.findItem(item).setVisible(false);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Отрисовать фрагменты
    private void renderFragment(Fragment leftFragment) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (isLandscape) {
            // Если прилетел фрагмент со списком городов, то сделать
            // видимым правый фрейм и для вывода фрагмента с прогнозом по городу
            if (leftFragment instanceof CitiesFragment) {
                ft.replace(R.id.leftFrame, leftFragment, leftFragment.getClass().getCanonicalName());

                // Иначе сделать невидимым правый фрейм. Так освобождается место на экране
                // для фрагментов получаемых от SideMenu
            } else {
                rightFrame.setVisibility(View.GONE);
                ft.replace(R.id.leftFrame, leftFragment, leftFragment.getClass().getCanonicalName());
            }
        } else {
            ft.replace(R.id.leftFrame, leftFragment, leftFragment.getClass().getCanonicalName());
        }

        ft.addToBackStack(null).commit();
    }

}
