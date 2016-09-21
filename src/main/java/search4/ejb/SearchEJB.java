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

    @Override
    public List<MovieEntity> search(String parameter) {
        return searchDAOBean.search(parameter);
    }

    @Override
    public List<MovieEntity> searchOrderByDateAsc(String parameter) {
        return searchDAOBean.searchOrderByDateAsc(parameter);
    }

    @Override
    public List<MovieEntity> searchOrderByDateDesc(String parameter) {
        return searchDAOBean.searchOrderByDateDesc(parameter);
    }

    @Override
    public List<MovieEntity> searchOrderByTitleAsc(String parameter) {
        return searchDAOBean.searchOrderByTitleAsc(parameter);
    }

    @Override
    public List<MovieEntity> searchOrderByTitleDesc(String parameter) {
        return searchDAOBean.searchOrderByTitleDesc(parameter);
    }


}
