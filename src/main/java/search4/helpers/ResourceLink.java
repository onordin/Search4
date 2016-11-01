package search4.helpers;

public class ResourceLink {

	private String rel;
	private String link;
	
	public ResourceLink() {
	
	}
	
	public ResourceLink(String rel, String link) {
		this.rel = rel;
		this.link = link;
	}

	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
}
