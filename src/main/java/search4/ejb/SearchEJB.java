package search4.ejb;

import search4.daobeans.SearchDAOBean;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.ejb.interfaces.LocalSearch;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;


@Stateless
public class SearchEJB implements LocalSearch{

    @EJB
    private SearchDAOBean searchDAOBean;
    
    public List<MovieEntity> searchOrderByDateAsc(String parameter, Integer limit) {
        return searchDAOBean.searchOrderByDateAsc(parameter, limit);
    }

    
    public List<MovieEntity> searchOrderByDateDesc(String parameter, Integer limit) {
        return searchDAOBean.searchOrderByDateDesc(parameter, limit);
    }

    
    public List<MovieEntity> searchOrderByTitleAsc(String parameter, Integer limit) {
        return searchDAOBean.searchOrderByTitleAsc(parameter, limit);
    }

   
    public List<MovieEntity> searchOrderByTitleDesc(String parameter, Integer limit) {
        return searchDAOBean.searchOrderByTitleDesc(parameter, limit);
    }


}
