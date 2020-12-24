package ap.behrouzi.smartr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
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
import java.util.GregorianCalendar;
import java.util.Objects;

import ap.behrouzi.smartr.dataModels.Reminders;
import ap.behrouzi.smartr.database.DatabaseHelper;
import ap.behrouzi.smartr.services.AlarmService;
import ap.behrouzi.smartr.services.ReminderBroadcast;
import ap.behrouzi.smartr.ui.MainActivity;
import ap.behrouzi.smartr.utils.Jdate;
import ap.behrouzi.smartr.utils.PDate;

public class AddNormalReminderActivity extends AppCompatActivity {

    LinearLayout timeChooser, dateChooser;
    AppCompatEditText reminderNameEditText, reminderLinkEditText;
    AppCompatTextView timeHolderTextView, dateHolderTextView;
    AppCompatButton submitButton;
    AppCompatCheckBox shanbeh, doShanbeh, yekShanbeh, seShanbeh, chShanbeh, panjShanbeh, jome;
    SwitchCompat alarmCombat, repeat;
    Jdate.DateFormat dates;
    String[] time = new String[2];
    int[] dw;
    String dayOfWeek = "";
    boolean isTimeSelected = false;
    Reminders editReminder = null;
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
        repeat = findViewById(R.id.every_week_switch);
        findViewById(R.id.back_button).setOnClickListener( v -> {
            finish();
        });
        DatabaseHelper databaseHelper = new DatabaseHelper(AddNormalReminderActivity.this);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("isEdit", false)) {
            Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
            editReminder = databaseHelper.getSingleReminder(intent.getIntExtra("reminder_id", 0));
            reminderNameEditText.setText(editReminder.getName());
            timeHolderTextView.setText(editReminder.getTime().replace("-",":"));
            reminderLinkEditText.setText(editReminder.getExtra2());
            if (editReminder.getAlarm().equals("yes")) {
                alarmCombat.setChecked(true);
            }
            if (editReminder.getRepeat().equals("yes")) {
                repeat.setChecked(true);
            }
            time[0] = Integer.toString(Integer.parseInt(editReminder.getTime().split("-")[0]));
            time[1] = Integer.toString(Integer.parseInt(editReminder.getTime().split("-")[1]));
            isTimeSelected = true;
            checkFields();
        }
        dayOfWeek = intent.getStringExtra("whichDay");
        timeChooser.setOnClickListener( v-> {
            Calendar c = Calendar.getInstance();
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                    time[0] = String.valueOf(hourOfDay);
                    time[1] = String.valueOf(minute);
                    timeHolderTextView.setText(hourOfDay + ":" + minute);
                    isTimeSelected = true;
                    checkFields();
                }
            },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
            timePickerDialog.show(getFragmentManager(), "TimepickerDialog");
        });

        dateChooser.setOnClickListener( v-> {
//            dates = Jdate.jalali_to_gregorian(year, monthOfYear, dayOfMonth, true);
//            dateHolderTextView.setText(dates.getsYear() + "/" + dates.getsMonth() + "/" + dates.getsDay());

//            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            dateHolderTextView.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                            dw = PDate.jalali_to_gregorian(year,(monthOfYear + 1),dayOfMonth);
                            Log.e("TIME", "onDateSet: On Formattred: " + dw[0] + '/' + dw[2] + '/' + dw[1]);

                            persianCalendar.set(Calendar.YEAR,dw[0]);
                            persianCalendar.set(Calendar.DAY_OF_MONTH,dw[2]);
                            persianCalendar.set(Calendar.MONTH,dw[1]);

                            Log.e("TIME", "onDateSet: " + persianCalendar.getTimeInMillis());
                        }
                    },
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

        });

        submitButton.setOnClickListener( v -> {
            if (intent.getBooleanExtra("isEdit", false)) {
                if (time[0] == null || time[1] == null  || reminderNameEditText.getText().toString().equals("")) {
                    Toast.makeText(AddNormalReminderActivity.this, "نام و زمان هشدار الزامی میباشد!", Toast.LENGTH_SHORT).show();
                    if (reminderNameEditText.getText().toString().equals("")) {
                        reminderNameEditText.setError("نام هشدار اجباری میباشد!");
                    }
                }else {

                    String alarm = "no";
                    if (alarmCombat.isChecked()) {
                        alarm = "yes";
                    }
                    String repeat2 = "no";
                    if (repeat.isChecked()) {
                        repeat2 = "yes";
                    }
                    databaseHelper.updateReminder(
                            Objects.requireNonNull(reminderNameEditText.getText()).toString().trim(),
                            time[0] + "-" + time[1],
                            repeat2,
                            editReminder.getDate(), //dw[0] + "-" + dw[1] + "-" + dw[2]
                            "no",
                            reminderLinkEditText.getText().toString().trim(),
                            "",
                            "no",
                            alarm,
                            Integer.toString(intent.getIntExtra("reminder_id", 0))
                    );
                }
                finish();
                //TODO
//                addReminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0, 0, 0,reminderLinkEditText.getText().toString().trim(), reminderNameEditText.getText().toString().trim(),databaseHelper.getLatestRecord());
            }else {
                if (time[0] == null || time[1] == null  || reminderNameEditText.getText().toString().equals("")) {
                    Toast.makeText(AddNormalReminderActivity.this, "نام و زمان هشدار الزامی میباشد!", Toast.LENGTH_SHORT).show();
                    if (reminderNameEditText.getText().toString().equals("")) {
                        reminderNameEditText.setError("نام هشدار اجباری میباشد!");
                    }
                }else {

                    String alarm = "no";
                    if (alarmCombat.isChecked()) {
                        alarm = "yes";
                    }
                    String repeat2 = "no";
                    if (repeat.isChecked()) {
                        repeat2 = "yes";
                    }
                    databaseHelper.addNormalReminder(
                            Objects.requireNonNull(reminderNameEditText.getText()).toString().trim(),
                            time[0] + "-" + time[1],
                            repeat2,
                            dayOfWeek, //dw[0] + "-" + dw[1] + "-" + dw[2]
                            "no",
                            reminderLinkEditText.getText().toString().trim(),
                            "",
                            "no",
                            alarm
                    );

//                addReminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]), dw[2], dw[1], dw[0],reminderLinkEditText.getText().toString().trim(), reminderNameEditText.getText().toString().trim(),databaseHelper.getLatestRecord());
                    addReminder(Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0, 0, 0,reminderLinkEditText.getText().toString().trim(), reminderNameEditText.getText().toString().trim(),databaseHelper.getLatestRecord());
                }

            }

        });
        reminderNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkFields() {
        if (!Objects.requireNonNull(reminderNameEditText.getText()).toString().trim().isEmpty() && isTimeSelected) {
            submitButton.setEnabled(true);
            submitButton.setBackgroundColor(Color.rgb(29,161,242));
        }
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
//        AlarmManager alarmMgr;
//        PendingIntent alarmIntent;
//        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(AddNormalReminderActivity.this, ReminderBroadcast.class);
//        alarmIntent = PendingIntent.getBroadcast(AddNormalReminderActivity.this, 0, intent, 0);
//
//// Set the alarm to start at 8:30 a.m.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, hour);
//        calendar.set(Calendar.MINUTE, min);
//        calendar.set(Calendar.DAY_OF_MONTH, day);
//        calendar.set(Calendar.MONTH, mon);
////        Toast.makeText(this, Integer.toString(min), Toast.LENGTH_SHORT).show();
//        Log.e("TIME", "addReminder: " + calendar.getTimeInMillis());
//
//// setRepeating() lets you specify a precise custom interval--in this case,
//// 20 minutes.
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                1000 * 60 * 20, alarmIntent);

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, name);
        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
        intent.putExtra(AlarmClock.EXTRA_MINUTES, min);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI,true);
        if (repeat.isChecked()) {
            ArrayList<Integer> integers = new ArrayList<>();
            Toast.makeText(this, dayOfWeek, Toast.LENGTH_SHORT).show();
            integers.add(Jdate.getDayOfWeek(dayOfWeek));
            intent.putExtra(AlarmClock.EXTRA_DAYS, integers);
        }
        startActivity(intent);
        finish();
    }
}