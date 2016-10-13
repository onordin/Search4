package search4.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.SubscriptionDAOBean;
import search4.entities.DisplaySubscriptionEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.SubscriptionEntity;
import search4.entities.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class SubscriptionEJB {

	@EJB
	private SubscriptionDAOBean subsctiptionDAOBean;
	
	public List<DisplaySubscriptionEntity> getAllFor(Integer userId) {
		List<SubscriptionEntity> list = subsctiptionDAOBean.getAllFor(userId);
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
	
}
