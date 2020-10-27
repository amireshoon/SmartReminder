package ap.behrouzi.smartr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import ap.behrouzi.smartr.database.DatabaseHelper;
import ap.behrouzi.smartr.services.AlarmService;
import ap.behrouzi.smartr.services.ReminderBroadcast;
import ap.behrouzi.smartr.utils.Jdate;

public class AddNormalReminderActivity extends AppCompatActivity {

    LinearLayout timeChooser, dateChooser;
    AppCompatEditText reminderNameEditText, reminderLinkEditText;
    AppCompatTextView timeHolderTextView, dateHolderTextView;
    AppCompatButton submitButton;
    AppCompatCheckBox shanbeh, doShanbeh, yekShanbeh, seShanbeh, chShanbeh, panjShanbeh, jome;
    SwitchCompat alarmCombat;
    Jdate.DateFormat dates;
    String[] time = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_normal_reminder);

        timeChooser = findViewById(R.id.time_choose_linear_layout);
        dateChooser = findViewById(R.id.date_choose_linear_layout);
        timeHolderTextView = findViewById(R.id.time_holder_text_view);
        dateHolderTextView = findViewById(R.id.date_holder_text_view);
        reminderLinkEditText = findViewById(R.id.reminder_link_edit_text);
        reminderNameEditText = findViewById(R.id.reminder_name_edit_text);
        submitButton = findViewById(R.id.submit_btn);
        shanbeh = findViewById(R.id.sh);
        yekShanbeh = findViewById(R.id.y_sh);
        doShanbeh = findViewById(R.id.d_sh);
        seShanbeh = findViewById(R.id.s_sh);
        chShanbeh = findViewById(R.id.ch_sh);
        panjShanbeh = findViewById(R.id.p_sh);
        jome = findViewById(R.id.jome);
        alarmCombat = findViewById(R.id.switch_alarm);

        timeChooser.setOnClickListener( v-> {
            Calendar c = Calendar.getInstance();
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                    time[0] = String.valueOf(hourOfDay);
                    time[1] = String.valueOf(minute);
                    timeHolderTextView.setText(hourOfDay + ":" + minute);
                }
            },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
            timePickerDialog.show(getFragmentManager(), "TimepickerDialog");
        });

        dateChooser.setOnClickListener( v-> {
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {
                        dates = Jdate.jalali_to_gregorian(year, monthOfYear, dayOfMonth, true);
                        dateHolderTextView.setText(dates.getsYear() + "/" + dates.getsMonth() + "/" + dates.getsDay());
                    },
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        });

        submitButton.setOnClickListener( v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(AddNormalReminderActivity.this);
            String alarm = "no";
            if (alarmCombat.isChecked()) {
                alarm = "yes";
            }
            databaseHelper.addNormalReminder(
                    Objects.requireNonNull(reminderNameEditText.getText()).toString().trim(),
                    time[0] + "-" + time[1],
                    separateComma(getRepeatMode()),
                    dates.getsYear() + "-" + dates.getsMonth() + "-" + dates.getsDay(),
                    "no",
                    reminderLinkEditText.toString().trim(),
                    "",
                    "no",
                    alarm
                    );
            addReminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]), dates.getiDay(), dates.getiMonth(), dates.getiYear());
        });
    }

    private ArrayList<Integer> getRepeatMode() {
        ArrayList<Integer> mode = new ArrayList<>();

        if (shanbeh.isChecked()) {
            mode.add(0);
        }

        if (yekShanbeh.isChecked()) {
            mode.add(1);
        }

        if (doShanbeh.isChecked()) {
            mode.add(2);
        }

        if (seShanbeh.isChecked()) {
            mode.add(3);
        }

        if (chShanbeh.isChecked()) {
            mode.add(4);
        }

        if (panjShanbeh.isChecked()) {
            mode.add(5);
        }

        if (jome.isChecked()) {
            mode.add(6);
        }
        return mode;
    }

    private String separateComma(ArrayList<Integer> modes) {
        StringBuilder s = new StringBuilder();
        for (int j = 0; j <= modes.size() - 1; j++) {
            s.append(modes.get(j).intValue());
        }
        return s.toString().trim();
    }

    private void addReminder(int hour, int min, int day, int mon, int year) {
//        Intent myIntent = new Intent(this , AlarmService.class);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
//
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, min);

        if (hour > 12) {
            int ampmhour = hour - 12;
            Log.e("ERROR", "addReminder: PM " + ampmhour);
            calendar.set(Calendar.HOUR, ampmhour);
            calendar.set(Calendar.AM_PM, Calendar.PM);
        }else {
            Log.e("ERROR", "addReminder: AM " + hour);
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.AM_PM, Calendar.AM);
        }

//
//        calendar.add(Calendar.DAY_OF_MONTH, day);
//        calendar.add(Calendar.MONTH, mon);
//        calendar.add(Calendar.YEAR, year);
//        Log.e("ERROR", "addReminder: " + min + hour + day);
//



        Intent intent = new Intent(AddNormalReminderActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNormalReminderActivity.this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60 , pendingIntent);
        Toast.makeText(AddNormalReminderActivity.this, "Heyyyyyyy", Toast.LENGTH_SHORT).show();
        Log.e("ERROR", "addReminder: " + calendar.getTimeInMillis());
//        alarmManager.set(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),
//                pendingIntent);
    }
}