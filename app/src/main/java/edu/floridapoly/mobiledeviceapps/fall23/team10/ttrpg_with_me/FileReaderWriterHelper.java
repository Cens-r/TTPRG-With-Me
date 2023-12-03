package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
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
            String name = json.getString("name");
            Writer output = null;
            File file = new File("storage/sdcard/Documents/" +name +".json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(json.toString());
            output.close();


        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return json;
    }
}