package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.daobeans.UpdateDatabaseDAOBean;
import search4.ejb.interfaces.LocalUpdateDatabase;
import search4.email.EmailEJB;
import search4.entities.DisplayMovieEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.helpers.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UpdateDatabaseEJB implements LocalUpdateDatabase{

    @EJB
    private UpdateDatabaseDAOBean updateDatabaseDAOBean;
    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;
    @EJB
    private DisplayMovieEJB displayMovieEJB;
    @EJB
    private UserEJB userEJB;
    @EJB
    private EmailEJB emailEJB;

    public void getMovieChanges() throws Exception {
        URLBuilder urlBuilder = new URLBuilder();
        JSonHelper jSonHelper = new JSonHelper();
        Date currentDate = new Date(new java.util.Date().getTime());
        Date lastUpdate = new Date(new java.util.Date().getTime()-604800000);
        Long current = currentDate.getTime();
        Long last = lastUpdate.getTime();

        String path = "updates/movies/changes/";
        String endQuery = "?limit=100&page=1";
        String url = urlBuilder.guideboxUpdateUrl(path, last, endQuery);

        JsonObject jsonObject = jSonHelper.getObject(url);
        List<JsonObject> movies = jSonHelper.getObjectList(jsonObject, "results");

        List<MovieEntity> movieEntities = new ArrayList<MovieEntity>();
        for (JsonObject obj : movies) {
            movieEntities.add(displayMovieDAOBean.getMovieByGuideboxId(obj.getInt("id")));
        }
        List<DisplayMovieEntity> displayMovies = new ArrayList<DisplayMovieEntity>();
        for (MovieEntity movieEntity : movieEntities) {
            displayMovies.add(displayMovieEJB.createDisplayMovie(movieEntity));
        }
        DisplayMovieEntity displayMovie;
        MovieEntity movie;
        for (int i = 0; i < displayMovies.size(); i++) {
            List<DisplayUserEntity> displayUsers = userEJB.getDisplayUsersSubscribedTo(movieEntities.get(i).getId());
            for (DisplayUserEntity userEntity : displayUsers) {
                emailEJB.sendNotificationMail(userEntity, "LINK TO MOVIE", movieEntities.get(i).getTitle());
            }
        }
    }

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
            throw new DataNotFoundException("Unknown Error: "+e); //TODO figure out posssible exceptions thrown, and send a proper message
        }
        return movieEntity;
    }

    //TODO probably rewrite this so it works better and is easier to use, hard for outside user to understand passes
    public void updateDatabase(Integer passes) throws Exception{
        Integer start;
        Integer startMod;
        Integer limit;
        List<MovieEntity> movieEntities;

        //Marked methods both loops through the list of movies; combine them? Bad idea from an OOP perspective, good from an computer resource perspective
        startMod = 0;
        for (int i = 0; i < passes; i++) {
            start = getLastTMDBIdFromDB()+startMod;
            if (passes == 0) {
                limit = getTMDBLimit(start);
            } else {
                limit = start+40;
            }
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
