package search4.daobeans;

import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Stateful
public class DisplayMovieDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

    public MovieEntity getMovieData(Integer id) throws DataNotFoundException{
        try {
            return (MovieEntity) entityManager.createNamedQuery("MovieEntity.getMovieById").setParameter("id", id).getSingleResult(); //TODO failsafe? can it be wrong?
        }
        catch (NoResultException ne) {
            throw new DataNotFoundException("No movie in database with that ID ("+id+")");
        }
    }
}
