package search4.soap;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import search4.ejb.interfaces.LocalProvider;
import search4.entities.DisplayProviderEntity;
import search4.entities.ProviderEntity;
@WebService(serviceName="providerSoap")
public class ProviderSoap {
	
	
	@EJB
	private LocalProvider providerEJB;
	
	
	public List<DisplayProviderEntity> getProviderByUserId(Integer userId) throws Exception{
		return providerEJB.getAllForUser(userId);
	}
	
	public DisplayProviderEntity getProviderByProviderId(Integer provderId) throws Exception{
		return providerEJB.getProviderById(provderId);
	}
	
	public List<DisplayProviderEntity> getAllProviders(){
		return providerEJB.getAllProviders("");
	}
	
	public String addProvider(String provider, Integer userId) {
		if (providerEJB.addProvider(provider, userId)) {
			return "Provider added.";
		}else {
			return "Failed to add provider.";
		}
	}
	
	public String removeProvider(Integer id) {
		if (providerEJB.removeProvider(id) == true) {
			return "Provider removed.";
		}
		return "Failed to remove provider.";
	}
	
}