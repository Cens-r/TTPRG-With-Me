package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.util.Log;

import androidx.databinding.ObservableField;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Item extends ClassManager {
    private final static String API_KEY = "AIzaSyDUohOwTPg5-OpYdRQupOXEFXh_l9WvYlc";
    private final static String URL_STR = "https://generativelanguage.googleapis.com/v1beta3/models/text-bison-001:generateText?key=" + API_KEY;

    // Format Variables: Item Type, Level, Race, Class
    private final static String ITEM_PROMPT =
            "Create and explain a DnD %s item for a level %d %s %s using the JSON format.\n" +
            "It should only have a name and description key.";


    ObservableField<String> name;
    ObservableField<String> description;
    ObservableField<String> type;
    ObservableField<Boolean> favorited;

    public Item(String name, String description) {
        // Initialized Values:
        this.name = new ObservableField<>(name);
        this.description = new ObservableField<>(description);
        // Default Values:
        this.type = new ObservableField<>("Extra");
        this.favorited = new ObservableField<>(false);

        trackObject(this);
    }
    public Item type(String type) {
        this.type.set(type);
        return this;
    }
    public Item favorited(boolean favorited) {
        this.favorited.set(favorited);
        return this;
    }

    // This method yields so it needs to be wrapped in an AsyncTask or Executor
    public static Item Generate(String type, Character character) {
        HttpURLConnection connection;
        Item item = null;
        try {
            connection = GetConnection();
            String formattedPrompt = String.format(ITEM_PROMPT, type, 5, "human", "bard");

            JSONObject requestJson = new JSONObject();
            JSONObject promptJson = new JSONObject();
            promptJson.put("text", formattedPrompt);
            requestJson.put("prompt", promptJson);

            WriteToConnection(connection, requestJson.toString());

            int responseCode = connection.getResponseCode();
            Log.d("WebResponse", "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                JSONObject responseJson = GetOutputJson(connection.getInputStream());
                Log.d("WebResponse", "Web Response: " + responseJson);
                String outputStr = responseJson.getJSONArray("candidates")
                        .getJSONObject(0).getString("output");

                String startStr = "```json\n";
                int startIndex = outputStr.indexOf(startStr) + startStr.length();
                int endIndex = outputStr.indexOf("\n```");
                if ((startIndex < endIndex) && (outputStr.length() > startIndex)) {
                    outputStr = outputStr.substring(startIndex, endIndex);
                }

                JSONObject itemJson = new JSONObject(outputStr);
                item = new Item(itemJson.getString("name"), itemJson.getString("description"))
                        .type(type);
            } else {
                JSONObject errorJson = GetOutputJson(connection.getErrorStream());
                Log.d("WebResponse", "Web Response Error: " + errorJson);
            }
        } catch (IOException | JSONException e) {
            Log.e("WebResponse", e.toString());
        }
        return item;
    }
    private static HttpURLConnection GetConnection() throws IOException {
        URL url = new URL(URL_STR);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        return connection;
    }
    private static void WriteToConnection(HttpURLConnection connection, String request) throws IOException {
        OutputStream outStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream, StandardCharsets.UTF_8));
        writer.write(request);
        writer.flush();
        writer.close();
        outStream.close();
        connection.connect();
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
