package search4.entities;

import java.util.List;

import search4.helpers.ResourceLink;

public class DisplayProviderEntity {

	private Integer id;
	private Integer userId;
	private String provider;
	private List<ResourceLink> links;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public List<ResourceLink> getLinks() {
		return links;
	}
	public void setLinks(List<ResourceLink> links) {
		this.links = links;
	}
	
	
	
}
