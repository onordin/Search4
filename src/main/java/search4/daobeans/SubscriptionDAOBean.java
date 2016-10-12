package search4.daobeans;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class SubscriptionDAOBean {

	
	@PersistenceContext
	private EntityManager entityManager;
	
	
}
