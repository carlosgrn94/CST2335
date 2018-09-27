package com.example.chuck_csp.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by chuck_csp on 11/9/17.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private final String ACTIVITY_NAME = "ChatDatabaseHelper";
    private static String DATABASE_NAME = "ChatDB";
    private static int VERSION_NUM = 8;
    public static String TABLE_NAME = "ChatMessages";

    final static String KEY_ID = "Id";
    final static String KEY_MESSAGE = "Messages";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
        + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + KEY_MESSAGE + " TEXT "
        + ");"
        );

        Log.i(ACTIVITY_NAME, "onCreate()");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion=" + oldVersion + " newVersion= " + newVersion);
    }


}
