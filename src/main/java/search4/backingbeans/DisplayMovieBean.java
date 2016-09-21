package search4.backingbeans;


import search4.ejb.interfaces.LocalDisplayMovie;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.UnregisteredProviderException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named(value="displayBean")
@SessionScoped //TODO should be ViewScoped?
//@ViewScoped
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
        try {
            displayMovieEntity = displayMovieEJB.getDisplayMovie(id);
        } catch (UnregisteredProviderException pe) {
            System.err.println("Provider Error: "+pe);
        } catch (DataNotFoundException de) {
            System.err.println("Data Error: "+de);
        }
        catch (Exception e) {
            System.err.println("Another Error: "+e);
        }
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
