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

    // Статическая переменная для сохранения переменных состояния
    static CustomState customState;

    public static void start(Context context, String city) {
        Intent intent = new Intent(context, HistoryActivity.class);
        intent.putExtra(EXTRA_HISTORY, city);
        context.startActivity(intent);
    }

    RecyclerView rvHistory;
    TextView tvCity;
    ProgressBar progressBar;
    LinearLayout pbContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // Город
        String city = (String) getIntent().getSerializableExtra(EXTRA_HISTORY);

        // isNotRestored == true если активити создается первый раз
        // или выбран новый город
        boolean isNotRestored = customState == null || !customState.lastCity.equals(city);

        initViews(city, isNotRestored);

        if (isNotRestored) {
            customState = new CustomState(city, new LoadTask());
            customState.loadTask.bind(this);
            customState.loadTask.execute(progressBar.getMax());
        } else {
            pbContainer.setVisibility(View.GONE);
            rvHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        customState.loadTask.unbind();
    }

    private void initViews(String city, boolean isNotRestored) {
        tvCity = findViewById(R.id.tvCity);
        rvHistory = findViewById(R.id.rvHistory);
        pbContainer = findViewById(R.id.pbContainer);
        progressBar = findViewById(R.id.myProgressBar);

        tvCity.setText(city);

        // Установить фоновое изображение для CollapsingToolbarLayout
        TypedArray imagesBg = getResources().obtainTypedArray(R.array.historyBg);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsingToolbar);
        collapsingToolbar.setBackground(ContextCompat.getDrawable(
                getApplicationContext(),
                imagesBg.getResourceId(posRandom(imagesBg.length()), 0)));
        imagesBg.recycle();

        // Установить LayoutManager в зависимости от ориентации экрана
        boolean isPortrate =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        RecyclerView.LayoutManager orientationCompatibleLayoutManager =
                isPortrate ? new LinearLayoutManager(getApplicationContext()) :
                        new GridLayoutManager(getApplicationContext(), 2);

        // Инициализировать элемент RecyclerView
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(orientationCompatibleLayoutManager);
        rvHistory.setItemAnimator(new DefaultItemAnimator());
        rvHistory.setAdapter(new HistoryRVAdapter(HistoryProvider.build(
                getApplicationContext(),
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
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
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

    /**
     * Класс для сохранения состояния
     */
    static class CustomState {
        String lastCity;
        LoadTask loadTask;

        public CustomState(String lastCity, LoadTask loadTask) {
            this.lastCity = lastCity;
            this.loadTask = loadTask;
        }
    }

    /**
     * Task загрузки истории. Эмулирует долгую работу
     */
    static class LoadTask extends AsyncTask<Integer, Integer, Void> {
        HistoryActivity historyActivity;

        void bind(HistoryActivity activity) {
            this.historyActivity = activity;
        }

        void unbind() {
            historyActivity = null;
        }

        @Override
        protected Void doInBackground(Integer... args) {
            int max = args[0];

            try {
                for (int i = 10; i < max; ) {
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
            if (historyActivity != null) {
                historyActivity.rvHistory.setVisibility(View.GONE);
                historyActivity.pbContainer.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (historyActivity != null) {
                historyActivity.pbContainer.setVisibility(View.GONE);
                historyActivity.rvHistory.setVisibility(View.VISIBLE);
                historyActivity = null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (historyActivity != null) {
                historyActivity.progressBar.setProgress(values[0]);
            }
        }
    }
}