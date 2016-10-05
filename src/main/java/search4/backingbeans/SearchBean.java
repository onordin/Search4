package search4.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import search4.ejb.interfaces.LocalSearch;
import search4.entities.MovieEntity;

@Named(value="searchBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class SearchBean implements Serializable {

	private static final long serialVersionUID = 8335492042247599634L;
	
	private String query;
    private List<MovieEntity> movieEntities;

    @EJB
    private LocalSearch searchEJB;

    //TODO choose order by from frontend?
    public String search() {
        boolean titleSort = true;
        boolean ascending = true;
        if (titleSort && ascending) {
            movieEntities = searchEJB.searchOrderByTitleAsc(query);
        }
        else if (titleSort && !ascending) {
            movieEntities = searchEJB.searchOrderByTitleDesc(query);
        }
        else if (!titleSort && ascending) {
            movieEntities = searchEJB.searchOrderByDateAsc(query);
        }
        else {
            movieEntities = searchEJB.searchOrderByDateDesc(query);
        }
        return "full_startpage";
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<MovieEntity> getMovieEntities() {
        return movieEntities;
    }

    public void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }
    
    
    public String ajaxListener(AjaxBehaviorEvent event) {
    	return search();
    }
    
}
