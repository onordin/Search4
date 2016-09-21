package search4.backingbeans;


import search4.ejb.interfaces.LocalDisplayMovie;
import search4.entities.DisplayMovieEntity;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value="displayBean")
@SessionScoped //TODO should be ViewScoped?
public class DisplayMovieBean implements Serializable{

	private static final long serialVersionUID = -1109287815566247040L;

	@EJB
    LocalDisplayMovie displayMovieEJB;

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
