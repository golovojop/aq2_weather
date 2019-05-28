package j.s.yarlykov.ui.fragmentbased.history;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import j.s.yarlykov.R;
import j.s.yarlykov.data.provider.HistoryProvider;
import j.s.yarlykov.ui.fragmentbased.InfoActivityFr;
import static j.s.yarlykov.util.Utils.*;

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
    ProgressBar progressBar;
    LoadTask loadTask;
    LinearLayout pbContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        loadTask = (LoadTask)getLastNonConfigurationInstance();

        initViews();

        if(loadTask == null) {
            loadTask = new LoadTask();
            loadTask.bind(this);
            loadTask.execute();
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        loadTask.unbind();
        return loadTask;
    }

    private void initViews() {
        tvCity = findViewById(R.id.tvCity);
        tvCity.setText((String)getIntent().getSerializableExtra(EXTRA_HISTORY));

        pbContainer = findViewById(R.id.pbContainer);
        progressBar = findViewById(R.id.myProgressBar);

        // Установить фоновое изображение для CollapsingToolbarLayout
        TypedArray imagesBg = getResources().obtainTypedArray(R.array.historyBg);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setBackground(ContextCompat.getDrawable(
                this,
                imagesBg.getResourceId(posRandom(imagesBg.length()), 0)));
        imagesBg.recycle();
        boolean isNotRestored = !lastCity.equals(tvCity.getText());

        // Установить LayoutManager в зависимости от ориентации экрана
        boolean isPortrate =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        RecyclerView.LayoutManager orientationCompatibleLayoutManager =
                isPortrate ? new LinearLayoutManager(this) :
                        new GridLayoutManager(this, 2);

        // Инициализировать элемент RecyclerView
        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(orientationCompatibleLayoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(new HistoryRVAdapter(HistoryProvider.build(
                this,
                DAYS,
                isNotRestored)));

        // Обработчик нажатия на FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryProvider.oneMoreDay(HistoryActivity.this);
                try {
                    rvHistory.getAdapter().notifyDataSetChanged();
                } catch (NullPointerException e) {e.printStackTrace();}
            }
        });
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

    private void saveLastCity() {
        lastCity = tvCity.getText().toString();
    }

    /**
     * Task загрузки истории. Эмулирует долгую работу
     */
    static class LoadTask extends AsyncTask<Void, Integer, Integer> {
        HistoryActivity historyActivity;

        void bind(HistoryActivity activity) {
            this.historyActivity = activity;
        }

        void unbind() {
            historyActivity = null;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int max = historyActivity.progressBar.getMax();

            try {
                for(int i = 10; i < max;) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    publishProgress(i);
                    i += 5;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            historyActivity.rvHistory.setVisibility(View.GONE);
            historyActivity.pbContainer.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            historyActivity.pbContainer.setVisibility(View.GONE);
            historyActivity.rvHistory.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            historyActivity.progressBar.setProgress(values[0]);
        }
    }
}
