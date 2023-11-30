package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "DND";
    static final int DATABASE_VERSION = 1;

    String DATABASE_TABLE;

    static final String pk = "PK";
    static final String JSON = "JSON";

    String CREATE_DB_QUERY;
    public DatabaseHelper(Context context, String tableName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DATABASE_TABLE = tableName;
        CREATE_DB_QUERY = "CREATE TABLE" + DATABASE_TABLE + " ( " + pk + " INTEGER PRIMARY KEY AUTOINCREMENT, " + JSON + " TEXT " + ")";
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

    }
}
