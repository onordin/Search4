package search4.ejb;

import java.io.InputStream;
import java.net.URL;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import search4.daobeans.SearchDatabaseDAOBean;
import search4.entities.MovieEntity;
import search4.helpers.APIKeyReader;
import search4.helpers.DateParser;

@Stateless
public class SearchDatabaseEJB {

	@EJB
	private SearchDatabaseDAOBean searchDatabaseDAOBean;

	
	public Integer searchDatabaseFindTMDbId(String title) {
		MovieEntity movie = searchDatabaseByTitle(title);
		if(movie != null) {
			return movie.getTmdbId();
		}else {
			return null;
		}
	}
	
	
	public MovieEntity searchDatabaseByTitle(String title) {
		MovieEntity movie = null;
		movie = searchDatabaseDAOBean.findTMDbIdFromTitle(title);
		return movie;
	}
	
	
	public Integer requestGuideBoxIdFromMovieTitle(String title) {
		
		int guideBoxId = 0;
		MovieEntity movie = searchDatabaseByTitle(title);
		
		APIKeyReader apiKeyReader = new APIKeyReader();
        String guideBoxUrl = "https://api-public.guidebox.com/v1.43/US/"; 
        String guideBoxAPIKey = apiKeyReader.getKey("guidebox");
        String endQuery = "/search/movie/id/themoviedb/";
        int TMDbId = movie.getTmdbId();

        try {
            String url = guideBoxUrl+guideBoxAPIKey+endQuery+TMDbId;
            System.out.println(url);
            InputStream is = new URL(url).openStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();

            guideBoxId = jsonObject.getInt("id");
            System.out.println("GuideBox ID: " + guideBoxId);
            
        } catch (Exception e) {
            System.err.println("No Movie in GuideBox with that ID " + e);
        }
		
		return guideBoxId;
	}
	
	
	
public void printSourcesFromGuideBoxId(int guideboxId) {
		
		List<String> listOfAllSources = new ArrayList<String>();
	
		APIKeyReader apiKeyReader = new APIKeyReader();
        String guideBoxUrl = "https://api-public.guidebox.com/v1.43/US/"; 
        String guideBoxAPIKey = apiKeyReader.getKey("guidebox");
        String endQuery = "/movie/";

        try {
            String url = guideBoxUrl+guideBoxAPIKey+endQuery+guideboxId;
            System.out.println(url);
            InputStream is = new URL(url).openStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();
            
            JsonArray array = (JsonArray) jsonObject.get("purchase_web_sources");

            for(int i = 0; i < array.size(); i++) {
            	String currentSource = array.getJsonObject(i).getString("link");
            	listOfAllSources.add(currentSource);
            }            
        } catch (Exception e) {
            System.err.println("No Movie in GuideBox with that ID " + e);
        }
		
        for(String source : listOfAllSources) {
        	System.out.println("Link to movie: " + source);
        }
	}
	
}
