package search4.ejb;

import search4.daobeans.UpdateDatabaseDAOBean;
import search4.ejb.interfaces.LocalUpdateDatabase;
import search4.entities.MovieEntity;
import search4.helpers.APIKeyReader;
import search4.helpers.DateParser;
import search4.helpers.MovieBubbleSort;

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
public class UpdateDatabaseEJB{

    @EJB
    private UpdateDatabaseDAOBean updateDatabaseDAOBean;

    //TODO implement jsonhelper
    public MovieEntity getMovieFromTMDB(int id) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        DateParser dateParser = new DateParser();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/";
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
            //Guidebox id defaults to 0

        } catch (Exception e) {
            System.err.println("No Movie in TMDB with that ID ("+id+")" + e);
            movieEntity = null;
        }
        return movieEntity;
    }

    //TODO return object instead of boolean for error message purposes and such? or convert to message here and return to backing bean?
    public boolean updateDatabase() {
        Integer start;
        Integer startMod;
        Integer limit;
        List<MovieEntity> movieEntities;
        MovieBubbleSort bubbleSort = new MovieBubbleSort();

        //TODO this makes us loop through the list twice, is this wise?
        startMod = 0;
        for (int i = 0; i < 500; i++) {
            start = getLastTMDBIdFromDB()+startMod;
            limit = getTMDBLimit(start);
            movieEntities = getMoviesInInterval(start, limit);
            if (movieEntities.size() < 1) {
                startMod += 40;
            }
            else {
//                bubbleSort.bubbleSort(movieEntities); //TODO write proper sorting algorithm    THIS IS UNESSECARY
                insertMovies(movieEntities);
                startMod = 0;
            }
            try {
                Thread.sleep(11*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public Integer getTMDBLimit(int start) {
        //TODO get last added from TMDB API
        return start+40;
    }
    public Integer getLastTMDBIdFromDB() {
        return updateDatabaseDAOBean.getLastTmdbId()+1;
    }

    //TODO private? return value boolean or object?
    private void insertMovies(List<MovieEntity> movies) {
        for (MovieEntity movieEntity : movies) {
            updateDatabaseDAOBean.createMovie(movieEntity);
        }
    }


    //TODO private?
    private List<MovieEntity> getMoviesInInterval(Integer start, Integer limit) {
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
