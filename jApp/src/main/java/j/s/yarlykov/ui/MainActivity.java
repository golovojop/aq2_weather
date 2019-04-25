package j.s.yarlykov.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.data.provider.ForecastProvider;
import j.s.yarlykov.util.Utils;

public class MainActivity extends AppCompatActivity {

    ForecastProvider provider = new ForecastProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        final Button btn = findViewById(R.id.bt_request);
        final EditText et = findViewById(R.id.et_city);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = et.getText().toString();
                Utils.logI(this, city);

                if(city != null && city.length() > 1) {
                    ForecastActivity.startActivity(MainActivity.this, provider.getForecastByCity(city));
                } else {
                    Utils.logI(this, "Empty or incorrect request");
                }
            }
        });
    }

}
