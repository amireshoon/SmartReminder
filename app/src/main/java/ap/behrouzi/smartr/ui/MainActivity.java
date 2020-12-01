package ap.behrouzi.smartr.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.adapters.ViewPagerAdapter;
import ap.behrouzi.smartr.fragments.DaysFragment;
import ap.behrouzi.smartr.utils.Splash;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            viewPager.setCurrentItem(day,true);
        });

    }
}