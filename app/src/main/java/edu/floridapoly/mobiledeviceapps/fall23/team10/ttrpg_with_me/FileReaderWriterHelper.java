package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Objects;

public class FileReaderWriterHelper {
    static final int EXPORT_CHARACTER = 1000;
    static final int IMPORT_CHARACTER = 2000;

    Context context;
    public FileReaderWriterHelper(Context c)
    {
        context = c;
    }

    // NOTE: Number of Characters is capped at 1000 because of this!
    public void CreateFile(Character character) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TITLE, character.name + "_Data.json");
        startActivityForResult((Activity) context, intent, EXPORT_CHARACTER + character.id, null);
    }
    public void GetFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        startActivityForResult((Activity) context, intent, IMPORT_CHARACTER, null);
    }

    public boolean exporter(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.d("FileWrite", "Failed request or result!");
            return false;
        }
        Uri uri = data.getData();
        Log.d("FileWrite", data.toString());
        int cid = requestCode - EXPORT_CHARACTER;
        if (cid < 0) {
            Log.d("FileWrite", "Character Index Failure!");
            return false;
        }
        Character character = (Character) Character.getObject(Character.class, cid);

        DatabaseManager db = new DatabaseManager(context);
        try {
            JSONObject characterJson = new JSONObject(character.toJson());

            JSONObject json = new JSONObject().put("Character", characterJson);
            JSONObject classJson = new JSONObject(character.classArc.toJson());
            json.put("Class", classJson);

            Cursor c = db.getAllItems(character.pk);
            if (c.moveToFirst()) {
                JSONArray items = new JSONArray();
                do {
                    int jsonIndex = c.getColumnIndex("JSON");
                    JSONObject item = new JSONObject(c.getString(jsonIndex));
                    items.put(item);
                } while (c.moveToNext());
                json.put("Items", items);
            }
            c.close();

            String jsonString = json.toString();
            OutputStream output = context.getContentResolver().openOutputStream(uri);
            output.write(jsonString.getBytes());
            output.flush();
            output.close();

            db.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("FileWrite", "Failed to write to file!");
            db.close();
        }
        return false;
    }
    public Character importer(int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) { return null; }
        Uri uri = data.getData();
        DatabaseManager db = new DatabaseManager(context);
        try {
            StringBuilder stringBuilder = new StringBuilder();
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONObject json = new JSONObject(stringBuilder.toString());

            JSONObject characterJson = json.getJSONObject("Character");
            long pk = db.addLine("CHARACTERS", characterJson.toString());
            JSONObject classJson = json.getJSONObject("Class");
            db.addClass(classJson.getString("uuid"), classJson.toString());

            if (json.has("Items")) {
                JSONArray itemArray = json.getJSONArray("Items");
                int numItems = itemArray.length();
                for (int i = 0; i < numItems; i++) {
                    JSONObject itemJson = itemArray.getJSONObject(i);
                    db.setItem(pk, itemJson.toString(), itemJson.getString("type"));
                }
            }

            Character character = Character.fromJson(characterJson.toString(), Character.class);
            Character.IntializeItems(character);
            Character.trackObject(character);
            character.classArc = ClassArchetype.fromJson(classJson.toString(), ClassArchetype.class);
            character.class_uuid = character.classArc.uuid;

            return character;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("FileRead", "Failed to read from file!");
            db.close();
        }
        return null;
    }
}