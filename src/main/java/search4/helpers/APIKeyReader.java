package search4.helpers;

import java.io.File;
import java.util.Scanner;

/**
 * Created by Adrian on 2016-09-14.
 * Simple class to read API keys from resource folders as to not have them pushed
 * Remember to follow conventions on api key files! Must be in resource folder, must end in .api
 */
public class APIKeyReader {

    //Parameter should be 'tmdb' or 'guidebox'
    public String getKey(String api) {
        //System.out.println("test  "+this.getClass().getResource("APIKeyReader.class")); //TODO use to see path to class
        String key = "";
        try {
            File file = new File(getClass().getResource("../../"+api+"_api_key.api").getFile());
            Scanner scanner = new Scanner(file);
            key = scanner.nextLine();
        } catch (Exception e) {
            System.err.println("Error reading "+api+" key "+e);
        }
        return key;
    }
}
