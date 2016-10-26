package search4.backingbeans;


import search4.ejb.SubscriptionEJB;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.ejb.interfaces.LocalSubscription;
import search4.entities.DisplayMovieEntity;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.MovieEntity;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.UnregisteredProviderException;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Named(value="displayBean")
//@SessionScoped //TODO should be ViewScoped?
@ManagedBean(name="displayBean")
@ViewScoped
public class DisplayMovieBean implements Serializable{

	private static final long serialVersionUID = -1109287815566247040L;

	@EJB
    private LocalDisplayMovie displayMovieEJB;
	
	@EJB
	private LocalSubscription subscriptionEJB;

	
    private DisplayMovieEntity displayMovieEntity;
    private Integer movieId;
    private String message;
    private boolean userSubscribesToMovie;
    private Integer subscriptionId;
    private List<String> matchingProviders;
    

    public void postInit() {
        getMovieData(movieId);
        matchingProviders = new ArrayList<>();
    }

    
	public void checkIfUserSubscribes(List<DisplaySubscriptionEntity> displaySubscriptionEntities) {
		userSubscribesToMovie = false;
		
		if(displaySubscriptionEntities != null) {
			for(DisplaySubscriptionEntity displaySubscriptionEntity : displaySubscriptionEntities) {
				if(movieId.equals(displaySubscriptionEntity.getSubscribedMovieId())) {
					userSubscribesToMovie = true;
					subscriptionId = displaySubscriptionEntity.getId();
					break;
				}
			}
		}
    }
	
	
	public void checkForMatchingSubscriptions(List<String> requestedProviders) {
		matchingProviders = displayMovieEJB.getMatchingProviders(requestedProviders, displayMovieEntity);
	}
	
    
    public void getMovieData(Integer id) {
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
    
/*
    public String subscribe(Integer userId){
    	System.out.println("inside subscribe");
    	try {
    		subscriptionEJB.subscribeToMovie(movieId, userId);
    		return "full_profile";
		} catch (Exception e) {
			message = "Error" + e;
		}
    	return "";
    }
*/
    
    public String getMessage() {
        return message;
    }

	public void setMessage(String message) {
		this.message = message;
	}

	public DisplayMovieEntity getDisplayMovieEntity() {
        return displayMovieEntity;
    }
	
	public void setDisplayMovieEntity(DisplayMovieEntity displayMovieEntity) {
		this.displayMovieEntity = displayMovieEntity;
	}

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
    }

	public boolean isUserSubscribesToMovie() {
		return userSubscribesToMovie;
	}

	public void setUserSubscribesToMovie(boolean userSubscribesToMovie) {
		this.userSubscribesToMovie = userSubscribesToMovie;
	}


	public Integer getSubscriptionId() {
		return subscriptionId;
	}


	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;
	}


	public List<String> getMatchingProviders() {
		return matchingProviders;
	}


	public void setMatchingProviders(List<String> matchingProviders) {
		this.matchingProviders = matchingProviders;
	}
    
    
    
}
