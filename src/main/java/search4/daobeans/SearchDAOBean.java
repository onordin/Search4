package search4.daobeans;

import search4.entities.MovieEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful
public class SearchDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

    public List<MovieEntity> search(String parameter) {
        List<MovieEntity> movies = entityManager.createNamedQuery("MovieEntity.search").setParameter("query", parameter+"%").getResultList(); //TODO solve NamedQueries in MovieEntity when internet
        movies.addAll(entityManager.createNamedQuery("MovieEntity.search").setParameter("query", "% "+parameter+"%").getResultList());
        return movies; //TODO return directly when works
    }
}
