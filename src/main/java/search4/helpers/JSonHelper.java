package search4.helpers;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;

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
}
