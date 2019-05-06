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

    private CheckBox chbWind, chbHumidity, chbPressure;
    private Button btnRequest;
    private EditText etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setOnClickRequestButton();
    }

    private void initViews() {
        btnRequest = findViewById(R.id.btnRequest);
        etCity = findViewById(R.id.etCity);
        chbWind = findViewById(R.id.chbWind);
        chbHumidity = findViewById(R.id.chbHumidity);
        chbPressure = findViewById(R.id.chbPressure);

        chbWind.setChecked(true);
        chbHumidity.setChecked(true);
        chbPressure.setChecked(true);
    }

    private void setOnClickRequestButton() {

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCity.getText().toString();
                ForecastProvider provider = ForecastProvider.getInstance();

                if (city.length() > 1) {
                    Set<CityForecast.MeteoData> requiredMeteoData = new HashSet<>();

                    if (chbWind.isChecked()) requiredMeteoData.add(MeteoData.WIND);
                    if (chbHumidity.isChecked()) requiredMeteoData.add(MeteoData.HUMIDITY);
                    if (chbPressure.isChecked()) requiredMeteoData.add(MeteoData.PRESSURE);

                    ForecastActivity.start(
                            MainActivity.this,
                            provider.getForecastCustom(city, requiredMeteoData));
                } else {
                    Utils.logI(this, getString(R.string.incorrectRequest));
                }
            }
        });
    }
}
