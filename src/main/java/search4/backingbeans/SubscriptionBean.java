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
	public List<DisplaySubscriptionEntity> displaySubscriptionEntities;
	
	@EJB
	private LocalSubscription subscriptionEJB;
	
	
	public void postInit(Integer userId) {
		System.out.println("setting up postInit for user id: " + userId);
		displaySubscriptionEntities = subscriptionEJB.getAllFor(userId);
		System.out.println("List size: " + displaySubscriptionEntities.size());
    }
	
	

	public List<DisplaySubscriptionEntity> getSubscriptionEntities() {
		return displaySubscriptionEntities;
	}
	
	public void setSubscriptionEntities(List<DisplaySubscriptionEntity> displaySubscriptionEntities) {
		this.displaySubscriptionEntities = displaySubscriptionEntities;
	}
	
	
	
	
	
}
