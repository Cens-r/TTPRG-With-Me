package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Item extends ClassManager {
    private final static String API_KEY = "AIzaSyDUohOwTPg5-OpYdRQupOXEFXh_l9WvYlc";
    private final static String URL_STR = "https://generativelanguage.googleapis.com/v1beta3/models/text-bison-001:generateText?key=" + API_KEY;

    // Format Variables: Item Type, Level, Race, Class
    private final static String ITEM_PROMPT =
            "Create and explain a DnD %s for a level %d %s %s using the JSON format.\n" +
            "It should only have a name and description key.";

    // Format Variables: Model Name, Prompt
    private final static String REQUEST_PROMPT = "{ \"prompt\": { \"text\": \"%s\" } }";


    String name;
    String description;
    String type;
    boolean favorited;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        trackObject(this);
    }
    public Item type(String type) {
        this.type = type;
        return this;
    }
    public Item favorited(boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    public static Item Generate(String type, Character character) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            HttpURLConnection connection;
            String itemName, itemDescription;
            try {
                connection = GetConnection();

                String formattedPrompt = String.format(ITEM_PROMPT, type, 5, "human", "bard");

                JSONObject requestJson = new JSONObject();
                JSONObject promptJson = new JSONObject();
                promptJson.put("text", formattedPrompt);
                requestJson.put("prompt", promptJson);

                OutputStream outStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8));
                String requestStr = requestJson.toString();
                writer.write(requestStr);
                writer.flush();
                writer.close();
                outStream.close();
                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.d("WebResponse", "Response Code: " + Integer.toString(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    JSONObject responseJson = GetOutputJson(connection.getInputStream());

                } else {
                    JSONObject errorJson = GetOutputJson(connection.getErrorStream());
                    Log.d("WebResponse", "Web Response Error: " + errorJson.toString());
                }
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }

            handler.post(() -> {
                // Generate an item here if itemName and itemDescription
            });
        });

        return null;
    }
    private static HttpURLConnection GetConnection() throws IOException {
        URL url = new URL(URL_STR);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        return connection;
    }
    private static JSONObject GetOutputJson(InputStream inStream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
        String output;
        StringBuffer response = new StringBuffer();
        while ((output = br.readLine()) != null) {
            response.append(output);
        }
        br.close();
        return new JSONObject(response.toString());
    }
}
