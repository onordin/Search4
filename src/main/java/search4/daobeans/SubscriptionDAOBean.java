package search4.daobeans;

import search4.entities.SubscriptionEntity;
import search4.entities.UserEntity;
import search4.exceptions.DataNotFoundException;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

@Stateful
public class SubscriptionDAOBean implements Serializable{

	
	@PersistenceContext
	private EntityManager entityManager;

	public SubscriptionEntity getSubscription(Integer id) throws DataNotFoundException {
		try {
			return (SubscriptionEntity) entityManager.createNamedQuery("SubscriptionEntity.getWithId")
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException nre) {
			throw new DataNotFoundException("No subscription in database with that ID ("+id+")");
		}
	}

	public List<SubscriptionEntity> getAll() {
        //Only possible exception should be if database does not exist, should throw a InternalServerError TODO look into this?
		return entityManager.createNamedQuery("SubscriptionEntity.findAll").getResultList();
	}
	public List<SubscriptionEntity> getAllFor(Integer userId) throws DataNotFoundException {
        try {
		    return entityManager.createNamedQuery("SubscriptionEntity.findAllFor")
                .setParameter("userId", userId)
                .getResultList();
        } catch (NoResultException nre) {
			throw new DataNotFoundException("No user in database with that ID ("+userId+")");
		}
	}

	public List<SubscriptionEntity> getUsersSubscribedTo(Integer movieId) throws NoResultException {
        try {
		    return entityManager.createNamedQuery("SubscriptionEntity.findAllSubscribedTo")
				.setParameter("movieId", movieId)
				.getResultList();
        } catch (NoResultException nre) {
            throw new DataNotFoundException("No movie in database with that ID ("+movieId+")");
        }
    }

	public boolean subscribeToMovie(SubscriptionEntity subscriptionEntity) {
        //database or table does not exist, wrong format on subscription entity?
		try {
			entityManager.merge(subscriptionEntity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean removeSubscription(Integer id) {
		try {
			SubscriptionEntity entity = (SubscriptionEntity) entityManager.createNamedQuery("SubscriptionEntity.getOneSubscription")
			.setParameter("id", id)
			.getSingleResult();
			System.out.println(entity.toString());
			entityManager.remove(entity);
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
}
