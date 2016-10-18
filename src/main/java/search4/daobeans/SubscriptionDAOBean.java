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


	/*
	public boolean removeSubscription(Integer id) {
		try{
			entityManager.createNamedQuery("SubscriptionEntity.removeSubscription")
			.setParameter("id", id);
			System.out.println("deleted??");
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	*/
	
	
	public boolean removeSubscription(Integer id) {
		
		try{
			SubscriptionEntity entity = (SubscriptionEntity) entityManager.createNamedQuery("SubscriptionEntity.getOneSubscription")
			.setParameter("id", id)
			.getSingleResult();
			System.out.println(entity.toString());
			entityManager.remove(entity);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;			
		}
	
	}
	
	
	
}
