package search4.backingbeans;

import search4.ejb.interfaces.LocalSearch;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value="searchBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class SearchBean implements Serializable{

	private static final long serialVersionUID = 8335492042247599634L;
	
	private String query;
    private List<MovieEntity> movieEntities;

    @EJB
    private LocalSearch searchEJB;

    //TODO choose order by from frontend?
    public String search() {
        movieEntities = searchEJB.searchOrderByDateDesc(query);
        return "search";
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
}
