package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
    SQLiteDatabase db = getWritableDatabase();
    static final String table = " (pk INTEGER PRIMARY KEY AUTOINCREMENT, JSON TEXT);";
    public DatabaseManager(Context context) {
        super(context, "Dnd", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CHARACTERS" + table);
        db.execSQL("CREATE TABLE CLASSES" + table);
    }

    public long addLine(String Type, String json)
    {
        ContentValues values = new ContentValues();

        values.put("JSON", json);
        long key = db.insert(Type, null, values);
        return key;
    }

    public void update(long pk, String Type, String json)
    {
        ContentValues cv = new ContentValues();
        cv.put("JSON", json);
        if(pk == -1) {
            db.insert(Type, null, cv);
        } else {
            db.update(Type, cv, "pk = ?", new String[] { String.valueOf(pk) });
        }
    }

    public String getJson(long pk, String Type)
    {
        Cursor c = db.rawQuery("SELECT JSON FROM " + Type + " WHERE pk = " + pk, null);
        c.moveToFirst();

        int i  = c.getColumnIndex("JSON");
        String json = c.getString(i);
        c.close();
        return json;
    }

    public Cursor fetchAll(String Type)
    {
        Cursor c = db.rawQuery("SELECT * FROM " + Type, null);
        return c;
    }

    public void delete(long pk, String Type)
    {
        db.delete(Type, "pk = ?", new String[] {String.valueOf(pk)});
    }

    public void deleteLine(long pk, String Type)
    {
        db.execSQL("DELETE FROM " + Type + " WHERE pk = " + pk);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
