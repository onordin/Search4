package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.helpers.APIKeyReader;
import search4.helpers.DateParser;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

//TODO interface?
@Stateless
public class DisplayMovieEJB {

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    public MovieEntity getMovieData(Integer id) {
    	MovieEntity movieEntity = displayMovieDAOBean.getMovieData(id); //TODO anything we want to handle here?
    	Integer guideBoxId = requestGuideBoxIdFromTMDbId(movieEntity.getTmdbId());
    	if(guideBoxId != -1) {
    		movieEntity.setGuideboxId(guideBoxId);
    		if(displayMovieDAOBean.updateMovie(movieEntity)) {
    			System.out.println("Guidebox Id added to movie in database");
    		}
    	}
    	return movieEntity;
    }

    /*
    public DisplayMovieEntity getMovieInfo(Integer tmdbId) {
        return getFullMovieFromTMDB(tmdbId);
=======
    public DisplayMovieEntity getMovieInfoAndUpdateGuideboxId(MovieEntity movieEntity) {
        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
        checkIfGuideboxSet(movieEntity);
        retrieveStreamingServices(displayMovieEntity, movieEntity.getGuideboxId());
        getFullMovieFromTMDB(displayMovieEntity, movieEntity.getTmdbId());
        return displayMovieEntity;
    }

    public void checkIfGuideboxSet(MovieEntity movieEntity) {
        //TODO check if guidebox ID is set in movieEntity, otherwise make call and set it
    }

    public void retrieveStreamingServices(DisplayMovieEntity displayMovieEntity, Integer guideBoxId) {
        //TODO retrieve streaming services from guidebox
>>>>>>> 23dc4b919a30eee2208562aed5ffc38df9620922
    }
    */
    
    

    public DisplayMovieEntity getFullMovieFromTMDB(MovieEntity movie) {

        APIKeyReader apiKeyReader = new APIKeyReader();
        DateParser dateParser = new DateParser();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/"; //TODO import from file?
        String tmdbAPIKey = apiKeyReader.getKey("tmdb");

        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
        int tmdbId = movie.getTmdbId();
        
        try {
            String url = tmdbUrl+tmdbId+tmdbAPIKey;
            InputStream is = new URL(url).openStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();

            displayMovieEntity.setTitle(jsonObject.getString("original_title"));
            displayMovieEntity.setDescription(jsonObject.getString("overview"));
            displayMovieEntity.setDate(dateParser.getDateFromString(jsonObject.getString("release_date")));
            displayMovieEntity.setPosterUrl("http://image.tmdb.org/t/p/w185"+jsonObject.getString("poster_path"));

            if(movie.getGuideboxId() != 0) {
            	displayMovieEntity.setLinks(getSourcesFromGuideBoxId(movie.getGuideboxId()));
            }
            
        } catch (Exception e) {
            System.err.println("No Movie in TMDB with that ID ("+tmdbId+")" + e);
            //TODO handle error?
        }
        
        return displayMovieEntity;
    }
    
    
    
    
    public Integer requestGuideBoxIdFromTMDbId(Integer TMDbId) {
		
		Integer guideBoxId = -1;
		
		APIKeyReader apiKeyReader = new APIKeyReader();
        String guideBoxUrl = "https://api-public.guidebox.com/v1.43/US/"; 
        String guideBoxAPIKey = apiKeyReader.getKey("guidebox");
        String endQuery = "/search/movie/id/themoviedb/";

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
    
    
    public List<String> getSourcesFromGuideBoxId(Integer guideboxId) {
		
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
		
        return listOfAllSources;
	}



}
