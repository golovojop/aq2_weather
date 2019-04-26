package j.s.yarlykov.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


import java.util.HashSet;
import java.util.Set;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.provider.ForecastProvider;
import j.s.yarlykov.util.Utils;
import static j.s.yarlykov.data.domain.CityForecast.*;

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
        final CheckBox chbWind = findViewById(R.id.chb_wind);
        final CheckBox chbHumidity = findViewById(R.id.chb_humidity);
        final CheckBox chbPressure = findViewById(R.id.chb_pressure);
        chbWind.setChecked(true);
        chbHumidity.setChecked(true);
        chbPressure.setChecked(true);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = et.getText().toString();
                Utils.logI(this, city);

                if(city != null && city.length() > 1) {
                    Set<CityForecast.MeteoData> meteoData = new HashSet<>();

                    if(chbWind.isChecked()) meteoData.add(MeteoData.WIND);
                    if(chbHumidity.isChecked()) meteoData.add(MeteoData.HUMIDITY);
                    if(chbPressure.isChecked()) meteoData.add(MeteoData.PRESSURE);

                    ForecastActivity.startActivity(MainActivity.this, provider.getForecastCustom(city, meteoData));
                } else {
                    Utils.logI(this, "Empty or incorrect request");
                }
            }
        });
    }

}
