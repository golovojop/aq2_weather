package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.Formatter;

import j.s.yarlykov.R;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.domain.Forecast;
import j.s.yarlykov.services.CityForecastService;
import j.s.yarlykov.ui.fragmentbased.history.HistoryActivity;
import j.s.yarlykov.util.Utils;

import static j.s.yarlykov.util.Utils.isRu;

public class ForecastFragment extends Fragment implements CityForecastService.ForecastReceiver {

    public static final String forecastBundleKey = "forecastKey";
    public static final String cityBundleKey = "cityKey";
    public static final String binderBundleKey = "binderKey";
    public static final String indexBundleKey = "indexKey";

    private TextView tvCity, tvTemperature, tvWind, tvHumidity, tvPressure;
    private LinearLayout pbfContainer, forecastContainer;
    private CityForecastService forecastService;
    private ImageView ivSky;
    private Context context;
    private View vStatus;
    private final long TTL = 1 * 1000;

    public static ForecastFragment create(int index, CityForecast forecast) {
        ForecastFragment fragment = new ForecastFragment();

        // Передача параметров
        Bundle args = new Bundle();
        args.putSerializable(forecastBundleKey, forecast);
        args.putInt(indexBundleKey, index);

        fragment.setArguments(args);
        return fragment;
    }

    public static ForecastFragment create(IBinder binder, String city, int index) {
        ForecastFragment fragment = new ForecastFragment();

        // Передача параметра
        Bundle args = new Bundle();
        args.putBinder(binderBundleKey, binder);
        args.putString(cityBundleKey, city);
        args.putInt(indexBundleKey, index);
        fragment.setArguments(args);
        return fragment;
    }

    // Вызывается из CityForecastService
    @Override
    public void onForecastReady(final Forecast forecast) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    renderForecast((CityForecast)forecast);
                }
            });
        } catch (NullPointerException e) {
            Utils.logI(this, "onForecastReady: activity was destroyed");
        }
    }

    @Override
    public void onAttach(Context context) {
        Utils.logI(this, "onAttach");
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.logI(this, "onCreate");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getServiceBinder();
    }

    @Override
    public void onDestroy() {
        Utils.logI(this, "onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Utils.logI(this, "onCreateView");
        return inflater.inflate(R.layout.city_forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Utils.logI(this, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    @Override
    public void onResume() {
        Utils.logI(this, "onResume");
        super.onResume();

        pbfContainer.setVisibility(View.VISIBLE);
        forecastContainer.setVisibility(View.GONE);
        forecastService.requestForecast(this, getCity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.week, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionWeek:
                loadHistory();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(View parent) {

        pbfContainer = parent.findViewById(R.id.pbfContainer);
        forecastContainer = parent.findViewById(R.id.llForecast);
        ivSky = parent.findViewById(R.id.iv_sky);
        tvCity = parent.findViewById(R.id.tv_city);
        tvTemperature = parent.findViewById(R.id.tv_temperature);
        tvWind = parent.findViewById(R.id.tv_wind);
        tvHumidity = parent.findViewById(R.id.tv_humidity);
        tvPressure = parent.findViewById(R.id.tv_pressure);
        vStatus = parent.findViewById(R.id.onlineStatus);

        pbfContainer.setVisibility(View.VISIBLE);
        forecastContainer.setVisibility(View.GONE);

        tvCity.setText(getCity());
    }

    private void getServiceBinder() {
        forecastService
                = ((CityForecastService.ServiceBinder)
                getArguments()
                        .getBinder(binderBundleKey))
                .getService();
    }

    public String getCity() {
        return getArguments().getString(cityBundleKey);
    }

    public int getIndex() {
        return getArguments().getInt(indexBundleKey, 0);
    }

    // Отрисовать прогноз на экране
    private void renderForecast(CityForecast forecast) {

        // Прогноз не получен - алерт
        if(forecast == null) {
            vStatus.setBackgroundResource(R.drawable.red_circle);
            AlertInternet();
            return;
        }

        // Если получен прогноз онлайн, то зеленый индикатор,
        // иначе красный.
        long currentTime = new Date().getTime();
        int drawableId = (currentTime - forecast.getTimestamp() > TTL) ?
                R.drawable.red_circle : R.drawable.green_circle;
        vStatus.setBackgroundResource(drawableId);

        // Set Weather image
        ivSky.setImageResource(forecast.getImgId());

        //Set City (Uppercase first letter)
        String city = forecast.getCity();
        tvCity.setText(Utils.capitalize(city));

        // Set Temperature
        Formatter fmt = new Formatter();
        fmt.format("%+2d \u2103", forecast.getTemperature());
        tvTemperature.setText(fmt.toString());
        fmt.close();

        // Set Wind
        fmt = new Formatter();
        fmt.format("%.1f %s", forecast.getWind(), getResources().getString(R.string.infoWind));
        tvWind.setText(fmt.toString());
        fmt.close();

        // Set Humidity
        fmt = new Formatter();
        fmt.format("%2d %%", forecast.getHumidity());
        tvHumidity.setText(fmt.toString());
        fmt.close();

        // Set Pressure
        fmt = new Formatter();
        fmt.format("%4d %s", (int) forecast.getPressure(isRu()), getResources().getString(R.string.infoPressure));
        tvPressure.setText(fmt.toString());
        fmt.close();

        pbfContainer.setVisibility(View.GONE);
        forecastContainer.setVisibility(View.VISIBLE);
    }

    // Эмуляция длительной работы в AsyncTask
    private void loadHistory() {
        HistoryActivity.start(requireContext(), tvCity.getText().toString());
    }

    private void AlertInternet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.connectivity_alert));

        TextView tv = new TextView(getActivity());
        tv.setText(getString(R.string.check_connection));
        builder.setView(tv);

        builder.setPositiveButton(getString(R.string.buttonClose), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}