package search4.daobeans;

import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;

import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Stateless
public class DisplayMovieDAOBean implements Serializable{

    @PersistenceContext
    private EntityManager entityManager;

    public MovieEntity getMovieData(Integer id) throws Exception{
        try {
            return (MovieEntity) entityManager.createNamedQuery("MovieEntity.getMovieById")
                    .setParameter("id", id)
                    .getSingleResult();
        }
        catch (NoResultException ne) {
            throw new DataNotFoundException("No movie in database with that ID ("+id+")");
        }
    }

    public boolean movieExists(Integer id) {
        try {
            getMovieData(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Movie should ideally return a Status object with the status (DataNotFound or similar, or OK) + the Object
    //However, due to lack of time, returns null in case of error and handled higher up
    //Problem due to Java EE's way of handling thrown Exceptions and transaction rollbacks
    public MovieEntity getMovieByGuideboxId(Long guideboxId) throws DataNotFoundException {
        MovieEntity m = null;
        try {
            m = (MovieEntity) entityManager.createNamedQuery("MovieEntity.getWithGuideboxId")
                    .setParameter("guideboxId", guideboxId)
                    .getSingleResult();
            return m;
        }
        catch (NoResultException ne) {
            System.out.println("LOG: Error, No movie in database with that guidebox ID ("+guideboxId+")");
        }
        return m;
    }
}
