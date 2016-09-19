package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.helpers.APIKeyReader;
import search4.helpers.DateParser;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;

//TODO interface?
@Stateless
public class DisplayMovieEJB {

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    public MovieEntity getMovieData(Integer id) {
        return displayMovieDAOBean.getMovieData(id); //TODO anything we want to handle here?
    }

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
    }

    public void getFullMovieFromTMDB(DisplayMovieEntity displayMovieEntity, Integer tmdbId) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        DateParser dateParser = new DateParser();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/"; //TODO import from file?
        String tmdbAPIKey = apiKeyReader.getKey("tmdb");
        try {
            String url = tmdbUrl+tmdbId+tmdbAPIKey;
            InputStream is = new URL(url).openStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();

            displayMovieEntity.setTitle(jsonObject.getString("original_title"));
            displayMovieEntity.setDescription(jsonObject.getString("overview"));
            displayMovieEntity.setDate(dateParser.getDateFromString(jsonObject.getString("release_date")));
            displayMovieEntity.setPosterUrl("http://image.tmdb.org/t/p/w185"+jsonObject.getString("poster_path"));

        } catch (Exception e) {
            System.err.println("No Movie in TMDB with that ID ("+tmdbId+")" + e);
            //TODO handle error?
        }
    }
}
