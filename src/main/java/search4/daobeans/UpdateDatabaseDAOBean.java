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

    public MovieEntity getMovieWithTmdbId(Integer tmdbId) throws Exception{
        try {
            return (MovieEntity) entityManager.createNamedQuery("MovieEntity.getMovieByTmdbId").setParameter("tmdbId", tmdbId).getSingleResult();
        } catch (NoResultException nre) {
            throw new DataNotFoundException("No movie in database with that TMdB ID ("+tmdbId+")");
        }
    }

    public boolean createMovie(MovieEntity movieEntity) throws Exception{
        Integer tmdbId = movieEntity.getTmdbId();
        MovieEntity result = null;
        try {
            result = entityManager.merge(movieEntity);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to insert movie in database.");
        }
        if (getMovieWithTmdbId(tmdbId) != null) { //TODO this should be done before merge?!
            throw new DuplicateDataException("Already exists movie in database with that TMdB ID ("+tmdbId+")");
        }
        else if (result == null) {
            throw new InternalServerErrorException("Failed to insert entity in database."); //TODO unreacable?
        }
        return true; //TODO return type?
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
