package search4.daobeans;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class SearchDatabaseDAOBean {

    @PersistenceContext
    private EntityManager entityManager;

	public String findTMDbIdFromTitle() {
		entityManager.createNamedQuery();
		return null;
	}
    
    
    
    
}
