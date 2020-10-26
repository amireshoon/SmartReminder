package ap.behrouzi.smartr.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
                        C_REPEAT + " INT, " +
                        C_DONE + " TEXT, " +
                        C_IS_LOCATIONAL + " TEXT, " +
                        C_LOCATION + " TEXT, " +
                        C_DATE + " TEXT, " +
                        C_EXTRA_2 + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNormalReminder(String rName, String rTime, int rRepeat, String rDate, String rDone, String rE2, String rLocation, String rIsLocational) {
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
}
