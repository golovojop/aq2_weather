package j.s.yarlykov.ui.fragmentbased;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;
import java.util.List;

import j.s.yarlykov.R;
import j.s.yarlykov.util.Utils;

public class MainActivityFr extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean isLandscape;
    LinearLayout layoutWeather;
    FrameLayout layoutAux = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        // Контейнеры для вывода различных фрагментов
        // layoutWeather - для фрагментов с погодой
        // layoutAux - для фрагментов из SideMenu
        if (isLandscape) {
            layoutWeather = findViewById(R.id.layoutWeather);
            layoutAux = findViewById(R.id.layoutAux);
            layoutAux.setVisibility(View.GONE);
            layoutWeather.setVisibility(View.VISIBLE);
        }

        Toolbar toolbar = findViewById(R.id.toolbarDrawer);
        setSupportActionBar(toolbar);
        initSideMenu(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.citiesContainer, CitiesFragment.create(), CitiesFragment.class.getCanonicalName()).commit();
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(isLandscape && layoutWeather.getVisibility() != View.VISIBLE) {
            layoutAux.setVisibility(View.GONE);
            layoutWeather.setVisibility(View.VISIBLE);
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
                renderWindow(FeedbackFragment.create());
                break;
            case R.id.nav_developer:
                renderWindow(DevInfoFragment.create());
                break;
            default:

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initSideMenu(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
    private void renderWindow(Fragment leftFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (isLandscape) {
            // Если прилетел фрагмент со списком городов,
            // то сделать видимым макет для погоды и поместить
            // в него фрагмент
            if (leftFragment instanceof CitiesFragment) {
                layoutAux.setVisibility(View.GONE);
                layoutWeather.setVisibility(View.VISIBLE);
                ft.replace(R.id.citiesContainer, leftFragment, leftFragment.getClass().getCanonicalName());
                // Иначе сделать видимым макет для доп фрагментов
            } else {
                layoutWeather.setVisibility(View.GONE);
                layoutAux.setVisibility(View.VISIBLE);
                ft.replace(R.id.layoutAux, leftFragment, leftFragment.getClass().getCanonicalName());
            }
        } else {
            ft.replace(R.id.citiesContainer, leftFragment, leftFragment.getClass().getCanonicalName());
        }

        ft.addToBackStack(null).commit();
    }
}
