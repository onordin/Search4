package search4.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.swing.plaf.synth.SynthScrollBarUI;

public class DisplayMovieEntity {

//	private static final long serialVersionUID = -8197962163887331570L;

	private String title;
    private String description;
    private Date date;
    private String posterUrl;

    private List<ServiceProviderLink> providerListWebSubscription;
    private List<ServiceProviderLink> providerListWebPurchase;
    private List<ServiceProviderLink> providerListWebFree;
    private List<ServiceProviderLink> providerListWebTvEverywhere;

    private List<ServiceProviderLink> providerListAndroidSubscription;
    private List<ServiceProviderLink> providerListAndroidPurchase;
    private List<ServiceProviderLink> providerListAndroidFree;
    private List<ServiceProviderLink> providerListAndroidTvEverywhere;

    private List<ServiceProviderLink> providerListIOSSubscription;
    private List<ServiceProviderLink> providerListIOSPurchase;
    private List<ServiceProviderLink> providerListIOSFree;
    private List<ServiceProviderLink> providerListIOSTvEverywhere;

    private List<ServiceProviderLink> providerListOther;
    
    private List<String> currentProviders;


	private boolean hasWeb;
    private boolean hasWebSubscription;
    private boolean hasWebPurchase;
    private boolean hasWebFree;
    private boolean hasWebTvEverywhere;
    
    private boolean hasAndroid;
    private boolean hasAndroidSubscription;
    private boolean hasAndroidPurchase;
    private boolean hasAndroidFree;
    private boolean hasAndroidTvEverywhere;
    
    private boolean hasIOS;
    private boolean hasIOSSubscription;
    private boolean hasIOSPurchase;
    private boolean hasIOSFree;
    private boolean hasIOSTvEverywhere;
    
    private boolean hasOther;
    
    private boolean hasNone;
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<ServiceProviderLink> getProviderListWebSubscription() {
        return providerListWebSubscription;
    }

    public void setProviderListWebSubscription(List<ServiceProviderLink> providerListWebSubscription) {
        this.providerListWebSubscription = providerListWebSubscription;
    }

    public List<ServiceProviderLink> getProviderListWebPurchase() {
        return providerListWebPurchase;
    }

    public void setProviderListWebPurchase(List<ServiceProviderLink> providerListWebPurchase) {
        this.providerListWebPurchase = providerListWebPurchase;
    }

    public List<ServiceProviderLink> getProviderListWebFree() {
        return providerListWebFree;
    }

    public void setProviderListWebFree(List<ServiceProviderLink> providerListWebFree) {
        this.providerListWebFree = providerListWebFree;
    }

    public List<ServiceProviderLink> getProviderListAndroidSubscription() {
        return providerListAndroidSubscription;
    }

    public void setProviderListAndroidSubscription(List<ServiceProviderLink> providerListAndroidSubscription) {
        this.providerListAndroidSubscription = providerListAndroidSubscription;
    }

    public List<ServiceProviderLink> getProviderListAndroidPurchase() {
        return providerListAndroidPurchase;
    }

    public void setProviderListAndroidPurchase(List<ServiceProviderLink> providerListAndroidPurchase) {
        this.providerListAndroidPurchase = providerListAndroidPurchase;
    }

    public List<ServiceProviderLink> getProviderListAndroidFree() {
        return providerListAndroidFree;
    }

    public void setProviderListAndroidFree(List<ServiceProviderLink> providerListAndroidFree) {
        this.providerListAndroidFree = providerListAndroidFree;
    }

    public List<ServiceProviderLink> getProviderListIOSSubscription() {
        return providerListIOSSubscription;
    }

    public void setProviderListIOSSubscription(List<ServiceProviderLink> providerListIOSSubscription) {
        this.providerListIOSSubscription = providerListIOSSubscription;
    }

    public List<ServiceProviderLink> getProviderListIOSPurchase() {
        return providerListIOSPurchase;
    }

    public void setProviderListIOSPurchase(List<ServiceProviderLink> providerListIOSPurchase) {
        this.providerListIOSPurchase = providerListIOSPurchase;
    }

    public List<ServiceProviderLink> getProviderListIOSFree() {
        return providerListIOSFree;
    }

    public void setProviderListIOSFree(List<ServiceProviderLink> providerListIOSFree) {
        this.providerListIOSFree = providerListIOSFree;
    }

    public List<ServiceProviderLink> getProviderListWebTvEverywhere() {
        return providerListWebTvEverywhere;
    }

    public void setProviderListWebTvEverywhere(List<ServiceProviderLink> providerListWebTvEverywhere) {
        this.providerListWebTvEverywhere = providerListWebTvEverywhere;
    }

    public List<ServiceProviderLink> getProviderListAndroidTvEverywhere() {
        return providerListAndroidTvEverywhere;
    }

    public void setProviderListAndroidTvEverywhere(List<ServiceProviderLink> providerListAndroidTvEverywhere) {
        this.providerListAndroidTvEverywhere = providerListAndroidTvEverywhere;
    }

    public List<ServiceProviderLink> getProviderListIOSTvEverywhere() {
        return providerListIOSTvEverywhere;
    }

    public void setProviderListIOSTvEverywhere(List<ServiceProviderLink> providerListIOSTvEverywhere) {
        this.providerListIOSTvEverywhere = providerListIOSTvEverywhere;
    }

    public List<ServiceProviderLink> getProviderListOther() {
        return providerListOther;
    }

    public void setProviderListOther(List<ServiceProviderLink> providerListOther) {
        this.providerListOther = providerListOther;
    }
    
    
    
    
    
    public boolean isHasWeb() {
		return hasWeb;
	}

	public void setHasWeb(boolean hasWeb) {
		this.hasWeb = hasWeb;
	}

	public boolean isHasWebSubscription() {
		return hasWebSubscription;
	}

	public void setHasWebSubscription(boolean hasWebSubscription) {
		this.hasWebSubscription = hasWebSubscription;
	}

	public boolean isHasWebPurchase() {
		return hasWebPurchase;
	}

	public void setHasWebPurchase(boolean hasWebPurchase) {
		this.hasWebPurchase = hasWebPurchase;
	}

	public boolean isHasWebFree() {
		return hasWebFree;
	}

	public void setHasWebFree(boolean hasWebFree) {
		this.hasWebFree = hasWebFree;
	}

	public boolean isHasWebTvEverywhere() {
		return hasWebTvEverywhere;
	}

	public void setHasWebTvEverywhere(boolean hasWebTvEverywhere) {
		this.hasWebTvEverywhere = hasWebTvEverywhere;
	}

	public boolean isHasAndroid() {
		return hasAndroid;
	}

	public void setHasAndroid(boolean hasAndroid) {
		this.hasAndroid = hasAndroid;
	}

	public boolean isHasAndroidSubscription() {
		return hasAndroidSubscription;
	}

	public void setHasAndroidSubscription(boolean hasAndroidSubscription) {
		this.hasAndroidSubscription = hasAndroidSubscription;
	}

	public boolean isHasAndroidPurchase() {
		return hasAndroidPurchase;
	}

	public void setHasAndroidPurchase(boolean hasAndroidPurchase) {
		this.hasAndroidPurchase = hasAndroidPurchase;
	}

	public boolean isHasAndroidFree() {
		return hasAndroidFree;
	}

	public void setHasAndroidFree(boolean hasAndroidFree) {
		this.hasAndroidFree = hasAndroidFree;
	}

	public boolean isHasAndroidTvEverywhere() {
		return hasAndroidTvEverywhere;
	}

	public void setHasAndroidTvEverywhere(boolean hasAndroidTvEverywhere) {
		this.hasAndroidTvEverywhere = hasAndroidTvEverywhere;
	}

	public boolean isHasIOS() {
		return hasIOS;
	}

	public void setHasIOS(boolean hasIOS) {
		this.hasIOS = hasIOS;
	}

	public boolean isHasIOSSubscription() {
		return hasIOSSubscription;
	}

	public void setHasIOSSubscription(boolean hasIOSSubscription) {
		this.hasIOSSubscription = hasIOSSubscription;
	}

	public boolean isHasIOSPurchase() {
		return hasIOSPurchase;
	}

	public void setHasIOSPurchase(boolean hasIOSPurchase) {
		this.hasIOSPurchase = hasIOSPurchase;
	}

	public boolean isHasIOSFree() {
		return hasIOSFree;
	}

	public void setHasIOSFree(boolean hasIOSFree) {
		this.hasIOSFree = hasIOSFree;
	}

	public boolean isHasIOSTvEverywhere() {
		return hasIOSTvEverywhere;
	}

	public void setHasIOSTvEverywhere(boolean hasIOSTvEverywhere) {
		this.hasIOSTvEverywhere = hasIOSTvEverywhere;
	}

	public boolean isHasOther() {
		return hasOther;
	}

	public void setHasOther(boolean hasOther) {
		this.hasOther = hasOther;
	}

	public boolean isHasNone() {
		return hasNone;
	}

	public void setHasNone(boolean hasNone) {
		this.hasNone = hasNone;
	}

	
    public List<String> getCurrentProviders() {
		return currentProviders;
	}

	public void setCurrentProviders(List<String> currentProviders) {
		this.currentProviders = currentProviders;
	}

	
	public void checkAddedServices () {
    	hasWebSubscription = (providerListWebSubscription.size() > 0 ? true : false);
    	hasWebPurchase = (providerListWebPurchase.size() > 0 ? true : false);
    	hasWebFree = (providerListWebFree.size() > 0 ? true : false);
    	hasWebTvEverywhere = (providerListWebTvEverywhere.size() > 0 ? true : false);
    	hasWeb = (hasWebSubscription || hasWebPurchase || hasWebFree || hasWebTvEverywhere ? true : false);
    	
    	hasAndroidSubscription = (providerListAndroidSubscription.size() > 0 ? true : false);
    	hasAndroidPurchase = (providerListAndroidPurchase.size() > 0 ? true : false);
    	hasAndroidFree = (providerListAndroidFree.size() > 0 ? true : false);
    	hasAndroidTvEverywhere = (providerListAndroidTvEverywhere.size() > 0 ? true : false);
    	hasAndroid = (hasAndroidSubscription || hasAndroidPurchase || hasAndroidFree || hasAndroidTvEverywhere ? true : false);
    	
    	hasIOSSubscription = (providerListIOSSubscription.size() > 0 ? true : false);
    	hasIOSPurchase = (providerListIOSPurchase.size() > 0 ? true : false);
    	hasIOSFree = (providerListIOSFree.size() > 0 ? true : false);
    	hasIOSTvEverywhere = (providerListIOSTvEverywhere.size() > 0 ? true : false);
    	hasIOS = (hasIOSSubscription || hasIOSPurchase || hasIOSFree || hasIOSTvEverywhere ? true : false);
    	
    	hasOther = (providerListOther.size() > 0 ? true : false);
    	
    	hasNone = (hasAndroid != true
    				&& hasIOS != true
    				&& hasWeb != true
    				&& hasOther !=  true ? true : false);
    	
    }
    
    
}
