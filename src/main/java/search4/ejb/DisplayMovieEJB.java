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

    public DisplayMovieEntity getMovieInfo(Integer tmdbId) {
        return getFullMovieFromTMDB(tmdbId);
    }

    public DisplayMovieEntity getFullMovieFromTMDB(Integer tmdbId) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        DateParser dateParser = new DateParser();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/"; //TODO import from file?
        String tmdbAPIKey = apiKeyReader.getKey("tmdb");

        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();

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
            displayMovieEntity = null;
        }
        return displayMovieEntity;
    }
}
