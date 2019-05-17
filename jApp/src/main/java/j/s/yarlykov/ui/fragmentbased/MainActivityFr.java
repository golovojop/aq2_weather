package j.s.yarlykov.ui.fragmentbased;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import j.s.yarlykov.R;

public class MainActivityFr extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fr);
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
        if(menu != null) {
            cleanUpMenu(menu, R.id.actionWeek);
        }
        return true;
    }

    // Метод требуется для того, чтобы убрать ненужные меню
    // при портретной ориентации.
    private void cleanUpMenu(Menu menu, int ... items) {
        boolean isPortrait = getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT;
        if(isPortrait) {
            invalidateOptionsMenu();
            for(int item : items) {
                try {
                    menu.findItem(item).setVisible(false);
                } catch (NullPointerException e) {e.printStackTrace();}
            }
        }
    }
}
