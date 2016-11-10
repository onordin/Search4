package search4.ejb.interfaces;

import java.util.List;

import javax.ejb.Local;

import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;

@Local
public interface LocalDisplayMovie {

	void getSimpleDisplayMovieEntity(Integer id) throws Exception;

	DisplayMovieEntity getDisplayMovie(Integer id) throws Exception;

	DisplayMovieEntity createDisplayMovie(MovieEntity movieEntity) throws Exception;

	List<String> getMatchingProviders(List<String> requestedProviders, DisplayMovieEntity displayMovieEntity) throws Exception;

}
