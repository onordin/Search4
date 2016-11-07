package search4.resources.entities;

import search4.entities.MovieEntity;
import search4.helpers.ResourceLink;

public class MovieResourceEntity {

	
	private MovieEntity movieEntity;
	private ResourceLink resoureLink;
	
	public MovieEntity getMovieEntity() {
		return movieEntity;
	}
	public void setMovieEntity(MovieEntity movieEntity) {
		this.movieEntity = movieEntity;
	}
	public ResourceLink getResoureLink() {
		return resoureLink;
	}
	public void setResoureLink(ResourceLink resoureLink) {
		this.resoureLink = resoureLink;
	}
	
}
