package ap.behrouzi.smartr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ap.behrouzi.smartr.R;

public class ReminderBroadcast extends BroadcastReceiver {
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
