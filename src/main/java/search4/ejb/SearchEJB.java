package search4.ejb;

import search4.daobeans.SearchDAOBean;
import search4.ejb.interfaces.LocalSearch;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;


@Stateless
public class SearchEJB{

    @EJB
    private SearchDAOBean searchDAOBean;

   
    public List<MovieEntity> search(String parameter) {
        return searchDAOBean.search(parameter);
    }

    
    public List<MovieEntity> searchOrderByDateAsc(String parameter) {
        return searchDAOBean.searchOrderByDateAsc(parameter);
    }

    
    public List<MovieEntity> searchOrderByDateDesc(String parameter) {
        return searchDAOBean.searchOrderByDateDesc(parameter);
    }

    
    public List<MovieEntity> searchOrderByTitleAsc(String parameter) {
        return searchDAOBean.searchOrderByTitleAsc(parameter);
    }

   
    public List<MovieEntity> searchOrderByTitleDesc(String parameter) {
        return searchDAOBean.searchOrderByTitleDesc(parameter);
    }


}
