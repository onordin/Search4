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
        return entityManager.createNamedQuery("MovieEntity.search").setParameter("query", parameter+"%").setParameter("query", "% "+parameter+"%").getResultList(); //TODO solve NamedQueries in MovieEntity when internet
    }

    //TODO for this to actually work we need it to be only one query; otherwise we get 2 sorted lists
    public List<MovieEntity> searchOrderByDate(String parameter) {
        return entityManager.createNamedQuery("MovieEntity.searchSortedByDate").setParameter("query", parameter+"%").setParameter("query2", "% "+parameter+"%").getResultList();
    }
}
