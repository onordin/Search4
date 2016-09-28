package search4.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class DisplayMovieEntity {

//	private static final long serialVersionUID = -8197962163887331570L;

	private String title;
    private String description;
    private Date date;
    private String posterUrl;
    //TODO right categories?
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
}
