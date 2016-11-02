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

	public ProviderEntity addProvider(ProviderEntity providerEntity) {
		try {
			return entityManager.merge(providerEntity);
		} catch (Exception e) {
			return null;
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
	
	public ProviderEntity getProvider(Integer providerId) {
		return (ProviderEntity)entityManager.createNamedQuery("ProviderEntity.getOne")
				.setParameter("id", providerId)
				.getSingleResult();
	}
	
	public List<ProviderEntity> getAll(String input){
		return entityManager.createNamedQuery("ProviderEntity.search")
				.setParameter("input", input+"%")
				.setParameter("inputWithSpace","% "+input+"%")
				.getResultList();
	}
	
	public boolean providerExist(Integer providerId) {
		try {
			ProviderEntity providerEntity = (ProviderEntity) entityManager.createNamedQuery("ProviderEntity.getOne")
					.setParameter("id", providerId)
					.getSingleResult();
			return true;
		} catch (Exception e) {
			System.err.println("PROVIDEREXISTS ERROR: " + e);
			return false;
		}
	}
}
