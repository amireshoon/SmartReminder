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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

            addReminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]), dates.getiDay(), dates.getiMonth(), dates.getiYear(),reminderLinkEditText.getText().toString().trim(), reminderNameEditText.getText().toString().trim(),databaseHelper.getLatestRecord());
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

    private void addReminder(int hour, int min, int day, int mon, int year, String link, String name, int id) {
        String str_date=mon+"-"+day+"-"+year+" "+hour+":"+min+":"+"00";
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy kk:mm:ss");
        Date date = null;
        try {
            date = (Date)formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        long output=date.getTime()/1000L;
        String str=Long.toString(output);
        long timestamp = Long.parseLong(str) * 1000;
        long curr = System.currentTimeMillis();
        Log.e("TIME", "addReminder: Curr: " + curr + " / then: " + timestamp);
        Intent intent = new Intent(AddNormalReminderActivity.this, ReminderBroadcast.class);
        intent.putExtra("reminder_id", id);
        intent.putExtra("reminder_name", name);
        intent.putExtra("reminder_link", link);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNormalReminderActivity.this,0,intent,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                timestamp,
                pendingIntent);
        Toast.makeText(AddNormalReminderActivity.this, "Heyyyyyyy", Toast.LENGTH_SHORT).show();
        Log.e("ERROR", "addReminder: " + timestamp);
    }
}