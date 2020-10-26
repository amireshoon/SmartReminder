package ap.behrouzi.smartr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.time.RadialPickerLayout;
import com.mohamadamin.persianmaterialdatetimepicker.time.TimePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.Objects;

import ap.behrouzi.smartr.database.DatabaseHelper;

public class AddNormalReminderActivity extends AppCompatActivity {

    LinearLayout timeChooser, dateChooser;
    AppCompatEditText reminderNameEditText, reminderLinkEditText;
    AppCompatTextView timeHolderTextView, dateHolderTextView;
    AppCompatButton submitButton;
    AppCompatCheckBox everyDay, everyMonth, everyWeek, everyYear;
    SwitchCompat alarmCombat;

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
        everyDay = findViewById(R.id.every_day_cb);
        everyWeek = findViewById(R.id.every_week_cb);
        everyMonth = findViewById(R.id.every_month_cb);
        everyYear = findViewById(R.id.every_year_cb);
        alarmCombat = findViewById(R.id.switch_alarm);

        submitButton.setOnClickListener( v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(AddNormalReminderActivity.this);
            databaseHelper.addNormalReminder(Objects.requireNonNull(reminderNameEditText.getText()).toString().trim(), "", 0, "","no","","","no");
        });

        timeChooser.setOnClickListener( v-> {
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
                    timeHolderTextView.setText(Integer.toString(hourOfDay) + Integer.toString(minute));
                }
            },24,60,true);
            timePickerDialog.show(getFragmentManager(), "TimepickerDialog");
        });

        dateChooser.setOnClickListener( v-> {
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            dateHolderTextView.setText(String.valueOf(year) + String.valueOf(monthOfYear) + String.valueOf(dayOfMonth));
                        }
                    },
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
        });
    }
}