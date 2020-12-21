package ap.behrouzi.smartr.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.adapters.ViewPagerAdapter;
import ap.behrouzi.smartr.database.DatabaseHelper;
import ap.behrouzi.smartr.fragments.DaysFragment;
import ap.behrouzi.smartr.services.AlarmDetector;
import ap.behrouzi.smartr.services.MapServiceJob;
import ap.behrouzi.smartr.utils.Jdate;
import ap.behrouzi.smartr.utils.Splash;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter1 = new IntentFilter("com.android.deskclock.ALARM_DONE");
        filter1.addAction("com.android.deskclock.ALARM_DISMISS");
        filter1.addAction("com.lge.clock.alarmalert.stop");

        filter1.addAction("com.android.deskclock.ALARM_ALERT");
        filter1.addAction("com.android.deskclock.ALARM_DISMISS");
        filter1.addAction("com.android.deskclock.ALARM_DONE");
        filter1.addAction("com.android.deskclock.ALARM_SNOOZE");
        filter1.addAction("com.android.alarmclock.ALARM_ALERT");
        filter1.addAction("com.samsung.sec.android.clockpackage.alarm.ALARM_ALERT");
        filter1.addAction("com.htc.android.worldclock.ALARM_ALERT");
        filter1.addAction("com.sonyericsson.alarm.ALARM_ALERT");
        filter1.addAction("zte.com.cn.alarmclock.ALARM_ALERT");
        filter1.addAction("com.motorola.blur.alarmclock.ALARM_ALERT");
        filter1.addAction("com.mobitobi.android.gentlealarm.ALARM_INFO");
        filter1.addAction("com.urbandroid.sleep.alarmclock.ALARM_ALERT");
        filter1.addAction("com.splunchy.android.alarmclock.ALARM_ALERT");

        BroadcastReceiver rec = new AlarmDetector();
        registerReceiver(rec, filter1);
        setContentView(R.layout.activity_main);
        Splash.Builder splash = new Splash.Builder(this, getSupportActionBar());
        splash.perform();
        tabLayout = findViewById(R.id.main_tab_layout);
        viewPager = findViewById(R.id.main_view_page);

        getTabs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SmartReminderApp";
            String desc = "For alarms and more on";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("smartreminder", name, importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                try {
                    if (data.getBooleanExtra("map",false)) {
                        Log.e("ERROR", "onActivityResult: " + data.getDoubleExtra("lat", 0));
                        Log.e("ERROR", "onActivityResult: " + data.getDoubleExtra("lon", 0));
                        // Here we should add reminder to db
                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                        databaseHelper.createMapReminder(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lon", 0));
                        Intent intent = new Intent(this, MapServiceJob.class);
                        startService(intent);
                    }
                }catch (Exception e) {
                    Toast.makeText(this, "مشکلی در ایجاد یادآور بوجود آمد!", Toast.LENGTH_SHORT).show();
                }
            }
    }

    public void getTabs() {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        new Handler().post(() -> {
            DaysFragment shnbe = DaysFragment.newInstance(0);
            DaysFragment ykshnbe = DaysFragment.newInstance(1);
            DaysFragment doshnbe = DaysFragment.newInstance(2);
            DaysFragment seshnbe = DaysFragment.newInstance(3);
            DaysFragment chshnbe = DaysFragment.newInstance(4);
            DaysFragment pnshnbe = DaysFragment.newInstance(5);
            DaysFragment jome = DaysFragment.newInstance(6);
            viewPagerAdapter.addFragment(shnbe, "شنبه");
            viewPagerAdapter.addFragment(ykshnbe, "یک شنبه");
            viewPagerAdapter.addFragment(doshnbe, "دو شنبه");
            viewPagerAdapter.addFragment(seshnbe, "سه شنبه");
            viewPagerAdapter.addFragment(chshnbe, "چهار شنبه");
            viewPagerAdapter.addFragment(pnshnbe, "پنج شنبه");
            viewPagerAdapter.addFragment(jome, "جمعه");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            Calendar calendar = Calendar.getInstance();
            viewPager.setCurrentItem(calendar.get(Calendar.DAY_OF_WEEK),true);
        });

    }
}