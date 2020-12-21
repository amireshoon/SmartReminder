package ap.behrouzi.smartr.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.os.Process;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ap.behrouzi.smartr.database.DatabaseHelper;


public class MapServiceJob extends Service implements LocationListener {

    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 1000;
    public static String str_receiver = "servicetutorial.service.receiver";
    Intent intent;
    private List<LatLngDataModel> latLngDataModels;

    public MapServiceJob() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
//        fn_getlocation();
        latLngDataModels = new ArrayList<>();
        DatabaseHelper databaseHelper = new DatabaseHelper(MapServiceJob.this);
        Cursor cursor = databaseHelper.getAllMapReminders();
        if (cursor.getCount() <= 0) {
            onDestroy();
        }

        while(cursor.moveToNext()) {
            LatLngDataModel latLngDataModel = new LatLngDataModel();
            latLngDataModel.setLat(cursor.getDouble(cursor.getColumnIndex("map_lat")));
            latLngDataModel.setLon(cursor.getDouble(cursor.getColumnIndex("map_lon")));
            latLngDataModels.add(latLngDataModel);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Here we should get all of the map reminders and check for matching one
//        Toast.makeText(this, "On location change " + location.getAltitude() + " / " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i <= latLngDataModels.size() - 1; i ++) {
            if (location.getLatitude() == latLngDataModels.get(i).getLat() && location.getLongitude() == latLngDataModels.get(i).getLon()) {
                Toast.makeText(MapServiceJob.this, "You did it", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MapServiceJob.this, "Sorry", Toast.LENGTH_SHORT).show();
            }
        }
//        Log.i("LOCATION", latLngDataModels);
// For creating range
//        int rEarth = 6378;
//        double pi = 3.141592653589793;
//        double rightLat = location.getLatitude() + (1d / rEarth) * (180 / pi);
//        double rightLon = location.getLongitude() + (1d / rEarth) * (180 / pi) / Math.cos(location.getLatitude() * pi / 180);
//
//        double leftLat = location.getLatitude() - (1d / rEarth) * (180 / pi);
//        double leftLon = location.getLongitude() - (1d / rEarth) * (180 / pi) / Math.cos(location.getLatitude() * pi / 180);
//        Log.i("LOCATION", "originalLat => " + location.getLatitude());
//        Log.i("LOCATION", "originalLong => " + location.getLongitude());
//        Log.i("LOCATION", "leftLat => " + leftLat);
//        Log.i("LOCATION", "rightLat => " + rightLat);
//        Log.i("LOCATION", "leftLong => " + leftLon);
//        Log.i("LOCATION", "rightLong => " + rightLon);
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

    @SuppressLint("MissingPermission")
    private void fn_getlocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {

            if (isNetworkEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {

                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");

                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }

            }


            if (isGPSEnable) {
                location = null;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        Log.e("latitude", location.getLatitude() + "");
                        Log.e("longitude", location.getLongitude() + "");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        fn_update(location);
                    }
                }
            }


        }

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    private void fn_update(Location location) {

        intent.putExtra("latutide", location.getLatitude() + "");
        intent.putExtra("longitude", location.getLongitude() + "");
        sendBroadcast(intent);
    }
}

class LatLngDataModel {
    private double lat;
    private double lon;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}