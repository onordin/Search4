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


    public boolean createMovie(MovieEntity movieEntity) {
        if (entityManager.merge(movieEntity) != null) {
            return true;
        }
        return false;
    }

    public Integer getLastTmdbId() {
        List<Integer> resultList = entityManager.createNamedQuery("MovieEntity.getLastTmdbId").getResultList();
        Integer tmdbId = resultList.get(0);
        if (tmdbId < 1) {
            return 1;
        }
        return tmdbId;
    }

    public boolean updateDatabaseWithMovieList(List<MovieEntity> movieList) {
        //TODO temporary to test
        for (MovieEntity movie : movieList) {
            System.out.println(entityManager.merge(movie));
        }
        return false;
    }

}
