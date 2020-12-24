package ap.behrouzi.smartr.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
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
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.database.DatabaseHelper;
import ap.behrouzi.smartr.ui.MainActivity;

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
        latLngDataModels = new ArrayList<>();
        latLngDataModels.clear();
        DatabaseHelper databaseHelper = new DatabaseHelper(MapServiceJob.this);
        Cursor cursor = databaseHelper.getAllMapReminders();
        Toast.makeText(this, Integer.toString(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor.getCount() <= 0) {
            onDestroy();
        }

        while(cursor.moveToNext()) {
            LatLngDataModel latLngDataModel = new LatLngDataModel();
            latLngDataModel.setLat(cursor.getDouble(cursor.getColumnIndex("map_lat")));
            latLngDataModel.setLon(cursor.getDouble(cursor.getColumnIndex("map_lon")));
            latLngDataModel.setId(cursor.getInt(cursor.getColumnIndex("map_id")));
            latLngDataModel.setDesc(cursor.getString(cursor.getColumnIndex("map_desc")));
            latLngDataModels.add(latLngDataModel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onCreate();
//        Toast.makeText(this, "ReCreating service", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLocationChanged(Location location) {
        for (int i = 0; i <= latLngDataModels.size() - 1; i ++) {
            float[] distance = new float[1];
            Location.distanceBetween(latLngDataModels.get(i).getLat(), latLngDataModels.get(i).getLon(),
                    location.getLatitude(), location.getLongitude(), distance);
            double radiusInMeters = 20.0*1000.0; //1 KM = 1000 Meter
            if( distance[0] > radiusInMeters ){ // Out Of Area
                Log.e("location", "Keep moving");
            } else { // In Area
                Toast.makeText(this, "You are there!", Toast.LENGTH_SHORT).show();
                Log.e("location", "You got there!");
                // Showing notification
                NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel(mNotifyManager);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MapServiceJob.this, "mymapchannel").setSmallIcon(android.R.drawable.stat_sys_warning).setColor
                        (ContextCompat.getColor(MapServiceJob.this, R.color.reminderDoneColor)).setContentTitle("به محدوده وارد شدید!").setContentText(latLngDataModels.get(i).getDesc());
                mNotifyManager.notify(latLngDataModels.get(i).getId(), mBuilder.build());
                // Marking reminder as done
                DatabaseHelper databaseHelper = new DatabaseHelper(MapServiceJob.this);
                databaseHelper.markMapAsDone(latLngDataModels.get(i).getId()); // Mark as done
                // Removing from tracking list
                latLngDataModels.remove(i);
            }
        }
    }
    @TargetApi(26)
    private void createChannel(NotificationManager notificationManager) {
        String name = "mymapchannel";
        String description = "Notifications for download status";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(name, name, importance);
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        mChannel.setLightColor(Color.BLUE);
        notificationManager.createNotificationChannel(mChannel);
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
    private String desc;
    private int    id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}