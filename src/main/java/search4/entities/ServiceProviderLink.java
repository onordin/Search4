package search4.entities;

import search4.entities.enums.ServiceProviderType;

public class ServiceProviderLink {

    private ServiceProviderType type;
    private String url;

    public ServiceProviderType getType() {
        return type;
    }

    public void setType(ServiceProviderType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
