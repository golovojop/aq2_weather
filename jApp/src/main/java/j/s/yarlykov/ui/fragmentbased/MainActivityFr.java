package j.s.yarlykov.ui.fragmentbased;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import j.s.yarlykov.R;

public class MainActivityFr extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        Toolbar toolbar = findViewById(R.id.toolbarDrawer);
        setSupportActionBar(toolbar);
        initSideMenu(toolbar);
        renderLeftFragment(CitiesFragment.create());
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
            for(Fragment f : fragmentList) {
                if(f instanceof FeedbackFragment || f instanceof DevInfoFragment) {
                    FrameLayout rightContainer = findViewById(R.id.forecastContainer);
                    if(rightContainer != null) rightContainer.setVisibility(View.VISIBLE);
                }
            }
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_send:
                renderLeftFragment(FeedbackFragment.create());
                break;
            case R.id.nav_slideshow:
                renderLeftFragment(DevInfoFragment.create());
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
    private void renderLeftFragment(Fragment leftFragment){

        boolean isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        FrameLayout rightContainer = findViewById(R.id.forecastContainer);
        Fragment rightFragment = getSupportFragmentManager().findFragmentById(R.id.forecastContainer);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(isLandscape && rightFragment != null) {

            // Если прилетел фрагмент со списком городов,
            // то сделать видимым правы фрагмент с прогнозом
            if(leftFragment instanceof CitiesFragment) {
                ft.replace(R.id.citiesContainer, leftFragment);
                rightContainer.setVisibility(View.VISIBLE);
                // Иначе скрыть правый фрагмент и отрисовать на весь экран новый фрагмент
            } else {
                rightContainer.setVisibility(View.GONE);
                ft.replace(R.id.citiesContainer, leftFragment);
            }
        } else {
            ft.replace(R.id.citiesContainer, leftFragment);
        }

        ft.addToBackStack(String.format("%s", leftFragment.hashCode())).commit();
    }
}
