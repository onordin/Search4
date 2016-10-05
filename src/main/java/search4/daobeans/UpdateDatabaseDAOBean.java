package search4.daobeans;

import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.DuplicateDataException;

import javax.batch.operations.JobExecutionNotRunningException;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.InternalServerErrorException;
import java.util.List;

@Stateful
public class UpdateDatabaseDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

    public MovieEntity getMovieWithTmdbId(Integer tmdbId) throws DataNotFoundException{
        try {
            return (MovieEntity) entityManager.createNamedQuery("MovieEntity.getMovieByTmdbId").setParameter("tmdbId", tmdbId).getSingleResult();
        } catch (NoResultException nre) {
            throw new DataNotFoundException("No movie in database with that TMdB ID ("+tmdbId+")");
        }
    }

    private boolean movieWithIdInDB(Integer tmdbId) throws DataNotFoundException{
        if (getMovieWithTmdbId(tmdbId) != null) {
            return true;
        }
        return false;
    }

    public boolean createMovie(MovieEntity movieEntity) throws Exception{
        if (movieEntity == null) { //Just to be super duper sure
            return false;
        }
        Integer tmdbId = movieEntity.getTmdbId();
        if (movieWithIdInDB(tmdbId)) {
            throw new DuplicateDataException("Already exists movie in database with that TMdB ID ("+tmdbId+")");
        }
        MovieEntity result = null;
        try { //TODO possibly encase above if in this; if it throws DataNotFound, will it work properly? Wait for REST
            result = entityManager.merge(movieEntity);
            return true;
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to insert movie in database.");
        }
    }

    public Integer getLastTmdbId() throws Exception{
        Integer tmdbId;
        try {
            tmdbId = (Integer) entityManager.createNamedQuery("MovieEntity.getLastTmdbId").getSingleResult();
        } catch (NoResultException nre) {
            throw new DataNotFoundException("Got nothing from database when trying to get last TMdB ID"); //TODO will this explode with an empty database?
        }
        if (tmdbId < 1) {
            return 1;
        }
        return tmdbId;
    }
}
