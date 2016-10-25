package search4.daobeans;

import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import search4.entities.ProviderEntity;

@Stateful
public class ProviderDOABean {

	@PersistenceContext
	private EntityManager entityManager;

	
	public boolean addProvider(ProviderEntity providerEntity) {
		try {
			entityManager.merge(providerEntity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public boolean removeProvider(Integer id) {
		try{
			ProviderEntity providerEntity = (ProviderEntity) entityManager.createNamedQuery("ProviderEntity.getOne")
			.setParameter("id", id)
			.getSingleResult();
			entityManager.remove(providerEntity);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;			
		}
	
	}	
	
	
	public List<ProviderEntity> getAllFor(Integer userId) {
		return entityManager.createNamedQuery("ProviderEntity.getAllFor")
                .setParameter("userId", userId)
                .getResultList();
	}
	
	
	
}
