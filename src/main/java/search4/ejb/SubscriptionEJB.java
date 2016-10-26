package search4.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.DisplayMovieDAOBean;
import search4.daobeans.SubscriptionDAOBean;
import search4.ejb.interfaces.LocalSubscription;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.MovieEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.SubscriptionEntity;
import search4.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class SubscriptionEJB implements LocalSubscription{

	@EJB
	private SubscriptionDAOBean subscriptionDAOBean;

	@EJB
	private DisplayMovieDAOBean displayMovieDAOBean;

	
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
		displayEntity.setId(se.getId());
		try {
			MovieEntity movieEntity = displayMovieDAOBean.getMovieData(se.getMovieId());
			displayEntity.setTitle(movieEntity.getTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return displayEntity;
	}


	public void subscribeToMovie(Integer movieId, Integer userId) {
		SubscriptionEntity subscriptionEntity = new SubscriptionEntity();
		subscriptionEntity.setMovieId(movieId);
		subscriptionEntity.setUserId(userId);
		subscriptionDAOBean.subscribeToMovie(subscriptionEntity);
	}




	public boolean removeSubscription(Integer id) {
		return subscriptionDAOBean.removeSubscription(id);
	}





}
