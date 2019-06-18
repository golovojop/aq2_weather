package j.s.yarlykov.data.provider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GeoProvider {
    private LocationManager manager;
    private Location lastLocation = null;
    private FusedLocationProviderClient client;

    private GeoProvider(FusedLocationProviderClient client, LocationManager manager) {
        this.manager = manager;
        this.client = client;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void requestLocation() {

        // Сначала попробовать получить location из кеша системы
        try {
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lastLocation = location;
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if(lastLocation != null) return;

        // Или запросить текущее расположение через активного провайдера
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = manager.getBestProvider(criteria, true);

        if (provider != null) {
            try {
                manager.requestLocationUpdates(provider, 3000, 1, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        lastLocation = location;
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }
                    @Override
                    public void onProviderEnabled(String provider) {
                    }
                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                });
            } catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }

    public static class GeoProviderHelper {
        private static GeoProvider provider = null;

        public static void init(FusedLocationProviderClient client, LocationManager manager) {
            if (provider == null) {
                provider = new GeoProvider(client, manager);
            }
        }

        public static GeoProvider getProvider() {
            return provider;
        }
    }
}
