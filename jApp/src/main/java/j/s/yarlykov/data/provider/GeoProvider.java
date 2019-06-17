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

public class GeoProvider {
    private LocationManager manager;
    private Location lastLocation = null;
    private Context context;

    private GeoProvider(Context context, LocationManager manager) {
        this.manager = manager;
        this.context = context;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void requestLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            return;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        String provider = manager.getBestProvider(criteria, true);

        if (provider != null) {
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
        }
    }

    public static class GeoProviderHelper {
        private static GeoProvider provider = null;

        public static void init(Context context, LocationManager manager) {
            if (provider == null) {
                provider = new GeoProvider(context, manager);
            }
        }

        public static GeoProvider getProvider() {
            return provider;
        }


    }

}
