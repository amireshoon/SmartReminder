package ap.behrouzi.smartr.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ap.behrouzi.smartr.R;
import ap.behrouzi.smartr.adapters.RemindersAdapter;
import ap.behrouzi.smartr.dataModels.Reminders;
import ap.behrouzi.smartr.database.DatabaseHelper;
import ap.behrouzi.smartr.services.MapServiceJob;
import ap.behrouzi.smartr.utils.Jdate;

public class MapRemindersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Reminders> reminders;
    private DatabaseHelper db;
    private RemindersAdapter remindersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_reminders);
        recyclerView = findViewById(R.id.map_reminders_recycle_view);

        reminders = new ArrayList<>();

        db = new DatabaseHelper(MapRemindersActivity.this);
        Cursor cursor = db.getAllMapRemindersForRecycle();
        if (cursor.getCount() == 0) {

        }else {
            while (cursor.moveToNext()) {
                reminders.add(new Reminders(cursor.getDouble(cursor.getColumnIndex("map_lat")) + Double.toString(cursor.getDouble(cursor.getColumnIndex("map_lon"))),"","","yes","yes","yes","yes","yes","yes", cursor.getInt(cursor.getColumnIndex("map_id"))));
            }
        }

        remindersAdapter = new RemindersAdapter(MapRemindersActivity.this,reminders);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MapRemindersActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        recyclerView.setAdapter(remindersAdapter);

        remindersAdapter.setOnItemCheckedListener((reminders, isChecked) ->{
            db.markMapAsDone(reminders.getId(), isChecked);
        });

        remindersAdapter.setOnDeletedListener((reminders1, position) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapRemindersActivity.this);
            builder.setTitle("پاک کردن هشدار")
                    .setMessage("درصورت حذف کردن هشدار شما قادر به برگرداندن آن نیستید!")
                    .setPositiveButton("حذف", (dialog, which) -> {
                        Toast.makeText(MapRemindersActivity.this, "هشدار " + reminders1.getName()  + " با موفقیت پاک شد. ", Toast.LENGTH_SHORT).show();
                        db.removeMapReminder(reminders1.getId());
                        reminders.remove(position);
                        remindersAdapter.notifyItemRemoved(position);
                        remindersAdapter.notifyItemRangeChanged(position, reminders.size());
                        onResume();
                    })
                    .setNegativeButton("انصراف", (dialog, which) -> {

                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        findViewById(R.id.add_map_btn).setOnClickListener( v-> {
            startActivityForResult(new Intent(MapRemindersActivity.this, MapAddActivity.class), 123);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        reminders.clear();

        Cursor cursor = db.getAllMapRemindersForRecycle();
        if (cursor.getCount() == 0) {

        }else {
            while (cursor.moveToNext()) {
                reminders.add(new Reminders(Double.toString(cursor.getDouble(cursor.getColumnIndex("map_lat"))) + Double.toString(cursor.getDouble(cursor.getColumnIndex("map_lon"))),"","","","","","","","",cursor.getInt(cursor.getColumnIndex("map_id"))));
            }
        }

        recyclerView.setAdapter(remindersAdapter);
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
                    DatabaseHelper databaseHelper = new DatabaseHelper(MapRemindersActivity.this);
                    databaseHelper.createMapReminder(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lon", 0));
                    Intent intent = new Intent(this, MapServiceJob.class);
                    startService(intent);
                }
            }catch (Exception e) {
                Toast.makeText(this, "مشکلی در ایجاد یادآور بوجود آمد!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}