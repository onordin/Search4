package search4.backingbeans;

import search4.ejb.DisplayMovieEJB;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value="displayBean")
@SessionScoped //TODO should be ViewScoped?
public class DisplayMovieBean implements Serializable{

    @EJB
    DisplayMovieEJB displayMovieEJB;

    private DisplayMovieEntity displayMovieEntity;
    private Integer movieId;

    public void postInit() {
        getMovieData(movieId);
    }

    public void getMovieData(Integer id) {
        displayMovieEntity = displayMovieEJB.getDisplayMovie(id);
    }

    public DisplayMovieEntity getDisplayMovieEntity() {
        return displayMovieEntity;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }
}
