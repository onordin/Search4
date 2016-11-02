package search4.soap;

import java.util.List;

import javax.ejb.EJB;
import javax.jws.WebService;

import search4.ejb.interfaces.LocalProvider;
import search4.entities.DisplayProviderEntity;
import search4.entities.InfoPayload;

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
		if (providerEJB.addProvider(provider, userId) != null) {
			return "Provider added";
		}else {
			return "failed to add provider";
		}
	}
	
	public String removeProvider(Integer id) {
		InfoPayload infoPayload = providerEJB.removeProviderById(id);
		if (infoPayload.isResultOK() != false) {
			return "Provider removed";
		}else{
			return "failed to remove provider";
		}
	}
}