package ap.behrouzi.smartr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "smartReminder.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "reminders";

    private static final String C_ID = "_id";
    private static final String C_TIME = "_time";
    private static final String C_IS_LOCATIONAL = "_is_locational";
    private static final String C_LOCATION = "_location";
    private static final String C_REPEAT = "_repeat";
    private static final String C_NAME = "_name";
    private static final String C_DONE = "_done";
    private static final String C_DATE = "_date";
    private static final String C_EXTRA_2 = "_extra2";
    private static final String C_ALARM = "_alarm";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " (" + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        C_NAME + " TEXT, " +
                        C_TIME + " TEXT, " +
                        C_REPEAT + " TEXT, " +
                        C_DONE + " TEXT, " +
                        C_IS_LOCATIONAL + " TEXT, " +
                        C_LOCATION + " TEXT, " +
                        C_DATE + " TEXT, " +
                        C_EXTRA_2 + " TEXT, " +
                        C_ALARM + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNormalReminder(String rName, String rTime, String rRepeat, String rDate, String rDone, String rE2, String rLocation, String rIsLocational, String alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(C_NAME, rName);
        cv.put(C_TIME, rTime);
        cv.put(C_REPEAT, rRepeat);
        cv.put(C_DATE, rDate);
        cv.put(C_DONE, rDone);
        cv.put(C_EXTRA_2, rE2);
        cv.put(C_LOCATION, rLocation);
        cv.put(C_IS_LOCATIONAL, rIsLocational);
        cv.put(C_ALARM, alarm);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(this.context, "Failed", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this.context, "Success", Toast.LENGTH_LONG).show();
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readAllData(String day) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + "_date" + " = '" + day + "'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public int getLatestRecord() {
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + C_ID + " DESC LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        assert cursor != null;
        if (cursor.getCount() == 0) {
            return 0;
        }else {
            int i = 0;
            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                 i = cursor.getInt(cursor.getColumnIndex(C_ID));
                 cursor.close();
            }
            return i;
        }
    }

    public void removeRow(int id) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + C_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void changeAlarmStatus(int id, String status) {
        String query = "UPDATE " + TABLE_NAME + " SET " + C_DONE + " = '" + status + "' WHERE " + C_ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

    public void createMapReminder(double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String query2 =
                "CREATE TABLE IF NOT EXISTS " + "map_reminder" +
                        " (" + "map_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "map_lat" + " DOUBLE, " +
                        "map_lon" + " DOUBLE, " +
                        "is_done" + " BOOLEAN);";
        db.execSQL(query2);
        cv.put("map_lat", lat);
        cv.put("map_lon", lon);
        cv.put("is_done", false);
        long result = db.insert("map_reminder", null, cv);
        if (result == -1) {
            Toast.makeText(this.context, "Failed", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this.context, "Success", Toast.LENGTH_LONG).show();
        }
    }

    public Cursor getAllMapReminders() {
        String query = "SELECT * FROM " + "map_reminder WHERE is_done = 'false'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
