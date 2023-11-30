package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Item extends ClassManager {
    private final static String API_KEY = "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";
    private final static String AI_MODEL = "gpt-3.5-turbo";
    private final static String URL_STR = "https://api.openai.com/v1/chat/completions";

    // Format Variables: Item Type, Level, Race, Class
    private final static String ITEM_PROMPT =
            "Create and explain a DnD %s for a level %d %s %s using the format <Name> <Description>\n"
            + "Do not provide any extra info, only the name and description.";

    // Format Variables: Model Name, Prompt
    private final static String REQUEST_PROMPT = "{\"model\": \"%s\", "
            + "\"messages\": [{\"role\": \"user\", \"content\": \"%s\"}]}";


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

    public static Item Generate(String type, Character character) throws IOException, JSONException {
        HttpURLConnection connection = GetConnection();

        String formattedPrompt = String.format(ITEM_PROMPT, type, 5, "human", "bard");
        String requestBody = String.format(REQUEST_PROMPT, AI_MODEL, formattedPrompt);
        connection.setDoOutput(true);

        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(requestBody);
        writer.flush();
        writer.close();

        String responseText;

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String output;
            StringBuffer response = new StringBuffer();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            br.close();

            Log.d("Response", response.toString());

            //JSONObject responseJson = new JSONObject(response.toString());
            //responseText = responseJson.getString("content");
        }

        return null;
    }
    private static HttpURLConnection GetConnection() throws IOException {
        URL url = new URL(URL_STR);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");
        return connection;
    }
}
