package search4.backingbeans;

import search4.ejb.SearchEJB;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value="searchBean") //if we want to use another name to call this from the JSF file
@SessionScoped
public class SearchBean implements Serializable{

    private String query;
    private List<MovieEntity> movieEntities;

    @EJB
    private SearchEJB searchEJB;

    public String search() { //TODO parameter?
        //MovieEntity movieEntity = searchEJB.search(query).get(0); //TODO actually handle properly. Send parameter from frontend how?
        //System.out.println("DA MOVVE ESSS "+movieEntity);
        movieEntities = searchEJB.search(query);
        return "result";
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