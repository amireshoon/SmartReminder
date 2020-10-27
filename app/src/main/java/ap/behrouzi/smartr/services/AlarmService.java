package ap.behrouzi.smartr.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationBuilderWithBuilderAccessor;
import androidx.core.app.NotificationCompat;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.ui.MainActivity;

public class AlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
//        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        Notification notification = new Notification(R.drawable.ic_baseline_arrow_back_ios_24, "Notify Alarm strart", System.currentTimeMillis());
//
//        Intent myIntent = new Intent(this , MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myIntent, 0);
//        notification.setLatestEventInfo(this, "Notify label", "Notify text", contentIntent);
//        notification.
//        mNM.notify(NOTIFICATION, notification);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
}
