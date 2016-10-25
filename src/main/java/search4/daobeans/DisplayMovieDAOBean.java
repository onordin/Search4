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

    public MovieEntity getMovieByGuideboxId(Integer guideboxId) throws Exception {
        try {
            return (MovieEntity) entityManager.createNamedQuery("MovieEntity.getWithGuideboxId")
                    .setParameter("guideboxId", guideboxId)
                    .getSingleResult();
        }
        catch (NoResultException ne) {
            throw new DataNotFoundException("No movie in database with that guidebox ID ("+guideboxId+")");
        }
    }
}
