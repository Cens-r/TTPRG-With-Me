package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseManager {
    public DatabaseHelper dbh;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context ctx) {
        context = ctx;
    }

    public DatabaseManager open(String tablename) {
        try {
            dbh = new DatabaseHelper(context, tablename);
            database = dbh.getWritableDatabase();
            return this;
        } catch (Exception e) {
            return null;
        }
    }




    public void close() {
        dbh.close();
    }

    public void insert(String json) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.JSON, json);
        database.insert(DatabaseHelper.DATABASE_NAME, null, contentValues);
    }

    public Cursor fetch() {
        String[] data = new String[]{DatabaseHelper.pk, DatabaseHelper.JSON};
        Cursor cursor = database.query(dbh.DATABASE_TABLE, data, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long pk, String json)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.JSON, json);
        int ret = database.update(dbh.DATABASE_TABLE, contentValues, DatabaseHelper.pk + "=" + pk, null);
        return ret;
    }

    public void delete (long pk) {
        database.delete(dbh.DATABASE_TABLE, DatabaseHelper.pk + "=" + pk, null);
    }
}
