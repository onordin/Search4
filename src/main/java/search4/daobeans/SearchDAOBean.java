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

    public List<MovieEntity> search(String query) {
        return entityManager.createNamedQuery("MovieEntity.search")
                .setParameter("query", query+"%")
                .setParameter("query", "% "+query+"%")
                .getResultList();
    }

    public List<MovieEntity> searchOrderByDateAsc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByDateAsc")
                .setParameter("query", query+"%")
                .setParameter("query2", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MovieEntity> searchOrderByDateDesc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByDateDesc")
                .setParameter("query", query+"%")
                .setParameter("query2", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MovieEntity> searchOrderByTitleAsc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByTitleAsc")
                .setParameter("query", query+"%")
                .setParameter("query2", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }

    public List<MovieEntity> searchOrderByTitleDesc(String query, Integer limit) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByTitleDesc")
                .setParameter("query", query+"%")
                .setParameter("query2", "% "+query+"%")
                .setMaxResults(limit)
                .getResultList();
    }
}
