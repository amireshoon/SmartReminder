package ap.behrouzi.smartr.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmDetector extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.android.deskclock.ALARM_DISMISS") || action.equals("com.android.deskclock.ALARM_DONE") || action.equals("com.lge.clock.alarmalert.stop"))
        {
            Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
        }
    }
}
