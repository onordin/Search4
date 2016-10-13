package search4.backingbeans;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import search4.ejb.interfaces.LocalSubscription;
import search4.entities.DisplaySubscriptionEntity;

@Named(value="subscriptionBean")
@SessionScoped
public class SubscriptionBean implements Serializable{

	
	private static final long serialVersionUID = -7246771514304788402L;
	private List<DisplaySubscriptionEntity> displaySubscriptionEntities;
	
	@EJB
	private LocalSubscription subscriptionEJB;
	
	
	public void postInit(Integer userId) {
		displaySubscriptionEntities = subscriptionEJB.getAllFor(userId);
    }
	
	

	public List<DisplaySubscriptionEntity> getDisplaySubscriptionEntities() {
		return displaySubscriptionEntities;
	}



	public void setDisplaySubscriptionEntities(List<DisplaySubscriptionEntity> displaySubscriptionEntities) {
		this.displaySubscriptionEntities = displaySubscriptionEntities;
	}


	
	
}
