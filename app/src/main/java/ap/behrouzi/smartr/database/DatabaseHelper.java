package ap.behrouzi.smartr.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void addNormalReminder() {

    }
}
