package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.SubscriptionDAOBean;
import search4.ejb.interfaces.LocalSubscription;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.SubscriptionEntity;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class SubscriptionEJB implements LocalSubscription{

	@EJB
	private SubscriptionDAOBean subscriptionDAOBean;
	
	public List<DisplaySubscriptionEntity> getAllFor(Integer userId) {
		List<SubscriptionEntity> list = subscriptionDAOBean.getAllFor(userId);
		List<DisplaySubscriptionEntity> displayEntities = new ArrayList<DisplaySubscriptionEntity>();
		for (SubscriptionEntity se : list) {
			displayEntities.add(dbEntityToDisplayEntity(se));
		}
		return displayEntities;
	}

	private DisplaySubscriptionEntity dbEntityToDisplayEntity(SubscriptionEntity se) {
		DisplaySubscriptionEntity displayEntity = new DisplaySubscriptionEntity();
		displayEntity.setSubscribedMovieId(se.getMovieId());
		return displayEntity;
	}

	
	public void subscribeToMovie(Integer movieId, Integer userId) {
		SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
		subscriptionEntity.setMovieId(movieId);
		subscriptionEntity.setUserId(userId);
		subscriptionDAOBean.subscribeToMovie(subscriptionEntity);
	}
	
	
	
}
