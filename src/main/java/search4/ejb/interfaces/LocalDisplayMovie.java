package search4.ejb.interfaces;

import javax.ejb.Local;

import search4.entities.DisplayMovieEntity;

@Local
public interface LocalDisplayMovie {

	DisplayMovieEntity getDisplayMovie(Integer id) throws Exception;
}
