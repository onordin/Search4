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

    public List<MovieEntity> searchOrderByDateAsc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByDateAsc")
                .setParameter("input", query+"%")
                .setParameter("inputWithSpace", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MovieEntity> searchOrderByDateDesc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByDateDesc")
                .setParameter("input", query+"%")
                .setParameter("inputWithSpace", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MovieEntity> searchOrderByTitleAsc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByTitleAsc")
                .setParameter("input", query+"%")
                .setParameter("inputWithSpace", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MovieEntity> searchOrderByTitleDesc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByTitleDesc")
                .setParameter("input", query+"%")
                .setParameter("inputWithSpace", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }
}
