package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.Writer;

public class FileReaderWriterHelper {

    Context context;
    public FileReaderWriterHelper(Context c)
    {
        context = c;
    }
    public JSONObject exporter(JSONObject json){

        try {
            DatabaseManager db = new DatabaseManager(context);
            Cursor c = db.getAllItems(json.getLong("pk"));
            String name = json.getString("name");
            json = new JSONObject().put("Character", json);

            if(c.moveToFirst())
            {
                JSONArray items = new JSONArray();
                JSONObject temp = new JSONObject();
                do {
                    int jsonIndex = c.getColumnIndex("JSON");
                    temp = new JSONObject(c.getString(jsonIndex));
                    items.put(temp);

                } while (c.moveToNext());
                json.put("items", items);
            }

            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, name +".json");

            Log.d("Path", path.getAbsolutePath());

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(json.toString().getBytes());
            stream.close();

            db.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }
}