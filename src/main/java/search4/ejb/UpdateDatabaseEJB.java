package search4.ejb;

import search4.daobeans.UpdateDatabaseDAOBean;
import search4.ejb.interfaces.LocalUpdateDatabase;
import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.helpers.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UpdateDatabaseEJB implements LocalUpdateDatabase{

    @EJB
    private UpdateDatabaseDAOBean updateDatabaseDAOBean;

    public MovieEntity getMovieFromTMDB(Integer tmdbId) throws Exception{
        MovieEntity movieEntity = new MovieEntity();
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();
        DateParser dateParser = new DateParser();

        String url = urlBuilder.tmdbUrl(tmdbId);

        try {
            JsonObject jsonObject = jSonHelper.getObject(url);
            movieEntity.setTitle(jsonObject.getString("original_title"));
            movieEntity.setTmdbId(tmdbId);
            movieEntity.setDate(dateParser.getDateFromString(jsonObject.getString("release_date")));
        } catch (Exception e) {
            throw new DataNotFoundException("HORRIBLE ERROR"); //TODO figure out posssible exceptions thrown, and send a proper message
        }
        return movieEntity;
    }

    public void updateDatabase() throws Exception{
        Integer start;
        Integer startMod;
        Integer limit;
        List<MovieEntity> movieEntities;

        //Marked methods both loops through the list of movies; combine them? Bad idea from an OOP perspective, good from an computer resource perspective
        startMod = 0;
        for (int i = 0; i < 500; i++) {
            start = getLastTMDBIdFromDB()+startMod;
            limit = getTMDBLimit(start);
            movieEntities = getMoviesInInterval(start, limit); //#1
            if (movieEntities.size() < 1) {
                startMod += 40;
            }
            else {
                insertMovies(movieEntities); //#2
                startMod = 0;
            }
            try {
                Thread.sleep(11*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Integer getTMDBLimit(int start) {
        //TODO get last tmdb movie added
        return start+40;
    }
    public Integer getLastTMDBIdFromDB() throws Exception {
        return updateDatabaseDAOBean.getLastTmdbId()+1;
    }

    private void insertMovies(List<MovieEntity> movies) throws Exception {
        for (MovieEntity movieEntity : movies) {
            updateDatabaseDAOBean.createMovie(movieEntity);
        }
    }

    private List<MovieEntity> getMoviesInInterval(Integer start, Integer limit) throws Exception{
        List<MovieEntity> movieInterval = new ArrayList<MovieEntity>();
        MovieEntity currentMovie = null;
        for (int i = start; i < limit; i++) {
            try {
                currentMovie = getMovieFromTMDB(i);
            } catch (DataNotFoundException dnfe) {
                System.err.println("Data not found: "+dnfe); //TODO send to admin frontend in future
            }
            catch (Exception e) {
                System.err.println(""+e);
                currentMovie = null;
            }
            if (currentMovie != null) {
                movieInterval.add(currentMovie);
            }
        }
        return movieInterval;
    }
}
