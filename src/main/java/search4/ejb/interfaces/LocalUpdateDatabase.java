package search4.ejb.interfaces;

import javax.ejb.Local;

import search4.entities.MovieEntity;

@Local
public interface LocalUpdateDatabase {
	
	MovieEntity getMovieFromTMDB(Integer id) throws Exception;
	
	void updateDatabase() throws Exception;
	
	Integer getTMDBLimit(int start);
	
	Integer getLastTMDBIdFromDB() throws Exception;
}
