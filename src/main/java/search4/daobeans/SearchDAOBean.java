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
        return entityManager.createNamedQuery("MovieEntity.search")
                .setParameter("query", parameter+"%")
                .setParameter("query", "% "+parameter+"%")
                .getResultList();
    }

    public List<MovieEntity> searchOrderByDateAsc(String parameter) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByDateAsc")
                .setParameter("query", parameter+"%")
                .setParameter("query2", "% "+parameter+"%")
                .getResultList();
    }

    public List<MovieEntity> searchOrderByDateDesc(String parameter) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByDateDesc")
                .setParameter("query", parameter+"%")
                .setParameter("query2", "% "+parameter+"%")
                .getResultList();
    }

    public List<MovieEntity> searchOrderByTitleAsc(String parameter) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByTitleAsc")
                .setParameter("query", parameter+"%")
                .setParameter("query2", "% "+parameter+"%")
                .getResultList();
    }

    public List<MovieEntity> searchOrderByTitleDesc(String parameter) {
        return entityManager.createNamedQuery("MovieEntity.searchOrderByTitleDesc")
                .setParameter("query", parameter+"%")
                .setParameter("query2", "% "+parameter+"%")
                .getResultList();
    }
}
