package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.daobeans.UpdateDatabaseDAOBean;
import search4.ejb.interfaces.LocalUpdateDatabase;
import search4.ejb.interfaces.LocalUser;
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
    private LocalUser userEJB;
    @EJB
    private EmailEJB emailEJB;

    public void getMovieChanges() throws Exception {
        URLBuilder urlBuilder = new URLBuilder();
        JSonHelper jSonHelper = new JSonHelper();
        Long lastUpdate = getLastChanges();

        if (lastUpdate == null) { //TODO fix
            throw new Exception("NO NO NO NONO");
        }

//        String path = "/updates/movies/changes/"; //TODO shows returns correctly for test, but is it properly?
        String path = "/updates/shows/changes/";
        String endQuery = "?limit=100&page=1";
        String url = urlBuilder.guideboxUpdateUrl(path, lastUpdate, endQuery);
        JsonObject jsonObject = jSonHelper.getObject(url);

        List<JsonObject> jsonMovies = jSonHelper.getObjectList(jsonObject, "results");

        //TODO currently this always returns 0 with a bunch of transactionRollbacks
        List<MovieEntity> movieEntities = getMovieEntitiesByGuideboxId(jsonMovies);
        System.out.println(movieEntities);
        System.out.println("l: "+movieEntities.size());
        System.out.println("TEST");
        for (MovieEntity movie : movieEntities) {
            List<DisplayUserEntity> displayUsers = userEJB.getDisplayUsersSubscribedTo(movie.getId());
            for (DisplayUserEntity userEntity : displayUsers) {
                System.out.println("Send mail to "+userEntity.getEmail()+" that "+movie.getTitle()+" is available");
//                emailEJB.sendNotificationMail(userEntity, movie);
            }
        }
        setLastChanges(getCurrentDate());
    }

    private List<MovieEntity> getMovieEntitiesByGuideboxId(List<JsonObject> jsonMovies) {
        List<MovieEntity> movieEntities = new ArrayList<MovieEntity>();
        MovieEntity movieEntity = null;
        String tried = "";
        for (JsonObject obj : jsonMovies) {
//            System.out.println("Another try for "+obj);
            try {
//                System.out.println("This happens");
                Long gid = Long.parseLong(obj.get("id").toString());
//                System.out.println("Id: "+gid);
                tried += ""+gid+", ";
                movieEntity = displayMovieDAOBean.getMovieByGuideboxId(gid);
//                movieEntity = displayMovieDAOBean.getMovieData(100);
//                System.out.println("Got movie: "+movieEntity);
                if (movieEntity != null) {
                    movieEntities.add(movieEntity);
                }
            } catch (DataNotFoundException dnfe) {
//                System.err.println(dnfe);
//                System.err.println("DNFE");
            }
            catch (Exception e) {
//                System.err.println("Error retrieving: "+e);
//                System.err.println("OTHER11");
            }
        }
        System.out.println("TRIDE: "+tried);
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
