package search4.daobeans;

import search4.entities.SubscriptionEntity;
import search4.entities.UserEntity;

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

	public List<SubscriptionEntity> getUsersSubscribedTo(Integer movieId) {
		return entityManager.createNamedQuery("SubscriptionEntity.findAllSubscribedTo")
				.setParameter("movieId", movieId)
				.getResultList();
	}
}
