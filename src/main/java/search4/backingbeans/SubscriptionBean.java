package search4.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import search4.ejb.interfaces.LocalSubscription;
import search4.entities.DisplayMovieEntity;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.InfoPayload;
import search4.exceptions.DataNotFoundException;

@Named(value="subscriptionBean")
@SessionScoped
public class SubscriptionBean implements Serializable{

	private static final long serialVersionUID = -7246771514304788402L;
	private List<DisplaySubscriptionEntity> displaySubscriptionEntities;
    private String message;
	
	@EJB
	private LocalSubscription subscriptionEJB;

	private List<DisplaySubscriptionEntity> getSubscriptionEntities(Integer userId) {
		try {
            message = "";
			return subscriptionEJB.getAllFor(userId);
		} catch (DataNotFoundException dnfe) {
			message = "Data not found: " + dnfe.getMessage();
		} catch (Exception e) {
			message = "Unknown error: " + e;
		}
		return new ArrayList<DisplaySubscriptionEntity>();
	}

	public void postInit(Integer userId) {
        message = "";
		displaySubscriptionEntities = getSubscriptionEntities(userId);
    }
	
    public void subscribe(Integer id, Integer userId){
    	try {
            message = "";
    		subscriptionEJB.subscribeToMovie(id, userId);
		} catch (DataNotFoundException dnfe) {
			message = "Data not found: "+dnfe.getMessage();
		}
    	displaySubscriptionEntities = getSubscriptionEntities(userId);
    }

	public void removeSubscription(Integer id, Integer userId) {
        message = "";
        InfoPayload infoPayload = subscriptionEJB.removeSubscription(id);
        if (!infoPayload.isResultOK()) {
            message = "Something went wrong mith deleting subscription";
        }
		displaySubscriptionEntities = getSubscriptionEntities(userId);	//to update subscribebutton with displayBean.checkIfUserSubscribes()
	}

	public List<DisplaySubscriptionEntity> getDisplaySubscriptionEntities() {
		return displaySubscriptionEntities;
	}

	public void setDisplaySubscriptionEntities(List<DisplaySubscriptionEntity> displaySubscriptionEntities) {
		this.displaySubscriptionEntities = displaySubscriptionEntities;
	}
}
