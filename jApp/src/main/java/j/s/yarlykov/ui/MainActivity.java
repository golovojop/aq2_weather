package j.s.yarlykov.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import j.s.yarlykov.R;
import j.s.yarlykov.services.RestForecastService;
import j.s.yarlykov.ui.fragmentbased.CitiesFragment;
import j.s.yarlykov.ui.fragmentbased.DevInfoFragment;
import j.s.yarlykov.ui.fragmentbased.FeedbackFragment;
import j.s.yarlykov.ui.fragmentbased.InfoActivityFr;
import j.s.yarlykov.ui.fragmentbased.SensorsFragment;
import j.s.yarlykov.ui.fragmentbased.TemperatureSensorFragment;
import j.s.yarlykov.util.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String F_KEY = "F_KEY";
    private static final String SP_FCM_DATA = "SP_FCM_DATA";
    private static final String SP_FCM_TOKEN = "token";

    private FrameLayout rightFrame;
    private boolean isLandscape, isBound;
    private ServiceConnection serviceConnection;
    private final int permissionRequestCode = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        // Инициализировать Firebase, сохранить токен приложения
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        saveFirebaseToken(instanceIdResult.getToken());
                    }
                });

        subcribePushNotifications();

        requestSmsPermissions();

        isLandscape = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;

        if (isLandscape) {
            rightFrame = findViewById(R.id.rightFrame);
        }

        Toolbar toolbar = findViewById(R.id.toolbarDrawer);
        setSupportActionBar(toolbar);
        initSideMenu(toolbar);

        // Если стартуем первый раз, то привязываемся к службе
        if (savedInstanceState == null) {
            Intent serviceIntent = new Intent(getApplicationContext(), RestForecastService.class);
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    isBound = service != null;

                    if (isBound) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.leftFrame,
                                CitiesFragment.create(service),
                                CitiesFragment.class.getCanonicalName()).commit();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isBound = false;
                }
            };

            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        } else {
            String fName = savedInstanceState.getString(F_KEY);

            // Если сохраняли имя класса фрагмента, и оно не "CitiesFragment",
            // то скрыть правую панель. Далее система сама восстановит последний
            // фрагмент в левой панеле и он займет все окно.
            //
            // Если fName == "CitiesFragment", то система восстановит его в левой панеле,
            // а в процессе запуска он сделает видимой правую панель.
            if (fName != null
                    && !fName.equals(CitiesFragment.class.getCanonicalName())
                    && isLandscape) {
                rightFrame.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = !isBound;
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
            case R.id.nav_sensor:
                renderFragment(SensorsFragment.create());
                break;
            case R.id.nav_temperature:
                renderFragment(TemperatureSensorFragment.create());
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode == permissionRequestCode) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Спасибо!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Извините, апп без данного разрешения может работать неправильно",
                        Toast.LENGTH_SHORT).show();
            }
        }
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

    // Подписка на прием Push в топик "weather"
    private void subcribePushNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "subscribed";
                        if (!task.isSuccessful()) {
                            msg = "not " + msg;
                        }
                        Utils.logI(this, String.format("subcribePushNotifications: %s", msg));
                    }
                });
    }

    private void saveFirebaseToken(String token) {
        SharedPreferences shPrefs = getSharedPreferences(SP_FCM_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shPrefs.edit();
        editor.putString(SP_FCM_TOKEN, token);
        editor.apply();
    }

    private String readFirebaseToken() {
        SharedPreferences shPrefs = getSharedPreferences(SP_FCM_DATA, Context.MODE_PRIVATE);
        return shPrefs.getString(SP_FCM_TOKEN, "");
    }

    private void requestSmsPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (!smsRcvGranted() || !smsSendGranted())) {
            final String[] permissions =
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.SEND_SMS};
            ActivityCompat.requestPermissions(this, permissions, permissionRequestCode);
        }
    }

    private boolean smsRcvGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    private boolean smsSendGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }
}
