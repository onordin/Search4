package search4.helpers;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSonHelper {

    public JsonObject getObject(String url) {
        try {
            InputStream is = new URL(url).openStream();

            JsonReader jsonReader = Json.createReader(is);
            return jsonReader.readObject();
        } catch (Exception e) {
            System.err.println("Error retrieving Json object: "+e);
        }
        return null;
    }

    public List<JsonObject> getObjectList(String url, String listId) {
        List<JsonObject> jsonObjects = new ArrayList<JsonObject>();
        try {
            JsonObject jsonObject = getObject(url);
            JsonArray jsonArray = (JsonArray) jsonObject.get(listId);

            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObjects.add(jsonArray.getJsonObject(i));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving Json Object List: " + e);
        }
        return jsonObjects;
    }
}
