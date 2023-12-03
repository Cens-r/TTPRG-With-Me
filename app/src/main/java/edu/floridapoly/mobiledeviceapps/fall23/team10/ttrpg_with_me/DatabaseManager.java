package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseManager extends SQLiteOpenHelper {
    SQLiteDatabase db = getWritableDatabase();
    static final String table = " (pk INTEGER PRIMARY KEY AUTOINCREMENT, JSON TEXT);";

    public DatabaseManager(Context context) {
        super(context, "Dnd", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CHARACTERS" + table);
        db.execSQL("CREATE TABLE CLASSES (UUID TEXT PRIMARY KEY NOT NULL, JSON TEXT)");
        db.execSQL("CREATE TABLE ITEMS (pk INTEGER PRIMARY KEY AUTOINCREMENT, fk NOT NULL, JSON TEXT, TYPE TEXT)");
    }

    public long addLine(String Type, String json) {
        ContentValues values = new ContentValues();

        values.put("JSON", json);
        long key = db.insert(Type, null, values);
        return key;
    }

    public void update(long pk, String Type, String json) {
        ContentValues cv = new ContentValues();
        cv.put("JSON", json);
        if (pk == -1) {
            db.insert(Type, null, cv);
        } else {
            db.update(Type, cv, "pk = ?", new String[]{String.valueOf(pk)});
        }
    }

    public String getJson(long pk, String Type) {
        Cursor c = db.rawQuery("SELECT JSON FROM " + Type + " WHERE pk = " + pk, null);
        c.moveToFirst();

        int i = c.getColumnIndex("JSON");
        String json = c.getString(i);
        c.close();
        return json;
    }

    public Cursor fetch(String Type, Long pk) {
        return db.rawQuery("SELECT JSON FROM " + Type + " WHERE pk = " + pk, null);
    }
    public Cursor fetchAll(String Type) {
        Cursor c = db.rawQuery("SELECT * FROM " + Type, null);
        return c;
    }

    public boolean classExists(String uuid) {
        Cursor cursor = db.rawQuery("SELECT * FROM CLASSES WHERE UUID = '" + uuid + "'", null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }
    public void addClass(String uuid, String json) {
        if (classExists(uuid)) { return; }
        ContentValues values = new ContentValues();
        values.put("UUID", uuid);
        values.put("JSON", json);
        db.insert("CLASSES", null, values);
    }
    public void removeClass(String uuid) {
        db.delete("CLASSES", "UUID = ?", new String[] {uuid});
    }
    public String retrieveClass(String uuid) {
        Cursor cursor = db.rawQuery("SELECT JSON FROM CLASSES WHERE UUID = '" + uuid + "'", null);
        if (cursor.moveToFirst()) {
            int jsonIndex = cursor.getColumnIndex("JSON");
            String json = cursor.getString(jsonIndex);
            cursor.close();
            Log.d("Database", json);
            return json;
        } else {
            Log.d("Database", "No class found!");
        }
        return null;
    }


    public Cursor getItems(long fk, String Type)
    {
        return db.rawQuery("SELECT pk, JSON FROM ITEMS WHERE fk = " + fk +  " AND TYPE = '" + Type + "'", null );
    }

    public Cursor getAllItems(long fk)
    {
        return db.rawQuery("SELECT pk, JSON FROM ITEMS WHERE fk = " + fk, null);
    }

    public long setItem(long fk, String json, String Type)
    {
        ContentValues values = new ContentValues();

        values.put("JSON", json);
        values.put("TYPE", Type);
        values.put("fk", fk);
        long key = db.insert("ITEMS", null, values);
        return key;
    }

    public void delete(long pk, String Type) {
        db.delete(Type, "pk = ?", new String[]{String.valueOf(pk)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}