package search4.daobeans;

import search4.entities.MovieEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class DisplayMovieDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

    public MovieEntity getMovieData(Integer id) {
        return  (MovieEntity) entityManager.createNamedQuery("MovieEntity.getMovieById").setParameter("id", id).getSingleResult(); //TODO failsafe? can it be wrong?
    }
}
