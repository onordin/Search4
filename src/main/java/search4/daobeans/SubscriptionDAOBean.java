package search4.daobeans;

import search4.entities.SubscriptionEntity;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful
public class SubscriptionDAOBean {

	
	@PersistenceContext
	private EntityManager entityManager;
	

	public List<SubscriptionEntity> getAllFor(Integer userId) {
		return entityManager.createNamedQuery("SubscriptionEntity.findAllFor")
                .setParameter("userId", userId)
                .getResultList();
	}


	public boolean subscribeToMovie(SubscriptionEntity subscriptionEntity) {
		try {
			entityManager.merge(subscriptionEntity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	
}
