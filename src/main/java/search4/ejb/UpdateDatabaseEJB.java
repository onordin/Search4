package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.daobeans.UpdateDatabaseDAOBean;
import search4.ejb.interfaces.*;
import search4.entities.DisplayMovieEntity;
import search4.entities.DisplayProviderEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.helpers.*;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UpdateDatabaseEJB implements LocalUpdateDatabase, Serializable{

    @EJB
    private UpdateDatabaseDAOBean updateDatabaseDAOBean;
    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;
    @EJB
    private LocalDisplayMovie displayMovieEJB;
    @EJB
    private LocalUser userEJB;
    @EJB
    private LocalEmail emailEJB;
    @EJB
    private LocalProvider providerEJB;

    public void getMovieChanges() throws Exception {
        URLBuilder urlBuilder = new URLBuilder();
        JSonHelper jSonHelper = new JSonHelper();
        Long lastUpdate = getLastChanges();

        if (lastUpdate == null) { //TODO fix
            throw new Exception("NO NO NO NONO");
        }

        String path = "/updates/movies/changes/";
        String endQuery = "?limit=100&page=1";
        String url = urlBuilder.guideboxUpdateUrl(path, lastUpdate, endQuery);
        JsonObject jsonObject = jSonHelper.getObject(url);

        List<JsonObject> jsonMovies = jSonHelper.getObjectList(jsonObject, "results");

        List<MovieEntity> movieEntities = getMovieEntitiesByGuideboxId(jsonMovies);

        for (MovieEntity movie : movieEntities) {
            List<DisplayUserEntity> displayUsers = userEJB.getDisplayUsersSubscribedTo(movie.getId());
            DisplayMovieEntity displayMovieEntity = displayMovieEJB.createDisplayMovie(movie);
            List<DisplayUserEntity> usersWithMatching = userWithMatichingProviders(displayUsers, displayMovieEntity);
            for (DisplayUserEntity userEntity : usersWithMatching) {
                emailEJB.sendNotificationMail(userEntity, displayMovieEntity);
            }
        }
        setLastChanges(getCurrentDate());
    }

    private List<DisplayUserEntity> userWithMatichingProviders (List<DisplayUserEntity> allSubscribedUsers, DisplayMovieEntity displayMovieEntity) {
        List<DisplayUserEntity> matchingUsers = new ArrayList<DisplayUserEntity>();
        for (DisplayUserEntity userEntity : allSubscribedUsers) {
            List<DisplayProviderEntity> desiredProviders = providerEJB.getAllForUser(userEntity.getId());
            List<String> movieProviders = displayMovieEntity.getCurrentProviders();
            String tempProvider;
            for (DisplayProviderEntity displayProvider : desiredProviders) {
                tempProvider = displayProvider.getProvider();
                if (movieProviders.contains(tempProvider)) {
                    matchingUsers.add(userEntity);
                    break;
                }
            }
        }
        return matchingUsers;
    }

    private List<MovieEntity> getMovieEntitiesByGuideboxId(List<JsonObject> jsonMovies) {
        List<MovieEntity> movieEntities = new ArrayList<MovieEntity>();
        MovieEntity movieEntity;
        for (JsonObject obj : jsonMovies) {
            Long gid = Long.parseLong(obj.get("id").toString());
            movieEntity = displayMovieDAOBean.getMovieByGuideboxId(gid);
            if (movieEntity != null) { //Uggly hack, see DisplayMovieDAOBean.getMovieByGuideboxId() for explanation
                movieEntities.add(movieEntity);
            }
        }
        return movieEntities;
    }

    private Long getCurrentDate() {
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();
        String url = urlBuilder.guideboxDateUrl();
        JsonObject jsonObject = jSonHelper.getObject(url);
        Long date = Long.parseLong(jsonObject.get("results").toString());
        return date;
    }
    private void setLastChanges(Long date) {
        SingleFileReaderAndWriter fileWriter = new SingleFileReaderAndWriter();
        fileWriter.writeDate(date);
    }

    private Long getLastChanges() {
        SingleFileReaderAndWriter fileReader = new SingleFileReaderAndWriter();
        Long s = fileReader.readDate();
        return s;
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
