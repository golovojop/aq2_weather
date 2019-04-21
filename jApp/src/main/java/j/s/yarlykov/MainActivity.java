package j.s.yarlykov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logI("onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logI("onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        logI("onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logI("onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();
        logI("onPause");

    }

    @Override
    protected void onResume() {
        super.onResume();
        logI("onResume");

    }

    private void logI(String message) {
        final String tag = "jWeather";
        Log.i(tag, this.getClass().getSimpleName() + ": " + message);
    }
}
