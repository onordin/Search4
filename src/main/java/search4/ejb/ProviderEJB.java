package search4.ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import search4.daobeans.ProviderDOABean;
import search4.ejb.interfaces.LocalProvider;
import search4.entities.DisplayProviderEntity;
import search4.entities.InfoPayload;
import search4.entities.ProviderEntity;


@Stateless
public class ProviderEJB implements LocalProvider{
	
	@EJB
	private ProviderDOABean providerDOABean;

	
	public List<DisplayProviderEntity> getAllForUser(Integer userId) {
		
		List<ProviderEntity> listOfEntities = providerDOABean.getAllFor(userId);
		List<DisplayProviderEntity> listOfDisplayEntities = new ArrayList<DisplayProviderEntity>();
		System.out.println("list form DAOBean : " + listOfEntities);
		if(listOfEntities != null && !listOfEntities.isEmpty()) {
			for(ProviderEntity providerEntity : listOfEntities) {
				listOfDisplayEntities.add(dbEntityToDisplayEntity(providerEntity));
			}
		}
		return listOfDisplayEntities;
	}

	//converts ProviderEntity to DisplayProviderEntity
	private DisplayProviderEntity dbEntityToDisplayEntity(ProviderEntity providerEntity) {
		DisplayProviderEntity displayProviderEntity = new DisplayProviderEntity();
		displayProviderEntity.setId(providerEntity.getId());
		displayProviderEntity.setUserId(providerEntity.getUserId());
		displayProviderEntity.setProvider(providerEntity.getProvider());
		return displayProviderEntity;
	}
	
	public void updateForUser(Integer userId, List<String> providersToKeep) {

		List<DisplayProviderEntity> oldList = getAllForUser(userId);
		
		if(oldList.isEmpty()) {
			for(String provider : providersToKeep) {
				addProvider(provider, userId);
			}
		}else{
			removeUnwantedProviders(oldList, providersToKeep);
			addNewProviders(userId, oldList, providersToKeep);
		}		
	}
	
	private void addNewProviders(Integer userId, List<DisplayProviderEntity> oldList, List<String> providersToKeep) {
		for(String currentProviderToAdd : providersToKeep) {
			boolean alreadyAdded = false;
			for(DisplayProviderEntity currentProvider : oldList) {
				if(currentProvider.getProvider().equalsIgnoreCase(currentProviderToAdd)) {
					alreadyAdded = true;
				}
			}
			if(!alreadyAdded) {
				addProvider(currentProviderToAdd, userId);
			}
		}
	}

	private void removeUnwantedProviders(List<DisplayProviderEntity> oldList, List<String> providersToKeep) {
		for(DisplayProviderEntity currentEntity : oldList) {
			if(!providersToKeep.contains(currentEntity.getProvider())) {
				removeProviderById(currentEntity.getId());
			}
		}
	}


	public ProviderEntity addProvider(String provider, Integer userId) {
		ProviderEntity providerEntity = new ProviderEntity();
		providerEntity.setUserId(userId);
		providerEntity.setProvider(provider);
		if (!providerDOABean.addProvider(providerEntity).equals(null)) {
			return providerEntity;
		}else {
			return null;
		}
	}
		
	public InfoPayload removeProviderById(Integer id) {
		InfoPayload infoPayload = new InfoPayload();
		if (providerDOABean.providerExist(id)!=false) {
			if (providerDOABean.removeProvider(id) != false) {
				infoPayload.setUser_Message("Provider with id: " + id + " has been deleted");
				infoPayload.setResultOK(true);
			}else {
				infoPayload.setUser_Message("Provider with id: " + id + " has not been deleted");
				infoPayload.setResultOK(false);
			}		
		}else {
			infoPayload.setUser_Message("Provider with id: " + id + " do not exist");
			infoPayload.setResultOK(false);
		}
		return infoPayload;

	}
	
	public DisplayProviderEntity getProviderById(Integer providerId){
		return dbEntityToDisplayEntity(providerDOABean.getProvider(providerId));
	}
	
	public List<DisplayProviderEntity> getAllProviders(String search){
		List<ProviderEntity> providerEntities = providerDOABean.getAll(search);
		List<DisplayProviderEntity> displayProviderEntities = new ArrayList<DisplayProviderEntity>();
		if (providerEntities != null && !providerEntities.isEmpty()) {
			for (ProviderEntity providerEntity : providerEntities) {
				displayProviderEntities.add(dbEntityToDisplayEntity(providerEntity));
			}
		}
		return displayProviderEntities;
	}
}
