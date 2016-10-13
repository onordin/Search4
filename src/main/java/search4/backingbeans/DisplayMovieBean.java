package search4.backingbeans;


import search4.ejb.SubscriptionEJB;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.UnregisteredProviderException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

//@Named(value="displayBean")
//@SessionScoped //TODO should be ViewScoped?
@ManagedBean(name="displayBean")
@ViewScoped
public class DisplayMovieBean implements Serializable{

	private static final long serialVersionUID = -1109287815566247040L;

	@EJB
    LocalDisplayMovie displayMovieEJB;
	
	@EJB
	private SubscriptionEJB subscriptionEJB;

    private DisplayMovieEntity displayMovieEntity;
    private Integer movieId;
    private String message;

    public void postInit() {
        getMovieData(movieId);
    }

    public void getMovieData(Integer id) {
        //TODO use getMovie
        try {
            displayMovieEntity = displayMovieEJB.getDisplayMovie(id);
            message = "";
        } catch (UnregisteredProviderException pe) {
            displayMovieEntity = null;
            message = "Error: "+pe;
        } catch (DataNotFoundException de) {
            displayMovieEntity = null;
            message = "400 Bad Request: No such movie!";
        }
        catch (Exception e) {
            displayMovieEntity = null;
            message = "Error"+e;
        }
    }

    public DisplayMovieEntity getMovie(Integer id) {
        try {
            message = "";
            return displayMovieEJB.getDisplayMovie(id);
        } catch (UnregisteredProviderException pe) {
            displayMovieEntity = null;
            message = "Error: "+pe;
        } catch (DataNotFoundException de) {
            displayMovieEntity = null;
            message = "400 Bad Request: No such movie!";
        }
        catch (Exception e) {
            displayMovieEntity = null;
            message = "Error"+e;
        }
        return null;
    }
    
    public String subscribe(Integer userId){
        try {
//            subscriptionEJB.subscribeToMovie(movieId, userId);
            System.out.println("BOB");
        } catch (Exception e) {
            message = "Error" + e;
        }
        return "bob";
    }

    public String getMessage() {
        return message;
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
