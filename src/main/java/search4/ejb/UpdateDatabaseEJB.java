package search4.ejb;

import search4.daobeans.UpdateDatabaseDAOBean;
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
import java.util.ArrayList;
import java.util.List;

//TODO interface?

@Stateless
public class UpdateDatabaseEJB {

    @EJB
    private UpdateDatabaseDAOBean updateDatabaseDAOBean;

    public MovieEntity getMovieFromTMDB(int id) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        DateParser dateParser = new DateParser();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/"; //TODO import from file?
        String tmdbAPIKey = apiKeyReader.getKey("tmdb");

        MovieEntity movieEntity = new MovieEntity();

        try {
            String url = tmdbUrl+id+tmdbAPIKey;
            InputStream is = new URL(url).openStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();

            movieEntity.setTitle(jsonObject.getString("original_title"));
            movieEntity.setTmdbId(id);
            movieEntity.setDate(dateParser.getDateFromString(jsonObject.getString("release_date")));
            //TODO guidebox id? wrong place to set, default value?

        } catch (Exception e) {
            System.err.println("No Movie in TMDB with that ID ("+id+")" + e);
            movieEntity = null;
        }
        return movieEntity;
    }

    //TODO return object instead of boolean for error message purposes and such? or convert to message here and return to backing bean?
    public boolean updateDatabase() {
        /*
        check current tmdb id
        decide span/interval limit
        loop from id+1 to limit calling getMovieFormTMDB
            for each insert movie object into DB
         */

        //TODO this makes us loop through the list twice, is this wise?
        Integer start = getLastTMDBIdFromDB();
        Integer limit = getTMDBLimit();
        List<MovieEntity> movieEntities = getMoviesInInterval(start, limit);
        insertMovies(movieEntities);

        return true;
    }

    public Integer getTMDBLimit() {
        //TODO get last added from TMDB API
        return getLastTMDBIdFromDB()+6;
    }
    public Integer getLastTMDBIdFromDB() {
        //TODO get last from DB in a elegant fashion
        return 3;
    }

    //TODO private? return value boolean or object?
    public void insertMovies(List<MovieEntity> movies) {
        for (MovieEntity movieEntity : movies) {
            System.out.println("inserting movie "+movieEntity);
            updateDatabaseDAOBean.createMovie(movieEntity);
        }
    }


    //TODO private?
    public List<MovieEntity> getMoviesInInterval(Integer start, Integer limit) {
        List<MovieEntity> movieInterval = new ArrayList<MovieEntity>();
        MovieEntity currentMovie;
        for (int i = start; i < limit; i++) {
            currentMovie = getMovieFromTMDB(i);
            if (currentMovie != null) {
                movieInterval.add(currentMovie);
            }
        }
        return movieInterval;
    }
}
