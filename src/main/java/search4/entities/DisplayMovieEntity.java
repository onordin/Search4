package search4.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class DisplayMovieEntity implements Serializable{

	private static final long serialVersionUID = -8197962163887331570L;

	private String title;
    private String description;
    private Date date;
    private String posterUrl;
    private List<ServiceProviderLink> providerList;


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

    public List<ServiceProviderLink> getProviderList() {
        return providerList;
    }

    public void setProviderList(List<ServiceProviderLink> providerList) {
        this.providerList = providerList;
    }
}
