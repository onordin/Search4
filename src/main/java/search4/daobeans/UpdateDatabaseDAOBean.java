package search4.daobeans;

import search4.entities.MovieEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful
public class UpdateDatabaseDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

    public MovieEntity getMovieWithTmdbId(Integer tmdbId) {
        return (MovieEntity) entityManager.createNamedQuery("MovieEntity.getMovieByTmdbId").setParameter("tmdbId", tmdbId).getSingleResult();
    }

    public boolean createMovie(MovieEntity movieEntity) {
        if (entityManager.merge(movieEntity) != null && getMovieWithTmdbId(movieEntity.getTmdbId()) == null) {
            return true;
        }
        return false;
    }

    public Integer getLastTmdbId() {
        Integer tmdbId = (Integer) entityManager.createNamedQuery("MovieEntity.getLastTmdbId").getSingleResult();
        if (tmdbId < 1) {
            return 1;
        }
        return tmdbId;
    }
}
