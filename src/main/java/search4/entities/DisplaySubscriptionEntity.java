package search4.entities;

import search4.helpers.ResourceLink;

import java.util.List;

public class DisplaySubscriptionEntity {

    private Integer subscribedMovieId;
    private String title;
    private Integer id;
	private List<ResourceLink> links;

    public Integer getSubscribedMovieId() {
        return subscribedMovieId;
    }

    public void setSubscribedMovieId(Integer subscribedMovieId) {
        this.subscribedMovieId = subscribedMovieId;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ResourceLink> getLinks() {
		return links;
	}

	public void setLinks(List<ResourceLink> links) {
		this.links = links;
	}
}
