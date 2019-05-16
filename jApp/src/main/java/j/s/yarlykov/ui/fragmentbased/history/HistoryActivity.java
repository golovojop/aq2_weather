package j.s.yarlykov.ui.fragmentbased.history;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    public static final int WEEK = 7;

    public static void start(Context context, String city) {

        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putExtra(EXTRA_HISTORY, city);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        TextView tvCity = findViewById(R.id.tvCity);
        tvCity.setText((String)getIntent().getSerializableExtra(EXTRA_HISTORY));

        boolean isPortrate =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        RecyclerView.LayoutManager orientationCompatibleLayoutManager =
                isPortrate ? new LinearLayoutManager(this) :
                        new GridLayoutManager(this, 2);

        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(orientationCompatibleLayoutManager);
        rvHistory.setAdapter(new HistoryRVAdapter(HistoryProvider.build(this, WEEK)));
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
