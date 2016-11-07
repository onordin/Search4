package search4.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import search4.entities.DisplayProviderEntity;
import search4.entities.InfoPayload;
import search4.entities.ProviderEntity;

@Local
public interface LocalProvider {
	
	public void updateForUser(Integer id, List<String> providers);
	public List<DisplayProviderEntity> getAllForUser(Integer id);

	public ProviderEntity addProvider(String provider, Integer userId);
	public InfoPayload removeProviderById(Integer id);

	public DisplayProviderEntity getProviderById(Integer providerId);
	public List<DisplayProviderEntity> getAllProviders(String search);
}
