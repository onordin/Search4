package search4.ejb;

import search4.daobeans.SearchDAOBean;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

//TODO interface?
@Stateless
public class SearchEJB {

    @EJB
    private SearchDAOBean searchDAOBean;

    public List<MovieEntity> search(String parameter) {
        return searchDAOBean.search(parameter);
    }
    public List<MovieEntity> searchOrderByDate(String parameter) {
        return searchDAOBean.searchOrderByDate(parameter);
    }
}
