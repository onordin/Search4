package search4.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import search4.entities.DisplayProviderEntity;

@Local
public interface LocalProvider {
	
	public void updateForUser(Integer id, List<String> providers);
	public List<DisplayProviderEntity> getAllForUser(Integer id);
	public boolean addProvider(String provider, Integer userId);
	public boolean removeProvider(Integer id);
	public DisplayProviderEntity getProviderById(Integer providerId);
	public List<DisplayProviderEntity> getAllProviders(String search);
}
