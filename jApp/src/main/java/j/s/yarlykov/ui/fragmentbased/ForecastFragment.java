package j.s.yarlykov.ui.fragmentbased;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
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

import java.nio.channels.GatheringByteChannel;
import java.util.Formatter;

import j.s.yarlykov.R;
import j.s.yarlykov.data.db.DBHelper;
import j.s.yarlykov.data.domain.CityForecast;
import j.s.yarlykov.data.network.api.FirebaseProvider;
import j.s.yarlykov.data.network.model.firebase.FcmResponseModel;
import j.s.yarlykov.data.network.model.firebase.PushDataModel;
import j.s.yarlykov.data.network.model.firebase.PushMessageModel;
import j.s.yarlykov.data.provider.GeoProvider;
import j.s.yarlykov.services.RestForecastService;
import j.s.yarlykov.ui.fragmentbased.history.HistoryActivity;
import j.s.yarlykov.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static j.s.yarlykov.util.Utils.isRu;

public class ForecastFragment extends Fragment implements RestForecastService.RestForecastReceiver {

    public static final String placeBundleKey = "cityKey";
    public static final String binderBundleKey = "binderKey";
    public static final String indexBundleKey = "indexKey";

    public static final String fcmTopic = "/topics/weather";
    public static final String fcmKey = "key=AIzaSyBWI-ySOo3LMex1e-y27jBmLHhO6VRTydQ";
    public static final String fcmMimeType = "application/json";

    private TextView tvCity, tvTemperature, tvWind, tvHumidity, tvPressure;
    private LinearLayout pbfContainer, forecastContainer;
    private RestForecastService forecastService;
    private ImageView ivSky;
    private View vStatus;
    private SQLiteDatabase dataBase;
    private CityForecast lastForecast;

    public static ForecastFragment create(IBinder binder, String city, int index) {
        ForecastFragment fragment = new ForecastFragment();

        // Передача параметра
        Bundle args = new Bundle();
        args.putBinder(binderBundleKey, binder);
        args.putString(placeBundleKey, city);
        args.putInt(indexBundleKey, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataBase = new DBHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getServiceBinder();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.city_forecast_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        GeoProvider provider = GeoProvider.GeoProviderHelper.getProvider();

        if(getIndex() == 0 && provider.getLastLocation() != null) {
            forecastService.requestGeoForecast(this,
                    (int)provider.getLastLocation().getLatitude(),
                    (int)provider.getLastLocation().getLongitude(),
                    dataBase);
        } else {
            forecastService.requestForecast(this, getCity(), getCountry(), dataBase);
        }
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
            case R.id.actionShare:
                pushForecast();
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onForecastOnline(CityForecast forecast, Bitmap icon) {
        lastForecast = forecast;
        renderForecast(forecast, true, icon);
    }

    @Override
    public void onForecastOffline(CityForecast forecast) {
        if (forecast != null) {
            lastForecast = forecast;
            renderForecast(forecast, false, null);
        } else {
            vStatus.setBackgroundResource(android.R.color.transparent);
            pbfContainer.setVisibility(View.GONE);
            forecastContainer.setVisibility(View.GONE);
            AlertNoData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dataBase.close();
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
                = ((RestForecastService.ServiceBinder)
                getArguments()
                        .getBinder(binderBundleKey))
                .getService();
    }

    public String getPlace() {
        return getArguments().getString(placeBundleKey);
    }

    public String getCity() {
        String[] arr = getPlace().split(",", 2);
        return arr[0];
    }

    public String getCountry() {
        String[] arr = getPlace().split(",", 2);
        return arr[1];
    }

    public int getIndex() {
        return getArguments().getInt(indexBundleKey, 0);
    }

    // Отрисовать прогноз на экране
    private void renderForecast(CityForecast forecast, boolean isOnline, Bitmap icon) {

        // Для онлайн прогноза - зеленый индикатор, иначе красный.
        int drawableId = isOnline ? R.drawable.green_circle : R.drawable.red_circle;
        vStatus.setBackgroundResource(drawableId);

        // Set Weather image
        if (icon != null) {
            ivSky.setImageBitmap(icon);
        } else {
            ivSky.setImageResource(forecast.getIcon());
        }

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

    private void loadHistory() {
        HistoryActivity.start(requireContext(), tvCity.getText().toString());
    }

    private void AlertNoData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.connectivity_alert));

        View view = getLayoutInflater().inflate(R.layout.no_data_dialog, null);
        builder.setView(view);

        builder.setPositiveButton(getString(R.string.buttonClose), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Отправить Push-сообщение
    private void pushForecast() {
        if (lastForecast == null) return;

        PushDataModel pdm = PushDataModel.createFrom(lastForecast);
        FirebaseProvider
                .getInstance()
                .getApi()
                .sendPushNotification(
                        fcmKey,
                        fcmMimeType,
                        new PushMessageModel(fcmTopic, pdm))
                .enqueue(new Callback<FcmResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<FcmResponseModel> call,
                                           @NonNull Response<FcmResponseModel> response) {
                        okhttp3.Response rawResp = response.raw();

                        // Debug
                        if(rawResp.networkResponse() != null) {
                            Utils.logI(this, "FCM sent successfully");
                            Utils.logI(this, "Operation result code: " + rawResp.code());
                            Utils.logI(this, "Response headers: " + rawResp.headers().toString());
                            Utils.logI(this, rawResp.networkResponse().toString());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FcmResponseModel> call,
                                          @NonNull Throwable t) {
                        Utils.logI(this, "FCM sent failure");
                        Utils.logI(this, t.getMessage());
                    }
                });

    }
}