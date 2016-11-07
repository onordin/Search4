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

@Named(value="subscriptionBean")
@SessionScoped
public class SubscriptionBean implements Serializable{

	private static final long serialVersionUID = -7246771514304788402L;
	private List<DisplaySubscriptionEntity> displaySubscriptionEntities;
	
	@EJB
	private LocalSubscription subscriptionEJB;

	private List<DisplaySubscriptionEntity> getSubscriptionEntities(Integer userId) {
		try {
			return subscriptionEJB.getAllFor(userId);
		} catch (Exception e) {
			System.err.println("Error "+e);
			//TODO take proper look at entire chain here
		}
		return new ArrayList<DisplaySubscriptionEntity>();
	}

	public void postInit(Integer userId) {
		displaySubscriptionEntities = getSubscriptionEntities(userId);
    }
	
    public void subscribe(Integer id, Integer userId){
    	try {
    		subscriptionEJB.subscribeToMovie(id, userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	displaySubscriptionEntities = getSubscriptionEntities(userId);	//TODO what does this mean -> to update subscribebutton with displayBean.checkIfUserSubscribes()
    }

	public void removeSubscription(Integer id, Integer userId) {
		subscriptionEJB.removeSubscription(id);
		displaySubscriptionEntities = getSubscriptionEntities(userId);	//to update subscribebutton with displayBean.checkIfUserSubscribes()
	}

	public List<DisplaySubscriptionEntity> getDisplaySubscriptionEntities() {
		return displaySubscriptionEntities;
	}

	public void setDisplaySubscriptionEntities(List<DisplaySubscriptionEntity> displaySubscriptionEntities) {
		this.displaySubscriptionEntities = displaySubscriptionEntities;
	}
}
