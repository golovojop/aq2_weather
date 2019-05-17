package j.s.yarlykov.ui.fragmentbased.history;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import j.s.yarlykov.R;
import j.s.yarlykov.data.provider.HistoryProvider;
import j.s.yarlykov.ui.fragmentbased.InfoActivityFr;

public class HistoryActivity extends AppCompatActivity {

    private static final String EXTRA_HISTORY =
            HistoryActivity.class.getSimpleName() + ".extra.HISTORY";

    public static final int DAYS = 3;

    private static String lastCity = "";

    public static void start(Context context, String city) {
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putExtra(EXTRA_HISTORY, city);
        context.startActivity(intent);
    }

    RecyclerView rvHistory;
    TextView tvCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tvCity = findViewById(R.id.tvCity);
        tvCity.setText((String)getIntent().getSerializableExtra(EXTRA_HISTORY));

        boolean isNotRestored = !lastCity.equals(tvCity.getText());

        boolean isPortrate =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        RecyclerView.LayoutManager orientationCompatibleLayoutManager =
                isPortrate ? new LinearLayoutManager(this) :
                        new GridLayoutManager(this, 2);

        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(orientationCompatibleLayoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(new HistoryRVAdapter(HistoryProvider.build(
                this,
                DAYS,
                isNotRestored)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveLastCity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveLastCity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionAbout:
                InfoActivityFr.start(this);
                break;
            case R.id.actionAdd:
                HistoryProvider.oneMoreDay(this);
                try {
                    rvHistory.getAdapter().notifyDataSetChanged();
                } catch (NullPointerException e) {e.printStackTrace();}
                break;
                default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveLastCity() {
        lastCity = tvCity.getText().toString();
    }
}
