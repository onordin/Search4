package search4.backingbeans;

import java.io.Serializable;
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
	
	@EJB
	private LocalProvider providerEJB;
	

	public void postInit(Integer userId) {
		displayProviderEntities = providerEJB.getAllForUser(userId);
		System.out.println("inside postinit");
	}	
	
	public void updateProviders(Integer userId, List<String> providersToKeep) {
		providerEJB.updateForUser(userId, providersToKeep);
	}

	public List<DisplayProviderEntity> getDisplayProviderEntities() {
		return displayProviderEntities;
	}

	public void setDisplayProviderEntities(List<DisplayProviderEntity> displayProviderEntities) {
		this.displayProviderEntities = displayProviderEntities;
	}
	
	
	
	
	
	
}
