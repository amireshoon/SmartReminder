package ap.behrouzi.smartr.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ap.behrouzi.smartr.AddNormalReminderActivity;
import ap.behrouzi.smartr.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "smartreminder")
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle(intent.getStringExtra("reminder_name") + intent.getIntExtra("reminder_id", 0))
                .setContentText("Ohh shit here we go again!" + intent.getStringExtra("reminder_link"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1,builder.build());

    }


}
