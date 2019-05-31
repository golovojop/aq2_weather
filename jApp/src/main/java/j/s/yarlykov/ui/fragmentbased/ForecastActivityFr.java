package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;

public class ForecastActivityFr extends AppCompatActivity {

    public static final String EXTRA_FORECAST = ForecastActivityFr.class.getSimpleName() + ".extra.FORECAST";
    public static final String cityBundleKey = "cityKey";
    public static final String binderBundleKey = "binderKey";
    public static final String indexBundleKey = "indexKey";

    public static void start(Context context, CityForecast forecast) {
        Intent intent = new Intent(context, ForecastActivityFr.class);
        intent.putExtra(EXTRA_FORECAST, forecast);
        context.startActivity(intent);
    }

    public static void start(Context context, IBinder binder, String city, int index) {
        Bundle extras = new Bundle();
        extras.putBinder(binderBundleKey, binder);
        extras.putString(cityBundleKey, city);
        extras.putInt(indexBundleKey, index);

        Intent intent = new Intent(context, ForecastActivityFr.class);
        intent.putExtras(extras);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_fr);

        // Продолжать работать только в портретной ориентации
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {

            Bundle extras = getIntent().getExtras();
            IBinder serice = extras.getBinder(binderBundleKey);
            String city = extras.getString(cityBundleKey);
            int index = extras.getInt(indexBundleKey);

            ForecastFragment forecastFragment = ForecastFragment.create(serice, city, index);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.rightFrame, forecastFragment).commit();
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
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }
}
