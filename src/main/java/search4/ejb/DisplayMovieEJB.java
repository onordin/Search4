package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.ejb.Stateless;

//TODO interface?
@Stateless
public class DisplayMovieEJB {

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    public MovieEntity getMovieData(Integer id) {
        return displayMovieDAOBean.getMovieData(id); //TODO anything we want to handle here?
    }
}
