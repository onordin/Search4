package search4.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import search4.entities.DisplayMovieEntity;

@Local
public interface LocalDisplayMovie {

	DisplayMovieEntity getDisplayMovie(Integer id) throws Exception;

	List<String> getMatchingProviders(List<String> requestedProviders, DisplayMovieEntity displayMovieEntity);

}
