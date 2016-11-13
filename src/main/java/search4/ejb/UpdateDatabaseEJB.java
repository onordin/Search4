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
        
        if (lastUpdate == null) {
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
            throw new DataNotFoundException("Unknown Error: "+e); 
        }
        return movieEntity;
    }

    public void updateDatabase(Integer amount) throws Exception{
        Integer start;
        Integer limit;
        Integer tmdbLast;
        Integer current;
        Integer intervalLimit;
        List<MovieEntity> movieEntities;

        start = getLastTMDBIdFromDB();
        System.out.println("LOG: Start: " + start);
//        tmdbLast = getLatestMovieFromTmdb();
        tmdbLast = 100000;
        limit = start + amount;
        if (limit > tmdbLast || amount == 0) {
            limit = tmdbLast;
        }
        current = start;
        while (current < limit) {
            intervalLimit = current + 40;
            if (limit < intervalLimit) {
                intervalLimit = limit;
            }
            movieEntities = getMoviesInInterval(current, intervalLimit);
            insertMovies(movieEntities);
            current += intervalLimit;
            try {
                Thread.sleep(11*1000);
            } catch (InterruptedException ie) {
                System.out.println("LOG: Error, unexpected interrupt");
            }
        }
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
                System.err.println("LOG: Error, Data not found: " + dnfe);
            }
            catch (Exception e) {
                System.err.println("LOG: Unknown Error: " + e);
                currentMovie = null;
            }
            if (currentMovie != null) {
                movieInterval.add(currentMovie);
            }
        }
        return movieInterval;
    }

    private Integer getLatestMovieFromTmdb(){
        URLBuilder urlBuilder = new URLBuilder();
        JSonHelper jSonHelper = new JSonHelper();

        String path = "latest/";
        String url = urlBuilder.tmdbUrlUpdate(path);
        JsonObject jsonObject = jSonHelper.getObject(url);
        Integer latestId = jsonObject.getInt("id");
        return latestId;
    }
}
