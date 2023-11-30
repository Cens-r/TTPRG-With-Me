package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;

import org.json.JSONObject;

public class FileReaderWriterHelper
{
    Context ctx;
    DatabaseManager db;
    JSONObject json;
    long pk;
    FileReaderWriterHelper(Context context, long PK)
    {
        pk = PK;
        ctx = context;
        db = new DatabaseManager(ctx);
    }

    public void newThing(JSONObject J, String table)
    {
        db.open(table);
        db.insert(J.toString());
        json = J;

        db.close();
    }
    public void save()
    {
        try {
            db.open(json.get("Type").toString());
            db.update(pk, json.toString());
            db.close();
        } catch (Exception e)
        {

        }
    }
}
