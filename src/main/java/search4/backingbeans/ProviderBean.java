package search4.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import search4.ejb.interfaces.LocalProvider;
import search4.entities.DisplayProviderEntity;

@Named(value="providerBean")
@SessionScoped
public class ProviderBean implements Serializable{

	
	private static final long serialVersionUID = 3275142775468170715L;
	private List<DisplayProviderEntity> displayProviderEntities;
	private List<String> providers;
	
	
	@EJB
	private LocalProvider providerEJB;
	

	public void postInit(Integer userId) {
		providers = new ArrayList<String>();
		updateProvidersFromDatabase(userId);
	}	
	
	public void updateProviders(Integer userId) {
		providerEJB.updateForUser(userId, providers);
		updateProvidersFromDatabase(userId);
	}

	
	public void updateProvidersFromDatabase(Integer userId) {
		displayProviderEntities = providerEJB.getAllForUser(userId);
		providers.clear();
		for(DisplayProviderEntity displayProviderEntity : displayProviderEntities) {
			providers.add(displayProviderEntity.getProvider());
		}
	}
	
	
	public List<DisplayProviderEntity> getDisplayProviderEntities() {
		return displayProviderEntities;
	}

	public void setDisplayProviderEntities(List<DisplayProviderEntity> displayProviderEntities) {
		this.displayProviderEntities = displayProviderEntities;
	}

	public List<String> getProviders() {
		return providers;
	}

	public void setProviders(List<String> providers) {
		this.providers = providers;
	}
	
	
	
	
	
	
}
